package com.onewhohears.dscombat.data.weapon;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.command.DSCGameRules;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

/**
 * manages available weapons for {@link EntityVehicle}.
 * available weapons are found in a list of {@link WeaponData}.
 * used to fire a vehicle's selected weapon.
 * will synch ammo numbers and other info with client.
 * will tell a pilot why a weapon launch failed.
 * @author 1whohears
 */
public class WeaponSystem {
	
	private final EntityVehicle parent;
	private boolean readData = false;
	private List<WeaponData> weapons = new ArrayList<WeaponData>();
	private int weaponIndex = 0;
	
	public WeaponSystem(EntityVehicle parent) {
		this.parent = parent;
		weapons.add(NoWeaponData.get());
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
	
	public boolean shootSelected(Entity controller) {
		boolean consume = true;
		if (parent.isNoConsume()) consume = false;
		else if (controller instanceof Player p && p.isCreative()) consume = false;
		boolean consumeAmmo = parent.level.getGameRules().getBoolean(DSCGameRules.CONSUME_AMMO);
		return shootSelected(controller, consume && consumeAmmo);
	}
	
	public boolean shootSelected(Entity controller, boolean consume) {
		WeaponData data = getSelected();
		if (data == null) return false;
		String name = data.getId();
		String reason = null;
		data.shootFromVehicle(parent.level, controller, getShootDirection(data), parent, consume);
		if (data.isFailedLaunch()) reason = data.getFailedLaunchReason();
		for (WeaponData wd : weapons) if (wd.getType().isBullet() && wd.getId().equals(name) && !wd.getSlotId().equals(data.getSlotId())) {
			wd.shootFromVehicle(parent.level, controller, getShootDirection(wd), parent, consume);
			if (reason == null && wd.isFailedLaunch()) reason = wd.getFailedLaunchReason();
		}
		if (reason != null && controller instanceof ServerPlayer player) {
			player.displayClientMessage(Component.translatable(reason), true);
		}
		return true;
	}
	
	public Vec3 getShootDirection(WeaponData data) {
		Quaternion q = parent.getQ();
		if (parent.isWeaponAngledDown() && data.canAngleDown()) {
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
	public void serverTick() {
		for (int i = 0; i < weapons.size(); ++i) {
			weapons.get(i).tick(parent, i == getSelectedIndex());
		}
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
