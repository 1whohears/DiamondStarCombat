package com.onewhohears.dscombat.data.parts.instance;

import com.onewhohears.dscombat.data.parts.stats.SmokeGrenadeDispenserStats;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.weapon.EntitySmokeGrenade;
import com.onewhohears.dscombat.init.ModSounds;
import com.onewhohears.dscombat.util.UtilSound;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SmokeGrenadeDispenserInstance extends PartInstance<SmokeGrenadeDispenserStats> {

    private int grenades = 0;

    public SmokeGrenadeDispenserInstance(SmokeGrenadeDispenserStats stats) {
        super(stats);
    }

    @Override
    public void setFilled(String param) {
        super.setFilled(param);
        grenades = getStats().getMaxGrenades();
    }

    @Override
    public void readNBT(CompoundTag tag) {
        super.readNBT(tag);
        grenades = tag.getInt("grenades");
    }

    @Override
    public CompoundTag writeNBT() {
        CompoundTag tag = super.writeNBT();
        tag.putInt("grenades", grenades);
        return tag;
    }

    @Override
    public void readBuffer(FriendlyByteBuf buffer) {
        super.readBuffer(buffer);
        grenades = buffer.readInt();
    }

    @Override
    public void writeBuffer(FriendlyByteBuf buffer) {
        super.writeBuffer(buffer);
        buffer.writeInt(grenades);
    }

    public int getGrenades() { return grenades; }

    public void addGrenades(int n) {
        grenades = Math.max(0, Math.min(grenades + n, getStats().getMaxGrenades()));
        setDirty();
    }

    /**
     * Fires all launchers. Called server-side from EntityVehicle.fireSmokeGrenades().
     * @return true if at least one grenade was fired
     */
    public boolean fire(Entity owner, EntityVehicle vehicle, boolean consume) {
        if (isDamaged()) return false;
        if (getParent() == null) return false;
        if (grenades <= 0) return false;

        Level level = getParent().getWorld();
        SmokeGrenadeDispenserStats stats = getStats();

        for (SmokeGrenadeDispenserStats.LauncherDef launcher : stats.getLaunchers()) {
            Vec3 worldPos = vehicle.position()
                    .add(UtilAngles.rotateVector(
                            getRelPos().add(launcher.pos), vehicle.getQ()));
            float yaw   = vehicle.getYRot() + launcher.yaw;
            float pitch = -launcher.pitch;
            Vec3 dir = UtilAngles.rotationToVector(yaw, pitch);

            EntitySmokeGrenade grenade = new EntitySmokeGrenade(level);
            grenade.setPreset("smoke_grenade");
            grenade.setOwner(owner);
            grenade.setPos(worldPos);
            grenade.setXRot(pitch);
            grenade.setYRot(yaw);
            // Inherit vehicle velocity + launch impulse
            grenade.setDeltaMovement(vehicle.getDeltaMovement().add(dir.scale(0.5)));
            level.addFreshEntity(grenade);
        }

        // Shoot sound
        String soundKey = stats.getDeploySoundKey();
        SoundEvent shootSound = soundKey.isEmpty()
                ? ModSounds.SMOKE_GRENADE_SHOOT
                : UtilSound.getSoundById(soundKey, ModSounds.SMOKE_GRENADE_SHOOT, level.registryAccess());
        UtilSound.sendDelayedSound((ServerLevel) level, shootSound, vehicle.position(), 64, 1f, 1f);

        if (consume) addGrenades(-1);
        setDirty();
        return true;
    }
}
