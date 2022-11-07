package com.onewhohears.dscombat.item;

import java.util.function.Consumer;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.data.parts.WeaponPartData;
import com.onewhohears.dscombat.data.parts.WeaponRackData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.util.UtilMCText;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class ItemWeaponPart extends ItemPart {
	
	private final int num;
	
	public ItemWeaponPart(int num) {
		super(1);
		this.num = num;
	}
	
	@Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				return this.getCustomRenderer();
			}
		});
	}
	
	@Override
	public Component getName(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		String weapon = tag.getString("weaponId");
		MutableComponent name = UtilMCText.simpleText(getDescriptionId()).append(" ");
		if (weapon.isEmpty()) {
			name.append("EMPTY");
		} else {
			name.append(UtilMCText.simpleText("item."+DSCombatMod.MODID+"."+weapon))
				.append(" "+tag.getInt("ammo")+"/"+tag.getInt("max"));
		}
		return name;	
	}
	
	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (group.getId() == ModItems.PARTS.getId()) {
			if (num == 0) {
				ItemStack rack1 = new ItemStack(ModItems.TEST_MISSILE_RACK.get());
				rack1.setTag(new WeaponRackData(0.005f, 4, 4, "fox3_1", 
						WeaponPresets.TEST_MISSILE_RACK, ModItems.TEST_MISSILE_RACK.getId())
						.write());
				items.add(rack1);
				
				ItemStack rack2 = new ItemStack(ModItems.TEST_MISSILE_RACK.get());
				rack2.setTag(new WeaponRackData(0.005f, 12, 12, "fox2_1", 
						WeaponPresets.TEST_MISSILE_RACK, ModItems.TEST_MISSILE_RACK.getId())
						.write());
				items.add(rack2);
				
				ItemStack rack3 = new ItemStack(ModItems.TEST_MISSILE_RACK.get());
				rack3.setTag(new WeaponRackData(0.005f, 8, 8, "gbu", 
						WeaponPresets.TEST_MISSILE_RACK, ModItems.TEST_MISSILE_RACK.getId())
						.write());
				items.add(rack3);
			} else if (num == 1) {
				ItemStack part = new ItemStack(ModItems.TEST_BIG_GUN.get());
				part.setTag(new WeaponPartData(0.005f, 1000, 1000, "bullet_1", 
						WeaponPresets.TEST_BIG_GUN, ModItems.TEST_BIG_GUN.getId())
						.write());
				items.add(part);
			}
		}
	}

}
