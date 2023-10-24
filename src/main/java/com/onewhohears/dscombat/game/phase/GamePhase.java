package com.onewhohears.dscombat.game.phase;

import com.onewhohears.dscombat.game.agent.PlayerAgent;
import com.onewhohears.dscombat.game.agent.TeamAgent;
import com.onewhohears.dscombat.game.condition.PhaseExitCondition;
import com.onewhohears.dscombat.game.data.GameData;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.Vec3;

public abstract class GamePhase<T extends GameData> {
	
	private final String id;
	private final T gameData;
	private final PhaseExitCondition<T>[] exitConditions;
	private int age;
	
	@SafeVarargs
	protected GamePhase(String id, T gameData, PhaseExitCondition<T>...exitConditions) {
		this.id = id;
		this.gameData = gameData;
		this.exitConditions = exitConditions;
	}
	
	public void tickPhase(MinecraftServer server) {
		++age;
		checkExitConditions(server);
	}
	
	public void tickPlayerAgent(MinecraftServer server, PlayerAgent<?> agent) {
		ServerPlayer player = agent.getPlayer(server);
		if (player == null) return;
		if (!player.gameMode.isCreative()) {
			if (isForceSurvivalMode()) player.setGameMode(GameType.SURVIVAL);
			else if (isForceAdventureMode()) player.setGameMode(GameType.ADVENTURE);
		}
	}
	
	public void tickTeamAgent(MinecraftServer server, TeamAgent<?> agent) {
		
	}
	
	public void onReset(MinecraftServer server) {
		System.out.println("PHASE RESET "+id);
		age = 0;
	}
	
	public void onStart(MinecraftServer server) {
		System.out.println("PHASE END "+id);
		updateWorldBorder(server);
	}
	
	public void onStop(MinecraftServer server) {
		System.out.println("PHASE STOP "+id);
	}
	
	public void checkExitConditions(MinecraftServer server) {
		if (exitConditions == null || exitConditions.length == 0) return;
		for (PhaseExitCondition<T> con : exitConditions) {
			if (con.shouldExit(server, this)) {
				con.onExit(server, this);
				gameData.changePhase(server, con.getNextPhaseId());
				break;
			}
		}
	}
	
	public void updateWorldBorder(MinecraftServer server) {
		WorldBorder border = server.overworld().getWorldBorder();
		if (hasWorldBorder()) {
			double size = getWorldBorderSize();
			long time = getWorldBorderChangeTime();
			if (size != -1 && time != -1) border.lerpSizeBetween(border.getSize(), size, time);
			else if (size != -1) border.setSize(size);
			Vec3 center = getGameData().getGameCenter();
			border.setCenter(center.x, center.z);
		} else border.applySettings(WorldBorder.DEFAULT_SETTINGS);
	}
	
	public String getId() {
		return id;
	}
	
	public T getGameData() {
		return gameData;
	}
	
	public PhaseExitCondition<T>[] getExitConditions() {
		return exitConditions;
	}
	
	public int getAge() {
		return age;
	}
	
	public boolean isSetupPhase() {
		return false;
	}
	
	public boolean isForceAdventureMode() {
		return false;
	}
	
	public boolean isForceSurvivalMode() {
		return false;
	}
	
	public boolean hasWorldBorder() {
		return false;
	}
	
	public double getWorldBorderSize() {
		return -1;
	}
	
	public long getWorldBorderChangeTime() {
		return -1;
	}
	
}
