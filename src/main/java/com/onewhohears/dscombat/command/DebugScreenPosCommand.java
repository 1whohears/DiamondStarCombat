package com.onewhohears.dscombat.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ToClientVehicleScreenDebug;
import com.onewhohears.dscombat.data.aircraft.EntityScreenData;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.AngleArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

public class DebugScreenPosCommand {
	
	public DebugScreenPosCommand(CommandDispatcher<CommandSourceStack> d) {
		d.register(Commands.literal("debugscreenpos").requires((stack) -> { return stack.hasPermission(2);})
			.then(Commands.argument("type", IntegerArgumentType.integer(0))
			.then(Commands.argument("rel_pos", Vec3Argument.vec3())
			.then(Commands.argument("width", FloatArgumentType.floatArg(0))
			.then(Commands.argument("height", FloatArgumentType.floatArg(0))
			.then(Commands.argument("rel_x_rot", AngleArgument.angle())
			.then(Commands.argument("rel_y_rot", AngleArgument.angle())
			.then(Commands.argument("rel_z_rot", AngleArgument.angle())
			.executes((context) -> {
				int type = IntegerArgumentType.getInteger(context, "type");
				Vec3 rel_pos = Vec3Argument.getVec3(context, "rel_pos");
				float width = FloatArgumentType.getFloat(context, "width");
				float height = FloatArgumentType.getFloat(context, "height");
				float rel_x_rot = AngleArgument.getAngle(context, "rel_x_rot");
				float rel_y_rot = AngleArgument.getAngle(context, "rel_y_rot");
				float rel_z_rot = AngleArgument.getAngle(context, "rel_z_rot");
				ServerPlayer player = context.getSource().getPlayer();
				if (!(player.getRootVehicle() instanceof EntityVehicle vehicle)) {
					context.getSource().sendFailure(Component.literal("You must ride a vehicle to use this command!"));
					return 0;
				}
				EntityScreenData screenData = vehicle.getScreenByTypeId(type);
				if (screenData == null) {
					context.getSource().sendFailure(Component.literal("This vehicle doesn't have this screen type!"));
					return 0;
				}
				screenData.rel_pos = rel_pos;
				screenData.width = width;
				screenData.height = height;
				screenData.xRot = rel_x_rot;
				screenData.yRot = rel_y_rot;
				screenData.zRot = rel_z_rot;
				PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), 
						new ToClientVehicleScreenDebug(screenData));
				context.getSource().sendSuccess(Component.literal("Changed screen position! (NOT permanent!)"), false);
				return 1;
			})
		))))))));
	}
	
}
