package com.onewhohears.dscombat.data.mine;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.entity.weapon.EntityMine;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetStats;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;
import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.resources.ResourceLocation;

public class MineStats extends JsonPresetStats {
	
	private final EntityMine.MineType mineType;
	private final float damage;
	private final float radius;
	private final int armTime;
	private final boolean chainReaction;
	
	public MineStats(ResourceLocation key, JsonObject json) {
		super(key, json);
		
		String typeStr = UtilParse.getStringSafe(json, "mineType", "anti_personnel");
		this.mineType = typeStr.equalsIgnoreCase("anti_tank") ? 
				EntityMine.MineType.ANTI_TANK : EntityMine.MineType.ANTI_PERSONNEL;
		
		this.damage = UtilParse.getFloatSafe(json, "damage", mineType.getDamage());
		this.radius = UtilParse.getFloatSafe(json, "radius", mineType.getRadius());
		this.armTime = UtilParse.getIntSafe(json, "armTime", mineType.getArmTime());
		this.chainReaction = UtilParse.getBooleanSafe(json, "chainReaction", true);
	}

	@Override
	public JsonPresetType getType() {
		return MineType.INSTANCE;
	}
	
	@Override
	public com.onewhohears.onewholibs.data.jsonpreset.JsonPresetInstance<?> createPresetInstance() {
		return null; // Mines don't need instances, they use entity directly
	}
	
	public EntityMine.MineType getMineType() {
		return mineType;
	}
	
	public float getDamage() {
		return damage;
	}
	
	public float getRadius() {
		return radius;
	}
	
	public int getArmTime() {
		return armTime;
	}
	
	public boolean hasChainReaction() {
		return chainReaction;
	}
	
	public static class MineType extends JsonPresetType {
		public static final String ID = "mine";
		public static final MineType INSTANCE = new MineType();
		
		public MineType() {
			super(ID, MineStats::new);
		}
	}
}
