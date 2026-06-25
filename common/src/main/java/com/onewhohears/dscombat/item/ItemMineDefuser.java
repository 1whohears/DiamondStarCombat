package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.entity.weapon.EntityMine;
import com.onewhohears.dscombat.init.ModCMTabs;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.onewholibs.client.model.obj.ObjEntityModels;
import com.onewhohears.onewholibs.item.ObjModelItem;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemMineDefuser extends Item implements ObjModelItem {
	
	@ExpectPlatform
	public static ItemMineDefuser create() {
		throw new AssertionError();
	}
	
	private static final double DEFUSE_RANGE = 3.0;
	
	public ItemMineDefuser() {
		super(new Item.Properties()
				.stacksTo(1)
				.durability(100)
				.arch$tab(ModCMTabs.DSC_ITEMS));
	}
	
	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		
		if (!level.isClientSide()) {
			// Find mines in front of player
			Vec3 eyePos = player.getEyePosition();
			Vec3 lookVec = player.getLookAngle();
			Vec3 endPos = eyePos.add(lookVec.scale(DEFUSE_RANGE));
			
			// Create search box
			AABB searchBox = new AABB(eyePos, endPos).inflate(1.0);
			List<EntityMine> mines = level.getEntitiesOfClass(EntityMine.class, searchBox);
			
			if (!mines.isEmpty()) {
				// Find closest mine
				EntityMine closestMine = null;
				double closestDist = Double.MAX_VALUE;
				
				for (EntityMine mine : mines) {
					double dist = mine.position().distanceTo(eyePos);
					if (dist < closestDist && dist <= DEFUSE_RANGE) {
						closestDist = dist;
						closestMine = mine;
					}
				}
				
				if (closestMine != null) {
					// Give player the mine item back
					EntityMine.MineType mineType = closestMine.getMineType();
					ItemStack mineItem = new ItemStack(
							mineType == EntityMine.MineType.ANTI_TANK ? 
									ModItems.ANTI_TANK_MINE.get() : 
									ModItems.ANTI_PERSONNEL_MINE.get()
					);
					
					if (!player.getInventory().add(mineItem)) {
						player.drop(mineItem, false);
					}
					
					// Remove mine
					closestMine.discard();
					
					// Damage tool
					stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
					
					return InteractionResultHolder.success(stack);
				}
			}
		}
		
		return InteractionResultHolder.pass(stack);
	}
	
	@Override
	public @NotNull String getObjModelId(@NotNull String preset) {
		return "mine_defuser";
	}
	
	@Override
	public @NotNull String getPreset(@NotNull ItemStack stack) {
		return "mine_defuser";
	}
	
	@Override
	public ObjEntityModels.@NotNull ModelOverrides getItemModelOverrides(@NotNull String preset) {
		return ObjEntityModels.NO_OVERRIDES;
	}
}
