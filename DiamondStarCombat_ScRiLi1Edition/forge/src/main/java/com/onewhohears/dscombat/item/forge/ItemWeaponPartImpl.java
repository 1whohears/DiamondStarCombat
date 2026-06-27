package com.onewhohears.dscombat.item.forge;

import com.onewhohears.dscombat.item.ItemWeaponPart;
import com.onewhohears.onewholibs.util.forge.UtilItemClient;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ItemWeaponPartImpl {
    public static ItemWeaponPart create(int stackSize, String defaultPresetId) {
        return new ItemWeaponPart(stackSize, defaultPresetId) {
            @Override
            public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
                UtilItemClient.onObjModelItemInitClient(consumer);
            }
        };
    }
}
