package com.onewhohears.dscombat.data;

public abstract class WeaponData {
	
	public static enum WeaponType {
		BULLET,
		ROCKET,
		BOMB
	}
	
	public abstract WeaponType getType();
	
	public abstract float getDamage();
	
	public abstract double getSpeed();
	
}
