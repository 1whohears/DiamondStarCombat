package com.onewhohears.dscombat.crafting;

import javax.annotation.Nonnull;

import com.onewhohears.dscombat.data.parts.TurretData;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.init.ModRecipes;
import com.onewhohears.dscombat.item.ItemAmmo;
import com.onewhohears.dscombat.item.ItemTurret;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class TurretUnloadRecipe extends PartItemUnloadRecipe<TurretData> {

	public TurretUnloadRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipes.TURRET_UNLOAD.get();
	}

	@Override
	public boolean isLoadablePartItem(ItemStack stack) {
		return stack.getItem() instanceof ItemTurret;
	}

	@Override
	public boolean isItemAmmo(ItemStack stack) {
		return stack.getItem() instanceof ItemAmmo;
	}

	@Override
	public boolean checkAmmoContinuity() {
		return true;
	}

	@Override
	public String getItemAmmoContinuity(ItemStack stack) {
		return ItemAmmo.getWeaponId(stack);
	}

	@Override
	public boolean isContinuityValid(String continuity) {
		return WeaponPresets.get().has(continuity);
	}

	@Override
	public int getContinuityMaxAmmo(String continuity) {
		return WeaponPresets.get().getPreset(continuity).getMaxAmmo();
	}

	@Override @Nonnull
	public ItemStack getNewAmmoItem(String continuity) {
		WeaponData wd = WeaponPresets.get().getPreset(continuity);
		if (wd == null) return ItemStack.EMPTY;
		return wd.getNewItem();
	}

}
