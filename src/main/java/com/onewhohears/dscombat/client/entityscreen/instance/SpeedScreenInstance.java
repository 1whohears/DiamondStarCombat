package com.onewhohears.dscombat.client.entityscreen.instance;

import com.onewhohears.dscombat.DSCombatMod;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class SpeedScreenInstance extends SpinMeterScreenInstance {
	
	public static final ResourceLocation BACKGROUND = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/entity_screen/speed_bg.png");
    public static final ResourceLocation SPIN = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/entity_screen/speed_spin.png");
    
	public SpeedScreenInstance(int id) {
		super(id, BACKGROUND, SPIN);
	}

	@Override
	protected float getAngleDegrees(Entity entity) {
		// meters/tick * 20 * 100 / 360
		return (float)entity.getDeltaMovement().length()*5.5556f;
	}

}
