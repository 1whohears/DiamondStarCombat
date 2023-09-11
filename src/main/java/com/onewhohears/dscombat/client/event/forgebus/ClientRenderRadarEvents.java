package com.onewhohears.dscombat.client.event.forgebus;

import java.util.List;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.radar.RadarData.RadarPing;
import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
public final class ClientRenderRadarEvents {
	
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
	
	private static void test(float x, float y, float z, Matrix4f viewMat, Matrix4f projMat, Minecraft m) {
		System.out.println("\nPING TEST "+x+" "+y+" "+z);
		System.out.println("viewMat = "+viewMat);
		System.out.println("projMat = "+projMat);
		Vector4f clipSpace = new Vector4f(x, y, z, 1f);
		clipSpace.transform(viewMat);
		clipSpace.transform(projMat);
		System.out.println("clipSpace = "+clipSpace);
		if (clipSpace.w() == 0) return;
		Vector3f ndcSpace = new Vector3f(clipSpace);
		ndcSpace.mul(1/clipSpace.w());
		System.out.println("ndcSpace = "+ndcSpace);
		int win_x = (int)((ndcSpace.x()+1f)/2f*m.getWindow().getWidth());
		int win_y = (int)((ndcSpace.y()+1f)/2f*m.getWindow().getHeight());
		System.out.println("win_x = "+win_x);
		System.out.println("win_y = "+win_y);
	}
	
	public static Matrix4f getViewMatrix() {
		return viewMat;
	}
	
	public static Matrix4f getProjMatrix() {
		return projMat;
	}
	
	private static VertexBuffer pingBuffer;
	private static int colorR, colorG, colorB, colorA;
	private static final double farDist = 300;
	private static final double tan1 = Math.tan(Math.toRadians(1));
	
	//@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void renderLevelStage(RenderLevelStageEvent event) {
		// FIXME 2.1 can't see radar pings that are behind blocks
		if (event.getStage() != Stage.AFTER_TRIPWIRE_BLOCKS) return;
		//if (event.getStage() != Stage.AFTER_PARTICLES) return;
		Minecraft m = Minecraft.getInstance();
		final var player = m.player;
		if (player == null || !player.isPassenger()) return;
		if (!(player.getRootVehicle() instanceof EntityAircraft plane)) return;
		RadarSystem radar = plane.radarSystem;
		if (radar == null) return;
		List<RadarPing> pings = radar.getClientRadarPings();
		int selected = radar.getClientSelectedPingIndex();
		if (pings == null) return;
		RenderSystem.depthMask(false);
		RenderSystem.disableCull();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableTexture();
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		PoseStack poseStack = event.getPoseStack();
		Vec3 view = m.gameRenderer.getMainCamera().getPosition();
		var shader = GameRenderer.getPositionColorShader();
		// TODO 6.1 show last known target location
		for (int i = 0; i < pings.size(); ++i) {
			RadarPing p = pings.get(i);
			
			if (i == selected) setSelectedColor();
			else if (i == ClientInputEvents.getHoverIndex()) setHoverColor();
			else if (p.isFriendly) setFriendlyColor();
			else if (p.isShared()) setSharedColor();
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
			
			poseStack.pushPose();
			poseStack.translate(-view.x, -view.y, -view.z);
			Matrix4f viewMat = poseStack.last().pose();
			Matrix4f projMat = event.getProjectionMatrix().copy();
			test((float)x, (float)y, (float)z, viewMat, projMat, m);
			pingBuffer.drawWithShader(viewMat, projMat, shader);
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
	
	private static void setSharedColor() {
		colorR = 102; colorG = 205; colorB = 170; colorA = 255;
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
	
}
