package com.onewhohears.dscombat.client.screen;

import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.input.DSCKeys;
import com.onewhohears.dscombat.client.screen.key_bind.VehicleKeyBindsScreen;
import com.onewhohears.dscombat.data.sound.VehiclePassengerSoundPacks;
import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.resources.ResourceLocation;

public class VehicleOtherScreen extends VehicleSubScreen {

    public static final ResourceLocation BG_TEXTURE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/background/vehicle_other_screen.png");

    private static final int imageWidth = 240, imageHeight = 180;
    private static final int textureSize = 256;

    public VehicleOtherScreen() {
        super("screen.dscombat.vehicle_other_screen",
                BG_TEXTURE, imageWidth, imageHeight, textureSize, textureSize);
    }

    @Override
    protected void init() {
        super.init();
        // CUSTOM DISMOUNT
        positionWidgetGrid(new Checkbox(0, 0, 20, 20, UtilMCText.translatable("ui.dscombat.dismount_key",
                                   DSCKeys.dismount.getTranslatedKeyMessage()),
                                   Config.CLIENT.customDismount.get()) {
                @Override
                public void onPress() {
                    super.onPress();
                    Config.CLIENT.customDismount.set(selected());
                }
            },
                ROWS, COLUMNS, 1, 2);
        // PASSENGER SOUND PACK FIELD
        COLUMNS = 1;
        positionWidgetGrid(new CycleButton.Builder<String>(
                pack -> UtilMCText.translatable("passenger_sound_pack."+pack))
                .withValues(VehiclePassengerSoundPacks.get().getAllIds())
                .withInitialValue(Config.CLIENT.passengerSoundPack.get())
                .create(0, 0, 20, 20,
                        UtilMCText.translatable("ui.dscombat.passenger_sounds"),
                        onSoundPackCycle()), ROWS, COLUMNS, 1, 2);
        // KEYBINDS BUTTON
        positionWidgetGrid(new Button(0, 0, 20, 20,
                        UtilMCText.translatable("ui.dscombat.key_binds"),
                        onPress -> minecraft.setScreen(new VehicleKeyBindsScreen(0))),
                ROWS, COLUMNS, 2, 2);
    }

    private CycleButton.OnValueChange<String> onSoundPackCycle() {
        return (button, value) -> Config.CLIENT.passengerSoundPack.set(value);
    }
}
