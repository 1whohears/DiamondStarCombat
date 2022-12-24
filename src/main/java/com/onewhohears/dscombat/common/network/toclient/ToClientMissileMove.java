package com.onewhohears.dscombat.common.network.toclient;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.util.UtilPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToClientMissileMove extends IPacket {
	
	public final int id;
	public final Vec3 pos;
	public final Vec3 move;
	public final float pitch;
	public final float yaw;
	public final Vec3 targetPos;
	
	public ToClientMissileMove(int id, Vec3 pos, Vec3 move, float pitch, float yaw, Vec3 target) {
		this.id = id;
		this.pos = pos;
		this.move = move;
		this.pitch = pitch;
		this.yaw = yaw;
		this.targetPos = target;
	}
	
	public ToClientMissileMove(FriendlyByteBuf buffer) {
		super(buffer);
		id = buffer.readInt();
		pos = DataSerializers.VEC3.read(buffer);
		move = DataSerializers.VEC3.read(buffer);
		pitch = buffer.readFloat();
		yaw = buffer.readFloat();
		boolean targetNull = buffer.readBoolean();
		if (!targetNull) targetPos = DataSerializers.VEC3.read(buffer);
		else targetPos = null;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		DataSerializers.VEC3.write(buffer, pos);
		DataSerializers.VEC3.write(buffer, move);
		buffer.writeFloat(pitch);
		buffer.writeFloat(yaw);
		buffer.writeBoolean(targetPos == null);
		if (targetPos != null) DataSerializers.VEC3.write(buffer, targetPos);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				UtilPacket.entityMissileMovePacket(id, pos, move, pitch, yaw, targetPos);	
				success.set(true);
			});
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
