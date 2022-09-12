package com.onewhohears.dscombat.client.renderer;

import com.onewhohears.dscombat.entity.aircraft.EntitySeat;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;

public class RendererEntitySeat<T extends EntitySeat> extends EntityRenderer<T> {

	public RendererEntitySeat(Context ctx) {
		super(ctx);
	}

	@Override
	public ResourceLocation getTextureLocation(T pEntity) {
		return null;
	}

}
