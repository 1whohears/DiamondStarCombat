package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.jetbrains.annotations.NotNull;

import static com.onewhohears.dscombat.client.overlay.components.VehicleControlOverlay.STICK_BASE_SIZE;

// TODO: redo texture
public class VehicleThrottleOverlay extends VehicleOverlayComponent {
    public static final ResourceLocation THROTTLE_RAIL = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/throttle_rail.png");
    public static final ResourceLocation THROTTLE_HANDLE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/throttle_handle.png");

    public static final int THROTTLE_RAIL_LENGTH = 70, THROTTLE_WIDTH = 10, THROTTLE_KNOB_HEIGHT = 10;

    @Override
    protected boolean shouldRender(ForgeGui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
        if (defaultRenderConditions()) return false;
        return getPlayerRootVehicle() instanceof EntityVehicle;
    }

    @Override
    protected void render(ForgeGui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
        EntityVehicle vehicle = (EntityVehicle) getPlayerRootVehicle();
        assert vehicle != null;

        int xOrigin = screenWidth - STICK_BASE_SIZE - PADDING - THROTTLE_WIDTH - PADDING;
        int yOrigin = screenHeight - THROTTLE_RAIL_LENGTH - PADDING;
        RenderSystem.setShaderTexture(0, THROTTLE_RAIL);
        blit(poseStack,
                xOrigin, yOrigin,
                0, 0,
                THROTTLE_WIDTH, THROTTLE_RAIL_LENGTH,
                THROTTLE_WIDTH, THROTTLE_RAIL_LENGTH);
        RenderSystem.setShaderTexture(0, THROTTLE_HANDLE);
        int throttleYPos = yOrigin+ THROTTLE_RAIL_LENGTH - THROTTLE_KNOB_HEIGHT;
        int throttleLength = THROTTLE_RAIL_LENGTH - THROTTLE_KNOB_HEIGHT;
        float throttle = vehicle.getCurrentThrottle();
        if (vehicle.getStats().negativeThrottle) throttleYPos = throttleYPos-throttleLength/2-(int)(throttle*throttleLength/2);
        else throttleYPos = throttleYPos-(int)(throttle*throttleLength);
        blit(poseStack,
                xOrigin, throttleYPos,
                0, 0,
                THROTTLE_WIDTH, THROTTLE_KNOB_HEIGHT,
                THROTTLE_WIDTH, THROTTLE_KNOB_HEIGHT);
    }

    @Override
    protected @NotNull String componentId() {
        return "dscombat_throttle";
    }
}
