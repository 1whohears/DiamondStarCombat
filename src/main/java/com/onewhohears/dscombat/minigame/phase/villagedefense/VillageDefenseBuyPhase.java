package com.onewhohears.dscombat.minigame.phase.villagedefense;

import java.util.Collection;
import java.util.List;

import com.onewhohears.dscombat.minigame.condition.ScoreWinExitCondition;
import com.onewhohears.dscombat.minigame.data.VillageDefenseData;
import com.onewhohears.minigames.minigame.agent.PlayerAgent;
import com.onewhohears.minigames.minigame.agent.TeamAgent;
import com.onewhohears.minigames.minigame.condition.TimeoutPhaseExitCondition;
import com.onewhohears.minigames.minigame.phase.deathmatch.DeathMatchPlayPhase;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class VillageDefenseBuyPhase extends DeathMatchPlayPhase<VillageDefenseData> {

	public VillageDefenseBuyPhase(VillageDefenseData gameData) {
		super("village_defense_buy", gameData, 
			new ScoreWinExitCondition(),
			new TimeoutPhaseExitCondition<>("village_defense_buy_timeout", 
				"village_defense_attack", gameData.getBuyTime(), 
				"info.dscombat.buy_phase_end"));
	}
	
	@Override
	public void tickPhase(MinecraftServer server) {
		super.tickPhase(server);
	}
	
	@Override
	public void onReset(MinecraftServer server) {
		super.onReset(server);
	}
	
	@Override
	public void onStart(MinecraftServer server) {
		super.onStart(server);
		getGameData().addShops("attacker", "defender");
		getGameData().getAllPlayerAgents().forEach(
				(agent) -> agent.setLives(getGameData().getInitialLives()));
		getGameData().tpPlayersToSpawnPosition(server);
		// TODO 3.8.2 refill kits
		giveMoneyToTeams(server);
		// TODO 3.8.3.1 tell teams current game info (buy phase start/score/time left)
	}
	
	public void giveMoneyToTeams(MinecraftServer server) {
		List<TeamAgent<?>> teams = getGameData().getTeamAgents();
		int totalPlayers = getGameData().getAllPlayerAgents().size();
		int totalMoney = totalPlayers * getGameData().getBuyPhaseMoney();
		int moneyPerTeam = (int)((double)totalMoney / (double)teams.size());
		ItemStack money = com.onewhohears.minigames.init.ModItems.MONEY.get().getDefaultInstance();
		for (TeamAgent<?> team : teams) {
			Collection<?> players = team.getPlayerAgents();
			int moneyPerPlayer = (int)((double)moneyPerTeam / (double)players.size());
			money.setCount(moneyPerPlayer);
			players.forEach((player) -> {
				PlayerAgent<?> pa = (PlayerAgent<?>)player;
				ServerPlayer sp = pa.getPlayer(server);
				if (sp == null) return;
				sp.addItem(money.copy());
			});
		}
	}
	
	@Override
	public void onStop(MinecraftServer server) {
		super.onStop(server);
		getGameData().removeShops("attacker", "defender");
	}

}
