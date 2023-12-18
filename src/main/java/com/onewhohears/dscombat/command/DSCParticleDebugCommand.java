package com.onewhohears.dscombat.command;

import com.mojang.brigadier.CommandDispatcher;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ToClientVehicleExplode;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

public class DSCParticleDebugCommand {
	
	public DSCParticleDebugCommand(CommandDispatcher<CommandSourceStack> d) {
		d.register(Commands.literal("debugparticle").requires((stack) -> { return stack.hasPermission(2);})
			.then(Commands.literal("vehicle_crash").then(Commands.argument("pos", Vec3Argument.vec3()).executes((context) -> {
				Vec3 pos = Vec3Argument.getVec3(context, "pos");
				PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> context.getSource().getPlayer()), 
						new ToClientVehicleExplode(pos));
				return 1;
			})))
		);		
	}
	
}
