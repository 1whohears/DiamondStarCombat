package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.vehicle.RotableHitbox;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import com.onewhohears.onewholibs.util.math.UtilGeometry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public interface RotableHitboxRenderer {

	public default boolean shouldDrawRotableHitboxes(EntityVehicle entity) {
		if (entity.isInvisible() || entity.getHitboxes().size() == 0) return false;
		Minecraft m = Minecraft.getInstance();
		return !m.showOnlyReducedInfo() && m.getEntityRenderDispatcher().shouldRenderHitBoxes();
	}

	public default void drawRotableHitboxeOutlines(EntityVehicle entity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource) {
		VertexConsumer buff = bufferSource.getBuffer(RenderType.lines());
		Quaternionf q = UtilAngles.lerpQ(partialTicks, entity.getPrevQ(), entity.getClientQ());
		for (RotableHitbox hitbox : entity.getHitboxes()) drawRotableHitboxOutline(hitbox, poseStack, buff, q);
	}

	static final int[] DEFAULT_COLOR = new int[] {188, 85, 41, 255};
	static final int[] DESTROYED_COLOR = new int[] {120, 0, 0, 255};

	private void drawRotableHitboxOutline(RotableHitbox hitbox, PoseStack poseStack, VertexConsumer buff, Quaternionf q) {
		poseStack.pushPose();
		Vec3 trans = UtilAngles.rotateVector(hitbox.getRelPos(), q);
		poseStack.translate(trans.x, trans.y, trans.z);
		Matrix4f m4 = poseStack.last().pose();
		Matrix3f m3 = poseStack.last().normal();
		Vector3f ext = UtilGeometry.convertVector(hitbox.getHitbox().getExtents());
		Vector3f c0 = new Vector3f(ext).rotate(q);
		Vector3f c1 = new Vector3f(ext).mul(-1, 1, 1).rotate(q);
		Vector3f c2 = new Vector3f(ext).mul(1, -1, 1).rotate(q);
		Vector3f c3 = new Vector3f(ext).mul(1, 1, -1).rotate(q);
		Vector3f c4 = new Vector3f(c3).negate();
		Vector3f c5 = new Vector3f(c2).negate();
		Vector3f c6 = new Vector3f(c1).negate();
		Vector3f c7 = new Vector3f(c0).negate();

		int[] color;
		if (hitbox.isDestroyed()) color = DESTROYED_COLOR;
		else color = DEFAULT_COLOR;
		addLine(c0, c1, buff, m4, m3, color);
		addLine(c0, c2, buff, m4, m3, color);
		addLine(c0, c3, buff, m4, m3, color);
		addLine(c1, c5, buff, m4, m3, color);
		addLine(c1, c4, buff, m4, m3, color);
		addLine(c2, c4, buff, m4, m3, color);
		addLine(c2, c6, buff, m4, m3, color);
		addLine(c3, c5, buff, m4, m3, color);
		addLine(c3, c6, buff, m4, m3, color);
		addLine(c4, c7, buff, m4, m3, color);
		addLine(c5, c7, buff, m4, m3, color);
		addLine(c6, c7, buff, m4, m3, color);
		poseStack.popPose();
	}

	private void addLine(Vector3f start, Vector3f end, VertexConsumer buff, Matrix4f m4, Matrix3f m3, int[] color) {
		Vector3f n = new Vector3f(end);
		n.sub(start);
		n.normalize();
		buff.vertex(m4,start.x(),start.y(),start.z())
			.color(color[0],color[1],color[2],color[3])
			.normal(m3,n.x(),n.y(),n.z())
			.endVertex();
		buff.vertex(m4,end.x(),end.y(),end.z())
			.color(color[0],color[1],color[2],color[3])
			.normal(m3,n.x(),n.y(),n.z())
			.endVertex();
	}

}
