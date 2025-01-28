package com.onewhohears.dscombat.common.network.toclient;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.entity.parts.EntityWeaponRack;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.util.UtilClientPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.network.PacketDistributor;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class ToClientOnShoot extends IPacket {

	public static void onShootWeaponRack(EntityWeaponRack rack) {
		if (rack.getLevel().isClientSide()) return;
		PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> rack),
				new ToClientOnShoot(rack.getId(), UtilClientPacket.ShootType.WEAPON_RACK));
	}

	public static void onShootTurret(EntityTurret turret) {
		if (turret.getLevel().isClientSide()) return;
		PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> turret),
				new ToClientOnShoot(turret.getId(), UtilClientPacket.ShootType.TURRET));
	}

	public static void onShootFlareRack(EntityVehicle vehicle) {
		if (vehicle.getLevel().isClientSide()) return;
		PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> vehicle),
				new ToClientOnShoot(vehicle.getId(), UtilClientPacket.ShootType.FLARE));
	}

	public static void onShootChaffRack(EntityVehicle vehicle) {
		if (vehicle.getLevel().isClientSide()) return;
		PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> vehicle),
				new ToClientOnShoot(vehicle.getId(), UtilClientPacket.ShootType.CHAFF));
	}

	public final int id;
	public final UtilClientPacket.ShootType type;

	private ToClientOnShoot(int id, UtilClientPacket.ShootType type) {
		this.id = id;
		this.type = type;
	}

	public ToClientOnShoot(FriendlyByteBuf buffer) {
		id = buffer.readInt();
		type = buffer.readEnum(UtilClientPacket.ShootType.class);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		buffer.writeEnum(type);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				UtilClientPacket.onShoot(id, type);
				success.set(true);
			});
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
