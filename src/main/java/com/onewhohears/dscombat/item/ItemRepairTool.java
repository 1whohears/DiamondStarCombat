package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.world.item.Item;

public class ItemRepairTool extends Item {
	
	public final float repair;
	
	public ItemRepairTool(int durability, float repair) {
		super(new Item.Properties().tab(ModItems.PARTS).stacksTo(1).durability(durability));
		this.repair = repair;
	}
	
	/*@Override
	public void setDamage(ItemStack stack, int damage) {
		super.setDamage(stack, damage);
		if (this.getDamage(stack) >= this.getMaxDamage(stack)) {
			stack.hurtAndBreak(damage, null, null);
		}
	}*/
	
	// TODO override use function

}
