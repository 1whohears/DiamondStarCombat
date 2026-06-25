package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.data.vehicle.TrackTextureData;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Matrix3f;

/**
 * Renderer for displaying block textures on tank tracks
 */
public class TrackTextureRenderer {
    
    private static boolean debugMode = false; // Toggle with /trackdebug command or config
    
    /**
     * Sets the debug mode state
     */
    public static void setDebugMode(boolean enabled) {
        debugMode = enabled;
    }
    
    /**
     * Gets the current debug mode state
     */
    public static boolean isDebugMode() {
        return debugMode;
    }
    
    /**
     * Renders block textures on tank tracks
     */
    public static void renderTrackTextures(EntityVehicle vehicle, PoseStack poseStack, 
                                          MultiBufferSource bufferSource, int packedLight, float partialTicks) {
        if (!vehicle.isTank()) return;
        if (!Config.CLIENT.enableTrackGroundTextures.get()) return;
        
        TrackTextureData trackData = vehicle.getStats().getTrackTextureData();
        if (!trackData.isEnabled()) return;
        
        BlockState leftBlock = vehicle.getLeftTrackGroundBlock();
        BlockState rightBlock = vehicle.getRightTrackGroundBlock();
        
        Vec3 leftPos = trackData.getLeftTrackPos();
        Vec3 rightPos = trackData.getRightTrackPos();
        
        // Debug visualization - colored markers at track positions
        if (debugMode || Config.CLIENT.debugMode.get() || Config.CLIENT.trackDebugMode.get()) {
            renderDebugMarker(poseStack, bufferSource, leftPos, 1.0f, 0.0f, 0.0f, "LEFT"); // Red
            renderDebugMarker(poseStack, bufferSource, rightPos, 0.0f, 0.0f, 1.0f, "RIGHT"); // Blue
            
            // Render text with coordinates and block info
            renderDebugText(vehicle, poseStack, bufferSource, leftPos, 
                String.format("L: %.1f,%.1f,%.1f\n%s", leftPos.x, leftPos.y, leftPos.z, 
                leftBlock.isAir() ? "AIR" : leftBlock.getBlock().getName().getString()), 
                packedLight);
            renderDebugText(vehicle, poseStack, bufferSource, rightPos, 
                String.format("R: %.1f,%.1f,%.1f\n%s", rightPos.x, rightPos.y, rightPos.z,
                rightBlock.isAir() ? "AIR" : rightBlock.getBlock().getName().getString()), 
                packedLight);
        }
        
        // Render left track
        if (!leftBlock.isAir()) {
            renderTrackTexture(vehicle, poseStack, bufferSource, packedLight, 
                             leftBlock, leftPos, true);
        }
        
        // Render right track
        if (!rightBlock.isAir()) {
            renderTrackTexture(vehicle, poseStack, bufferSource, packedLight, 
                             rightBlock, rightPos, false);
        }
    }
    
    /**
     * Renders a colored debug marker (cube) at track position
     */
    private static void renderDebugMarker(PoseStack poseStack, MultiBufferSource bufferSource,
                                         Vec3 pos, float r, float g, float b, String label) {
        poseStack.pushPose();
        poseStack.translate(pos.x, pos.y, pos.z);
        
        // Draw a small colored cube
        float size = 0.15f;
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.lines());
        Matrix4f matrix = poseStack.last().pose();
        
        // Draw cube wireframe
        drawLine(consumer, matrix, -size, -size, -size, size, -size, -size, r, g, b);
        drawLine(consumer, matrix, size, -size, -size, size, size, -size, r, g, b);
        drawLine(consumer, matrix, size, size, -size, -size, size, -size, r, g, b);
        drawLine(consumer, matrix, -size, size, -size, -size, -size, -size, r, g, b);
        
        drawLine(consumer, matrix, -size, -size, size, size, -size, size, r, g, b);
        drawLine(consumer, matrix, size, -size, size, size, size, size, r, g, b);
        drawLine(consumer, matrix, size, size, size, -size, size, size, r, g, b);
        drawLine(consumer, matrix, -size, size, size, -size, -size, size, r, g, b);
        
        drawLine(consumer, matrix, -size, -size, -size, -size, -size, size, r, g, b);
        drawLine(consumer, matrix, size, -size, -size, size, -size, size, r, g, b);
        drawLine(consumer, matrix, size, size, -size, size, size, size, r, g, b);
        drawLine(consumer, matrix, -size, size, -size, -size, size, size, r, g, b);
        
        poseStack.popPose();
    }
    
    /**
     * Helper to draw a line
     */
    private static void drawLine(VertexConsumer consumer, Matrix4f matrix,
                                 float x1, float y1, float z1, float x2, float y2, float z2,
                                 float r, float g, float b) {
        consumer.vertex(matrix, x1, y1, z1).color(r, g, b, 1.0f).normal(0, 1, 0).endVertex();
        consumer.vertex(matrix, x2, y2, z2).color(r, g, b, 1.0f).normal(0, 1, 0).endVertex();
    }
    
    /**
     * Renders debug text above track position
     */
    private static void renderDebugText(EntityVehicle vehicle, PoseStack poseStack, 
                                       MultiBufferSource bufferSource, Vec3 pos, 
                                       String text, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(pos.x, pos.y + 0.5, pos.z);
        
        // Face camera
        poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
        poseStack.scale(-0.025f, -0.025f, 0.025f);
        
        Font font = Minecraft.getInstance().font;
        Matrix4f matrix = poseStack.last().pose();
        
        // Draw text with background
        String[] lines = text.split("\n");
        float yOffset = 0;
        for (String line : lines) {
            float xOffset = -font.width(line) / 2.0f;
            font.drawInBatch(line, xOffset, yOffset, 0xFFFFFF, false, 
                           matrix, bufferSource, Font.DisplayMode.NORMAL, 0x80000000, packedLight);
            yOffset += 10;
        }
        
        poseStack.popPose();
    }
    
    /**
     * Renders block texture on a single track
     */
    private static void renderTrackTexture(EntityVehicle vehicle, PoseStack poseStack,
                                          MultiBufferSource bufferSource, int packedLight,
                                          BlockState blockState, Vec3 trackPos, boolean isLeft) {
        poseStack.pushPose();
        
        // Position texture on track (relative to vehicle center)
        poseStack.translate(trackPos.x, trackPos.y, trackPos.z);
        
        // Rotate texture to lie flat on track (horizontal plane)
        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90));
        
        // Scale texture to track size
        float scaleX = 0.4f; // Track width
        float scaleZ = 2.5f; // Track length
        poseStack.scale(scaleX, scaleZ, 0.01f);
        
        try {
            // Use solid rendering for better visibility
            VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.solid());
            
            // Get matrices for rendering
            Matrix4f matrix4f = poseStack.last().pose();
            Matrix3f matrix3f = poseStack.last().normal();
            
            // Draw quad with block texture
            renderTexturedQuad(vertexConsumer, matrix4f, matrix3f, blockState, packedLight);
            
        } catch (Exception e) {
            // Ignore rendering errors
        }
        
        poseStack.popPose();
    }
    
    /**
     * Draws a quad with block texture
     */
    private static void renderTexturedQuad(VertexConsumer consumer, Matrix4f pose, Matrix3f normal,
                                          BlockState blockState, int packedLight) {
        // Get block texture sprite
        var sprite = Minecraft.getInstance().getBlockRenderer()
            .getBlockModel(blockState)
            .getParticleIcon();
        
        if (sprite == null) return;
        
        float minU = sprite.getU0();
        float maxU = sprite.getU1();
        float minV = sprite.getV0();
        float maxV = sprite.getV1();
        
        // Draw quad (4 vertices)
        // Vertex 1 (bottom left)
        consumer.vertex(pose, -0.5f, -0.5f, 0)
            .color(255, 255, 255, 255)
            .uv(minU, minV)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(packedLight)
            .normal(normal, 0, 1, 0)
            .endVertex();
        
        // Vertex 2 (bottom right)
        consumer.vertex(pose, 0.5f, -0.5f, 0)
            .color(255, 255, 255, 255)
            .uv(maxU, minV)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(packedLight)
            .normal(normal, 0, 1, 0)
            .endVertex();
        
        // Vertex 3 (top right)
        consumer.vertex(pose, 0.5f, 0.5f, 0)
            .color(255, 255, 255, 255)
            .uv(maxU, maxV)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(packedLight)
            .normal(normal, 0, 1, 0)
            .endVertex();
        
        // Vertex 4 (top left)
        consumer.vertex(pose, -0.5f, 0.5f, 0)
            .color(255, 255, 255, 255)
            .uv(minU, maxV)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(packedLight)
            .normal(normal, 0, 1, 0)
            .endVertex();
    }
}
