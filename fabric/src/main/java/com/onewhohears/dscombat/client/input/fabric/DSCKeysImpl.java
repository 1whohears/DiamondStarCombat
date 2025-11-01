package com.onewhohears.dscombat.client.input.fabric;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.InputConstants;
import com.onewhohears.dscombat.DSCombatMod;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DSCKeysImpl {

    public static final Map<String, NoConflictKeyMapping> DSC_ALL = Maps.newHashMap();
    public static final Map<InputConstants.Key, Set<NoConflictKeyMapping>> DSC_MAP = Maps.newHashMap();

    public static void onKeyMappingClick(InputConstants.Key key) {
        Set<NoConflictKeyMapping> keyMappings = DSC_MAP.get(key);
        if (keyMappings != null) {
            keyMappings.forEach(map -> ++map.clickCount);
        }
    }

    public static void onKeyMappingSet(InputConstants.Key key, boolean value) {
        Set<NoConflictKeyMapping> keyMappings = DSC_MAP.get(key);
        if (keyMappings != null) {
            keyMappings.forEach(map -> map.setDown(value));
        }
    }

    public static void onKeyMappingReset() {
        DSC_MAP.clear();
        for(NoConflictKeyMapping keyMapping : DSC_ALL.values()) {
            Set<NoConflictKeyMapping> keyMappings = DSC_MAP.get(keyMapping.key);
            if (keyMappings != null) {
                keyMappings.add(keyMapping);
            } else {
                keyMappings = new HashSet<>();
                keyMappings.add(keyMapping);
                DSC_MAP.put(keyMapping.key, keyMappings);
            }
        }
    }

    public static KeyMapping registerKeyImpl(String name, String category, int keycode) {
        final var key = new NoConflictKeyMapping("key."+ DSCombatMod.MODID+"."+name,
                InputConstants.Type.KEYSYM, keycode, category);
        KeyBindingHelper.registerKeyBinding(key);
        return key;
    }

    public static KeyMapping registerMouseImpl(String name, String category, int keycode) {
        final var key = new NoConflictKeyMapping("key."+DSCombatMod.MODID+"."+name,
                InputConstants.Type.MOUSE, keycode, category);
        KeyBindingHelper.registerKeyBinding(key);
        return key;
    }

    public static class NoConflictKeyMapping extends KeyMapping {
        public NoConflictKeyMapping(String name, InputConstants.Type type, int keycode, String category) {
            super(name, type, keycode, category);
            KeyMapping.ALL.remove(name);
            KeyMapping.MAP.remove(this.key);
            DSC_ALL.put(name, this);
            Set<NoConflictKeyMapping> keyMappings = new HashSet<>();
            keyMappings.add(this);
            DSC_MAP.put(this.key, keyMappings);
        }
    }

}
