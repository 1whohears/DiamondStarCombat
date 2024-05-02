package com.onewhohears.dscombat.common.network.toserver;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ToClientVehicleTexture;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.network.PacketDistributor;

public class ToServerVehicleTexture extends IPacket {
	
	public final int id;
	private EntityVehicle vehicle;
	private ByteBuf buffer;
	
	public ToServerVehicleTexture(EntityVehicle vehicle) {
		this.id = vehicle.getId();
		this.vehicle = vehicle;
	}
	
	public ToServerVehicleTexture(FriendlyByteBuf buffer) {
		this.id = buffer.readInt();
		this.buffer = buffer.copy().asReadOnly();
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		vehicle.textureManager.write(buffer);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			success.set(true);
			ServerPlayer player = ctx.get().getSender();
			ServerLevel level = player.getLevel();
			if (level.getEntity(id) instanceof EntityVehicle vehicle) {
				vehicle.textureManager.read(buffer);
				PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> vehicle), 
						new ToClientVehicleTexture(player, vehicle));
			}
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
