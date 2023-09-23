package com.onewhohears.dscombat.data.weapon;

import java.util.List;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.JsonPreset;
import com.onewhohears.dscombat.data.weapon.WeaponData.ComponentColor;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.entity.weapon.EntityBunkerBuster;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

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
	public EntityWeapon getShootEntity(Level level, Entity owner, Vec3 pos, Vec3 direction, @Nullable EntityAircraft vehicle, boolean ignoreRecoil) {
		EntityBunkerBuster bomb = (EntityBunkerBuster) super.getShootEntity(level, owner, pos, direction, vehicle, ignoreRecoil);
		if (bomb == null) return null;
		return bomb;
	}
	
	@Override
	public EntityWeapon getEntity(Level level, Entity owner) {
		return new EntityBunkerBuster(level, owner, this);
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
	public void addToolTips(List<Component> tips) {
		super.addToolTips(tips);
		tips.add(Component.literal("Block Strength: ").append(getBlockStrength()+"").setStyle(Style.EMPTY.withColor(0xAAAAAA)));
	}
	
	@Override
	public List<ComponentColor> getInfoComponents() {
		List<ComponentColor> list = super.getInfoComponents();
		list.add(new ComponentColor(Component.literal("Block Strength: ").append(getBlockStrength()+""), 0x040404));
		return list;
	}
	
	@Override
	public String getWeaponTypeCode() {
		String code = "BB";
		if (isCausesFire()) code += "I";
		return code;
	}

}
