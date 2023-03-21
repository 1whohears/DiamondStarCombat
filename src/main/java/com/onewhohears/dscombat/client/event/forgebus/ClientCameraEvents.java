package com.onewhohears.dscombat.client.event.forgebus;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.entity.parts.EntitySeat;
import com.onewhohears.dscombat.entity.parts.EntitySeatCamera;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
public class ClientCameraEvents {
	
	private static Entity prevCamera;
	//private static float prevPlayerX, prevPlayerY;
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void cameraSetup(ViewportEvent.ComputeCameraAngles event) {
		Minecraft m = Minecraft.getInstance();
		final var player = m.player;
		if (player == null) return;
		prevCamera = m.getCameraEntity();
		if (player.getVehicle() instanceof EntitySeat seat 
				&& seat.getVehicle() instanceof EntityAircraft plane) {
			EntitySeatCamera camera = seat.getCamera();
			float xi, yi, zi;
			float pt = (float)event.getPartialTick();
			if (!plane.isFreeLook() && plane.getControllingPassenger() != null 
					&& plane.getControllingPassenger().equals(player)) {
				xi = UtilAngles.lerpAngle(pt, plane.xRotO, plane.getXRot());
				yi = UtilAngles.lerpAngle180(pt, plane.yRotO, plane.getYRot());
				zi = UtilAngles.lerpAngle(pt, plane.zRotO, plane.zRot);
				player.setXRot(xi);
				player.setYRot(yi);
			} else {
				zi = UtilAngles.lerpAngle(pt, plane.zRotO, plane.zRot);
				xi = player.getXRot();
				yi = player.getYRot();
				// HOW make the mouse moves change the camera angles relative to the plane axis
				// player.getXRot() = player.xRotO on the client's player
				// mouseHandler.getXVelocity() is always 0
				/*float diffx = 0; // how to get mouse movements?
				float diffy = 0; // how to get mouse movements?
				float[] rel = UtilAngles.globalToRelativeDegrees(prevPlayerX, prevPlayerY, plane.getClientQ());
				rel[0] += diffx; rel[1] += diffy;
				float[] global = UtilAngles.relativeToGlobalDegrees(rel[0], rel[1], plane.getClientQ());
				xi = global[0];
				yi = global[1];
				player.setXRot(xi);
				player.setYRot(yi);*/
			}
			if (m.options.getCameraType() == CameraType.THIRD_PERSON_FRONT) {
				event.setPitch(xi*-1f);
				event.setYaw(yi+180f);
				event.setRoll(zi*-1f);
			} else {
				event.setPitch(xi);
				event.setYaw(yi);
				event.setRoll(zi);
			}
			if (camera != null) {
				camera.setXRot(xi);
				camera.setYRot(yi);
				camera.xRotO = xi;
				camera.yRotO = yi;
				if (!prevCamera.equals(camera)) m.setCameraEntity(camera);
			}
		} else {
			if (!prevCamera.equals(player)) m.setCameraEntity(player);
		}
		//prevPlayerX = player.getXRot();
		//prevPlayerY = player.getYRot();
	}
	
}
