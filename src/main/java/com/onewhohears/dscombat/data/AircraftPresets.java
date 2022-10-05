package com.onewhohears.dscombat.data;

import java.util.ArrayList;
import java.util.List;

import com.onewhohears.dscombat.data.MissileData.GuidanceType;
import com.onewhohears.dscombat.data.MissileData.TargetType;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;

import net.minecraft.world.phys.Vec3;

public class AircraftPresets {
	
	public static List<AircraftPreset> presets = new ArrayList<AircraftPreset>();
	
	public static void setupPresets() {
		presets.add(new TestPlanePreset());
	}
	
	public static void setupAircraftByPreset(EntityAbstractAircraft plane, String preset) {
		for (int i = 0; i < presets.size(); ++i) {
			if (presets.get(i).getPresetName().equals(preset)) 
				presets.get(i).setupAircraft(plane);
		}
	}
	
	public static abstract class AircraftPreset {
		
		public void setupAircraft(EntityAbstractAircraft plane) {
			plane.partsManager = this.getParts();
			plane.weaponSystem = this.getWeapons();
			plane.setHealth(getHealth());
			plane.setMaxHealth(getMaxHealth());
			plane.setMaxSpeed(getMaxSpeed());
		}
		
		public abstract String getPresetName();
		public abstract PartsManager getParts();
		public abstract WeaponSystem getWeapons();
		public abstract float getHealth();
		public abstract float getMaxHealth();
		public abstract float getMaxSpeed();
		
	}
	
	public static class TestPlanePreset extends AircraftPreset {
		
		@Override
		public String getPresetName() {
			return "test_plane";
		}

		@Override
		public PartsManager getParts() {
			PartsManager pm = new PartsManager();
			// seat
			pm.addPart(new SeatData("pilot_seat", new Vec3(0, -0.5, 0)));
			// radar
			RadarData radar = new RadarData("main-radar", 1000, 90, 20);
			radar.setScanAircraft(true);
			radar.setScanPlayers(true);
			radar.setScanMobs(true);
			pm.addPart(radar);
			return pm;
		}

		@Override
		public WeaponSystem getWeapons() {
			WeaponSystem ws = new WeaponSystem();
			// bullet 1
			BulletData test = new BulletData("bullet1", new Vec3(0, 0.5, 1), 
					600, 100000, 1, 10, 4, 2);
			ws.addWeapon(test);
			test.setCurrentAmmo(test.getMaxAmmo());
			// bullet 2
			BulletData test2 = new BulletData("bullet2", new Vec3(0, 0.5, 1),
					600, 100000, 15, 100, 4, 1, 
					true, true, false, 100d, 4f);
			test2.setCurrentAmmo(test2.getMaxAmmo());
			ws.addWeapon(test2);
			// missile 1
			MissileData test3 = new MissileData("missile1", new Vec3(0, 0.5, 1),
					600, 100000, 10, 1000, 1d, 0, 
					true, true, false, 100d, 4f,
					TargetType.GROUND, GuidanceType.PITBULL,
					2.5f, 0.2d, 1.0d);
			test3.setCurrentAmmo(test3.getMaxAmmo());
			ws.addWeapon(test3);
			// missile 2
			MissileData test4 = new MissileData("missile2", new Vec3(0, 0.5, 1),
					600, 100000, 20, 1000, 1.5d, 0, 
					true, true, false, 100d, 8f,
					TargetType.AIR, GuidanceType.PITBULL,
					1.5f, 0.3d, 1.0d);
			test4.setCurrentAmmo(test4.getMaxAmmo());
			ws.addWeapon(test4);
			return ws;
		}

		@Override
		public float getHealth() {
			return 100f;
		}

		@Override
		public float getMaxHealth() {
			return 100f;
		}

		@Override
		public float getMaxSpeed() {
			return 1.5f;
		}
		
	}
	
}
