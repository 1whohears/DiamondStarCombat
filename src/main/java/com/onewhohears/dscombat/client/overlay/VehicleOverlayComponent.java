package com.onewhohears.dscombat.client.overlay;

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
    @Nullable
    protected static Entity getPlayerVehicle() {
        return Minecraft.getInstance().player != null ? Minecraft.getInstance().player.getRootVehicle() : null;
    }

    public abstract void render();

    public abstract boolean shouldRender();
}
