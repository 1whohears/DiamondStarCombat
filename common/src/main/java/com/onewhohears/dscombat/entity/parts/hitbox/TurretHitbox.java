package com.onewhohears.dscombat.entity.parts.hitbox;

import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.init.ModEntities;
import com.onewhohears.dscombat.util.math.RotableAABB;
import com.onewhohears.onewholibs.util.UtilEntity;
import com.onewhohears.onewholibs.util.math.QuaternionF;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import com.onewhohears.onewholibs.util.math.Vec3f;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

/**
 * A destructible hitbox attached to a turret entity.
 * Follows the turret's rotation (yaw + pitch) unlike vehicle hitboxes which only follow vehicle rotation.
 * Used for ERA/armor plates on turrets.
 */
public class TurretHitbox extends Entity {

    public static final EntityDataAccessor<Float> HEALTH =
            SynchedEntityData.defineId(TurretHitbox.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> ARMOR =
            SynchedEntityData.defineId(TurretHitbox.class, EntityDataSerializers.FLOAT);

    private EntityTurret parent;
    private TurretHitboxData data;
    private RotableAABB hitbox;

    public TurretHitbox(EntityTurret parent, TurretHitboxData data) {
        this(ModEntities.TURRET_HITBOX.get(), parent.level());
        this.parent = parent;
        this.data = data;
        this.noPhysics = true;
        initStats();
        setHealth(data.getMaxHealth());
        setArmor(data.getMaxArmor());
    }

    public TurretHitbox(EntityType<?> type, Level level) {
        super(type, level);
    }

    protected void initStats() {
        if (data == null) return;
        hitbox = new RotableAABB(data.getSize().x(), data.getSize().y(), data.getSize().z());
        refreshDimensions();
    }

    @Override
    public void tick() {
        if (parent == null || parent.isRemoved()) {
            discard();
            return;
        }
        if (data == null || hitbox == null) {
            discard();
            return;
        }
        if (!level().isClientSide() && tickCount > 20 && data.isRemoveOnDestroy() && isDestroyed()) {
            kill();
            return;
        }
        positionSelf();
        firstTick = false;
    }

    /**
     * Positions this hitbox relative to the turret, accounting for turret yaw rotation.
     * The rel_pos is rotated by the turret's world-space quaternion (vehicle Q * turret yaw).
     */
    protected void positionSelf() {
        setOldPosAndRot();
        // Build turret world quaternion: vehicle rotation * turret yaw
        QuaternionF turretQ = getVehicleQ().copy();
        turretQ.mul(Vec3f.YP.rotationDegrees(parent.getRelRotY()));

        Vec3 pos = parent.position().add(UtilAngles.rotateVector(data.getRelPos(), turretQ));
        hitbox.setCenterAndRot(pos, turretQ);
        setPos(pos);
    }

    private QuaternionF getVehicleQ() {
        if (parent.getParentVehicle() != null) {
            return parent.getParentVehicle().getQBySide();
        }
        return QuaternionF.ONE;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (isDestroyed()) return false;
        if (isInvulnerableTo(source)) return false;
        float armorDamage = Math.max(0, amount - getArmor());
        if (getArmor() > 0) {
            addArmor(-amount);
            // ERA: absorb the hit, damage_root=false means no damage passes to turret
            if (!data.isDamageRoot()) return true;
            amount = armorDamage;
        }
        addHealth(-amount);
        if (data.isDamageRoot() && parent != null) {
            parent.hurt(source, amount);
        }
        return true;
    }

    @Override
    protected @NotNull AABB makeBoundingBox() {
        if (hitbox == null) return AABB.ofSize(position(), 1f, 1f, 1f);
        return hitbox.getDisguisedAABB(position());
    }

    @Override
    public boolean isPickable() { return true; }

    @Override
    public boolean isPushable() { return false; }

    @Override
    public boolean canBeCollidedWith() { return !isDestroyed(); }

    @Override
    public boolean shouldRender(double pX, double pY, double pZ) { return false; }

    @Override
    public boolean shouldRenderAtSqrDistance(double pDistance) { return false; }

    @Override
    protected void defineSynchedData() {
        entityData.define(HEALTH, 10f);
        entityData.define(ARMOR, 0f);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag nbt) {}

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag nbt) {}

    @Override
    public boolean shouldBeSaved() { return false; }

    public void readNbt(CompoundTag nbt) {
        CompoundTag tag = nbt.getCompound(getHitboxName());
        if (tag.isEmpty()) return;
        if (tag.contains("health")) setHealth(tag.getFloat("health"));
        if (tag.contains("armor")) setArmor(tag.getFloat("armor"));
    }

    public void writeNbt(CompoundTag nbt) {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("health", getHealth());
        tag.putFloat("armor", getArmor());
        nbt.put(getHitboxName(), tag);
    }

    public String                 getHitboxName() { return data != null ? data.getName() : "unknown"; }
    public TurretHitboxData getData() { return data; }
    public EntityTurret getParent() { return parent; }

    public float getHealth() { return entityData.get(HEALTH); }
    public float getMaxHealth() { return data != null ? data.getMaxHealth() : 0; }
    public float getArmor() { return entityData.get(ARMOR); }
    public float getMaxArmor() { return data != null ? data.getMaxArmor() : 0; }

    public void setHealth(float health) {
        entityData.set(HEALTH, Math.max(0, Math.min(health, getMaxHealth())));
    }

    public void setArmor(float armor) {
        entityData.set(ARMOR, Math.max(0, Math.min(armor, getMaxArmor())));
    }

    public void addHealth(float delta) { setHealth(getHealth() + delta); }
    public void addArmor(float delta) { setArmor(getArmor() + delta); }

    public boolean isDestroyed() { return getHealth() <= 0 && getMaxHealth() > 0; }

    public void fullyRepair() {
        setHealth(getMaxHealth());
        setArmor(getMaxArmor());
    }
}
