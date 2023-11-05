package com.onewhohears.dscombat.client.overlay;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implementing classes should use a singleton pattern so rendering does not instantiate this repeatedly.
 * <br><br>
 * I've laid it out like this so the logic for a piece of the GUI is all in its own class.
 * Ultimately there is at most one <code>LocalPlayer</code> and <code>Minecraft</code> instance on a client at
 * any given time, so this *should* be fine.
 * @author kawaiicakes
 */
@OnlyIn(Dist.CLIENT)
public abstract class VehicleOverlayComponent extends GuiComponent {
    protected static final int PADDING = 1;

    protected static @NotNull Minecraft getMc() {
        return Minecraft.getInstance();
    }

    @Nullable
    protected static LocalPlayer getPlayer() {
        return getMc().player;
    }

    @Nullable
    protected static Entity getPlayerRootVehicle() {
        return getPlayer() != null ? getPlayer().getRootVehicle() : null;
    }
    
    @Nullable
    protected static Entity getPlayerVehicle() {
        return getPlayer() != null ? getPlayer().getVehicle() : null;
    }

    protected static Font getFont() {
        return Minecraft.getInstance().font;
    }

    protected abstract void render(PoseStack poseStack, int screenWidth, int screenHeight);
}
