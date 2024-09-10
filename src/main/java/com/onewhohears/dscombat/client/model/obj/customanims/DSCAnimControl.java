package com.onewhohears.dscombat.client.model.obj.customanims;

import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.client.model.obj.customanims.keyframe.BasicControllers;
import com.onewhohears.onewholibs.client.model.obj.customanims.keyframe.KeyframeAnimationController;
import com.onewhohears.onewholibs.client.model.obj.customanims.keyframe.KeyframeAnimationTrigger;
import net.minecraft.world.entity.Entity;

public class DSCAnimControl {

    public static float secondsSince(Entity entity, int tick, float partialTicks) {
        return ((float)entity.tickCount - (float)tick + partialTicks) * 0.05f;
    }

    public static KeyframeAnimationController<EntityTurret> TURRET_SHOOT_CONTROL = (entity, partialTicks, length) ->
            secondsSince(entity, entity.getLastShootTick(), partialTicks);

    public static KeyframeAnimationTrigger<EntityTurret> TURRET_SHOOT_TRIGGER = (entity) ->
            secondsSince(entity, entity.getLastShootTick(), 0) < 10;

    public static KeyframeAnimationController<EntityTurret> TURRET_SHOOT_LOOP_CONTROL = BasicControllers.continuous();

    public static KeyframeAnimationTrigger<EntityTurret> TURRET_SHOOT_LOOP_TRIGGER = (entity) ->
            secondsSince(entity, entity.getLastShootTick(), 0) < 0.25f;

    public static KeyframeAnimationController<EntityTurret> TURRET_SHOOT_LOOP_END_CONTROL = (entity, partialTicks, length) ->
            secondsSince(entity, entity.getLastShootTick(), partialTicks);

    public static KeyframeAnimationTrigger<EntityTurret> TURRET_SHOOT_LOOP_END_TRIGGER = (entity) ->
            secondsSince(entity, entity.getLastShootTick(), 0) >= 0.25f;

    public static KeyframeAnimationController<EntityVehicle> LANDING_GEAR_CONTROL = (entity, partialTicks, length) ->
            entity.getLandingGearPos(partialTicks) * length;

    public static KeyframeAnimationTrigger<EntityVehicle> LANDING_GEAR_TRIGGER = (entity) ->
            entity.getLandingGearPos(0) != 0;
}
