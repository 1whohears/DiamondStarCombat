package com.onewhohears.dscombat.mixin;

import com.onewhohears.dscombat.client.event.ClientInputEventHandlers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin {
    @Inject(method = "tick(ZF)V", at = @At("TAIL"))
    private void dscombat_fabric_onUpdateInput(CallbackInfo ci) {
        if (ClientInputEventHandlers.isCancelShiftInput(Minecraft.getInstance().player))
            ((KeyboardInput)(Object)this).shiftKeyDown = false;
    }
}
