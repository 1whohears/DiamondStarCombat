package com.onewhohears.dscombat.common.network.toserver;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.UtilMCText;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToServerVehicleToItem extends IPacket {
	
	public final int id;
	
	public ToServerVehicleToItem(int id) {
		this.id = id;
	}
	
	public ToServerVehicleToItem(FriendlyByteBuf buffer) {
		id = buffer.readInt();
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			success.set(true);
			ServerPlayer player = ctx.get().getSender();
			Level level = player.level();
			if (!(level.getEntity(id) instanceof EntityVehicle plane)) return;
			if (!plane.canBecomeItem()) {
		    	player.displayClientMessage(
		    			UtilMCText.translatable("error.dscombat.cant_item_yet"), 
		    		true);
		    	return;
		    }
			ItemStack item = plane.getItem();
			if (player.getInventory().getFreeSlot() != -1 && player.addItem(item)) {
				plane.discard();
				return;
			}
			plane.becomeItem(player.position());
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
