package com.onewhohears.dscombat.data.parts.stats;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.entity.parts.hitbox.TurretHitbox;
import com.onewhohears.dscombat.entity.parts.hitbox.TurretHitboxData;
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
import net.minecraft.world.phys.Vec3;

public class TurretStats extends SeatStats {
	
	private final String[] compatible;
	private final float maxHealth;
	private final int maxAmmo;
	private final Vec3[] weaponOffsets; // массив точек вылета главного оружия
	private final boolean alternatingBarrels; // поочерёдная стрельба из стволов
	private final double muzzleParticleOffset;
	private final EntityTurret.ShootType shootType;
	private final RotBounds rotBounds;
	private final String crosshair;
	private final float zoom;
	private final boolean showRangeMeter;
	private final boolean fixedPassengerOffset;
	private TurretHitboxData[] hitboxData;

	// --- Smoke grenade launcher (built into turret) ---
	private final int maxSmokeGrenades;
	private final int smokeFuseTicks;
	private final int smokeSmokeDuration;
	private final float smokeSmokeRadius;
	private final String smokeDeploySoundKey;
	private SmokeGrenadeLauncherDef[] smokeLaunchers;

	/** Definition of a single smoke grenade launcher tube on the turret. */
	public static class SmokeGrenadeLauncherDef {
		public final Vec3 pos;
		/** Yaw offset in degrees relative to turret forward. */
		public final float yaw;
		/** Pitch offset in degrees (positive = upward). */
		public final float pitch;
		public SmokeGrenadeLauncherDef(Vec3 pos, float yaw, float pitch) {
			this.pos = pos; this.yaw = yaw; this.pitch = pitch;
		}
	}

	public TurretStats(ResourceLocation key, JsonObject json) {
		super(key, json);
		List<String> list = WeaponPresets.get().getCompatibleWeapons(getId());
		compatible = list.toArray(new String[list.size()]);
		maxHealth = UtilParse.getFloatSafe(json, "maxHealth", 0);
		maxAmmo = UtilParse.getIntSafe(json, "maxAmmo", 0);
		// Поддержка трёх форматов:
		//   1. "weaponOffsets": [{x,y,z}, ...] — массив точек вылета (новый)
		//   2. "weaponOffset": {x, y, z}        — одна точка (объект)
		//   3. "weaponOffset": <число>           — только Y-смещение (старый)
		if (json.has("weaponOffsets") && json.get("weaponOffsets").isJsonArray()) {
			JsonArray arr = json.getAsJsonArray("weaponOffsets");
			weaponOffsets = new Vec3[arr.size()];
			for (int i = 0; i < arr.size(); i++) {
				JsonObject o = arr.get(i).getAsJsonObject();
				weaponOffsets[i] = new Vec3(
					UtilParse.getFloatSafe(o, "x", 0),
					UtilParse.getFloatSafe(o, "y", 0),
					UtilParse.getFloatSafe(o, "z", 0)
				);
			}
		} else if (json.has("weaponOffset")) {
			if (json.get("weaponOffset").isJsonObject()) {
				weaponOffsets = new Vec3[]{ UtilParse.readVec3(json, "weaponOffset") };
			} else {
				// Старый формат - число означает только Y смещение
				double yOffset = UtilParse.getFloatSafe(json, "weaponOffset", 0);
				weaponOffsets = new Vec3[]{ new Vec3(0, yOffset, 0) };
			}
		} else {
			weaponOffsets = new Vec3[]{ Vec3.ZERO };
		}
		alternatingBarrels = UtilParse.getBooleanSafe(json, "alternatingBarrels", false);
		muzzleParticleOffset = UtilParse.getFloatSafe(json, "muzzleParticleOffset", 0.2f);
		shootType = UtilParse.getEnumSafe(json, "shootType", EntityTurret.ShootType.class);
		rotBounds = RotBounds.getFromJson(json);
		crosshair = UtilParse.getStringSafe(json, "crosshair", "");
		zoom = UtilParse.getFloatSafe(json, "zoom", 1.0f);
		showRangeMeter = UtilParse.getBooleanSafe(json, "showRangeMeter", false);
		fixedPassengerOffset = UtilParse.getBooleanSafe(json, "fixedPassengerOffset", false);
		// smoke grenades
		maxSmokeGrenades   = UtilParse.getIntSafe(json, "max_smoke_grenades", 0);
		smokeFuseTicks     = UtilParse.getIntSafe(json, "smoke_fuse_ticks", 40);
		smokeSmokeDuration = UtilParse.getIntSafe(json, "smoke_duration", 300);
		smokeSmokeRadius   = UtilParse.getFloatSafe(json, "smoke_radius", 5f);
		smokeDeploySoundKey = UtilParse.getStringSafe(json, "smoke_deploy_sound_key", "");
		System.out.println("DEBUG: TurretStats loading - presetId: " + getId() + ", max_smoke_grenades: " + maxSmokeGrenades);
		if (json.has("smoke_launchers")) {
			JsonArray arr = json.getAsJsonArray("smoke_launchers");
			List<SmokeGrenadeLauncherDef> defs = new ArrayList<>();
			for (int i = 0; i < arr.size(); i++) {
				JsonObject o = arr.get(i).getAsJsonObject();
				Vec3 pos   = UtilParse.readVec3(o, "pos");
				float yaw  = UtilParse.getFloatSafe(o, "yaw", 0f);
				float pitch = UtilParse.getFloatSafe(o, "pitch", 15f);
				defs.add(new SmokeGrenadeLauncherDef(pos, yaw, pitch));
			}
			smokeLaunchers = defs.toArray(new SmokeGrenadeLauncherDef[0]);
			System.out.println("DEBUG: TurretStats loaded " + smokeLaunchers.length + " smoke launchers");
		} else {
			smokeLaunchers = new SmokeGrenadeLauncherDef[0];
			System.out.println("DEBUG: TurretStats - no smoke_launchers in JSON");
		}
	}

	public boolean hasSmokeGrenades() { 
		boolean result = maxSmokeGrenades > 0 && smokeLaunchers.length > 0;
		System.out.println("DEBUG: TurretStats.hasSmokeGrenades - maxSmokeGrenades: " + maxSmokeGrenades + ", launchers: " + smokeLaunchers.length + ", result: " + result);
		return result;
	}
	public int getMaxSmokeGrenades()   { return maxSmokeGrenades; }
	public int getSmokeFuseTicks()     { return smokeFuseTicks; }
	public int getSmokeSmokeDuration() { return smokeSmokeDuration; }
	public float getSmokeSmokeRadius() { return smokeSmokeRadius; }
	public String getSmokeDeploySoundKey() { return smokeDeploySoundKey; }
	public SmokeGrenadeLauncherDef[] getSmokeLaunchers() { return smokeLaunchers; }

	public TurretHitboxData[] getHitboxData() {
		if (hitboxData == null) {
			if (!getJsonData().has("hitboxes")) {
				hitboxData = new TurretHitboxData[0];
			} else {
				JsonArray arr = getJsonData().get("hitboxes").getAsJsonArray();
				hitboxData = new TurretHitboxData[arr.size()];
				for (int i = 0; i < arr.size(); ++i)
					hitboxData[i] = new TurretHitboxData(arr.get(i).getAsJsonObject(), i);
			}
		}
		return hitboxData;
	}

	public List<TurretHitbox> createTurretHitboxes(EntityTurret turret) {
		List<TurretHitbox> list = new ArrayList<>();
		for (TurretHitboxData d : getHitboxData())
			list.add(new TurretHitbox(turret, d));
		return list;
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

    public Vec3 getWeaponOffset() {
        return weaponOffsets[0];
    }

	/** Все точки вылета главного оружия. Всегда содержит хотя бы один элемент. */
	public Vec3[] getWeaponOffsets() {
		return weaponOffsets;
	}

	/** Если true — стволы стреляют поочерёдно. Если false — все сразу. */
	public boolean isAlternatingBarrels() {
		return alternatingBarrels;
	}

	/**
	 * Additional offset along the barrel for particle emission point (in blocks).
	 * Allows per-turret adjustment so particles appear at the muzzle, not at the seat.
	 */
	public double getMuzzleParticleOffset() {
		return muzzleParticleOffset;
	}

    public EntityTurret.ShootType getShootType() {
        return shootType;
    }

    public RotBounds getRotBounds() {
        return rotBounds;
    }

	public String getCrosshair() {
		return crosshair;
	}

	public float getZoom() {
		return zoom;
	}

	public boolean isShowRangeMeter() {
		return showRangeMeter;
	}

	public boolean isFixedPassengerOffset() {
		return fixedPassengerOffset;
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
