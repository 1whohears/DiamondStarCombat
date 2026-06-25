package com.onewhohears.dscombat.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.client.event.ClientRenderEventHandlers;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
    @Inject(method = "renderHandsWithItems", at = @At("HEAD"), cancellable = true)
    private void dscombat_fabric_cancelHandRender(float f, PoseStack poseStack, MultiBufferSource.BufferSource bufferSource,
                                  LocalPlayer localPlayer, int i, CallbackInfo ci) {
        if (ClientRenderEventHandlers.isCancelRenderHand()) ci.cancel();
    }
}
