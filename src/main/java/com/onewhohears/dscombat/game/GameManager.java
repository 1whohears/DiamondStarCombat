package com.onewhohears.dscombat.game;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.game.data.GameData;

import net.minecraft.server.MinecraftServer;

public class GameManager {
	
	public static Map<String, GameData> games = new HashMap<>();
	
	@Nullable
	public static GameData getGameData(String id) {
		return games.get(id);
	}
	
	public static boolean hasGame(String id) {
		return games.containsKey(id);
	}
	
	public static void serverTick(MinecraftServer server) {
		games.forEach((id, game) -> {
			game.serverTick(server);
		});
		/*
		 *  TODO 3.1 add mini game modes using commands
		 *  team/solo vehicle death match, bomb the flag
		 *  long battlebit modes: multiple lives, control territory 
		 */
	}
	
	public static void serverStarted(MinecraftServer server) {
		// TODO 3.3 load games from global saved data
	}

	public static void serverStopping(MinecraftServer server) {
		// TODO 3.2 write games to global saved data
	}
	
}
