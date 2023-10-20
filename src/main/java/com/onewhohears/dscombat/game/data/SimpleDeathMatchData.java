package com.onewhohears.dscombat.game.data;

import com.onewhohears.dscombat.game.agent.PlayerAgent;
import com.onewhohears.dscombat.game.agent.TeamAgent;
import com.onewhohears.dscombat.game.phase.GamePhase;
import com.onewhohears.dscombat.game.phase.SetupPhase;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.PlayerTeam;

public class SimpleDeathMatchData extends GameData {
	
	private boolean canAddPlayers, canAddTeams;
	
	public SimpleDeathMatchData(String id, SetupPhase<?> setupPhase, GamePhase<?> nextPhase, GamePhase<?>[] otherPhases) {
		super(id, setupPhase, nextPhase, otherPhases);
	}

	@Override
	public boolean canAddIndividualPlayers() {
		return canAddPlayers;
	}

	@Override
	public boolean canAddTeams() {
		return canAddTeams;
	}

	@Override
	public <D extends GameData> PlayerAgent<D> createPlayerAgent(ServerPlayer player) {
		return null;
	}

	@Override
	public <D extends GameData> PlayerAgent<D> createTeamPlayerAgent(ServerPlayer player, TeamAgent<D> teamAgent) {
		return null;
	}

	@Override
	public <D extends GameData> TeamAgent<D> createTeamAgent(PlayerTeam team) {
		return null;
	}

}
