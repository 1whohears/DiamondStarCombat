package com.onewhohears.dscombat.data;

public class BulletData extends WeaponData {
	
	private float damage;
	private double speed;
	
	public BulletData(float damage, double speed) {
		this.damage = damage;
		this.speed = speed;
	}
	
	@Override
	public WeaponType getType() {
		return WeaponType.BULLET;
	}

	@Override
	public float getDamage() {
		return damage;
	}

	@Override
	public double getSpeed() {
		return speed;
	}

}
