package com.onewhohears.dscombat.data.weapon;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.data.weapon.WeaponData.WeaponType;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.util.UtilParse;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class WeaponSystem {
	
	private final EntityAircraft parent;
	private boolean readData = false;
	private List<WeaponData> weapons = new ArrayList<WeaponData>();
	private int weaponIndex = 0;
	
	public WeaponSystem(EntityAircraft parent) {
		this.parent = parent;
	}
	
	public void read(CompoundTag compound) {
		weapons.clear();
		this.weaponIndex = compound.getInt("index");
		ListTag list = compound.getList("weapons", 10);
		for (int i = 0; i < list.size(); ++i) {
			WeaponData w = UtilParse.parseWeaponFromCompound(list.getCompound(i));
			if (w != null) weapons.add(w);
		}
		readData = true;
	}
	
	public void write(CompoundTag compound) {
		ListTag list = new ListTag();
		for (WeaponData w : weapons) list.add(w.writeNbt());
		compound.put("weapons", list);
		compound.putInt("index", weaponIndex);
		//System.out.println(this);
	}
	
	public static List<WeaponData> readWeaponsFromBuffer(FriendlyByteBuf buffer) {
		List<WeaponData> weapons = new ArrayList<WeaponData>();
		int num = buffer.readInt();
		for (int i = 0; i < num; ++i) weapons.add(DataSerializers.WEAPON_DATA.read(buffer));
		return weapons;
	}
	
	public static void writeWeaponsToBuffer(FriendlyByteBuf buffer, List<WeaponData> weapons) {
		buffer.writeInt(weapons.size());
		for (WeaponData w : weapons) DataSerializers.WEAPON_DATA.write(buffer, w);
	}
	
	public void setWeapons(List<WeaponData> weapons) {
		this.weapons = weapons;
		readData = true;
	}
	
	public boolean addWeapon(WeaponData data) {
		if (get(data.getId(), data.getSlotId()) != null) return false;
		weapons.add(data);
		return true;
	}
	
	public boolean removeWeapon(String id, String slotId) {
		return weapons.remove(get(id, slotId));
	}
	
	@Nullable
	public WeaponData get(String id, String slotId) {
		for (WeaponData w : weapons) if (w.idMatch(id, slotId)) return w;
		return null;
	}
	
	@Nullable
	public WeaponData get(String slotId) {
		for (WeaponData w : weapons) if (w.getSlotId().equals(slotId)) return w;
		return null;
	}
	
	@Nullable
	public WeaponData getSelected() {
		if (weapons.size() == 0) return null;
		checkIndex();
		return weapons.get(weaponIndex);
	}
	
	public int getSelectedIndex() {
		return weaponIndex;
	}
	
	public boolean shootSelected(Entity controller, boolean consume) {
		WeaponData data = getSelected();
		if (data == null) return false;
		String name = data.getId();
		String reason = null;
		data.shootFromVehicle(parent.level, controller, getShootDirection(data), parent, consume);
		if (data.isFailedLaunch()) reason = data.getFailedLaunchReason();
		for (WeaponData wd : weapons) if (wd.getType() == WeaponType.BULLET && wd.getId().equals(name) && !wd.getSlotId().equals(data.getSlotId())) {
			wd.shootFromVehicle(parent.level, controller, getShootDirection(wd), parent, consume);
			if (reason == null && wd.isFailedLaunch()) reason = wd.getFailedLaunchReason();
		}
		if (reason != null && controller instanceof ServerPlayer player) {
			player.displayClientMessage(Component.translatable(reason), true);
		}
		if (reason == null) parent.lastShootTime = parent.tickCount;
		return true;
	}
	
	public Vec3 getShootDirection(WeaponData data) {
		Quaternion q = parent.getQ();
		if (parent.isWeaponAngledDown() && data.getSlotId().equals("slotname.dscombat.frame_1")) {
			q.mul(Vector3f.XP.rotationDegrees(25f));
		}
    	return UtilAngles.getRollAxis(q);
    }
	
	public void selectNextWeapon(int input) {
		if (input == 1) ++weaponIndex;
		else if (input == -1) --weaponIndex;
		else return;
		checkIndex();
		//System.out.println("new weapon index "+weaponIndex);
	}
	
	public void setSelected(int index) {
		weaponIndex = index;
		checkIndex();
		//System.out.println("client new weapon index "+weaponIndex);
	}
	
	private void checkIndex() {
		if (weaponIndex >= weapons.size()) weaponIndex = 0;
		else if (weaponIndex < 0) weaponIndex = weapons.size()-1;
	}
	
	/**
	 * called by this weapon system's entity tick function server side
	 */
	public void tick() {
		for (WeaponData w : weapons) w.tick();
	}
	
	public boolean isReadData() {
		return readData;
	}
	
	public List<WeaponData> getWeapons() {
		return weapons;
	}
	
	@Override
	public String toString() {
		String s = "Weapons:";
		for (WeaponData w : weapons) s += w;
		return s;
	}
	
	public int addAmmo(String id, int ammo, boolean updateClient) {
		for (WeaponData w : weapons) if (w.getId().equals(id)) {
			ammo = w.addAmmo(ammo);
			if (updateClient) w.updateClientAmmo(parent);
			if (ammo == 0) return 0;
		}
		return ammo;
	}
	
}
