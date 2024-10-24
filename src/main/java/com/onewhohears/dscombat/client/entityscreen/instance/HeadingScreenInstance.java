package com.onewhohears.dscombat.client.entityscreen.instance;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.onewholibs.util.UtilMCText;

import net.minecraft.client.renderer.MultiBufferSource;
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
	public void draw(Entity entity, PoseStack poseStack, MultiBufferSource buffer, 
			float partialTicks, int packedLight, float worldWidth, float worldHeight) {
		super.draw(entity, poseStack, buffer, partialTicks, packedLight, worldWidth, worldHeight);
		drawText(UtilMCText.literal(String.format("%04.0f", entity.getYRot())), 
				-0.25f, -0.1f, 0.5f, poseStack, buffer, 0x00ff00, packedLight);
	}

	@Override
	protected float getAngleDegrees(Entity entity) {
		return 180 - entity.getYRot();
	}

}
