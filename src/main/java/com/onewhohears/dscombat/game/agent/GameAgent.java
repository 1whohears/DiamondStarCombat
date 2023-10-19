package com.onewhohears.dscombat.game.agent;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.game.data.GameData;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.damagesource.DamageSource;

public abstract class GameAgent<D extends GameData> {
	
	private final String id;
	private final D gameData;
	private int age;
	private int score;
	private int lives;
	private int money;
	
	protected GameAgent(String id, D gameData) {
		this.id = id;
		this.gameData = gameData;
	}
	
	public void tickAgent(MinecraftServer server) {
		age++;
		if (isDead()) tickDead(server);
	}
	
	public boolean canTickAgent(MinecraftServer server) {
		return true;
	}
	
	protected void tickDead(MinecraftServer server) {
	}
	
	public void onDeath(MinecraftServer server, @Nullable DamageSource source) {
		lives = Math.max(lives-1, 0);
	}
	
	public boolean isDead() {
		return lives <= 0;
	}
	
	public void resetAgent(int initLives, int initMoney) {
		age = 0;
		score = 0;
		lives = initLives;
		money = initMoney;
	}
	
	public String getId() {
		return id;
	}
	
	public D getGameData() {
		return gameData;
	}
	
	public int getAge() {
		return age;
	}
	
	public int getScore() {
		return score;
	}
	
	public int getLives() {
		return lives;
	}
	
	public int getMoney() {
		return money;
	}
	
	public abstract boolean isPlayer();
	public abstract boolean isPlayerOnTeam();
	public abstract boolean isTeam();
	
}
