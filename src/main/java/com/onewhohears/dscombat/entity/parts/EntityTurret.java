package com.onewhohears.dscombat.entity.parts;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.data.parts.PartData.PartType;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.parts.TurretData;
import com.onewhohears.dscombat.data.parts.TurretData.RotBounds;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.util.UtilParse;
import com.onewhohears.dscombat.util.math.UtilAngles;

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
	//public static final EntityDataAccessor<Quaternion> RQ = SynchedEntityData.defineId(EntityAircraft.class, DataSerializers.QUATERNION);
	public static final EntityDataAccessor<Float> RELROTX = SynchedEntityData.defineId(EntityTurret.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> RELROTY = SynchedEntityData.defineId(EntityTurret.class, EntityDataSerializers.FLOAT);
	
	//public Quaternion clientQ = Quaternion.ONE.copy();
	//public Quaternion prevRQ = Quaternion.ONE.copy();
	//public float zRot, zRotO; 
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
		//entityData.define(RQ, Quaternion.ONE);
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
		//zRot = tag.getFloat("zRot");
		setRelRotX(tag.getFloat("relrotx"));
		setRelRotX(tag.getFloat("relroty"));
		/*Quaternion q = UtilAngles.toQuaternion(getRelRotY(), getRelRotX(), zRot);
		setRelQ(q);
		setPrevRelQ(q);*/
		
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		if (data != null) tag.put("weapondata", data.write());
		getRotBounds().write(tag);
		tag.putFloat("xRot", getXRot());
		tag.putFloat("yRot", getYRot());
		//tag.putFloat("zRot", zRot);
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
		//setPrevRelQ(getRelQ());
		Player player = getPlayer();
		if (player == null) return;
		Quaternion ra = Quaternion.ONE;
		if (!level.isClientSide) {
			//Quaternion rq = getRelQ();
			//EulerAngles rr = UtilAngles.toDegrees(rq);
			//float rely = (float)rr.yaw, relx = (float)rr.pitch;
			float rely = yRotRelO, relx = xRotRelO;
			float rotrate = getRotRate(), minrotx = getMinRotX(), maxrotx = getMaxRotX();
			//float zgoal = 0;
			if (getVehicle() instanceof EntityAircraft plane) {
				ra = plane.getQ();
				//zgoal = plane.zRot;
			}
			//Quaternion rrg = UtilAngles.globalDegreesToRelativeRotation(player.getXRot(), player.getYRot(), zgoal, ra);
			//EulerAngles rrga = UtilAngles.toDegrees(rrg);
			float[] relangles = UtilAngles.globalToRelativeDegrees(player.getXRot(), player.getYRot(), ra);
			
			float rg1 = relangles[1] + 360, rg2 = relangles[1] - 360;
			float d1 = Math.abs(rg1-rely), d2 = Math.abs(rg2-rely), d3 =  Math.abs(relangles[1]-rely);
			if (d1 < d2 && d1 < d3) relangles[1] += 360;
			else if (d2 < d1 && d2 < d3) relangles[1] -= 360;
			
			if (relangles[0] > maxrotx) relangles[0] = maxrotx;
			else if (relangles[0] < minrotx) relangles[0] = minrotx;
			
			float rotdiffx = relangles[0]-relx, rotdiffy = relangles[1]-rely;
			
			float dx, dy;
			
			if (Math.abs(rotdiffx) < rotrate) dx = rotdiffx;
			else dx = rotrate*Math.signum(rotdiffx);
			
			if (Math.abs(rotdiffy) < rotrate) dy = rotdiffy;
			else dy = rotrate*Math.signum(rotdiffy);
			
			/*rq.mul(Vector3f.XP.rotationDegrees(dx));
			rq.mul(Vector3f.YN.rotationDegrees(dy));
			rq.normalize();
			setRelQ(rq);
			
			EulerAngles nrr = UtilAngles.toDegrees(rq);
			setRelRotX((float)nrr.pitch);
			setRelRotY((float)nrr.yaw);*/
			
			setRelRotX(relx+dx);
			setRelRotY(rely+dy);
			
			System.out.println("TURRET SERVER TICK "+tickCount);
			System.out.println("rely     = "+rely);
			System.out.println("relgoaly = "+relangles[1]);
			System.out.println("rotdiffy = "+rotdiffy);
			System.out.println("relyn    = "+getRelRotY());
			/*System.out.println("relx     = "+relx);
			System.out.println("relgoalx = "+(float)rrga.pitch);
			System.out.println("rotdiffx = "+rotdiffx);
			System.out.println("relxn    = "+getRelRotX());*/
		}
		float[] global = UtilAngles.relativeToGlobalDegrees(getRelRotX(), getRelRotY(), ra);
		setXRot(global[0]);
		setYRot(global[1]);
		//zRot = (float)global.roll;
		if (!level.isClientSide) {
			System.out.println("playery  = "+player.getYRot());
			System.out.println("globaly  = "+global[1]);
			//System.out.println("playerx  = "+player.getXRot());
			//System.out.println("globalx  = "+global.pitch);
		}
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
	
	/*public Quaternion getRelQ() {
        return entityData.get(RQ).copy();
    }
    
    public void setRelQ(Quaternion q) {
        entityData.set(RQ, q.copy());
    }
    
    public Quaternion getPrevRelQ() {
        return prevRQ.copy();
    }
    
    public void setPrevRelQ(Quaternion q) {
        prevRQ = q.copy();
    }*/
    
    /*public Quaternion getClientQ() {
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
