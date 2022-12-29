package com.onewhohears.dscombat.entity.parts;

import com.onewhohears.dscombat.data.parts.PartData.PartType;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.parts.TurretData;
import com.onewhohears.dscombat.data.parts.TurretData.RotBounds;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.util.UtilParse;
import com.onewhohears.dscombat.util.math.UtilAngles;
import com.onewhohears.dscombat.util.math.UtilAngles.EulerAngles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class EntityTurret extends EntitySeat {
	
	public static final EntityDataAccessor<Integer> AMMO = SynchedEntityData.defineId(EntityTurret.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Float> MINROTX = SynchedEntityData.defineId(EntityTurret.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> MAXROTX = SynchedEntityData.defineId(EntityTurret.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> ROTRATE = SynchedEntityData.defineId(EntityTurret.class, EntityDataSerializers.FLOAT);
	//public static final EntityDataAccessor<Quaternion> Q = SynchedEntityData.defineId(EntityAircraft.class, DataSerializers.QUATERNION);
	public static final EntityDataAccessor<Float> RELROTX = SynchedEntityData.defineId(EntityTurret.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> RELROTY = SynchedEntityData.defineId(EntityTurret.class, EntityDataSerializers.FLOAT);
	
	//public Quaternion clientQ = Quaternion.ONE.copy();
	//public Quaternion prevQ = Quaternion.ONE.copy();
	public float zRot, zRotO; 
	public float xRotRelO, yRotRelO;
	
	/**
	 * only used on server side
	 */
	private String weaponId;
	/**
	 * only used on server side
	 */
	private WeaponData data;
	
	public EntityTurret(EntityType<?> type, Level level) {
		super(type, level);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(AMMO, 0);
		entityData.define(MINROTX, 0f);
		entityData.define(MAXROTX, 0f);
		entityData.define(ROTRATE, 0f);
		//entityData.define(Q, Quaternion.ONE);
		entityData.define(RELROTX, 0f);
		entityData.define(RELROTY, 0f);
	}
	
	@Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        /*if (level.isClientSide() && Q.equals(key)) {
        	setPrevQ(getClientQ());
        	setClientQ(getQ());
        }*/
    }
	
	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		data = UtilParse.parseWeaponFromCompound(tag.getCompound("weapondata"));
		if (data == null) data = WeaponPresets.getNewById(weaponId);
		setRotBounds(new RotBounds(tag));
		setXRot(tag.getFloat("xRot"));
		setYRot(tag.getFloat("yRot"));
		zRot = tag.getFloat("zRot");
		/*Quaternion q = UtilAngles.toQuaternion(-getYRot(), getXRot(), zRot);
		setQ(q);
		setPrevQ(q);*/
		setRelRotX(tag.getFloat("relrotx"));
		setRelRotX(tag.getFloat("relroty"));
		
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		if (data != null) tag.put("weapondata", data.write());
		getRotBounds().write(tag);
		tag.putFloat("xRot", getXRot());
		tag.putFloat("yRot", getYRot());
		tag.putFloat("zRot", zRot);
		tag.putFloat("relrotx", getRelRotX());
		tag.putFloat("relroty", getRelRotY());
	}
	
	public void init() {
		super.init();
		if (data != null) data.setCurrentAmmo(getAmmo());
	}
	
	@Override
	public void tick() {
		super.tick();
		xRotRelO = getRelRotX();
		yRotRelO = getRelRotY();
		Player player = getPlayer();
		if (player == null) return;
		// TODO change based on where player looks
		// TODO this is not working lmao
		/*float angle = 0; // whatever the new x rot is
		if (getVehicle() instanceof EntityAircraft plane) {
			Quaternion pq;
			if (level.isClientSide) pq = plane.getClientQ();
			else pq = plane.getQ();
			Quaternion pqpc = plane.getPrevQ(); pqpc.conj();
			Quaternion diff = pq.copy();
			diff.mul(pqpc);
			q.mul(diff);
			Vec3 dir = UtilAngles.getRollAxis(q);
			Vec3 planeNormal = UtilAngles.getYawAxis(pq);
			angle = (float) UtilGeometry.angleBetweenVecPlaneDegrees(dir, planeNormal);
		}
		EulerAngles angles = UtilAngles.toDegrees(q);
		setXRot((float)angles.pitch);
		setYRot((float)angles.yaw);
		zRot = (float)angles.roll;*/
		EulerAngles ra;
		if (getVehicle() instanceof EntityAircraft plane) ra = UtilAngles.toDegrees(plane.getQ());
		else ra = new EulerAngles();
		if (!level.isClientSide) {
			float gx = player.getXRot(), gy = player.getYRot();
			float[] relgoal = UtilAngles.globalToRelativeDegrees(gx, gy, 0f, ra);
			
			float relx = xRotRelO, rely = yRotRelO;
			float rotrate = getRotRate(), minrotx = getMinRotX(), maxrotx = getMaxRotX();
			
			if (relgoal[0] > maxrotx) relgoal[0] = maxrotx;
			else if (relgoal[0] < minrotx) relgoal[0] = minrotx;
			
			float rotdiffx = relgoal[0]-relx, rotdiffy = relgoal[1]-rely;
			
			if (Math.abs(rotdiffx) < rotrate) setRelRotX(relgoal[0]);
			else setRelRotX(relx + rotrate*Math.signum(rotdiffx));
			
			if (Math.abs(rotdiffy) < rotrate) setRelRotY(relgoal[1]);
			else setRelRotY(rely + rotrate*Math.signum(rotdiffy));
		}
		float[] global = UtilAngles.relativeToGlobalDegrees(getRelRotX(), getRelRotY(), 0f, ra);
		setXRot(global[0]);
		setYRot(global[1]);
		zRot = global[2];
	}
	
	@Override
	public boolean shouldRender() {
		return true;
	}
	
	@Override
	public boolean shouldRenderAtSqrDistance(double dist) {
		return dist < 25600;
	}
	
	public void setAmmo(int ammo) {
		entityData.set(AMMO, ammo);
	}
	
	public int getAmmo() {
		return entityData.get(AMMO);
	}
	
	public void setWeaponId(String wid) {
		weaponId = wid;
		data = WeaponPresets.getNewById(weaponId);
	}
	
	public String getWeaponId() {
		return weaponId;
	}
	
	public void shoot(Entity shooter) {
		if (level.isClientSide || data == null) return;
		data.shoot(level, shooter, getLookAngle(), position(), null);
		if (data.isFailedLaunch()) {
			if (shooter instanceof ServerPlayer player) {
				player.displayClientMessage(Component.translatable(data.getFailedLaunchReason()), true);
			}
		} else {
			setAmmo(data.getCurrentAmmo());
			if (getRootVehicle() instanceof EntityAircraft plane) {
				PartSlot slot = plane.partsManager.getSlot(getSlotId());
				if (slot != null && slot.filled() && slot.getPartData().getType() == PartType.TURRENT) { 
					TurretData td = (TurretData) slot.getPartData();
					td.setAmmo(data.getCurrentAmmo());
				}
			}
		}
	}
	
	@Override
    public void positionRider(Entity passenger) {
		/*if (passenger instanceof Player player) {
			player.setPos(position());
		} else if (passenger instanceof EntitySeatCamera camera) {
			if (!(getVehicle() instanceof EntityAircraft craft)) return;
			Vec3 pos = position();
			Quaternion q;
			if (level.isClientSide) {
				q = craft.getClientQ();
				q.mul(Vector3f.YP.rotationDegrees(getYRot()));
				q.mul(Vector3f.XP.rotationDegrees(getXRot()));
			} else q = craft.getQ();
			Vec3 seatPos = UtilAngles.rotateVector(new Vec3(0, 1.62, 0), q);
			camera.setPos(pos.add(seatPos));
		} else {
			super.positionRider(passenger);
		}*/
		super.positionRider(passenger);
	}
	
	@Override
	public PartType getPartType() {
		return PartType.TURRENT;
	}
	
	public void setRotBounds(RotBounds rb) {
		setMinRotX(rb.minRotX);
		setMaxRotX(rb.maxRotX);
		setRotRate(rb.rotRate);
	}
	
	public RotBounds getRotBounds() {
		return new RotBounds(getRotRate(), getMinRotX(), getMaxRotX());
	}
	
	public void setMinRotX(float rot) {
		entityData.set(MINROTX, rot);
	}
	
	public void setMaxRotX(float rot) {
		entityData.set(MAXROTX, rot);
	}
	
	public void setRotRate(float rot) {
		entityData.set(ROTRATE, rot);
	}
	
	public float getMinRotX() {
		return entityData.get(MINROTX);
	}
	
	public float getMaxRotX() {
		return entityData.get(MAXROTX);
	}
	
	public float getRotRate() {
		return entityData.get(ROTRATE);
	}
	
	/*public Quaternion getQ() {
        return entityData.get(Q).copy();
    }
    
    public void setQ(Quaternion q) {
        entityData.set(Q, q.copy());
    }
    
    public Quaternion getPrevQ() {
        return prevQ.copy();
    }
    
    public void setPrevQ(Quaternion q) {
        prevQ = q.copy();
    }
    
    public Quaternion getClientQ() {
        return clientQ.copy();
    }
    
    public void setClientQ(Quaternion q) {
        clientQ = q.copy();
    }*/
	
	public float getRelRotX() {
		return entityData.get(RELROTX);
	}
	
	public float getRelRotY() {
		return entityData.get(RELROTY);
	}
	
	public void setRelRotX(float degrees) {
		entityData.set(RELROTX, degrees);
	}
	
	public void setRelRotY(float degrees) {
		entityData.set(RELROTY, degrees);
	}

}
