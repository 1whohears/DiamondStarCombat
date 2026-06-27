package com.onewhohears.dscombat.client.util;

import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.client.screen.VehiclePaintScreen;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.parts.instance.PartInstance;
import com.onewhohears.dscombat.data.radar.RadarStats.RadarPing;
import com.onewhohears.dscombat.data.radar.RadarSystem.RWRWarning;
import com.onewhohears.dscombat.data.vehicle.VehicleInputManager;
import com.onewhohears.dscombat.data.vehicle.VehicleTextureManager;
import com.onewhohears.dscombat.data.vehicle.VehicleDecalManager.DecalData;
import com.onewhohears.dscombat.data.vehicle.physics.DSCPhyCons;
import com.onewhohears.dscombat.data.weapon.instance.WeaponInstance;
import com.onewhohears.dscombat.data.weapon.stats.WeaponStats;
import com.onewhohears.dscombat.entity.parts.EntityChainHook;
import com.onewhohears.dscombat.entity.parts.EntityChainHook.ChainUpdateType;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.entity.parts.EntityWeaponRack;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.vehicle.hitbox.RotableHitbox;
import com.onewhohears.dscombat.init.ModSounds;
import com.onewhohears.dscombat.util.UtilSound;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class UtilClientPacket {

    public static SoundEvent getSoundByIdClient(String id, SoundEvent alt) {
        return UtilSound.getSoundById(id, alt, Minecraft.getInstance().level.registryAccess());
    }

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
	
	public static void addPartPacket(int id, String slotId, PartInstance<?> data) {
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
	
	public static void syncPartPacket(int id, String slotId, FriendlyByteBuf buffer) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
        buffer.readUtf(); // preset id is given so the DataSerializer works, but it isn't needed here
		if (world.getEntity(id) instanceof EntityVehicle plane) {
			PartSlot slot = plane.partsManager.getSlot(slotId);
			if (slot != null && slot.getPartData() != null) {
				slot.getPartData().readBuffer(buffer);
				slot.getPartData().onReceiveClientSync();
			}
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
	
	public static void openVehicleTextureScreen(VehicleTextureManager textures) {
		Minecraft m = Minecraft.getInstance();
		m.setScreen(new VehiclePaintScreen(textures));
	}

	public static void openDecalScreen(EntityVehicle vehicle) {
		Minecraft m = Minecraft.getInstance();
		m.setScreen(new com.onewhohears.dscombat.client.screen.DecalScreen(vehicle.getId(), vehicle.decalManager));
	}
	
	public static void vehicleExplode(int id, Vec3 pos, boolean isAmmoExplosion) {
		Minecraft m = Minecraft.getInstance();
		float explosionRadius = 5;
		
		if (id == -1) {
			UtilParticles.vehicleCrashExplosion(m.level, pos, explosionRadius, -1);
		} else if (m.level.getEntity(id) instanceof EntityVehicle plane) {
			explosionRadius = plane.getStats().crashExplosionRadius;
			
			// If this is ammo explosion, increase radius
			if (isAmmoExplosion) {
				explosionRadius *= 2.0f;
				
				// Main explosion at tank position
				UtilParticles.vehicleCrashExplosion(m.level, pos, explosionRadius, id);
				
				// POWERFUL UPWARD EXPLOSION from turret (yellow lines going up)
				Vec3 turretPos = pos.add(0, 1.5, 0);
				
				// Large smoke column shooting straight up
				for (int i = 0; i < 30; i++) {
					double height = 2.0 + m.level.random.nextDouble() * 8.0; // 2-10 blocks up
					m.level.addParticle(ParticleTypes.LARGE_SMOKE,
						turretPos.x + (m.level.random.nextDouble() - 0.5) * 0.5,
						turretPos.y,
						turretPos.z + (m.level.random.nextDouble() - 0.5) * 0.5,
						(m.level.random.nextDouble() - 0.5) * 0.1,
						0.3 + m.level.random.nextDouble() * 0.4, // Fast upward
						(m.level.random.nextDouble() - 0.5) * 0.1);
				}
				
				// Campfire smoke column (thick black smoke)
				for (int i = 0; i < 20; i++) {
					double height = 2.0 + m.level.random.nextDouble() * 8.0;
					m.level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE,
						turretPos.x + (m.level.random.nextDouble() - 0.5) * 0.4,
						turretPos.y,
						turretPos.z + (m.level.random.nextDouble() - 0.5) * 0.4,
						(m.level.random.nextDouble() - 0.5) * 0.08,
						0.35 + m.level.random.nextDouble() * 0.3,
						(m.level.random.nextDouble() - 0.5) * 0.08);
				}
				
				// Explosion particles shooting upward (yellow/orange streaks)
				for (int i = 0; i < 25; i++) {
					m.level.addParticle(ParticleTypes.FLAME,
						turretPos.x + (m.level.random.nextDouble() - 0.5) * 0.3,
						turretPos.y,
						turretPos.z + (m.level.random.nextDouble() - 0.5) * 0.3,
						(m.level.random.nextDouble() - 0.5) * 0.15,
						0.4 + m.level.random.nextDouble() * 0.5, // Very fast upward
						(m.level.random.nextDouble() - 0.5) * 0.15);
				}
				
				// Soul fire flames (bright blue-ish flames shooting up)
				for (int i = 0; i < 15; i++) {
					m.level.addParticle(ParticleTypes.SOUL_FIRE_FLAME,
						turretPos.x + (m.level.random.nextDouble() - 0.5) * 0.3,
						turretPos.y,
						turretPos.z + (m.level.random.nextDouble() - 0.5) * 0.3,
						(m.level.random.nextDouble() - 0.5) * 0.12,
						0.45 + m.level.random.nextDouble() * 0.4,
						(m.level.random.nextDouble() - 0.5) * 0.12);
				}
				
				// SIDE EXPLOSIONS (yellow/gray clouds to the sides)
				for (int side = 0; side < 8; side++) {
					double angle = (Math.PI * 2 / 8) * side;
					double distance = 2.0 + m.level.random.nextDouble() * 1.5;
					double offsetX = Math.cos(angle) * distance;
					double offsetZ = Math.sin(angle) * distance;
					double offsetY = 0.5 + m.level.random.nextDouble() * 1.0;
					
					Vec3 sidePos = pos.add(offsetX, offsetY, offsetZ);
					
					// Large smoke clouds to sides
					for (int j = 0; j < 8; j++) {
						m.level.addParticle(ParticleTypes.LARGE_SMOKE,
							sidePos.x,
							sidePos.y,
							sidePos.z,
							Math.cos(angle) * (0.2 + m.level.random.nextDouble() * 0.2),
							0.05 + m.level.random.nextDouble() * 0.15,
							Math.sin(angle) * (0.2 + m.level.random.nextDouble() * 0.2));
					}
					
					// Campfire smoke to sides
					for (int j = 0; j < 5; j++) {
						m.level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE,
							sidePos.x,
							sidePos.y,
							sidePos.z,
							Math.cos(angle) * (0.15 + m.level.random.nextDouble() * 0.15),
							0.08 + m.level.random.nextDouble() * 0.12,
							Math.sin(angle) * (0.15 + m.level.random.nextDouble() * 0.15));
					}
					
					// Explosion particles
					for (int j = 0; j < 4; j++) {
						m.level.addParticle(ParticleTypes.EXPLOSION,
							sidePos.x,
							sidePos.y,
							sidePos.z,
							Math.cos(angle) * (0.15 + m.level.random.nextDouble() * 0.1),
							0.05 + m.level.random.nextDouble() * 0.1,
							Math.sin(angle) * (0.15 + m.level.random.nextDouble() * 0.1));
					}
				}
				
				// Additional lava sparks flying everywhere
				for (int i = 0; i < 40; i++) {
					double angle = m.level.random.nextDouble() * Math.PI * 2;
					double pitch = m.level.random.nextDouble() * Math.PI - Math.PI/4;
					double speed = 0.4 + m.level.random.nextDouble() * 0.6;
					m.level.addParticle(ParticleTypes.LAVA,
						turretPos.x,
						turretPos.y,
						turretPos.z,
						Math.cos(angle) * Math.cos(pitch) * speed,
						Math.sin(pitch) * speed * 1.5,
						Math.sin(angle) * Math.cos(pitch) * speed);
				}
			} else {
				// Normal explosion
				UtilParticles.vehicleCrashExplosion(m.level, pos, explosionRadius, id);
			}
		}
		
		// Play sound with proper distance attenuation (like vanilla explosions)
		float pitch = 0.9f + m.level.random.nextFloat() * 0.2f;
		float volume = 4.0f;
		
		// For ammo explosion make sound MUCH louder and lower
		if (isAmmoExplosion) {
			volume *= 2.0f; // Very loud
			pitch *= 0.7f;  // Very low tone
		}
		
		m.level.playSound(m.player, pos.x, pos.y, pos.z, 
			ModSounds.VEHICLE_EXPLOSION, SoundSource.BLOCKS, volume, pitch);
	}

	public static void mineExplode(Vec3 pos, boolean isAntiTank) {
		Minecraft m = Minecraft.getInstance();
		
		if (isAntiTank) {
			// Anti-tank mines use vehicle explosion effects
			float explosionRadius = 3.5f;
			UtilParticles.vehicleCrashExplosion(m.level, pos, explosionRadius, -1);
			
			// Play vehicle explosion sound
			float pitch = 0.9f + m.level.random.nextFloat() * 0.2f;
			float volume = 4.0f;
			
			m.level.playSound(m.player, pos.x, pos.y, pos.z, 
				ModSounds.VEHICLE_EXPLOSION, SoundSource.BLOCKS, volume, pitch);
		}
		// Anti-personnel mines use default explosion (no special effects)
	}
	
	public static void ballisticExplosion(Vec3 pos, float radius, boolean fire) {
		Minecraft m = Minecraft.getInstance();
		if (m.level == null) return;
		
		// Create epic ballistic missile explosion effect
		UtilParticles.ballisticMissileExplosion(m.level, pos, radius, fire);
		
		// Play powerful ballistic explosion sound
		float pitch = 0.6f + m.level.random.nextFloat() * 0.2f;
		float volume = 10.0f; // Very loud and powerful
		
		m.level.playSound(m.player, pos.x, pos.y, pos.z, 
			ModSounds.BALLISTIC_EXPLOSION, SoundSource.BLOCKS, volume, pitch);
	}

	public static void weaponImpact(WeaponStats.WeaponClientImpactType impactType, Vec3 pos) {
		Minecraft m = Minecraft.getInstance();
		impactType.onClientImpact(m.level, pos);
	}

	public static void weaponImpact(WeaponStats.WeaponClientImpactType impactType, Vec3 pos, float explosionRadius, boolean causesFire) {
		Minecraft m = Minecraft.getInstance();
		if (explosionRadius > 3.0f) {
			UtilParticles.bombExplode(m.level, pos, explosionRadius, causesFire);
		} else if (explosionRadius > 0f) {
			// small explosion — just a couple of vanilla smoke puffs
			for (int i = 0; i < 4; ++i) {
				m.level.addParticle(net.minecraft.core.particles.ParticleTypes.SMOKE,
					pos.x, pos.y + 0.1, pos.z,
					(m.level.random.nextDouble() - 0.5) * 0.1,
					0.05,
					(m.level.random.nextDouble() - 0.5) * 0.1);
			}
		}
		// explosionRadius == 0 → no particles
	}

	/**
	 * The modelling of sound attenuation here is dependent on range and distance; not volume.
	 * Attenuation coefficient is closer to 1.0 where the ratio of distance to range is 0.
	 * <br><br>
	 * -kawaiicakes
	 */
	public static void delayedSound(String soundId, Vec3 pos, float range, float volume, float pitch) {
		SoundEvent sound = getSoundByIdClient(soundId, null);
		if (sound == null) return;

		Minecraft m = Minecraft.getInstance();

		float dist = (float) Objects.requireNonNull(m.getCameraEntity()).position().distanceTo(pos);
		float attenuationCoefficient = (float) Mth.clamp((1 / Math.pow(((dist + (range / 5)) / (range * 2)), 2)) / 100, 0.0, 1.0);

		SimpleSoundInstance ssi = new SimpleSoundInstance(sound, SoundSource.PLAYERS, 
				volume * attenuationCoefficient, pitch, RandomSource.create(UtilParticles.random.nextLong()),
				pos.x, pos.y, pos.z);
		int delay = (int)(dist  / DSCPhyCons.getVelSound());

		m.getSoundManager().playDelayed(ssi, delay);
	}
	
	public static void updateVehicleChain(int vehicleId, int hookId, int playerId, ChainUpdateType type) {
		Minecraft m = Minecraft.getInstance();
		EntityVehicle vehicle = null;
		EntityChainHook hook = null;
		Player player = null;
		if (m.level == null) return;
		if (m.level.getEntity(vehicleId) instanceof EntityVehicle v) vehicle = v;
		if (m.level.getEntity(hookId) instanceof EntityChainHook c) hook = c;
		if (m.level.getEntity(playerId) instanceof Player p) player = p;
		switch (type) {
		case CHAIN_ADD_PLAYER:
			if (hook == null) return;
			hook.addPlayerConnection(player);
			return;
		case CHAIN_ADD_VEHICLE:
			if (hook == null) return;
			hook.addVehicleConnection(player, vehicle);
			return;
		case CHAIN_DISCONNECT_PLAYER:
			if (hook == null) return;
			hook.disconnectPlayer(player);
			return;
		case CHAIN_DISCONNECT_VEHICLE:
			if (hook == null) return;
			hook.disconnectVehicle(vehicle);
			return;
		case VEHICLE_ADD_PLAYER:
			if (vehicle == null) return;
			vehicle.chainToPlayer(player);
			return;		
		}
	}
	
	public static void debugHitboxPos(int id, String hitbox_name, Vec3 pos, Vec3 size) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world == null) return;
		if (!(world.getEntity(id) instanceof EntityVehicle vehicle)) return;
		RotableHitbox hitbox = vehicle.getHitboxByName(hitbox_name);
		if (hitbox == null) return;
		hitbox.setTestPos(pos);
		hitbox.setTestSize(size);
	}

	public static void onShoot(int vehicleId, int shooterId, ShootType type) {
		Minecraft m = Minecraft.getInstance();
		Level world = m.level;
		if (world == null) return;
		if (type == ShootType.WEAPON_RACK) {
			if (!(world.getEntity(vehicleId) instanceof EntityWeaponRack rack)) return;
			rack.onClientShoot();
		} else if (type == ShootType.TURRET) {
			if (!(world.getEntity(vehicleId) instanceof EntityTurret turret)) return;
			turret.onClientShoot();
		} else if (type == ShootType.FLARE) {
			if (m.player == null || m.player.getRootVehicle().getId() != vehicleId) return;
			if (!(world.getEntity(vehicleId) instanceof EntityVehicle vehicle)) return;
			vehicle.soundManager.playPassengerFlareSound();
		} else if (type == ShootType.CHAFF) {
			if (m.player == null || m.player.getRootVehicle().getId() != vehicleId) return;
			if (!(world.getEntity(vehicleId) instanceof EntityVehicle vehicle)) return;
			vehicle.soundManager.playPassengerChaffSound();
		}
	}

    public static void setTargetPos(Vec3 targetPos) {
        Config.CLIENT.targetPosX.set(targetPos.x);
        Config.CLIENT.targetPosY.set(targetPos.y);
        Config.CLIENT.targetPosZ.set(targetPos.z);
    }

	public static void distantGunfire(String soundId, Vec3 pos, float volume, float pitch, int durationTicks) {
		SoundEvent sound = getSoundByIdClient(soundId, null);
		if (sound == null) return;
		UtilClientSafeSounds.distantGunfireSound(pos, sound, volume, pitch, durationTicks);
	}

	public static void vehicleDecal(int vehicleId, int senderPlayerId, byte action, 
	                                @Nullable DecalData decalData, @Nullable String decalId) {
		Minecraft mc = Minecraft.getInstance();
		// Skip if this client is the one who sent the packet (already applied locally)
		if (mc.player != null && mc.player.getId() == senderPlayerId) return;
		Level level = com.onewhohears.onewholibs.util.UtilEntity.getLevel(mc.player);
		if (level == null) return;
		if (!(level.getEntity(vehicleId) instanceof com.onewhohears.dscombat.entity.vehicle.EntityVehicle vehicle)) return;
		if (action == com.onewhohears.dscombat.common.network.toserver.ToServerVehicleDecal.ACTION_ADD && decalData != null) {
			vehicle.decalManager.addDecal(decalData);
		} else if (action == com.onewhohears.dscombat.common.network.toserver.ToServerVehicleDecal.ACTION_UPDATE && decalData != null) {
			vehicle.decalManager.updateDecal(decalData);
		} else if (decalId != null) {
			vehicle.decalManager.removeDecal(decalId);
		}
	}

	public static void ecmJam() {
		Minecraft m = Minecraft.getInstance();
		net.minecraft.client.player.LocalPlayer player = m.player;
		if (player == null) return;
		net.minecraft.world.entity.Entity root = player.getRootVehicle();
		if (root instanceof com.onewhohears.dscombat.entity.vehicle.EntityVehicle vehicle) {
			if (vehicle.partsManager.getActiveJammerStrength() > 0f) {
				com.onewhohears.dscombat.client.overlay.components.EcmStatusOverlay.onJamEvent();
			}
		}
	}

    public enum ShootType {
		TURRET,
		WEAPON_RACK,
		FLARE,
		CHAFF
	}
	
}
