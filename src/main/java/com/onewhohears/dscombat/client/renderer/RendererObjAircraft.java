package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.client.model.obj.ObjAircraftModel;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.entity.aircraft.RotableHitbox;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.world.phys.Vec3;

public class RendererObjAircraft<T extends EntityVehicle> extends RendererObjEntity<T> {

	public RendererObjAircraft(Context ctx, ObjAircraftModel<T> model) {
		super(ctx, model);
	}
	
	private boolean shouldDrawRotableHitboxes(T entity) {
		if (entity.isInvisible() || !entity.isMultipartEntity()) return false;
		Minecraft m = Minecraft.getInstance();
		return !m.showOnlyReducedInfo() && m.getEntityRenderDispatcher().shouldRenderHitBoxes();
	}
	
	@Override
	public void render(T entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int lightmap) {
		super.render(entity, yaw, partialTicks, poseStack, bufferSource, lightmap);
		if (shouldDrawRotableHitboxes(entity)) drawRotableHitboxeOutlines(entity, partialTicks, poseStack, bufferSource);
	}
	
	private void drawRotableHitboxeOutlines(T entity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource) {
		VertexConsumer buff = bufferSource.getBuffer(RenderType.lines());
		Quaternion q = UtilAngles.lerpQ(partialTicks, entity.getPrevQ(), entity.getClientQ());
		for (RotableHitbox hitbox : entity.getHitboxes()) drawRotableHitboxOutline(hitbox, poseStack, buff, q);
	}
	
	private void drawRotableHitboxOutline(RotableHitbox hitbox, PoseStack poseStack, VertexConsumer buff, Quaternion q) {
		poseStack.pushPose();
		Vec3 trans = UtilAngles.rotateVector(hitbox.getRelPos(), q);
		poseStack.translate(trans.x, trans.y, trans.z);
		Matrix4f m4 = poseStack.last().pose();
		Matrix3f m3 = poseStack.last().normal();
		Vector3f ext = hitbox.getHitbox().getExtents();
		Vector3f c0 = ext.copy(); c0.transform(q);
		Vector3f c1 = ext.copy(); c1.mul(-1,1,1); c1.transform(q);
		Vector3f c2 = ext.copy(); c2.mul(1,-1,1); c2.transform(q);
		Vector3f c3 = ext.copy(); c3.mul(1,1,-1); c3.transform(q);
		Vector3f c4 = c3.copy(); c4.mul(-1);
		Vector3f c5 = c2.copy(); c5.mul(-1);
		Vector3f c6 = c1.copy(); c6.mul(-1);
		Vector3f c7 = c0.copy(); c7.mul(-1);
		addLine(c0, c1, buff, m4, m3);
		addLine(c0, c2, buff, m4, m3);
		addLine(c0, c3, buff, m4, m3);
		addLine(c1, c5, buff, m4, m3);
		addLine(c1, c4, buff, m4, m3);
		addLine(c2, c4, buff, m4, m3);
		addLine(c2, c6, buff, m4, m3);
		addLine(c3, c5, buff, m4, m3);
		addLine(c3, c6, buff, m4, m3);
		addLine(c4, c7, buff, m4, m3);
		addLine(c5, c7, buff, m4, m3);
		addLine(c6, c7, buff, m4, m3);
		poseStack.popPose();
	}
	
	private void addLine(Vector3f start, Vector3f end, VertexConsumer buff, Matrix4f m4, Matrix3f m3) {
		Vector3f n = end.copy();
		n.sub(start);
		n.normalize();
		buff.vertex(m4,start.x(),start.y(),start.z())
			.color(188,85,41,255)
			.normal(m3,n.x(),n.y(),n.z())
			.endVertex();
		buff.vertex(m4,end.x(),end.y(),end.z())
			.color(188,85,41,255)
			.normal(m3,n.x(),n.y(),n.z())
			.endVertex();
	}

}
