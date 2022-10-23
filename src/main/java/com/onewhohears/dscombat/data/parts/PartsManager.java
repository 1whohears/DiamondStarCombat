package com.onewhohears.dscombat.data.parts;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ClientBoundAddPartPacket;
import com.onewhohears.dscombat.common.network.toclient.ClientBoundRemovePartPacket;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

public class PartsManager {
	
	private List<PartSlot> slots = new ArrayList<PartSlot>();
	private boolean readData = true;
	private EntityAbstractAircraft parent;
	
	public PartsManager() {
		readData = false;
	}
	
	public PartsManager(CompoundTag compound) {
		ListTag list = compound.getList("slots", 10);
		for (int i = 0; i < list.size(); ++i) {
			CompoundTag tag = list.getCompound(i);
			slots.add(new PartSlot(tag));
		}
	}
	
	public void write(CompoundTag compound) {
		ListTag list = new ListTag();
		for (PartSlot s : slots) list.add(s.write());
		compound.put("slots", list);
	}
	
	public PartsManager(FriendlyByteBuf buffer) {
		int num = buffer.readInt();
		for (int i = 0; i < num; ++i) slots.add(new PartSlot(buffer));
	}
	
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(slots.size());
		for (PartSlot p : slots) p.write(buffer);
	}
	
	public void setupParts(EntityAbstractAircraft craft) {
		this.parent = craft;
		System.out.println("setupParts "+this);
		for (PartSlot p : slots) p.setup(craft);
	}
	
	public void clientPartsSetup(EntityAbstractAircraft craft) {
		this.parent = craft;
		System.out.println("clientPartsSetup "+this);
		for (PartSlot p : slots) p.clientSetup(craft);
	}
	
	public boolean addSlot(String slotName, SlotType slotType, Vec3 slotPos, int uix, int uiy) {
		if (getSlot(slotName) != null) return false;
		slots.add(new PartSlot(slotName, slotType, slotPos, uix, uiy));
		return true;
	}
	
	public boolean addPart(PartData part, String slotName, boolean updateClient) {
		for (PartSlot p : slots) if (p.getName().equals(slotName) && !p.filled()) {
			boolean ok = p.addPartData(part, parent);
			if (updateClient && ok) {
				PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> parent), 
						new ClientBoundAddPartPacket(parent.getId(), slotName, part));
			}
			return true;
		}
		return false;
	}
	
	public void removePart(String slotName, boolean updateClient) {
		for (PartSlot p : slots) if (p.getName().equals(slotName)) {
			boolean ok = p.removePartData(parent);
			if (updateClient && ok) {
				PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> parent), 
						new ClientBoundRemovePartPacket(parent.getId(), slotName));
			}
			return;
		}
	}
	
	@Nullable
	public PartSlot getSlot(String slotName) {
		for (PartSlot p : slots) if (p.getName().equals(slotName)) return p;
		return null;
	}
	
	@Override
	public String toString() {
		String s = "Parts:";
		for (int i = 0; i < slots.size(); ++i) s += slots.get(i).toString();
		return s;
	}
	
	public boolean isReadData() {
		return readData;
	}
	
	public float getPartsWeight() {
		float total = 0;
		for (PartSlot p : slots) if (p.filled()) total += p.getPartData().getWeight();
		return total;
	}
	
	public Container getContainer() {
		System.out.println("GETTING CONTAINER client side = "+parent.level.isClientSide+" for slots "+this);
		Container c = new SimpleContainer(slots.size());
		for (int i = 0; i < slots.size(); ++i) if (slots.get(i).filled()) {
			c.setItem(i, slots.get(i).getPartData().getItemStack());
			System.out.println("putting item in slot "+i+" "+c.getItem(i).getOrCreateTag()); // TODO the item nbt is empty here
		}
		return c;
	}
	
	public List<PartSlot> getSlots() {
		return slots;
	}
	
}
