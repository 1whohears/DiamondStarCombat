package com.onewhohears.dscombat.data.parts.instance;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.onewhohears.dscombat.crafting.*;
import com.onewhohears.dscombat.data.parts.stats.TurretStats;
import com.onewhohears.dscombat.data.weapon.ExtraWeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.data.weapon.instance.WeaponInstance;
import com.onewhohears.dscombat.entity.parts.EntityPart;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.weapon.EntitySmokeGrenade;
import com.onewhohears.dscombat.init.ModSounds;
import com.onewhohears.dscombat.util.UtilSound;
import com.onewhohears.onewholibs.util.math.UtilAngles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TurretInstance<T extends TurretStats> extends SeatInstance<T> implements ReloadablePartInstance {
	
	/** Primary weapon id (legacy / continuity field) */
	@NotNull private String weapon = "";
	/** Additional weapon data with positions from slot JSON "extra_weapons" array */
	@NotNull private List<ExtraWeaponData> extraWeapons = new ArrayList<>();
	private int ammo = 0;
	/** All weapon instances: index 0 = primary, 1..n = extra */
	@NotNull private List<WeaponInstance<?>> weaponList = new ArrayList<>();
	private int weaponIndex = 0;
	@Nullable private WeaponInstance<?> data;
	/** Smoke grenades stored in this turret (if turret has smoke_launchers configured). */
	private int smokeGrenades = 0;
	/** Индекс текущего активного ствола для поочерёдной стрельбы (weaponOffsets). */
	private int currentOffsetIndex = 0;
	
	public TurretInstance(T stats) {
		super(stats);
	}
	
	@Override
	public void setFilled(String param) {
		super.setFilled(param);
		if (param.isEmpty()) {
			List<String> list = WeaponPresets.get().getCompatibleWeapons(getStatsId());
			if (!list.isEmpty()) param = list.get(0);
		}
		weapon = param;
		ammo = getStats().getMaxAmmo();
		smokeGrenades = getStats().getMaxSmokeGrenades();
		System.out.println("DEBUG: TurretInstance.setFilled - statsId: " + getStatsId() + ", maxSmokeGrenades: " + getStats().getMaxSmokeGrenades() + ", smokeGrenades set to: " + smokeGrenades);
	}

	@Override
	public void setParamNotFilled(String param) {
		super.setParamNotFilled(param);
		if (param.isEmpty()) {
			List<String> list = WeaponPresets.get().getCompatibleWeapons(getStatsId());
			if (!list.isEmpty()) param = list.get(0);
		}
		weapon = param;
		smokeGrenades = getStats().getMaxSmokeGrenades();
	}

	/**
	 * Set additional weapon data from slot JSON "extra_weapons" array.
	 * Called during vehicle preset loading.
	 */
	public void setExtraWeapons(List<ExtraWeaponData> extra) {
		this.extraWeapons = new ArrayList<>(extra);
	}
	
	/**
	 * Get extra weapon data list
	 */
	public List<ExtraWeaponData> getExtraWeapons() {
		return extraWeapons;
	}
	
	@Override
	public void readNBT(CompoundTag tag) {
		super.readNBT(tag);
		if (tag.contains("weapon")) weapon = tag.getString("weapon");
		else if (tag.contains("weaponId")) weapon = tag.getString("weaponId");
		if (!WeaponPresets.get().has(weapon)) weapon = "";
		ammo = tag.getInt("ammo");
		weaponIndex = tag.getInt("turretWeaponIndex");
		smokeGrenades = tag.getInt("smokeGrenades");
		currentOffsetIndex = tag.getInt("currentOffsetIndex");
		extraWeapons.clear();
		if (tag.contains("extraWeapons")) {
			ListTag list = tag.getList("extraWeapons", 10); // 10 = TAG_Compound
			for (int i = 0; i < list.size(); i++) {
				CompoundTag weaponTag = list.getCompound(i);
				String name = weaponTag.getString("name");
				String id = weaponTag.getString("weapon");
				if (WeaponPresets.get().has(id)) {
					double x = weaponTag.getDouble("x");
					double y = weaponTag.getDouble("y");
					double z = weaponTag.getDouble("z");
					List<String> linkedWeapons = new ArrayList<>();
					if (weaponTag.contains("linked_weapons")) {
						ListTag linkedList = weaponTag.getList("linked_weapons", 8); // 8 = TAG_String
						for (int j = 0; j < linkedList.size(); j++) {
							linkedWeapons.add(linkedList.getString(j));
						}
					}
					extraWeapons.add(new ExtraWeaponData(name, id, new Vec3(x, y, z), linkedWeapons));
				}
			}
		}
	}
	
	@Override
	public CompoundTag writeNBT() {
		CompoundTag tag = super.writeNBT();
		tag.putString("weapon", weapon);
		tag.putInt("ammo", ammo);
		tag.putInt("turretWeaponIndex", weaponIndex);
		tag.putInt("smokeGrenades", smokeGrenades);
		tag.putInt("currentOffsetIndex", currentOffsetIndex);
		if (!extraWeapons.isEmpty()) {
			ListTag list = new ListTag();
			for (ExtraWeaponData data : extraWeapons) {
				CompoundTag weaponTag = new CompoundTag();
				weaponTag.putString("name", data.name);
				weaponTag.putString("weapon", data.weaponId);
				weaponTag.putDouble("x", data.pos.x);
				weaponTag.putDouble("y", data.pos.y);
				weaponTag.putDouble("z", data.pos.z);
				if (!data.linkedWeapons.isEmpty()) {
					ListTag linkedList = new ListTag();
					for (String linked : data.linkedWeapons) {
						linkedList.add(StringTag.valueOf(linked));
					}
					weaponTag.put("linked_weapons", linkedList);
				}
				list.add(weaponTag);
			}
			tag.put("extraWeapons", list);
		}
		return tag;
	}
	
	@Override
	public void readBuffer(FriendlyByteBuf buffer) {
		super.readBuffer(buffer);
		weapon = buffer.readUtf();
		ammo = buffer.readInt();
		weaponIndex = buffer.readInt();
		smokeGrenades = buffer.readInt();
		currentOffsetIndex = buffer.readInt();
		int extraCount = buffer.readInt();
		extraWeapons.clear();
		for (int i = 0; i < extraCount; i++) {
			String name = buffer.readUtf();
			String id = buffer.readUtf();
			double x = buffer.readDouble();
			double y = buffer.readDouble();
			double z = buffer.readDouble();
			int linkedCount = buffer.readInt();
			List<String> linkedWeapons = new ArrayList<>();
			for (int j = 0; j < linkedCount; j++) {
				linkedWeapons.add(buffer.readUtf());
			}
			extraWeapons.add(new ExtraWeaponData(name, id, new Vec3(x, y, z), linkedWeapons));
		}
	}
	
	@Override
	public void writeBuffer(FriendlyByteBuf buffer) {
		super.writeBuffer(buffer);
		buffer.writeUtf(weapon);
		buffer.writeInt(ammo);
		buffer.writeInt(weaponIndex);
		buffer.writeInt(smokeGrenades);
		buffer.writeInt(currentOffsetIndex);
		buffer.writeInt(extraWeapons.size());
		for (ExtraWeaponData data : extraWeapons) {
			buffer.writeUtf(data.name);
			buffer.writeUtf(data.weaponId);
			buffer.writeDouble(data.pos.x);
			buffer.writeDouble(data.pos.y);
			buffer.writeDouble(data.pos.z);
			buffer.writeInt(data.linkedWeapons.size());
			for (String linked : data.linkedWeapons) {
				buffer.writeUtf(linked);
			}
		}
	}
	
	public String getWeaponId() {
		return weapon;
	}

	/** Returns all weapon ids: primary first, then extra. */
	public List<String> getAllWeaponIds() {
		List<String> all = new ArrayList<>();
		if (!weapon.isEmpty()) all.add(weapon);
		for (ExtraWeaponData data : extraWeapons) all.add(data.weaponId);
		return all;
	}

	public int getWeaponIndex() {
		return weaponIndex;
	}

	/** Cycle to next/previous weapon. input: +1 or -1. */
	public void selectNextWeapon(int input) {
		int count = weaponList.size();
		if (count <= 1) return;
		weaponIndex += input;
		if (weaponIndex >= count) weaponIndex = 0;
		else if (weaponIndex < 0) weaponIndex = count - 1;
		data = weaponList.get(weaponIndex);
		setDirty();
	}

	public void setWeaponIndex(int index) {
		int count = weaponList.size();
		if (count == 0) return;
		weaponIndex = Math.max(0, Math.min(index, count - 1));
		data = weaponList.isEmpty() ? null : weaponList.get(weaponIndex);
	}

	/** Returns the currently selected weapon instance. */
	@Nullable
	public WeaponInstance<?> getSelectedWeaponData() {
		return data;
	}

	public int getWeaponCount() {
		return weaponList.size();
	}

	/** Returns all weapon instances (primary + extra). */
	public List<WeaponInstance<?>> getWeaponList() {
		return weaponList;
	}

	@Override
	public void setup(EntityVehicle craft, String slotId, Vec3 pos) {
		super.setup(craft, slotId, pos);
		weaponList.clear();
		
		// Add primary weapon
		if (!weapon.isEmpty() && WeaponPresets.get().has(weapon)) {
			WeaponInstance<?> w = WeaponPresets.get().get(weapon).createWeaponInstance();
			if (w != null) {
				int wMax = w.getStats().getMaxAmmo();
				w.setMaxAmmo(wMax > 0 ? wMax : (int) getMaxAmmo());
				w.setCurrentAmmo(ammo);
				weaponList.add(w);
			}
		}
		
		// Add extra weapons as separate selectable weapons
		for (ExtraWeaponData extraData : extraWeapons) {
			String weaponId = extraData.getWeaponId();
			if (WeaponPresets.get().has(weaponId)) {
				WeaponInstance<?> w = WeaponPresets.get().get(weaponId).createWeaponInstance();
				if (w != null) {
					int wMax = w.getStats().getMaxAmmo();
					w.setMaxAmmo(wMax > 0 ? wMax : (int) getMaxAmmo());
					w.setCurrentAmmo(ammo);
					// Store extra weapon data reference for position
					w.setSlot(extraData.getName());
					weaponList.add(w);
				}
			}
		}
		
		// Fallback: if no weapons loaded, try compatible list
		if (weaponList.isEmpty()) {
			List<String> list = WeaponPresets.get().getCompatibleWeapons(getStatsId());
			if (!list.isEmpty()) {
				WeaponInstance<?> w = WeaponPresets.get().get(list.get(0)).createWeaponInstance();
				if (w != null) {
					int wMax = w.getStats().getMaxAmmo();
					w.setMaxAmmo(wMax > 0 ? wMax : (int) getMaxAmmo());
					w.setCurrentAmmo(ammo);
					weaponList.add(w);
				}
			}
		}
		weaponIndex = Math.max(0, Math.min(weaponIndex, weaponList.size() - 1));
		data = weaponList.isEmpty() ? null : weaponList.get(weaponIndex);
	}
	
	@Nullable
	public EntityTurret getTurret(String slotId) {
		EntityVehicle craft = getParent();
		if (craft == null) return null;
		for (EntityPart part : craft.getPartEntities()) 
			if (part.getPartType().is(getStats().getType()) && part.getSlotId().equals(slotId)
					&& part instanceof EntityTurret turret)
				return turret;
		return null;
	}

	@Override
	public float getCurrentAmmo() {
		return ammo;
	}

	@Override
	public float getMaxAmmo() {
		return getStats().getMaxAmmo();
	}

	/**
	 * this function is mostly used internally.
	 * see {@link #setWeaponAmmo(int)}
	 */
	@Override
	public void setCurrentAmmo(float ammo) {
		this.ammo = (int)ammo;
		for (WeaponInstance<?> w : weaponList) w.setCurrentAmmo((int) ammo);
		setDirty();
	}

	/**
	 * update the part's ammo and the turret entity's ammo.
	 * @param ammo
	 */
	public void setWeaponAmmo(int ammo) {
		if (data != null) {
			data.setCurrentAmmo(ammo);
			setCurrentAmmo(data.getCurrentAmmo());
		}
	}

	/**
	 * update the part's ammo and the turret entity's ammo.
	 * @param ammo
	 */
	public int addWeaponAmmo(int ammo) {
		if (data == null) return 0;
		int r = data.addAmmo(ammo);
		setCurrentAmmo(data.getCurrentAmmo());
		return r;
	}

	@Override
	public void setMaxAmmo(float max) {
	}

	@Override
	public boolean isCompatibleWithAmmoContinuity(String continuity) {
		return getStats().isWeaponCompatible(continuity);
	}

	@Override
	public boolean updateContinuityIfEmpty() {
		return true;
	}

	@Override
	public void setContinuity(String continuity) {
		if (continuity == null) continuity = "";
		if (continuity.equals(weapon)) return;
		this.weapon = continuity;
		setDirty();
		if (!isSetup()) return;
		// Rebuild weapon list keeping extra weapons, replacing primary
		weaponList.clear();
		for (String id : getAllWeaponIds()) {
			if (WeaponPresets.get().has(id)) {
				WeaponInstance<?> w = WeaponPresets.get().get(id).createWeaponInstance();
				if (w != null) {
					w.setMaxAmmo((int) getMaxAmmo());
					w.setCurrentAmmo(ammo);
					weaponList.add(w);
				}
			}
		}
		weaponIndex = Math.max(0, Math.min(weaponIndex, weaponList.size() - 1));
		data = weaponList.isEmpty() ? null : weaponList.get(weaponIndex);
	}

	@Override
	public String getContinuity() {
		return getWeaponId();
	}
	
	@Override
	public boolean isContinuityEmpty() {
		return getContinuity() == null || getContinuity().isEmpty() || getCurrentAmmo() == 0;
	}

	private static final PartItemLoadRecipe<?> LOAD_RECIPE = new TurretLoadRecipe(
			new ResourceLocation("dscombat:turret_load_recipe"));
	private static final PartItemUnloadRecipe<?> UNLOAD_RECIPE = new TurretUnloadRecipe(
			new ResourceLocation("dscombat:turret_unload_recipe"));

	@Override
	public PartItemLoadRecipe<?> getLoadRecipe() {
		return LOAD_RECIPE;
	}

	@Override
	public @Nullable PartItemUnloadRecipe<?> getUnloadRecipe() {
		return UNLOAD_RECIPE;
	}

	@Nullable
	public WeaponInstance<?> getWeaponData() {
		return data;
	}

	// --- Alternating barrel (weaponOffsets) ---

	/** Возвращает индекс текущего активного ствола. */
	public int getCurrentOffsetIndex() { return currentOffsetIndex; }

	/**
	 * Переключает на следующий ствол и возвращает индекс, который нужно использовать
	 * для ТЕКУЩЕГО выстрела (до переключения).
	 */
	public int consumeAndAdvanceOffsetIndex() {
		int count = getStats().getWeaponOffsets().length;
		if (count <= 1) return 0;
		int used = currentOffsetIndex;
		currentOffsetIndex = (currentOffsetIndex + 1) % count;
		setDirty();
		return used;
	}

	// --- Smoke grenade methods ---

	public int getSmokeGrenades() { return smokeGrenades; }

	public int getMaxSmokeGrenades() { return getStats().getMaxSmokeGrenades(); }

	public void addSmokeGrenades(int n) {
		smokeGrenades = Math.max(0, Math.min(smokeGrenades + n, getStats().getMaxSmokeGrenades()));
		setDirty();
	}

	/**
	 * Fires all smoke grenade launchers configured on this turret.
	 * Called server-side. Returns true if at least one grenade was fired.
	 */
	public boolean fireSmokeGrenades(Entity owner, EntityVehicle vehicle, boolean consume) {
		System.out.println("DEBUG: fireSmokeGrenades called - hasSmokeGrenades: " + getStats().hasSmokeGrenades() + ", smokeGrenades: " + smokeGrenades);
		if (!getStats().hasSmokeGrenades()) return false;
		if (smokeGrenades <= 0) return false;

		Level level = vehicle.getWorld();
		TurretStats stats = getStats();
		System.out.println("DEBUG: Firing smoke grenades, launcher count: " + stats.getSmokeLaunchers().length);
		System.out.println("DEBUG: TurretInstance relPos: " + getRelPos() + ", vehicle pos: " + vehicle.position());

		// Get turret entity to get its rotation
		EntityTurret turretEntity = null;
		for (com.onewhohears.dscombat.data.parts.PartSlot slot : vehicle.partsManager.getSlots()) {
			if (slot.getPartData() == this) {
				turretEntity = getTurret(slot.getSlotId());
				break;
			}
		}
		float turretYaw = turretEntity != null ? turretEntity.getRelRotY() : 0f;
		System.out.println("DEBUG: Turret relative yaw: " + turretYaw);

		for (TurretStats.SmokeGrenadeLauncherDef launcher : stats.getSmokeLaunchers()) {
			// Rotate launcher position by turret yaw
			Vec3 launcherPosRotated = launcher.pos;
			if (turretYaw != 0) {
				// Rotate launcher position around Y axis by turret yaw
				float radians = turretYaw * (float)Math.PI / 180f;
				float cos = (float)Math.cos(radians);
				float sin = (float)Math.sin(radians);
				double newX = launcher.pos.x * cos - launcher.pos.z * sin;
				double newZ = launcher.pos.x * sin + launcher.pos.z * cos;
				launcherPosRotated = new Vec3(newX, launcher.pos.y, newZ);
			}
			
			Vec3 launcherOffset = getRelPos().add(launcherPosRotated);
			Vec3 worldPos = vehicle.position().add(UtilAngles.rotateVector(launcherOffset, vehicle.getQ()));
			
			// Ensure grenade spawns above ground level
			double groundY = vehicle.position().y + 1.0; // At least 1 block above vehicle center
			if (worldPos.y < groundY) {
				worldPos = new Vec3(worldPos.x, groundY, worldPos.z);
				System.out.println("DEBUG: Adjusted grenade spawn Y from " + worldPos.y + " to " + groundY);
			}
			
			// Add turret rotation to launcher yaw
			float yaw   = vehicle.getYRot() + turretYaw + launcher.yaw;
			float pitch = -launcher.pitch;
			Vec3 dir = UtilAngles.rotationToVector(yaw, pitch);

			System.out.println("DEBUG: Creating smoke grenade at pos: " + worldPos + ", yaw: " + yaw + ", pitch: " + pitch + ", dir: " + dir);
			EntitySmokeGrenade grenade = new EntitySmokeGrenade(level);
			grenade.setPos(worldPos);
			grenade.setXRot(pitch);
			grenade.setYRot(yaw);
			// Reduced velocity from 1.5 to 0.8 for shorter range
			grenade.setDeltaMovement(vehicle.getDeltaMovement().add(dir.scale(0.8)));
			grenade.setPreset("smoke_grenade");
			grenade.setOwner(owner);
			boolean added = level.addFreshEntity(grenade);
			System.out.println("DEBUG: Smoke grenade added to world: " + added + ", grenade ID: " + grenade.getId() + ", isRemoved: " + grenade.isRemoved() + ", stats: " + (grenade.getStats() != null ? "loaded" : "NULL"));
		}

		// Play shoot sound from smoke_grenade.json shootSoundKey
		SoundEvent shootSound = ModSounds.SMOKE_GRENADE_SHOOT;
		// Try to get shootSoundKey from the smoke_grenade weapon stats
		com.onewhohears.dscombat.data.weapon.stats.WeaponStats smokeStats = 
			com.onewhohears.dscombat.data.weapon.WeaponPresets.get().get("smoke_grenade");
		if (smokeStats != null) {
			shootSound = smokeStats.getShootSound(level.registryAccess());
		}
		UtilSound.sendDelayedSound((ServerLevel) level, shootSound, vehicle.position(), 64, 1f, 1f);

		if (consume) addSmokeGrenades(-1);
		return true;
	}

	@Override
	public void onReceiveClientSync() {
		super.onReceiveClientSync();
		setContinuity(getWeaponId());
		weaponIndex = Math.max(0, Math.min(weaponIndex, weaponList.size() - 1));
		data = weaponList.isEmpty() ? null : weaponList.get(weaponIndex);
		for (WeaponInstance<?> w : weaponList) w.setCurrentAmmo(ammo);
	}
}
