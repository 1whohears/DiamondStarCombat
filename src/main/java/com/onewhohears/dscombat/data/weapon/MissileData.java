package com.onewhohears.dscombat.data.weapon;

import java.util.List;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.entity.weapon.EntityMissile;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
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
	public EntityWeapon getShootEntity(Level level, Entity owner, Vec3 pos, Vec3 direction, @Nullable EntityVehicle vehicle, boolean ignoreRecoil) {
		EntityMissile missile = (EntityMissile) super.getShootEntity(level, owner, pos, direction, vehicle, ignoreRecoil);
		if (missile == null) return null;
		if (vehicle != null) {
			missile.setPos(missile.position().add(vehicle.getDeltaMovement()));
			missile.setDeltaMovement(vehicle.getDeltaMovement());
		} else {
			missile.setDeltaMovement(direction.scale(1.0));
		}
		return missile;
	}
	
	@Override
	public void addToolTips(List<Component> tips) {
		super.addToolTips(tips);
		if (getFov() != -1) tips.add(Component.literal("FOV: ").append(getFov()+"").setStyle(Style.EMPTY.withColor(0xAAAAAA)));
		tips.add(Component.literal("Turn Radius: ").append(getTurnRadius()+"").setStyle(Style.EMPTY.withColor(0xAAAAAA)));
	}
	
	@Override
	public List<ComponentColor> getInfoComponents() {
		List<ComponentColor> list = super.getInfoComponents();
		if (getFov() != -1) list.add(new ComponentColor(Component.literal("FOV: ").append(getFov()+""), 0x040404));
		list.add(new ComponentColor(Component.literal("Turn Radius: ").append(getTurnRadius()+""), 0x040404));
		list.add(new ComponentColor(Component.literal("Acceleration: ").append(getAcceleration()+""), 0x040404));
		return list;
	}

}
