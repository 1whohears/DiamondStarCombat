package com.onewhohears.dscombat.mixin;

import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Camera.class)
public interface CameraAccess {
    @Invoker(value = "setRotation")
    void invokeSetRotation(float pYRot, float pXRot);
}
