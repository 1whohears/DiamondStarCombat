package com.onewhohears.dscombat.game.agent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.game.data.GameData;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.PlayerTeam;

public class TeamAgent<D extends GameData> extends GameAgent<D> {
	
	private final Map<String, PlayerAgent<D>> playerAgents = new HashMap<>();
	
	public TeamAgent(PlayerTeam team, D gameData) {
		super(team.getName(), gameData);
	}
	
	@Override
	public void tickAgent(MinecraftServer server) {
		super.tickAgent(server);
		updatePlayerAgentMap(server);
		tickPlayerAgents(server);
	}
	
	@Override
	public boolean canTickAgent(MinecraftServer server) {
		return getTeam(server) != null;
	}
	
	protected void tickPlayerAgents(MinecraftServer server) {
		playerAgents.forEach((username, player) -> {
			player.tickAgent(server, this);
		});
	}
	
	protected void updatePlayerAgentMap(MinecraftServer server) {
		PlayerTeam team = getTeam(server);
		Collection<String> usernames = team.getPlayers();
		playerAgents.forEach((username, player) -> {
			if (!usernames.contains(username)) playerAgents.remove(username);
		});
		for (String username : usernames) {
			if (playerAgents.containsKey(username)) continue;
			ServerPlayer player = server.getPlayerList().getPlayerByName(username);
			if (player != null) playerAgents.put(username, getGameData().createPlayerAgent(player));
		}
	}
	
	@Nullable
	public PlayerTeam getTeam(MinecraftServer server) {
		Collection<PlayerTeam> teams = server.getScoreboard().getPlayerTeams();
		for (PlayerTeam team : teams) 
			if (team.getName().equals(getId())) 
				return team;
		return null;
	}
	
	@Nullable
	public PlayerAgent<D> getPlayerAgentByUsername(String name) {
		return playerAgents.get(name);
	}
	
	@Nullable
	public PlayerAgent<D> getPlayerAgentByUUID(String uuid) {
		for (PlayerAgent<D> agent : playerAgents.values()) 
			if (agent.getId().equals(uuid)) 
				return agent;
		return null;
	}

	@Override
	public boolean isPlayer() {
		return false;
	}

	@Override
	public boolean isTeam() {
		return true;
	}

}
