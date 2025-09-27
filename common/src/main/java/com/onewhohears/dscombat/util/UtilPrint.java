package com.onewhohears.dscombat.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class UtilPrint {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static String printVec3SigFig(Vec3 vec) {
        return printVec3SigFig(vec, 3);
    }

    public static String printVec3SigFig(Vec3 vec, int s) {
        String format = "[%."+s+"e,%."+s+"e,%."+s+"e]";
        return String.format(format, vec.x, vec.y, vec.z);
    }

    public static String printSigFig(double d) {
        return printSigFig(d, 3);
    }

    public static String printSigFig(double d, int s) {
        String format = "%."+s+"e";
        return String.format(format, d);
    }

    public static String printDec(double d) {
        return printDec(d, 2);
    }

    public static String printDec(double d, int s) {
        String format = "%."+s+"f";
        return String.format(format, d);
    }

    public static String getClientDirectory() {
        File dir = Minecraft.getInstance().gameDirectory;
        String p;
        if (dir.getPath().equals(".")) p = dir.getAbsolutePath().substring(0, dir.getAbsolutePath().length()-2);
        else p = dir.getAbsolutePath();
        return p;
    }

    public static void printJsonClientDirectory(String path, JsonObject json) {
        String p = getClientDirectory() + "/" + path;
        printJsonAbsoluteDirectory(p, json);
    }

    public static void printJsonAbsoluteDirectory(String path, JsonObject json) {
        int remove = path.lastIndexOf("/");
        new File(path.substring(0, remove+1)).mkdirs();
        try {
            Writer writer = new FileWriter(path);
            GSON.toJson(json, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonObject readJsonClientDirectory(String path) {
        String p = getClientDirectory() + "/" + path;
        return readJsonAbsoluteDirectory(p);
    }

    public static JsonObject readJsonAbsoluteDirectory(String path) {
        Path p = Path.of(path);
        try (Reader reader = Files.newBufferedReader(p)) {
            return GSON.fromJson(reader, JsonObject.class).getAsJsonObject();
        } catch (IOException e) {
            return new JsonObject();
        }
    }
}
