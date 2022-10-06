package com.onewhohears.dscombat.util;

import java.util.List;

import com.onewhohears.dscombat.data.PartsManager;
import com.onewhohears.dscombat.data.RadarData;
import com.onewhohears.dscombat.data.RadarData.RadarPing;
import com.onewhohears.dscombat.data.WeaponData;
import com.onewhohears.dscombat.data.WeaponSystem;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class UtilPacket {
	
	public static void entityMovePacket(int id, Vec3 pos, Vec3 move, float pitch, float yaw) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		Entity e = world.getEntity(id);
		if (e != null) {
			e.setPos(pos);
			e.setDeltaMovement(move);
			e.setXRot(pitch);
			e.setYRot(yaw);
		}
	}
	
	public static void pingsPacket(int id, List<RadarPing> pings) {
		//System.out.println("ping packet recieved");
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		EntityAbstractAircraft plane = (EntityAbstractAircraft) world.getEntity(id);
		if (plane != null) {
			RadarData radar = plane.getRadar();
			if (radar != null) radar.readClientPingsFromServer(pings);
		}
	}
	
	public static void planeDataPacket(int id, PartsManager pm, WeaponSystem ws) {
		System.out.println("plane data packet recieved");
		System.out.println(pm.toString());
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		EntityAbstractAircraft plane = (EntityAbstractAircraft) world.getEntity(id);
		if (plane != null) {
			plane.partsManager = pm;
			plane.weaponSystem = ws;
			plane.partsManager.clientPartsSetup(plane);
			plane.weaponSystem.clientSetup(plane);
			System.out.println("plane data updated");
		}
	}
	
	public static void weaponAmmoPacket(int id, String weaponId, int ammo) {
		System.out.println("ammo packet recieved");
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		EntityAbstractAircraft plane = (EntityAbstractAircraft) world.getEntity(id);
		if (plane != null) {
			WeaponData w = plane.weaponSystem.get(weaponId);
			if (w != null) w.setCurrentAmmo(ammo);
		}
	}
	
	public static void weaponSelectPacket(int id, int index) {
		System.out.println("ammo select packet recieved");
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		EntityAbstractAircraft plane = (EntityAbstractAircraft) world.getEntity(id);
		if (plane != null) {
			plane.weaponSystem.clientSetSelected(index);
		}
	}
	
}
