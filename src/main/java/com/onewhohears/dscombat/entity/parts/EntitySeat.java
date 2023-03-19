package com.onewhohears.dscombat.entity.parts;

import java.util.List;

import javax.annotation.Nullable;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.data.parts.PartData.PartType;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft.AircraftType;
import com.onewhohears.dscombat.init.ModEntities;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
	
	public EntitySeat(EntityType<?> type, Level level) {
		super(type, level);
	}
	
	public EntitySeat(Level level, String slotId, Vec3 pos) {
		super(ModEntities.SEAT.get(), level, slotId, pos);
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
		if (getCamera() != null) return;
		EntitySeatCamera cam = new EntitySeatCamera(level);
		cam.setPos(position());
		cam.startRiding(this);
		level.addFreshEntity(cam);
	}
	
	@Override
	public void tick() {
		super.tick();
	}
	
	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		return InteractionResult.PASS;
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
						30, 0, false, false));
			}	
			if (craft.getAircraftType() == AircraftType.SUBMARINE) {
				player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 
						30, 0, false, false));
				player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 
						30, 0, false, false));
			}
		}
		Vec3 pos = position();
		Quaternion q;
		if (level.isClientSide) q = craft.getClientQ();
		else q = craft.getQ();
		double offset = getPassengersRidingOffset() + passenger.getMyRidingOffset();
		Vec3 passPos = UtilAngles.rotateVector(new Vec3(0, offset, 0), q);
		passenger.setPos(pos.add(passPos));
	}
	
	@Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
	}
	
	@Override
    public boolean canAddPassenger(Entity passenger) {
		if (passenger instanceof Player) return getPlayer() == null;
		if (passenger instanceof EntitySeatCamera) return getCamera() == null;
		return false;
	}
	
	@Override
    protected boolean canRide(Entity entityIn) {
		return entityIn instanceof EntityAircraft;
    }
	
	@Override
    public Vec3 getDismountLocationForPassenger(LivingEntity livingEntity) {
		return super.getDismountLocationForPassenger(livingEntity).add(0, 1, 0);
	}
	
	@Nullable
	public Player getPlayer() {
		List<Entity> list = getPassengers();
		for (Entity e : list) if (e instanceof Player p) return p;
		return null;
	}
	
	@Nullable
	public EntitySeatCamera getCamera() {
		List<Entity> list = getPassengers();
		for (Entity e : list) if (e instanceof EntitySeatCamera c) return c;
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
        return 0.0;
    }

	@Override
	public boolean shouldRender() {
		return false;
	}

	@Override
	public PartType getPartType() {
		return PartType.SEAT;
	}

}
