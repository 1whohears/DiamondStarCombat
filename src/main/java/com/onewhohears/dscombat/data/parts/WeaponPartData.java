package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.entity.parts.EntityAbstractPart;
import com.onewhohears.dscombat.entity.parts.EntityWeaponRack;
import com.onewhohears.dscombat.init.ModItems;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class WeaponPartData extends PartData {
	
	public final String weaponId;
	private int ammo;
	private int max;
	
	public WeaponPartData(float weight, String preset, int ammo, int max) {
		super(weight);
		this.weaponId = preset;
		this.ammo = ammo;
		this.max = max;
	}
	
	public WeaponPartData(CompoundTag tag) {
		super(tag);
		weaponId = tag.getString("weaponId");
		ammo = tag.getInt("ammo");
		max = tag.getInt("max");
	}
	
	public CompoundTag write() {
		CompoundTag tag = super.write();
		tag.putString("weaponId", weaponId);
		tag.putInt("ammo", ammo);
		tag.putInt("max", max);
		return tag;
	}

	public WeaponPartData(FriendlyByteBuf buffer) {
		super(buffer);
		weaponId = buffer.readUtf();
		ammo = buffer.readInt();
		max = buffer.readInt();
	}
	
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
		buffer.writeUtf(weaponId);
		buffer.writeInt(ammo);
		buffer.writeInt(max);
	}

	@Override
	public PartType getType() {
		return PartType.WEAPON_RACK;
	}
	
	@Override
	public void setup(EntityAbstractAircraft craft, String slotId, Vec3 pos) {
		super.setup(craft, slotId, pos);
		if (!isSetup(slotId, craft)) {
			EntityWeaponRack rack = new EntityWeaponRack(craft.level, slotId, pos);
			rack.setPos(craft.position());
			rack.startRiding(craft);
			craft.level.addFreshEntity(rack);
		}
		WeaponData data = craft.weaponSystem.get(weaponId, slotId);
		if (data == null) {
			data = WeaponPresets.getById(weaponId);
			data.setSlot(slotId);
			craft.weaponSystem.addWeapon(data, true);
		}
		data.setCurrentAmmo(ammo);
		data.setMaxAmmo(max);
		data.updateClientAmmo(craft);
	}
	
	@Override
	public boolean isSetup(String slotId, EntityAbstractAircraft craft) {
		for (EntityAbstractPart part : craft.getPartEntities()) 
			if (part.getSlotId().equals(slotId)) 
				return true;
		return false;
	}
	
	@Override
	public void remove(String slotId) {
		for (EntityAbstractPart part : getParent().getPartEntities()) 
			if (part.getSlotId().equals(slotId)) 
				part.discard();
		this.getParent().weaponSystem.removeWeapon(weaponId, slotId, true);
	}
	
	@Override
	public void tick(String slotId) {
		super.tick(slotId);
		WeaponData data = this.getParent().weaponSystem.get(weaponId, slotId);
		if (data != null) {
			ammo = data.getCurrentAmmo();
			max = data.getMaxAmmo();
		}
	}
	
	@Override
	public void clientTick(String slotId) {
		super.clientTick(slotId);
		this.tick(slotId);
	}
	
	public static final SlotType[] INTERNAL = new SlotType[] {SlotType.INTERNAL};
	public static final SlotType[] EXTERNAL = new SlotType[] {SlotType.WING, SlotType.FRAME};
	
	@Override
	public SlotType[] getCompatibleSlots() {
		return INTERNAL;
	}

	@Override
	public ItemStack getItemStack() {
		ItemStack stack = new ItemStack(ModItems.WEAPON_RACK.get(), 1);
		stack.setTag(write());
		System.out.println("created stack "+stack.toString()+" "+stack.getTag());
		return stack;
	}

}
