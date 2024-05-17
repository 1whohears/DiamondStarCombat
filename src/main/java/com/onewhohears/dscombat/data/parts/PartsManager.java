package com.onewhohears.dscombat.data.parts;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ToClientVehicleFuel;
import com.onewhohears.dscombat.data.parts.instance.FlareDispenserInstance;
import com.onewhohears.dscombat.data.parts.instance.FuelTankInstance;
import com.onewhohears.dscombat.data.parts.instance.PartInstance;
import com.onewhohears.dscombat.data.parts.instance.SeatInstance;
import com.onewhohears.dscombat.data.parts.instance.StorageInstance;
import com.onewhohears.dscombat.data.parts.stats.EngineStats;
import com.onewhohears.dscombat.data.parts.stats.EngineStats.EngineType;
import com.onewhohears.dscombat.data.parts.stats.FuelTankStats;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;

/**
 * manages the parts/inventory system for {@link EntityVehicle}.
 * reads the entity's nbt to load the saved parts.
 * communicates with {@link com.onewhohears.dscombat.data.radar.WeaponSystem} 
 * and {@link com.onewhohears.dscombat.data.radar.RadarSystem} to update a vehicle's 
 * available weapons/radars based on parts. 
 * used to add/update/remove parts.
 * used to access various information about the parts in a vehicle.
 * this part manager has a list of {@link PartSlot}.
 * each {@link PartSlot} is either empty or has {@link PartData}.
 * will synch part data with client.
 * @author 1whohears
 */
public class PartsManager {
	
	private static final Logger LOGGER = LogUtils.getLogger();
	public static final int SLOT_VERSION = 1;
	
	private final EntityVehicle parent;
	private List<PartSlot> slots = new ArrayList<>();
	private Container inventory = new SimpleContainer(0);
	private boolean readData = false;
	private int storageIndex = -1;
	
	public PartsManager(EntityVehicle parent) {
		this.parent = parent;
	}
	
	/**
	 * deletes all cached slot data.
	 * reads new slot data from the entity's nbt and preset data.
	 * preset data from a data pack an data the entity spawned with are merged 
	 * so that old entities can be updated (example: if slot positions are changed for a new model)
	 * while preserving changes a player made to a vehicle's inventory.
	 * @param entityNbt
	 */
	public void read(CompoundTag entityNbt, CompoundTag presetNbt) {
		slots.clear();
		ListTag entityNbtList = entityNbt.getList("slots", 10);
		ListTag presetNbtList = presetNbt.getList("slots", 10);
		for (int i = 0; i < entityNbtList.size(); ++i) {
			CompoundTag entitySlot = entityNbtList.getCompound(i);
			CompoundTag presetSlot = findPresetSlot(entitySlot, presetNbtList);
			PartSlot slot = new PartSlot(entitySlot, presetSlot);
			slots.add(slot);
		}
		readData = true;
	}
	
	@Nullable
	private CompoundTag findPresetSlot(CompoundTag entitySlot, ListTag presetNbtList) {
		String slotId = entitySlot.getString("name");
		for (int i = 0; i < presetNbtList.size(); ++i) {
			CompoundTag presetSlot = presetNbtList.getCompound(i);
			String presetSlotId = presetSlot.getString("name");
			if (PartSlot.getSlotId(slotId).equals(PartSlot.getSlotId(presetSlotId))) {
				return presetSlot;
			}
		}
		return null;
	}
	
	private void createNewInventory() {
		//System.out.println("CREATING NEW INVENTORY "+this.toString());
		inventory = new SimpleContainer(slots.size()) {
			@Override
			public void setChanged() {
				super.setChanged();
			}
			@Override
			public void setItem(int i, ItemStack stack) {
				//System.out.println("SET ITEM "+i+" "+stack);
				if (readData && !parent.level.isClientSide) inventorySetItem(i, stack);
				super.setItem(i, stack);
			}
			@Override
			public ItemStack removeItem(int i, int count) {
				//System.out.println("REMOVE ITEM "+i);
				if (readData && !parent.level.isClientSide) inventoryRemoveItem(i, count);
				return super.removeItem(i, count);
			}			
		};
		for (int i = 0; i < slots.size(); ++i) if (slots.get(i).filled()) {
			inventory.setItem(i, slots.get(i).getPartData().getNewItemStack());
			//System.out.println("new item in slot "+i+" "+inventory.getItem(i)+" "+inventory.getItem(i).getTag());
		}
	}
	
	private void inventorySetItem(int i, ItemStack stack) {
		if (i < 0 || i >= slots.size()) {
			LOGGER.warn("WARNING! INDEX "+i+" IS OUT OF BOUNDS IN PARTS MANAGER "+this);
			return;
		}
		PartSlot slot = slots.get(i);
		Entity pilot = null;
		if (slot.isPilotSlot()) pilot = parent.getControllingPassenger();
		if (stack.isEmpty()) {
			slot.removePartData(parent);
			if (pilot != null && pilot.getVehicle() == null) {
				SeatInstance<?> seatdata = getDefaultSeat();
				slot.addPartData(seatdata, parent);
				parent.rideAvailableSeat(pilot);
			}
		} else {
			PartInstance<?> data = UtilParse.parsePartFromItem(stack);
			if (data == null) {
				LOGGER.warn("ERROR! COULD NOT GET PART DATA FROM "+stack+" "+stack.getTag());
				return;
			}
			if (slot.filled()) slot.removePartData(parent);
			slot.addPartData(data, parent);
			if (pilot != null && pilot.getVehicle() == null) {
				parent.rideAvailableSeat(pilot);
			}
		}	
	}
	
	private void inventoryRemoveItem(int i, int count) {
		if (i < 0 || i >= slots.size()) {
			LOGGER.warn("WARNING! INDEX "+i+" IS OUT OF BOUNDS IN PARTS MANAGER "+this);
			return;
		}
		Entity pilot = null;
		if (slots.get(i).isPilotSlot()) pilot = parent.getControllingPassenger();
		slots.get(i).removePartData(parent);
		if (pilot != null && pilot.getVehicle() == null) {
			SeatInstance<?> seatdata = getDefaultSeat();
			slots.get(i).addPartData(seatdata, parent);
			parent.rideAvailableSeat(pilot);
		}
	}
	
	public SeatInstance<?> getDefaultSeat() {
		return (SeatInstance<?>) ModItems.SEAT.get().getPartData();
	}
	
	public void write(CompoundTag compound) {
		ListTag list = new ListTag();
		for (PartSlot s : slots) list.add(s.write());
		compound.put("slots", list);
	}
	
	public static List<PartSlot> readSlotsFromBuffer(FriendlyByteBuf buffer) {
		List<PartSlot> ps = new ArrayList<PartSlot>();
		int num = buffer.readInt();
		for (int i = 0; i < num; ++i) ps.add(new PartSlot(buffer));
		return ps;
	}
	
	public static void writeSlotsToBuffer(FriendlyByteBuf buffer, List<PartSlot> slots) {
		buffer.writeInt(slots.size());
		for (PartSlot p : slots) p.write(buffer);
	}
	
	public void setPartSlots(List<PartSlot> slots) {
		this.slots = slots;
		this.readData = true;
	}
	
	public void setupParts() {
		//System.out.println("setupParts "+this);
		for (PartSlot p : slots) p.serverSetup(parent);
	}
	
	public void clientPartsSetup() {
		//System.out.println("clientPartsSetup "+this);
		for (PartSlot p : slots) p.clientSetup(parent);
	}
	
	public void tickParts() {
		for (PartSlot p : slots) p.tick();
	}
	
	public void clientTickParts() {
		for (PartSlot p : slots) p.clientTick();
	}
	
	@Nullable
	public PartSlot getSlot(String slotId) {
		for (PartSlot p : slots) if (p.getSlotId().equals(slotId)) return p;
		return null;
	}
	
	public boolean killPartInSlot(String slotId) {
		PartSlot slot = getSlot(slotId);
		if (slot == null) return false;
		Entity pilot = null;
		if (slot.isPilotSlot()) pilot = parent.getControllingPassenger();
		slot.removePartData(parent);
		if (pilot != null && pilot.getVehicle() == null) {
			SeatInstance<?> seatdata = getDefaultSeat();
			slot.addPartData(seatdata, parent);
			parent.rideAvailableSeat(pilot);
		}
		return true;
	}
	
	@Override
	public String toString() {
		String s = "Parts:";
		for (int i = 0; i < slots.size(); ++i) s += slots.get(i).toString();
		return s + " client?"+parent.level.isClientSide;
	}
	
	public boolean isReadData() {
		return readData;
	}
	
	public float getPartsWeight() {
		float total = 0;
		for (PartSlot p : slots) if (p.filled()) {
			float w = p.getPartData().getWeight();
			if (Float.isNaN(w)) {
				LOGGER.warn("ERROR: PART WEIGHT IS NAN "+p.toString());
				continue;
			}
			total += w;
		}
		return total;
	}
	
	public float getTotalPushThrust() {
		float total = 0;
		for (PartSlot p : slots) if (p.filled() && p.getPartData().getStats().isEngine()) {
			EngineStats engine = ((EngineStats)p.getPartData().getStats());
			if (engine.getEngineType() == EngineType.PUSH) total += engine.getThrust();
		}
		return total;
	}
	
	public float getTotalSpinThrust() {
		float total = 0;
		for (PartSlot p : slots) if (p.filled() && p.getPartData().getStats().isEngine()) {
			EngineStats engine = ((EngineStats)p.getPartData().getStats());
			if (engine.getEngineType() == EngineType.SPIN) total += engine.getThrust();
		}
		return total;
	}
	
	public float getTotalEngineHeat() {
		float total = 0;
		for (PartSlot p : slots) if (p.filled() && p.getPartData().getStats().isEngine()) 
			total += ((EngineStats)p.getPartData().getStats()).getHeat();
		return total;
	}
	
	public float getTotalEngineFuelConsume() {
		float total = 0;
		for (PartSlot p : slots) if (p.filled() && p.getPartData().getStats().isEngine()) 
			total += ((EngineStats)p.getPartData().getStats()).getFuelPerTick();
		return total;
	}
	
	public float getCurrentFuel() {
		float total = 0;
		for (PartSlot p : slots) if (p.filled() && p.getPartData().getStats().isFuelTank()) 
			total += ((FuelTankInstance<?>)p.getPartData()).getFuel();
		return total;
	}
	
	public float getMaxFuel() {
		float total = 0;
		for (PartSlot p : slots) if (p.filled() && p.getPartData().getStats().isFuelTank()) 
			total += ((FuelTankStats)p.getPartData().getStats()).getMaxFuel();
		return total;
	}
	
	public float addFuel(float fuel) {
		for (PartSlot p : slots) if (p.filled() && p.getPartData().getStats().isFuelTank()) {
			FuelTankInstance<?> data = (FuelTankInstance<?>) p.getPartData();
			fuel = data.addFuel(fuel);
			if (fuel == 0) break;
		}
		return fuel;
	}
	
	public void tickFuel(boolean updateClient) {
		float amount = -getTotalEngineFuelConsume() * Math.abs(parent.getCurrentThrottle());
		if (parent.isFuelLeak()) amount -= 0.06f;
		addFuel(amount);
		if (updateClient && parent.tickCount % 100 == 0) {
			PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> parent), 
					new ToClientVehicleFuel(parent));
		}
	}
	
	public List<PartSlot> getFuelTanks() {
		List<PartSlot> tanks = new ArrayList<PartSlot>();
		for (PartSlot p : slots) if (p.filled() && p.getPartData().getStats().isFuelTank()) tanks.add(p);
		return tanks;
	}
	
	public float[] getFuelsForClient() {
		List<PartSlot> tanks = getFuelTanks();
		float[] fuels = new float[tanks.size()];
		for (int i = 0; i < tanks.size(); ++i) fuels[i] = ((FuelTankInstance<?>)tanks.get(i).getPartData()).getFuel();
		return fuels;
	}
	
	public void readFuelsForClient(float[] fuels) {
		List<PartSlot> tanks = getFuelTanks();
		if (fuels.length != tanks.size()) return;
		for (int i = 0; i < tanks.size(); ++i) ((FuelTankInstance<?>)tanks.get(i).getPartData()).setFuel(fuels[i]);
	}
	
	public List<PartSlot> getSlots() {
		return slots;
	}
	
	public void dropAllItems() {
		if (parent.level.isClientSide) return;
		Containers.dropContents(parent.level, parent.blockPosition().above(1), getInventory());
		removeAllParts();
	}
	
	public void removeAllParts() {
		if (parent.level.isClientSide) return;
		for (int i = 0; i < slots.size(); ++i) 
			if (!slots.get(i).isSeat()) 
				slots.get(i).removePartData(parent);
	}
	
	public Container getInventory() {
		this.readData = false;
		createNewInventory();
		this.readData = true;
		return inventory;
	}
	
	public List<PartSlot> getFlares() {
		List<PartSlot> flares = new ArrayList<PartSlot>();
		for (PartSlot p : slots) 
			if (p.filled() && p.getPartData().getStats().isFlareDispenser()) 
				flares.add(p);
		return flares;
	}
	
	public boolean useFlares(boolean consume) {
		if (parent.getFlareNum() < 1) return false;
		for (PartSlot p : getFlares())
			if (((FlareDispenserInstance<?>)p.getPartData()).flare(consume))
				return true;
		return false;
	}
	
	public int getNumFlares() {
		int num = 0;
		for (PartSlot p : slots) if (p.filled()) 
			num += p.getPartData().getFlares();
		return num;
	}
	
	public int addFlares(int num) {
		for (PartSlot p : getFlares()) {
			FlareDispenserInstance<?> fd = (FlareDispenserInstance<?>)p.getPartData();
			num = fd.addFlares(num);
			if (num == 0) return 0;
		}
		return num;
	}
	
	public float getTotalExtraArmor() {
		float armor = 0;
		for (PartSlot p : slots) if (p.filled()) 
			armor += p.getPartData().getStats().getAdditionalArmor();
		return armor;
	}
	
	public boolean hasGimbal() {
		for (PartSlot p : slots) 
			if (p.filled() && p.getPartData().getStats().isGimbal()) 
				return true;
		return false;
	}
	
	public boolean hasStorageBoxes() {
		return getStorageBoxes().size() > 0;
	}
	
	public List<StorageInstance<?>> getStorageBoxes() {
		List<StorageInstance<?>> list = new ArrayList<>();
		for (PartSlot p : slots) 
			if (p.filled() && p.getPartData().getStats().isStorageBox()) 
				list.add((StorageInstance<?>) p.getPartData());
		return list;
	}
	
	@Nullable
	public StorageInstance<?> cycleStorageData() {
		List<StorageInstance<?>> boxes = getStorageBoxes();
		if (boxes.size() == 0) {
			storageIndex = -1;
			return null;
		} else if (storageIndex == -1 || storageIndex >= boxes.size()-1) {
			storageIndex = 0;
			return boxes.get(storageIndex);
		} 
		return boxes.get(++storageIndex);
	}
	
}
