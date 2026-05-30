package com.onewhohears.dscombat.common.network.toclient;

import com.onewhohears.dscombat.common.core.PositionMarker;
import com.onewhohears.dscombat.common.core.PositionMarkerManager;
import com.onewhohears.dscombat.common.network.PacketHandler;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.fabricmc.api.EnvType;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class ToClientPositionMarkers extends BaseS2CMessage {

    private final Collection<PositionMarker> markers;

    public ToClientPositionMarkers(@NotNull Set<Integer> ids) {
        markers = new ArrayList<>();
        for (int id : ids) {
            PositionMarker marker = PositionMarkerManager.getServer().getMarker(id);
            if (marker == null) continue;
            markers.add(marker);
        }
    }

    public ToClientPositionMarkers(FriendlyByteBuf buffer) {
        markers = new ArrayList<>();
        int num = buffer.readInt();
        for (int i = 0; i < num; ++i) {
            PositionMarker marker = PositionMarker.create(buffer);
            markers.add(marker);
        }
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(markers.size());
        for (PositionMarker marker : markers) marker.writePacket(buffer);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            if (context.getEnvironment().toPlatform() != EnvType.CLIENT) return;
            PositionMarkerManager.getClient().addClientMarkers(markers);
        });
    }

    @Override
    public MessageType getType() {
        return PacketHandler.S2C_SEND_POS_MARKERS;
    }

}
