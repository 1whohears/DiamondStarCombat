package com.onewhohears.dscombat.data.replay;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.weapon.EntityMissile;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class EntityKeyframe {

    public final Vec3 pos;
    public final Vec3 vel;
    public final float xRot, yRot;

    public EntityKeyframe(Entity entity) {
        this.pos = entity.position();
        this.vel = entity.getDeltaMovement();
        this.xRot = entity.getXRot();
        this.yRot = entity.getYRot();
    }

    public EntityKeyframe(JsonObject data) {
        this.pos = UtilParse.readVec3(data, "pos");
        this.vel = UtilParse.readVec3(data, "vel");
        this.xRot = UtilParse.getFloatSafe(data, "xRot", 0);
        this.yRot = UtilParse.getFloatSafe(data, "yRot", 0);
    }

    public final JsonObject getSaveData() {
        JsonObject data = new JsonObject();
        addSaveData(data);
        return data;
    }

    protected void addSaveData(JsonObject data) {
        UtilParse.writeVec3(data, "pos", pos);
        UtilParse.writeVec3(data, "vel", pos);
        data.addProperty("xRot", xRot);
        data.addProperty("yRot", yRot);
    }

    public static class VehicleKeyframe extends EntityKeyframe {

        public final float roll;
        public final float fuel, fuelMax;
        public final float mass;

        public VehicleKeyframe(EntityVehicle vehicle) {
            super(vehicle);
            this.roll = vehicle.zRot;
            this.fuel = vehicle.getCurrentFuel();
            this.fuelMax = vehicle.getMaxFuel();
            this.mass = vehicle.getTotalMass();
        }

        public VehicleKeyframe(JsonObject data) {
            super(data);
            this.roll = UtilParse.getFloatSafe(data, "roll", 0);
            this.fuel = UtilParse.getFloatSafe(data, "fuel", 0);
            this.fuelMax = UtilParse.getFloatSafe(data, "fuelMax", 0);
            this.mass = UtilParse.getFloatSafe(data, "mass", 0);
        }

        @Override
        protected void addSaveData(JsonObject data) {
            super.addSaveData(data);
            data.addProperty("roll", roll);
            data.addProperty("fuel", fuel);
            data.addProperty("fuelMax", fuelMax);
            data.addProperty("mass", mass);
        }
    }

    public static class WeaponKeyframe extends EntityKeyframe {

        public final int age;
        public final int ageMax;

        public WeaponKeyframe(EntityWeapon weapon) {
            super(weapon);
            this.age = weapon.getAge();
            this.ageMax = weapon.getMaxAge();
        }

        public WeaponKeyframe(JsonObject data) {
            super(data);
            this.age = UtilParse.getIntSafe(data, "age", 0);
            this.ageMax = UtilParse.getIntSafe(data, "ageMax", 0);
        }

        @Override
        protected void addSaveData(JsonObject data) {
            super.addSaveData(data);
            data.addProperty("age", age);
            data.addProperty("ageMax", ageMax);
        }
    }

    public static class MissileKeyframe extends WeaponKeyframe {

        public final Vec3 targetPos;

        public MissileKeyframe(EntityMissile missile) {
            super(missile);
            this.targetPos = missile.getTargetPos();
        }

        public MissileKeyframe(JsonObject data) {
            super(data);
            this.targetPos = UtilParse.readVec3(data, "targetPos");
        }

        @Override
        protected void addSaveData(JsonObject data) {
            super.addSaveData(data);
            UtilParse.writeVec3(data, "targetPos", targetPos);
        }
    }

}
