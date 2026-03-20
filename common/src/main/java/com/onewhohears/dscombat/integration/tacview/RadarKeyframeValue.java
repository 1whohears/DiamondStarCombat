package com.onewhohears.dscombat.integration.tacview;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.radar.RadarStats;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.tacview.common.core.EntityKeyframe;
import com.onewhohears.tacview.common.core.KeyframeValue;
import com.onewhohears.tacview.common.core.RecordingSession;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class RadarKeyframeValue<E extends EntityVehicle> extends KeyframeValue.ListV<RadarStats.RadarPing, E> {

    public RadarKeyframeValue(String name, Function<E, List<RadarStats.RadarPing>> entityReader,
                              BiConsumer<E, List<RadarStats.RadarPing>> entitySetter) {
        super(name, entityReader, entitySetter);
    }

    @Override
    public RadarStats.RadarPing readFromArray(@NotNull JsonElement element, @NotNull RecordingSession session) {
        JsonObject pingJson = element.getAsJsonObject();

        return new RadarStats.RadarPing(id, pos, isFriendly, isShared, terrainType, entityType);
    }

    @Override
    public void addToArray(@NotNull RadarStats.RadarPing value, @NotNull JsonArray ja, @NotNull RecordingSession session) {
        JsonObject pingJson = new JsonObject();

        ja.add(pingJson);
    }

    @Override
    public boolean isEqual(@NotNull RadarStats.RadarPing a, @NotNull RadarStats.RadarPing b) {
        return a.equals(b);
    }

    @Override
    public @NotNull List<RadarStats.RadarPing> get() {
        return value;
    }
}
