package com.onewhohears.dscombat.client.event;

import com.onewhohears.dscombat.client.model.obj.ObjWeaponRackModel;
import com.onewhohears.dscombat.client.overlay.OverlayController;
import com.onewhohears.onewholibs.util.math.Mat4f;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public class ClientEventHandlersFabric {

    public static void init() {
        WorldRenderEvents.AFTER_ENTITIES.register(ClientEventHandlersFabric::onRenderLevel);
    }

    public static void onRenderLevel(WorldRenderContext context) {
        OverlayController.PROJECTION_MATRIX = Mat4f.from(context.projectionMatrix());
        ObjWeaponRackModel.renderedRackWeaponNum = 0;
    }

}
