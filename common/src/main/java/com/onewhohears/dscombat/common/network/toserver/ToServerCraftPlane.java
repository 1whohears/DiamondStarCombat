package com.onewhohears.dscombat.common.network.toserver;

import java.util.Optional;

import com.mojang.datafixers.util.Pair;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.crafting.VehicleRecipe;
import com.onewhohears.onewholibs.util.UtilEntity;
import com.onewhohears.onewholibs.util.UtilItem;

import com.onewhohears.onewholibs.util.math.UtilGeometry;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ToServerCraftPlane extends BaseC2SMessage {
	
	public final String recipeId;
	public final BlockPos pos;
	
	public ToServerCraftPlane(ResourceLocation recipeId, BlockPos pos) {
		this.recipeId = recipeId.toString();
		this.pos = pos;
	}
	
	public ToServerCraftPlane(FriendlyByteBuf buffer) {
		recipeId = buffer.readUtf();
		double x = buffer.readDouble();
		double y = buffer.readDouble();
		double z = buffer.readDouble();
		pos = UtilGeometry.toBlockPos(new Vec3(x, y, z));
	}
	
	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeUtf(recipeId);
		buffer.writeDouble(pos.getX());
		buffer.writeDouble(pos.getY());
		buffer.writeDouble(pos.getZ());
	}

	@Override
	public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            Player player = context.getPlayer();
            Level level = UtilEntity.getLevel(player);
            Optional<Pair<ResourceLocation, VehicleRecipe>> option = level.getRecipeManager().getRecipeFor(
                    VehicleRecipe.Type.INSTANCE, player.getInventory(), level,
                    new ResourceLocation(recipeId));
            if (option.isEmpty()) return;
            VehicleRecipe recipe = option.get().getSecond();
            UtilItem.handleInventoryRecipe(player, recipe, pos);
        });
	}

    @Override
    public MessageType getType() {
        return PacketHandler.C2S_CRAFT_PLANE;
    }
}
