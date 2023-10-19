package com.onewhohears.dscombat.game.phase;

import com.onewhohears.dscombat.game.condition.PhaseExitCondition;
import com.onewhohears.dscombat.game.data.GameData;

import net.minecraft.server.MinecraftServer;

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
	
	public void onReset(MinecraftServer server) {
		age = 0;
	}
	
	public void onStart(MinecraftServer server) {
		
	}
	
	public void onStop(MinecraftServer server) {
		
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
	
}
