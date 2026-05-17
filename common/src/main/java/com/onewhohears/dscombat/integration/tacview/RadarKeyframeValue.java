package com.onewhohears.dscombat.integration.tacview;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.radar.PingEntityType;
import com.onewhohears.dscombat.data.radar.PingTerrainType;
import com.onewhohears.dscombat.data.radar.RadarTarget;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.UtilParse;
import com.onewhohears.onewholibs.util.math.UtilGeometry;
import com.onewhohears.tacview.common.core.KeyframeValue;
import com.onewhohears.tacview.common.core.RecordingSession;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RadarKeyframeValue<E extends EntityVehicle> extends KeyframeValue.ListV<RadarTarget, E> {

    public RadarKeyframeValue() {
        super("pings",
                entity -> new ArrayList<>(entity.radarSystem.getServerPings()),
                (entity, value) -> entity.radarSystem.readClientPingsFromServer(value)
        );
    }

    @Override
    public RadarTarget readFromArray(@NotNull JsonElement element, @NotNull RecordingSession session) {
        JsonObject pingJson = element.getAsJsonObject();
        int id = UtilParse.getIntSafe(pingJson, "id", 0);
        Vec3 pos = UtilParse.readVec3(pingJson, "pos");
        boolean isFriendly = UtilParse.getBooleanSafe(pingJson, "isFriendly", false);
        boolean isShared = UtilParse.getBooleanSafe(pingJson, "isShared", false);
        PingTerrainType terrainType = UtilParse.getEnumSafe(pingJson, "terrainType", PingTerrainType.class);
        PingEntityType entityType = UtilParse.getEnumSafe(pingJson, "entityType", PingEntityType.class);
        return new RadarTarget(id, pos, isFriendly, isShared, terrainType, entityType, 0);
    }

    @Override
    public void addToArray(@NotNull RadarTarget value, @NotNull JsonArray ja, @NotNull RecordingSession session) {
        JsonObject pingJson = new JsonObject();
        pingJson.addProperty("id", value.entityId);
        UtilParse.writeVec3(pingJson, "pos", value.pos);
        pingJson.addProperty("isFriendly", value.isFriendly);
        pingJson.addProperty("isShared", value.isShared());
        UtilParse.writeEnum(pingJson, "terrainType", value.terrainType);
        UtilParse.writeEnum(pingJson, "entityType", value.entityType);
        ja.add(pingJson);
    }

    @Override
    public boolean isEqual(@NotNull RadarTarget a, @NotNull RadarTarget b) {
        return a.entityId == b.entityId && UtilGeometry.isEqual(a.pos, b.pos);
    }

    @Override
    public @NotNull List<RadarTarget> get() {
        return value;
    }
}
