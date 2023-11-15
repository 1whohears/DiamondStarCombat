package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.data.aircraft.EntityScreenData;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.player.Player;

public interface VehicleScreenRenderer<T extends EntityVehicle> extends EntityScreenRenderer<T> {
	
	public default boolean shouldRenderScreens(T vehicle) {
		if (vehicle.getVehicleScreens().length == 0) return false;
		Minecraft m = Minecraft.getInstance();
		Player player = m.player;
		if (player == null) return false;
		return player.distanceToSqr(vehicle) < 256;
	}
	
	public default void renderVehicleScreens(T vehicle, PoseStack poseStack, 
			MultiBufferSource buffer, int packedLight, float partialTicks) {
		poseStack.pushPose();
		// TODO 1.5 render radar and other overlay components onto the vehicle's dash
		Quaternion q = UtilAngles.lerpQ(partialTicks, vehicle.getPrevQ(), vehicle.getClientQ());
        poseStack.mulPose(q);
        
        for (EntityScreenData screen : vehicle.getVehicleScreens()) {
        	renderScreen(vehicle, screen.instanceId, screen.type, 
        			poseStack, buffer, partialTicks, packedLight, 
        			screen.pos, screen.width, screen.height, 
        			screen.xRot, screen.yRot, screen.zRot);
        }
		
		poseStack.popPose();
	}
	
}
