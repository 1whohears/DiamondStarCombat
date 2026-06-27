package com.onewhohears.dscombat.item.forge;

import com.onewhohears.dscombat.entity.weapon.EntityMine;
import com.onewhohears.dscombat.item.ItemMine;
import com.onewhohears.onewholibs.util.forge.UtilItemClient;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ItemMineImpl {
	public static ItemMine create(EntityMine.MineType mineType) {
		return new ItemMine(mineType) {
			@Override
			public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
				UtilItemClient.onObjModelItemInitClient(consumer);
			}
		};
	}
}
