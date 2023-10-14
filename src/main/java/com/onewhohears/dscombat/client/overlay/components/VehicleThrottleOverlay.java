package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import net.minecraft.resources.ResourceLocation;

import static com.onewhohears.dscombat.client.overlay.components.VehicleControlOverlay.STICK_BASE_SIZE;

public class VehicleThrottleOverlay extends VehicleOverlayComponent {
    public static final ResourceLocation THROTTLE_RAIL = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/throttle_rail.png");
    public static final ResourceLocation THROTTLE_HANDLE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/throttle_handle.png");

    public static final int THROTTLE_RAIL_LENGTH = 70, THROTTLE_WIDTH = 10, THROTTLE_KNOB_HEIGHT = 10;

    public VehicleThrottleOverlay(int screenWidth, int screenHeight) {
        super(screenWidth, screenHeight);
    }

    @Override
    public void render(PoseStack poseStack) {
        if (!(getPlayerVehicle() instanceof EntityAircraft vehicle)) return;

        int x = this.screenWidth - STICK_BASE_SIZE - PADDING - THROTTLE_WIDTH - PADDING;
        int y = this.screenHeight - THROTTLE_RAIL_LENGTH - PADDING;
        RenderSystem.setShaderTexture(0, THROTTLE_RAIL);
        blit(poseStack,
                x, y,
                0, 0,
                THROTTLE_WIDTH, THROTTLE_RAIL_LENGTH,
                THROTTLE_WIDTH, THROTTLE_RAIL_LENGTH);
        RenderSystem.setShaderTexture(0, THROTTLE_HANDLE);
        int sy = y+ THROTTLE_RAIL_LENGTH - THROTTLE_KNOB_HEIGHT;
        int l = THROTTLE_RAIL_LENGTH - THROTTLE_KNOB_HEIGHT;
        float th = vehicle.getCurrentThrottle();
        if (vehicle.negativeThrottle) sy = sy-l/2-(int)(th*l/2);
        else sy = sy-(int)(th*l);
        blit(poseStack,
                x, sy,
                0, 0,
                THROTTLE_WIDTH, THROTTLE_KNOB_HEIGHT,
                THROTTLE_WIDTH, THROTTLE_KNOB_HEIGHT);
    }
}
