package com.onewhohears.dscombat.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.weapon.WeaponSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class WeaponButton extends Button {

    private static final int TEXTURE_WIDTH = 93, TEXTURE_HEIGHT = 24;
    private static final ResourceLocation BG_TEX = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/weapon_icons/weapon_tab.png");
    private static final ResourceLocation SELECTED_TEX = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/weapon_icons/selection_box.png");

    private final ResourceLocation texture;
    private final WeaponSystem system;
    private final int weaponIndex;

    public WeaponButton(int x, int y, int width, int height, ResourceLocation texture,
                        OnPress onPress, MutableComponent name, WeaponSystem system, int weaponIndex) {
        super(x, y, width, height, name.append(" "+system.getWeapons().get(weaponIndex).getCurrentAmmo()),
                onPress, Supplier::get);
        this.texture = texture;
        this.system = system;
        this.weaponIndex = weaponIndex;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderTexture(0, BG_TEX);
        blit(BG_TEX, graphics);
        RenderSystem.setShaderTexture(0, texture);
        blit(texture, graphics);
        Font font = Minecraft.getInstance().font;
        int textXOffset = (width - font.width(getMessage().getString())) / 2;
        int textYOffset = (height - 10) / 2;
        int color = 0xFFFFFF;
        if (system.getSelectedIndex() == weaponIndex) {
            RenderSystem.setShaderTexture(0, SELECTED_TEX);
            blit(SELECTED_TEX, graphics);
            color = 0xFCEC3E;
        }
        graphics.drawString(font, getMessage(), getX()+textXOffset, getY()+textYOffset, color);
    }

    private void blit(ResourceLocation texture, GuiGraphics graphics) {
        graphics.blit(texture, getX(), getY(), width, height, 0, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
    }
}
