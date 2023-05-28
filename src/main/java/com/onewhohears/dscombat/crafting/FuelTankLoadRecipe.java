package com.onewhohears.dscombat.crafting;

import java.util.ArrayList;
import java.util.List;

import com.onewhohears.dscombat.init.ModRecipeSerializers;
import com.onewhohears.dscombat.item.ItemFuelTank;
import com.onewhohears.dscombat.item.ItemGasCan;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class FuelTankLoadRecipe extends CustomRecipe {

	public FuelTankLoadRecipe(ResourceLocation pId) {
		super(pId);
	}

	@Override
	public boolean matches(CraftingContainer container, Level pLevel) {
		ItemStack tank = getFuelTank(container);
		List<ItemStack> gas = getGas(container);
		return hasStuff(tank, gas);
	}

	@Override
	public ItemStack assemble(CraftingContainer container) {
		ItemStack tank = getFuelTank(container);
		List<ItemStack> gas = getGas(container);
		if (!hasStuff(tank, gas)) return ItemStack.EMPTY;
		float cf = tank.getOrCreateTag().getFloat("fuel");
		float mf = tank.getOrCreateTag().getFloat("max");
		for (int i = 0; i < gas.size(); ++i) {
			int d = gas.get(i).getDamageValue();
			int m = gas.get(i).getMaxDamage();
			int f = m-d;
			cf += f;
		}
		if (cf > mf) cf = mf;
		ItemStack r = tank.copy();
		r.getOrCreateTag().putFloat("fuel", cf);
		return r;
	}
	
	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
		ItemStack tank = getFuelTank(container);
		List<ItemStack> gas = getGas(container);
		float cf = tank.getOrCreateTag().getFloat("fuel");
		float mf = tank.getOrCreateTag().getFloat("max");
		for (int i = 0; i < gas.size(); ++i) {
			ItemStack stack = gas.get(i);
			int d = stack.getDamageValue();
			int m = stack.getMaxDamage();
			int f = m-d;
			float t = f + cf;
			if (t <= mf) {
				stack.setDamageValue(m);
				stack.setCount(2);
				cf += f;
			} else if (t > mf && cf != mf) {
				stack.setDamageValue(m-(int)(t-mf));
				stack.setCount(2);
				cf = mf;
			} else {
				stack.setCount(2);
			}
		}
		NonNullList<ItemStack> list = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
		for(int i = 0; i < list.size(); ++i) {
			ItemStack stack = container.getItem(i);
			list.set(i, stack.getContainerItem());
		}
		return list;
	}
	
	private boolean hasStuff(ItemStack tank, List<ItemStack> gas) {
		return tank != null && gas.size() > 0;
	}
	
	private ItemStack getFuelTank(CraftingContainer container) {
		ItemStack tank = null;
		for (int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack stack  = container.getItem(i);
			if (stack.isEmpty()) continue;
			Item item = stack.getItem();
			if (item instanceof ItemFuelTank) {
				if (tank != null) return null;
				tank = stack;
			}
		}
		return tank;
	}
	
	private List<ItemStack> getGas(CraftingContainer container) {
		List<ItemStack> gas = new ArrayList<ItemStack>();
		for (int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack stack  = container.getItem(i);
			if (stack.isEmpty()) continue;
			Item item = stack.getItem();
			if (item instanceof ItemGasCan) {
				gas.add(stack);
			}
		}
		return gas;
	}
	
	@Override
	public boolean isSpecial() {
		return true;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.FUEL_TANK_LOAD.get();
	}

}
