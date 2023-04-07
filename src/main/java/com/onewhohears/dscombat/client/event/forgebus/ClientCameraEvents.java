package com.onewhohears.dscombat.client.event.forgebus;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.entity.parts.EntitySeat;
import com.onewhohears.dscombat.entity.parts.EntitySeatCamera;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
public class ClientCameraEvents {
	
	private static Entity prevCamera;
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void cameraSetup(ViewportEvent.ComputeCameraAngles event) {
		Minecraft m = Minecraft.getInstance();
		final var player = m.player;
		if (player == null) return;
		prevCamera = m.getCameraEntity();
		if (player.getVehicle() instanceof EntitySeat seat 
				&& seat.getVehicle() instanceof EntityAircraft plane) {
			EntitySeatCamera camera = seat.getCamera();
			if (camera == null) return;
			float xi, yi, zi;
			float pt = (float)event.getPartialTick();
			if (!plane.isFreeLook() && player.equals(plane.getControllingPassenger())) {
				xi = UtilAngles.lerpAngle(pt, plane.xRotO, plane.getXRot());
				yi = UtilAngles.lerpAngle180(pt, plane.yRotO, plane.getYRot());
				zi = UtilAngles.lerpAngle(pt, plane.zRotO, plane.zRot);
				player.setXRot(xi);
				player.setYRot(yi);
			} else {
				zi = UtilAngles.lerpAngle(pt, plane.zRotO, plane.zRot);
				xi = player.getXRot();
				yi = player.getYRot();
			}
			camera.setXRot(xi);
			camera.setYRot(yi);
			if (!prevCamera.equals(camera)) m.setCameraEntity(camera);
			boolean detached = !m.options.getCameraType().isFirstPerson();
			boolean mirrored = m.options.getCameraType().isMirrored();
			event.getCamera().setup(m.level, camera, 
					detached, mirrored, 
					(float) event.getPartialTick());
			if (detached && mirrored) zi *= -1;
			event.setRoll(zi);
		} else {
			if (!prevCamera.equals(player)) m.setCameraEntity(player);
		}
	}
	
	@SubscribeEvent
	public static void clientTickSetMouseCallback(TickEvent.ClientTickEvent event) {
		// TODO make the mouse moves change the camera angles relative to the plane axis
		// use access transformer to make MouseHandler.onMove method public
		// have GLFW switch between vanilla and custom one
		// for performance reasons this should probably be done in client tick
		// GLFW.glfwSetCursorPosCallback(m.getWindow().getWindow(), null);
	}
	
}
