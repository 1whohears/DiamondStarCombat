package com.onewhohears.dscombat.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.onewhohears.dscombat.data.vehicle.VehiclePresets;
import com.onewhohears.dscombat.data.vehicle.stats.VehicleStats;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.item.ItemVehicle;
import com.onewhohears.onewholibs.util.UtilEntity;
import com.onewhohears.onewholibs.util.UtilMCText;
import com.onewhohears.onewholibs.util.math.UtilGeometry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.AngleArgument;
import net.minecraft.commands.arguments.CompoundTagArgument;
import net.minecraft.commands.arguments.NbtTagArgument;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.commands.arguments.coordinates.RotationArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

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
        d.register(Commands.literal("summon_vehicle_item").requires((stack) -> stack.hasPermission(2))
                        .then(Commands.argument("pos", Vec3Argument.vec3())
                                .executes(
                                context -> summonVehicleItem(context,
                                        Vec3Argument.getVec3(context, "pos"), null, null
                                ))
                                .then(Commands.argument("rotation", RotationArgument.rotation())
                                        .executes(
                                                context -> summonVehicleItem(context,
                                                        Vec3Argument.getVec3(context, "pos"),
                                                        RotationArgument.getRotation(context, "rotation"),
                                                        null
                                                ))
                                        .then(Commands.argument("nbt", CompoundTagArgument.compoundTag())
                                                .executes(
                                                        context -> summonVehicleItem(context,
                                                                Vec3Argument.getVec3(context, "pos"),
                                                                RotationArgument.getRotation(context, "rotation"),
                                                                CompoundTagArgument.getCompoundTag(context, "nbt")
                                                        ))
                                        )
                                )
                        )
                .executes(context -> summonVehicleItem(
                        context, null, null, null)));
    }

    private static int summonVehicleItem(CommandContext<CommandSourceStack> context, @Nullable Vec3 pos,
                                         @Nullable Coordinates rotation, @Nullable CompoundTag additionalNbt) {
        ServerPlayer player = context.getSource().getPlayer();
        if (player == null) {
            context.getSource().sendFailure(UtilMCText.literal("Command must be used by player!"));
            return 0;
        }
        if (pos == null) pos = player.position();
        float yaw = player.getYRot();
        if (rotation != null) yaw = rotation.getRotation(context.getSource()).y;
        ItemStack stack = null;
        ItemVehicle item = null;
        if (player.getOffhandItem().getItem() instanceof ItemVehicle iv) {
            stack = player.getOffhandItem();
            item = iv;
        } else if (player.getInventory().getSelected().getItem() instanceof ItemVehicle iv) {
            stack = player.getInventory().getSelected();
            item = iv;
        } else {
            for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
                if (player.getInventory().getItem(i).getItem() instanceof ItemVehicle iv) {
                    stack = player.getInventory().getItem(i);
                    item = iv;
                }
            }
        }
        if (stack == null) {
            context.getSource().sendFailure(UtilMCText.literal(
                    "There are no vehicle items in "+player.getScoreboardName()+" inventory!"));
            return 0;
        }
        Level level = UtilEntity.getLevel(player);
        String presetName = item.getPresetName(stack);
        VehicleStats vs = VehiclePresets.get().get(presetName);
        if (vs == null) vs = VehiclePresets.get().get(item.getDefaultPreset());
        EntityType<? extends EntityVehicle> entityType = vs.getEntityType();
        ItemStack spawn_data_stack = item.spawnData(stack, player, vs.getId(), yaw, additionalNbt);
        EntityVehicle vehicle = entityType.spawn((ServerLevel)level,
                spawn_data_stack, player,
                UtilGeometry.toBlockPos(pos).above(4),
                MobSpawnType.SPAWN_EGG,
                false, false);
        if (vehicle == null) {
            context.getSource().sendFailure(UtilMCText.literal("Failed to spawn vehicle with preset "+presetName));
            return 0;
        }
        level.gameEvent(player, GameEvent.ENTITY_PLACE, pos);
        if (!player.isCreative()) stack.shrink(1);
        return 1;
    }
}
