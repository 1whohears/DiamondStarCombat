package com.onewhohears.dscombat.client.event.fabric;

import com.onewhohears.dscombat.client.event.ClientCameraEventHandlers;
import com.onewhohears.dscombat.client.model.obj.ObjWeaponRackModel;
import com.onewhohears.dscombat.client.overlay.OverlayController;
import com.onewhohears.onewholibs.util.math.Mat4f;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Camera;

public class ClientEventHandlersFabric {

    public static void init() {
        WorldRenderEvents.AFTER_ENTITIES.register(ClientEventHandlersFabric::onRenderLevel);
    }

    public static void onRenderLevel(WorldRenderContext context) {
        OverlayController.PROJECTION_MATRIX = Mat4f.from(context.projectionMatrix());
        ObjWeaponRackModel.renderedRackWeaponNum = 0;
    }

    public static void onCameraSetupEvent(Camera camera, float partialTick, float yaw, float pitch,
                                          ClientCameraEventHandlers.CameraAngles cameraAngles) {
        cameraAngles.reset();
        ClientCameraEventHandlers.onSetupCameraAngles(camera, partialTick, yaw, pitch, cameraAngles);
    }

}
