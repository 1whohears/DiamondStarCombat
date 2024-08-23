package com.onewhohears.dscombat.integration.minigame.data;

import com.onewhohears.minigames.minigame.data.DeathMatchData;
import com.onewhohears.minigames.minigame.phase.deathmatch.DeathMatchEndPhase;
import com.onewhohears.minigames.minigame.phase.deathmatch.DeathMatchPlayPhase;
import com.onewhohears.minigames.minigame.phase.deathmatch.DeathMatchSetupPhase;

public class DogFightData extends DeathMatchData {
	
	public static DogFightData createSimpleFFADogFight(String instanceId, String gameTypeId) {
		DogFightData game = new DogFightData(instanceId, gameTypeId);
		game.setPhases(new DeathMatchSetupPhase<>(game), 
				new DeathMatchPlayPhase<>(game), 
				new DeathMatchEndPhase<>(game));
		game.canAddIndividualPlayers = true;
		game.canAddTeams = false;
		game.requiresSetRespawnPos = true;
		game.worldBorderDuringGame = false;
		game.initialLives = 1;
		return game;
	}
	
	protected DogFightData(String instanceId, String gameTypeId) {
		super(instanceId, gameTypeId);
	}

}
