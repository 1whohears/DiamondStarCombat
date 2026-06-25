package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.parts.instance.PartInstance;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.math.QuaternionF;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.List;

/**
 * Renders debug information for vehicle parts in 3D space at their actual positions
 * Shows: icon, name, slot ID, and XYZ coordinates
 * All elements render through walls and always face the player
 * @author 1whohears
 */
public class PartsDebugRenderer {
    
    private static boolean debugEnabled = false;
    
    private static final int NAME_COLOR = 0xFFFFFF00; // Yellow
    private static final int SLOT_COLOR = 0xFFAAAAAA; // Light gray
    private static final int COORD_COLOR = 0xFF00FF00; // Green
    private static final float TEXT_SCALE = 0.025f; // Scale for text rendering (increased)
    private static final float ICON_SCALE = 0.8f; // Scale for item icon (increased)
    private static final int LINE_SPACING = 12; // Spacing between text lines (increased)
    private static final int MARKER_COLOR_R = 255;
    private static final int MARKER_COLOR_G = 0;
    private static final int MARKER_COLOR_B = 0;
    private static final int MARKER_ALPHA = 200;
    
    public static void setDebugEnabled(boolean enabled) {
        debugEnabled = enabled;
    }
    
    public static boolean isDebugEnabled() {
        return debugEnabled;
    }
    
    /**
     * Renders debug info for all parts in the vehicle
     */
    public static void renderPartsDebug(EntityVehicle entity, float partialTicks, 
                                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (!debugEnabled) return;
        
        List<PartSlot> slots = entity.partsManager.getSlots();
        if (slots.isEmpty()) return;
        
        QuaternionF q = UtilAngles.lerpQ(partialTicks, entity.getPrevQ(), entity.getClientQ());
        Font font = Minecraft.getInstance().font;
        Minecraft mc = Minecraft.getInstance();
        
        for (PartSlot slot : slots) {
            if (!slot.filled()) continue;
            
            PartInstance<?> partData = slot.getPartData();
            if (partData == null) continue;
            
            Vec3 relPos = slot.getRelPos();
            
            poseStack.pushPose();
            
            // Apply vehicle rotation
            poseStack.mulPose(q.convert());
            
            // Move to part position
            poseStack.translate(relPos.x, relPos.y, relPos.z);
            
            // Render marker box at part position
            renderMarkerBox(poseStack, bufferSource);
            
            // Billboard rotation to face camera (always look at player)
            poseStack.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
            
            // Flip text to read correctly
            poseStack.scale(-TEXT_SCALE, -TEXT_SCALE, TEXT_SCALE);
            
            // Render text information with see-through effect
            int yOffset = 0;
            
            // Part name
            String partName = partData.getStats().getDisplayName();
            int nameWidth = font.width(partName);
            font.drawInBatch(partName, -nameWidth / 2f, yOffset, NAME_COLOR, false,
                    poseStack.last().pose(), bufferSource, Font.DisplayMode.SEE_THROUGH, 0, 15728880);
            yOffset += LINE_SPACING;
            
            // Slot ID
            String slotId = "Slot: " + slot.getSlotId();
            int slotWidth = font.width(slotId);
            font.drawInBatch(slotId, -slotWidth / 2f, yOffset, SLOT_COLOR, false,
                    poseStack.last().pose(), bufferSource, Font.DisplayMode.SEE_THROUGH, 0, 15728880);
            yOffset += LINE_SPACING;
            
            // Coordinates
            String coordX = String.format("X: %.2f", relPos.x);
            int coordXWidth = font.width(coordX);
            font.drawInBatch(coordX, -coordXWidth / 2f, yOffset, COORD_COLOR, false,
                    poseStack.last().pose(), bufferSource, Font.DisplayMode.SEE_THROUGH, 0, 15728880);
            yOffset += LINE_SPACING;
            
            String coordY = String.format("Y: %.2f", relPos.y);
            int coordYWidth = font.width(coordY);
            font.drawInBatch(coordY, -coordYWidth / 2f, yOffset, COORD_COLOR, false,
                    poseStack.last().pose(), bufferSource, Font.DisplayMode.SEE_THROUGH, 0, 15728880);
            yOffset += LINE_SPACING;
            
            String coordZ = String.format("Z: %.2f", relPos.z);
            int coordZWidth = font.width(coordZ);
            font.drawInBatch(coordZ, -coordZWidth / 2f, yOffset, COORD_COLOR, false,
                    poseStack.last().pose(), bufferSource, Font.DisplayMode.SEE_THROUGH, 0, 15728880);
            yOffset += 10;
            
            poseStack.popPose();
            
            // Render item icon above text (also through walls and facing player)
            renderPartIcon(entity, partData, relPos, q, poseStack, bufferSource, packedLight, partialTicks);
        }
    }
    
    /**
     * Renders a small colored box marker at the part position
     */
    private static void renderMarkerBox(PoseStack poseStack, MultiBufferSource bufferSource) {
        float size = 0.1f;
        AABB box = new AABB(-size, -size, -size, size, size, size);
        
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.lines());
        Matrix4f matrix = poseStack.last().pose();
        
        // Draw box outline
        LevelRenderer.renderLineBox(poseStack, consumer, box, 
                MARKER_COLOR_R / 255f, 
                MARKER_COLOR_G / 255f, 
                MARKER_COLOR_B / 255f, 
                MARKER_ALPHA / 255f);
    }
    
    /**
     * Renders the part's item icon in 3D space, always facing the player and visible through walls
     */
    private static void renderPartIcon(EntityVehicle entity, PartInstance<?> partData, Vec3 relPos,
                                      QuaternionF vehicleQ, PoseStack poseStack, 
                                      MultiBufferSource bufferSource, int packedLight, float partialTicks) {
        ItemStack itemStack = partData.getNewItemStack();
        if (itemStack.isEmpty()) return;
        
        Minecraft mc = Minecraft.getInstance();
        
        poseStack.pushPose();
        
        // Apply vehicle rotation
        poseStack.mulPose(vehicleQ.convert());
        
        // Move to part position, slightly above the marker
        poseStack.translate(relPos.x, relPos.y + 0.3, relPos.z);
        
        // Billboard rotation to face camera (always look at player)
        poseStack.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
        
        // Scale the icon
        poseStack.scale(ICON_SCALE, ICON_SCALE, 0.01f);
        
        // Temporarily disable depth test for item rendering
        RenderSystem.disableDepthTest();
        
        // Render the item
        mc.getItemRenderer().renderStatic(itemStack, net.minecraft.world.item.ItemDisplayContext.GUI,
                15728880, 0, poseStack, bufferSource, entity.level(), entity.getId());
        
        // Re-enable depth test
        RenderSystem.enableDepthTest();
        
        poseStack.popPose();
    }
}
