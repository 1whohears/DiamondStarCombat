package com.onewhohears.dscombat.data.weapon;

import static com.onewhohears.dscombat.DSCombatMod.MODID;

import java.util.List;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.jsonpreset.JsonPreset;
import com.onewhohears.dscombat.entity.weapon.EntityBunkerBuster;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.util.UtilMCText;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

public class BunkerBusterData extends BombData {
	
	public static class Builder extends AbstractWeaponBuilders.BunkerBusterBuilder<Builder> {
		
		protected Builder(String namespace, String name, JsonPresetFactory<? extends BunkerBusterData> sup) {
			super(namespace, name, sup, WeaponType.BUNKER_BUSTER);
		}
		
		public static Builder bunkerBusterBuilder(String namespace, String name) {
			return new Builder(namespace, name, (key, json) -> new BunkerBusterData(key, json));
		}
		
	}
	
	private final int blockStrength;
	
	public BunkerBusterData(ResourceLocation key, JsonObject json) {
		super(key, json);
		blockStrength = json.get("blockStrength").getAsInt();
	}
	
	public int getBlockStrength() {
		return blockStrength;
	}
	
	@Override
	public EntityWeapon getShootEntity(WeaponShootParameters params) {
		EntityBunkerBuster bomb = (EntityBunkerBuster) super.getShootEntity(params);
		if (bomb == null) return null;
		return bomb;
	}
	
	@Override
	public void readNBT(CompoundTag tag) {
		super.readNBT(tag);
	}
	
	@Override
	public CompoundTag writeNbt() {
		CompoundTag tag = super.writeNbt();
		return tag;
	}
	
	@Override
	public void readBuffer(FriendlyByteBuf buffer) {
		super.readBuffer(buffer);
	}
	
	@Override
	public void writeBuffer(FriendlyByteBuf buffer) {
		super.writeBuffer(buffer);
	}
	
	@Override
	public WeaponType getType() {
		return WeaponType.BUNKER_BUSTER;
	}
	
	@Override
	public <T extends JsonPreset> T copy() {
		return (T) new BunkerBusterData(getKey(), getJsonData());
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

}
