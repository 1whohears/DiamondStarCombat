package com.onewhohears.dscombat.common.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.onewhohears.dscombat.common.network.toclient.ToClientPositionMarkers;
import com.onewhohears.onewholibs.common.core.Serializable;
import com.onewhohears.onewholibs.util.UtilEntity;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LerpingModel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PositionMarkerManager extends Serializable {

    private static final long TEMP_MARKER_TIMEOUT = 30 * 1000;

    private final Map<UUID, PlayerPositionMarkers> PLAYERS = new HashMap<>();
    private final IntObjectMap<PositionMarker> MARKERS = new IntObjectHashMap<>();
    private final Set<Integer> markersForRemoval = new HashSet<>();
    private final Map<String, Set<Integer>> teamVisibleIds = new HashMap<>();

    private int MARKER_ID_COUNTER = 0;

    public void onServerTick(@NotNull MinecraftServer server) {
        long currentTime = System.currentTimeMillis();
        teamVisibleIds.clear();
        MARKERS.forEach((id, marker) -> {
            if (markersForRemoval.contains(id) || (marker.getType() == MarkerType.TEMP && currentTime - marker.getCreatedTime() > TEMP_MARKER_TIMEOUT)) {
                removeMarker(id);
                return;
            }
            UUID ownerUUID = marker.getOwner();
            if (ownerUUID == null) return;
            getPlayerData(ownerUUID).setVisible(id);
            ServerPlayer player = server.getPlayerList().getPlayer(ownerUUID);
            if (player == null) return;
            Team team = player.getTeam();
            if (team != null) {
                Set<Integer> visibleIds = teamVisibleIds.computeIfAbsent(team.getName(), name -> new HashSet<>());
                visibleIds.add(id);
            }
        });
        PLAYERS.forEach((uuid, playerData) -> {
            ServerPlayer player = server.getPlayerList().getPlayer(uuid);
            if (player == null) return;
            if (player.getTeam() != null) {
                Set<Integer> ids = teamVisibleIds.get(player.getTeam().getName());
                if (ids != null) ids.forEach(playerData::setVisible);
            }
            playerData.onServerTick(server, this, player);
        });
        markersForRemoval.forEach(MARKERS::remove);
    }

    public void addTempMarker(@NotNull ServerPlayer player, @NotNull Vec3 position) {
        addMarker(player.getScoreboardName()+":"+(MARKER_ID_COUNTER+1), position,
                UtilEntity.getLevel(player).dimension(), player.getUUID(), MarkerType.TEMP);
    }

    public void addMarker(@NotNull String name, @NotNull Vec3 position, @NotNull ResourceKey<Level> dimension,
                          @Nullable UUID owner, @NotNull MarkerType type) {
        int id = ++MARKER_ID_COUNTER;
        PositionMarker marker = PositionMarker.create(id, name, position, dimension, owner, type);
        MARKERS.put(marker.getId(), marker);
    }

    public @NotNull PlayerPositionMarkers getPlayerData(UUID uuid) {
        return PLAYERS.computeIfAbsent(uuid, PlayerPositionMarkers::create);
    }

    public void removeMarker(int id) {
        markersForRemoval.add(id);
    }

    public boolean isMarkerRemoved(int id) {
        return markersForRemoval.contains(id) || !MARKERS.containsKey(id);
    }

    public @Nullable PositionMarker getMarker(int id) {
        return MARKERS.get(id);
    }

    public boolean hasMarker(int id) {
        return MARKERS.containsKey(id);
    }

    public void addClientMarkers(Collection<PositionMarker> markers) {
        for (PositionMarker marker : markers) MARKERS.put(marker.getId(), marker);
    }

    public void handlePositionMarkersRequest(@NotNull ServerPlayer player, @NotNull Set<Integer> ids) {
        ids.removeIf(id -> !hasMarker(id));
        new ToClientPositionMarkers(ids).sendTo(player);
    }

    public static Set<Integer> getClientVisibleMarkerIds(Minecraft minecraft) {
        return getClient().getPlayerData(minecraft.player.getUUID()).getVisibleIds();
    }

    public void resetClient() {
        PLAYERS.clear();
        MARKERS.clear();
        markersForRemoval.clear();
    }

    @Override
    protected void addSaveData(@NotNull JsonObject data) {
        JsonArray playerDataList = new JsonArray();
        for (PlayerPositionMarkers playerData : PLAYERS.values()) {
            playerDataList.add(playerData.getSaveData());
        }
        data.add("players", playerDataList);
        JsonArray markerArray = new JsonArray();
        MARKERS.forEach((id, marker) -> markerArray.add(marker.getSaveData()));
        data.add("markers", markerArray);
    }

    @Override
    protected void readSaveData(@NotNull JsonObject data) {
        MARKERS.clear();
        JsonArray markerArray = data.has("markers") ? data.get("markers").getAsJsonArray() : new JsonArray();
        for (int i = 0; i < markerArray.size(); ++i) {
            JsonObject markerJson = markerArray.get(i).getAsJsonObject();
            PositionMarker marker = PositionMarker.create(markerJson);
            MARKERS.put(marker.getId(), marker);
            if (marker.getId() >= MARKER_ID_COUNTER) MARKER_ID_COUNTER = marker.getId() + 1;
        }
        PLAYERS.clear();
        JsonArray playerDataList = data.has("players") ?
                data.get("players").getAsJsonArray() : new JsonArray();
        for (int i = 0; i < playerDataList.size(); ++i) {
            JsonObject playerData = playerDataList.get(i).getAsJsonObject();
            PlayerPositionMarkers player = PlayerPositionMarkers.create(playerData);
            PLAYERS.put(player.getUUID(), player);
        }
    }

    private static PositionMarkerManager SERVER_INSTANCE;
    private static PositionMarkerManager CLIENT_INSTANCE;

    public static PositionMarkerManager get(Entity entity) {
        return get(UtilEntity.getLevel(entity).isClientSide());
    }

    public static PositionMarkerManager get(boolean isClientSide) {
        return isClientSide ? getClient() : getServer();
    }

    public static PositionMarkerManager getServer() {
        return SERVER_INSTANCE;
    }

    public static PositionMarkerManager getClient() {
        return CLIENT_INSTANCE;
    }

    public static void initServer() {
        SERVER_INSTANCE = new PositionMarkerManager();
    }

    public static void initClient() {
        CLIENT_INSTANCE = new PositionMarkerManager();
    }

    public void onClientTick(Minecraft minecraft) {
        if (minecraft.player == null) return;
        getPlayerData(minecraft.player.getUUID()).onClientTick(minecraft);
    }
}
