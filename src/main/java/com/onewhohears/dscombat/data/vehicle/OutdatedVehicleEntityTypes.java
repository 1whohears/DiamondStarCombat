package com.onewhohears.dscombat.data.vehicle;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import net.minecraft.world.entity.EntityType;

public class OutdatedVehicleEntityTypes {
	
	private static final Map<String, Supplier<Optional<EntityType<?>>>> map = new HashMap<>();
	
	private static final Supplier<Optional<EntityType<?>>> plane = () -> Optional.of(VehicleType.PLANE.getEntityType());
	private static final Supplier<Optional<EntityType<?>>> heli = () -> Optional.of(VehicleType.HELICOPTER.getEntityType());
	private static final Supplier<Optional<EntityType<?>>> car = () -> Optional.of(VehicleType.CAR.getEntityType());
	private static final Supplier<Optional<EntityType<?>>> boat = () -> Optional.of(VehicleType.BOAT.getEntityType());
	private static final Supplier<Optional<EntityType<?>>> sub = () -> Optional.of(VehicleType.SUBMARINE.getEntityType());
	
	static {
		map.put("dscombat:javi_plane", plane);
		map.put("dscombat:alexis_plane", plane);
		map.put("dscombat:wooden_plane", plane);
		map.put("dscombat:e3sentry_plane", plane);
		map.put("dscombat:bronco_plane", plane);
		map.put("dscombat:felix_plane", plane);
		map.put("dscombat:jason_plane", plane);
		map.put("dscombat:eden_plane", plane);
		map.put("dscombat:noah_chopper", heli);
		map.put("dscombat:mrbudger_tank", car);
		map.put("dscombat:small_roller", car);
		map.put("dscombat:orange_tesla", car);
		map.put("dscombat:axcel_truck", car);
		map.put("dscombat:nathan_boat", boat);
		map.put("dscombat:gronk_battleship", boat);
		map.put("dscombat:destroyer", boat);
		map.put("dscombat:cruiser", boat);
		map.put("dscombat:corvette", boat);
		map.put("dscombat:aircraft_carrier", boat);
		map.put("dscombat:andolf_sub", sub);
		map.put("dscombat:google_sub", sub);
	}
	
	public static Optional<EntityType<?>> getEntityTypeByOldId(String id) {
		if (map.containsKey(id)) return map.get(id).get();
		return Optional.empty();
	}
	
}
