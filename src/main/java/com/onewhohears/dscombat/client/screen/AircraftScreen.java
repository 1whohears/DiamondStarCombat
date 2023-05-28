package com.onewhohears.dscombat.client.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.common.container.AircraftMenuContainer;
import com.onewhohears.dscombat.common.container.slot.PartItemSlot;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toserver.ToServerAircraftToItem;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class AircraftScreen extends AbstractContainerScreen<AircraftMenuContainer> {
	
	private static final ResourceLocation BG_TEXTURE = new ResourceLocation(DSCombatMod.MODID,
			"textures/ui/aircraft_screen.png");
	private static final ResourceLocation SLOTS_TEXTURE = new ResourceLocation(DSCombatMod.MODID,
			"textures/ui/slots.png");
	
	public AircraftScreen(AircraftMenuContainer pMenu, Inventory pPlayerInventory, Component title) {
		super(pMenu, pPlayerInventory, title);
		this.leftPos = 0;
		this.topPos = 0;
		this.imageWidth = 256;
		this.imageHeight = 256;
	}
	
	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(poseStack);
		super.render(poseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(poseStack, mouseX, mouseY);
	}
	
	@Override
	protected void renderTooltip(PoseStack poseStack, int mouseX, int mouseY) {
		if (!menu.getCarried().isEmpty() || hoveredSlot == null) return;
		if (hoveredSlot.hasItem()) {
			ItemStack stack = hoveredSlot.getItem();
			renderTooltip(poseStack, getTooltipFromItem(stack), 
					stack.getTooltipImage(), mouseX, mouseY);
		} else {
			renderTooltip(poseStack, getSlotTooltip(), 
					Optional.empty(), mouseX, mouseY);
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
			c.add(new TranslatableComponent(slot.data.getName()).setStyle(Style.EMPTY.withColor(0xFF55FF)));
			c.add(new TranslatableComponent(slot.data.getSlotType().getTranslatableName()).setStyle(Style.EMPTY.withColor(0xFFAA00)));
			if (slot.data.isLocked()) c.add(new TranslatableComponent("info.dscombat.locked").setStyle(Style.EMPTY.withColor(0xAA0000)));
		}
		return c;
	}
	
	@Override
	protected void renderBg(PoseStack stack, float pTicks, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.setShaderTexture(0, BG_TEXTURE);
		blit(stack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
		RenderSystem.setShaderTexture(0, SLOTS_TEXTURE);
		for (int i = 0; i < menu.slots.size(); ++i) {
			if (!(menu.slots.get(i) instanceof PartItemSlot slot)) continue;
			blit(stack, leftPos+slot.x, topPos+slot.y, 
					slot.data.getSlotType().getIconXOffset(), 0, 
					16, 16, 256, 16);
		}
	}

	@Override
	protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
		font.draw(stack, title, titleLabelX+38, titleLabelY, 0x404040);
		font.draw(stack, playerInventoryTitle, inventoryLabelX+38, inventoryLabelY+56, 0x404040);
	}
	
	@Override
	protected void init() {
		super.init();
		Button getItemButton = new Button(0, 0, 80, 20, 
				new TranslatableComponent("ui.dscombat.shrink_plane_button"), 
				onPress -> { onPlaneItemButton(); });
		getItemButton.x = this.getGuiLeft() + titleLabelX+122;
		getItemButton.y = this.getGuiTop() + titleLabelY+110;
		this.addRenderableWidget(getItemButton);
	}
	
	@Override
	public void containerTick() {
		super.containerTick();
		Minecraft m = Minecraft.getInstance();
		if (m.player == null) {
			m.setScreen(null);
			return;
		}
		Entity rv = m.player.getRootVehicle();
		if (!(rv instanceof EntityAircraft plane)) {
			m.setScreen(null);
			return;
		}
		Entity c = plane.getControllingPassenger();
		if (c == null || !c.equals(m.player)) {
			m.setScreen(null);
			return;
		}
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
	
	private void onPlaneItemButton() {
		Minecraft m = Minecraft.getInstance();
		Entity rv = m.player.getRootVehicle();
		PacketHandler.INSTANCE.sendToServer(new ToServerAircraftToItem(rv.getId()));
	}

}
