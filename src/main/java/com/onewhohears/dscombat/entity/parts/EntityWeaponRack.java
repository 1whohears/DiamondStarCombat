package com.onewhohears.dscombat.entity.parts;

import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.data.parts.PartType;
import com.onewhohears.dscombat.data.parts.instance.WeaponExternalInstance;
import com.onewhohears.dscombat.data.parts.stats.WeaponExternalStats;
import com.onewhohears.dscombat.data.weapon.client.WeaponAssets;
import com.onewhohears.dscombat.data.weapon.client.WeaponClientStats;
import com.onewhohears.dscombat.data.weapon.instance.WeaponInstance;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityWeaponRack extends EntityPart<WeaponExternalStats, WeaponExternalInstance<WeaponExternalStats>> {
	
	private String weaponModelId;
	public int lastShootTime;
	
	public EntityWeaponRack(EntityType<?> type, Level level) {
		super(type, level, "xm12");
	}
	
	public int getAmmoNum() {
		EntityVehicle vehicle = getParentVehicle();
		if (vehicle == null) return 0;
		WeaponInstance<?> wd = vehicle.weaponSystem.get(getSlotId());
		if (wd == null) return 0;
		return wd.getCurrentAmmo();
	}
	
	public String getWeaponModelId() {
		if (weaponModelId == null) {
			weaponModelId = "";
			EntityVehicle vehicle = getParentVehicle();
			if (vehicle == null) return weaponModelId;
			WeaponInstance<?> wd = vehicle.weaponSystem.get(getSlotId());
			if (wd == null) return weaponModelId;
			String assetId = wd.getStats().getAssetId();
			WeaponClientStats<?> assets = WeaponAssets.get().get(assetId);
			if (assets == null) return weaponModelId;
			weaponModelId = assets.getModelId();
		}
		return weaponModelId;
	}

	@Override
	public boolean shouldRender() {
		return true;
	}
	
	@Override
	protected double getClientRenderDistance() {
		return Config.CLIENT.renderWeaponRackDistance.get();
	}
	
	@Override
	public PartType getPartType() {
		return PartType.EXTERNAL_WEAPON;
	}
	
	@Override
	public boolean canGetHurt() {
		return false;
	}

	public void onClientShoot() {
		lastShootTime = tickCount;
	}

}
