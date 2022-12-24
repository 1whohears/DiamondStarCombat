package com.onewhohears.dscombat.data.weapon;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ToClientWeaponAmmo;
import com.onewhohears.dscombat.crafting.DSCIngredient;
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

public abstract class WeaponData {
	
	public final List<DSCIngredient> ingredients;
	public final int craftNum;
	
	private final String entityTypeKey;
	private final String shootSoundKey;
	private final String rackTypeKey;
	
	private EntityType<?> entityType;
	private SoundEvent shootSound;
	private EntityType<?> rackType;
	private String id;
	private Vec3 pos = Vec3.ZERO;
	private int maxAge;
	private int currentAmmo;
	private int maxAmmo;
	private int fireRate;
	private int recoilTime;
	private String failedLaunchReason;
	private boolean canShootOnGround;
	private String slotId = "";
	
	public static enum WeaponType {
		BULLET,
		BOMB,
		POS_MISSILE,
		TRACK_MISSILE,
		IR_MISSILE
	}
	
	/*protected WeaponData(RegistryObject<EntityType<?>> entityType, RegistryObject<SoundEvent> shootSound, List<Ingredient> ingredients,
			String id, Vec3 pos, int maxAge, int maxAmmo, int fireRate, boolean canShootOnGround) {
		this.id = id;
		this.pos = pos;
		this.maxAge = maxAge;
		this.maxAmmo = maxAmmo;
		this.fireRate = fireRate;
		this.canShootOnGround = canShootOnGround;
		this.entityType = entityType;
		this.shootSound = shootSound;
		this.ingredients = ingredients;
	}*/
	
	public WeaponData(CompoundTag tag) {
		id = tag.getString("id");
		pos = UtilParse.readVec3(tag, "pos");
		maxAge = tag.getInt("maxAge");
		currentAmmo = tag.getInt("currentAmmo");
		maxAmmo = tag.getInt("maxAmmo");
		fireRate = tag.getInt("fireRate");
		canShootOnGround = tag.getBoolean("canShootOnGround");
		slotId = tag.getString("slotId");
		entityTypeKey = tag.getString("entityType");
		shootSoundKey = tag.getString("shootSound");
		rackTypeKey = tag.getString("rackType");
		ingredients = DSCIngredient.getIngredients(tag);
		craftNum = tag.getInt("craftNum");
	}
	
	public CompoundTag write() {
		CompoundTag tag = new CompoundTag();
		tag.putInt("type", this.getType().ordinal());
		tag.putString("id", getId());
		UtilParse.writeVec3(tag, pos, "pos");
		tag.putInt("maxAge", maxAge);
		tag.putInt("currentAmmo", currentAmmo);
		tag.putInt("maxAmmo", maxAmmo);
		tag.putInt("fireRate", fireRate);
		tag.putBoolean("canShootOnGround", canShootOnGround);
		tag.putString("slotId", slotId);
		tag.putString("entityType", entityTypeKey);
		tag.putString("shootSound", shootSoundKey);
		tag.putString("rackType", rackTypeKey);
		DSCIngredient.writeIngredients(ingredients, tag);
		tag.putInt("craftNum", craftNum);
		return tag;
	}
	
	public WeaponData(FriendlyByteBuf buffer) {
		// type int is read in DataSerializers
		//System.out.println("WEAPON DATA BUFFER");
		entityTypeKey = buffer.readUtf();
		//System.out.println("entityTypeKey = "+entityTypeKey);
		shootSoundKey = buffer.readUtf();
		//System.out.println("shootSoundKey = "+shootSoundKey);
		rackTypeKey = buffer.readUtf();
		//
		craftNum = buffer.readInt();
		//System.out.println("craftNum = "+craftNum);
		ingredients = DSCIngredient.getIngredients(buffer);
		//System.out.println("ingredients = "+ingredients);
		id = buffer.readUtf();
		//System.out.println("id = "+id);
		pos = DataSerializers.VEC3.read(buffer);
		//System.out.println("pos = "+pos);
		maxAge = buffer.readInt();
		//System.out.println("maxAge = "+maxAge);
		currentAmmo = buffer.readInt();
		//System.out.println("currentAmmo = "+currentAmmo);
		maxAmmo = buffer.readInt();
		//System.out.println("maxAmmo = "+maxAmmo);
		fireRate = buffer.readInt();
		//System.out.println("fireRate = "+fireRate);
		canShootOnGround = buffer.readBoolean();
		//System.out.println("canShootOnGround = "+canShootOnGround);
		slotId = buffer.readUtf();
		//System.out.println("slotId = "+slotId);
	}
	
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(getType().ordinal());
		buffer.writeUtf(entityTypeKey);
		buffer.writeUtf(shootSoundKey);
		buffer.writeUtf(rackTypeKey);
		buffer.writeInt(craftNum);
		DSCIngredient.writeIngredients(ingredients, buffer);
		buffer.writeUtf(id);
		DataSerializers.VEC3.write(buffer, pos);
		buffer.writeInt(maxAge);
		buffer.writeInt(currentAmmo);
		buffer.writeInt(maxAmmo);
		buffer.writeInt(fireRate);
		buffer.writeBoolean(canShootOnGround);
		buffer.writeUtf(slotId);
	}
	
	public abstract WeaponType getType();
	public abstract EntityWeapon getEntity(Level level, Entity owner);
	
	public EntityWeapon getShootEntity(Level level, Entity owner, Vec3 pos, Vec3 direction) {
		if (!this.checkRecoil()) {
			this.setLaunchFail(null);
			return null;
		}
		if (!this.checkAmmo(1, owner)) {
			this.setLaunchFail("dscombat.no_ammo");
			return null;
		}
		EntityWeapon w = getEntity(level, owner);
		w.setPos(pos);
		this.setDirection(w, direction);
		return w;
	}
	
	public EntityWeapon getShootEntity(Level level, Entity owner, Vec3 direction, EntityAircraft vehicle) {
		EntityWeapon w = this.getShootEntity(level, owner, vehicle.position(), direction);
		if (w == null) return null;
		if (!this.canShootOnGround() && vehicle.isOnGround()) {
			this.setLaunchFail("dscombat.cant_shoot_on_ground");
			return null;
		}
		w.setPos(vehicle.position().add(UtilAngles.rotateVector(this.getLaunchPos(), vehicle.getQ())));
		return w;
	}
	
	public void setDirection(EntityWeapon weapon, Vec3 direction) {
		float pitch = UtilAngles.getPitch(direction);
		float yaw = UtilAngles.getYaw(direction);
		weapon.setXRot(pitch);
		weapon.setYRot(yaw);
	}
	
	public boolean shoot(Level level, Entity owner, Vec3 direction, @Nullable Vec3 pos, @Nullable EntityAircraft vehicle) {
		EntityWeapon w;
		if (vehicle == null && pos != null) w = getShootEntity(level, owner, pos, direction);
		else if (vehicle != null && pos == null) w = getShootEntity(level, owner, direction, vehicle);
		else return false;
		if (w == null) return false;
		level.addFreshEntity(w);
		level.playSound(null, w.blockPosition(), 
				getShootSound(), SoundSource.PLAYERS, 
				1f, 1f);
		setLaunchSuccess(1, owner);
		updateClientAmmo(vehicle);
		return true;
	}
	
	public void updateClientAmmo(EntityAircraft vehicle) {
		if (vehicle == null) return;
		PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> vehicle), 
				new ToClientWeaponAmmo(vehicle.getId(), getId(), slotId, getCurrentAmmo()));
	}
	
	protected void tick() {
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
	
	public String getId() {
		return id;
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
	
	public void addAmmo(int num) {
		this.setCurrentAmmo(currentAmmo+num);
	}
	
	public int getMaxAmmo() {
		return maxAmmo;
	}
	
	public void setMaxAmmo(int max) {
		maxAmmo = max;
	}

	public int getFireRate() {
		return fireRate;
	}

	public void setFireRate(int fireRate) {
		this.fireRate = fireRate;
	}
	
	public boolean canShootOnGround() {
		return canShootOnGround;
	}

	public void setCanShootOnGround(boolean canShootOnGround) {
		this.canShootOnGround = canShootOnGround;
	}

	public boolean isFailedLaunch() {
		return failedLaunchReason != null;
	}
	
	@Nullable
	public String getFailedLaunchReason() {
		return failedLaunchReason;
	}
	
	public void setLaunchSuccess(int ammoNum, Entity shooter) {
		failedLaunchReason = null;
		if (shooter instanceof ServerPlayer p) {
			if (p.isCreative()) ammoNum = 0;
		}
		this.addAmmo(-ammoNum);
		recoilTime = this.getFireRate();
	}
	
	public void setLaunchFail(String failedLaunchReason) {
		this.failedLaunchReason = failedLaunchReason;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof WeaponData w) return w.getId().equals(id) && w.getSlotId().equals(slotId);
		return false;
	}
	
	public abstract WeaponData copy();
	
	@Override
	public String toString() {
		return "["+id+":"+this.getType().toString()+"]";
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
		return this.id.equals(id) && this.slotId.equals(slotId);
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
	
	public Item getItem() {
		if (item == null) {
			try {
				item = ForgeRegistries.ITEMS.getDelegate(
					new ResourceLocation(DSCombatMod.MODID, id))
						.get().get();
			} catch(NoSuchElementException e) {
				item = Items.AIR;
			}
		}
		return item;
	}
	
	public ItemStack getDisplayStack() {
		if (stack == null) {
			stack = new ItemStack(getItem());
			stack.setCount(craftNum);
		}
		return stack;
	}
	
	public List<ComponentColor> getInfoComponents() {
		List<ComponentColor> list = new ArrayList<>();
		list.add(new ComponentColor(Component.translatable("item.dscombat."+getId()), 0x000000));
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
	
}
