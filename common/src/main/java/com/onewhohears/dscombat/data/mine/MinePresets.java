package com.onewhohears.dscombat.data.mine;

import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetReloadListener;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class MinePresets extends JsonPresetReloadListener<MineStats> {
	
	private static MinePresets instance;
	
	public static MinePresets get() {
		if (instance == null) instance = new MinePresets();
		return instance;
	}
	
	public MinePresets() {
		super("mines");
	}
	
	@Override
	public void registerDefaultPresetTypes() {
		addPresetType(MineStats.MineType.INSTANCE);
	}
	
	@Nullable
	public static MineStats getStats(String id) {
		return get().get(id);
	}
	
	@Nullable
	public static MineStats getStats(ResourceLocation id) {
		return get().get(id.toString());
	}
	
	@Override
	public MineStats[] getNewArray(int i) {
		return new MineStats[i];
	}
	
	@Override
	protected void resetCache() {
		// No cache to reset for mines
	}
}
