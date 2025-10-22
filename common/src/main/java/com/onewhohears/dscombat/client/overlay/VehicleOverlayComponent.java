package com.onewhohears.dscombat.client.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.onewhohears.dscombat.client.overlay.components.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

/**
 * Superclass for drawing stuff to the screen are partitioned to have self-containing logic. In other words, the logic
 * for rendering an arbitrary piece of the GUI is in a single class.
 * <br><br>
 * Ultimately there is at most one <code>LocalPlayer</code> and <code>Minecraft</code> instance on a client at
 * any given time, so it is probably fine to use instances of this as intrinsic objects in a pseudo-flywheel pattern
 * when information about the client state is needed (extrinsic "object" being the static fields).
 * @author kawaiicakes
 */
public abstract class VehicleOverlayComponent {

    public static final VehicleOverlayComponent[] OVERLAYS = new VehicleOverlayComponent[]{
            VehicleOverlayComponent.ManagerTicker.INSTANCE,
            new AimAssistOverlay(),
            new DebugOverlay(),
            new HudOverlay(),
            new KeyBindsOverlay(),
            //new PlaneAttitudeOverlay(),
            new PlaneDataOverlay(),
            new RadarOverlay(),
            //new TurnCoordinatorOverlay(),
            new VehicleCompassOverlay(),
            new VehicleControlOverlay(),
            //new VehicleFuelOverlay(),
            new VehicleStatsOverlay(),
            new VehicleThrottleOverlay(),
            new HeliStatusOverlay(),
            new ModernHudOverlay(),
            new VehicleWeaponsOverlay(),
            new PosRangeOverlay()
    };

    protected static boolean HIDE_GUI = Minecraft.getInstance().options.hideGui;
    protected static Font FONT = Minecraft.getInstance().font;
    // caching entities is generally a bad idea
    @Nullable
    protected static WeakReference<Entity> ROOT_VEHICLE;
    @Nullable
    protected static WeakReference<Entity> VEHICLE;
    protected static final int PADDING = 1;

    @Nullable
    protected static LocalPlayer getPlayer() {
        return Minecraft.getInstance().player;
    }
    @Nullable
    protected static Entity getPlayerRootVehicle() {
        if (ROOT_VEHICLE != null) return ROOT_VEHICLE.get();
        return null;
    }
    @Nullable
    protected static Entity getPlayerVehicle() {
        if (VEHICLE != null) return VEHICLE.get();
        return null;
    }
    protected static boolean isInSpectator() {
        if (Minecraft.getInstance().gameMode == null) return true;
        return Minecraft.getInstance().gameMode.getPlayerMode() == GameType.SPECTATOR;
    }
    protected static boolean defaultRenderConditions() {
        if (HIDE_GUI) return true;
        return isInSpectator();
    }

    public static void renderAll(Gui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        for (VehicleOverlayComponent overlay : OVERLAYS) {
            overlay.renderBase(gui, graphics, partialTick, screenWidth, screenHeight);
        }
    }

    void renderBase(Gui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        // TODO: vehicles declare what overlays they render and is taken into account independent of #shouldRender
        if (!this.shouldRender(gui, graphics, partialTick, screenWidth, screenHeight)) return;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        this.render(gui, graphics, partialTick, screenWidth, screenHeight);
    }

    /**
        This should only govern the most general conditions needed for this overlay to work; vehicle-specific requirements
        will be handled by whatever is checking for vehicle matches (L55).
     */
    protected abstract boolean shouldRender(Gui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight);
    protected abstract void render(Gui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight);
    /**
     * @return A short <code>String</code> uniquely identifying this overlay.
     */
    @NotNull
    protected abstract String componentId();

    /**
     * This is a lazy way of letting the superclass gather data lol
     */
    static class ManagerTicker extends VehicleOverlayComponent {
        static VehicleOverlayComponent INSTANCE = new ManagerTicker();

        @Override
        protected boolean shouldRender(Gui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
            ROOT_VEHICLE = getPlayer() != null ? new WeakReference<>(getPlayer().getRootVehicle()) : null;
            VEHICLE = getPlayer() != null ? new WeakReference<>(getPlayer().getVehicle()) : null;
            return false;
        }

        @Override
        protected void render(Gui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {}

        @Override
        protected @NotNull String componentId() {
            return "dscombat_overlay_manager";
        }
    }
}
