package com.onewhohears.dscombat.data.weapon.instance;

import java.util.Random;

import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.data.weapon.WeaponShootParameters;
import com.onewhohears.dscombat.data.weapon.stats.BulletStats;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.entity.weapon.EntityBullet;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.world.phys.Vec3;

public class BulletInstance<T extends BulletStats> extends WeaponInstance<T> {

	public BulletInstance(T stats) {
		super(stats);
	}
	
	@Override
	public EntityWeapon<?> getShootEntity(WeaponShootParameters params) {
		EntityBullet<?> bullet = (EntityBullet<?>) super.getShootEntity(params);
		if (bullet == null) return null;
		bullet.setDeltaMovement(bullet.getLookAngle().scale(getStats().getSpeed()));
		return bullet;
	}
	
	@Override
	public void setDirection(EntityWeapon<?> weapon, Vec3 direction) {
		float pitch = UtilAngles.getPitch(direction);
		float yaw = UtilAngles.getYaw(direction);
		Random r = new Random();
		pitch = pitch + (r.nextFloat()-0.5f) * 2f * getStats().getInnacuracy();
		yaw = yaw + (r.nextFloat()-0.5f) * 2f * getStats().getInnacuracy();
		weapon.setXRot(pitch-changeLaunchPitch);
		weapon.setYRot(yaw);
	}
	
	@Override
	protected Vec3 getStartMove(EntityVehicle vehicle) {
		Vec3 move = vehicle.getLookAngle().scale(getStats().getSpeed());
		if (vehicle.isWeaponAngledDown() && getStats().canAngleDown())
			move = UtilAngles.rotateVector(move, Vector3f.XN.rotationDegrees(25f));
		return move;
	}

}
