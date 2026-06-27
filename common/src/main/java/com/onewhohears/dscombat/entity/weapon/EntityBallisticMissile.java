package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.data.vehicle.physics.DSCPhyCons;
import com.onewhohears.dscombat.data.weapon.WeaponType;
import com.onewhohears.dscombat.data.weapon.stats.MissileStats;
import com.onewhohears.dscombat.entity.damagesource.WeaponDamageSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Ballistic missile: follows a parabolic arc toward targetPos.
 * The arc apex height scales with horizontal distance so short and long shots
 * both look natural. The missile always approaches the target from above.
 */
public class EntityBallisticMissile<T extends MissileStats> extends EntityMissile<T> {

    /**
     * Fraction of horizontal distance used as arc apex height.
     * e.g. 0.4 means a 100-block shot peaks ~40 blocks above the midpoint.
     */
    private static final double ARC_HEIGHT_FACTOR = 0.4;
    /** Minimum apex height above the higher of launch/target Y. */
    private static final double MIN_ARC_HEIGHT = 20.0;

    /** Cached launch position, set on first tickGuide call. */
    private Vec3 launchPos = null;

    public EntityBallisticMissile(EntityType<? extends EntityBallisticMissile<?>> type,
                                   Level level, String defaultWeaponId) {
        super(type, level, defaultWeaponId);
    }

    @Override
    public WeaponType getWeaponType() {
        return WeaponType.BALLISTIC_MISSILE;
    }

    @Override
    public void tickGuide() {
        if (targetPos == null) return;

        // Capture launch position once
        if (launchPos == null) launchPos = position();

        // Horizontal distance from launch to target (XZ plane)
        double dx = targetPos.x - launchPos.x;
        double dz = targetPos.z - launchPos.z;
        double horizDist = Math.sqrt(dx * dx + dz * dz);

        // Apex height: scales with distance, at least MIN_ARC_HEIGHT above the tallest endpoint
        double topY = Math.max(launchPos.y, targetPos.y);
        double apexY = topY + Math.max(MIN_ARC_HEIGHT, horizDist * ARC_HEIGHT_FACTOR);

        // Parametric arc: t=0 at launch, t=1 at target
        // We use a quadratic Bezier with control point at the apex above the midpoint
        double midX = (launchPos.x + targetPos.x) / 2.0;
        double midZ = (launchPos.z + targetPos.z) / 2.0;

        // Current horizontal progress t along the XZ line (clamped 0..1)
        double curDx = getX() - launchPos.x;
        double curDz = getZ() - launchPos.z;
        double t = horizDist > 0.5
                ? Mth.clamp((curDx * dx + curDz * dz) / (horizDist * horizDist), 0.0, 1.0)
                : 1.0;

        // Next t step: advance by one missile-speed unit along the arc
        double speed = getDeltaMovement().length();
        double tStep = horizDist > 0.5 ? speed / horizDist : 0.05;
        double tNext = Mth.clamp(t + tStep, 0.0, 1.0);

        // Quadratic Bezier point at tNext
        // B(t) = (1-t)^2 * P0 + 2(1-t)t * P1 + t^2 * P2
        // P0 = launchPos, P1 = (midX, apexY, midZ), P2 = targetPos
        double inv = 1.0 - tNext;
        double bx = inv * inv * launchPos.x + 2 * inv * tNext * midX      + tNext * tNext * targetPos.x;
        double by = inv * inv * launchPos.y + 2 * inv * tNext * apexY     + tNext * tNext * targetPos.y;
        double bz = inv * inv * launchPos.z + 2 * inv * tNext * midZ      + tNext * tNext * targetPos.z;

        Vec3 arcPoint = new Vec3(bx, by, bz);
        Vec3 dir = arcPoint.subtract(position());

        if (dir.lengthSqr() < 0.01) {
            // Very close to target — home directly
            guideToPosition();
            return;
        }

        // Steer toward the next arc point using the normal turn-rate limiter
        Vec3 goal_dir = dir.normalize();
        Vec3 cur_dir = getLookAngle();
        float deg_diff = (float) com.onewhohears.onewholibs.util.math.UtilGeometry.angleBetweenDegrees(goal_dir, cur_dir);
        float rot = getTurnDegrees();
        if (deg_diff <= rot) {
            setXRot(com.onewhohears.onewholibs.util.math.UtilAngles.getPitch(goal_dir));
            setYRot(com.onewhohears.onewholibs.util.math.UtilAngles.getYaw(goal_dir));
        } else {
            Vec3 P = cur_dir.cross(goal_dir).normalize();
            Vec3 new_dir = com.onewhohears.onewholibs.util.math.UtilAngles.rotateVector(cur_dir,
                    new com.onewhohears.onewholibs.util.math.QuaternionF(
                            com.onewhohears.onewholibs.util.math.UtilGeometry.convertVector(P),
                            rot, true));
            setXRot(com.onewhohears.onewholibs.util.math.UtilAngles.getPitch(new_dir));
            setYRot(com.onewhohears.onewholibs.util.math.UtilAngles.getYaw(new_dir));
        }
    }

    // -------------------------------------------------------------------------
    // Static utility used by MissileLaunchStationBlockEntity for GUI estimates
    // -------------------------------------------------------------------------

    /**
     * Estimates flight distance (blocks) and time (ticks) for display in the GUI.
     * Simple straight-line approximation.
     *
     * @return int[2]: [0] = distance in blocks, [1] = estimated ticks
     */
    public static int[] estimateFlight(Vec3 launchPos, Vec3 targetPos, double missileSpeed) {
        double dist = launchPos.distanceTo(targetPos);
        int ticks = missileSpeed > 0 ? (int) (dist / missileSpeed) : 0;
        return new int[]{ (int) Math.round(dist), ticks };
    }

    @Override
    protected WeaponDamageSource getExplosionDamageSource() {
        return WeaponDamageSource.WeaponDamageType.MISSILE.getSource(getOwner(), this);
    }
}
