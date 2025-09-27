package com.onewhohears.dscombat.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.common.network.VehicleSyncAction;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.client.screen.BackgroundScreen;
import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class VehicleScreen extends BackgroundScreen {
    protected int ROWS = 7, COLUMNS = 2;
    protected String titleText = "";
    protected Component info = null;
    protected int infoColor = 0x0000AA, infoTextYOffset = 164, infoTicks = -1;
    protected VehicleScreen(Component title, ResourceLocation backgroundTexture,
                            int imageWidth, int imageHeight, int textureWidth, int textureHeight) {
        super(title, backgroundTexture, imageWidth, imageHeight, textureWidth, textureHeight);
        vertical_widget_shift = 10;
    }
    protected VehicleScreen(String translatableScreenName, ResourceLocation backgroundTexture,
                            int imageWidth, int imageHeight, int textureWidth, int textureHeight) {
        super(translatableScreenName, backgroundTexture, imageWidth, imageHeight, textureWidth, textureHeight);
        vertical_widget_shift = 10;
        titleText = translatableScreenName;
    }
    @Override
    public void tick() {
        super.tick();
        if (minecraft.player == null) {
            minecraft.setScreen(null);
            return;
        }
        if (!minecraft.player.isPassenger()) {
            minecraft.setScreen(null);
            return;
        }
        if (infoTicks > 0) --infoTicks;
    }
    @Override
    public boolean isPauseScreen() {
        return false;
    }
    @NotNull
    public Player getPlayer() {
        return Objects.requireNonNull(minecraft.player);
    }
    @NotNull
    public EntityVehicle getVehicle() {
        return (EntityVehicle) getPlayer().getRootVehicle();
    }
    @Override
    public void renderBackground(@NotNull PoseStack poseStack) {
        super.renderBackground(poseStack);
        if (!titleText.isEmpty()) minecraft.font.draw(poseStack,
                UtilMCText.translatable(titleText), guiX+left_padding, guiY+top_padding, infoColor);
        if (info != null && infoTicks != 0) minecraft.font.draw(poseStack, info,
                guiX+left_padding, guiY+top_padding+infoTextYOffset, infoColor);
    }
    public void setInfoText(String info_text, int display_time) {
        info = UtilMCText.translatable(info_text);
        infoTicks = display_time;
    }
    public void setInfoFromMessage(Component component, int display_time) {
        info = component;
        infoTicks = display_time;
    }
    public static void sendSyncAction(VehicleSyncAction action) {
        VehicleSyncAction.sendSyncAction(action);
    }
}
