package com.onewhohears.dscombat.client.renderer;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.client.entityscreen.VehicleScreenMapReader;
import com.onewhohears.dscombat.client.model.obj.ObjAircraftModel;
import com.onewhohears.dscombat.data.aircraft.EntityScreenData;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.util.math.UtilAngles;
import com.onewhohears.dscombat.util.math.UtilGeometry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.world.entity.Entity;

public class RendererObjAircraft<T extends EntityVehicle> extends RendererObjEntity<T> implements RotableHitboxRenderer, VehicleScreenRenderer<T> {
	
	protected List<EntityScreenData> screens;
	
	public RendererObjAircraft(Context ctx, ObjAircraftModel<T> model) {
		super(ctx, model);
	}
	
	@Override
	public void render(T entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		super.render(entity, yaw, partialTicks, poseStack, bufferSource, packedLight);
		if (shouldDrawRotableHitboxes(entity)) drawRotableHitboxeOutlines(entity, partialTicks, poseStack, bufferSource);
		if (shouldRenderScreens(entity)) renderVehicleScreens(entity, poseStack, bufferSource, packedLight, partialTicks);
	}
	
	@Override
	public void renderVehicleScreens(T vehicle, PoseStack poseStack, 
			MultiBufferSource buffer, int packedLight, float partialTicks) {
		poseStack.pushPose();
		
		Quaternion q = UtilAngles.lerpQ(partialTicks, vehicle.getPrevQ(), vehicle.getClientQ());
        poseStack.mulPose(q);
        
        for (EntityScreenData screen : getScreens(vehicle)) {
        	renderScreen(vehicle, screen.instanceId, screen.type, 
        			poseStack, buffer, partialTicks, packedLight, 
        			screen.rel_pos, screen.width, screen.height, 
        			screen.xRot, screen.yRot, screen.zRot);
        }
		
		poseStack.popPose();
	}
	
	@Override
	public boolean shouldRenderScreens(T vehicle) {
		if (getScreens(vehicle).size() == 0) return false;
		Minecraft m = Minecraft.getInstance();
		if (m.player == null) return false;
		Entity seat = vehicle.getPilotSeat();
		if (seat == null) return false;
		return m.player.distanceToSqr(seat) < 64;
	}
	
	protected List<EntityScreenData> getScreens(T vehicle) {
		if (screens == null) screens = VehicleScreenMapReader.generateScreens(vehicle, model.modelId, 
				UtilGeometry.convertVector(model.getGlobalPivot()));
		return screens;
	}

}
