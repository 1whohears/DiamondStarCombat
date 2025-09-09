package com.onewhohears.dscombat.data.tag;

import org.jetbrains.annotations.Nullable;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.init.ModFluids;
import com.onewhohears.dscombat.init.ModTags;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;

public class FluidTagGen extends FluidTagsProvider {

	public FluidTagGen(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
		super(generator, DSCombatMod.MODID, existingFileHelper);
	}
	
	@Override
	protected void addTags() {
		tag(ModTags.Fluids.OIL)
			.add(ModFluids.OIL_FLUID_FLOWING.get(), ModFluids.OIL_FLUID_SOURCE.get())
			.addOptional(new ResourceLocation("createindustry:crude_oil_fluid"));
	}

}
