package com.onewhohears.dscombat.data.weapon;

import java.util.List;

import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.entity.weapon.TrackEntityMissile;
import com.onewhohears.dscombat.util.UtilEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TrackMissileData extends MissileData {
	
	public static enum TargetType {
		AIR,
		GROUND,
		WATER
	}
	
	private final TargetType targetType;

	public TrackMissileData(CompoundTag tag) {
		super(tag);
		targetType = TargetType.values()[tag.getInt("targetType")];
	}
	
	@Override
	public CompoundTag write() {
		CompoundTag tag = super.write();
		tag.putInt("targetType", targetType.ordinal());
		return tag;
	}
	
	public TrackMissileData(FriendlyByteBuf buffer) {
		super(buffer);
		targetType = TargetType.values()[buffer.readInt()];
	}
	
	@Override
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
		buffer.writeInt(targetType.ordinal());
	}
	
	@Override
	public WeaponType getType() {
		return WeaponType.TRACK_MISSILE;
	}
	
	@Override
	public WeaponData copy() {
		return new TrackMissileData(this.write());
	}
	
	public TargetType getTargetType() {
		return targetType;
	}
	
	@Override
	public EntityWeapon getEntity(Level level, Entity owner) {
		return new TrackEntityMissile(level, owner, this);
	}
	
	@Override
	public EntityWeapon getShootEntity(Level level, Entity owner, Vec3 direction, EntityAircraft vehicle) {
		TrackEntityMissile missile = (TrackEntityMissile) super.getShootEntity(level, owner, direction, vehicle);
		if (missile == null) return null;
		RadarSystem radar = vehicle.radarSystem;
		if (!radar.hasRadar()) {
			this.setLaunchFail("dscombat.no_radar");
			return null;
		}
		Entity target = radar.getSelectedTarget(level);
		if (target == null) {
			this.setLaunchFail("dscombat.no_target_selected");
			return null;
		}
		boolean groundWater = UtilEntity.isOnGroundOrWater(target);
		if (targetType == TargetType.AIR && groundWater) {
			this.setLaunchFail("dscombat.air_target_only");
			return null;
		} else if (targetType == TargetType.GROUND && !groundWater) {
			this.setLaunchFail("dscombat.ground_target_only");
			return null;
		}
		missile.target = target;
		return missile;
	}
	
	@Override
	public List<ComponentColor> getInfoComponents() {
		List<ComponentColor> list = super.getInfoComponents();
		switch(getTargetType()) {
		case AIR:
			list.add(2, new ComponentColor(Component.literal("TARGETS FLYING"), 0xaaaa00));
			break;
		case GROUND:
			list.add(2, new ComponentColor(Component.literal("TARGETS GROUNDED"), 0xaaaa00));
			break;
		case WATER:
			list.add(2, new ComponentColor(Component.literal("TARGETS IN WATER"), 0xaaaa00));
			break;
		}
		list.add(3, new ComponentColor(Component.literal("SELF GUIDED"), 0xaaaa00));
		return list;
	}

}
