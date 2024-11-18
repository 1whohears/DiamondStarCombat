package com.onewhohears.dscombat.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.data.radar.RadarStats;
import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.UtilMCText;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class VehicleRadarScreen extends VehicleSubScreen {

    public static final ResourceLocation BG_TEXTURE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/background/vehicle_radar_screen.png");
    public static final ResourceLocation RADAR_BG = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/radar.png");
    public static final ResourceLocation RADAR_PING = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/hud_ping.png");

    private static final int imageWidth = 240, imageHeight = 180;
    private static final int textureSize = 256;

    protected VehicleRadarScreen() {
        super("screen.dscombat.vehicle_radar_screen",
                BG_TEXTURE, imageWidth, imageHeight, textureSize, textureSize);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        super.render(poseStack, mouseX, mouseY, partialTick);
        renderRadar(poseStack, mouseX, mouseY, partialTick);
    }

    protected void renderRadar(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, RADAR_BG);
        blit(poseStack, guiX+4, guiY+66, 110, 110, 0, 0,
                127, 128, 127, 128);
        EntityVehicle vehicle = getVehicle();
        RadarSystem radar = vehicle.radarSystem;
        List<RadarStats.RadarPing> pings = radar.getClientRadarPings();
        if (pings.isEmpty()) return;
        int selected = radar.getClientSelectedPingIndex();
        int hover = DSCClientInputs.getRadarHoverIndex();
        int centerX = guiX + 4 + 55, centerY = guiY + 66 + 55;
        for (int i = 0; i < pings.size(); ++i) {
            RadarStats.RadarPing ping = pings.get(i);
            Vec3 dp = ping.getPosForClient().subtract(vehicle.position());
            double dist = dp.horizontalDistance();
            double screen_dist = getScreenDistRatio(dist);
            if (screen_dist > 1) screen_dist = 1;
            float yaw = (UtilAngles.getYaw(dp)-vehicle.getYRot()+180)* Mth.DEG_TO_RAD;
            int x = Mth.clamp((int)(-Mth.sin(yaw)*55*screen_dist), -50, 50) + centerX;
            int y = Mth.clamp((int)(Mth.cos(yaw)*55*screen_dist), -50, 50) + centerY;
            drawPingAtPos(ping, x, y, i == selected, i == hover, poseStack);
        }
    }

    protected void drawPingAtPos(RadarStats.RadarPing ping, int x, int y, boolean selected, boolean hover,
                                 @NotNull PoseStack poseStack) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, RADAR_PING);
        blit(poseStack, x, y, 6, 6, 0, 0,
                200, 200, 200, 200);
    }

    protected double getScreenDistRatio(double distance) {
        return distance / DSCClientInputs.getRadarDisplayRange();
    }

    @Override
    protected void init() {
        vertical_widget_shift = 10;
        super.init();
        // RADAR MODE
        positionWidgetGrid(CycleButton.<RadarStats.RadarMode>builder(value -> UtilMCText.translatable(value.getTranslatable()))
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
