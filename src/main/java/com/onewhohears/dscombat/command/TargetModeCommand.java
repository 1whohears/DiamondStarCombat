package com.onewhohears.dscombat.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.onewhohears.dscombat.data.weapon.WeaponSystem;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public class TargetModeCommand {

    public TargetModeCommand(CommandDispatcher<CommandSourceStack> d) {
        d.register(Commands.literal("targetmode").requires((stack) -> { return stack.hasPermission(0);})
                .then(Commands.literal("look")
                        .executes((context) -> setTargetMode(context, WeaponSystem.TargetMode.LOOK, null)))
                .then(Commands.literal("coords")
                        .then(Commands.argument("pos", Vec3Argument.vec3())
                                .executes((context) -> setTargetMode(context,
                                        WeaponSystem.TargetMode.COORDS, Vec3Argument.getVec3(context, "pos")))))
                /*.then(Commands.literal("indicator"))*/);
    }

    private int setTargetMode(CommandContext<CommandSourceStack> context, WeaponSystem.TargetMode targetMode, Vec3 pos) {
        ServerPlayer player = context.getSource().getPlayer();
        if (player == null) {
            context.getSource().sendFailure(UtilMCText.translatable("error.dscombat.command_players_only"));
            return 0;
        }
        if (!(player.getRootVehicle() instanceof EntityVehicle vehicle)) {
            context.getSource().sendFailure(UtilMCText.translatable("error.dscombat.command_must_ride_vehicle"));
            return 0;
        }
        vehicle.weaponSystem.setTargetMode(targetMode);
        if (targetMode == WeaponSystem.TargetMode.COORDS) vehicle.weaponSystem.setTargetPos(pos);
        return 1;
    }

}
