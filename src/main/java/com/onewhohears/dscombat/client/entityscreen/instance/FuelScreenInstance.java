package com.onewhohears.dscombat.client.entityscreen.instance;

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
	
	public static final ResourceLocation FUEL_SCREEN = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/entity_screen/fuel_screen.png");
    public static final ResourceLocation FUEL_SCREEN_ARROW = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/entity_screen/fuel_screen_arrow.png");
	
    protected final RenderType arrowRenderType;
    
	public FuelScreenInstance(int id) {
		super(id, FUEL_SCREEN);
		arrowRenderType = RenderType.text(FUEL_SCREEN_ARROW);
	}
	
	public void draw(Entity entity, PoseStack poseStack, MultiBufferSource buffer, 
			float partialTicks, int packedLight, float worldWidth, float worldHeight) {
		super.draw(entity, poseStack, buffer, partialTicks, packedLight, worldWidth, worldHeight);
		EntityVehicle vehicle = (EntityVehicle)entity;
		float max = vehicle.getMaxFuel(), fuelPercent = 0;
        if (max != 0) fuelPercent = vehicle.getCurrentFuel() / max;
		poseStack.pushPose();
		poseStack.mulPose(Vector3f.ZP.rotationDegrees(270F * fuelPercent + 45F));
		Matrix4f matrix4f = poseStack.last().pose();
		drawTextureCentered(arrowRenderType, matrix4f, buffer, packedLight, -0.001f);
		poseStack.popPose();
	}

}
