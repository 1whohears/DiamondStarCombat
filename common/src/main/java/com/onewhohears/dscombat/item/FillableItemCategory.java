package com.onewhohears.dscombat.item;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface FillableItemCategory {
    void fillItemCategory(@NotNull List<ItemStack> items);
}
