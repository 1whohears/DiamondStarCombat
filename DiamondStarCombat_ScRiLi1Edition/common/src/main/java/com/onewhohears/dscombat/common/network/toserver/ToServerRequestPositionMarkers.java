package com.onewhohears.dscombat.common.network.toserver;

import com.onewhohears.dscombat.common.core.PositionMarkerManager;
import com.onewhohears.dscombat.common.network.PacketHandler;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.fabricmc.api.EnvType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashSet;
import java.util.Set;

public class ToServerRequestPositionMarkers extends BaseC2SMessage {

	public final Set<Integer> ids;

	public ToServerRequestPositionMarkers(Set<Integer> ids) {
        this.ids = ids;
    }

	public ToServerRequestPositionMarkers(FriendlyByteBuf buffer) {
		ids = new HashSet<>();
        int num = buffer.readInt();
        for (int i = 0; i < num; ++i) ids.add(buffer.readInt());
	}

    @Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(ids.size());
        ids.forEach(buffer::writeInt);
	}

	@Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            if (context.getEnvironment().toPlatform() != EnvType.SERVER) return;
            ServerPlayer player = (ServerPlayer) context.getPlayer();
            PositionMarkerManager.getServer().handlePositionMarkersRequest(player, ids);
		});
	}

    @Override
    public MessageType getType() {
        return PacketHandler.C2S_REQ_POS_MARKERS;
    }

}
