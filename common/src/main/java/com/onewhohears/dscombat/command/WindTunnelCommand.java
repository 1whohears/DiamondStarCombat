package com.onewhohears.dscombat.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.onewhohears.dscombat.command.argument.VehiclePresetArgument;
import com.onewhohears.dscombat.data.vehicle.stats.VehicleStats;
import com.onewhohears.dscombat.entity.vehicle.wind_tunnel.EntityWindTunnel;
import com.onewhohears.onewholibs.util.UtilMCText;
import com.onewhohears.onewholibs.util.UtilParse;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
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
                                                                .executes(((IWindTunnelCommand)(context, tunnel) ->
                                                                        setWindTunnelCommand(context, tunnel,
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
                )
                .then(Commands.literal("set_hidden")
                        .then(Commands.argument("hidden", BoolArgumentType.bool())
                                .executes(((IWindTunnelCommand)(context, tunnel) ->
                                        setVehicleHidden(context, tunnel, BoolArgumentType.getBool(context, "hidden")))
                                )
                        )
                )
                .then(Commands.literal("set_altitude")
                        .then(Commands.argument("altitude", FloatArgumentType.floatArg(-1000, 10000))
                                .executes(((IWindTunnelCommand)(context, tunnel) ->
                                        setVehicleAltitude(context, tunnel, FloatArgumentType.getFloat(context, "altitude")))
                                )
                        )
                )
                .then(Commands.literal("set_inputs")
                        .then(Commands.argument("pitch", FloatArgumentType.floatArg(-1f, 1f))
                                .then(Commands.argument("yaw", FloatArgumentType.floatArg(-1f, 1f))
                                        .then(Commands.argument("roll", FloatArgumentType.floatArg(-1f, 1f))
                                                .executes(((IWindTunnelCommand)(context, tunnel) ->
                                                        setVehicleInputs(context, tunnel,
                                                        FloatArgumentType.getFloat(context, "pitch"),
                                                        FloatArgumentType.getFloat(context, "yaw"),
                                                        FloatArgumentType.getFloat(context, "roll")))
                                                )
                                        )
                                )
                        )
                )
                .then(Commands.literal("find_lift_drag")
                        .then(Commands.argument("speed", FloatArgumentType.floatArg(0, 1000))
                                .then(Commands.argument("turn_rate", FloatArgumentType.floatArg(0, 1000))
                                        .then(Commands.argument("aoa", FloatArgumentType.floatArg(0, 90))
                                                .then(Commands.argument("altitude", FloatArgumentType.floatArg(-64, 10000))
                                                        .executes(((IWindTunnelCommand)(context, tunnel) ->
                                                                findLiftDrag(context, tunnel,
                                                                FloatArgumentType.getFloat(context, "speed"),
                                                                FloatArgumentType.getFloat(context, "turn_rate"),
                                                                FloatArgumentType.getFloat(context, "aoa"),
                                                                FloatArgumentType.getFloat(context, "altitude")))
                                                        )
                                                )
                                        )
                                )
                        )
                )
                .then(Commands.literal("set_preset")
                        .then(Commands.argument("preset", VehiclePresetArgument.vehiclePreset())
                                .executes(((IWindTunnelCommand)(context, tunnel) ->
                                        setPresetCommand(context, tunnel, VehiclePresetArgument.getVehiclePreset(context, "preset")))
                                )
                        )
                )
                .then(Commands.literal("set_speed_list")
                        .then(Commands.argument("speed_list", StringArgumentType.string())
                                .executes(((IWindTunnelCommand)(context, tunnel) ->
                                        setSpeedList(context, tunnel, StringArgumentType.getString(context, "speed_list")))
                                )
                        )
                )
                .then(Commands.literal("set_aoa_list")
                        .then(Commands.argument("aoa_list", StringArgumentType.string())
                                .executes(((IWindTunnelCommand)(context, tunnel) ->
                                        setAOAList(context, tunnel, StringArgumentType.getString(context, "aoa_list")))
                                )
                        )
                )
                .then(Commands.literal("set_turn_rate_list")
                        .then(Commands.argument("turn_rate_list", StringArgumentType.string())
                                .executes(((IWindTunnelCommand)(context, tunnel) ->
                                        setTurnRateList(context, tunnel, StringArgumentType.getString(context, "turn_rate_list")))
                                )
                        )
                )
                .then(Commands.literal("find_multi_lift_drag")
                        .then(Commands.argument("altitude", FloatArgumentType.floatArg(-64, 10000))
                                        .executes(((IWindTunnelCommand)(context, tunnel) ->
                                                findMultiLiftDrag(context, tunnel, FloatArgumentType.getFloat(context, "altitude")))
                                        )
                                )
                )
        );
    }

    private int findMultiLiftDrag(CommandContext<CommandSourceStack> context, EntityWindTunnel tunnel, float altitude) {
        tunnel.startFindMultiLiftDragJob(altitude);
        return 1;
    }

    private int setSpeedList(CommandContext<CommandSourceStack> context, EntityWindTunnel tunnel, String list) {
        tunnel.setSpeedList(parseNumList(list));
        return 1;
    }

    private int setAOAList(CommandContext<CommandSourceStack> context, EntityWindTunnel tunnel, String list) {
        tunnel.setAOAList(parseNumList(list));
        return 1;
    }

    private int setTurnRateList(CommandContext<CommandSourceStack> context, EntityWindTunnel tunnel, String list) {
        tunnel.setTurnRateList(parseNumList(list));
        return 1;
    }

    public static double[] parseNumList(String string_list) {
        String[] split = string_list.split(";");
        double[] num_list = new double[split.length];
        for (int i = 0; i < split.length; ++i) {
            try {
                num_list[i] = Double.parseDouble(split[i]);
            } catch (NumberFormatException e) {
                num_list[i] = 0;
            }
        }
        return num_list;
    }

    private int findLiftDrag(CommandContext<CommandSourceStack> context, EntityWindTunnel tunnel,
                             float speed, float turn_rate, float aoa, float altitude) {
        tunnel.startFindLiftDragJob(speed, turn_rate, aoa, altitude);
        return 1;
    }

    private int setWindTunnelCommand(CommandContext<CommandSourceStack> context, EntityWindTunnel tunnel,
                                     @Nullable VehicleStats preset, @Nullable Vec3 speed, @Nullable Vec3 rotation,
                                     float throttle, boolean afterburner) {
        if (preset != null) tunnel.setPreset(preset.getId());
        if (speed != null) tunnel.setSpeed(speed);
        if (rotation != null) tunnel.setQ(UtilAngles.toQuaternionF(rotation.y, rotation.x, rotation.z));
        if (throttle >= 0) tunnel.setThrottle(throttle);
        tunnel.setAfterBurner(afterburner);
        return 1;
    }

    private int setPresetCommand(CommandContext<CommandSourceStack> context, EntityWindTunnel tunnel,
                                 @Nullable VehicleStats preset) {
        if (preset != null) tunnel.setPreset(preset.getId());
        return 1;
    }

    private int setVehicleHidden(CommandContext<CommandSourceStack> context, EntityWindTunnel tunnel, boolean hidden) {
        tunnel.setHideModel(hidden);
        return 1;
    }

    private int setVehicleAltitude(CommandContext<CommandSourceStack> context, EntityWindTunnel tunnel, float altitude) {
        tunnel.setAltitude(altitude);
        return 1;
    }

    private int setVehicleInputs(CommandContext<CommandSourceStack> context, EntityWindTunnel tunnel,
                                 float pitch, float yaw, float roll) {
        tunnel.setInputs(new Vec3(pitch, yaw, roll));
        return 1;
    }

    private static Optional<EntityWindTunnel> getWindTunnel(CommandContext<CommandSourceStack> context) {
        return findWindTunnel(context.getSource().getPosition(), context.getSource().getLevel());
    }

    public interface IWindTunnelCommand extends Command<CommandSourceStack> {
        default int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
            Optional<EntityWindTunnel> opt = getWindTunnel(context);
            if (opt.isEmpty()) {
                context.getSource().sendFailure(UtilMCText.literal("No Wind Tunnels within "
                        +WIND_TUNNEL_SEARCH_RANGE+" blocks found!"));
                return 0;
            }
            return run(context, opt.get());
        }
        int run(CommandContext<CommandSourceStack> context, EntityWindTunnel tunnel) throws CommandSyntaxException;
    }

}
