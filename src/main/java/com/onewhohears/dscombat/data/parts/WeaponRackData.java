package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.parts.EntityWeaponRack;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class WeaponRackData extends WeaponPartData {
	
	public final float changeLaunchPitch;
	
	public WeaponRackData(float weight, int ammo, int max, String preset, String[] compatible, ResourceLocation itemid, SlotType[] compatibleSlots, float launchPitch) {
		super(weight, ammo, max, preset, compatible, itemid, compatibleSlots);
		this.changeLaunchPitch = launchPitch;
	}
	
	public WeaponRackData(float weight, String preset, String[] compatible, ResourceLocation itemid, SlotType[] compatibleSlots, float launchPitch) {
		super(weight, preset, compatible, itemid, compatibleSlots);
		this.changeLaunchPitch = launchPitch;
	}

	@Override
	public void serverSetup(EntityVehicle craft, String slotId, Vec3 pos) {
		super.serverSetup(craft, slotId, pos);
		WeaponData data = craft.weaponSystem.get(weaponId, slotId);
		if (data == null) return;
		data.setChangeLaunchPitch(changeLaunchPitch);
		if (!isEntitySetup(slotId, craft)) {
			EntityWeaponRack rack = (EntityWeaponRack) data.getRackEntityType().create(craft.level);
			setUpPartEntity(rack, craft, slotId, pos, 5);
			craft.level.addFreshEntity(rack);
		}
	}
	
	@Override
	public void serverRemove(String slotId) {
		super.serverRemove(slotId);
		removeEntity(slotId);
	}
	
	@Override
	public PartType getType() {
		return PartType.WEAPON_RACK;
	}

}
