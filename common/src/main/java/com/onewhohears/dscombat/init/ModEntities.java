package com.onewhohears.dscombat.init;

import com.google.common.collect.ImmutableSet;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.vehicle.presets.boat.BoatPresets;
import com.onewhohears.dscombat.data.vehicle.presets.ground_vehicle.CarPresets;
import com.onewhohears.dscombat.data.vehicle.presets.ground_vehicle.StationaryPresets;
import com.onewhohears.dscombat.data.vehicle.presets.helicopter.NoahChopperPresets;
import com.onewhohears.dscombat.data.vehicle.presets.plane.PlanePresets;
import com.onewhohears.dscombat.data.vehicle.presets.submarine.SubPresets;
import com.onewhohears.dscombat.entity.EntityParachute;
import com.onewhohears.dscombat.entity.parts.*;
import com.onewhohears.dscombat.entity.vehicle.*;
import com.onewhohears.dscombat.entity.vehicle.hitbox.RotableHitbox;
import com.onewhohears.dscombat.entity.vehicle.wind_tunnel.EntityWindTunnel;
import com.onewhohears.dscombat.entity.weapon.*;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {
	
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(
            DSCombatMod.MODID, Registries.ENTITY_TYPE);

	public static final RegistrySupplier<EntityType<EntityWindTunnel>> WIND_TUNNEL = ENTITIES.register("wind_tunnel",
			() -> createEntityType(EntityWindTunnel::new, EntityDimensions.fixed(16, 8)));

	// VEHICLES
	
	public static final RegistrySupplier<EntityType<EntityPlane>> PLANE = ENTITIES.register("plane",
			() -> createVehicleType((type, level) -> new EntityPlane(type, level, 
					PlanePresets.DEFAULT_WOODEN_PLANE.getId())));
	public static final RegistrySupplier<EntityType<EntityHelicopter>> HELICOPTER = ENTITIES.register("helicopter",
			() -> createVehicleType((type, level) -> new EntityHelicopter(type, level, 
					NoahChopperPresets.DEFAULT_NOAH_CHOPPER.getId())));
	public static final RegistrySupplier<EntityType<EntityGroundVehicle>> CAR = ENTITIES.register("car",
			() -> createVehicleType((type, level) -> new EntityGroundVehicle(type, level, 
					CarPresets.DEFAULT_AXCEL_TRUCK.getId())));
	public static final RegistrySupplier<EntityType<EntityBoat>> BOAT = ENTITIES.register("boat",
			() -> createVehicleType((type, level) -> new EntityBoat(type, level, 
					BoatPresets.DEFAULT_NATHAN_BOAT.getId())));
	public static final RegistrySupplier<EntityType<EntitySubmarine>> SUBMARINE = ENTITIES.register("submarine",
			() -> createVehicleType((type, level) -> new EntitySubmarine(type, level, 
					SubPresets.DEFAULT_ANDOLF_SUB.getId())));
	public static final RegistrySupplier<EntityType<EntityStationaryVehicle>> STATIONARY = ENTITIES.register("stationary_vehicle",
			() -> createVehicleType((type, level) -> new EntityStationaryVehicle(type, level,
					StationaryPresets.EWR4000.getId())));
	
	public static final RegistrySupplier<EntityType<RotableHitbox>> ROTABLE_HITBOX = ENTITIES.register("rotable_hitbox",
			() -> createEntityTypeFar(RotableHitbox::new, EntityDimensions.scalable(0.1f, 0.1f)));
	
	/* 
	 * IDEA 5 more vehicles
	 * 
	 * PLANES
	 * wooden planes, private jet, large passenger jet
	 * Spitfire, P51 Mustang, C-5 Galaxy, F14, Su-27, Su-57, F22
	 * Tariku Plane (F35-B VTOL)
	 * 
	 * HELICOPTORS
	 * small unarmed heli, large cargo heli, 2 seated heli with gunner on front bottom
	 * Noah Chopper should resemble black hawk
	 * 
	 * CARS/TANKS
	 * Honda Ferrari, Joanna Bike
	 * 
	 * BOATS/SUBMARINES
	 * 
	 * OTHER
	 * Weather Balloon
	 * 
	 * IDEA 9.1 AI for planes or an auto pilot module
	 * AI turret controllers need time and resources invested to level up
	 * it would be funny if they were villagers
	 * IDEA 9.2 pillager outposts with air defense
	 * 
	 * IDEA 9.3 large physical hanger to craft vehicle/put weapons on them
	 */
	
	public static final EntityDimensions SEAT_SIZE = EntityDimensions.scalable(0.8f, 0.8f);
	
	public static final RegistrySupplier<EntityType<EntitySeat>> SEAT = ENTITIES.register("seat",
			() -> createEntityType(EntitySeat::new, SEAT_SIZE));

	public static final RegistrySupplier<EntityType<EntityTurret>> TURRET = ENTITIES.register("turret",
			() -> createEntityTypeFar((type, level) -> new EntityTurret(type, level, "aa_turret"), SEAT_SIZE));
	
	public static final RegistrySupplier<EntityType<EntityChainHook>> CHAIN_HOOK = ENTITIES.register("chain_hook",
			() -> createEntityType(EntityChainHook::new, EntityDimensions.scalable(1.0f, 1.0f)));
	
	public static final EntityDimensions TINY = EntityDimensions.scalable(0.1f, 0.1f);
	
	public static final RegistrySupplier<EntityType<EntityGimbal>> GIMBAL_CAMERA = ENTITIES.register("gimbal_camera",
			() -> createEntityType(EntityGimbal::new, TINY));
	public static final RegistrySupplier<EntityType<EntityWeaponRack>> EXTERNAL_WEAPON_PART = ENTITIES.register("external_weapon_part",
			() -> createEntityType(EntityWeaponRack::new, TINY));
	public static final RegistrySupplier<EntityType<EntityEngine>> EXTERNAL_ENGINE = ENTITIES.register("external_engine",
			() -> createEntityType(EntityEngine::new, EntityDimensions.scalable(0.8f, 0.8f)));
	public static final RegistrySupplier<EntityType<EntityRadar>> EXTERNAL_RADAR = ENTITIES.register("external_radar",
			() -> createEntityType(EntityRadar::new, EntityDimensions.scalable(1.0f, 1.0f)));
    public static final RegistrySupplier<EntityType<EntityFuelTank>> EXTERNAL_FUEL_TANK = ENTITIES.register("external_fuel_tank",
            () -> createEntityType(EntityFuelTank::new, EntityDimensions.scalable(0.8f, 0.8f)));
	
	public static final RegistrySupplier<EntityType<EntityBullet<?>>> BULLET = ENTITIES.register("bullet",
			() -> createEntityType((type, level) -> new EntityBullet<>(type, level, "20mm"), 
					EntityDimensions.scalable(0.15f, 0.15f)));
	public static final RegistrySupplier<EntityType<EntityBomb<?>>> BOMB = ENTITIES.register("bomb",
			() -> createEntityType((type, level) -> new EntityBomb<>(type, level, "anm30"), 
					EntityDimensions.scalable(0.6f, 0.6f)));
	public static final RegistrySupplier<EntityType<EntityBunkerBuster<?>>> BUNKER_BUSTER = ENTITIES.register("bunker_buster",
			() -> createEntityType((type, level) -> new EntityBunkerBuster<>(type, level, "gruetz_bunker_buster"), 
					EntityDimensions.scalable(0.8f, 0.8f)));
	public static final RegistrySupplier<EntityType<PositionMissile<?>>> POS_MISSILE = ENTITIES.register("pos_missile",
			() -> createEntityTypeFar((type, level) -> new PositionMissile<>(type, level, "agm114k"), 
					EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistrySupplier<EntityType<IRMissile<?>>> IR_MISSILE = ENTITIES.register("ir_missile",
			() -> createEntityTypeFar((type, level) -> new IRMissile<>(type, level, "aim9l"), 
					EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistrySupplier<EntityType<TrackEntityMissile<?>>> TRACK_MISSILE = ENTITIES.register("track_missile",
			() -> createEntityTypeFar((type, level) -> new TrackEntityMissile<>(type, level, "aim120b"), 
					EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistrySupplier<EntityType<AntiRadarMissile<?>>> ANTI_RADAR_MISSILE = ENTITIES.register("anti_radar_missile",
			() -> createEntityTypeFar((type, level) -> new AntiRadarMissile<>(type, level, "agm88g"), 
					EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistrySupplier<EntityType<TorpedoMissile<?>>> TORPEDO_MISSILE = ENTITIES.register("torpedo_missile",
			() -> createEntityTypeFar((type, level) -> new TorpedoMissile<>(type, level, "mk13"), 
					EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistrySupplier<EntityType<EntityDumbTorpedo<?>>> DUMB_TORPEDO_MISSILE = ENTITIES.register("dumb_torpedo_missile",
			() -> createEntityTypeFar((type, level) -> new EntityDumbTorpedo<>(type, level, "type91"),
					EntityDimensions.scalable(1f, 1f)));
	
	public static final RegistrySupplier<EntityType<EntityFlare>> FLARE = ENTITIES.register("flare",
			() -> createEntityType(EntityFlare::new, EntityDimensions.scalable(0f, 0f)));
	public static final RegistrySupplier<EntityType<EntityParachute>> PARACHUTE = ENTITIES.register("parachute",
			() -> createEntityType(EntityParachute::new, EntityDimensions.scalable(0.625f, 0.125f)));
	
	private static <T extends Entity> EntityType<T> createEntityType(EntityType.EntityFactory<T> factory, EntityDimensions size) {
        return new EntityType<>(factory, MobCategory.MISC, true, true, false, 
        		true, ImmutableSet.of(), size, 5, 3);
    }
	
	private static <T extends Entity> EntityType<T> createEntityTypeFar(EntityType.EntityFactory<T> factory, EntityDimensions size) {
        return new EntityType<>(factory, MobCategory.MISC, true, true, false, 
        		true, ImmutableSet.of(), size, 16, 3);
    }
	
	private static <T extends Entity> EntityType<T> createVehicleType(EntityType.EntityFactory<T> factory) {
		return createEntityTypeFar(factory, EntityDimensions.fixed(4, 4));
	}

    public static void register() {
        ENTITIES.register();
    }
	
}
