package com.onewhohears.dscombat.util;

import java.util.List;

import com.onewhohears.dscombat.data.aircraft.AircraftInputs;
import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.radar.RadarData.RadarMode;
import com.onewhohears.dscombat.data.radar.RadarData.RadarPing;
import com.onewhohears.dscombat.data.radar.RadarSystem.RWRWarning;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class UtilPacket {
	
	public static void aircraftInputsPacket(int id, AircraftInputs inputs, int weaponIndex, RadarMode radarMode,
			boolean isLandingGear, boolean isFreeLook) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityAircraft plane) {
			if (!plane.isControlledByLocalInstance()) {
				plane.inputs.copy(inputs);
				plane.weaponSystem.setSelected(weaponIndex);
				plane.setRadarMode(radarMode);
				plane.setLandingGear(isLandingGear);
				plane.setFreeLook(isFreeLook);
			}
		}
	}
	
	public static void pingsPacket(int id, List<RadarPing> pings) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityAircraft plane) {
			plane.radarSystem.readClientPingsFromServer(pings);
		}
	}
	
	public static void weaponAmmoPacket(int id, String weaponId, String slotId, int ammo) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityAircraft plane) {
			WeaponData w = plane.weaponSystem.get(weaponId, slotId);
			if (w != null) w.setCurrentAmmo(ammo);
		}
	}
	
	public static void weaponSelectPacket(int id, int index) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityAircraft plane) {
			plane.weaponSystem.setSelected(index);
		}
	}
	
	public static void addPartPacket(int id, String slotId, PartData data) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityAircraft plane) {
			PartSlot slot = plane.partsManager.getSlot(slotId);
			if (slot != null) slot.addPartData(data, plane);
		}
	}
	
	public static void removePartPacket(int id, String slotId) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityAircraft plane) {
			PartSlot slot = plane.partsManager.getSlot(slotId);
			if (slot != null) slot.removePartData(plane);
		}
	}
	
	public static void setAircraftFuel(int id, float[] fuels) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityAircraft plane) {
			plane.partsManager.readFuelsForClient(fuels);
		}
	}
	
	public static void rwrPacket(int id, RWRWarning warning) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityAircraft plane) {
			plane.radarSystem.getClientRWRWarnings().add(warning);
		}
	}
	
	public static void addMomentPacket(int id, Vec3 moment) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityAircraft plane) {
			plane.addMomentFromServer = plane.addMomentFromServer.add(moment);
		}
	}
	
}
