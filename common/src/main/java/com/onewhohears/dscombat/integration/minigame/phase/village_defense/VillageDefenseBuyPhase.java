package com.onewhohears.dscombat.integration.minigame.phase.village_defense;

import com.onewhohears.dscombat.common.network.toclient.ToClientSetTargetPos;
import com.onewhohears.dscombat.integration.minigame.data.VillageDefenseData;
import com.onewhohears.dscombat.integration.minigame.phase.general_dsc.DSCBuyPhase;
import com.onewhohears.minigames.minigame.agent.GameAgent;
import com.onewhohears.minigames.minigame.agent.TeamAgent;
import com.onewhohears.minigames.minigame.phase.flag.KillFlagBuyPhase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class VillageDefenseBuyPhase extends KillFlagBuyPhase<VillageDefenseData> {

    public VillageDefenseBuyPhase(VillageDefenseData gameData) {
        super(gameData);
    }

    @Override
    public void onStart(MinecraftServer server) {
        if (!getGameData().isFirstRound()) DSCBuyPhase.givePlayersTheirVehicle(server, getGameData());
        super.onStart(server);
        setAttackerTargetPos(server);
    }

    protected void setAttackerTargetPos(MinecraftServer server) {
        List<GameAgent> defenders = getGameData().getDefenders();
        if (defenders.isEmpty()) return;
        GameAgent defender = defenders.get(0);
        Vec3 targetPos = defender.getRespawnPoint();
        forEachPlayer(server, (data, agent) -> {
            if (agent.isTeam() || (agent.isPlayer() && !agent.isPlayerOnTeam())) {
                return data.isAttacker(agent.getId());
            } else if (agent.isPlayer()) {
                TeamAgent team = agent.getTeamAgent();
                if (team != null) return data.isAttacker(team.getId());
            }
            return false;
        }, (data, agent, player) -> {
            ToClientSetTargetPos.setTargetPos(targetPos, player);
        });
    }
}
