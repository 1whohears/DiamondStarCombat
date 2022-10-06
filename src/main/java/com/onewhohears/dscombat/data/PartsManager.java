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
	
	public PartsManager() {
	}
	
	public PartsManager(CompoundTag compound) {
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
			case RADAR:
				parts.add(new RadarData(tag));
				break;
			}
		}
	}
	
	public void write(CompoundTag compound) {
		ListTag list = new ListTag();
		for (PartData p : parts) list.add(p.write());
		compound.put("parts", list);
	}
	
	public PartsManager(FriendlyByteBuf buffer) {
		int num = buffer.readInt();
		for (int i = 0; i < num; ++i) parts.add(DataSerializers.PART_DATA.read(buffer));
	}
	
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(parts.size());
		for (PartData p : parts) p.write(buffer);
	}
	
	public void setupParts(EntityAbstractAircraft craft) {
		System.out.println("setupParts "+this);
		for (PartData p : parts) p.setup(craft);
	}
	
	public void clientPartsSetup(EntityAbstractAircraft craft) {
		System.out.println("clientPartsSetup "+this);
		for (PartData p : parts) p.clientSetup(craft);
	}
	
	public void addPart(PartData part) {
		for (PartData p : parts) if (p.getId().equals(part.getId())) return;
		parts.add(part);
		// TODO add part packet to client
	}
	
	public void removePart(String id) {
		for (PartData p : parts) if (p.getId().equals(id)) {
			parts.remove(p);
			// TODO remove part packet to client
			return;
		}
	}
	
	public PartData get(String id) {
		for (PartData p : parts) if (p.getId().equals(id)) return p;
		return null;
	}
	
	@Override
	public String toString() {
		String s = "Parts:";
		for (int i = 0; i < parts.size(); ++i) s += parts.get(i).toString();
		return s;
	}
	
}
