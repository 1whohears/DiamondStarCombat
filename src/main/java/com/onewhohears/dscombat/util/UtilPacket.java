package com.onewhohears.dscombat.util;

import java.util.List;

import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartsManager;
import com.onewhohears.dscombat.data.radar.RadarData;
import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.data.radar.RadarData.RadarPing;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponSystem;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
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
		if (world.getEntity(id) instanceof EntityAircraft plane) {
			plane.radarSystem.readClientPingsFromServer(pings);
		}
	}
	
	public static void planeDataPacket(int id, PartsManager pm, WeaponSystem ws, RadarSystem rs) {
		//System.out.println("plane data packet received");
		//System.out.println(pm.toString());
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityAircraft plane) {
			plane.partsManager.copy(pm);
			plane.weaponSystem.copy(ws);
			plane.radarSystem.copy(rs);
			// ORDER MATTERS
			plane.weaponSystem.clientSetup(plane);
			plane.radarSystem.clientSetup(plane);
			plane.partsManager.clientPartsSetup(plane);
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
	
	public static void addPartPacket(int id, String slotName, PartData data) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityAircraft plane) {
			plane.partsManager.addPart(data, slotName, false);
		}
	}
	
	public static void removePartPacket(int id, String slotName) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world.getEntity(id) instanceof EntityAircraft plane) {
			plane.partsManager.removePart(slotName, false);
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
	
}
