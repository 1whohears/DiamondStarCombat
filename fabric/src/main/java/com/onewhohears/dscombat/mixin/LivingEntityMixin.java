package com.onewhohears.dscombat.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.onewhohears.dscombat.common.event.CommonEventHandlers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @ModifyVariable(method = "hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z",
            at = @At("HEAD"), argsOnly = true)
    private float dscombat_fabric_adjustDamage(float f, @Local(ordinal = 0, argsOnly = true) DamageSource source) {
        return CommonEventHandlers.onLivingHurt((LivingEntity)((Object)this), source, f);
    }
}
