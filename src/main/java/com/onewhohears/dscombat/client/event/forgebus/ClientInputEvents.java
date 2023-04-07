package com.onewhohears.dscombat.client.event.forgebus;

import java.util.List;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.KeyInit;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toserver.ToServerFlightControl;
import com.onewhohears.dscombat.common.network.toserver.ToServerShootTurret;
import com.onewhohears.dscombat.common.network.toserver.ToServerSwitchSeat;
import com.onewhohears.dscombat.data.radar.RadarData.RadarPing;
import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.util.math.UtilGeometry;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
public final class ClientInputEvents {
	
	private static double mouseCenterX = 0;
	private static double mouseCenterY = 0;
	
	private static final double deadZone = 250;
	private static final double max = 1000;
	
	private static final double tan1 = Math.tan(Math.toRadians(1));
	
	private static int hoverIndex = -1;
	
	@SubscribeEvent
	public static void clientTickPilotControl(TickEvent.ClientTickEvent event) {
		if (event.phase != Phase.END) return;
		Minecraft m = Minecraft.getInstance();
		final var player = m.player;
		if (player == null) return;
		boolean select = KeyInit.weaponSelectKey.consumeClick();
		boolean openMenu = KeyInit.planeMenuKey.consumeClick();
		boolean mouseMode = KeyInit.mouseModeKey.consumeClick();
		boolean gear = KeyInit.landingGear.consumeClick();
		boolean radarMode = KeyInit.radarModeKey.consumeClick();
		if (!player.isPassenger()) return;
		if (!(player.getRootVehicle() instanceof EntityAircraft plane)) return;
		Entity controller = plane.getControllingPassenger();
		if (controller == null || !controller.equals(player)) return;
		if (KeyInit.resetMouseKey.isDown()) centerMouse();
		else if (m.screen != null) centerMouse();
		double mouseX = m.mouseHandler.xpos() - mouseCenterX;
		double mouseY = -(m.mouseHandler.ypos() - mouseCenterY);
		boolean flare = KeyInit.flareKey.isDown();
		boolean shoot = KeyInit.shootKey.isDown();
		boolean flip = KeyInit.flipControlsKey.isDown();
		boolean special = KeyInit.specialKey.isDown();
		float pitch = 0, roll = 0, yaw = 0, throttle = 0;
		boolean pitchUp, pitchDown, yawLeft, yawRight;
		boolean rollLeft, rollRight, throttleUp, throttleDown;
		if (!plane.isFreeLook()) flip = !flip;
		if (flip) {
			pitchUp = KeyInit.throttleUpKey.isDown();
			pitchDown = KeyInit.throttleDownKey.isDown();
			yawLeft = KeyInit.rollLeftKey.isDown();
			yawRight = KeyInit.rollRightKey.isDown();
			rollLeft = KeyInit.yawLeftKey.isDown();
			rollRight = KeyInit.yawRightKey.isDown();
			throttleUp = KeyInit.pitchUpKey.isDown();
			throttleDown = KeyInit.pitchDownKey.isDown();
		} else {
			yawLeft = KeyInit.yawLeftKey.isDown();
			yawRight = KeyInit.yawRightKey.isDown();
			pitchUp = KeyInit.pitchUpKey.isDown();
			pitchDown = KeyInit.pitchDownKey.isDown();
			rollLeft = KeyInit.rollLeftKey.isDown();
			rollRight = KeyInit.rollRightKey.isDown();
			throttleUp = KeyInit.throttleUpKey.isDown();
			throttleDown = KeyInit.throttleDownKey.isDown();
		}
		if (!plane.isFreeLook()) {
			double ya = Math.abs(mouseY);
			double xa = Math.abs(mouseX);
			float ys = (float) Math.signum(mouseY);
			float xs = (float) Math.signum(mouseX);
			double md = max-deadZone;
			if (ya > max) {
				pitch = ys;
				mouseCenterY -= (ya - max) * ys;
			} else if (ya > deadZone) 
				pitch = (float) ((ya-deadZone) / md) * ys;
			if (xa > max) {
				yaw = xs;
				mouseCenterX += (xa - max) * xs;
			} else if (xa > deadZone) 
				yaw = (float) ((xa-deadZone) / md) * xs;
		}
		if (pitchUp && !pitchDown) pitch = 1;
		if (pitchDown && !pitchUp) pitch = -1;
		if (yawLeft && !yawRight) yaw = -1;
		if (yawRight && !yawLeft) yaw = 1;
		if (rollLeft) roll -= 1;
		if (rollRight) roll += 1;
		if (throttleUp) throttle += 1;
		if (throttleDown) throttle -= 1;
		PacketHandler.INSTANCE.sendToServer(new ToServerFlightControl(
				throttle, pitch, roll, yaw,
				mouseMode, flare, shoot, select, openMenu, gear, 
				special, radarMode, rollLeft && rollRight));
		plane.updateControls(throttle, pitch, roll, yaw,
				mouseMode, flare, shoot, select, openMenu, 
				special, radarMode, rollLeft && rollRight);
		if (mouseMode && !plane.isFreeLook()) centerMouse();
	}
	
	@SubscribeEvent
	public static void clientTickPassengerControl(TickEvent.ClientTickEvent event) {
		if (event.phase != Phase.END) return;
		Minecraft m = Minecraft.getInstance();
		final var player = m.player;
		if (player == null || !player.isPassenger()) return;
		if (!(player.getRootVehicle() instanceof EntityAircraft plane)) return;
		// DISMOUNT
		
		// SWITCH SEAT
		if (KeyInit.changeSeat.consumeClick()) {
			PacketHandler.INSTANCE.sendToServer(new ToServerSwitchSeat(plane.getId()));
		}
		// SELECT RADAR PING
		RadarSystem radar = plane.radarSystem;
		List<RadarPing> pings = radar.getClientRadarPings();
		boolean hovering = false;
		for (int i = 0; i < pings.size(); ++i) {
			RadarPing p = pings.get(i);
			if (isPlayerLookingAtPing(player, p)) {
				hoverIndex = i;
				hovering = true;
				if (m.mouseHandler.isLeftPressed()) radar.clientSelectTarget(p);
				break;
			}
		}
		if (!hovering) resetHoverIndex();
		// TURRET SHOOT
		boolean shoot = KeyInit.shootKey.isDown();
		if (shoot && player.getVehicle() instanceof EntityTurret turret) {
			PacketHandler.INSTANCE.sendToServer(new ToServerShootTurret(turret));
		}
	}
	
	public static void centerMouse() {
		Minecraft m = Minecraft.getInstance();
		mouseCenterX = m.mouseHandler.xpos();
		mouseCenterY = m.mouseHandler.ypos();
	}
	
	public static int getHoverIndex() {
		return hoverIndex;
	}
	
	private static void resetHoverIndex() {
		hoverIndex = -1;
	}
	
	private static boolean isPlayerLookingAtPing(Player player, RadarPing ping) {
		double d = ping.pos.distanceTo(player.position());
		double y = tan1*d;
		if (y < 1) y = 1;
		return UtilGeometry.isPointInsideCone(ping.pos.add(0, 0.5, 0), 
				player.getEyePosition(), player.getLookAngle(), 
				Math.toDegrees(Math.atan2(y, d)), 100000);
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void onInteractKey(InputEvent.InteractionKeyMappingTriggered event) {
		if (!event.isAttack()) return;
		Minecraft m = Minecraft.getInstance();
		if (m.hitResult.getType() != Type.ENTITY) return;
		EntityHitResult hit = (EntityHitResult) m.hitResult;
		if (hit.getEntity().equals(m.player)) event.setCanceled(true);
	}
	
}
