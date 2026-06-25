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
                .addOptional(ModDamageTypes.BULLET.location())
                .addOptional(ModDamageTypes.BULLET_EXPLODE.location())
                .addOptional(ModDamageTypes.BOMB.location())
                .addOptional(ModDamageTypes.MISSILE_CONTACT.location())
                .addOptional(ModDamageTypes.MISSILE.location())
                .addOptional(ModDamageTypes.TORPEDO.location())
                .addOptional(ModDamageTypes.IR_MISSILE.location());
        tag(DamageTypeTags.IS_EXPLOSION)
                .addOptional(ModDamageTypes.BULLET_EXPLODE.location())
                .addOptional(ModDamageTypes.BOMB.location())
                .addOptional(ModDamageTypes.MISSILE.location())
                .addOptional(ModDamageTypes.TORPEDO.location())
                .addOptional(ModDamageTypes.IR_MISSILE.location());
        tag(DamageTypeTags.BYPASSES_ARMOR)
                .addOptional(ModDamageTypes.MISSILE_CONTACT.location());
    }
}
