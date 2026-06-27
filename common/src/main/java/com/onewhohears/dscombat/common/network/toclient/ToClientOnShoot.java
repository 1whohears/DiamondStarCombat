package com.onewhohears.dscombat.common.network.toclient;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.entity.parts.EntityWeaponRack;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;

public class ToClientOnShoot extends BaseS2CMessage {

	// Ordinals matching com.onewhohears.dscombat.client.util.UtilClientPacket.ShootType
	// Stored as int constants so this class never references the client-only enum at load time.
	private static final int TYPE_TURRET      = 0; // ShootType.TURRET
	private static final int TYPE_WEAPON_RACK = 1; // ShootType.WEAPON_RACK
	private static final int TYPE_FLARE       = 2; // ShootType.FLARE
	private static final int TYPE_CHAFF       = 3; // ShootType.CHAFF

	public static void onShootWeaponRack(EntityWeaponRack rack, Entity shooter) {
		if (rack.isClientSide()) return;
        PacketHandler.sendToTrackers(new ToClientOnShoot(rack.getId(), shooter.getId(), TYPE_WEAPON_RACK), rack);
	}

	public static void onShootTurret(EntityTurret turret, Entity shooter) {
		if (turret.isClientSide()) return;
        PacketHandler.sendToTrackers(new ToClientOnShoot(turret.getId(), shooter.getId(), TYPE_TURRET), turret);
	}

	public static void onShootFlareRack(EntityVehicle vehicle, Entity shooter) {
		if (vehicle.getWorld().isClientSide()) return;
        PacketHandler.sendToTrackers(new ToClientOnShoot(vehicle.getId(), shooter.getId(), TYPE_FLARE), vehicle);
	}

	public static void onShootChaffRack(EntityVehicle vehicle, Entity shooter) {
		if (vehicle.getWorld().isClientSide()) return;
        PacketHandler.sendToTrackers(new ToClientOnShoot(vehicle.getId(), shooter.getId(), TYPE_CHAFF), vehicle);
	}

	public final int vehicleId, shooterId;
	/** Ordinal of {@code com.onewhohears.dscombat.client.util.UtilClientPacket.ShootType}. Stored as int to avoid loading the client-only class on the server. */
	public final int typeOrdinal;

	private ToClientOnShoot(int vehicleId, int shooterId, int typeOrdinal) {
		this.vehicleId = vehicleId;
		this.shooterId = shooterId;
		this.typeOrdinal = typeOrdinal;
	}

	public ToClientOnShoot(FriendlyByteBuf buffer) {
		vehicleId = buffer.readInt();
		shooterId = buffer.readInt();
		typeOrdinal = buffer.readInt();
	}

    @Override
    public MessageType getType() {
        return PacketHandler.S2C_ON_SHOOT;
    }

    @Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(vehicleId);
		buffer.writeInt(shooterId);
		buffer.writeInt(typeOrdinal);
	}

	@Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            com.onewhohears.dscombat.client.util.UtilClientPacket.ShootType[] values =
                com.onewhohears.dscombat.client.util.UtilClientPacket.ShootType.values();
            if (typeOrdinal < 0 || typeOrdinal >= values.length) return;
            com.onewhohears.dscombat.client.util.UtilClientPacket.onShoot(vehicleId, shooterId, values[typeOrdinal]);
		});
	}

}
