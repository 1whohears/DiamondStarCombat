package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.client.overlay.OverlayController;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.data.radar.RadarStats;
import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.data.weapon.instance.WeaponInstance;
import com.onewhohears.dscombat.entity.parts.EntityRidablePart;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.UtilEntity;
import com.onewhohears.onewholibs.util.math.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RadarOverlay extends VehicleOverlayComponent {
    public static final ResourceLocation PING_HUD = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/ping_hud.png");
    public static final ResourceLocation PING_DATA = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/ping_data.png");
    public static final ResourceLocation PING_ICONS = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/ping_data_icons_color.png");
    public static final int ICON_SIZE = 16, ICON_WIDTH = 240, DEFAULT_SIZE = 100;
    protected static final int[] HUD_PING_ANIM = new int[] {0,1,2,3,2,1};
    protected static float PARTIAL_TICK;

    @Override
    protected boolean shouldRender(Gui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
        if (defaultRenderConditions()) return false;
        if (Minecraft.getInstance().screen != null) return false;
        if (!(getPlayerVehicle() instanceof EntityRidablePart seat)) return false;
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
    protected void render(Gui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
        EntityRidablePart seat = (EntityRidablePart) getPlayerVehicle();
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
        float z_rot = UtilAngles.lerpAngle(PARTIAL_TICK, vehicle.zRotO, vehicle.zRot);
        poseStack.pushPose();
        QuaternionF q = Vec3f.ZP.rotationDegrees(z_rot);
        q.mul(Vec3f.XP.rotationDegrees(cam.getXRot()));
        q.mul(Vec3f.YP.rotationDegrees(cam.getYRot()+180f));
        poseStack.mulPose(q.convert());
        poseStack.translate(-view.x, -view.y, -view.z);
        Mat4f view_mat = Mat4f.from(poseStack.last().pose().copy());
        poseStack.popPose();
        Mat4f proj_mat = OverlayController.PROJECTION_MATRIX;
        float cursorX = screenWidth / 2F, cursorY = screenHeight / 2F;
        boolean hovering = false;
        int size = Config.CLIENT.radarPingOverlaySize.get();
        int icon_size = ICON_SIZE * size / DEFAULT_SIZE * 7 / 4;
        int halfSize = size / 2, halfIconSize = icon_size / 2, sizeFraction = -icon_size / 4;
        int iconLeft = halfIconSize + sizeFraction;
        int iconMid = halfSize - halfIconSize;
        int iconRight = size - icon_size - halfIconSize - sizeFraction;
        float min = 0.2f, max = 0.45f, max_dist = 1000f;
        for (int i = 0; i < pings.size(); ++i) {
            RadarStats.RadarPing ping = pings.get(i);
            // SCREEN
            Vec3 dp = ping.getPosForClient().subtract(vehicle.position());
            double dist = dp.multiply(1, 0, 1).length();
            int hud_ping_offset = 0;
            if (i == selected) {
                //color = 0xff0000;
                hud_ping_offset = size * 4;
            } else if (i == hover) {
                //color = 0xffff00;
                assert getPlayer() != null;
                hud_ping_offset = HUD_PING_ANIM[(getPlayer().tickCount/6)%6] * size;
            }
            // HUD
            float[] screen_pos = UtilGeometry.worldToScreenPos(ping.getPosForClient(),
                    view_mat, proj_mat, screenWidth, screenHeight);
            if (screen_pos[0] < 0 || screen_pos[1] < 0) continue;
            float x_win = screen_pos[0], y_win = screen_pos[1];
            float scale = (float) Math.max(min, max-(dist/max_dist*(max-min)));
            float adj = size*scale/2f, x_pos = x_win-adj, y_pos = y_win-adj;
            poseStack.pushPose();
            poseStack.translate(x_pos, y_pos, 0);
            poseStack.scale(scale, scale, scale);
            if (!ping.entityType.isMissile()) {
                RenderSystem.setShaderTexture(0, PING_HUD);
                blit(poseStack,
                        0, 0, 0, hud_ping_offset,
                        size, size, size, size * 5);
            }
            RenderSystem.setShaderTexture(0, PING_ICONS);
            if (ping.entityType.isMissile()) {
                blit(poseStack, iconMid, iconMid, icon_size, icon_size,
                        ICON_SIZE * 5, ICON_SIZE,
                        ICON_SIZE, ICON_SIZE, ICON_WIDTH, ICON_SIZE);
            }
            blit(poseStack, iconLeft, iconMid, icon_size, icon_size,
                    ping.entityType.getIconIndex() * ICON_SIZE, 0,
                    ICON_SIZE, ICON_SIZE, ICON_WIDTH, ICON_SIZE);
            blit(poseStack, iconRight, iconMid, icon_size, icon_size,
                    ping.terrainType.getIconIndex() * ICON_SIZE, 0,
                    ICON_SIZE, ICON_SIZE, ICON_WIDTH, ICON_SIZE);
            if (ping.isFriendly) {
                blit(poseStack, iconMid, iconLeft, icon_size, icon_size,
                        ICON_SIZE * 4, 0,
                        ICON_SIZE, ICON_SIZE, ICON_WIDTH, ICON_SIZE);
            }
            if (ping.isShared()) {
                blit(poseStack, iconMid, iconRight, icon_size, icon_size,
                        ICON_SIZE * 9, 0,
                        ICON_SIZE, ICON_SIZE, ICON_WIDTH, ICON_SIZE);
            }
            poseStack.popPose();
            if (!hovering && cursorX < x_win+adj && cursorX > x_win-adj
                    && cursorY < y_win+adj && cursorY > y_win-adj) {
                DSCClientInputs.setRadarHoverIndex(i);
                hovering = true;
            }
        }
        if (!hovering) DSCClientInputs.resetRadarHoverIndex();
        // LOOK AT PING DATA LAYER ORDER FIX
        if (hover != -1 && hover < pings.size()) {
            RadarStats.RadarPing ping = pings.get(hover);
            int dist = (int) ping.getPosForClient().distanceTo(vehicle.position());
            int alt = UtilEntity.getDistFromSeaLevel(ping.getPosForClient().y, vehicle.getWorld());
            String text = dist + " | " + alt;
            int color = 0xffff00;
            WeaponInstance<?> weapon = null;
            if (seat.isTurret()) weapon = ((EntityTurret)seat).getWeaponData(); 
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
            drawCenteredString(poseStack, FONT, text,
                    screenWidth / 2, screenHeight / 2 - 20, color);
        }
    }

    @Override
    protected @NotNull String componentId() {
        return "dscombat_radar";
    }
}
