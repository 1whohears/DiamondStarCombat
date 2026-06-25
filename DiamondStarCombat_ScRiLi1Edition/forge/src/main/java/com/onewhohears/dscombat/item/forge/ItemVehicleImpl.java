package com.onewhohears.dscombat.item.forge;

import com.onewhohears.dscombat.item.ItemVehicle;
import com.onewhohears.onewholibs.util.forge.UtilItemClient;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ItemVehicleImpl {
    public static ItemVehicle create(String defaultPresetId) {
        return new ItemVehicle(defaultPresetId) {
            @Override
            public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
                UtilItemClient.onObjModelItemInitClient(consumer);
            }
        };
    }
}
