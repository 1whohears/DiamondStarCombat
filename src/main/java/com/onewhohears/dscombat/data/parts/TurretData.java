package com.onewhohears.dscombat.data.parts;

import java.util.NoSuchElementException;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
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
import net.minecraftforge.registries.ForgeRegistries;

public class TurretData extends SeatData {
	
	private final String weaponId;
	private final String turretEntityKey;
	private final RotBounds rotBounds;
	private final float health;
	private EntityType<? extends EntityTurret> turretType;
	private int ammo = 0;
	
	public TurretData(float weight, ResourceLocation itemid, SlotType[] compatibleSlots, 
			String turrentEntityKey, String weaponId, RotBounds rotBounds, boolean filled, float health) {
		super(weight, itemid, compatibleSlots);
		this.weaponId = weaponId;
		this.turretEntityKey = turrentEntityKey;
		this.rotBounds = rotBounds;
		this.health = health;
		if (filled) {
			WeaponData data = WeaponPresets.get().getPreset(weaponId);
			if (data != null) ammo = data.getMaxAmmo();
		}
	}

	public void read(CompoundTag tag) {
		super.read(tag);
		ammo = tag.getInt("ammo");
	}
	
	public CompoundTag write() {
		CompoundTag tag = super.write();
		tag.putInt("ammo", ammo);
		return tag;
	}

	public void read(FriendlyByteBuf buffer) {
		super.read(buffer);
		ammo = buffer.readInt();
	}
	
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
		buffer.writeInt(ammo);
	}
	
	@Override
	public PartType getType() {
		return PartType.TURRENT;
	}
	
	@Override
	public void serverSetup(EntityVehicle craft, String slotId, Vec3 pos) {
		setParent(craft);
		setRelPos(pos);
		EntityTurret t = getTurret(slotId);
		t.setAmmo(ammo);
	}
	
	@Nullable
	public EntityTurret getTurret(String slotId) {
		EntityVehicle craft = getParent();
		if (craft == null) return null;
		for (EntityPart part : craft.getPartEntities()) 
			if (part.getSlotId().equals(slotId) && part.getType().equals(getTurretType())) 
				return (EntityTurret) part;
		EntityTurret t = getTurretType().create(craft.level);
		t.setSlotId(slotId);
		t.setRelativePos(getRelPos());
		t.setHealth(health);
		t.setPos(craft.position());
		t.startRiding(craft);
		t.setWeaponId(weaponId);
		t.setRotBounds(rotBounds);
		craft.level.addFreshEntity(t);
		return t;
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
