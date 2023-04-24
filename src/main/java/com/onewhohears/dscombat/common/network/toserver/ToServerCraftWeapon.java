package com.onewhohears.dscombat.common.network.toserver;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.crafting.DSCIngredient;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToServerCraftWeapon extends IPacket {
	
	public final String weaponId;
	public final BlockPos pos;
	
	public ToServerCraftWeapon(String weaponId, BlockPos pos) {
		this.weaponId = weaponId;
		this.pos = pos;
	}
	
	public ToServerCraftWeapon(FriendlyByteBuf buffer) {
		weaponId = buffer.readUtf();
		double x = buffer.readDouble();
		double y = buffer.readDouble();
		double z = buffer.readDouble();
		pos = new BlockPos(x, y, z);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeUtf(weaponId);
		buffer.writeDouble(pos.getX());
		buffer.writeDouble(pos.getY());
		buffer.writeDouble(pos.getZ());
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			ServerPlayer player = ctx.get().getSender();
			WeaponData data = WeaponPresets.getById(weaponId);
			if (DSCIngredient.hasIngredients(data.ingredients, player.getInventory())) {
				DSCIngredient.consumeIngredients(data.ingredients, player.getInventory());
				ItemStack stack = new ItemStack(data.getItem());
				stack.setCount(data.craftNum);
				Containers.dropItemStack(player.level, pos.getX()+0.5, 
						pos.getY()+1.125, pos.getZ()+0.5, stack);
			}
			success.set(true);
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
