package com.onewhohears.dscombat.client.screen;

import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.common.container.WeaponsBlockMenuContainer;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponData.ComponentColor;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class WeaponsBlockScreen extends AbstractContainerScreen<WeaponsBlockMenuContainer> {
	
	/*private static final HashMap<String, ResourceLocation> ITEM_TEXTURES = new HashMap<String, ResourceLocation>();
	private static ResourceLocation getItemTexture(String id) {
		ResourceLocation rl = ITEM_TEXTURES.get(id);
		if (rl == null) {
			rl = new ResourceLocation(DSCombatMod.MODID, "textures/item/"+id+".png");
			ITEM_TEXTURES.put(id, rl);
		}
		return rl;
	}*/
	
	private static final ResourceLocation BG_TEXTURE = new ResourceLocation(DSCombatMod.MODID,
			"textures/ui/aircraft_screen.png");
	/*private static final ResourceLocation WIDGETS = new ResourceLocation("minecraft",
			"textures/gui/widgets.png");*/
	
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
		/*
		 * TODO make image buttons on the top with the weapon icon
		 * when you click on these buttons the weapon info is displayed underneath 
		 * with the materials needed and craft button at the bottom
 		 * also have arrows on the sides to cycle through the weapons
		 */
		maxTab = (int)((float)WeaponPresets.weapons.size() / (float)buttonNum);
		//System.out.println("size = "+WeaponPresets.weapons.size()+" max tab = "+maxTab);
	}
	
	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(poseStack);
		super.render(poseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(poseStack, mouseX, mouseY);
        Minecraft m = Minecraft.getInstance();
        RenderSystem.enableBlend();
        // render weapon item options
        int startX = getGuiLeft() + titleLabelX;
		int startY = getGuiTop() + titleLabelY;
		int wx = startX + 51;
		int wy = startY + 12;
		for (int i = 0; i < buttonNum; ++i) {
			int index = tabIndex * buttonNum + i;
			if (index >= WeaponPresets.weapons.size()) break;
			ItemStack stack = WeaponPresets.weapons.get(index).getDisplayStack();
			m.getItemRenderer().renderAndDecorateItem(
					stack, wx, wy);
			m.getItemRenderer().renderGuiItemDecorations(font,
					stack, wx, wy);
			wx += 20;
		}
		// render ingredients
		if (WeaponPresets.weapons.size() == 0) return;
		WeaponData data = WeaponPresets.weapons.get(weaponIndex);
		int iix = startX + 122;
		int ix = iix;
		int iy = startY + 44;
		for (int i = 0; i < data.ingredients.size(); ++i) {
			if (i != 0 && i % 4 == 0) {
				ix = iix;
				iy += 20;
			}
			ItemStack stack = data.ingredients.get(i).getItem();
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
		font.draw(stack, Component.translatable("dscombat.ingredients"), titleLabelX+122, titleLabelY+34, 0x00aa00);
		// weapon stats
		if (WeaponPresets.weapons.size() == 0) return;
		WeaponData data = WeaponPresets.weapons.get(weaponIndex);
		int startX = titleLabelX+38;
		int startY = titleLabelY+34;
		List<ComponentColor> list = data.getInfoComponents();
		for (int i = 0; i < list.size(); ++i) {
			font.draw(stack, list.get(i).component, 
					startX, startY, list.get(i).color);
			startY += 10;
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
				Component.translatable("dscombat.craft_button"), 
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
		System.out.println("tabIndex = "+tabIndex);
	}
	
	private void nextButton() {
		tabIndex += 1;
		if (tabIndex > maxTab) tabIndex = 0;
		System.out.println("tabIndex = "+tabIndex);
	}
	
	private void weaponButton(int num) {
		int max = WeaponPresets.weapons.size();
		int a = tabIndex * buttonNum + num;
		if (a >= max) weaponIndex = max-1;
		else weaponIndex = a;
		System.out.println("buttonIndex = "+weaponIndex);
	}
	
	private void craftButton() {
		System.out.println("CRAFT BUTTON");
		System.out.println("tabIndex = "+tabIndex);
		System.out.println("weaponIndex = "+weaponIndex);
	}

}
