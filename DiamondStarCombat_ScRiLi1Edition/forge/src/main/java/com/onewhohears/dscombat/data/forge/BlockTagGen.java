package com.onewhohears.dscombat.data.forge;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.init.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BlockTagGen extends BlockTagsProvider {

	public BlockTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                       @Nullable ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, DSCombatMod.MODID, existingFileHelper);
	}
	
	@Override
	protected void addTags(HolderLookup.@NotNull Provider provider) {
		tag(ModTags.Blocks.FRAGILE)
                .add(Blocks.LILY_PAD)
                .add(Blocks.COCOA)
                .add(Blocks.END_ROD)
                .add(Blocks.SCAFFOLDING)
                .add(Blocks.SEA_PICKLE)
                .add(Blocks.TURTLE_EGG)
                .add(Blocks.GLOWSTONE)
                .add(Blocks.SEA_LANTERN)
                .add(Blocks.ICE)
                .add(Blocks.BLUE_ICE)
                .add(Blocks.FROSTED_ICE)
                .add(Blocks.PACKED_ICE)
                .add(Blocks.BAMBOO)
                .add(Blocks.SMALL_AMETHYST_BUD)
                .add(Blocks.MEDIUM_AMETHYST_BUD)
                .add(Blocks.LARGE_AMETHYST_BUD)
                .add(Blocks.AMETHYST_CLUSTER)
                .add(Blocks.LANTERN)
                .add(Blocks.SOUL_LANTERN)
                .add(Blocks.POINTED_DRIPSTONE)
                .addOptionalTag(Tags.Blocks.GLASS_PANES.location())
                .addOptionalTag(Tags.Blocks.GLASS.location())
                .addOptionalTag(ResourceLocation.tryParse("c:glass_blocks"))
                .addOptionalTag(ResourceLocation.tryParse("c:glass_panes"))
                .addTag(BlockTags.CANDLES);
		tag(ModTags.Blocks.ABSORBENT)
			.add(Blocks.HAY_BLOCK, Blocks.SLIME_BLOCK, Blocks.HONEY_BLOCK);
		tag(ModTags.Blocks.VEHICLE_TRAMPLE)
				.addTag(BlockTags.LEAVES).addTag(BlockTags.CROPS).addTag(BlockTags.FLOWERS)
				.addTag(BlockTags.CANDLES).addTag(BlockTags.SAPLINGS).addTag(BlockTags.ICE)
				.add(Blocks.GRASS).add(Blocks.TALL_GRASS).add(Blocks.TALL_SEAGRASS)
				.add(Blocks.FERN).add(Blocks.DEAD_BUSH).add(Blocks.COBWEB)
				.add(Blocks.SEAGRASS).add(Blocks.SEA_PICKLE).add(Blocks.SEA_LANTERN);
	}

}
