package com.onewhohears.dscombat.common.network.toserver;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.data.radar.RadarStats.RadarPing;
import com.onewhohears.dscombat.entity.parts.EntitySeat;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToServerVehicleShoot extends IPacket {
	
	public final int selectedWeaponIndex;
	public final RadarPing ping;
	
	public ToServerVehicleShoot(int selectedWeaponIndex, @Nullable RadarPing ping) {
		this.selectedWeaponIndex = selectedWeaponIndex;
		this.ping = ping;
	}
	
	public ToServerVehicleShoot(FriendlyByteBuf buffer) {
		selectedWeaponIndex = buffer.readInt();
		if (buffer.readBoolean()) {
			ping = new RadarPing(buffer);
		} else ping = null;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(selectedWeaponIndex);
		if (ping != null) {
			buffer.writeBoolean(true);
			ping.write(buffer);
		} else buffer.writeBoolean(false);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			success.set(true);
			ServerPlayer player = ctx.get().getSender();
			if (!(player.getVehicle() instanceof EntitySeat seat)) return;
			EntityVehicle vehicle = seat.getParentVehicle();
			if (vehicle == null) return;
			if (ping != null) vehicle.radarSystem.selectTarget(ping);
			if (seat.isTurret()) {
				((EntityTurret)seat).shoot(player);
				return;
			}
			if (selectedWeaponIndex == -1) return;
			if (!seat.canPassengerShootParentWeapon()) return;
			vehicle.weaponSystem.setSelected(selectedWeaponIndex);
			vehicle.weaponSystem.shootSelected(player);
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
