package com.onewhohears.dscombat.data.weapon.instance;

import com.onewhohears.dscombat.data.weapon.WeaponShootParameters;
import com.onewhohears.dscombat.data.weapon.stats.DumbTorpedoStats;
import com.onewhohears.dscombat.entity.weapon.EntityDumbTorpedo;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;

public class DumbTorpedoInstance<T extends DumbTorpedoStats> extends MissileInstance<T> {

    public DumbTorpedoInstance(T stats) {
        super(stats);
    }

    @Override
    public EntityWeapon<?> getShootEntity(WeaponShootParameters params) {
        EntityDumbTorpedo<?> missile = (EntityDumbTorpedo<?>) super.getShootEntity(params);
        if (missile == null) return null;
        return missile;
    }
}
