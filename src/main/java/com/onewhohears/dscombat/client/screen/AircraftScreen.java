package com.onewhohears.dscombat.client.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.common.container.AircraftMenuContainer;
import com.onewhohears.dscombat.common.container.slot.PartItemSlot;
import com.onewhohears.dscombat.util.UtilMCText;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class AircraftScreen extends AbstractContainerScreen<AircraftMenuContainer> {
	
	private static final ResourceLocation BG_TEXTURE = new ResourceLocation(DSCombatMod.MODID,
			"textures/ui/aircraft_screen.png");
	
	public AircraftScreen(AircraftMenuContainer pMenu, Inventory pPlayerInventory, Component title) {
		super(pMenu, pPlayerInventory, title);
		this.leftPos = 0;
		this.topPos = 0;
		this.imageWidth = 256;
		this.imageHeight = 256;
	}
	
	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		super.render(poseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(poseStack, mouseX, mouseY);
	}
	
	@Override
	protected void renderTooltip(PoseStack poseStack, int mouseX, int mouseY) {
		if (!menu.getCarried().isEmpty() || hoveredSlot == null) return;
		if (hoveredSlot.hasItem()) {
			ItemStack stack = hoveredSlot.getItem();
			renderTooltip(poseStack, getTooltipFromItem(stack), stack.getTooltipImage(), mouseX, mouseY);
		} else {
			renderTooltip(poseStack, getSlotTooltip(), Optional.empty(), mouseX, mouseY);
		}
	}
	
	@Override
	public List<Component> getTooltipFromItem(ItemStack itemStack) {
		List<Component> c = itemStack.getTooltipLines(this.minecraft.player, 
				minecraft.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL);
		List<Component> slots = getSlotTooltip();
		for (int i = 0; i < slots.size(); ++i) c.add(i, slots.get(i));
		return c;
	}
	
	public List<Component> getSlotTooltip() {
		List<Component> c = new ArrayList<Component>();
		if (this.hoveredSlot instanceof PartItemSlot slot) {
			MutableComponent type = UtilMCText.simpleText(slot.data.getTypeName());
			MutableComponent name = UtilMCText.simpleText(slot.data.getName());
			c.add(type);
			c.add(name);
		}
		return c;
	}
	
	@Override
	protected void renderBg(PoseStack stack, float pTicks, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.setShaderTexture(0, BG_TEXTURE);
		blit(stack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
		for (int i = 0; i < menu.slots.size(); ++i) {
			if (!(menu.slots.get(i) instanceof PartItemSlot slot)) continue;
			RenderSystem.setShaderTexture(0, slot.data.getSlotIcon());
			blit(stack, leftPos+slot.x, topPos+slot.y, 0, 0, 16, 16);
		}
	}

	@Override
	protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
		drawString(stack, font, title, titleLabelX+35, titleLabelY, 0x404040);
		drawString(stack, font, playerInventoryTitle, titleLabelX+35, titleLabelY+40, 0x404040);
	}
	
	@Override
	protected void init() {
		super.init();
		// use to add widgets like buttons and sliders
	}
	
	@Override
	public void containerTick() {
		super.containerTick();
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
		return super.mouseScrolled(mouseX, mouseY, scroll);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		return super.mouseReleased(mouseX, mouseY, button);
	}

}
