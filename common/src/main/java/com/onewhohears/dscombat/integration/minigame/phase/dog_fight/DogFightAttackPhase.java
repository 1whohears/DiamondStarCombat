package com.onewhohears.dscombat.integration.minigame.phase.dog_fight;

import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.integration.minigame.data.DogFightData;
import com.onewhohears.minigames.minigame.agent.PlayerAgent;
import com.onewhohears.minigames.minigame.phase.buyattackrounds.BuyAttackAttackPhase;
import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class DogFightAttackPhase extends BuyAttackAttackPhase<DogFightData> {

    public static final Style RED = Style.EMPTY.withBold(true).withColor(ChatFormatting.RED);

    public DogFightAttackPhase(DogFightData gameData) {
        super(gameData);
    }

    @Override
    public void tickPlayerAgent(MinecraftServer server, PlayerAgent agent) {
        super.tickPlayerAgent(server, agent);
        if (!agent.isDead()) checkDeath(server, agent);
    }

    protected void checkDeath(MinecraftServer server, PlayerAgent agent) {
        ServerPlayer sp = agent.getPlayer(server);
        if (sp == null) return;
        if (!sp.isPassenger()) {
            agent.onDeath(server, null);
            sp.sendSystemMessage(UtilMCText.literal("You are not in a vehicle. Considered Forfeit.")
                    .setStyle(RED));
            return;
        }
        if (!(sp.getRootVehicle() instanceof EntityVehicle vehicle)) {
            agent.onDeath(server, null);
            sp.sendSystemMessage(UtilMCText.literal("You are not in a vehicle. Considered Forfeit.")
                    .setStyle(RED));
            return;
        }
        if (!vehicle.isOperational()) {
            agent.onDeath(server, null);
            sp.sendSystemMessage(UtilMCText.literal("Your vehicle is not operational. You lost!")
                    .setStyle(RED));
            return;
        }
    }
}
