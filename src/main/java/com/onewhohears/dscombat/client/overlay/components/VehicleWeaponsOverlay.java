package com.onewhohears.dscombat.client.overlay.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.client.input.DSCKeys;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;

public class VehicleWeaponsOverlay extends VehicleOverlayComponent {
    private static final MutableComponent WEAPON_SELECT = Component.empty().append("->");
    
    public VehicleWeaponsOverlay(PoseStack poseStack, int screenWidth, int screenHeight) {
        super(poseStack, screenWidth, screenHeight);
    }

    @Override
    public void render(PoseStack poseStack, int screenWidth, int screenHeight) {
        if (!(getPlayerVehicle() instanceof EntityVehicle vehicle)) return;
        
        int yPos=1, xPos, weaponSelectWidth=getFont().width(WEAPON_SELECT)+1, maxNameWidth=46, maxTypeWidth=10;
        int color1=0x7340bf, color2=0x00ff00, color;
        // WEAPONS
        WeaponData selectedWeapon = vehicle.weaponSystem.getSelected();
        if (selectedWeapon != null) {
            List<WeaponData> weapons = vehicle.weaponSystem.getWeapons();
            Component[] names = new MutableComponent[weapons.size()];
            for (int i = 0; i < weapons.size(); ++i) {
                names[i] = weapons.get(i).getDisplayNameComponent();
                int w = getFont().width(names[i]);
                if (w > maxNameWidth) maxNameWidth = w;
            }
            maxNameWidth += 4;
            Component[] types = new MutableComponent[weapons.size()];
            for (int i = 0; i < weapons.size(); ++i) {
                types[i] = Component.literal(weapons.get(i).getWeaponTypeCode());
                int w = getFont().width(types[i]);
                if (w > maxTypeWidth) maxTypeWidth = w;
            }
            maxTypeWidth += 4;
            for (int i = 0; i < weapons.size(); ++i) {
                xPos = 1; color = color1;
                WeaponData data = weapons.get(i);
                if (data.equals(selectedWeapon)) {
                    color = color2;
                    drawString(poseStack, getFont(),
                            WEAPON_SELECT, xPos, yPos, color);
                }
                xPos += weaponSelectWidth;
                drawString(poseStack, getFont(),
                        names[i], xPos, yPos, color);
                xPos += maxNameWidth;
                drawString(poseStack, getFont(),
                        types[i], xPos, yPos, color);
                xPos += maxTypeWidth;
                drawString(poseStack, getFont(),
                        data.getCurrentAmmo()+"/"+data.getMaxAmmo(),
                        xPos, yPos, color);
                yPos += 10;
            } }
        // CONTROLS
        String text;
        if (maxNameWidth < 50) maxNameWidth = 50;
        // FLARES
        if (vehicle.hasFlares()) {
            xPos = 1+weaponSelectWidth;
            if (vehicle.inputs.flare) color = color2;
            else color = color1;
            drawString(poseStack, getFont(),
                    "Flares("+ DSCKeys.flareKey.getKey().getDisplayName().getString()+")",
                    xPos, yPos, color);
            xPos += maxNameWidth;
            drawString(poseStack, getFont(),
                    vehicle.getFlareNum()+"",
                    xPos, yPos, color);
            yPos += 10;
        }
        // FREE LOOK
        xPos = 1+weaponSelectWidth;
        String key = DSCKeys.mouseModeKey.getKey().getDisplayName().getString();
        if (vehicle.isFreeLook()) {
            text = "MouseMode("+key+")";
            color = color1;
        } else {
            text = "FreeLook("+key+")";
            color = color2;
        }
        drawString(poseStack, getFont(),
                text, xPos, yPos, color);
        yPos += 10;
        // LANDING GEAR
        if (vehicle.canToggleLandingGear()) {
            xPos = 1+weaponSelectWidth;
            if (vehicle.isLandingGear()) {
                text = "OUT";
                color = color2;
            } else {
                text = "IN";
                color = color1;
            }
            drawString(poseStack, getFont(),
                    "Gear("+DSCKeys.landingGear.getKey().getDisplayName().getString()+")",
                    xPos, yPos, color);
            xPos += maxNameWidth;
            drawString(poseStack, getFont(),
                    text, xPos, yPos, color);
            yPos += 10;
        }
        // BREAKS
        if (vehicle.canBreak()) {
            xPos = 1+weaponSelectWidth;
            if (vehicle.getAircraftType() == EntityVehicle.AircraftType.PLANE)
                text = DSCKeys.special2Key.getKey().getDisplayName().getString();
            else text = DSCKeys.specialKey.getKey().getDisplayName().getString();
            if (vehicle.isBreaking()) color = color2;
            else color = color1;
            drawString(poseStack, getFont(),
                    "Break("+text+")",
                    xPos, yPos, color);
            yPos += 10;
        }
        // FLAPS DOWN
        if (vehicle.canFlapsDown()) {
            xPos = 1+weaponSelectWidth;
            text = DSCKeys.specialKey.getKey().getDisplayName().getString();
            if (vehicle.inputs.special) color = color2;
            else color = color1;
            drawString(poseStack, getFont(),
                    "FlapsDown("+text+")",
                    xPos, yPos, color);
            yPos += 10;
        }
        // WEAPON ANGLED DOWN
        if (vehicle.canAngleWeaponDown()) {
            xPos = 1+weaponSelectWidth;
            text = DSCKeys.special2Key.getKey().getDisplayName().getString();
            if (vehicle.inputs.special2) color = color2;
            else color = color1;
            drawString(poseStack, getFont(),
                    "AimDown("+text+")",
                    xPos, yPos, color);
            yPos += 10;
        }
        // HOVER
        if (vehicle.canHover()) {
            xPos = 1+weaponSelectWidth;
            text = DSCKeys.specialKey.getKey().getDisplayName().getString();
            if (vehicle.inputs.special) color = color2;
            else color = color1;
            drawString(poseStack, getFont(),
                    "Hover("+text+")",
                    xPos, yPos, color);
            yPos += 10;
        }
        // PLAYERS ONLY
        if (vehicle.radarSystem.hasRadar()) {
            xPos = 1+weaponSelectWidth;
            if (vehicle.getRadarMode().isOff()) color = color2;
            else color = color1;
            drawString(poseStack, getFont(),
                    "RMode("+DSCKeys.radarModeKey.getKey().getDisplayName().getString()+")",
                    xPos, yPos, color);
            xPos += maxNameWidth;
            text = vehicle.getRadarMode().name();
            drawString(poseStack, getFont(),
                    text, xPos, yPos, color);
            // what is this
            yPos += 10;
        }
    }
}
