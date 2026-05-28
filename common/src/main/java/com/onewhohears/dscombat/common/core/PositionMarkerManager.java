package com.onewhohears.dscombat.common.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.common.network.toclient.ToClientPositionMarkers;
import com.onewhohears.onewholibs.common.core.Serializable;
import com.onewhohears.onewholibs.util.UtilEntity;
import com.onewhohears.onewholibs.util.UtilFile;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PositionMarkerManager extends Serializable {

    public static String MARKER_PATH = "data/dscombat/markers.json";
    private static final long TEMP_MARKER_TIMEOUT = 30 * 1000;

    private final Map<UUID, PlayerPositionMarkers> PLAYERS = new HashMap<>();
    private final IntObjectMap<PositionMarker> MARKERS = new IntObjectHashMap<>();
    private final Set<Integer> markersForRemoval = new HashSet<>();
    private final Set<Integer> changedMarkers = new HashSet<>();
    private final Set<Integer> playerChangedMarkers = new HashSet<>();
    private final Map<String, Set<Integer>> teamVisibleIds = new HashMap<>();

    private int MARKER_ID_COUNTER = 0;

    public void onServerTick(@NotNull MinecraftServer server) {
        long currentTime = System.currentTimeMillis();
        teamVisibleIds.clear();
        changedMarkers.clear();
        MARKERS.forEach((id, marker) -> {
            if (markersForRemoval.contains(id) || (marker.getType() == MarkerType.TEMP
                    && currentTime - marker.getCreatedTime() > TEMP_MARKER_TIMEOUT)) {
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
            if (marker.isDirty()) {
                changedMarkers.add(id);
                marker.resetDirty();
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
            playerChangedMarkers.clear();
            for (int id : changedMarkers) {
                if (playerData.getVisibleIds().contains(id)) {
                    playerChangedMarkers.add(id);
                }
            }
            new ToClientPositionMarkers(playerChangedMarkers).sendTo(player);
        });
        markersForRemoval.forEach(MARKERS::remove);
    }

    public PositionMarker addQuickTempMarker(@NotNull ServerPlayer player) {
        Vec3 pos = UtilEntity.getLookingAtBlockPos(player, 1024);
        pos = new Vec3(Math.floor(pos.x)+0.5, Math.floor(pos.y)+0.5, Math.floor(pos.z)+0.5);
        return addTempMarker(player, pos);
    }

    public PositionMarker addTempMarker(@NotNull ServerPlayer player, @NotNull Vec3 position) {
        PositionMarker marker = addMarker(getShortName(player)+":"+(MARKER_ID_COUNTER+1), position,
                UtilEntity.getLevel(player).dimension(), player.getUUID(), MarkerType.TEMP);
        getPlayerData(player.getUUID()).setTempMarkerId(marker.getId());
        return marker;
    }

    public PositionMarker addMarker(@NotNull String name, @NotNull Vec3 position, @NotNull ResourceKey<Level> dimension,
                                    @Nullable UUID owner, @NotNull MarkerType type) {
        int id = ++MARKER_ID_COUNTER;
        PositionMarker marker = PositionMarker.create(id, name, position, dimension, owner, type);
        MARKERS.put(marker.getId(), marker);
        return marker;
    }

    public @Nullable PositionMarker getMarkerByName(@NotNull String name, @Nullable UUID owner) {
        for (PositionMarker marker : MARKERS.values()) {
            if (!marker.getName().equals(name)) continue;
            if (owner != null && marker.getOwner() != null && !marker.getOwner().equals(owner)) continue;
            return marker;
        }
        return null;
    }

    public Set<String> getMarkerNames(@Nullable UUID owner) {
        Set<String> names = new HashSet<>();
        for (PositionMarker marker : MARKERS.values()) {
            if (owner != null && marker.getOwner() != null && !marker.getOwner().equals(owner)) continue;
            names.add(marker.getName());
        }
        return names;
    }

    public static String getShortName(@NotNull ServerPlayer player) {
        String name = player.getScoreboardName();
        int maxLength = 5;
        if (name.length() <= maxLength) return name;
        return name.substring(0, maxLength);
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
        UUID localPlayer = Minecraft.getInstance().player.getUUID();
        for (PositionMarker marker : markers) {
            MARKERS.put(marker.getId(), marker);
            if (marker.getOwner() != null && marker.getOwner().equals(localPlayer)
                    && marker.getType() == MarkerType.TEMP) {
                DSCClientInputs.setSelectedMarkerId(marker.getId());
            }
        }
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

    public void load(@NotNull ServerLevel level) {
        if (!level.dimension().location().toString().equals("minecraft:overworld")) return;
        MinecraftServer server = level.getServer();
        JsonObject data = UtilFile.readJsonGamePath(MARKER_PATH, server);
        loadSaveData(data);
    }

    public void save(@NotNull ServerLevel level) {
        if (!level.dimension().location().toString().equals("minecraft:overworld")) return;
        MinecraftServer server = level.getServer();
        UtilFile.printGamePath(MARKER_PATH, getSaveData(), server);
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
        if (SERVER_INSTANCE == null) SERVER_INSTANCE = new PositionMarkerManager();
        return SERVER_INSTANCE;
    }

    public static PositionMarkerManager getClient() {
        return CLIENT_INSTANCE;
    }

    public static void initClient() {
        CLIENT_INSTANCE = new PositionMarkerManager();
    }

    public void onClientTick(Minecraft minecraft) {
        if (minecraft.player == null) return;
        getPlayerData(minecraft.player.getUUID()).onClientTick(minecraft);
    }
}
