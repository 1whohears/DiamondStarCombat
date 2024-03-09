package com.onewhohears.dscombat.item;

import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.parts.EngineData;
import com.onewhohears.dscombat.data.parts.EngineData.EngineType;
import com.onewhohears.dscombat.data.parts.ExternalEngineData;
import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.util.UtilItem;
import com.onewhohears.dscombat.util.UtilMCText;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ItemEngine extends ItemPart {
	
	public final EngineType engineType;
	public final float thrust, heat, fuelRate;
	public final boolean external;
	public final String externalEntityKey;
	
	public ItemEngine(EngineType engineType, float weight, float thrust, float heat, float fuelRate, boolean external, SlotType[] compatibleSlots) {
		super(8, weight, compatibleSlots);
		this.engineType = engineType;
		this.thrust = thrust;
		this.heat = heat;
		this.fuelRate = fuelRate;
		this.external = external;
		this.externalEntityKey = "";
	}
	
	public ItemEngine(EngineType engineType, float weight, float thrust, float heat, float fuelRate, boolean external, 
			SlotType[] compatibleSlots, String externalEntityKey) {
		super(8, weight, compatibleSlots);
		this.engineType = engineType;
		this.thrust = thrust;
		this.heat = heat;
		this.fuelRate = fuelRate;
		this.external = external;
		this.externalEntityKey = externalEntityKey;
	}

	@Override
	public PartData getPartData() {
		if (external) return new ExternalEngineData(engineType, weight, thrust, heat, fuelRate, 
				UtilItem.getItemKey(this), compatibleSlots, externalEntityKey);
		return new EngineData(engineType, weight, thrust, heat, fuelRate, UtilItem.getItemKey(this), compatibleSlots);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tips, TooltipFlag isAdvanced) {
		super.appendHoverText(stack, level, tips, isAdvanced);
		tips.add(UtilMCText.literal("Thrust: "+thrust).setStyle(Style.EMPTY.withColor(0xAAAAAA)));
		tips.add(UtilMCText.literal("Fuel L/M: "+String.format("%.1f", fuelRate*1200)).setStyle(Style.EMPTY.withColor(0xAAAAAA)));
	}

}
