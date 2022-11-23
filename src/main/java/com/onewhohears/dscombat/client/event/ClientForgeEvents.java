package com.onewhohears.dscombat.client.event;

import java.util.List;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.KeyInit;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toserver.ServerBoundFlightControlPacket;
import com.onewhohears.dscombat.common.network.toserver.ServerBoundSwitchSeatPacket;
import com.onewhohears.dscombat.data.radar.RadarData.RadarPing;
import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.entity.parts.EntitySeat;
import com.onewhohears.dscombat.entity.parts.EntitySeatCamera;
import com.onewhohears.dscombat.util.math.UtilAngles;
import com.onewhohears.dscombat.util.math.UtilAngles.EulerAngles;
import com.onewhohears.dscombat.util.math.UtilGeometry;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
public final class ClientForgeEvents {
	
	private ClientForgeEvents() {}
	
	@SubscribeEvent
	public static void clientTickPilotControl(TickEvent.ClientTickEvent event) {
		if (event.phase != Phase.START) return;
		Minecraft m = Minecraft.getInstance();
		final var player = m.player;
		if (player == null) return;
		boolean select = KeyInit.weaponSelectKey.consumeClick();
		boolean openMenu = KeyInit.planeMenuKey.consumeClick();
		boolean mouseMode = KeyInit.mouseModeKey.consumeClick();
		boolean gear = KeyInit.landingGear.consumeClick();
		if (!(player.getRootVehicle() instanceof EntityAbstractAircraft plane)) return;
		//System.out.println("controller passenger "+plane.getControllingPassenger());
		if (plane.getControllingPassenger() == null 
				|| !plane.getControllingPassenger().equals(player)) return;
		if (KeyInit.resetMouseKey.isDown()) centerMouse();
		else if (m.screen != null) centerMouse();
		double mouseX = m.mouseHandler.xpos() - mouseCenterX;
		double mouseY = -(m.mouseHandler.ypos() - mouseCenterY);
		//System.out.println("VEHICLE "+player.getVehicle());
		//System.out.println("ROOT "+player.getRootVehicle());
		boolean flare = KeyInit.flareKey.isDown();
		boolean shoot = KeyInit.shootKey.isDown();
		boolean flip = KeyInit.flipControls.isDown();
		float pitch = 0, roll = 0, yaw = 0, throttle = 0;
		boolean pitchUp, pitchDown, yawLeft, yawRight;
		boolean rollLeft, rollRight, throttleUp, throttleDown;
		if (plane.isFreeLook()) flip = !flip;
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
			//System.out.println("x = "+mouseX+" y = "+mouseY);
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
		//System.out.println("pitch = "+pitch+" yaw = "+yaw);
		PacketHandler.INSTANCE.sendToServer(new ServerBoundFlightControlPacket(
				throttle, pitch, roll, yaw,
				mouseMode, flare, shoot, select, openMenu, gear));
		plane.updateControls(throttle, pitch, roll, yaw,
				mouseMode, flare, shoot, select, openMenu);
		if (mouseMode && !plane.isFreeLook()) centerMouse();
	}
	
	@SubscribeEvent
	public static void clientTickPassengerControl(TickEvent.ClientTickEvent event) {
		if (event.phase != Phase.START) return;
		Minecraft m = Minecraft.getInstance();
		final var player = m.player;
		if (player == null) return;
		boolean seat = KeyInit.changeSeat.consumeClick();
		if (!(player.getRootVehicle() instanceof EntityAbstractAircraft plane)) return;
		//boolean shoot = KeyInit.shootKey.isDown();
		if (seat) {
			//System.out.println("CHANGE SEAT BUTTON PRESSED");
			PacketHandler.INSTANCE.sendToServer(new ServerBoundSwitchSeatPacket(plane.getId()));
		}
		RadarSystem radar = plane.radarSystem;
		List<RadarPing> pings = radar.getClientRadarPings();
		boolean hovering = false;
		if (pings != null) {
			//System.out.println("ping size "+pings.size());
			for (int i = 0; i < pings.size(); ++i) {
				RadarPing p = pings.get(i);
				if (isPlayerLookingAtPing(player, p)) {
					hoverIndex = i;
					hovering = true;
					if (m.mouseHandler.isLeftPressed()) radar.clientSelectTarget(p);
					break;
				}
			}
		}
		if (!hovering) resetHoverIndex();
		//System.out.println("hover index = "+hoverIndex);
	}
	
	public static void centerMouse() {
		Minecraft m = Minecraft.getInstance();
		mouseCenterX = m.mouseHandler.xpos();
		mouseCenterY = m.mouseHandler.ypos();
	}
	
	private static double mouseCenterX = 0;
	private static double mouseCenterY = 0;
	
	private static final double deadZone = 250;
	private static final double max = 1000;
	
	private static int hoverIndex = -1;
	
	public static int getHoverIndex() {
		return hoverIndex;
	}
	
	private static void resetHoverIndex() {
		hoverIndex = -1;
		//System.out.println("reset hover index");
	}
	
	@SubscribeEvent
	public static void onAttackEntity(AttackEntityEvent event) {
		if (event.getPhase() != EventPriority.NORMAL) return;
		Minecraft m = Minecraft.getInstance();
		if (event.getTarget().equals(m.player)) {
			event.setCanceled(true);
			//System.out.println("canceled");
		}
	}
	
	@SubscribeEvent
	public static void onScrollInput(InputEvent.MouseScrollingEvent event) {
		//System.out.println(event.getMouseX()+" "+event.getMouseY());
		//System.out.println("mouse scroll "+event.getScrollDelta());
	}
	
	private static VertexBuffer pingBuffer;
	private static int colorR, colorG, colorB, colorA;
	
	private static void setDefaultColor() {
		colorR = 0; colorG = 255; colorB = 0; colorA = 255;
	}
	
	private static void setHoverColor() {
		colorR = 255; colorG = 255; colorB = 0; colorA = 255;
	}
	
	private static void setSelectedColor() {
		colorR = 255; colorG = 0; colorB = 0; colorA = 255;
	}
	
	private static void setFriendlyColor() {
		colorR = 0; colorG = 0; colorB = 255; colorA = 255;
	}
	
	private static final double tan1 = Math.tan(Math.toRadians(1));
	private static final double farDist = 300;
	
	private static boolean isPlayerLookingAtPing(Player player, RadarPing ping) {
		double d = ping.pos.distanceTo(player.position());
		double y = tan1*d;
		if (y < 1) y = 1;
		return UtilGeometry.isPointInsideCone(ping.pos.add(0, 0.5, 0), 
				player.getEyePosition(), player.getLookAngle(), 
				Math.toDegrees(Math.atan2(y, d)), 10000);
	}
	
	@SubscribeEvent
	public static void renderLevelStage(RenderLevelStageEvent event) {
		if (event.getPhase() != EventPriority.NORMAL) return;
		if (event.getStage() != Stage.AFTER_PARTICLES) return;
		Minecraft m = Minecraft.getInstance();
		final var player = m.player;
		if (!(player.getRootVehicle() instanceof EntityAbstractAircraft plane)) return;
		RadarSystem radar = plane.radarSystem;
		if (radar == null) return;
		List<RadarPing> pings = radar.getClientRadarPings();
		int selected = radar.getClientSelectedPingIndex();
		if (pings == null) return;
		//System.out.println("RADAR PINGS");
		RenderSystem.depthMask(false);
		RenderSystem.disableCull();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableTexture();
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		for (int i = 0; i < pings.size(); ++i) {
			RadarPing p = pings.get(i);
			//System.out.println(i+" "+p);
			if (i == selected) setSelectedColor();
			else if (i == hoverIndex) setHoverColor();
			else if (p.isFriendly) setFriendlyColor();
			else setDefaultColor();
			
			var tesselator = Tesselator.getInstance();
			var buffer = tesselator.getBuilder();
			if (pingBuffer != null) pingBuffer.close();
			pingBuffer = new VertexBuffer();
			buffer.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
			
			double x = p.pos.x, y = p.pos.y+0.02, z = p.pos.z;
			double d = p.pos.distanceTo(plane.position());
			if (d < farDist) closeBox(buffer, x, y, z, d);
			else farSquare(buffer, x, y, z, player);
			
			pingBuffer.bind();
			pingBuffer.upload(buffer.end());
			
			Vec3 view = m.gameRenderer.getMainCamera().getPosition();
			PoseStack poseStack = event.getPoseStack();
			poseStack.pushPose();
			poseStack.translate(-view.x, -view.y, -view.z);
			var shader = GameRenderer.getPositionColorShader();
			pingBuffer.drawWithShader(poseStack.last().pose(), event.getProjectionMatrix().copy(), shader);
			poseStack.popPose();
			
			VertexBuffer.unbind();
		}
		RenderSystem.depthMask(true);
		RenderSystem.disableBlend();
		RenderSystem.enableCull();
		RenderSystem.enableTexture();
	}
	
	private static void farSquare(BufferBuilder buffer, double x, double y, double z, Player player) {
		Vec3 pp = player.position();
		Vec3 tp = new Vec3(x, y, z);
		Vec3 nn = tp.subtract(pp).normalize();
		Vec3 np = nn.scale(farDist).add(pp);
		closeBox(buffer, np.x, np.y, np.z, farDist);
	}
	
	private static void closeBox(BufferBuilder buffer, double x, double y, double z, double d) {
		double w2 = tan1*d;
		if (w2 < 1) w2 = 1;
		double w = w2/2;
		
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
	}
	
	@SubscribeEvent
	public static void playerRender(RenderPlayerEvent.Pre event) {
		if (event.getPhase() != EventPriority.NORMAL) return;
		Minecraft m = Minecraft.getInstance();
		final var playerC = m.player;
		Player player = event.getEntity();
		if (player.getVehicle() instanceof EntitySeat seat 
				&& seat.getVehicle() instanceof EntityAbstractAircraft plane) {
			changePlayerHitbox(player);
			if (player.equals(playerC) && m.options.getCameraType().isFirstPerson()) {
				event.setCanceled(true);
				return;
			}
			//System.out.println("RENDER_PLAYER");
			Quaternion q = UtilAngles.lerpQ(event.getPartialTick(), plane.getPrevQ(), plane.getClientQ());
			event.getPoseStack().mulPose(q);
			// TODO player looks in wrong direction sometimes
			EulerAngles a = UtilAngles.toDegrees(q);
			player.setYBodyRot(player.getYRot()-(float)a.yaw);
			player.setYHeadRot(player.getYRot()-(float)a.yaw);
			//System.out.println("prx = "+player.getXRot()+" pry = "+player.getYRot());
			//System.out.println("plane rot = "+a);
			/*PlayerRenderer pr = event.getRenderer();
			PlayerModel<AbstractClientPlayer> model = pr.getModel();
			System.out.println("hrx1 = "+Math.toDegrees(model.getHead().xRot)+" hry1 = "+Math.toDegrees(model.getHead().yRot));
			float x = (float)Math.toRadians(player.getXRot()-(float)a.pitch);
			float y = (float)Math.toRadians(player.getYRot()-(float)a.yaw);
			//float z = (float)Math.toRadians(plane.zRot);
			model.getHead().setRotation(x, y, 0);
			System.out.println("hrx2 = "+Math.toDegrees(model.getHead().xRot)+" hry2 = "+Math.toDegrees(model.getHead().yRot));*/
		}
	}
	
	private static void changePlayerHitbox(Player player) {
		double x = player.getX();
		double y = player.getY();
		double z = player.getZ();
		double w = player.getBbWidth()/2;
		player.setBoundingBox(new AABB(x+w, y+0.5d, z+w, x-w, y, z-w)); 
	}
	
	private static Entity prevCamera;
	
	@SubscribeEvent
	public static void cameraSetup(ViewportEvent.ComputeCameraAngles event) {
		if (event.getPhase() != EventPriority.NORMAL) return;
		Minecraft m = Minecraft.getInstance();
		final var player = m.player;
		if (player == null) return;
		// TODO third person looks feels janky
		prevCamera = m.getCameraEntity();
		if (player.getVehicle() instanceof EntitySeat seat 
				&& seat.getVehicle() instanceof EntityAbstractAircraft plane) {
			EntitySeatCamera camera = seat.getCamera();
			float xo, xn, yo, yn, zo, zn;
			if (!plane.isFreeLook() && plane.getControllingPassenger() != null 
					&& plane.getControllingPassenger().equals(player)) {
				xo = plane.prevXRot;
				xn = plane.getXRot();
				yo = plane.prevYRot;
				yn = plane.getYRot();
				zo = plane.prevZRot;
				zn = plane.zRot;
			} else {
				xo = player.xRotO;
				xn = player.getXRot();
				yo = player.yRotO;
				yn = player.getYRot();
				zo = plane.prevZRot;
				zn = plane.zRot;
			}
			float xi = xo + (xn - xo) * (float)event.getPartialTick();
			float yi = yo + (yn - yo) * (float)event.getPartialTick();
			float zi = zo + (zn - zo) * (float)event.getPartialTick();
			if (m.options.getCameraType() == CameraType.THIRD_PERSON_FRONT) {
				event.setPitch(xi*-1f);
				event.setYaw(yi+180f);
				event.setRoll(zi*-1f);
			} else {
				event.setPitch(xi);
				event.setYaw(yi);
				event.setRoll(zi);
			}
			if (camera != null) {
				camera.setXRot(xi);
				camera.setYRot(yi);
				if (!prevCamera.equals(camera)) m.setCameraEntity(camera);
			}
			player.setXRot(xi);
			player.setYRot(yi);
		} else {
			if (!prevCamera.equals(player)) m.setCameraEntity(player);
		}
	}
	
}
