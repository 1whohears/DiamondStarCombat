package com.onewhohears.dscombat.integration.tacview;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.tacview.common.core.EntityRecorder;
import com.onewhohears.tacview.common.core.KeyframeValue;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.BiFunction;

public abstract class VehicleRecorder<K extends VehicleKeyframe<E>, E extends EntityVehicle> extends EntityRecorder<K,E> {

    public static final BiFunction<ServerLevel,UUID, EntityVehicle> DEFAULT_VEHICLE_GETTER =
            (level, uuid) -> level.getEntity(uuid) instanceof EntityVehicle vehicle ? vehicle : null;

    public final KeyframeValue.StringV<E> preset = registerStringValue("preset", EntityVehicle::getStatsId, EntityVehicle::setStatsId);

    public VehicleRecorder(@NotNull E entity, int recordRate, @NotNull BiFunction<ServerLevel,UUID,E> entityFinder) {
        super(entity, recordRate, entityFinder);
    }

    public VehicleRecorder(@NotNull JsonObject data, @NotNull BiFunction<ServerLevel,UUID,E> entityFinder) {
        super(data, entityFinder);
    }

    public static class Generic extends VehicleRecorder<VehicleKeyframe.Generic, EntityVehicle> {
        public Generic(@NotNull EntityVehicle entity, int recordRate) {
            super(entity, recordRate, DEFAULT_VEHICLE_GETTER);
        }
        public Generic(@NotNull JsonObject data) {
            super(data, DEFAULT_VEHICLE_GETTER);
        }
        @Override
        protected @Nullable VehicleKeyframe.Generic readKeyframe(@NotNull JsonObject data) {
            return new VehicleKeyframe.Generic(data);
        }
        @Override
        protected VehicleKeyframe.Generic newKeyframe(@NotNull EntityVehicle vehicle) {
            return new VehicleKeyframe.Generic(vehicle);
        }
        @Override
        protected VehicleKeyframe.Generic emptyKeyframe() {
            return new VehicleKeyframe.Generic();
        }
    }
}
