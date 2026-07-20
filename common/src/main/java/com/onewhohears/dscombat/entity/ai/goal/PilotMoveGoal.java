package com.onewhohears.dscombat.entity.ai.goal;

import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import org.jetbrains.annotations.NotNull;

public class PilotMoveGoal extends Goal {

    private @NotNull final Mob pilot;
    private @NotNull final EntityVehicle vehicle;

    public PilotMoveGoal(@NotNull Mob pilot, @NotNull EntityVehicle vehicle) {
        this.pilot = pilot;
        this.vehicle = vehicle;
    }

    @Override
    public boolean canUse() {
        return vehicle.isOperational() && vehicle.getCurrentFuel() > 0 && !vehicle.isAllEnginesDamaged();
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public void start() {
        super.start();
        vehicle.startSimulate();
    }

    @Override
    public void tick() {
        super.tick();
        vehicle.startSimulate();
        if (pilot.tickCount % 20 == 0) System.out.println("PILOT MOVE TICK "+pilot+" "+vehicle);
    }

    @Override
    public void stop() {
        super.stop();
        vehicle.stopSimulate();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}
