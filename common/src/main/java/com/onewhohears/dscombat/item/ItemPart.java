package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.PartPresets;
import com.onewhohears.dscombat.data.parts.instance.PartInstance;
import com.onewhohears.dscombat.data.parts.stats.PartStats;
import com.onewhohears.dscombat.init.ModCMTabs;
import com.onewhohears.dscombat.util.UtilPresetParse;
import com.onewhohears.onewholibs.util.UtilItem;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemPart extends Item implements FillableItemCategory {

	@NotNull private String defaultPresetId = "";

    public ItemPart(Properties props, @NotNull String defaultPresetId) {
        super(props);
        this.defaultPresetId = defaultPresetId;
        FillableItemCategory.onInit(this, props);
    }

	public ItemPart(int stackSize, @NotNull String defaultPresetId) {
		this(partProps(stackSize), defaultPresetId);
	}

	public ItemPart(int stackSize) {
		this(partProps(stackSize), "");
	}
	
	public ItemPart(Properties props) {
        this(props, "");
	}
	
	public static Properties partProps(int stackSize) {
		return new Item.Properties().stacksTo(stackSize).arch$tab(ModCMTabs.PARTS);
	}
	
	public static Properties itemProps(int stackSize) {
		return new Item.Properties().stacksTo(stackSize).arch$tab(ModCMTabs.DSC_ITEMS);
	}
	
	@Override
	public void fillItemCategory(@NotNull List<ItemStack> items) {
		String itemId = UtilItem.getItemKeyString(this);
		for (int i = 0; i < PartPresets.get().getNum(); ++i) {
			PartStats stats = PartPresets.get().getAll()[i];
			if (!stats.getItemId().equals(itemId)) continue;
			fillItemCategory(stats, items);
		}
	}
	
	protected void fillItemCategory(PartStats stats, List<ItemStack> items) {
		items.add(stats.createFilledPartInstance("").getNewItemStack());
	}
	
	public ResourceKey<CreativeModeTab> getCreativeTab() {
		return ModCMTabs.PARTS.getKey();
	}

	/**
	 * DO NOT CALL BEFORE THE DATA GENERATORS HAVE BEEN RUN OR IT WILL BE NULL
	 */
	@Nullable
	public PartStats getDefaultPartStats() {
		return PartPresets.get().get(getDefaultPartPresetId());
	}
	
	public String getDefaultPartPresetId() {
		if (defaultPresetId.isEmpty()) defaultPresetId = toString();
		return defaultPresetId;
	}
	
	@Override
	public @NotNull ItemStack getDefaultInstance() {
		ItemStack stack = new ItemStack(this);
		PartStats stats = getDefaultPartStats();
		if (stats != null) stack.setTag(stats.createPartInstance().writeNBT());
		return stack;
	}
	
	@Override
	public @NotNull Component getName(@NotNull ItemStack stack) {
		PartInstance<?> instance = getPartInstance(stack);
		if (instance != null) return instance.getItemName();
		return super.getName(stack);
	}
	
	@Override
	public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tips,
								@NotNull TooltipFlag isAdvanced) {
		super.appendHoverText(stack, level, tips, isAdvanced);
		PartInstance<?> instance = getPartInstance(stack);
		if (instance != null) {
			instance.addToolTips(tips, isAdvanced);
			if (isAdvanced.isAdvanced()) {
				tips.add(ItemVehicle.formatTooltip("PartId", instance.getStatsId()));
			}
		}
	}

	@Nullable
	public PartInstance<?> getPartInstance(ItemStack stack) {
		return UtilPresetParse.parsePartFromItem(stack, getDefaultPartPresetId());
	}

	public @NotNull String getPreset(@NotNull ItemStack stack) {
		PartStats stats = UtilPresetParse.getPartStatsFromItem(stack);
		if (stats == null) return getDefaultPartPresetId();
		return stats.getId();
	}

}
