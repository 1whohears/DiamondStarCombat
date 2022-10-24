package com.onewhohears.dscombat.entity.parts;

import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.util.UtilParse;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class EntityAbstractPart extends Entity {
	
	public static final EntityDataAccessor<Vec3> POS = SynchedEntityData.defineId(EntitySeat.class, DataSerializers.VEC3);
	public static final EntityDataAccessor<String> ID = SynchedEntityData.defineId(EntitySeat.class, EntityDataSerializers.STRING);
	
	public EntityAbstractPart(EntityType<?> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}
	
	public EntityAbstractPart(EntityType<?> pEntityType, Level level, String slotId, Vec3 pos) {
		this(pEntityType, level);
		this.setRelativePos(pos);
		this.setSlotId(slotId);
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(POS, Vec3.ZERO);
		entityData.define(ID, "");
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		setRelativePos(UtilParse.readVec3(compound, "relpos"));
		this.setSlotId(compound.getString("id"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		UtilParse.writeVec3(compound, getRelativePos(), "relpos");
		compound.putString("id", this.getSlotId());
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	public void init() {
		
	}
	
	@Override
	public void tick() {
		if (this.firstTick) init();
		super.tick();
		if (!this.level.isClientSide && this.getVehicle() == null) this.kill();
	}
	
	public Vec3 getRelativePos() {
		return entityData.get(POS);
	}
	
	public void setRelativePos(Vec3 pos) {
		entityData.set(POS, pos);
	}
	
	public String getSlotId() {
		return entityData.get(ID);
	}
	
	public void setSlotId(String id) {
		entityData.set(ID, id);
	}

}
