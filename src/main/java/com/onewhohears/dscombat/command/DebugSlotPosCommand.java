package com.onewhohears.dscombat.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.parts.EntityPart;
import com.onewhohears.dscombat.util.UtilMCText;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public class DebugSlotPosCommand {
	
	public DebugSlotPosCommand(CommandDispatcher<CommandSourceStack> d) {
		d.register(Commands.literal("debugslotpos").requires((stack) -> { return stack.hasPermission(2);})
			.then(Commands.argument("slot_id", StringArgumentType.string())
			.then(Commands.argument("rel_pos", Vec3Argument.vec3())
			.executes((context) -> {
				String slot_id = StringArgumentType.getString(context, "slot_id");
				Vec3 rel_pos = Vec3Argument.getVec3(context, "rel_pos");
				ServerPlayer player = context.getSource().getPlayer();
				if (!(player.getRootVehicle() instanceof EntityVehicle vehicle)) {
					context.getSource().sendFailure(UtilMCText.literal("You must ride a vehicle to use this command!"));
					return 0;
				}
				EntityPart part = vehicle.getPartBySlotId(slot_id);
				if (part == null) {
					context.getSource().sendFailure(UtilMCText.literal("Slot with id "+slot_id+" does not exist!"));
					return 0;
				}
				part.setRelativePos(rel_pos);
				context.getSource().sendSuccess(UtilMCText.literal("Changed slot position! (NOT permanent!)"), false);
				return 1;
			})
		)));
	}
	
}
