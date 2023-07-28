package com.onewhohears.dscombat.data.weapon;

import java.util.ArrayList;
import java.util.List;

import com.onewhohears.dscombat.Config;

import net.minecraft.world.entity.Entity;

public class RadarTargetTypes {
	
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
	
	private List<Class<? extends Entity>> irEntityClasses = new ArrayList<>();
	private List<Float> irEntityHeats = new ArrayList<>();
	
	private RadarTargetTypes() {
		
	}
	
	public void readConfig() {
		System.out.println("RadarTargetTypes READ CONFIG");
		readIRMissiles();
	}
	
	private void readIRMissiles() {
		irEntityClasses.clear();
		irEntityHeats.clear();
		List<? extends String> irList =  Config.COMMON.irEntities.get();
		for (int i = 0; i < irList.size(); ++i) {
			String a = irList.get(i);
			if (!a.contains("/")) continue;
			String[] split = a.split("/");
			if (split.length != 2) continue;
			String className = split[0];
			Class<? extends Entity> c;
			try {
				c = Class.forName(className, false, getClass().getClassLoader())
						.asSubclass(Entity.class);
			} catch (ClassNotFoundException e) {
				System.out.println("ERROR: "+className+" does not exist! IR missiles will not look for it.");
				continue;
			}
			if (c == null) {
				System.out.println("ERROR: "+className+" does not exist! IR missiles will not look for it.");
				continue;
			}
			irEntityClasses.add(c);
			System.out.println("ADDED: "+c.getName());
			float heat = 0;
			try {
				heat = Float.parseFloat(split[1]);
			} catch (NumberFormatException e) {
				System.out.println("ERROR: "+split[1]+" is not a number!");
			}
			irEntityHeats.add(heat);
		}
	}
	
	public List<Class<? extends Entity>> getIrEntityClasses() {
		return irEntityClasses;
	}
	
	public List<Float> getIrEntityHeats() {
		return irEntityHeats;
	}
	
}
