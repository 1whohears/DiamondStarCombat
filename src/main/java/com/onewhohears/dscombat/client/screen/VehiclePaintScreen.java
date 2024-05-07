package com.onewhohears.dscombat.client.screen;

import java.util.function.Consumer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.vehicle.VehicleTextureManager;
import com.onewhohears.dscombat.data.vehicle.VehicleTextureManager.BlendMode;
import com.onewhohears.dscombat.util.UtilMCText;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.CycleButton.OnValueChange;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

public class VehiclePaintScreen extends Screen {
	
	public static final ResourceLocation BG_TEXTURE = new ResourceLocation(DSCombatMod.MODID,
			"textures/ui/paintjob_screen.png");
	
	private static final int imageWidth = 176, imageHeight = 136;
	private static final int textureSize = 256;
	
	public final VehicleTextureManager textures;
	protected int guiX, guiY;
	
	public VehiclePaintScreen(VehicleTextureManager textures) {
		super(UtilMCText.literal("Vehicle Paint Job"));
		this.textures = textures;
	}
	
	@Override
	protected void init() {
		super.init();
		guiX = width/2-imageWidth/2;
		guiY = height/2-imageHeight/2;
		int widgetX = guiX + 4, widgetY = guiY + 4;
		addRenderableWidget(CycleButton.<Integer>builder((base) -> UtilMCText.literal(base+""))
				.withValues(count(textures.getBaseTextureNum()))
				.withInitialValue(textures.getBaseTextureIndex())
				.create(widgetX, widgetY, 168, 20, 
						UtilMCText.literal("Base"), 
					onBaseChange()));
		int layerX = widgetX, layerY = widgetY + 20;
		for (int i = 0; i < textures.getTextureLayers().length; ++i) {
			addRenderableWidget(CycleButton.onOffBuilder(textures.getTextureLayers()[i].canRender())
				.create(layerX, layerY, 44, 20, 
						UtilMCText.literal("See"), 
					onRenderLayerToggle(i)));
			addRenderableWidget(CycleButton.<BlendMode>builder((mode) -> UtilMCText.literal(mode.name()))
				.withValues(BlendMode.values())
				.withInitialValue(textures.getTextureLayers()[i].getBlendMode())
				.create(layerX+44, layerY, 74, 20, 
						UtilMCText.literal("Mix"), 
					onBlendModeChange(i)));
			EditBox colorBox = new EditBox(minecraft.font, layerX+118, layerY, 
					50, 20, UtilMCText.empty());
			colorBox.setValue(UtilParse.toColorString(textures.getTextureLayers()[i].getColor()));
			colorBox.setTextColor(textures.getTextureLayers()[i].getColorInt());
			colorBox.setResponder(layerColorBoxResponder(colorBox, i));
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
	
	private OnValueChange<BlendMode> onBlendModeChange(int layer) {
		return (button, mode) -> textures.getTextureLayers()[layer].setBlendMode(mode);
	}
	
	private Consumer<String> layerColorBoxResponder(EditBox colorBox, int layer) {
		return (color) -> {
			textures.getTextureLayers()[layer].setColor(color);
			colorBox.setTextColor(textures.getTextureLayers()[layer].getColorInt());
		};
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
			imageWidth, imageHeight, textureSize, textureSize);
	}

}
