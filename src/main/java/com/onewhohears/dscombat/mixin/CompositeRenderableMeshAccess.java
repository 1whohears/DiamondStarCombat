package com.onewhohears.dscombat.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.renderer.block.model.BakedQuad;

@Mixin(targets = "net.minecraftforge.client.model.renderable.CompositeRenderable$Mesh")
public interface CompositeRenderableMeshAccess {
	@Accessor(value = "quads", remap = false)
	List<BakedQuad> getQuads();
}
