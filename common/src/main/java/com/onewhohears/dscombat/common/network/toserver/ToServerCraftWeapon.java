package com.onewhohears.dscombat.common.network.toserver;

import java.util.Optional;

import com.mojang.datafixers.util.Pair;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.crafting.WeaponRecipe;
import com.onewhohears.onewholibs.util.UtilItem;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class ToServerCraftWeapon extends BaseC2SMessage {
	
	public final String recipeId;
	public final BlockPos pos;
	
	public ToServerCraftWeapon(ResourceLocation recipeId, BlockPos pos) {
		this.recipeId = recipeId.toString();
		this.pos = pos;
	}
	
	public ToServerCraftWeapon(FriendlyByteBuf buffer) {
		recipeId = buffer.readUtf();
		double x = buffer.readDouble();
		double y = buffer.readDouble();
		double z = buffer.readDouble();
		pos = new BlockPos(x, y, z);
	}

    @Override
    public MessageType getType() {
        return PacketHandler.C2S_CRAFT_WEAPON;
    }

    @Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeUtf(recipeId);
		buffer.writeDouble(pos.getX());
		buffer.writeDouble(pos.getY());
		buffer.writeDouble(pos.getZ());
	}

	@Override
	public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
			Player player = context.getPlayer();
			Optional<Pair<ResourceLocation, WeaponRecipe>> option = player.level.getRecipeManager().getRecipeFor(
					WeaponRecipe.Type.INSTANCE, player.getInventory(), player.level, 
					new ResourceLocation(recipeId));
			if (option.isEmpty()) return;
			WeaponRecipe recipe = option.get().getSecond();
			UtilItem.handleInventoryRecipe(player, recipe, pos);
		});
	}

}
