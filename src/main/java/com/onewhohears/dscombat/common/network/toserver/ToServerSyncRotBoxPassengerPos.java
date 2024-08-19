package com.onewhohears.dscombat.common.network.toserver;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.onewholibs.util.UtilEntity;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToServerSyncRotBoxPassengerPos extends IPacket {
	
	private final int[] ids;
	private final Vec3[] pos;
	
	/**
	 * ids and pos arrays must be the same length!
	 * @param ids
	 * @param pos
	 */
	public ToServerSyncRotBoxPassengerPos(int[] ids, Vec3[] pos) {
		this.ids = ids;
		this.pos = pos;
	}
	
	public ToServerSyncRotBoxPassengerPos(FriendlyByteBuf buffer) {
		ids = buffer.readVarIntArray();
		pos = new Vec3[ids.length];
		for (int i = 0; i < pos.length; ++i)
			pos[i] = DataSerializers.VEC3.read(buffer);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeVarIntArray(ids);
		for (int i = 0; i < pos.length; ++i) 
			DataSerializers.VEC3.write(buffer, pos[i]);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			Level level = ctx.get().getSender().level;
			for (int i = 0; i < ids.length; ++i) {
				if (pos[i].y == -1000) continue;
				Entity entity = level.getEntity(ids[i]);
				if (entity == null) continue;
				if (UtilEntity.isPlayer(entity)) continue;
				entity.setPos(pos[i]);
			}
			success.set(true);
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
