package com.onewhohears.dscombat.data.parts;

import java.util.NoSuchElementException;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.entity.parts.EntityPart;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.init.ModEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class TurretData extends SeatData {
	
	private final String weaponId;
	private final String turretEntityKey;
	private final RotBounds rotBounds;
	private EntityType<? extends EntityTurret> turretType;
	private int ammo;
	private int max;
	
	public TurretData(float weight, ResourceLocation itemid, SlotType[] compatibleSlots, String turrentEntityKey, String weaponId, RotBounds rotBounds) {
		super(weight, itemid, compatibleSlots);
		this.weaponId = weaponId;
		this.turretEntityKey = turrentEntityKey;
		this.rotBounds = rotBounds;
		WeaponData data = WeaponPresets.get().getPreset(weaponId);
		if (data != null) {
			this.ammo = data.getMaxAmmo();
			this.max = data.getMaxAmmo();
		}
	}

	public TurretData(CompoundTag tag) {
		super(tag);
		weaponId = tag.getString("weaponId");
		turretEntityKey = tag.getString("turretEntity");
		rotBounds = new RotBounds(tag);
		ammo = tag.getInt("ammo");
		max = tag.getInt("max");
	}
	
	public CompoundTag write() {
		CompoundTag tag = super.write();
		tag.putString("weaponId", weaponId);
		tag.putString("turretEntity", turretEntityKey);
		rotBounds.write(tag);
		tag.putInt("ammo", ammo);
		tag.putInt("max", max);
		return tag;
	}

	public TurretData(FriendlyByteBuf buffer) {
		super(buffer);
		weaponId = buffer.readUtf();
		turretEntityKey = buffer.readUtf();
		rotBounds = new RotBounds(buffer);
		ammo = buffer.readInt();
		max = buffer.readInt();
	}
	
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
		buffer.writeUtf(weaponId);
		buffer.writeUtf(turretEntityKey);
		rotBounds.write(buffer);
		buffer.writeInt(ammo);
		buffer.writeInt(max);
	}
	
	@Override
	public PartType getType() {
		return PartType.TURRENT;
	}
	
	@Override
	public void serverSetup(EntityAircraft craft, String slotId, Vec3 pos) {
		setParent(craft);
		setRelPos(pos);
		EntityTurret t = getTurret(slotId);
		t.setAmmo(ammo);
	}
	
	@Nullable
	public EntityTurret getTurret(String slotId) {
		EntityAircraft craft = getParent();
		if (craft == null) return null;
		for (EntityPart part : craft.getPartEntities()) 
			if (part.getSlotId().equals(slotId) && part.getType().equals(getTurretType())) 
				return (EntityTurret) part;
		EntityTurret t = getTurretType().create(craft.level);
		t.setSlotId(slotId);
		t.setRelativePos(getRelPos());
		t.setPos(craft.position());
		t.startRiding(craft);
		t.setWeaponId(weaponId);
		t.setRotBounds(rotBounds);
		craft.level.addFreshEntity(t);
		return t;
	}
	
	@Override
	public boolean isSetup(String slotId, EntityAircraft craft) {
		for (EntityPart part : craft.getPartEntities()) 
			if (part.getPartType() == getType() && part.getSlotId().equals(slotId)) 
				return true;
		return false;
	}
	
	@Override
	public void serverRemove(String slotId) {
		for (EntityPart part : getParent().getPartEntities()) 
			if (part.getSlotId().equals(slotId)) 
				part.discard();
	}
	
	@SuppressWarnings("unchecked")
	public EntityType<? extends EntityTurret> getTurretType() {
		if (turretType == null) {
			try { turretType = (EntityType<? extends EntityTurret>) ForgeRegistries.ENTITY_TYPES
					.getDelegate(new ResourceLocation(turretEntityKey)).get().get(); }
			catch(NoSuchElementException e) { turretType = ModEntities.MINIGUN_TURRET.get(); }
			catch(ClassCastException e) { turretType = ModEntities.MINIGUN_TURRET.get(); }
		}
		return turretType;
	}
	
	public void setAmmo(int ammo) {
		this.ammo = ammo;
	}
	
	public RotBounds getRotBounds() {
		return rotBounds;
	}
	
	public static class RotBounds {
		public final float minRotX, maxRotX;
		//public final float minRotY, maxRotY;
		public final float rotRate;
		public RotBounds(float rotRate, /*float minRotY,*/ float minRotX, /*float maxRotY,*/ float maxRotX) {
			this.minRotX = minRotX;
			this.maxRotX = maxRotX;
			//this.minRotY = minRotY;
			//this.maxRotY = maxRotY;
			this.rotRate = rotRate;
		}
		public RotBounds(CompoundTag tag) {
			this.minRotX = tag.getFloat("minRotX");
			this.maxRotX = tag.getFloat("maxRotX");
			//this.minRotY = tag.getFloat("minRotY");
			//this.maxRotY = tag.getFloat("maxRotY");
			this.rotRate = tag.getFloat("rotRate");
		}
		public void write(CompoundTag tag) {
			tag.putFloat("minRotX", minRotX);
			tag.putFloat("maxRotX", maxRotX);
			//tag.putFloat("minRotY", minRotY);
			//tag.putFloat("maxRotY", maxRotY);
			tag.putFloat("rotRate", rotRate);
		}
		public RotBounds(FriendlyByteBuf buffer) {
			this.minRotX = buffer.readFloat();
			this.maxRotX = buffer.readFloat();
			//this.minRotY = buffer.readFloat();
			//this.maxRotY = buffer.readFloat();
			this.rotRate = buffer.readFloat();
		}
		public void write(FriendlyByteBuf buffer) {
			buffer.writeFloat(minRotX);
			buffer.writeFloat(maxRotX);
			//buffer.writeFloat(minRotY);
			//buffer.writeFloat(maxRotY);
			buffer.writeFloat(rotRate);
		}
	}
	
}
