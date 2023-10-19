package com.onewhohears.dscombat.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.game.agent.PlayerAgent;
import com.onewhohears.dscombat.game.data.GameData;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class GameManager {
	
	private static Map<String, GameGenerator> gameGenerators = new HashMap<>();
	private static Map<String, GameData> runningGames = new HashMap<>();
	
	/**
	 * called in {@link net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent}
	 * register all games here
	 */
	public static void registerGames() {
		// TODO 3.1 create and register the following minigame modes
		// team/ffa death match (1 or multiple lives)
		// easy/fair dog fight resets (both players tp to runway when an aircraft is destroyed)
		// bomb the flag/s
		// team/ffa territory control
		// one volunteer runs away from everyone else
		// zombie apocalypse
		// TODO 3.4 shop system
		// TODO 3.5 load custom game presets from data packs
		// TODO 3.6 this game system should probably be a separate mod as an optional dependency...
	}
	
	/**
	 * add a game to the list of games players can play
	 * @param gameTypeId must be unique
	 * @param generator
	 * @return false if gameTypeId already exists
	 */
	public static boolean registerGame(String gameTypeId, GameGenerator generator) {
		if (gameGenerators.containsKey(gameTypeId)) return false;
		gameGenerators.put(gameTypeId, generator);
		return true;
	}
	
	/**
	 * add a new game to the list of running games
	 * @param gameTypeId the type of game
	 * @param gameInstanceId must be unique
	 * @return null if gameInstanceId already exists or gameTypeId doesn't exist
	 */
	@Nullable
	public static GameData startNewGame(String gameTypeId, String gameInstanceId) {
		if (runningGames.containsKey(gameInstanceId)) return null;
		GameGenerator gen = gameGenerators.get(gameTypeId);
		if (gen == null) return null;
		GameData game = gen.create(gameInstanceId);
		runningGames.put(gameInstanceId, game);
		return game;
	}
	
	public static boolean resetGame(String gameInstanceId, MinecraftServer server) {
		if (!runningGames.containsKey(gameInstanceId)) return false;
		runningGames.get(gameInstanceId).reset(server);
		return true;
	}
	
	public static void serverTick(MinecraftServer server) {
		runningGames.forEach((id, game) -> game.serverTick(server));
	}
	
	public static void serverStarted(MinecraftServer server) {
		// TODO 3.3 load games from global saved data
	}

	public static void serverStopping(MinecraftServer server) {
		// TODO 3.2 write games to global saved data
	}
	
	public static String[] getNewGameTypeIds() {
		return gameGenerators.keySet().toArray(new String[gameGenerators.size()]);
	}
	
	public static String[] getRunningeGameIds() {
		return runningGames.keySet().toArray(new String[runningGames.size()]);
	}
	
	public static boolean hasGameType(String gameTypeId) {
		return gameGenerators.containsKey(gameTypeId);
	}
	
	public static boolean isGameRunning(String gameInstanceId) {
		return runningGames.containsKey(gameInstanceId);
	}
	
	@Nullable
	public static GameData getRunningGame(String gameInstanceId) {
		return runningGames.get(gameInstanceId);
	}
	
	public static boolean removeGame(String gameInstanceId) {
		return runningGames.remove(gameInstanceId) != null;
	}
	
	public interface GameGenerator {
		GameData create(String gameInstanceId);
	}
	
	public static List<PlayerAgent<?>> getPlayerAgents(ServerPlayer player) {
		List<PlayerAgent<?>> agents = new ArrayList<>();
		for (GameData game : runningGames.values()) {
			PlayerAgent<?> agent = game.getPlayerAgentByUUID(player.getStringUUID());
			if (agent != null) agents.add(agent);
		}
		return agents;
	}
	
}
