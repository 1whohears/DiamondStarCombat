package com.onewhohears.dscombat.entity.ai.goal;

import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import org.jetbrains.annotations.NotNull;

public class VehiclePatrolGoal extends Goal {

    private @NotNull final EntityVehicle vehicle;

    public VehiclePatrolGoal(@NotNull EntityVehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public boolean canUse() {
        return vehicle.canAiPilotMoveTick();
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void stop() {
        super.stop();
    }
}
