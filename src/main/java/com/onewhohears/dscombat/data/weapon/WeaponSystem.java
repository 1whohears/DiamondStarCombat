package com.onewhohears.dscombat.data.weapon;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ToClientAddWeapon;
import com.onewhohears.dscombat.common.network.toclient.ToClientRemoveWeapon;
import com.onewhohears.dscombat.common.network.toclient.ToClientWeaponIndex;
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
import net.minecraftforge.network.PacketDistributor;

public class WeaponSystem {
	
	private List<WeaponData> weapons = new ArrayList<WeaponData>();
	private int weaponIndex = 0;
	private EntityAircraft parent;
	private boolean readData = false;
	//private HashMap<String, WeaponData> turrets = new HashMap<>();
	
	public WeaponSystem() {
		
	}
	
	public void read(CompoundTag compound) {
		weapons.clear();
		this.weaponIndex = compound.getInt("index");
		ListTag list = compound.getList("weapons", 10);
		for (int i = 0; i < list.size(); ++i) {
			weapons.add(UtilParse.parseWeaponFromCompound(list.getCompound(i)));
		}
		readData = true;
	}
	
	public void write(CompoundTag compound) {
		ListTag list = new ListTag();
		for (WeaponData w : weapons) list.add(w.write());
		compound.put("weapons", list);
		compound.putInt("index", weaponIndex);
		//System.out.println(this);
	}
	
	public WeaponSystem(FriendlyByteBuf buffer) {
		//System.out.println("WEAPON SYSTEM BUFFER");
		int num = buffer.readInt();
		//System.out.println("num = "+num);
		for (int i = 0; i < num; ++i) weapons.add(DataSerializers.WEAPON_DATA.read(buffer));
		weaponIndex = buffer.readInt();
		//System.out.println("weaponIndex = "+weaponIndex);
		readData = true;
	}
	
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(weapons.size());
		for (WeaponData w : weapons) w.write(buffer);
		buffer.writeInt(weaponIndex);
	}
	
	/**
	 * @param ws copies this weapon system's weapons list and selected weapon index
	 */
	public void copy(WeaponSystem ws) {
		this.weapons = ws.weapons;
		this.weaponIndex = ws.weaponIndex;
		this.readData = true;
	}
	
	public boolean addWeapon(WeaponData data, boolean updateClient) {
		if (get(data.getId(), data.getSlotId()) != null) return false;
		weapons.add(data);
		if (updateClient) {
			PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> parent), 
					new ToClientAddWeapon(parent.getId(), data));
		}
		return true;
	}
	
	public void removeWeapon(String id, String slotId, boolean updateClient) {
		boolean r = weapons.remove(get(id, slotId));
		if (r && updateClient) {
			PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> parent), 
					new ToClientRemoveWeapon(parent.getId(), id, slotId));
		}
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
	
	public boolean shootSelected(Entity controller, boolean consume) {
		WeaponData data = getSelected();
		if (data == null) return false;
		String name = data.getId();
		String reason = null;
		data.shoot(parent.level, controller, UtilAngles.getRollAxis(parent.getQ()), null, parent, consume);
		if (data.isFailedLaunch()) reason = data.getFailedLaunchReason();
		for (WeaponData wd : weapons) if (wd.getType() == WeaponType.BULLET && wd.getId().equals(name) && !wd.getSlotId().equals(data.getSlotId())) {
			wd.shoot(parent.level, controller, parent.getLookAngle(), null, parent, consume);
			if (reason == null && wd.isFailedLaunch()) reason = wd.getFailedLaunchReason();
		}
		if (reason != null && controller instanceof ServerPlayer player) {
			player.displayClientMessage(Component.translatable(reason), true);
		}
		return true;
	}
	
	public void selectNextWeapon() {
		++weaponIndex;
		checkIndex();
		//System.out.println("new weapon index "+weaponIndex);
		PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> parent), 
				new ToClientWeaponIndex(parent.getId(), weaponIndex));
	}
	
	public void clientSetSelected(int index) {
		weaponIndex = index;
		checkIndex();
		//System.out.println("client new weapon index "+weaponIndex);
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
	
	public void setup(EntityAircraft parent) {
		this.parent = parent;
	}
	
	public void clientSetup(EntityAircraft parent) {
		this.parent = parent;
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
