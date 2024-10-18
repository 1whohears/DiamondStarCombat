package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.stats.PartStats;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public class ItemSeat extends ItemPart {
    public ItemSeat(int stackSize) {
        super(stackSize);
    }
    @Override
    protected void fillItemCategory(PartStats stats, NonNullList<ItemStack> items) {
        super.fillItemCategory(stats, items);
        items.add(stats.createFilledPartInstance("eject").getNewItemStack());
    }
}
