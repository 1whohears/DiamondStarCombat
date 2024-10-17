package com.onewhohears.dscombat.client.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.onewhohears.dscombat.client.overlay.components.*;
import net.minecraft.client.Minecraft;

import net.minecraft.client.gui.Font;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

/**
 * Superclass for drawing stuff to the screen are partitioned to have self-containing logic. In other words, the logic
 * for rendering an arbitrary piece of the GUI is in a single class. Remember to include an instance of any subclasses
 * in {@link #registerOverlays(RegisterGuiOverlaysEvent)}.
 * <br><br>
 * Ultimately there is at most one <code>LocalPlayer</code> and <code>Minecraft</code> instance on a client at
 * any given time, so it is probably fine to use instances of this as intrinsic objects in a pseudo-flywheel pattern
 * when information about the client state is needed (extrinsic "object" being the static fields).
 * @author kawaiicakes
 */
import net.minecraft.client.gui.GuiGraphics; // New import for 1.20.1


@OnlyIn(Dist.CLIENT)
public abstract class VehicleOverlayComponent {

    protected static boolean HIDE_GUI = Minecraft.getInstance().options.hideGui;
    protected static Font FONT = Minecraft.getInstance().font;
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

    public static void registerOverlays(RegisterGuiOverlaysEvent event) {
        final VehicleOverlayComponent[] tempArray = new VehicleOverlayComponent[]{
                VehicleOverlayComponent.ManagerTicker.INSTANCE,
                new AimAssistOverlay(),
                new DebugOverlay(),
                new HudOverlay(),
                new KeyBindsOverlay(),
                new PlaneAttitudeOverlay(),
                new PlaneDataOverlay(),
                new RadarOverlay(),
                new TurnCoordinatorOverlay(),
                new VehicleCompassOverlay(),
                new VehicleControlOverlay(),
                new VehicleFuelOverlay(),
                new VehicleStatsOverlay(),
                new VehicleThrottleOverlay(),
                new VehicleWeaponsOverlay()
        };

        for (VehicleOverlayComponent overlay : tempArray) {
            event.registerBelowAll(overlay.componentId(), overlay::register);
        }
    }

    final void register(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        if (!this.shouldRender(gui, graphics, partialTick, screenWidth, screenHeight)) return;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        this.render(gui, graphics, partialTick, screenWidth, screenHeight);
    }

    protected abstract boolean shouldRender(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight);

    protected abstract void render(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight);

    @NotNull
    protected abstract String componentId();

    static class ManagerTicker extends VehicleOverlayComponent {
        static VehicleOverlayComponent INSTANCE = new ManagerTicker();

        @Override
        protected boolean shouldRender(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
            ROOT_VEHICLE = getPlayer() != null ? new WeakReference<>(getPlayer().getRootVehicle()) : null;
            VEHICLE = getPlayer() != null ? new WeakReference<>(getPlayer().getVehicle()) : null;
            return false;
        }

        @Override
        protected void render(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {}

        @Override
        protected @NotNull String componentId() {
            return "dscombat_overlay_manager";
        }
    }
}
