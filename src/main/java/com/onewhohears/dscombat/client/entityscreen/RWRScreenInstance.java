package com.onewhohears.dscombat.client.entityscreen;

import java.util.Collection;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class RWRScreenInstance extends EntityDynamicScreenInstance {
	
	public static final ResourceLocation TEXTURE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/entity_screen/rwr_screen_bg.png");
	
	protected final int centerX, centerY, textureRadius;
	
	public RWRScreenInstance(int id) {
		super("rwr", id, TEXTURE, 512, 512);
		textureRadius = 230;
		centerX = pixelWidth/2; 
		centerY = pixelHeight/2;
	}
	
	@Override
	public void draw(Entity entity, PoseStack poseStack, MultiBufferSource buffer, float partialTicks, int packedLight,
			float worldWidth, float worldHeight) {
		super.draw(entity, poseStack, buffer, partialTicks, packedLight, worldWidth, worldHeight);
		// draw text test
		drawText(Component.literal("obama?"), -0.5f, 0.5f, 1, poseStack, buffer, 0x0000ff, packedLight);
	}
	
	@Override
	public boolean shouldUpdateTexture(Entity entity) {
		EntityVehicle vehicle = (EntityVehicle)entity;
		if (entity.tickCount == prevUpdateTickCount) return false;
		if (entity.tickCount % 2 != 0) return false;
		if ((entity.tickCount-vehicle.radarSystem.clientRwrRefreshTime) > 100) return false;
		return true;
	}
	
	@Override
	protected void updateTexture(Entity entity) {
		clearDynamicPixels();
		EntityVehicle vehicle = (EntityVehicle)entity;
		if (!vehicle.radarSystem.clientHasRWRWarnings()) return;
		Collection<RadarSystem.RWRWarning> warnings = vehicle.radarSystem.getClientRWRWarnings();
		for (RadarSystem.RWRWarning warn : warnings) drawWarning(warn, vehicle);
	}
	
	protected void drawWarning(RadarSystem.RWRWarning warn, EntityVehicle vehicle) {
		Vec3 dp = warn.pos.subtract(vehicle.position());
		double dist = dp.horizontalDistance();
		double screen_dist = dist*0.001;
		if (screen_dist > 1) screen_dist = 1;
		else if (screen_dist < 0.1) screen_dist = 0.1;
		float yaw = (UtilAngles.getYaw(dp)-vehicle.getYRot()+180)*Mth.DEG_TO_RAD;
		int x = Math.min(centerX + (int)(-Mth.sin(yaw)*textureRadius*screen_dist), pixelWidth-1);
		int y = Math.min(centerY + (int)(Mth.cos(yaw)*textureRadius*screen_dist), pixelHeight-1);
		drawWarningAtPos(warn, x, y);
	}
	
	protected void drawWarningAtPos(RadarSystem.RWRWarning warn, int x, int y) {
		int r = 32, t = 5;
		if (warn.isMissile) {
			drawDiamond(x, y, r, t, 0xff0000ff); 
			drawCross(x, y, r/2, t, 0xff0000ff);
		} else if (warn.fromGround) {
			drawDiamond(x, y, r, t, 0xff00ffff); 
			drawPlus(x, y, r, t, 0xff00ffff);
		} else {
			drawDiamond(x, y, r, t, 0xff00ff00); 
		}
	}

}
