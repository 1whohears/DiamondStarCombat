package com.onewhohears.dscombat.client.overlay;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dev.architectury.platform.Platform;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Persist and retrieve user-customized HUD element layout (positions/sizes), per vehicle type.
 * Coordinates are normalized (0..1) relative to GUI-scaled screen size AFTER the Modern HUD scale is applied.
 */
public final class HudLayoutManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = Platform.getConfigFolder().resolve("dscombat_hud_layout.json");

    private static Map<String, Map<String, Rect>> layouts = new HashMap<>(); // vehicleType -> (elementId -> Rect)
    private static boolean loaded = false;

    private HudLayoutManager() {}

    public static class Rect {
        public double x; // normalized top-left x (0..1)
        public double y; // normalized top-left y (0..1)
        public double w; // normalized width (0..1)
        public double h; // normalized height (0..1)
        public Rect() {}
        public Rect(double x, double y, double w, double h) { this.x=x; this.y=y; this.w=w; this.h=h; }
    }

    public static void load() {
        if (loaded) return;
        loaded = true;
        if (!Files.exists(FILE)) return;
        try (BufferedReader r = Files.newBufferedReader(FILE, StandardCharsets.UTF_8)) {
            Type t = new TypeToken<Map<String, Map<String, Rect>>>(){}.getType();
            Map<String, Map<String, Rect>> data = GSON.fromJson(r, t);
            if (data != null) layouts = data;
        } catch (IOException ignored) {}
    }

    public static void save() {
        try (BufferedWriter w = Files.newBufferedWriter(FILE, StandardCharsets.UTF_8)) {
            GSON.toJson(layouts, w);
        } catch (IOException ignored) {}
    }

    private static Map<String, Rect> vehicleMap(String vt) {
        load();
        return layouts.computeIfAbsent(vt, k -> new HashMap<>());
    }

    public static Rect get(String vt, String id, Rect fallback) {
        Map<String, Rect> m = vehicleMap(vt);
        return m.getOrDefault(id, fallback);
    }

    public static void set(String vt, String id, Rect rect) {
        vehicleMap(vt).put(id, rect);
    }
}
