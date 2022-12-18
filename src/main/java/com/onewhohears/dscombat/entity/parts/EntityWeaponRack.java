package com.onewhohears.dscombat.entity.parts;

import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityWeaponRack extends EntityAbstractPart {
	
	public EntityWeaponRack(EntityType<?> type, Level level) {
		super(type, level);
	}
	
	public EntityWeaponRack(EntityType<?> type, Level level, String slotId, Vec3 pos) {
		super(type, level, slotId, pos);
	}
	
	// TODO display weapon models under aircraft wings
	
	public int getMissileNum() {
		if (this.getVehicle() instanceof EntityAbstractAircraft plane) {
			
		}
		return 0;
	}

}
