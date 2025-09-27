package com.onewhohears.dscombat.integration.jei;

import com.onewhohears.dscombat.item.ItemAmmo;
import com.onewhohears.dscombat.item.ItemPart;
import com.onewhohears.dscombat.item.ItemVehicle;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import net.minecraft.world.item.ItemStack;

public class DSCSubTypes {

    public static IIngredientSubtypeInterpreter<ItemStack> getVehicleSubType() {
        return (stack, context) -> {
            if (stack.getItem() instanceof ItemVehicle item) {
                return item.getPreset(stack);
            }
            return IIngredientSubtypeInterpreter.NONE;
        };
    }

    public static IIngredientSubtypeInterpreter<ItemStack> getPartSubType() {
        return (stack, context) -> {
            if (stack.getItem() instanceof ItemPart item) {
                return item.getPreset(stack);
            }
            return IIngredientSubtypeInterpreter.NONE;
        };
    }

    public static IIngredientSubtypeInterpreter<ItemStack> getAmmoSubType() {
        return (stack, context) -> ItemAmmo.getWeaponId(stack);
    }

}
