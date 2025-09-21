package com.onewhohears.dscombat.client.event.fabric;

import com.onewhohears.dscombat.client.event.ClientEventHandlers;
import com.onewhohears.dscombat.client.model.obj.ObjWeaponRackModel;
import com.onewhohears.dscombat.client.overlay.OverlayController;
import com.onewhohears.onewholibs.util.math.Mat4f;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public class ClientEventHandlersFabric {

    public static void init() {
        WorldRenderEvents.AFTER_ENTITIES.register(ClientEventHandlersFabric::onRenderLevel);
        ClientEventHandlers.registerParticleProvider();
    }

    public static void onRenderLevel(WorldRenderContext context) {
        OverlayController.PROJECTION_MATRIX = Mat4f.from(context.projectionMatrix());
        ObjWeaponRackModel.renderedRackWeaponNum = 0;
    }

}
