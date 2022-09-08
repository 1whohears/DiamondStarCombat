package com.onewhohears.dscombat.client.event;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.KeyInit;
import com.onewhohears.dscombat.common.PacketHandler;
import com.onewhohears.dscombat.common.network.ServerBoundFlightControlPacket;
import com.onewhohears.dscombat.entity.EntityAbstractPlane;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.RenderTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
public final class ClientForgeEvents {
	
	private ClientForgeEvents() {}
	
	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void clientTick(ClientTickEvent event) {
		if (event.phase != Phase.START) return;
		final var player = Minecraft.getInstance().player;
		if (player == null) return;
		if (!(player.getRootVehicle() instanceof EntityAbstractPlane plane)) return;
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
	
	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void renderClient(RenderTickEvent event) {
		/*if (event.phase != Phase.START) return;
		final var player = Minecraft.getInstance().player;
		if (player == null) return;
		if (!(player.getRootVehicle() instanceof EntityBasicPlane plane)) return;
		if (plane.getControllingPassenger() != player) return;
		if (plane.isFreeLook()) return;
		float xo = player.getXRot();
		float yo = player.getYRot();
		float xn = -plane.getXRot();
		float yn = -plane.getYRot();
		float xi = xo + (xn - xo) * event.renderTickTime;
		float yi = yo + (yn - yo) * event.renderTickTime;
		player.setXRot(xi);
		player.setYHeadRot(yi);*/
	}
	
	@SubscribeEvent
	public static void playerRender(RenderPlayerEvent.Pre event) {
		//System.out.println("render player");
		Player player = event.getPlayer();
		if (!(player.getRootVehicle() instanceof EntityAbstractPlane plane)) return;
		if (plane.getControllingPassenger() != player) return;
		//System.out.println("player model "+plane.getQ());
		//PoseStack stack = event.getPoseStack();
		//stack.pushPose();
		Quaternion q = UtilAngles.lerpQ(event.getPartialTick(), plane.getPrevQ(), plane.getQ());
		//Quaternion q = plane.getQ();
		//Quaternion q = UtilAngles.toQuaternion(plane.getYRot()-player.getYRot(), plane.getXRot()-player.getXRot(), plane.zRot);
		event.getPoseStack().mulPose(q);
		//stack.mulPose(Vector3f.ZP.rotationDegrees(90));
		/*stack.pushPose();
		stack.popPose();
		stack.popPose();*/
	}
	
	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void cameraSetup(EntityViewRenderEvent.CameraSetup event) {
		final var player = Minecraft.getInstance().player;
		if (player == null) return;
		if (!(player.getRootVehicle() instanceof EntityAbstractPlane plane)) return;
		if (plane.getControllingPassenger() != player) return;
		if (plane.isFreeLook()) return;
		Vec3 pos = event.getCamera().getPosition();
		//event.getCamera().getEntity().setPos(UtilAngles.rotateVector(pos, plane.getQ()));
		event.setRoll(plane.zRot);
	}
	
}
