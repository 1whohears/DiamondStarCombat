package com.onewhohears.dscombat.integration.minigame.phase.village_defense;

import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.integration.minigame.data.VillageDefenseData;
import com.onewhohears.minigames.minigame.agent.PlayerAgent;
import com.onewhohears.minigames.minigame.phase.flag.KillFlagAttackPhase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public class VillageDefenseAttackPhase extends KillFlagAttackPhase<VillageDefenseData> {

    public VillageDefenseAttackPhase(VillageDefenseData gameData) {
        super(gameData);
    }

    @Override
    protected boolean isPlayerOutsideFFRadius(MinecraftServer server, PlayerAgent agent, int ffRadiusSqr, Vec3 center) {
        ServerPlayer player = agent.getPlayer(server);
        if (player == null) return false;
        double distanceSqr = player.distanceToSqr(center);
        if (distanceSqr <= ffRadiusSqr) return false;
        if (!player.isPassenger()) return true;
        if (!(player.getRootVehicle() instanceof EntityVehicle vehicle)) return true;
        return !vehicle.isOperational() || vehicle.isOnGround();
    }
}
