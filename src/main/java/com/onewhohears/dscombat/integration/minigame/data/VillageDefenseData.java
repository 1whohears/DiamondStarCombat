package com.onewhohears.dscombat.integration.minigame.data;

import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.integration.minigame.phase.village_defense.VillageDefenseBuyPhase;
import com.onewhohears.minigames.minigame.data.KillFlagData;
import com.onewhohears.minigames.minigame.phase.buyattackrounds.BuyAttackAttackEndPhase;
import com.onewhohears.minigames.minigame.phase.buyattackrounds.BuyAttackEndPhase;
import com.onewhohears.minigames.minigame.phase.buyattackrounds.BuyAttackSetupPhase;
import com.onewhohears.minigames.minigame.phase.flag.KillFlagAttackPhase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class VillageDefenseData extends KillFlagData {
	
	public static VillageDefenseData createVillageDefenseVehicle(String instanceId, String gameTypeId) {
		VillageDefenseData game = new VillageDefenseData(instanceId, gameTypeId);
		game.setPhases(new BuyAttackSetupPhase<>(game),
				new VillageDefenseBuyPhase(game),
				new KillFlagAttackPhase<>(game),
				new BuyAttackAttackEndPhase<>(game),
				new BuyAttackEndPhase<>(game));
		game.addAttackerShop("vehicle_attacker");
		game.addDefenderShop("vehicle_defender");
		return game;
	}
	
	public VillageDefenseData(String instanceId, String gameTypeId) {
		super(instanceId, gameTypeId);
		this.canAddIndividualPlayers = false;
		this.canAddTeams = true;
		this.requiresSetRespawnPos = true;
		this.worldBorderDuringGame = true;
		this.initialLives = 1;
		this.buyTime = 600;
		this.attackTime = 6000;
		this.roundsToWin = 5;
		this.moneyPerRound = 24;
	}

	public void givePlayersTheirVehicle(MinecraftServer server) {
		getAllPlayerAgents().forEach(agent -> {
			ServerPlayer sp = agent.getPlayer(server);
			if (sp == null || !sp.isPassenger() || !(sp.getRootVehicle() instanceof EntityVehicle vehicle)) return;
			if (!sp.equals(vehicle.getControllingPlayerOrBot())) return;
			vehicle.becomeItem(sp);
		});
	}

}
