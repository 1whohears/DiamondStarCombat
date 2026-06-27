package com.onewhohears.dscombat.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractContainerMenu.class)
public abstract class AbstractContainerMenuMixin {

    @ModifyExpressionValue(
            method = "moveItemStackTo(Lnet/minecraft/world/item/ItemStack;IIZ)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getMaxStackSize()I")
    )
    private int dscombat_useSlotMinStackSize(int maxSize, ItemStack stack, int startIndex, int endIndex,
                                             boolean reverse, @Local Slot slot) {
        return Math.min(slot.getMaxStackSize(), stack.getMaxStackSize());
    }

}
