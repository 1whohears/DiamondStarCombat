package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.event.forgebus.ClientInputEvents;
import com.onewhohears.dscombat.client.event.forgebus.ClientRenderEvents;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.data.radar.RadarData;
import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.util.math.UtilAngles;
import com.onewhohears.dscombat.util.math.UtilGeometry;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class RadarOverlay extends VehicleOverlayComponent {
    public static final ResourceLocation RADAR = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/radar.png");
    public static final ResourceLocation PING_HUD = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/ping_hud.png");
    public static final ResourceLocation PING_DATA = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/ping_data.png");
    
    public static final int RADAR_SIZE = 120, RADAR_OFFSET = 8;

    protected static final int[] HUD_PING_ANIM = new int[] {0,100,200,300,200,100};

    protected final float partialTick;
    
    public RadarOverlay(int screenWidth, int screenHeight, float partialTick) {
        super(screenWidth, screenHeight);
        this.partialTick = partialTick;
    }

    @Override
    public void render(PoseStack poseStack) {
        if (!(getPlayerVehicle() instanceof EntityAircraft vehicle)) return;
        RadarSystem radar = vehicle.radarSystem;
        if (!radar.hasRadar()) return;
        
        // RADAR BACKDROP
        RenderSystem.setShaderTexture(0, RADAR);
        blit(poseStack,
                RADAR_OFFSET, this.screenHeight - RADAR_OFFSET - RADAR_SIZE,
                0, 0, RADAR_SIZE, RADAR_SIZE,
                RADAR_SIZE, RADAR_SIZE);
        
        int searchRadius = RADAR_SIZE / 2;
        int cx = RADAR_OFFSET + searchRadius;
        int cy = this.screenHeight - RADAR_OFFSET - searchRadius - 5;
        double displayRange = 1000;
        
        // CARDINAL
        int card_color = 0x0000ff, searchRadius2 = searchRadius + 4;
        float card_yaw = -vehicle.getYRot() * Mth.DEG_TO_RAD;
        drawCenteredString(poseStack, getFont(), "S",
                cx + (int) (Mth.sin(card_yaw) * searchRadius2),
                cy - (int) (Mth.cos(card_yaw) * searchRadius2),
                card_color);
        
        card_yaw = (180 - vehicle.getYRot()) * Mth.DEG_TO_RAD;
        drawCenteredString(poseStack, getFont(), "N",
                cx + (int) (Mth.sin(card_yaw) * searchRadius2),
                cy- (int) (Mth.cos(card_yaw) * searchRadius2),
                card_color);
        
        card_yaw = (270 - vehicle.getYRot()) * Mth.DEG_TO_RAD;
        drawCenteredString(poseStack, getFont(), "E",
                cx + (int) (Mth.sin(card_yaw) * searchRadius2),
                cy - (int) (Mth.cos(card_yaw) * searchRadius2),
                card_color);
        
        card_yaw = (90 - vehicle.getYRot()) * Mth.DEG_TO_RAD;
        drawCenteredString(poseStack, getFont(), "W",
                cx + (int) (Mth.sin(card_yaw) * searchRadius2),
                cy - (int) (Mth.cos(card_yaw) * searchRadius2),
                card_color);
        
        // ROLL PITCH YAW
        int heading = (int) vehicle.getYRot();
        if (heading < 0) heading += 360;
        int pitch = (int) -vehicle.getXRot();
        int roll = (int) vehicle.zRot;
        drawCenteredString(poseStack, getFont(),
                "P: "+pitch+" Y: "+heading+" R: "+roll, cx, this.screenHeight - RADAR_OFFSET - RADAR_SIZE -10, 0x8888ff);

        // RWR
        for (RadarSystem.RWRWarning warn : radar.getClientRWRWarnings()) {
            Vec3 dp = warn.pos.subtract(vehicle.position());
            float yaw = (UtilAngles.getYaw(dp)-vehicle.getYRot())*Mth.DEG_TO_RAD;
            int x = cx + (int)(Mth.sin(yaw)*searchRadius);
            int y = cy - (int)(Mth.cos(yaw)*searchRadius);
            int color = 0xffff00;
            String symbol = "W";
            if (warn.isMissile) {
                color = 0xff0000;
                symbol = "M";
            }
            drawCenteredString(poseStack, getFont(),
                    symbol, x, y, color);
        }
        // LOOK AT PING DATA
        List<RadarData.RadarPing> pings = radar.getClientRadarPings();
        if (pings.isEmpty()) return;
        int selected = radar.getClientSelectedPingIndex();
        int hover = ClientInputEvents.getHoverIndex();

        if (hover != -1 && hover < pings.size()) {
            RadarData.RadarPing ping = pings.get(hover);
            String text = "(" + (int) ping.pos.distanceTo(vehicle.position())
                    + " | " + (int) ping.pos.y + ")";
            drawCenteredString(poseStack, getFont(),
                    text, this.screenWidth / 2, this.screenHeight / 2 - 20, 0xffff00);
        }

        if (selected != -1 && selected < pings.size()) {
            RadarData.RadarPing ping = pings.get(selected);
            String text = "(" + (int) ping.pos.distanceTo(vehicle.position())
                    + " | " + (int) ping.pos.y + ")";
            int color = 0xff0000;
            if (ping.isFriendly) color = 0x0000ff;
            drawCenteredString(poseStack, getFont(),
                    text, cx, this.screenHeight - RADAR_OFFSET - RADAR_SIZE - 20, color);
        }

        // PINGS ON SCREEN AND HUD
        Camera cam = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 view = cam.getPosition();
        float z_rot = UtilAngles.lerpAngle(this.partialTick, vehicle.zRotO, vehicle.zRot);
        poseStack.pushPose();
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(z_rot));
        poseStack.mulPose(Vector3f.XP.rotationDegrees(cam.getXRot()));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(cam.getYRot()+180f));
        poseStack.translate(-view.x, -view.y, -view.z);
        Matrix4f view_mat = poseStack.last().pose().copy();
        poseStack.popPose();
        Matrix4f proj_mat = ClientRenderEvents.getProjMatrix();
        float cursorX = this.screenWidth / 2F, cursorY = this.screenHeight / 2F;
        boolean hovering = false;
        for (int i = 0; i < pings.size(); ++i) {
            RadarData.RadarPing ping = pings.get(i);
            // SCREEN
            Vec3 dp = ping.pos.subtract(vehicle.position());
            double dist = dp.multiply(1, 0, 1).length();
            double screen_dist = dist/displayRange;
            if (screen_dist > 1) screen_dist = 1;
            float yaw = (UtilAngles.getYaw(dp)-vehicle.getYRot())*Mth.DEG_TO_RAD;
            int x = cx + (int)(Mth.sin(yaw)*searchRadius*screen_dist);
            int y = cy + (int)(-Mth.cos(yaw)*searchRadius*screen_dist);
            int color = 0x00ff00;
            String symbol = "o";
            if (ping.isFriendly) symbol = "F";
            int hud_ping_offset = 0;
            if (i == selected) {
                color = 0xff0000;
                hud_ping_offset = 400;
            } else if (i == hover) {
                color = 0xffff00;
                hud_ping_offset = HUD_PING_ANIM[(Minecraft.getInstance().player.tickCount/6)%6];
            }
            else if (ping.isFriendly) color = 0x0000ff;
            else if (ping.isShared()) color = 0x66cdaa;
            drawCenteredString(poseStack, getFont(),
                    symbol, x, y, color);
            // HUD
            float[] screen_pos = UtilGeometry.worldToScreenPos(ping.pos,
                    view_mat, proj_mat, this.screenWidth, this.screenHeight);
            if (screen_pos[0] < 0 || screen_pos[1] < 0) continue;
            float x_win = screen_pos[0], y_win = this.screenHeight - screen_pos[1];
            int size = 100;
            float min = 0.2f, max = 0.45f, max_dist = 1000f;
            float scale = (float) Math.max(min, max-(dist/max_dist*(max-min)));
            float adj = size*scale/2f, x_pos = x_win-adj, y_pos = y_win-adj;
            poseStack.pushPose();
            poseStack.translate(x_pos, y_pos, 0);
            poseStack.scale(scale, scale, scale);
            RenderSystem.setShaderTexture(0, PING_HUD);
            blit(poseStack,
                    0, 0, 0, hud_ping_offset,
                    100, 100,
                    100, 500);
            RenderSystem.setShaderTexture(0, PING_DATA);
            blit(poseStack,
                    0, 0, ping.entityType.getIconOffset(), 0,
                    100, 100,
                    500, 200);
            blit(poseStack,
                    0, 0, ping.terrainType.getIconOffset(), 100,
                    100, 100,
                    500, 200);
            if (ping.isFriendly) {
                blit(poseStack,
                        0, 0, 400, 0,
                        100, 100,
                        500, 200);
            }
            if (ping.isShared()) {
                blit(poseStack,
                        0, 0, 400, 100,
                        100, 100,
                        500, 200);
            }
            poseStack.popPose();
            if (!hovering && cursorX < x_win+adj && cursorX > x_win-adj
                    && cursorY < y_win+adj && cursorY > y_win-adj) {
                ClientInputEvents.setHoverIndex(i);
                hovering = true;
            }
        }
        if (!hovering) ClientInputEvents.resetHoverIndex();
    }
}
