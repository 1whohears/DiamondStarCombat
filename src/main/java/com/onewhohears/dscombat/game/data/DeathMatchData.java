package com.onewhohears.dscombat.game.data;

import com.onewhohears.dscombat.game.phase.deathmatch.DeathMatchEndPhase;
import com.onewhohears.dscombat.game.phase.deathmatch.DeathMatchPlayPhase;
import com.onewhohears.dscombat.game.phase.deathmatch.DeathMatchSetupPhase;

public class DeathMatchData extends GameData {
	
	public static DeathMatchData createSimpleTeamDeathMatch(String id) {
		DeathMatchData game = new DeathMatchData(id);
		game.setPhases(new DeathMatchSetupPhase<>(game), 
				new DeathMatchPlayPhase<>(game), 
				new DeathMatchEndPhase<>(game));
		game.canAddIndividualPlayers = false;
		game.canAddTeams = true;
		return game;
	}
	
	protected DeathMatchData(String id) {
		super(id);
	}

}
