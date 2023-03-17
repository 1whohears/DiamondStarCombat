package com.onewhohears.dscombat.init;

import com.google.common.collect.ImmutableSet;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.AircraftPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityBoat;
import com.onewhohears.dscombat.entity.aircraft.EntityGroundVehicle;
import com.onewhohears.dscombat.entity.aircraft.EntityHelicopter;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;
import com.onewhohears.dscombat.entity.aircraft.EntitySubmarine;
import com.onewhohears.dscombat.entity.parts.EntitySeat;
import com.onewhohears.dscombat.entity.parts.EntitySeatCamera;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.entity.parts.EntityWeaponRack;
import com.onewhohears.dscombat.entity.weapon.AntiRadarMissile;
import com.onewhohears.dscombat.entity.weapon.EntityBullet;
import com.onewhohears.dscombat.entity.weapon.EntityFlare;
import com.onewhohears.dscombat.entity.weapon.IRMissile;
import com.onewhohears.dscombat.entity.weapon.PositionMissile;
import com.onewhohears.dscombat.entity.weapon.TorpedoMissile;
import com.onewhohears.dscombat.entity.weapon.TrackEntityMissile;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
	
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, DSCombatMod.MODID);
	
	public static void register(IEventBus eventBus) {
		ENTITIES.register(eventBus);
	}
	
	// PLANES
	
	public static final RegistryObject<EntityType<EntityPlane>> JAVI_PLANE = ENTITIES.register("javi_plane", 
			() -> createEntityTypeFar((type, level) -> new EntityPlane(type, level, 
					AircraftPresets.getAircraftTextures("javi_plane"),
					ModSounds.JET_1, ModItems.JAVI_PLANE), 
					EntityDimensions.scalable(2.45f, 2.45f)));
	
	public static final RegistryObject<EntityType<EntityPlane>> ALEXIS_PLANE = ENTITIES.register("alexis_plane", 
			() -> createEntityTypeFar((type, level) -> new EntityPlane(type, level, 
					AircraftPresets.getAircraftTextures("alexis_plane"),
					ModSounds.JET_1, ModItems.ALEXIS_PLANE), 
					EntityDimensions.scalable(2.0f, 2.0f)));
	
	// HELICOPTORS
	
	public static final RegistryObject<EntityType<EntityHelicopter>> NOAH_CHOPPER = ENTITIES.register("noah_chopper", 
			() -> createEntityTypeFar((type, level) -> new EntityHelicopter(type, level, 
					AircraftPresets.getAircraftTextures("noah_chopper"),
					ModSounds.HELI_1, ModItems.NOAH_CHOPPER, true), 
					EntityDimensions.scalable(2.8f, 2.8f)));
	
	// TANKS
	
	public static final RegistryObject<EntityType<EntityGroundVehicle>> MRBUDGER_TANK = ENTITIES.register("mrbudger_tank", 
			() -> createEntityTypeFar((type, level) -> new EntityGroundVehicle(type, level, 
					AircraftPresets.getAircraftTextures("mrbudger_tank"),
					ModSounds.JET_1, ModItems.MRBUDGER_TANK, true), 
					EntityDimensions.scalable(3.0f, 2.5f)));
	
	public static final RegistryObject<EntityType<EntityGroundVehicle>> SMALL_ROLLER = ENTITIES.register("small_roller", 
			() -> createEntityTypeFar((type, level) -> new EntityGroundVehicle(type, level, 
					AircraftPresets.getAircraftTextures("small_roller"),
					ModSounds.JET_1, ModItems.SMALL_ROLLER, true), 
					EntityDimensions.scalable(1.5f, 0.8f)));
	
	// CARS
	
	// BOATS
	
	public static final RegistryObject<EntityType<EntityBoat>> NATHAN_BOAT = ENTITIES.register("nathan_boat", 
			() -> createEntityTypeFar((type, level) -> new EntityBoat(type, level, 
					AircraftPresets.getAircraftTextures("nathan_boat"),
					ModSounds.JET_1, ModItems.NATHAN_BOAT), 
					EntityDimensions.scalable(2.5f,1.5f)));
	
	// SUBMARINES
	
	public static final RegistryObject<EntityType<EntitySubmarine>> ANDOLF_SUB = ENTITIES.register("andolf_sub", 
			() -> createEntityTypeFar((type, level) -> new EntitySubmarine(type, level, 
					AircraftPresets.getAircraftTextures("andolf_sub"),
					ModSounds.JET_1, ModItems.ANDOLF_SUB), 
					EntityDimensions.scalable(3.5f,3.5f)));
	
	/* 
	 * IDEA more vehicles
	 * 
	 * PLANES
	 * wooden plane, large wooden plane, something like a spitfire, something like a p51 mustang 
	 * private jet, large passenger jet, large cargo plane like a galaxy, large radar plane
	 * Tariku plane will be VTOL
	 * 
	 * HELICOPTORS
	 * small unarmed heli, large cargo heli, 2 seated heli with gunner on front bottom
	 * noah chopper should resemble black hawk
	 * 
	 * CARS/TANKS
	 * TODO Honda Ferrari/Orange Tesla
	 * 
	 * BOATS/SUBMARINES
	 * 
	 * IDEA more weapons
	 * 
	 * IDEA AI for planes and turrets
	 */
	
	public static final RegistryObject<EntityType<EntitySeat>> SEAT = ENTITIES.register("seat", 
			() -> createEntityType(EntitySeat::new, EntityDimensions.scalable(0f, 0f)));
	public static final RegistryObject<EntityType<EntitySeatCamera>> CAMERA = ENTITIES.register("seat_camera", 
			() -> createEntityType(EntitySeatCamera::new, EntityDimensions.scalable(0f, 0f)));
	
	public static final RegistryObject<EntityType<EntityTurret>> MINIGUN_TURRET = ENTITIES.register("minigun_turret", 
			() -> createEntityTypeFar((type, level) -> new EntityTurret(type, level, 0.0, 0.8), 
					EntityDimensions.scalable(0.1f, 0.1f)));
	public static final RegistryObject<EntityType<EntityTurret>> HEAVY_TANK_TURRET = ENTITIES.register("heavy_tank_turret", 
			() -> createEntityTypeFar((type, level) -> new EntityTurret(type, level, 0.0, 0.5), 
					EntityDimensions.scalable(0.1f, 0.1f)));
	public static final RegistryObject<EntityType<EntityTurret>> STEVE_UP_SMASH = ENTITIES.register("steve_up_smash", 
			() -> createEntityTypeFar((type, level) -> new EntityTurret(type, level, 0.0, 3.2), 
					EntityDimensions.scalable(0.1f, 0.1f)));
	
	public static final RegistryObject<EntityType<EntityWeaponRack>> LIGHT_MISSILE_RACK = ENTITIES.register("light_missile_rack", 
			() -> createEntityType(EntityWeaponRack::new, EntityDimensions.scalable(0.1f, 0.1f)));
	public static final RegistryObject<EntityType<EntityWeaponRack>> HEAVY_MISSILE_RACK = ENTITIES.register("heavy_missile_rack", 
			() -> createEntityType(EntityWeaponRack::new, EntityDimensions.scalable(0.1f, 0.1f)));
	public static final RegistryObject<EntityType<EntityWeaponRack>> XM12 = ENTITIES.register("xm12", 
			() -> createEntityType(EntityWeaponRack::new, EntityDimensions.scalable(0.1f, 0.1f)));
	
	public static final RegistryObject<EntityType<EntityBullet>> BULLET = ENTITIES.register("bullet", 
			() -> createEntityType(EntityBullet::new, EntityDimensions.scalable(0.15f, 0.15f)));
	public static final RegistryObject<EntityType<PositionMissile>> POS_MISSILE_1 = ENTITIES.register("pos_missile_1", 
			() -> createEntityTypeFar(PositionMissile::new, EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<IRMissile>> IR_MISSILE_1 = ENTITIES.register("ir_missile_1", 
			() -> createEntityTypeFar(IRMissile::new, EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<TrackEntityMissile>> TRACK_MISSILE_1 = ENTITIES.register("track_missile_1", 
			() -> createEntityTypeFar(TrackEntityMissile::new, EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<AntiRadarMissile>> ANTI_RADAR_MISSILE_1 = ENTITIES.register("anti_radar_missile_1", 
			() -> createEntityTypeFar(AntiRadarMissile::new, EntityDimensions.scalable(0.5f, 0.5f)));
	public static final RegistryObject<EntityType<TorpedoMissile>> TORPEDO_MISSILE_1 = ENTITIES.register("torpedo_missile_1", 
			() -> createEntityTypeFar(TorpedoMissile::new, EntityDimensions.scalable(0.5f, 0.5f)));
	
	public static final RegistryObject<EntityType<EntityFlare>> FLARE = ENTITIES.register("flare", 
			() -> createEntityType(EntityFlare::new, EntityDimensions.scalable(0f, 0f)));
	
	private static <T extends Entity> EntityType<T> createEntityType(EntityType.EntityFactory<T> factory, EntityDimensions size) {
        return new EntityType<>(factory, MobCategory.MISC, true, true, false, 
        		true, ImmutableSet.of(), size, 5, 3);
    }
	
	private static <T extends Entity> EntityType<T> createEntityTypeFar(EntityType.EntityFactory<T> factory, EntityDimensions size) {
        return new EntityType<>(factory, MobCategory.MISC, true, true, false, 
        		true, ImmutableSet.of(), size, 15, 3);
    }
	
}
