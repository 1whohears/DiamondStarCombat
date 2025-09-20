package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.onewhohears.onewholibs.util.math.Mat3f;
import com.onewhohears.onewholibs.util.math.Mat4f;
import com.onewhohears.onewholibs.util.math.QuaternionF;
import com.onewhohears.onewholibs.util.math.Vec3f;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.vehicle.hitbox.RotableHitbox;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import com.onewhohears.onewholibs.util.math.UtilGeometry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec3;

import static com.onewhohears.dscombat.util.UtilRender.drawLine;

public interface RotableHitboxRenderer {
	
	default boolean shouldDrawRotableHitboxes(EntityVehicle entity) {
		if (entity.isInvisible() || entity.getHitboxes().isEmpty()) return false;
		Minecraft m = Minecraft.getInstance();
		return !m.showOnlyReducedInfo() && m.getEntityRenderDispatcher().shouldRenderHitBoxes();
	}
	
	default void drawRotableHitboxeOutlines(EntityVehicle entity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource) {
		VertexConsumer buff = bufferSource.getBuffer(RenderType.lines());
		QuaternionF q = UtilAngles.lerpQ(partialTicks, entity.getPrevQ(), entity.getClientQ());
		for (RotableHitbox hitbox : entity.getHitboxes()) drawRotableHitboxOutline(hitbox, poseStack, buff, q);
	}
	
	int[] DEFAULT_COLOR = new int[] {188, 85, 41, 255};
	int[] DESTROYED_COLOR = new int[] {120, 0, 0, 255};
	
	private void drawRotableHitboxOutline(RotableHitbox hitbox, PoseStack poseStack, VertexConsumer buff, QuaternionF q) {
		poseStack.pushPose();
		Vec3 trans = UtilAngles.rotateVector(hitbox.getRelPos(), q);
		poseStack.translate(trans.x, trans.y, trans.z);
        Mat4f m4 = Mat4f.from(poseStack.last().pose());
        Mat3f m3 = Mat3f.from(poseStack.last().normal());
		Vec3f ext = UtilGeometry.convertVector(hitbox.getHitbox().getExtents());
		Vec3f c0 = ext.copy(); c0.transform(q);
		Vec3f c1 = ext.copy(); c1.mul(-1,1,1); c1.transform(q);
		Vec3f c2 = ext.copy(); c2.mul(1,-1,1); c2.transform(q);
		Vec3f c3 = ext.copy(); c3.mul(1,1,-1); c3.transform(q);
		Vec3f c4 = c3.copy(); c4.mul(-1);
		Vec3f c5 = c2.copy(); c5.mul(-1);
		Vec3f c6 = c1.copy(); c6.mul(-1);
		Vec3f c7 = c0.copy(); c7.mul(-1);
		int[] color;
		if (hitbox.isDestroyed()) color = DESTROYED_COLOR;
		else color = DEFAULT_COLOR;
		drawLine(c0, c1, buff, m4, m3, color);
		drawLine(c0, c2, buff, m4, m3, color);
		drawLine(c0, c3, buff, m4, m3, color);
		drawLine(c1, c5, buff, m4, m3, color);
		drawLine(c1, c4, buff, m4, m3, color);
		drawLine(c2, c4, buff, m4, m3, color);
		drawLine(c2, c6, buff, m4, m3, color);
		drawLine(c3, c5, buff, m4, m3, color);
		drawLine(c3, c6, buff, m4, m3, color);
		drawLine(c4, c7, buff, m4, m3, color);
		drawLine(c5, c7, buff, m4, m3, color);
		drawLine(c6, c7, buff, m4, m3, color);
		poseStack.popPose();
	}
	

	
}
