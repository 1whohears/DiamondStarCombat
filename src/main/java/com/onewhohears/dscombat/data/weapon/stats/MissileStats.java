package com.onewhohears.dscombat.data.weapon.stats;

import java.util.List;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.weapon.AbstractWeaponBuilders;
import com.onewhohears.dscombat.data.weapon.WeaponType;
import com.onewhohears.dscombat.util.UtilMCText;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

public abstract class MissileStats extends BulletStats {
	
	public static class Builder extends AbstractWeaponBuilders.MissileBuilder<Builder> {
		protected Builder(String namespace, String name, WeaponType type) {
			super(namespace, name, type);
		}
		public static Builder posMissileBuilder(String namespace, String name) {
			return new Builder(namespace, name, WeaponType.POS_MISSILE);
		}
		public static Builder irMissileBuilder(String namespace, String name) {
			return new Builder(namespace, name, WeaponType.IR_MISSILE);
		}
		public static Builder trackMissileBuilder(String namespace, String name) {
			return new Builder(namespace, name, WeaponType.TRACK_MISSILE);
		}
		public static Builder torpedoBuilder(String namespace, String name) {
			return new Builder(namespace, name, WeaponType.TORPEDO);
		}
		public static Builder antiRadarMissileBuilder(String namespace, String name) {
			return new Builder(namespace, name, WeaponType.ANTI_RADAR_MISSILE);
		}
	}
	
	private final float turnRadius;
	private final double acceleration;
	private final double fuseDist;
	private final float fov;
	private final double bleed;
	private final int fuelTicks;
	private final int seeThroWater;
	private final int seeThroBlock;
	
	public MissileStats(ResourceLocation key, JsonObject json, WeaponType type) {
		super(key, json, type);
		turnRadius = json.get("turnRadius").getAsFloat();
		acceleration = json.get("acceleration").getAsDouble();
		fuseDist = json.get("fuseDist").getAsDouble();
		fov = json.get("fov").getAsFloat();
		bleed = json.get("bleed").getAsDouble();
		fuelTicks = json.get("fuelTicks").getAsInt();
		seeThroWater = UtilParse.getIntSafe(json, "seeThroWater", 0);
		seeThroBlock = UtilParse.getIntSafe(json, "seeThroBlock", 0);
	}

	public float getTurnRadius() {
		return turnRadius;
	}

	public double getAcceleration() {
		return acceleration;
	}

	public double getFuseDist() {
		return fuseDist;
	}
	
	public float getFov() {
		return fov;
	}
	
	public double getBleed() {
		return bleed;
	}
	
	public int getFuelTicks() {
		return fuelTicks;
	}
	
	public int getSeeThroWater() {
		return seeThroWater;
	}
	
	public int getSeeThroBlock() {
		return seeThroBlock;
	}
	
	@Override
	public double getMobTurretRange() {
		return Math.min(2000, getSpeed() * getMaxAge() * 0.9);
	}
	
	@Override
	public void addToolTips(List<Component> tips, boolean advanced) {
		super.addToolTips(tips, advanced);
		if (advanced) {
			if (getFov() != -1) tips.add(UtilMCText.literal("FOV: ").append(getFov()+"").setStyle(Style.EMPTY.withColor(INFO_COLOR)));
			tips.add(UtilMCText.literal("Turn Radius: ").append(getTurnRadius()+"").setStyle(Style.EMPTY.withColor(INFO_COLOR)));
			tips.add(UtilMCText.literal("Acceleration: ").append(getAcceleration()+"").setStyle(Style.EMPTY.withColor(INFO_COLOR)));
		}
	}

}
