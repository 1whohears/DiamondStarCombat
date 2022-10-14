package com.onewhohears.dscombat.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class TestWeaponCommand {
	
	public TestWeaponCommand(CommandDispatcher<CommandSourceStack> d) {
		d.register(Commands.literal("testweapon").requires(null).requires((stack) -> {return stack.hasPermission(2);})
			.then(Commands.literal("missile").executes((context) -> {
				return 0;
			})
		));
	}
	
	private int testMissile(CommandContext source) throws CommandSyntaxException {
		
		return 1;
	}
	
}
