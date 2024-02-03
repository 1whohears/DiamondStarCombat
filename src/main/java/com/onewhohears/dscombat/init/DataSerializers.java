package com.onewhohears.dscombat.init;

import java.util.NoSuchElementException;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.radar.RadarData;
import com.onewhohears.dscombat.data.radar.RadarPresets;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.item.ItemPart;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DataSerializers {
	
	public static final DeferredRegister<EntityDataSerializer<?>> DATA_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, DSCombatMod.MODID);

    public static void register(IEventBus eventBus) {
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
    
    public static final EntityDataSerializer<RadarData> RADAR_DATA = new EntityDataSerializer<>() {

		@Override
		public void write(FriendlyByteBuf buffer, RadarData r) {
			r.writeBuffer(buffer);
		}

		@Override
		public RadarData read(FriendlyByteBuf buffer) {
			String id = buffer.readUtf();
			RadarData data = RadarPresets.get().getPreset(id);
			if (data == null) return null;
			data.readBuffer(buffer);
			return data;
		}

		@Override
		public RadarData copy(RadarData r) {
			return r;
		}
    	
    };
    
    public static final EntityDataSerializer<WeaponData> WEAPON_DATA = new EntityDataSerializer<>() {

		@Override
		public void write(FriendlyByteBuf buffer, WeaponData w) {
			w.writeBuffer(buffer);
		}

		@Override
		public WeaponData read(FriendlyByteBuf buffer) {
			String weaponId = buffer.readUtf();
			WeaponData data = WeaponPresets.get().getPreset(weaponId);
			if (data == null) return null;
			data.readBuffer(buffer);
			return data;
		}

		@Override
		public WeaponData copy(WeaponData w) {
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
			String itemId = buffer.readUtf();
			Item item;
			try {
				item = ForgeRegistries.ITEMS.getDelegate(new ResourceLocation(itemId)).get().get();
			} catch(NoSuchElementException e) {
				return null;
			}
			if (!(item instanceof ItemPart part)) return null;
			PartData data = part.getPartData();
			data.read(buffer);
			return data;
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
    
    public static final RegistryObject<EntityDataSerializer<?>> SERIALIZER_ENTRY_RADARDATA = DATA_SERIALIZERS
    		.register("radardata", () -> RADAR_DATA);
    
    public static final RegistryObject<EntityDataSerializer<?>> SERIALIZER_ENTRY_WEAPONDATA = DATA_SERIALIZERS
    		.register("weapondata", () -> WEAPON_DATA);
    
    public static final RegistryObject<EntityDataSerializer<?>> SERIALIZER_ENTRY_PARTDATA = DATA_SERIALIZERS
    		.register("partdata", () -> PART_DATA);
}
