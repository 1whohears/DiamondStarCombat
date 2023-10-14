package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import static com.onewhohears.dscombat.client.overlay.components.TurnCoordinatorOverlay.TURN_COORD_SIZE;
import static com.onewhohears.dscombat.client.overlay.components.VehicleControlOverlay.STICK_BASE_SIZE;
import static com.onewhohears.dscombat.client.overlay.components.VehicleThrottleOverlay.THROTTLE_WIDTH;

public class PlaneAttitudeOverlay extends VehicleOverlayComponent {
    public static final ResourceLocation ATTITUDE_BASE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/attitude_base.png");
    public static final ResourceLocation ATTITUDE_FRAME = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/attitude_frame.png");
    public static final ResourceLocation ATTITUDE_MID = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/attitude_mid.png");
    public static final ResourceLocation ATTITUDE_FRONT = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/attitude_front.png");

    private static final int ATTITUDE_SIZE = 80;
    
    public PlaneAttitudeOverlay(PoseStack poseStack, int screenWidth, int screenHeight) {
        super(poseStack, screenWidth, screenHeight);
    }

    @Override
    public void render(PoseStack poseStack, int screenWidth, int screenHeight) {
        if (!(getPlayerVehicle() instanceof EntityPlane plane)) return;
        
        int attX = screenWidth - ATTITUDE_SIZE - PADDING *3- STICK_BASE_SIZE - THROTTLE_WIDTH;
        int attY = screenHeight - PADDING *2- TURN_COORD_SIZE- ATTITUDE_SIZE;
        RenderSystem.setShaderTexture(0, ATTITUDE_BASE);
        blit(poseStack,
                attX, attY,
                0, 0,
                ATTITUDE_SIZE, ATTITUDE_SIZE,
                ATTITUDE_SIZE, ATTITUDE_SIZE);
        RenderSystem.setShaderTexture(0, ATTITUDE_MID);
        poseStack.pushPose();
        poseStack.translate(attX+ (double) ATTITUDE_SIZE /2, attY+ (double) ATTITUDE_SIZE /2, 0);
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(-plane.zRot));
        int pitchPointY = (int)(Mth.clamp(-plane.getXRot(), -30, 30) * ATTITUDE_SIZE * 0.0055);
        poseStack.translate(0, pitchPointY, 0);
        blit(poseStack,
                -ATTITUDE_SIZE /2, -ATTITUDE_SIZE /2,
                0, 0,
                ATTITUDE_SIZE, ATTITUDE_SIZE,
                ATTITUDE_SIZE, ATTITUDE_SIZE);
        poseStack.popPose();
        RenderSystem.setShaderTexture(0, ATTITUDE_FRAME);
        blit(poseStack,
                attX, attY,
                0, 0,
                ATTITUDE_SIZE, ATTITUDE_SIZE,
                ATTITUDE_SIZE, ATTITUDE_SIZE);
        RenderSystem.setShaderTexture(0, ATTITUDE_FRONT);
        blit(poseStack,
                attX, attY,
                0, 0,
                ATTITUDE_SIZE, ATTITUDE_SIZE,
                ATTITUDE_SIZE, ATTITUDE_SIZE);
    }
}
