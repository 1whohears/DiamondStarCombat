package com.onewhohears.dscombat.data.weapon;

import com.onewhohears.dscombat.data.weapon.stats.*;
import com.onewhohears.dscombat.init.ModEntities;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetStats;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;
import net.minecraft.world.entity.EntityType;

public abstract class WeaponType extends JsonPresetType {
	public static final None NONE = None.INSTANCE;
	public static class None extends WeaponType {
		public static final String ID = "none";
		public static final None INSTANCE = new None();
		public None() {
			super(ID, (key, data) -> NoWeaponStats.get());
		}
		@Override
		public EntityType<?> getDefaultEntityType() {
			return ModEntities.BULLET.get();
		}
	}
	public static final Bullet BULLET = Bullet.INSTANCE;
	public static class Bullet extends WeaponType {
		public static final String ID = "bullet";
		public static final Bullet INSTANCE = new Bullet();
		public Bullet() {
			super(ID, (key, data) -> new BulletStats(key, data));
		}
		@Override
		public EntityType<?> getDefaultEntityType() {
			return ModEntities.BULLET.get();
		}
	}
	public static final Bomb BOMB = Bomb.INSTANCE;
	public static class Bomb extends WeaponType {
		public static final String ID = "bomb";
		public static final Bomb INSTANCE = new Bomb();
		public Bomb() {
			super(ID, (key, data) -> new BombStats(key, data));
		}
		@Override
		public EntityType<?> getDefaultEntityType() {
			return ModEntities.BOMB.get();
		}
	}
	public static final BunkerBuster BUNKER_BUSTER = BunkerBuster.INSTANCE;
	public static class BunkerBuster extends WeaponType {
		public static final String ID = "bunker_buster";
		public static final BunkerBuster INSTANCE = new BunkerBuster();
		public BunkerBuster() {
			super(ID, (key, data) -> new BunkerBusterStats(key, data));
		}
		@Override
		public EntityType<?> getDefaultEntityType() {
			return ModEntities.BUNKER_BUSTER.get();
		}
	}
	public static final IrMissile IR_MISSILE = IrMissile.INSTANCE;
	public static class IrMissile extends WeaponType {
		public static final String ID = "ir_missile";
		public static final IrMissile INSTANCE = new IrMissile();
		public IrMissile() {
			super(ID, (key, data) -> new IRMissileStats(key, data));
		}
		@Override
		public EntityType<?> getDefaultEntityType() {
			return ModEntities.IR_MISSILE.get();
		}
	}
	public static final DumbTorpedo DUMB_TORPEDO = DumbTorpedo.INSTANCE;
	public static class DumbTorpedo extends WeaponType {
		public static final String ID = "dumb_torpedo";
		public static final DumbTorpedo INSTANCE = new DumbTorpedo();
		public DumbTorpedo() {
			super(ID, (key, data) -> new DumbTorpedoStats(key, data));
		}
		@Override
		public EntityType<?> getDefaultEntityType() {
			return ModEntities.DUMB_TORPEDO_MISSILE.get();
		}
	}
	public static final PosMissile POS_MISSILE = PosMissile.INSTANCE;
	public static class PosMissile extends WeaponType {
		public static final String ID = "pos_missile";
		public static final PosMissile INSTANCE = new PosMissile();
		public PosMissile() {
			super(ID, (key, data) -> new PosMissileStats(key, data));
		}
		@Override
		public EntityType<?> getDefaultEntityType() {
			return ModEntities.POS_MISSILE.get();
		}
	}
	public static final TrackMissile TRACK_MISSILE = TrackMissile.INSTANCE;
	public static class TrackMissile extends WeaponType {
		public static final String ID = "track_missile";
		public static final TrackMissile INSTANCE = new TrackMissile();
		public TrackMissile() {
			super(ID, (key, data) -> new TrackMissileStats(key, data));
		}
		@Override
		public EntityType<?> getDefaultEntityType() {
			return ModEntities.TRACK_MISSILE.get();
		}
	}
	public static final Torpedo TORPEDO = Torpedo.INSTANCE;
	public static class Torpedo extends WeaponType {
		public static final String ID = "torpedo";
		public static final Torpedo INSTANCE = new Torpedo();
		public Torpedo() {
			super(ID, (key, data) -> new TorpedoStats(key, data));
		}
		@Override
		public EntityType<?> getDefaultEntityType() {
			return ModEntities.TORPEDO_MISSILE.get();
		}
	}
	public static final AntiRadarMissile ANTI_RADAR_MISSILE = AntiRadarMissile.INSTANCE;
	public static class AntiRadarMissile extends WeaponType {
		public static final String ID = "anti_radar_missile";
		public static final AntiRadarMissile INSTANCE = new AntiRadarMissile();
		public AntiRadarMissile() {
			super(ID, (key, data) -> new AntiRadarMissileStats(key, data));
		}
		@Override
		public EntityType<?> getDefaultEntityType() {
			return ModEntities.ANTI_RADAR_MISSILE.get();
		}
	}
	public WeaponType(String id, JsonPresetStatsFactory<? extends JsonPresetStats> statsFactory) {
		super(id, statsFactory);
	}
	public abstract EntityType<?> getDefaultEntityType();
}
