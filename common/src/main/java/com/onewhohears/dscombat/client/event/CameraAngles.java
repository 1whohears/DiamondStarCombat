package com.onewhohears.dscombat.client.event;

public class CameraAngles {
    private float roll = 0, pitch = 0, yaw = 0;
    private boolean isRollChanged = false;
    private boolean isPitchChanged = false;
    private boolean isYawChanged = false;

    public void reset() {
        roll = 0; pitch = 0; yaw = 0;
        isRollChanged = false;
        isPitchChanged = false;
        isYawChanged = false;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
        this.isRollChanged = true;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
        this.isPitchChanged = true;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
        this.isYawChanged = true;
    }

    public boolean isRollChanged() {
        return isRollChanged;
    }

    public boolean isPitchChanged() {
        return isPitchChanged;
    }

    public boolean isYawChanged() {
        return isYawChanged;
    }
}
