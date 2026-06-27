package com.onewhohears.dscombat.item.forge;

import com.onewhohears.dscombat.item.ItemAmmo;
import com.onewhohears.onewholibs.util.forge.UtilItemClient;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ItemAmmoImpl {
    public static ItemAmmo create(int stackSize, String defaultWeaponId) {
        return new ItemAmmo(stackSize, defaultWeaponId) {
            @Override
            public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
                UtilItemClient.onObjModelItemInitClient(consumer);
            }
        };
    }
}
