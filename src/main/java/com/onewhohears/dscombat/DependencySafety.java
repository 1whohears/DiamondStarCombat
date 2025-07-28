package com.onewhohears.dscombat;

import com.onewhohears.dscombat.integration.distant_players.DSCDistantPlayers;
import com.onewhohears.dscombat.integration.minigame.DSCMiniGames;
import com.onewhohears.dscombat.integration.minigame.gen.DSCKitGenerator;
import com.onewhohears.dscombat.integration.minigame.gen.DSCShopGenerator;

import net.minecraft.data.DataGenerator;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class DependencySafety {
	
	public static void fmlCommonSetup() {
		if (DSCombatMod.minigamesLoaded) DSCMiniGames.registerGames();
		if (DSCombatMod.distantPlayersLoaded) DSCDistantPlayers.register();
	}
	
	public static void serverDataGen(DataGenerator generator) {
		if (DSCombatMod.minigamesLoaded) {
			DSCKitGenerator.register(generator);
			DSCShopGenerator.register(generator);
		}
	}

	public static void addExtraEntityToRDP(@NotNull MinecraftServer server, @NotNull Entity entity, @NotNull ServerPlayer... visibleTo) {
		if (DSCombatMod.distantPlayersLoaded) DSCDistantPlayers.addExtraEntity(server, entity, visibleTo);
	}
	
}
