package com.onewhohears.dscombat.client.overlay.components;

import javax.annotation.Nullable;

import com.onewhohears.onewholibs.util.math.VectorUtils;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.event.forgebus.ClientRenderEvents;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.data.weapon.instance.WeaponInstance;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import com.onewhohears.onewholibs.util.math.UtilGeometry;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.joml.Matrix4f;

public class AimAssistOverlay extends VehicleOverlayComponent {
	public static final ResourceLocation AIM_HUD = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/aim_hud.png");
	public static final int AIM_SIZE = 10;
	protected static float PARTIAL_TICK;

    private Vec3 targetWorldPos = null;
    private int prevTick = 0;

    @Override
    protected boolean shouldRender(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        if (defaultRenderConditions()) return false;
        if (!(getPlayerRootVehicle() instanceof EntityVehicle vehicle)) return false;

        RadarSystem radar = vehicle.radarSystem;
        if (!radar.hasRadar()) return false;

        WeaponInstance<?> data = vehicle.weaponSystem.getSelected();
        if (data == null) return false;

        return data.getStats().isAimAssist();
    }

    @Override
    protected void render(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        if (!(getPlayerRootVehicle() instanceof EntityVehicle vehicle)) return;

        WeaponInstance<?> data = vehicle.weaponSystem.getSelected();

        if (vehicle.tickCount != prevTick) {
            assert data != null;
            targetWorldPos = calcTargetWorldPos(vehicle, data);
        }
        prevTick = vehicle.tickCount;
        if (targetWorldPos == null) return;

        Camera cam = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 view = cam.getPosition();

        float z_rot = UtilAngles.lerpAngle(partialTick, vehicle.zRotO, vehicle.zRot);

        PoseStack stack = graphics.pose(); // Get the PoseStack from GuiGraphics
        stack.pushPose();

        stack.mulPose(VectorUtils.rotationQuaternion(VectorUtils.POSITIVE_Z, z_rot));
        stack.mulPose(VectorUtils.rotationQuaternion(VectorUtils.POSITIVE_X, cam.getXRot()));
        stack.mulPose(VectorUtils.rotationQuaternion(VectorUtils.POSITIVE_Y, cam.getYRot() + 180f));

        stack.translate(-view.x, -view.y, -view.z);
        Matrix4f view_mat = new Matrix4f(stack.last().pose());
        stack.popPose();

        Matrix4f proj_mat = ClientRenderEvents.getProjMatrix();
        float[] screen_pos = UtilGeometry.worldToScreenPos(targetWorldPos, view_mat, proj_mat, screenWidth, screenHeight);

        if (screen_pos[0] < 0 || screen_pos[1] < 0) return;

        float x_win = screen_pos[0], y_win = screen_pos[1];
        float adj = AIM_SIZE * 0.5f, x_pos = x_win - adj, y_pos = y_win - adj;

        // Updated call to drawCrossHair with GuiGraphics
        drawCrossHair(graphics, x_pos, y_pos);
    }




    @Override
    protected @NotNull String componentId() {
        return "dscombat_aim_assist";
    }

    protected void drawCrossHair(GuiGraphics graphics, float x_pos, float y_pos) {
        PoseStack poseStack = graphics.pose(); // Get the PoseStack from GuiGraphics
        poseStack.pushPose();

        poseStack.translate(x_pos, y_pos, 0);
        RenderSystem.setShaderTexture(0, AIM_HUD);
        // Use GuiGraphics.blit for rendering the texture
        graphics.blit(
                AIM_HUD,          // Texture resource location
                0, 0,             // Position on the screen (rendering starts at 0, 0 due to translation)
                0, 0,             // Texture coordinates
                AIM_SIZE, AIM_SIZE // Width and height of the texture
        );

        poseStack.popPose();
    }

	
	@Nullable
	protected Vec3 calcTargetWorldPos(EntityVehicle vehicle, WeaponInstance<?> data) {
		return data.estimateImpactPosition(vehicle);
	}

}
