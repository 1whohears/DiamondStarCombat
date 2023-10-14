package com.onewhohears.dscombat.client.event.forgebus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.annotation.Nullable;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;

import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
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
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper.UnableToFindMethodException;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
public class ClientCameraEvents {
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void cameraSetup(ViewportEvent.ComputeCameraAngles event) {
		Minecraft m = Minecraft.getInstance();
		final var player = m.player;
		if (player == null || !player.isPassenger()) return;
		if (!(player.getRootVehicle() instanceof EntityVehicle plane)) return;
		float pt = (float)event.getPartialTick();
		boolean isController = player.equals(plane.getControllingPassenger());
		if (!plane.isFreeLook() && isController) {
			float xi = UtilAngles.lerpAngle(pt, plane.xRotO, plane.getXRot());
			float yi = UtilAngles.lerpAngle180(pt, plane.yRotO, plane.getYRot());
			player.setXRot(xi);
			player.setYRot(yi);
			player.xRotO = xi;
			player.yRotO = yi;
			event.setPitch(xi);
			event.setYaw(yi);
		}
		boolean detached = !m.options.getCameraType().isFirstPerson();
		boolean mirrored = m.options.getCameraType().isMirrored();
		float zi = UtilAngles.lerpAngle(pt, plane.zRotO, plane.zRot);
		if (detached && mirrored) zi *= -1;
		event.setRoll(zi);
		if (detached && isController && plane.camDist > 4 && getCameraMove() != null) {
			// TODO 4.1 option to change turret camera position. so camera could be under the aircraft
			double vehicleCamDist = Math.min(0, 4-getMaxDist(event.getCamera(), player, plane.camDist));
			try {
				getCameraMove().invoke(event.getCamera(), vehicleCamDist, 0, 0);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
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
	
	public static Method cameraMove;
	public static boolean tried1;
	
	@Nullable
	public static Method getCameraMove() {
		if (cameraMove != null) return cameraMove; 
		if (tried1) return null;
		tried1 = true;
		cameraMove = tryGetCameraMove("m_90568_");
		if (cameraMove == null) cameraMove = tryGetCameraMove("move");
		return cameraMove;
	}
	
	@Nullable
	private static Method tryGetCameraMove(String methodName) {
		try {
			return ObfuscationReflectionHelper.findMethod(Camera.class, methodName, 
					double.class, double.class, double.class);
		} catch (UnableToFindMethodException e) {
			System.out.println("ERROR: THE CAMERA MOVE METHOD IS NOT "+methodName);
			return null;
		}
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void clientTickSetMouseCallback(TickEvent.ClientTickEvent event) {
		Minecraft m = Minecraft.getInstance();
		if (m.player == null || m.player.tickCount != 1) return;
		if (!Config.CLIENT.cameraTurnRelativeToVehicle.get()) return;
		if (getVanillaOnMove() == null) return;
		GLFW.glfwSetCursorPosCallback(m.getWindow().getWindow(), ClientCameraEvents.customMouseCallback);
	}
	
	public static Method vanillaOnMove;
	public static boolean tried = false;
	
	@Nullable
	public static Method getVanillaOnMove() {
		if (vanillaOnMove != null) return vanillaOnMove; 
		if (tried) return null;
		tried = true;
		try {
			vanillaOnMove = ObfuscationReflectionHelper.findMethod(MouseHandler.class, "m_91561_", 
				long.class, double.class, double.class);
		} catch (UnableToFindMethodException e) {
			System.out.println("ERROR: VANILLA MOUSE POS CALLBACK IS NOT m_91561_");
		}
		return vanillaOnMove;
	}
	
	public static final GLFWCursorPosCallbackI customMouseCallback = (window, x, y) -> {
		Minecraft m = Minecraft.getInstance();
		m.execute(() -> {
			double xn = x, yn = y;
			if (window != m.getWindow().getWindow()) return;
			if (m.player != null && m.screen == null 
					&& m.player.getRootVehicle() instanceof EntityVehicle craft
					&& craft.isFreeLook()) {
				double r = Math.toRadians(craft.zRot);
				double dx = x - m.mouseHandler.xpos();
				double dy = y - m.mouseHandler.ypos();
				xn = dx*Math.cos(r) - dy*Math.sin(r) + m.mouseHandler.xpos();
				yn = dy*Math.cos(r) + dx*Math.sin(r) + m.mouseHandler.ypos();
				GLFW.glfwSetCursorPos(window, xn, yn);
			}
			try { getVanillaOnMove().invoke(m.mouseHandler, window, xn, yn); } 
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		});
	};
	
}
