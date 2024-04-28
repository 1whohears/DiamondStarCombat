package com.onewhohears.dscombat.client.overlay.components;

import java.util.List;
import java.util.Objects;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.event.forgebus.ClientRenderEvents;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.data.radar.RadarData;
import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.util.UtilEntity;
import com.onewhohears.dscombat.util.math.UtilAngles;
import com.onewhohears.dscombat.util.math.UtilGeometry;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class RadarOverlay extends VehicleOverlayComponent {
    public static final ResourceLocation RADAR = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/radar.png");
    public static final ResourceLocation PING_HUD = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/ping_hud.png");
    public static final ResourceLocation PING_DATA = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/ping_data.png");
    
    public static final int RADAR_SIZE = 120, RADAR_OFFSET = 8;

    protected static final int[] HUD_PING_ANIM = new int[] {0,1,2,3,2,1};

    protected static float PARTIAL_TICK;
    private static RadarOverlay INSTANCE;

    public static void renderIfAllowed(PoseStack poseStack, int screenWidth, int screenHeight, float partialTick) {
        if (Objects.isNull(INSTANCE)) INSTANCE = new RadarOverlay();
        PARTIAL_TICK = partialTick;
        INSTANCE.render(poseStack, screenWidth, screenHeight);
    }

    private RadarOverlay() {}

    @Override
    protected void render(PoseStack poseStack, int screenWidth, int screenHeight) {
        if (!(getPlayerRootVehicle() instanceof EntityVehicle vehicle)) return;
        RadarSystem radar = vehicle.radarSystem;
        if (!radar.hasRadar()) return;
        // LOOK AT PING DATA
        List<RadarData.RadarPing> pings = radar.getClientRadarPings();
        if (pings.isEmpty()) return;
        int selected = radar.getClientSelectedPingIndex();
        int hover = DSCClientInputs.getRadarHoverIndex();
        // PINGS ON SCREEN AND HUD
        Camera cam = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 view = cam.getPosition();
        float z_rot = UtilAngles.lerpAngle(PARTIAL_TICK, vehicle.zRotO, vehicle.zRot);
        poseStack.pushPose();
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(z_rot));
        poseStack.mulPose(Vector3f.XP.rotationDegrees(cam.getXRot()));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(cam.getYRot()+180f));
        poseStack.translate(-view.x, -view.y, -view.z);
        Matrix4f view_mat = poseStack.last().pose().copy();
        poseStack.popPose();
        Matrix4f proj_mat = ClientRenderEvents.getProjMatrix();
        float cursorX = screenWidth / 2F, cursorY = screenHeight / 2F;
        boolean hovering = false;
        int size = Config.CLIENT.radarPingOverlaySize.get();
        for (int i = 0; i < pings.size(); ++i) {
            RadarData.RadarPing ping = pings.get(i);
            // SCREEN
            Vec3 dp = ping.getPosForClient().subtract(vehicle.position());
            double dist = dp.multiply(1, 0, 1).length();
            int hud_ping_offset = 0;
            if (i == selected) {
                //color = 0xff0000;
                hud_ping_offset = size * 4;
            } else if (i == hover) {
                //color = 0xffff00;
                hud_ping_offset = HUD_PING_ANIM[(getPlayer().tickCount/6)%6] * size;
            }
            // HUD
            float[] screen_pos = UtilGeometry.worldToScreenPos(ping.getPosForClient(),
                    view_mat, proj_mat, screenWidth, screenHeight);
            if (screen_pos[0] < 0 || screen_pos[1] < 0) continue;
            float x_win = screen_pos[0], y_win = screen_pos[1];
            float min = 0.2f, max = 0.45f, max_dist = 1000f;
            float scale = (float) Math.max(min, max-(dist/max_dist*(max-min)));
            float adj = size*scale/2f, x_pos = x_win-adj, y_pos = y_win-adj;
            poseStack.pushPose();
            poseStack.translate(x_pos, y_pos, 0);
            poseStack.scale(scale, scale, scale);
            RenderSystem.setShaderTexture(0, PING_HUD);
            blit(poseStack,
                    0, 0, 0, hud_ping_offset,
                    size, size,
                    size, size*5);
            RenderSystem.setShaderTexture(0, PING_DATA);
            blit(poseStack,
                    0, 0, ping.entityType.getIconOffset(size), 0,
                    size, size,
                    size*5, size*2);
            blit(poseStack,
                    0, 0, ping.terrainType.getIconOffset(size), 100,
                    size, size,
                    size*5, size*2);
            if (ping.isFriendly) {
                blit(poseStack,
                        0, 0, size*4, 0,
                        size, size,
                        size*5, size*2);
            }
            if (ping.isShared()) {
                blit(poseStack,
                        0, 0, size*4, size,
                        size, size,
                        size*5, size*2);
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
            RadarData.RadarPing ping = pings.get(hover);
            int dist = (int) ping.getPosForClient().distanceTo(vehicle.position());
            int alt = UtilEntity.getDistFromSeaLevel(ping.getPosForClient().y, vehicle.level);
            WeaponData weapon = vehicle.weaponSystem.getSelected();
            String text = dist + " | " + alt;
            int color = 0xffff00;
            if (weapon != null && weapon.requiresRadar()) {
            	if (dist <= weapon.getMobTurretRange()) {
            		color = 0x00ff00;
            		text += " | O";
            	} else {
            		color = 0xff0000;
            		text += " | X";
            	}
            }
            drawCenteredString(poseStack, getFont(), text, 
                    screenWidth / 2, screenHeight / 2 - 20, color);
        }
    }
}
