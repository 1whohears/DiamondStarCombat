package com.onewhohears.dscombat.data.weapon.stats;

import static com.onewhohears.dscombat.DSCombatMod.MODID;

import java.util.List;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetInstance;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetType;
import com.onewhohears.dscombat.data.weapon.AbstractWeaponBuilders;
import com.onewhohears.dscombat.data.weapon.WeaponType;
import com.onewhohears.dscombat.data.weapon.instance.BunkerBusterInstance;
import com.onewhohears.dscombat.util.UtilMCText;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

public class BunkerBusterStats extends BombStats {
	
	public static class Builder extends AbstractWeaponBuilders.BunkerBusterBuilder<Builder> {
		protected Builder(String namespace, String name) {
			super(namespace, name, WeaponType.BUNKER_BUSTER);
		}
		public static Builder bunkerBusterBuilder(String namespace, String name) {
			return new Builder(namespace, name);
		}
	}
	
	private final int blockStrength;
	
	public BunkerBusterStats(ResourceLocation key, JsonObject json) {
		super(key, json);
		blockStrength = json.get("blockStrength").getAsInt();
	}
	
	@Override
	public JsonPresetInstance<?> createPresetInstance() {
		return new BunkerBusterInstance<>(this);
	}
	
	public int getBlockStrength() {
		return blockStrength;
	}
	
	@Override
	public void addToolTips(List<Component> tips, boolean advanced) {
		super.addToolTips(tips, advanced);
		if (advanced) tips.add(UtilMCText.literal("Block Strength: ").append(getBlockStrength()+"").setStyle(Style.EMPTY.withColor(INFO_COLOR)));
	}
	
	@Override
	public String getWeaponTypeCode() {
		String code = "BB";
		if (isCausesFire()) code += "I";
		return code;
	}
	
	@Override
	public String getDefaultIconLocation() {
		return MODID+":textures/ui/weapon_icons/bunker_buster.png";
	}
	
	public boolean isBullet() {
		return false;
	}
	
	public boolean isAimAssist() {
		return false;
	}
	
	@Override
	public JsonPresetType getType() {
		return WeaponType.BUNKER_BUSTER;
	}

}
