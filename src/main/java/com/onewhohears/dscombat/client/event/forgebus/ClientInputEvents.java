package com.onewhohears.dscombat.client.event.forgebus;

import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.DSCKeys;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toserver.ToServerDismount;
import com.onewhohears.dscombat.common.network.toserver.ToServerSeatPos;
import com.onewhohears.dscombat.common.network.toserver.ToServerShootTurret;
import com.onewhohears.dscombat.common.network.toserver.ToServerSwitchSeat;
import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.entity.parts.EntitySeat;
import com.onewhohears.dscombat.entity.parts.EntityTurret;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
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
	
	private static int hoverIndex = -1;
	
	private static final long MOUNT_SHOOT_COOLDOWN = 500;
	private static long mountTime;
	
	private static double radarDisplayRange = 1000;
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void clientTickPilotControl(TickEvent.ClientTickEvent event) {
		if (event.phase != Phase.END) return;
		Minecraft m = Minecraft.getInstance();
		final var player = m.player;
		if (player == null) return;
		int selectNextWeapon = 0;
		if (DSCKeys.weaponSelect2Key.consumeClick()) selectNextWeapon = -1;
		else if (DSCKeys.weaponSelectKey.consumeClick()) selectNextWeapon = 1;
		boolean openMenu = DSCKeys.planeMenuKey.consumeClick();
		boolean toggleGear = DSCKeys.landingGear.consumeClick();
		boolean cycleRadarMode = DSCKeys.radarModeKey.consumeClick();
		if (!player.isPassenger()) {
			return;
		}
		if (!(player.getRootVehicle() instanceof EntityVehicle plane)) {
			return;
		}
		if (DSCKeys.mouseModeKey.consumeClick()) {
			plane.toggleFreeLook();
		}
		Entity controller = plane.getControllingPassenger();
		if (controller == null || !controller.equals(player)) return;
		if (DSCKeys.resetMouseKey.isDown()) centerMouse();
		else if (m.screen != null) centerMouse();
		double mouseX = m.mouseHandler.xpos() - mouseCenterX;
		double mouseY = -(m.mouseHandler.ypos() - mouseCenterY);
		boolean flare = DSCKeys.flareKey.isDown();
		boolean shoot = DSCKeys.shootKey.isDown() && playerCanShoot(player);
		boolean flip = DSCKeys.flipControlsKey.isDown();
		boolean special = DSCKeys.specialKey.isDown();
		boolean special2 = DSCKeys.special2Key.isDown();
		float pitch = 0, roll = 0, yaw = 0, throttle = 0;
		boolean pitchUp, pitchDown, yawLeft, yawRight;
		boolean rollLeft, rollRight, throttleUp, throttleDown;
		// should yaw/roll flip
		if (flip) {
			yawLeft = DSCKeys.rollLeftKey.isDown();
			yawRight = DSCKeys.rollRightKey.isDown();
			rollLeft = DSCKeys.yawLeftKey.isDown();
			rollRight = DSCKeys.yawRightKey.isDown();
		} else {
			yawLeft = DSCKeys.yawLeftKey.isDown();
			yawRight = DSCKeys.yawRightKey.isDown();
			rollLeft = DSCKeys.rollLeftKey.isDown();
			rollRight = DSCKeys.rollRightKey.isDown();
		}
		// should pitch/throttle flip
		boolean type_flip = plane.getAircraftType().flipPitchThrottle;
		boolean mode = !plane.onlyFreeLook();
		if ((!type_flip && (flip ^ mode)) || (type_flip && !flip)) {
			pitchUp = DSCKeys.throttleUpKey.isDown();
			pitchDown = DSCKeys.throttleDownKey.isDown();
			throttleUp = DSCKeys.pitchUpKey.isDown();
			throttleDown = DSCKeys.pitchDownKey.isDown();
		} else {
			pitchUp = DSCKeys.pitchUpKey.isDown();
			pitchDown = DSCKeys.pitchDownKey.isDown();
			throttleUp = DSCKeys.throttleUpKey.isDown();
			throttleDown = DSCKeys.throttleDownKey.isDown();
		}
		// should invert
		int invertY = Config.CLIENT.invertY.get() ? -1 : 1;
		if (plane.getAircraftType().ignoreInvertY) invertY = -1;
		if (!plane.onlyFreeLook()) {
			// FIXME 2 fix mouse mode
			double ya = Math.abs(mouseY);
			double xa = Math.abs(mouseX);
			float ys = (float) Math.signum(mouseY) * -invertY;
			float xs = (float) Math.signum(mouseX);
			double deadZone = Config.CLIENT.mouseStickDeadzoneRadius.get();
			double max = Config.CLIENT.mouseModeMaxRadius.get();
			double md = max-deadZone;
			// control pitch
			if (ya > max) {
				pitch = ys;
				mouseCenterY -= (ya - max) * ys;
			} else if (ya > deadZone) {
				pitch = (float)((ya-deadZone)/md) * ys;
			}
			if (m.mouseHandler.getYVelocity() == 0) {
				mouseCenterY = (int)Mth.approach((float)mouseCenterY, (float)m.mouseHandler.ypos(), 
						Config.CLIENT.mouseYReturnRate.get().floatValue());
			}
			// control roll
			if (xa > max) {
				roll = xs;
				mouseCenterX += (xa - max) * xs;
			} else if (xa > deadZone) {
				roll = (float)((xa-deadZone)/md) * xs;
			}
			if (m.mouseHandler.getXVelocity() == 0) {
				mouseCenterX = (int)Mth.approach((float)mouseCenterX, (float)m.mouseHandler.xpos(), 
						Config.CLIENT.mouseXReturnRate.get().floatValue());
			}
		}
		if (pitchUp && !pitchDown) pitch = -1 * invertY;
		if (pitchDown && !pitchUp) pitch = 1 * invertY;
		if (yawLeft && !yawRight) yaw = -1;
		if (yawRight && !yawLeft) yaw = 1;
		if (rollLeft) roll -= 1;
		if (rollRight) roll += 1;
		if (throttleUp) throttle += 1;
		if (throttleDown) throttle -= 1;
		plane.inputs.clientUpdateServerControls(plane, 
				throttle, pitch, roll, yaw, 
				flare, shoot, openMenu, special, special2, 
				rollLeft && rollRight, 
				selectNextWeapon, cycleRadarMode, toggleGear);
		if (!plane.onlyFreeLook()) centerMouse();
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void clientTickPassengerControl(TickEvent.ClientTickEvent event) {
		if (event.phase != Phase.END) return;
		Minecraft m = Minecraft.getInstance();
		final var player = m.player;
		if (player == null || !player.isPassenger()) return;
		if (!(player.getRootVehicle() instanceof EntityVehicle plane)) return;
		// TELL SERVER WHERE THE SEAT IS INCASE LAG CAUSES VIOLENCE
		/**
		 * HOW 4 the culprit of the seat desync issue is net.minecraft.server.level.ChunkMap.TrackedEntity.updatePlayer
		 * sometimes when the server lags the seat position on the server side doesn't get updated with the plane and the player
		 * so the server thinks the seat is outside the player render distance and sends a discard packet to the client
		 * is there a way to fix this without the ToServerSeatPos packet?
		 */
		if (player.tickCount % 40 == 0) {
			PacketHandler.INSTANCE.sendToServer(new ToServerSeatPos(player.getVehicle().position()));
		}
		// SWITCH SEAT
		if (DSCKeys.changeSeat.consumeClick()) {
			PacketHandler.INSTANCE.sendToServer(new ToServerSwitchSeat(plane.getId()));
		}
		// SELECT RADAR PING
		RadarSystem radar = plane.radarSystem;
		if (isHovering() && m.mouseHandler.isLeftPressed()) 
			radar.clientSelectTarget(radar.getClientRadarPings().get(getHoverIndex()));
		// CYCLE PING
		if (DSCKeys.pingCycleKey.consumeClick()) radar.clientSelectNextTarget();
		// TURRET SHOOT
		boolean shoot = DSCKeys.shootKey.isDown() && playerCanShoot(player);
		if (shoot && player.getVehicle() instanceof EntityTurret turret) {
			PacketHandler.INSTANCE.sendToServer(new ToServerShootTurret(turret));
		}
		// DISMOUNT 
		if (Config.CLIENT.customDismount.get() && DSCKeys.dismount.isDown()) {
			PacketHandler.INSTANCE.sendToServer(new ToServerDismount());
		}
		// RADAR DISPLAY RANGE
		if (DSCKeys.radarDisplayRangeKey.consumeClick()) {
			if (radarDisplayRange <= 250) radarDisplayRange = 1000;
			else if (radarDisplayRange <= 1000) radarDisplayRange = 2000;
			else if (radarDisplayRange <= 2000) radarDisplayRange = 5000;
			else if (radarDisplayRange <= 5000) radarDisplayRange = 250;
			else radarDisplayRange = 250;
		}
	}
	
	private static boolean playerCanShoot(Player player) {
		return (System.currentTimeMillis()-mountTime) > MOUNT_SHOOT_COOLDOWN 
				&& (!player.isUsingItem() || player.getItemInHand(player.getUsedItemHand()).is(Items.SHIELD));
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void clientMoveInput(MovementInputUpdateEvent event) {
		Player player = event.getEntity();
		if (!(player.getVehicle() instanceof EntitySeat)) return;
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
	
	public static void setHoverIndex(int index) {
		hoverIndex = index;
	}
	
	public static void resetHoverIndex() {
		hoverIndex = -1;
	}
	
	public static boolean isHovering() {
		return hoverIndex != -1;
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
	
	public static double getRadarDisplayRange() {
		return radarDisplayRange;
	}
	
}
