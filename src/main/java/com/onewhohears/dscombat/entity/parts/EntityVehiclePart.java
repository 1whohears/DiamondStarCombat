package com.onewhohears.dscombat.entity.parts;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.parts.PartData.PartType;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import net.minecraftforge.entity.PartEntity;

public abstract class EntityVehiclePart extends PartEntity<EntityAircraft> {
	
	public static final EntityDataAccessor<Float> HEALTH = SynchedEntityData.defineId(EntityVehiclePart.class, EntityDataSerializers.FLOAT);
	
	private final String modelId;
	private final EntityDimensions size;
	private final String slotId;
	private final Vec3 rel_pos;
	private final float z_rot;
	
	protected EntityVehiclePart(EntityAircraft parent, String modelId, EntityDimensions size, String slotId, Vec3 pos, float z_rot) {
		super(parent);
		this.modelId = modelId;
		this.size = size;
		this.slotId = slotId;
		this.rel_pos = pos;
		this.z_rot = z_rot;
		if (!canGetHurt()) setHealth(1000);
	}
	
	public void init() {
	}
	
	@Override
	public void tick() {
		if (firstTick) init();
		super.tick();
		setOldPosAndRot();
	}
	
	public String getModelId() {
		return modelId;
	}
	
	public Vec3 getRelativePos() {
		return rel_pos;
	}
	
	public String getSlotId() {
		return slotId;
	}
	
	public float getZRot() {
		return z_rot;
	}
	
	@Override
    public boolean hurt(DamageSource source, float amount) {
		if (!canGetHurt()) return false;
		addHealth(-amount);
		if (getHealth() <= 0) kill();
		return true;
	}
	
	public void onDeath() {
		if (!canGetHurt()) return;
		getParent().partsManager.killPartInSlot(getSlotId());
	}
	
	@Override
	public void kill() {
		onDeath();
		super.kill();
	}
	
	public abstract boolean shouldRender();
	
	@Override
	public boolean isPickable() {
		return canGetHurt();
	}
	
	@Override 
	public boolean canCollideWith(Entity entity) {
		return false;
	}
	
	@Override
	public boolean canBeCollidedWith() {
		return false;
	}
	
	@Override
    protected boolean canAddPassenger(Entity passenger) {
		return false;
	}
	
	@Override
    protected boolean canRide(Entity entityIn) {
        return false;
    }
	
	public abstract PartType getPartType();
	
	public boolean isPilotSeat() {
		return PartSlot.isPilotSeat(getSlotId());
	}
	
	@Override
    public boolean isAlliedTo(Entity entity) {
    	if (entity == null) return false;
    	Entity c = entity.getControllingPassenger();
    	if (c != null) return isAlliedTo(c.getTeam());
    	return super.isAlliedTo(entity);
    }
    
    @Override
    public boolean isAlliedTo(Team team) {
    	if (team == null) return false;
    	Entity c = getParent().getControllingPassenger();
		if (c != null) return team.isAlliedTo(c.getTeam());
    	return super.isAlliedTo(team);
    }
    
    @Nullable
	@Override
    public Entity getControllingPassenger() {
    	Entity v = getParent();
    	if (v == null) return null;
		return v.getControllingPassenger();
    }
    
    public float getHealth() {
    	return entityData.get(HEALTH);
    }
    
    public void setHealth(float health) {
    	entityData.set(HEALTH, Math.max(0, health));
    }
    
    public void addHealth(float health) {
    	setHealth(getHealth()+health);
    }
    
    public abstract boolean canGetHurt();
    
    @Override
    protected AABB makeBoundingBox() {
    	if (Mth.abs(getZRot()) <= 90) return super.makeBoundingBox();
    	double pX = getX(), pY = getY(), pZ = getZ();
    	EntityDimensions d = getDimensions(getPose());
    	double f = d.width / 2.0F;
        double f1 = d.height;
        return new AABB(pX-f, pY-f1, pZ-f, 
        		pX+f, pY, pZ+f);
    }
    
    @Override
	public EntityDimensions getDimensions(Pose pose) {
		return size;
	}
    
	@Override
	public boolean is(Entity entity) {
		return this == entity || getParent() == entity;
	}
	
	@Override
	public boolean shouldBeSaved() {
		return false;
	}
	
	@Override
	public String toString() {
		String s = super.toString();
		s += getPartType().toString()+" "+getSlotId()+" "+getRelativePos()+" "+getModelId();
		return s;
	}
    
    @Override
	protected void defineSynchedData() {
    	entityData.define(HEALTH, 0f);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
	}

}
