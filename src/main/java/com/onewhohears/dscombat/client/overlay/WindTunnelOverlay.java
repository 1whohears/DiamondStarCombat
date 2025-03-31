package com.onewhohears.dscombat.client.overlay;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.data.vehicle.physics.DSCPhyCons;
import com.onewhohears.dscombat.entity.vehicle.EntityWindTunnel;
import com.onewhohears.dscombat.util.UtilVehicleEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class WindTunnelOverlay extends GuiComponent {

    public static void register(RegisterGuiOverlaysEvent event) {
        event.registerBelowAll("dscombat_wind_tunnel", WindTunnelOverlay::render);
    }

    static final long CHECK_TUNNEL_RATE = 2000;
    static long prevTunnelCheckTime = System.currentTimeMillis();
    static final int VALUE_OFFSET = 50;
    static final int LABEL_PADDING = 2;

    @Nullable
    static EntityWindTunnel tunnel;

    static void render(ForgeGui gui, PoseStack stack, float partialTick, int screenWidth, int screenHeight) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        if (!player.hasPermissions(2)) return;
        if (System.currentTimeMillis() - prevTunnelCheckTime >= CHECK_TUNNEL_RATE)
            findTunnel(player.position(), player.getLevel());
        if (tunnel == null) return;
        int index = 0;
        gui.getFont().draw(stack, tunnel.getPresetId(), LABEL_PADDING, LABEL_PADDING, 0x00ff00);

        gui.getFont().draw(stack, "Speed", LABEL_PADDING, LABEL_PADDING+10*(++index), 0x00ffff);
        gui.getFont().draw(stack, printVec3SigFig(tunnel.getSpeed()), VALUE_OFFSET, LABEL_PADDING+10*index, 0x00ffff);

        gui.getFont().draw(stack, "Total", LABEL_PADDING, LABEL_PADDING+10*(++index), 0xffffff);
        gui.getFont().draw(stack, printVec3SigFig(tunnel.totalAcc), VALUE_OFFSET, LABEL_PADDING+10*index, 0xffffff);

        gui.getFont().draw(stack, "Thrust", LABEL_PADDING, LABEL_PADDING+10*(++index), 0x0000ff);
        gui.getFont().draw(stack, printVec3SigFig(tunnel.thrustAcc), VALUE_OFFSET, LABEL_PADDING+10*index, 0x0000ff);

        gui.getFont().draw(stack, "Drag", LABEL_PADDING, LABEL_PADDING+10*(++index), 0xff0000);
        gui.getFont().draw(stack, printVec3SigFig(tunnel.dragAcc), VALUE_OFFSET, LABEL_PADDING+10*index, 0xff0000);

        gui.getFont().draw(stack, "Lift", LABEL_PADDING, LABEL_PADDING+10*(++index), 0x00ff00);
        gui.getFont().draw(stack, printVec3SigFig(tunnel.liftAcc), VALUE_OFFSET, LABEL_PADDING+10*index, 0x00ff00);

        gui.getFont().draw(stack, "Weight", LABEL_PADDING, LABEL_PADDING+10*(++index), 0x000000);
        gui.getFont().draw(stack, printVec3SigFig(tunnel.weightAcc), VALUE_OFFSET, LABEL_PADDING+10*index, 0x000000);

        int color = 0x00ff00;
        if (tunnel.windCompAcc < 0) color = 0xff0000;
        gui.getFont().draw(stack, "WCA", LABEL_PADDING, LABEL_PADDING+10*(++index), color);
        gui.getFont().draw(stack, printSigFig(tunnel.windCompAcc), VALUE_OFFSET, LABEL_PADDING+10*index, color);

        double Ny = tunnel.centripetalAcc / (DSCPhyCons.GRAVITY * DSCPhyCons.ACC_TIME_SCALE);
        gui.getFont().draw(stack, "Ny", LABEL_PADDING, LABEL_PADDING+10*(++index), 0xffff00);
        gui.getFont().draw(stack, printSigFig(Ny), VALUE_OFFSET, LABEL_PADDING+10*index, 0xffff00);

        gui.getFont().draw(stack, "YawRate", LABEL_PADDING, LABEL_PADDING+10*(++index), 0xff00ff);
        gui.getFont().draw(stack, printDec(tunnel.yawRate*20), VALUE_OFFSET, LABEL_PADDING+10*index, 0xff00ff);

        gui.getFont().draw(stack, "TurnRad", LABEL_PADDING, LABEL_PADDING+10*(++index), 0x00ffff);
        gui.getFont().draw(stack, printDec(tunnel.turnRadius), VALUE_OFFSET, LABEL_PADDING+10*index, 0x00ffff);
    }

    static void findTunnel(Vec3 pos, Level level) {
        Optional<EntityWindTunnel> opt = UtilVehicleEntity.findWindTunnel(pos, level);
        tunnel = opt.orElse(null);
        prevTunnelCheckTime = System.currentTimeMillis();
    }

    public static String printVec3SigFig(Vec3 vec) {
        return printVec3SigFig(vec, 3);
    }

    public static String printVec3SigFig(Vec3 vec, int s) {
        String format = "[%."+s+"e,%."+s+"e,%."+s+"e]";
        return String.format(format, vec.x, vec.y, vec.z);
    }

    public static String printSigFig(double d) {
        return printSigFig(d, 3);
    }

    public static String printSigFig(double d, int s) {
        String format = "%."+s+"e";
        return String.format(format, d);
    }

    public static String printDec(double d) {
        return printDec(d, 2);
    }

    public static String printDec(double d, int s) {
        String format = "%."+s+"f";
        return String.format(format, d);
    }

}
