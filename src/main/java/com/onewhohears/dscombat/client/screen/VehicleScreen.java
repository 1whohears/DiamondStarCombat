package com.onewhohears.dscombat.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.client.screen.BackgroundScreen;
import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Objects;

public abstract class VehicleScreen extends BackgroundScreen {
    public static int ROWS = 6, COLUMNS = 2;
    protected String infoText = "";
    protected int infoColor = 0x111111;
    protected VehicleScreen(Component title, ResourceLocation backgroundTexture,
                            int imageWidth, int imageHeight, int textureWidth, int textureHeight) {
        super(title, backgroundTexture, imageWidth, imageHeight, textureWidth, textureHeight);
        vertical_widget_shift = 10;
    }
    protected VehicleScreen(String translatableScreenName, ResourceLocation backgroundTexture,
                            int imageWidth, int imageHeight, int textureWidth, int textureHeight) {
        super(translatableScreenName, backgroundTexture, imageWidth, imageHeight, textureWidth, textureHeight);
        vertical_widget_shift = 10;
        infoText = translatableScreenName;
    }
    @Override
    public void tick() {
        super.tick();
        if (getMinecraft().player == null) {
            getMinecraft().setScreen(null);
            return;
        }
        if (!getMinecraft().player.isPassenger()) {
            getMinecraft().setScreen(null);
        }
    }
    @Override
    public boolean isPauseScreen() {
        return false;
    }
    @Nonnull
    public Player getPlayer() {
        return Objects.requireNonNull(getMinecraft().player);
    }
    @Nonnull
    public EntityVehicle getVehicle() {
        return (EntityVehicle) getPlayer().getRootVehicle();
    }
    @Override
    public void renderBackground(@NotNull PoseStack poseStack) {
        super.renderBackground(poseStack);
        if (!infoText.isEmpty()) getMinecraft().font.draw(poseStack,
                UtilMCText.translatable(infoText), guiX+left_padding, guiY+top_padding, infoColor);
    }
}
