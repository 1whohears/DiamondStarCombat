package com.onewhohears.dscombat.client.renderer;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class RendererEntityInvisible<T extends Entity> extends EntityRenderer<T> {

	public RendererEntityInvisible(Context ctx) {
		super(ctx);
	}

	@Override
	public ResourceLocation getTextureLocation(T pEntity) {
		return null;
	}

}
