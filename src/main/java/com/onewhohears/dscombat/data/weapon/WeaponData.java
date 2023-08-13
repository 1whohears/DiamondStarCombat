package com.onewhohears.dscombat.data.weapon;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ToClientWeaponAmmo;
import com.onewhohears.dscombat.crafting.DSCIngredient;
import com.onewhohears.dscombat.data.JsonPreset;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.init.ModEntities;
import com.onewhohears.dscombat.init.ModSounds;
import com.onewhohears.dscombat.util.UtilParse;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class WeaponData extends JsonPreset {
	
	private final List<DSCIngredient> ingredients;
	private final int craftNum;
	private final int maxAge;
	private final int maxAmmo;
	private final int fireRate;
	private final boolean canShootOnGround;
	private final String entityTypeKey;
	private final String shootSoundKey;
	private final String rackTypeKey;
	private final String compatibleWeaponPart;
	private final String itemKey;
	
	private EntityType<?> entityType;
	private SoundEvent shootSound;
	private EntityType<?> rackType;
	private int currentAmmo;
	private int recoilTime;
	private Vec3 pos = Vec3.ZERO;
	private String failedLaunchReason;
	private String slotId = "";
	private boolean overrideGroundCheck = false;
	
	public WeaponData(ResourceLocation key, JsonObject json) {
		super(key, json);
		this.ingredients = DSCIngredient.getIngredients(json);
		this.craftNum = json.get("craftNum").getAsInt();
		this.maxAge = json.get("maxAge").getAsInt();
		this.maxAmmo = json.get("maxAmmo").getAsInt();
		this.fireRate = json.get("fireRate").getAsInt();
		this.canShootOnGround = json.get("canShootOnGround").getAsBoolean();
		this.entityTypeKey = json.get("entityTypeKey").getAsString();
		this.shootSoundKey = json.get("shootSoundKey").getAsString();
		this.rackTypeKey = json.get("rackTypeKey").getAsString();
		this.compatibleWeaponPart = json.get("compatibleWeaponPart").getAsString();
		this.itemKey = json.get("itemKey").getAsString();
	}
	
	public void readNBT(CompoundTag tag) {
		currentAmmo = tag.getInt("currentAmmo");
		slotId = tag.getString("slotId");
		pos = UtilParse.readVec3(tag, "pos");
	}
	
	public CompoundTag writeNbt() {
		CompoundTag tag = new CompoundTag();
		tag.putString("weaponId", getId());
		tag.putInt("currentAmmo", currentAmmo);
		UtilParse.writeVec3(tag, pos, "pos");
		tag.putString("slotId", slotId);
		return tag;
	}
	
	public void readBuffer(FriendlyByteBuf buffer) {
		// weaponId String is read in DataSerializers
		currentAmmo = buffer.readInt();
		slotId = buffer.readUtf();
		pos = DataSerializers.VEC3.read(buffer);
	}
	
	public void writeBuffer(FriendlyByteBuf buffer) {
		buffer.writeUtf(getId());
		buffer.writeInt(currentAmmo);
		buffer.writeUtf(slotId);
		DataSerializers.VEC3.write(buffer, pos);
	}
	
	public List<DSCIngredient> getIngredients() {
		return ingredients;
	}
	
	public int getCraftNum() {
		return craftNum;
	}
	
	public abstract WeaponType getType();
	public abstract EntityWeapon getEntity(Level level, Entity owner);
	
	public EntityWeapon getShootEntity(Level level, Entity owner, Vec3 pos, Vec3 direction, @Nullable EntityAircraft vehicle) {
		if (!checkRecoil()) {
			setLaunchFail(null);
			return null;
		}
		if (!checkAmmo(1, owner)) {
			setLaunchFail("error.dscombat.no_ammo");
			return null;
		}
		EntityWeapon w = getEntity(level, owner);
		w.setPos(pos);
		setDirection(w, direction);
		if (vehicle != null) {
			if (!overrideGroundCheck && !canShootOnGround() && vehicle.isOnGround()) {
				setLaunchFail("error.dscombat.cant_shoot_on_ground");
				return null;
			}
		}
		return w;
	}
	
	public void setDirection(EntityWeapon weapon, Vec3 direction) {
		float pitch = UtilAngles.getPitch(direction);
		float yaw = UtilAngles.getYaw(direction);
		weapon.setXRot(pitch);
		weapon.setYRot(yaw);
	}
	
	public boolean shootFromVehicle(Level level, Entity owner, Vec3 direction, EntityAircraft vehicle, boolean consume) {
		overrideGroundCheck = false;
		EntityWeapon w = getShootEntity(level, owner, 
				vehicle.position().add(UtilAngles.rotateVector(getLaunchPos(), vehicle.getQ())), 
				direction, vehicle);
		if (w == null) return false;
		level.addFreshEntity(w);
		level.playSound(null, w.blockPosition(), 
				getShootSound(), SoundSource.PLAYERS, 
				1f, 1f);
		setLaunchSuccess(1, owner, consume);
		updateClientAmmo(vehicle);
		vehicle.lastShootTime = vehicle.tickCount;
		return true;
	}
	
	public boolean shootFromTurret(Level level, Entity owner, Vec3 direction, Vec3 pos, @Nullable EntityAircraft vehicle, boolean consume) {
		overrideGroundCheck = true;
		EntityWeapon w = getShootEntity(level, owner, pos, direction, vehicle);
		if (w == null) return false;
		level.addFreshEntity(w);
		level.playSound(null, w.blockPosition(), 
				getShootSound(), SoundSource.PLAYERS, 
				1f, 1f);
		setLaunchSuccess(1, owner, consume);
		if (vehicle != null) vehicle.lastShootTime = vehicle.tickCount;
		return true;
	}
	
	public void updateClientAmmo(EntityAircraft vehicle) {
		if (vehicle == null) return;
		if (vehicle.level.isClientSide) return;
		PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> vehicle), 
				new ToClientWeaponAmmo(vehicle.getId(), getId(), slotId, getCurrentAmmo()));
	}
	
	public void tick(@Nullable EntityAircraft parent, boolean isSelected) {
		if (recoilTime > 1) --recoilTime;
	}
	
	/**
	 * called inside the shoot function
	 * @param ammoNum
	 * @return if this weapon can shoot
	 */
	public boolean checkAmmo(int ammoNum, Entity shooter) {
		if (shooter instanceof ServerPlayer p) {
			if (p.isCreative()) return true;
		}
		return currentAmmo >= ammoNum;
	}
	
	public boolean checkRecoil() {
		return recoilTime <= 1;
	}
	
	public Vec3 getLaunchPos() {
		return pos;
	}

	public void setLaunchPos(Vec3 pos) {
		this.pos = pos;
	}

	public int getMaxAge() {
		return maxAge;
	}

	public int getCurrentAmmo() {
		return currentAmmo;
	}

	public void setCurrentAmmo(int currentAmmo) {
		if (currentAmmo < 0) currentAmmo = 0;
		if (currentAmmo > maxAmmo) currentAmmo = maxAmmo;
		this.currentAmmo = currentAmmo;
	}
	
	/**
	 * @param num
	 * @return overflow
	 */
	public int addAmmo(int num) {
		int total = currentAmmo+num;
		int r = 0;
		if (total > maxAmmo) {
			r = total - maxAmmo;
			total = maxAmmo;
		} else if (total < 0) {
			r = total;
			total = 0;
		}
		setCurrentAmmo(total);
		return r;
	}
	
	public int getMaxAmmo() {
		return maxAmmo;
	}

	public int getFireRate() {
		return fireRate;
	}
	
	public boolean canShootOnGround() {
		return canShootOnGround;
	}

	public boolean isFailedLaunch() {
		return failedLaunchReason != null;
	}
	
	@Nullable
	public String getFailedLaunchReason() {
		return failedLaunchReason;
	}
	
	public void setLaunchSuccess(int ammoNum, Entity shooter, boolean consume) {
		failedLaunchReason = null;
		if (consume) addAmmo(-ammoNum);
		recoilTime = getFireRate();
	}
	
	public void setLaunchFail(String failedLaunchReason) {
		this.failedLaunchReason = failedLaunchReason;
	}
	
	@Override
	public <T extends JsonPreset> int compare(T other) {
		WeaponData wd = (WeaponData) other;
		if (this.getType() != wd.getType()) 
			return this.getType().ordinal() - wd.getType().ordinal();
		return super.compare(other);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof WeaponData w) return w.getId().equals(getId()) && w.getSlotId().equals(slotId);
		return false;
	}
	
	@Override
	public String toString() {
		return "["+getId()+":"+this.getType().toString()+"]";
	}
	
	public String getSlotId() {
		return slotId;
	}
	
	public boolean isInternal() {
		return slotId == "";
	}
	
	public void setSlot(String slotId) {
		this.slotId = slotId;
	}
	
	public void setInternal() {
		this.slotId = "";
	}
	
	public boolean idMatch(String id, String slotId) {
		if (slotId == null) return false;
		if (id == null) return false;
		return getId().equals(id) && getSlotId().equals(slotId);
	}
	
	public EntityType<?> getEntityType() {
		if (entityType == null) {
			try { entityType = ForgeRegistries.ENTITY_TYPES
					.getDelegate(new ResourceLocation(entityTypeKey)).get().get(); }
			catch(NoSuchElementException e) { entityType = ModEntities.BULLET.get(); }
		}
		return entityType;
	}
	
	public SoundEvent getShootSound() {
		if (shootSound == null) {
			try { shootSound = ForgeRegistries.SOUND_EVENTS
					.getDelegate(new ResourceLocation(shootSoundKey)).get().get(); }
			catch(NoSuchElementException e) { shootSound = ModSounds.BULLET_SHOOT_1.get(); }
		}
		return shootSound;
	}
	
	public EntityType<?> getRackEntityType() {
		if (rackType == null) {
			try { rackType = ForgeRegistries.ENTITY_TYPES
					.getDelegate(new ResourceLocation(rackTypeKey)).get().get(); }
			catch(NoSuchElementException e) { rackType = ModEntities.XM12.get(); }
		}
		return rackType;
	}
	
	private Item item;
	private ItemStack stack;
	
	private Item getItem() {
		if (item == null) {
			try {
				item = ForgeRegistries.ITEMS.getDelegate(
					new ResourceLocation(itemKey)).get().get();
			} catch(NoSuchElementException e) { item = Items.AIR; }
		}
		return item;
	}
	
	public ItemStack getDisplayStack() {
		if (stack == null) {
			stack = new ItemStack(getItem());
			stack.setCount(craftNum);
			CompoundTag tag = new CompoundTag();
			tag.putString("weapon", getId());
			stack.setTag(tag);
		}
		return stack;
	}
	
	public ItemStack getNewItem() {
		return getDisplayStack().copy();
	}
	
	public String getItemKey() {
		return itemKey;
	}
	
	public String getCompatibleWeaponPart() {
		return compatibleWeaponPart;
	}
	
	public abstract String getWeaponTypeCode();
	
	public void addToolTips(List<Component> tips) {
		tips.add(Component.literal("Fire Rate: ").append(getFireRate()+"").setStyle(Style.EMPTY.withColor(0xAAAAAA)));
		tips.add(Component.literal("Max Age: ").append(getMaxAge()+"").setStyle(Style.EMPTY.withColor(0xAAAAAA)));
		if (!canShootOnGround) tips.add(Component.literal("Must Fly").setStyle(Style.EMPTY.withColor(0xAAAAAA)));
	}
	
	public List<ComponentColor> getInfoComponents() {
		List<ComponentColor> list = new ArrayList<>();
		list.add(new ComponentColor(getDisplayNameComponent(), 0x000000));
		list.add(new ComponentColor(Component.literal(getType().toString()), 0x0000aa));
		list.add(new ComponentColor(Component.literal("Max Ammo: ").append(getMaxAmmo()+""), 0x040404));
		list.add(new ComponentColor(Component.literal("Fire Rate: ").append(getFireRate()+""), 0x040404));
		list.add(new ComponentColor(Component.literal("Max Age: ").append(getMaxAge()+""), 0x040404));
		return list;
	}
	
	public static class ComponentColor {
		public final Component component;
		public final int color;
		public ComponentColor(Component component, int color) {
			this.component = component;
			this.color = color;
		}
	}
	
	public static enum WeaponType {
		BULLET,
		BOMB,
		POS_MISSILE,
		TRACK_MISSILE,
		IR_MISSILE,
		ANTIRADAR_MISSILE,
		TORPEDO,
		BUNKER_BUSTER;
		
		@Nullable
		public static WeaponType getById(String id) {
			for (int i = 0; i < values().length; ++i) {
				if (values()[i].getId().equals(id)) 
					return values()[i];
			}
			return null;
		}
		
		private final String id;
		
		private WeaponType() {
			this.id = name().toLowerCase();
		}
		
		public String getId() {
			return id;
		}
		
		@Override
		public String toString() {
			return getId();
		}
	}
	
}
