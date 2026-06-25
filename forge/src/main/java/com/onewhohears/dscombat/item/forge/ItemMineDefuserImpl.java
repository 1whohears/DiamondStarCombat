package com.onewhohears.dscombat.item.forge;

import com.onewhohears.dscombat.item.ItemMineDefuser;
import com.onewhohears.onewholibs.util.forge.UtilItemClient;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ItemMineDefuserImpl {
	public static ItemMineDefuser create() {
		return new ItemMineDefuser() {
			@Override
			public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
				UtilItemClient.onObjModelItemInitClient(consumer);
			}
		};
	}
}
