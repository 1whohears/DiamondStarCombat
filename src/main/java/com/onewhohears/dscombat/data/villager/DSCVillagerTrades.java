package com.onewhohears.dscombat.data.villager;

import java.util.List;
import java.util.Random;

import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.item.ItemPart;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;

public class DSCVillagerTrades {
	
	public static void putWeaponEngineerTrades(Int2ObjectMap<List<ItemListing>> trades) {
		// Novice
		trades.get(1).add(new ItemForEmerald(Items.COPPER_INGOT, 5, 1, 16, 2));
		trades.get(1).add(new ItemForEmerald(Items.GUNPOWDER, 4, 1, 16, 2));
		trades.get(1).add(new EmeraldForItem(ModItems.B_20MM.get(), 16, 2, 30, 4));
		trades.get(1).add(new EmeraldForItem(ModItems.B_50MMHE.get(), 8, 3, 30, 4));
		// Apprentice
		trades.get(2).add(new EmeraldForItem(ModItems.B_120MMHE.get(), 3, 2, 30, 6));
		trades.get(2).add(new EmeraldForAmmo(ModItems.BOMB.get(), "anm30", 4, 3, 20, 8));
		trades.get(2).add(new EmeraldForAmmo(ModItems.BOMB.get(), "anm57", 2, 3, 20, 8));
		trades.get(2).add(new EmeraldForAmmo(ModItems.BOMB.get(), "anm64", 1, 3, 20, 8));
		// Journeyman
		trades.get(3).add(new EmeraldForItem(ModItems.AGM114K.get(), 1, 3, 10, 11));
		trades.get(3).add(new EmeraldForItem(ModItems.AGM65L.get(), 1, 4, 10, 11));
		trades.get(3).add(new EmeraldForItem(ModItems.AIM9L.get(), 1, 6, 10, 14));
		// Expert
		trades.get(4).add(new EmeraldForItem(ModItems.AGM84E.get(), 1, 10, 10, 18));
		trades.get(4).add(new EmeraldForItem(ModItems.AIM9P5.get(), 1, 8, 10, 18));
		trades.get(4).add(new EmeraldForItem(ModItems.AIM7F.get(), 1, 12, 10, 18));
		// Master
		trades.get(5).add(new EmeraldForItem(ModItems.RIFEL1.get(), 1, 18, 10, 24));
		trades.get(5).add(new EmeraldForItem(ModItems.AIM9X.get(), 1, 12, 10, 24));
		trades.get(5).add(new EmeraldForItem(ModItems.AIM120B.get(), 1, 16, 10, 24));
	}
	
	public static void putAircraftEngineerTrades(Int2ObjectMap<List<ItemListing>> trades) {
		// Novice
		trades.get(1).add(new EmeraldRangeForItem(ModItems.COCKPIT.get(), 2, 4, 4, 4));
		trades.get(1).add(new EmeraldRangeForItem(ModItems.LIGHT_FUEL_TANK.get(), 3, 5, 4, 4));
		trades.get(1).add(new EmeraldRangeForItem(ModItems.C6_ENGINE.get(), 7, 11, 4, 5));
		trades.get(1).add(new EmeraldRangeForItem(ModItems.CM_MANLY_52.get(), 5, 9, 4, 5));
		// Apprentice
		trades.get(2).add(new EmeraldRangeForItem(ModItems.WOODEN_PLANE.get(), 13, 17, 2, 8));
		trades.get(2).add(new EmeraldRangeForItem(ModItems.ORANGE_TESLA.get(), 16, 20, 2, 9));
		trades.get(2).add(new EmeraldRangeForItem(ModItems.HEAVY_FUEL_TANK.get(), 5, 7, 4, 5));
		trades.get(2).add(new EmeraldRangeForItem(ModItems.TURBOFAN_F25.get(), 9, 13, 4, 6));
		// Journeyman
		trades.get(3).add(new EmeraldRangeForItem(ModItems.ADVANCED_COCKPIT.get(), 6, 8, 4, 7));
		trades.get(3).add(new EmeraldRangeForItem(ModItems.TURBOFAN_F145.get(), 13, 17, 4, 8));
		trades.get(3).add(new EmeraldRangeForItem(ModItems.MRBUDGER_TANK.get(), 26, 30, 2, 11));
		trades.get(3).add(new EmeraldRangeForItem(ModItems.STEVE_UP_SMASH.get(), 21, 25, 2, 10));
		// Expert
		trades.get(4).add(new EmeraldForAircraft(ModItems.JAVI_PLANE.get(), "javi_plane_unarmed", 31, 34, 2, 15));
		trades.get(4).add(new EmeraldForAircraft(ModItems.NOAH_CHOPPER.get(), "noah_chopper_unarmed", 31, 34, 2, 15));
		// Master
		trades.get(5).add(new EmeraldForAircraft(ModItems.ALEXIS_PLANE.get(), "alexis_plane_unarmed", 36, 40, 2, 20));
		trades.get(5).add(new EmeraldForAircraft(ModItems.AXCEL_TRUCK.get(), "axcel_truck", 29, 33, 2, 20));
	}
	
	public static class ItemForEmerald implements VillagerTrades.ItemListing {
		
		protected final Item item;
		protected final int num;
		protected final int value;
		protected final int maxUses;
		protected final int villagerXp;
		protected final float priceMultiplier;
		
		public ItemForEmerald(ItemLike item, int num, int value, int maxUses, int villagerXp) {
			this.item = item.asItem();
			this.num = num;
			if (value > 64) value = 64;
			else if (value < 1) value = 1;
			this.value = value;
			this.maxUses = maxUses;
			this.villagerXp = villagerXp;
			this.priceMultiplier = 0.05F;
		}

		@Override
		public MerchantOffer getOffer(Entity trader, Random random) {
			ItemStack costA = new ItemStack(item, num);
			ItemStack result = new ItemStack(Items.EMERALD, value);
			return new MerchantOffer(costA, result, 
					maxUses, villagerXp, priceMultiplier);
		}
		
	}
	
	public static class EmeraldForItem implements VillagerTrades.ItemListing {
		
		protected final Item item;
		protected final int num;
		protected final int cost;
		protected final int maxUses;
		protected final int villagerXp;
		protected final float priceMultiplier;

		public EmeraldForItem(ItemLike item, int num, int cost, int maxUses, int villagerXp) {
			this.item = item.asItem();
			this.num = num;
			if (cost > 128) cost = 128;
			else if (cost < 1) cost = 1;
			this.cost = cost;
			this.maxUses = maxUses;
			this.villagerXp = villagerXp;
			this.priceMultiplier = 0.05F;
		}
		
		public EmeraldForItem(ItemLike item, int cost, int maxUses, int villagerXp) {
			this(item, 1, cost, maxUses, villagerXp);
		}
		
		public int getCost(Random random) {
			return cost;
		}
		
		public ItemStack getResult() {
			ItemStack stack = new ItemStack(item, num);
			if (item instanceof ItemPart part) stack.setTag(part.getNbt());
			return stack;
		}

		public MerchantOffer getOffer(Entity trader, Random random) {
			ItemStack costA, costB;
			int emeralds = getCost(random);
			if (emeralds > 64) {
				costA = new ItemStack(Items.EMERALD, 64);
				costB = new ItemStack(Items.EMERALD, emeralds-64);
			} else {
				costA = new ItemStack(Items.EMERALD, emeralds);
				costB = ItemStack.EMPTY;
			}
			return new MerchantOffer(costA, costB, getResult(), 
					maxUses, villagerXp, priceMultiplier);
		}
		
	}
	
	public static class EmeraldForAmmo extends EmeraldForItem {

		protected final String weapon;
		
		public EmeraldForAmmo(ItemLike item, String weapon, int num, int cost, int maxUses, int villagerXp) {
			super(item, num, cost, maxUses, villagerXp);
			this.weapon = weapon;
		}
		
		@Override
		public ItemStack getResult() {
			ItemStack stack = super.getResult();
			CompoundTag nbt = stack.getOrCreateTag();
			nbt.putString("weapon", weapon);
			return stack;
		}
		
	}
	
	public static class EmeraldRangeForItem extends EmeraldForItem {
		
		protected final int maxCost;
		
		public EmeraldRangeForItem(ItemLike item, int minCost, int maxCost, int maxUses, int villagerXp) {
			super(item, 1, minCost, maxUses, villagerXp);
			this.maxCost = maxCost;
		}
		
		@Override
		public int getCost(Random random) {
			return Mth.nextInt(random, cost, maxCost);
		}
		
	}
	
	public static class EmeraldForAircraft extends EmeraldRangeForItem {
		
		protected final String preset;

		public EmeraldForAircraft(ItemLike item, String preset, int minCost, int maxCost, int maxUses, int villagerXp) {
			super(item, minCost, maxCost, maxUses, villagerXp);
			this.preset = preset;
		}
		
		@Override
		public ItemStack getResult() {
			ItemStack stack = super.getResult();
			CompoundTag nbt = stack.getOrCreateTag();
			nbt.putString("preset", preset);
			return stack;
		}
		
	}
	
}
