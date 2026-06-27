package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.*;

/**
 * Renders continuous tank track strips on the ground.
 *
 * Each tank track is identified by (entityId, isLeft).
 * Points are added every tick while moving; a new strip starts when the tank stops.
 */
public class TrackMarkManager {

    private static final ResourceLocation TRACK_MARK_TEXTURE =
        new ResourceLocation("dscombat", "textures/particle/track_mark.png");

    // Half-width of one track strip in blocks
    private static final float HALF_WIDTH = 0.28f;

    // Key: entityId * 2 + (isLeft ? 0 : 1)
    private static final Map<Long, TrackMarkDecal> activeStrips = new HashMap<>();
    private static final List<TrackMarkDecal> finishedStrips = new ArrayList<>();

    // -------------------------------------------------------

    /**
     * Called every tick while the tank is moving.
     * entityId identifies the tank; isLeft distinguishes left/right track.
     */
    public static void addTrackPoint(int entityId, Vec3 pos, float yawDeg, boolean isLeft) {
        long key = (long) entityId * 2 + (isLeft ? 0 : 1);
        TrackMarkDecal strip = activeStrips.computeIfAbsent(key, k -> new TrackMarkDecal());
        strip.addPoint(pos, yawDeg);
    }

    /**
     * Called when the tank stops — seals the current strip so a new one starts next time.
     */
    public static void sealStrip(int entityId, boolean isLeft) {
        long key = (long) entityId * 2 + (isLeft ? 0 : 1);
        TrackMarkDecal strip = activeStrips.remove(key);
        if (strip != null && !strip.getPoints().isEmpty())
            finishedStrips.add(strip);
    }

    /** @deprecated Use addTrackPoint instead */
    public static void addTrackMark(Vec3 position, float rotation, boolean isLeftTrack) {
        // legacy stub — no-op, kept for compile compatibility
    }

    // -------------------------------------------------------

    public static void tick() {
        // Tick finished strips and remove expired
        finishedStrips.removeIf(s -> { s.tick(); return s.shouldRemove(); });
        // Also tick active strips
        activeStrips.values().forEach(TrackMarkDecal::tick);
    }

    public static void clear() {
        activeStrips.clear();
        finishedStrips.clear();
    }

    // -------------------------------------------------------

    public static void renderTrackMarks(PoseStack poseStack, MultiBufferSource bufferSource,
                                        Vec3 cameraPos, int packedLight) {
        if (activeStrips.isEmpty() && finishedStrips.isEmpty()) return;

        // Используем полный свет чтобы следы были видны в любое время суток
        int fullLight = 0xF000F0;

        VertexConsumer vc = bufferSource.getBuffer(
            RenderType.entityTranslucentCull(TRACK_MARK_TEXTURE));

        for (TrackMarkDecal strip : activeStrips.values())
            renderStrip(strip, poseStack, vc, cameraPos, fullLight);
        for (TrackMarkDecal strip : finishedStrips)
            renderStrip(strip, poseStack, vc, cameraPos, fullLight);
    }

    private static void renderStrip(TrackMarkDecal strip, PoseStack poseStack,
                                    VertexConsumer vc, Vec3 cam, int light) {
        List<TrackMarkDecal.Point> pts = strip.getPoints();
        if (pts.size() < 2) return;

        float alpha = strip.getAlpha();
        if (alpha <= 0.01f) return;
        int a = (int)(alpha * 180); // max ~70% opacity

        poseStack.pushPose();
        Matrix4f mat = poseStack.last().pose();
        Matrix3f nrm = poseStack.last().normal();

        float vStep = 1f / (pts.size() - 1);

        for (int i = 0; i < pts.size() - 1; i++) {
            TrackMarkDecal.Point p0 = pts.get(i);
            TrackMarkDecal.Point p1 = pts.get(i + 1);

            // Side vectors perpendicular to each point's direction
            float[] side0 = sideVec(p0.yawDeg);
            float[] side1 = sideVec(p1.yawDeg);

            float v0 = vStep * i;
            float v1 = vStep * (i + 1);

            // Four corners of this quad segment
            // p0 left, p0 right, p1 right, p1 left
            float x0l = (float)(p0.pos.x - cam.x - side0[0] * HALF_WIDTH);
            float z0l = (float)(p0.pos.z - cam.z - side0[1] * HALF_WIDTH);
            float x0r = (float)(p0.pos.x - cam.x + side0[0] * HALF_WIDTH);
            float z0r = (float)(p0.pos.z - cam.z + side0[1] * HALF_WIDTH);
            float x1l = (float)(p1.pos.x - cam.x - side1[0] * HALF_WIDTH);
            float z1l = (float)(p1.pos.z - cam.z - side1[1] * HALF_WIDTH);
            float x1r = (float)(p1.pos.x - cam.x + side1[0] * HALF_WIDTH);
            float z1r = (float)(p1.pos.z - cam.z + side1[1] * HALF_WIDTH);

            float y0 = (float)(p0.pos.y - cam.y + 0.01);
            float y1 = (float)(p1.pos.y - cam.y + 0.01);

            vertex(vc, mat, nrm, x0l, y0, z0l, 0f, v0, a, light);
            vertex(vc, mat, nrm, x0r, y0, z0r, 1f, v0, a, light);
            vertex(vc, mat, nrm, x1r, y1, z1r, 1f, v1, a, light);
            vertex(vc, mat, nrm, x1l, y1, z1l, 0f, v1, a, light);
        }

        poseStack.popPose();
    }

    /** Returns (sin, cos) perpendicular to yaw — i.e. the right-side direction */
    private static float[] sideVec(float yawDeg) {
        double rad = Math.toRadians(yawDeg);
        return new float[]{ (float) Math.cos(rad), (float) -Math.sin(rad) };
    }

    private static void vertex(VertexConsumer vc, Matrix4f mat, Matrix3f nrm,
                                float x, float y, float z,
                                float u, float v, int a, int light) {
        vc.vertex(mat, x, y, z)
          .color(40, 30, 20, a)
          .uv(u, v)
          .overlayCoords(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY)
          .uv2(light)
          .normal(nrm, 0, 1, 0)
          .endVertex();
    }
}
