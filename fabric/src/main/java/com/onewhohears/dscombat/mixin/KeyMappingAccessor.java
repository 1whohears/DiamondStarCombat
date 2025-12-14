package com.onewhohears.dscombat.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(KeyMapping.class)
public interface KeyMappingAccessor {
    @Accessor("ALL")
    static Map<String, KeyMapping> dscombat$getAll() {
        throw new AssertionError();
    }
    @Accessor("MAP")
    static Map<InputConstants.Key, KeyMapping> dscombat$getMap() {
        throw new AssertionError();
    }
    @Accessor("key")
    InputConstants.Key dscombat$getKey();
}
