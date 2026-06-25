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

import org.jetbrains.annotations.Nullable;

public class UtilPresetParse {

    @Nullable
    public static PartStats getPartStatsFromItem(ItemStack stack) {
        if (stack.hasTag()) return getPartStatsFromCompound(stack.getTag());
        if (!(stack.getItem() instanceof ItemPart part)) return null;
        return part.getDefaultPartStats();
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
        PartInstance<?> data;
        if (tag.getBoolean("filled")) {
            data = stats.createFilledPartInstance(tag.getString("param"));
        } else {
            data = stats.createPartInstance();
            boolean old = tag.getInt("parse_version") < 2;
            if (old || tag.getBoolean("readnbt")) data.readNBT(tag);
            if (tag.contains("param")) data.setParamNotFilled(tag.getString("param"));
        }
        // Load extra_weapons for turrets
        if (data instanceof com.onewhohears.dscombat.data.parts.instance.TurretInstance<?> turret
                && tag.contains("extra_weapons")) {
            java.util.List<com.onewhohears.dscombat.data.weapon.ExtraWeaponData> extra = new java.util.ArrayList<>();
            net.minecraft.nbt.ListTag list = tag.getList("extra_weapons", 10); // 10 = TAG_Compound
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    net.minecraft.nbt.CompoundTag weaponTag = list.getCompound(i);
                    String name = weaponTag.getString("name");
                    String weaponId = weaponTag.getString("weapon");
                    // Check if pos is stored as compound (from JSON) or as separate x,y,z (from NBT save)
                    net.minecraft.world.phys.Vec3 pos;
                    if (weaponTag.contains("pos")) {
                        // From JSON: pos is a compound with x, y, z
                        net.minecraft.nbt.CompoundTag posTag = weaponTag.getCompound("pos");
                        double x = posTag.getDouble("x");
                        double y = posTag.getDouble("y");
                        double z = posTag.getDouble("z");
                        pos = new net.minecraft.world.phys.Vec3(x, y, z);
                    } else {
                        // From NBT save: x, y, z are direct fields
                        double x = weaponTag.getDouble("x");
                        double y = weaponTag.getDouble("y");
                        double z = weaponTag.getDouble("z");
                        pos = new net.minecraft.world.phys.Vec3(x, y, z);
                    }
                    // Read linked_weapons
                    java.util.List<String> linkedWeapons = new java.util.ArrayList<>();
                    if (weaponTag.contains("linked_weapons")) {
                        net.minecraft.nbt.ListTag linkedList = weaponTag.getList("linked_weapons", 8); // 8 = TAG_String
                        for (int j = 0; j < linkedList.size(); j++) {
                            linkedWeapons.add(linkedList.getString(j));
                        }
                    }
                    extra.add(new com.onewhohears.dscombat.data.weapon.ExtraWeaponData(name, weaponId, pos, linkedWeapons));
                }
            } else {
                // Legacy support: try reading as string array
                net.minecraft.nbt.ListTag stringList = tag.getList("extra_weapons", 8);
                for (int i = 0; i < stringList.size(); i++) {
                    String weaponId = stringList.getString(i);
                    // Default position at origin and auto-generate name for legacy data
                    extra.add(new com.onewhohears.dscombat.data.weapon.ExtraWeaponData("extra_" + i, weaponId, net.minecraft.world.phys.Vec3.ZERO));
                }
            }
            turret.setExtraWeapons(extra);
        }
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
