package com.onewhohears.dscombat.integration.tacview;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.tacview.common.core.EntityKeyframe;
import com.onewhohears.tacview.common.core.KeyframeValue;
import org.jetbrains.annotations.NotNull;

public abstract class VehicleKeyframe<E extends EntityVehicle> extends EntityKeyframe<E> {

    public final KeyframeValue.AngleV<E> zRot = registerAngleValue("zRot", EntityVehicle::getZRot, EntityVehicle::setZRot);
    public final KeyframeValue.FloatV<E> health = registerFloatValue("health", EntityVehicle::getHealth, EntityVehicle::setHealth);
    public final KeyframeValue.FloatV<E> shield = registerFloatValue("shield", EntityVehicle::getArmor, EntityVehicle::setArmor);
    public final KeyframeValue.FloatV<E> throttle = registerFloatValue("throttle", EntityVehicle::getCurrentThrottle, EntityVehicle::setCurrentThrottle);
    public final KeyframeValue.BoolV<E> gear = registerBooleanValue("gear", EntityVehicle::isLandingGear, EntityVehicle::setLandingGear);
    public final KeyframeValue.IntV<E> baseTextureIndex = registerIntValue("baseTextureIndex",
            entity -> entity.textureManager.getBaseTextureIndex(),
            (entity, value) -> entity.textureManager.setBaseTexture(value));
    public final RadarKeyframeValue<E> pings = registerValue(new RadarKeyframeValue<>());

    protected VehicleKeyframe() {
        super();
    }

    public VehicleKeyframe(@NotNull E entity) {
        super(entity);
    }

    public VehicleKeyframe(@NotNull JsonObject data) {
        super(data);
    }

    public static class Generic extends VehicleKeyframe<EntityVehicle> {
        protected Generic() {
            super();
        }
        public Generic(@NotNull EntityVehicle entity) {
            super(entity);
        }
        public Generic(@NotNull JsonObject data) {
            super(data);
        }
    }
}
