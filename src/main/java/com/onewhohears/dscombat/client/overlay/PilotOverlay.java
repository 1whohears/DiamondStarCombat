package com.onewhohears.dscombat.client.overlay;

import java.awt.Color;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.event.forgebus.ClientInputEvents;
import com.onewhohears.dscombat.data.radar.RadarData.RadarPing;
import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.util.UtilEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.common.ForgeMod;

public class PilotOverlay {
	
	private static final int stickBaseSize = 60;
	private static final int stickKnobSize = (int)(stickBaseSize/6);
	private static final int stickOffset = 1;
	private static final ResourceLocation STICK_BASE_CIRCLE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/stickcanvascircle.png");
	private static final ResourceLocation STICK_BASE_SQUARE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/stickcanvassquare.png");
	private static final ResourceLocation STICK_KNOB = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/stickknob.png");
	private static final ResourceLocation FUEL = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/fuel.png");
	
	public static final IGuiOverlay HUD_Aircraft_Stats = ((gui, poseStack, partialTick, width, height) -> {
		Minecraft m = Minecraft.getInstance();
		if (m.options.hideGui) return;
		if (m.gameMode.getPlayerMode() == GameType.SPECTATOR) return;
		final var player = m.player;
		
		if (!(player.getRootVehicle() instanceof EntityAircraft plane)) return;
		drawAircraftStats(m, player, plane, gui, poseStack, partialTick, width, height);
		drawAircraftWeapons(m, player, plane, gui, poseStack, partialTick, width, height);
		// TODO display heading, pitch, and roll
		// TODO display direction missile is coming from
			// flares
			/*GuiComponent.drawString(poseStack, m.font, 
					"   Flares "+plane.getFlares(), 
					1, hieght, 0xffff00);*/
			// target distance
			RadarSystem radar = plane.radarSystem;
			if (radar != null) {
				List<RadarPing> pings = radar.getClientRadarPings();
				if (pings != null && pings.size() != 0) {
					int selected = radar.getClientSelectedPingIndex();
					int hover = ClientInputEvents.getHoverIndex();
					if (hover != -1 && hover < pings.size()) {
						GuiComponent.drawString(poseStack, m.font, 
								"Dist: "+(int)pings.get(hover).pos.distanceTo(plane.position()), 
								width/2-20, height/2-20, 0xffff00);
					}
					if (selected != -1 && selected < pings.size()) {
						GuiComponent.drawString(poseStack, m.font, 
								"Target Dist: "+(int)pings.get(selected).pos.distanceTo(plane.position()), 
								width/2-90, 1, 0xff0000);
					}
				}
			}
			// vanilla overlays
			Entity camera = m.getCameraEntity();
			m.setCameraEntity(player);
			gui.setupOverlayRenderState(true, false);
			gui.renderHotbar(partialTick, poseStack);
			if (gui.shouldDrawSurvivalElements()) {
				gui.renderHealth(width, height, poseStack);
				gui.renderFood(width, height, poseStack);
				renderArmor(width, height, poseStack, m, gui);
				renderAir(width, height, poseStack, m, gui);
			}
			m.setCameraEntity(camera);
			// textures
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
	        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
	        // pitch yaw input
	        RenderSystem.setShaderTexture(0, STICK_BASE_CIRCLE);
	        GuiComponent.blit(poseStack, 
	        		width-stickBaseSize-stickOffset, height-stickBaseSize-stickOffset, 
	        		0, 0, stickBaseSize, stickBaseSize, 
	        		stickBaseSize, stickBaseSize);
	        int b = stickBaseSize/2;
	        int n = stickKnobSize/2;
	        RenderSystem.setShaderTexture(0, STICK_KNOB);
	        GuiComponent.blit(poseStack, 
	        		width-b-n-stickOffset+(int)(plane.inputYaw*b), 
	        		height-b-n-stickOffset-(int)(plane.inputPitch*b), 
	        		0, 0, stickKnobSize, stickKnobSize, 
	        		stickKnobSize, stickKnobSize);
			// roll input
	        RenderSystem.setShaderTexture(0, STICK_BASE_SQUARE);
	        GuiComponent.blit(poseStack, 
	        		width-stickBaseSize-stickOffset, 
	        		height-stickBaseSize-stickOffset-stickKnobSize-stickOffset, 
	        		0, 0, stickBaseSize, stickKnobSize, 
	        		stickBaseSize, stickBaseSize);
	        RenderSystem.setShaderTexture(0, STICK_KNOB);
	        GuiComponent.blit(poseStack, 
	        		width-b-n-stickOffset+(int)(plane.inputRoll*b), 
	        		height-stickKnobSize-stickOffset-stickOffset-stickBaseSize, 
	        		0, 0, stickKnobSize, stickKnobSize, 
	        		stickKnobSize, stickKnobSize);
	        // aoa
	     	if (plane instanceof EntityPlane p) {
	     		GuiComponent.drawString(poseStack, m.font, 
	     			String.format("AOA: %3.3f", p.getAOA()), 
	     			width-stickBaseSize-stickOffset, 
	     			height-stickBaseSize-stickOffset-stickKnobSize-stickOffset-10, 
	     			0x00ff00);
	     	}
	        // throttle input
	        RenderSystem.setShaderTexture(0, STICK_BASE_SQUARE);
	        GuiComponent.blit(poseStack, 
	        		width-stickBaseSize-stickOffset-stickKnobSize-stickOffset, 
	        		height-stickBaseSize-stickOffset, 
	        		0, 0, stickKnobSize, stickBaseSize, 
	        		stickBaseSize, stickBaseSize);
	        RenderSystem.setShaderTexture(0, STICK_KNOB);
	        int sy;
	        int sb2 = stickBaseSize/2;
	        if (plane.negativeThrottle) sy = height-n-stickOffset-sb2-(int)(plane.getCurrentThrottle()*sb2);
	        else sy = height-n-stickOffset-(int)(plane.getCurrentThrottle()*stickBaseSize);
	        GuiComponent.blit(poseStack, 
	        		width-stickBaseSize-stickOffset-stickKnobSize-stickOffset, 
	        		sy, 
	        		0, 0, stickKnobSize, stickKnobSize, 
	        		stickKnobSize, stickKnobSize);
	        // fuel
	        RenderSystem.setShaderTexture(0, FUEL);
	        float r = plane.getFuel() / plane.getMaxFuel();
	        int fh = (int)(r * 128f);
	        int fh2 = 128-fh;
	        GuiComponent.blit(poseStack, width-16, fh2, 
	        		0, fh2, 
	        		16, fh, 
	        		16, 128);
	        // turret
	        if (player.getVehicle() instanceof EntityTurret turret) {
				GuiComponent.drawString(poseStack, m.font, 
						"Ammo: "+turret.getAmmo(), 
						width/2-90, 11, 0xffff00);
			}
	        // missile warning
	        if (plane.getMissileTrackedTicks() > 0) {
	        	GuiComponent.drawString(poseStack, m.font, 
						"MISSILE TRACKING YOU", 
						1, height-10, 0xff0000);
	        } else if (plane.getLockedOntoTicks() > 0) {
	        	GuiComponent.drawString(poseStack, m.font, 
						"RADAR TRACKING YOU", 
						1, height-10, 0xffff00);
	        }
	});
	
	private static void drawAircraftStats(Minecraft m, Player player, EntityAircraft plane, ForgeGui gui, PoseStack poseStack, float partialTick, int width, int height) {
		// plane speed
		int s = (int)(plane.getDeltaMovement().length() * 20d);
		GuiComponent.drawString(poseStack, m.font, 
				"m/s: "+s, 
				width/2+11, height-50, 0x00ff00);
		// distance from ground
		GuiComponent.drawString(poseStack, m.font, 
				"H: "+UtilEntity.getDistFromGround(plane), 
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
	}
	
	private static void drawAircraftWeapons(Minecraft m, Player player, EntityAircraft plane, ForgeGui gui, PoseStack poseStack, float partialTick, int width, int height) {
		int wh = 1;
		List<WeaponData> weapons = plane.weaponSystem.getWeapons();
		WeaponData sw = plane.weaponSystem.getSelected();
		if (sw != null) for (int i = 0; i < weapons.size(); ++i) {
			WeaponData data = weapons.get(i);
			MutableComponent c = Component.empty();
			if (data.equals(sw)) c.append("->");
			else c.append("   ");
			c.append(Component.translatable("item."+DSCombatMod.MODID+"."+data.getId()));
			c.append(" "+data.getCurrentAmmo()+"/"+data.getMaxAmmo());
			GuiComponent.drawString(poseStack, m.font, c, 
					1, wh, 0x0000ff);
			wh += 10;
		}
	}
	
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
	
	// ForgeGui.renderArmor is protected for some reason
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
	
	// ForgeGui.renderAir is protected for some reason
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
