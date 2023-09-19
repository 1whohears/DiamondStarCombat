package com.onewhohears.dscombat.client.screen;

import java.util.List;

import javax.annotation.Nullable;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.common.container.AircraftBlockMenuContainer;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toserver.ToServerCraftPlane;
import com.onewhohears.dscombat.crafting.DSCIngredient;
import com.onewhohears.dscombat.data.aircraft.AircraftPreset;
import com.onewhohears.dscombat.data.aircraft.AircraftPresets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class AircraftBlockScreen extends AbstractContainerScreen<AircraftBlockMenuContainer> {
	
	private static final ResourceLocation BG_TEXTURE = new ResourceLocation(DSCombatMod.MODID,
			"textures/ui/vehicle_forge_ui.png");
	private final int bg_tex_size;
	
	private AircraftTab tab = AircraftTab.TANKS;
	
	public AircraftBlockScreen(AircraftBlockMenuContainer menu, Inventory playerInv, Component title) {
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
	}
	
	@Override
	protected void renderBg(PoseStack stack, float pTicks, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.setShaderTexture(0, BG_TEXTURE);
		blit(stack, leftPos, topPos, 0, 0, 
				imageWidth, imageHeight, 
				bg_tex_size, bg_tex_size);
	}
	
	protected void renderVehicle(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		AircraftPreset ap = tab.getSelectedPreset();
        if (ap == null) return;
		Minecraft m = Minecraft.getInstance();
        ItemStack stack = ap.getItem();
        // TODO 1.3 render 3d vehicle 
        m.getItemRenderer().renderAndDecorateItem(stack, leftPos+170, topPos+60);
	}
	
	protected void renderIngredients(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		AircraftPreset ap = tab.getSelectedPreset();
        if (ap == null) return;
		Minecraft m = Minecraft.getInstance();
        RenderSystem.enableBlend();
		List<DSCIngredient> ingredients = ap.getIngredients();
		int startX = leftPos+97, startY = topPos+122;
		int ix = startX, iy = startY;
		int space = 18;
		for (int i = 0; i < ingredients.size(); ++i) {
			ItemStack stack = ingredients.get(i).getDisplayItem();
			m.getItemRenderer().renderAndDecorateItem(stack, ix, iy);
			m.getItemRenderer().renderGuiItemDecorations(font, stack, ix, iy);
			if (mouseX >= ix && mouseX < (ix+space) && mouseY >= iy && mouseY < (iy+space)) renderTooltip(
					poseStack, 
					getTooltipFromItem(stack), 
					stack.getTooltipImage(), 
					mouseX, mouseY);
			ix += space;
		}
	}
	
	@Override
	protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
		font.draw(stack, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0x404040);
		// plane stats
		AircraftPreset ap = tab.getSelectedPreset();
		if (ap == null) return;
		font.draw(stack, ap.getDisplayNameComponent(), titleLabelX, titleLabelY, 0x000000);
		CompoundTag data = ap.getDataAsNBT();
		float scale = 0.8f;
		stack.scale(scale, scale, scale);
		float invScale = 1f / scale;
		int startX = (int)(294f * invScale);
		int startY = (int)(34f * invScale);
		int pColor = 0x4CFF00;
		font.draw(stack, Component.literal("Health: "+data.getDouble("max_health")), startX, startY, pColor);
		startY += font.lineHeight;
		font.draw(stack, Component.literal("Max Speed: "+(int)(data.getDouble("max_speed")*20)+" m/s"), startX, startY, pColor);
		startY += font.lineHeight;
		font.draw(stack, Component.literal("Mass: "+data.getDouble("mass")+" Anvils"), startX, startY, pColor);
		startY += font.lineHeight;
		font.draw(stack, Component.literal("Radar Absorption: "+data.getDouble("stealth")), startX, startY, pColor);
		startY += font.lineHeight;
		font.draw(stack, Component.literal("Idle Heat: "+data.getDouble("idleheat")), startX, startY, pColor);
		startY += font.lineHeight;
		font.draw(stack, Component.literal("Base Armor: "+data.getFloat("base_armor")), startX, startY, pColor);
		startY += font.lineHeight;
		font.draw(stack, Component.literal("Yaw: "+(int)(data.getDouble("maxyaw")*20)+" deg/sec"), startX, startY, pColor);
		startY += font.lineHeight;
		font.draw(stack, Component.literal("Pitch: "+(int)(data.getDouble("maxpitch")*20)+" deg/sec"), startX, startY, pColor);
		startY += font.lineHeight;
		font.draw(stack, Component.literal("Roll: "+(int)(data.getDouble("maxroll")*20)+" deg/sec"), startX, startY, pColor);
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
				Component.translatable("ui.dscombat.craft_button"), 
				onPress -> { craftButton(); });
		craftButton.x = leftPos+140;
		craftButton.y = topPos+86;
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
	
	private void tabButton(AircraftTab tab) {
		this.tab = tab;
	}
	
	private void prevButton() {
		tab.cycleIndexLeft();
	}
	
	private void nextButton() {
		tab.cycleIndexRight();
	}
	
	private void craftButton() {
		Minecraft m = Minecraft.getInstance();
		Player player = m.player;
		if (player == null) return;
		AircraftPreset ap = tab.getSelectedPreset();
		if (ap == null) return;
		if (DSCIngredient.hasIngredients(ap.getIngredients(), player.getInventory())) {
			PacketHandler.INSTANCE.sendToServer(new ToServerCraftPlane(ap.getId(), menu.getPos()));
		} else {
			player.displayClientMessage(Component.translatable("error.dscombat.cant_craft"), true);
			minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.VILLAGER_NO, 1.0F));
		}
	}
	
	public static enum AircraftTab {
		TANKS(() -> AircraftPresets.get().getCraftableTanks()),
		HELIS(() -> AircraftPresets.get().getCraftableHelis()),
		PLANES(() -> AircraftPresets.get().getCraftablePlanes()),
		BOATS(() -> AircraftPresets.get().getCraftableBoats());
		
		private AircraftPresetList presetFactory;
		private int index = 0;
		
		private AircraftTab(AircraftPresetList presetFactory) {
			this.presetFactory = presetFactory;
		}
		
		public AircraftPreset[] getPresets() {
			return presetFactory.get();
		}
		
		public int getIndex() {
			return checkIndex();
		}
		
		private int checkIndex() {
			if (getPresets().length == 0) index = -1;
			else if (index >= getPresets().length) index = getPresets().length-1;
			else if (index < 0) index = 0;
			return index;
		}
		
		@Nullable
		public AircraftPreset getSelectedPreset() {
			if (checkIndex() == -1) return null;
			return getPresets()[getIndex()];
		}
		
		public int cycleIndexRight() {
			++index;
			if (index >= getPresets().length) index = 0;
			return index;
		}
		
		public int cycleIndexLeft() {
			--index;
			if (index < 0) index = getPresets().length-1;
			return index;
		}
		
	}
	
	public static interface AircraftPresetList {
		AircraftPreset[] get();
	}

}
