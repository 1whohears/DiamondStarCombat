package com.onewhohears.dscombat.data.parts.stats;

import java.util.List;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.parts.PartType;
import com.onewhohears.dscombat.data.parts.instance.TurretInstance;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.init.ModEntities;
import com.onewhohears.minigames.util.UtilParse;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;

public class TurretStats extends SeatStats {
	
	private final String[] compatible;
	private final float maxHealth;
	private final int maxAmmo;
	
	public TurretStats(ResourceLocation key, JsonObject json) {
		super(key, json);
		List<String> list = WeaponPresets.get().getCompatibleWeapons(getId());
		compatible = list.toArray(new String[list.size()]);
		maxHealth = UtilParse.getFloatSafe(json, "maxHealth", 0);
		maxAmmo = UtilParse.getIntSafe(json, "maxAmmo", 0);
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
		return ModEntities.AA_TURRET.get();
	}
	
	@Override
	public float getExternalEntityDefaultHealth() {
		return getMaxHealth();
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
	}

}
