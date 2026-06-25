package com.onewhohears.dscombat.client.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.onewhohears.dscombat.command.argument.VehiclePresetArgument;
import com.onewhohears.dscombat.data.vehicle.stats.VehicleStats;
import com.onewhohears.onewholibs.client.model.obj.ObjEntityModels;
import com.onewhohears.onewholibs.client.model.obj.ObjMeshData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class GenerateTrackPathCommand {

    public GenerateTrackPathCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("gentrackpath")
            .requires(s -> s.hasPermission(2))
            .then(Commands.argument("preset", VehiclePresetArgument.vehiclePreset())
                .executes(ctx -> run(ctx.getSource(),
                        VehiclePresetArgument.getVehiclePreset(ctx, "preset"), 0.3f, 0.15f, "wheel|w"))
                .then(Commands.argument("segmentLength", FloatArgumentType.floatArg(0.05f, 2f))
                    .executes(ctx -> run(ctx.getSource(),
                            VehiclePresetArgument.getVehiclePreset(ctx, "preset"),
                            FloatArgumentType.getFloat(ctx, "segmentLength"), 0.15f, "wheel|w"))
                    .then(Commands.argument("padding", FloatArgumentType.floatArg(0f, 2f))
                        .executes(ctx -> run(ctx.getSource(),
                                VehiclePresetArgument.getVehiclePreset(ctx, "preset"),
                                FloatArgumentType.getFloat(ctx, "segmentLength"),
                                FloatArgumentType.getFloat(ctx, "padding"), "wheel|w"))
                        .then(Commands.argument("wheelPattern", com.mojang.brigadier.arguments.StringArgumentType.greedyString())
                            .executes(ctx -> run(ctx.getSource(),
                                    VehiclePresetArgument.getVehiclePreset(ctx, "preset"),
                                    FloatArgumentType.getFloat(ctx, "segmentLength"),
                                    FloatArgumentType.getFloat(ctx, "padding"),
                                    com.mojang.brigadier.arguments.StringArgumentType.getString(ctx, "wheelPattern"))))))));
    }

    private int run(CommandSourceStack src, VehicleStats stats, float segLen, float padding, String wheelPattern) {
        String modelId = stats.getAssetId();
        if (!ObjEntityModels.get().hasModel(modelId)) {
            src.sendFailure(Component.literal("Model not loaded: " + modelId));
            return 0;
        }
        ObjMeshData meshData = ObjEntityModels.get().getUnbakedModel(modelId).bakeToMeshData();
        List<double[]> leftYZ = new ArrayList<>(), rightYZ = new ArrayList<>();
        List<Double> leftX = new ArrayList<>(), rightX = new ArrayList<>();
        java.util.Set<String> wheelNames = parseWheelPattern(wheelPattern);
        collectWheelVerts(meshData.getComponents(), leftYZ, rightYZ, leftX, rightX, wheelNames, wheelPattern);
        if (leftYZ.isEmpty() && rightYZ.isEmpty()) {
            src.sendFailure(Component.literal("No wheel groups found matching pattern: '" + wheelPattern + "' in model: " + modelId));
            return 0;
        }
        boolean useLeft = leftYZ.size() >= rightYZ.size();
        List<double[]> points = useLeft ? leftYZ : rightYZ;
        double zOffset = (useLeft ? leftX : rightX).stream().mapToDouble(Double::doubleValue).average().orElse(1.5);
        List<double[]> hull = buildTrackPath(points);
        if (hull.size() < 4) {
            src.sendFailure(Component.literal("Track path too small: " + hull.size() + " (need >= 4)"));
            return 0;
        }
        
        List<double[]> padded = new ArrayList<>();
        double[] c = centroid(hull);
        for (double[] p : hull) {
            double dy = p[0]-c[0], dz = p[1]-c[1], len = Math.sqrt(dy*dy+dz*dz);
            padded.add(len < 1e-6 ? p : new double[]{p[0]+dy/len*padding, p[1]+dz/len*padding});
        }
        String json = "\"crawler_tracks\": [\n"
                + buildJson("left_track",  false, segLen, -Math.abs(zOffset), padded) + ",\n"
                + buildJson("right_track", false, segLen,  Math.abs(zOffset), padded) + "\n]";
        
        // Save to file
        try {
            java.nio.file.Path outputPath = java.nio.file.Paths.get(modelId + "_tracks.json");
            java.nio.file.Files.writeString(outputPath, json);
            src.sendSuccess(() -> Component.literal("Track path saved to: " + outputPath.toAbsolutePath()), false);
        } catch (Exception e) {
            src.sendFailure(Component.literal("Failed to save file: " + e.getMessage()));
            return 0;
        }
        return 1;
    }

    private java.util.Set<String> parseWheelPattern(String pattern) {
        // If pattern contains comma, treat as explicit list: "w1,w2,w3"
        if (pattern.contains(",")) {
            java.util.Set<String> names = new java.util.HashSet<>();
            for (String part : pattern.split(",")) {
                names.add(part.trim().toLowerCase());
            }
            return names;
        }
        // Otherwise it's a regex pattern, return null to signal regex mode
        return null;
    }

    private void collectWheelVerts(List<ObjMeshData.Component> comps,
                                   List<double[]> lYZ, List<double[]> rYZ,
                                   List<Double> lX, List<Double> rX,
                                   java.util.Set<String> wheelNames, String regexPattern) {
        for (ObjMeshData.Component comp : comps) {
            String name = comp.name.toLowerCase();
            boolean isWheel;
            if (wheelNames != null) {
                // Explicit list mode: check if name is in the set
                isWheel = wheelNames.contains(name);
            } else {
                // Regex mode: match against pattern
                isWheel = name.matches(".*(" + regexPattern + ").*");
            }
            if (isWheel) {
                for (ObjMeshData.Mesh mesh : comp.meshes) {
                    int s = ObjMeshData.FLOATS_PER_VERTEX;
                    for (int i = 0; i < mesh.vertices.length; i += s) {
                        double x = mesh.vertices[i], y = mesh.vertices[i+1], z = mesh.vertices[i+2];
                        if (x < -0.05) { lYZ.add(new double[]{y,z}); lX.add(Math.abs(x)); }
                        else if (x > 0.05) { rYZ.add(new double[]{y,z}); rX.add(x); }
                    }
                }
            }
            collectWheelVerts(comp.children, lYZ, rYZ, lX, rX, wheelNames, regexPattern);
        }
    }

    private String buildJson(String name, boolean rev, float seg, double z, List<double[]> hull) {
        StringBuilder sb = new StringBuilder();
        sb.append("  {\n    \"name\": \"").append(name).append("\",\n");
        sb.append("    \"reverse\": ").append(rev).append(",\n");
        sb.append(String.format(java.util.Locale.US, "    \"segment_length\": %.2f,\n", seg));
        sb.append(String.format(java.util.Locale.US, "    \"z_offset\": %.3f,\n", z));
        sb.append("    \"control_points\": [\n");
        
        for (int i = 0; i < hull.size(); i++) {
            double[] p = hull.get(i);
            sb.append(String.format(java.util.Locale.US, "      \"%.3f/%.3f\"", p[0], p[1]));
            if (i < hull.size()-1) sb.append(",");
            sb.append("\n");
        }
        return sb.append("    ]\n  }").toString();
    }

    private List<double[]> buildTrackPath(List<double[]> pts) {
        if (pts.size() < 3) return new ArrayList<>(pts);
        
        // Find bounding box
        double minY = Double.MAX_VALUE, maxY = -Double.MAX_VALUE;
        double minZ = Double.MAX_VALUE, maxZ = -Double.MAX_VALUE;
        for (double[] p : pts) {
            minY = Math.min(minY, p[0]);
            maxY = Math.max(maxY, p[0]);
            minZ = Math.min(minZ, p[1]);
            maxZ = Math.max(maxZ, p[1]);
        }
        
        // Build track path using convex hull, then sort by position
        List<double[]> hull = convexHull(pts);
        
        // Sort hull points to form proper track loop: 
        // Start from bottom-front, go to bottom-back, then top-back, then top-front
        double centerY = (minY + maxY) / 2.0;
        double centerZ = (minZ + maxZ) / 2.0;
        
        List<double[]> bottom = new ArrayList<>();
        List<double[]> top = new ArrayList<>();
        
        for (double[] p : hull) {
            if (p[0] < centerY) {
                bottom.add(p);
            } else {
                top.add(p);
            }
        }
        
        // Sort bottom points front to back (increasing Z)
        bottom.sort((a, b) -> Double.compare(a[1], b[1]));
        // Sort top points back to front (decreasing Z)
        top.sort((a, b) -> Double.compare(b[1], a[1]));
        
        List<double[]> path = new ArrayList<>();
        path.addAll(bottom);
        path.addAll(top);
        
        return path.isEmpty() ? hull : path;
    }
    
    private List<double[]> convexHull(List<double[]> pts) {
        if (pts.size() < 3) return new ArrayList<>(pts);
        // Find bottom-most point (lowest Y, then lowest Z)
        double[] piv = pts.get(0);
        for (double[] p : pts) {
            if (p[0] < piv[0] || (Math.abs(p[0]-piv[0]) < 1e-6 && p[1] < piv[1])) {
                piv = p;
            }
        }
        final double[] fp = piv;
        
        // Sort by polar angle from pivot
        List<double[]> s = new ArrayList<>(pts);
        s.sort((a, b) -> {
            double da = Math.atan2(a[1]-fp[1], a[0]-fp[0]);
            double db = Math.atan2(b[1]-fp[1], b[0]-fp[0]);
            if (Math.abs(da-db) > 1e-9) return Double.compare(da, db);
            return Double.compare(d2(a,fp), d2(b,fp));
        });
        
        // Graham scan
        List<double[]> h = new ArrayList<>();
        for (double[] p : s) {
            while (h.size() >= 2 && cross(h.get(h.size()-2), h.get(h.size()-1), p) <= 1e-9)
                h.remove(h.size()-1);
            h.add(p);
        }
        return h;
    }

    private double cross(double[] O, double[] A, double[] B) {
        return (A[0]-O[0])*(B[1]-O[1]) - (A[1]-O[1])*(B[0]-O[0]);
    }

    private double d2(double[] a, double[] b) {
        double dy = a[0]-b[0], dz = a[1]-b[1];
        return dy*dy + dz*dz;
    }

    private double[] centroid(List<double[]> pts) {
        double sy = 0, sz = 0;
        for (double[] p : pts) { sy += p[0]; sz += p[1]; }
        return new double[]{sy/pts.size(), sz/pts.size()};
    }
}
