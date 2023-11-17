package com.onewhohears.dscombat.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.client.model.obj.ObjModelColorHolder;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.renderable.ITextureRenderTypeLookup;

@Mixin(targets = "net.minecraftforge.client.model.renderable.CompositeRenderable$Mesh")
public abstract class CompositeRenderableMeshMixin {
	
	@Final @Shadow(remap = false)
	private ResourceLocation texture;
	@Final @Shadow(remap = false)
	private List<BakedQuad> quads;
	/**
	 * @reason I could not figure out how to get ModifyArg to work. I doubt this will conflict with anything. Famous last words I know. 
	 * I am going to **redacted** whoever decided to not make the color in the mesh renderer changeable and set them all to 1. 
	 * @author 1whohears
	 */
	@Overwrite(remap = false)
	public void render(PoseStack poseStack, MultiBufferSource bufferSource, ITextureRenderTypeLookup textureRenderTypeLookup, int lightmap, int overlay) {
		var consumer = bufferSource.getBuffer(textureRenderTypeLookup.get(texture));
		for (var quad : quads) 
			consumer.putBulkData(poseStack.last(), quad, 
					ObjModelColorHolder.RED, ObjModelColorHolder.GREEN, ObjModelColorHolder.BLUE, 1, 
					lightmap, overlay, true);
	}
	
}
