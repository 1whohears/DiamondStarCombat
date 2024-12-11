package com.onewhohears.dscombat.data.villager;

import java.util.List;

import com.onewhohears.dscombat.init.ModItems;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
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
		trades.get(1).add(new EmeraldForItem(ModItems.B_20MM.get(), 4, 1, 30, 4));
		trades.get(1).add(new EmeraldForItem(ModItems.B_50MMHE.get(), 1, 3, 30, 4));
		// Apprentice
		trades.get(2).add(new EmeraldForItem(ModItems.B_120MMHE.get(), 1, 8, 30, 6));
		trades.get(2).add(new EmeraldForAmmo(ModItems.BOMB.get(), "anm30", 4, 9, 20, 8));
		trades.get(2).add(new EmeraldForAmmo(ModItems.BOMB.get(), "anm57", 2, 9, 20, 8));
		trades.get(2).add(new EmeraldForAmmo(ModItems.BOMB.get(), "anm64", 1, 9, 20, 8));
		// Journeyman
		trades.get(3).add(new EmeraldForItem(ModItems.AGM114K.get(), 1, 15, 10, 12));
		trades.get(3).add(new EmeraldForItem(ModItems.AGM65L.get(), 1, 20, 10, 15));
		trades.get(3).add(new EmeraldForItem(ModItems.AIM9L.get(), 1, 31, 10, 21));
		// Expert
		trades.get(4).add(new EmeraldForItem(ModItems.AGM84E.get(), 1, 30, 10, 20));
		trades.get(4).add(new EmeraldForItem(ModItems.AIM9P5.get(), 1, 43, 10, 26));
		trades.get(4).add(new EmeraldForItem(ModItems.AIM7F.get(), 1, 38, 10, 22));
		// Master
		trades.get(5).add(new EmeraldForItem(ModItems.AGM88G.get(), 1, 50, 10, 24));
		trades.get(5).add(new EmeraldForItem(ModItems.AIM9X.get(), 1, 55, 10, 24));
		trades.get(5).add(new EmeraldForItem(ModItems.AIM120B.get(), 1, 70, 10, 24));
	}
	
	public static void putAircraftEngineerTrades(Int2ObjectMap<List<ItemListing>> trades) {
		// Novice
		trades.get(1).add(new EmeraldRangeForItem(ModItems.COCKPIT.get(), 5, 7, 4, 4));
		trades.get(1).add(new EmeraldRangeForItem(ModItems.LIGHT_FUEL_TANK.get(), 6, 8, 4, 4));
		trades.get(1).add(new EmeraldRangeForItem(ModItems.C6_ENGINE.get(), 10, 14, 4, 5));
		trades.get(1).add(new EmeraldRangeForItem(ModItems.CM_MANLY_52.get(), 8, 12, 4, 5));
		// Apprentice
		trades.get(2).add(new EmeraldRangeForItem(ModItems.WOODEN_PLANE.get(), 20, 24, 2, 10));
		trades.get(2).add(new EmeraldRangeForItem(ModItems.ORANGE_TESLA.get(), 16, 20, 2, 10));
		trades.get(2).add(new EmeraldRangeForItem(ModItems.HEAVY_FUEL_TANK.get(), 10, 12, 4, 8));
		trades.get(2).add(new EmeraldRangeForItem(ModItems.TURBOFAN_F25.get(), 14, 18, 4, 9));
		// Journeyman
		trades.get(3).add(new EmeraldRangeForItem(ModItems.ADVANCED_COCKPIT.get(), 6, 8, 4, 15));
		trades.get(3).add(new EmeraldRangeForItem(ModItems.TURBOFAN_F145.get(), 20, 24, 4, 16));
		trades.get(3).add(new EmeraldRangeForItem(ModItems.CIWS.get(), 28, 32, 2, 17));
		trades.get(3).add(new EmeraldRangeForItem(ModItems.STEVE_UP_SMASH.get(), 29, 33, 2, 18));
		// Expert
		trades.get(4).add(new EmeraldForAircraft(ModItems.FELIX_PLANE.get(), "felix_plane_unarmed", 78, 83, 2, 25));
		trades.get(4).add(new EmeraldForAircraft(ModItems.NOAH_CHOPPER.get(), "noah_chopper_unarmed", 50, 55, 2, 25));
		trades.get(4).add(new EmeraldRangeForItem(ModItems.SAM_LAUNCHER.get(), 40, 45, 2, 20));
		// Master
		trades.get(5).add(new EmeraldForAircraft(ModItems.JAVI_PLANE.get(), "javi_plane_unarmed", 101, 107, 2, 25));
		trades.get(5).add(new EmeraldForAircraft(ModItems.KRAIT_CHOPPER.get(), "krait_chopper_unarmed", 65, 70, 2, 25));
		trades.get(5).add(new EmeraldRangeForItem(ModItems.MRBUDGER_TANK.get(), 90, 96, 2, 25));
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
		public MerchantOffer getOffer(Entity trader, RandomSource random) {
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
		
		public int getCost(RandomSource random) {
			return cost;
		}
		
		public ItemStack getResult() {
			ItemStack stack = item.getDefaultInstance();
			stack.setCount(num);
			return stack;
		}

		public MerchantOffer getOffer(Entity trader, RandomSource random) {
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
			if (maxCost > 128) maxCost = 128;
			this.maxCost = maxCost;
		}
		
		@Override
		public int getCost(RandomSource random) {
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
