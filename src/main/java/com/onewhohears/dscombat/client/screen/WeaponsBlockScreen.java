package com.onewhohears.dscombat.client.screen;

import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.common.container.WeaponsBlockMenuContainer;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toserver.ToServerCraftWeapon;
import com.onewhohears.dscombat.crafting.DSCIngredient;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponData.ComponentColor;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class WeaponsBlockScreen extends AbstractContainerScreen<WeaponsBlockMenuContainer> {
	
	private static final ResourceLocation BG_TEXTURE = new ResourceLocation(DSCombatMod.MODID,
			"textures/ui/aircraft_screen.png");
	
	private static final int buttonNum = 7;
	
	private final int maxTab;
	private int tabIndex = 0;
	private int weaponIndex = 0;
	
	public WeaponsBlockScreen(WeaponsBlockMenuContainer menu, Inventory playerInv, Component title) {
		super(menu, playerInv, title);
		this.leftPos = 0;
		this.topPos = 0;
		this.imageWidth = 256;
		this.imageHeight = 256;
		maxTab = (int)((float)WeaponPresets.get().getPresetNum() / (float)buttonNum);
	}
	
	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(poseStack);
		super.render(poseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(poseStack, mouseX, mouseY);
        Minecraft m = Minecraft.getInstance();
        RenderSystem.enableBlend();
        if (WeaponPresets.get().getPresetNum() == 0) return;
        // render weapon item options
        int startX = getGuiLeft() + titleLabelX;
		int startY = getGuiTop() + titleLabelY;
		int wx = startX + 51;
		int wy = startY + 12;
		for (int i = 0; i < buttonNum; ++i) {
			int index = tabIndex * buttonNum + i;
			if (index >= WeaponPresets.get().getPresetNum()) break;
			ItemStack stack = WeaponPresets.get().getAllPresets()[index].getDisplayStack();
			m.getItemRenderer().renderAndDecorateItem(
					stack, wx, wy);
			m.getItemRenderer().renderGuiItemDecorations(font,
					stack, wx, wy);
			wx += 20;
		}
		// render ingredients
		WeaponData data = WeaponPresets.get().getAllPresets()[weaponIndex];
		int iix = startX + 122;
		int ix = iix;
		int iy = startY + 44;
		for (int i = 0; i < data.getIngredients().size(); ++i) {
			if (i != 0 && i % 4 == 0) {
				ix = iix;
				iy += 20;
			}
			ItemStack stack = data.getIngredients().get(i).getDisplayItem();
			m.getItemRenderer().renderAndDecorateItem(
					stack, ix, iy);
			m.getItemRenderer().renderGuiItemDecorations(font, 
					stack, ix, iy);
			ix += 20;
		}
	}

	@Override
	protected void renderBg(PoseStack stack, float pTicks, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.setShaderTexture(0, BG_TEXTURE);
		blit(stack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
	}
	
	@Override
	protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
		font.draw(stack, title, titleLabelX+38, titleLabelY, 0x404040);
		font.draw(stack, playerInventoryTitle, inventoryLabelX+38, inventoryLabelY+56, 0x404040);
		font.draw(stack, Component.translatable("ui.dscombat.ingredients"), titleLabelX+122, titleLabelY+34, 0x00aa00);
		// weapon stats
		if (WeaponPresets.get().getPresetNum() == 0) return;
		WeaponData data = WeaponPresets.get().getAllPresets()[weaponIndex];
		List<ComponentColor> list = data.getInfoComponents();
		font.draw(stack, list.get(0).component, 
				titleLabelX+38, titleLabelY+34, list.get(0).color);
		float scale = 0.5f;
		stack.scale(scale, scale, scale);
		float invScale = 1f / scale;
		int startX = (int)((float)(titleLabelX+38) * invScale);
		int startY = (int)((float)(titleLabelY+43) * invScale);
		//int inc = (int)((float)font.lineHeight * scale);
		for (int i = 1; i < list.size(); ++i) {
			font.draw(stack, list.get(i).component, 
					startX, startY, list.get(i).color);
			//startY += inc;
			startY += font.lineHeight;
		}
	}
	
	@Override
	protected void init() {
		super.init();
		int startX = getGuiLeft() + titleLabelX;
		int startY = getGuiTop() + titleLabelY;
		// weapons buttons
		int wx = startX + 39;
		int wy = startY + 10;
		// prev
		Button prevButton = new Button(0, 0, 10, 20, 
				Component.literal("<"), 
				onPress -> { prevButton(); });
		prevButton.x = wx;
		prevButton.y = wy;
		addRenderableWidget(prevButton);
		// buttons
		wx += 10;
		for (int b = 0; b < buttonNum; ++b) {
			final int c = b;
			Button acb = new Button(0, 0, 20, 20,
					Component.empty(),
					onPress -> { weaponButton(c); });
			acb.x = wx;
			acb.y = wy;
			addRenderableWidget(acb);
			wx += 20;
		}
		// next
		Button nextButton = new Button(0, 0, 10, 20, 
				Component.literal(">"), 
				onPress -> { nextButton(); });
		nextButton.x = wx;
		nextButton.y = wy;
		addRenderableWidget(nextButton);
		// craft
		Button craftButton = new Button(0, 0, 80, 20, 
				Component.translatable("ui.dscombat.craft_button"), 
				onPress -> { craftButton(); });
		craftButton.x = startX+122;
		craftButton.y = startY+110;
		addRenderableWidget(craftButton);
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
	
	private void prevButton() {
		tabIndex -= 1;
		if (tabIndex < 0) tabIndex = maxTab;
	}
	
	private void nextButton() {
		tabIndex += 1;
		if (tabIndex > maxTab) tabIndex = 0;
	}
	
	private void weaponButton(int num) {
		int max = WeaponPresets.get().getPresetNum();
		int a = tabIndex * buttonNum + num;
		if (a >= max) weaponIndex = max-1;
		else weaponIndex = a;
	}
	
	private void craftButton() {
		Minecraft m = Minecraft.getInstance();
		Player player = m.player;
		if (player == null) return;
		WeaponData data = WeaponPresets.get().getAllPresets()[weaponIndex];
		if (DSCIngredient.hasIngredients(data.getIngredients(), player.getInventory())) {
			PacketHandler.INSTANCE.sendToServer(new ToServerCraftWeapon(data.getId(), menu.getPos()));
		} else {
			player.displayClientMessage(Component.translatable("error.dscombat.cant_craft"), true);
			minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.VILLAGER_NO, 1.0F));
		}
	}

}
