package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.entity.weapon.EntityMine;
import com.onewhohears.dscombat.init.ModCMTabs;
import com.onewhohears.onewholibs.client.model.obj.ObjEntityModels;
import com.onewhohears.onewholibs.item.ObjModelItem;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ItemMine extends Item implements ObjModelItem {
	
	@ExpectPlatform
	public static ItemMine create(EntityMine.MineType mineType) {
		throw new AssertionError();
	}
	
	private final EntityMine.MineType mineType;
	
	public ItemMine(EntityMine.MineType mineType) {
		super(new Item.Properties()
				.stacksTo(16)
				.arch$tab(ModCMTabs.DSC_ITEMS));
		this.mineType = mineType;
	}
	
	@Override
	public @NotNull InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		
		if (!level.isClientSide()) {
			BlockPos pos = context.getClickedPos();
			Direction face = context.getClickedFace();
			
			// Calculate placement position
			BlockPos placeBlockPos = face == Direction.UP ? pos.above() : pos;
			Vec3 placePos = new Vec3(
					placeBlockPos.getX() + 0.5,
					placeBlockPos.getY() + 0.5,
					placeBlockPos.getZ() + 0.5
			);
			
			// Check if there's already a mine at this block position
			java.util.List<EntityMine> existingMines = level.getEntitiesOfClass(
					EntityMine.class,
					new net.minecraft.world.phys.AABB(placeBlockPos)
			);
			
			if (!existingMines.isEmpty()) {
				// Already a mine here, don't place another
				return InteractionResult.FAIL;
			}
			
			EntityMine mine = new EntityMine(level, placePos, mineType);
			level.addFreshEntity(mine);
			
			// Consume item
			if (!context.getPlayer().isCreative()) {
				context.getItemInHand().shrink(1);
			}
			
			return InteractionResult.SUCCESS;
		}
		
		return InteractionResult.CONSUME;
	}
	
	@Override
	public @NotNull String getObjModelId(@NotNull String preset) {
		// Return model ID based on mine type
		return mineType == EntityMine.MineType.ANTI_TANK ? 
				"anti_tank_mine" : "anti_personnel_mine";
	}
	
	@Override
	public @NotNull String getPreset(@NotNull ItemStack stack) {
		// Mines don't have presets, just return the model ID
		return mineType == EntityMine.MineType.ANTI_TANK ? 
				"anti_tank_mine" : "anti_personnel_mine";
	}
	
	@Override
	public ObjEntityModels.@NotNull ModelOverrides getItemModelOverrides(@NotNull String preset) {
		return ObjEntityModels.NO_OVERRIDES;
	}
}
