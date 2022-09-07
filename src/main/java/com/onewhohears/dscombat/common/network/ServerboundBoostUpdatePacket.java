package com.onewhohears.dscombat.common.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class ServerboundBoostUpdatePacket {
	
	public final BlockPos entityPos;
	
	public ServerboundBoostUpdatePacket(BlockPos pos) {
		this.entityPos = pos;
	}
	
	public ServerboundBoostUpdatePacket(FriendlyByteBuf buffer) {
		this(buffer.readBlockPos());
	}
	
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeBlockPos(this.entityPos);
	}
	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			// move player
			ServerPlayer player = ctx.get().getSender();
			//Vec3 dmove = player.getDeltaMovement();
			//player.setDeltaMovement(dmove.x, 1.0d, dmove.z);
			//Vec3 pos = player.position();
			player.getCommandSenderWorld().addParticle(ParticleTypes.FLAME, entityPos.getX(), entityPos.getY(), entityPos.getZ(), 0, -0.05d, 0);
			// say something to player
			/*PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK
					.with(() -> player.getCommandSenderWorld().getChunkAt(entityPos)), 
					new ClientboundUpdateBoostPacket(entityPos));*/
			// success
			success.set(true);
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}
	
}
