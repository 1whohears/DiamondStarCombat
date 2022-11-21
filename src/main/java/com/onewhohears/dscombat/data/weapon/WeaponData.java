package com.onewhohears.dscombat.data.weapon;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ClientBoundWeaponAmmoPacket;
import com.onewhohears.dscombat.crafting.DSCIngredient;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.entity.weapon.EntityAbstractWeapon;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.init.ModEntities;
import com.onewhohears.dscombat.init.ModSounds;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public abstract class WeaponData {
	
	public final List<DSCIngredient> ingredients;
	public final int craftNum;
	protected final RegistryObject<EntityType<?>> entityType;
	protected final RegistryObject<SoundEvent> shootSound;
	
	private String id;
	private Vec3 pos;
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
		ROCKET,
		BOMB
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
		String preset = tag.getString("preset");
		if (!preset.isEmpty()) {
			CompoundTag data = WeaponPresets.getNbtById(preset);
			if (data != null) tag.merge(data);
		}
		id = tag.getString("id");
		pos = UtilParse.readVec3(tag, "pos");
		maxAge = tag.getInt("maxAge");
		currentAmmo = tag.getInt("currentAmmo");
		maxAmmo = tag.getInt("maxAmmo");
		fireRate = tag.getInt("fireRate");
		canShootOnGround = tag.getBoolean("canShootOnGround");
		slotId = tag.getString("slotId");
		
		RegistryObject<EntityType<?>> type = ModEntities.getObjectByKey(tag.getString("entityType"));
		if (type == null) type = ModEntities.BULLET;
		entityType = type;
		RegistryObject<SoundEvent> sound = ModSounds.getObjectByKey(tag.getString("shootSound"));
		if (sound == null) sound = ModSounds.BULLET_SHOOT_1;
		shootSound = sound;
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
		tag.putString("entityType", entityType.getId().toString());
		tag.putString("shootSound", shootSound.getId().toString());
		DSCIngredient.writeIngredients(ingredients, tag);
		tag.putInt("craftNum", craftNum);
		return tag;
	}
	
	public WeaponData(FriendlyByteBuf buffer) {
		// type int is read in DataSerializers
		entityType = ModEntities.getObjectByKey(buffer.readUtf());
		shootSound = ModSounds.getObjectByKey(buffer.readUtf());
		ingredients = DSCIngredient.getIngredients(buffer);
		craftNum = buffer.readInt();
		id = buffer.readUtf();
		pos = DataSerializers.VEC3.read(buffer);
		maxAge = buffer.readInt();
		currentAmmo = buffer.readInt();
		maxAmmo = buffer.readInt();
		fireRate = buffer.readInt();
		canShootOnGround = buffer.readBoolean();
		slotId = buffer.readUtf();
	}
	
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(getType().ordinal());
		buffer.writeUtf(entityType.getId().toString());
		buffer.writeUtf(shootSound.getId().toString());
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
	
	public abstract EntityAbstractWeapon shoot(Level level, EntityAbstractAircraft vehicle, Entity owner, Vec3 direction, Quaternion vehicleQ);
	
	public void updateClientAmmo(EntityAbstractAircraft vehicle) {
		PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> vehicle), 
				new ClientBoundWeaponAmmoPacket(vehicle.getId(), this.getId(), this.slotId, this.getCurrentAmmo()));
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
		// TODO make this translatable
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
		return entityType.get();
	}
	
	public SoundEvent getShootSound() {
		return shootSound.get();
	}
	
	private Item item;
	private ItemStack stack;
	
	public Item getItem() {
		if (item == null) {
			item = ForgeRegistries.ITEMS.getDelegate(
					new ResourceLocation(DSCombatMod.MODID, id))
						.get().get();
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
		list.add(new ComponentColor(Component.translatable("item.dscombat."+getId()), 0x040404));
		list.add(new ComponentColor(Component.literal(getType().toString()), 0x0000aa));
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
