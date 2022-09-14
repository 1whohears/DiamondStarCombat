package com.onewhohears.dscombat.entity.weapon;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.BulletData;
import com.onewhohears.dscombat.data.WeaponData;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.init.ModEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public abstract class EntityAbstractWeapon extends Projectile {
	
	public static final EntityDataAccessor<WeaponData> DATA = SynchedEntityData.defineId(EntityAbstractWeapon.class, DataSerializers.WEAPON_DATA);
	
	public EntityAbstractWeapon(EntityType<? extends EntityAbstractWeapon> type, Level level) {
		super(type, level);
	}
	
	public EntityAbstractWeapon(Level level, Entity pilot, WeaponData data) {
		super(ModEntities.BULLET.get(), level);
		this.setOwner(pilot);
		this.setWeaponData(data);
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(DATA, BulletData.getDefault());
	}
	
	/*@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
	}*/
	
	@Override
	public void tick() {
		if (!this.level.isClientSide && touchingUnloadedChunk()) {
			System.out.println("bullet unloaded");
			discard(); 
			return; 
		}
		super.tick();
		if (!this.level.isClientSide && this.tickCount > 600) { 
			System.out.println("bullet to old");
			discard(); 
			return; 
		}
		if (Double.isNaN(getDeltaMovement().length())) {
			setDeltaMovement(Vec3.ZERO);
        }
		//System.out.println(this);
		Vec3 vec3 = this.getDeltaMovement();
		Vec3 vec32 = this.position();
        Vec3 vec33 = vec32.add(vec3);
        HitResult hitresult = this.level.clip(new ClipContext(vec32, vec33, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (hitresult.getType() != HitResult.Type.MISS) {
        	vec33 = hitresult.getLocation();
        }
        while(!this.isRemoved()) {
           EntityHitResult entityhitresult = this.findHitEntity(vec32, vec33);
           if (entityhitresult != null) {
              hitresult = entityhitresult;
           }
           if (hitresult != null && hitresult.getType() == HitResult.Type.ENTITY) {
              Entity entity = ((EntityHitResult)hitresult).getEntity();
              Entity entity1 = this.getOwner();
              if (entity instanceof Player && entity1 instanceof Player && !((Player)entity1).canHarmPlayer((Player)entity)) {
                 hitresult = null;
                 entityhitresult = null;
              }
           }
           if (hitresult != null && hitresult.getType() != HitResult.Type.MISS && !noPhysics 
        		   && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
              this.onHit(hitresult);
              this.hasImpulse = true;
              break;
           }
           if (entityhitresult == null /*|| this.getPierceLevel() <= 0*/) {
              break;
           }
           hitresult = null;
        }
		move(MoverType.SELF, getDeltaMovement());
		setPacketCoordinates(this.getX(), this.getY(), this.getZ());
	}
	
	public void shoot(Vec3 pos, Vec3 direction) {
		this.shoot(pos, direction, Vec3.ZERO);
	}
	
	public void shoot(Vec3 pos, Vec3 direction, Vec3 initMove) {
		this.setPos(pos);
		this.setDeltaMovement(initMove.add(direction.scale(getWeaponData().getSpeed())));
	}
	
	@Override
	public void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		System.out.println("BULLET HIT "+result.getBlockPos());
		this.discard();
	}
	
	@Override
	public void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		System.out.println("BULLET HIT "+result.getEntity());
		DamageSource source = null;
		if (getOwner() instanceof Player player) {
			source = DamageSource.playerAttack(player);
		} else if (getOwner() instanceof LivingEntity living) {
			source = DamageSource.mobAttack(living);
		}
		if (source != null) {
			result.getEntity().hurt(source, getWeaponData().getDamage());
		}
		this.discard();
	}
	
	@Nullable
	protected EntityHitResult findHitEntity(Vec3 p_36758_, Vec3 p_36759_) {
		return ProjectileUtil.getEntityHitResult(this.level, this, p_36758_, p_36759_, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), this::canHitEntity);
	}
	
	public void setWeaponData(WeaponData data) {
		entityData.set(DATA, data);
	}
	
	public WeaponData getWeaponData() {
		return entityData.get(DATA);
	}

}
