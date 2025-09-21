package com.onewhohears.dscombat.client.event;

public class CameraAngles {
    private float roll = 0, pitch = 0, yaw = 0;
    private boolean isChanged = false;

    public void reset() {
        roll = 0; pitch = 0; yaw = 0;
        isChanged = false;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
        this.isChanged = true;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
        this.isChanged = true;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
        this.isChanged = true;
    }

    public boolean isChanged() {
        return isChanged;
    }
}
