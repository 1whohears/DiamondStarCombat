package com.onewhohears.dscombat.data.parts.instance;

import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.crafting.*;
import com.onewhohears.dscombat.data.parts.ReloadablePartInstance;
import com.onewhohears.dscombat.data.parts.stats.TurretStats;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.entity.parts.EntityPart;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class TurretInstance<T extends TurretStats> extends SeatInstance<T> implements ReloadablePartInstance {
	
	private String weapon = "";
	private int ammo = 0;
	
	public TurretInstance(T stats) {
		super(stats);
	}
	
	@Override
	public void setFilled(String param) {
		super.setFilled(param);
		if (param.isEmpty()) {
			List<String> list = WeaponPresets.get().getCompatibleWeapons(getStatsId());
			if (!list.isEmpty()) param = list.get(0);
		}
		weapon = param;
		ammo = getStats().getMaxAmmo();
	}

	@Override
	public void setParamNotFilled(String param) {
		super.setParamNotFilled(param);
		if (param.isEmpty()) {
			List<String> list = WeaponPresets.get().getCompatibleWeapons(getStatsId());
			if (!list.isEmpty()) param = list.get(0);
		}
		weapon = param;
	}
	
	@Override
	public void readNBT(CompoundTag tag) {
		super.readNBT(tag);
		if (tag.contains("weapon")) weapon = tag.getString("weapon");
		else if (tag.contains("weaponId")) weapon = tag.getString("weaponId");
		if (!WeaponPresets.get().has(weapon)) weapon = "";
		ammo = tag.getInt("ammo");
	}
	
	@Override
	public CompoundTag writeNBT() {
		CompoundTag tag = super.writeNBT();
		tag.putString("weapon", weapon);
		tag.putInt("ammo", ammo);
		return tag;
	}
	
	@Override
	public void readBuffer(FriendlyByteBuf buffer) {
		super.readBuffer(buffer);
		weapon = buffer.readUtf();
		ammo = buffer.readInt();
	}
	
	@Override
	public void writeBuffer(FriendlyByteBuf buffer) {
		super.writeBuffer(buffer);
		buffer.writeUtf(weapon);
		buffer.writeInt(ammo);
	}
	
	public String getWeaponId() {
		return weapon;
	}
	
	@Override
	public void serverSetup(EntityVehicle craft, String slotId, Vec3 pos) {
		super.serverSetup(craft, slotId, pos);
		EntityTurret turret = getTurret(slotId);
		if (turret == null) return;
		turret.setMaxAmmo(getStats().getMaxAmmo());
		turret.setAmmo(ammo);
	}
	
	@Nullable
	public EntityTurret getTurret(String slotId) {
		EntityVehicle craft = getParent();
		if (craft == null) return null;
		for (EntityPart part : craft.getPartEntities()) 
			if (part.getPartType().is(getStats().getType()) && part.getSlotId().equals(slotId)
					&& part instanceof EntityTurret turret)
				return turret;
		return null;
	}
	
	@Override
	public void setUpPartEntity(EntityPart part, EntityVehicle craft, String slotId, Vec3 pos, float health) {
		super.setUpPartEntity(part, craft, slotId, pos, health);
		if (!(part instanceof EntityTurret turret)) return;
		turret.setWeaponId(getWeaponId());
	}
	
	public void setAmmo(int ammo) {
		this.ammo = ammo;
	}

	@Override
	public float getCurrentAmmo() {
		return ammo;
	}

	@Override
	public float getMaxAmmo() {
		return getStats().getMaxAmmo();
	}

	@Override
	public void setCurrentAmmo(float ammo) {
		this.ammo = (int)ammo;
		EntityTurret turret = getTurret(getSlotId());
		if (turret == null) return;
		turret.setAmmo(this.ammo);
	}

	@Override
	public void setMaxAmmo(float max) {
	}

	@Override
	public boolean isCompatibleWithAmmoContinuity(String continuity) {
		return getStats().isWeaponCompatible(continuity);
	}

	@Override
	public boolean updateContinuityIfEmpty() {
		return true;
	}

	@Override
	public void setContinuity(String continuity) {
		this.weapon = continuity;
		EntityTurret turret = getTurret(getSlotId());
		if (turret == null) return;
		turret.setWeaponId(this.weapon);
	}

	@Override
	public String getContinuity() {
		return getWeaponId();
	}
	
	@Override
	public boolean isContinuityEmpty() {
		return getContinuity() == null || getContinuity().isEmpty() || getCurrentAmmo() == 0;
	}

	private static final PartItemLoadRecipe<?> LOAD_RECIPE = new TurretLoadRecipe(
			new ResourceLocation("dscombat:turret_load_recipe"));
	private static final PartItemUnloadRecipe<?> UNLOAD_RECIPE = new TurretUnloadRecipe(
			new ResourceLocation("dscombat:turret_unload_recipe"));

	@Override
	public PartItemLoadRecipe<?> getLoadRecipe() {
		return LOAD_RECIPE;
	}

	@Override
	public @Nullable PartItemUnloadRecipe<?> getUnloadRecipe() {
		return UNLOAD_RECIPE;
	}

}
