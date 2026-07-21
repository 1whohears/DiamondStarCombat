package com.onewhohears.dscombat.entity.ai.nav;

import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import net.minecraft.world.entity.ai.control.Control;
import net.minecraft.world.phys.Vec3;

public class VehicleMoveControl implements Control {

    protected final EntityVehicle vehicle;

    protected double wantedX;
    protected double wantedY;
    protected double wantedZ;

    public VehicleMoveControl(EntityVehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void tick() {
        Vec3 wanted = new Vec3(wantedX, wantedY, wantedZ);
        System.out.println("MOVE CONTROL "+wanted);
    }

    public void setWantedPosition(double wantedX, double wantedY, double wantedZ) {
        this.wantedX = wantedX;
        this.wantedY = wantedY;
        this.wantedZ = wantedZ;
    }
}
