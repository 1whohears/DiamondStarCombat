package com.onewhohears.dscombat.data.parts;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.common.container.AircraftMenuContainer;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ToClientAddPart;
import com.onewhohears.dscombat.common.network.toclient.ToClientAircraftFuel;
import com.onewhohears.dscombat.common.network.toclient.ToClientRemovePart;
import com.onewhohears.dscombat.data.parts.PartData.PartType;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
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
	private boolean readData = false;
	private EntityAircraft parent;
	
	public PartsManager() {
		
	}
	
	/**
	 * deletes all slot data currently in the list and reads from this compound
	 * @param compound
	 */
	public void read(CompoundTag compound) {
		slots.clear();
		ListTag list = compound.getList("slots", 10);
		for (int i = 0; i < list.size(); ++i) {
			CompoundTag tag = list.getCompound(i);
			slots.add(new PartSlot(tag));
		}
		readData = true;
	}
	
	public void write(CompoundTag compound) {
		ListTag list = new ListTag();
		for (PartSlot s : slots) list.add(s.write());
		compound.put("slots", list);
	}
	
	public PartsManager(FriendlyByteBuf buffer) {
		int num = buffer.readInt();
		for (int i = 0; i < num; ++i) slots.add(new PartSlot(buffer));
		readData = true;
	}
	
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(slots.size());
		for (PartSlot p : slots) p.write(buffer);
	}
	
	/**
	 * @param copy copies this part manager's slot data
	 */
	public void copy(PartsManager copy) {
		this.slots = copy.slots;
		readData = true;
	}
	
	public void setupParts(EntityAircraft craft) {
		this.parent = craft;
		//System.out.println("setupParts "+this);
		for (PartSlot p : slots) p.setup(craft);
	}
	
	public void clientPartsSetup(EntityAircraft craft) {
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
	
	public boolean addSlot(String slotName, SlotType slotType, Vec3 slotPos, int uix, int uiy, float zRot) {
		if (getSlot(slotName) != null) return false;
		slots.add(new PartSlot(slotName, slotType, slotPos, uix, uiy, zRot));
		return true;
	}
	
	public boolean addSlot(String slotName, SlotType slotType, double x, double y, double z, int uix, int uiy, float zRot) {
		if (getSlot(slotName) != null) return false;
		slots.add(new PartSlot(slotName, slotType, new Vec3(x, y, z), uix, uiy, zRot));
		return true;
	}
	
	public boolean addPart(PartData part, String slotName, boolean updateClient) {
		//System.out.println("ADDING PART "+part+" IN SLOT "+slotName);
		for (PartSlot p : slots) if (p.getName().equals(slotName) && !p.filled()) {
			boolean ok = p.addPartData(part, parent);
			if (updateClient && ok) {
				PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> parent), 
						new ToClientAddPart(parent.getId(), slotName, part));
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
						new ToClientRemovePart(parent.getId(), slotName));
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
	
	public float addFuel(float fuel) {
		for (PartSlot p : slots) if (p.filled() && p.getPartData().getType() == PartType.FUEL_TANK) {
			FuelTankData data = (FuelTankData) p.getPartData();
			fuel = data.addFuel(fuel);
			if (fuel == 0) break;
		}
		return fuel;
	}
	
	public void tickFuel(boolean updateClient) {
		addFuel(-getTotalEngineFuelConsume() * parent.getCurrentThrottle());
		if (updateClient && parent.tickCount % 100 == 0) {
			PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> parent), 
					new ToClientAircraftFuel(parent));
		}
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
			c.setItem(i, slots.get(i).getPartData().getNewItemStack());
			//System.out.println("putting item in slot "+i+" "+c.getItem(i)+" "+c.getItem(i).getTag());
		}
		return c;
	}
	
	public void readContainer(Container c) {
		//System.out.println("READING CHANGED CONTAINER client side "+parent.level.isClientSide+" items "+c);
		// TODO if you replace a seat with a turret without taking the seat out first the seat doesn't get removed and the turret data doesn't get read
		if (c.getContainerSize() != slots.size()) {
			System.out.println("WARNING! THIS CONTAINER HAS THE WRONG NUMBER OF SLOTS!");
			return;
		}
		for (int i = 0; i < c.getContainerSize(); ++i) {
			ItemStack stack = c.getItem(i);
			if (stack.isEmpty()) {
				removePart(slots.get(i).getName(), false);
				continue;
			}
		}
		for (int i = 0; i < c.getContainerSize(); ++i) {
			//System.out.println("### CHECKING CONTAINER "+i);
			ItemStack stack = c.getItem(i);
			if (stack.isEmpty()) continue;
			PartSlot slot = slots.get(i);
			//System.out.println("stack = "+stack+" "+stack.getTag());
			//System.out.println("slot = "+slot);
			PartData data = UtilParse.parsePartFromCompound(stack.getTag());
			if (data == null) {
				System.out.println("ERROR! COULD NOT GET PART DATA FROM "+stack+" "+stack.getTag());
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
	
	public List<PartSlot> getFlares() {
		List<PartSlot> flares = new ArrayList<PartSlot>();
		for (PartSlot p : slots) if (p.filled() && p.getPartData().getType() == PartType.FLARE_DISPENSER) flares.add(p);
		return flares;
	}
	
	public boolean useFlares(boolean isCreative) {
		List<PartSlot> flares = getFlares();
		boolean r = false;
		for (PartSlot p : flares)
			if (((FlareDispenserData)p.getPartData()).flare(isCreative))
				r = true;
		return r;
	}
	
}
