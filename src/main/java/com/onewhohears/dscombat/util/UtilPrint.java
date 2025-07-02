package com.onewhohears.dscombat.util;

import net.minecraft.world.phys.Vec3;

public class UtilPrint {

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
}
