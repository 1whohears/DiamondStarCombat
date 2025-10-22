
package com.onewhohears.dscombat.common.network.toserver;

import com.mojang.datafixers.util.Pair;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.crafting.WeaponPartRecipe;
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

import java.util.Optional;

public class ToServerCraftWeaponPart extends BaseC2SMessage {

	public final String recipeId;
	public final BlockPos pos;

	public ToServerCraftWeaponPart(ResourceLocation recipeId, BlockPos pos) {
		this.recipeId = recipeId.toString();
		this.pos = pos;
	}

	public ToServerCraftWeaponPart(FriendlyByteBuf buffer) {
		recipeId = buffer.readUtf();
		double x = buffer.readDouble();
		double y = buffer.readDouble();
		double z = buffer.readDouble();
		pos = UtilGeometry.toBlockPos(new Vec3(x, y, z));
	}

    @Override
    public MessageType getType() {
        return PacketHandler.C2S_CRAFT_WEAPON_PART;
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
			Optional<Pair<ResourceLocation, WeaponPartRecipe>> option = level.getRecipeManager().getRecipeFor(
					WeaponPartRecipe.Type.INSTANCE, player.getInventory(), level,
					new ResourceLocation(recipeId));
			if (option.isEmpty()) return;
			WeaponPartRecipe recipe = option.get().getSecond();
			UtilItem.handleInventoryRecipe(player, recipe, pos);
		});
	}

}
