package com.onewhohears.dscombat.game.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.game.agent.GameAgent;
import com.onewhohears.dscombat.game.agent.PlayerAgent;
import com.onewhohears.dscombat.game.agent.TeamAgent;
import com.onewhohears.dscombat.game.phase.GamePhase;
import com.onewhohears.dscombat.game.phase.SetupPhase;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team;

public abstract class GameData {
	
	private final String id;
	private final Map<String, GameAgent<?>> agents = new HashMap<>();
	private final Map<String, GamePhase<?>> phases = new HashMap<>();
	private SetupPhase<?> setupPhase;
	private GamePhase<?> nextPhase;
	private GamePhase<?> currentPhase;
	private int age;
	private boolean isStarted, isStopped;
	
	protected Vec3 gameCenter = Vec3.ZERO;
	protected boolean canAddIndividualPlayers, canAddTeams, requiresSetRespawnPos;
	protected int initialLives = 3;
	protected double gameBorderSize = 1000;
	
	protected GameData(String id) {
		this.id = id;
	}
	
	protected void setPhases(SetupPhase<?> setupPhase, GamePhase<?> nextPhase, GamePhase<?>...otherPhases) {
		this.setupPhase = setupPhase;
		this.nextPhase = nextPhase;
		phases.put(setupPhase.getId(), setupPhase);
		phases.put(nextPhase.getId(), nextPhase);
		for (GamePhase<?> phase : otherPhases) phases.put(phase.getId(), phase);
		currentPhase = setupPhase;
	}
	
	public void serverTick(MinecraftServer server) {
		if (!isStarted() && shouldStart(server)) start(server);
		if (isStarted() && !isStopped() && shouldStop(server)) stop(server);
 		if (shouldTickGame(server)) tickGame(server);
	}
	
	public void tickGame(MinecraftServer server) {
		++age;
		currentPhase.tickPhase(server);
		agents.forEach((id, agent) -> { 
			if (agent.canTickAgent(server)) 
				tickAgent(server, agent); 
		});
	}
	
	protected void tickAgent(MinecraftServer server, GameAgent<?> agent) {
		agent.tickAgent(server);
		if (agent.isPlayer()) currentPhase.tickPlayerAgent(server, (PlayerAgent<?>) agent);
		else if (agent.isTeam()) currentPhase.tickTeamAgent(server, (TeamAgent<?>) agent);
 	}
	
	public boolean changePhase(MinecraftServer server, String phaseId) {
		if (!phases.containsKey(phaseId)) return false;
		System.out.println("GAME CHANGE PHASE "+id+" to "+phaseId);
		currentPhase = phases.get(phaseId);
		currentPhase.onReset(server);
		currentPhase.onStart(server);
		return true;
	}
	
	public boolean finishSetupPhase(MinecraftServer server) {
		if (!isSetupPhase()) return false;
		if (!canFinishSetupPhase(server)) return false;
		currentPhase.onStop(server);
		if (requiresSetRespawnPos()) applyAllAgentRespawnPoints(server);
		return changePhase(server, nextPhase.getId());
	}
	
	public boolean canFinishSetupPhase(MinecraftServer server) {
		return agents.size() >= 2 && areAgentRespawnPosSet();
	}
	
	public void reset(MinecraftServer server) {
		System.out.println("GAME RESET "+id);
		isStarted = false;
		isStopped = false;
		age = 0;
		agents.clear();
		phases.forEach((id, phase) -> phase.onReset(server));
	}
	
	public void start(MinecraftServer server) {
		System.out.println("GAME START "+id);
		isStarted = true;
		isStopped = false;
		resetAllAgents();
		changePhase(server, setupPhase.getId());
	}
	
	public void stop(MinecraftServer server) {
		System.out.println("GAME STOP "+id);
		isStarted = true;
		isStopped = true;
	}
	
	public boolean shouldTickGame(MinecraftServer server) {
		return isStarted() && !isStopped();
	}
	
	public boolean isSetupPhase() {
		return isStarted() && currentPhase.isSetupPhase();
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
	
	public boolean canAddIndividualPlayers() {
		return canAddIndividualPlayers;
	}
	
	public boolean canAddTeams() {
		return canAddTeams;
	}
	
	public int getInitialLives() {
		return initialLives;
	}
	
	public void setInitialLives(int lives) {
		this.initialLives = lives;
	}
	
	public void setGameCenter(Vec3 center) {
		gameCenter = center;
	}
	
	public void setGameCenter(Vec3 center, MinecraftServer server) {
		setGameCenter(center);
		currentPhase.updateWorldBorder(server);
	}
	
	public Vec3 getGameCenter() {
		return gameCenter;
	}
	
	public double getGameBorderSize() {
		return gameBorderSize;
	}
	
	public void setGameBorderSize(double size) {
		gameBorderSize = size;
	}
	
	public GamePhase<?> getCurrentPhase() {
		return currentPhase;
	}
	
	public boolean requiresSetRespawnPos() {
		return requiresSetRespawnPos;
	}
	
	public boolean areAgentRespawnPosSet() {
		if (!requiresSetRespawnPos()) return true;
		for (GameAgent<?> agent : agents.values()) 
			if (!agent.hasRespawnPoint()) 
				return false;
		return true;
	}
	
	public String getSetupInfo() {
		String info = "";
		if (canAddIndividualPlayers()) info += "use add_player to add players to the game. ";
		if (canAddTeams()) info += "use add_team to add teams to the game. ";
		info += "use set_center to set the middle of the game. ";
		info += "use set_size to set the game world border size and random start position distance. ";
		if (requiresSetRespawnPos()) info += "use set_spawn to set a player or team spawnpoint. ";
		info += "use set_lives to set the number of initial lives. ";
		return info;
	}
	
	@SuppressWarnings("unchecked")
	public <D extends GameData> PlayerAgent<D> createPlayerAgent(ServerPlayer player) {
		return new PlayerAgent<D>(player, (D) this);
	}
	
	@SuppressWarnings("unchecked")
	public <D extends GameData> TeamAgent<D> createTeamAgent(PlayerTeam team) {
		return new TeamAgent<D>(team, (D)this);
	}
	
	@Nullable
	public PlayerAgent<?> getAddIndividualPlayer(ServerPlayer player) {
		if (!canAddIndividualPlayers()) return null;
		PlayerAgent<?> agent = getPlayerAgentByUUID(player.getStringUUID());
		if (agent == null) agent = createPlayerAgent(player);
		return agent;
	}
	
	@Nullable
	public TeamAgent<?> getAddTeam(PlayerTeam team) {
		if (!canAddTeams()) return null;
		TeamAgent<?> agent = getTeamAgentByName(team.getName());
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
	
	public boolean removeAgentById(String id) {
		return agents.remove(id) != null;
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
	
	public List<GameAgent<?>> getLivingAgents() {
		List<GameAgent<?>> living = new ArrayList<>();
		for (GameAgent<?> agent : agents.values()) 
			if (!agent.isDead()) living.add(agent);
		return living;
	}
	
	public List<GameAgent<?>> getDeadAgents() {
		List<GameAgent<?>> dead = new ArrayList<>();
		for (GameAgent<?> agent : agents.values()) 
			if (agent.isDead()) dead.add(agent);
		return dead;
	}
	
	public List<PlayerAgent<?>> getAllPlayerAgents() {
		List<PlayerAgent<?>> players = new ArrayList<>();
		for (GameAgent<?> agent : agents.values()) {
			if (agent.isPlayer()) players.add((PlayerAgent<?>) agent);
			else if (agent.isTeam()) {
				TeamAgent<?> team = (TeamAgent<?>) agent;
				for (PlayerAgent<?> player : team.getPlayerAgents()) 
					players.add(player);
			}
		}
		return players;
	}
	
	public void resetAllAgents() {
		agents.forEach((id, agent) -> {
			agent.resetAgent();
		});
	}
	
	public void applyAllAgentRespawnPoints(MinecraftServer server) {
		agents.forEach((id, agent) -> {
			agent.applySpawnPoint(server);
		});
	}
	
	public void tpPlayersToSpawnPosition(MinecraftServer server) {
		agents.forEach((id, agent) -> {
			agent.tpToSpawnPoint(server);
		});
	}
	
	public void spreadPlayers(MinecraftServer server) {
		// TODO 3.7 spread players at start of game option
	}
	
	public void announceWinners(MinecraftServer server) {
		List<GameAgent<?>> winners = getLivingAgents();
		if (winners.size() != 1) return;
		GameAgent<?> winner = winners.get(0);
		winner.onWin(server);
	}
	
}
