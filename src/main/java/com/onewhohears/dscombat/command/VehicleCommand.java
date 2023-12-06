package com.onewhohears.dscombat.command;

import java.util.Collection;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.onewhohears.dscombat.command.argument.VehiclePresetArgument;
import com.onewhohears.dscombat.data.aircraft.AircraftPreset;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class VehicleCommand {
	
	public VehicleCommand(CommandDispatcher<CommandSourceStack> d) {
		d.register(Commands.literal("givevehicle").requires((stack) -> { return stack.hasPermission(2);})
			.then(Commands.argument("preset", VehiclePresetArgument.vehiclePreset())
			.executes((context) -> giveVehicle(context, VehiclePresetArgument.getVehiclePreset(context, "preset"), null))
				.then(Commands.argument("player", EntityArgument.players())
				.executes((context) -> giveVehicle(context, VehiclePresetArgument.getVehiclePreset(context, "preset"), 
						EntityArgument.getPlayers(context, "player")))
			)));
	}
	
	private int giveVehicle(CommandContext<CommandSourceStack> context, AircraftPreset preset, Collection<ServerPlayer> players) {
		ItemStack item = preset.getItem();
		if (players == null) {
			ServerPlayer player = context.getSource().getPlayer();
			if (player != null) player.addItem(item);
		} else for (ServerPlayer player : players) player.addItem(item);
		return 1;
	}
	
}
