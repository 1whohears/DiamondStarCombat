package com.onewhohears.dscombat.client.input;

import com.google.gson.JsonObject;
import com.onewhohears.onewholibs.util.UtilMCText;
import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
        CONTROLLER_AXIS("controller_axis", ControllerAxis::read);
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
            return getValue() != 0;
        }
        AxisType getAxisType();
        default @NotNull String getType() {
            return getAxisType().id;
        }
    }

    class DSCKeyButton extends Button {
        private final String id;
        public DSCKeyButton(@NotNull String key_mapping_id) {
            id = key_mapping_id;
        }
        @Override
        public @NotNull String getId() {
            return id;
        }
        @Override
        public @NotNull JsonObject write() {
            JsonObject json = new JsonObject();
            UtilParse.writeEnum(json, "type", getButtonType());
            json.addProperty("key_mapping_id", id);
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
        @NotNull private final String key_mapping_id_negative, key_mapping_id_positive;
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
        private final int joystick_id, button_id;
        public ControllerButton(int joystick_id, int button_id) {
            this.joystick_id = joystick_id;
            this.button_id = button_id;
            this.id = joystick_id+":"+button_id;
        }
        protected boolean checkIsPressed() {
            if (!GLFW.glfwJoystickPresent(joystick_id)) return false;
            ByteBuffer buttons = GLFW.glfwGetJoystickButtons(joystick_id);
            if (buttons == null) return false;
            if (button_id > buttons.limit() - 1) return false;
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
        private final int joystick_id, axis_id;
        private final float dead_zone;
        private final boolean invert;
        private float value;
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
            if (!GLFW.glfwJoystickPresent(joystick_id)) return 0;
            FloatBuffer axes = GLFW.glfwGetJoystickAxes(joystick_id);
            if (axes == null) return 0;
            if (axis_id > axes.limit() - 1) return 0;
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
        private final int joystick_id, axis_id;
        private final float dead_zone;
        private final boolean positive;
        public ControllerAxisButton(int joystick_id, int axis_id, float dead_zone, boolean positive) {
            this.joystick_id = joystick_id;
            this.axis_id = axis_id;
            this.positive = positive;
            this.dead_zone = dead_zone;
            this.id = joystick_id+":"+axis_id+":"+positive;
        }
        protected boolean checkIsPressed() {
            if (!GLFW.glfwJoystickPresent(joystick_id)) return false;
            FloatBuffer axes = GLFW.glfwGetJoystickAxes(joystick_id);
            if (axes == null) return false;
            if (axis_id > axes.limit() - 1) return false;
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
