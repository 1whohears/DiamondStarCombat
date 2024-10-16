package com.onewhohears.dscombat.util;

import com.onewhohears.dscombat.data.parts.PartPresets;
import com.onewhohears.dscombat.data.parts.instance.PartInstance;
import com.onewhohears.dscombat.data.parts.stats.PartStats;
import com.onewhohears.dscombat.data.radar.RadarPresets;
import com.onewhohears.dscombat.data.radar.RadarStats;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.data.weapon.instance.WeaponInstance;
import com.onewhohears.dscombat.item.ItemPart;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class UtilPresetParse {

    @Nullable
    public static PartStats getPartStatsFromItem(ItemStack stack) {
        if (stack.hasTag()) return getPartStatsFromCompound(stack.getTag());
        if (!(stack.getItem() instanceof ItemPart part)) return null;
        String presetId = part.getDefaultPartPresetId();
        return PartPresets.get().get(presetId);
    }

    @Nullable
    public static PartStats getPartStatsFromCompound(CompoundTag tag) {
        if (tag == null) return null;
        if (tag.isEmpty()) return null;
        String presetId = "";
        if (tag.contains("part")) presetId = tag.getString("part");
        else if (tag.contains("presetId")) presetId = tag.getString("presetId");
        else if (tag.contains("itemid")) {
            presetId = tag.getString("itemid");
            presetId = presetId.split(":")[1];
        }
        if (presetId.isEmpty()) return null;
        return PartPresets.get().get(presetId);
    }

    @Nullable
    public static PartInstance<?> parsePartFromItem(ItemStack stack, String defaultPartPresetId) {
        //System.out.println("parsePartFromItem = "+stack+" "+stack.getTag());
        if (stack.hasTag()) return parsePartFromCompound(stack.getTag());
        PartStats stats = PartPresets.get().get(defaultPartPresetId);
        if (stats == null) return null;
        return stats.createPartInstance();
    }

    @Nullable
    public static PartInstance<?> parsePartFromItem(ItemStack stack) {
        //System.out.println("parsePartFromItem = "+stack+" "+stack.getTag());
        if (stack.hasTag()) return parsePartFromCompound(stack.getTag());
        if (!(stack.getItem() instanceof ItemPart part)) return null;
        String presetId = part.getDefaultPartPresetId();
        PartStats stats = PartPresets.get().get(presetId);
        if (stats == null) return null;
        return stats.createPartInstance();
    }

    @Nullable
    public static PartInstance<?> parsePartFromCompound(CompoundTag tag) {
        //System.out.println("parsePartFromCompound tag = "+tag);
        PartStats stats = getPartStatsFromCompound(tag);
        if (stats == null) return null;
        if (tag.getBoolean("filled")) return stats.createFilledPartInstance(tag.getString("param"));
        PartInstance<?> data = stats.createPartInstance();
        boolean old = tag.getInt("parse_version") < 2;
        if (old || tag.getBoolean("readnbt")) data.readNBT(tag);
        if (tag.contains("param")) data.setParamNotFilled(tag.getString("param"));
        return data;
    }

    @Nullable
    public static WeaponInstance<?> parseWeaponFromCompound(CompoundTag tag) {
        return (WeaponInstance<?>) WeaponPresets.get().createInstanceFromNbt(tag);
    }

    @Nullable
    public static RadarStats parseRadarFromCompound(CompoundTag tag) {
        return RadarPresets.get().getFromNbt(tag);
    }
}
