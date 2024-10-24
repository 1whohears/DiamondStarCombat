package com.onewhohears.dscombat.client.screen;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toserver.ToServerOpenStorage;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;

public class VehicleStorageScreen extends ContainerScreen {

    private static int index = 0;
    private int maxIndex = 0;

    public VehicleStorageScreen(ChestMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
        // Back to Main Menu
        Button backButton = new Button(0, 0, 60, 20,
                UtilMCText.translatable("ui.dscombat.back"),
                onPress -> { getMinecraft().setScreen(new VehicleMainScreen()); });
        backButton.x = leftPos + 2;
        backButton.y = topPos - 20;
        addRenderableWidget(backButton);
        // Cycle Storage Left Button
        Button leftButton = new Button(0, 0, 20, 20,
                UtilMCText.literal("<-"), onPress -> {
                    --index;
                    fixIndex();
                    PacketHandler.INSTANCE.sendToServer(new ToServerOpenStorage(index));
                });
        leftButton.x = leftPos + 133;
        leftButton.y = topPos - 20;
        addRenderableWidget(leftButton);
        // Cycle Storage Right Button
        Button rightButton = new Button(0, 0, 20, 20,
                UtilMCText.literal("->"), onPress -> {
                    ++index;
                    fixIndex();
                    PacketHandler.INSTANCE.sendToServer(new ToServerOpenStorage(index));
                });
        rightButton.x = leftPos + 153;
        rightButton.y = topPos - 20;
        addRenderableWidget(rightButton);
    }

    @Override
    public void containerTick() {
        super.containerTick();
        Minecraft m = Minecraft.getInstance();
        if (m.player == null) {
            m.setScreen(null);
            return;
        }
        Entity rv = m.player.getRootVehicle();
        if (!(rv instanceof EntityVehicle plane)) {
            m.setScreen(null);
            return;
        }
        maxIndex = plane.partsManager.getStorageBoxesNum() - 1;
        Entity c = plane.getControllingPassenger();
        if (c == null || !c.equals(m.player)) {
            m.setScreen(null);
            return;
        }
    }

    private void fixIndex() {
        if (index < 0) index = maxIndex;
        else if (index > maxIndex) index = 0;
    }
}
