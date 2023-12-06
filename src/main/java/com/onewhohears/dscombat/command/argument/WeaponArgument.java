package com.onewhohears.dscombat.command.argument;

import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

public class WeaponArgument implements ArgumentType<String> {
	
	private static final DynamicCommandExceptionType ERROR_WEAPON_NOT_FOUND = new DynamicCommandExceptionType((arg) -> {
		return Component.translatable("error.weapon.notFound", arg);
	});
	
	public static WeaponArgument weapon() {
		return new WeaponArgument();
	}
	
	public static WeaponData getWeapon(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
		String w = context.getArgument(name, String.class);
		WeaponData data = WeaponPresets.get().getPreset(w);
		if (data == null) throw ERROR_WEAPON_NOT_FOUND.create(w);
		return data;
	}
	
	@Override
	public String parse(StringReader reader) throws CommandSyntaxException {
		return reader.readUnquotedString();
	}
	
	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		return context.getSource() instanceof SharedSuggestionProvider ? 
				SharedSuggestionProvider.suggest(WeaponPresets.get().getPresetIds(), builder) : 
				Suggestions.empty();
	}

}
