package com.onewhohears.dscombat.data.forge;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.init.ModDamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class DamageTypeTagGen extends DamageTypeTagsProvider {

    public DamageTypeTagGen(PackOutput arg, CompletableFuture<HolderLookup.Provider> completableFuture,
                            @Nullable ExistingFileHelper existingFileHelper) {
        super(arg, completableFuture, DSCombatMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(DamageTypeTags.IS_PROJECTILE)
                .add(ModDamageTypes.BULLET)
                .add(ModDamageTypes.BULLET_EXPLODE)
                .add(ModDamageTypes.BOMB)
                .add(ModDamageTypes.MISSILE_CONTACT)
                .add(ModDamageTypes.MISSILE)
                .add(ModDamageTypes.TORPEDO)
                .add(ModDamageTypes.IR_MISSILE);
        tag(DamageTypeTags.IS_EXPLOSION)
                .add(ModDamageTypes.BULLET_EXPLODE)
                .add(ModDamageTypes.BOMB)
                .add(ModDamageTypes.MISSILE)
                .add(ModDamageTypes.TORPEDO)
                .add(ModDamageTypes.IR_MISSILE);
        tag(DamageTypeTags.BYPASSES_ARMOR)
                .add(ModDamageTypes.MISSILE_CONTACT);
    }
}
