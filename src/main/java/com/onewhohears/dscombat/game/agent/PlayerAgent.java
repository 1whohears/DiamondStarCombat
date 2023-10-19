package com.onewhohears.dscombat.game.agent;

import java.util.UUID;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.game.data.GameData;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;

public class PlayerAgent<D extends GameData> extends GameAgent<D> {
	
	private UUID playerId;
	private ServerPlayer player;
	private TeamAgent<D> teamAgent;
	
	public PlayerAgent(ServerPlayer player, D gameData, @Nullable TeamAgent<D> teamAgent) {
		super(player.getStringUUID(), gameData);
		this.playerId = player.getUUID();
		this.player = player;
		this.teamAgent = teamAgent;
	}
	
	public PlayerAgent(ServerPlayer player, D gameData) {
		this(player, gameData, null);
	}
	
	@Override
	public void tickAgent(MinecraftServer server) {
		super.tickAgent(server);
		ServerPlayer player = getPlayer(server);
		if (player == null) return;
		
	}
	
	public void tickAgent(MinecraftServer server, TeamAgent<D> team) {
		tickAgent(server);
		ServerPlayer player = getPlayer(server);
		if (player == null) return;
		
	}
	
	@Override
	protected void tickDead(MinecraftServer server) {
		super.tickDead(server);
		ServerPlayer player = getPlayer(server);
		if (player == null) return;
		
	}
	
	@Override
	public void onDeath(MinecraftServer server, @Nullable DamageSource source) {
		super.onDeath(server, source);
		
	}
	
	@Override
	public boolean canTickAgent(MinecraftServer server) {
		return getPlayer(server) != null && player.isAddedToWorld();
	}
	
	@Nullable
	public ServerPlayer getPlayer(MinecraftServer server) {
		if (player.isAddedToWorld()) return player;
		player = server.getPlayerList().getPlayer(getPlayerId());
		return player;
	}
	
	@Nullable
	public UUID getPlayerId() {
		if (playerId == null) playerId = UUID.fromString(getId());
		return playerId;
	}

	@Override
	public boolean isPlayer() {
		return true;
	}

	@Override
	public boolean isTeam() {
		return false;
	}
	
	@Override
	public boolean isPlayerOnTeam() {
		return teamAgent != null;
	}
	
	@Nullable
	public TeamAgent<D> getTeamAgent() {
		return teamAgent;
	}

}
