package com.onewhohears.dscombat.entity.ai.goal;

import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

import static com.onewhohears.dscombat.item.ItemTicketBook.VEHICLE_SEARCH_RANGE;

public class MoveToPassengerSeatGoal extends Goal {

    private final PathfinderMob mob;
    private EntityVehicle target;
    private Vec3 wanted;

    public MoveToPassengerSeatGoal(PathfinderMob mob, EntityVehicle target) {
        this.mob = mob;
        this.target = target;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (target == null) {
            return false;
        } else if (target.distanceToSqr(mob) > VEHICLE_SEARCH_RANGE * VEHICLE_SEARCH_RANGE) {
            return false;
        } else {
            wanted = target.position();
            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (target == null) return false;
        if (mob.getNavigation().isDone()) {
            target.ridePassengerSeat(mob);
            return false;
        }
        return target.isOperational() && target.hasOpenPassengerSeat() &&
                target.distanceToSqr(mob) <= VEHICLE_SEARCH_RANGE * VEHICLE_SEARCH_RANGE;
    }

    @Override
    public void stop() {
        target = null;
    }

    @Override
    public void start() {
        mob.getNavigation().moveTo(wanted.x(), wanted.y(), wanted.z(), 0.5);
    }
}
