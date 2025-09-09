package com.onewhohears.dscombat.common.network.toclient;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.util.UtilClientPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToClientDebugHitboxPos extends IPacket {
	
	private final int id;
	private final String hitbox_name;
	private final Vec3 pos, size;
	
	public ToClientDebugHitboxPos(EntityVehicle vehicle, String hitbox_name, Vec3 pos, Vec3 size) {
		this.id = vehicle.getId();
		this.hitbox_name = hitbox_name;
		this.pos = pos;
		this.size = size;
	}
	
	public ToClientDebugHitboxPos(FriendlyByteBuf buffer) {
		super(buffer);
		id = buffer.readInt();
		hitbox_name = buffer.readUtf();
		pos = DataSerializers.VEC3.read(buffer);
		size = DataSerializers.VEC3.read(buffer);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		buffer.writeUtf(hitbox_name);
		DataSerializers.VEC3.write(buffer, pos);
		DataSerializers.VEC3.write(buffer, size);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				UtilClientPacket.debugHitboxPos(id, hitbox_name, pos, size);
				success.set(true);
			});
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
