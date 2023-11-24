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
    protected int renderDelay;

    public static final ResourceLocation HUD_BASE = new ResourceLocation(MODID,
            "textures/ui/hud_overlay_base.png");

    public HudScreenInstance(int id) {
        super("hud", id, HUD_BASE);
        this.renderDelay = 0;
    }

    /**
     * @param xPos a <code>double</code> corresponding to the x offset of the screen.
     *             At present this number is largely arbitrary and needs to be found
     *             by trial & error. For aircraft whose pilot seat lines up with the
     *             origin of the aircraft, this value will be close to 0.
     */
    // FIXME: pX value is off significantly on my laptop vs. my PC... why? pY to lesser extent
    // (visually the hud no longer lines up when in mouse mode and the rotation of the aircraft changes...)
    public static EntityScreenData getDefaultData(double xPos, double seatY, double seatZ) {
        return new EntityScreenData(
                HUD_SCREEN,
                new Vec3(xPos, seatY + 1.27, seatZ + 0.13),
                0.1f, 0.1f,
                0, 0, 0
        );
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
