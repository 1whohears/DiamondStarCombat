package com.onewhohears.dscombat.entity.parts;

import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.command.DSCGameRules;
import com.onewhohears.dscombat.data.parts.PartData.PartType;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle.AircraftType;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;

public class EntitySeat extends EntityPart {
	
	public final Vec3 passengerOffset;
	
	public EntitySeat(EntityType<?> type, Level level, Vec3 offset) {
		super(type, level);
		this.passengerOffset = offset;
	}
	
	public void tick() {
		super.tick();
		if (!level.isClientSide && level.getGameRules().getBoolean(DSCGameRules.MOBS_RIDE_VEHICLES)) 
			tickRideCollision();
	}
	
	protected void tickRideCollision() {
		if (isPilotSeat()) return;
		if (getPassenger() != null) return;
		if (!(getVehicle() instanceof EntityVehicle vehicle)) return;
		if (vehicle.getXZSpeed() > 0.1) return;
		List<Entity> entities = level.getEntities(this, 
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
		} else if (!level.isClientSide) {
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
    public void positionRider(Entity passenger) {
		if (!(getVehicle() instanceof EntityVehicle craft)) {
			super.positionRider(passenger);
			return;
		}
		if (tickCount % 20 != 0 && passenger instanceof Player player) {
			if (craft.nightVisionHud) {
				player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 
						80, 0, false, false));
			}	
			if (craft.getAircraftType() == AircraftType.SUBMARINE) {
				player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 
						80, 0, false, false));
				player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 
						80, 0, false, false));
			}
		}
		passenger.setPos(position().add(getPassengerRelPos(passenger, craft)));
	}
	
	protected Vec3 getPassengerRelPos(Entity passenger, EntityVehicle craft) {
		Quaternion q = craft.getQBySide();
		double offset = getPassengersRidingOffset() + passenger.getMyRidingOffset() + passenger.getEyeHeight();
		return UtilAngles.rotateVector(new Vec3(0, offset, 0), q)
				.subtract(0, passenger.getEyeHeight(), 0);
	}
	
	@Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
	}
	
	@Override
    public boolean canAddPassenger(Entity passenger) {
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
		if (level.isClientSide) return;
		EntityVehicle vehicle = getParentVehicle();
		if (vehicle == null) return;
		vehicle.onSeatDismount(passenger);
	}
	
	@Override
    public Vec3 getDismountLocationForPassenger(LivingEntity entity) {
		return super.getDismountLocationForPassenger(entity);
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
    public Entity getControllingPassenger() {
		Player p = getPlayer();
		if (p == null) return super.getControllingPassenger();
		return p;
    }
	
	public boolean isPlayerOrBotRiding() {
		if (getPlayer() != null) return true;
		if (hasAIUsingTurret()) return true;
		return false;
	}
	
	public boolean hasAIUsingTurret() {
		return false;
	}

    @Override
    public boolean canBeRiddenUnderFluidType(FluidType type, Entity rider) {
        return true;
    }
    
    @Override
    public double getPassengersRidingOffset() {
        return passengerOffset.y;
    }

	@Override
	public boolean shouldRender() {
		return false;
	}
	
	@Override
    public boolean hurt(DamageSource source, float amount) {
		if (source.isExplosion() || source.isMagic()) return true;
		Entity v = getVehicle();
		if (v == null) return false;
		v.hurt(source, amount);
		return true;
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
		// TODO 4.2 option to change turret camera position. so camera could be under the aircraft
		return 0;
	}
	
	@Override
	public boolean isSeat() {
		return true;
	}

}
