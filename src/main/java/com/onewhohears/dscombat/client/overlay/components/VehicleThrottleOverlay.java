package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import net.minecraft.resources.ResourceLocation;

import static com.onewhohears.dscombat.client.overlay.components.VehicleControlOverlay.STICK_BASE_SIZE;

public class VehicleThrottleOverlay extends VehicleOverlayComponent {
    public static final ResourceLocation THROTTLE_RAIL = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/throttle_rail.png");
    public static final ResourceLocation THROTTLE_HANDLE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/throttle_handle.png");

    public static final int THROTTLE_RAIL_LENGTH = 70, THROTTLE_WIDTH = 10, THROTTLE_KNOB_HEIGHT = 10;

    public VehicleThrottleOverlay(PoseStack poseStack, int screenWidth, int screenHeight) {
        super(poseStack, screenWidth, screenHeight);
    }

    @Override
    public void render(PoseStack poseStack, int screenWidth, int screenHeight) {
        if (!(getPlayerVehicle() instanceof EntityVehicle vehicle)) return;

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
        if (vehicle.negativeThrottle) throttleYPos = throttleYPos-throttleLength/2-(int)(throttle*throttleLength/2);
        else throttleYPos = throttleYPos-(int)(throttle*throttleLength);
        blit(poseStack,
                xOrigin, throttleYPos,
                0, 0,
                THROTTLE_WIDTH, THROTTLE_KNOB_HEIGHT,
                THROTTLE_WIDTH, THROTTLE_KNOB_HEIGHT);
    }
}
