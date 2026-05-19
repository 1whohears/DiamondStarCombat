package com.onewhohears.dscombat.common.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.onewhohears.dscombat.common.network.toclient.ToClientPlayerMarkerData;
import com.onewhohears.dscombat.common.network.toserver.ToServerRequestPositionMarkers;
import com.onewhohears.onewholibs.common.core.Serializable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerPositionMarkers extends Serializable {

    private UUID uuid;
    private final Set<Integer> visibleIds = new HashSet<>();
    private final Set<Integer> requested = new HashSet<>();
    private final Set<Integer> toRequest = new HashSet<>();

    public void onServerTick(@NotNull MinecraftServer server, @NotNull PositionMarkerManager manager,
                             @NotNull ServerPlayer player) {
        int initVisibleNum = visibleIds.size();
        visibleIds.removeIf(manager::isMarkerRemoved);
        if (initVisibleNum != visibleIds.size()) setDirty();
        if (isDirty()) {
            resetDirty();
            new ToClientPlayerMarkerData(uuid).sendTo(player);
        }
    }

    public void onClientTick() {
        toRequest.clear();
        for (int id : visibleIds) {
            if (!PositionMarkerManager.getClient().hasMarker(id) && !requested.contains(id)) {
                toRequest.add(id);
                requested.add(id);
            }
        }
        if (!toRequest.isEmpty()) new ToServerRequestPositionMarkers(toRequest).sendToServer();
    }

    public void setVisible(int id) {
        if (visibleIds.add(id)) {
            setDirty();
        }
    }

    @Override
    protected void addSaveData(@NotNull JsonObject data) {
        data.addProperty("uuid", uuid.toString());
        JsonArray markerList = new JsonArray();
        for (Integer id : visibleIds) markerList.add(id);
        data.add("visibleIds", markerList);
    }

    @Override
    protected void readSaveData(@NotNull JsonObject data) {
        uuid = data.has("uuid") ? UUID.fromString(data.get("uuid").getAsString()) : null;
        visibleIds.clear();
        JsonArray markerArray = data.has("visibleIds") ? data.get("visibleIds").getAsJsonArray() : new JsonArray();
        for (int i = 0; i < markerArray.size(); ++i) visibleIds.add(markerArray.get(i).getAsInt());
        setDirty();
    }

    public static @NotNull PlayerPositionMarkers create(@NotNull JsonObject playerData) {
        PlayerPositionMarkers data = new PlayerPositionMarkers();
        data.loadSaveData(playerData);
        return data;
    }

    public static @NotNull PlayerPositionMarkers create(@NotNull UUID playerUUID) {
        PlayerPositionMarkers data = new PlayerPositionMarkers();
        data.uuid = playerUUID;
        return data;
    }

    private PlayerPositionMarkers() {}

    public UUID getUUID() {
        return uuid;
    }

    public Set<Integer> getVisibleIds() {
        return visibleIds;
    }
}
