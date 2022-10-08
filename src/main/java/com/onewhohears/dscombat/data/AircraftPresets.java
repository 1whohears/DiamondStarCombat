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
			plane.radarSystem = this.getRadar();
			plane.setHealth(getHealth());
			plane.setMaxHealth(getMaxHealth());
			plane.setMaxSpeed(getMaxSpeed());
			System.out.println("setting up aircraft preset client side "+plane.level.isClientSide+" parts "+plane.partsManager);
		}
		
		public abstract String getPresetName();
		public abstract PartsManager getParts();
		public abstract WeaponSystem getWeapons();
		public abstract RadarSystem getRadar();
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
			pm.addPart(new SeatData("pilot_seat", new Vec3(0, -0.5, 0)), false);
			System.out.println("getting parts from test plane preset "+pm);
			return pm;
		}

		@Override
		public WeaponSystem getWeapons() {
			WeaponSystem ws = new WeaponSystem();
			// bullet 1
			BulletData test = new BulletData("bullet1", new Vec3(0, 0.5, 1), 
					600, 100000, 1, 10, 4, 2);
			ws.addWeapon(test, false);
			test.setCurrentAmmo(test.getMaxAmmo());
			// bullet 2
			BulletData test2 = new BulletData("bullet2", new Vec3(0, 0.5, 1),
					600, 100000, 15, 100, 4, 1, 
					true, true, false, 100d, 4f);
			test2.setCurrentAmmo(test2.getMaxAmmo());
			ws.addWeapon(test2, false);
			// missile 1
			MissileData test3 = new MissileData("missile1", new Vec3(0, 0.5, 1),
					600, 100000, 10, 1000, 1d, 0, 
					true, true, false, 100d, 4f,
					TargetType.GROUND, GuidanceType.PITBULL,
					3.0f, 0.2d, 2.0d);
			test3.setCurrentAmmo(test3.getMaxAmmo());
			ws.addWeapon(test3, false);
			// missile 2
			MissileData test4 = new MissileData("missile2", new Vec3(0, 0.5, 1),
					600, 100000, 20, 1000, 1.5d, 0, 
					true, true, false, 100d, 8f,
					TargetType.AIR, GuidanceType.PITBULL,
					2.0f, 0.3d, 2.0d);
			test4.setCurrentAmmo(test4.getMaxAmmo());
			ws.addWeapon(test4, false);
			return ws;
		}
		
		public RadarSystem getRadar() {
			RadarSystem rs = new RadarSystem();
			// radar air
			RadarData radar = new RadarData("radar1", 1000, 70, 20);
			radar.setScanAircraft(true);
			radar.setScanPlayers(true);
			radar.setScanMobs(false);
			radar.setScanAir(true);
			radar.setScanGround(false);
			rs.addRadar(radar, false);
			// radar air
			RadarData radar2 = new RadarData("radar2", 300, -1, 15);
			radar2.setScanAircraft(true);
			radar2.setScanPlayers(true);
			radar2.setScanMobs(true);
			radar2.setScanAir(false);
			radar2.setScanGround(true);
			rs.addRadar(radar2, false);
			return rs;
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
