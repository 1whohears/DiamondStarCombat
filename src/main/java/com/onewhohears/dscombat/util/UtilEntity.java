package com.onewhohears.dscombat.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.weapon.RadarTargetTypes;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class UtilEntity {
	
	public static boolean canPosSeeEntity(Vec3 pos, Entity entity, int maxBlockCheckDepth, double throWater, double throBlock) {
		Level level = entity.getLevel();
		Vec3 diff = entity.getBoundingBox().getCenter().subtract(pos);
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
		return canPosSeeEntity(e1.position(), e2, maxBlockCheckDepth, throWater, throBlock);
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
	
	private static Field explosion_radius_field = null;
	private static boolean tried_to_get_explosion_radius_field = false;
	
	@Nullable
	public static Field getExplosionRadiusField() {
		if (!tried_to_get_explosion_radius_field) {
			try {
				explosion_radius_field = Explosion.class.getDeclaredField("f_46017_");
				explosion_radius_field.setAccessible(true);
			} catch(Exception e) {
				try {
					explosion_radius_field = Explosion.class.getDeclaredField("radius");
					explosion_radius_field.setAccessible(true);
				} catch(Exception e2) {
					e.printStackTrace();
					e2.printStackTrace();
				}
			}
			tried_to_get_explosion_radius_field = true;
		}
		return explosion_radius_field;
	}
	
	private static Method on_hit_entity_method = null;
	private static boolean tried_on_hit_entity_method = false;
	
	@Nullable
	public static Method getOnHitEntityMethod() {
		if (!tried_on_hit_entity_method) {
			try {
				on_hit_entity_method = Projectile.class.getDeclaredMethod("m_5790_", EntityHitResult.class);
				on_hit_entity_method.setAccessible(true);
			} catch(Exception e) {
				try {
					on_hit_entity_method = Projectile.class.getDeclaredMethod("onHitEntity", EntityHitResult.class);
					on_hit_entity_method.setAccessible(true);
				} catch(Exception e2) {
					e.printStackTrace();
					e2.printStackTrace();
				}
			}
			tried_on_hit_entity_method = true;
		}
		return on_hit_entity_method;
	}
	
	private static Method can_hit_entity_method = null;
	private static boolean tried_can_hit_entity_method = false;
	
	@Nullable
	public static Method getCanHitEntityMethod() {
		if (!tried_can_hit_entity_method) {
			try {
				can_hit_entity_method = Projectile.class.getDeclaredMethod("m_5603_", Entity.class);
				can_hit_entity_method.setAccessible(true);
			} catch(Exception e) {
				try {
					can_hit_entity_method = Projectile.class.getDeclaredMethod("canHitEntity", Entity.class);
					can_hit_entity_method.setAccessible(true);
				} catch(Exception e2) {
					e.printStackTrace();
					e2.printStackTrace();
				}
			}
			tried_can_hit_entity_method = true;
		}
		return can_hit_entity_method;
	}
	
}
