package com.onewhohears.dscombat.init;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.BulletData;
import com.onewhohears.dscombat.data.WeaponData;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DataSerializers {
	
	public static final DeferredRegister<DataSerializerEntry> DATA_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.DATA_SERIALIZERS, DSCombatMod.MODID);

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
				return null;
			case BULLET:
				return new BulletData(buffer);
			case ROCKET:
				return null;
			}
			return null;
		}

		@Override
		public WeaponData copy(WeaponData w) {
			return w.copy();
		}
    	
    };

    public static final RegistryObject<DataSerializerEntry> SERIALIZER_ENTRY_QUATERNION = DATA_SERIALIZERS
    		.register("quaternion", () -> new DataSerializerEntry(QUATERNION));
    
    public static final RegistryObject<DataSerializerEntry> SERIALIZER_ENTRY_VEC3 = DATA_SERIALIZERS
    		.register("vec3", () -> new DataSerializerEntry(VEC3));
    
    public static final RegistryObject<DataSerializerEntry> SERIALIZER_ENTRY_WEAPONDATA = DATA_SERIALIZERS
    		.register("weapondata", () -> new DataSerializerEntry(WEAPON_DATA));
}
