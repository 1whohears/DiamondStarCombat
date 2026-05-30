package com.onewhohears.dscombat.client.entityscreen.instance;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.data.radar.RadarTarget;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.Collection;

import static com.onewhohears.dscombat.util.UtilRender.drawText;

public class BigRadarScreenInstance extends RadarScreenInstance {

    public static final ResourceLocation TEXTURE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/entity_screen/big_radar_screen_bg.png");

    public BigRadarScreenInstance(int id) {
        super("big_radar", id, TEXTURE, 512, 512,
                256, 256, 240, 20);
    }

    @Override
    public void draw(Entity entity, PoseStack poseStack, MultiBufferSource buffer,
                     float partialTicks, int packedLight, float worldWidth, float worldHeight) {
        super.draw(entity, poseStack, buffer, partialTicks, packedLight, worldWidth, worldHeight);
        int range = (int) DSCClientInputs.getRadarDisplayRange();
        String format_range = String.format("DR: %4d", range);
        drawText(UtilMCText.literal(format_range), -0.48f, -0.48f, 0.25f,
                poseStack, buffer, 0x00ff00, packedLight);
    }

    @Override
    protected void updateTexture(Entity entity) {
        clearDynamicPixels();
        // render cool radar line
        int x2 = (int) (centerX + textureRadius * Math.cos(Math.toRadians(-entity.tickCount*4)));
        int y2 = (int) (centerY + textureRadius * Math.sin(Math.toRadians(-entity.tickCount*4)));
        drawLine(centerX, centerY, x2, y2, 9, 0xff00ff00);
        // render radar pings
        EntityVehicle vehicle = (EntityVehicle)entity;
        Collection<RadarTarget> targets = vehicle.radarSystem.getClientRadarPings();
        int selected = vehicle.radarSystem.getClientSelectedTargetId();
        int hover = DSCClientInputs.getRadarHoverId();
        // render all other pings first
        for (RadarTarget target : targets) {
            if (target.entityId == selected || target.entityId == hover) continue;
            drawPing(target, vehicle, false, false);
        }
        // render hover next
        RadarTarget hoverTarget = vehicle.radarSystem.getClientTarget(hover);
        if (hoverTarget != null && hoverTarget.terrainType.isAir())
            drawPing(hoverTarget, vehicle, false, true);
        // render selected last
        RadarTarget selectedTarget = vehicle.radarSystem.getClientTarget(selected);
        if (selectedTarget != null && selectedTarget.terrainType.isAir())
            drawPing(selectedTarget, vehicle, true, false);
    }
}
