package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.entity.parts.EntityAbstractPart;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class WeaponRackData extends PartData {
	
	public final String weaponId;
	
	public WeaponRackData(float weight, String preset) {
		super(weight);
		this.weaponId = preset;
	}
	
	public WeaponRackData(CompoundTag tag) {
		super(tag);
		weaponId = tag.getString("weaponId");
	}
	
	public CompoundTag write() {
		CompoundTag tag = super.write();
		tag.putString("weaponId", weaponId);
		return tag;
	}

	public WeaponRackData(FriendlyByteBuf buffer) {
		super(buffer);
		weaponId = buffer.readUtf();
	}
	
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
		buffer.writeUtf(weaponId);
	}

	@Override
	public PartType getType() {
		return PartType.WEAPON_RACK;
	}
	
	@Override
	public void setup(EntityAbstractAircraft craft, String slotId, Vec3 pos) {
		super.setup(craft, slotId, pos);
		if (!isSetup(slotId, craft)) {
			
		}
		// TODO setup weapon rack
		WeaponData data = craft.weaponSystem.get(weaponId, slotId);
		if (data == null) {
			data = WeaponPresets.getById(weaponId);
			data.setSlot(slotId);
			craft.weaponSystem.addWeapon(data, true);
		}
	}
	
	@Override
	public boolean isSetup(String slotId, EntityAbstractAircraft craft) {
		EntityAbstractAircraft p = getParent();
		if (p == null) return false;
		for (EntityAbstractPart part : p.getPartEntities()) 
			if (part.getSlotId().equals(slotId)) 
				return true;
		return false;
	}
	
	@Override
	public void remove(String slotId) {
		// TODO remove weapon rack
		this.getParent().weaponSystem.removeWeapon(weaponId, slotId, true);
	}

	@Override
	public SlotType[] getCompatibleSlots() {
		return new SlotType[] {SlotType.WING, SlotType.FRAME};
	}

	@Override
	public ItemStack getItemStack() {
		ItemStack stack = new ItemStack(ModItems.WEAPON_RACK.get(), 1);
		stack.setTag(write());
		System.out.println("created stack "+stack.toString()+" "+stack.getTag());
		return stack;
	}

}
