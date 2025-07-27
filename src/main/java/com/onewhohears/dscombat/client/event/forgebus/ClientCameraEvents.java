package com.onewhohears.dscombat.client.event.forgebus;

import com.onewhohears.dscombat.entity.parts.EntityRidablePart;
import com.onewhohears.dscombat.mixin.CameraAccess;
import com.onewhohears.onewholibs.util.math.UtilGeometry;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.parts.EntityGimbal;
import com.onewhohears.onewholibs.util.math.UtilAngles;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
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

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
public class ClientCameraEvents {
	
	private static Entity prevGimbal;
	private static long prevCamSetupTime;
	@Nullable static private Quaternion prevQ;
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void cameraSetup(ViewportEvent.ComputeCameraAngles event) {
		Minecraft m = Minecraft.getInstance();
		final var player = m.player;
		if (player == null) return;
		if (!player.isPassenger()) {
			if (m.getCameraEntity().equals(prevGimbal)) m.setCameraEntity(player);
			prevGimbal = null;
			return;
		}
		if (!(player.getRootVehicle() instanceof EntityVehicle vehicle)) return;
		float pt = (float)event.getPartialTick();
		boolean detached = !m.options.getCameraType().isFirstPerson();
		boolean mirrored = m.options.getCameraType().isMirrored();
		float camYOffset = 0;
		boolean isPilot = false, isCopilot = false;
		if (player.getVehicle() instanceof EntityRidablePart seat) {
			isPilot = seat.isPilotSeat();
			isCopilot = seat.isCoPilotSeat();
			if (DSCClientInputs.isGimbalMode()) camYOffset = seat.getCameraYOffset();
		}
		if ((prevGimbal != null && prevGimbal.isRemoved())
				|| (!DSCClientInputs.isGimbalMode() && !m.getCameraEntity().equals(player))) {
			m.setCameraEntity(player);
			prevGimbal = null;
		}
		if (DSCClientInputs.isGimbalMode() && (isPilot || isCopilot || camYOffset == 0) 
				&& vehicle.getGimbalForPilotCamera() != null) {
			EntityGimbal gimbal = vehicle.getGimbalForPilotCamera();
			if (!m.getCameraEntity().equals(gimbal)) m.setCameraEntity(gimbal);
			gimbal.setXRot(player.getViewXRot(pt));
			gimbal.setYRot(player.getViewYRot(pt));
			prevGimbal = gimbal;
			camYOffset = -0.2f;
		} 
		if (isPilot && DSCClientInputs.isCameraLockedForward()) {
			float xi = UtilAngles.lerpAngle(pt, vehicle.xRotO, vehicle.getXRot());
			float yi = UtilAngles.lerpAngle180(pt, vehicle.yRotO, vehicle.getYRot());
			player.setXRot(xi);
			player.setYRot(yi);
			player.xRotO = xi;
			player.yRotO = yi;
			if (mirrored) {
				xi *= -1;
				yi += 180;
			}
			event.setPitch(xi);
			event.setYaw(yi);
		} else if (isPilot && DSCClientInputs.isCameraFreeRelative()) {
			double ptDiff = (System.currentTimeMillis()-prevCamSetupTime) / 50.0d; // time diff in ticks
			Vec3 vehicleAngVel = vehicle.getAngularVel().scale(ptDiff);
			Quaternion qPT = vehicle.getClientQ(pt);
			if (!UtilGeometry.isZero(vehicleAngVel) && prevQ != null) {
				/*Vec3 playerLookDir = player.getLookAngle();
				Quaternion qV = vehicle.getClientQ();
				qV.normalize();
				Quaternion qVi = new Quaternion(qV); qVi.conj();
				Quaternion dQ = PhysicsBody.rotateAngularVel(vehicleAngVel);
				dQ.normalize();
				Quaternion qR = new Quaternion(qV);
				qR.mul(dQ); qR.mul(qVi);
				qR.normalize();
				Vec3 rotLookDir = UtilAngles.rotateVector(playerLookDir, qR);
				float x = UtilAngles.getPitch(rotLookDir);
				float y = UtilAngles.getYaw(rotLookDir);*/
				float[] relativeAngles = UtilAngles.globalToRelativeDegrees(player.getXRot(), player.getYRot(), prevQ);
				float[] globalAngles = UtilAngles.relativeToGlobalDegrees(relativeAngles[0], relativeAngles[1], qPT);
				float x = globalAngles[0];
				float y = globalAngles[1];
				player.setXRot(x);
				player.xRotO = x;
				player.setYRot(y);
				player.yRotO = y;
				if (mirrored) {
					x *= -1;
					y += 180;
				}
				event.setPitch(x);
				event.setYaw(y);
			}
			prevQ = qPT;
			// TODO 4.1 making third person work in mouse mode (again, àla garry's mod WAC planes)
			/*float ptDiff = (System.currentTimeMillis()-prevCamSetupTime) / 50f; // time diff in ticks
			float planeXRotDiff = vehicle.getXRot()-vehicle.xRotO;
			if (planeXRotDiff != 0) {
				float dxi = Mth.wrapDegrees(planeXRotDiff) * ptDiff;
				float x = player.getXRot() + dxi;
				player.setXRot(x);
				player.xRotO = x;
				if (mirrored) x *= -1;
				event.setPitch(x);
			}
			float planeYRotDiff = vehicle.getYRot()-vehicle.yRotO;
			if (planeYRotDiff != 0) {
				float dyi = Mth.wrapDegrees(planeYRotDiff) * ptDiff;
				float y = player.getYRot() + dyi;
				player.setYRot(y);
				player.yRotO = y;
				if (mirrored) y += 180;
				event.setYaw(y);
			}*/
			System.out.printf("player x=%.2f y=%.2f vehicle x=%.2f y=%.2f%n",
					event.getPitch(), event.getYaw(), vehicle.getXRot(), vehicle.getYRot());
		}
		float zi = UtilAngles.lerpAngle(pt, vehicle.zRotO, vehicle.zRot);
		if (detached && mirrored) zi *= -1;
		event.setRoll(zi);
		double camDist = vehicle.getStats().cameraDistance;
		Camera camera = event.getCamera();
		if (detached && isPilot && camDist > 4) {
			double vehicleCamDist = Math.min(0, 4-getMaxDist(camera, player, camDist));
			((CameraAccess)camera).invokeSetRotation(event.getYaw(), event.getPitch());
			camera.move(vehicleCamDist, 0, 0);
		}
		Quaternion q = null;
		if (camYOffset != 0) {
			q = UtilAngles.lerpQ(pt, vehicle.getPrevQ(), vehicle.getClientQ());
			Vec3 yawAxis = UtilAngles.getYawAxis(q);
			camera.setPosition(camera.getPosition().add(yawAxis.scale(camYOffset)));
		}
		if (DSCClientInputs.getLeanAmount() != 0) {
			if (q == null) q = UtilAngles.lerpQ(pt, vehicle.getPrevQ(), vehicle.getClientQ());
			Vec3 pitchAxis = UtilAngles.getPitchAxis(q);
			camera.setPosition(camera.getPosition().add(pitchAxis.scale(-DSCClientInputs.getLeanAmount())));
		}
		prevCamSetupTime = System.currentTimeMillis();
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
			if (DSCClientInputs.isCameraFree()
					&& m.player != null && m.screen == null 
					&& m.player.getRootVehicle() instanceof EntityVehicle craft) {
				double r = Math.toRadians(craft.zRot);
				double dx = x - m.mouseHandler.xpos();
				double dy = y - m.mouseHandler.ypos();
				double cosR = Math.cos(r), sinR = Math.sin(r);
				xn = dx*cosR - dy*sinR + m.mouseHandler.xpos();
				yn = dy*cosR + dx*sinR + m.mouseHandler.ypos();
				GLFW.glfwSetCursorPos(window, xn, yn);
			}
			m.mouseHandler.onMove(window, xn, yn);
		});
	};
	
}
