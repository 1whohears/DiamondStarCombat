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
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;

public class PilotOverlay {
	
	private static final int stickBaseSize = 60;
	private static final int stickKnobSize = (int)(stickBaseSize/6);
	private static final int padding = 1;
	private static final int radarSize = 120, radarOffset = 8;
	private static final int fuelGuageHeight = 40, fuelGuageWidth = 60;
	private static final int fuelArrowHeight = 7, fuelArrowWidth = 24;
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
	
	private static final int attitudeSize = 80;
	private static final ResourceLocation ATTITUDE_BASE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/attitude_base.png");
	private static final ResourceLocation ATTITUDE_FRAME = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/attitude_frame.png");
	private static final ResourceLocation ATTITUDE_MID = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/attitude_mid.png");
	private static final ResourceLocation ATTITUDE_FRONT = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/attitude_front.png");
	
	private static final int turnCooSize = 80;
	private static final ResourceLocation TURN_COORD_BASE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/turn_coord_base.png");
	private static final ResourceLocation TURN_COORD_BALL = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/turn_coord_ball.png");
	private static final ResourceLocation TURN_COORD_NEEDLE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/turn_coord_needle.png");
	
	private static final int throttleRailLength = 60, throttleWidth = 10, throttleKnobHeight = 10;
	private static final ResourceLocation THROTTLE_RAIL = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/throttle_rail.png");
	private static final ResourceLocation THROTTLE_HANDLE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/throttle_handle.png");
	
	public static final IIngameOverlay HUD_Aircraft_Stats = ((gui, poseStack, partialTick, width, height) -> {
		Minecraft m = Minecraft.getInstance();
		if (m.options.hideGui) return;
		if (m.gameMode.getPlayerMode() == GameType.SPECTATOR) return;
		final var player = m.player;
		if (!(player.getRootVehicle() instanceof EntityAircraft plane)) return;
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		drawAircraftStats(m, player, plane, gui, poseStack, partialTick, width, height);
		drawAircraftAngles(m, player, plane, gui, poseStack, partialTick, width, height);
		drawAircraftWeaponsAndKeys(m, player, plane, gui, poseStack, partialTick, width, height);
		drawAircraftRadarData(m, player, plane, gui, poseStack, partialTick, width, height);
		drawAircraftControls(m, player, plane, gui, poseStack, partialTick, width, height);
		drawAircraftThrottle(m, player, plane, gui, poseStack, partialTick, width, height);
		drawAircraftFuel(m, player, plane, gui, poseStack, partialTick, width, height);
		if (plane.getAircraftType() == AircraftType.PLANE) {
			drawPlaneData(m, player, (EntityPlane)plane, gui, poseStack, partialTick, width, height);
			drawPlaneAttitude(m, player, (EntityPlane)plane, gui, poseStack, partialTick, width, height);
			drawPlaneTurnCoordinator(m, player, (EntityPlane)plane, gui, poseStack, partialTick, width, height);
		}
		if (player.getVehicle() instanceof EntityTurret turret)
			drawAircraftTurretData(m, player, turret, gui, poseStack, partialTick, width, height);
		if (Config.CLIENT.debugMode.get()) drawDebug(m, player, plane, gui, poseStack, partialTick, width, height);
	});
	
	private static void drawDebug(Minecraft m, Player player, EntityAircraft plane, ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height) {
		int color = 0x00ff00;
		GuiComponent.drawString(poseStack, m.font, 
			"V"+UtilParse.prettyVec3(plane.getDeltaMovement(), 2), width-100, 0, color);
		GuiComponent.drawString(poseStack, m.font, 
			"F"+UtilParse.prettyVec3(plane.forces, 2), width-100, 10, color);
		GuiComponent.drawString(poseStack, m.font, 
			"A"+UtilParse.prettyVec3(plane.getAngularVel(), 2), width-100, 20, color);
		GuiComponent.drawString(poseStack, m.font, 
			"M"+UtilParse.prettyVec3(plane.moment, 2), width-100, 30, color);
	}
	
	private static void drawAircraftStats(Minecraft m, Player player, EntityAircraft plane, ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height) {
		// TODO 0.2 redo plane speed 
		GuiComponent.drawString(poseStack, m.font, 
				"m/s: "+String.format("%3.1f", plane.getDeltaMovement().length()*20), 
				width-stickBaseSize-padding, 
	     		height-stickBaseSize-padding-stickKnobSize-padding-10, 
				0x00ff00);
		// TODO 0.2 redo distance from ground
		GuiComponent.drawString(poseStack, m.font, 
				"A: "+UtilEntity.getDistFromSeaLevel(plane), 
				width-stickBaseSize-padding, 
	     		height-stickBaseSize-padding-stickKnobSize-padding-20, 
				0x00ff00);
		// TODO 0.2 redo plane health
		float h = plane.getHealth(), max = plane.getMaxHealth();
		GuiComponent.drawString(poseStack, m.font, 
					"H: "+(int)h+"/"+(int)max, 
					width-stickBaseSize-padding, 
		     		height-stickBaseSize-padding-stickKnobSize-padding-30, 
					getHealthColor(h, max));
		// TODO 0.2 redo plane position
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
	
	private static final MutableComponent weaponSelect = new TextComponent("->");
	private static void drawAircraftWeaponsAndKeys(Minecraft m, Player player, EntityAircraft plane, ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height) {
		int wh=1,x=1,weaponSelectWidth=m.font.width(weaponSelect)+1,maxNameWidth=46,color1=0x7340bf,color2=0x00ff00,color=color1;
		// WEAPONS
		WeaponData sw = plane.weaponSystem.getSelected();
		if (sw != null) {
		List<WeaponData> weapons = plane.weaponSystem.getWeapons();
		Component[] names = new MutableComponent[weapons.size()];
		for (int i = 0; i < weapons.size(); ++i) {
			names[i] = weapons.get(i).getDisplayNameComponent();
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
			if (plane.inputs.flare) color = color2;
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
    		if (plane.inputs.special) color = color2;
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
    		if (plane.inputs.special2) color = color2;
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
    		if (plane.inputs.special) color = color2;
    		else color = color1;
    		GuiComponent.drawString(poseStack, m.font, 
    			"Hover("+text+")", 
    			x, wh, color);
    		wh += 10;
		}
		// PLAYERS ONLY
        if (plane.radarSystem.hasRadar()) {
        	x = 1+weaponSelectWidth;
        	if (plane.getRadarMode().isOff()) color = color2;
        	else color = color1;
        	GuiComponent.drawString(poseStack, m.font, 
        		"RMode("+DSCKeys.radarModeKey.getKey().getDisplayName().getString()+")", 
        		x, wh, color);
        	x += maxNameWidth;
        	text = plane.getRadarMode().name();
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
			RadarPing ping = pings.get(hover);
			String text = "("+(int)ping.pos.distanceTo(plane.position())
					+" | "+(int)ping.pos.y+")";
			GuiComponent.drawCenteredString(poseStack, m.font, 
				text, width/2, height/2-20, 0xffff00);
		}
		if (selected != -1 && selected < pings.size()) {
			RadarPing ping = pings.get(selected);
			String text = "("+(int)ping.pos.distanceTo(plane.position())
					+" | "+(int)ping.pos.y+")";
			int color = 0xff0000;
			if (ping.isFriendly) color = 0x0000ff;
			GuiComponent.drawCenteredString(poseStack, m.font, 
				text, cx, height-radarOffset-radarSize-20, color);
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
        // TODO 0.2 redo overlay control stick
		// pitch yaw input
        RenderSystem.setShaderTexture(0, STICK_BASE_CIRCLE);
        GuiComponent.blit(poseStack, 
        		width-stickBaseSize-padding, height-stickBaseSize-padding, 
        		0, 0, stickBaseSize, stickBaseSize, 
        		stickBaseSize, stickBaseSize);
        int b = stickBaseSize/2, n = stickKnobSize/2;
        RenderSystem.setShaderTexture(0, STICK_KNOB);
        GuiComponent.blit(poseStack, 
        		width-b-n-padding+(int)(plane.inputs.yaw*b), 
        		height-b-n-padding-(int)(plane.inputs.pitch*b), 
        		0, 0, stickKnobSize, stickKnobSize, 
        		stickKnobSize, stickKnobSize);
		// roll input
        RenderSystem.setShaderTexture(0, STICK_BASE_SQUARE);
        GuiComponent.blit(poseStack, 
        		width-stickBaseSize-padding, 
        		height-stickBaseSize-padding-stickKnobSize-padding, 
        		0, 0, stickBaseSize, stickKnobSize, 
        		stickBaseSize, stickBaseSize);
        RenderSystem.setShaderTexture(0, STICK_KNOB);
        GuiComponent.blit(poseStack, 
        		width-b-n-padding+(int)(plane.inputs.roll*b), 
        		height-stickKnobSize-padding-padding-stickBaseSize, 
        		0, 0, stickKnobSize, stickKnobSize, 
        		stickKnobSize, stickKnobSize);
	}
	
	private static void drawAircraftThrottle(Minecraft m, Player player, EntityAircraft plane, ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height) {
		int x = width-stickBaseSize-padding-throttleWidth-padding;
		int y = height-throttleRailLength-padding;
		RenderSystem.setShaderTexture(0, THROTTLE_RAIL);
        GuiComponent.blit(poseStack, 
        		x, y, 
        		0, 0, 
        		throttleWidth, throttleRailLength, 
        		throttleWidth, throttleRailLength);
        RenderSystem.setShaderTexture(0, THROTTLE_HANDLE);
        int sy = y+throttleRailLength-throttleKnobHeight;
        int l = throttleRailLength-throttleKnobHeight;
        float th = plane.getCurrentThrottle();
        if (plane.negativeThrottle) sy = sy-l/2-(int)(th*l/2);
        else sy = sy-(int)(th*l);
        GuiComponent.blit(poseStack, 
        		x, sy, 
        		0, 0, 
        		throttleWidth, throttleKnobHeight, 
        		throttleWidth, throttleKnobHeight);
	}
	
	private static void drawAircraftFuel(Minecraft m, Player player, EntityAircraft plane, ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height) {
		// TODO 0.2 redo plane fuel
		int x = width-stickBaseSize-stickKnobSize-3*padding-fuelGuageWidth;
		int y = height-fuelGuageHeight;
		RenderSystem.setShaderTexture(0, FUEL_GUAGE);
        GuiComponent.blit(poseStack, 
        	x, y, 
        	0, 0, 
        	fuelGuageWidth, fuelGuageHeight, 
        	fuelGuageWidth, fuelGuageHeight);
        RenderSystem.setShaderTexture(0, FUEL_GUAGE_ARROW);
        float max = plane.getMaxFuel(), r = 0;
        if (max != 0) r = plane.getCurrentFuel() / max;
        poseStack.pushPose();
        poseStack.translate(x+fuelGuageWidth/2, y+24, 0);
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(160f*r+10f));
        GuiComponent.blit(poseStack, 
        	-fuelArrowWidth+5, -fuelArrowHeight/2, 
        	0, 0, 
        	fuelArrowWidth, fuelArrowHeight, 
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
     		width-stickBaseSize-padding, 
     		height-stickBaseSize-padding-stickKnobSize-padding-40, 
     		0x00ff00);
	}
	
	private static void drawPlaneAttitude(Minecraft m, Player player, EntityPlane plane, ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height) {
		int attX = width-attitudeSize-padding, attY = padding;
		RenderSystem.setShaderTexture(0, ATTITUDE_BASE);
		GuiComponent.blit(poseStack, 
				attX, attY, 
	        	0, 0, 
	        	attitudeSize, attitudeSize, 
	        	attitudeSize, attitudeSize);
		RenderSystem.setShaderTexture(0, ATTITUDE_MID);
		poseStack.pushPose();
		poseStack.translate(attX+attitudeSize/2, attY+attitudeSize/2, 0);
		poseStack.mulPose(Vector3f.ZP.rotationDegrees(-plane.zRot));
		int p = (int)(Mth.clamp(-plane.getXRot(), -30, 30) * attitudeSize * 0.0055);
		poseStack.translate(0, p, 0);
		GuiComponent.blit(poseStack, 
				-attitudeSize/2, -attitudeSize/2, 
	        	0, 0, 
	        	attitudeSize, attitudeSize, 
	        	attitudeSize, attitudeSize);
		poseStack.popPose();
		RenderSystem.setShaderTexture(0, ATTITUDE_FRAME);
		GuiComponent.blit(poseStack, 
				attX, attY, 
	        	0, 0, 
	        	attitudeSize, attitudeSize, 
	        	attitudeSize, attitudeSize);
		RenderSystem.setShaderTexture(0, ATTITUDE_FRONT);
		GuiComponent.blit(poseStack, 
				attX, attY, 
	        	0, 0, 
	        	attitudeSize, attitudeSize, 
	        	attitudeSize, attitudeSize);
	}
	
	private static void drawPlaneTurnCoordinator(Minecraft m, Player player, EntityPlane plane, ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height) {
		int tcX = width-turnCooSize-padding, tcY = padding*2+attitudeSize;
		RenderSystem.setShaderTexture(0, TURN_COORD_BASE);
		GuiComponent.blit(poseStack, 
				tcX, tcY, 
	        	0, 0, 
	        	turnCooSize, turnCooSize, 
	        	turnCooSize, turnCooSize);
		RenderSystem.setShaderTexture(0, TURN_COORD_BALL);
		int move = (int)((plane.getCentripetalForce()-plane.getCentrifugalForce())*10);
		GuiComponent.blit(poseStack, 
				tcX+move, tcY, 
	        	0, 0, 
	        	turnCooSize, turnCooSize, 
	        	turnCooSize, turnCooSize);
		RenderSystem.setShaderTexture(0, TURN_COORD_NEEDLE);
		poseStack.pushPose();
		poseStack.translate(tcX+turnCooSize/2, tcY+turnCooSize/2, 0);
		float yawRate = plane.getYawRate()*20 / 40 * 30; // yawRate /  (indicator rate) * (indicator angle)
		poseStack.mulPose(Vector3f.ZP.rotationDegrees(yawRate));
		GuiComponent.blit(poseStack, 
				-turnCooSize/2, -turnCooSize/2, 
	        	0, 0, 
	        	turnCooSize, turnCooSize, 
	        	turnCooSize, turnCooSize);
		poseStack.popPose();
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
	
}
