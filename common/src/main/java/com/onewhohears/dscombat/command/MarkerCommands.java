package com.onewhohears.dscombat.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.onewhohears.dscombat.common.core.MarkerType;
import com.onewhohears.dscombat.common.core.PlayerPositionMarkers;
import com.onewhohears.dscombat.common.core.PositionMarker;
import com.onewhohears.dscombat.common.core.PositionMarkerManager;
import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public class MarkerCommands {

    public MarkerCommands(CommandDispatcher<CommandSourceStack> d) {
        d.register(Commands.literal("marker")
                .then(Commands.argument("pos", Vec3Argument.vec3())
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayer();
                            if (player == null) {
                                ctx.getSource().sendFailure(UtilMCText.literal("Command must be used by a player!"));
                                return 0;
                            }
                            Vec3 position = Vec3Argument.getVec3(ctx, "pos");
                            PositionMarkerManager.getServer().addTempMarker(player, position);
                            return 1;
                        })
                )
                .then(Commands.literal("look")
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayer();
                            if (player == null) {
                                ctx.getSource().sendFailure(UtilMCText.literal("Command must be used by a player!"));
                                return 0;
                            }
                            PositionMarkerManager.getServer().addQuickTempMarker(player);
                            return 1;
                        })
                )
                .then(Commands.literal("save")
                        .then(Commands.argument("new_name", StringArgumentType.word())
                                .executes(ctx -> {
                                    ServerPlayer player = ctx.getSource().getPlayer();
                                    if (player == null) {
                                        ctx.getSource().sendFailure(UtilMCText.literal("Command must be used by a player!"));
                                        return 0;
                                    }
                                    PlayerPositionMarkers playerData = PositionMarkerManager.getServer().getPlayerData(player.getUUID());
                                    int tempId = playerData.getTempMarkerId();
                                    PositionMarker marker = PositionMarkerManager.getServer().getMarker(tempId);
                                    if (marker == null) {
                                        ctx.getSource().sendFailure(UtilMCText.literal("You have not created a temp marker! Press the Quick Marker key!"));
                                        return 0;
                                    }
                                    String newName = StringArgumentType.getString(ctx, "new_name");
                                    if (PositionMarkerManager.getServer().getMarkerByName(newName, player.getUUID()) != null) {
                                        ctx.getSource().sendFailure(UtilMCText.literal("You cannot make multiple markers with the same name!"));
                                        return 0;
                                    }
                                    marker.saveMarker(newName);
                                    return 1;
                                })
                        )
                )
                .then(Commands.literal("remove")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .executes(ctx -> {
                                    ServerPlayer player = ctx.getSource().getPlayer();
                                    if (player == null) {
                                        ctx.getSource().sendFailure(UtilMCText.literal("Command must be used by a player!"));
                                        return 0;
                                    }
                                    String name = StringArgumentType.getString(ctx, "name");
                                    PositionMarker marker = PositionMarkerManager.getServer().getMarkerByName(name, player.getUUID());
                                    if (marker == null) {
                                        ctx.getSource().sendFailure(UtilMCText.literal("You are not the owner of any marker named "+name));
                                        return 0;
                                    }
                                    PositionMarkerManager.getServer().removeMarker(marker.getId());
                                    return 1;
                                })
                        )
                )
                .then(Commands.literal("remove_admin").requires((stack) -> stack.hasPermission(2))
                        .then(Commands.argument("name", StringArgumentType.word())
                                .executes(ctx -> {
                                    String name = StringArgumentType.getString(ctx, "name");
                                    PositionMarker marker = PositionMarkerManager.getServer().getMarkerByName(name, null);
                                    if (marker == null) {
                                        ctx.getSource().sendFailure(UtilMCText.literal("There are no markers with name "+name));
                                        return 0;
                                    }
                                    PositionMarkerManager.getServer().removeMarker(marker.getId());
                                    return 1;
                                })
                        )
                )
        );
    }

}
