package com.onewhohears.dscombat.item;

import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.parts.EngineData;
import com.onewhohears.dscombat.data.parts.EngineData.EngineType;
import com.onewhohears.dscombat.data.parts.ExternalEngineData;
import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemEngine extends ItemPart {
	
	public final EngineType engineType;
	public final float thrust, heat, fuelRate;
	public final boolean external;
	
	public ItemEngine(EngineType engineType, float weight, float thrust, float heat, float fuelRate, boolean external, SlotType[] compatibleSlots) {
		super(1, weight, compatibleSlots);
		this.engineType = engineType;
		this.thrust = thrust;
		this.heat = heat;
		this.fuelRate = fuelRate;
		this.external = external;
	}

	@Override
	public PartData getPartData() {
		if (external) return new ExternalEngineData(engineType, weight, thrust, heat, fuelRate, ForgeRegistries.ITEMS.getKey(this), compatibleSlots);
		return new EngineData(engineType, weight, thrust, heat, fuelRate, ForgeRegistries.ITEMS.getKey(this), compatibleSlots);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tips, TooltipFlag isAdvanced) {
		super.appendHoverText(stack, level, tips, isAdvanced);
		tips.add(Component.literal("Thrust: "+thrust));
		tips.add(Component.literal("Fuel Rate: "+fuelRate));
	}

}
