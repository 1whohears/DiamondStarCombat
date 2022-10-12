package com.onewhohears.dscombat.data;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.common.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ClientBoundWeaponIndexPacket;
import com.onewhohears.dscombat.entity.aircraft.EntityAbstractAircraft;
import com.onewhohears.dscombat.init.DataSerializers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.PacketDistributor;

public class WeaponSystem {
	
	private List<WeaponData> weapons = new ArrayList<WeaponData>();
	private int weaponIndex = 0;
	private EntityAbstractAircraft parent;
	private boolean readData = true;
	
	public WeaponSystem() {
		readData = false;
	}
	
	public WeaponSystem(CompoundTag compound) {
		ListTag list = compound.getList("weapons", 10);
		for (int i = 0; i < list.size(); ++i) {
			CompoundTag tag = list.getCompound(i);
			int index = tag.getInt("type");
			WeaponData.WeaponType type = WeaponData.WeaponType.values()[index];
			switch (type) {
			case BOMB:
				weapons.add(new BombData(tag));
				break;
			case BULLET:
				weapons.add(new BulletData(tag));
				break;
			case ROCKET:
				weapons.add(new MissileData(tag));
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
	
	public boolean addWeapon(WeaponData data, boolean updateClient) {
		if (get(data.getId()) != null) return false;
		weapons.add(data);
		// TODO add weapon packet
		return true;
	}
	
	public void removeWeapon(String id, boolean updateClient) {
		weapons.remove(get(id));
		// TODO remove weapon packet
	}
	
	@Nullable
	public WeaponData get(String id) {
		for (WeaponData w : weapons) if (w.getId().equals(id)) return w;
		return null;
	}
	
	public WeaponData getSelected() {
		if (weapons.size() == 0) return null;
		checkIndex();
		return weapons.get(weaponIndex);
	}
	
	public void selectNextWeapon() {
		++weaponIndex;
		checkIndex();
		System.out.println("new weapon index "+weaponIndex);
		PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> parent), 
				new ClientBoundWeaponIndexPacket(parent.getId(), weaponIndex));
	}
	
	public void clientSetSelected(int index) {
		weaponIndex = index;
		checkIndex();
		System.out.println("client new weapon index "+weaponIndex);
	}
	
	private void checkIndex() {
		if (weaponIndex >= weapons.size() || weaponIndex < 0) weaponIndex = 0;
	}
	
	/**
	 * called by this weapon system's entity tick function server side
	 */
	public void tick() {
		for (WeaponData w : weapons) w.tick();
	}
	
	public void setup(EntityAbstractAircraft parent) {
		this.parent = parent;
	}
	
	public void clientSetup(EntityAbstractAircraft parent) {
		this.parent = parent;
	}
	
	public boolean isReadData() {
		return readData;
	}
	
	public List<WeaponData> getWeapons() {
		return weapons;
	}
	
}
