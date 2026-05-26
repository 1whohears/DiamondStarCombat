package com.onewhohears.dscombat.common.core;

import com.google.gson.JsonObject;
import com.onewhohears.onewholibs.common.core.Serializable;
import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PositionMarker extends Serializable {

    private int id;
    private String name;
    private Vec3 position;
    private ResourceKey<Level> dimension;
    private @Nullable UUID owner;
    private long created;
    private MarkerType type;

    public void saveMarker(@NotNull String name) {
        this.type = MarkerType.SAVE;
        this.name = name;
        this.setDirty();
    }

    @Override
    protected void addSaveData(@NotNull JsonObject data) {
        data.addProperty("id", id);
        data.addProperty("name", name);
        UtilParse.writeVec3(data, "pos", position);
        data.addProperty("dim", dimension.location().toString());
        if (owner != null) data.addProperty("owner", owner.toString());
        data.addProperty("created", created);
        UtilParse.writeEnum(data, "type", type);
    }

    @Override
    protected void readSaveData(@NotNull JsonObject data) {
        id = UtilParse.getIntSafe(data, "id", 0);
        name = UtilParse.getStringSafe(data, "name", "");
        position = UtilParse.readVec3(data, "pos");
        String dimensionId = UtilParse.getStringSafe(data, "dim", "minecraft:overworld");
        ResourceLocation id = ResourceLocation.tryParse(dimensionId);
        dimension = ResourceKey.create(Registries.DIMENSION, id);
        owner = data.has("owner") ? UUID.fromString(data.get("owner").getAsString()) : null;
        created = data.has("created") ? data.get("created").getAsLong() : System.currentTimeMillis();
        type = UtilParse.getEnumSafe(data, "type", MarkerType.class);
    }

    public static @NotNull PositionMarker create(@NotNull JsonObject posData) {
        PositionMarker data = new PositionMarker();
        data.loadSaveData(posData);
        return data;
    }

    public static @NotNull PositionMarker create(@NotNull FriendlyByteBuf buffer) {
        PositionMarker data = new PositionMarker();
        data.readPacket(buffer);
        return data;
    }

    public static @NotNull PositionMarker create(int id, String name, @NotNull Vec3 position,
                                                 @NotNull ResourceKey<Level> dimension,
                                                 @Nullable UUID owner, @NotNull MarkerType type) {
        PositionMarker data = new PositionMarker();
        data.id = id;
        data.name = name;
        data.position = position;
        data.dimension = dimension;
        data.owner = owner;
        data.created = System.currentTimeMillis();
        data.type = type;
        return data;
    }

    private PositionMarker() {}

    public int getId() {
        return id;
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull Vec3 getPosition() {
        return position;
    }

    public @NotNull ResourceKey<Level> getDimension() {
        return dimension;
    }

    public @Nullable UUID getOwner() {
        return owner;
    }

    public long getCreatedTime() {
        return created;
    }

    public MarkerType getType() {
        return type;
    }
}
