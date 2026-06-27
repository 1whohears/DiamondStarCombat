package com.onewhohears.dscombat.data.parts.stats;

import java.util.List;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.parts.PartType;
import com.onewhohears.dscombat.data.parts.instance.TurretInstance;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.init.ModEntities;

import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;

public class TurretStats extends SeatStats {
	
	private final String[] compatible;
	private final float maxHealth;
	private final int maxAmmo;
	private final double weaponOffset;
	private final EntityTurret.ShootType shootType;
	private final RotBounds rotBounds;
	
	public TurretStats(ResourceLocation key, JsonObject json) {
		super(key, json);
		List<String> list = WeaponPresets.get().getCompatibleWeapons(getId());
		compatible = list.toArray(new String[list.size()]);
		maxHealth = UtilParse.getFloatSafe(json, "maxHealth", 0);
		maxAmmo = UtilParse.getIntSafe(json, "maxAmmo", 0);
		weaponOffset = UtilParse.getFloatSafe(json, "weaponOffset", 0);
		shootType = UtilParse.getEnumSafe(json, "shootType", EntityTurret.ShootType.class);
		rotBounds = RotBounds.getFromJson(json);
	}

	@Override
	public JsonPresetType getType() {
		return PartType.TURRENT;
	}

	@Override
	public JsonPresetInstance<?> createPresetInstance() {
		return new TurretInstance<>(this);
	}
	
	public boolean isWeaponCompatible(String preset) {
		if (preset == null) return false;
		for (int i = 0; i < compatible.length; ++i) 
			if (compatible[i].equals(preset)) 
				return true;
		return false;
	}
	
	public float getMaxHealth() {
		return maxHealth;
	}
	
	public int getMaxAmmo() {
		return maxAmmo;
	}
	
	@Override
	public EntityType<?> getDefaultExternalEntity() {
		return ModEntities.TURRET.get();
	}
	
	@Override
	public float getExternalEntityDefaultHealth() {
		return getMaxHealth();
	}

    public double getWeaponOffset() {
        return weaponOffset;
    }

    public EntityTurret.ShootType getShootType() {
        return shootType;
    }

    public RotBounds getRotBounds() {
        return rotBounds;
    }

    public static class RotBounds {
		public final float minRotX, maxRotX;
		public final float rotRate;
		public static RotBounds create(float rotRate, float maxLookUpAngle, float maxLookDownAngle) {
			return new RotBounds(Mth.abs(rotRate), -Mth.abs(maxLookUpAngle), Mth.abs(maxLookDownAngle));
		}
		private RotBounds(float rotRate, float minRotX, float maxRotX) {
			this.minRotX = minRotX;
			this.maxRotX = maxRotX;
			this.rotRate = rotRate;
		}
		public RotBounds(CompoundTag tag) {
			this.minRotX = tag.getFloat("minRotX");
			this.maxRotX = tag.getFloat("maxRotX");
			this.rotRate = tag.getFloat("rotRate");
		}
		public void write(CompoundTag tag) {
			tag.putFloat("minRotX", minRotX);
			tag.putFloat("maxRotX", maxRotX);
			tag.putFloat("rotRate", rotRate);
		}
		public RotBounds(FriendlyByteBuf buffer) {
			this.minRotX = buffer.readFloat();
			this.maxRotX = buffer.readFloat();
			this.rotRate = buffer.readFloat();
		}
		public void write(FriendlyByteBuf buffer) {
			buffer.writeFloat(minRotX);
			buffer.writeFloat(maxRotX);
			buffer.writeFloat(rotRate);
		}
		public static RotBounds getFromJson(JsonObject json) {
			JsonObject rotBounds = UtilParse.getJsonSafe(json, "rotBounds");
			return new RotBounds(UtilParse.getFloatSafe(rotBounds, "rotRate", 0),
					UtilParse.getFloatSafe(rotBounds, "minRotX", 0),
					UtilParse.getFloatSafe(rotBounds, "maxRotX", 0));
		}
		public void writeToJson(JsonObject json) {
			JsonObject rotBounds = new JsonObject();
			rotBounds.addProperty("rotRate", rotRate);
			rotBounds.addProperty("minRotX", minRotX);
			rotBounds.addProperty("maxRotX", maxRotX);
			json.add("rotBounds", rotBounds);
		}
	}

	@Override
	public boolean isCraftableWeaponPart() {
		return true;
	}

}
