package com.onewhohears.dscombat.init;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.BombData;
import com.onewhohears.dscombat.data.BulletData;
import com.onewhohears.dscombat.data.MissileData;
import com.onewhohears.dscombat.data.PartData;
import com.onewhohears.dscombat.data.PartsManager;
import com.onewhohears.dscombat.data.RadarData;
import com.onewhohears.dscombat.data.SeatData;
import com.onewhohears.dscombat.data.WeaponData;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DataSerializers {
	
	public static final DeferredRegister<EntityDataSerializer<?>> DATA_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, DSCombatMod.MODID);

    public static void init(IEventBus eventBus) {
        DATA_SERIALIZERS.register(eventBus);
    }

    public static final EntityDataSerializer<Quaternion> QUATERNION = new EntityDataSerializer<>() {

		@Override
		public void write(FriendlyByteBuf buffer, Quaternion q) {
			buffer.writeFloat(q.i());
			buffer.writeFloat(q.j());
			buffer.writeFloat(q.k());
			buffer.writeFloat(q.r());
		}

		@Override
		public Quaternion read(FriendlyByteBuf buffer) {
			return new Quaternion(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
		}

		@Override
		public Quaternion copy(Quaternion q) {
			return new Quaternion(q);
		}
    	
    };
    
    public static final EntityDataSerializer<Vec3> VEC3 = new EntityDataSerializer<>() {

		@Override
		public void write(FriendlyByteBuf buffer, Vec3 v) {
			buffer.writeDouble(v.x);
			buffer.writeDouble(v.y);
			buffer.writeDouble(v.z);
		}

		@Override
		public Vec3 read(FriendlyByteBuf buffer) {
			return new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
		}

		@Override
		public Vec3 copy(Vec3 v) {
			return new Vec3(v.x, v.y, v.z);
		}
    	
    };
    
    public static final EntityDataSerializer<WeaponData> WEAPON_DATA = new EntityDataSerializer<>() {

		@Override
		public void write(FriendlyByteBuf buffer, WeaponData w) {
			w.write(buffer);
		}

		@Override
		public WeaponData read(FriendlyByteBuf buffer) {
			int index = buffer.readInt();
			WeaponData.WeaponType type = WeaponData.WeaponType.values()[index];
			switch (type) {
			case BOMB:
				return new BombData(buffer);
			case BULLET:
				return new BulletData(buffer);
			case ROCKET:
				return new MissileData(buffer);
			}
			return null;
		}

		@Override
		public WeaponData copy(WeaponData w) {
			return w;
		}
    	
    };
    
    public static final EntityDataSerializer<PartsManager> PARTS_MANAGER = new EntityDataSerializer<>() {

		@Override
		public void write(FriendlyByteBuf buffer, PartsManager w) {
			w.write(buffer);
		}

		@Override
		public PartsManager read(FriendlyByteBuf buffer) {
			return new PartsManager(buffer);
		}

		@Override
		public PartsManager copy(PartsManager w) {
			return w;
		}
    	
    };
    
    public static final EntityDataSerializer<PartData> PART_DATA = new EntityDataSerializer<>() {

		@Override
		public void write(FriendlyByteBuf buffer, PartData p) {
			p.write(buffer);
		}

		@Override
		public PartData read(FriendlyByteBuf buffer) {
			int index = buffer.readInt();
			PartData.PartType type = PartData.PartType.values()[index];
			switch (type) {
			case SEAT:
				return new SeatData(buffer);
			case TURRENT:
				return null;
			case RADAR:
				return new RadarData(buffer);
			}
			return null;
		}

		@Override
		public PartData copy(PartData p) {
			return p;
		}
    	
    };

    public static final RegistryObject<EntityDataSerializer<?>> SERIALIZER_ENTRY_QUATERNION = DATA_SERIALIZERS
    		.register("quaternion", () -> QUATERNION);
    
    public static final RegistryObject<EntityDataSerializer<?>> SERIALIZER_ENTRY_VEC3 = DATA_SERIALIZERS
    		.register("vec3", () -> VEC3);
    
    public static final RegistryObject<EntityDataSerializer<?>> SERIALIZER_ENTRY_WEAPONDATA = DATA_SERIALIZERS
    		.register("weapondata", () -> WEAPON_DATA);
    
    public static final RegistryObject<EntityDataSerializer<?>> SERIALIZER_ENTRY_PARTSMANAGER = DATA_SERIALIZERS
    		.register("weaponsystem", () -> PARTS_MANAGER);
    
    public static final RegistryObject<EntityDataSerializer<?>> SERIALIZER_ENTRY_PARTDATA = DATA_SERIALIZERS
    		.register("partdata", () -> PART_DATA);
}
