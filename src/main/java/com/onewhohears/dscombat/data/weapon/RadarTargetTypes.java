package com.onewhohears.dscombat.data.weapon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.util.UtilEntity;

import net.minecraft.world.entity.Entity;

public class RadarTargetTypes {
	
	private static final Logger LOGGER = LogUtils.getLogger();
	private static RadarTargetTypes instance;
	
	public static RadarTargetTypes get() {
		if (instance == null) {
			instance = new RadarTargetTypes();
		}
		return instance;
	}
	
	public static void close() {
		instance = null;
	}
	
	private List<Class<? extends Entity>> radarVehicleClasses = new ArrayList<>();
	
	private List<Class<? extends Entity>> radarMobClasses = new ArrayList<>();
	
	private Map<String, Float> entityAreas = new HashMap<>();
	
	private RadarTargetTypes() {
	}
	
	public void readConfig() {
		LOGGER.info("RadarTargetTypes READ CONFIG");
		readRadarVehicles();
		readRadarMobs();
		readEntityAreas();
	}
	
	private void readRadarVehicles() {
		radarVehicleClasses.clear();
		List<? extends String> vehicleList =  Config.COMMON.radarVehicles.get();
		for (int i = 0; i < vehicleList.size(); ++i) {
			String className = vehicleList.get(i);
			Class<? extends Entity> c = UtilEntity.getEntityClass(className);
			if (c == null) {
				LOGGER.warn("ERROR: "+className+" does not exist! Vehicle Radar will not look for it.");
				continue;
			}
			radarVehicleClasses.add(c);
			LOGGER.debug("ADDED VEHICLE ENTITY CLASS: "+c.getName());
		}
	}
	
	public List<Class<? extends Entity>> getRadarVehicleClasses() {
		return radarVehicleClasses;
	}
	
	private void readRadarMobs() {
		radarMobClasses.clear();
		List<? extends String> mobList =  Config.COMMON.radarMobs.get();
		for (int i = 0; i < mobList.size(); ++i) {
			String className = mobList.get(i);
			Class<? extends Entity> c = UtilEntity.getEntityClass(className);
			if (c == null) {
				LOGGER.warn("ERROR: "+className+" does not exist! Mob Radar will not look for it.");
				continue;
			}
			radarMobClasses.add(c);
			LOGGER.debug("ADDED MOB ENTITY CLASS: "+c.getName());
		}
	}
	
	public List<Class<? extends Entity>> getRadarMobClasses() {
		return radarMobClasses;
	}
	
	private void readEntityAreas() {
		entityAreas.clear();
		List<? extends String> areaList =  Config.COMMON.radarCrossSecAreas.get();
		for (int i = 0; i < areaList.size(); ++i) {
			String a = areaList.get(i);
			if (!a.contains("/")) continue;
			String[] split = a.split("/");
			if (split.length != 2) continue;
			String entityId = split[0];
			if (!UtilEntity.doesEntityTypeExist(entityId)) {
				LOGGER.warn("ERROR: "+entityId+" does not exist! Radars will not read that area value!");
				continue;
			}
			LOGGER.debug("ADDED ENTITY AREA OVERRIDE: "+a);
			float area = 0;
			try {
				area = Float.parseFloat(split[1]);
			} catch (NumberFormatException e) {
				LOGGER.warn("ERROR: "+split[1]+" is not a number!");
			}
			entityAreas.put(entityId, area);
		}
	}
	
	public float getEntityCrossSectionalArea(String id, float alt) {
		if (!entityAreas.containsKey(id)) return alt;
		return entityAreas.get(id);
	}
	
}
