package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.data.weapon.instance.WeaponInstance;
import com.onewhohears.dscombat.data.weapon.stats.WeaponStats;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class WeaponPartData extends PartData implements LoadableRecipePartData {
	
	private final String[] compatible;
	protected String weaponId;
	private int ammo;
	private int max;
	
	public WeaponPartData(float weight, int ammo, int max, String preset, String[] compatible, ResourceLocation itemid, SlotType[] compatibleSlots) {
		super(weight, itemid, compatibleSlots);
		this.compatible = compatible;
		this.weaponId = preset;
		if (!WeaponPresets.get().has(weaponId)) weaponId = "";
		this.ammo = ammo;
		this.max = max;
	}
	
	public WeaponPartData(float weight, String preset, String[] compatible, ResourceLocation itemid, SlotType[] compatibleSlots) {
		this(weight, 0, 0, preset, compatible, itemid, compatibleSlots);
		WeaponStats data = WeaponPresets.get().get(weaponId);
		if (data != null) {
			this.ammo = data.getMaxAmmo();
			this.max = data.getMaxAmmo();
		}
	}
	
	public void read(CompoundTag tag) {
		super.read(tag);
		weaponId = tag.getString("weaponId");
		if (!WeaponPresets.get().has(weaponId)) weaponId = "";
		ammo = tag.getInt("ammo");
		max = tag.getInt("max");
	}
	
	public CompoundTag write() {
		CompoundTag tag = super.write();
		tag.putString("weaponId", weaponId);
		tag.putInt("ammo", ammo);
		tag.putInt("max", max);
		return tag;
	}

	public void read(FriendlyByteBuf buffer) {
		super.read(buffer);
		weaponId = buffer.readUtf();
		ammo = buffer.readInt();
		max = buffer.readInt();
	}
	
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
		buffer.writeUtf(weaponId);
		buffer.writeInt(ammo);
		buffer.writeInt(max);
	}

	@Override
	public PartType getType() {
		return PartType.INTERNAL_WEAPON;
	}
	
	@Override
	public void setup(EntityVehicle craft, String slotId, Vec3 pos) {
		super.setup(craft, slotId, pos);
		WeaponInstance<?> data = craft.weaponSystem.get(weaponId, slotId);
		if (data == null) {
			if (!WeaponPresets.get().has(weaponId)) return;
			data = WeaponPresets.get().get(weaponId).createWeaponInstance();
			data.setSlot(slotId);
			craft.weaponSystem.addWeapon(data);
		}
		data.setCurrentAmmo(ammo);
		data.setLaunchPos(pos);
		if (!craft.level.isClientSide) data.updateClientAmmo(craft);
	}
	
	@Override
	public boolean isSetup(String slotId, EntityVehicle craft) {
		WeaponInstance<?> data = craft.weaponSystem.get(weaponId, slotId);
		if (data == null) return false;
		return data.getCurrentAmmo() == ammo && data.getStats().getMaxAmmo() == max;
	}
	
	@Override
	public void remove(String slotId) {
		super.remove(slotId);
		if (getParent() == null) return;
		getParent().weaponSystem.removeWeapon(weaponId, slotId);
	}
	
	@Override
	public void tick(String slotId) {
		super.tick(slotId);
		if (getParent() == null) return;
		WeaponInstance<?> data = getParent().weaponSystem.get(weaponId, slotId);
		if (data != null) {
			ammo = data.getCurrentAmmo();
			max = data.getStats().getMaxAmmo();
		}
	}
	
	@Override
	public void clientTick(String slotId) {
		super.clientTick(slotId);
		this.tick(slotId);
	}
	
	@Override
	public float getWeight() {
		if (max == 0) return 0;
		float w = super.getWeight();
		return w * (float)ammo / (float)max;
	}
	
	public boolean isWeaponCompatible(String preset) {
		if (preset == null) return false;
		for (int i = 0; i < compatible.length; ++i) 
			if (compatible[i].equals(preset)) 
				return true;
		return false;
	}

	@Override
	public float getCurrentAmmo() {
		return ammo;
	}

	@Override
	public float getMaxAmmo() {
		return max;
	}

	@Override
	public void setCurrentAmmo(float ammo) {
		this.ammo = (int)ammo;
	}

	@Override
	public void setMaxAmmo(float max) {
		this.max = (int)max;
	}

	@Override
	public boolean isCompatibleWithAmmoContinuity(String continuity) {
		return isWeaponCompatible(continuity);
	}

	@Override
	public boolean updateContinuityIfEmpty() {
		return true;
	}

	@Override
	public void setContinuity(String continuity) {
		this.weaponId = continuity;
	}

	@Override
	public String getContinuity() {
		return weaponId;
	}
	
	@Override
	public boolean isContinuityEmpty() {
		return getContinuity() == null || getContinuity().isEmpty() || getCurrentAmmo() == 0;
	}

}
