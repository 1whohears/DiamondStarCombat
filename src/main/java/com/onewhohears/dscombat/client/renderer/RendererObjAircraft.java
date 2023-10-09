package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.client.model.obj.ObjAircraftModel;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.entity.aircraft.RotableHitbox;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.world.phys.Vec3;

public class RendererObjAircraft<T extends EntityAircraft> extends RendererObjEntity<T> {

	public RendererObjAircraft(Context ctx, ObjAircraftModel<T> model) {
		super(ctx, model);
	}
	
	private boolean shouldRenderHitboxes(T entity) {
		if (entity.isInvisible() || !entity.isMultipartEntity()) return false;
		Minecraft m = Minecraft.getInstance();
		return !m.showOnlyReducedInfo() && m.getEntityRenderDispatcher().shouldRenderHitBoxes();
	}
	
	@Override
	public void render(T entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int lightmap) {
		super.render(entity, yaw, partialTicks, poseStack, bufferSource, lightmap);
		if (!shouldRenderHitboxes(entity)) return;
		VertexConsumer buff = bufferSource.getBuffer(RenderType.lines());
		Quaternion q = UtilAngles.lerpQ(partialTicks, entity.getPrevQ(), entity.getClientQ());
		for (RotableHitbox hitbox : entity.getHitboxes()) {
			poseStack.pushPose();
			Vec3 trans = hitbox.getRelPos();
			poseStack.translate(trans.x, trans.y, trans.z);
			Matrix4f m4 = poseStack.last().pose();
			Matrix3f m3 = poseStack.last().normal();
			Vec3 ext = hitbox.getHitbox().getExtents();
			Vec3 c0 = UtilAngles.rotateVector(ext, q);
			Vec3 c1 = UtilAngles.rotateVector(ext.multiply(-1, 1, 1), q);
			Vec3 c2 = UtilAngles.rotateVector(ext.multiply(1, -1, 1), q);
			Vec3 c3 = UtilAngles.rotateVector(ext.multiply(1, 1, -1), q);
			Vec3 c4 = c3.scale(-1);
			Vec3 c5 = c2.scale(-1);
			Vec3 c6 = c1.scale(-1);
			Vec3 c7 = c0.scale(-1);
			addLine(entity, c0, c1, buff, m4, m3);
			addLine(entity, c0, c2, buff, m4, m3);
			addLine(entity, c0, c3, buff, m4, m3);
			addLine(entity, c1, c5, buff, m4, m3);
			addLine(entity, c1, c4, buff, m4, m3);
			addLine(entity, c2, c4, buff, m4, m3);
			addLine(entity, c2, c6, buff, m4, m3);
			addLine(entity, c3, c5, buff, m4, m3);
			addLine(entity, c3, c6, buff, m4, m3);
			addLine(entity, c4, c7, buff, m4, m3);
			addLine(entity, c5, c7, buff, m4, m3);
			addLine(entity, c6, c7, buff, m4, m3);
			poseStack.popPose();
		}
	}
	
	private void addLine(T entity, Vec3 start, Vec3 end, VertexConsumer buff, Matrix4f m4, Matrix3f m3) {
		Vec3 n = end.subtract(start).normalize();
		buff.vertex(m4,(float)start.x(),(float)start.y(),(float)start.z())
			.color(188,85,41,255)
			.normal(m3,(float)n.x,(float)n.y,(float)n.z)
			.endVertex();
		buff.vertex(m4,(float)end.x(),(float)end.y(),(float)end.z())
			.color(188,85,41,255)
			.normal(m3,(float)n.x,(float)n.y,(float)n.z)
			.endVertex();
	}

}
