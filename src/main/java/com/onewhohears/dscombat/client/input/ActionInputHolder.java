package com.onewhohears.dscombat.client.input;

import com.google.gson.JsonObject;
import com.onewhohears.onewholibs.util.UtilMCText;
import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ActionInputHolder<A extends ActionInput> {
    @NotNull private final String id;
    @Nullable private A primaryAction, secondaryAction;
    public ActionInputHolder(@NotNull String id, @Nullable A defaultAction) {
        this.id = id;
        this.primaryAction = defaultAction;
    }
    public void setPrimaryAction(@Nullable A primaryAction) {
        this.primaryAction = primaryAction;
    }
    public @Nullable A getPrimaryAction() {
        return primaryAction;
    }
    public void setSecondaryAction(@Nullable A secondaryAction) {
        this.secondaryAction = secondaryAction;
    }
    public @Nullable A getSecondaryAction() {
        return secondaryAction;
    }
    public @Nullable A getActiveAction() {
        if (getPrimaryAction() != null && getPrimaryAction().isActive()) return getPrimaryAction();
        return getSecondaryAction();
    }
    public void tick() {
        if (getPrimaryAction() != null)
            getPrimaryAction().tick();
        if (getSecondaryAction() != null)
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
        if (getPrimaryAction() != null) json.add("primary", getPrimaryAction().write());
        if (getSecondaryAction() != null) json.add("secondary", getSecondaryAction().write());
        return json;
    }
    public abstract void read(JsonObject json);

    public static class Button extends ActionInputHolder<ActionInput.Button> {
        public Button(@NotNull String id, ActionInput.@Nullable Button defaultAction) {
            super(id, defaultAction);
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
            ActionInput.Button action = getActiveAction();
            if (action == null) return false;
            return action.isPressed();
        }
        public boolean wasPressed() {
            ActionInput.Button action = getActiveAction();
            if (action == null) return false;
            return action.wasPressed();
        }
        public boolean isInitPressed() {
            ActionInput.Button action = getActiveAction();
            if (action == null) return false;
            return action.isInitPressed();
        }
        public boolean isInitReleased() {
            ActionInput.Button action = getActiveAction();
            if (action == null) return false;
            return action.isInitReleased();
        }
    }

    public static class Axis extends ActionInputHolder<ActionInput.Axis> {
        public Axis(@NotNull String id, ActionInput.@Nullable Axis defaultAction) {
            super(id, defaultAction);
        }
        public float getValue() {
            ActionInput.Axis action = getActiveAction();
            if (action == null) return 0;
            return action.getValue();
        }
        public boolean isNegAndPos() {
            ActionInput.Axis action = getActiveAction();
            if (action == null) return false;
            return action.isNegAndPos();
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
