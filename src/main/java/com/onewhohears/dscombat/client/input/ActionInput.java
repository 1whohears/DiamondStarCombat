package com.onewhohears.dscombat.client.input;

import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public interface ActionInput {
    @NotNull String getId();
    void tick();
    boolean isActive();

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
    }

    interface Axis extends ActionInput {
        float getValue();
        boolean isNegAndPos();
        default boolean isActive() {
            return getValue() != 0;
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
        protected boolean checkIsPressed() {
            return DSCKeys.isKeyPressed(getId());
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
    }

}
