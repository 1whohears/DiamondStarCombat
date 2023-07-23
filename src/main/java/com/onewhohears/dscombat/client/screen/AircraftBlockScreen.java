package com.onewhohears.dscombat.client.screen;

import java.util.List;

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
			"textures/ui/aircraft_screen.png");
	
	private static final int buttonNum = 7;
	
	private final int maxTab;
	private int tabIndex = 0;
	private int planeIndex = 0;
	
	public AircraftBlockScreen(AircraftBlockMenuContainer menu, Inventory playerInv, Component title) {
		super(menu, playerInv, title);
		this.leftPos = 0;
		this.topPos = 0;
		this.imageWidth = 256;
		this.imageHeight = 256;
		maxTab = (int)((float)AircraftPresets.get().getCraftablePresetNum() / (float)buttonNum);
	}
	
	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(poseStack);
		super.render(poseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(poseStack, mouseX, mouseY);
        Minecraft m = Minecraft.getInstance();
        RenderSystem.enableBlend();
        if (AircraftPresets.get().getCraftablePresetNum() == 0) return;
        // render plane item options
        int startX = getGuiLeft() + titleLabelX;
		int startY = getGuiTop() + titleLabelY;
		int wx = startX + 51;
		int wy = startY + 12;
		for (int i = 0; i < buttonNum; ++i) {
			int index = tabIndex * buttonNum + i;
			if (index >= AircraftPresets.get().getCraftablePresetNum()) break;
			ItemStack stack = AircraftPresets.get().getCraftablePresets()[index].getItem();
			m.getItemRenderer().renderAndDecorateItem(
					stack, wx, wy);
			m.getItemRenderer().renderGuiItemDecorations(font,
					stack, wx, wy);
			wx += 20;
		}
		// render ingredients
		List<DSCIngredient> ingredients = AircraftPresets.get().getCraftablePresets()[planeIndex].getIngredients();
		int iix = startX + 122;
		int ix = iix;
		int iy = startY + 40;
		int space = 18;
		for (int i = 0; i < ingredients.size(); ++i) {
			if (i != 0 && i % 4 == 0) {
				ix = iix;
				iy += space;
			}
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
	protected void renderBg(PoseStack stack, float pTicks, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.setShaderTexture(0, BG_TEXTURE);
		blit(stack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
	}
	
	@Override
	protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
		font.draw(stack, title, titleLabelX+38, titleLabelY, 0x404040);
		font.draw(stack, playerInventoryTitle, inventoryLabelX+38, inventoryLabelY+56, 0x404040);
		//font.draw(stack, Component.translatable("ui.dscombat.ingredients"), titleLabelX+122, titleLabelY+34, 0x00aa00);
		// plane stats
		if (AircraftPresets.get().getCraftablePresetNum() == 0) return;
		AircraftPreset ap = AircraftPresets.get().getCraftablePresets()[planeIndex];
		font.draw(stack, ap.getDisplayNameComponent(), titleLabelX+38, titleLabelY+34, 0x000000);
		CompoundTag data = ap.getDataAsNBT();
		float scale = 0.5f;
		stack.scale(scale, scale, scale);
		float invScale = 1f / scale;
		int startX = (int)((float)(titleLabelX+38) * invScale);
		int startY = (int)((float)(titleLabelY+43) * invScale);
		int initY = startY;
		// FIXME 6 fix aircraft preset display stats
		font.draw(stack, Component.literal("Health: "+data.getDouble("max_health")), startX, startY, 0x404040);
		startY += font.lineHeight;
		font.draw(stack, Component.literal("Speed: "+data.getDouble("max_speed")), startX, startY, 0x404040);
		startY += font.lineHeight;
		font.draw(stack, Component.literal("Weight: "+data.getDouble("weight")), startX, startY, 0x404040);
		startY += font.lineHeight;
		font.draw(stack, Component.literal("Wing Area: "+data.getDouble("surfacearea")), startX, startY, 0x404040);
		startY = initY;
		startX += 80;
		font.draw(stack, Component.literal("Stealth: "+data.getDouble("stealth")), startX, startY, 0x404040);
		startY += font.lineHeight;
		font.draw(stack, Component.literal("Heat: "+data.getDouble("idleheat")), startX, startY, 0x404040);
		startY += font.lineHeight;
		font.draw(stack, Component.literal("Yaw Rate: "+data.getDouble("maxyaw")), startX, startY, 0x404040);
		startY += font.lineHeight;
		font.draw(stack, Component.literal("Pitch Rate: "+data.getDouble("maxpitch")), startX, startY, 0x404040);
		startY += font.lineHeight;
		font.draw(stack, Component.literal("Roll Rate: "+data.getDouble("maxroll")), startX, startY, 0x404040);
		stack.scale(1/scale, 1/scale, 1/scale);
		// HOW 2 display plane model
		//Minecraft m = Minecraft.getInstance();
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
					onPress -> { planeButton(c); });
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
	
	private void planeButton(int num) {
		int max = AircraftPresets.get().getCraftablePresetNum();
		int a = tabIndex * buttonNum + num;
		if (a >= max) planeIndex = max-1;
		else planeIndex = a;
	}
	
	private void craftButton() {
		Minecraft m = Minecraft.getInstance();
		Player player = m.player;
		if (player == null) return;
		AircraftPreset ap = AircraftPresets.get().getCraftablePresets()[planeIndex];
		if (DSCIngredient.hasIngredients(ap.getIngredients(), player.getInventory())) {
			PacketHandler.INSTANCE.sendToServer(new ToServerCraftPlane(ap.getId(), menu.getPos()));
		} else {
			player.displayClientMessage(Component.translatable("error.dscombat.cant_craft"), true);
			minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.VILLAGER_NO, 1.0F));
		}
	}

}
