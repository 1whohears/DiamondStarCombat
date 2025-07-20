package com.onewhohears.dscombat.client.screen.key_bind;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.*;
import com.onewhohears.dscombat.client.screen.VehicleSubScreen;
import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.nio.FloatBuffer;
import java.util.Set;

public abstract class SelectBindScreen<H extends ActionInputHolder<A>, A extends ActionInput> extends VehicleSubScreen {

    public static class ControllerAxis extends AxisScreen {
        private int joystick_id, axis_id;
        private float dead_zone;
        private boolean invert;
        public ControllerAxis(int page, ActionInputHolder.Axis action, boolean primary) {
            super(page, action, primary);
        }
        @Override
        protected void init() {
            super.init();
            ActionInput.Axis input = getActionInput();
            if (input instanceof ActionInput.ControllerAxis data) {
                joystick_id = data.joystick_id;
                axis_id = data.axis_id;
                dead_zone = data.dead_zone;
                invert = data.invert;
            }
            // SET JOYSTICK ID
            EditBox joystickIDBox = new EditBox(getMinecraft().font, 0, 0, 20, 20, UtilMCText.empty());
            positionWidgetGrid(joystickIDBox, ROWS, 4, 4, 2);
            joystickIDBox.setValue(joystick_id+"");
            joystickIDBox.setTextColor(0xFFFFFF);
            joystickIDBox.setResponder(string -> {
                try { joystick_id = Integer.parseInt(string); }
                catch(NumberFormatException ignored) {}
            });
            // SET AXIS ID
            EditBox axisIDBox = new EditBox(getMinecraft().font, 0, 0, 20, 20, UtilMCText.empty());
            positionWidgetGrid(axisIDBox, ROWS, 4, 5, 2);
            axisIDBox.setValue(axis_id+"");
            axisIDBox.setTextColor(0xFFFFFF);
            axisIDBox.setResponder(string -> {
                try { axis_id = Integer.parseInt(string); }
                catch(NumberFormatException ignored) {}
            });
            // SET DEAD ZONE
            EditBox deadZoneBox = new EditBox(getMinecraft().font, 0, 0, 20, 20, UtilMCText.empty());
            positionWidgetGrid(deadZoneBox, ROWS, 4, 6, 2);
            deadZoneBox.setValue(dead_zone+"");
            deadZoneBox.setTextColor(0xFFFFFF);
            deadZoneBox.setResponder(string -> {
                try { dead_zone = Float.parseFloat(string); }
                catch(NumberFormatException ignored) {}
            });
            // SET INVERT
            positionWidgetGrid(new Checkbox(0, 0, 20, 20, UtilMCText.translatable("ui.dscombat.invert"), invert) {
                                   @Override
                                   public void onPress() {
                                       super.onPress();
                                       invert = !invert;
                                   }
                               },
                    ROWS, 4, 7, 2);
        }
        @Override
        public void renderBackground(@NotNull PoseStack poseStack) {
            super.renderBackground(poseStack);
            float scale = 0.80f;
            float startY = (guiY + top_padding + 70) / scale;
            float startX = (guiX + left_padding) / scale;
            poseStack.pushPose();
            poseStack.scale(scale, scale, scale);
            int k = 0;
            for (int j = 0; j < 16; ++j) {
                if (!GLFW.glfwJoystickPresent(j)) continue;
                String name = GLFW.glfwGetJoystickName(j);
                if (name == null) name = "N/A";
                if (filterJoystick(name)) continue;
                FloatBuffer axes = GLFW.glfwGetJoystickAxes(j);
                if (axes == null) continue;
                for (int a = 0; a < axes.limit(); ++a) {
                    float value = axes.get(a);
                    if (Mth.abs(value) < 0.1f) continue;
                    String text = name+" | ID:"+j+" | Axis:"+a+" | "+value;
                    getMinecraft().font.draw(poseStack, text, startX, startY+k*10, infoColor);
                    ++k;
                }
            }
            poseStack.popPose();
        }
        @Override
        protected ActionInput.Axis createNewInput() {
            return new ActionInput.ControllerAxis(joystick_id, axis_id, dead_zone, invert);
        }
    }

    private static final String[] filteredNames = {"keyboard", "ducky one"};

    /**
     * this exists because linux thinks my keyboard is a joystick
     */
    public static boolean filterJoystick(String name) {
        String n = name.toLowerCase();
        for (String f : filteredNames) if (n.contains(f)) return true;
        return false;
    }

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
