package com.onewhohears.dscombat.client.screen;

import java.util.function.Consumer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.aircraft.VehicleTextureManager;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.CycleButton.OnValueChange;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class VehiclePaintScreen extends Screen {
	
	public static final ResourceLocation BG_TEXTURE = new ResourceLocation(DSCombatMod.MODID,
			"textures/ui/paintjob_screen.png");
	
	public final VehicleTextureManager textures;
	protected int guiX, guiY;
	
	public VehiclePaintScreen(VehicleTextureManager textures) {
		super(Component.literal("Vehicle Paint Job"));
		this.textures = textures;
		this.width = 176;
		this.height = 136;
		this.guiX = 40;
		this.guiY = 10;
	}
	
	@Override
	protected void init() {
		super.init();
		addRenderableWidget(CycleButton.<Integer>builder((base) -> Component.literal(base+""))
				.withValues(count(textures.getBaseTextureNum()))
				.withInitialValue(textures.getBaseTextureIndex())
				.create(guiX+6, guiY+4, 100, 20, 
					Component.literal("Base"), 
					onBaseChange()));
		int layerX = guiX+120, layerY = guiY+4;
		for (int i = 0; i < textures.getTextureLayers().length; ++i) {
			addRenderableWidget(CycleButton.onOffBuilder(textures.getTextureLayers()[i].canRender())
				.create(layerX, layerY, 60, 20, 
					Component.literal("Show"), 
					onRenderLayerToggle(i)));
			EditBox colorBox = new EditBox(minecraft.font, layerX+60, layerY, 
					100, 20, Component.empty());
			colorBox.setValue(UtilParse.toColorString(textures.getTextureLayers()[i].getColor()));
			colorBox.setResponder(layerColorBoxResponder(i));
			addRenderableWidget(colorBox);
			layerY += 20;
		}
	}
	
	private OnValueChange<Integer> onBaseChange() {
		return (button, base) -> textures.setBaseTexture(base);
	}
	
	private OnValueChange<Boolean> onRenderLayerToggle(int layer) {
		return (button, render) -> textures.getTextureLayers()[layer].setCanRender(render);
	}
	
	private Consumer<String> layerColorBoxResponder(int layer) {
		return (color) -> textures.getTextureLayers()[layer].setColor(color);
	}
	
	private Integer[] count(int num) {
		Integer[] r = new Integer[num];
		for (int i = 0; i < num; ++i) r[i] = i;
		return r;
	}
	
	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
		renderBackground(poseStack);
		super.render(poseStack, mouseX, mouseY, partialTick);
	}
	
	@Override
	public void renderBackground(PoseStack poseStack, int vOffset) {
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.setShaderTexture(0, BG_TEXTURE);
		blit(poseStack, guiX, guiY, 0, 0, 
				width, height, width, height);
				//width, height, 256, 256);
	}

}
