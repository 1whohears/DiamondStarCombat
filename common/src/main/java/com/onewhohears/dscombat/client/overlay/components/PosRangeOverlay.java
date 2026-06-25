package com.onewhohears.dscombat.client.overlay.components;

import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.client.input.DSCClientInputs;
import com.onewhohears.dscombat.client.overlay.VehicleOverlayComponent;
import com.onewhohears.dscombat.data.weapon.instance.WeaponInstance;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.util.UtilVehicleEntity;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class PosRangeOverlay extends VehicleOverlayComponent {

    @Override
    protected boolean shouldRender(Gui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        if (DSCClientInputs.getTargetMode() == DSCClientInputs.TargetMode.LOOK) return false;

        if (defaultRenderConditions()) return false;
        if (!(getPlayerRootVehicle() instanceof EntityVehicle vehicle)) return false;

        WeaponInstance<?> data = vehicle.weaponSystem.getSelected();
        if (data == null) return false;

        return data.getStats().isPosGuided();
    }

    @Override
    protected void render(Gui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        if (!(getPlayerRootVehicle() instanceof EntityVehicle vehicle)) return;

        WeaponInstance<?> data = vehicle.weaponSystem.getSelected();
        assert data != null;

        double range = data.getStats().getMobTurretRange();
        Vec3 pos = Config.CLIENT.getTargetPos();
        int dist = (int) pos.distanceTo(vehicle.position());
        int alt = UtilVehicleEntity.getDistFromSeaLevel(pos.y, vehicle.getWorld());
        String text = dist + " | " + alt;
        int color;
        if (dist <= range) {
            color = 0x00ff00;
            text += " | O";
        } else {
            color = 0xff0000;
            text += " | X";
        }

        graphics.drawCenteredString(FONT, text, screenWidth / 2, screenHeight / 2 - 20, color);
    }

    @Override
    protected @NotNull String componentId() {
        return "dscombat_pos_range";
    }

}
