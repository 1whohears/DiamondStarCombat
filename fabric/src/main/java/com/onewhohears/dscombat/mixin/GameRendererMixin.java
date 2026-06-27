package com.onewhohears.dscombat.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.client.event.ClientCameraEventHandlers;
import com.onewhohears.onewholibs.util.math.Vec3f;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.onewhohears.dscombat.client.event.ClientCameraEventHandlers.CAMERA_ANGLES;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Final @Shadow
    private Camera mainCamera;
    @Inject(
            method = "renderLevel(FJLcom/mojang/blaze3d/vertex/PoseStack;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Camera;setup(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/world/entity/Entity;ZZF)V",
                    shift = At.Shift.AFTER
            )
    )
    private void dscombat_fabric_cameraAngleSetup(float f, long l, PoseStack poseStack, CallbackInfo ci) {
        CAMERA_ANGLES.reset();
        ClientCameraEventHandlers.onSetupCameraAngles(mainCamera, f, CAMERA_ANGLES);
        if (CAMERA_ANGLES.isPitchChanged())
            mainCamera.xRot = CAMERA_ANGLES.getPitch();
        if (CAMERA_ANGLES.isYawChanged())
            mainCamera.yRot = CAMERA_ANGLES.getYaw();
        if (CAMERA_ANGLES.isRollChanged())
            poseStack.mulPose(Vec3f.ZP.rotationDegrees(CAMERA_ANGLES.getRoll()).convert());
    }

    @ModifyVariable(
            method = "getFov(Lnet/minecraft/client/Camera;FZ)D",
            at = @At("RETURN"),
            ordinal = 0
    )
    private double dscombat_fabric_modifyFov(double fov) {
        float zoom = ClientCameraEventHandlers.getTurretZoom();
        if (zoom != 1.0f) {
            return fov / zoom;
        }
        return fov;
    }
}
