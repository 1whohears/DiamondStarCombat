package com.onewhohears.dscombat.data.replay;

import com.google.gson.JsonObject;
import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;

public abstract class KeyframeValue<T, E extends Entity> {
    public final String name;
    private final Function<E, T> entityReader;
    protected T value;
    public KeyframeValue(String name, Function<E, T> entityReader) {
        this.name = name;
        this.entityReader = entityReader;
    }
    public void readFromEntity(E entity) {
        value = entityReader.apply(entity);
    }
    public abstract void readFromData(JsonObject data);
    public abstract void writeToData(JsonObject data);
    public abstract T get();

    public static class IntV<E extends Entity> extends KeyframeValue<Integer, E> {
        public IntV(String name, Function<E, Integer> entityReader) {
            super(name, entityReader);
        }
        @Override
        public void readFromData(JsonObject data) {
            value = UtilParse.getIntSafe(data, name, 0);
        }
        @Override
        public void writeToData(JsonObject data) {
            data.addProperty(name, value);
        }
        @Override
        public Integer get() {
            return value;
        }
    }

    public static class LongV<E extends Entity> extends KeyframeValue<Long, E> {
        public LongV(String name, Function<E, Long> entityReader) {
            super(name, entityReader);
        }
        @Override
        public void readFromData(JsonObject data) {
            value = !data.has(name) ? 0 : data.get(name).getAsLong();
        }
        @Override
        public void writeToData(JsonObject data) {
            data.addProperty(name, value);
        }
        @Override
        public Long get() {
            return value;
        }
    }

    public static class FloatV<E extends Entity> extends KeyframeValue<Float, E> {
        public FloatV(String name, Function<E, Float> entityReader) {
            super(name, entityReader);
        }
        @Override
        public void readFromData(JsonObject data) {
            value = UtilParse.getFloatSafe(data, name, 0);
        }
        @Override
        public void writeToData(JsonObject data) {
            data.addProperty(name, value);
        }
        @Override
        public Float get() {
            return value;
        }
    }

    public static class Vec3V<E extends Entity> extends KeyframeValue<Vec3, E> {
        public Vec3V(String name, Function<E, Vec3> entityReader) {
            super(name, entityReader);
        }
        @Override
        public void readFromData(JsonObject data) {
            value = UtilParse.readVec3(data, name);
        }
        @Override
        public void writeToData(JsonObject data) {
            UtilParse.writeVec3(data, name, value);
        }
        @Override
        public Vec3 get() {
            return value;
        }
    }

}
