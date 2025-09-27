package com.onewhohears.dscombat.client.entityscreen.instance;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class FuelScreenInstance extends SpinMeterScreenInstance {
	
	public static final ResourceLocation FUEL_SCREEN = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/entity_screen/fuel_screen.png");
    public static final ResourceLocation FUEL_SCREEN_ARROW = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/entity_screen/fuel_screen_arrow.png");
    
	public FuelScreenInstance(int id) {
		super(id, FUEL_SCREEN, FUEL_SCREEN_ARROW);
	}

	@Override
	protected float getAngleDegrees(Entity entity) {
		EntityVehicle vehicle = (EntityVehicle)entity;
		float max = vehicle.getMaxFuel(), fuelPercent = 0;
        if (max != 0) fuelPercent = vehicle.getCurrentFuel() / max;
		return 270F * fuelPercent + 45F;
	}

}
