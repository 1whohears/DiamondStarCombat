package com.onewhohears.dscombat.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.onewhohears.onewholibs.util.math.Mat3f;
import com.onewhohears.onewholibs.util.math.Mat4f;
import com.onewhohears.onewholibs.util.math.Vec3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class UtilRender {

    public static final int[] BLACK = new int[] {0, 0, 0, 255};
    public static final int[] RED = new int[] {255, 0, 0, 255};
    public static final int[] GREEN = new int[] {0, 255, 0, 255};
    public static final int[] BLUE = new int[] {0, 0, 255, 255};
    public static final int[] YELLOW = new int[] {255, 255, 0, 255};
    public static final int[] CYAN = new int[] {0, 255, 255, 255};
    public static final int[] MAGENTA = new int[] {255, 0, 255, 255};
    public static final int[] WHITE = new int[] {255, 255, 255, 255};

    public static void drawLine(Vec3f start, Vec3f end, VertexConsumer buff, Mat4f m4, Mat3f m3, int[] color) {
        Vec3f n = end.copy();
        n.sub(start);
        n.normalize();
        Matrix4f m4f = m4.convert();
        Matrix3f m3f = m3.convert();
        buff.vertex(m4f,start.x(),start.y(),start.z())
                .color(color[0],color[1],color[2],color[3])
                .normal(m3f,n.x(),n.y(),n.z())
                .endVertex();
        buff.vertex(m4f,end.x(),end.y(),end.z())
                .color(color[0],color[1],color[2],color[3])
                .normal(m3f,n.x(),n.y(),n.z())
                .endVertex();
    }

    public static void drawText(Component text, float xPos, float yPos, float maxWidth, PoseStack poseStack, MultiBufferSource buffer, int color, int packedLight) {
        Font font = Minecraft.getInstance().font;
        float width = font.width(text);
        float scale = maxWidth/width;
        poseStack.pushPose();
        poseStack.translate(xPos, yPos, -0.005f);
        poseStack.scale(scale, scale, 1);
        font.drawInBatch(text, 0, 0, -1, true, poseStack.last().pose(), buffer, Font.DisplayMode.NORMAL, 0, color);
        poseStack.popPose();
    }

    public static void drawTextureTopLeft(RenderType type, Mat4f matrix4f, MultiBufferSource buffer, int packedLight, float z) {
        VertexConsumer vertexconsumer = buffer.getBuffer(type);
        Matrix4f m4f = matrix4f.convert();
        vertexconsumer.vertex(m4f, 0, 1, z)
                .color(255, 255, 255, 255)
                .uv(0.0F, 1.0F).uv2(packedLight).endVertex();
        vertexconsumer.vertex(m4f, 1, 1, z)
                .color(255, 255, 255, 255)
                .uv(1.0F, 1.0F).uv2(packedLight).endVertex();
        vertexconsumer.vertex(m4f, 1, 0, z)
                .color(255, 255, 255, 255)
                .uv(1.0F, 0.0F).uv2(packedLight).endVertex();
        vertexconsumer.vertex(m4f, 0, 0, z)
                .color(255, 255, 255, 255)
                .uv(0.0F, 0.0F).uv2(packedLight).endVertex();
    }

    public static void drawTextureCentered(RenderType type, Mat4f matrix4f, MultiBufferSource buffer, int packedLight, float z) {
        VertexConsumer vertexconsumer = buffer.getBuffer(type);
        Matrix4f m4f = matrix4f.convert();
        vertexconsumer.vertex(m4f, -0.5f, 0.5f, z)
                .color(255, 255, 255, 255)
                .uv(0.0F, 1.0F).uv2(packedLight).endVertex();
        vertexconsumer.vertex(m4f, 0.5f, 0.5f, z)
                .color(255, 255, 255, 255)
                .uv(1.0F, 1.0F).uv2(packedLight).endVertex();
        vertexconsumer.vertex(m4f, 0.5f, -0.5f, z)
                .color(255, 255, 255, 255)
                .uv(1.0F, 0.0F).uv2(packedLight).endVertex();
        vertexconsumer.vertex(m4f, -0.5f, -0.5f, z)
                .color(255, 255, 255, 255)
                .uv(0.0F, 0.0F).uv2(packedLight).endVertex();
    }

    public static void drawTextureCentered(RenderType type, Mat4f matrix4f, MultiBufferSource buffer,
                                           int packedLight, float z, int[] color) {
        VertexConsumer vertexconsumer = buffer.getBuffer(type);
        Matrix4f m4f = matrix4f.convert();
        vertexconsumer.vertex(m4f, -0.5f, 0.5f, z)
                .color(color[0], color[1], color[2], color[3])
                .uv(0.0F, 1.0F).uv2(packedLight).endVertex();
        vertexconsumer.vertex(m4f, 0.5f, 0.5f, z)
                .color(color[0], color[1], color[2], color[3])
                .uv(1.0F, 1.0F).uv2(packedLight).endVertex();
        vertexconsumer.vertex(m4f, 0.5f, -0.5f, z)
                .color(color[0], color[1], color[2], color[3])
                .uv(1.0F, 0.0F).uv2(packedLight).endVertex();
        vertexconsumer.vertex(m4f, -0.5f, -0.5f, z)
                .color(color[0], color[1], color[2], color[3])
                .uv(0.0F, 0.0F).uv2(packedLight).endVertex();
    }

}
