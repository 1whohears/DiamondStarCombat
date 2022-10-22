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
import com.onewhohears.dscombat.data.radar.RadarData.RadarPing;
import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.entity.parts.EntitySeat;
import com.onewhohears.dscombat.entity.parts.EntitySeatCamera;
import com.onewhohears.dscombat.util.math.UtilAngles;
import com.onewhohears.dscombat.util.math.UtilGeometry;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
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
		final var player = m.player;
		if (player == null) return;
		if (KeyInit.resetMouseKey.isDown()) centerMouse();
		else if (m.screen != null) {
			//System.out.println("screen = "+m.screen);
			centerMouse();
			/*if (m.screen.isPauseScreen()) centerMouse();
			else if (m.screen instanceof ChatScreen i) centerMouse();
			else if (m.screen instanceof InventoryScreen i) centerMouse();
			else if (m.screen instanceof CreativeModeInventoryScreen i) centerMouse();*/
			//else if (m.screen instanceof BookViewScreen i) centerMouse(m);
		} 
		double mouseX = m.mouseHandler.xpos() - mouseCenterX;
		double mouseY = -(m.mouseHandler.ypos() - mouseCenterY);
		//System.out.println("VEHICLE "+player.getVehicle());
		//System.out.println("ROOT "+player.getRootVehicle());
		if (!(player.getRootVehicle() instanceof EntityAbstractAircraft plane)) return;
		if (plane.getControllingPassenger() != player) return;
		boolean mouseMode = KeyInit.mouseModeKey.consumeClick();
		boolean flare = KeyInit.flareKey.isDown();
		boolean shoot = KeyInit.shootKey.isDown();
		boolean select = KeyInit.weaponSelectKey.consumeClick();
		boolean openMenu = KeyInit.planeMenuKey.consumeClick();
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
				mouseMode, flare, shoot, select, openMenu));
		plane.updateControls(throttle, pitch, roll, yaw,
				mouseMode, flare, shoot, select, openMenu);
		if (mouseMode && !plane.isFreeLook()) centerMouse();
		RadarSystem radar = plane.radarSystem;
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
					if (m.mouseHandler.isLeftPressed()) radar.clientSelectTarget(p);
					break;
				}
			}
		}
		if (!hovering) resetHoverIndex();
		//System.out.println("selected = "+selected);
		//System.out.println("hover index = "+hoverIndex);
		// TODO middle click to mark a spot to target with a position guided missile
		// TODO share selected target with team members button
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
		Minecraft m = Minecraft.getInstance();
		/*if (m.hitResult.getType() == HitResult.Type.ENTITY) {
			EntityHitResult hit = (EntityHitResult) m.hitResult;
			if (hit.getEntity().equals(m.player)) {
				event.setCanceled(true);
				//System.out.println("canceled");
			}
		}*/
		if (event.getTarget().equals(m.player)) {
			event.setCanceled(true);
			//System.out.println("canceled");
		}
	}
	
	@SubscribeEvent
	public static void onScrollInput(InputEvent.MouseScrollingEvent event) {
		//System.out.println(event.getMouseX()+" "+event.getMouseY());
		//System.out.println("mouse scroll "+event.getScrollDelta());
		// TODO scroll to select weapon
	}
	
	private static VertexBuffer pingBuffer;
	private static int colorR, colorG, colorB, colorA;
	
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
	
	private static void setFriendlyColor() {
		colorR = 0;
		colorG = 0;
		colorB = 255;
		colorA = 255;
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
		Minecraft m = Minecraft.getInstance();
		final var player = m.player;
		if (player.getVehicle() instanceof EntitySeat seat 
				&& seat.getVehicle() instanceof EntityAbstractAircraft plane) {
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
				
				double x = p.pos.x, y = p.pos.y+0.02, z = p.pos.z;
				double d = p.pos.distanceTo(plane.position());
				
				var tesselator = Tesselator.getInstance();
				var buffer = tesselator.getBuilder();
				if (pingBuffer != null) pingBuffer.close();
				pingBuffer = new VertexBuffer();
				buffer.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
				
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
	
	/*@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void renderOverlay(RenderGuiOverlayEvent.Post event) {
		Minecraft m = Minecraft.getInstance();
		if (m.options.hideGui) return;
		if (m.gameMode.getPlayerMode() == GameType.SPECTATOR) return;
		final var player = m.player;
		if (!(player.getRootVehicle() instanceof EntityAbstractAircraft plane)) return;
		RadarSystem radar = plane.radarSystem;
		List<RadarPing> pings = radar.getClientRadarPings();
		if (pings.size() == 0) return;
		int selected = radar.getClientSelectedPingIndex();
		int w = event.getWindow().getScreenWidth();
		int h = event.getWindow().getScreenHeight();
		int r = 200;
		System.out.println("w = "+w+" h = "+h);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glPushMatrix();
		GL11.glColor4f(0.6F, 0.6F, 0.6F, 0.3F);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glVertex2f(w/2, h/2);
		for( int n = 0; n <= 18; n++ )
        {
            float t = 2 * 3.14152f * (float)n / (float)18;
            GL11.glVertex2d(w + Math.sin(t) * r, h + Math.cos(t) * r);
        }
        GL11.glRectd(w, h, w+100, h+200);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
		for (int i = 0; i < pings.size(); ++i) {
			RadarPing p = pings.get(i);
			if (i == selected) setSelectedColor();
			else if (i == hoverIndex) setHoverColor();
			else if (p.isFriendly) setFriendlyColor();
			else setDefaultColor();
			
		}
	}*/
	
	@SubscribeEvent
	public static void playerRender(RenderPlayerEvent.Pre event) {
		//System.out.println("render player");
		Minecraft m = Minecraft.getInstance();
		final var playerC = m.player;
		Player player = event.getEntity();
		if (player.getVehicle() instanceof EntitySeat seat 
				&& seat.getVehicle() instanceof EntityAbstractAircraft plane) {
			changePlayerHitbox(player);
			if (player.equals(playerC) &&
					m.options.getCameraType().isFirstPerson()) {
				event.setCanceled(true);
				return;
			}
			//float x = player.getXRot(), y = player.getYRot();
			/*if (!plane.isFreeLook()) {
				player.setXRot(0);
				player.setYRot(0);
			}*/
			Quaternion q = UtilAngles.lerpQ(event.getPartialTick(), plane.getPrevQ(), plane.getQ());
			event.getPoseStack().mulPose(q);
			// TODO player looks in wrong direction
			//System.out.println("plane zRot = "+plane.zRot);
			//event.getPoseStack().mulPose(Vector3f.ZN.rotation((float)Math.toRadians(plane.zRot)));
		}
	}
	
	private static void changePlayerHitbox(Player player) {
		double x = player.getX();
		double y = player.getY();
		double z = player.getZ();
		double w = player.getBbWidth()/2;
		player.setBoundingBox(new AABB(x+w, y+0.5d, z+w, x-w, y, z-w)); 
	}
	
	@SubscribeEvent
	public static void cameraSetup(ViewportEvent.ComputeCameraAngles event) {
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
			// TODO third person camera shakes probably because no lerp
			if (camera != null) {
				camera.setXRot(xi);
				camera.setYRot(yi);
				if (playerCam) m.setCameraEntity(camera);
			}
			player.setXRot(xi);
			player.setYRot(yi);
		} else {
			if (!playerCam) m.setCameraEntity(player);
		}
	}
	
}
