package com.onewhohears.dscombat.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.onewhohears.dscombat.command.argument.VehiclePresetArgument;
import com.onewhohears.dscombat.data.vehicle.stats.VehicleStats;
import com.onewhohears.dscombat.entity.vehicle.EntityWindTunnel;
import com.onewhohears.onewholibs.util.UtilMCText;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;

import static com.onewhohears.dscombat.util.UtilVehicleEntity.WIND_TUNNEL_SEARCH_RANGE;
import static com.onewhohears.dscombat.util.UtilVehicleEntity.findWindTunnel;

public class WindTunnelCommand {

    public WindTunnelCommand(CommandDispatcher<CommandSourceStack> d) {
        d.register(Commands.literal("windtunnel").requires((stack) -> stack.hasPermission(2))
                .then(Commands.literal("set_all")
                        .then(Commands.argument("preset", VehiclePresetArgument.vehiclePreset())
                                .then(Commands.argument("speed", Vec3Argument.vec3())
                                        .then(Commands.argument("rotation", Vec3Argument.vec3())
                                                .then(Commands.argument("throttle", FloatArgumentType.floatArg(0, 1))
                                                        .then(Commands.argument("afterburner", BoolArgumentType.bool())
                                                                .executes(context -> setWindTunnelCommand(context,
                                                                        VehiclePresetArgument.getVehiclePreset(context, "preset"),
                                                                        Vec3Argument.getVec3(context, "speed"),
                                                                        Vec3Argument.getVec3(context, "rotation"),
                                                                        FloatArgumentType.getFloat(context, "throttle"),
                                                                        BoolArgumentType.getBool(context, "afterburner")))
                                                        )
                                                )
                                        )
                                )
                        )
                )
                .then(Commands.literal("set_hidden")
                        .then(Commands.argument("hidden", BoolArgumentType.bool())
                                .executes(context -> setVehicleHidden(context, BoolArgumentType.getBool(context, "hidden")))
                        )
                )
                .then(Commands.literal("set_altitude")
                        .then(Commands.argument("altitude", FloatArgumentType.floatArg(-1000, 10000))
                                .executes(context -> setVehicleAltitude(context, FloatArgumentType.getFloat(context, "altitude")))
                        )
                )
        );
    }

    private int setWindTunnelCommand(CommandContext<CommandSourceStack> context, @Nullable VehicleStats preset,
                                     @Nullable Vec3 speed, @Nullable Vec3 rotation, float throttle, boolean afterburner) {
        Optional<EntityWindTunnel> opt = getWindTunnel(context);
        if (opt.isEmpty()) {
            context.getSource().sendFailure(UtilMCText.literal("No Wind Tunnels within "
                    +WIND_TUNNEL_SEARCH_RANGE+" blocks found!"));
            return 0;
        }
        EntityWindTunnel tunnel = opt.get();
        if (preset != null) tunnel.setPresetId(preset.getId());
        if (speed != null) tunnel.setSpeed(speed);
        if (rotation != null) tunnel.setQ(UtilAngles.toQuaternion(rotation.y, rotation.x, rotation.z));
        if (throttle >= 0) tunnel.setThrottle(throttle);
        tunnel.setAfterBurner(afterburner);
        return 1;
    }

    private int setVehicleHidden(CommandContext<CommandSourceStack> context, boolean hidden) {
        Optional<EntityWindTunnel> opt = getWindTunnel(context);
        if (opt.isEmpty()) {
            context.getSource().sendFailure(UtilMCText.literal("No Wind Tunnels within "
                    +WIND_TUNNEL_SEARCH_RANGE+" blocks found!"));
            return 0;
        }
        EntityWindTunnel tunnel = opt.get();
        tunnel.setHideModel(hidden);
        return 1;
    }

    private int setVehicleAltitude(CommandContext<CommandSourceStack> context, float altitude) {
        Optional<EntityWindTunnel> opt = getWindTunnel(context);
        if (opt.isEmpty()) {
            context.getSource().sendFailure(UtilMCText.literal("No Wind Tunnels within "
                    +WIND_TUNNEL_SEARCH_RANGE+" blocks found!"));
            return 0;
        }
        EntityWindTunnel tunnel = opt.get();
        tunnel.setAltitude(altitude);
        return 1;
    }

    private Optional<EntityWindTunnel> getWindTunnel(CommandContext<CommandSourceStack> context) {
        return findWindTunnel(context.getSource().getPosition(), context.getSource().getLevel());
    }

}
