package com.onewhohears.dscombat.data.replay;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.weapon.EntityMissile;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.onewholibs.util.UtilEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class EntityKeyframe<E extends Entity> {

    private final Map<String, KeyframeValue<?,E>> values = new HashMap<>();

    public final KeyframeValue.LongV<E> tick = registerLongValue("tick", entity -> UtilEntity.getLevel(entity).getGameTime());
    public final KeyframeValue.Vec3V<E> pos = registerVec3Value("pos", Entity::position);
    public final KeyframeValue.Vec3V<E> vel = registerVec3Value("vel", Entity::getDeltaMovement);
    public final KeyframeValue.FloatV<E> xRot = registerFloatValue("xRot", Entity::getXRot);
    public final KeyframeValue.FloatV<E> yRot = registerFloatValue("yRot", Entity::getYRot);

    public EntityKeyframe(E entity) {
        values.forEach((name, value) -> value.readFromEntity(entity));
    }

    protected <K extends KeyframeValue<?,E>> K registerValue(K value) {
        values.put(value.name, value);
        return value;
    }

    protected KeyframeValue.IntV<E> registerIntValue(String name, Function<E, Integer> entityReader) {
        return registerValue(new KeyframeValue.IntV<>(name, entityReader));
    }

    protected KeyframeValue.LongV<E> registerLongValue(String name, Function<E, Long> entityReader) {
        return registerValue(new KeyframeValue.LongV<>(name, entityReader));
    }

    protected KeyframeValue.FloatV<E> registerFloatValue(String name, Function<E, Float> entityReader) {
        return registerValue(new KeyframeValue.FloatV<>(name, entityReader));
    }

    protected KeyframeValue.Vec3V<E> registerVec3Value(String name, Function<E, Vec3> entityReader) {
        return registerValue(new KeyframeValue.Vec3V<>(name, entityReader));
    }

    public EntityKeyframe(JsonObject data) {
        values.forEach((name, value) -> value.readFromData(data));
    }

    public final JsonObject getSaveData() {
        JsonObject data = new JsonObject();
        addSaveData(data);
        return data;
    }

    protected void addSaveData(JsonObject data) {
        values.forEach((name, value) -> value.writeToData(data));
    }

    public static class VehicleKeyframe extends EntityKeyframe<EntityVehicle> {

        public final KeyframeValue.FloatV<EntityVehicle> zRot = registerFloatValue("zRot", entity -> entity.zRot);
        public final KeyframeValue.FloatV<EntityVehicle> fuel = registerFloatValue("fuel", EntityVehicle::getCurrentFuel);
        public final KeyframeValue.FloatV<EntityVehicle> fuelMax = registerFloatValue("fuelMax", EntityVehicle::getMaxFuel);
        public final KeyframeValue.FloatV<EntityVehicle> mass = registerFloatValue("mass", EntityVehicle::getTotalMass);
        public final KeyframeValue.FloatV<EntityVehicle> health = registerFloatValue("health", EntityVehicle::getHealth);
        public final KeyframeValue.FloatV<EntityVehicle> armor = registerFloatValue("armor", EntityVehicle::getArmor);

        public VehicleKeyframe(EntityVehicle vehicle) {
            super(vehicle);
        }

        public VehicleKeyframe(JsonObject data) {
            super(data);
        }
    }

    public static class WeaponKeyframe<E extends EntityWeapon> extends EntityKeyframe<E> {

        public final KeyframeValue.IntV<E> age = registerIntValue("age", EntityWeapon::getAge);

        public WeaponKeyframe(E weapon) {
            super(weapon);
        }

        public WeaponKeyframe(JsonObject data) {
            super(data);
        }
    }

    public static class MissileKeyframe extends WeaponKeyframe<EntityMissile> {

        public final KeyframeValue.Vec3V<EntityMissile> targetPos = registerVec3Value("targetPos", EntityMissile::getTargetPos);

        public MissileKeyframe(EntityMissile missile) {
            super(missile);
        }

        public MissileKeyframe(JsonObject data) {
            super(data);
        }
    }

}
