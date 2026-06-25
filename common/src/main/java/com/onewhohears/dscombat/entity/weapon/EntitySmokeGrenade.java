package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.data.weapon.WeaponType;
import com.onewhohears.dscombat.data.weapon.stats.SmokeGrenadeStats;
import com.onewhohears.dscombat.data.weapon.stats.WeaponStats;
import com.onewhohears.dscombat.entity.damagesource.WeaponDamageSource;
import com.onewhohears.dscombat.init.ModEntities;
import com.onewhohears.dscombat.init.ModParticles;
import com.onewhohears.dscombat.init.ModSounds;
import com.onewhohears.dscombat.util.UtilSound;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntitySmokeGrenade extends EntityWeapon<SmokeGrenadeStats> {

    private static final EntityDataAccessor<Integer> SMOKE_AGE =
            SynchedEntityData.defineId(EntitySmokeGrenade.class, EntityDataSerializers.INT);

    public EntitySmokeGrenade(Level level) {
        this(ModEntities.SMOKE_GRENADE.get(), level, "smoke_grenade");
    }

    public EntitySmokeGrenade(EntityType<? extends EntitySmokeGrenade> type, Level level, String defaultId) {
        super(type, level, defaultId);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SMOKE_AGE, -1);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("smokeAge", getSmokeAge());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setSmokeAge(tag.getInt("smokeAge"));
    }

    private int getSmokeAge() { return this.entityData.get(SMOKE_AGE); }
    private void setSmokeAge(int age) { this.entityData.set(SMOKE_AGE, age); }

    @Override
    public WeaponType getWeaponType() { return WeaponType.SMOKE_GRENADE; }

    @Override
    protected WeaponDamageSource getImpactDamageSource() {
        return WeaponDamageSource.WeaponDamageType.BULLET.getSource(getOwner(), this);
    }

    @Override
    protected WeaponDamageSource getExplosionDamageSource() {
        return WeaponDamageSource.WeaponDamageType.BULLET.getSource(getOwner(), this);
    }

    @Override
    public WeaponStats.WeaponClientImpactType getClientImpactType() {
        return WeaponStats.WeaponClientImpactType.SMALL_BULLET_IMPACT;
    }

    @Override
    public float getDamage() { return 0; }

    @Override
    public boolean canBreakFragileBlocks() { return false; }

    @Override
    public int getMaxAge() {
        if (getStats() == null) return Integer.MAX_VALUE;
        return getStats().getMaxAge();
    }

    @Override
    public void tick() {
        // Small tracer during flight
        if (getSmokeAge() < 0 && getWorld().isClientSide() && tickCount > 1) {
            getWorld().addAlwaysVisibleParticle(ParticleTypes.POOF, getX(), getY(), getZ(), 0, 0, 0);
        }

        if (getSmokeAge() >= 0) {
            tickSmoke();
            return;
        }
        super.tick();
        if (!getWorld().isClientSide() && getStats() != null && tickCount >= getStats().getFuseTicks()) {
            detonate();
        }
    }

    private void detonate() {
        setSmokeAge(0);

        // Sparks on detonation (client-side)
        if (getWorld().isClientSide()) {
            for (int i = 0; i < 12; i++) {
                double vx = (random.nextDouble() - 0.5) * 0.4;
                double vy = random.nextDouble() * 0.3 + 0.1;
                double vz = (random.nextDouble() - 0.5) * 0.4;
                getWorld().addParticle(ParticleTypes.CRIT, getX(), getY() + 0.2, getZ(), vx, vy, vz);
            }
        }

        // Deploy sound
        if (!getWorld().isClientSide()) {
            String soundKey = getStats().getDeploySoundKey();
            SoundEvent deploySound = soundKey.isEmpty()
                    ? ModSounds.SMOKE_GRENADE_DEPLOY
                    : UtilSound.getSoundById(soundKey, ModSounds.SMOKE_GRENADE_DEPLOY, getWorld().registryAccess());
            UtilSound.sendDelayedSound((ServerLevel) getWorld(), deploySound, position(), 64, 1f, 1f);
        }

        setDeltaMovement(Vec3.ZERO);
        noPhysics = true;
    }

    private void tickSmoke() {
        int smokeAge = getSmokeAge();
        setSmokeAge(smokeAge + 1);
        smokeAge = getSmokeAge();

        if (getStats() == null) return;
        int duration = getStats().getSmokeDuration();
        float radius = getStats().getSmokeRadius();

        if (getWorld().isClientSide() && smokeAge % 2 == 0) {
            // Spread in all directions: up, down, sides (spherical distribution)
            double speed = 0.04 + random.nextDouble() * 0.06;
            double theta = random.nextDouble() * 2 * Math.PI;
            double phi = Math.acos(2 * random.nextDouble() - 1);
            double vx = speed * Math.sin(phi) * Math.cos(theta);
            double vy = speed * Math.cos(phi) * 0.5; // slightly less vertical
            double vz = speed * Math.sin(phi) * Math.sin(theta);
            double ox = (random.nextDouble() - 0.5) * radius * 0.5;
            double oy = (random.nextDouble() - 0.5) * radius * 0.5;
            double oz = (random.nextDouble() - 0.5) * radius * 0.5;
            getWorld().addAlwaysVisibleParticle(
                ModParticles.SMOKE_GRENADE_CLOUD.get(), true,
                getX() + ox, getY() + oy, getZ() + oz,
                vx, vy, vz
            );
        }

        if (!getWorld().isClientSide() && smokeAge >= duration) {
            discard();
        }
    }

    @Override
    public void kill() {
        if (getSmokeAge() >= 0) { discard(); return; }
        super.kill();
    }

    @Override
    protected void tickCheckCollide() {
        if (getSmokeAge() >= 0) return;
    }
}
