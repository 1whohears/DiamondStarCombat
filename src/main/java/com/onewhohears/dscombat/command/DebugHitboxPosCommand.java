package com.onewhohears.dscombat.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ToClientDebugHitboxPos;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.vehicle.RotableHitbox;
import com.onewhohears.onewholibs.util.UtilMCText;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class DebugHitboxPosCommand {
	
	public DebugHitboxPosCommand(CommandDispatcher<CommandSourceStack> d) {
		d.register(Commands.literal("debughitboxpos").requires((stack) -> { return stack.hasPermission(2);})
			.then(Commands.argument("hitbox_name", StringArgumentType.string())
			.then(Commands.argument("size", Vec3Argument.vec3())
			.then(Commands.argument("rel_pos", Vec3Argument.vec3())
			.executes((context) -> {
				String hitbox_name = StringArgumentType.getString(context, "hitbox_name");
				Vec3 rel_pos = Vec3Argument.getVec3(context, "rel_pos");
				Vec3 size = Vec3Argument.getVec3(context, "size");
				ServerPlayer player = context.getSource().getPlayer();
				if (player == null) {
					context.getSource().sendFailure(UtilMCText.translatable("error.dscombat.command_players_only"));
					return 0;
				}
				if (!(player.getRootVehicle() instanceof EntityVehicle vehicle)) {
					context.getSource().sendFailure(UtilMCText.translatable("error.dscombat.command_must_ride_vehicle"));
					return 0;
				}
				RotableHitbox hitbox = vehicle.getHitboxByName(hitbox_name);
				if (hitbox == null) {
					context.getSource().sendFailure(UtilMCText.literal("Hitbox with name "+hitbox_name+" does not exist!"));
					return 0;
				}
				hitbox.setTestPos(rel_pos);
				hitbox.setTestSize(size);
				PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> vehicle), 
						new ToClientDebugHitboxPos(vehicle, hitbox_name, rel_pos, size));
				context.getSource().sendSuccess((Supplier<Component>) UtilMCText.literal("Changed hitbox position and size! (NOT permanent!)"), false);
				return 1;
			})
		))));
	}
	
}
