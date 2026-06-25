package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.onewhohears.dscombat.data.vehicle.CrawlerTrackPath;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.client.model.obj.ObjModelHandlerImpl;
import com.onewhohears.onewholibs.util.math.QuaternionF;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.List;

/**
 * Renders crawler tracks with McHeliCE-style animation
 * Ported from McHeliCE's renderCrawlerTrack method
 * All track logic is now in DSC - no dependencies on onewholibs crawler track code
 */
public class CrawlerTrackRenderer {
    
    // Cache for generated interpolated points (to avoid regenerating every frame)
    private static final java.util.Map<CrawlerTrackPath, List<TrackPoint>> pointsCache = new java.util.HashMap<>();
    
    // DEBUG: Enable visual debug rendering - toggle with /trackdebug command
    private static boolean debugRender = false;

    public static void setDebugRender(boolean enabled) { debugRender = enabled; }
    public static boolean isDebugRender() { return debugRender; }

    /**
     * Renders animated crawler tracks for a tank entity
     */
    public static void renderCrawlerTracks(EntityVehicle entity, PoseStack poseStack, 
                                          MultiBufferSource bufferSource, int packedLight, 
                                          float partialTicks, List<CrawlerTrackPath> paths,
                                          ObjModelHandlerImpl impl) {
        if (paths.isEmpty() || impl == null) return;

        // Use NO_OVERLAY to avoid red tint (damage overlay)
        // NO_OVERLAY = OverlayTexture.pack(0, 10) = no white overlay, max V coordinate
        int packedOverlay = net.minecraft.client.renderer.texture.OverlayTexture.pack(0, 10);

        // For each track path (left and right)
        for (int trackIndex = 0; trackIndex < Math.min(paths.size(), 2); trackIndex++) {
            CrawlerTrackPath path = paths.get(trackIndex);
            
            // Get interpolated rotation progress (0-1)
            float currentProgress = entity.rotCrawlerTrack[trackIndex];
            float prevProgress = entity.prevRotCrawlerTrack[trackIndex];
            float interpolatedProgress = prevProgress + (currentProgress - prevProgress) * partialTicks;
            
            // DEBUG: Render visual debug points
            if (debugRender) {
                renderDebugPoints(path, interpolatedProgress, poseStack, bufferSource, trackIndex, entity, partialTicks);
            }
            
            // Render track segments along the path
            renderTrackPath(path, interpolatedProgress, poseStack, bufferSource, packedLight, packedOverlay, impl);
        }
    }

    private static void renderTrackPath(CrawlerTrackPath path, float progress, 
                                       PoseStack poseStack, MultiBufferSource bufferSource,
                                       int packedLight, int packedOverlay, ObjModelHandlerImpl impl) {
        double[] cx = path.getControlPointsX();
        double[] cy = path.getControlPointsY();
        if (cx == null || cy == null || cx.length < 2) return;

        float segmentLength = path.getSegmentLength();
        float zOffset = path.getZOffset();

        // Get or generate interpolated points (cached for performance)
        List<TrackPoint> points = pointsCache.get(path);
        if (points == null) {
            points = generateInterpolatedPoints(cx, cy, segmentLength);
            pointsCache.put(path, points);
        }
        
        if (points.isEmpty()) return;

        int pointCount = points.size();
        
        // Find the crawler_track component
        com.onewhohears.onewholibs.client.model.obj.ObjBakedModel.Component trackComponent = 
            findComponent(impl.getBakedModel().getComponents(), "crawler_track");
        
        if (trackComponent == null) return;
        
        // Prepare transforms map (empty for track segments)
        java.util.Map<String, com.onewhohears.onewholibs.util.math.Mat4f> transforms = new java.util.HashMap<>();
        
        // CRITICAL FIX: Use progress as a CONTINUOUS offset, not discrete jumps
        // progress goes from 0.0 to 1.0, multiply by pointCount to get offset in points
        float continuousOffset = progress * pointCount;
        
        // Render track segments with INTERPOLATION between points
        int renderEveryNthPoint = 1;
        
        for (int i = 0; i < pointCount; i += renderEveryNthPoint) {
            // Calculate the continuous position for this segment
            float segmentPosition = (i - continuousOffset + pointCount * 100) % pointCount;
            
            // Get the two points to interpolate between
            int point1Index = (int)Math.floor(segmentPosition) % pointCount;
            int point2Index = (point1Index + 1) % pointCount;
            float t = segmentPosition - (float)Math.floor(segmentPosition); // Interpolation factor (0-1)
            
            TrackPoint p1 = points.get(point1Index);
            TrackPoint p2 = points.get(point2Index);
            
            // INTERPOLATE position and rotation between two points
            double interpX = p1.x + (p2.x - p1.x) * t;
            double interpY = p1.y + (p2.y - p1.y) * t;
            double interpRot = p1.rotation + (p2.rotation - p1.rotation) * t;
            
            poseStack.pushPose();
            poseStack.translate(zOffset, interpX, interpY);
            poseStack.mulPose(com.mojang.math.Axis.XN.rotationDegrees((float) interpRot));
            
            // Render the crawler_track component
            trackComponent.render(poseStack, bufferSource, 
                (texture) -> net.minecraft.client.renderer.RenderType.entityCutoutNoCull(texture),
                packedLight, packedOverlay, transforms);
            
            poseStack.popPose();
        }
    }
    
    /**
     * Recursively find a component by name
     */
    private static com.onewhohears.onewholibs.client.model.obj.ObjBakedModel.Component findComponent(
            List<com.onewhohears.onewholibs.client.model.obj.ObjBakedModel.Component> components, String name) {
        for (com.onewhohears.onewholibs.client.model.obj.ObjBakedModel.Component comp : components) {
            if (comp.getName().equals(name)) {
                return comp;
            }
            com.onewhohears.onewholibs.client.model.obj.ObjBakedModel.Component found = 
                findComponent(comp.getChildren(), name);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    /**
     * Generate interpolated points along the path (like McHeliCE's lp list)
     * This matches the logic in MCH_AircraftInfo.createCrawlerTrack()
     */
    private static List<TrackPoint> generateInterpolatedPoints(double[] cx, double[] cy, float segmentLength) {
        List<TrackPoint> points = new ArrayList<>();
        int controlPointCount = cx.length;
        
        // Add first point
        points.add(new TrackPoint(cx[0], cy[0], 0));
        
        double dist = 0.0;
        
        // Increase point density for smoother animation
        float densityMultiplier = 0.5f;
        float adjustedSegmentLength = segmentLength * densityMultiplier;
        
        // Generate points between control points
        for (int i = 0; i < controlPointCount; i++) {
            int next = (i + 1) % controlPointCount;
            double dx = cx[next] - cx[i];
            double dy = cy[next] - cy[i];
            dist += Math.sqrt(dx * dx + dy * dy);
            double dist2 = dist;
            
            // Add intermediate points along this segment
            for (int j = 1; dist >= adjustedSegmentLength; j++) {
                double t = (adjustedSegmentLength * j) / dist2;
                double x = cx[i] + dx * t;
                double y = cy[i] + dy * t;
                points.add(new TrackPoint(x, y, 0));
                dist -= adjustedSegmentLength;
            }
        }
        
        // Calculate rotation for each point based on neighbors (like McHeliCE)
        for (int i = 0; i < points.size(); i++) {
            TrackPoint prev = points.get((i + points.size() - 1) % points.size());
            TrackPoint current = points.get(i);
            TrackPoint next = points.get((i + 1) % points.size());
            
            // Calculate rotation from previous and next points
            double prevAngle = Math.toDegrees(Math.atan2(prev.x - current.x, prev.y - current.y));
            double nextAngle = Math.toDegrees(Math.atan2(next.x - current.x, next.y - current.y));
            
            double prevAngleNorm = (prevAngle + 360.0) % 360.0;
            double nextAngleNorm = nextAngle + 180.0;
            
            // Smooth angle transition
            if ((nextAngleNorm < prevAngleNorm - 0.3 || nextAngleNorm > prevAngleNorm + 0.3) 
                && nextAngleNorm - prevAngleNorm < 100.0 && nextAngleNorm - prevAngleNorm > -100.0) {
                nextAngleNorm = (nextAngleNorm + prevAngleNorm) / 2.0;
            }
            
            current.rotation = nextAngleNorm;
        }
        
        return points;
    }

    /**
     * DEBUG: Renders colored points along the track path to visualize animation
     * Points will change color based on animation progress - creates a "wave" effect
     */
    private static void renderDebugPoints(CrawlerTrackPath path, float progress, 
                                         PoseStack poseStack, MultiBufferSource bufferSource,
                                         int trackIndex, EntityVehicle entity, float partialTicks) {
        double[] cx = path.getControlPointsX();
        double[] cy = path.getControlPointsY();
        if (cx == null || cy == null || cx.length < 2) return;

        float zOffset = path.getZOffset();
        
        // Get or generate interpolated points
        List<TrackPoint> points = pointsCache.get(path);
        if (points == null) {
            points = generateInterpolatedPoints(cx, cy, path.getSegmentLength());
            pointsCache.put(path, points);
        }
        
        if (points.isEmpty()) return;

        int pointCount = points.size();
        VertexConsumer lineConsumer = bufferSource.getBuffer(RenderType.lines());
        Matrix4f matrix = poseStack.last().pose();
        
        // Calculate offset for animation - this makes points "move"
        float offset = progress * pointCount;
        
        // Render debug points - every 5th point for clarity
        for (int i = 0; i < pointCount; i += 5) {
            // IMPORTANT: Use offset to select which point to render at position i
            // This makes the points physically move along the track
            int pointIndex = (int) ((i + offset) % pointCount);
            if (pointIndex < 0) pointIndex += pointCount;
            
            TrackPoint point = points.get(pointIndex);
            
            // Color changes based on animation progress - creates "moving wave" effect
            // If animation works, you'll see colors flowing along the track
            float colorPhase = (progress + (float)i / pointCount) % 1.0f;
            float r = 0.5f + 0.5f * (float)Math.sin(colorPhase * Math.PI * 2);
            float g = 0.5f + 0.5f * (float)Math.sin((colorPhase + 0.33f) * Math.PI * 2);
            float b = 0.5f + 0.5f * (float)Math.sin((colorPhase + 0.66f) * Math.PI * 2);
            
            // Draw a small cross at each point
            float size = 0.1f;
            float px = (float)(zOffset);
            float py = (float)point.x;
            float pz = (float)point.y;
            
            // Horizontal line
            lineConsumer.vertex(matrix, px - size, py, pz).color(r, g, b, 1.0f).normal(0, 1, 0).endVertex();
            lineConsumer.vertex(matrix, px + size, py, pz).color(r, g, b, 1.0f).normal(0, 1, 0).endVertex();
            
            // Vertical line
            lineConsumer.vertex(matrix, px, py - size, pz).color(r, g, b, 1.0f).normal(0, 1, 0).endVertex();
            lineConsumer.vertex(matrix, px, py + size, pz).color(r, g, b, 1.0f).normal(0, 1, 0).endVertex();
            
            // Depth line
            lineConsumer.vertex(matrix, px, py, pz - size).color(r, g, b, 1.0f).normal(0, 1, 0).endVertex();
            lineConsumer.vertex(matrix, px, py, pz + size).color(r, g, b, 1.0f).normal(0, 1, 0).endVertex();
        }
        
        // Pass 1: draw all yellow crosses for control points (lines only, no text)
        for (int i = 0; i < cx.length; i++) {
            float px = (float)(zOffset);
            float py = (float)cx[i];
            float pz = (float)cy[i];
            float size = 0.15f;

            lineConsumer.vertex(matrix, px - size, py, pz).color(1.0f, 1.0f, 0.0f, 1.0f).normal(0, 1, 0).endVertex();
            lineConsumer.vertex(matrix, px + size, py, pz).color(1.0f, 1.0f, 0.0f, 1.0f).normal(0, 1, 0).endVertex();

            lineConsumer.vertex(matrix, px, py - size, pz).color(1.0f, 1.0f, 0.0f, 1.0f).normal(0, 1, 0).endVertex();
            lineConsumer.vertex(matrix, px, py + size, pz).color(1.0f, 1.0f, 0.0f, 1.0f).normal(0, 1, 0).endVertex();

            lineConsumer.vertex(matrix, px, py, pz - size).color(1.0f, 1.0f, 0.0f, 1.0f).normal(0, 1, 0).endVertex();
            lineConsumer.vertex(matrix, px, py, pz + size).color(1.0f, 1.0f, 0.0f, 1.0f).normal(0, 1, 0).endVertex();
        }

        // Pass 2: draw text labels AFTER all line vertices are done
        // Getting a new buffer for a different RenderType while lineConsumer is open causes the crash,
        // so we must not touch bufferSource for any other type until we are done with lines.
        // Also: poseStack is already in tank-local space (tank rotation applied).
        // To make text face the camera correctly, we must undo the tank rotation first.
        QuaternionF tankQ = UtilAngles.lerpQ(partialTicks, entity.getPrevQ(), entity.getClientQ());
        Quaternionf invTankQ = tankQ.convert().conjugate(new Quaternionf());

        Font font = Minecraft.getInstance().font;
        for (int i = 0; i < cx.length; i++) {
            float px = (float)(zOffset);
            float py = (float)cx[i];
            float pz = (float)cy[i];

            // Label matches /trackpoint list format: "[i] x=... y=..."
            String label = String.format("[%d] x=%.3f y=%.3f", i, cx[i], cy[i]);
            poseStack.pushPose();
            poseStack.translate(px, py + 0.25f, pz);
            // Undo tank rotation so text is in world space, then face camera
            poseStack.mulPose(invTankQ);
            poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
            poseStack.scale(-0.02f, -0.02f, 0.02f);
            float xOff = -font.width(label) / 2.0f;
            font.drawInBatch(label, xOff, 0, 0xFFFF00, false,
                    poseStack.last().pose(), bufferSource, Font.DisplayMode.SEE_THROUGH, 0x80000000, 15728880);
            poseStack.popPose();
        }
    }

    private static class TrackPoint {
        double x, y;
        double rotation;
        
        TrackPoint(double x, double y, double rotation) {
            this.x = x;
            this.y = y;
            this.rotation = rotation;
        }
    }
}
