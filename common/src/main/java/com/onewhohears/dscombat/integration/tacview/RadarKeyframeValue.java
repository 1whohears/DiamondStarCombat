package com.onewhohears.dscombat.integration.tacview;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.radar.RadarStats;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.UtilParse;
import com.onewhohears.onewholibs.util.math.UtilGeometry;
import com.onewhohears.tacview.common.core.KeyframeValue;
import com.onewhohears.tacview.common.core.RecordingSession;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RadarKeyframeValue<E extends EntityVehicle> extends KeyframeValue.ListV<RadarStats.RadarPing, E> {

    public RadarKeyframeValue() {
        super("pings",
                entity -> new ArrayList<>(entity.radarSystem.getServerPings()),
                (entity, value) -> entity.radarSystem.readClientPingsFromServer(value)
        );
    }

    @Override
    public RadarStats.RadarPing readFromArray(@NotNull JsonElement element, @NotNull RecordingSession session) {
        JsonObject pingJson = element.getAsJsonObject();
        int id = UtilParse.getIntSafe(pingJson, "id", 0);
        Vec3 pos = UtilParse.readVec3(pingJson, "pos");
        boolean isFriendly = UtilParse.getBooleanSafe(pingJson, "isFriendly", false);
        boolean isShared = UtilParse.getBooleanSafe(pingJson, "isShared", false);
        RadarStats.PingTerrainType terrainType = UtilParse.getEnumSafe(pingJson, "terrainType", RadarStats.PingTerrainType.class);
        RadarStats.PingEntityType entityType = UtilParse.getEnumSafe(pingJson, "entityType", RadarStats.PingEntityType.class);
        return new RadarStats.RadarPing(id, pos, isFriendly, isShared, terrainType, entityType);
    }

    @Override
    public void addToArray(@NotNull RadarStats.RadarPing value, @NotNull JsonArray ja, @NotNull RecordingSession session) {
        JsonObject pingJson = new JsonObject();
        pingJson.addProperty("id", value.id);
        UtilParse.writeVec3(pingJson, "pos", value.pos);
        pingJson.addProperty("isFriendly", value.isFriendly);
        pingJson.addProperty("isShared", value.isShared());
        UtilParse.writeEnum(pingJson, "terrainType", value.terrainType);
        UtilParse.writeEnum(pingJson, "entityType", value.entityType);
        ja.add(pingJson);
    }

    @Override
    public boolean isEqual(@NotNull RadarStats.RadarPing a, @NotNull RadarStats.RadarPing b) {
        return a.id == b.id && UtilGeometry.isEqual(a.pos, b.pos);
    }

    @Override
    public @NotNull List<RadarStats.RadarPing> get() {
        return value;
    }
}
