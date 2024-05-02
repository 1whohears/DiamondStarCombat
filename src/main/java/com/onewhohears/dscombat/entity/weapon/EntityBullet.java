package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.data.vehicle.DSCPhyCons;
import com.onewhohears.dscombat.data.weapon.BulletData;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.entity.damagesource.WeaponDamageSource;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityBullet extends EntityWeapon {
	
	protected BulletData bulletData;
	
	public EntityBullet(EntityType<? extends EntityBullet> type, Level level, String defaultWeaponId) {
		super(type, level, defaultWeaponId);
	}
	
	@Override
	protected void castWeaponData() {
		bulletData = (BulletData)weaponData;
	}
	
	@Override
	public void init() {
		super.init();
		Vec3 dir = UtilAngles.rotationToVector(getYRot(), getXRot());
		setDeltaMovement(dir.scale(getSpeed()));
	}
	
	@Override
	protected void tickSetMove() {
		setDeltaMovement(getDeltaMovement().add(0, -DSCPhyCons.GRAVITY, 0));
	}
	
	protected void checkExplode() {
		if (getAge() < minExplodeAge()) return;
		if (!level.hasChunk(chunkPosition().x, chunkPosition().z)) return;
		if (!level.isClientSide && getExplosive()) {
			Explosion.BlockInteraction interact = Explosion.BlockInteraction.NONE;
			if (getTerrain()) interact = Explosion.BlockInteraction.BREAK;
			for (int i = 0; i < getExplodeNum(); ++i) {
				level.explode(this, getExplosionDamageSource(),
					null, getX(), getY(), getZ(), 
					getRadius(), getFire(), interact);
			}
		}
	}
	
	public int minExplodeAge() {
		return 1;
	}
	
	@Override
	public float getDamage() {
		return bulletData.getDamage();
	}
	
	public boolean getExplosive() {
		return bulletData.isExplosive();
	}
	
	public boolean getTerrain() {
		return bulletData.isDestroyTerrain();
	}
	
	public boolean getFire() {
		return bulletData.isCausesFire();
	}
	
	public float getRadius() {
		return bulletData.getExplosionRadius();
	}
	
	public double getSpeed() {
		return bulletData.getSpeed();
	}
	
	public int getExplodeNum() {
		return bulletData.getExplodeNum();
	}
	
	@Override
	public void kill() {
		// ORDER MATTERS
		super.kill();
		checkExplode();
	}

	@Override
	protected WeaponDamageSource getImpactDamageSource() {
		return WeaponDamageSource.WeaponDamageType.BULLET.getSource(getOwner(), this);
	}

	@Override
	protected WeaponDamageSource getExplosionDamageSource() {
		return WeaponDamageSource.WeaponDamageType.BULLET_EXPLODE.getSource(getOwner(), this);
	}

	@Override
	public WeaponData.WeaponType getWeaponType() {
		return WeaponData.WeaponType.BULLET;
	}

	@Override
	public WeaponData.WeaponClientImpactType getClientImpactType() {
		if (getExplosive()) return WeaponData.WeaponClientImpactType.SMALL_BULLET_EXPLODE;
		return WeaponData.WeaponClientImpactType.SMALL_BULLET_IMPACT;
	}

}
