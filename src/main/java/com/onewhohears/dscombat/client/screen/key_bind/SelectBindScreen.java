package com.onewhohears.dscombat.client.screen.key_bind;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.ActionInput;
import com.onewhohears.dscombat.client.input.ActionInputHolder;
import com.onewhohears.dscombat.client.input.ClientInputManager;
import com.onewhohears.dscombat.client.screen.VehicleSubScreen;
import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public abstract class SelectBindScreen<H extends ActionInputHolder<A>, A extends ActionInput> extends VehicleSubScreen {

    public static class DSCKeyAxis extends AxisScreen {
        private final boolean positive;
        private String key_mapping_id_negative, key_mapping_id_positive;
        public DSCKeyAxis(int page, ActionInputHolder.Axis action, boolean primary, boolean positive) {
            super(page, action, primary);
            this.positive = positive;
        }
        @Override
        protected void init() {
            super.init();

        }
        @Override
        public void renderBackground(@NotNull PoseStack poseStack) {
            super.renderBackground(poseStack);

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
                        }),
                ROWS, COLUMNS, 1, padding);
    }

    protected abstract A createNewInput();
    protected void applyChanges() {
        A input = createNewInput();
        if (primary) action.setPrimaryAction(input);
        else action.setSecondaryAction(input);
    }

    @Override
    public void renderBackground(@NotNull PoseStack poseStack) {
        super.renderBackground(poseStack);
        Component alt;
        if (primary) alt = UtilMCText.translatable("ui.dscombat.primary");
        else alt = UtilMCText.translatable("ui.dscombat.secondary");
        getMinecraft().font.draw(poseStack, alt, guiX+width-40, guiY+top_padding, infoColor);
    }

}
