package com.onewhohears.dscombat.common.network.toclient;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.entity.parts.EntityChainHook;
import com.onewhohears.dscombat.entity.parts.EntityChainHook.ChainUpdateType;
import com.onewhohears.dscombat.util.UtilPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToClientVehicleChainUpdate extends IPacket {
	
	public final int vehicleId, hookId, playerId;
	public final ChainUpdateType type;
	
	public ToClientVehicleChainUpdate(@Nullable EntityVehicle vehicle, @Nullable EntityChainHook hook, @Nullable Player player, ChainUpdateType type) {
		if (vehicle != null) this.vehicleId = vehicle.getId();
		else this.vehicleId = -1;
		if (hook != null) this.hookId = hook.getId();
		else this.hookId = -1;
		if (player != null) this.playerId = player.getId();
		else this.playerId = -1;
		this.type = type;
	}
	
	public ToClientVehicleChainUpdate(FriendlyByteBuf buffer) {
		this.vehicleId = buffer.readInt();
		this.hookId = buffer.readInt();
		this.playerId = buffer.readInt();
		this.type = ChainUpdateType.values()[buffer.readInt()];
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(vehicleId);
		buffer.writeInt(hookId);
		buffer.writeInt(playerId);
		buffer.writeInt(type.ordinal());
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				UtilPacket.updateVehicleChain(vehicleId, hookId, playerId, type);
			});
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
