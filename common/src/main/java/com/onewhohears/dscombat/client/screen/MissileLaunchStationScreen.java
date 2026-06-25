package com.onewhohears.dscombat.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.common.container.menu.MissileLaunchStationContainerMenu;
import com.onewhohears.dscombat.common.network.toserver.ToServerMissileStationLaunch;
import com.onewhohears.dscombat.common.network.toserver.ToServerMissileStationSetTarget;
import com.onewhohears.dscombat.common.network.toserver.ToServerMissileStationToggleArmed;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MissileLaunchStationScreen extends AbstractContainerScreen<MissileLaunchStationContainerMenu> {
	
	private static final ResourceLocation BG_TEXTURE = new ResourceLocation(DSCombatMod.MODID,
			"textures/gui/missile_launch_station.png");
	
	private Button launchButton;
	private Button armButton;
	private EditBox targetXField;
	private EditBox targetYField;
	private EditBox targetZField;
	private Button setTargetButton;
	
	public MissileLaunchStationScreen(MissileLaunchStationContainerMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
		this.imageWidth = 280; // Wider to fit info panel on the right
		this.imageHeight = 190;
	}

	@Override
	protected void init() {
		super.init();
		
		// Arm/Disarm button (справа вверху)
		armButton = Button.builder(
			Component.literal("ARM"),
			button -> {
				new ToServerMissileStationToggleArmed(menu.getPos()).sendToServer();
			}
		).bounds(leftPos + 130, topPos + 20, 70, 20).build();
		addRenderableWidget(armButton);
		
		// Launch button (между координатами и DISARM, в центре)
		launchButton = Button.builder(
			Component.literal("LAUNCH"),
			button -> {
				new ToServerMissileStationLaunch(menu.getPos()).sendToServer();
			}
		).bounds(leftPos + 30, topPos + 20, 70, 18).build();
		addRenderableWidget(launchButton);
		
		// Target coordinate input fields (в ряд)
		int fieldX = leftPos + 30;
		int fieldY = topPos + 50;
		int fieldWidth = 45;
		int fieldSpacing = 50; // Расстояние между полями
		
		targetXField = new EditBox(font, fieldX, fieldY, fieldWidth, 12, Component.literal("X"));
		targetXField.setValue(String.valueOf((int)menu.getTargetX()));
		targetXField.setMaxLength(10);
		targetXField.setBordered(true);
		addRenderableWidget(targetXField);
		
		targetYField = new EditBox(font, fieldX + fieldSpacing, fieldY, fieldWidth, 12, Component.literal("Y"));
		targetYField.setValue(String.valueOf((int)menu.getTargetY()));
		targetYField.setMaxLength(10);
		targetYField.setBordered(true);
		addRenderableWidget(targetYField);
		
		targetZField = new EditBox(font, fieldX + fieldSpacing * 2, fieldY, fieldWidth, 12, Component.literal("Z"));
		targetZField.setValue(String.valueOf((int)menu.getTargetZ()));
		targetZField.setMaxLength(10);
		targetZField.setBordered(true);
		addRenderableWidget(targetZField);
		
		// Set target button (по центру под полями координат)
		setTargetButton = Button.builder(
			Component.literal("SET"),
			button -> {
				try {
					double x = Double.parseDouble(targetXField.getValue());
					double y = Double.parseDouble(targetYField.getValue());
					double z = Double.parseDouble(targetZField.getValue());
					new ToServerMissileStationSetTarget(menu.getPos(), x, y, z).sendToServer();
				} catch (NumberFormatException e) {
					// Invalid input, ignore
				}
			}
		).bounds(leftPos + 80, topPos + 67, 40, 15).build();
		addRenderableWidget(setTargetButton);
	}

	@Override
	protected void containerTick() {
		super.containerTick();
		
		// Update button states
		boolean armed = menu.isArmed();
		armButton.setMessage(Component.literal(armed ? "DISARM" : "ARM"));
		
		int cooldown = menu.getLaunchCooldown();
		launchButton.active = armed && cooldown == 0;
		
		// Update text fields if not focused
		if (!targetXField.isFocused()) {
			targetXField.setValue(String.valueOf((int)menu.getTargetX()));
		}
		if (!targetYField.isFocused()) {
			targetYField.setValue(String.valueOf((int)menu.getTargetY()));
		}
		if (!targetZField.isFocused()) {
			targetZField.setValue(String.valueOf((int)menu.getTargetZ()));
		}
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;
		
		// Draw a solid gray background if texture is missing
		graphics.fill(x, y, x + imageWidth, y + imageHeight, 0xFFC6C6C6);
		
		// Draw darker border
		graphics.fill(x, y, x + imageWidth, y + 1, 0xFF8B8B8B); // Top
		graphics.fill(x, y, x + 1, y + imageHeight, 0xFF8B8B8B); // Left
		graphics.fill(x + imageWidth - 1, y, x + imageWidth, y + imageHeight, 0xFFFFFFFF); // Right
		graphics.fill(x, y + imageHeight - 1, x + imageWidth, y + imageHeight, 0xFFFFFFFF); // Bottom
		
		// Draw separator line between launcher interface and player inventory
		int separatorY = y + 90;
		graphics.fill(x + 4, separatorY, x + 220, separatorY + 1, 0xFF8B8B8B);

		// Vertical separator between main GUI and info panel
		graphics.fill(x + 222, y + 4, x + 223, y + imageHeight - 4, 0xFF8B8B8B);

		int slotX = x + 8;
		int slotY = y + 20;
		graphics.fill(slotX - 1, slotY - 1, slotX + 17, slotY + 17, 0xFF8B8B8B);
		graphics.fill(slotX, slotY, slotX + 16, slotY + 16, 0xFF373737);
		
		// Draw player inventory slots background (standard position)
		int invStartX = x + 8;
		int invStartY = y + 108; // Ниже, чтобы был больше интерфейс пусковой
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 9; col++) {
				int invSlotX = invStartX + col * 18;
				int invSlotY = invStartY + row * 18;
				graphics.fill(invSlotX - 1, invSlotY - 1, invSlotX + 17, invSlotY + 17, 0xFF8B8B8B);
				graphics.fill(invSlotX, invSlotY, invSlotX + 16, invSlotY + 16, 0xFF373737);
			}
		}
		
		// Draw hotbar slots background (standard position)
		for (int col = 0; col < 9; col++) {
			int hotbarSlotX = invStartX + col * 18;
			int hotbarSlotY = invStartY + 58;
			graphics.fill(hotbarSlotX - 1, hotbarSlotY - 1, hotbarSlotX + 17, hotbarSlotY + 17, 0xFF8B8B8B);
			graphics.fill(hotbarSlotX, hotbarSlotY, hotbarSlotX + 16, hotbarSlotY + 16, 0xFF373737);
		}
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		// Render a full screen background to hide the world/inventory behind
		this.renderBackground(graphics);
		super.render(graphics, mouseX, mouseY, partialTick);
		
		// Render text fields on top of everything
		targetXField.render(graphics, mouseX, mouseY, partialTick);
		targetYField.render(graphics, mouseX, mouseY, partialTick);
		targetZField.render(graphics, mouseX, mouseY, partialTick);
		
		renderTooltip(graphics, mouseX, mouseY);
		
		// Render labels for coordinate fields (X, Y, Z над полями)
		int labelX = leftPos + 30;
		int labelY = topPos + 38;
		graphics.drawString(font, "X:", labelX + 18, labelY, 0x404040, false);
		graphics.drawString(font, "Y:", labelX + 68, labelY, 0x404040, false);
		graphics.drawString(font, "Z:", labelX + 118, labelY, 0x404040, false);
		
		// Render cooldown
		int cooldown = menu.getLaunchCooldown();
		if (cooldown > 0) {
			graphics.drawString(font, "Cooldown: " + (cooldown / 20) + "s", leftPos + 130, topPos + 70, 0xFF0000, false);
		}
		
		// Render armed status
		if (menu.isArmed()) {
			graphics.drawString(font, "ARMED", leftPos + 145, topPos + 10, 0xFF0000, false);
		}

		// ---- Info panel (right side of inventory) ----
		renderInfoPanel(graphics);
	}

	private void renderInfoPanel(GuiGraphics graphics) {
		// Panel starts at x = leftPos + 228, same top as main GUI
		int px = leftPos + 228;
		int py = topPos + 8;
		int pw = 44;
		int ph = 80;

		// Panel background
		graphics.fill(px, py, px + pw, py + ph, 0xFF2A2A2A);
		// Border
		graphics.fill(px,        py,        px + pw, py + 1,  0xFF8B8B8B);
		graphics.fill(px,        py + ph - 1, px + pw, py + ph, 0xFF8B8B8B);
		graphics.fill(px,        py,        px + 1,  py + ph, 0xFF8B8B8B);
		graphics.fill(px + pw - 1, py,      px + pw, py + ph, 0xFF8B8B8B);

		// Title
		graphics.drawString(font, "MISSILE", px + 4, py + 4, 0xFFFFAA00, false);
		graphics.drawString(font, "INFO",    px + 10, py + 13, 0xFFFFAA00, false);

		// Separator
		graphics.fill(px + 2, py + 22, px + pw - 2, py + 23, 0xFF555555);

		int dist = menu.getEstimatedDistance();
		int ticks = menu.getEstimatedFlightTicks();

		if (dist == 0 && ticks == 0) {
			// No missile loaded
			graphics.drawString(font, "No", px + 14, py + 28, 0xFF888888, false);
			graphics.drawString(font, "missile", px + 4, py + 37, 0xFF888888, false);
		} else {
			// Distance
			graphics.drawString(font, "DIST:", px + 4, py + 28, 0xFFAAAAAA, false);
			graphics.drawString(font, dist + "m", px + 4, py + 37, 0xFFFFFFFF, false);

			// Separator
			graphics.fill(px + 2, py + 47, px + pw - 2, py + 48, 0xFF444444);

			// Time
			graphics.drawString(font, "TIME:", px + 4, py + 51, 0xFFAAAAAA, false);
			// Show as seconds with one decimal
			String timeStr = String.format("%.1fs", ticks / 20.0f);
			graphics.drawString(font, timeStr, px + 4, py + 60, 0xFFFFFFFF, false);
		}
	}

	@Override
	protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
		graphics.drawString(font, title, titleLabelX, titleLabelY, 0x404040, false);
		graphics.drawString(font, this.playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0x404040, false);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		// Handle text field clicks first
		if (targetXField.mouseClicked(mouseX, mouseY, button)) {
			setFocused(targetXField);
			return true;
		}
		if (targetYField.mouseClicked(mouseX, mouseY, button)) {
			setFocused(targetYField);
			return true;
		}
		if (targetZField.mouseClicked(mouseX, mouseY, button)) {
			setFocused(targetZField);
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		// Allow text field input
		if (targetXField.isFocused() || targetYField.isFocused() || targetZField.isFocused()) {
			if (targetXField.keyPressed(keyCode, scanCode, modifiers) 
				|| targetYField.keyPressed(keyCode, scanCode, modifiers)
				|| targetZField.keyPressed(keyCode, scanCode, modifiers)) {
				return true;
			}
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean charTyped(char codePoint, int modifiers) {
		// Allow text field input
		if (targetXField.isFocused() || targetYField.isFocused() || targetZField.isFocused()) {
			if (targetXField.charTyped(codePoint, modifiers)
				|| targetYField.charTyped(codePoint, modifiers)
				|| targetZField.charTyped(codePoint, modifiers)) {
				return true;
			}
		}
		return super.charTyped(codePoint, modifiers);
	}
}
