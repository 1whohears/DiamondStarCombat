package com.onewhohears.dscombat.entity.parts;

import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.init.ModEntities;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.network.NetworkHooks;

public class EntitySeat extends EntityAbstractPart {
	
	public EntitySeat(EntityType<?> type, Level level) {
		super(type, level);
	}
	
	public EntitySeat(Level level, String seatId, Vec3 pos) {
		super(ModEntities.SEAT.get(), level);
		this.setRelativePos(pos);
		this.setPartId(seatId);
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
		List<Entity> passengers = getPassengers();
		for (Entity p : passengers) {
			System.out.println("CHECK CAMERA "+p);
			if (p instanceof EntitySeatCamera camera) {
				System.out.println("ALREADY CAMERA");
				return;
			}
		}
		EntitySeatCamera cam = new EntitySeatCamera(level);
		cam.setPos(position());
		cam.startRiding(this);
		level.addFreshEntity(cam);
		System.out.println("ADDED CAMERA "+cam);
	}
	
	@Override
	public void tick() {
		super.tick();
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		return InteractionResult.PASS;
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
    public boolean canAddPassenger(Entity passenger) {
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
		Entity root = this.getRootVehicle();
		if (root == null) return super.getDismountLocationForPassenger(livingEntity);
		return root.getDismountLocationForPassenger(livingEntity);
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
    public boolean canBeRiddenUnderFluidType(FluidType type, Entity rider) {
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
		if (source.isExplosion() || source.isFire() || source.isMagic()) return false;
		Entity v = this.getRootVehicle();
		if (v != null) v.hurt(source, amount);
		return true;
	}
	
	@Override
	public String toString() {
		return "seat "+this.getRelativePos();
	}

}
