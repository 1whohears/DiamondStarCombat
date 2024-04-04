package com.onewhohears.dscombat.data.weapon;

import java.util.ArrayList;
import java.util.List;

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
	
	private List<Class<? extends Entity>> radarMobClasses = new ArrayList<>();
	
	private RadarTargetTypes() {
	}
	
	public void readConfig() {
		LOGGER.info("RadarTargetTypes READ CONFIG");
		readRadarMobs();
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
	
}
