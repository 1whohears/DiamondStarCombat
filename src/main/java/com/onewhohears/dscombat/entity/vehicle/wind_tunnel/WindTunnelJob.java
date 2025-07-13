package com.onewhohears.dscombat.entity.vehicle.wind_tunnel;

import com.google.gson.*;
import com.onewhohears.dscombat.util.math.UtilEstimate;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public abstract class WindTunnelJob {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static class MultiLiftDragJob extends JobArray {
        public MultiLiftDragJob(JsonArray params) {
            super(createJobs(params));
        }
        static JobGen[] createJobs(JsonArray params) {
            JobGen[] gens = new JobGen[params.size()];
            for (int i = 0; i < params.size(); ++i) {
                JsonObject param = params.get(i).getAsJsonObject();
                float speed = param.get("speed").getAsFloat();
                float aoa = param.get("aoa").getAsFloat();
                float turn_rate = param.get("turn_rate").getAsFloat();
                gens[i] = (tunnel, previous_job) -> new FindLiftDragJob(aoa, turn_rate) {
                    @Override
                    protected void init(EntityWindTunnel tunnel) {
                        super.init(tunnel);
                        tunnel.setSpeed(new Vec3(0, 0, speed));
                    }
                };
            }
            return gens;
        }
        @Override
        protected void onJobComplete(EntityWindTunnel tunnel) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
            String time = now.format(formatter);

            String dir = getJobDataPath(tunnel);
            new File(dir).mkdirs();

            String path = dir+"liftdragaoa_"+tunnel.getStatsId()+"_"+time+"_";
            String lift_path = path + "lift.json";
            String drag_path = path + "drag.json";

            JsonObject lift_data = new JsonObject();
            JsonObject drag_data = new JsonObject();
            JsonArray lift_map = new JsonArray();
            JsonArray drag_map = new JsonArray();

            for (WindTunnelJob j : jobs) {
                FindLiftDragJob job = (FindLiftDragJob) j;
                JsonObject l = new JsonObject();
                l.addProperty("key", job.getAoa());
                l.addProperty("value", job.getOptimalLift());
                WindTunnelJob.addToArraySorted(lift_map, l);
                JsonObject d = new JsonObject();
                d.addProperty("key", job.getAoa());
                d.addProperty("value", job.getOptimalDrag());
                WindTunnelJob.addToArraySorted(drag_map, d);
            }
            lift_data.add("map", lift_map);
            drag_data.add("map", drag_map);

            try {
                Writer lift_writer = new FileWriter(lift_path);
                GSON.toJson(lift_data, lift_writer);
                lift_writer.flush();
                lift_writer.close();
                Writer drag_writer = new FileWriter(drag_path);
                GSON.toJson(drag_data, drag_writer);
                drag_writer.flush();
                drag_writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            tunnel.chatToNearbyPlayers("Completed Job! Outputting data to "+lift_path+" and "+drag_path, ChatFormatting.LIGHT_PURPLE);
        }
    }

    private static void addToArraySorted(JsonArray a, JsonObject o) {
        if (a.isEmpty()) {
            a.add(o);
            return;
        }
        float key = o.get("key").getAsFloat();
        float firstKey = a.get(0).getAsJsonObject().get("key").getAsFloat();
        if (key < firstKey) addToStart(a, o);
        else a.add(o);
    }

    public static void addToStart(JsonArray a, JsonElement e) {
        JsonArray temp = a.deepCopy();
        while (!a.isEmpty()) a.remove(0);
        a.add(e);
        a.addAll(temp);
    }

    public static String getJobDataPath(EntityWindTunnel tunnel) {
        File dir;
        if (tunnel.getLevel().isClientSide()) dir = Minecraft.getInstance().gameDirectory;
        else dir = tunnel.getLevel().getServer().getServerDirectory();
        String path;
        if (dir.getPath().equals(".")) path = dir.getAbsolutePath().substring(0, dir.getAbsolutePath().length()-2);
        else path = dir.getAbsolutePath();
        return path + "/dscombat/wind_tunnel_results/";
    }

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
        private final float aoa, turn_rate;
        private double optimal_pitch, optimal_lift, optimal_drag;
        public FindLiftDragJob(float aoa, float turn_rate) {
            super(((tunnel, previous_job) ->
                            new FindOptimalPitchJob(aoa, 90)),
                    ((tunnel, previous_job) ->
                            new FindOptimalLiftC(aoa, 90,
                                    ((FindOptimalPitchJob)previous_job).getPitch(), turn_rate)),
                    ((tunnel, previous_job) ->
                            new FindOptimalDragC(aoa, 90,
                                    ((FindOptimalLiftC)previous_job).getPitch(),
                                    ((FindOptimalLiftC)previous_job).getLiftC())));
            this.aoa = aoa;
            this.turn_rate = turn_rate;
        }
        @Override
        protected void onJobComplete(EntityWindTunnel tunnel) {
            if (jobs.size() < 3) {
                tunnel.chatToNearbyPlayers("FindLiftDragJob Error | Job Index "+getJobIndex()+
                        " | Yaw "+ aoa +" | Turn Rate "+turn_rate, ChatFormatting.RED);
                return;
            }
            optimal_pitch = ((FindOptimalPitchJob)jobs.get(0)).getPitch();
            optimal_lift = ((FindOptimalLiftC)jobs.get(1)).getLiftC();
            optimal_drag = ((FindOptimalDragC)jobs.get(2)).getDragC();
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
        public float getAoa() {
            return aoa;
        }
        public float getTurnRate() {
            return turn_rate;
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
            return tunnel.windCompAcc > 0 && tunnel.windCompAcc < 0.00001;
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
