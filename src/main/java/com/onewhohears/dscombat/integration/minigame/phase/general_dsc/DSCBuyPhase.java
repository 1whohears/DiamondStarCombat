package com.onewhohears.dscombat.integration.minigame.phase.general_dsc;

import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.minigames.minigame.condition.PhaseExitCondition;
import com.onewhohears.minigames.minigame.data.BuyAttackData;
import com.onewhohears.minigames.minigame.data.MiniGameData;
import com.onewhohears.minigames.minigame.phase.buyattackrounds.BuyAttackBuyPhase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class DSCBuyPhase<T extends BuyAttackData> extends BuyAttackBuyPhase<T> {

    public DSCBuyPhase(T gameData) {
        super(gameData);
    }

    @SafeVarargs
    public DSCBuyPhase(String id, T gameData, PhaseExitCondition<T>... exitConditions) {
        super(id, gameData, exitConditions);
    }

    @Override
    public void onStart(MinecraftServer server) {
        if (!getGameData().isFirstRound()) givePlayersTheirVehicle(server);
        super.onStart(server);
    }

    public void givePlayersTheirVehicle(MinecraftServer server) {
        givePlayersTheirVehicle(server, getGameData());
    }

    public static void givePlayersTheirVehicle(MinecraftServer server, MiniGameData data) {
        data.getAllPlayerAgents().forEach(agent -> {
            ServerPlayer sp = agent.getPlayer(server);
            if (sp == null || !sp.isPassenger() || !(sp.getRootVehicle() instanceof EntityVehicle vehicle)) return;
            if (!sp.equals(vehicle.getControllingPlayerOrBot())) return;
            vehicle.becomeItem(sp);
        });
    }
}
