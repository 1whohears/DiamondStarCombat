package com.onewhohears.dscombat.client.input;

import com.google.gson.JsonObject;
import com.onewhohears.onewholibs.util.UtilMCText;
import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.function.Function;

public interface ActionInput {

    enum ButtonType {
        UNBOUND_BUTTON("unbound_button", json -> new UnboundButton()),
        DSC_KEY_BUTTON("dsc_key_button", DSCKeyButton::read),
        CONTROLLER_BUTTON("controller_button", ControllerButton::read),
        CONTROLLER_AXIS_BUTTON("controller_axis_button", ControllerAxisButton::read);
        public final String id;
        public final Function<JsonObject, Button> gen;
        ButtonType(String id, Function<JsonObject, Button> gen) {
            this.id = id;
            this.gen = gen;
        }
        public String getTypeNameString() {
            return "action_type.dscombat."+id;
        }
        public Component getTypeName() {
            return UtilMCText.translatable(getTypeNameString());
        }
    }

    enum AxisType {
        UNBOUND_AXIS("unbound_axis", json -> new UnboundAxis()),
        DSC_KEY_AXIS("dsc_key_axis", DSCKeyAxis::read),
        CONTROLLER_AXIS("controller_axis", ControllerAxis::read),
        CONTROLLER_BUTTON_AXIS("controller_button_axis", ControllerButtonAxis::read);
        public final String id;
        public final Function<JsonObject, Axis> gen;
        AxisType(String id, Function<JsonObject, Axis> gen) {
            this.id = id;
            this.gen = gen;
        }
        public String getTypeNameString() {
            return "action_type.dscombat."+id;
        }
        public Component getTypeName() {
            return UtilMCText.translatable(getTypeNameString());
        }
    }

    @NotNull
    static Button getButtonByJson(JsonObject json) {
        ButtonType type = UtilParse.getEnumSafe(json, "type", ButtonType.class);
        return type.gen.apply(json);
    }

    @NotNull
    static Axis getAxisByJson(JsonObject json) {
        AxisType type = UtilParse.getEnumSafe(json, "type", AxisType.class);
        return type.gen.apply(json);
    }

    static boolean isWindowActive() {
        Minecraft m = Minecraft.getInstance();
        return m.isWindowActive() && m.screen == null;
    }

    @NotNull String getId();
    @NotNull String getType();
    void tick();
    boolean isActive();
    @NotNull JsonObject write();
    default boolean isUnbound() {
        return false;
    }

    abstract class Button implements ActionInput {
        private boolean isPressed, wasPressed;
        @Override
        public void tick() {
            wasPressed = isPressed;
            isPressed = checkIsPressed();
        }
        public boolean isPressed() {
            return isPressed;
        }
        public boolean wasPressed() {
            return wasPressed;
        }
        protected abstract boolean checkIsPressed();
        public boolean isInitPressed() {
            return isPressed() && !wasPressed();
        }
        public boolean isInitReleased() {
            return !isPressed() && wasPressed();
        }
        @Override
        public boolean isActive() {
            return isPressed() || wasPressed();
        }
        public abstract ButtonType getButtonType();
        @Override
        public @NotNull String getType() {
            return getButtonType().id;
        }
    }

    interface Axis extends ActionInput {
        float getValue();
        boolean isNegAndPos();
        default boolean isActive() {
            return getValue() != 0 || isNegAndPos();
        }
        AxisType getAxisType();
        default @NotNull String getType() {
            return getAxisType().id;
        }
        boolean isControllerJoystick();
    }

    class DSCKeyButton extends Button {
        public final String key_mapping_id;
        public DSCKeyButton(@NotNull String key_mapping_id) {
            this.key_mapping_id = key_mapping_id;
        }
        @Override
        public @NotNull String getId() {
            return key_mapping_id;
        }
        @Override
        public @NotNull JsonObject write() {
            JsonObject json = new JsonObject();
            UtilParse.writeEnum(json, "type", getButtonType());
            json.addProperty("key_mapping_id", key_mapping_id);
            return json;
        }
        protected boolean checkIsPressed() {
            return DSCKeys.isKeyPressed(getId());
        }
        @Override
        public ButtonType getButtonType() {
            return ButtonType.DSC_KEY_BUTTON;
        }
        public static DSCKeyButton read(JsonObject json) {
            return new DSCKeyButton(UtilParse.getStringSafe(json, "key_mapping_id", ""));
        }
    }

    class DSCKeyAxis implements Axis {
        @NotNull public final String key_mapping_id_negative, key_mapping_id_positive;
        @NotNull private final String id;
        private boolean negativePressed, positivePressed;
        public DSCKeyAxis(@NotNull String key_mapping_id_negative, @NotNull String key_mapping_id_positive) {
            id = key_mapping_id_negative+":"+key_mapping_id_positive;
            this.key_mapping_id_negative = key_mapping_id_negative;
            this.key_mapping_id_positive = key_mapping_id_positive;
        }
        @Override
        public @NotNull String getId() {
            return id;
        }
        @Override
        public AxisType getAxisType() {
            return AxisType.DSC_KEY_AXIS;
        }
        @Override
        public boolean isControllerJoystick() {
            return false;
        }
        @Override
        public void tick() {
            negativePressed = DSCKeys.isKeyPressed(key_mapping_id_negative);
            positivePressed = DSCKeys.isKeyPressed(key_mapping_id_positive);
        }
        @Override
        public float getValue() {
            if (negativePressed && positivePressed) return 0;
            else if (negativePressed) return -1;
            else if (positivePressed) return 1;
            return 0;
        }
        @Override
        public boolean isNegAndPos() {
            return negativePressed && positivePressed;
        }
        @Override
        public @NotNull JsonObject write() {
            JsonObject json = new JsonObject();
            UtilParse.writeEnum(json, "type", getAxisType());
            json.addProperty("key_mapping_id_negative", key_mapping_id_negative);
            json.addProperty("key_mapping_id_positive", key_mapping_id_positive);
            return json;
        }
        public static DSCKeyAxis read(JsonObject json) {
            return new DSCKeyAxis(UtilParse.getStringSafe(json, "key_mapping_id_negative", ""),
                    UtilParse.getStringSafe(json, "key_mapping_id_positive", ""));
        }
    }

    class ControllerButton extends Button {
        @NotNull private final String id;
        public final int joystick_id, button_id;
        public ControllerButton(int joystick_id, int button_id) {
            this.joystick_id = joystick_id;
            this.button_id = button_id;
            this.id = joystick_id+":"+button_id;
        }
        protected boolean checkIsPressed() {
            if (!isWindowActive()) return false;
            if (!GLFW.glfwJoystickPresent(joystick_id)) return false;
            ByteBuffer buttons = GLFW.glfwGetJoystickButtons(joystick_id);
            if (buttons == null) return false;
            if (button_id > buttons.limit() - 1 && button_id >= 0) return false;
            return buttons.get(button_id) == 1;
        }
        @Override
        public @NotNull String getId() {
            return id;
        }
        @Override
        public ButtonType getButtonType() {
            return ButtonType.CONTROLLER_BUTTON;
        }
        @Override
        public @NotNull JsonObject write() {
            JsonObject json = new JsonObject();
            UtilParse.writeEnum(json, "type", getButtonType());
            json.addProperty("joystick_id", joystick_id);
            json.addProperty("button_id", button_id);
            return json;
        }
        public static ControllerButton read(JsonObject json) {
            return new ControllerButton(UtilParse.getIntSafe(json, "joystick_id", 0),
                    UtilParse.getIntSafe(json, "button_id", 0));
        }
    }

    class ControllerAxis implements Axis {
        @NotNull private final String id;
        public final int joystick_id, axis_id;
        public final float dead_zone;
        public final boolean invert;
        public float value;
        public ControllerAxis(int joystick_id, int axis_id, float dead_zone, boolean invert) {
            this.joystick_id = joystick_id;
            this.axis_id = axis_id;
            this.id = joystick_id+":"+axis_id;
            this.dead_zone = dead_zone;
            this.invert = invert;
        }
        @Override
        public void tick() {
            value = checkValue();
        }
        @Override
        public float getValue() {
            return value;
        }
        @Override
        public boolean isNegAndPos() {
            return false;
        }
        private float checkValue() {
            if (!isWindowActive()) return value;
            if (!GLFW.glfwJoystickPresent(joystick_id)) return 0;
            FloatBuffer axes = GLFW.glfwGetJoystickAxes(joystick_id);
            if (axes == null) return 0;
            if (axis_id > axes.limit() - 1 && axis_id >= 0) return 0;
            float axis = axes.get(axis_id);
            if (Mth.abs(axis) < dead_zone) return 0;
            if (invert) axis *= -1;
            return axis;
        }
        @Override
        public @NotNull String getId() {
            return id;
        }
        @Override
        public AxisType getAxisType() {
            return AxisType.CONTROLLER_AXIS;
        }
        @Override
        public boolean isControllerJoystick() {
            return true;
        }
        @Override
        public @NotNull JsonObject write() {
            JsonObject json = new JsonObject();
            UtilParse.writeEnum(json, "type", getAxisType());
            json.addProperty("joystick_id", joystick_id);
            json.addProperty("axis_id", axis_id);
            json.addProperty("dead_zone", dead_zone);
            json.addProperty("invert", invert);
            return json;
        }
        public static ControllerAxis read(JsonObject json) {
            return new ControllerAxis(UtilParse.getIntSafe(json, "joystick_id", 0),
                    UtilParse.getIntSafe(json, "axis_id", 0),
                    UtilParse.getFloatSafe(json, "dead_zone", 0),
                    UtilParse.getBooleanSafe(json, "invert", false));
        }
    }

    class ControllerAxisButton extends Button {
        @NotNull private final String id;
        public final int joystick_id, axis_id;
        public final float dead_zone;
        public final boolean positive;
        public ControllerAxisButton(int joystick_id, int axis_id, float dead_zone, boolean positive) {
            this.joystick_id = joystick_id;
            this.axis_id = axis_id;
            this.positive = positive;
            this.dead_zone = dead_zone;
            this.id = joystick_id+":"+axis_id+":"+positive;
        }
        protected boolean checkIsPressed() {
            if (!isWindowActive()) return false;
            if (!GLFW.glfwJoystickPresent(joystick_id)) return false;
            FloatBuffer axes = GLFW.glfwGetJoystickAxes(joystick_id);
            if (axes == null) return false;
            if (axis_id > axes.limit() - 1 && axis_id >= 0) return false;
            float axis = axes.get(axis_id);
            if (Mth.abs(axis) < dead_zone) return false;
            return (positive && axis > 0) || (!positive && axis < 0);
        }
        @Override
        public @NotNull String getId() {
            return id;
        }
        @Override
        public ButtonType getButtonType() {
            return ButtonType.CONTROLLER_AXIS_BUTTON;
        }
        @Override
        public @NotNull JsonObject write() {
            JsonObject json = new JsonObject();
            UtilParse.writeEnum(json, "type", getButtonType());
            json.addProperty("joystick_id", joystick_id);
            json.addProperty("axis_id", axis_id);
            json.addProperty("dead_zone", dead_zone);
            json.addProperty("positive", positive);
            return json;
        }
        public static ControllerAxisButton read(JsonObject json) {
            return new ControllerAxisButton(UtilParse.getIntSafe(json, "joystick_id", 0),
                    UtilParse.getIntSafe(json, "axis_id", 0),
                    UtilParse.getFloatSafe(json, "dead_zone", 0),
                    UtilParse.getBooleanSafe(json, "positive", false));
        }
    }

    class ControllerButtonAxis implements Axis {
        @NotNull private final String id;
        public final int joystick_id, positive_button_id, negative_button_id;
        private boolean negativePressed, positivePressed;
        public ControllerButtonAxis(int joystickId, int positiveButtonId, int negativeButtonId) {
            this.id = joystickId+":"+positiveButtonId+":"+negativeButtonId;
            joystick_id = joystickId;
            positive_button_id = positiveButtonId;
            negative_button_id = negativeButtonId;
        }
        @Override
        public void tick() {
            negativePressed = checkIsPressed(negative_button_id);
            positivePressed = checkIsPressed(positive_button_id);
        }
        protected boolean checkIsPressed(int button_id) {
            if (!isWindowActive()) return false;
            if (!GLFW.glfwJoystickPresent(joystick_id)) return false;
            ByteBuffer buttons = GLFW.glfwGetJoystickButtons(joystick_id);
            if (buttons == null) return false;
            if (button_id > buttons.limit() - 1 && button_id >= 0) return false;
            return buttons.get(button_id) == 1;
        }
        @Override
        public float getValue() {
            if (negativePressed && positivePressed) return 0;
            else if (negativePressed) return -1;
            else if (positivePressed) return 1;
            return 0;
        }
        @Override
        public boolean isNegAndPos() {
            return negativePressed && positivePressed;
        }
        @Override
        public AxisType getAxisType() {
            return AxisType.CONTROLLER_BUTTON_AXIS;
        }
        @Override
        public boolean isControllerJoystick() {
            return false;
        }
        @Override
        public @NotNull String getId() {
            return id;
        }
        @Override
        public @NotNull JsonObject write() {
            JsonObject json = new JsonObject();
            UtilParse.writeEnum(json, "type", getAxisType());
            json.addProperty("joystick_id", joystick_id);
            json.addProperty("positive_button_id", positive_button_id);
            json.addProperty("negative_button_id", negative_button_id);
            return json;
        }
        public static ControllerButtonAxis read(JsonObject json) {
            return new ControllerButtonAxis(UtilParse.getIntSafe(json, "joystick_id", 0),
                    UtilParse.getIntSafe(json, "positive_button_id", 0),
                    UtilParse.getIntSafe(json, "negative_button_id", 0));
        }
    }

    class UnboundButton extends Button {
        @Override
        public boolean isUnbound() {
            return true;
        }
        @Override
        protected boolean checkIsPressed() {
            return false;
        }
        @Override
        public @NotNull String getId() {
            return "none";
        }
        @Override
        public ButtonType getButtonType() {
            return ButtonType.UNBOUND_BUTTON;
        }
        @Override
        public @NotNull JsonObject write() {
            JsonObject json = new JsonObject();
            UtilParse.writeEnum(json, "type", getButtonType());
            return json;
        }
    }

    class UnboundAxis implements Axis {
        @Override
        public boolean isUnbound() {
            return true;
        }
        @Override
        public float getValue() {
            return 0;
        }
        @Override
        public boolean isNegAndPos() {
            return false;
        }
        @Override
        public AxisType getAxisType() {
            return AxisType.UNBOUND_AXIS;
        }
        @Override
        public boolean isControllerJoystick() {
            return false;
        }
        @Override
        public @NotNull String getId() {
            return "none";
        }
        @Override
        public void tick() {

        }
        @Override
        public @NotNull JsonObject write() {
            JsonObject json = new JsonObject();
            UtilParse.writeEnum(json, "type", getAxisType());
            return json;
        }
    }

}
