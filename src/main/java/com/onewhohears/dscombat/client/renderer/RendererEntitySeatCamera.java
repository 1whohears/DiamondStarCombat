package com.onewhohears.dscombat.client.renderer;

import com.onewhohears.dscombat.entity.aircraft.EntitySeatCamera;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;

public class RendererEntitySeatCamera<T extends EntitySeatCamera> extends EntityRenderer<T> {

	public RendererEntitySeatCamera(Context ctx) {
		super(ctx);
	}

	@Override
	public ResourceLocation getTextureLocation(T pEntity) {
		return null;
	}

}
