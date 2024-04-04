package com.onewhohears.dscombat.client.screen;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.common.container.menu.AircraftBlockContainerMenu;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toserver.ToServerCraftPlane;
import com.onewhohears.dscombat.crafting.AircraftRecipe;
import com.onewhohears.dscombat.data.aircraft.AircraftPreset;
import com.onewhohears.dscombat.data.aircraft.AircraftPresets;
import com.onewhohears.dscombat.util.UtilItem;
import com.onewhohears.dscombat.util.UtilMCText;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class AircraftBlockScreen extends AbstractContainerScreen<AircraftBlockContainerMenu> {
	
	private static final ResourceLocation BG_TEXTURE = new ResourceLocation(DSCombatMod.MODID,
			"textures/ui/vehicle_forge_ui.png");
	private final int bg_tex_size;
	
	private AircraftTab tab = AircraftTab.TANKS;
	private List<Integer> fails = new ArrayList<>();
	
	public AircraftBlockScreen(AircraftBlockContainerMenu menu, Inventory playerInv, Component title) {
		super(menu, playerInv, title);
		this.imageWidth = 352;
		this.imageHeight = 260;
		this.bg_tex_size = 512;
		this.titleLabelX = 122;
		this.titleLabelY = 24;
		this.inventoryLabelX = 96;
		this.inventoryLabelY = 146;
	}
	
	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(poseStack);
		super.render(poseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(poseStack, mouseX, mouseY);
        this.renderIngredients(poseStack, mouseX, mouseY, partialTicks);
        this.renderVehicle(poseStack, mouseX, mouseY, partialTicks);
        this.renderBookmark(poseStack, mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void renderBg(PoseStack stack, float pTicks, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.setShaderTexture(0, BG_TEXTURE);
		blit(stack, leftPos, topPos, 0, 0, 
				imageWidth, imageHeight, 
				bg_tex_size, bg_tex_size);
	}
	
	protected void renderBookmark(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.setShaderTexture(0, BG_TEXTURE);
		blit(poseStack, leftPos+tab.getBookmarkXPos(), topPos, 
				352, 0, 7, 11, 
				bg_tex_size, bg_tex_size);
	}
	
	protected void renderVehicle(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		AircraftRecipe ap = tab.getSelectedRecipe();
        if (ap == null) return;
		Minecraft m = Minecraft.getInstance();
        ItemStack stack = ap.getResultItem();
        m.getItemRenderer().renderAndDecorateItem(stack, leftPos+170, topPos+50);
        // HOW 2 render 3d vehicle and make it spin
	}
	
	protected void renderIngredients(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		AircraftRecipe ap = tab.getSelectedRecipe();
        if (ap == null) return;
        Minecraft m = Minecraft.getInstance();
        NonNullList<Ingredient> ingredients = ap.getIngredients();
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
	protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
		font.draw(stack, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0x404040);
		// plane stats
		AircraftRecipe ar = tab.getSelectedRecipe();
		if (ar == null) return;
		AircraftPreset ap = ar.getVehiclePreset();
		if (ap == null) return;
		font.draw(stack, ap.getDisplayNameComponent(), titleLabelX, titleLabelY, 0x000000);
		CompoundTag data = ap.getDataAsNBT().getCompound("stats");
		float scale = 1f;
		stack.scale(scale, scale, scale);
		float invScale = 1f / scale;
		int startX = (int)(293f * invScale);
		int startY = (int)(34f * invScale);
		int pColor = 0x4CFF00;
		font.draw(stack, UtilMCText.literal("Health: "+data.getDouble("max_health")), startX, startY, pColor);
		startY += font.lineHeight;
		font.draw(stack, UtilMCText.literal("Speed: "+(int)(data.getDouble("max_speed")*20)+" m/s"), startX, startY, pColor);
		startY += font.lineHeight;
		font.draw(stack, UtilMCText.literal("Mass: "+data.getDouble("mass")), startX, startY, pColor);
		startY += font.lineHeight;
		font.draw(stack, UtilMCText.literal("Area: "+data.getDouble("cross_sec_area")), startX, startY, pColor);
		startY += font.lineHeight;
		font.draw(stack, UtilMCText.literal("Stealth: "+data.getDouble("stealth")), startX, startY, pColor);
		startY += font.lineHeight;
		font.draw(stack, UtilMCText.literal("Heat: "+data.getDouble("idleheat")), startX, startY, pColor);
		startY += font.lineHeight;
		font.draw(stack, UtilMCText.literal("Armor: "+data.getFloat("base_armor")), startX, startY, pColor);
		startY += font.lineHeight;
		font.draw(stack, UtilMCText.literal("Yaw: "+(int)(data.getDouble("maxyaw")*20)+" d/s"), startX, startY, pColor);
		startY += font.lineHeight;
		font.draw(stack, UtilMCText.literal("Pitch: "+(int)(data.getDouble("maxpitch")*20)+" d/s"), startX, startY, pColor);
		startY += font.lineHeight;
		font.draw(stack, UtilMCText.literal("Roll: "+(int)(data.getDouble("maxroll")*20)+" d/s"), startX, startY, pColor);
		stack.scale(1/scale, 1/scale, 1/scale);
	}
	
	@Override
	protected void init() {
		super.init();
		// tabs
		Button tankButton = new ImageButton(0, 0, 45, 20, 
				83, 0, 300, 
				BG_TEXTURE, 512, 512,
				onPress -> { tabButton(AircraftTab.TANKS); });
		tankButton.x = leftPos+83;
		tankButton.y = topPos;
		addRenderableWidget(tankButton);
		Button heliButton = new ImageButton(0, 0, 45, 20, 
				130, 0, 300, 
				BG_TEXTURE, 512, 512,
				onPress -> { tabButton(AircraftTab.HELIS); });
		heliButton.x = leftPos+130;
		heliButton.y = topPos;
		addRenderableWidget(heliButton);
		Button planeButton = new ImageButton(0, 0, 45, 20, 
				177, 0, 300, 
				BG_TEXTURE, 512, 512,
				onPress -> { tabButton(AircraftTab.PLANES); });
		planeButton.x = leftPos+177;
		planeButton.y = topPos;
		addRenderableWidget(planeButton);
		Button boatButton = new ImageButton(0, 0, 45, 20, 
				224, 0, 300, 
				BG_TEXTURE, 512, 512,
				onPress -> { tabButton(AircraftTab.BOATS); });
		boatButton.x = leftPos+224;
		boatButton.y = topPos;
		addRenderableWidget(boatButton);
		// prev
		Button prevButton = new ImageButton(0, 0, 41, 10, 
				78, 52, 230, 
				BG_TEXTURE, 512, 512,
				onPress -> { prevButton(); });
		prevButton.x = leftPos+78;
		prevButton.y = topPos+52;
		addRenderableWidget(prevButton);
		// next
		Button nextButton = new ImageButton(0, 0, 41, 10, 
				233, 52, 230, 
				BG_TEXTURE, 512, 512,
				onPress -> { nextButton(); });
		nextButton.x = leftPos+233;
		nextButton.y = topPos+52;
		addRenderableWidget(nextButton);
		// craft
		Button craftButton = new Button(0, 0, 80, 20, 
				UtilMCText.translatable("ui.dscombat.craft_button"), 
				onPress -> { craftButton(); });
		craftButton.x = leftPos+140;
		craftButton.y = topPos+86;
		addRenderableWidget(craftButton);
	}
	
	private void tabButton(AircraftTab tab) {
		this.tab = tab;
		resetFails();
	}
	
	private void prevButton() {
		tab.cycleIndexLeft();
		resetFails();
	}
	
	private void nextButton() {
		tab.cycleIndexRight();
		resetFails();
	}
	
	private void craftButton() {
		resetFails();
		Minecraft m = Minecraft.getInstance();
		Player player = m.player;
		if (player == null) return;
		AircraftRecipe ap = tab.getSelectedRecipe();
		if (ap == null) return;
		if (ap.matches(player.getInventory(), m.level)) {
			PacketHandler.INSTANCE.sendToServer(new ToServerCraftPlane(ap.getId(), menu.getPos()));
		} else {
			player.displayClientMessage(UtilMCText.translatable("error.dscombat.cant_craft"), true);
			minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.VILLAGER_NO, 1.0F));
			setFails(UtilItem.testRecipeFails(ap.getIngredients(), player.getInventory()));
		}
	}
	
	public void setFails(List<Integer> fails) {
		if (fails == null) return;
		this.fails = fails;
	}
	
	public void resetFails() {
		fails.clear();
	}
	
	public static enum AircraftTab {
		TANKS(() -> AircraftPresets.get().getTankRecipes(Minecraft.getInstance().level.getRecipeManager()), 86),
		HELIS(() -> AircraftPresets.get().getHeliRecipes(Minecraft.getInstance().level.getRecipeManager()), 133),
		PLANES(() -> AircraftPresets.get().getPlaneRecipes(Minecraft.getInstance().level.getRecipeManager()), 180),
		BOATS(() -> AircraftPresets.get().getBoatRecipes(Minecraft.getInstance().level.getRecipeManager()), 227);
		
		private AircraftPresetList presetFactory;
		private int index = 0;
		private final int bookmarkX;
		
		private AircraftTab(AircraftPresetList presetFactory, int bookmarkX) {
			this.presetFactory = presetFactory;
			this.bookmarkX = bookmarkX;
		}
		
		public AircraftRecipe[] getRecipes() {
			return presetFactory.get();
		}
		
		public int getIndex() {
			return checkIndex();
		}
		
		private int checkIndex() {
			if (getRecipes().length == 0) index = -1;
			else if (index >= getRecipes().length) index = getRecipes().length-1;
			else if (index < 0) index = 0;
			return index;
		}
		
		@Nullable
		public AircraftRecipe getSelectedRecipe() {
			if (checkIndex() == -1) return null;
			return getRecipes()[getIndex()];
		}
		
		public int cycleIndexRight() {
			++index;
			if (index >= getRecipes().length) index = 0;
			return index;
		}
		
		public int cycleIndexLeft() {
			--index;
			if (index < 0) index = getRecipes().length-1;
			return index;
		}
		
		public int getBookmarkXPos() {
			return bookmarkX;
		}
		
	}
	
	public static interface AircraftPresetList {
		AircraftRecipe[] get();
	}

}
