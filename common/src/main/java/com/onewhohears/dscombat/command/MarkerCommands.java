package com.onewhohears.dscombat.command;

import com.mojang.brigadier.CommandDispatcher;
import com.onewhohears.dscombat.common.core.PositionMarkerManager;
import com.onewhohears.onewholibs.util.UtilEntity;
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
        );
    }

}
