package com.onewhohears.dscombat.data.sound;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.vehicle.DSCPhyCons;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.util.UtilClientSafeSounds;
import com.onewhohears.minigames.util.UtilParse;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetStats;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;
import com.onewhohears.onewholibs.util.UtilEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

public class PassengerSoundPack extends JsonPresetStats {

    private static final Map<String, BiPredicate<EntityVehicle, PassengerSound>> soundTriggers = new HashMap<>();

    /**
     * addon mods can register their own triggers by calling this in
     * {@link net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent}
     */
    public static void registerPassengerSoundTrigger(String id, BiPredicate<EntityVehicle, PassengerSound> trigger) {
        soundTriggers.put(id, trigger);
    }

    public static boolean testTime(PassengerSound sound, int ticks) {
        if (sound.getGroupBurstSize() > 0 && sound.getGroupBurstRepeatRate() > -1) {
            int burstLength = sound.getRepeatRate() * sound.getGroupBurstSize();
            return ticks % sound.getGroupBurstRepeatRate() <= burstLength
                    && ticks % sound.getRepeatRate() == 1;
        } else if (sound.getGroupBurstSize() > 0) {
            int burstLength = sound.getRepeatRate() * sound.getGroupBurstSize();
            return ticks <= burstLength && ticks % sound.getRepeatRate() == 1;
        } else {
            return ticks % sound.getRepeatRate() == 1;
        }
    }

    public static void registerBuiltInPassengerSoundTriggers() {
        registerPassengerSoundTrigger("always", (vehicle, sound) -> true);
        registerPassengerSoundTrigger("never", (vehicle, sound) -> false);
        registerPassengerSoundTrigger("rwr_missile_alert", (vehicle, sound) ->
                vehicle.radarSystem.isTrackedByMissile() && testTime(sound, vehicle.tickCount));
        registerPassengerSoundTrigger("rwr_tracked_alert", (vehicle, sound) ->
                !vehicle.radarSystem.isTrackedByMissile() && vehicle.radarSystem.isTrackedByRadar()
                        && testTime(sound, vehicle.tickCount));
        registerPassengerSoundTrigger("rwr_pinged_warning", (vehicle, sound) ->
                !vehicle.radarSystem.isTrackedByMissile() && !vehicle.radarSystem.isTrackedByRadar()
                        && vehicle.radarSystem.clientConsumePingWarningSound());
        registerPassengerSoundTrigger("ir_tone_low", (vehicle, sound) ->
                vehicle.shouldPlayLowIRTone() && !vehicle.shouldPlayHighIRTone()
                        && testTime(sound, vehicle.tickCount));
        registerPassengerSoundTrigger("ir_tone_high", (vehicle, sound) ->
                vehicle.shouldPlayHighIRTone() && testTime(sound, vehicle.tickCount));
        registerPassengerSoundTrigger("stall_alert", (vehicle, sound) ->
                vehicle.getStats().isPlane() && vehicle.isStalling()
                        && testTime(sound, vehicle.getStallTicks()));
        registerPassengerSoundTrigger("stall_warning", (vehicle, sound) ->
                vehicle.getStats().isPlane() && !vehicle.isStalling() && vehicle.isAboutToStall()
                        && testTime(sound, vehicle.getAboutToStallTicks()));
        registerPassengerSoundTrigger("pull_up", (vehicle, sound) ->
                vehicle.getStats().isPlane() && vehicle.getDeltaMovement().y <= -DSCPhyCons.COLLIDE_SPEED
                        && testTime(sound, vehicle.tickCount)
                        && UtilEntity.getDistFromGround(vehicle) / -vehicle.getDeltaMovement().y <= 80);
        registerPassengerSoundTrigger("engine_fire", (vehicle, sound) ->
                vehicle.getStats().isAircraft() && testTime(sound, vehicle.getEngineFireTicks()));
        registerPassengerSoundTrigger("fuel_leak", (vehicle, sound) ->
                vehicle.getStats().isAircraft() && testTime(sound, vehicle.getFuelLeakTicks()));
        registerPassengerSoundTrigger("bingo", (vehicle, sound) ->
                vehicle.getStats().isAircraft() && testTime(sound, vehicle.getBingoTicks()));
        registerPassengerSoundTrigger("radar_lock", (vehicle, sound) -> false);
        // Hydraulics Failure
        // Altitude
        // Flare
        // Chaff
        // New radar target found
        // Jammer warning
    }

    public static final JsonPresetType STANDARD = new JsonPresetType(
            "standard", PassengerSoundPack::new) {};

    private final List<PassengerSound> passengerSounds = new ArrayList<>();
    private int radarLockIndex = -1;

    public PassengerSoundPack(ResourceLocation key, JsonObject json) {
        super(key, json);
        JsonArray sounds;
        if (json.has("sounds") && json.get("sounds").isJsonArray())
            sounds = json.get("sounds").getAsJsonArray();
        else sounds = new JsonArray();
        for (int i = 0; i < sounds.size(); ++i) {
            if (!sounds.get(i).isJsonObject()) continue;
            JsonObject sound = sounds.get(i).getAsJsonObject();
            PassengerSound ps = new PassengerSound(sound);
            passengerSounds.add(ps);
            if (ps.getTriggerId().equals("radar_lock")) radarLockIndex = i;
        }
    }

    public void playRadarLockSound() {
        if (radarLockIndex > -1) passengerSounds.get(radarLockIndex).playSound();
    }

    public void clientTickPassengerSounds(EntityVehicle vehicle) {
        if (!vehicle.isOperational()) return;
        passengerSounds.forEach(sound -> sound.testPlaySound(vehicle));
    }

    public static class PassengerSound {
        @Nullable final SoundEvent sound;
        private final float volume, pitch;
        private final int tick_length, repeat_rate;
        private final int group_burst_size, group_burst_repeat_rate;
        private final boolean skip_queue;
        private final String triggerId;
        private final BiPredicate<EntityVehicle, PassengerSound> shouldPlaySound;
        public PassengerSound(JsonObject json) {
            String soundId = UtilParse.getStringSafe(json, "sound", "");
            if (soundId.isEmpty()) sound = null;
            else sound = new SoundEvent(new ResourceLocation(soundId));
            volume = UtilParse.getFloatSafe(json, "volume", 1);
            pitch = UtilParse.getFloatSafe(json, "pitch", 1);
            repeat_rate = UtilParse.getIntSafe(json, "repeat_rate", 20);
            tick_length = UtilParse.getIntSafe(json, "tick_length", repeat_rate);
            group_burst_size = UtilParse.getIntSafe(json, "group_burst_size", -1);
            group_burst_repeat_rate = UtilParse.getIntSafe(json, "group_burst_repeat_rate", -1);
            skip_queue = UtilParse.getBooleanSafe(json, "skip_queue", false);
            triggerId = UtilParse.getStringSafe(json, "trigger", "never");
            shouldPlaySound = soundTriggers.getOrDefault(triggerId, (vehicle, sound) -> false);
        }
        public boolean shouldPlaySound(EntityVehicle vehicle) {
            return shouldPlaySound.test(vehicle, this);
        }
        public void testPlaySound(EntityVehicle vehicle) {
            if (getSound() != null && shouldPlaySound(vehicle)) playSound();
        }
        public void playSound() {
            if (getSound() == null) return;
            if (skipQueue()) UtilClientSafeSounds.playCockpitSound(getSound(), getPitch(), getVolume());
            else UtilClientSafeSounds.queueCockpitSound(getSound(), getPitch(), getVolume(), getTickLength());
        }
        @Nullable
        public SoundEvent getSound() {
            return sound;
        }
        public float getVolume() {
            return volume;
        }
        public float getPitch() {
            return pitch;
        }
        public int getTickLength() {
            return tick_length;
        }
        public boolean skipQueue() {
            return skip_queue;
        }
        public int getRepeatRate() {
            return repeat_rate;
        }
        public int getGroupBurstSize() {
            return group_burst_size;
        }
        public int getGroupBurstRepeatRate() {
            return group_burst_repeat_rate;
        }
        public String getTriggerId() {
            return triggerId;
        }
    }

    @Override
    public JsonPresetType getType() {
        return STANDARD;
    }

    @Override
    public @Nullable JsonPresetInstance<?> createPresetInstance() {
        return null;
    }
}
