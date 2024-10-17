package com.onewhohears.dscombat.client.model.obj.custom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import com.onewhohears.dscombat.client.model.obj.ObjPartModel;
import com.onewhohears.dscombat.entity.parts.EntityChainHook;

import com.onewhohears.onewholibs.util.math.UtilAngles;
import com.onewhohears.onewholibs.util.math.VectorUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class ChainHookModel extends ObjPartModel<EntityChainHook> {

	public static final ResourceLocation TEXTURE = new ResourceLocation("dscombat:textures/entity/part/chain.png");

	public ChainHookModel(String modelId) {
		super(modelId);
	}

	@Override
	public void render(EntityChainHook entity, PoseStack poseStack, MultiBufferSource bufferSource, int lightmap, float partialTicks) {
		if (entity.getConnections().size() != 0) {
			poseStack.pushPose();
			VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.text(TEXTURE));
			Quaternionf hookPosRot = VectorUtils.rotationQuaternion(VectorUtils.POSITIVE_Z, entity.getZRot());
			Vec3 chainOffset = UtilAngles.rotateVector(new Vec3(0, 0.45, 0), hookPosRot);
			if (entity.getParentVehicle() != null) chainOffset = UtilAngles.rotateVector(chainOffset, entity.getParentVehicle().getClientQ(partialTicks));
			Vec3 hookPos = entity.getPosition(partialTicks).add(chainOffset);
			poseStack.translate(chainOffset.x, chainOffset.y, chainOffset.z);
			Matrix4f matrix4f = poseStack.last().pose();
			for (EntityChainHook.ChainConnection chain : entity.getConnections()) {
				Entity connector = chain.getEntity();
				if (connector == null || connector.isRemoved()) continue;
				Vec3 connPos = connector.getPosition(partialTicks);
				Vec3 chainDiff = connPos.add(0, 1, 0).subtract(hookPos);
				Vec3 chainDiffNorm = chainDiff.normalize();
				Vec3 pitchAxis = UtilAngles.rotateVector(chainDiffNorm, VectorUtils.rotationQuaternion(VectorUtils.POSITIVE_Y, 90));
				Vec3 yawAxis = UtilAngles.rotateVector(chainDiffNorm, VectorUtils.rotationQuaternion(VectorUtils.NEGATIVE_X, 90));

				float t = 0.1f;
				float l = (float) (chainDiff.length() / EntityChainHook.CHAIN_LENGTH);
				drawChainSide(vertexconsumer, matrix4f, chainDiff, pitchAxis, t, l, lightmap);
				drawChainSide(vertexconsumer, matrix4f, chainDiff, pitchAxis.scale(-1), t, l, lightmap);
				drawChainSide(vertexconsumer, matrix4f, chainDiff, yawAxis, t, l, lightmap);
				drawChainSide(vertexconsumer, matrix4f, chainDiff, yawAxis.scale(-1), t, l, lightmap);
			}
			poseStack.popPose();
		}
		super.render(entity, poseStack, bufferSource, lightmap, partialTicks);
	}

	private void drawChainSide(VertexConsumer vertexconsumer, Matrix4f matrix4f, Vec3 chainDiff, Vec3 axis, float t, float l, int lightmap) {
		vertexconsumer.vertex(matrix4f, (float)-axis.x*t, (float)-axis.y*t, (float)-axis.z*t)
			.color(255, 255, 255, 255)
			.uv(0.0F, 0.0F).uv2(lightmap).endVertex();
		vertexconsumer.vertex(matrix4f, (float)(chainDiff.x-axis.x*t),
				(float)(chainDiff.y-axis.y*t), (float)(chainDiff.z-axis.z*t))
			.color(255, 255, 255, 255)
			.uv(l, 0.0F).uv2(lightmap).endVertex();
		vertexconsumer.vertex(matrix4f, (float)(chainDiff.x+axis.x*t),
				(float)(chainDiff.y+axis.y*t), (float)(chainDiff.z+axis.z*t))
			.color(255, 255, 255, 255)
			.uv(l, 1.0F).uv2(lightmap).endVertex();
		vertexconsumer.vertex(matrix4f, (float)axis.x*t, (float)axis.y*t, (float)axis.z*t)
			.color(255, 255, 255, 255)
			.uv(0.0F, 1.0F).uv2(lightmap).endVertex();
	}

}
