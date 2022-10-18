package com.onewhohears.dscombat.data;

import java.util.ArrayList;
import java.util.List;

import com.onewhohears.dscombat.data.weapon.BulletData;
import com.onewhohears.dscombat.data.weapon.MissileData;
import com.onewhohears.dscombat.data.weapon.WeaponSystem;
import com.onewhohears.dscombat.data.weapon.MissileData.GuidanceType;
import com.onewhohears.dscombat.data.weapon.MissileData.TargetType;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

public class AircraftPresets {
	
	public static List<AircraftPreset> presets = new ArrayList<AircraftPreset>();
	
	public static void setupPresets() {
		presets.add(new TestPlanePreset());
		presets.add(new JaviPreset());
		// TODO check for a text file with the names of all the presets to read
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
			plane.setFlares(getFlares());
			plane.setStealth(getStealth());
			plane.setMaxDeltaPitch(getMaxPitch());
			plane.setMaxDeltaRoll(getMaxRoll());
			plane.setMaxDeltaYaw(getMaxYaw());
			plane.setThrottleIncreaseRate(getThrottleUp());
			plane.setThrottleDecreaseRate(getThrottleDown());
			plane.setIdleHeat(getIdleHeat());
			plane.setEngineHeat(getEngineHeat());
			System.out.println("setting up aircraft preset client side "+plane.level.isClientSide+" parts "+plane.partsManager);
		}
		
		public abstract String getPresetName();
		public abstract PartsManager getParts();
		public abstract WeaponSystem getWeapons();
		public abstract RadarSystem getRadar();
		public abstract float getHealth();
		public abstract float getMaxHealth();
		public abstract float getMaxSpeed();
		public abstract int getFlares();
		public abstract float getStealth();
		public abstract float getMaxPitch();
		public abstract float getMaxRoll();
		public abstract float getMaxYaw();
		public abstract float getThrottleUp();
		public abstract float getThrottleDown();
		public abstract float getIdleHeat();
		public abstract float getEngineHeat();
		public abstract float getAircraftWeight();
		public abstract float getMaxThrust();
		public abstract float getSurfaceArea();
		
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
			BulletData test = new BulletData("bullet1", new Vec3(0, 0, 1), 
					600, 100000, 1, true, 10, 4, 2);
			ws.addWeapon(test, false);
			test.setCurrentAmmo(test.getMaxAmmo());
			// bullet 2
			BulletData test2 = new BulletData("bullet2", new Vec3(0, 0, 1),
					600, 100000, 15, true, 100, 4, 1, 
					true, true, false, 100d, 4f);
			test2.setCurrentAmmo(test2.getMaxAmmo());
			ws.addWeapon(test2, false);
			// missile 1
			MissileData test3 = new MissileData("ground_fox3", new Vec3(0, 0, 1),
					600, 100000, 20, true, 1000, 2d, 0, 
					true, true, false, 100d, 4f,
					TargetType.GROUND, GuidanceType.PITBULL,
					3.0f, 0.02d, 2.0d, -1);
			test3.setCurrentAmmo(test3.getMaxAmmo());
			ws.addWeapon(test3, false);
			// missile 2
			MissileData test4 = new MissileData("air_fox3", new Vec3(0, 0, 1),
					600, 100000, 80, true, 1000, 2.0d, 0, 
					true, true, false, 100d, 5f,
					TargetType.AIR, GuidanceType.PITBULL,
					2.0f, 0.04d, 2.0d, 90);
			test4.setCurrentAmmo(test4.getMaxAmmo());
			ws.addWeapon(test4, false);
			// missile 3
			MissileData test5 = new MissileData("air_fox2", new Vec3(0, 0, 1),
					600, 100000, 40, true, 1000, 2.0d, 0, 
					true, true, false, 100d, 4f,
					TargetType.AIR, GuidanceType.IR,
					4.0f, 0.03d, 3.0d, 90);
			test5.setCurrentAmmo(test5.getMaxAmmo());
			ws.addWeapon(test5, false);
			// missile 4
			MissileData test6 = new MissileData("test_pos", new Vec3(0, 0, 1),
					600, 100000, 40, true, 1000, 2.0d, 0, 
					true, true, false, 100d, 4f,
					TargetType.POS, GuidanceType.PITBULL,
					5.0f, 0.3d, 3.0d, 90);
			test6.setCurrentAmmo(test6.getMaxAmmo());
			ws.addWeapon(test6, false);			
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

		@Override
		public int getFlares() {
			return 100;
		}

		@Override
		public float getStealth() {
			return 1;
		}

		@Override
		public float getMaxPitch() {
			return 2;
		}

		@Override
		public float getMaxRoll() {
			return 8;
		}

		@Override
		public float getMaxYaw() {
			return 1;
		}

		@Override
		public float getThrottleUp() {
			return 0.02f;
		}

		@Override
		public float getThrottleDown() {
			return 0.05f;
		}

		@Override
		public float getIdleHeat() {
			return 4f;
		}

		@Override
		public float getEngineHeat() {
			return 4f;
		}

		@Override
		public float getAircraftWeight() {
			return 0.05f;
		}

		@Override
		public float getMaxThrust() {
			return 0.1f;
		}

		@Override
		public float getSurfaceArea() {
			return 1f;
		}
		
	}
	
	public static class JaviPreset extends AircraftPreset {
		
		@Override
		public String getPresetName() {
			return "javi";
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
			BulletData bullet = new BulletData("bullet", new Vec3(0, 0, 1), 
					200, 1000, 1, true, 10, 6, 0.5f);
			ws.addWeapon(bullet, false);
			bullet.setCurrentAmmo(bullet.getMaxAmmo());
			// missile 1
			MissileData gbu = new MissileData("GBU", new Vec3(0, 0, 1),
					400, 8, 100, false, 100, 1.5d, 0, 
					true, true, true, 100d, 4f,
					TargetType.GROUND, GuidanceType.PITBULL,
					4.0f, 0.02d, 2.5d, -1);
			gbu.setCurrentAmmo(gbu.getMaxAmmo());
			ws.addWeapon(gbu, false);
			// missile 2
			MissileData fox3 = new MissileData("fox3", new Vec3(0, 0, 1),
					600, 4, 80, false, 100, 4d, 0, 
					true, true, false, 100d, 4f,
					TargetType.AIR, GuidanceType.PITBULL,
					2.0f, 0.05d, 4.0d, 90);
			fox3.setCurrentAmmo(fox3.getMaxAmmo());
			ws.addWeapon(fox3, false);
			// missile 3
			MissileData fox2 = new MissileData("fox2", new Vec3(0, 0, 1),
					200, 10, 40, false,1000, 2.5d, 0, 
					true, true, false, 100d, 4f,
					TargetType.AIR, GuidanceType.IR,
					4.0f, 0.03d, 3.0d, 80);
			fox2.setCurrentAmmo(fox2.getMaxAmmo());
			ws.addWeapon(fox2, false);
			return ws;
		}
		
		public RadarSystem getRadar() {
			RadarSystem rs = new RadarSystem();
			// radar air
			RadarData radar = new RadarData("radar_air", 1000, 70, 20);
			radar.setScanAircraft(true);
			radar.setScanPlayers(true);
			radar.setScanMobs(false);
			radar.setScanAir(true);
			radar.setScanGround(false);
			rs.addRadar(radar, false);
			// radar ground
			RadarData radar2 = new RadarData("radar_ground", 200, -1, 15);
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
		
		@Override
		public int getFlares() {
			return 100;
		}

		@Override
		public float getStealth() {
			return 1;
		}

		@Override
		public float getMaxPitch() {
			return 2;
		}

		@Override
		public float getMaxRoll() {
			return 8;
		}

		@Override
		public float getMaxYaw() {
			return 1;
		}

		@Override
		public float getThrottleUp() {
			return 0.02f;
		}

		@Override
		public float getThrottleDown() {
			return 0.05f;
		}

		@Override
		public float getIdleHeat() {
			return 4f;
		}

		@Override
		public float getEngineHeat() {
			return 8f;
		}

		@Override
		public float getAircraftWeight() {
			return 0.04f;
		}

		@Override
		public float getMaxThrust() {
			return 0.1f;
		}

		@Override
		public float getSurfaceArea() {
			return 1f;
		}
		
	}
	
	public static class JsonPreset extends AircraftPreset {
		
		private CompoundTag tag;
		
		public JsonPreset(String path) {
			tag = UtilParse.getComoundFromResource(path);
		}
		
		@Override
		public String getPresetName() {
			return tag.getString("name");
		}

		@Override
		public PartsManager getParts() {
			return new PartsManager(tag);
		}

		@Override
		public WeaponSystem getWeapons() {
			return new WeaponSystem(tag);
		}

		@Override
		public RadarSystem getRadar() {
			return new RadarSystem(tag);
		}

		@Override
		public float getHealth() {
			return tag.getFloat("health");
		}

		@Override
		public float getMaxHealth() {
			return tag.getFloat("max_health");
		}

		@Override
		public float getMaxSpeed() {
			return tag.getFloat("max_speed");
		}

		@Override
		public int getFlares() {
			return tag.getInt("flares");
		}

		@Override
		public float getStealth() {
			return tag.getFloat("stealth");
		}

		@Override
		public float getMaxPitch() {
			return tag.getFloat("maxpitch");
		}

		@Override
		public float getMaxRoll() {
			return tag.getFloat("maxroll");
		}

		@Override
		public float getMaxYaw() {
			return tag.getFloat("maxyaw");
		}

		@Override
		public float getThrottleUp() {
			return tag.getFloat("throttleup");
		}

		@Override
		public float getThrottleDown() {
			return tag.getFloat("throttledown");
		}

		@Override
		public float getIdleHeat() {
			return tag.getFloat("idleheat");
		}

		@Override
		public float getEngineHeat() {
			return tag.getFloat("engineheat");
		}

		@Override
		public float getAircraftWeight() {
			return tag.getFloat("weight");
		}

		@Override
		public float getMaxThrust() {
			return tag.getFloat("maxthrust");
		}

		@Override
		public float getSurfaceArea() {
			return tag.getFloat("surfacearea");
		}
		
	}
	
}
