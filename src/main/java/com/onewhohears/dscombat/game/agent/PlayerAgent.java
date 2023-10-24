package com.onewhohears.dscombat.game.agent;

import java.util.UUID;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.game.data.GameData;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;

public class PlayerAgent<D extends GameData> extends GameAgent<D> {
	
	private UUID playerId;
	private ServerPlayer player;
	private TeamAgent<D> teamAgent;
	
	public PlayerAgent(ServerPlayer player, D gameData) {
		super(player.getStringUUID(), gameData);
		this.playerId = player.getUUID();
		this.player = player;
	}
	
	@Override
	public void tickAgent(MinecraftServer server) {
		super.tickAgent(server);
	}
	
	@Override
	protected void tickDead(MinecraftServer server) {
		super.tickDead(server);
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
	
	public void setTeamAgent(TeamAgent<D> teamAgent) {
		this.teamAgent = teamAgent;
	}

	@Override
	public void applySpawnPoint(MinecraftServer server) {
		if (!hasRespawnPoint()) return;
		ServerPlayer player = getPlayer(server);
		if (player == null) return;
		player.setRespawnPosition(server.overworld().dimension(), 
				new BlockPos(getRespawnPoint()), 
				0, true, true);
	}

	@Override
	public void tpToSpawnPoint(MinecraftServer server) {
		ServerPlayer player = getPlayer(server);
		if (player == null) return;
		BlockPos pos = player.getRespawnPosition();
		if (pos == null) pos = server.overworld().getSharedSpawnPos();
		ResourceKey<Level> dim = player.getRespawnDimension();
		ServerLevel level = server.getLevel(dim);
		player.teleportTo(level, 
				pos.getX(), pos.getY(), pos.getZ(), 
				0, 0);
	}

	@Override
	public void onWin(MinecraftServer server) {
		// TODO 3.8.1 announce winning player
	}

}
