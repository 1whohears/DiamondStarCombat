package com.onewhohears.dscombat.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.DSCombatMod;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class VehiclePaintScreen extends Screen {
	
	public static final ResourceLocation BG_TEXTURE = new ResourceLocation(DSCombatMod.MODID,
			"textures/ui/paintjob_screen.png");
	
	public VehiclePaintScreen() {
		super(Component.literal("Vehicle Paint Job"));
	}
	
	@Override
	protected void init() {
		super.init();
		
	}
	
	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
		renderBackground(poseStack);
		
		super.render(poseStack, mouseX, mouseY, partialTick);
	}
	
	@Override
	public void renderBackground(PoseStack poseStack, int vOffset) {
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.setShaderTexture(0, BG_TEXTURE);
	}

}
