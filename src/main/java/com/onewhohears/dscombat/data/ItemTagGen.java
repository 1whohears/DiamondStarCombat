package com.onewhohears.dscombat.data;

import org.jetbrains.annotations.Nullable;

import com.onewhohears.dscombat.DSCombatMod;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemTagGen extends ItemTagsProvider {

	public ItemTagGen(DataGenerator generator, BlockTagsProvider blockGen, @Nullable ExistingFileHelper existingFileHelper) {
		super(generator, blockGen, DSCombatMod.MODID, existingFileHelper);
	}

	@Override
	protected void addTags() {
		
	}
	
}
