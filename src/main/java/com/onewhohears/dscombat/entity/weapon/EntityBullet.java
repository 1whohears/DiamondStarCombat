package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.data.vehicle.DSCPhyCons;
import com.onewhohears.dscombat.data.weapon.WeaponType;
import com.onewhohears.dscombat.data.weapon.stats.BulletStats;
import com.onewhohears.dscombat.data.weapon.stats.WeaponStats;
import com.onewhohears.dscombat.entity.damagesource.WeaponDamageSource;
import com.onewhohears.onewholibs.util.math.UtilAngles;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityBullet<T extends BulletStats> extends EntityWeapon<T> {
	
	public EntityBullet(EntityType<? extends EntityBullet<?>> type, Level level, String defaultWeaponId) {
		super(type, level, defaultWeaponId);
	}
	
	@Override
	public WeaponType getWeaponType() {
		return WeaponType.BULLET;
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
		if (!level().hasChunk(chunkPosition().x, chunkPosition().z)) return;
		if (!level().isClientSide && getExplosive()) {
			Explosion.BlockInteraction interact = Explosion.BlockInteraction.KEEP;
			if (getTerrain()) interact = Explosion.BlockInteraction.DESTROY;
			for (int i = 0; i < getExplodeNum(); ++i) {
				level().explode(this, getExplosionDamageSource(),
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
		return getWeaponStats().getDamage();
	}
	
	public boolean getExplosive() {
		return getWeaponStats().isExplosive();
	}
	
	public boolean getTerrain() {
		return getWeaponStats().isDestroyTerrain();
	}
	
	public boolean getFire() {
		return getWeaponStats().isCausesFire();
	}
	
	public float getRadius() {
		return getWeaponStats().getExplosionRadius();
	}
	
	public double getSpeed() {
		return getWeaponStats().getSpeed();
	}
	
	public int getExplodeNum() {
		return getWeaponStats().getExplodeNum();
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
	public WeaponStats.WeaponClientImpactType getClientImpactType() {
		if (getExplosive()) return WeaponStats.WeaponClientImpactType.SMALL_BULLET_EXPLODE;
		return WeaponStats.WeaponClientImpactType.SMALL_BULLET_IMPACT;
	}

}
