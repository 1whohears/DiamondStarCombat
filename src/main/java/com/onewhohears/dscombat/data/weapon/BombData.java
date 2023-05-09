package com.onewhohears.dscombat.data.weapon;

import java.util.List;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.JsonPreset;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class BombData extends BulletData {
	
	public static class Builder extends AbstractWeaponBuilders.BombBuilder<Builder> {
		
		protected Builder(String namespace, String name, JsonPresetFactory<? extends BombData> sup) {
			super(namespace, name, sup, WeaponType.BOMB);
		}
		
		public static Builder bombBuilder(String namespace, String name) {
			return new Builder(namespace, name, (key, json) -> new BombData(key, json));
		}
		
	}
	
	public BombData(ResourceLocation key, JsonObject json) {
		super(key, json);
	}
	
	@Override
	public void readNBT(CompoundTag tag) {
		super.readNBT(tag);
	}
	
	@Override
	public CompoundTag writeNbt() {
		CompoundTag tag = super.writeNbt();
		// TODO 2.2 make a bomb
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
		return WeaponType.BOMB;
	}
	
	@Override
	public <T extends JsonPreset> T copy() {
		return (T) new BombData(getKey(), getJsonData());
	}
	
	@Override
	public List<ComponentColor> getInfoComponents() {
		List<ComponentColor> list = super.getInfoComponents();
		
		return list;
	}
}
