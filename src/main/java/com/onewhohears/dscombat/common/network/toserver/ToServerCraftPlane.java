package com.onewhohears.dscombat.common.network.toserver;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.crafting.DSCIngredient;
import com.onewhohears.dscombat.data.aircraft.AircraftPreset;
import com.onewhohears.dscombat.data.aircraft.AircraftPresets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToServerCraftPlane extends IPacket {
	
	public final String preset;
	public final BlockPos pos;
	
	public ToServerCraftPlane(String preset, BlockPos pos) {
		this.preset = preset;
		this.pos = pos;
	}
	
	public ToServerCraftPlane(FriendlyByteBuf buffer) {
		preset = buffer.readUtf();
		double x = buffer.readDouble();
		double y = buffer.readDouble();
		double z = buffer.readDouble();
		pos = new BlockPos(x, y, z);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeUtf(preset);
		buffer.writeDouble(pos.getX());
		buffer.writeDouble(pos.getY());
		buffer.writeDouble(pos.getZ());
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			ServerPlayer player = ctx.get().getSender();
			AircraftPreset ap = AircraftPresets.get().getAircraftPreset(preset);
			if (ap != null) {
				List<DSCIngredient> ingredients = ap.getIngredients();
				if (DSCIngredient.hasIngredients(ingredients, player.getInventory())) {
					DSCIngredient.consumeIngredients(ingredients, player.getInventory());
					ItemStack stack = ap.getItem();
					Containers.dropItemStack(player.level, pos.getX()+0.5, 
						pos.getY()+1.125, pos.getZ()+0.5, stack);
				}
			}
			success.set(true);
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
