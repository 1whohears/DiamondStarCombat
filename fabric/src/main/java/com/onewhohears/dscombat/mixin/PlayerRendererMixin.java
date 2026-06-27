package com.onewhohears.dscombat.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.client.event.ClientRenderEventHandlers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin {

    @Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
    private void dscombat_fabric_checkHidePlayer(AbstractClientPlayer player, float f, float g, PoseStack stack,
                              MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        if (ClientRenderEventHandlers.shouldHidePlayer(player)) {
            ci.cancel();
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/entity/player/PlayerRenderer;setModelProperties(Lnet/minecraft/client/player/AbstractClientPlayer;)V"))
    private void dscombat_fabric_onRenderHead(AbstractClientPlayer player, float f, float g, PoseStack stack,
                              MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        ClientRenderEventHandlers.onRenderPlayerPre(player, f, stack);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void dscombat_fabric_onRenderTail(AbstractClientPlayer player, float f, float g, PoseStack stack,
                              MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        ClientRenderEventHandlers.onRenderPlayerPost(player);
    }

}
