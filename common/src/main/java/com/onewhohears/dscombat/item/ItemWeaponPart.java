package com.onewhohears.dscombat.item;

import com.onewhohears.dscombat.data.parts.client.PartAssets;
import com.onewhohears.dscombat.data.parts.client.PartClientStats;
import com.onewhohears.dscombat.data.parts.instance.WeaponPartInstance;
import com.onewhohears.dscombat.data.parts.stats.PartStats;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.data.weapon.stats.WeaponStats;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.util.UtilPresetParse;
import com.onewhohears.onewholibs.client.model.obj.ObjEntityModels;
import com.onewhohears.onewholibs.item.ObjModelItem;
import com.onewhohears.onewholibs.util.UtilMCText;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemWeaponPart extends ItemPart implements ObjModelItem {

    @ExpectPlatform
    public static ItemWeaponPart create(int stackSize, String defaultPresetId) {
        throw new AssertionError();
    }

	public ItemWeaponPart(int stackSize, String defaultPresetId) {
		super(stackSize, defaultPresetId);
	}
	
	@Override
	public @NotNull Component getName(@NotNull ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		String weapon = tag.getString("weapon");
		MutableComponent name = ((MutableComponent)super.getName(stack)).append(" ");
		if (weapon.isEmpty()) {
			name.append("EMPTY");
		} else {
			WeaponStats wd = WeaponPresets.get().get(weapon);
			if (wd != null) {
				name.append(wd.getDisplayNameComponent()).append(" ")
					.append(UtilMCText.literal(wd.getWeaponTypeCode()));
			}
			else name.append(weapon+"?");
		}
		return name;	
	}
	
	@Override
	public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tips, @NotNull TooltipFlag isAdvanced) {
		super.appendHoverText(stack, level, tips, isAdvanced);
		WeaponPartInstance<?> data = (WeaponPartInstance<?>) UtilPresetParse.parsePartFromItem(stack);
		if (data == null) return;
		String id = data.getWeaponId();
		if (id.isEmpty()) return;
		tips.add(UtilMCText.translatable("info.dscombat.ammo")
				.append(": "+(int)data.getCurrentAmmo()+"/"+data.getStats().getMaxAmmo())
				.setStyle(Style.EMPTY.withColor(0xAAAAAA)));
		WeaponStats wd = WeaponPresets.get().get(id);
		if (wd == null) return;
		wd.addToolTips(tips, isAdvanced.isAdvanced());
	}
	
	@Override
	protected void fillItemCategory(PartStats stats, NonNullList<ItemStack> items) {
		List<String> list = WeaponPresets.get().getCompatibleWeapons(stats.getId());
        for (String s : list) addWeaponRack(stats, s, items);
	}
	
	private void addWeaponRack(PartStats stats, String preset, NonNullList<ItemStack> items) {
		ItemStack rack = new ItemStack(this);
		if (stats != null) rack.setTag(stats.createFilledPartInstance(preset).writeNBT());
		items.add(rack);
	}
	
	@Override
	public ResourceKey<CreativeModeTab> getCreativeTab() {
		return ModItems.WEAPON_PARTS;
	}

	@Override
	public @NotNull String getPreset(@NotNull ItemStack stack) {
		PartStats stats = UtilPresetParse.getPartStatsFromItem(stack);
		if (stats == null) return getDefaultPartPresetId();
		return stats.getId();
	}

	@Override
	public @NotNull String getObjModelId(@NotNull String preset) {
		PartClientStats<?> pcs = PartAssets.get().get(preset);
		if (pcs == null) return "";
		return pcs.getModelId();
	}

	@Override
	public ObjEntityModels.@NotNull ModelOverrides getItemModelOverrides(@NotNull String preset) {
		PartClientStats<?> pcs = PartAssets.get().get(preset);
		if (pcs == null) return ObjEntityModels.NO_OVERRIDES;
		return pcs.getItemModelOverrides();
	}
}
