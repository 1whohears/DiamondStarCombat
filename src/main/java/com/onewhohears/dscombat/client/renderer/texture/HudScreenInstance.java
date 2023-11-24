package com.onewhohears.dscombat.client.renderer.texture;

import com.onewhohears.dscombat.entity.aircraft.EntityPlane;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import static com.onewhohears.dscombat.DSCombatMod.MODID;

public class HudScreenInstance extends EntityDynamicScreenInstance {
    protected int renderDelay;

    public static final ResourceLocation HUD_BASE = new ResourceLocation(MODID,
            "textures/ui/hud_overlay_base");

    public HudScreenInstance(int id) {
        super("hud", id, HUD_BASE);
        this.renderDelay = 0;
    }

    @Override
    public boolean shouldUpdateTexture(Entity entity) {
        if (Minecraft.getInstance().player == null) return false;
        if (!(entity instanceof EntityPlane plane)) return false;

        this.renderDelay++;
        // if no players are in the plane the HUD should not update as frequently to reduce strain on the client render thread
        return plane.hasPassenger(entityPredicate -> entityPredicate instanceof Player)
                || this.renderDelay % 10 == 0;
    }

    @Override
    protected void updateTexture(Entity entity) {
        this.clearDynamicPixels();
    }
}
