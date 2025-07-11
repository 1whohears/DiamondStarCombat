package com.onewhohears.dscombat.entity.vehicle.wind_tunnel;

import com.onewhohears.dscombat.util.math.UtilEstimate;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class WindTunnelJob {

    public interface JobGen {
        @NotNull WindTunnelJob create(EntityWindTunnel tunnel, WindTunnelJob previous_job);
    }

    public static abstract class JobArray extends WindTunnelJob {
        private final JobGen[] job_gens;
        protected final List<WindTunnelJob> jobs = new ArrayList<>();
        private final int job_num;
        @Nullable private WindTunnelJob current_job;
        private int job_index = 0;
        public JobArray(JobGen... job_gens) {
            this.job_gens = job_gens;
            this.job_num = this.job_gens.length;
        }
        @Override
        protected void init(EntityWindTunnel tunnel) {
            if (job_gens.length == 0) {
                finishEarly(tunnel);
                return;
            }
            current_job = job_gens[0].create(tunnel, null);
            jobs.add(current_job);
        }
        @Override
        protected boolean isJobComplete(EntityWindTunnel tunnel) {
            return current_job == null || (job_index == job_num - 1 && current_job.complete);
        }
        @Override
        protected void run(EntityWindTunnel tunnel) {
            if (current_job == null) {
                finishEarly(tunnel);
                return;
            }
            if (current_job.complete) {
                ++job_index;
                current_job = job_gens[job_index].create(tunnel, current_job);
                jobs.add(current_job);
            }
            current_job.tick(tunnel);
        }
        @Override
        public int getUpdateRate() {
            return 1;
        }
        public int getJobIndex() {
            return job_index;
        }
    }

    public static class FindLiftDragJob extends JobArray {
        private final float yaw, turn_rate;
        private double optimal_pitch, optimal_lift, optimal_drag;
        public FindLiftDragJob(float yaw, float turn_rate) {
            super(((tunnel, previous_job) ->
                            new FindOptimalPitchJob(yaw, 90)),
                    ((tunnel, previous_job) ->
                            new FindOptimalLiftC(yaw, 90,
                                    ((FindOptimalPitchJob)previous_job).getPitch(), turn_rate)),
                    ((tunnel, previous_job) ->
                            new FindOptimalDragC(yaw, 90,
                                    ((FindOptimalLiftC)previous_job).getPitch(),
                                    ((FindOptimalLiftC)previous_job).getLiftC())));
            this.yaw = yaw;
            this.turn_rate = turn_rate;
        }
        @Override
        protected void onJobComplete(EntityWindTunnel tunnel) {
            if (jobs.size() < 3) {
                tunnel.chatToNearbyPlayers("FindLiftDragJob Error | Job Index "+getJobIndex()+
                        " | Yaw "+yaw+" | Turn Rate "+turn_rate, ChatFormatting.RED);
            }
        }
        public double getOptimalDrag() {
            return optimal_drag;
        }
        public double getOptimalLift() {
            return optimal_lift;
        }
        public double getOptimalPitch() {
            return optimal_pitch;
        }
    }

    public static class FindOptimalDragC extends WindTunnelJob {
        private final float yaw, roll, pitch, liftC;
        private double prevWindAcc;
        private double dragC, prevDragC;
        private double first_guess_delta = 0.01, first_guess = 0.01;
        int attempts = 0;
        private double bestGuessDragC, bestGuessWindAcc = 1E6;
        public FindOptimalDragC(float yaw, float roll, float pitch, float liftC) {
            this.yaw = yaw;
            this.roll = roll;
            this.pitch = pitch;
            this.liftC = liftC;
        }
        @Override
        protected void init(EntityWindTunnel tunnel) {
            if (attempts == 0)
                tunnel.chatToNearbyPlayers("Searching for optimal Drag Coefficient...", ChatFormatting.YELLOW);
            else if (attempts == 1)
                tunnel.chatToNearbyPlayers("First attempt failed trying again...", ChatFormatting.YELLOW);
            tunnel.setQ(UtilAngles.toQuaternion(yaw, pitch, roll));
            prevWindAcc = -1000;
            prevDragC = -1000;
            dragC = first_guess;
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
                dragC += first_guess_delta;
                updateDragC(tunnel);
                return;
            }
            try {
                dragC = UtilEstimate.nextGuessSecantMethod(prevDragC, currentDragC, prevWindAcc, currentWindAcc) * 0.001;
            } catch (IllegalArgumentException e) {
                if (attempts >= 40) {
                    finishEarly(tunnel);
                    tunnel.chatToNearbyPlayers("Best Guess: "+bestGuessDragC+" | Error: "+bestGuessWindAcc, ChatFormatting.RED);
                    return;
                }
                ++attempts;
                if (Math.abs(currentWindAcc) < Math.abs(bestGuessWindAcc)) {
                    bestGuessWindAcc = currentWindAcc;
                    bestGuessDragC = currentDragC * 0.001;
                }
                if (attempts % 10 == 0 && attempts != 0) {
                    first_guess_delta = Math.ceil(attempts/10d) * 0.01;
                    first_guess += 0.04;
                } else {
                    first_guess_delta += Math.ceil(attempts/10d) * 0.01;
                }
                init(tunnel);
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
                finishEarly(tunnel);
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
        public float getPitch() {
            return pitch;
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
                finishEarly(tunnel);
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
        public float getYaw() {
            return yaw;
        }
    }

    private int age = -1;
    protected boolean complete = false;

    protected abstract void init(EntityWindTunnel tunnel);
    protected abstract boolean isJobComplete(EntityWindTunnel tunnel);
    protected abstract void run(EntityWindTunnel tunnel);
    protected abstract void onJobComplete(EntityWindTunnel tunnel);

    protected void finishEarly(EntityWindTunnel tunnel) {
        complete = true;
        onJobComplete(tunnel);
    }

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
