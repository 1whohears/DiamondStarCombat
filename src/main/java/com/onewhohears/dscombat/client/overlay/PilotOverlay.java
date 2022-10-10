package com.onewhohears.dscombat.client.overlay;

import java.awt.Color;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.client.event.ClientForgeEvents;
import com.onewhohears.dscombat.data.RadarData.RadarPing;
import com.onewhohears.dscombat.data.RadarSystem;
import com.onewhohears.dscombat.data.WeaponData;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.entity.aircraft.parts.EntitySeat;
import com.onewhohears.dscombat.util.math.UtilGeometry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.common.ForgeMod;

public class PilotOverlay {
	
	public static final IGuiOverlay HUD_Aircraft_Stats = ((gui, poseStack, partialTick, width, height) -> {
		Minecraft m = Minecraft.getInstance();
		if (m.options.hideGui) return;
		if (m.gameMode.getPlayerMode() == GameType.SPECTATOR) return;
		final var player = m.player;
		if (player.getVehicle() instanceof EntitySeat seat 
				&& seat.getVehicle() instanceof EntityAbstractAircraft plane) {
			// plane speed
			int s = (int)(plane.getDeltaMovement().length() * 20d);
			GuiComponent.drawString(poseStack, m.font, 
					"m/s: "+s, 
					width/2+11, height-50, 0x00ff00);
			// distance from ground
			GuiComponent.drawString(poseStack, m.font, 
					"H: "+UtilGeometry.getDistFromGround(plane), 
					width/2+62, height-50, 0x00ff00);
			// plane health
			float h = plane.getHealth(), max = plane.getMaxHealth();
			GuiComponent.drawString(poseStack, m.font, 
						"Health: "+(int)h+"/"+(int)max, 
						width/2-90, height-60, getHealthColor(h, max));
			// plane position
			GuiComponent.drawString(poseStack, m.font, 
					"["+plane.getBlockX()+","+plane.getBlockY()+","+plane.getBlockZ()+"]", 
					width/2+11, height-60, 0x00ff00);
			// weapon data TODO not displaying on client sometimes
			WeaponData weapon = plane.weaponSystem.getSelected();
			if (weapon != null) {
				GuiComponent.drawString(poseStack, m.font, 
						"Weapon: "+weapon.getId()+" "+weapon.getCurrentAmmo()+"/"+weapon.getMaxAmmo(), 
						width/2-90, 5, 0x0000ff);
			}
			// target distance
			RadarSystem radar = plane.radarSystem;
			if (radar != null) {
				List<RadarPing> pings = radar.getClientRadarPings();
				if (pings != null && pings.size() != 0) {
					int selected = radar.getClientSelectedPingIndex();
					int hover = ClientForgeEvents.getHoverIndex();
					if (hover != -1 && hover < pings.size()) {
						GuiComponent.drawString(poseStack, m.font, 
								"Dist: "+(int)pings.get(hover).pos.distanceTo(plane.position()), 
								width/2-20, height/2-20, 0xffff00);
					}
					if (selected != -1 && selected < pings.size()) {
						GuiComponent.drawString(poseStack, m.font, 
								"Target Dist: "+(int)pings.get(selected).pos.distanceTo(plane.position()), 
								width/2-90, 15, 0xff0000);
					}
				}
			}
			Entity camera = m.getCameraEntity();
			m.setCameraEntity(player);
			if (gui.shouldDrawSurvivalElements()) {
				gui.setupOverlayRenderState(true, false);
				gui.renderHealth(width, height, poseStack);
				gui.renderFood(width, height, poseStack);
				renderArmor(width, height, poseStack, m, gui);
				renderAir(width, height, poseStack, m, gui);
			}
			m.setCameraEntity(camera);
		}
	});
	
	private static final Color green = new Color(0, 255, 0);
	private static final Color red = new Color(255, 0, 0);
	private static final float start = 0.6f;
	private static final float end = 0.1f;
	private static final float changeG = (float)green.getGreen() / (start-end);
	private static final float changeR = (float)red.getRed() / (start-end);
	
	private static int getHealthColor(float health, float max) {
		float r = health / max;
		if (r >= start) return green.getRGB();
		if (r < start && r > end) {
			return new Color(
				(int)(changeR*(start-r)), 
				(int)(changeG*(r-end)), 
				0).getRGB();
		}
		return red.getRGB();
	}
	
	// TODO ForgeGui.renderArmor is protected for some reason
	private static void renderArmor(int width, int height, PoseStack poseStack, Minecraft minecraft, ForgeGui gui) {
        minecraft.getProfiler().push("armor");

        RenderSystem.enableBlend();
        int left = width / 2 - 91;
        int top = height - gui.leftHeight;

        int level = minecraft.player.getArmorValue();
        for (int i = 1; level > 0 && i < 20; i += 2)
        {
            if (i < level)
            {
                gui.blit(poseStack, left, top, 34, 9, 9, 9);
            }
            else if (i == level)
            {
                gui.blit(poseStack, left, top, 25, 9, 9, 9);
            }
            else if (i > level)
            {
                gui.blit(poseStack, left, top, 16, 9, 9, 9);
            }
            left += 8;
        }
        gui.leftHeight += 10;

        RenderSystem.disableBlend();
        minecraft.getProfiler().pop();
    }
	
	// TODO ForgeGui.renderAir is protected for some reason
	private static void renderAir(int width, int height, PoseStack poseStack, Minecraft minecraft, ForgeGui gui) {
        minecraft.getProfiler().push("air");
        Player player = (Player) minecraft.getCameraEntity();
        //System.out.println("render air "+player);
        RenderSystem.enableBlend();
        int left = width / 2 + 91;
        int top = height - gui.rightHeight;

        int air = player.getAirSupply();
        if (player.isEyeInFluidType(ForgeMod.WATER_TYPE.get()) || air < 300)
        {
            int full = Mth.ceil((double) (air - 2) * 10.0D / 300.0D);
            int partial = Mth.ceil((double) air * 10.0D / 300.0D) - full;

            for (int i = 0; i < full + partial; ++i)
            {
                gui.blit(poseStack, left - i * 8 - 9, top, (i < full ? 16 : 25), 18, 9, 9);
            }
            gui.rightHeight += 10;
        }

        RenderSystem.disableBlend();
        minecraft.getProfiler().pop();
    }
	
}
