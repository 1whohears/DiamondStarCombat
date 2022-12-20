package com.onewhohears.dscombat.entity.parts;

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
import net.minecraftforge.network.NetworkHooks;

public abstract class EntityPart extends Entity {
	
	public static final EntityDataAccessor<Vec3> POS = SynchedEntityData.defineId(EntityPart.class, DataSerializers.VEC3);
	public static final EntityDataAccessor<String> SLOT_ID = SynchedEntityData.defineId(EntityPart.class, EntityDataSerializers.STRING);
	
	public EntityPart(EntityType<?> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}
	
	public EntityPart(EntityType<?> pEntityType, Level level, String slotId, Vec3 pos) {
		this(pEntityType, level);
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
		/* THIS IS NO
		if (source.isExplosion() || source.isFire() || source.isMagic()) return false;
		Entity v = this.getRootVehicle();
		if (v != null) v.hurt(source, amount);
		return true;*/
		return false;
	}
	
	public abstract boolean shouldRender();
	
	public float getZRot() {
		if (!(getVehicle() instanceof EntityAircraft plane)) return 0;
		PartSlot ps = plane.partsManager.getSlot(getSlotId());
		if (ps == null) return 0;
		return ps.getZRot();
	}
	
	@Override 
	public boolean canCollideWith(Entity entity) {
		return false;
	}

}
