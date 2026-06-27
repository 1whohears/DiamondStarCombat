package com.onewhohears.dscombat.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import com.onewhohears.dscombat.client.input.fabric.DSCKeysImpl;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyMapping.class)
public abstract class KeyMappingMixin {
    @Inject(method = "click(Lcom/mojang/blaze3d/platform/InputConstants$Key;)V", at = @At("TAIL"))
    private static void dscombat_fabric_click(InputConstants.Key key, CallbackInfo ci) {
        DSCKeysImpl.onKeyMappingClick(key);
    }
    @Inject(method = "set(Lcom/mojang/blaze3d/platform/InputConstants$Key;Z)V", at = @At("TAIL"))
    private static void dscombat_fabric_set(InputConstants.Key key, boolean value, CallbackInfo ci) {
        DSCKeysImpl.onKeyMappingSet(key, value);
    }
    //Lnet/minecraft/client/KeyMapping;resetMapping()V
    @Inject(method = "resetMapping()V", at = @At("TAIL"))
    private static void dscombat_fabric_resetMapping(CallbackInfo ci) {
        DSCKeysImpl.onKeyMappingReset();
    }
}
