package com.onewhohears.dscombat.client.overlay;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.data.vehicle.physics.DSCPhyCons;
import com.onewhohears.dscombat.entity.vehicle.wind_tunnel.EntityWindTunnel;
import com.onewhohears.dscombat.util.UtilPrint;
import com.onewhohears.dscombat.util.UtilVehicleEntity;
import com.onewhohears.onewholibs.util.UtilEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class WindTunnelOverlay extends GuiComponent {

    static final long CHECK_TUNNEL_RATE = 2000;
    static long prevTunnelCheckTime = System.currentTimeMillis();
    static final int VALUE_OFFSET = 50;
    static final int LABEL_PADDING = 2;

    @Nullable
    static EntityWindTunnel tunnel;

    static void render(Gui gui, PoseStack stack, float partialTick, int screenWidth, int screenHeight) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        if (!player.hasPermissions(2)) return;
        if (System.currentTimeMillis() - prevTunnelCheckTime >= CHECK_TUNNEL_RATE)
            findTunnel(player.position(), UtilEntity.getLevel(player));
        if (tunnel == null) return;
        int index = 0;
        gui.getFont().draw(stack, tunnel.getStatsId(), LABEL_PADDING, LABEL_PADDING, 0x00ff00);

        gui.getFont().draw(stack, "Speed", LABEL_PADDING, LABEL_PADDING+10*(++index), 0x00ffff);
        gui.getFont().draw(stack, UtilPrint.printVec3SigFig(tunnel.getSpeed()), VALUE_OFFSET, LABEL_PADDING+10*index, 0x00ffff);

        gui.getFont().draw(stack, "Total", LABEL_PADDING, LABEL_PADDING+10*(++index), 0xffffff);
        gui.getFont().draw(stack, UtilPrint.printVec3SigFig(tunnel.totalAcc), VALUE_OFFSET, LABEL_PADDING+10*index, 0xffffff);

        gui.getFont().draw(stack, "Thrust", LABEL_PADDING, LABEL_PADDING+10*(++index), 0x0000ff);
        gui.getFont().draw(stack, UtilPrint.printVec3SigFig(tunnel.thrustAcc), VALUE_OFFSET, LABEL_PADDING+10*index, 0x0000ff);

        gui.getFont().draw(stack, "Drag", LABEL_PADDING, LABEL_PADDING+10*(++index), 0xff0000);
        gui.getFont().draw(stack, UtilPrint.printVec3SigFig(tunnel.dragAcc), VALUE_OFFSET, LABEL_PADDING+10*index, 0xff0000);

        gui.getFont().draw(stack, "Lift", LABEL_PADDING, LABEL_PADDING+10*(++index), 0x00ff00);
        gui.getFont().draw(stack, UtilPrint.printVec3SigFig(tunnel.liftAcc), VALUE_OFFSET, LABEL_PADDING+10*index, 0x00ff00);

        gui.getFont().draw(stack, "Weight", LABEL_PADDING, LABEL_PADDING+10*(++index), 0x000000);
        gui.getFont().draw(stack, UtilPrint.printVec3SigFig(tunnel.weightAcc), VALUE_OFFSET, LABEL_PADDING+10*index, 0x000000);

        gui.getFont().draw(stack, "RotAcc", LABEL_PADDING, LABEL_PADDING+10*(++index), 0x0000ff);
        gui.getFont().draw(stack, UtilPrint.printVec3SigFig(tunnel.rotAcc), VALUE_OFFSET, LABEL_PADDING+10*index, 0x0000ff);

        gui.getFont().draw(stack, "AOA", LABEL_PADDING, LABEL_PADDING+10*(++index), 0xff00ff);
        StringBuilder aoaStr = new StringBuilder();
        for (Float aoa : tunnel.aoas) aoaStr.append(UtilPrint.printDec(aoa)).append(" ");
        gui.getFont().draw(stack, aoaStr.toString(), VALUE_OFFSET, LABEL_PADDING+10*index, 0xff00ff);

        int color = 0x00ff00;
        if (tunnel.windCompAcc < 0) color = 0xff0000;
        gui.getFont().draw(stack, "WCA", LABEL_PADDING, LABEL_PADDING+10*(++index), color);
        gui.getFont().draw(stack, UtilPrint.printSigFig(tunnel.windCompAcc), VALUE_OFFSET, LABEL_PADDING+10*index, color);

        double Ny = tunnel.centripetalAcc / (DSCPhyCons.GRAVITY * DSCPhyCons.ACC_TIME_SCALE);
        gui.getFont().draw(stack, "Ny", LABEL_PADDING, LABEL_PADDING+10*(++index), 0xffff00);
        gui.getFont().draw(stack, UtilPrint.printSigFig(Ny), VALUE_OFFSET, LABEL_PADDING+10*index, 0xffff00);

        gui.getFont().draw(stack, "YawRate", LABEL_PADDING, LABEL_PADDING+10*(++index), 0xff00ff);
        gui.getFont().draw(stack, UtilPrint.printDec(tunnel.yawRate*20), VALUE_OFFSET, LABEL_PADDING+10*index, 0xff00ff);

        gui.getFont().draw(stack, "TurnRad", LABEL_PADDING, LABEL_PADDING+10*(++index), 0x00ffff);
        gui.getFont().draw(stack, UtilPrint.printDec(tunnel.turnRadius), VALUE_OFFSET, LABEL_PADDING+10*index, 0x00ffff);
    }

    static void findTunnel(Vec3 pos, Level level) {
        Optional<EntityWindTunnel> opt = UtilVehicleEntity.findWindTunnel(pos, level);
        tunnel = opt.orElse(null);
        prevTunnelCheckTime = System.currentTimeMillis();
    }

}
