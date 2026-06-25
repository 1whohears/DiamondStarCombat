package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.onewhohears.dscombat.block.entity.MissileLaunchStationBlockEntity;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.data.weapon.client.WeaponAssets;
import com.onewhohears.dscombat.data.weapon.client.WeaponClientStats;
import com.onewhohears.dscombat.data.weapon.stats.WeaponStats;
import com.onewhohears.onewholibs.client.model.obj.ObjEntityModels;
import com.onewhohears.onewholibs.client.model.obj.ObjModelHandler;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;

import java.util.Collections;

public class MissileLaunchStationRenderer implements BlockEntityRenderer<MissileLaunchStationBlockEntity> {
	
	public MissileLaunchStationRenderer(BlockEntityRendererProvider.Context context) {
	}
	
	@Override
	public void render(MissileLaunchStationBlockEntity station, float partialTick, PoseStack poseStack,
					   MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
		
		String missileId = station.getLoadedMissileId();
		if (missileId == null) return;
		
		WeaponStats stats = WeaponPresets.get().get(missileId);
		if (stats == null) return;
		
		String assetId = stats.getAssetId();
		WeaponClientStats<?> clientStats = WeaponAssets.get().get(assetId);
		if (clientStats == null) return;
		
		String modelId = clientStats.getModelId();
		if (modelId == null || modelId.isEmpty()) return;
		
		// Get the model handler
		ObjModelHandler modelHandler = ObjEntityModels.get().getObjModelHandler(modelId);
		if (modelHandler == null) return;
		
		poseStack.pushPose();
		
		// Position missile at center of block (x=0, y=2.0, z=0 relative to block)
		poseStack.translate(0.5, 0.0, 0.5);
		
		// Missile always points straight up (no rotation based on facing)
		// Rotate 90 degrees on X axis to point upward
		poseStack.mulPose(Axis.XP.rotationDegrees(-90f));
		
		// Scale to actual entity size (1.0 = full size, not item size)
		float scale = 1.0f;
		poseStack.scale(scale, scale, scale);
		
		// Render using the texture defined in the model's MTL file
		modelHandler.render(poseStack, bufferSource, partialTick, packedLight, packedOverlay,
			Collections.emptyMap(), (tex) -> RenderType.entityCutout(tex));
		
		poseStack.popPose();
	}
}
