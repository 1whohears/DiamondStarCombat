package com.onewhohears.dscombat.command.argument;

import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.onewhohears.dscombat.data.vehicle.VehiclePresets;
import com.onewhohears.dscombat.data.vehicle.stats.VehicleStats;
import com.onewhohears.onewholibs.util.UtilMCText;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;

public class VehiclePresetArgument implements ArgumentType<String> {
	
	private static final DynamicCommandExceptionType ERROR_VEHICLE_NOT_FOUND = new DynamicCommandExceptionType((arg) -> {
		return UtilMCText.translatable("error.vehicle.notFound", arg);
	});
	
	public static VehiclePresetArgument vehiclePreset() {
		return new VehiclePresetArgument();
	}
	
	public static VehicleStats getVehiclePreset(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
		String a = context.getArgument(name, String.class);
		VehicleStats data = VehiclePresets.get().get(a);
		if (data == null) throw ERROR_VEHICLE_NOT_FOUND.create(a);
		return data;
	}
	
	@Override
	public String parse(StringReader reader) throws CommandSyntaxException {
		return reader.readUnquotedString();
	}
	
	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		return context.getSource() instanceof SharedSuggestionProvider ? 
				SharedSuggestionProvider.suggest(VehiclePresets.get().getAllIds(), builder) : 
				Suggestions.empty();
	}

}
