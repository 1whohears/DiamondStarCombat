package com.onewhohears.dscombat.data.parts;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.entity.parts.EntityVehiclePart;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class WeaponPartData extends PartData {
	
	private final String[] compatible;
	protected String weaponId;
	private int ammo;
	private int max;
	
	public WeaponPartData(float weight, String preset, String[] compatible, ResourceLocation itemid, SlotType[] compatibleSlots) {
		super(weight, itemid, compatibleSlots);
		this.compatible = compatible;
		this.weaponId = preset;
		if (!WeaponPresets.get().has(weaponId)) weaponId = "";
		WeaponData data = WeaponPresets.get().getPreset(weaponId);
		if (data != null) {
			this.ammo = data.getMaxAmmo();
			this.max = data.getMaxAmmo();
		}
	}
	
	public WeaponPartData(CompoundTag tag) {
		super(tag);
		ListTag list = tag.getList("compatible", 8);
		compatible = new String[list.size()];
		for (int i = 0; i < list.size(); ++i) compatible[i] = list.getString(i);
		weaponId = tag.getString("weaponId");
		if (!WeaponPresets.get().has(weaponId)) weaponId = "";
		ammo = tag.getInt("ammo");
		max = tag.getInt("max");
	}
	
	public CompoundTag write() {
		CompoundTag tag = super.write();
		ListTag list = new ListTag();
		for (int i = 0; i < compatible.length; ++i) list.add(StringTag.valueOf(compatible[i]));
		tag.put("compatible", list);
		tag.putString("weaponId", weaponId);
		tag.putInt("ammo", ammo);
		tag.putInt("max", max);
		return tag;
	}

	public WeaponPartData(FriendlyByteBuf buffer) {
		super(buffer);
		int num = buffer.readInt();
		compatible = new String[num];
		for (int i = 0; i < num; ++i) compatible[i] = buffer.readUtf();
		weaponId = buffer.readUtf();
		ammo = buffer.readInt();
		max = buffer.readInt();
	}
	
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
		buffer.writeInt(compatible.length);
		for (int i = 0; i < compatible.length; ++i) buffer.writeUtf(compatible[i]);
		buffer.writeUtf(weaponId);
		buffer.writeInt(ammo);
		buffer.writeInt(max);
	}

	@Override
	public PartType getType() {
		return PartType.INTERNAL_WEAPON;
	}
	
	@Override
	public void setup(EntityAircraft craft, String slotId, Vec3 pos, float zRot) {
		super.setup(craft, slotId, pos, zRot);
		WeaponData data = craft.weaponSystem.get(weaponId, slotId);
		if (data == null) {
			data = WeaponPresets.get().getPreset(weaponId);
			if (data == null) return;
			data.setSlot(slotId);
			craft.weaponSystem.addWeapon(data);
		}
		data.setCurrentAmmo(ammo);
		//data.setMaxAmmo(max);
		data.setLaunchPos(pos);
		if (!craft.level.isClientSide) data.updateClientAmmo(craft);
	}
	
	@Override
	public void remove() {
		super.remove();
		getParent().weaponSystem.removeWeapon(weaponId, getSlotId());
	}
	
	@Override
	public void tick() {
		super.tick();
		WeaponData data = getParent().weaponSystem.get(weaponId, getSlotId());
		if (data != null) {
			ammo = data.getCurrentAmmo();
			max = data.getMaxAmmo();
		}
	}
	
	@Override
	public void clientTick() {
		super.clientTick();
		this.tick();
	}
	
	@Override
	public float getWeight() {
		if (max == 0) return 0;
		float w = super.getWeight();
		return w * (float)ammo / (float)max;
	}
	
	public boolean isWeaponCompatible(String preset) {
		if (preset == null) return false;
		for (int i = 0; i < compatible.length; ++i) {
			if (compatible[i].equals(preset)) return true;
		}
		return false;
	}
	
	@Nullable
	@Override
	public EntityVehiclePart getPartEntity() {
		return null;
	}

	@Override
	public boolean hasExternalPartEntity() {
		return false;
	}

}
