package com.onewhohears.dscombat.entity.parts;

import com.onewhohears.dscombat.client.event.ClientInputEventHandlers;
import com.onewhohears.dscombat.command.DSCGameRules;
import com.onewhohears.dscombat.data.parts.PartType;
import com.onewhohears.dscombat.data.parts.instance.SeatInstance;
import com.onewhohears.dscombat.data.parts.stats.SeatStats;
import com.onewhohears.dscombat.entity.ai.goal.VehicleTargetGoal;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.ModTags;
import com.onewhohears.onewholibs.util.UtilEntity;
import com.onewhohears.onewholibs.util.math.QuaternionF;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public abstract class EntityRidablePart<P extends SeatStats, I extends SeatInstance<P>> extends EntityPart<P,I> {

    protected boolean addedGoals = false;
    protected Goal shootGoal, targetGoal;

	public EntityRidablePart(EntityType<?> type, Level level, String defaultPreset) {
		super(type, level, defaultPreset);
	}
	
	public void tick() {
		super.tick();
		if (!isClientSide() && getWorld().getGameRules().getBoolean(DSCGameRules.MOBS_RIDE_VEHICLES)) {
            tickRideCollision();
            if (canAIControl() && getPassenger() instanceof Mob mob && !addedGoals && mob.tickCount > 10) {
                addRiderAI(mob);
            }
        }
	}

    @Override
    public void simulatedTick() {
        super.simulatedTick();
        if (addedGoals && getPassenger() instanceof Mob mob) {
            mob.goalSelector.tick();
            mob.targetSelector.tick();
        }
    }

    protected void addRiderAI(Mob mob) {
        if (canAIControl() && mob.tickCount > 10) {
            shootGoal = makeShootGoal(mob);
            if (shootGoal != null) mob.goalSelector.addGoal(0, shootGoal);
            if (mob.getType().is(ModTags.EntityTypes.TURRET_TARGET_PLAYERS)) {
                targetGoal = makeTargetPlayerGoal(mob);
            } else if (mob.getType().is(ModTags.EntityTypes.TURRET_TARGET_MONSTERS)) {
                targetGoal = makeTargetEnemyGoal(mob);
            }
            if (targetGoal != null) mob.targetSelector.addGoal(0, targetGoal);
            addedGoals = true;
        }
    }

    protected void removeRiderAI(Mob mob) {
        if (shootGoal != null) {
            mob.goalSelector.removeGoal(shootGoal);
            shootGoal = null;
        }
        if (targetGoal != null) {
            mob.targetSelector.removeGoal(targetGoal);
            targetGoal = null;
        }
        addedGoals = false;
    }

    protected Goal makeShootGoal(Mob mob) {
        return null;
    }

    protected Goal makeTargetPlayerGoal(Mob mob) {
        EntityVehicle vehicle = getParentVehicle();
        if (vehicle == null) return null;
        return VehicleTargetGoal.targetPlayers(mob, vehicle);
    }

    protected Goal makeTargetEnemyGoal(Mob mob) {
        EntityVehicle vehicle = getParentVehicle();
        if (vehicle == null) return null;
        return VehicleTargetGoal.targetEnemy(mob, vehicle);
    }

    public boolean canAIControl() {
        return isPilotSeat() || isCoPilotSeat();
    }
	
	protected void tickRideCollision() {
		if (getPassenger() != null) return;
		if (!(getVehicle() instanceof EntityVehicle vehicle)) return;
		if (vehicle.getXZSpeed() > 0.1) return;
		List<Entity> entities = getWorld().getEntities(this,
			getBoundingBox().inflate(0.1), 
			getRidePredicate());
		for (Entity entity : entities) 
			if (entity.startRiding(this)) 
				return;
	}
	
	protected Predicate<? super Entity> getRidePredicate() {
		return ((entity) -> {
			if (this.equals(entity.getRootVehicle())) return false;
			if (entity.isSpectator()) return false;
			if (!(entity instanceof Mob)) return false;
			return true;
		});
	}
	
	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		if (player.isSecondaryUseActive()) {
			return InteractionResult.PASS;
		} else if (!isClientSide()) {
			if (player.isPassenger()) return InteractionResult.PASS;
			if (player.startRiding(this)) return InteractionResult.CONSUME;
			if (getVehicle() != null && player.startRiding(getVehicle())) return InteractionResult.CONSUME;
			return InteractionResult.PASS;
		}
		return InteractionResult.SUCCESS;
	}
	
	@Override
	public boolean isPickable() {
		return true;
	}
	
	@Override
    public void positionRider(Entity passenger, MoveFunction moveFunction) {
		if (!(getVehicle() instanceof EntityVehicle craft)) {
			super.positionRider(passenger, moveFunction);
			return;
		}
		if (tickCount % 20 == 0 && passenger instanceof Player player) {
			if (craft.nightVisionHud) {
				player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 
						240, 0, false, false));
			}	
			if (craft.getStats().isSub()) {
				player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING,
                        240, 0, false, false));
				player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION,
                        240, 0, false, false));
			}
		}
        Vec3 pos = position().add(getPassengerRelPos(passenger, craft));
        moveFunction.accept(passenger, pos.x, pos.y, pos.z);
	}
	
	protected Vec3 getPassengerRelPos(Entity passenger, EntityVehicle craft) {
		QuaternionF q = craft.getQBySide();
		double offset = getPassengersRidingOffset() + passenger.getMyRidingOffset() + passenger.getEyeHeight();
		return UtilAngles.rotateVector(new Vec3(0, offset, 0), q)
				.subtract(0, passenger.getEyeHeight(), 0);
	}
	
	@Override
    protected void addPassenger(Entity passenger) {
        if (!isClientSide()) {
			EntityVehicle vehicle = getParentVehicle();
			if (vehicle != null && !vehicle.hasOwner()) {
				vehicle.setOwner(passenger);
			}
		} else {
            ClientInputEventHandlers.onEntityMountVehicle(passenger);
        }
		super.addPassenger(passenger);
        if (passenger instanceof Mob m) addRiderAI(m);
	}
	
	@Override
    public boolean canAddPassenger(Entity passenger) {
		EntityVehicle vehicle = getParentVehicle();
		if (vehicle == null) return false;
		if (!vehicle.hasPermission(passenger)) return false;
		if (passenger instanceof LivingEntity) return getPassenger() == null;
		return false;
	}
	
	@Override
    protected boolean canRide(Entity entityIn) {
		return entityIn instanceof EntityVehicle;
    }
	
	@Override
	protected void removePassenger(Entity passenger) {
		super.removePassenger(passenger);
		if (isClientSide()) return;
		EntityVehicle vehicle = getParentVehicle();
		if (vehicle == null) return;
		vehicle.onSeatDismount(passenger);
        if (passenger instanceof Mob m) removeRiderAI(m);
	}
	
	@Override
    public @NotNull Vec3 getDismountLocationForPassenger(LivingEntity entity) {
		int minY = getWorld().getMinBuildHeight()+4;
        EntityHitResult ehr = UtilEntity.getEntityHitResultAtClip(getWorld(), entity,
                position().add(0, 10, 0),
                position(),
                entity.getBoundingBox(),
                e -> !e.equals(entity),
                0.3f);
        Vec3 dis;
        if (ehr != null) dis = ehr.getLocation().add(0, 0.2, 0);
        else dis = super.getDismountLocationForPassenger(entity);
		if (dis.y() < minY) dis = new Vec3(dis.x(), minY, dis.z());
		return dis;
	}
	
	@Nullable
	public Player getPlayer() {
		List<Entity> list = getPassengers();
		for (Entity e : list) if (e instanceof Player p) return p;
		return null;
	}
	
	@Nullable
	public LivingEntity getPassenger() {
		List<Entity> list = getPassengers();
		for (Entity e : list) if (e instanceof LivingEntity l) return l;
		return null;
	}
	
	@Nullable
	@Override
    public LivingEntity getControllingPassenger() {
		Player p = getPlayer();
		if (p == null) return super.getControllingPassenger();
		return p;
    }
	
	public boolean isPlayerOrBotRiding() {
		if (getPlayer() != null) return true;
		if (hasAIUsingTurret()) return true;
		return false;
	}

    public void explode(DamageSource source, Entity parent) {
        getWorld().explode(parent, source, null, getX(), getY(), getZ(),
                3, true, Level.ExplosionInteraction.TNT);
    }
	
	public boolean hasAIUsingTurret() {
		return false;
	}
    
    @Override
    public double getPassengersRidingOffset() {
        return getPassengerOffsets().y;
    }

	@Override
	public boolean shouldRender() {
		return false;
	}

	@Override
	public PartType getPartType() {
		return PartType.SEAT;
	}
	
	@Override
	public boolean fireImmune() {
		return true;
	}

	@Override
	public boolean canGetHurt() {
		return false;
	}
	
	public float getCameraYOffset() {
		// TODO 4.2 option to change turret camera position. so camera could be under the vehicle
		return 0;
	}
	
	@Override
	public boolean isSeat() {
		return true;
	}
	// set to false so players can't melee attack the seats
	@Override
	public boolean isAttackable() {
		return false;
	}
	// set to false so projectiles go through seat hitboxes
	@Override
	public boolean isAlive() {
		return false;
	}

	public boolean canEject() {
		SeatInstance<?> data = getPartInstance();
		if (data == null) return false;
		return data.canEject();
	}

	public void useEject() {
		SeatInstance<?> data = getPartInstance();
		if (data == null) return;
		data.setCanEject(false);
	}

	public Vec3 getPassengerOffsets() {
		return getStats().getPassengerOffsets();
	}

}
