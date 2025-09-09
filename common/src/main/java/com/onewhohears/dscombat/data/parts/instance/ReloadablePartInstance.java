package com.onewhohears.dscombat.data.parts.instance;

import com.onewhohears.dscombat.crafting.PartItemLoadRecipe;
import com.onewhohears.dscombat.crafting.PartItemUnloadRecipe;
import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

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
	@Nullable PartItemUnloadRecipe<?> getUnloadRecipe();

	default boolean canUnload() {
		return getUnloadRecipe() != null;
	}

	default void loadPartFromInventory(ServerPlayer player) {
		PartItemLoadRecipe<?> recipe = getLoadRecipe();
		recipe.reloadFromInventory(player.getInventory(), this);
	}

	default void unloadPartToInventory(ServerPlayer player) {
		PartItemUnloadRecipe<?> recipe = getUnloadRecipe();
		if (recipe == null) {
			player.displayClientMessage(
					UtilMCText.translatable("error.dscombat.cant_unload"),
					true);
			return;
		}
		String continuity = getContinuity();
		if (recipe.checkAmmoContinuity() && !isContinuityEmpty() && !recipe.isContinuityValid(continuity)) {
			player.displayClientMessage(
					UtilMCText.translatable("error.dscombat.cant_unload"),
					true);
			return;
		}
		ItemStack ammo = recipe.getNewAmmoItem(continuity);
		if (ammo.isEmpty()) return;
		ammo.setCount((int)getCurrentAmmo());
		player.addItem(ammo);
		setCurrentAmmo(0);
	}

	MutableComponent getItemName();
	
}
