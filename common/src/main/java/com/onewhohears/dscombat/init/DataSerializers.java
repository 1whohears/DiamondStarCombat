package com.onewhohears.dscombat.init;

import com.onewhohears.dscombat.data.parts.PartPresets;
import com.onewhohears.dscombat.data.parts.instance.PartInstance;
import com.onewhohears.dscombat.data.parts.stats.PartStats;
import com.onewhohears.dscombat.data.radar.RadarStats.RadarMode;

import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.math.QuaternionF;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class DataSerializers {

    public static final EntityDataSerializer<QuaternionF> QuaternionF = register("QuaternionF", new EntityDataSerializer<>() {
		@Override
		public void write(FriendlyByteBuf buffer, QuaternionF q) {
			buffer.writeFloat(q.i());
			buffer.writeFloat(q.j());
			buffer.writeFloat(q.k());
			buffer.writeFloat(q.r());
		}
		@Override
		public @NotNull QuaternionF read(FriendlyByteBuf buffer) {
			return new QuaternionF(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
		}
		@Override
		public @NotNull QuaternionF copy(QuaternionF q) {
			return new QuaternionF(q);
		}
    });
    
    public static final EntityDataSerializer<Vec3> VEC3 = register("vec3", new EntityDataSerializer<>() {
		@Override
		public void write(FriendlyByteBuf buffer, Vec3 v) {
			buffer.writeFloat((float)v.x);
			buffer.writeFloat((float)v.y);
			buffer.writeFloat((float)v.z);
		}
		@Override
		public @NotNull Vec3 read(FriendlyByteBuf buffer) {
			return new Vec3(buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
		}
		@Override
		public @NotNull Vec3 copy(Vec3 v) {
			return new Vec3(v.x, v.y, v.z);
		}
    });
    
    public static final EntityDataSerializer<PartInstance<?>> PART_DATA = register("partdata", new EntityDataSerializer<>() {
		@Override
		public void write(FriendlyByteBuf buffer, PartInstance<?> p) {
			p.writeBuffer(buffer);
		}
		@Override
		public @NotNull PartInstance<?> read(FriendlyByteBuf buffer) {
			String presetId = buffer.readUtf();
			PartStats stats = PartPresets.get().get(presetId);
			PartInstance<?> data = stats.createPartInstance();
			data.readBuffer(buffer);
			return data;
		}
		@Override
		public @NotNull PartInstance<?> copy(PartInstance<?> p) {
			return p;
		}
    });
    
    public static final EntityDataSerializer<RadarMode> RADAR_MODE = register("radarmode",
            getEnumSerializer(RadarMode.class));
	public static final EntityDataSerializer<EntityVehicle.PermMode> PERM_MODE = register("permmode",
            getEnumSerializer(EntityVehicle.PermMode.class));
    
    private static <E extends Enum<E>> EntityDataSerializer<E> getEnumSerializer(Class<E> enumClass) {
    	return new EntityDataSerializer<>() {
    		@Override
    		public void write(FriendlyByteBuf buffer, E e) {
    			buffer.writeEnum(e);
    		}
    		@Override
    		public @NotNull E read(FriendlyByteBuf buffer) {
    			return buffer.<E>readEnum(enumClass);
    		}
    		@Override
    		public @NotNull E copy(E e) {
    			return e;
    		}
    	};
    }

    @ExpectPlatform
    public static <T> EntityDataSerializer<T> register(String id, EntityDataSerializer<T> serializer) {
        throw new AssertionError();
    }
}
