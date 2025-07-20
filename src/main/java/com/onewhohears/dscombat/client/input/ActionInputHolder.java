package com.onewhohears.dscombat.client.input;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ActionInputHolder<A extends ActionInput> {
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

    public static class Button extends ActionInputHolder<ActionInput.Button> {
        public Button(@NotNull String id, ActionInput.@Nullable Button defaultAction) {
            super(id, defaultAction);
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
    }
}
