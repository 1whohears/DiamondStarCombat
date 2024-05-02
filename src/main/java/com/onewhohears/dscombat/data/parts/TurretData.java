package com.onewhohears.dscombat.data.parts;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.data.weapon.stats.WeaponStats;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.entity.parts.EntityPart;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.init.ModEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;

public class TurretData extends SeatData implements LoadableRecipePartData {
	
	private final String[] compatible;
	private String weaponId;
	private final float health;
	private int ammo = 0, maxAmmo = 0;
	
	public TurretData(float weight, ResourceLocation itemid, SlotType[] compatibleSlots, 
			String turrentEntityKey, String weaponId, String[] compatible, boolean filled, float health) {
		super(weight, itemid, compatibleSlots);
		this.compatible = compatible;
		this.weaponId = weaponId;
		if (!WeaponPresets.get().has(weaponId)) this.weaponId = "";
		this.entityTypeKey = turrentEntityKey;
		this.health = health;
		WeaponStats data = WeaponPresets.get().get(weaponId);
		if (data != null) {
			maxAmmo = data.getMaxAmmo();
			if (filled) ammo = maxAmmo;
		}
	}

	public void read(CompoundTag tag) {
		super.read(tag);
		ammo = tag.getInt("ammo");
		if (tag.contains("weaponId")) weaponId = tag.getString("weaponId");
	}
	
	public CompoundTag write() {
		CompoundTag tag = super.write();
		tag.putInt("ammo", ammo);
		tag.putString("weaponId", weaponId);
		return tag;
	}

	public void read(FriendlyByteBuf buffer) {
		super.read(buffer);
		ammo = buffer.readInt();
		weaponId = buffer.readUtf();
	}
	
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
		buffer.writeInt(ammo);
		buffer.writeUtf(weaponId);
	}
	
	@Override
	public PartType getType() {
		return PartType.TURRENT;
	}
	
	@Override
	public void serverSetup(EntityVehicle craft, String slotId, Vec3 pos) {
		super.serverSetup(craft, slotId, pos);
		EntityTurret turret = getTurret(slotId);
		if (turret == null) return;
		turret.setAmmo(ammo);
	}
	
	@Nullable
	public EntityTurret getTurret(String slotId) {
		EntityVehicle craft = getParent();
		if (craft == null) return null;
		for (EntityPart part : craft.getPartEntities()) 
			if (part.getPartType() == getType() && part.getSlotId().equals(slotId)
					&& part instanceof EntityTurret turret)
				return turret;
		return null;
	}
	
	@Override
	public void setUpPartEntity(EntityPart part, EntityVehicle craft, String slotId, Vec3 pos, float health) {
		super.setUpPartEntity(part, craft, slotId, pos, health);
		if (!(part instanceof EntityTurret turret)) return;
		turret.setWeaponId(weaponId);
	}
	
	@Override
	public EntityType<?> getDefaultExternalEntity() {
		return ModEntities.AA_TURRET.get();
	}
	
	@Override
	public float getExternalEntityDefaultHealth() {
		return health;
	}
	
	public void setAmmo(int ammo) {
		this.ammo = ammo;
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
	
	public boolean isWeaponCompatible(String preset) {
		if (preset == null) return false;
		for (int i = 0; i < compatible.length; ++i) 
			if (compatible[i].equals(preset)) 
				return true;
		return false;
	}

	@Override
	public float getCurrentAmmo() {
		return ammo;
	}

	@Override
	public float getMaxAmmo() {
		return maxAmmo;
	}

	@Override
	public void setCurrentAmmo(float ammo) {
		this.ammo = (int)ammo;
	}

	@Override
	public void setMaxAmmo(float max) {
		this.maxAmmo = (int)max;
	}

	@Override
	public boolean isCompatibleWithAmmoContinuity(String continuity) {
		return isWeaponCompatible(continuity);
	}

	@Override
	public boolean updateContinuityIfEmpty() {
		return true;
	}

	@Override
	public void setContinuity(String continuity) {
		this.weaponId = continuity;
	}

	@Override
	public String getContinuity() {
		return getWeaponId();
	}
	
	@Override
	public boolean isContinuityEmpty() {
		return getContinuity() == null || getContinuity().isEmpty() || getCurrentAmmo() == 0;
	}
	
	public String getWeaponId() {
		return weaponId;
	}
	
}
