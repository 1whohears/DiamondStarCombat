package com.onewhohears.dscombat.client.event.forgebus;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;

import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
public class ClientCameraEvents {
	
	private static float ptOld;
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void cameraSetup(ViewportEvent.ComputeCameraAngles event) {
		Minecraft m = Minecraft.getInstance();
		final var player = m.player;
		if (player == null || !player.isPassenger()) return;
		if (!(player.getRootVehicle() instanceof EntityVehicle plane)) return;
		float pt = (float)event.getPartialTick();
		boolean isController = player.equals(plane.getControllingPassenger());
		boolean detached = !m.options.getCameraType().isFirstPerson();
		boolean mirrored = m.options.getCameraType().isMirrored();
		if (isController && DSCClientInputs.isCameraLockedForward()) {
			float xi = UtilAngles.lerpAngle(pt, plane.xRotO, plane.getXRot());
			float yi = UtilAngles.lerpAngle180(pt, plane.yRotO, plane.getYRot());
			player.setXRot(xi);
			player.setYRot(yi);
			player.xRotO = xi;
			player.yRotO = yi;
			event.setPitch(xi);
			event.setYaw(yi);
		} else if (isController && DSCClientInputs.isCameraFreeRelative()) {
			float ptDiff = ptDiff(pt, ptOld);
			float planeXRotDiff = plane.getXRot()-plane.xRotO;
			if (planeXRotDiff != 0) {
				float dxi = Mth.wrapDegrees(planeXRotDiff) * ptDiff;
				float x = player.getXRot() + dxi;
				player.setXRot(x);
				player.xRotO = x;
				event.setPitch(x);
			}
			float planeYRotDiff = plane.getYRot()-plane.yRotO;
			if (planeYRotDiff != 0) {
				float dyi = Mth.wrapDegrees(planeYRotDiff) * ptDiff;
				float y = player.getYRot() + dyi;
				if (mirrored) y += 180;
				player.setYRot(y);
				player.yRotO = y;
				event.setYaw(y);
			}
		}
		float zi = UtilAngles.lerpAngle(pt, plane.zRotO, plane.zRot);
		if (detached && mirrored) zi *= -1;
		event.setRoll(zi);
		double camDist = plane.vehicleData.cameraDistance;
		if (detached && isController && camDist > 4) {
			// TODO 4.2 making third person work in mouse mode (again, à la garry's mod WAC planes)
			// TODO 4.1 option to change turret camera position. so camera could be under the aircraft
			// To add onto this, a thermal camera option would be nice too; or in the future, an attack helicopter
			// could have a movable turret
			double vehicleCamDist = Math.min(0, 4-getMaxDist(event.getCamera(), player, camDist));
			event.getCamera().move(vehicleCamDist, 0, 0);
		}
		ptOld = pt;
	}
	
	private static float ptDiff(float pt, float ptOld) {
		if (pt == ptOld) return 0;
		else if (pt > ptOld) return pt - ptOld;
		return pt+1f - ptOld;
	}
	
	public static double getMaxDist(Camera cam, Player player, double dist) {
		Vec3 from = cam.getPosition();
		Vector3f d = cam.getLookVector().copy();
		d.mul((float)-dist);
		Vec3 to = from.add(d.x(), d.y(), d.z());
		HitResult hitresult = player.level.clip(new ClipContext(from, to, 
				ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, player));
		if (hitresult.getType() != HitResult.Type.MISS) {
			double d0 = hitresult.getLocation().distanceTo(from);
			if (d0 < dist) dist = d0;
		}
		return dist;
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void clientTickSetMouseCallback(TickEvent.ClientTickEvent event) {
		Minecraft m = Minecraft.getInstance();
		if (m.player == null || m.player.tickCount != 1) return;
		if (!Config.CLIENT.cameraTurnRelativeToVehicle.get()) return;
		GLFW.glfwSetCursorPosCallback(m.getWindow().getWindow(), ClientCameraEvents.customMouseCallback);
	}
	
	public static final GLFWCursorPosCallbackI customMouseCallback = (window, x, y) -> {
		Minecraft m = Minecraft.getInstance();
		m.execute(() -> {
			double xn = x, yn = y;
			if (window != m.getWindow().getWindow()) return;
			if (m.player != null && m.screen == null 
					&& m.player.getRootVehicle() instanceof EntityVehicle craft
					&& DSCClientInputs.isCameraFree()) {
				double r = Math.toRadians(craft.zRot);
				double dx = x - m.mouseHandler.xpos();
				double dy = y - m.mouseHandler.ypos();
				xn = dx*Math.cos(r) - dy*Math.sin(r) + m.mouseHandler.xpos();
				yn = dy*Math.cos(r) + dx*Math.sin(r) + m.mouseHandler.ypos();
				GLFW.glfwSetCursorPos(window, xn, yn);
			}
			m.mouseHandler.onMove(window, xn, yn);
		});
	};
	
}
