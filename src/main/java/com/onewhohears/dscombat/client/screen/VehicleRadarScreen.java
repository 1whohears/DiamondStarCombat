package com.onewhohears.dscombat.client.screen;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.data.radar.RadarStats;
import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.resources.ResourceLocation;

public class VehicleRadarScreen extends VehicleSubScreen {

    public static final ResourceLocation BG_TEXTURE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/background/vehicle_weapons_screen.png");

    private static final int imageWidth = 240, imageHeight = 180;
    private static final int textureSize = 256;

    protected VehicleRadarScreen() {
        super("screen.dscombat.vehicle_radar_screen",
                BG_TEXTURE, imageWidth, imageHeight, textureSize, textureSize);
    }

    @Override
    protected void init() {
        vertical_widget_shift = 10;
        super.init();
        // RADAR MODE
        positionWidgetGrid(CycleButton.<RadarStats.RadarMode>builder(value -> UtilMCText.literal(value.name()))
                        .withValues(RadarStats.RadarMode.values())
                        .withInitialValue(DSCClientInputs.getPreferredRadarMode())
                        .create(0, 0, 20, 20,
                                UtilMCText.translatable("ui.dscombat.radar_mode"),
                                onRadarModeCycle()),
                ROWS, COLUMNS, 1, 2);
    }

    private CycleButton.OnValueChange<RadarStats.RadarMode> onRadarModeCycle() {
        return (button, value) -> DSCClientInputs.setPreferredRadarMode(value);
    }
}
