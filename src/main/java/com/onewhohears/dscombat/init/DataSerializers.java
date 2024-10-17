package com.onewhohears.dscombat.init;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.parts.PartPresets;
import com.onewhohears.dscombat.data.parts.instance.PartInstance;
import com.onewhohears.dscombat.data.parts.stats.PartStats;
import com.onewhohears.dscombat.data.radar.RadarStats.RadarMode;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.joml.Quaternionf;

public class DataSerializers {
	
	public static final DeferredRegister<EntityDataSerializer<?>> DATA_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, DSCombatMod.MODID);

    public static void register(IEventBus eventBus) {
        DATA_SERIALIZERS.register(eventBus);
    }

    public static final EntityDataSerializer<Quaternionf> QUATERNION = new EntityDataSerializer<>() {
		@Override
		public void write(FriendlyByteBuf buffer, Quaternionf q) {
			buffer.writeFloat(q.x());
			buffer.writeFloat(q.y());
			buffer.writeFloat(q.z());
			buffer.writeFloat(q.w());
		}
		@Override
		public Quaternionf read(FriendlyByteBuf buffer) {
			return new Quaternionf(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
		}
		@Override
		public Quaternionf copy(Quaternionf q) {
			return new Quaternionf(q);
		}
    };
    
    public static final EntityDataSerializer<Vec3> VEC3 = new EntityDataSerializer<>() {
		@Override
		public void write(FriendlyByteBuf buffer, Vec3 v) {
			buffer.writeFloat((float)v.x);
			buffer.writeFloat((float)v.y);
			buffer.writeFloat((float)v.z);
		}
		@Override
		public Vec3 read(FriendlyByteBuf buffer) {
			return new Vec3(buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
		}
		@Override
		public Vec3 copy(Vec3 v) {
			return new Vec3(v.x, v.y, v.z);
		}
    };
    
    public static final EntityDataSerializer<PartInstance<?>> PART_DATA = new EntityDataSerializer<>() {
		@Override
		public void write(FriendlyByteBuf buffer, PartInstance<?> p) {
			p.writeBuffer(buffer);
		}
		@Override
		public PartInstance<?> read(FriendlyByteBuf buffer) {
			String presetId = buffer.readUtf();
			PartStats stats = PartPresets.get().get(presetId);
			PartInstance<?> data = stats.createPartInstance();
			data.readBuffer(buffer);
			return data;
		}
		@Override
		public PartInstance<?> copy(PartInstance<?> p) {
			return p;
		}
    };
    
    public static final EntityDataSerializer<RadarMode> RADAR_MODE = getEnumSerializer(RadarMode.class);
    
    private static <E extends Enum<E>> EntityDataSerializer<E> getEnumSerializer(Class<E> enumClass) {
    	return new EntityDataSerializer<>() {
    		@Override
    		public void write(FriendlyByteBuf buffer, E e) {
    			buffer.writeEnum(e);
    		}
    		@Override
    		public E read(FriendlyByteBuf buffer) {
    			return buffer.<E>readEnum(enumClass);
    		}
    		@Override
    		public E copy(E e) {
    			return e;
    		}
    	};
    }

    public static final RegistryObject<EntityDataSerializer<?>> SERIALIZER_ENTRY_QUATERNION = DATA_SERIALIZERS
    		.register("quaternion", () -> QUATERNION);
    public static final RegistryObject<EntityDataSerializer<?>> SERIALIZER_ENTRY_VEC3 = DATA_SERIALIZERS
    		.register("vec3", () -> VEC3);
    public static final RegistryObject<EntityDataSerializer<?>> SERIALIZER_ENTRY_PARTDATA = DATA_SERIALIZERS
    		.register("partdata", () -> PART_DATA);
    public static final RegistryObject<EntityDataSerializer<?>> SERIALIZER_ENTRY_RADARMODE = DATA_SERIALIZERS
    		.register("radarmode", () -> RADAR_MODE);
}
