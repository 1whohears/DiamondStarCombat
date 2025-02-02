package com.onewhohears.dscombat.integration.minigame.data;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.integration.minigame.agent.AttackTeamAgent;
import com.onewhohears.dscombat.integration.minigame.agent.DefendTeamAgent;
import com.onewhohears.dscombat.integration.minigame.phase.villagedefense.VillageDefenseAttackPhase;
import com.onewhohears.dscombat.integration.minigame.phase.villagedefense.VillageDefenseBuyPhase;
import com.onewhohears.dscombat.integration.minigame.phase.villagedefense.VillageDefenseEndPhase;
import com.onewhohears.dscombat.integration.minigame.phase.villagedefense.VillageDefenseSetupPhase;
import com.onewhohears.onewholibs.util.UtilMCText;
import com.onewhohears.minigames.minigame.agent.TeamAgent;
import com.onewhohears.minigames.minigame.data.DeathMatchData;
import com.onewhohears.minigames.minigame.data.MiniGameData;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.scores.PlayerTeam;

public class VillageDefenseData extends DeathMatchData {
	
	public static VillageDefenseData createVillageDefense(String instanceId, String gameTypeId, int roundsToWin) {
		VillageDefenseData game = new VillageDefenseData(instanceId, gameTypeId);
		game.setPhases(new VillageDefenseSetupPhase(game), 
				new VillageDefenseBuyPhase(game), 
				new VillageDefenseAttackPhase(game),
				new VillageDefenseEndPhase(game));
		game.canAddIndividualPlayers = false;
		game.canAddTeams = false;
		game.requiresSetRespawnPos = true;
		game.worldBorderDuringGame = true;
		game.initialLives = 1;
		game.buyTime = 300;
		game.attackTime = 6000;
		game.roundsToWin = roundsToWin;
		game.buyInAttackTime = false;
		return game;
	}
	
	public static VillageDefenseData createExtendedVillageDefense(String instanceId, String gameTypeId) {
		VillageDefenseData game = createVillageDefense(instanceId, gameTypeId, 1);
		game.initialLives = 1000;
		game.attackTime = 36000;
		game.buyInAttackTime = true;
		return game;
	}
	
	protected int buyTime = 200, attackTime = 6000, roundsToWin = 3;
	protected boolean buyInAttackTime = false;
	
	private AttackTeamAgent attackers;
	private DefendTeamAgent defenders;
	
	protected VillageDefenseData(String instanceId, String gameTypeId) {
		super(instanceId, gameTypeId);
		this.addKits("scout", "soldier", "demoman", "heavy", "sniper");
		this.moneyPerRound = 24;
	}

	@Override
	public CompoundTag save() {
		CompoundTag nbt = super.save();
		nbt.putInt("buyTime", buyTime);
		nbt.putInt("attackTime", attackTime);
		nbt.putInt("roundsToWin", roundsToWin);
		nbt.putBoolean("buyInAttackTime", buyInAttackTime);
		return nbt;
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		buyTime = nbt.getInt("buyTime");
		attackTime = nbt.getInt("attackTime");
		roundsToWin = nbt.getInt("roundsToWin");
		buyInAttackTime = nbt.getBoolean("buyInAttackTime");
	}
	
	public void serverTick(MinecraftServer server) {
		if (isFirstTick()) setupAttackDefendTeams(server);
		super.serverTick(server);
	}
	
	protected void setupAttackDefendTeams(MinecraftServer server) {
		PlayerTeam attack = server.getScoreboard().addPlayerTeam("attack");
		PlayerTeam defend = server.getScoreboard().addPlayerTeam("defend");
		setupPlayerTeam(attack, true);
		setupPlayerTeam(defend, false);
		attackers = (AttackTeamAgent) getAddTeam(attack, true);
		defenders = (DefendTeamAgent) getAddTeam(defend, true);
	}
	
	protected void setupPlayerTeam(PlayerTeam team, boolean attack) {
		team.setAllowFriendlyFire(false);
		team.setSeeFriendlyInvisibles(true);
		if (attack) {
			team.setDisplayName(UtilMCText.translatable("team.minigames.attackers"));
			team.setColor(ChatFormatting.RED);
		} else {
			team.setDisplayName(UtilMCText.translatable("team.minigames.defenders"));
			team.setColor(ChatFormatting.BLUE);
		}
	}
	
	@Override
	public boolean canFinishSetupPhase(MinecraftServer server) {
		return super.canFinishSetupPhase(server) && isFlagPlaced(server);
	}
	
	public boolean isFlagPlaced(MinecraftServer server) {
		// TODO 3.9.1 is flag placed to finish setup phase
		return false;
	}
	
	@Nullable
	public AttackTeamAgent getAttackers() {
		return attackers;
	}
	
	@Nullable
	public DefendTeamAgent getDefenders() {
		return defenders;
	}
	
	public int getBuyTime() {
		return buyTime;
	}
	
	public int getAttackTime() {
		return attackTime;
	}
	
	public int getRoundsToWin() {
		return roundsToWin;
	}
	
	public boolean canBuyInAttackTime() {
		return buyInAttackTime;
	}

	@Override
	public TeamAgent createTeamAgent(String teamName) {
		if (teamName.equals("defend")) return new DefendTeamAgent(teamName, this);
		else if (teamName.equals("attack")) return new AttackTeamAgent(teamName, this);
		return new TeamAgent(teamName, this);
	}

}
