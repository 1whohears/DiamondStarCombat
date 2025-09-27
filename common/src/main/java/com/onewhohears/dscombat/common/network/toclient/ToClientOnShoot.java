package com.onewhohears.dscombat.common.network.toclient;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.entity.parts.EntityWeaponRack;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.util.UtilClientPacket;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;

public class ToClientOnShoot extends BaseS2CMessage {

	public static void onShootWeaponRack(EntityWeaponRack rack, Entity shooter) {
		if (rack.isClientSide()) return;
        PacketHandler.sendToTrackers(new ToClientOnShoot(rack.getId(),
                shooter.getId(), UtilClientPacket.ShootType.WEAPON_RACK), rack);
	}

	public static void onShootTurret(EntityTurret turret, Entity shooter) {
		if (turret.isClientSide()) return;
        PacketHandler.sendToTrackers(new ToClientOnShoot(turret.getId(),
                shooter.getId(), UtilClientPacket.ShootType.TURRET), turret);
	}

	public static void onShootFlareRack(EntityVehicle vehicle, Entity shooter) {
		if (vehicle.getWorld().isClientSide()) return;
        PacketHandler.sendToTrackers(new ToClientOnShoot(vehicle.getId(),
                shooter.getId(), UtilClientPacket.ShootType.FLARE), vehicle);
	}

	public static void onShootChaffRack(EntityVehicle vehicle, Entity shooter) {
		if (vehicle.getWorld().isClientSide()) return;
        PacketHandler.sendToTrackers(new ToClientOnShoot(vehicle.getId(),
                shooter.getId(), UtilClientPacket.ShootType.CHAFF), vehicle);
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
    public MessageType getType() {
        return PacketHandler.S2C_ON_SHOOT;
    }

    @Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(vehicleId);
		buffer.writeInt(shooterId);
		buffer.writeEnum(type);
	}

	@Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            UtilClientPacket.onShoot(vehicleId, shooterId, type);
		});
	}

}
