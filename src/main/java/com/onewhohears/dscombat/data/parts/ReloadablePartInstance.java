package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.crafting.PartItemLoadRecipe;
import com.onewhohears.dscombat.crafting.PartItemUnloadRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

import javax.annotation.Nullable;

public interface ReloadablePartInstance {
	
	float getCurrentAmmo();
	float getMaxAmmo();
	void setCurrentAmmo(float ammo);
	void setMaxAmmo(float max);
	
	boolean isCompatibleWithAmmoContinuity(String continuity);
	boolean updateContinuityIfEmpty();
	void setContinuity(String continuity);
	String getContinuity();
	
	default boolean isContinuityEmpty() {
		return getContinuity() == null || getContinuity().isEmpty();
	}
	
	ItemStack getNewItemStack();

	PartItemLoadRecipe<?> getLoadRecipe();
	@Nullable
	PartItemUnloadRecipe<?> getUnloadRecipe();
	
}
