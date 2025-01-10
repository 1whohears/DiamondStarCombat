package com.onewhohears.dscombat.data.parts.instance;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.onewhohears.dscombat.crafting.*;
import com.onewhohears.dscombat.data.parts.stats.TurretStats;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.data.weapon.instance.WeaponInstance;
import com.onewhohears.dscombat.entity.parts.EntityPart;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class TurretInstance<T extends TurretStats> extends SeatInstance<T> implements ReloadablePartInstance {
	
	@Nonnull private String weapon = "";
	private int ammo = 0;
	@Nullable private WeaponInstance<?> data;
	
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
	}

	@Override
	public void setParamNotFilled(String param) {
		super.setParamNotFilled(param);
		if (param.isEmpty()) {
			List<String> list = WeaponPresets.get().getCompatibleWeapons(getStatsId());
			if (!list.isEmpty()) param = list.get(0);
		}
		weapon = param;
	}
	
	@Override
	public void readNBT(CompoundTag tag) {
		super.readNBT(tag);
		if (tag.contains("weapon")) weapon = tag.getString("weapon");
		else if (tag.contains("weaponId")) weapon = tag.getString("weaponId");
		if (!WeaponPresets.get().has(weapon)) weapon = "";
		ammo = tag.getInt("ammo");
	}
	
	@Override
	public CompoundTag writeNBT() {
		CompoundTag tag = super.writeNBT();
		tag.putString("weapon", weapon);
		tag.putInt("ammo", ammo);
		return tag;
	}
	
	@Override
	public void readBuffer(FriendlyByteBuf buffer) {
		super.readBuffer(buffer);
		weapon = buffer.readUtf();
		ammo = buffer.readInt();
	}
	
	@Override
	public void writeBuffer(FriendlyByteBuf buffer) {
		super.writeBuffer(buffer);
		buffer.writeUtf(weapon);
		buffer.writeInt(ammo);
	}
	
	public String getWeaponId() {
		return weapon;
	}

	@Override
	public void setup(EntityVehicle craft, String slotId, Vec3 pos) {
		super.setup(craft, slotId, pos);
		if (data == null && WeaponPresets.get().has(getWeaponId()))
			data = WeaponPresets.get().get(getWeaponId()).createWeaponInstance();
		if (data != null) {
			data.setMaxAmmo((int)getMaxAmmo());
			data.setCurrentAmmo(ammo);
		}
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
		if (WeaponPresets.get().has(getWeaponId())) {
			data = WeaponPresets.get().get(getWeaponId()).createWeaponInstance();
			if (data != null) {
				data.setMaxAmmo((int) getMaxAmmo());
				data.setCurrentAmmo(ammo);
			}
		} else {
			data = null;
		}
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

	@Override
	public void onReceiveClientSync() {
		super.onReceiveClientSync();
		setContinuity(getWeaponId());
		if (data != null) data.setCurrentAmmo(ammo);
	}
}
