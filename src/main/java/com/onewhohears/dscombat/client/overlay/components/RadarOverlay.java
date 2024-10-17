package com.onewhohears.dscombat.client.overlay.components;

import java.util.List;

import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.event.forgebus.ClientRenderEvents;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.data.radar.RadarStats;
import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.data.weapon.instance.WeaponInstance;
import com.onewhohears.dscombat.entity.parts.EntitySeat;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.UtilEntity;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import com.onewhohears.onewholibs.util.math.UtilGeometry;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class RadarOverlay extends VehicleOverlayComponent {
    public static final ResourceLocation PING_HUD = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/ping_hud.png");
    public static final ResourceLocation PING_DATA = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/ping_data.png");
    protected static final int[] HUD_PING_ANIM = new int[] {0,1,2,3,2,1};
    protected static float PARTIAL_TICK;

    @Override
    protected boolean shouldRender(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        if (defaultRenderConditions()) return false;
        if (!(getPlayerVehicle() instanceof EntitySeat seat)) return false;
        EntityVehicle vehicle = seat.getParentVehicle();
        if (vehicle == null) return false;
        RadarSystem radar = vehicle.radarSystem;
        if (!radar.hasRadar()) return false;
        PARTIAL_TICK = partialTick;
        // LOOK AT PING DATA
        List<RadarStats.RadarPing> pings = radar.getClientRadarPings();
        return !pings.isEmpty();
    }

    @Override
    protected void render(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        EntitySeat seat = (EntitySeat) getPlayerVehicle();
        assert seat != null;

        EntityVehicle vehicle = seat.getParentVehicle();
        assert vehicle != null;

        RadarSystem radar = vehicle.radarSystem;
        List<RadarStats.RadarPing> pings = radar.getClientRadarPings();

        int selected = radar.getClientSelectedPingIndex();
        int hover = DSCClientInputs.getRadarHoverIndex();

        // PINGS ON SCREEN AND HUD
        Camera cam = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 view = cam.getPosition();
        float z_rot = UtilAngles.lerpAngle(partialTick, vehicle.zRotO, vehicle.zRot);

        // Push matrix and apply transformations
        graphics.pose().pushPose();
        graphics.pose().mulPose(new Quaternionf().rotateZ((float) Math.toRadians(z_rot)));
        graphics.pose().mulPose(new Quaternionf().rotateX((float) Math.toRadians(cam.getXRot())));
        graphics.pose().mulPose(new Quaternionf().rotateY((float) Math.toRadians(cam.getYRot() + 180f)));
        graphics.pose().translate(-view.x, -view.y, -view.z);

        Matrix4f view_mat = new Matrix4f(graphics.pose().last().pose());
        graphics.pose().popPose();

        Matrix4f proj_mat = ClientRenderEvents.getProjMatrix();
        float cursorX = screenWidth / 2F, cursorY = screenHeight / 2F;
        boolean hovering = false;
        int size = Config.CLIENT.radarPingOverlaySize.get();

        // Iterate over radar pings and render them
        for (int i = 0; i < pings.size(); ++i) {
            RadarStats.RadarPing ping = pings.get(i);

            // SCREEN
            Vec3 dp = ping.getPosForClient().subtract(vehicle.position());
            double dist = dp.multiply(1, 0, 1).length();
            int hud_ping_offset = 0;

            if (i == selected) {
                hud_ping_offset = size * 4;
            } else if (i == hover) {
                hud_ping_offset = HUD_PING_ANIM[(getPlayer().tickCount / 6) % 6] * size;
            }

            // HUD
            float[] screen_pos = UtilGeometry.worldToScreenPos(ping.getPosForClient(),
                    view_mat, proj_mat, screenWidth, screenHeight);

            if (screen_pos[0] < 0 || screen_pos[1] < 0) continue;
            float x_win = screen_pos[0], y_win = screen_pos[1];

            float min = 0.2f, max = 0.45f, max_dist = 1000f;
            float scale = (float) Math.max(min, max - (dist / max_dist * (max - min)));
            float adj = size * scale / 2f, x_pos = x_win - adj, y_pos = y_win - adj;

            // Render ping
            graphics.pose().pushPose();
            graphics.pose().translate(x_pos, y_pos, 0);
            graphics.pose().scale(scale, scale, scale);
            RenderSystem.setShaderTexture(0, PING_HUD);

            graphics.blit(PING_HUD,
                    0, 0, 0, hud_ping_offset,
                    size, size,
                    size, size * 5);

            RenderSystem.setShaderTexture(0, PING_DATA);

            graphics.blit(PING_DATA,
                    0, 0, ping.entityType.getIconOffset(size), 0,
                    size, size,
                    size * 5, size * 2);

            graphics.blit(PING_DATA,
                    0, 0, ping.terrainType.getIconOffset(size), 100,
                    size, size,
                    size * 5, size * 2);

            if (ping.isFriendly) {
                graphics.blit(PING_DATA,
                        0, 0, size * 4, 0,
                        size, size,
                        size * 5, size * 2);
            }

            if (ping.isShared()) {
                graphics.blit(PING_DATA,
                        0, 0, size * 4, size,
                        size, size,
                        size * 5, size * 2);
            }

            graphics.pose().popPose();

            if (!hovering && cursorX < x_win + adj && cursorX > x_win - adj
                    && cursorY < y_win + adj && cursorY > y_win - adj) {
                DSCClientInputs.setRadarHoverIndex(i);
                hovering = true;
            }
        }

        if (!hovering) DSCClientInputs.resetRadarHoverIndex();

        // LOOK AT PING DATA LAYER ORDER FIX
        if (hover != -1 && hover < pings.size()) {
            RadarStats.RadarPing ping = pings.get(hover);
            int dist = (int) ping.getPosForClient().distanceTo(vehicle.position());
            int alt = UtilEntity.getDistFromSeaLevel(ping.getPosForClient().y, vehicle.level());
            String text = dist + " | " + alt;
            int color = 0xffff00;

            WeaponInstance<?> weapon = null;
            if (seat.isTurret()) weapon = ((EntityTurret) seat).getWeaponData();
            else if (seat.canPassengerShootParentWeapon()) weapon = vehicle.weaponSystem.getSelected();

            if (weapon != null && weapon.getStats().requiresRadar()) {
                if (dist <= weapon.getStats().getMobTurretRange()) {
                    color = 0x00ff00;
                    text += " | O";
                } else {
                    color = 0xff0000;
                    text += " | X";
                }
            }

            graphics.drawCenteredString(FONT, text,
                    screenWidth / 2, screenHeight / 2 - 20, color);
        }
    }




    @Override
    protected @NotNull String componentId() {
        return "dscombat_radar";
    }
}
