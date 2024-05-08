package com.onewhohears.dscombat.data.jsonpreset;

import net.minecraft.nbt.CompoundTag;

public abstract class JsonPresetInstance<T extends JsonPresetStats> {
	
	private final T stats;
	
	protected JsonPresetInstance(T stats) {
		this.stats = stats;
	}
	
	public T getStats() {
		return stats;
	}
	
	public String getStatsId() {
		return getStats().getId();
	}
	
	public String getTypeId() {
		return getStats().getType().getId();
	}
	
	public void readNBT(CompoundTag tag) {
		
	}
	
	public CompoundTag writeNBT() {
		CompoundTag tag = new CompoundTag();
		tag.putString("presetId", getStatsId());
		return tag;
	}
	
}
