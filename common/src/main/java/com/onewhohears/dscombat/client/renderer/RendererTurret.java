package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.onewhohears.dscombat.data.weapon.MuzzleSmokeData;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.entity.parts.hitbox.TurretHitbox;
import com.onewhohears.onewholibs.client.renderer.RendererCustomAnimObjEntity;
import com.onewhohears.onewholibs.util.math.Mat3f;
import com.onewhohears.onewholibs.util.math.Mat4f;
import com.onewhohears.onewholibs.util.math.QuaternionF;
import com.onewhohears.onewholibs.util.math.Vec3f;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import com.onewhohears.onewholibs.util.math.UtilGeometry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import static com.onewhohears.dscombat.client.util.UtilRender.drawLine;

public class RendererTurret extends RendererCustomAnimObjEntity<EntityTurret> {

    private static final int[] ERA_COLOR = new int[]{188, 85, 41, 255};       // orange — alive
    private static final int[] ERA_DESTROYED = new int[]{120, 0, 0, 255};     // dark red — destroyed

    public RendererTurret(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(EntityTurret entity, float yaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, yaw, partialTicks, poseStack, bufferSource, packedLight);
        
        if (shouldDrawHitboxes(entity)) {
            drawTurretHitboxes(entity, partialTicks, poseStack, bufferSource);
        }
    }
    
    /**
     * Рендерит 3D дульную вспышку если турель недавно стреляла
     * Вспышки теперь рендерятся через RendererObjWeapon привязанный к пуле
     */

    private boolean shouldDrawHitboxes(EntityTurret entity) {
        if (entity.getTurretHitboxes().isEmpty()) return false;
        Minecraft m = Minecraft.getInstance();
        return !m.showOnlyReducedInfo() && m.getEntityRenderDispatcher().shouldRenderHitBoxes();
    }

    private void drawTurretHitboxes(EntityTurret entity, float partialTicks,
                                    PoseStack poseStack, MultiBufferSource bufferSource) {
        VertexConsumer buff = bufferSource.getBuffer(RenderType.lines());

        // Build turret world quaternion for rendering (same as positionSelf in TurretHitbox)
        QuaternionF vehicleQ = QuaternionF.ONE;
        if (entity.getParentVehicle() != null) {
            vehicleQ = entity.getParentVehicle().getClientQ();
        }
        QuaternionF turretQ = vehicleQ.copy();
        turretQ.mul(Vec3f.YP.rotationDegrees(entity.getRelRotY()));

        for (TurretHitbox hb : entity.getTurretHitboxes()) {
            drawHitboxOutline(hb, entity, turretQ, poseStack, buff);
        }
    }

    private void drawHitboxOutline(TurretHitbox hb, EntityTurret entity,
                                   QuaternionF turretQ, PoseStack poseStack, VertexConsumer buff) {
        poseStack.pushPose();
        // Translate to hitbox center relative to turret position
        Vec3 relPos = hb.getData().getRelPos();
        Vec3 trans = UtilAngles.rotateVector(relPos, turretQ);
        poseStack.translate(trans.x, trans.y, trans.z);

        Mat4f m4 = Mat4f.from(poseStack.last().pose());
        Mat3f m3 = Mat3f.from(poseStack.last().normal());

        Vec3 size = hb.getData().getSize();
        Vec3f ext = new Vec3f((float) size.x * 0.5f, (float) size.y * 0.5f, (float) size.z * 0.5f);

        // Rotate extents by turret quaternion
        Vec3f c0 = ext.copy(); c0.transform(turretQ);
        Vec3f c1 = ext.copy(); c1.mul(-1, 1, 1); c1.transform(turretQ);
        Vec3f c2 = ext.copy(); c2.mul(1, -1, 1); c2.transform(turretQ);
        Vec3f c3 = ext.copy(); c3.mul(1, 1, -1); c3.transform(turretQ);
        Vec3f c4 = c3.copy(); c4.mul(-1);
        Vec3f c5 = c2.copy(); c5.mul(-1);
        Vec3f c6 = c1.copy(); c6.mul(-1);
        Vec3f c7 = c0.copy(); c7.mul(-1);

        int[] color = hb.isDestroyed() ? ERA_DESTROYED : ERA_COLOR;
        drawLine(c0, c1, buff, m4, m3, color);
        drawLine(c0, c2, buff, m4, m3, color);
        drawLine(c0, c3, buff, m4, m3, color);
        drawLine(c1, c5, buff, m4, m3, color);
        drawLine(c1, c4, buff, m4, m3, color);
        drawLine(c2, c4, buff, m4, m3, color);
        drawLine(c2, c6, buff, m4, m3, color);
        drawLine(c3, c5, buff, m4, m3, color);
        drawLine(c3, c6, buff, m4, m3, color);
        drawLine(c4, c7, buff, m4, m3, color);
        drawLine(c5, c7, buff, m4, m3, color);
        drawLine(c6, c7, buff, m4, m3, color);
        poseStack.popPose();
    }
}
