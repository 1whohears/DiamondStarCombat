package com.onewhohears.dscombat.client.input.forge;

import com.mojang.blaze3d.platform.InputConstants;
import com.onewhohears.dscombat.DSCombatMod;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;

public class DSCKeysImpl {

    public static KeyMapping registerKeyImpl(String name, String category, int keycode) {
        final var key = new KeyMapping("key."+ DSCombatMod.MODID+"."+name,
                KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, keycode, category);
        KeyMappingRegistry.register(key);
        return key;
    }

    public static KeyMapping registerMouseImpl(String name, String category, int keycode) {
        final var key = new KeyMapping("key."+DSCombatMod.MODID+"."+name,
                KeyConflictContext.IN_GAME, InputConstants.Type.MOUSE, keycode, category);
        KeyMappingRegistry.register(key);
        return key;
    }

}
