package com.onewhohears.dscombat.client.event;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.KeyInit;
import com.onewhohears.dscombat.common.PacketHandler;
import com.onewhohears.dscombat.common.network.ServerBoundFlightControlPacket;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.entity.aircraft.EntitySeat;
import com.onewhohears.dscombat.entity.aircraft.EntitySeatCamera;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
public final class ClientForgeEvents {
	
	private ClientForgeEvents() {}
	
	@SubscribeEvent
	public static void clientTick(ClientTickEvent event) {
		if (event.phase != Phase.START) return;
		Minecraft m = Minecraft.getInstance();
		final var player = m.player;
		if (player == null) return;
		//System.out.println("VEHICLE "+player.getVehicle());
		//System.out.println("ROOT "+player.getRootVehicle());
		if (!(player.getRootVehicle() instanceof EntityAbstractAircraft plane)) return;
		if (plane.getControllingPassenger() != player) return;
		boolean throttleUp = KeyInit.throttleUpKey.isDown();
		boolean throttleDown = KeyInit.throttleDownKey.isDown();
		boolean pitchUp = KeyInit.pitchUpKey.isDown();
		boolean pitchDown = KeyInit.pitchDownKey.isDown();
		boolean rollLeft = KeyInit.rollLeftKey.isDown();
		boolean rollRight = KeyInit.rollRightKey.isDown();
		boolean yawLeft = KeyInit.yawLeftKey.isDown();
		boolean yawRight = KeyInit.yawRightKey.isDown();
		boolean mouseMode = KeyInit.mouseModeKey.isDown();
		boolean flare = KeyInit.flareKey.isDown();
		PacketHandler.INSTANCE.sendToServer(new ServerBoundFlightControlPacket(
				throttleUp, throttleDown, pitchUp, pitchDown, 
				rollLeft, rollRight, yawLeft, yawRight,
				mouseMode, flare));
		plane.updateControls(throttleUp, throttleDown, pitchUp, pitchDown, 
				rollLeft, rollRight, yawLeft, yawRight, mouseMode, flare);
	}
	
	/*@SubscribeEvent
	public static void renderClient(RenderTickEvent event) {
		if (event.phase != Phase.START) return;
		Minecraft m = Minecraft.getInstance();
		final var player = m.player;
		if (player == null) return;
		if (player.getVehicle() instanceof EntitySeat seat 
				&& seat.getVehicle() instanceof EntityAbstractAircraft plane) {
			EntitySeatCamera camera = seat.getCamera();
			float xo, xn, yo, yn;
			if (plane.isFreeLook()) {
				xo = player.xRotO;
				xn = player.getXRot();
				yo = player.yRotO;
				yn = player.getYRot();
			} else {
				xo = plane.prevXRot;
				xn = plane.getXRot();
				yo = plane.prevYRot;
				yn = plane.getYRot();
			}
			float xi = xo + (xn - xo) * event.renderTickTime;
			float yi = yo + (yn - yo) * event.renderTickTime;
			camera.setXRot(xi);
			camera.setYRot(yi);
		}
	}*/
	
	@SubscribeEvent
	public static void playerRender(RenderPlayerEvent.Pre event) {
		//System.out.println("render player");
		Minecraft m = Minecraft.getInstance();
		final var playerC = m.player;
		Player player = event.getPlayer();
		if (player.getVehicle() instanceof EntitySeat seat 
				&& seat.getVehicle() instanceof EntityAbstractAircraft plane) {
			if (player.equals(playerC) &&
					m.options.getCameraType().isFirstPerson()) {
				event.setCanceled(true);
				return;
			}
			Quaternion q = UtilAngles.lerpQ(event.getPartialTick(), plane.getPrevQ(), plane.getQ());
			event.getPoseStack().mulPose(q);
		}
		// TODO player inventory not rendering
	}
	
	@SubscribeEvent
	public static void cameraSetup(EntityViewRenderEvent.CameraSetup event) {
		Minecraft m = Minecraft.getInstance();
		final var player = m.player;
		boolean playerCam = m.getCameraEntity().equals(player);
		if (player == null) return;
		if (player.getVehicle() instanceof EntitySeat seat 
				&& seat.getVehicle() instanceof EntityAbstractAircraft plane) {
			EntitySeatCamera camera = seat.getCamera();
			float xo, xn, yo, yn, zo, zn;
			if (plane.isFreeLook()) {
				xo = player.xRotO;
				//xo = player.getXRot();
				//xo = event.getPitch();
				xn = player.getXRot();
				yo = player.yRotO;
				//yo = player.getYRot();
				//yo = event.getYaw();
				yn = player.getYRot();
				zo = plane.prevZRot;
				//zo = event.getRoll();
				zn = plane.zRot;
			} else {
				xo = plane.prevXRot;
				//xo = event.getPitch();
				xn = plane.getXRot();
				yo = plane.prevYRot;
				//yo = event.getYaw();
				yn = plane.getYRot();
				zo = plane.prevZRot;
				//zo = event.getRoll();
				zn = plane.zRot;
			}
			float xi = xo + (xn - xo) * (float)event.getPartialTicks();
			float yi = yo + (yn - yo) * (float)event.getPartialTicks();
			float zi = zo + (zn - zo) * (float)event.getPartialTicks();
			event.setPitch(xi);
			event.setYaw(yi);
			event.setRoll(zi);
			// TODO third person camera shakes probably because no lerp
			camera.setXRot(xi);
			camera.setYRot(yi);
			player.setXRot(xi);
			player.setYRot(yi);
			if (playerCam) m.setCameraEntity(camera);
		} else {
			if (!playerCam) m.setCameraEntity(player);
		}
	}
	
	@SubscribeEvent
	public static void onClick(InputEvent.ClickInputEvent event) {
		// TODO cancel attack actions/tool uses when not in free look
		if (event.isAttack()) {
			
		}
	}
	
}
