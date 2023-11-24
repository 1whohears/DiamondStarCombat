package com.onewhohears.dscombat.client.renderer.texture;

import com.onewhohears.dscombat.data.aircraft.EntityScreenData;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import static com.onewhohears.dscombat.DSCombatMod.MODID;
import static com.onewhohears.dscombat.client.renderer.texture.EntityScreenTypes.HUD_SCREEN;

public class HudScreenInstance extends EntityDynamicScreenInstance {
    public static final EntityScreenData DEFAULT_DATA = new EntityScreenData(
            HUD_SCREEN,
            new Vec3(0.39895, 0.62, 1.63),
            0.1f, 0.1f,
            0, 0, 0
    );

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
