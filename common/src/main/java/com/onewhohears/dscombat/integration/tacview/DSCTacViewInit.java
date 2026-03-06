package com.onewhohears.dscombat.integration.tacview;

import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.ModEntities;
import com.onewhohears.tacview.common.core.EntityRecorders;

public class DSCTacViewInit {

    public static void registerDSCRecorders() {
        EntityRecorders.registerEntityRecorder(ModEntities.PLANE.get(),
                (entity, recordRate) -> new VehicleRecorder.Generic((EntityVehicle) entity, recordRate),
                VehicleRecorder.Generic::new);
        EntityRecorders.registerEntityRecorder(ModEntities.HELICOPTER.get(),
                (entity, recordRate) -> new VehicleRecorder.Generic((EntityVehicle) entity, recordRate),
                VehicleRecorder.Generic::new);
        EntityRecorders.registerEntityRecorder(ModEntities.CAR.get(),
                (entity, recordRate) -> new VehicleRecorder.Generic((EntityVehicle) entity, recordRate),
                VehicleRecorder.Generic::new);
        EntityRecorders.registerEntityRecorder(ModEntities.STATIONARY.get(),
                (entity, recordRate) -> new VehicleRecorder.Generic((EntityVehicle) entity, recordRate),
                VehicleRecorder.Generic::new);
        EntityRecorders.registerEntityRecorder(ModEntities.BOAT.get(),
                (entity, recordRate) -> new VehicleRecorder.Generic((EntityVehicle) entity, recordRate),
                VehicleRecorder.Generic::new);
        EntityRecorders.registerEntityRecorder(ModEntities.SUBMARINE.get(),
                (entity, recordRate) -> new VehicleRecorder.Generic((EntityVehicle) entity, recordRate),
                VehicleRecorder.Generic::new);
    }

}
