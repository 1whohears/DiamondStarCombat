package com.onewhohears.dscombat.entity.parts;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.client.model.obj.ObjRadarModel.MastType;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.parts.PartType;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import net.minecraftforge.network.NetworkHooks;

public abstract class EntityPart extends Entity {
	
	public static final EntityDataAccessor<Vec3> POS = SynchedEntityData.defineId(EntityPart.class, DataSerializers.VEC3);
	public static final EntityDataAccessor<String> SLOT_ID = SynchedEntityData.defineId(EntityPart.class, EntityDataSerializers.STRING);
	public static final EntityDataAccessor<Float> HEALTH = SynchedEntityData.defineId(EntityPart.class, EntityDataSerializers.FLOAT);
	
	private float z_rot;
	protected double renderSqrDistance = 0;
	
	protected EntityPart(EntityType<?> entityType, Level level) {
		super(entityType, level);
		if (level.isClientSide && shouldRender()) {
			double dist = getClientRenderDistance();
			renderSqrDistance = dist * dist;
		}
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(POS, Vec3.ZERO);
		entityData.define(SLOT_ID, "");
		entityData.define(HEALTH, 0f);
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		setRelativePos(UtilParse.readVec3(compound, "relpos"));
		setSlotId(compound.getString("slotid"));
		setHealth(compound.getFloat("health"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		UtilParse.writeVec3(compound, getRelativePos(), "relpos");
		compound.putString("slotid", getSlotId());
		compound.putFloat("health", getHealth());
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	public void init() {
		if (getVehicle() instanceof EntityVehicle plane) {
			PartSlot ps = plane.partsManager.getSlot(getSlotId());
			if (ps != null) {
				z_rot = ps.getZRot();
				setRelativePos(ps.getRelPos());
			}
		}
	}
	
	@Override
	public void tick() {
		if (firstTick) init();
		super.tick();
		if (!level.isClientSide && tickCount > 10 && getVehicle() == null) onNoParent();
	}
	
	protected void onNoParent() {
		discard();
	}
	
	public Vec3 getRelativePos() {
		return entityData.get(POS);
	}
	
	public void setRelativePos(Vec3 pos) {
		entityData.set(POS, pos);
	}
	
	public String getSlotId() {
		return entityData.get(SLOT_ID);
	}
	
	public void setSlotId(String id) {
		entityData.set(SLOT_ID, id);
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
		if (!(getVehicle() instanceof EntityVehicle plane)) return;
		plane.partsManager.killPartInSlot(getSlotId());
	}
	
	@Override
	public void kill() {
		onDeath();
		super.kill();
	}
	
	@Nullable
	public EntityVehicle getParentVehicle() {
		if (getVehicle() instanceof EntityVehicle vehicle) return vehicle;
		return null;
	}
	
	public abstract boolean shouldRender();
	
	protected double getClientRenderDistance() {
		return Config.CLIENT.renderOtherExternalPartDistance.get();
	}
	
	@Override
	public boolean shouldRenderAtSqrDistance(double dist) {
		return shouldRender() && dist <= renderSqrDistance;
	}
	
	public float getZRot() {
		return z_rot;
	}
	
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
	
	public abstract PartType getPartType();
	
	public boolean isPilotSeat() {
		return PartSlot.isPilotSeat(getSlotId());
	}
	
	public boolean isCoPilotSeat() {
		return PartSlot.isCoPilotSeat(getSlotId());
	}
	
	public boolean isSeat() {
		return false;
	}
	
	public boolean isTurret() {
		return false;
	}
	
	public boolean canPassengerShootParentWeapon() {
		return isPilotSeat() || isCoPilotSeat();
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
    	Entity v = getVehicle();
    	if (v == null) return false;
    	Entity c = v.getControllingPassenger();
		if (c != null) return team.isAlliedTo(c.getTeam());
    	return super.isAlliedTo(team);
    }
    
    @Nullable
	@Override
    public Entity getControllingPassenger() {
    	Entity v = getVehicle();
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
    
    public MastType getVehicleMastType() {
    	EntityVehicle vehicle = getParentVehicle();
    	if (vehicle == null) return MastType.NONE;
    	return vehicle.getMastType();
    }

}
