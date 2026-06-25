package com.onewhohears.dscombat.entity.parts;

import com.onewhohears.dscombat.data.parts.instance.SeatInstance;
import com.onewhohears.dscombat.data.parts.stats.SeatStats;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntitySeat extends EntityRidablePart<SeatStats, SeatInstance<SeatStats>> {
    public EntitySeat(EntityType<?> type, Level level) {
        super(type, level, "seat");
    }
}
