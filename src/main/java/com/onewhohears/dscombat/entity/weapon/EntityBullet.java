package com.onewhohears.dscombat.entity.weapon;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.BulletData;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntityBullet extends EntityAbstractWeapon {
	
	public EntityBullet(EntityType<? extends EntityBullet> type, Level level) {
		super(type, level);
	}
	
	public EntityBullet(Level level, Entity owner, BulletData data) {
		super(level, owner, data);
	}
	
	/*@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setWeaponData(new BulletData(compound.getCompound("weapondata")));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.put("weapondata", this.getWeaponData().write());
	}*/
	
	@Override
	public void tick() {
		super.tick();
		// TODO bullets can sometimes randomly change direction or get pushed by exploding bullets
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
	
	@Override
	public void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		//System.out.println("BULLET HIT "+result.getBlockPos());
		this.checkExplode();
		this.discard();
	}
	
	@Override
	public void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		//System.out.println("BULLET HIT "+result.getEntity());
		DamageSource source = this.getDamageSource();
		if (source != null) {
			result.getEntity().hurt(source, ((BulletData)getWeaponData()).getDamage());
			checkExplode(source);
		}
		this.discard();
	}
	
	@Nullable
	protected EntityHitResult findHitEntity(Vec3 p_36758_, Vec3 p_36759_) {
		return ProjectileUtil.getEntityHitResult(this.level, this, p_36758_, p_36759_, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), this::canHitEntity);
	}
	
	protected DamageSource getDamageSource() {
		DamageSource source = null;
		if (getOwner() instanceof Player player) {
			source = DamageSource.playerAttack(player);
		} else if (getOwner() instanceof LivingEntity living) {
			source = DamageSource.mobAttack(living);
		}
		return source;
	}
	
	protected void checkExplode() {
		this.checkExplode(getDamageSource());
	}
	
	protected void checkExplode(DamageSource source) {
		BulletData data = (BulletData) this.getWeaponData();
		if (data.isExplosive()) {
			if (!this.level.isClientSide) {
				Explosion.BlockInteraction interact = Explosion.BlockInteraction.NONE;
				if (data.isDestroyTerrain()) interact = Explosion.BlockInteraction.BREAK;
				level.explode(this, source,
						null, getX(), getY(), getZ(), 
						data.getExplosionRadius(), data.isCausesFire(), 
						interact);
			} else {
				level.addParticle(ParticleTypes.SMOKE, 
						this.getX(), this.getY()+0.5D, this.getZ(), 
						0.0D, 0.0D, 0.0D);
			}
		}
	}
	
	/*@Override
	public boolean canBeCollidedWith() {
		return false;
	}*/
	
	@Override 
	public boolean canCollideWith(Entity entity) {
		if (entity instanceof EntityBullet) return false;
		return true;
	}

}
