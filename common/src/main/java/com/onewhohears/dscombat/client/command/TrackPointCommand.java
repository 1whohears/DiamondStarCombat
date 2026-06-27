package com.onewhohears.dscombat.client.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.onewhohears.dscombat.client.renderer.CrawlerTrackRenderer;
import com.onewhohears.dscombat.client.trackeditor.TrackEditSession;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

/**
 * Command for editing track points
 * Usage: /trackpoint <subcommand> [args]
 */
public class TrackPointCommand {
    
    public TrackPointCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("trackpoint")
            .requires(s -> s.hasPermission(2))
            
            // /trackpoint start
            .then(Commands.literal("start")
                .executes(ctx -> {
                    var session = TrackEditSession.getInstance();
                    if (session.isEditing()) {
                        ctx.getSource().sendFailure(Component.literal("Already editing! Use /trackpoint stop first."));
                        return 0;
                    }
                    
                    EntityVehicle vehicle = getPlayerVehicle();
                    if (vehicle == null) {
                        ctx.getSource().sendFailure(Component.literal("You must be riding a tank!"));
                        return 0;
                    }
                    
                    if (session.startEditing(vehicle)) {
                        ctx.getSource().sendSuccess(() -> Component.literal(
                            "§aStarted editing tracks for " + vehicle.getStats().getId() + "\n" +
                            "§7Loaded " + session.getTrackCount() + " tracks\n" +
                            "§7Use /trackpoint list <track> to see points"), false);
                        return 1;
                    }
                    
                    ctx.getSource().sendFailure(Component.literal("Failed to start editing"));
                    return 0;
                }))
            
            // /trackpoint stop
            .then(Commands.literal("stop")
                .executes(ctx -> {
                    var session = TrackEditSession.getInstance();
                    if (!session.isEditing()) {
                        ctx.getSource().sendFailure(Component.literal("Not editing!"));
                        return 0;
                    }
                    
                    if (session.isModified()) {
                        ctx.getSource().sendFailure(Component.literal("§eYou have unsaved changes! Use /trackpoint save first or /trackpoint discard"));
                        return 0;
                    }
                    
                    session.stopEditing();
                    ctx.getSource().sendSuccess(() -> Component.literal("§aStopped editing"), false);
                    return 1;
                }))
            
            // /trackpoint discard
            .then(Commands.literal("discard")
                .executes(ctx -> {
                    var session = TrackEditSession.getInstance();
                    if (!session.isEditing()) {
                        ctx.getSource().sendFailure(Component.literal("Not editing!"));
                        return 0;
                    }
                    
                    session.stopEditing();
                    ctx.getSource().sendSuccess(() -> Component.literal("§eDiscarded changes and stopped editing"), false);
                    return 1;
                }))
            
            // /trackpoint list <track>
            .then(Commands.literal("list")
                .then(Commands.argument("track", IntegerArgumentType.integer(0))
                    .executes(ctx -> {
                        var session = TrackEditSession.getInstance();
                        if (!session.isEditing()) {
                            ctx.getSource().sendFailure(Component.literal("Not editing! Use /trackpoint start first"));
                            return 0;
                        }
                        
                        int trackIndex = IntegerArgumentType.getInteger(ctx, "track");
                        var track = session.getTrack(trackIndex);
                        if (track == null) {
                            ctx.getSource().sendFailure(Component.literal("Invalid track index! Use 0-" + (session.getTrackCount() - 1)));
                            return 0;
                        }
                        
                        StringBuilder sb = new StringBuilder();
                        sb.append("§eTrack ").append(trackIndex).append(" (").append(track.name).append("): ")
                          .append(track.points.size()).append(" points\n");
                        sb.append(String.format("§7z_offset: %.3f\n", track.zOffset));
                        
                        for (int i = 0; i < track.points.size(); i++) {
                            var p = track.points.get(i);
                            sb.append(String.format("§7[%d] x=%.3f, y=%.3f\n", i, p.x, p.y));
                        }
                        
                        ctx.getSource().sendSuccess(() -> Component.literal(sb.toString()), false);
                        return 1;
                    })))
            
            // /trackpoint add <track> <x> <y>
            .then(Commands.literal("add")
                .then(Commands.argument("track", IntegerArgumentType.integer(0))
                    .then(Commands.argument("x", DoubleArgumentType.doubleArg())
                        .then(Commands.argument("y", DoubleArgumentType.doubleArg())
                            .executes(ctx -> {
                                var session = TrackEditSession.getInstance();
                                if (!session.isEditing()) {
                                    ctx.getSource().sendFailure(Component.literal("Not editing! Use /trackpoint start first"));
                                    return 0;
                                }
                                
                                int track = IntegerArgumentType.getInteger(ctx, "track");
                                double x = DoubleArgumentType.getDouble(ctx, "x");
                                double y = DoubleArgumentType.getDouble(ctx, "y");
                                
                                session.addPoint(track, x, y);
                                ctx.getSource().sendSuccess(() -> Component.literal(
                                    String.format("§aAdded point to track %d: (%.3f, %.3f)", track, x, y)), false);
                                return 1;
                            })))))
            
            // /trackpoint insert <track> <index> <x> <y>
            .then(Commands.literal("insert")
                .then(Commands.argument("track", IntegerArgumentType.integer(0))
                    .then(Commands.argument("index", IntegerArgumentType.integer(0))
                        .then(Commands.argument("x", DoubleArgumentType.doubleArg())
                            .then(Commands.argument("y", DoubleArgumentType.doubleArg())
                                .executes(ctx -> {
                                    var session = TrackEditSession.getInstance();
                                    if (!session.isEditing()) {
                                        ctx.getSource().sendFailure(Component.literal("Not editing! Use /trackpoint start first"));
                                        return 0;
                                    }
                                    
                                    int track = IntegerArgumentType.getInteger(ctx, "track");
                                    int index = IntegerArgumentType.getInteger(ctx, "index");
                                    double x = DoubleArgumentType.getDouble(ctx, "x");
                                    double y = DoubleArgumentType.getDouble(ctx, "y");
                                    
                                    session.insertPoint(track, index, x, y);
                                    ctx.getSource().sendSuccess(() -> Component.literal(
                                        String.format("§aInserted point at track %d index %d: (%.3f, %.3f)", track, index, x, y)), false);
                                    return 1;
                                }))))))

            
            // /trackpoint move <track> <index> <dx> <dy>
            .then(Commands.literal("move")
                .then(Commands.argument("track", IntegerArgumentType.integer(0))
                    .then(Commands.argument("index", IntegerArgumentType.integer(0))
                        .then(Commands.argument("dx", DoubleArgumentType.doubleArg())
                            .then(Commands.argument("dy", DoubleArgumentType.doubleArg())
                                .executes(ctx -> {
                                    var session = TrackEditSession.getInstance();
                                    if (!session.isEditing()) {
                                        ctx.getSource().sendFailure(Component.literal("Not editing! Use /trackpoint start first"));
                                        return 0;
                                    }
                                    
                                    int track = IntegerArgumentType.getInteger(ctx, "track");
                                    int index = IntegerArgumentType.getInteger(ctx, "index");
                                    double dx = DoubleArgumentType.getDouble(ctx, "dx");
                                    double dy = DoubleArgumentType.getDouble(ctx, "dy");
                                    
                                    session.movePoint(track, index, dx, dy);
                                    ctx.getSource().sendSuccess(() -> Component.literal(
                                        String.format("§aMoved point track %d index %d by (%.3f, %.3f)", track, index, dx, dy)), false);
                                    return 1;
                                }))))))
            
            // /trackpoint delete <track> <index>
            .then(Commands.literal("delete")
                .then(Commands.argument("track", IntegerArgumentType.integer(0))
                    .then(Commands.argument("index", IntegerArgumentType.integer(0))
                        .executes(ctx -> {
                            var session = TrackEditSession.getInstance();
                            if (!session.isEditing()) {
                                ctx.getSource().sendFailure(Component.literal("Not editing! Use /trackpoint start first"));
                                return 0;
                            }
                            
                            int track = IntegerArgumentType.getInteger(ctx, "track");
                            int index = IntegerArgumentType.getInteger(ctx, "index");
                            
                            session.deletePoint(track, index);
                            ctx.getSource().sendSuccess(() -> Component.literal(
                                String.format("§aDeleted point track %d index %d", track, index)), false);
                            return 1;
                        }))))
            
            // /trackpoint scale <track> <scaleX> <scaleY>
            .then(Commands.literal("scale")
                .then(Commands.argument("track", IntegerArgumentType.integer(0))
                    .then(Commands.argument("scaleX", DoubleArgumentType.doubleArg())
                        .then(Commands.argument("scaleY", DoubleArgumentType.doubleArg())
                            .executes(ctx -> {
                                var session = TrackEditSession.getInstance();
                                if (!session.isEditing()) {
                                    ctx.getSource().sendFailure(Component.literal("Not editing! Use /trackpoint start first"));
                                    return 0;
                                }
                                
                                int track = IntegerArgumentType.getInteger(ctx, "track");
                                double scaleX = DoubleArgumentType.getDouble(ctx, "scaleX");
                                double scaleY = DoubleArgumentType.getDouble(ctx, "scaleY");
                                
                                session.scaleTrack(track, scaleX, scaleY);
                                ctx.getSource().sendSuccess(() -> Component.literal(
                                    String.format("§aScaled track %d by (%.3f, %.3f)", track, scaleX, scaleY)), false);
                                return 1;
                            })))))
            
            // /trackpoint shift <track> <dx> <dy>
            .then(Commands.literal("shift")
                .then(Commands.argument("track", IntegerArgumentType.integer(0))
                    .then(Commands.argument("dx", DoubleArgumentType.doubleArg())
                        .then(Commands.argument("dy", DoubleArgumentType.doubleArg())
                            .executes(ctx -> {
                                var session = TrackEditSession.getInstance();
                                if (!session.isEditing()) {
                                    ctx.getSource().sendFailure(Component.literal("Not editing! Use /trackpoint start first"));
                                    return 0;
                                }
                                
                                int track = IntegerArgumentType.getInteger(ctx, "track");
                                double dx = DoubleArgumentType.getDouble(ctx, "dx");
                                double dy = DoubleArgumentType.getDouble(ctx, "dy");
                                
                                session.shiftTrack(track, dx, dy);
                                ctx.getSource().sendSuccess(() -> Component.literal(
                                    String.format("§aShifted track %d by (%.3f, %.3f)", track, dx, dy)), false);
                                return 1;
                            })))))
            
            // /trackpoint mirror <fromTrack> <toTrack>
            .then(Commands.literal("mirror")
                .then(Commands.argument("fromTrack", IntegerArgumentType.integer(0))
                    .then(Commands.argument("toTrack", IntegerArgumentType.integer(0))
                        .executes(ctx -> {
                            var session = TrackEditSession.getInstance();
                            if (!session.isEditing()) {
                                ctx.getSource().sendFailure(Component.literal("Not editing! Use /trackpoint start first"));
                                return 0;
                            }
                            
                            int from = IntegerArgumentType.getInteger(ctx, "fromTrack");
                            int to = IntegerArgumentType.getInteger(ctx, "toTrack");
                            
                            session.mirrorTrack(from, to);
                            ctx.getSource().sendSuccess(() -> Component.literal(
                                String.format("§aMirrored track %d to track %d", from, to)), false);
                            return 1;
                        }))))
            
            // /trackpoint debug [true|false]  — toggle control-point labels
            .then(Commands.literal("debug")
                .then(Commands.argument("enabled", BoolArgumentType.bool())
                    .executes(ctx -> {
                        boolean enabled = BoolArgumentType.getBool(ctx, "enabled");
                        CrawlerTrackRenderer.setDebugRender(enabled);
                        ctx.getSource().sendSuccess(
                            () -> Component.literal("Track point debug " + (enabled ? "§aenabled" : "§cdisabled")), false);
                        return 1;
                    }))
                .executes(ctx -> {
                    boolean newState = !CrawlerTrackRenderer.isDebugRender();
                    CrawlerTrackRenderer.setDebugRender(newState);
                    ctx.getSource().sendSuccess(
                        () -> Component.literal("Track point debug " + (newState ? "§aenabled" : "§cdisabled")), false);
                    return 1;
                }))

            // /trackpoint save
            .then(Commands.literal("save")
                .executes(ctx -> {
                    var session = TrackEditSession.getInstance();
                    if (!session.isEditing()) {
                        ctx.getSource().sendFailure(Component.literal("Not editing! Use /trackpoint start first"));
                        return 0;
                    }
                    
                    if (!session.isModified()) {
                        ctx.getSource().sendFailure(Component.literal("No changes to save"));
                        return 0;
                    }
                    
                    if (session.saveToFile()) {
                        return 1;
                    }
                    
                    return 0;
                }))
        );
    }
    
    private static EntityVehicle getPlayerVehicle() {
        var mc = Minecraft.getInstance();
        if (mc.player == null) return null;
        
        Entity vehicle = mc.player.getVehicle();
        if (vehicle instanceof EntityVehicle) {
            return (EntityVehicle) vehicle;
        }
        
        Entity rootVehicle = mc.player.getRootVehicle();
        if (rootVehicle instanceof EntityVehicle) {
            return (EntityVehicle) rootVehicle;
        }
        
        return null;
    }
}
