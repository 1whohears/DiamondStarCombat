package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.client.renderer.texture.EntityScreen;
import com.onewhohears.dscombat.client.renderer.texture.RadarScreen;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.entity.aircraft.VehicleScreenData;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.client.renderer.MultiBufferSource;

public interface VehicleScreenRenderer<T extends EntityVehicle> extends TextureOnEntityRenderer<T> {
	
	public default boolean shouldRenderScreens(T vehicle) {
		return vehicle.getVehicleScreens().length > 0;
	}
	
	public default void renderVehicleScreens(T vehicle, PoseStack poseStack, 
			MultiBufferSource buffer, int packedLight, float partialTicks) {
		poseStack.pushPose();
		// TODO 1.3 render radar and other overlay component's onto the vehicle's dash
		Quaternion q = UtilAngles.lerpQ(partialTicks, vehicle.getPrevQ(), vehicle.getClientQ());
        poseStack.mulPose(q);
        
        for (VehicleScreenData screen : vehicle.getVehicleScreens()) {
        	renderScreen(vehicle, screen.type.screen, poseStack, buffer, partialTicks, packedLight, 
        			screen.pos, screen.width, screen.height, screen.xRot, screen.yRot, screen.zRot);
        }
		
		poseStack.popPose();
	}
	
	public static enum VehicleScreenType {
		RADAR_SCREEN(new RadarScreen());
		public final EntityScreen screen;
		private VehicleScreenType(EntityScreen screen) {
			this.screen = screen;
		}
	}
	
}
