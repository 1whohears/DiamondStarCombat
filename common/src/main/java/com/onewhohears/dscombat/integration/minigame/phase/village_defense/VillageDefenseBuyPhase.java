package com.onewhohears.dscombat.integration.minigame.phase.village_defense;

import com.onewhohears.dscombat.common.network.toclient.ToClientSetTargetPos;
import com.onewhohears.dscombat.integration.minigame.data.VillageDefenseData;
import com.onewhohears.dscombat.integration.minigame.phase.general_dsc.DSCBuyPhase;
import com.onewhohears.minigames.entity.FlagEntity;
import com.onewhohears.minigames.minigame.agent.TeamAgent;
import com.onewhohears.minigames.minigame.phase.flag.KillFlagBuyPhase;
import net.minecraft.server.MinecraftServer;

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
        List<FlagEntity> flags = getGameData().getLivingFlags();
        if (flags.isEmpty()) return;
        FlagEntity flag = flags.getFirst();
        forEachPlayer(server, (data, agent) -> {
            if (agent.isTeam() || (agent.isPlayer() && !agent.isPlayerOnTeam())) {
                return data.isAttacker(agent.getId());
            } else if (agent.isPlayer()) {
                TeamAgent team = agent.getTeamAgent();
                if (team != null) return data.isAttacker(team.getId());
            }
            return false;
        }, (data, agent, player) -> {
            ToClientSetTargetPos.setTargetPos(flag.position(), player);
        });
    }
}
