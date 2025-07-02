package com.onewhohears.dscombat.entity.vehicle;

import com.onewhohears.dscombat.util.math.UtilEstimate;
import com.onewhohears.onewholibs.util.math.UtilAngles;

public abstract class WindTunnelJob {

    public static class FindOptimalLiftC extends WindTunnelJob {
        private final float yaw, roll, pitch, turn_rate;
        private double yawRate, prevYawRate;
        private double liftC, prevLiftC;
        public FindOptimalLiftC(float yaw, float roll, float pitch, float turn_rate) {
            this.yaw = yaw;
            this.roll = roll;
            this.pitch = pitch;
            this.turn_rate = turn_rate;
        }
        @Override
        protected void init(EntityWindTunnel tunnel) {
            tunnel.chatToNearbyPlayers("Searching for optimal Lift Coefficient...");
            tunnel.setQ(UtilAngles.toQuaternion(yaw, pitch, roll));
            prevYawRate = -1000;
            prevLiftC = -1000;
            updateLiftC(tunnel);
        }
        @Override
        protected boolean isJobComplete(EntityWindTunnel tunnel) {
            return Math.abs(turn_rate - yawRate) < 0.01;
        }
        @Override
        protected void run(EntityWindTunnel tunnel) {
            double currentYawRate = yawRate * 1E3;
            double currentLiftC = liftC * 1E3;
            //System.out.println("currentPitch = "+currentPitch+" currentAccY = "+currentAccY);
            if (prevYawRate == -1000) {
                prevYawRate = currentYawRate;
                prevLiftC = currentLiftC;
                liftC -= 0.1;
                updateLiftC(tunnel);
                return;
            }
            try {
                liftC = UtilEstimate.nextGuessSecantMethod(prevLiftC, liftC, prevYawRate, currentYawRate) * 0.001;
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
            tunnel.chatToNearbyPlayers("Optimal Lift Coefficient Found: "+liftC);
        }
        public float getLiftC() {
            return (float)liftC;
        }
        private void updateLiftC(EntityWindTunnel tunnel) {

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
            tunnel.chatToNearbyPlayers("Searching for optimal pitch...");
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
            tunnel.chatToNearbyPlayers("Optimal Pitch Found: "+pitch);
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
