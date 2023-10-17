package com.onewhohears.dscombat.game.data;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.game.agent.GameAgent;
import com.onewhohears.dscombat.game.agent.PlayerAgent;
import com.onewhohears.dscombat.game.agent.TeamAgent;
import com.onewhohears.dscombat.game.phase.GamePhase;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team;

public abstract class GameData {
	
	private final String id;
	private final Map<String, GameAgent<?>> agents = new HashMap<>();
	private final Map<String, GamePhase<?>> phases = new HashMap<>();
	private GamePhase<?> currentPhase;
	private int age;
	private boolean isStarted, isStopped;
	
	protected GameData(String id, GamePhase<?>...gamePhases) {
		this.id = id;
		if (gamePhases != null && gamePhases.length > 0) {
			currentPhase = gamePhases[0];
			for (GamePhase<?> phase : gamePhases) 
				phases.put(phase.getId(), phase);
		}
	}
	
	public void serverTick(MinecraftServer server) {
		if (!isStarted() && shouldStart(server)) start(server);
		if (isStarted() && !isStopped() && shouldStop(server)) stop(server);
 		if (shouldTickGame(server)) tickGame(server);
	}
	
	public void tickGame(MinecraftServer server) {
		++age;
		if (currentPhase != null) currentPhase.tickPhase(server);
		agents.forEach((id, agent) -> {
			if (agent.canTickAgent(server)) agent.tickAgent(server);
		});
	}
	
	public boolean changePhase(MinecraftServer server, String phaseId) {
		if (!phases.containsKey(phaseId)) return false;
		currentPhase = phases.get(phaseId);
		currentPhase.onReset(server);
		currentPhase.onStart(server);
		return true;
	}
	
	public void reset(MinecraftServer server) {
		isStarted = false;
		isStopped = false;
		age = 0;
	}
	
	public void start(MinecraftServer server) {
		isStarted = true;
		isStopped = false;
	}
	
	public void stop(MinecraftServer server) {
		isStarted = true;
		isStopped = true;
	}
	
	public boolean shouldTickGame(MinecraftServer server) {
		return isStarted() && !isStopped();
	}
	
	public boolean shouldStart(MinecraftServer server) {
		return true;
	}
	
	public boolean shouldStop(MinecraftServer server) {
		return false;
	}
	
	public boolean isStarted() {
		return isStarted;
	}
	
	public boolean isStopped() {
		return isStopped;
	}
	
	public String getId() {
		return id;
	}
	
	public int getAge() {
		return age;
	}
	
	public GamePhase<?> getCurrentPhase() {
		return currentPhase;
	}
	
	public abstract boolean canAddIndividualPlayers();
	public abstract boolean canAddTeams();
	
	public abstract <D extends GameData> PlayerAgent<D> createPlayerAgent(ServerPlayer player);
	public abstract <D extends GameData> TeamAgent<D> createTeamAgent(PlayerTeam team);
	
	@SuppressWarnings("unchecked")
	@Nullable
	public <D extends GameData> PlayerAgent<D> getAddIndividualPlayer(ServerPlayer player) {
		if (!canAddIndividualPlayers()) return null;
		PlayerAgent<D> agent = (PlayerAgent<D>) getPlayerAgentByUUID(player.getStringUUID());
		if (agent == null) agent = createPlayerAgent(player);
		return agent;
	}
	
	@SuppressWarnings("unchecked")
	@Nullable
	public <D extends GameData> TeamAgent<D> getAddTeam(PlayerTeam team) {
		if (!canAddTeams()) return null;
		TeamAgent<D> agent = (TeamAgent<D>) getTeamAgentByName(team.getName());
		if (agent == null) agent = createTeamAgent(team);
		return agent;
	}
	
	public boolean hasAgentById(String id) {
		return agents.containsKey(id);
	}
	
	@Nullable
	public GameAgent<?> getAgentById(String id) {
		return agents.get(id);
	}
	
	@Nullable
	public PlayerAgent<?> getPlayerAgentByUUID(String uuid) {
		if (canAddIndividualPlayers()) {
			GameAgent<?> agent = getAgentById(uuid);
			if (agent != null && agent.isPlayer()) return (PlayerAgent<?>) agent;
		}
		if (canAddTeams()) {
			for (GameAgent<?> agent : agents.values()) if (agent.isTeam()) {
				TeamAgent<?> team = (TeamAgent<?>)agent;
				PlayerAgent<?> player = team.getPlayerAgentByUUID(uuid);
				if (player != null) return player;
			}
		}
		return null;
	}
	
	@Nullable
	public TeamAgent<?> getTeamAgentByName(String name) {
		if (!canAddTeams()) return null;
		GameAgent<?> agent = getAgentById(name);
		if (agent == null) return null;
		if (agent.isTeam()) return (TeamAgent<?>) agent;
		return null;
	}
	
	@Nullable
	public TeamAgent<?> getPlayerTeamAgent(ServerPlayer player) {
		if (!canAddTeams()) return null;
		Team team = player.getTeam();
		if (team == null) return null;
		return getTeamAgentByName(team.getName());
	}
	
	@Nullable
	public TeamAgent<?> getPlayerTeamAgent(String uuid) {
		if (!canAddTeams()) return null;
		for (GameAgent<?> agent : agents.values()) if (agent.isTeam()) {
			TeamAgent<?> team = (TeamAgent<?>)agent;
			if (team.getPlayerAgentByUUID(uuid) != null) 
				return team;
		}
		return null;
	}
	
}
