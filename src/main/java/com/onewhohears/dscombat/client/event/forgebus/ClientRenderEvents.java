package com.onewhohears.dscombat.client.event.forgebus;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.client.renderer.EntityScreenRenderer;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.util.math.UtilAngles;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import java.util.Objects;

import static net.minecraftforge.client.gui.overlay.VanillaGuiOverlay.*;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
public class ClientRenderEvents {
	
	// TODO 4.3 thermal camera option
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void playerRenderPre(RenderPlayerEvent.Pre event) {
		Player player = event.getEntity();
		if (!player.isPassenger()) return;
		if (!(player.getRootVehicle() instanceof EntityVehicle plane)) return;
		Quaternion q = UtilAngles.lerpQ(event.getPartialTick(), plane.getPrevQ(), plane.getClientQ());
		Vec3 eye = new Vec3(0, player.getEyeHeight(), 0);
		Vec3 t = eye.subtract(UtilAngles.rotateVector(eye, q));
		event.getPoseStack().translate(t.x, t.y, t.z);
		event.getPoseStack().mulPose(q);
		float[] relangles = UtilAngles.globalToRelativeDegrees(player.getXRot(), player.getYRot(), q);
		player.setYHeadRot(relangles[1]);
		player.yHeadRotO = relangles[1];
		player.setYBodyRot(relangles[1]);
		player.yBodyRotO = relangles[1];
		// HOW 1 set player head model part x rot to relangles[0]
		// neither of these work
		//event.getRenderer().getModel().head.xRot = (float) Math.toRadians(relangles[0]);
		/*event.getRenderer().getModel().setupAnim((LocalPlayer)player, 0f, 
				0f, 0f, 0f, (float) Math.toRadians(relangles[0]));*/
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void playerRenderPost(RenderPlayerEvent.Post event) {
		Player player = event.getEntity();
		if (!player.isPassenger()) return;
		if (!(player.getRootVehicle() instanceof EntityVehicle plane)) return;
		player.setYHeadRot(player.getYRot());
		player.yHeadRotO = player.getYRot();
		player.setYBodyRot(player.getYRot());
		player.yBodyRotO = player.getYRot();
	}
	
	private static Matrix4f viewMat = new Matrix4f();
	private static Matrix4f projMat = new Matrix4f();
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void getViewMatrices(RenderLevelStageEvent event) {
		if (event.getStage() != Stage.AFTER_TRIPWIRE_BLOCKS) return;
		Minecraft m = Minecraft.getInstance();
		Vec3 view = m.gameRenderer.getMainCamera().getPosition();
		PoseStack poseStack = event.getPoseStack();
		poseStack.pushPose();
		poseStack.translate(-view.x, -view.y, -view.z);
		viewMat = poseStack.last().pose();
		projMat = event.getProjectionMatrix();
		poseStack.popPose();
	}

	// TODO: register our overlays under more IDs in case modders/us in the future need to selectively disable overlays
	// TODO: add some config to allow disabling other mods' overlays
	@SubscribeEvent
	public static void onRenderGui(RenderGuiOverlayEvent.Pre event) {
		if (!(Minecraft.getInstance().player.getRootVehicle() instanceof EntityVehicle vehicle)) return;
		if (DSCClientInputs.isCameraFree()) return;

		if (Objects.equals(event.getOverlay().id(), HOTBAR.id())) event.setCanceled(true);
		if (Objects.equals(event.getOverlay().id(), CROSSHAIR.id())) event.setCanceled(true);
		if (Objects.equals(event.getOverlay().id(), PLAYER_HEALTH.id())) event.setCanceled(true);
		if (Objects.equals(event.getOverlay().id(), ARMOR_LEVEL.id())) event.setCanceled(true);
		if (Objects.equals(event.getOverlay().id(), FOOD_LEVEL.id())) event.setCanceled(true);
		if (Objects.equals(event.getOverlay().id(), EXPERIENCE_BAR.id())) event.setCanceled(true);
		if (Objects.equals(event.getOverlay().id(), ITEM_NAME.id())) event.setCanceled(true);
	}

	@SubscribeEvent
	public static void onRenderHand(RenderHandEvent event) {
		if (!(Minecraft.getInstance().player.getRootVehicle() instanceof EntityVehicle vehicle)) return;
		if (DSCClientInputs.isCameraFree()) return;

		event.setCanceled(true);
	}
	
	public static Matrix4f getViewMatrix() {
		return viewMat;
	}
	
	public static Matrix4f getProjMatrix() {
		return projMat;
	}
	
	@SubscribeEvent
	public static void clientLogout(ClientPlayerNetworkEvent.LoggingOut event) {
		EntityScreenRenderer.clearCache();
	}
	
}
