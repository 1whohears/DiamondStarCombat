package com.onewhohears.dscombat.common.network.toserver;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.mojang.datafixers.util.Pair;
import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.crafting.WeaponRecipe;
import com.onewhohears.dscombat.util.UtilItem;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToServerCraftWeapon extends IPacket {
	
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
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeUtf(recipeId);
		buffer.writeDouble(pos.getX());
		buffer.writeDouble(pos.getY());
		buffer.writeDouble(pos.getZ());
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			success.set(true);
			ServerPlayer player = ctx.get().getSender();
			Optional<Pair<ResourceLocation, WeaponRecipe>> option = player.level.getRecipeManager().getRecipeFor(
					WeaponRecipe.Type.INSTANCE, player.getInventory(), player.level, 
					new ResourceLocation(recipeId));
			if (option.isEmpty()) return;
			WeaponRecipe recipe = option.get().getSecond();
			UtilItem.handleInventoryRecipe(player, recipe, pos);
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
