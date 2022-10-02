package com.onewhohears.dscombat.common.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.init.DataSerializers;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class ClientBoundMissileMovePacket extends IPacket {
	
	public final int id;
	public final Vec3 pos;
	public final Vec3 move;
	public final float pitch;
	public final float yaw;
	
	public ClientBoundMissileMovePacket(int id, Vec3 pos, Vec3 move, float pitch, float yaw) {
		this.id = id;
		this.pos = pos;
		this.move = move;
		this.pitch = pitch;
		this.yaw = yaw;
	}
	
	public ClientBoundMissileMovePacket(FriendlyByteBuf buffer) {
		id = buffer.readInt();
		pos = DataSerializers.VEC3.read(buffer);
		move = DataSerializers.VEC3.read(buffer);
		pitch = buffer.readFloat();
		yaw = buffer.readFloat();
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		DataSerializers.VEC3.write(buffer, pos);
		DataSerializers.VEC3.write(buffer, move);
		buffer.writeFloat(pitch);
		buffer.writeFloat(yaw);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				Minecraft m = Minecraft.getInstance();
				Level world = m.level;
				Entity e = world.getEntity(id);
				if (e != null) {
					e.setPos(pos);
					e.setDeltaMovement(move);
					e.setXRot(pitch);
					e.setYRot(yaw);
				}
				success.set(true);
			});
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
