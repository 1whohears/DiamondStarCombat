package com.onewhohears.dscombat.client.event;

import java.util.List;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.KeyInit;
import com.onewhohears.dscombat.common.PacketHandler;
import com.onewhohears.dscombat.common.network.ServerBoundFlightControlPacket;
import com.onewhohears.dscombat.data.RadarData;
import com.onewhohears.dscombat.data.RadarData.RadarPing;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.entity.aircraft.parts.EntitySeat;
import com.onewhohears.dscombat.entity.aircraft.parts.EntitySeatCamera;
import com.onewhohears.dscombat.util.math.UtilAngles;
import com.onewhohears.dscombat.util.math.UtilGeometry;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.InputEvent.MouseScrollEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
public final class ClientForgeEvents {
	
	private ClientForgeEvents() {}
	
	@SubscribeEvent
	public static void clientTick(TickEvent.ClientTickEvent event) {
		if (event.phase != Phase.START) return;
		Minecraft m = Minecraft.getInstance();
		mouseX = m.mouseHandler.xpos();
		mouseY = -m.mouseHandler.ypos();
		final var player = m.player;
		if (player == null) return;
		//System.out.println("VEHICLE "+player.getVehicle());
		//System.out.println("ROOT "+player.getRootVehicle());
		if (!(player.getRootVehicle() instanceof EntityAbstractAircraft plane)) return;
		if (plane.getControllingPassenger() != player) return;
		float pitch = 0, roll = 0, yaw = 0;
		boolean throttleUp = KeyInit.throttleUpKey.isDown();
		boolean throttleDown = KeyInit.throttleDownKey.isDown();
		boolean mouseMode = KeyInit.mouseModeKey.isDown();
		boolean flare = KeyInit.flareKey.isDown();
		boolean shoot = KeyInit.shootKey.isDown();
		boolean select = KeyInit.weaponSelectKey.isDown();
		if (plane.isFreeLook()) {
			boolean pitchUp = KeyInit.pitchUpKey.isDown();
			boolean pitchDown = KeyInit.pitchDownKey.isDown();
			boolean yawLeft = KeyInit.rollLeftKey.isDown();
			boolean yawRight = KeyInit.rollRightKey.isDown();
			boolean rollLeft = KeyInit.yawLeftKey.isDown();
			boolean rollRight = KeyInit.yawRightKey.isDown();
			if (pitchUp) pitch += 1;
			if (pitchDown) pitch -= 1;
			if (yawLeft) yaw -= 1;
			if (yawRight) yaw += 1;
			if (rollLeft) roll -= 1;
			if (rollRight) roll += 1;
		} else {
			//System.out.println("x = "+mouseX+" y = "+mouseY);
			double ya = Math.abs(mouseY);
			double xa = Math.abs(mouseX);
			float ys = (float) Math.signum(mouseY);
			float xs = (float) Math.signum(mouseX);
			double md = max-deadZone;
			if (ya > max) 
				pitch = ys;
			else if (ya > deadZone) 
				pitch = (float) ((ya-deadZone) / md) * ys;
			if (xa > max) 
				yaw = xs;
			else if (xa > deadZone) 
				yaw = (float) ((xa-deadZone) / md) * xs;
			boolean rollLeft = KeyInit.rollLeftKey.isDown();
			boolean rollRight = KeyInit.rollRightKey.isDown();
			if (rollLeft) roll -= 1;
			if (rollRight) roll += 1;
		}
		//System.out.println("pitch = "+pitch+" yaw = "+yaw);
		PacketHandler.INSTANCE.sendToServer(new ServerBoundFlightControlPacket(
				throttleUp, throttleDown, 
				pitch, roll, yaw,
				mouseMode, flare, shoot, select));
		plane.updateControls(throttleUp, throttleDown,
				pitch, roll, yaw,
				mouseMode, flare, shoot, select);
		RadarData radar = plane.getRadar();
		if (radar == null) return;
		List<RadarPing> pings = radar.getClientRadarPings();
		//int selected = radar.getSelectedPingIndex();
		boolean hovering = false;
		if (pings != null) {
			//System.out.println("ping size "+pings.size());
			for (int i = 0; i < pings.size(); ++i) {
				RadarPing p = pings.get(i);
				if (isPlayerLookingAtPing(player, p)) {
					hoverIndex = i;
					hovering = true;
					if (leftClick) radar.clientSelectTarget(plane, p);
					break;
				}
			}
		}
		if (!hovering) resetHoverIndex();
		//System.out.println("selected = "+selected);
		//System.out.println("hover index = "+hoverIndex);
		// TODO middle click to mark a spot to target with a position guided missile
	}
	
	private static void resetHoverIndex() {
		hoverIndex = -1;
		//System.out.println("reset hover index");
	}
	
	private static boolean leftClick = false;
	private static boolean rightClick = false;
	private static double mouseX = 0;
	private static double mouseY = 0;
	
	private static final double deadZone = 250;
	private static final double max = 1000;
	
	@SubscribeEvent
	public static void onClickInput(InputEvent.ClickInputEvent event) {
		if (event.isAttack()) {
			//System.out.println("input attack event");
			Minecraft m = Minecraft.getInstance();
			if (m.hitResult.getType() == HitResult.Type.ENTITY) {
				EntityHitResult hit = (EntityHitResult) m.hitResult;
				if (hit.getEntity().equals(m.player)) {
					event.setSwingHand(false);
					event.setCanceled(true);
					//System.out.println("canceled");
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onMouseInput(InputEvent.MouseInputEvent event) {
		//System.out.println("mouse input button "+event.getButton());
		//System.out.println("mouse input action "+event.getAction());
		if (event.getButton() == 0) {
			if (event.getAction() == 1) leftClick = true;
			else leftClick = false;
		}
		if (event.getButton() == 1) {
			if (event.getAction() == 1) rightClick = true;
			else rightClick = false;
		} 
	}
	
	@SubscribeEvent
	public static void onScrollInput(MouseScrollEvent event) {
		//System.out.println(event.getMouseX()+" "+event.getMouseY());
		//System.out.println("mouse scroll "+event.getScrollDelta());
		// TODO scroll to select weapon
	}
	
	private static VertexBuffer pingBuffer;
	private static int colorR, colorG, colorB, colorA;
	private static int hoverIndex = -1;
	
	private static void setDefaultColor() {
		colorR = 0;
		colorG = 255;
		colorB = 0;
		colorA = 255;
	}
	
	private static void setHoverColor() {
		colorR = 255;
		colorG = 255;
		colorB = 0;
		colorA = 255;
	}
	
	private static void setSelectedColor() {
		colorR = 255;
		colorG = 0;
		colorB = 0;
		colorA = 255;
	}
	
	private static boolean isPlayerLookingAtPing(Player player, RadarPing ping) {
		return UtilGeometry.isPointInsideCone(ping.pos.add(0, 0.5, 0), 
				player.getEyePosition(), player.getLookAngle(), 5, 1000);
	}
	
	@SubscribeEvent
	public static void renderLevel(RenderLevelLastEvent event) {
		Minecraft m = Minecraft.getInstance();
		final var player = m.player;
		if (player.getVehicle() instanceof EntitySeat seat 
				&& seat.getVehicle() instanceof EntityAbstractAircraft plane) {
			RadarData radar = plane.getRadar();
			if (radar == null) return;
			List<RadarPing> pings = radar.getClientRadarPings();
			int selected = radar.getClientSelectedPingIndex();
			if (pings == null) return;
			//System.out.println("RADAR PINGS");
			for (int i = 0; i < pings.size(); ++i) {
				RadarPing p = pings.get(i);
				if (i == selected) setSelectedColor();
				else if (i == hoverIndex) setHoverColor(); // TODO display distance when hovering over it
				else setDefaultColor();
				//System.out.println(p.pos);
				Vec3 view = m.gameRenderer.getMainCamera().getPosition();
				double x = p.pos.x, y = p.pos.y+0.02, z = p.pos.z, w2 = 1, w = w2/2;
				
				var tesselator = Tesselator.getInstance();
				var buffer = tesselator.getBuilder();
				if (pingBuffer != null) pingBuffer.close();
				pingBuffer = new VertexBuffer();
				buffer.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
				
				buffer.vertex(x-w, y, z-w).color(colorR, colorG, colorB, colorA).endVertex();
				buffer.vertex(x+w, y, z-w).color(colorR, colorG, colorB, colorA).endVertex();
				
				buffer.vertex(x+w, y, z-w).color(colorR, colorG, colorB, colorA).endVertex();
				buffer.vertex(x+w, y, z+w).color(colorR, colorG, colorB, colorA).endVertex();
				
				buffer.vertex(x+w, y, z+w).color(colorR, colorG, colorB, colorA).endVertex();
				buffer.vertex(x-w, y, z+w).color(colorR, colorG, colorB, colorA).endVertex();
				
				buffer.vertex(x-w, y, z+w).color(colorR, colorG, colorB, colorA).endVertex();
				buffer.vertex(x-w, y, z-w).color(colorR, colorG, colorB, colorA).endVertex();
				
				buffer.vertex(x-w, y, z-w).color(colorR, colorG, colorB, colorA).endVertex();
				buffer.vertex(x-w, y+w2, z-w).color(colorR, colorG, colorB, colorA).endVertex();
				
				buffer.vertex(x+w, y, z-w).color(colorR, colorG, colorB, colorA).endVertex();
				buffer.vertex(x+w, y+w2, z-w).color(colorR, colorG, colorB, colorA).endVertex();
				
				buffer.vertex(x+w, y, z+w).color(colorR, colorG, colorB, colorA).endVertex();
				buffer.vertex(x+w, y+w2, z+w).color(colorR, colorG, colorB, colorA).endVertex();
				
				buffer.vertex(x-w, y, z+w).color(colorR, colorG, colorB, colorA).endVertex();
				buffer.vertex(x-w, y+w2, z+w).color(colorR, colorG, colorB, colorA).endVertex();
				
				buffer.vertex(x-w, y+w2, z-w).color(colorR, colorG, colorB, colorA).endVertex();
				buffer.vertex(x+w, y+w2, z-w).color(colorR, colorG, colorB, colorA).endVertex();
				
				buffer.vertex(x+w, y+w2, z-w).color(colorR, colorG, colorB, colorA).endVertex();
				buffer.vertex(x+w, y+w2, z+w).color(colorR, colorG, colorB, colorA).endVertex();
				
				buffer.vertex(x+w, y+w2, z+w).color(colorR, colorG, colorB, colorA).endVertex();
				buffer.vertex(x-w, y+w2, z+w).color(colorR, colorG, colorB, colorA).endVertex();
				
				buffer.vertex(x-w, y+w2, z+w).color(colorR, colorG, colorB, colorA).endVertex();
				buffer.vertex(x-w, y+w2, z-w).color(colorR, colorG, colorB, colorA).endVertex();
				
				buffer.end();
				pingBuffer.upload(buffer);

				RenderSystem.depthMask(false);
				RenderSystem.disableCull();
				RenderSystem.enableBlend();
				RenderSystem.defaultBlendFunc();
				RenderSystem.disableTexture();
				GL11.glEnable(GL11.GL_LINE_SMOOTH);
				GL11.glEnable(GL11.GL_DEPTH_TEST);

				PoseStack poseStack = event.getPoseStack();
				poseStack.pushPose();
				m.font.draw(poseStack, "TEST", 0, 0, 0xFFFFFF); // TODO how to draw text?
				poseStack.translate(-view.x, -view.y, -view.z);
				var shader = GameRenderer.getPositionColorShader();
				pingBuffer.drawWithShader(poseStack.last().pose(), event.getProjectionMatrix().copy(), shader);
				poseStack.popPose();

				RenderSystem.depthMask(true);
				RenderSystem.disableBlend();
				RenderSystem.enableCull();
				RenderSystem.enableTexture();
			}
		}
	}
	
	/*@SubscribeEvent
	public static void renderOverlay(RenderGameOverlayEvent.Pre event) {
		if (event.getType() != ElementType.ALL) return;
		PoseStack ps = event.getMatrixStack();
	}*/
	
	/*@SubscribeEvent
	public static void renderOverlay(RenderNameplateEvent event) {
		
	}*/
	
	@SubscribeEvent
	public static void playerRender(RenderPlayerEvent.Pre event) {
		//System.out.println("render player");
		Minecraft m = Minecraft.getInstance();
		final var playerC = m.player;
		Player player = event.getPlayer();
		if (player.getVehicle() instanceof EntitySeat seat 
				&& seat.getVehicle() instanceof EntityAbstractAircraft plane) {
			changePlayerHitbox(player);
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
	
	private static void changePlayerHitbox(Player player) {
		double x = player.getX();
		double y = player.getY();
		double z = player.getZ();
		double w = player.getBbWidth()/2;
		player.setBoundingBox(new AABB(x+w, y+0.5d, z+w, x-w, y, z-w)); 
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
			if (m.options.getCameraType() == CameraType.THIRD_PERSON_FRONT) {
				event.setPitch(xi*-1f);
				event.setYaw(yi+180f);
				event.setRoll(zi*-1f);
			} else {
				event.setPitch(xi);
				event.setYaw(yi);
				event.setRoll(zi);
			}
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
	
	/*@SubscribeEvent
	public static void onPlayerAttack(AttackEntityEvent event) {
		System.out.println("attacked entity "+event.getTarget());
		if (event.getTarget().equals(event.getEntity())) {
			System.out.println("canceled");
			event.setCanceled(true);
		}
	}*/
	
	/*@SubscribeEvent
	public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
		System.out.println(event.getEntity()+" interacted with "+event.getTarget());
		if (event.getTarget().equals(event.getEntity())) {
			System.out.println("canceled");
			event.setCanceled(true);
		}
	}*/
	
	@SubscribeEvent
	public static void onEntityJoin(EntityJoinWorldEvent event) {
		Level level = event.getWorld();
		if (!level.isClientSide) return;
		Minecraft m = Minecraft.getInstance();
		Entity entity = event.getEntity();
		//System.out.println("entity joined client "+entity);
		if (entity instanceof Player player) {
			if (m.player.equals(player)) {
				leftClick = false;
				rightClick = false;
			}
		}
	}
	
}
