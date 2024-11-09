package com.onewhohears.dscombat.client.screen;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.data.radar.RadarStats;
import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class VehicleRadarScreen extends VehicleSubScreen {

    public static final ResourceLocation BG_TEXTURE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/background/vehicle_radar_screen.png");

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
        // RADAR DISPLAY RANGE FIELD
        vertical_widget_shift = 34;
        COLUMNS = 4;
        EditBox rangeBox = new EditBox(getMinecraft().font, 0, 0, 20, 20, UtilMCText.empty());
        positionWidgetGrid(rangeBox, ROWS, COLUMNS, 3, 2);
        rangeBox.setValue(DSCClientInputs.getRadarDisplayRange()+"");
        rangeBox.setTextColor(0xFFFFFF);
        rangeBox.setResponder(onRadarDisplayRangeChange());
        // RADAR DISPLAY RANGE CYCLE
        positionWidgetGrid(new Button(0, 0, 20, 20,
                        UtilMCText.translatable("ui.dscombat.cycle_radar_display_range"),
                        onPress -> {
                            DSCClientInputs.cycleRadarDisplayRange();
                            rangeBox.setValue(DSCClientInputs.getRadarDisplayRange()+"");
                        }),
                ROWS, COLUMNS, 0, 2, 3);
    }

    private CycleButton.OnValueChange<RadarStats.RadarMode> onRadarModeCycle() {
        return (button, value) -> DSCClientInputs.setPreferredRadarMode(value);
    }

    private Consumer<String> onRadarDisplayRangeChange() {
        return range -> {
            try {
                double number = Double.parseDouble(range);
                DSCClientInputs.setRadarDisplayRange(number);
            } catch(NumberFormatException e) {
                DSCClientInputs.setRadarDisplayRange(250);
            }
        };
    }
}
