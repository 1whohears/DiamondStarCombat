package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.client.util.UtilClientPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemDecal extends Item implements VehicleInteractItem {

    public ItemDecal(Properties props) {
        super(props);
    }

    @Override
    public InteractionResult onServerInteract(EntityVehicle vehicle, ItemStack stack, Player player, InteractionHand hand) {
        // Server side: just signal client to open the screen
        // The actual screen open happens on client via the interact method in EntityVehicle
        return InteractionResult.SUCCESS;
    }

    /**
     * Called client-side from EntityVehicle.onItemInteract when player shift+right-clicks.
     * Opens the decal placement GUI.
     */
    public static void openDecalScreen(EntityVehicle vehicle) {
        UtilClientPacket.openDecalScreen(vehicle);
    }
}
