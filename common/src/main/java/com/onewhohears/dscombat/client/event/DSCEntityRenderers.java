package com.onewhohears.dscombat.client.event;

import com.onewhohears.dscombat.client.model.obj.custom.ChainHookModel;
import com.onewhohears.dscombat.client.model.obj.custom.GimbalCameraModel;
import com.onewhohears.dscombat.client.renderer.RendererEntityInvisible;
import com.onewhohears.dscombat.client.renderer.RendererObjVehicle;
import com.onewhohears.dscombat.client.renderer.RendererObjWeapon;
import com.onewhohears.dscombat.client.renderer.RendererWindTunnel;
import com.onewhohears.dscombat.init.ModEntities;
import com.onewhohears.onewholibs.client.model.obj.ObjEntityModel;
import com.onewhohears.onewholibs.client.renderer.RendererCustomAnimObjEntity;
import com.onewhohears.onewholibs.client.renderer.RendererObjEntity;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;

public class DSCEntityRenderers {

    public static void register() {
        EntityRendererRegistry.register(ModEntities.PLANE, RendererObjVehicle::new);
        EntityRendererRegistry.register(ModEntities.HELICOPTER, RendererObjVehicle::new);
        EntityRendererRegistry.register(ModEntities.CAR, RendererObjVehicle::new);
        EntityRendererRegistry.register(ModEntities.BOAT, RendererObjVehicle::new);
        EntityRendererRegistry.register(ModEntities.SUBMARINE, RendererObjVehicle::new);
        EntityRendererRegistry.register(ModEntities.STATIONARY, RendererObjVehicle::new);
        // BULLETS
        EntityRendererRegistry.register(ModEntities.BULLET, RendererObjWeapon::new);
        // BOMBS
        EntityRendererRegistry.register(ModEntities.BOMB, RendererObjWeapon::new);
        // BUNKER BUSTERS
        EntityRendererRegistry.register(ModEntities.BUNKER_BUSTER, RendererObjWeapon::new);
        // MISSILES
        EntityRendererRegistry.register(ModEntities.POS_MISSILE, RendererObjWeapon::new);
        EntityRendererRegistry.register(ModEntities.IR_MISSILE, RendererObjWeapon::new);
        EntityRendererRegistry.register(ModEntities.TRACK_MISSILE, RendererObjWeapon::new);
        EntityRendererRegistry.register(ModEntities.ANTI_RADAR_MISSILE, RendererObjWeapon::new);
        EntityRendererRegistry.register(ModEntities.TORPEDO_MISSILE, RendererObjWeapon::new);
        EntityRendererRegistry.register(ModEntities.DUMB_TORPEDO_MISSILE, RendererObjWeapon::new);
        // PARTS
        EntityRendererRegistry.register(ModEntities.TURRET, RendererCustomAnimObjEntity::new);
        EntityRendererRegistry.register(ModEntities.EXTERNAL_WEAPON_PART, RendererCustomAnimObjEntity::new);
        EntityRendererRegistry.register(ModEntities.EXTERNAL_ENGINE, RendererCustomAnimObjEntity::new);
        EntityRendererRegistry.register(ModEntities.EXTERNAL_RADAR, RendererCustomAnimObjEntity::new);
        EntityRendererRegistry.register(ModEntities.EXTERNAL_FUEL_TANK, RendererCustomAnimObjEntity::new);
        // OTHER
        EntityRendererRegistry.register(ModEntities.SEAT, RendererEntityInvisible::new);
        EntityRendererRegistry.register(ModEntities.FLARE, RendererEntityInvisible::new);
        EntityRendererRegistry.register(ModEntities.ROTABLE_HITBOX, RendererEntityInvisible::new);
        EntityRendererRegistry.register(ModEntities.CHAIN_HOOK,
                (context) -> new RendererObjEntity<>(context, new ChainHookModel("chain_hook")));
        EntityRendererRegistry.register(ModEntities.GIMBAL_CAMERA,
                (context) -> new RendererObjEntity<>(context, new GimbalCameraModel()));
        EntityRendererRegistry.register(ModEntities.PARACHUTE,
                (context) -> new RendererObjEntity<>(context, new ObjEntityModel<>("parachute")));
        EntityRendererRegistry.register(ModEntities.WIND_TUNNEL, RendererWindTunnel::new);
    }

}
