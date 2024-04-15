package com.onewhohears.dscombat.common.network.toclient;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.util.UtilClientPacket;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToClientVehicleTexture extends IPacket {
	
	public final int ignore_player_id;
	public final int vehicle_id;
	private EntityVehicle vehicle;
	private ByteBuf buffer;
	
	public ToClientVehicleTexture(Player ignorePlayer, EntityVehicle vehicle) {
		this.ignore_player_id = ignorePlayer.getId();
		this.vehicle_id = vehicle.getId();
		this.vehicle = vehicle;
	}
	
	public ToClientVehicleTexture(FriendlyByteBuf buffer) {
		this.ignore_player_id = buffer.readInt();
		this.vehicle_id = buffer.readInt();
		this.buffer = buffer.copy().asReadOnly();
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(ignore_player_id);
		buffer.writeInt(vehicle_id);
		vehicle.textureManager.write(buffer);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				UtilClientPacket.vehicleTexturePacket(ignore_player_id, vehicle_id, buffer);
				success.set(true);
			});
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
