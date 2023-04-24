package com.onewhohears.dscombat.client.overlay;

import java.awt.Color;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.event.forgebus.ClientInputEvents;
import com.onewhohears.dscombat.client.input.DSCKeys;
import com.onewhohears.dscombat.data.radar.RadarData.RadarPing;
import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.data.radar.RadarSystem.RWRWarning;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft.AircraftType;
import com.onewhohears.dscombat.entity.aircraft.EntityPlane;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.util.UtilEntity;
import com.onewhohears.dscombat.util.UtilParse;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;

public class PilotOverlay {
	
	private static final int stickBaseSize = 60;
	private static final int stickKnobSize = (int)(stickBaseSize/6);
	private static final int stickOffset = 1;
	private static final int radarSize = 120, radarOffset = 8;
	private static final int fuelGuageHeight = 60, fuelGuageWidth = 90;
	private static final int fuelArrowHeight = 10, fuelArrowWidth = 36;
	private static final ResourceLocation STICK_BASE_CIRCLE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/stickcanvascircle.png");
	private static final ResourceLocation STICK_BASE_SQUARE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/stickcanvassquare.png");
	private static final ResourceLocation STICK_KNOB = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/stickknob.png");
	private static final ResourceLocation FUEL_GUAGE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/fuel_guage.png");
	private static final ResourceLocation FUEL_GUAGE_ARROW = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/fuel_guage_arrow.png");
	private static final ResourceLocation RADAR = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/radar.png");
	
	public static final IIngameOverlay HUD_Aircraft_Stats = ((gui, poseStack, partialTick, width, height) -> {
		Minecraft m = Minecraft.getInstance();
		if (m.options.hideGui) return;
		if (m.gameMode.getPlayerMode() == GameType.SPECTATOR) return;
		final var player = m.player;
		if (!(player.getRootVehicle() instanceof EntityAircraft plane)) return;
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		drawMissingVanillaOverlays(m, player, plane, gui, poseStack, partialTick, width, height);
		drawAircraftStats(m, player, plane, gui, poseStack, partialTick, width, height);
		drawAircraftAngles(m, player, plane, gui, poseStack, partialTick, width, height);
		drawAircraftWeaponsAndKeys(m, player, plane, gui, poseStack, partialTick, width, height);
		drawAircraftRadarData(m, player, plane, gui, poseStack, partialTick, width, height);
		drawAircraftControls(m, player, plane, gui, poseStack, partialTick, width, height);
		drawAircraftThrottle(m, player, plane, gui, poseStack, partialTick, width, height);
		drawAircraftFuel(m, player, plane, gui, poseStack, partialTick, width, height);
		if (plane.getAircraftType() == AircraftType.PLANE) 
			drawPlaneData(m, player, (EntityPlane)plane, gui, poseStack, partialTick, width, height);
		if (player.getVehicle() instanceof EntityTurret turret)
			drawAircraftTurretData(m, player, turret, gui, poseStack, partialTick, width, height);
		if (Config.CLIENT.debugMode.get()) drawDebug(m, player, plane, gui, poseStack, partialTick, width, height);
	});
	
	private static void drawDebug(Minecraft m, Player player, EntityAircraft plane, ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height) {
		int color = 0x00ff00;
		GuiComponent.drawString(poseStack, m.font, 
			"V"+UtilParse.prettyVec3(plane.getDeltaMovement()), width-100, 0, color);
		GuiComponent.drawString(poseStack, m.font, 
			"F"+UtilParse.prettyVec3(plane.forces), width-100, 10, color);
		GuiComponent.drawString(poseStack, m.font, 
			"A"+UtilParse.prettyVec3(plane.getAngularVel()), width-100, 20, color);
		GuiComponent.drawString(poseStack, m.font, 
			"M"+UtilParse.prettyVec3(plane.moment), width-100, 30, color);
	}
	
	private static void drawMissingVanillaOverlays(Minecraft m, Player player, EntityAircraft plane, ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height) {
		Entity camera = m.getCameraEntity();
		m.setCameraEntity(player);
		//gui.setupOverlayRenderState(true, false);
		ForgeIngameGui.HOTBAR_ELEMENT.render(gui, poseStack, partialTick, width, height);
		if (gui.shouldDrawSurvivalElements()) {
			ForgeIngameGui.PLAYER_HEALTH_ELEMENT.render(gui, poseStack, partialTick, width, height);
			ForgeIngameGui.FOOD_LEVEL_ELEMENT.render(gui, poseStack, partialTick, width, height);
			ForgeIngameGui.ARMOR_LEVEL_ELEMENT.render(gui, poseStack, partialTick, width, height);
			ForgeIngameGui.AIR_LEVEL_ELEMENT.render(gui, poseStack, partialTick, width, height);
			//gui.renderHealth(width, height, poseStack);
			//gui.renderFood(width, height, poseStack);
			//renderArmor(width, height, poseStack, m, gui);
			//renderAir(width, height, poseStack, m, gui);
		}
		m.setCameraEntity(camera);
	}
	
	private static void drawAircraftStats(Minecraft m, Player player, EntityAircraft plane, ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height) {
		// plane speed 
		int s = (int)(plane.getDeltaMovement().length() * 20d);
		GuiComponent.drawString(poseStack, m.font, 
				"m/s: "+s, 
				width-stickBaseSize-stickOffset, 
	     		height-stickBaseSize-stickOffset-stickKnobSize-stickOffset-10, 
				0x00ff00);
		// distance from ground
		GuiComponent.drawString(poseStack, m.font, 
				"A: "+UtilEntity.getDistFromGround(plane), 
				width-stickBaseSize-stickOffset, 
	     		height-stickBaseSize-stickOffset-stickKnobSize-stickOffset-20, 
				0x00ff00);
		// plane health
		float h = plane.getHealth(), max = plane.getMaxHealth();
		GuiComponent.drawString(poseStack, m.font, 
					"H: "+(int)h+"/"+(int)max, 
					width-stickBaseSize-stickOffset, 
		     		height-stickBaseSize-stickOffset-stickKnobSize-stickOffset-30, 
					getHealthColor(h, max));
		// plane position
		GuiComponent.drawCenteredString(poseStack, m.font, 
				"["+plane.getBlockX()+","+plane.getBlockY()+","+plane.getBlockZ()+"]", 
				width/2, 0, 0x00ff00);
	}
	
	private static void drawAircraftAngles(Minecraft m, Player player, EntityAircraft plane, ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height) {
		// HEADING
		int y = 10, color = 0xe6e600;
        int heading = (int)Mth.wrapDegrees(player.getYRot());
        if (heading < 0) heading += 360f;
        GuiComponent.drawCenteredString(poseStack, m.font, 
        	heading+"", width/2, y+20, color);
        int num = 15, degSpace = 3, degPerLine = 3, steps = 3;
        for (int i = -num; i < num; ++i) {
        	int j = i*degPerLine;
        	int x = width/2+j*degSpace-heading%degPerLine*degSpace;
        	GuiComponent.drawCenteredString(poseStack, m.font, 
            	"|", x, y+10, color);
        	int hl = heading / degPerLine;
        	if ((hl+i) % steps != 0) continue;
        	int h = hl*degPerLine+j;
        	if (h < 0) h += 360;
        	else if (h >= 360) h-= 360;
        	GuiComponent.drawCenteredString(poseStack, m.font, 
        		textByHeading(h), x, y, color);
        }
	}
	
	private static String textByHeading(int h) {
    	if (h == 0) return "S";
    	else if (h == 180) return "N";
    	else if (h == 90) return "W";
    	else if (h == 270) return "E";
    	else if (h == 45) return "SW";
    	else if (h == 135) return "NW";
    	else if (h == 225) return "NE";
    	else if (h == 315) return "SE";
    	return h+"";
	}
	
	private static final MutableComponent weaponSelect = new TextComponent("->");
	private static void drawAircraftWeaponsAndKeys(Minecraft m, Player player, EntityAircraft plane, ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height) {
		int wh=1,x=1,weaponSelectWidth=m.font.width(weaponSelect)+1,maxNameWidth=46,color1=0x7340bf,color2=0x00ff00,color=color1;
		// WEAPONS
		WeaponData sw = plane.weaponSystem.getSelected();
		if (sw != null) {
		List<WeaponData> weapons = plane.weaponSystem.getWeapons();
		MutableComponent[] names = new MutableComponent[weapons.size()];
		for (int i = 0; i < weapons.size(); ++i) {
			names[i] = new TranslatableComponent("item."+DSCombatMod.MODID+"."+weapons.get(i).getId());
			int w = m.font.width(names[i]);
			if (w > maxNameWidth) maxNameWidth = w;
		}
		maxNameWidth += 4;
		for (int i = 0; i < weapons.size(); ++i) {
			x = 1; color = color1;
			WeaponData data = weapons.get(i);
			if (data.equals(sw)) {
				color = color2;
				GuiComponent.drawString(poseStack, m.font, 
					weaponSelect, x, wh, color);
			}
			x += weaponSelectWidth;
			GuiComponent.drawString(poseStack, m.font, 
					names[i], x, wh, color);
			x += maxNameWidth;
			GuiComponent.drawString(poseStack, m.font, 
					data.getCurrentAmmo()+"/"+data.getMaxAmmo(), 
					x, wh, color);
			wh += 10;
		} }
		// CONTROLS
		String text = "";
		if (maxNameWidth < 50) maxNameWidth = 50;
		// FLARES
		if (plane.hasFlares()) {
			x = 1+weaponSelectWidth;
			if (plane.inputFlare) color = color2;
			else color = color1;
			GuiComponent.drawString(poseStack, m.font, 
				"Flares("+DSCKeys.flareKey.getKey().getDisplayName().getString()+")", 
				x, wh, color);
			x += maxNameWidth;
			GuiComponent.drawString(poseStack, m.font, 
				plane.getFlareNum()+"", 
				x, wh, color);
			wh += 10;
		}
		// FREE LOOK
		x = 1+weaponSelectWidth;
		String key = DSCKeys.mouseModeKey.getKey().getDisplayName().getString();
		if (plane.isFreeLook()) {
			text = "MouseMode("+key+")";
			color = color1;
		} else {
			text = "FreeLook("+key+")";
			color = color2;
		}
		GuiComponent.drawString(poseStack, m.font, 
			text, x, wh, color);
		wh += 10;
		// LANDING GEAR
		if (plane.canToggleLandingGear()) {
			x = 1+weaponSelectWidth;
			if (plane.isLandingGear()) {
    			text = "OUT";
    			color = color2;
    		} else {
    			text = "IN";
    			color = color1;
    		}
    		GuiComponent.drawString(poseStack, m.font, 
    			"Gear("+DSCKeys.landingGear.getKey().getDisplayName().getString()+")", 
    			x, wh, color);
    		x += maxNameWidth;
    		GuiComponent.drawString(poseStack, m.font, 
    			text, x, wh, color);
    		wh += 10;
		}
		// BREAKS
    	if (plane.canBreak()) {
    		x = 1+weaponSelectWidth;
    		if (plane.getAircraftType() == AircraftType.PLANE) 
    			text = DSCKeys.special2Key.getKey().getDisplayName().getString();
    		else text = DSCKeys.specialKey.getKey().getDisplayName().getString();
    		if (plane.isBreaking()) color = color2;
    		else color = color1;
    		GuiComponent.drawString(poseStack, m.font, 
    			"Break("+text+")", 
    			x, wh, color);
    		wh += 10;
    	}
		// FLAPS DOWN
    	if (plane.canFlapsDown()) {
    		x = 1+weaponSelectWidth;
    		text = DSCKeys.specialKey.getKey().getDisplayName().getString();
    		if (plane.inputSpecial) color = color2;
    		else color = color1;
    		GuiComponent.drawString(poseStack, m.font, 
    			"FlapsDown("+text+")", 
    			x, wh, color);
    		wh += 10;
    	}
    	// WEAPON ANGLED DOWN
		if (plane.canAngleWeaponDown()) {
    		x = 1+weaponSelectWidth;
    		text = DSCKeys.special2Key.getKey().getDisplayName().getString();
    		if (plane.inputSpecial2) color = color2;
    		else color = color1;
    		GuiComponent.drawString(poseStack, m.font, 
    			"AimDown("+text+")", 
    			x, wh, color);
    		wh += 10;
		}
		// HOVER
		if (plane.canHover()) {
			x = 1+weaponSelectWidth;
    		text = DSCKeys.specialKey.getKey().getDisplayName().getString();
    		if (plane.inputSpecial) color = color2;
    		else color = color1;
    		GuiComponent.drawString(poseStack, m.font, 
    			"Hover("+text+")", 
    			x, wh, color);
    		wh += 10;
		}
		// PLAYERS ONLY
        if (plane.radarSystem.hasRadar()) {
        	x = 1+weaponSelectWidth;
        	if (plane.inputRadarMode) color = color2;
        	else color = color1;
        	GuiComponent.drawString(poseStack, m.font, 
        		"RMode("+DSCKeys.radarModeKey.getKey().getDisplayName().getString()+")", 
        		x, wh, color);
        	x += maxNameWidth;
        	if (plane.isRadarPlayersOnly()) text = "PLAYER";
        	else text = "ALL";
        	GuiComponent.drawString(poseStack, m.font, 
        		text, x, wh, color);
        	wh += 10;
        }
	}
	
	private static void drawAircraftRadarData(Minecraft m, Player player, EntityAircraft plane, ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height) {
		RadarSystem radar = plane.radarSystem;
		if (!radar.hasRadar()) return;
		RenderSystem.setShaderTexture(0, RADAR);
        GuiComponent.blit(poseStack, 
        		radarOffset, height-radarOffset-radarSize, 
        		0, 0, radarSize, radarSize, 
        		radarSize, radarSize);
        int radius = radarSize/2;
        int cx = radarOffset + radius;
        int cy = height-radarOffset-radius-5;
        double displayRange = 1000;
        // CARDINAL
        int card_color = 0x0000ff, radius2 = radius+4;
        float card_yaw = -plane.getYRot()*Mth.DEG_TO_RAD;
        GuiComponent.drawCenteredString(poseStack, m.font, "S", 
    			cx+(int)(Mth.sin(card_yaw)*radius2), 
    			cy-(int)(Mth.cos(card_yaw)*radius2), 
    			card_color);
        card_yaw = (180-plane.getYRot())*Mth.DEG_TO_RAD;
        GuiComponent.drawCenteredString(poseStack, m.font, "N", 
    			cx+(int)(Mth.sin(card_yaw)*radius2), 
    			cy-(int)(Mth.cos(card_yaw)*radius2), 
    			card_color);
        card_yaw = (270-plane.getYRot())*Mth.DEG_TO_RAD;
        GuiComponent.drawCenteredString(poseStack, m.font, "E", 
    			cx+(int)(Mth.sin(card_yaw)*radius2), 
    			cy-(int)(Mth.cos(card_yaw)*radius2), 
    			card_color);
        card_yaw = (90-plane.getYRot())*Mth.DEG_TO_RAD;
        GuiComponent.drawCenteredString(poseStack, m.font, "W", 
    			cx+(int)(Mth.sin(card_yaw)*radius2), 
    			cy-(int)(Mth.cos(card_yaw)*radius2), 
    			card_color);
        // ROLL PITCH YAW
        int heading = (int)plane.getYRot();
        if (heading < 0) heading += 360;
        int pitch = (int)-plane.getXRot();
        int roll = (int)plane.zRot;
        GuiComponent.drawCenteredString(poseStack, m.font, 
    		"P: "+pitch+" Y: "+heading+" R: "+roll, cx, height-radarOffset-radarSize-10, 0x8888ff);
        // RWR
        for (RWRWarning warn : radar.getClientRWRWarnings()) {
        	Vec3 dp = warn.pos.subtract(plane.position());
        	float yaw = (UtilAngles.getYaw(dp)-plane.getYRot())*Mth.DEG_TO_RAD;
        	int x = cx + (int)(Mth.sin(yaw)*radius);
        	int y = cy - (int)(Mth.cos(yaw)*radius);
        	int color = 0xffff00;
        	String symbol = "W";
        	if (warn.isMissile) {
        		color = 0xff0000;
        		symbol = "M";
        	}
        	GuiComponent.drawCenteredString(poseStack, m.font, 
        			symbol, x, y, color);
        }
		// PINGS
		List<RadarPing> pings = radar.getClientRadarPings();
		if (pings == null || pings.size() == 0) return;
		int selected = radar.getClientSelectedPingIndex();
		int hover = ClientInputEvents.getHoverIndex();
		if (hover != -1 && hover < pings.size()) {
			String text = "("+(int)pings.get(hover).pos.distanceTo(plane.position())
					+" | "+(int)pings.get(hover).pos.y+")";
			GuiComponent.drawCenteredString(poseStack, m.font, 
				text, width/2, height/2-20, 0xffff00);
		}
		if (selected != -1 && selected < pings.size()) {
			String text = "("+(int)pings.get(selected).pos.distanceTo(plane.position())
					+" | "+(int)pings.get(selected).pos.y+")";
			GuiComponent.drawCenteredString(poseStack, m.font, 
				text, cx, height-radarOffset-radarSize-20, 0xff0000);
		}
		for (int i = 0; i < pings.size(); ++i) {
			RadarPing ping = pings.get(i);
			Vec3 dp = ping.pos.subtract(plane.position());
			double dist = dp.multiply(1, 0, 1).length()/displayRange;
			if (dist > 1) dist = 1;
        	float yaw = (UtilAngles.getYaw(dp)-plane.getYRot())*Mth.DEG_TO_RAD;
        	int x = cx + (int)(Mth.sin(yaw)*radius*dist);
        	int y = cy + (int)(-Mth.cos(yaw)*radius*dist);
        	int color = 0x00ff00;
        	String symbol = "o";
        	if (i == selected) color = 0xff0000;
        	else if (i == hover) color = 0xffff00;
        	else if (ping.isFriendly) {
        		color = 0x0000ff;
        		symbol = "F";
        	}
        	GuiComponent.drawCenteredString(poseStack, m.font, 
        			symbol, x, y, color);
		}
	}
	
	private static void drawAircraftControls(Minecraft m, Player player, EntityAircraft plane, ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height) {
        // pitch yaw input
        RenderSystem.setShaderTexture(0, STICK_BASE_CIRCLE);
        GuiComponent.blit(poseStack, 
        		width-stickBaseSize-stickOffset, height-stickBaseSize-stickOffset, 
        		0, 0, stickBaseSize, stickBaseSize, 
        		stickBaseSize, stickBaseSize);
        int b = stickBaseSize/2, n = stickKnobSize/2;
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
	}
	
	private static void drawAircraftThrottle(Minecraft m, Player player, EntityAircraft plane, ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height) {
		RenderSystem.setShaderTexture(0, STICK_BASE_SQUARE);
        GuiComponent.blit(poseStack, 
        		width-stickBaseSize-stickOffset-stickKnobSize-stickOffset, 
        		height-stickBaseSize-stickOffset, 
        		0, 0, stickKnobSize, stickBaseSize, 
        		stickBaseSize, stickBaseSize);
        RenderSystem.setShaderTexture(0, STICK_KNOB);
        int sy, sb2 = stickBaseSize/2, n = stickKnobSize/2;
        float th = plane.getCurrentThrottle();
        if (plane.negativeThrottle) sy = height-n-stickOffset-sb2-(int)(th*sb2);
        else sy = height-n-stickOffset-(int)(th*stickBaseSize);
        GuiComponent.blit(poseStack, 
        		width-stickBaseSize-stickOffset-stickKnobSize-stickOffset, 
        		sy, 
        		0, 0, stickKnobSize, stickKnobSize, 
        		stickKnobSize, stickKnobSize);
	}
	
	private static void drawAircraftFuel(Minecraft m, Player player, EntityAircraft plane, ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height) {
		RenderSystem.setShaderTexture(0, FUEL_GUAGE);
        GuiComponent.blit(poseStack, 
        	width-stickBaseSize-stickKnobSize-3*stickOffset-fuelGuageWidth, 
        	height-fuelGuageHeight, 
        	0, 0, fuelGuageWidth, fuelGuageHeight, 
        	fuelGuageWidth, fuelGuageHeight);
        RenderSystem.setShaderTexture(0, FUEL_GUAGE_ARROW);
        float max = plane.getMaxFuel(), r = 0;
        if (max != 0) r = plane.getCurrentFuel() / max;
        poseStack.pushPose();
        poseStack.translate(width-stickBaseSize-stickKnobSize-3*stickOffset-fuelGuageWidth/2, 
        	height-18, 0);
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(160f*r+10f));
        GuiComponent.blit(poseStack, 
        	-fuelArrowWidth+5, -fuelArrowHeight/2, 
        	0, 0, fuelArrowWidth, fuelArrowHeight, 
        	fuelArrowWidth, fuelArrowHeight);
        poseStack.popPose();
	}
	
	private static void drawAircraftTurretData(Minecraft m, Player player, EntityTurret turret, ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height) {
		GuiComponent.drawString(poseStack, m.font, 
				"Turret: "+turret.getAmmo(), 
				width/2-100, 1, 0xffff00);
	}
	
	private static void drawPlaneData(Minecraft m, Player player, EntityPlane plane, ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height) {
		GuiComponent.drawString(poseStack, m.font, 
     		String.format("AOA: %3.1f", plane.getAOA()), 
     		width-stickBaseSize-stickOffset, 
     		height-stickBaseSize-stickOffset-stickKnobSize-stickOffset-40, 
     		0x00ff00);
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
	
	// ForgeIngameGui.renderArmor is protected for some reason
	private static void renderArmor(int width, int height, PoseStack poseStack, Minecraft minecraft, ForgeIngameGui gui) {
        minecraft.getProfiler().push("armor");

        RenderSystem.enableBlend();
        int left = width / 2 - 91;
        int top = height - gui.left_height;

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
        gui.left_height += 10;

        RenderSystem.disableBlend();
        minecraft.getProfiler().pop();
    }
	
	// ForgeIngameGui.renderAir is protected for some reason
	private static void renderAir(int width, int height, PoseStack poseStack, Minecraft minecraft, ForgeIngameGui gui) {
        minecraft.getProfiler().push("air");
        Player player = (Player) minecraft.getCameraEntity();
        //System.out.println("render air "+player);
        RenderSystem.enableBlend();
        int left = width / 2 + 91;
        int top = height - gui.right_height;

        int air = player.getAirSupply();
        if (player.isUnderWater() || air < 300)
        {
            int full = Mth.ceil((double) (air - 2) * 10.0D / 300.0D);
            int partial = Mth.ceil((double) air * 10.0D / 300.0D) - full;

            for (int i = 0; i < full + partial; ++i)
            {
                gui.blit(poseStack, left - i * 8 - 9, top, (i < full ? 16 : 25), 18, 9, 9);
            }
            gui.right_height += 10;
        }

        RenderSystem.disableBlend();
        minecraft.getProfiler().pop();
    }
	
}
