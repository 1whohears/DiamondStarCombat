package com.onewhohears.dscombat.mixin;

import com.onewhohears.dscombat.client.event.ClientInputEventHandlers;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {
    @Shadow
    public Input input;
    @Inject(method = "aiStep", at = @At("HEAD"))
    private void onUpdateInput(CallbackInfo ci) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        if (ClientInputEventHandlers.isCancelShiftInput(player))
            input.shiftKeyDown = false;
    }
}
