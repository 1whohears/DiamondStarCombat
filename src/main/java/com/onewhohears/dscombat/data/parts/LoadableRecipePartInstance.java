package com.onewhohears.dscombat.data.parts;

import net.minecraft.world.item.ItemStack;

public interface LoadableRecipePartInstance {
	
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
	
}
