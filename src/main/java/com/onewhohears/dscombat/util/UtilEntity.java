package com.onewhohears.dscombat.util;

import com.onewhohears.dscombat.data.weapon.RadarTargetTypes;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;

public class UtilEntity {
	
	public static boolean canPosSeeEntity(Vec3 pos, Entity entity, int maxBlockCheckDepth, double throWater, double throBlock) {
		Level level = entity.getLevel();
		Vec3 diff = entity.getEyePosition().subtract(pos);
		Vec3 look = diff.normalize();
		double distance = diff.length();
		double[] through = new double[] {throWater, throBlock};
		if (distance <= maxBlockCheckDepth) {
			if (!checkBlocksByRange(level, pos, look, (int)distance, through)) return false;
		} else {
			int maxCheckDist = maxBlockCheckDepth / 2;
			if (!checkBlocksByRange(level, pos, look, maxCheckDist, through)) return false;
			if (!checkBlocksByRange(level, 
				pos.add(look.scale(distance-maxCheckDist).subtract(look)), 
				look, maxCheckDist, through)) return false;
		}
		return true;
	}
	
	public static boolean canEntitySeeEntity(Entity e1, Entity e2, int maxBlockCheckDepth) {
		return canEntitySeeEntity(e1, e2, maxBlockCheckDepth, 0, 0);
	}
	
	public static boolean canEntitySeeEntity(Entity e1, Entity e2, int maxBlockCheckDepth, double throWater, double throBlock) {
		return canPosSeeEntity(e1.getEyePosition(), e2, maxBlockCheckDepth, throWater, throBlock);
	}
	
	private static boolean checkBlocksByRange(Level level, Vec3 pos, Vec3 look, int dist, double[] through) {
		int k = 0;
		while (k++ < dist) {
			pos = pos.add(look);
			BlockPos bp = new BlockPos(pos);
			ChunkPos cp = new ChunkPos(bp);
			if (!level.hasChunk(cp.x, cp.z)) continue;
			BlockState block = level.getBlockState(bp);
			if (block == null || block.isAir()) continue;
			if (!block.getMaterial().blocksMotion() && !block.getMaterial().isLiquid()) continue;
			if (through[0] <= 0 && through[1] <= 0) return false;
			if (block.getFluidState().getType().isSame(Fluids.WATER)) {
				if (through[0] > 0) {
					--through[0];
					continue;
				} else return false;
			}
			if (through[1] > 0) {
				--through[1];
				continue;
			} else return false;
		}
		return true;
	}
	
	public static double getCrossSectionalArea(Entity entity) {
		if (entity instanceof EntityVehicle plane) return plane.getCrossSectionArea();
		return RadarTargetTypes.get().getEntityCrossSectionalArea(
				entity.getType().toString(), 
				entity.getBbHeight()*entity.getBbWidth());
	}
	
	public static int getDistFromGround(Entity e) {
		Level l = e.getLevel();
		int[] pos = {e.getBlockX(), e.getBlockY(), e.getBlockZ()};
		int dist = 0;
		while (pos[1] >= -64) {
			BlockState block = l.getBlockState(new BlockPos(pos[0], pos[1], pos[2]));
			if (block != null && !block.isAir()) break;
			--pos[1];
			++dist;
		}
		return dist;
	}
	
	public static int getDistFromSeaLevel(Entity e) {
		DimensionType dt = e.level.dimensionType();
		return getDistFromSeaLevel(e.position().y, dt.natural());
	}
	
	public static int getDistFromSeaLevel(double yPos, boolean isNatural) {
		int sea;
		if (isNatural) sea = 64;
		else sea = 0;
		return (int)yPos - sea;
	}
	
	public static Vec3 getLookingAtBlockPos(Entity e, int max) {
		Level level = e.level;
		Vec3 look = e.getLookAngle();
		Vec3 pos = e.position();
		for (int i = 0; i < max; ++i) {
			BlockState block = level.getBlockState(new BlockPos(pos));
			if (block != null && !block.isAir()) return pos;
			pos = pos.add(look);
		}
		return pos.add(look);
	}
	
	/**
	 * @param posY
	 * @return between 0 (no air pressure) and 1
	 */
	public static double getAirPressure(Entity entity) {
		DimensionType dt = entity.level.dimensionType();
		double space, surface;
		if (dt.natural()) {
			space = 2500;
			surface = 64;
		} else {
			space = 2000;
			surface = 0;
		}
		double scale = 1, exp = 2;
		double posY = entity.getY();
		if (posY <= surface) return scale;
		if (posY > space) return 0;
		posY -= surface;
		return Math.pow(Math.abs(posY-space), exp) * Math.pow(space, -exp);
	}
	
	public static boolean isOnGroundOrWater(Entity entity) {
		if (entity.isPassenger()) {
			Entity rv = entity.getRootVehicle();
			if (rv instanceof Boat) return true;
			if (rv instanceof Minecart) return true;
			if (rv.isOnGround() || isHeadAboveWater(rv)) return true;
		}
		if (entity instanceof Player p && p.isFallFlying()) return false;
		if (entity instanceof Minecart) return true;
		if (!entity.isInWater() && entity.isSprinting() && entity.fallDistance < 1.15) return true;
		if (entity.isOnGround() || isHeadAboveWater(entity)) return true;
		return false;
	}
	
	public static boolean isHeadAboveWater(Entity entity) {
		return entity.isInWater() && !entity.isUnderWater();
	}
	
	public static void entityLookAtPos(Entity entity, Vec3 pos, float headTurnRate) {
		Vec3 diff = pos.subtract(entity.getEyePosition());
		float yRot = UtilAngles.getYaw(diff);
		float xRot = UtilAngles.getPitch(diff);
		float newYRot = UtilAngles.rotLerp(entity.getYRot(), yRot, headTurnRate);
		float newXRot = UtilAngles.rotLerp(entity.getXRot(), xRot, headTurnRate);
		entity.setYRot(newYRot);
		entity.setXRot(newXRot);
	}
	
	public static void mobLookAtPos(Mob mob, Vec3 pos, float headTurnRate) {
		mob.getLookControl().setLookAt(pos.x, pos.y, pos.z, headTurnRate, 360);
	}
	
	public static String[] getSplitEncodeId(Entity entity) {
		return entity.getEncodeId().split(":");
	}
	
}
