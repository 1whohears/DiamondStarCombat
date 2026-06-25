package com.onewhohears.dscombat.data.forge;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.init.ModFluids;
import com.onewhohears.dscombat.init.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class FluidTagGen extends FluidTagsProvider {

	public FluidTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture,
                       @Nullable ExistingFileHelper existingFileHelper) {
		super(output, completableFuture, DSCombatMod.MODID, existingFileHelper);
	}
	
	@Override
	protected void addTags(HolderLookup.@NotNull Provider provider) {
		tag(ModTags.Fluids.OIL)
			.add(ModFluids.getOilFluidSource().get(), ModFluids.getOilFluidFlowing().get())
			.addOptional(ResourceLocation.tryParse("createindustry:crude_oil_fluid"))
            .addOptionalTag(ResourceLocation.tryParse("forge:oil"));
        tag(ModTags.Fluids.FORGE_OIL).addTag(ModTags.Fluids.OIL);
	}

}
