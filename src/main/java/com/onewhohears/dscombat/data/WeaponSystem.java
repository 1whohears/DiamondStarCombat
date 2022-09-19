package com.onewhohears.dscombat.data;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.init.DataSerializers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;

public class WeaponSystem {
	
	private List<WeaponData> weapons = new ArrayList<WeaponData>();
	
	private int weaponIndex = 0;
	
	public WeaponSystem(CompoundTag compound) {
		ListTag list = compound.getList("weapons", 10);
		for (int i = 0; i < list.size(); ++i) {
			CompoundTag tag = list.getCompound(i);
			int index = tag.getInt("type");
			WeaponData.WeaponType type = WeaponData.WeaponType.values()[index];
			switch (type) {
			case BOMB:
				break;
			case BULLET:
				weapons.add(new BulletData(tag));
				break;
			case ROCKET:
				break;
			}
		}
	}
	
	public void write(CompoundTag compound) {
		ListTag list = new ListTag();
		for (WeaponData w : weapons) list.add(w.write());
		compound.put("weapons", list);
	}
	
	public WeaponSystem(FriendlyByteBuf buffer) {
		int num = buffer.readInt();
		for (int i = 0; i < num; ++i) weapons.add(DataSerializers.WEAPON_DATA.read(buffer));
	}
	
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(weapons.size());
		for (WeaponData w : weapons) w.write(buffer);
	}
	
	public boolean addWeapon(WeaponData data) {
		if (get(data.getId()) != null) return false;
		weapons.add(data);
		return true;
	}
	
	public void removeWeapon(String id) {
		weapons.remove(get(id));
	}
	
	@Nullable
	public WeaponData get(String id) {
		for (WeaponData w : weapons) if (w.getId().equals(id)) return w;
		return null;
	}
	
	public WeaponData get() {
		if (weapons.size() == 0) return null;
		checkIndex();
		return weapons.get(weaponIndex);
	}
	
	public void selectNextWeapon() {
		++weaponIndex;
		checkIndex();
	}
	
	private void checkIndex() {
		if (weaponIndex >= weapons.size() || weaponIndex < 0) weaponIndex = 0;
	}
	
}
