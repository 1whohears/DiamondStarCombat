package com.onewhohears.dscombat.util;

import java.util.List;

import com.onewhohears.dscombat.data.aircraft.VehicleInputManager;
import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.radar.RadarData.RadarPing;
import com.onewhohears.dscombat.data.radar.RadarSystem.RWRWarning;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class UtilPacket {
	
	public static void aircraftInputsPacket(int id, VehicleInputManager inputs) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityVehicle plane) {
			if (!plane.isControlledByLocalInstance()) {
				plane.inputs.updateInputsFromPacket(inputs, plane);
			}
		}
	}
	
	public static void pingsPacket(int id, List<RadarPing> pings) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityVehicle plane) {
			plane.radarSystem.readClientPingsFromServer(pings);
		}
	}
	
	public static void weaponAmmoPacket(int id, String weaponId, String slotId, int ammo) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityVehicle plane) {
			WeaponData w = plane.weaponSystem.get(weaponId, slotId);
			if (w != null) w.setCurrentAmmo(ammo);
		}
	}
	
	public static void weaponSelectPacket(int id, int index) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityVehicle plane) {
			plane.weaponSystem.setSelected(index);
		}
	}
	
	public static void addPartPacket(int id, String slotId, PartData data) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityVehicle plane) {
			PartSlot slot = plane.partsManager.getSlot(slotId);
			if (slot != null) slot.addPartData(data, plane);
		}
	}
	
	public static void removePartPacket(int id, String slotId) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityVehicle plane) {
			PartSlot slot = plane.partsManager.getSlot(slotId);
			if (slot != null) slot.removePartData(plane);
		}
	}
	
	public static void setAircraftFuel(int id, float[] fuels) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityVehicle plane) {
			plane.partsManager.readFuelsForClient(fuels);
		}
	}
	
	public static void rwrPacket(int id, RWRWarning warning) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityVehicle plane) {
			plane.radarSystem.getClientRWRWarnings().add(warning);
		}
	}
	
	public static void addMomentPacket(int id, Vec3 force, Vec3 moment) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityVehicle plane) {
			//System.out.println("adding from server "+force+" "+moment);
			plane.addForceBetweenTicks = plane.addForceBetweenTicks.add(force);
			plane.addMomentBetweenTicks = plane.addMomentBetweenTicks.add(moment);
		}
	}
	
	public static void vehicleTexturePacket(int ignore_player_id, int vehicle_id, FriendlyByteBuf buffer) {
		Minecraft m = Minecraft.getInstance();
		if (m.player.getId() == ignore_player_id) return;
		Level world = m.level;
		if (world.getEntity(vehicle_id) instanceof EntityVehicle plane) {
			plane.textureManager.read(buffer);
		}
	}
	
}
