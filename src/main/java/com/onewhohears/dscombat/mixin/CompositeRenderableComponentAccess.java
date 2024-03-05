package com.onewhohears.dscombat.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "net.minecraftforge.client.model.renderable.CompositeRenderable$Component")
public interface CompositeRenderableComponentAccess {
	@Accessor(value = "children", remap = false)
	List<CompositeRenderableComponentAccess> getChildren();
	@Accessor(value = "meshes", remap = false)
	List<CompositeRenderableMeshAccess> getMeshes();
}
