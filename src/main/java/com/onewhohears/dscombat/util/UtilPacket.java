package com.onewhohears.dscombat.util;

import java.util.List;

import com.onewhohears.dscombat.data.PartData;
import com.onewhohears.dscombat.data.PartsManager;
import com.onewhohears.dscombat.data.RadarData;
import com.onewhohears.dscombat.data.RadarData.RadarPing;
import com.onewhohears.dscombat.data.RadarSystem;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponSystem;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.entity.weapon.EntityMissile;
import com.onewhohears.dscombat.init.ModSounds;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
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
		if (world.getEntity(id) instanceof EntityAbstractAircraft plane) {
			plane.radarSystem.readClientPingsFromServer(pings);
		}
	}
	
	public static void planeDataPacket(int id, PartsManager pm, WeaponSystem ws, RadarSystem rs) {
		System.out.println("plane data packet recieved");
		System.out.println(pm.toString());
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityAbstractAircraft plane) {
			plane.partsManager = pm;
			plane.weaponSystem = ws;
			plane.radarSystem = rs;
			plane.partsManager.clientPartsSetup(plane);
			plane.weaponSystem.clientSetup(plane);
			plane.radarSystem.clientSetup(plane);
			System.out.println("plane data updated");
		}
	}
	
	public static void weaponAmmoPacket(int id, String weaponId, int ammo) {
		System.out.println("ammo packet recieved");
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityAbstractAircraft plane) {
			WeaponData w = plane.weaponSystem.get(weaponId);
			if (w != null) w.setCurrentAmmo(ammo);
		}
	}
	
	public static void weaponSelectPacket(int id, int index) {
		//System.out.println("ammo select packet recieved");
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityAbstractAircraft plane) {
			plane.weaponSystem.clientSetSelected(index);
		}
	}
	
	public static void playSoundPacket(int sound) {
		Minecraft m = Minecraft.getInstance();
		if (sound == 1) {
			m.level.playSound(m.player, new BlockPos(m.player.position()), 
	    			ModSounds.MISSILE_WARNING.get(), SoundSource.PLAYERS, 1f, 1f);
		}
	}
	
	public static void addWeaponPacket(int id, WeaponData data) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityAbstractAircraft plane) {
			plane.weaponSystem.addWeapon(data, false);
		}
	}
	
	public static void removeWeaponPacket(int id, String wid) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityAbstractAircraft plane) {
			plane.weaponSystem.removeWeapon(wid, false);
		}
	}
	
	public static void addPartPacket(int id, PartData data) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityAbstractAircraft plane) {
			plane.partsManager.addPart(data, false);
		}
	}
	
	public static void removePartPacket(int id, String pid) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityAbstractAircraft plane) {
			plane.partsManager.removePart(pid, false);
		}
	}
	
	public static void addRadarPacket(int id, RadarData data) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityAbstractAircraft plane) {
			plane.radarSystem.addRadar(data, false);
		}
	}
	
	public static void removeRadarPacket(int id, String rid) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityAbstractAircraft plane) {
			plane.radarSystem.removeRadar(rid, false);
		}
	}
	
}
