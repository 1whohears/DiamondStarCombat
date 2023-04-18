package com.onewhohears.dscombat.util;

import java.util.List;

import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.radar.RadarData;
import com.onewhohears.dscombat.data.radar.RadarData.RadarPing;
import com.onewhohears.dscombat.data.radar.RadarSystem.RWRWarning;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.entity.weapon.EntityMissile;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class UtilPacket {
	
	public static void entityMissileMovePacket(int id, Vec3 pos, Vec3 move, float pitch, float yaw, Vec3 targetPos) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityMissile e) {
			e.setPos(pos);
			e.setDeltaMovement(move);
			e.setXRot(pitch);
			e.setYRot(yaw);
			e.targetPos = targetPos;
		}
	}
	
	public static void pingsPacket(int id, List<RadarPing> pings) {
		//System.out.println("ping packet received");
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityAircraft plane) {
			plane.radarSystem.readClientPingsFromServer(pings);
		}
	}
	
	public static void planeDataPacket(int id, List<PartSlot> slots, List<WeaponData> weapons, int weaponIndex, List<RadarData> radars) {
		//System.out.println("plane data packet received");
		//System.out.println(pm.toString());
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityAircraft plane) {
			plane.weaponSystem.clientSetSelected(weaponIndex);
			plane.weaponSystem.setWeapons(weapons);
			plane.radarSystem.setRadars(radars);
			plane.partsManager.setPartSlots(slots);
			// ORDER MATTERS
			plane.weaponSystem.clientSetup();
			plane.radarSystem.clientSetup();
			plane.partsManager.clientPartsSetup();
			//System.out.println("plane data updated");
		}
	}
	
	public static void weaponAmmoPacket(int id, String weaponId, String slotId, int ammo) {
		//System.out.println("ammo packet received");
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityAircraft plane) {
			WeaponData w = plane.weaponSystem.get(weaponId, slotId);
			if (w != null) w.setCurrentAmmo(ammo);
		}
	}
	
	public static void weaponSelectPacket(int id, int index) {
		//System.out.println("ammo select packet received");
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityAircraft plane) {
			plane.weaponSystem.clientSetSelected(index);
		}
	}
	
	public static void addWeaponPacket(int id, WeaponData data) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityAircraft plane) {
			plane.weaponSystem.addWeapon(data, false);
		}
	}
	
	public static void removeWeaponPacket(int id, String wid, String slotId) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityAircraft plane) {
			plane.weaponSystem.removeWeapon(wid, slotId, false);
		}
	}
	
	public static void addRadarPacket(int id, RadarData data) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityAircraft plane) {
			plane.radarSystem.addRadar(data, false);
		}
	}
	
	public static void removeRadarPacket(int id, String rid, String slotId) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityAircraft plane) {
			plane.radarSystem.removeRadar(rid, slotId, false);
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
	
	public static void synchTorquePacket(int id, float tx, float ty, float tz) {
		/*Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityAircraft plane) {
			plane.torqueX = tx;
			plane.torqueY = ty;
			plane.torqueZ = tz;
		}*/
	}
	
}
