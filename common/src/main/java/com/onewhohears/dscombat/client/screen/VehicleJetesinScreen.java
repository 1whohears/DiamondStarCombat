package com.onewhohears.dscombat.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.common.network.VehicleSyncAction;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.parts.instance.PartInstance;
import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class VehicleJetesinScreen extends VehicleSubScreen {

    public static final ResourceLocation BG_TEXTURE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/background/vehicle_jetesin_screen.png");

    private static final int imageWidth = 240, imageHeight = 180;
    private static final int textureSize = 256;

    private static final Style style = Style.EMPTY.withColor(0x008800);

    private List<PartSlot> slots = new ArrayList<>();

    protected VehicleJetesinScreen() {
        super("screen.dscombat.vehicle_jetesin_screen",
                BG_TEXTURE, imageWidth, imageHeight, textureSize, textureSize);
    }

    @Override
    protected void init() {
        vertical_widget_shift = 10;
        super.init();
        vertical_widget_shift = 34;
        float scale = 2f / 3f;
        int textHeight = (int)(10f * scale);
        int xStart = guiX + left_padding, x = xStart;
        int y = guiY + top_padding + vertical_widget_shift;
        int width = (image_width - left_padding - right_padding) / 3;
        slots = getVehicle().partsManager.getExternalParts();
        for (int i = 0; i < slots.size(); ++i) {
            PartSlot slot = slots.get(i);
            PartInstance<?> part = slot.getPartData();
            if (part == null) continue;
            Button jetesin = new Button(0, 0, width, 20,
                    UtilMCText.translatable("ui.dscombat.drop"),
                    button -> onJetesinButton(button, slot.getSlotId()));
            jetesin.x = x;
            jetesin.y = y + textHeight;
            addRenderableWidget(jetesin);
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
        int xStart = (int)((float)(guiX + left_padding) / scale) + 1, x = xStart;
        int y = (int)((float)(guiY + top_padding + vertical_widget_shift) / scale);
        int width = (int)((float)(image_width - left_padding - right_padding) / 3f / scale);
        for (int i = 0; i < slots.size(); ++i) {
            PartSlot slot = slots.get(i);
            PartInstance<?> part = slot.getPartData();
            if (part != null) minecraft.font.draw(poseStack,
                    part.getItemName().setStyle(style), x, y, 0xFFFFFF);
            if (i % 3 == 2) {
                y += 40;
                x = xStart;
            } else x += width;
        }
        poseStack.popPose();
    }

    private void onJetesinButton(Button button, String slotId) {
        sendSyncAction(new VehicleSyncAction.JetesinAction(slotId));
        button.visible = false;
    }
}
