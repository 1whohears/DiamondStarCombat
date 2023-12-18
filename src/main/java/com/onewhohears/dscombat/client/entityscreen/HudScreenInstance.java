package com.onewhohears.dscombat.client.entityscreen;

import static com.onewhohears.dscombat.DSCombatMod.MODID;

import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class HudScreenInstance extends EntityDynamicScreenInstance {
    protected int renderDelay;

    public static final ResourceLocation HUD_BASE = new ResourceLocation(MODID,
            "textures/ui/hud_overlay_base.png");

    public HudScreenInstance(int id) {
        super("hud", id, HUD_BASE);
        this.renderDelay = 0;
    }

    @Override
    public boolean shouldUpdateTexture(Entity entity) {
        if (Minecraft.getInstance().player == null) return false;
        if (!(entity instanceof EntityVehicle vehicle)) return false;

        this.renderDelay++;
        // if no players are in the vehicle the HUD should not update as frequently to reduce strain on the client render thread
        return vehicle.hasPassenger(entityPredicate -> entityPredicate instanceof Player)
                || this.renderDelay % 10 == 0;
    }

    @Override
    protected void updateTexture(Entity entity) {
        this.clearDynamicPixels();
    }
}
