package com.onewhohears.dscombat.client.event.forgebus;

import java.util.List;

import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.client.input.DSCKeys;
import com.onewhohears.dscombat.client.screen.VehicleMainScreen;
import com.onewhohears.dscombat.common.network.VehicleSyncAction;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toserver.ToServerSeatPos;
import com.onewhohears.dscombat.data.radar.RadarStats.RadarPing;
import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.entity.parts.EntityRidablePart;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.ModSounds;
import com.onewhohears.onewholibs.util.UtilEntity;
import com.onewhohears.onewholibs.util.UtilMCText;

import net.minecraft.Util;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
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
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void clientTickPilotControl(TickEvent.ClientTickEvent event) {
		if (event.phase != Phase.START) return;

		Minecraft mc = Minecraft.getInstance();
		final var player = mc.player;
		if (player == null) return;
		if (!player.isPassenger() || !(player.getRootVehicle() instanceof EntityVehicle vehicle)) return;
		Entity controller = vehicle.getControllingPassenger();
		if (controller == null || !controller.equals(player)) return;

		if (DSCKeys.mouseModeKey.consumeClick()) DSCClientInputs.cycleMouseMode();
		if (DSCKeys.resetMouseKey.isDown()) DSCClientInputs.centerMousePos();
		else if (mc.screen != null) DSCClientInputs.centerMousePos();
		double mouseX = mc.mouseHandler.xpos() - DSCClientInputs.getMouseCenterX();
		double mouseY = -(mc.mouseHandler.ypos() - DSCClientInputs.getMouseCenterY());
		boolean flare = DSCKeys.flareKey.isDown();
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
		boolean type_flip = vehicle.getStats().flipPitchThrottle();
		boolean mode = DSCClientInputs.isCameraLockedForward();
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
		if (vehicle.getStats().ignoreInvertY()) invertY = -1;
		if (DSCClientInputs.isCameraLockedForward()) {
			// FIXME 2.1 fix mouse control mode
			double ya = Math.abs(mouseY);
			double xa = Math.abs(mouseX);
			float ys = (float) Math.signum(mouseY) * -invertY;
			float xs = (float) Math.signum(mouseX);
			double max = Config.CLIENT.mouseModeMaxRadius.get();
			float stickStepsY = Config.CLIENT.mouseYSteps.get();
			float stickStepsX = Config.CLIENT.mouseXSteps.get();
			if (ya >= max) pitch = ys;
			else {
				int step = (int)(ya/max*stickStepsY*ys);
				pitch = ((float)step)/stickStepsY;
			}
			if (xa >= max) roll = xs;
			else {
				int step = (int)(xa/max*stickStepsX*xs);
				roll = ((float)step)/stickStepsX;
			}
			if (mc.mouseHandler.getYVelocity() == 0) {
				DSCClientInputs.setMouseCenterY((int)Mth.approach(
					(float)DSCClientInputs.getMouseCenterY(), 
					(float)mc.mouseHandler.ypos(),
					Config.CLIENT.mouseYReturnRate.get().floatValue()));
			}
			if (mc.mouseHandler.getXVelocity() == 0) {
				DSCClientInputs.setMouseCenterX((int)Mth.approach(
					(float)DSCClientInputs.getMouseCenterX(), 
					(float)mc.mouseHandler.xpos(), 
					Config.CLIENT.mouseXReturnRate.get().floatValue()));
			}
		}
		if (pitchUp && !pitchDown) pitch = -invertY;
		if (pitchDown && !pitchUp) pitch = invertY;
		if (yawLeft && !yawRight) yaw = -1;
		if (yawRight && !yawLeft) yaw = 1;
		if (rollLeft) roll -= 1;
		if (rollRight) roll += 1;
		if (throttleUp) throttle += 1;
		if (throttleDown) throttle -= 1;
		vehicle.inputs.clientPilotControlsToServer(vehicle,
				throttle, pitch, roll, yaw, 
				flare, false, special, special2,
				rollLeft && rollRight,
				DSCClientInputs.isCameraLockedForward());
		if (!DSCClientInputs.isCameraLockedForward()) DSCClientInputs.centerMousePos();
		if (DSCKeys.landingGear.consumeClick()) {
			sendSyncAction(new VehicleSyncAction.LandingGearAction(vehicle.toggleLandingGear()));
		}
	}
	
	private static int leftTicks = 0;
	private static long radarModeUpdateTime = 0;
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void clientTickPassengerControl(TickEvent.ClientTickEvent event) {
		if (event.phase != Phase.START) return;
		Minecraft m = Minecraft.getInstance();
		if (m.mouseHandler.isLeftPressed()) ++leftTicks;
		else leftTicks = 0;
		final var player = m.player;
		if (player == null || !player.isPassenger()) return;
		if (!(player.getVehicle() instanceof EntityRidablePart seat)) return;
		EntityVehicle vehicle = seat.getParentVehicle();
		if (vehicle == null) return;
		boolean isRadarController = player.equals(vehicle.getControllingPlayerOrBot());
		if (DSCClientInputs.disable3rdPersonVehicle) m.options.setCameraType(CameraType.FIRST_PERSON);
		/*
		 * THIS TELLS SERVER WHERE THE SEAT IS IN CASE LAG CAUSES VIOLENCE
		 * HOW 4 the culprit of the seat de-sync issue is net.minecraft.server.level.ChunkMap.TrackedEntity.updatePlayer
		 * sometimes when the server lags the seat position on the server side doesn't get updated with the plane and the player
		 * so the server thinks the seat is outside the player render distance and sends a discard packet to the client
		 * is there a way to fix this without the ToServerSeatPos packet?
		 */
		if (player.tickCount % Config.CLIENT.syncSeatPosRate.get() == 0) {
			PacketHandler.INSTANCE.sendToServer(new ToServerSeatPos(player.getVehicle().position()));
		}
		// SWITCH SEAT
		if (DSCKeys.changeSeat.consumeClick()) {
			sendSyncAction(new VehicleSyncAction.SwitchSeatAction());
		}
		// CYCLE WEAPON
		int selectNextWeapon = 0;
		if (DSCKeys.weaponSelect2Key.consumeClick()) selectNextWeapon = -1;
		else if (DSCKeys.weaponSelectKey.consumeClick()) selectNextWeapon = 1;
		vehicle.weaponSystem.selectNextWeapon(selectNextWeapon);
		// SELECT RADAR PING
		RadarSystem radar = vehicle.radarSystem;
		if (DSCClientInputs.isRadarHovering() && leftTicks == 1) {
			List<RadarPing> pings = radar.getClientRadarPings();
			if (DSCClientInputs.getRadarHoverIndex() < pings.size()) 
				radar.clientSelectTarget(pings.get(DSCClientInputs.getRadarHoverIndex()));
		}
		// CYCLE PING
		if (DSCKeys.pingCycleKey.consumeClick()) radar.clientSelectNextTarget();
		// SHOOT PILOT WEAPON OR TURRET
		if (DSCKeys.shootKey.isDown() && playerCanShoot(player)) {
			sendSyncAction(new VehicleSyncAction.ShootAction(
					vehicle.weaponSystem.getSelectedIndex(),
					radar.getClientSelectedPing(),
					getShootPos(player, vehicle)));
		}
		// DISMOUNT 
		if (Config.CLIENT.customDismount.get() && DSCKeys.dismount.isDown()) {
			sendSyncAction(new VehicleSyncAction.DismountAction());
		}
		// EJECT
		if (DSCKeys.eject.consumeClick()) {
			if (seat.canEject()) {
				seat.useEject();
				sendSyncAction(new VehicleSyncAction.DismountAction(true));
				player.getLevel().playLocalSound(player.getX(), player.getY(), player.getZ(),
						ModSounds.EJECT_WIND, SoundSource.PLAYERS, 0.5f, 1, false);
			} else {
				sendSyncAction(new VehicleSyncAction.DismountAction(false));
			}
		}
		// CYCLE RADAR MODE
		boolean cycleRadarMode = DSCKeys.radarModeKey.consumeClick();
		if (cycleRadarMode) {
			DSCClientInputs.cyclePreferredRadarMode();
			if (!isRadarController) player.displayClientMessage(UtilMCText.translatable("info.dscombat.not_radar_controller"), true);
		}
		if (isRadarController && DSCClientInputs.getPreferredRadarMode() != vehicle.getRadarMode() && Util.getMillis() - radarModeUpdateTime > 500) {
			sendSyncAction(new VehicleSyncAction.SetRadarModeAction(DSCClientInputs.getPreferredRadarMode()));
			radarModeUpdateTime = Util.getMillis();
		}
		// USE GIMBAL
		if (DSCKeys.gimbalKey.consumeClick()) {
			DSCClientInputs.toggleGimbalMode();
		}
		// OPEN VEHICLE MENU
		if (DSCKeys.vehicleMenuKey.consumeClick()) {
			m.setScreen(new VehicleMainScreen());
		}
		// CAMERA LEAN
		boolean leanLeft = DSCKeys.leanLeftKey.consumeClick();
		boolean leanRight = DSCKeys.leanRightKey.consumeClick();
		if (leanLeft && leanRight) DSCClientInputs.leanNot();
		else if (leanLeft) DSCClientInputs.leanLeft();
		else if (leanRight) DSCClientInputs.leanRight();
	}

	public static Vec3 getShootPos(Player player, EntityVehicle vehicle) {
		switch (DSCClientInputs.getTargetMode()) {
            case LOOK -> { return getLookPos(player, vehicle); }
            case COORDS -> {  return Config.CLIENT.getTargetPos(); }
			case INDICATOR -> { return Vec3.ZERO; }
		}
		return Vec3.ZERO;
	}

	public static Vec3 getLookPos(Player player, EntityVehicle vehicle) {
		Entity looker = player;
		if (DSCClientInputs.isGimbalMode()) {
			Entity gimbal = vehicle.getGimbalForPilotCamera();
			if (gimbal != null) {
				looker = gimbal;
				looker.setXRot(player.getXRot());
				looker.setYRot(player.getYRot());
			}
		}
		return UtilEntity.getLookingAtBlockPos(looker, 300);
	}
	
	private static boolean playerCanShoot(Player player) {
		return (System.currentTimeMillis()-DSCClientInputs.getClientMountTime()) > DSCClientInputs.MOUNT_SHOOT_COOLDOWN
				&& (!player.isUsingItem() || player.getItemInHand(player.getUsedItemHand()).is(Items.SHIELD));
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void clientMoveInput(MovementInputUpdateEvent event) {
		Player player = event.getEntity();
		if (!(player.getVehicle() instanceof EntityRidablePart)) return;
		if (Config.CLIENT.customDismount.get()) {
			event.getInput().shiftKeyDown = false;
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void playerMountSeat(EntityMountEvent event) {
		if (!event.isMounting()) return;
		Entity mounting = event.getEntityMounting();
		if (!mounting.level.isClientSide) return;
		if (!(mounting instanceof Player)) return;
		Entity mounted = event.getEntityBeingMounted();
		if (!(mounted instanceof EntityRidablePart)) return;
		DSCClientInputs.setClientMountTime(System.currentTimeMillis());
		DSCClientInputs.leanNot();
	}
	
	@SubscribeEvent
	public static void clientLogin(ClientPlayerNetworkEvent.LoggingIn event) {
		DSCClientInputs.setPreferredRadarMode(Config.CLIENT.defaultRadarMode.get());
	}

	public static void sendSyncAction(VehicleSyncAction action) {
		VehicleSyncAction.sendSyncAction(action);
	}
	
}
