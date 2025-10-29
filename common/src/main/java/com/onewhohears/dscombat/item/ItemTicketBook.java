package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.entity.ai.goal.MoveToPassengerSeatGoal;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.ModCMTabs;
import com.onewhohears.dscombat.init.ModTags;
import com.onewhohears.onewholibs.util.UtilEntity;
import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.UUID;

public class ItemTicketBook extends Item implements VehicleInteractItem {

    public static final int VEHICLE_SEARCH_RANGE = 32;

    public ItemTicketBook() {
        super(new Item.Properties().stacksTo(1).arch$tab(ModCMTabs.DSC_ITEMS));
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, @NotNull Player player,
                                                           @NotNull LivingEntity entity, @NotNull InteractionHand hand) {
        Level level = UtilEntity.getLevel(player);
        if (level.isClientSide()) return InteractionResult.PASS;
        if (entity.isPassenger())
            return sendError(player, "error.dscombat.entity_already_passenger");
        if (!entity.getType().is(ModTags.EntityTypes.TICKET_BOOKER) || !(entity instanceof PathfinderMob mob))
            return sendError(player, "error.dscombat.entity_cant_use_tickets");
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains("vehicle"))
            return sendError(player, "error.dscombat.ticket_not_linked_vehicle");
        UUID vehicleId = tag.getUUID("vehicle");
        List<EntityVehicle> vehicles = level.getEntitiesOfClass(EntityVehicle.class,
                player.getBoundingBox().inflate(VEHICLE_SEARCH_RANGE), vehicle -> vehicle.getUUID().equals(vehicleId));
        if (vehicles.isEmpty())
            return sendError(player, "error.dscombat.vehicle_not_found");
        EntityVehicle vehicle = vehicles.get(0);
        if (!vehicle.hasOpenPassengerSeat())
            return sendError(player, "error.dscombat.no_open_seats");
        mob.goalSelector.addGoal(30, new MoveToPassengerSeatGoal(mob, vehicle));
        player.displayClientMessage(UtilMCText.translatable("success.dscombat.ticket_book_assign")
                .setStyle(Style.EMPTY.withColor(0x00FF00)), true);
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult onServerInteract(EntityVehicle vehicle, ItemStack stack, Player player,
                                              InteractionHand hand) {
        if (!vehicle.hasPermission(player)) return InteractionResult.PASS;
        Component name = vehicle.getName();
        CompoundTag tag = stack.getOrCreateTag();
        tag.putUUID("vehicle", vehicle.getUUID());
        tag.putString("vehicle_name", name.getString());
        stack.setTag(tag);
        player.displayClientMessage(UtilMCText.translatable("success.dscombat.ticket_book_link", name)
                .setStyle(Style.EMPTY.withColor(0x00FF00)), true);
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains("vehicle");
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tips,
                                @NotNull TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tips, isAdvanced);
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains("vehicle_name")) return;
        String name = tag.getString("vehicle_name");
        tips.add(UtilMCText.translatable("info.dscombat.ticket_linked_to", name)
                .setStyle(Style.EMPTY.withColor(0xAAAAAA)));
    }

    public static InteractionResult sendError(Player player, String error) {
        player.displayClientMessage(UtilMCText.translatable(error)
                .setStyle(Style.EMPTY.withColor(0xFF0000)), true);
        return InteractionResult.FAIL;
    }
}
