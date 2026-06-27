package com.onewhohears.dscombat.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.parts.EntityPart;
import com.onewhohears.onewholibs.util.UtilMCText;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public class DebugSlotPosCommand {
	
	public DebugSlotPosCommand(CommandDispatcher<CommandSourceStack> d) {
		d.register(Commands.literal("debugslotpos").requires((stack) -> { return stack.hasPermission(2);})
			.then(Commands.argument("slot_id", StringArgumentType.string())
			.then(Commands.argument("rel_pos", Vec3Argument.vec3(false))
			.executes((context) -> {
				String slot_id = StringArgumentType.getString(context, "slot_id");
				Vec3 rel_pos = Vec3Argument.getVec3(context, "rel_pos");
				ServerPlayer player = context.getSource().getPlayer();
				if (player == null) {
					context.getSource().sendFailure(UtilMCText.translatable("error.dscombat.command_players_only"));
					return 0;
				}
				if (!(player.getRootVehicle() instanceof EntityVehicle vehicle)) {
					context.getSource().sendFailure(UtilMCText.translatable("error.dscombat.command_must_ride_vehicle"));
					return 0;
				}
				EntityPart part = vehicle.getPartBySlotId(slot_id);
				if (part == null) {
					context.getSource().sendFailure(UtilMCText.translatable("slot_id_not_exist", slot_id));
					return 0;
				}
				part.setRelativePos(rel_pos);
				context.getSource().sendSuccess(() -> UtilMCText.translatable("success.dscombat.changed_slot_position"), false);
				return 1;
			})
		)));
	}
	
}
