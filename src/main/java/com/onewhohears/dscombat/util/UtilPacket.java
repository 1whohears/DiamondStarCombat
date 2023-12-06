package com.onewhohears.dscombat.util;

import java.util.List;

import com.onewhohears.dscombat.client.screen.VehiclePaintScreen;
import com.onewhohears.dscombat.command.DSCGameRules;
import com.onewhohears.dscombat.data.aircraft.EntityScreenData;
import com.onewhohears.dscombat.data.aircraft.VehicleInputManager;
import com.onewhohears.dscombat.data.aircraft.VehicleTextureManager;
import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.radar.RadarData.RadarPing;
import com.onewhohears.dscombat.data.radar.RadarSystem.RWRWarning;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
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
			plane.radarSystem.readRWRWarningFromServer(warning);
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
	
	public static void vehicleTexturePacket(int ignore_player_id, int vehicle_id, ByteBuf buffer) {
		Minecraft m = Minecraft.getInstance();
		if (m.player.getId() == ignore_player_id) return;
		Level world = m.level;
		if (world.getEntity(vehicle_id) instanceof EntityVehicle plane) {
			plane.textureManager.read(buffer);
		}
	}
	
	public static void vehicleScreenDebug(int type, Vec3 rel_pos, float width, float height, float rel_x_rot, float rel_y_rot, float rel_z_rot) {
		Minecraft m = Minecraft.getInstance();
		if (!(m.player.getRootVehicle() instanceof EntityVehicle vehicle)) return;
		EntityScreenData screenData = vehicle.getScreenByTypeId(type);
		if (screenData == null) return;
		screenData.rel_pos = rel_pos;
		screenData.width = width;
		screenData.height = height;
		screenData.xRot = rel_x_rot;
		screenData.yRot = rel_y_rot;
		screenData.zRot = rel_z_rot;
	}
	
	public static void openVehicleTextureScreen(VehicleTextureManager textures) {
		Minecraft m = Minecraft.getInstance();
		m.setScreen(new VehiclePaintScreen(textures));
	}
	
	public static void vehicleExplode(int id, Vec3 pos) {
		Minecraft m = Minecraft.getInstance();
		if (id == -1) 
			UtilParticles.vehicleCrashExplosion(m.level, pos, 5);
		else if (m.level.getEntity(id) instanceof EntityVehicle plane) 
			UtilParticles.vehicleCrashExplosion(m.level, pos, plane.vehicleData.crashExplosionRadius);
	}

	public static void weaponImpact(WeaponData.WeaponClientImpactType impactType, Vec3 pos) {
		Minecraft m = Minecraft.getInstance();
		//System.out.println("impact type = "+impactType.name()+" pos = "+pos);
		impactType.onClientImpact(m.level, pos);
		if (m.level.getGameRules().getBoolean(DSCGameRules.WEAPON_HIT_FEEDBACK)) {
			// TODO 6.4 visual feedback on owner client cross hair
		}
	}
	
}
