package com.onewhohears.dscombat.fluid;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer.FogMode;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import org.joml.Vector3f;

public class CustomFluidType extends FluidType {
	
	private final ResourceLocation stillTexture;
	private final ResourceLocation flowingTexture;
	private final ResourceLocation overlayTexture;
	private final int tintColor;
	private final Vector3f fogColor;
	private final float fogStart, fogEnd;
	
	public CustomFluidType(Properties properties, ResourceLocation stillTexture, ResourceLocation overlayTexture, ResourceLocation flowingTexture, 
			int tintColor, Vector3f fogColor, float fogStart, float fogEnd) {
		super(properties);
		this.stillTexture = stillTexture;
		this.flowingTexture = flowingTexture;
		this.overlayTexture = overlayTexture;
		this.tintColor = tintColor;
		this.fogColor = fogColor;
		this.fogStart = fogStart;
		this.fogEnd = fogEnd;
	}
	
	@Override
	public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
		consumer.accept(new IClientFluidTypeExtensions() {
			@Override
			public int getTintColor() {
				return tintColor;
			}
			@Override
			public ResourceLocation getStillTexture() {
				return stillTexture;
			}
			@Override
			public ResourceLocation getFlowingTexture() {
				return flowingTexture;
			}
			@Override
			public @Nullable ResourceLocation getOverlayTexture() {
				return overlayTexture;
			}
			@Override
			public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level,
					int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
				return fogColor;
			}
			@Override
			public void modifyFogRender(Camera camera, FogMode mode, float renderDistance, float partialTick,
					float nearDistance, float farDistance, FogShape shape) {
				RenderSystem.setShaderFogStart(fogStart);
				RenderSystem.setShaderFogEnd(fogEnd);
			}
		});
	}

	public ResourceLocation getStillTexture() {
		return stillTexture;
	}

	public ResourceLocation getFlowingTexture() {
		return flowingTexture;
	}

	public ResourceLocation getOverlayTexture() {
		return overlayTexture;
	}

	public int getTintColor() {
		return tintColor;
	}

	public Vector3f getFogColor() {
		return fogColor;
	}

}
