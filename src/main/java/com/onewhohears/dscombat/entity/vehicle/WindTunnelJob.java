package com.onewhohears.dscombat.entity.vehicle;

import com.onewhohears.dscombat.util.math.UtilEstimate;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;

public abstract class WindTunnelJob {

    public static class FindOptimalDragC extends WindTunnelJob {
        private final float yaw, roll, pitch, liftC;
        private double prevWindAcc;
        private double dragC = 0.02, prevDragC;
        public FindOptimalDragC(float yaw, float roll, float pitch, float liftC) {
            this.yaw = yaw;
            this.roll = roll;
            this.pitch = pitch;
            this.liftC = liftC;
        }
        @Override
        protected void init(EntityWindTunnel tunnel) {
            tunnel.chatToNearbyPlayers("Searching for optimal Drag Coefficient...", ChatFormatting.YELLOW);
            tunnel.setQ(UtilAngles.toQuaternion(yaw, pitch, roll));
            prevWindAcc = -1000;
            prevDragC = -1000;
            updateDragC(tunnel);
        }
        @Override
        protected boolean isJobComplete(EntityWindTunnel tunnel) {
            return tunnel.windCompAcc > 0 && tunnel.windCompAcc < 0.0001;
        }
        @Override
        protected void run(EntityWindTunnel tunnel) {
            double currentWindAcc = tunnel.windCompAcc * 1E3;
            double currentDragC = dragC * 1E3;
            //System.out.println("currentDragC = "+dragC+" currentWindAcc = "+tunnel.windCompAcc);
            if (prevWindAcc == -1000) {
                prevWindAcc = currentWindAcc;
                prevDragC = currentDragC;
                dragC += 0.01;
                updateDragC(tunnel);
                return;
            }
            try {
                dragC = UtilEstimate.nextGuessSecantMethod(prevDragC, currentDragC, prevWindAcc, currentWindAcc) * 0.001;
            } catch (IllegalArgumentException e) {
                onJobComplete(tunnel);
                this.complete = true;
                return;
            }
            updateDragC(tunnel);
            prevWindAcc = currentWindAcc;
            prevDragC = currentDragC;
        }
        @Override
        protected void onJobComplete(EntityWindTunnel tunnel) {
            tunnel.chatToNearbyPlayers(""+dragC, ChatFormatting.BLUE);
            tunnel.clearOverrideValue("lift_coefficient");
            tunnel.clearOverrideValue("drag_coefficient");
        }
        public float getDragC() {
            return (float)dragC;
        }
        private void updateDragC(EntityWindTunnel tunnel) {
            CompoundTag tag = new CompoundTag();
            tag.putDouble("aoa", yaw);
            tag.putDouble("liftC", liftC);
            tunnel.setOverrideValue("lift_coefficient", tag);

            CompoundTag tag2 = new CompoundTag();
            tag2.putDouble("aoa", yaw);
            tag2.putDouble("dragC", dragC);
            tunnel.setOverrideValue("drag_coefficient", tag2);
        }
    }

    public static class FindOptimalLiftC extends WindTunnelJob {
        private final float yaw, roll, pitch, turn_rate;
        private double prevYawRate;
        private double liftC = 0.4, prevLiftC;
        public FindOptimalLiftC(float yaw, float roll, float pitch, float turn_rate) {
            this.yaw = yaw;
            this.roll = roll;
            this.pitch = pitch;
            this.turn_rate = turn_rate;
        }
        @Override
        protected void init(EntityWindTunnel tunnel) {
            tunnel.chatToNearbyPlayers("Searching for optimal Lift Coefficient...", ChatFormatting.YELLOW);
            tunnel.setQ(UtilAngles.toQuaternion(yaw, pitch, roll));
            prevYawRate = -1000;
            prevLiftC = -1000;
            updateLiftC(tunnel);
        }
        @Override
        protected boolean isJobComplete(EntityWindTunnel tunnel) {
            return Math.abs(turn_rate - tunnel.yawRate*20) < 0.001;
        }
        @Override
        protected void run(EntityWindTunnel tunnel) {
            double currentYawRate = (turn_rate - tunnel.yawRate*20) * 1E3;
            double currentLiftC = liftC * 1E3;
            //System.out.println("currentLiftC = "+liftC+" currentYawRate = "+tunnel.yawRate*20);
            if (prevYawRate == -1000) {
                prevYawRate = currentYawRate;
                prevLiftC = currentLiftC;
                liftC += 0.1;
                updateLiftC(tunnel);
                return;
            }
            try {
                liftC = UtilEstimate.nextGuessSecantMethod(prevLiftC, currentLiftC, prevYawRate, currentYawRate) * 0.001;
            } catch (IllegalArgumentException e) {
                onJobComplete(tunnel);
                this.complete = true;
                return;
            }
            updateLiftC(tunnel);
            prevYawRate = currentYawRate;
            prevLiftC = currentLiftC;
        }
        @Override
        protected void onJobComplete(EntityWindTunnel tunnel) {
            tunnel.chatToNearbyPlayers(""+liftC, ChatFormatting.BLUE);
            tunnel.clearOverrideValue("lift_coefficient");
        }
        public float getLiftC() {
            return (float)liftC;
        }
        private void updateLiftC(EntityWindTunnel tunnel) {
            CompoundTag tag = new CompoundTag();
            tag.putDouble("aoa", yaw);
            tag.putDouble("liftC", liftC);
            tunnel.setOverrideValue("lift_coefficient", tag);
        }
    }

    public static class FindOptimalPitchJob extends WindTunnelJob {
        private final float yaw, roll;
        private double pitch = 0, prevPitch = -1000;
        private double prevAccY = -1000;
        public FindOptimalPitchJob(float yaw, float roll) {
            this.yaw = yaw;
            this.roll = roll;
        }
        @Override
        protected void init(EntityWindTunnel tunnel) {
            tunnel.chatToNearbyPlayers("Searching for optimal Pitch...", ChatFormatting.YELLOW);
            pitch = 0;
            updatePitch(tunnel);
            prevPitch = -1000;
            prevAccY = -1000;
        }
        @Override
        protected boolean isJobComplete(EntityWindTunnel tunnel) {
            return tunnel.totalAcc.y > 0 && tunnel.totalAcc.y < 0.0001;
        }
        @Override
        protected void run(EntityWindTunnel tunnel) {
            double currentAccY = tunnel.totalAcc.y * 1E6;
            double currentPitch = pitch * 1000;
            //System.out.println("currentPitch = "+currentPitch+" currentAccY = "+currentAccY);
            if (prevAccY == -1000) {
                prevPitch = currentPitch;
                prevAccY = currentAccY;
                pitch -= 1;
                updatePitch(tunnel);
                return;
            }
            try {
                pitch = UtilEstimate.nextGuessSecantMethod(prevPitch, currentPitch, prevAccY, currentAccY) * 0.001;
            } catch (IllegalArgumentException e) {
                onJobComplete(tunnel);
                this.complete = true;
                return;
            }
            updatePitch(tunnel);
            prevAccY = currentAccY;
            prevPitch = currentPitch;
        }
        @Override
        protected void onJobComplete(EntityWindTunnel tunnel) {
            tunnel.chatToNearbyPlayers(""+pitch, ChatFormatting.BLUE);
        }
        private void updatePitch(EntityWindTunnel tunnel){
            tunnel.setQ(UtilAngles.toQuaternion(yaw, pitch, roll));
        }
        public float getPitch() {
            return (float)pitch;
        }
    }

    private int age = -1;
    protected boolean complete = false;

    protected abstract void init(EntityWindTunnel tunnel);
    protected abstract boolean isJobComplete(EntityWindTunnel tunnel);
    protected abstract void run(EntityWindTunnel tunnel);
    protected abstract void onJobComplete(EntityWindTunnel tunnel);

    public final void tick(EntityWindTunnel tunnel) {
        if (complete) return;
        ++age;
        if (age == 0) init(tunnel);
        else if (age % getUpdateRate() == 0) {
            if (isJobComplete(tunnel)) {
                onJobComplete(tunnel);
                complete = true;
            } else run(tunnel);
        }
    }

    public int getAge() {
        return age;
    }

    public int getUpdateRate() {
        return 5;
    }
}
