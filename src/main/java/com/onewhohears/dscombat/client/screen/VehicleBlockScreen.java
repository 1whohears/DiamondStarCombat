package com.onewhohears.dscombat.client.screen;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.common.container.menu.VehicleBlockContainerMenu;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toserver.ToServerCraftPlane;
import com.onewhohears.dscombat.crafting.VehicleRecipe;
import com.onewhohears.dscombat.data.vehicle.VehiclePresets;
import com.onewhohears.dscombat.data.vehicle.stats.VehicleStats;
import com.onewhohears.onewholibs.util.UtilItem;
import com.onewhohears.onewholibs.util.UtilMCText;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class VehicleBlockScreen extends AbstractContainerScreen<VehicleBlockContainerMenu> {
	
	private static final ResourceLocation BG_TEXTURE = new ResourceLocation(DSCombatMod.MODID,
			"textures/ui/vehicle_forge_ui.png");
	private final int bg_tex_size;
	
	private AircraftTab tab = AircraftTab.TANKS;
	private List<Integer> fails = new ArrayList<>();
	
	public VehicleBlockScreen(VehicleBlockContainerMenu menu, Inventory playerInv, Component title) {
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
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
		this.renderBookmark(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderIngredients(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderVehicle(guiGraphics, mouseX, mouseY, partialTicks);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.setShaderTexture(0, BG_TEXTURE);
		guiGraphics.blit(BG_TEXTURE, leftPos, topPos, 0, 0,
				imageWidth, imageHeight,
				bg_tex_size, bg_tex_size);
	}

	protected void renderBookmark(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.setShaderTexture(0, BG_TEXTURE);
		guiGraphics.blit(BG_TEXTURE, leftPos + tab.getBookmarkXPos(), topPos,
				352, 0, 7, 11,
				bg_tex_size, bg_tex_size);
	}

	protected void renderVehicle(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		VehicleRecipe ap = tab.getSelectedRecipe();
		if (ap == null) return;
		RegistryAccess registryAccess = Minecraft.getInstance().level.registryAccess(); // Get RegistryAccess
		ItemStack stack = ap.getResultItem(registryAccess);
		int posX = leftPos + 170, posY = topPos + 52;
		PoseStack modelViewStack = RenderSystem.getModelViewStack();
		modelViewStack.pushPose();
		float scale = 2f, scaleInv = 1f / scale;
		modelViewStack.scale(scale, scale, scale);
		float zOffset = 200f;  // Adjust this value if you need different rendering depth
		modelViewStack.translate((posX + 8) * scaleInv, (posY + 8) * scaleInv, zOffset);

		long time = Util.getMillis();
		float spinRate = 0.1f;
		modelViewStack.mulPose(Axis.YP.rotationDegrees(time * spinRate));
		modelViewStack.translate(-(posX + 8) * scaleInv, -(posY + 8) * scaleInv, -zOffset);
		guiGraphics.renderItem(stack, posX, posY);

		modelViewStack.popPose();
	}


	protected void renderIngredients(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		VehicleRecipe ap = tab.getSelectedRecipe();
		if (ap == null) return;
		Minecraft m = Minecraft.getInstance();
		NonNullList<Ingredient> ingredients = ap.getIngredients();

		for (int i = 0; i < getMenu().recipeSlots.getContainerSize(); ++i) {
			if (i < ingredients.size()) {
				ItemStack[] items = ingredients.get(i).getItems();
				ItemStack stack = items[(m.player.tickCount / 20) % items.length];
				getMenu().recipeSlots.setItem(i, stack);

				if (fails.contains(i)) {
					Slot slot = getMenu().getSlot(i);
					int left = leftPos + slot.x;
					int top = topPos + slot.y;
					guiGraphics.fill(left, top, left + 17, top + 17, 0x77ff0000); // Highlight failed ingredients in red
				}
			} else {
				getMenu().recipeSlots.setItem(i, ItemStack.EMPTY);
			}
		}
	}


	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		guiGraphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0x404040);

		VehicleRecipe ar = tab.getSelectedRecipe();
		if (ar == null) return;
		VehicleStats ap = ar.getVehicleStats();
		if (ap == null) return;

		Component name = ap.getDisplayNameComponent();
		int nameWidth = font.width(name);

		// Drawing the vehicle name
		guiGraphics.drawString(font, name, titleLabelX - nameWidth / 2 + 54, titleLabelY, 0x000000);

		CompoundTag data = ap.getDataAsNBT().getCompound("stats");
		float scale = 1f;

		guiGraphics.pose().scale(scale, scale, scale);

		float invScale = 1f / scale;
		int startX = (int) (293f * invScale);
		int startY = (int) (34f * invScale);
		int pColor = 0x4CFF00;

		guiGraphics.drawString(font, UtilMCText.literal("Health: " + data.getDouble("max_health")), startX, startY, pColor);
		startY += font.lineHeight;
		guiGraphics.drawString(font, UtilMCText.literal("Speed: " + (int)(data.getDouble("max_speed") * 20) + " m/s"), startX, startY, pColor);
		startY += font.lineHeight;
		guiGraphics.drawString(font, UtilMCText.literal("Mass: " + data.getDouble("mass")), startX, startY, pColor);
		startY += font.lineHeight;
		guiGraphics.drawString(font, UtilMCText.literal("Area: " + data.getDouble("cross_sec_area")), startX, startY, pColor);
		startY += font.lineHeight;
		guiGraphics.drawString(font, UtilMCText.literal("Stealth: " + data.getDouble("stealth")), startX, startY, pColor);
		startY += font.lineHeight;
		guiGraphics.drawString(font, UtilMCText.literal("Heat: " + data.getDouble("idleheat")), startX, startY, pColor);
		startY += font.lineHeight;
		guiGraphics.drawString(font, UtilMCText.literal("Armor: " + data.getFloat("base_armor")), startX, startY, pColor);
		startY += font.lineHeight;
		guiGraphics.drawString(font, UtilMCText.literal("Yaw: " + (int)(data.getDouble("maxyaw") * 20) + " d/s"), startX, startY, pColor);
		startY += font.lineHeight;
		guiGraphics.drawString(font, UtilMCText.literal("Pitch: " + (int)(data.getDouble("maxpitch") * 20) + " d/s"), startX, startY, pColor);
		startY += font.lineHeight;
		guiGraphics.drawString(font, UtilMCText.literal("Roll: " + (int)(data.getDouble("maxroll") * 20) + " d/s"), startX, startY, pColor);
		guiGraphics.pose().scale(1 / scale, 1 / scale, 1 / scale);
	}


	@Override
	protected void init() {
		super.init();
		// tabs
		ImageButton tankButton = new ImageButton(0, 0, 45, 20,
				83, 0, 300,
				BG_TEXTURE, 512, 512,
				onPress -> { tabButton(AircraftTab.TANKS); });
		tankButton.setX(leftPos + 83);
		tankButton.setY(topPos);
		addRenderableWidget(tankButton);
		ImageButton heliButton = new ImageButton(0, 0, 45, 20,
				130, 0, 300,
				BG_TEXTURE, 512, 512,
				onPress -> { tabButton(AircraftTab.HELIS); });
		heliButton.setX(leftPos + 130);
		heliButton.setY(topPos);
		addRenderableWidget(heliButton);
		ImageButton planeButton = new ImageButton(0, 0, 45, 20,
				177, 0, 300,
				BG_TEXTURE, 512, 512,
				onPress -> { tabButton(AircraftTab.PLANES); });
		planeButton.setX(leftPos + 177);
		planeButton.setY(topPos);
		addRenderableWidget(planeButton);
		Button boatButton = new ImageButton(0, 0, 45, 20,
				224, 0, 300,
				BG_TEXTURE, 512, 512,
				onPress -> { tabButton(AircraftTab.BOATS); });
		boatButton.setX(leftPos + 224);
		boatButton.setY(topPos);
		addRenderableWidget(boatButton);
		// prev
		Button prevButton = new ImageButton(0, 0, 41, 10,
				78, 52, 230,
				BG_TEXTURE, 512, 512,
				onPress -> { prevButton(); });
		prevButton.setX(leftPos+78);
		prevButton.setY(topPos+52);
		addRenderableWidget(prevButton);
		// next
		Button nextButton = new ImageButton(0, 0, 41, 10,
				233, 52, 230,
				BG_TEXTURE, 512, 512,
				onPress -> { nextButton(); });
		nextButton.setX(leftPos+233);
		nextButton.setY(topPos+52);
		addRenderableWidget(nextButton);
		// craft
		Button craftButton = Button.builder(UtilMCText.translatable("ui.dscombat.craft_button"),
				onPress -> { craftButton(); }).build();
		craftButton.setX(leftPos+140);
		craftButton.setX(topPos+86);
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
		VehicleRecipe ap = tab.getSelectedRecipe();
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
		TANKS(() -> VehiclePresets.get().getTankRecipes(Minecraft.getInstance().level.getRecipeManager()), 86),
		HELIS(() -> VehiclePresets.get().getHeliRecipes(Minecraft.getInstance().level.getRecipeManager()), 133),
		PLANES(() -> VehiclePresets.get().getPlaneRecipes(Minecraft.getInstance().level.getRecipeManager()), 180),
		BOATS(() -> VehiclePresets.get().getBoatRecipes(Minecraft.getInstance().level.getRecipeManager()), 227);
		
		private AircraftPresetList presetFactory;
		private int index = 0;
		private final int bookmarkX;
		
		private AircraftTab(AircraftPresetList presetFactory, int bookmarkX) {
			this.presetFactory = presetFactory;
			this.bookmarkX = bookmarkX;
		}
		
		public VehicleRecipe[] getRecipes() {
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
		public VehicleRecipe getSelectedRecipe() {
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
		VehicleRecipe[] get();
	}

}
