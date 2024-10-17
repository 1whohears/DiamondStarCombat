package com.onewhohears.dscombat.client.screen;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.common.container.menu.WeaponsBlockContainerMenu;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toserver.ToServerCraftWeapon;
import com.onewhohears.dscombat.crafting.WeaponRecipe;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.data.weapon.stats.WeaponStats;
import com.onewhohears.onewholibs.util.UtilItem;
import com.onewhohears.onewholibs.util.UtilMCText;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class WeaponsBlockScreen extends AbstractContainerScreen<WeaponsBlockContainerMenu> {
	
	private static final ResourceLocation BG_TEXTURE = new ResourceLocation(DSCombatMod.MODID,
			"textures/ui/aircraft_screen.png");
	
	private static final int buttonNum = 7;
	private List<Integer> fails = new ArrayList<>();
	
	private final int maxTab;
	private int tabIndex = 0;
	private int weaponIndex = 0;
	
	public WeaponsBlockScreen(WeaponsBlockContainerMenu menu, Inventory playerInv, Component title) {
		super(menu, playerInv, title);
		this.leftPos = 0;
		this.topPos = 0;
		this.imageWidth = 256;
		this.imageHeight = 256;
		maxTab = (int)((float)WeaponPresets.get().getWeaponRecipes(Minecraft.getInstance().level.getRecipeManager()).length / (float)buttonNum);
	}
	
	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(poseStack);
		super.render(poseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(poseStack, mouseX, mouseY);
        Minecraft m = Minecraft.getInstance();
        RenderSystem.enableBlend();
        if (WeaponPresets.get().getWeaponRecipeNum() == 0) return;
        // render weapon item options
        int startX = getGuiLeft() + titleLabelX;
		int startY = getGuiTop() + titleLabelY;
		int wx = startX + 51;
		int wy = startY + 12;
		for (int i = 0; i < buttonNum; ++i) {
			int index = tabIndex * buttonNum + i;
			if (index >= WeaponPresets.get().getWeaponRecipeNum()) break;
			ItemStack stack = WeaponPresets.get().getWeaponRecipes(m.level.getRecipeManager())[index].getResultItem();
			m.getItemRenderer().renderAndDecorateItem(
					stack, wx, wy);
			m.getItemRenderer().renderGuiItemDecorations(font,
					stack, wx, wy);
			wx += 20;
		}
		// render ingredients
		WeaponRecipe recipe = WeaponPresets.get().getWeaponRecipes(m.level.getRecipeManager())[weaponIndex];
		NonNullList<Ingredient> ingredients = recipe.getIngredients();
        for (int i = 0; i < getMenu().recipeSlots.getContainerSize(); ++i) {
        	if (i < ingredients.size()) {
        		ItemStack[] items = ingredients.get(i).getItems();
    			ItemStack stack = items[(m.player.tickCount/20)%items.length];
        		getMenu().recipeSlots.setItem(i, stack);
        		if (fails.contains(i)) {
        			Slot slot = getMenu().getSlot(i);
        			int left = leftPos + slot.x;
        			int top = topPos + slot.y;
        			fill(poseStack, left, top, left+17, top+17, 0x77ff0000);
        		}
        	} else getMenu().recipeSlots.setItem(i, ItemStack.EMPTY);
        }
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.setShaderTexture(0, BG_TEXTURE);
		guiGraphics.blit(stack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
	}
	
	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		font.draw(stack, title, titleLabelX+38, titleLabelY, 0x404040);
		font.draw(stack, playerInventoryTitle, inventoryLabelX+38, inventoryLabelY+56, 0x404040);
		font.draw(stack, UtilMCText.translatable("ui.dscombat.ingredients"), titleLabelX+123, titleLabelY+32, 0x00aa00);
		// weapon stats
		if (WeaponPresets.get().getWeaponRecipeNum() == 0) return;
		WeaponRecipe wr = WeaponPresets.get().getWeaponRecipes(Minecraft.getInstance().level.getRecipeManager())[weaponIndex];
		WeaponStats data = wr.getWeaponData();
		if (data == null) return;
		List<Component> list = new ArrayList<>();
		list.add(data.getDisplayNameComponent());
		data.addToolTips(list, true);
		font.draw(stack, list.get(0), titleLabelX+38, titleLabelY+34, 0x040404);
		float scale = 0.5f;
		stack.scale(scale, scale, scale);
		float invScale = 1f / scale;
		int startX = (int)((float)(titleLabelX+38) * invScale);
		int startY = (int)((float)(titleLabelY+43) * invScale);
		for (int i = 1; i < list.size(); ++i) {
			font.draw(stack, list.get(i), startX, startY, 
					list.get(i).getStyle().getColor().getValue());
			startY += font.lineHeight;
		}
		stack.scale(1/scale, 1/scale, 1/scale);
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
				UtilMCText.literal("<"), 
				onPress -> { prevButton(); });
		prevButton.x = wx;
		prevButton.y = wy;
		addRenderableWidget(prevButton);
		// buttons
		wx += 10;
		for (int b = 0; b < buttonNum; ++b) {
			final int c = b;
			Button acb = new Button(0, 0, 20, 20,
					UtilMCText.empty(),
					onPress -> { weaponButton(c); });
			acb.x = wx;
			acb.y = wy;
			addRenderableWidget(acb);
			wx += 20;
		}
		// next
		Button nextButton = new Button(0, 0, 10, 20, 
				UtilMCText.literal(">"), 
				onPress -> { nextButton(); });
		nextButton.x = wx;
		nextButton.y = wy;
		addRenderableWidget(nextButton);
		// craft
		Button craftButton = new Button(0, 0, 80, 20, 
				UtilMCText.translatable("ui.dscombat.craft_button"), 
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
		resetFails();
		int max = WeaponPresets.get().getWeaponRecipeNum();
		int a = tabIndex * buttonNum + num;
		if (a >= max) weaponIndex = max-1;
		else weaponIndex = a;
	}
	
	private void craftButton() {
		Minecraft m = Minecraft.getInstance();
		Player player = m.player;
		if (player == null) return;
		WeaponRecipe recipe = WeaponPresets.get().getWeaponRecipes(m.level.getRecipeManager())[weaponIndex];
		if (recipe.matches(player.getInventory(), m.level)) {
			PacketHandler.INSTANCE.sendToServer(new ToServerCraftWeapon(recipe.getId(), menu.getPos()));
		} else {
			player.displayClientMessage(UtilMCText.translatable("error.dscombat.cant_craft"), true);
			minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.VILLAGER_NO, 1.0F));
			setFails(UtilItem.testRecipeFails(recipe.getIngredients(), player.getInventory()));
		}
	}
	
	public void setFails(List<Integer> fails) {
		if (fails == null) return;
		this.fails = fails;
	}
	
	public void resetFails() {
		fails.clear();
	}

}
