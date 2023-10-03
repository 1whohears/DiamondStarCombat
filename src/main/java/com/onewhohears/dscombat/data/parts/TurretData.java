package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.entity.parts.EntityTurret.ShootType;
import com.onewhohears.dscombat.entity.parts.EntityVehiclePart;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.phys.Vec3;

public class TurretData extends SeatData {
	
	private final String weaponId;
	private final RotBounds rotBounds;
	private final float health;
	private final Vec3 offset;
	private final double weaponOffset;
	private final ShootType shootType;
	private int ammo = 0;
	private int max = 0;
	
	public TurretData(float weight, ResourceLocation itemid, SlotType[] compatibleSlots, 
			String weaponId, RotBounds rotBounds, boolean filled, float health,
			String modelId, EntityDimensions size, Vec3 offset, double weaponOffset, ShootType shootType) {
		super(weight, itemid, compatibleSlots, modelId, size);
		this.weaponId = weaponId;
		this.rotBounds = rotBounds;
		this.health = health;
		this.offset = offset;
		this.weaponOffset = weaponOffset;
		this.shootType = shootType;
		WeaponData data = WeaponPresets.get().getPreset(weaponId);
		if (data != null) {
			if (filled) this.ammo = data.getMaxAmmo();
			this.max = data.getMaxAmmo();
		}
	}

	public TurretData(CompoundTag tag) {
		super(tag);
		weaponId = tag.getString("weaponId");
		rotBounds = new RotBounds(tag);
		health = tag.getFloat("health");
		ammo = tag.getInt("ammo");
		max = tag.getInt("max");
		offset = UtilParse.readVec3(tag, "offset");
		weaponOffset = tag.getFloat("weaponOffset");
		shootType = ShootType.valueOf(tag.getString("shootType"));
	}
	
	public CompoundTag write() {
		CompoundTag tag = super.write();
		tag.putString("weaponId", weaponId);
		rotBounds.write(tag);
		tag.putFloat("health", health);
		tag.putInt("ammo", ammo);
		tag.putInt("max", max);
		UtilParse.writeVec3(tag, offset, "offset");
		tag.putFloat("weaponOffset", (float)weaponOffset);
		tag.putString("shootType", shootType.name());
		return tag;
	}

	public TurretData(FriendlyByteBuf buffer) {
		super(buffer);
		weaponId = buffer.readUtf();
		rotBounds = new RotBounds(buffer);
		health = buffer.readFloat();
		ammo = buffer.readInt();
		max = buffer.readInt();
		offset = DataSerializers.VEC3.read(buffer);
		weaponOffset = buffer.readFloat();
		shootType = ShootType.values()[buffer.readInt()];
	}
	
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
		buffer.writeUtf(weaponId);
		rotBounds.write(buffer);
		buffer.writeFloat(health);
		buffer.writeInt(ammo);
		buffer.writeInt(max);
		DataSerializers.VEC3.write(buffer, offset);
		buffer.writeFloat((float)weaponOffset);
		buffer.writeInt(shootType.ordinal());
	}
	
	@Override
	public PartType getType() {
		return PartType.TURRENT;
	}
	
	@Override
	public EntityVehiclePart getPartEntity() {
		EntityTurret turret = new EntityTurret(getParent(), getModelId(), getExternalEntitySize(),
				getSlotId(), getRelPos(), getZRot(), getPassengerOffset(), 
				getWeaponOffset(), getRotBounds(), getShootType());
		turret.setHealth(health);
		return turret;
	}

	@Override
	public boolean hasExternalPartEntity() {
		return true;
	}
	
	public void setAmmo(int ammo) {
		this.ammo = ammo;
	}
	
	public Vec3 getPassengerOffset() {
		return offset;
	}
	
	public double getWeaponOffset() {
		return weaponOffset;
	}
	
	public RotBounds getRotBounds() {
		return rotBounds;
	}
	
	public ShootType getShootType() {
		return shootType;
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
