package com.onewhohears.dscombat.common.network.toclient;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.entity.parts.EntityWeaponRack;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.util.UtilClientPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;
import net.minecraftforge.network.PacketDistributor;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class ToClientOnShoot extends IPacket {

	public static void onShootWeaponRack(EntityWeaponRack rack, Entity shooter) {
		if (rack.getLevel().isClientSide()) return;
		PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> rack),
				new ToClientOnShoot(rack.getId(), shooter.getId(), UtilClientPacket.ShootType.WEAPON_RACK));
	}

	public static void onShootTurret(EntityTurret turret, Entity shooter) {
		if (turret.getLevel().isClientSide()) return;
		PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> turret),
				new ToClientOnShoot(turret.getId(), shooter.getId(), UtilClientPacket.ShootType.TURRET));
	}

	public static void onShootFlareRack(EntityVehicle vehicle, Entity shooter) {
		if (vehicle.getLevel().isClientSide()) return;
		PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> vehicle),
				new ToClientOnShoot(vehicle.getId(), shooter.getId(), UtilClientPacket.ShootType.FLARE));
	}

	public static void onShootChaffRack(EntityVehicle vehicle, Entity shooter) {
		if (vehicle.getLevel().isClientSide()) return;
		PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> vehicle),
				new ToClientOnShoot(vehicle.getId(), shooter.getId(), UtilClientPacket.ShootType.CHAFF));
	}

	public final int vehicleId, shooterId;
	public final UtilClientPacket.ShootType type;

	private ToClientOnShoot(int vehicleId, int shooterId, UtilClientPacket.ShootType type) {
		this.vehicleId = vehicleId;
		this.shooterId = shooterId;
		this.type = type;
	}

	public ToClientOnShoot(FriendlyByteBuf buffer) {
		vehicleId = buffer.readInt();
		shooterId = buffer.readInt();
		type = buffer.readEnum(UtilClientPacket.ShootType.class);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(vehicleId);
		buffer.writeInt(shooterId);
		buffer.writeEnum(type);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				UtilClientPacket.onShoot(vehicleId, shooterId, type);
				success.set(true);
			});
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
