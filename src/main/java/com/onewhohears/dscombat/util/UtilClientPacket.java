package com.onewhohears.dscombat.util;

import java.util.List;
import java.util.Objects;

import com.onewhohears.dscombat.client.screen.VehiclePaintScreen;
import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.radar.RadarStats.RadarPing;
import com.onewhohears.dscombat.data.radar.RadarSystem.RWRWarning;
import com.onewhohears.dscombat.data.vehicle.DSCPhyCons;
import com.onewhohears.dscombat.data.vehicle.EntityScreenData;
import com.onewhohears.dscombat.data.vehicle.VehicleInputManager;
import com.onewhohears.dscombat.data.vehicle.VehicleTextureManager;
import com.onewhohears.dscombat.data.weapon.instance.WeaponInstance;
import com.onewhohears.dscombat.data.weapon.stats.WeaponStats;
import com.onewhohears.dscombat.entity.parts.EntityChainHook;
import com.onewhohears.dscombat.entity.parts.EntityChainHook.ChainUpdateType;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class UtilClientPacket {
	
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
			WeaponInstance<?> w = plane.weaponSystem.get(weaponId, slotId);
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
			UtilParticles.vehicleCrashExplosion(m.level, pos, plane.getStats().crashExplosionRadius);
	}

	public static void weaponImpact(WeaponStats.WeaponClientImpactType impactType, Vec3 pos) {
		Minecraft m = Minecraft.getInstance();
		impactType.onClientImpact(m.level, pos);
	}

	/**
	 * The modelling of sound attenuation here is dependent on range and distance; not volume.
	 * Attenuation coefficient is closer to 1.0 where the ratio of distance to range is 0.
	 * <br><br>
	 * -kawaiicakes
	 */
	public static void delayedSound(String soundId, Vec3 pos, float range, float volume, float pitch) {
		SoundEvent sound = UtilSound.getSoundById(soundId, null);
		if (sound == null) return;

		Minecraft m = Minecraft.getInstance();

		float dist = (float) Objects.requireNonNull(m.getCameraEntity()).position().distanceTo(pos);
		float attenuationCoefficient = (float) Mth.clamp((1 / Math.pow(((dist + (range / 5)) / (range * 2)), 2)) / 100, 0.0, 1.0);

		SimpleSoundInstance ssi = new SimpleSoundInstance(sound, SoundSource.PLAYERS, 
				volume * attenuationCoefficient, pitch, RandomSource.create(UtilParticles.random.nextLong()),
				pos.x, pos.y, pos.z);
		int delay = (int)(dist  / DSCPhyCons.VEL_SOUND);

		m.getSoundManager().playDelayed(ssi, delay);
	}
	
	public static void updateVehicleChain(int vehicleId, int hookId, int playerId, ChainUpdateType type) {
		Minecraft m = Minecraft.getInstance();
		EntityVehicle vehicle = null;
		EntityChainHook hook = null;
		Player player = null;
		if (m.level.getEntity(vehicleId) instanceof EntityVehicle v) vehicle = v;
		if (m.level.getEntity(hookId) instanceof EntityChainHook c) hook = c;
		if (m.level.getEntity(playerId) instanceof Player p) player = p;
		switch (type) {
		case CHAIN_ADD_PLAYER:
			hook.addPlayerConnection(player);
			return;
		case CHAIN_ADD_VEHICLE:
			hook.addVehicleConnection(player, vehicle);
			return;
		case CHAIN_DISCONNECT_PLAYER:
			hook.disconnectPlayer(player);
			return;
		case CHAIN_DISCONNECT_VEHICLE:
			hook.disconnectVehicle(vehicle);
			return;
		case VEHICLE_ADD_PLAYER:
			vehicle.chainToPlayer(player);
			return;		
		}
	}
	
}
