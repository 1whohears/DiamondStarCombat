package com.onewhohears.dscombat.client.event;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.client.model.obj.ObjWeaponRackModel;
import com.onewhohears.dscombat.client.overlay.OverlayController;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.math.Mat4f;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

import static net.minecraftforge.client.gui.overlay.VanillaGuiOverlay.*;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandlersForge {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void playerRenderPre(RenderPlayerEvent.Pre event) {
        ClientRenderEventHandlers.onRenderPlayerPre(event.getEntity(), event.getPartialTick(), event.getPoseStack());
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void playerRenderPost(RenderPlayerEvent.Post event) {
        ClientRenderEventHandlers.onRenderPlayerPost(event.getEntity());
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        event.setCanceled(ClientRenderEventHandlers.isCancelRenderHand());
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void getViewMatrices(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) return;
        OverlayController.PROJECTION_MATRIX = Mat4f.from(event.getProjectionMatrix());
        ObjWeaponRackModel.renderedRackWeaponNum = 0;
    }

    // TODO: register our overlays under more IDs in case modders/us in the future need to selectively disable overlays
    // TODO: add some config to allow disabling other mods' overlays
    // FIXME a fabric equivalent of onRenderGui has not yet been implemented
    @SubscribeEvent
    public static void onRenderGui(RenderGuiOverlayEvent.Pre event) {
        if (!(Minecraft.getInstance().player.getRootVehicle() instanceof EntityVehicle vehicle)) return;
        if (DSCClientInputs.isCameraFree()) return;

        if (Objects.equals(event.getOverlay().id(), HOTBAR.id())) event.setCanceled(true);
        if (Objects.equals(event.getOverlay().id(), CROSSHAIR.id())) event.setCanceled(true);
        if (Objects.equals(event.getOverlay().id(), PLAYER_HEALTH.id())) event.setCanceled(true);
        if (Objects.equals(event.getOverlay().id(), ARMOR_LEVEL.id())) event.setCanceled(true);
        if (Objects.equals(event.getOverlay().id(), FOOD_LEVEL.id())) event.setCanceled(true);
        if (Objects.equals(event.getOverlay().id(), EXPERIENCE_BAR.id())) event.setCanceled(true);
        if (Objects.equals(event.getOverlay().id(), ITEM_NAME.id())) event.setCanceled(true);
    }

}
