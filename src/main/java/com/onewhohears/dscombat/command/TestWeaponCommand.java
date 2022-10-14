package com.onewhohears.dscombat.command;

import java.util.Collection;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

public class TestWeaponCommand {
	
	public TestWeaponCommand(CommandDispatcher<CommandSourceStack> d) {
		d.register(Commands.literal("testweapon").requires(null).requires((stack) -> {return stack.hasPermission(2);})
			.then(Commands.literal("missile")
			.then(Commands.argument("target", EntityArgument.players())
			.executes((context) -> {
				return testMissile(context, EntityArgument.getPlayers(context, "target"));
			}
		))));
	}
	
	private int testMissile(CommandContext<CommandSourceStack> context, Collection<ServerPlayer> players) throws CommandSyntaxException {
		int i = 0;
		for (ServerPlayer p : players) {
			
			++i;
		}
		return i;
	}
	
}
