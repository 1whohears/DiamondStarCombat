package com.onewhohears.dscombat.client.entityscreen.instance;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.entity.vehicle.EntityPlane;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class AOAScreenInstance extends SpinMeterScreenInstance {

	public static final ResourceLocation BACKGROUND = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/entity_screen/aoa_bg.png");
    public static final ResourceLocation SPIN = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/entity_screen/point_needle.png");

	public AOAScreenInstance(int id) {
		super(id, BACKGROUND, SPIN);
	}

	@Override
	protected float getAngleDegrees(Entity entity) {
		return 90 - ((EntityPlane)entity).getAOA()*3;
	}

}
