package com.onewhohears.dscombat.client.event.forgebus;

import java.util.List;

import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.DSCKeys;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toserver.ToServerAircraftControl;
import com.onewhohears.dscombat.common.network.toserver.ToServerDismount;
import com.onewhohears.dscombat.common.network.toserver.ToServerSeatPos;
import com.onewhohears.dscombat.common.network.toserver.ToServerShootTurret;
import com.onewhohears.dscombat.common.network.toserver.ToServerSwitchSeat;
import com.onewhohears.dscombat.data.radar.RadarData.RadarPing;
import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.entity.parts.EntitySeat;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.util.math.UtilGeometry;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
public final class ClientInputEvents {
	
	private static double mouseCenterX = 0;
	private static double mouseCenterY = 0;
	
	private static final double tan1 = Math.tan(Math.toRadians(1));
	
	private static int hoverIndex = -1;
	
	private static final long MOUNT_SHOOT_COOLDOWN = 500;
	private static long mountTime;
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void clientTickPilotControl(TickEvent.ClientTickEvent event) {
		if (event.phase != Phase.END) return;
		Minecraft m = Minecraft.getInstance();
		final var player = m.player;
		if (player == null) return;
		int select = 0;
		if (DSCKeys.weaponSelect2Key.consumeClick()) select = -1;
		else if (DSCKeys.weaponSelectKey.consumeClick()) select = 1;
		boolean openMenu = DSCKeys.planeMenuKey.consumeClick();
		boolean mouseMode = DSCKeys.mouseModeKey.consumeClick();
		boolean gear = DSCKeys.landingGear.consumeClick();
		boolean radarMode = DSCKeys.radarModeKey.consumeClick();
		if (!player.isPassenger()) return;
		if (!(player.getRootVehicle() instanceof EntityAircraft plane)) return;
		Entity controller = plane.getControllingPassenger();
		if (controller == null || !controller.equals(player)) return;
		if (DSCKeys.resetMouseKey.isDown()) centerMouse();
		else if (m.screen != null) centerMouse();
		double mouseX = m.mouseHandler.xpos() - mouseCenterX;
		double mouseY = -(m.mouseHandler.ypos() - mouseCenterY);
		boolean flare = DSCKeys.flareKey.isDown();
		boolean shoot = DSCKeys.shootKey.isDown() 
						&& (System.currentTimeMillis()-mountTime) > MOUNT_SHOOT_COOLDOWN 
						&& !player.isUsingItem();
		boolean flip = DSCKeys.flipControlsKey.isDown();
		boolean special = DSCKeys.specialKey.isDown();
		boolean special2 = DSCKeys.special2Key.isDown();
		float pitch = 0, roll = 0, yaw = 0, throttle = 0;
		boolean pitchUp, pitchDown, yawLeft, yawRight;
		boolean rollLeft, rollRight, throttleUp, throttleDown;
		if (!plane.isFreeLook()) {
			pitchUp = DSCKeys.throttleUpKey.isDown();
			pitchDown = DSCKeys.throttleDownKey.isDown();
			throttleUp = DSCKeys.pitchUpKey.isDown();
			throttleDown = DSCKeys.pitchDownKey.isDown();
			
			yawLeft = DSCKeys.yawLeftKey.isDown();
			yawRight = DSCKeys.yawRightKey.isDown();
			rollLeft = DSCKeys.rollLeftKey.isDown();
			rollRight = DSCKeys.rollRightKey.isDown();
		} else if (flip) {
			pitchUp = DSCKeys.throttleUpKey.isDown();
			pitchDown = DSCKeys.throttleDownKey.isDown();
			throttleUp = DSCKeys.pitchUpKey.isDown();
			throttleDown = DSCKeys.pitchDownKey.isDown();
			
			yawLeft = DSCKeys.rollLeftKey.isDown();
			yawRight = DSCKeys.rollRightKey.isDown();
			rollLeft = DSCKeys.yawLeftKey.isDown();
			rollRight = DSCKeys.yawRightKey.isDown();
		} else {
			pitchUp = DSCKeys.pitchUpKey.isDown();
			pitchDown = DSCKeys.pitchDownKey.isDown();
			throttleUp = DSCKeys.throttleUpKey.isDown();
			throttleDown = DSCKeys.throttleDownKey.isDown();
			
			yawLeft = DSCKeys.yawLeftKey.isDown();
			yawRight = DSCKeys.yawRightKey.isDown();
			rollLeft = DSCKeys.rollLeftKey.isDown();
			rollRight = DSCKeys.rollRightKey.isDown();
		}
		int invertY = Config.CLIENT.invertY.get() ? -1 : 1;
		if (plane.ignoreInvertY()) invertY = -1;
		if (!plane.isFreeLook()) {
			// FIXME 1 mouse mode sucks. check how other mods do it
			double ya = Math.abs(mouseY);
			double xa = Math.abs(mouseX);
			float ys = (float) Math.signum(mouseY);
			float xs = (float) Math.signum(mouseX);
			double deadZone = Config.CLIENT.mouseStickDeadzoneRadius.get();
			double max = Config.CLIENT.mouseModeMaxRadius.get();
			double md = max-deadZone;
			if (ya > max) {
				pitch = ys * invertY;
				mouseCenterY -= (ya - max) * ys;
			} else if (ya > deadZone) 
				pitch = (float) ((ya-deadZone) / md) * ys * invertY;
			if (xa > max) {
				roll = xs;
				mouseCenterX += (xa - max) * xs;
			} else if (xa > deadZone) 
				roll = (float) ((xa-deadZone) / md) * xs;
		}
		if (pitchUp && !pitchDown) pitch = -1 * invertY;
		if (pitchDown && !pitchUp) pitch = 1 * invertY;
		if (yawLeft && !yawRight) yaw = -1;
		if (yawRight && !yawLeft) yaw = 1;
		if (rollLeft) roll -= 1;
		if (rollRight) roll += 1;
		if (throttleUp) throttle += 1;
		if (throttleDown) throttle -= 1;
		plane.inputs.update(throttle, pitch, roll, yaw, 
				flare, shoot, openMenu, special, special2, 
				rollLeft && rollRight);
		plane.weaponSystem.selectNextWeapon(select);
		if (radarMode) plane.cycleRadarMode();
		if (mouseMode) plane.toggleFreeLook();
		if (gear) plane.toggleLandingGear();
		PacketHandler.INSTANCE.sendToServer(new ToServerAircraftControl(plane));
		if (mouseMode && !plane.isFreeLook()) centerMouse();
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void clientTickPassengerControl(TickEvent.ClientTickEvent event) {
		if (event.phase != Phase.END) return;
		Minecraft m = Minecraft.getInstance();
		final var player = m.player;
		if (player == null || !player.isPassenger()) return;
		if (!(player.getRootVehicle() instanceof EntityAircraft plane)) return;
		// TELL SERVER WHERE THE SEAT IS INCASE LAG CAUSES VIOLENCE
		if (player.tickCount % 40 == 0) {
			PacketHandler.INSTANCE.sendToServer(new ToServerSeatPos(player.getVehicle().position()));
		}
		// SWITCH SEAT
		if (DSCKeys.changeSeat.consumeClick()) {
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
		// CYCLE PING
		if (DSCKeys.pingCycleKey.consumeClick()) radar.clientSelectNextTarget();
		// TURRET SHOOT
		boolean shoot = DSCKeys.shootKey.isDown();
		if (shoot && player.getVehicle() instanceof EntityTurret turret) {
			PacketHandler.INSTANCE.sendToServer(new ToServerShootTurret(turret));
		}
		// DISMOUNT 
		if (Config.CLIENT.customDismount.get() && DSCKeys.dismount.isDown()) {
			PacketHandler.INSTANCE.sendToServer(new ToServerDismount());
		}
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void clientMoveInput(MovementInputUpdateEvent event) {
		Player player = event.getEntity();
		if (!(player.getVehicle() instanceof EntitySeat plane)) return;
		if (Config.CLIENT.customDismount.get()) {
			event.getInput().shiftKeyDown = false;
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
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void playerMountSeat(EntityMountEvent event) {
		if (!event.isMounting()) return;
		Entity mounting = event.getEntityMounting();
		if (!mounting.level.isClientSide) return;
		if (!(mounting instanceof Player)) return;
		Entity mounted = event.getEntityBeingMounted();
		if (!(mounted instanceof EntitySeat)) return;
		mountTime = System.currentTimeMillis();
	}
	
}
