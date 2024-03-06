package com.onewhohears.dscombat.client.entityscreen.instance;

import com.onewhohears.dscombat.DSCombatMod;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class HeadingScreenInstance extends SpinMeterScreenInstance {
	
	public static final ResourceLocation BACKGROUND = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/entity_screen/heading_bg.png");
    public static final ResourceLocation SPIN = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/entity_screen/heading_spin.png");
    
	public HeadingScreenInstance(int id) {
		super(id, BACKGROUND, SPIN);
	}

	@Override
	protected float getAngleDegrees(Entity entity) {
		return -entity.getYRot();
	}

}
