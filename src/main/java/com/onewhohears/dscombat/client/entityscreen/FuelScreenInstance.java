package com.onewhohears.dscombat.client.entityscreen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class FuelScreenInstance extends EntityScreenInstance {
	
	public static final ResourceLocation FUEL_GAUGE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/fuel_guage.png");
    public static final ResourceLocation FUEL_GAUGE_ARROW = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/fuel_guage_arrow_fit.png");
	
    protected final RenderType arrowRenderType;
    
	public FuelScreenInstance(int id) {
		super(id, FUEL_GAUGE);
		arrowRenderType = RenderType.text(FUEL_GAUGE_ARROW);
	}
	
	public void draw(Entity entity, PoseStack poseStack, MultiBufferSource buffer, float partialTicks, int packedLight,
			float worldWidth, float worldHeight) {
		super.draw(entity, poseStack, buffer, partialTicks, packedLight, worldWidth, worldHeight);
		EntityVehicle vehicle = (EntityVehicle)entity;
		float max = vehicle.getMaxFuel(), fuelPercent = 0;
        if (max != 0) fuelPercent = vehicle.getCurrentFuel() / max;
		poseStack.pushPose();
		poseStack.translate(0.5, 0.6, 0);
		poseStack.mulPose(Vector3f.ZP.rotationDegrees(160F * fuelPercent + 10F));
		Matrix4f matrix4f = poseStack.last().pose();
		drawTextureCentered(arrowRenderType, matrix4f, buffer, packedLight, -0.001f);
		poseStack.popPose();
	}

}
