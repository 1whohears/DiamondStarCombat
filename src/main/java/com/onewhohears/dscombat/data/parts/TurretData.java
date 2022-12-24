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
	private EntityType<? extends EntityTurret> turretType;
	private int ammo;
	private int max;
	
	public TurretData(float weight, ResourceLocation itemid, SlotType[] compatibleSlots, String turrentEntityKey, String weaponId) {
		super(weight, itemid, compatibleSlots);
		this.weaponId = weaponId;
		this.turretEntityKey = turrentEntityKey;
		WeaponData data = WeaponPresets.getById(weaponId);
		if (data != null) {
			this.ammo = data.getMaxAmmo();
			this.max = data.getMaxAmmo();
		}
	}

	public TurretData(CompoundTag tag) {
		super(tag);
		weaponId = tag.getString("weaponId");
		turretEntityKey = tag.getString("turretEntity");
		ammo = tag.getInt("ammo");
		max = tag.getInt("max");
	}
	
	public CompoundTag write() {
		CompoundTag tag = super.write();
		tag.putString("weaponId", weaponId);
		tag.putString("turretEntity", turretEntityKey);
		tag.putInt("ammo", ammo);
		tag.putInt("max", max);
		return tag;
	}

	public TurretData(FriendlyByteBuf buffer) {
		super(buffer);
		weaponId = buffer.readUtf();
		turretEntityKey = buffer.readUtf();
		ammo = buffer.readInt();
		max = buffer.readInt();
	}
	
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
		buffer.writeUtf(weaponId);
		buffer.writeUtf(turretEntityKey);
		buffer.writeInt(ammo);
		buffer.writeInt(max);
	}
	
	@Override
	public PartType getType() {
		return PartType.TURRENT;
	}
	
	@Override
	public void setup(EntityAircraft craft, String slotId, Vec3 pos) {
		setParent(craft);
		setRelPos(pos);
		EntityTurret t = getTurret(slotId);
		t.setWeaponId(weaponId);
		t.setAmmo(ammo);
	}
	
	@Nullable
	public EntityTurret getTurret(String slotId) {
		EntityAircraft craft = getParent();
		if (craft == null) return null;
		for (EntityPart part : craft.getPartEntities()) 
			if (part.getSlotId().equals(slotId)) 
				return (EntityTurret) part;
		EntityTurret t = getTurretType().create(craft.level);
		t.setSlotId(slotId);
		t.setRelativePos(getRelPos());
		t.setPos(craft.position());
		t.startRiding(craft);
		craft.level.addFreshEntity(t);
		return t;
	}
	
	@Override
	public boolean isSetup(String slotId, EntityAircraft craft) {
		for (EntityPart part : craft.getPartEntities()) 
			if (part.getSlotId().equals(slotId)) 
				return true;
		return false;
	}
	
	@Override
	public void remove(String slotId) {
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
	
}
