package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.entity.vehicle.EntityPlane;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

import static com.onewhohears.dscombat.client.overlay.components.VehicleControlOverlay.STICK_BASE_SIZE;
import static com.onewhohears.dscombat.client.overlay.components.VehicleControlOverlay.STICK_KNOB_SIZE;

// TODO: redo texture
public class TurnCoordinatorOverlay extends VehicleOverlayComponent {
    public static final ResourceLocation TURN_COORD_BASE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/turn_coord_base.png");
    public static final ResourceLocation TURN_COORD_BALL = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/turn_coord_ball.png");
    public static final ResourceLocation TURN_COORD_NEEDLE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/turn_coord_needle.png");

    public static final int TURN_COORD_SIZE = 80;
    public static final int MAX_BALL_MOVE = (int)(TURN_COORD_SIZE*0.25d);

    @Override
    protected boolean shouldRender(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        if (defaultRenderConditions()) return false;
        return getPlayerRootVehicle() instanceof EntityPlane;
    }

    @Override
    protected void render(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        EntityPlane plane = (EntityPlane) getPlayerRootVehicle();
        assert plane != null;

        final int xOrigin = screenWidth - TURN_COORD_SIZE - PADDING * 3 - STICK_BASE_SIZE - STICK_KNOB_SIZE;
        final int yOrigin = screenHeight - PADDING - TURN_COORD_SIZE;

        graphics.blit(TURN_COORD_BASE,
                xOrigin, yOrigin,
                0, 0,
                TURN_COORD_SIZE, TURN_COORD_SIZE,
                TURN_COORD_SIZE, TURN_COORD_SIZE);

        int xTranslation = (int) ((plane.getCentripetalForce() - plane.getCentrifugalForce()) * 0.1);
        if (Math.abs(xTranslation) > MAX_BALL_MOVE) xTranslation = MAX_BALL_MOVE * (int)Math.signum(xTranslation);
        graphics.blit(TURN_COORD_BALL,
                xOrigin + xTranslation, yOrigin,
                0, 0,
                TURN_COORD_SIZE, TURN_COORD_SIZE,
                TURN_COORD_SIZE, TURN_COORD_SIZE);

        graphics.pose().pushPose();
        graphics.pose().translate(xOrigin + (double) TURN_COORD_SIZE / 2, yOrigin + (double) TURN_COORD_SIZE / 2, 0);
        float yawRate = plane.getYawRate() * 20 / 40 * 30;
        graphics.pose().mulPose(new Quaternionf().rotateZ((float) Math.toRadians(yawRate)));
        graphics.blit(TURN_COORD_NEEDLE,
                -TURN_COORD_SIZE / 2, -TURN_COORD_SIZE / 2,
                0, 0,
                TURN_COORD_SIZE, TURN_COORD_SIZE,
                TURN_COORD_SIZE, TURN_COORD_SIZE);
        graphics.pose().popPose();
    }





    @Override
    protected @NotNull String componentId() {
        return "dscombat_turn_coordinator";
    }
}
