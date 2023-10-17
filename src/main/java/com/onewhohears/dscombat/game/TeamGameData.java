package com.onewhohears.dscombat.game;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.scores.Team;

public class TeamGameData extends GameData {
	
	protected List<Team> teams = new ArrayList<Team>();
	
	protected TeamGameData(String id) {
		super(id);
	}
	
	@Override
	public void tickGame(MinecraftServer server) {
		super.tickGame(server);
	}

}
