package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TestItem extends Item {
    public TestItem() {
        super(new Item.Properties().tab(ModItems.VEHICLES).stacksTo(1));
    }

    @Override
    public void fillItemCategory(@NotNull CreativeModeTab category, @NotNull NonNullList<ItemStack> items) {
        if (category == CreativeModeTab.TAB_SEARCH) {
            ItemStack test2 = new ItemStack(this);
            test2.getOrCreateTag().putString("name", "biden");
            items.add(test2);

            ItemStack test3 = new ItemStack(this);
            test3.getOrCreateTag().putString("name", "obama");
            items.add(test3);

            ItemStack test1 = new ItemStack(this);
            test1.getOrCreateTag().putString("name", "trump");
            items.add(test1);
        }
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        String name = stack.getOrCreateTag().getString("name");
        if (!name.isEmpty()) {
            return UtilMCText.translatable("item.dscombat." + name.toLowerCase());
        }
        return super.getName(stack);
    }

    @Override
    public @NotNull String getDescriptionId(@NotNull ItemStack stack) {
        String name = stack.getOrCreateTag().getString("name");
        if (!name.isEmpty()) {
            return "item.dscombat." + name.toLowerCase();
        }
        return super.getDescriptionId(stack);
    }
}
