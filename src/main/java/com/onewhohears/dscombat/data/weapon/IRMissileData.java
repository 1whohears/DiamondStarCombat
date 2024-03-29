package com.onewhohears.dscombat.data.weapon;

import static com.onewhohears.dscombat.DSCombatMod.MODID;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.JsonPreset;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.entity.weapon.IRMissile;
import com.onewhohears.dscombat.entity.weapon.IRMissile.IrTarget;
import com.onewhohears.dscombat.util.UtilMCText;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class IRMissileData extends MissileData {
	
	private final float flareResistance;
	
	public IRMissileData(ResourceLocation key, JsonObject json) {
		super(key, json);
		flareResistance = json.get("flareResistance").getAsFloat();
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
		return WeaponType.IR_MISSILE;
	}
	
	@Override
	public <T extends JsonPreset> T copy() {
		return (T) new IRMissileData(getKey(), getJsonData());
	}
	
	/**
	 * only matters if this is an IR missile
	 * 0 means immune to flares
	 * 1 means flares effect tracking normally
	 * @return missiles flare resistance
	 */
	public float getFlareResistance() {
		return flareResistance;
	}
	
	@Override
	public EntityWeapon getShootEntity(WeaponShootParameters params) {
		IRMissile missile = (IRMissile) super.getShootEntity(params);
		if (missile == null) return null;
		return missile;
	}
	
	@Override
	public List<ComponentColor> getInfoComponents() {
		List<ComponentColor> list = super.getInfoComponents();
		list.add(2, new ComponentColor(UtilMCText.literal("TARGETS FLYING"), 0xaaaa00));
		list.add(3, new ComponentColor(UtilMCText.literal("IR GUIDED"), 0xaaaa00));
		if (getFlareResistance() != 0) if (getFov() != -1) list.add(new ComponentColor(UtilMCText.literal("Flare Resistance: ").append(getFlareResistance()+""), 0x040404));
		return list;
	}
	
	@Override
	public String getWeaponTypeCode() {
		return "AAIR";
	}
	
	protected List<IrTarget> targets = new ArrayList<IrTarget>();
	
	@Override
	public void tick(@Nullable EntityVehicle parent, boolean isSelected) {
		super.tick(parent, isSelected);
		if (parent == null) return;
		if (parent.tickCount % 10 == 0) {
			if (!isSelected) {
				parent.stopIRTone();
				return;
			}
			IRMissile.updateIRTargetsList(parent, targets, flareResistance, getFov());
			if (targets.size() > 0) parent.playIRTone();
			else parent.stopIRTone();
		}
	}
	
	@Override
	public String getDefaultIconLocation() {
		return MODID+":textures/ui/weapon_icons/ir_missile.png";
	}

}
