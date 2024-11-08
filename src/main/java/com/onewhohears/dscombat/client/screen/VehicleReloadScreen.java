package com.onewhohears.dscombat.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.common.network.VehicleSyncAction;
import com.onewhohears.dscombat.data.parts.ReloadablePartInstance;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VehicleReloadScreen extends VehicleSubScreen {

    public static final ResourceLocation BG_TEXTURE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/background/vehicle_reload_screen.png");

    private static final int imageWidth = 240, imageHeight = 180;
    private static final int textureSize = 256;

    private static final Style style = Style.EMPTY.withColor(0x00AA00);

    protected VehicleReloadScreen() {
        super("screen.dscombat.vehicle_reload_screen",
                BG_TEXTURE, imageWidth, imageHeight, textureSize, textureSize);
    }

    @Override
    protected void init() {
        COLUMNS = 3;
        vertical_widget_shift = 10;
        super.init();
        positionWidgetGrid(new Button(0, 0, 20, 20,
                        UtilMCText.translatable("ui.dscombat.reload_all"),
                        onPress -> onReloadAllButton()),
                ROWS, COLUMNS, 1, 2);
        positionWidgetGrid(new Button(0, 0, 20, 20,
                        UtilMCText.translatable("ui.dscombat.unload_all"),
                        onPress -> onUnloadAllButton()),
                ROWS, COLUMNS, 2, 2);
        vertical_widget_shift = 34;
        float scale = 2f / 3f;
        int textHeight = (int)(20f * scale);
        int xStart = guiX + left_padding, x = xStart;
        int y = guiY + top_padding + vertical_widget_shift;
        int width = (image_width - left_padding - right_padding) / 3;
        List<PartSlot> slots = getVehicle().partsManager.getReloadableParts();
        int halfWidth = width / 2;
        for (int i = 0; i < slots.size(); ++i) {
            PartSlot slot = slots.get(i);
            ReloadablePartInstance part = (ReloadablePartInstance) slot.getPartData();
            if (part == null) continue;
            Button reload = new Button(0, 0, halfWidth, 20,
                    UtilMCText.translatable("ui.dscombat.reload"),
                    onPress -> onReloadButton(slot.getSlotId()));
            reload.x = x;
            reload.y = y + textHeight;
            addRenderableWidget(reload);
            Button unload = new Button(0, 0, halfWidth, 20,
                    UtilMCText.translatable("ui.dscombat.unload"),
                    onPress -> onUnloadButton(slot.getSlotId()));
            unload.x = x + halfWidth;
            unload.y = y + textHeight;
            addRenderableWidget(unload);
            if (i % 3 == 2) {
                y += textHeight + 20;
                x = xStart;
            } else x += width;
        }
    }

    @Override
    public void renderBackground(@NotNull PoseStack poseStack) {
        super.renderBackground(poseStack);
        poseStack.pushPose();
        float scale = 2f / 3f;
        poseStack.scale(scale, scale, 1);
        int xStart = (int)((float)(guiX + left_padding) / scale), x = xStart;
        int y = (int)((float)(guiY + top_padding + vertical_widget_shift) / scale);
        int width = (int)((float)(image_width - left_padding - right_padding) / 3f / scale);
        List<PartSlot> slots = getVehicle().partsManager.getReloadableParts();
        for (int i = 0; i < slots.size(); ++i) {
            ReloadablePartInstance part = (ReloadablePartInstance) slots.get(i).getPartData();
            if (part == null) continue;
            getMinecraft().font.draw(poseStack, part.getItemName().setStyle(style), x, y, 0xFFFFFF);
            MutableComponent ammo = UtilMCText.translatable("info.dscombat.ammo")
                    .append(": "+(int)part.getCurrentAmmo()+"/"+(int)part.getMaxAmmo());
            getMinecraft().font.draw(poseStack, ammo.setStyle(style), x, y+10, 0xFFFFFF);
            if (i % 3 == 2) {
                y += 20 + (int)(20f / scale);
                x = xStart;
            } else x += width;
        }
        poseStack.popPose();
    }

    private void onReloadAllButton() {
        sendSyncAction(new VehicleSyncAction.LoadPartAction(false));
    }

    private void onUnloadAllButton() {
        sendSyncAction(new VehicleSyncAction.LoadPartAction(true));
    }

    private void onReloadButton(String slotId) {
        sendSyncAction(new VehicleSyncAction.LoadPartAction(slotId, false));
    }

    private void onUnloadButton(String slotId) {
        sendSyncAction(new VehicleSyncAction.LoadPartAction(slotId, true));
    }
}
