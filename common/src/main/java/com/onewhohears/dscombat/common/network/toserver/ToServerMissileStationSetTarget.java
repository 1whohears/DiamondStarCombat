package com.onewhohears.dscombat.common.network.toserver;

import com.onewhohears.dscombat.block.entity.MissileLaunchStationBlockEntity;
import com.onewhohears.dscombat.common.network.PacketHandler;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ToServerMissileStationSetTarget extends BaseC2SMessage {
	
	private final BlockPos stationPos;
	private final double targetX;
	private final double targetY;
	private final double targetZ;
	
	public ToServerMissileStationSetTarget(BlockPos stationPos, double targetX, double targetY, double targetZ) {
		this.stationPos = stationPos;
		this.targetX = targetX;
		this.targetY = targetY;
		this.targetZ = targetZ;
	}
	
	public ToServerMissileStationSetTarget(FriendlyByteBuf buffer) {
		this.stationPos = buffer.readBlockPos();
		this.targetX = buffer.readDouble();
		this.targetY = buffer.readDouble();
		this.targetZ = buffer.readDouble();
	}

	@Override
	public MessageType getType() {
		return PacketHandler.C2S_MISSILE_STATION_SET_TARGET;
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeBlockPos(stationPos);
		buffer.writeDouble(targetX);
		buffer.writeDouble(targetY);
		buffer.writeDouble(targetZ);
	}

	@Override
	public void handle(NetworkManager.PacketContext context) {
		context.queue(() -> {
			Player player = context.getPlayer();
			if (player == null) return;
			
			BlockEntity blockEntity = player.level().getBlockEntity(stationPos);
			if (blockEntity instanceof MissileLaunchStationBlockEntity station) {
				// Check if player is close enough
				if (station.stillValid(player)) {
					station.setTargetPos(targetX, targetY, targetZ);
				}
			}
		});
	}
}
