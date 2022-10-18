package com.onewhohears.dscombat.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.common.container.AircraftMenuContainer;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AircraftScreen extends AbstractContainerScreen<AircraftMenuContainer> {
	
	private static final ResourceLocation TEXTURE = new ResourceLocation(DSCombatMod.MODID,
			"textures/gui/aircraft_screen.png");
	
	public AircraftScreen(AircraftMenuContainer pMenu, Inventory pPlayerInventory) {
		super(pMenu, pPlayerInventory, Component.empty());
		this.leftPos = 0;
		this.topPos = 0;
		this.imageWidth = 64;
		this.imageHeight = 64;
	}

	@Override
	protected void renderBg(PoseStack stack, float pTicks, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.setShaderTexture(0, TEXTURE);
		blit(stack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
		// TODO create plane menu screen
	}

	@Override
	protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
		drawString(stack, font, title, this.leftPos+8, this.topPos+3, 0x404040);
		drawString(stack, font, playerInventoryTitle, this.leftPos+8, this.topPos+80, 0x404040);
	}
	
	@Override
	protected void init() {
		super.init();
		// use to add widgets like buttons and sliders
	}

}
