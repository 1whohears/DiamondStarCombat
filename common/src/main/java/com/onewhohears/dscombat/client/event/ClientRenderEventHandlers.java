package com.onewhohears.dscombat.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.math.QuaternionF;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class ClientRenderEventHandlers {

    public static void onRenderPlayerPre(Player player, float partialTick, PoseStack stack) {
        if (!player.isPassenger()) return;
        if (!(player.getRootVehicle() instanceof EntityVehicle plane)) return;
        QuaternionF q = UtilAngles.lerpQ(partialTick, plane.getPrevQ(), plane.getClientQ());
        Vec3 eye = new Vec3(0, player.getEyeHeight(), 0);
        Vec3 t = eye.subtract(UtilAngles.rotateVector(eye, q));
        stack.translate(t.x, t.y, t.z);
        stack.mulPose(q.convert());
        float[] relangles = UtilAngles.globalToRelativeDegrees(player.getXRot(), player.getYRot(), q);
        player.setYHeadRot(relangles[1]);
        player.yHeadRotO = relangles[1];
        player.setYBodyRot(relangles[1]);
        player.yBodyRotO = relangles[1];
        // HOW 1 set player head model part x rot to relangles[0]
        // neither of these work
        //event.getRenderer().getModel().head.xRot = (float) Math.toRadians(relangles[0]);
		/*event.getRenderer().getModel().setupAnim((LocalPlayer)player, 0f,
				0f, 0f, 0f, (float) Math.toRadians(relangles[0]));*/
    }

    public static void onRenderPlayerPost(Player player) {
        if (!player.isPassenger()) return;
        if (!(player.getRootVehicle() instanceof EntityVehicle)) return;
        player.setYHeadRot(player.getYRot());
        player.yHeadRotO = player.getYRot();
        player.setYBodyRot(player.getYRot());
        player.yBodyRotO = player.getYRot();
    }

    public static boolean isCancelRenderHand() {
        if (!(Minecraft.getInstance().player.getRootVehicle() instanceof EntityVehicle vehicle)) return false;
        return DSCClientInputs.isCameraLockedForward() || DSCClientInputs.isGimbalMode();
    }

}
