package com.onewhohears.dscombat.data;

import java.util.ArrayList;
import java.util.List;

import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.init.DataSerializers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;

public class PartsManager {
	
	private List<PartData> parts = new ArrayList<PartData>();
	private WeaponSystem weapons = new WeaponSystem();
	
	public PartsManager() {
	}
	
	public PartsManager(CompoundTag compound) {
		weapons = new WeaponSystem(compound);
		ListTag list = compound.getList("parts", 10);
		for (int i = 0; i < list.size(); ++i) {
			CompoundTag tag = list.getCompound(i);
			int index = tag.getInt("type");
			PartData.PartType type = PartData.PartType.values()[index];
			switch (type) {
			case SEAT:
				parts.add(new SeatData(tag));
				break;
			case TURRENT:
				break;				
			}
		}
	}
	
	public void write(CompoundTag compound) {
		weapons.write(compound);
		ListTag list = new ListTag();
		for (PartData p : parts) list.add(p.write());
		compound.put("parts", list);
	}
	
	public PartsManager(FriendlyByteBuf buffer) {
		weapons = new WeaponSystem(buffer);
		int num = buffer.readInt();
		for (int i = 0; i < num; ++i) parts.add(DataSerializers.PART_DATA.read(buffer));
	}
	
	public void write(FriendlyByteBuf buffer) {
		weapons.write(buffer);
		buffer.writeInt(parts.size());
		for (PartData p : parts) p.write(buffer);
	}
	
	public WeaponSystem getWeapons() {
		return weapons;
	}
	
	public void setupParts(EntityAbstractAircraft craft) {
		for (PartData p : parts) p.setup(craft);
	}
	
	public void addPart(PartData part) {
		for (PartData p : parts) if (p.getId().equals(part.getId())) return;
		parts.add(part);
	}
	
	public void removePart(String id) {
		for (PartData p : parts) if (p.getId().equals(id)) {
			parts.remove(p);
			return;
		}
	}
	
	public PartData get(String id) {
		for (PartData p : parts) if (p.getId().equals(id)) return p;
		return null;
	}
	
}
