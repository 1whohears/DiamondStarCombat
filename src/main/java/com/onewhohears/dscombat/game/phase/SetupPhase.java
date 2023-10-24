package com.onewhohears.dscombat.game.phase;

import com.onewhohears.dscombat.game.condition.NeverExitCondition;
import com.onewhohears.dscombat.game.data.GameData;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;

public abstract class SetupPhase<T extends GameData> extends GamePhase<T> {
	
	protected boolean smallWorldBorderStartArea;
	protected boolean forceAdventureMode;
	
	protected SetupPhase(String id, T gameData) {
		super(id, gameData, new NeverExitCondition<>());
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
		if (smallWorldBorderStartArea) {
			BlockPos pos = new BlockPos(getGameData().getGameCenter());
			server.overworld().setDefaultSpawnPos(pos, 0);
		}
	}
	
	@Override
	public void onStop(MinecraftServer server) {
		super.onStop(server);
	}
	
	@Override
	public boolean isForceAdventureMode() {
		return forceAdventureMode;
	}
	
	@Override
	public boolean isSetupPhase() {
		return true;
	}
	
	@Override
	public boolean hasWorldBorder() {
		return smallWorldBorderStartArea;
	}
	
	@Override
	public double getWorldBorderSize() {
		return 32;
	}
	
}
