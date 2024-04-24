package com.onewhohears.dscombat.data.weapon;

import java.util.List;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.entity.weapon.EntityMissile;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.util.UtilMCText;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public abstract class MissileData extends BulletData {
	
	public static class Builder extends AbstractWeaponBuilders.MissileBuilder<Builder> {

		protected Builder(String namespace, String name, JsonPresetFactory<? extends MissileData> sup, WeaponType type) {
			super(namespace, name, sup, type);
		}
		
		public static Builder posMissileBuilder(String namespace, String name) {
			return new Builder(namespace, name, (key, json) -> new PosMissileData(key, json), WeaponType.POS_MISSILE);
		}
		
		public static Builder irMissileBuilder(String namespace, String name) {
			return new Builder(namespace, name, (key, json) -> new IRMissileData(key, json), WeaponType.IR_MISSILE);
		}
		
		public static Builder trackMissileBuilder(String namespace, String name) {
			return new Builder(namespace, name, (key, json) -> new TrackMissileData(key, json), WeaponType.TRACK_MISSILE);
		}
		
		public static Builder torpedoBuilder(String namespace, String name) {
			return new Builder(namespace, name, (key, json) -> new TorpedoData(key, json), WeaponType.TORPEDO);
		}
		
		public static Builder antiRadarMissileBuilder(String namespace, String name) {
			return new Builder(namespace, name, (key, json) -> new AntiRadarMissileData(key, json), WeaponType.ANTIRADAR_MISSILE);
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
	
	public MissileData(ResourceLocation key, JsonObject json) {
		super(key, json);
		turnRadius = json.get("turnRadius").getAsFloat();
		acceleration = json.get("acceleration").getAsDouble();
		fuseDist = json.get("fuseDist").getAsDouble();
		fov = json.get("fov").getAsFloat();
		bleed = json.get("bleed").getAsDouble();
		fuelTicks = json.get("fuelTicks").getAsInt();
		seeThroWater = UtilParse.getIntSafe(json, "seeThroWater", 0);
		seeThroBlock = UtilParse.getIntSafe(json, "seeThroBlock", 0);
	}
	
	@Override
	public void readNBT(CompoundTag tag) {
		super.readNBT(tag);
	}
	
	@Override
	public CompoundTag writeNbt() {
		CompoundTag tag = super.writeNbt();
		return tag;
	}
	
	@Override
	public void readBuffer(FriendlyByteBuf buffer) {
		super.readBuffer(buffer);
	}
	
	@Override
	public void writeBuffer(FriendlyByteBuf buffer) {
		super.writeBuffer(buffer);
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
	public EntityWeapon getShootEntity(WeaponShootParameters params) {
		EntityMissile missile = (EntityMissile) super.getShootEntity(params);
		if (missile == null) return null;
		if (params.vehicle != null) {
			missile.setPos(missile.position().add(params.vehicle.getDeltaMovement()));
			Vec3 move = params.vehicle.getDeltaMovement();
			if (params.isTurret) move = move.add(params.direction.scale(0.5));
			missile.setDeltaMovement(move);
		} else {
			missile.setDeltaMovement(params.direction.scale(0.5));
		}
		return missile;
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
