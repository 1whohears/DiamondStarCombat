package com.onewhohears.dscombat.client.overlay;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

/**
 * Implementations are encouraged to 'feed' data into method bodies directly as opposed to passing them in
 * calls. Anyhow, I've laid it out like this so the logic for a piece of the GUI is all in its own class.
 * Ultimately there is at most one <code>LocalPlayer</code> and <code>Minecraft</code> instance on a client at
 * any given time, so this *should* be fine.
 * @author kawaiicakes
 */
@OnlyIn(Dist.CLIENT)
public abstract class VehicleOverlayComponent extends GuiComponent {
    protected final int screenWidth, screenHeight;
    protected static final int PADDING = 1;

    @Nullable
    protected static Entity getPlayerVehicle() {
        return Minecraft.getInstance().player != null ? Minecraft.getInstance().player.getRootVehicle() : null;
    }

    public VehicleOverlayComponent(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public abstract void render(PoseStack poseStack);

    public abstract boolean shouldRender();
}
