package com.onewhohears.dscombat.entity.parts;

import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.data.parts.PartType;
import com.onewhohears.dscombat.data.parts.instance.WeaponExternalInstance;
import com.onewhohears.dscombat.data.parts.stats.WeaponExternalStats;
import com.onewhohears.dscombat.data.weapon.client.WeaponAssets;
import com.onewhohears.dscombat.data.weapon.client.WeaponClientStats;
import com.onewhohears.dscombat.data.weapon.instance.WeaponInstance;
import com.onewhohears.dscombat.data.weapon.MuzzleSmokeData;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.client.util.UtilParticles;
import com.onewhohears.onewholibs.util.math.UtilAngles;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

public class EntityWeaponRack extends EntityPart<WeaponExternalStats, WeaponExternalInstance<WeaponExternalStats>> {
	
	private String weaponModelId;
	public int lastShootTime;
	private MuzzleSmokeData[] lastSmokeData = null;
	private int flashMaxLifetime = 5;
	
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
		if (weaponModelId != null && !weaponModelId.isEmpty()) return weaponModelId;
		EntityVehicle vehicle = getParentVehicle();
		if (vehicle == null) return "";
		WeaponInstance<?> wd = vehicle.weaponSystem.get(getSlotId());
		if (wd == null) return "";
		String assetId = wd.getStats().getAssetId();
		WeaponClientStats<?> assets = WeaponAssets.get().get(assetId);
		if (assets == null) return "";
		weaponModelId = assets.getModelId();
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
		EntityVehicle vehicle = getParentVehicle();
		if (vehicle != null) {
			WeaponInstance<?> wi = vehicle.weaponSystem.get(getSlotId());
			if (wi != null) {
				MuzzleSmokeData[] smokeData = wi.getStats().getMuzzleSmokeData();
				lastSmokeData = smokeData;
				
				if (smokeData != null && smokeData.length > 0) {
					flashMaxLifetime = smokeData[0].flashLifetime;
				}
			}
		}
	}

	public MuzzleSmokeData[] getLastSmokeData() {
		return lastSmokeData;
	}

	public int getFlashMaxLifetime() {
		return flashMaxLifetime;
	}

	/**
	 * Muzzle position for particle effects. Uses weapon launch pos when on vehicle.
	 */
	public Vec3 getMuzzlePosition() {
		EntityVehicle vehicle = getParentVehicle();
		Vec3 pos = position();
		if (vehicle != null) {
			WeaponInstance<?> wi = vehicle.weaponSystem.get(getSlotId());
			if (wi != null) {
				pos = vehicle.position().add(UtilAngles.rotateVector(wi.getLaunchPos(),
					vehicle.isClientSide() ? vehicle.getClientQ() : vehicle.getQ()));
			}
		}
		return pos.add(getShootDirection().scale(getStats().getMuzzleParticleOffset()));
	}

	public Vec3 getShootDirection() {
		EntityVehicle vehicle = getParentVehicle();
		return vehicle != null ? vehicle.getLookAngle() : getLookAngle();
	}

}
