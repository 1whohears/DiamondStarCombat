package com.onewhohears.dscombat.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraftforge.client.model.renderable.CompositeRenderable;

@Mixin(CompositeRenderable.class)
public interface CompositeRenderableAccess {
	@Accessor(value = "components", remap = false)
	List<CompositeRenderableComponentAccess> getComponents();
}
