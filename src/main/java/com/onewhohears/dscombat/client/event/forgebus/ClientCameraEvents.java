package com.onewhohears.dscombat.client.event.forgebus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;

import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
public class ClientCameraEvents {
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void cameraSetup(ViewportEvent.ComputeCameraAngles event) {
		Minecraft m = Minecraft.getInstance();
		final var player = m.player;
		if (player == null || !player.isPassenger()) return;
		if (!(player.getRootVehicle() instanceof EntityAircraft plane)) return;
		float pt = (float)event.getPartialTick();
		if (!plane.isFreeLook() && player.equals(plane.getControllingPassenger())) {
			float xi = UtilAngles.lerpAngle(pt, plane.xRotO, plane.getXRot());
			float yi = UtilAngles.lerpAngle180(pt, plane.yRotO, plane.getYRot());
			player.setXRot(xi);
			player.setYRot(yi);
		}
		boolean detached = !m.options.getCameraType().isFirstPerson();
		boolean mirrored = m.options.getCameraType().isMirrored();
		float zi = UtilAngles.lerpAngle(pt, plane.zRotO, plane.zRot);
		if (detached && mirrored) zi *= -1;
		event.setRoll(zi);
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
	
	public static Method getVanillaOnMove() {
		if (vanillaOnMove != null) return vanillaOnMove; 
		if (tried) return null;
		tried = true;
		try {
			vanillaOnMove = MouseHandler.class.getDeclaredMethod(
				"m_91561_", long.class, double.class, double.class);
			vanillaOnMove.setAccessible(true);
			return vanillaOnMove;
		} catch (NoSuchMethodException | SecurityException e) {
			System.out.println("WARNING: VANILLA MOUSE POS CALLBACK IS NOT m_91561_");
		}
		try {
			vanillaOnMove = MouseHandler.class.getDeclaredMethod(
				"onMove", long.class, double.class, double.class);
			vanillaOnMove.setAccessible(true);
			return vanillaOnMove;
		} catch (NoSuchMethodException | SecurityException e) {
			System.out.println("WARNING: VANILLA MOUSE POS CALLBACK IS NOT onMove");
		}
		return null;
	}
	
	public static final GLFWCursorPosCallbackI customMouseCallback = (window, x, y) -> {
		Minecraft m = Minecraft.getInstance();
		m.execute(() -> {
			double xn = x, yn = y;
			if (window != m.getWindow().getWindow()) return;
			if (m.player != null && m.screen == null 
					&& m.player.getRootVehicle() instanceof EntityAircraft craft
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
