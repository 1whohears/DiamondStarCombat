package com.onewhohears.dscombat.entity.parts;

import java.util.List;

import javax.annotation.Nullable;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.data.parts.PartData.PartType;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft.AircraftType;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
	}
	
	public void init() {
		super.init();
	}
	
	@Override
	public void tick() {
		super.tick();
	}
	
	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		if (player.isSecondaryUseActive()) {
			return InteractionResult.PASS;
		} else if (!level.isClientSide) {
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
		if (!(getVehicle() instanceof EntityAircraft craft)) {
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
	
	protected Vec3 getPassengerRelPos(Entity passenger, EntityAircraft craft) {
		Quaternion q;
		if (level.isClientSide) q = craft.getClientQ();
		else q = craft.getQ();
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
		return entityIn instanceof EntityAircraft;
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
		return getPlayer();
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
		LivingEntity p = getPassenger();
		if (p == null) return true;
		p.hurt(source, amount);
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

}
