package com.onewhohears.dscombat.client.input;

import com.google.gson.JsonObject;
import com.onewhohears.onewholibs.util.UtilMCText;
import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public abstract class ActionInputHolder<A extends ActionInput> {
    @NotNull private final String id;
    @NotNull private A primaryAction, secondaryAction;
    public ActionInputHolder(@NotNull String id, @NotNull A defaultAction, @NotNull A secondaryAction) {
        this.id = id;
        this.primaryAction = defaultAction;
        this.secondaryAction = secondaryAction;
    }
    public void setPrimaryAction(@NotNull A primaryAction) {
        this.primaryAction = primaryAction;
    }
    public @NotNull A getPrimaryAction() {
        return primaryAction;
    }
    public void setSecondaryAction(@NotNull A secondaryAction) {
        this.secondaryAction = secondaryAction;
    }
    public @NotNull A getSecondaryAction() {
        return secondaryAction;
    }
    public @NotNull A getActiveAction() {
        if (getPrimaryAction().isActive() || !getSecondaryAction().isActive()) return getPrimaryAction();
        return getSecondaryAction();
    }
    public void tick() {
        getPrimaryAction().tick();
        getSecondaryAction().tick();
    }
    public @NotNull String getId() {
        return id;
    }
    public @NotNull String getNameString() {
        return "action.dscombat."+getId();
    }
    public @NotNull Component getName() {
        return UtilMCText.translatable(getNameString());
    }
    public abstract String getType();
    public JsonObject write() {
        JsonObject json = new JsonObject();
        json.addProperty("type", getType());
        json.add("primary", getPrimaryAction().write());
        json.add("secondary", getSecondaryAction().write());
        return json;
    }
    public abstract void read(JsonObject json);

    public static class Button extends ActionInputHolder<ActionInput.Button> {
        public Button(@NotNull String id, ActionInput.@NotNull Button defaultAction) {
            super(id, defaultAction, new ActionInput.UnboundButton());
        }
        @Override
        public String getType() {
            return "button";
        }
        @Override
        public void read(JsonObject json) {
            if (json.has("primary")) {
                JsonObject data = UtilParse.getJsonSafe(json, "primary");
                setPrimaryAction(ActionInput.getButtonByJson(data));
            }
            if (json.has("secondary")) {
                JsonObject data = UtilParse.getJsonSafe(json, "secondary");
                setSecondaryAction(ActionInput.getButtonByJson(data));
            }
        }
        public boolean isPressed() {
            return getActiveAction().isPressed();
        }
        public boolean wasPressed() {
            return getActiveAction().wasPressed();
        }
        public boolean isInitPressed() {
            return getActiveAction().isInitPressed();
        }
        public boolean isInitReleased() {
            return getActiveAction().isInitReleased();
        }
    }

    public static class Axis extends ActionInputHolder<ActionInput.Axis> {
        public Axis(@NotNull String id, ActionInput.@NotNull Axis defaultAction) {
            super(id, defaultAction, new ActionInput.UnboundAxis());
        }
        public float getValue() {
            return getActiveAction().getValue();
        }
        public boolean isNegAndPos() {
            return getActiveAction().isNegAndPos();
        }
        public boolean isJoystickController() {
            return getActiveAction().isControllerJoystick();
        }
        @Override
        public String getType() {
            return "axis";
        }
        @Override
        public void read(JsonObject json) {
            if (json.has("primary")) {
                JsonObject data = UtilParse.getJsonSafe(json, "primary");
                setPrimaryAction(ActionInput.getAxisByJson(data));
            }
            if (json.has("secondary")) {
                JsonObject data = UtilParse.getJsonSafe(json, "secondary");
                setSecondaryAction(ActionInput.getAxisByJson(data));
            }
        }
    }
}
