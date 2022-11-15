package com.onewhohears.dscombat.data.parts;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.common.container.AircraftMenuContainer;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ClientBoundAddPartPacket;
import com.onewhohears.dscombat.common.network.toclient.ClientBoundFuelPacket;
import com.onewhohears.dscombat.common.network.toclient.ClientBoundRemovePartPacket;
import com.onewhohears.dscombat.data.parts.PartData.PartType;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
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
		//System.out.println("setupParts "+this);
		for (PartSlot p : slots) p.setup(craft);
	}
	
	public void clientPartsSetup(EntityAbstractAircraft craft) {
		this.parent = craft;
		//System.out.println("clientPartsSetup "+this);
		for (PartSlot p : slots) p.clientSetup(craft);
	}
	
	public void tickParts() {
		for (PartSlot p : slots) p.tick();
	}
	
	public void clientTickParts() {
		for (PartSlot p : slots) p.clientTick();
	}
	
	public boolean addSlot(String slotName, SlotType slotType, Vec3 slotPos, int uix, int uiy) {
		if (getSlot(slotName) != null) return false;
		slots.add(new PartSlot(slotName, slotType, slotPos, uix, uiy));
		return true;
	}
	
	public boolean addPart(PartData part, String slotName, boolean updateClient) {
		//System.out.println("ADDING PART "+part+" IN SLOT "+slotName);
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
	
	public float getTotalEngineThrust() {
		float total = 0;
		for (PartSlot p : slots) if (p.filled() && p.getPartData().getType() == PartType.ENGINE) 
			total += ((EngineData)p.getPartData()).getThrust();
		return total;
	}
	
	public float getTotalEngineHeat() {
		float total = 0;
		for (PartSlot p : slots) if (p.filled() && p.getPartData().getType() == PartType.ENGINE) 
			total += ((EngineData)p.getPartData()).getHeat();
		return total;
	}
	
	public float getTotalEngineFuelConsume() {
		float total = 0;
		for (PartSlot p : slots) if (p.filled() && p.getPartData().getType() == PartType.ENGINE) 
			total += ((EngineData)p.getPartData()).getFuelPerTick();
		return total;
	}
	
	public float getCurrentFuel() {
		float total = 0;
		for (PartSlot p : slots) if (p.filled() && p.getPartData().getType() == PartType.FUEL_TANK) 
			total += ((FuelTankData)p.getPartData()).getFuel();
		return total;
	}
	
	public float getMaxFuel() {
		float total = 0;
		for (PartSlot p : slots) if (p.filled() && p.getPartData().getType() == PartType.FUEL_TANK) 
			total += ((FuelTankData)p.getPartData()).getMaxFuel();
		return total;
	}
	
	public float addFuel(float fuel, boolean updateClient) {
		for (PartSlot p : slots) if (p.filled() && p.getPartData().getType() == PartType.FUEL_TANK) {
			FuelTankData data = (FuelTankData) p.getPartData();
			fuel = data.addFuel(fuel);
			if (fuel == 0) break;
		}
		if (updateClient) {
			PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> parent), 
					new ClientBoundFuelPacket(parent));
		}
		return fuel;
	}
	
	public void tickFuel(boolean updateClient) {
		addFuel(-getTotalEngineFuelConsume() * parent.getCurrentThrottle(), updateClient);
	}
	
	public List<PartSlot> getFuelTanks() {
		List<PartSlot> tanks = new ArrayList<PartSlot>();
		for (PartSlot p : slots) if (p.filled() && p.getPartData().getType() == PartType.FUEL_TANK) tanks.add(p);
		return tanks;
	}
	
	public float[] getFuelsForClient() {
		List<PartSlot> tanks = getFuelTanks();
		float[] fuels = new float[tanks.size()];
		for (int i = 0; i < tanks.size(); ++i) fuels[i] = ((FuelTankData)tanks.get(i).getPartData()).getFuel();
		return fuels;
	}
	
	public void readFuelsForClient(float[] fuels) {
		List<PartSlot> tanks = getFuelTanks();
		if (fuels.length != tanks.size()) return;
		for (int i = 0; i < tanks.size(); ++i) ((FuelTankData)tanks.get(i).getPartData()).setFuel(fuels[i]);
	}
	
	public Container getContainer(AircraftMenuContainer menu) {
		//System.out.println("GETTING CONTAINER client side = "+parent.level.isClientSide+" for slots "+this);
		Container c = new SimpleContainer(slots.size()){
			@Override
			public void setChanged() {
				super.setChanged();
				menu.slotsChanged(this);
			}
		};
		for (int i = 0; i < slots.size(); ++i) if (slots.get(i).filled()) {
			c.setItem(i, slots.get(i).getPartData().getItemStack());
			//System.out.println("putting item in slot "+i+" "+c.getItem(i)+" "+c.getItem(i).getTag());
		}
		return c;
	}
	
	public void readContainer(Container c) {
		//System.out.println("READING CHANGED CONTAINER client side "+parent.level.isClientSide+" items "+c);
		if (c.getContainerSize() != slots.size()) {
			//System.out.println("WARNING! THIS CONTAINER HAS THE WRONG NUMBER OF SLOTS!");
			return;
		}
		for (int i = 0; i < c.getContainerSize(); ++i) {
			//System.out.println("### CHECKING CONTAINER "+i);
			ItemStack stack = c.getItem(i);
			PartSlot slot = slots.get(i);
			//System.out.println("stack = "+stack+" "+stack.getTag());
			//System.out.println("slot = "+slot);
			if (stack.isEmpty()) {
				removePart(slot.getName(), false);
				//System.out.println("REMOVING CAUSE EMPTY");
				continue;
			}
			PartData data = UtilParse.parsePartFromCompound(stack.getTag());
			if (data == null) {
				//System.out.println("ERROR! COULD NOT GET PART DATA FROM "+stack+" "+stack.getTag());
				continue;
			}
			if (data.isSetup(slot.getName(), parent)) {
				//System.out.println("ALREADY SETUP");
				continue;
			}
			//System.out.println("REMOVING");
			removePart(slot.getName(), false);
			//System.out.println("ADDING");
			addPart(data, slot.getName(), false);
		}
	}
	
	public List<PartSlot> getSlots() {
		return slots;
	}
	
}
