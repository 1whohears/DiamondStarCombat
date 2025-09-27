package com.onewhohears.dscombat.data.radar;

import com.onewhohears.onewholibs.util.UtilEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TrackableEntitiesManager {

    private static final Map<Integer, Entity> trackMap = new HashMap<>();

    public static Collection<Entity> getTrackableEntities() {
        return trackMap.values();
    }

    public static void addTrackableEntity(@NotNull Entity entity) {
        if (UtilEntity.getLevel(entity).isClientSide()) return;
        trackMap.put(entity.getId(), entity);
    }

    public static void removeTrackableEntity(@NotNull Entity entity) {
        if (UtilEntity.getLevel(entity).isClientSide()) return;
        trackMap.remove(entity.getId());
    }

    public static void serverTick(MinecraftServer server) {
        if (server.getTickCount() % 20 == 0)
            trackMap.entrySet().removeIf(entry -> entry.getValue().isRemoved());
    }

}
