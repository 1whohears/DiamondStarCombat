package com.onewhohears.dscombat.entity.ai.goal;

import com.onewhohears.dscombat.command.DSCGameRules;
import com.onewhohears.dscombat.data.weapon.instance.WeaponInstance;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.util.UtilVehicleEntity;
import com.onewhohears.onewholibs.common.core.DistantVisibleManager;
import com.onewhohears.onewholibs.util.UtilEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

import static com.onewhohears.dscombat.entity.ai.goal.TurretShootGoal.LOGGER;
import static com.onewhohears.dscombat.entity.ai.goal.TurretShootGoal.debugTurretAI;

public class VehicleTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

	public static VehicleTargetGoal<Player> targetPlayers(Mob mob, EntityVehicle vehicle) {
		double range = vehicle.getAIHorizontalRange();
		return new VehicleTargetGoal<>(mob, vehicle, Player.class, range,
				checkCanTarget(mob, vehicle, false));
	}

	public static VehicleTargetGoal<LivingEntity> targetEnemy(Mob mob, EntityVehicle vehicle) {
		double range = vehicle.getAIHorizontalRange();
		return new VehicleTargetGoal<>(mob, vehicle, LivingEntity.class, range,
				checkCanTarget(mob, vehicle, true));
	}

	public static Predicate<LivingEntity> checkCanTarget(Mob mob, EntityVehicle vehicle, boolean enemyCheck) {
		return entity -> {
			if (debugTurretAI()) LOGGER.info("vehicle {} checking {}", vehicle, entity);
			WeaponInstance<?> wd = vehicle.weaponSystem.getSelected();
			if (wd == null) {
				if (debugTurretAI()) LOGGER.info("FAIL weapon instance null");
				return false;
			}
			if (entity == null) {
				if (debugTurretAI()) LOGGER.info("FAIL entity null");
				return false;
			}
			if (entity.isRemoved()) {
				if (debugTurretAI()) LOGGER.info("FAIL entity removed");
				return false;
			}
			if (entity.isDeadOrDying()) {
				if (debugTurretAI()) LOGGER.info("FAIL entity dead or dying");
				return false;
			}
			if (entity.isSpectator()) {
				if (debugTurretAI()) LOGGER.info("FAIL entity is spectator");
				return false;
			}
			if (entity.position().subtract(vehicle.position()).horizontalDistance() > vehicle.getAIHorizontalRange()) {
				if (debugTurretAI()) LOGGER.info("FAIL entity outside of range {}", vehicle.getAIHorizontalRange());
				return false;
			}
			if (entity.isAlliedTo(mob)) {
				if (debugTurretAI()) LOGGER.info("FAIL entity is allied");
				return false;
			}
			if (mob.isPassengerOfSameVehicle(entity)) {
				if (debugTurretAI()) LOGGER.info("FAIL entity is passenger of the same vehicle of the mob");
				return false;
			}
			boolean isPlayer = false;
			if (entity instanceof Player player) {
				isPlayer = true;
				if (player.isCreative()) {
					if (debugTurretAI()) LOGGER.info("FAIL player is creative");
					return false;
				}
			}
			if (enemyCheck) {
				if (mob.getTeam() == null && isPlayer) {
					if (debugTurretAI()) LOGGER.info("FAIL team null and is player");
					return false;
				}
				if (!(entity instanceof Enemy)) {
					if (debugTurretAI()) LOGGER.info("FAIL not an enemy");
					return false;
				}
			}
			if (wd.getStats().isIRMissile()) {
				if (UtilVehicleEntity.isOnGroundOrWater(entity)) return false;
			} else if (wd.getStats().requiresRadar()) {
                if (!vehicle.radarSystem.hasTarget(entity)) {
					if (debugTurretAI()) LOGGER.info("FAIL radar doesn't have target");
					return false;
				}
			}
			return true;
		};
	}

    private static final int REQUEST_ID = 0x2403;
    public final DistantVisibleManager.VisibleRequestData TURRET_AI_HANDLER = new DistantVisibleManager.VisibleRequestData(
            REQUEST_ID, 40, 20, event -> {
        if (!event.result().computeComplete || event.result().failed) {
            DistantVisibleManager.cancelFirstEntityQuery(event.data().entityId1, event.data().entityId2, REQUEST_ID);
            if (debugTurretAI()) LOGGER.info("FAIL raycast failed");
            this.stop();
            return;
        }
        if (event.result().passed) {
            if (debugTurretAI()) LOGGER.info("PASS can see target");
            setCanSeeTarget();
        } else {
            if (debugTurretAI()) LOGGER.info("FAIL can't see target");
            this.stop();
        }
    });

    private final EntityVehicle vehicle;
	private final Predicate<LivingEntity> check;
	private final double range;

	@Override
	protected void findTarget() {
		if (debugTurretAI()) LOGGER.info("find target {}", mob);
		WeaponInstance<?> wd = vehicle.weaponSystem.getSelected();
		if (wd == null) return;
		if (wd.getStats().requiresRadar()) {
			if (targetType != Player.class && targetType != ServerPlayer.class)
				target = vehicle.radarSystem.getLivingTargetByWeapon(wd);
			else target = vehicle.radarSystem.getPlayerTargetByWeapon(wd);
		} else {
            Level level = UtilEntity.getLevel(mob);
            if (targetType != Player.class && targetType != ServerPlayer.class)
                target = level.getNearestEntity(level.getEntitiesOfClass(targetType,
                                getTargetSearchArea(getFollowDistance()), (entity) -> true), targetConditions,
                        mob, mob.getX(), mob.getEyeY(), mob.getZ());
            else target = level.getNearestPlayer(targetConditions,
                    mob, mob.getX(), mob.getEyeY(), mob.getZ());
        }
        if (debugTurretAI()) LOGGER.info("target = {}", target);
        Level level = UtilEntity.getLevel(mob);
        if (target != null && !level.isClientSide() && level.getServer() != null) {
            DistantVisibleManager.queryVisible(level.getServer(), vehicle, target, TURRET_AI_HANDLER);
        }
	}

	@Override
	public boolean canUse() {
		if (debugTurretAI()) LOGGER.info("canUse? {}", mob);
		if (!UtilEntity.getLevel(mob).getGameRules().getBoolean(DSCGameRules.MOBS_USE_TURRETS)) return false;
		if (mob.getVehicle() == null) return false;
		return super.canUse();
	}

	@Override
	public boolean canContinueToUse() {
		LivingEntity living = mob.getTarget();
		if (living == null) living = target;
		if (living == null) return false;
		if (mob.isDeadOrDying()) return false;
        Entity mobRoot = mob.getRootVehicle();
		if (!check.test(living)) {
            DistantVisibleManager.cancelFirstEntityQuery(mobRoot.getId(), living.getId(), REQUEST_ID);
			if (debugTurretAI()) LOGGER.info("cant continue to use {} {}", mob, mob.getTarget());
			return false;
		}
        Level level = UtilEntity.getLevel(mob);
        if (!level.isClientSide() && level.getServer() != null) {
            DistantVisibleManager.queryVisible(level.getServer(), mobRoot, living, TURRET_AI_HANDLER);
        }
		target = living;
		if (debugTurretAI()) LOGGER.info("canContinueToUse {} {}", mob, mob.getTarget());
        return true;
	}

    @Override
    public void start() {
        super.start();
        mob.setTarget(null); // wait for the visibility check to finish
    }

	@Override
	public void stop() {
		mob.setTarget(null);
		target = null;
	}

    public void setCanSeeTarget() {
        mob.setTarget(target);
    }

	private VehicleTargetGoal(Mob mob, EntityVehicle vehicle,
                              Class<T> type, double range, Predicate<LivingEntity> check) {
		super(mob, type, 8, false, false, check);
		this.vehicle = vehicle;
		this.range = range;
		this.check = check;
		this.targetConditions.ignoreLineOfSight(); // vanilla line of sight has a limit of 128 blocks
	}
	
	@Override
	protected @NotNull AABB getTargetSearchArea(double targetDistance) {
		return mob.getBoundingBox().inflate(getFollowDistance(), getVerticalRange(), getFollowDistance());
	}
	
	public double getVerticalRange() {
		return vehicle.getAIVerticalRange();
	}
	
	@Override
	protected double getFollowDistance() {
		return range;
	}

}
