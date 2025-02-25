package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.data.vehicle.DSCPhyCons;
import com.onewhohears.dscombat.data.weapon.WeaponType;
import com.onewhohears.dscombat.data.weapon.stats.DumbTorpedoStats;
import com.onewhohears.dscombat.entity.damagesource.WeaponDamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityDumbTorpedo<T extends DumbTorpedoStats> extends EntityMissile<T> {

    public EntityDumbTorpedo(EntityType<? extends EntityMissile<?>> type, Level level, String defaultWeaponId) {
        super(type, level, defaultWeaponId);
    }

    @Override
    public void tickGuide() {

    }

    @Override
    protected void tickSetMove() {
        if (isInWater()) {
            super.tickSetMove();
            if (isUnderWater()) {
                Vec3 cm = getDeltaMovement();
                double my = Math.min(cm.y + 0.1, 0.4);
                cm = new Vec3(0, my, 0);
                setDeltaMovement(cm);
            }
        } else {
            Vec3 cm = getDeltaMovement();
            cm = cm.add(0, -DSCPhyCons.GRAVITY, 0);
            setDeltaMovement(cm);
        }
    }

    @Override
    public WeaponType getWeaponType() {
        return WeaponType.DUMB_TORPEDO;
    }

    @Override
    public ClipContext.Fluid getFluidClipContext() {
        return ClipContext.Fluid.NONE;
    }

    @Override
    protected WeaponDamageSource getImpactDamageSource() {
        return WeaponDamageSource.WeaponDamageType.MISSILE_CONTACT.getSource(getOwner(), this);
    }

    @Override
    protected WeaponDamageSource getExplosionDamageSource() {
        return WeaponDamageSource.WeaponDamageType.TORPEDO.getSource(getOwner(), this);
    }
}
