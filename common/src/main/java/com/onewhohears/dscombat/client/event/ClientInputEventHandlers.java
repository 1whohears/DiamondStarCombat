package com.onewhohears.dscombat.client.event;

import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.client.input.ClientInputManager;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.common.core.PositionMarkerManager;
import com.onewhohears.dscombat.entity.parts.EntityRidablePart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class ClientInputEventHandlers {

    public static void clientTickPilotControl(Minecraft minecraft) {
        ClientInputManager.clientTickFirst();
        ClientInputManager.clientTickSecond();
    }

    public static void onClientPlayerJoin(LocalPlayer localPlayer) {
        DSCClientInputs.setPreferredRadarMode(Config.CLIENT.defaultRadarMode.get());
        ClientInputManager.loadKeyBinds();
        PositionMarkerManager.getClient().resetClient();
    }

    public static boolean isCancelShiftInput(Player player) {
        if (!Config.CLIENT.customDismount.get()) return false;
        return player.getVehicle() instanceof EntityRidablePart;
    }

    public static void onEntityDismountVehicle(Entity entity) {
        if (!entity.equals(Minecraft.getInstance().player)) return;
        DSCClientInputs.leanNot();
    }

    public static void onEntityMountVehicle(Entity entity) {
        if (!entity.equals(Minecraft.getInstance().player)) return;
        DSCClientInputs.setClientMountTime(System.currentTimeMillis());
    }
}
