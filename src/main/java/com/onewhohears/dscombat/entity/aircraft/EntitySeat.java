package com.onewhohears.dscombat.entity.aircraft;

import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.init.ModEntities;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class EntitySeat extends Entity {
	
	public static final EntityDataAccessor<Vec3> POS = SynchedEntityData.defineId(EntitySeat.class, DataSerializers.VEC3);
	
	public EntitySeat(EntityType<?> type, Level level) {
		super(type, level);
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(POS, Vec3.ZERO);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		double x = compound.getDouble("relposx");
		double y = compound.getDouble("relposy");
		double z = compound.getDouble("relposz");
		setRelativeSeatPos(new Vec3(x, y, z));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		Vec3 pos = getRelativeSeatPos();
		compound.putDouble("relposx", pos.x);
		compound.putDouble("relposy", pos.y);
		compound.putDouble("relposz", pos.z);
	}
	
	public void init() {
		List<Entity> passengers = getPassengers();
		for (Entity p : passengers) {
			System.out.println("CHECK CAMERA "+p);
			if (p instanceof EntitySeatCamera camera) {
				System.out.println("ALREADY CAMERA");
				return;
			}
		}
		EntitySeatCamera cam = new EntitySeatCamera(ModEntities.CAMERA.get(), level);
		cam.setPos(position());
		cam.startRiding(this);
		level.addFreshEntity(cam);
		System.out.println("ADDED CAMERA "+cam);
	}
	
	@Override
	public void tick() {
		if (this.tickCount == 1) init();
		super.tick();
		if (!this.level.isClientSide && this.getVehicle() == null) this.discard();
		//System.out.println("SEAT POS "+this.position());
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		if (player.isSecondaryUseActive()) {
			return InteractionResult.PASS;
		} else if (!this.level.isClientSide) {
			return player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
		} else {
			return InteractionResult.SUCCESS;
		}
	}
	
	public Vec3 getRelativeSeatPos() {
		return entityData.get(POS);
	}
	
	public void setRelativeSeatPos(Vec3 pos) {
		entityData.set(POS, pos);
	}
	
	@Override
    public void positionRider(Entity passenger) {
		//System.out.println("SEAT POSITION "+passenger);
		if (passenger instanceof Player player) {
			player.setPos(position());
		} else if (passenger instanceof EntitySeatCamera camera) {
			//System.out.println("SEAT ROOT "+getVehicle());
			if (!(this.getVehicle() instanceof EntityAbstractAircraft craft)) return;
			Vec3 pos = position();
			Vec3 seatPos = UtilAngles.rotateVector(new Vec3(0, 1.62, 0), craft.getQ());
			camera.setPos(pos.add(seatPos));
		} else {
			super.positionRider(passenger);
		}
	}
	
	@Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        System.out.println("SEAT ADDED PASSENGER "+passenger);
	}
	
	@Override
    protected boolean canAddPassenger(Entity passenger) {
		System.out.println("CAN SEAT ADD "+passenger);
		if (passenger instanceof Player) return getPlayer() == null;
		if (passenger instanceof EntitySeatCamera) return getCamera() == null;
		return false;
	}
	
	@Override
    protected boolean canRide(Entity entityIn) {
		System.out.println("CAN RIDE SEAT "+entityIn);
		return entityIn instanceof EntityAbstractAircraft;
    }
	
	@Override
    public Vec3 getDismountLocationForPassenger(LivingEntity livingEntity) {
		// TODO get dismount location for root plane
		return super.getDismountLocationForPassenger(livingEntity);
	}
	
	public Player getPlayer() {
		List<Entity> list = getPassengers();
		for (Entity e : list) if (e instanceof Player p) return p;
		return null;
	}
	
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
	public boolean isPickable() {
		return !this.isRemoved();
	}

    @Override
    public boolean canBeRiddenInWater(Entity rider) {
        return true;
    }
    
    @Override
    public double getPassengersRidingOffset() {
        return 0;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }
	
	@Override
    public boolean hurt(DamageSource source, float amount) {
		return false;
	}
	
	@Override
	public String toString() {
		return "seat "+this.getRelativeSeatPos();
	}

}
