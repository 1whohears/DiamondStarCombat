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

public class ToServerMissileStationToggleArmed extends BaseC2SMessage {
	
	private final BlockPos stationPos;
	
	public ToServerMissileStationToggleArmed(BlockPos stationPos) {
		this.stationPos = stationPos;
	}
	
	public ToServerMissileStationToggleArmed(FriendlyByteBuf buffer) {
		this.stationPos = buffer.readBlockPos();
	}

	@Override
	public MessageType getType() {
		return PacketHandler.C2S_MISSILE_STATION_TOGGLE_ARMED;
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeBlockPos(stationPos);
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
					station.toggleArmed();
				}
			}
		});
	}
}
