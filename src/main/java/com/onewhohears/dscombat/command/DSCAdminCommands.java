package com.onewhohears.dscombat.command;

import com.mojang.brigadier.CommandDispatcher;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class DSCAdminCommands {

    public DSCAdminCommands(CommandDispatcher<CommandSourceStack> d) {
        d.register(Commands.literal("dsc_vehicle_to_item").requires((stack) -> stack.hasPermission(2))
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayer();
                    if (player == null) {
                        context.getSource().sendFailure(UtilMCText.literal("Command must be used by player in a dsc vehicle!"));
                        return 0;
                    }
                    if (!(player.getRootVehicle() instanceof EntityVehicle vehicle)) {
                        context.getSource().sendFailure(UtilMCText.literal("Command must be used by player in a dsc vehicle!"));
                        return 0;
                    }
                    vehicle.becomeItem(player);
                    return 1;
                }));
    }
}
