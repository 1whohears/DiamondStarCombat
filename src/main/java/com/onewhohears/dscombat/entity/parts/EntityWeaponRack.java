package com.onewhohears.dscombat.entity.parts;

import com.onewhohears.dscombat.init.ModEntities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityWeaponRack extends EntityAbstractPart {
	
	public EntityWeaponRack(EntityType<?> type, Level level) {
		super(type, level);
	}
	
	public EntityWeaponRack(Level level, String slotId, Vec3 pos) {
		super(ModEntities.WEAPON_RACK.get(), level, slotId, pos);
	}
	
	// TODO display weapon models under aircraft wings

}
