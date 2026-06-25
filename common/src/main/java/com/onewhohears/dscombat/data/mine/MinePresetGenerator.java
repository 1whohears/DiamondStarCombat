package com.onewhohears.dscombat.data.mine;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetGenerator;
import net.minecraft.data.PackOutput;

public class MinePresetGenerator extends JsonPresetGenerator<MineStats> {
	
	public static MinePresetGenerator INSTANCE;
	
	@Override
	protected void registerPresets() {
		// Anti-Personnel Mine
		addPresetToGenerate(MineBuilder.create("anti_personnel_mine")
				.setMineType("anti_personnel")
				.setDamage(20.0f)
				.setRadius(2.5f)
				.setArmTime(40)
				.setChainReaction(true)
				.build());
		
		// Anti-Tank Mine
		addPresetToGenerate(MineBuilder.create("anti_tank_mine")
				.setMineType("anti_tank")
				.setDamage(50.0f)
				.setRadius(3.5f)
				.setArmTime(60)
				.setChainReaction(true)
				.build());
		
		// Heavy Anti-Tank Mine (example of custom variant)
		addPresetToGenerate(MineBuilder.create("heavy_anti_tank_mine")
				.setMineType("anti_tank")
				.setDamage(100.0f)
				.setRadius(5.0f)
				.setArmTime(80)
				.setChainReaction(true)
				.build());
	}
	
	public MinePresetGenerator(PackOutput output) {
		super(output, "mines");
		INSTANCE = this;
	}
	
	@Override
	public String getName() {
		return "Mines: " + DSCombatMod.MODID;
	}
}
