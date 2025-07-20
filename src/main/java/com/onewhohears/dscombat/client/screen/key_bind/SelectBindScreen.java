package com.onewhohears.dscombat.client.screen.key_bind;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.*;
import com.onewhohears.dscombat.client.screen.VehicleSubScreen;
import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class SelectBindScreen<H extends ActionInputHolder<A>, A extends ActionInput> extends VehicleSubScreen {

    public static class DSCKeyAxis extends AxisScreen {
        private final boolean positive;
        private String key_mapping_id_negative = "unbound", key_mapping_id_positive = "unbound";
        public DSCKeyAxis(int page, ActionInputHolder.Axis action, boolean primary, boolean positive) {
            super(page, action, primary);
            this.positive = positive;
        }
        @Override
        protected void init() {
            super.init();
            ActionInput.Axis input = getActionInput();
            if (input instanceof ActionInput.DSCKeyAxis data) {
                key_mapping_id_negative = data.key_mapping_id_negative;
                key_mapping_id_positive = data.key_mapping_id_positive;
            }
            // KEY ID BOX
            EditBox keyIDBox = new EditBox(getMinecraft().font, 0, 0, 20, 20, UtilMCText.empty());
            positionWidgetGrid(keyIDBox, ROWS, COLUMNS, 2, 2);
            if (positive) keyIDBox.setValue(key_mapping_id_positive);
            else keyIDBox.setValue(key_mapping_id_negative);
            keyIDBox.setTextColor(0xFFFFFF);
            keyIDBox.setResponder(string -> {
                if (DSCKeys.hasKey(string)) {
                    if (positive) key_mapping_id_positive = string;
                    else key_mapping_id_negative = string;
                    keyIDBox.setTextColor(0x00FF00);
                } else {
                    keyIDBox.setTextColor(0xFF0000);
                }
            });
        }
        @Override
        public void renderBackground(@NotNull PoseStack poseStack) {
            super.renderBackground(poseStack);
            float scale = 0.70f;
            float startY = (guiY + top_padding + 60) / scale;
            float startX = (guiX + left_padding) / scale;
            float width2 = image_width / 2f / scale;
            poseStack.pushPose();
            poseStack.scale(scale, scale, scale);
            String test = positive ? key_mapping_id_positive : key_mapping_id_negative;
            int i = 0;
            Set<String> keys = DSCKeys.getKeyIds();
            for (String key : keys) {
                int color = infoColor;
                if (key.equals(test)) color = 0x00FF00;
                getMinecraft().font.draw(poseStack, key, startX+width2*(i%2), startY+Mth.floor(i/2f)*10, color);
                ++i;
            }
            poseStack.popPose();
        }
        @Override
        protected ActionInput.Axis createNewInput() {
            return new ActionInput.DSCKeyAxis(key_mapping_id_negative, key_mapping_id_positive);
        }
    }

    public abstract static class AxisScreen extends SelectBindScreen<ActionInputHolder.Axis, ActionInput.Axis> {
        public AxisScreen(int page, ActionInputHolder.Axis action, boolean primary) {
            super(page, action, primary);
        }
        @Override
        protected Screen getBackScreen() {
            return new VehicleEditAxisBindScreen(page, action);
        }
    }

    public abstract static class ButtonScreen extends SelectBindScreen<ActionInputHolder.Button, ActionInput.Button> {
        public ButtonScreen(int page, ActionInputHolder.Button action, boolean primary) {
            super(page, action, primary);
        }
        @Override
        protected Screen getBackScreen() {
            return new VehicleEditButtonBindScreen(page, action);
        }
    }

    public static final ResourceLocation BG_TEXTURE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/background/vehicle_other_screen.png");

    private static final int imageWidth = 240, imageHeight = 180;
    private static final int textureSize = 256;

    protected final int page;
    protected final H action;
    protected final boolean primary;

    public SelectBindScreen(int page, H action, boolean primary) {
        super(action.getNameString(), BG_TEXTURE, imageWidth, imageHeight, textureSize, textureSize);
        this.page = page;
        this.action = action;
        this.primary = primary;
    }

    @Override
    protected void init() {
        super.init();
        positionWidgetGrid(new Button(0, 0, 20, 20,
                        UtilMCText.translatable("ui.dscombat.save"),
                        onPress -> {
                            applyChanges();
                            ClientInputManager.saveKeyBinds();
                            Minecraft.getInstance().setScreen(getBackScreen());
                        }),
                ROWS, COLUMNS, 1, padding);
    }

    protected abstract A createNewInput();
    protected void applyChanges() {
        A input = createNewInput();
        if (primary) action.setPrimaryAction(input);
        else action.setSecondaryAction(input);
    }

    protected A getActionInput() {
        if (primary) return action.getPrimaryAction();
        else return action.getSecondaryAction();
    }

    @Override
    public void renderBackground(@NotNull PoseStack poseStack) {
        super.renderBackground(poseStack);
        Component alt;
        if (primary) alt = UtilMCText.translatable("ui.dscombat.primary");
        else alt = UtilMCText.translatable("ui.dscombat.secondary");
        getMinecraft().font.draw(poseStack, alt, guiX+image_width-100, guiY+top_padding, infoColor);
    }

}
