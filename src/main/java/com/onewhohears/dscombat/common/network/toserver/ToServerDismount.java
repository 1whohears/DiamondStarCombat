package com.onewhohears.dscombat.common.network.toserver;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;

import com.onewhohears.dscombat.entity.parts.EntitySeat;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.item.ItemParachute;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToServerDismount extends IPacket {

	private final boolean eject;

	public ToServerDismount() {
		eject = false;
	}

	public ToServerDismount(boolean eject) {
		this.eject = eject;
	}
	
	public ToServerDismount(FriendlyByteBuf buffer) {
		eject = buffer.readBoolean();
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeBoolean(eject);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			success.set(true);
			ServerPlayer player = ctx.get().getSender();
			if (player == null) return;
			if (eject && player.getVehicle() instanceof EntitySeat seat && seat.canEject()) {
				seat.useEject();
				player.stopRiding();
				double EJECT_PUSH = 4, EJECT_MOVE = 1;
				EntityVehicle vehicle = seat.getParentVehicle();
				Vec3 dir;
				if (vehicle != null) {
					dir = UtilAngles.getYawAxis(vehicle.getQ());
					player.setDeltaMovement(vehicle.getDeltaMovement().add(dir.scale(EJECT_MOVE)));
				} else dir = new Vec3(0, 1, 0);
				player.setPos(player.position().add(dir.scale(EJECT_PUSH)));
				ItemParachute.createParachute(player.serverLevel(), player, null);
			} else player.stopRiding();
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
