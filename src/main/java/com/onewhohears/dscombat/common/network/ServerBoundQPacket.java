package com.onewhohears.dscombat.common.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.util.math.UtilAngles;
import com.onewhohears.dscombat.util.math.UtilAngles.EulerAngles;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class ServerBoundQPacket extends IServerBoundPacket {
	
	public final Quaternion Q;
	
	public ServerBoundQPacket(Quaternion Q) {
		this.Q = Q;
	}
	
	public ServerBoundQPacket(FriendlyByteBuf buffer) {
		this.Q = DataSerializers.QUATERNION.read(buffer);
	}
	
	public void encode(FriendlyByteBuf buffer) {
		DataSerializers.QUATERNION.write(buffer, Q);
	}
	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			ServerPlayer player = ctx.get().getSender();
			if (player.getRootVehicle() instanceof EntityAbstractAircraft plane) {
				plane.setQ(Q);
				plane.setClientQ(Q);
				EulerAngles angles = UtilAngles.toDegrees(Q);
				plane.setXRot((float)angles.pitch);
				plane.setYRot((float)angles.yaw);
				plane.zRot = (float)angles.roll;
				// TODO other clients not seeing rotate
			}
			success.set(true);
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}
}
