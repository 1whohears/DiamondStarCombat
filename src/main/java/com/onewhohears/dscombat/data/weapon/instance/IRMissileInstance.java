package com.onewhohears.dscombat.data.weapon.instance;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.weapon.WeaponShootParameters;
import com.onewhohears.dscombat.data.weapon.stats.IRMissileStats;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.entity.weapon.IRMissile;
import com.onewhohears.dscombat.entity.weapon.IRMissile.IrTarget;

public class IRMissileInstance<T extends IRMissileStats> extends MissileInstance<T> {

	public IRMissileInstance(T stats) {
		super(stats);
	}
	
	@Override
	public EntityWeapon<?> getShootEntity(WeaponShootParameters params) {
		IRMissile<?> missile = (IRMissile<?>) super.getShootEntity(params);
		if (missile == null) return null;
		return missile;
	}
	
	protected List<IrTarget> targets = new ArrayList<IrTarget>();
	
	@Override
	public void tick(@Nullable EntityVehicle parent, boolean isSelected) {
		super.tick(parent, isSelected);
		if (parent == null) return;
		if (parent.tickCount % 10 == 0) {
			if (!isSelected) {
				parent.stopIRTone();
				return;
			}
			IRMissile.updateIRTargetsList(parent, targets, getStats().getFlareResistance(), getStats().getFov());
			if (targets.size() > 0) parent.playIRTone();
			else parent.stopIRTone();
		}
	}

}
