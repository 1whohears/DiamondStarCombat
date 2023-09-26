package com.onewhohears.dscombat.entity.aircraft;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;

public class SubCollider extends PartEntity<RotableHitbox> {

	private final int subId;
	private final EntityDimensions size;
	private final Vec3 rel_pos;
	
	public SubCollider(RotableHitbox parent, int subId, float precision, Vec3 rel_pos) {
		super(parent);
		this.subId = subId;
		this.size = EntityDimensions.scalable(precision, precision);
		this.refreshDimensions();
		this.rel_pos = rel_pos;
		this.noPhysics = true;
		this.blocksBuilding = true;
	}
	
	@Override
	public void tick() {
		firstTick = false;
	}
	
	@Override
    public boolean hurt(DamageSource source, float amount) {
    	return getParent().hurt(source, amount);
    }
	
	@Override
    protected AABB makeBoundingBox() {
		double pX = getX(), pY = getY(), pZ = getZ();
    	EntityDimensions d = getDimensions(getPose());
    	double f = d.width / 2.0F;
        double f1 = d.height / 2.0F;
        return new AABB(pX-f, pY-f1, pZ-f, 
        		pX+f, pY+f1, pZ+f);
    }

	public int getSubId() {
		return subId;
	}
	
	public Vec3 getRelPos() {
		return rel_pos;
	}
	
	@Override
	public boolean isPickable() {
		return true;
	}
	
	@Override
    public boolean isPushable() {
    	return false;
    }
    
    @Override
    public boolean isPushedByFluid() {
    	return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }
    
    @Override
    public boolean canCollideWith(Entity entity) {
    	return getParent().getParent().canCollideWith(entity);
    }
	
    @Override
	public EntityDimensions getDimensions(Pose pose) {
		return size;
	}
    
	@Override
	public boolean is(Entity entity) {
		return this == entity || getParent() == entity || getParent().getParent() == entity;
	}
	
	@Override
	public Packet<?> getAddEntityPacket() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean shouldBeSaved() {
		return false;
	}

	@Override
	protected void defineSynchedData() {
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag nbt) {
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag nbt) {
	}

}
