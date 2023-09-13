package com.onewhohears.dscombat.entity.parts;

import com.onewhohears.dscombat.data.parts.PartData.PartType;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import net.minecraftforge.network.NetworkHooks;

public abstract class EntityPart extends Entity {
	
	public static final EntityDataAccessor<Vec3> POS = SynchedEntityData.defineId(EntityPart.class, DataSerializers.VEC3);
	public static final EntityDataAccessor<String> SLOT_ID = SynchedEntityData.defineId(EntityPart.class, EntityDataSerializers.STRING);
	
	private float z_rot;
	
	protected EntityPart(EntityType<?> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}
	
	protected EntityPart(EntityType<?> entityType, Level level, String slotId, Vec3 pos) {
		this(entityType, level);
		this.setRelativePos(pos);
		this.setSlotId(slotId);
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(POS, Vec3.ZERO);
		entityData.define(SLOT_ID, "");
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		setRelativePos(UtilParse.readVec3(compound, "relpos"));
		this.setSlotId(compound.getString("slotid"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		UtilParse.writeVec3(compound, getRelativePos(), "relpos");
		compound.putString("slotid", this.getSlotId());
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	public void init() {
		if (getVehicle() instanceof EntityAircraft plane) {
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
		if (!level.isClientSide && tickCount > 10 && getVehicle() == null) kill(); 
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
		return false;
	}
	
	public abstract boolean shouldRender();
	
	public float getZRot() {
		return z_rot;
	}
	
	@Override
	public boolean isPickable() {
		return false;
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

}
