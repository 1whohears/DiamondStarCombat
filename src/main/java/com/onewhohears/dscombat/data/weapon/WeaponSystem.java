package com.onewhohears.dscombat.data.weapon;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;


import com.onewhohears.dscombat.command.DSCGameRules;
import com.onewhohears.dscombat.data.weapon.instance.NoWeaponInstance;
import com.onewhohears.dscombat.data.weapon.instance.WeaponInstance;
import com.onewhohears.dscombat.data.weapon.stats.WeaponStats;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.UtilMCText;
import com.onewhohears.onewholibs.util.math.UtilAngles;

import com.onewhohears.onewholibs.util.math.VectorUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

/**
 * manages available weapons for {@link EntityVehicle}.
 * available weapons are found in a list of {@link WeaponStats}.
 * used to fire a vehicle's selected weapon.
 * will synch ammo numbers and other info with client.
 * will tell a pilot why a weapon launch failed.
 * @author 1whohears
 */
public class WeaponSystem {
	
	private final EntityVehicle parent;
	private boolean readData = false;
	private List<WeaponInstance<?>> weapons = new ArrayList<>();
	private int weaponIndex = 0;

	private TargetMode targetMode = TargetMode.LOOK;
	private Vec3 targetPos = Vec3.ZERO;
	
	public WeaponSystem(EntityVehicle parent) {
		this.parent = parent;
		weapons.add(NoWeaponInstance.get());
	}
	
	public boolean addWeapon(WeaponInstance<?> data) {
		if (get(data.getStatsId(), data.getSlotId()) != null) return false;
		weapons.add(data);
		return true;
	}
	
	public boolean removeWeapon(String id, String slotId) {
		WeaponInstance<?> w = get(id, slotId);
		if (w == null) return false;
		if (w.getStats().isNoWeapon()) return false;
		return weapons.remove(w);
	}
	
	@Nullable
	public WeaponInstance<?> get(String id, String slotId) {
		for (WeaponInstance<?> w : weapons) if (w.idMatch(id, slotId)) return w;
		return null;
	}
	
	@Nullable
	public WeaponInstance<?> get(String slotId) {
		for (WeaponInstance<?> w : weapons) if (w.getSlotId().equals(slotId)) return w;
		return null;
	}
	
	public WeaponInstance<?> getSelected() {
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
		boolean consumeAmmo = parent.level().getGameRules().getBoolean(DSCGameRules.CONSUME_AMMO);
		return shootSelected(controller, consume && consumeAmmo);
	}
	
	public boolean shootSelected(Entity controller, boolean consume) {
		WeaponInstance<?> data = getSelected();
		if (data == null) return false;
		String name = data.getStatsId();
		String reason = null;
		data.shootFromVehicle(parent.level(), controller, getShootDirection(data), parent, consume);
		if (data.isFailedLaunch()) reason = data.getFailedLaunchReason();
		for (WeaponInstance<?> wd : weapons) if (wd.getStats().isBullet() && wd.getStatsId().equals(name) && !wd.getSlotId().equals(data.getSlotId())) {
			wd.shootFromVehicle(parent.level(), controller, getShootDirection(wd), parent, consume);
			if (reason == null && wd.isFailedLaunch()) reason = wd.getFailedLaunchReason();
		}
		if (reason != null && controller instanceof ServerPlayer player) {
			player.displayClientMessage(UtilMCText.translatable(reason), true);
		}
		return true;
	}
	
	public Vec3 getShootDirection(WeaponInstance<?> data) {
		Quaternionf q = parent.getQ();
		if (parent.isWeaponAngledDown() && data.getStats().canAngleDown()) {
			q.mul(VectorUtils.rotationQuaternion(VectorUtils.POSITIVE_X, 25f));
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
	
	public List<WeaponInstance<?>> getWeapons() {
		return weapons;
	}
	
	@Override
	public String toString() {
		String s = "Weapons:";
		for (WeaponInstance<?> w : weapons) s += w;
		return s;
	}
	
	public int addAmmo(String id, int ammo, boolean updateClient) {
		for (WeaponInstance<?> w : weapons) if (w.getStatsId().equals(id)) {
			ammo = w.addAmmo(ammo);
			if (updateClient) w.updateClientAmmo(parent);
			if (ammo == 0) return 0;
		}
		return ammo;
	}
	
	public void refillAll() {
		if (parent.level().isClientSide) return;
		for (WeaponInstance<?> w : weapons) {
			w.addAmmo(100000);
			w.updateClientAmmo(parent);
		}
	}

	public static enum TargetMode {
		LOOK,
		COORDS,
		INDICATOR
	}

	public TargetMode getTargetMode() {
		return targetMode;
	}

	public void setTargetMode(TargetMode targetMode) {
		this.targetMode = targetMode;
	}

	public Vec3 getTargetPos() {
		return targetPos;
	}

	public void setTargetPos(Vec3 targetPos) {
		this.targetPos = targetPos;
	}
	
}
