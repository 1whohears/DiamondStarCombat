package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * Renders custom crosshairs for turrets when zoomed in.
 * Crosshair textures are loaded from assets/dscombat/textures/ui/crosshairs/{crosshair_id}.png
 * 
 * The crosshair texture is rendered at its native size in the center of the screen.
 * The rest of the screen is filled with black to create a scope effect.
 * 
 * Recommended size: 256x256 pixels (will be displayed as 256x256 on screen).
 * 
 * To add custom crosshairs:
 * 1. Create a 256x256 PNG file with transparency
 * 2. Save in common/src/main/resources/assets/dscombat/textures/ui/crosshairs/
 * 3. Set the "crosshair" field in turret JSON to match the filename (without .png)
 * 4. Set the "zoom" field to a value greater than 1.0 (e.g., 1.5 for 1.5x zoom)
 * 5. Press C (zoom key) while in the turret to see the crosshair
 * 
 * Example: "crosshair": "sniper", "zoom": 2.0 will load crosshairs/sniper.png with 2x zoom
 * 
 * Note: 
 * - If "crosshair" field is not set or empty, no crosshair will be rendered
 * - If "zoom" field is not set or equals 1.0, zoom will not be applied
 * - Both fields must be properly set for the zoom and crosshair to work
 */
public class TurretCrosshairOverlay extends VehicleOverlayComponent {
    
    private static final int CROSSHAIR_SIZE = 256; // Native size of crosshair texture
    
    @Override
    protected boolean shouldRender(Gui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        if (defaultRenderConditions()) return false;
        
        // Only render when player is in a turret and zoom is active
        if (!(getPlayerVehicle() instanceof EntityTurret turret)) return false;
        
        // Only render if turret has a crosshair defined (not null and not empty)
        String crosshairType = turret.getStats().getCrosshair();
        if (crosshairType == null || crosshairType.isEmpty()) return false;
        
        return DSCClientInputs.isZoomIn();
    }
    
    @Override
    protected void render(Gui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        EntityTurret turret = (EntityTurret) getPlayerVehicle();
        if (turret == null) return;
        
        String crosshairType = turret.getStats().getCrosshair();
        if (crosshairType == null || crosshairType.isEmpty()) return;
        
        // Build resource location for crosshair texture
        ResourceLocation crosshairTexture = new ResourceLocation(
            DSCombatMod.MODID, 
            "textures/ui/crosshairs/" + crosshairType + ".png"
        );
        
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        
        // Center the crosshair on screen at native size (no stretching)
        int x = (screenWidth - CROSSHAIR_SIZE) / 2;
        int y = (screenHeight - CROSSHAIR_SIZE) / 2;
        
        // Render crosshair at native size
        RenderSystem.setShaderTexture(0, crosshairTexture);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        
        // Draw texture at native size (256x256) - no stretching
        graphics.blit(crosshairTexture,
            x, y,                           // position (centered)
            0, 0,                           // texture offset
            CROSSHAIR_SIZE, CROSSHAIR_SIZE, // render size (native)
            CROSSHAIR_SIZE, CROSSHAIR_SIZE);// texture size (native)
        
        RenderSystem.disableBlend();
        poseStack.popPose();
    }
    
    @Override
    protected @NotNull String componentId() {
        return "dscombat_turret_crosshair";
    }
}
