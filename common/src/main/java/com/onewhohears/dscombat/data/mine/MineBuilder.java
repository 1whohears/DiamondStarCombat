package com.onewhohears.dscombat.data.mine;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.onewholibs.data.crafting.IngredientStackBuilder;

public class MineBuilder extends IngredientStackBuilder<MineBuilder> {
	
	public static MineBuilder create(String name) {
		return new MineBuilder(DSCombatMod.MODID, name, MineStats.MineType.INSTANCE);
	}
	
	protected MineBuilder(String namespace, String name, MineStats.MineType type) {
		super(namespace, name, type);
	}
	
	public MineBuilder setMineType(String mineType) {
		return setString("mineType", mineType);
	}
	
	public MineBuilder setDamage(float damage) {
		return setFloat("damage", damage);
	}
	
	public MineBuilder setRadius(float radius) {
		return setFloat("radius", radius);
	}
	
	public MineBuilder setArmTime(int armTime) {
		return setInt("armTime", armTime);
	}
	
	public MineBuilder setChainReaction(boolean chainReaction) {
		return setBoolean("chainReaction", chainReaction);
	}
}
