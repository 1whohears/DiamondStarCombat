package com.onewhohears.dscombat.entity.ai.goal;

import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.onewhohears.dscombat.entity.ai.nav.VehicleNavigation.DEFAULT_VEHICLE_FOLLOW_RANGE;

public class VehicleFollowTargetGoal extends Goal {

    private @NotNull final EntityVehicle vehicle;

    private @Nullable LivingEntity target;
    private double wantedX;
    private double wantedY;
    private double wantedZ;

    public VehicleFollowTargetGoal(@NotNull EntityVehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public boolean canUse() {
        if (!vehicle.canAiPilotMoveTick()) return false;
        Mob pilot = vehicle.getPilotAiMob();
        if (pilot == null) return false;
        this.target = pilot.getTarget();
        if (this.target == null) {
            return false;
        } else if (this.target.distanceToSqr(vehicle) > (DEFAULT_VEHICLE_FOLLOW_RANGE * DEFAULT_VEHICLE_FOLLOW_RANGE)) {
            return false;
        } else {
            /*Vec3 vec3 = DefaultRandomPos.getPosTowards(pilot, 16, 7, this.target.position(), (double)((float)Math.PI / 2F));
            if (vec3 == null) {
                return false;
            } else {
                this.wantedX = vec3.x;
                this.wantedY = vec3.y;
                this.wantedZ = vec3.z;
                return true;
            }*/
            this.wantedX = target.getX();
            this.wantedY = target.getY();
            this.wantedZ = target.getZ();
        }
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return vehicle.canAiPilotMoveTick() && !vehicle.pilotAiNavigation.isDone() && target != null && target.isAlive()
                && target.distanceToSqr(vehicle) < (DEFAULT_VEHICLE_FOLLOW_RANGE * DEFAULT_VEHICLE_FOLLOW_RANGE);
    }

    @Override
    public void start() {
        super.start();
        this.vehicle.pilotAiNavigation.moveTo(this.wantedX, this.wantedY, this.wantedZ);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void stop() {
        super.stop();
        target = null;
    }
}
