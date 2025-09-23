package com.onewhohears.dscombat.client.input.fabric;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.InputConstants;
import com.onewhohears.dscombat.DSCombatMod;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;

import java.util.Map;

public class DSCKeysImpl {

    public static final Map<String, NoConflictKeyMapping> DSC_ALL = Maps.newHashMap();
    public static final Map<InputConstants.Key, NoConflictKeyMapping> DSC_MAP = Maps.newHashMap();

    public static void onKeyMappingClick(InputConstants.Key key) {
        NoConflictKeyMapping keyMapping = DSC_MAP.get(key);
        if (keyMapping != null) {
            ++keyMapping.clickCount;
        }
    }

    public static void onKeyMappingSet(InputConstants.Key key, boolean value) {
        NoConflictKeyMapping keyMapping = DSC_MAP.get(key);
        if (keyMapping != null) {
            keyMapping.setDown(value);
        }
    }

    public static void onKeyMappingReset() {
        DSC_MAP.clear();
        for(NoConflictKeyMapping keyMapping : DSC_ALL.values()) {
            DSC_MAP.put(keyMapping.key, keyMapping);
        }
    }

    public static KeyMapping registerKeyImpl(String name, String category, int keycode) {
        //InputConstants.Key vanillaKey = InputConstants.Type.KEYSYM.getOrCreate(keycode);
        //KeyMapping vanillaKeyMapping = KeyMapping.MAP.get(vanillaKey);
        final var key = new NoConflictKeyMapping("key."+ DSCombatMod.MODID+"."+name,
                InputConstants.Type.KEYSYM, keycode, category);
        KeyBindingHelper.registerKeyBinding(key);
        //KeyMappingRegistry.register(key);
        return key;
    }

    public static KeyMapping registerMouseImpl(String name, String category, int keycode) {
        final var key = new NoConflictKeyMapping("key."+DSCombatMod.MODID+"."+name,
                InputConstants.Type.MOUSE, keycode, category);
        KeyBindingHelper.registerKeyBinding(key);
        //KeyMappingRegistry.register(key);
        return key;
    }

    public static class NoConflictKeyMapping extends KeyMapping {
        public NoConflictKeyMapping(String name, InputConstants.Type type, int keycode, String category) {
            super(name, type, keycode, category);
            KeyMapping.ALL.remove(name);
            KeyMapping.MAP.remove(this.key);
            DSC_ALL.put(name, this);
            DSC_MAP.put(this.key, this);
        }
    }

}
