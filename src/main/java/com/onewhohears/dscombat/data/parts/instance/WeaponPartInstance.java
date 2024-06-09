package com.onewhohears.dscombat.data.parts.instance;

import java.util.List;

import com.onewhohears.dscombat.data.parts.LoadableRecipePartInstance;
import com.onewhohears.dscombat.data.parts.stats.WeaponPartStats;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.data.weapon.instance.WeaponInstance;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class WeaponPartInstance<T extends WeaponPartStats> extends PartInstance<T> implements LoadableRecipePartInstance {
	
	protected String weapon = "";
	private int ammo = 0;
	
	public WeaponPartInstance(T stats) {
		super(stats);
	}
	
	@Override
	public void setFilled(String param) {
		super.setFilled(param);
		if (param.isEmpty()) {
			List<String> list = WeaponPresets.get().getCompatibleWeapons(getStatsId());
			if (list.size() > 0) param = list.get(0);
		}
		weapon = param;
		ammo = getStats().getMaxAmmo();
	}
	
	@Override
	public void setParamNotFilled(String param) {
		super.setParamNotFilled(param);
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
	
	@Override
	public void setup(EntityVehicle craft, String slotId, Vec3 pos) {
		super.setup(craft, slotId, pos);
		WeaponInstance<?> data = craft.weaponSystem.get(weapon, slotId);
		if (data == null) {
			if (!WeaponPresets.get().has(weapon)) return;
			data = WeaponPresets.get().get(weapon).createWeaponInstance();
			data.setSlot(slotId);
			craft.weaponSystem.addWeapon(data);
		}
		data.setMaxAmmo(getStats().getMaxAmmo());
		data.setCurrentAmmo(ammo);
		data.setLaunchPos(pos);
		if (!craft.level.isClientSide) data.updateClientAmmo(craft);
	}
	
	@Override
	public boolean isSetup(String slotId, EntityVehicle craft) {
		WeaponInstance<?> data = craft.weaponSystem.get(weapon, slotId);
		if (data == null) return false;
		return data.getCurrentAmmo() == ammo;
	}
	
	@Override
	public void remove(String slotId) {
		super.remove(slotId);
		if (getParent() == null) return;
		getParent().weaponSystem.removeWeapon(weapon, slotId);
	}
	
	@Override
	public void tick(String slotId) {
		super.tick(slotId);
		if (getParent() == null) return;
		WeaponInstance<?> data = getParent().weaponSystem.get(weapon, slotId);
		if (data != null) {
			ammo = data.getCurrentAmmo();
		}
	}
	
	@Override
	public void clientTick(String slotId) {
		super.clientTick(slotId);
		this.tick(slotId);
	}
	
	@Override
	public float getWeight() {
		int max = getStats().getMaxAmmo();
		if (max == 0) return 0;
		float w = super.getWeight();
		return w * (float)ammo / (float)max;
	}

	@Override
	public float getCurrentAmmo() {
		return ammo;
	}

	@Override
	public float getMaxAmmo() {
		return getStats().getMaxAmmo();
	}

	@Override
	public void setCurrentAmmo(float ammo) {
		this.ammo = (int)ammo;
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
		this.weapon = continuity;
	}

	@Override
	public String getContinuity() {
		return getWeaponId();
	}
	
	@Override
	public boolean isContinuityEmpty() {
		return getContinuity() == null || getContinuity().isEmpty() || getCurrentAmmo() == 0;
	}
	
	public String getWeaponId() {
		return weapon;
	}

}
