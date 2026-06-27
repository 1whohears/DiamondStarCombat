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
        net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback.EVENT.register((dispatcher, ctx, env) -> {
            new com.onewhohears.dscombat.client.command.GenerateTrackPathCommand(dispatcher);
            new com.onewhohears.dscombat.client.command.TrackPointCommand(dispatcher);
        });
    }

    public static void onRenderLevel(WorldRenderContext context) {
        OverlayController.PROJECTION_MATRIX = Mat4f.from(context.projectionMatrix());
        ObjWeaponRackModel.renderedRackWeaponNum = 0;
        
        // Render track marks
        com.onewhohears.dscombat.client.renderer.TrackMarkManager.renderTrackMarks(
            context.matrixStack(),
            context.consumers(),
            context.camera().getPosition(),
            (int)(context.world().getGameTime() & 0xFFFFFF)
        );
    }

}
