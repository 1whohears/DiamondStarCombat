package com.onewhohears.dscombat.data.radar;

import net.minecraft.world.entity.Entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TrackableEntitiesManager {

    private static final Map<Integer, Entity> trackMap = new HashMap<>();

    public static Collection<Entity> getTrackableEntities() {
        return trackMap.values();
    }

    public static void addTrackableEntity(Entity entity) {
        if (entity.getLevel().isClientSide()) return;
        trackMap.put(entity.getId(), entity);
    }

    public static void removeTrackableEntity(Entity entity) {
        if (entity.getLevel().isClientSide()) return;
        trackMap.remove(entity.getId());
    }

}
