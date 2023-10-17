package com.onewhohears.dscombat.game;

import net.minecraft.server.MinecraftServer;

public abstract class GameData {
	
	private final String id;
	private int age;
	private boolean isStarted, isFinished;
	
	protected GameData(String id) {
		this.id = id;
	}
	
	public void serverTick(MinecraftServer server) {
		if (!isStarted() && shouldStart(server)) start(server);
		if (isStarted() && !isFinished() && shouldFinish(server)) finish(server);
 		if (shouldTickGame(server)) tickGame(server);
	}
	
	public void tickGame(MinecraftServer server) {
		++age;
	}
	
	public void start(MinecraftServer server) {
		isStarted = true;
		isFinished = false;
	}
	
	public void finish(MinecraftServer server) {
		isStarted = true;
		isFinished = true;
	}
	
	public void reset(MinecraftServer server) {
		isStarted = false;
		isFinished = false;
	}
	
	public boolean shouldTickGame(MinecraftServer server) {
		return isStarted() && !isFinished();
	}
	
	public boolean shouldStart(MinecraftServer server) {
		return true;
	}
	
	public boolean shouldFinish(MinecraftServer server) {
		return false;
	}
	
	public boolean isStarted() {
		return isStarted;
	}
	
	public boolean isFinished() {
		return isFinished;
	}
	
	// read and write game data functions
	
	public String getId() {
		return id;
	}
	
	public int getAge() {
		return age;
	}
	
}
