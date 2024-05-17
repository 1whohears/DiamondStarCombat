package com.onewhohears.dscombat.data.parts.instance;

import com.onewhohears.dscombat.data.parts.LoadableRecipePartInstance;
import com.onewhohears.dscombat.data.parts.stats.FuelTankStats;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class FuelTankInstance<T extends FuelTankStats> extends PartInstance<T> implements LoadableRecipePartInstance {
	
	private float fuel = 0;
	
	public FuelTankInstance(T stats) {
		super(stats);
	}
	
	@Override
	public void setFilled(String param) {
		super.setFilled(param);
		fuel = getStats().getMaxFuel();
	}

	@Override
	public boolean isSetup(String slotId, EntityVehicle craft) {
		return false;
	}
	
	@Override
	public float getWeight() {
		float w = super.getWeight();
		return w * fuel / getStats().getMaxFuel();
	}
	
	public float getFuel() {
		return fuel;
	}
	
	@Override
	public void readNBT(CompoundTag tag) {
		super.readNBT(tag);
		fuel = tag.getFloat("fuel");
	}
	
	@Override
	public CompoundTag writeNBT() {
		CompoundTag tag = super.writeNBT();
		tag.putFloat("fuel", fuel);
		return tag;
	}
	
	@Override
	public void readBuffer(FriendlyByteBuf buffer) {
		super.readBuffer(buffer);
		fuel = buffer.readFloat();
	}
	
	@Override
	public void writeBuffer(FriendlyByteBuf buffer) {
		super.writeBuffer(buffer);
		buffer.writeFloat(fuel);
	}
	
	/**
	 * @param fuel
	 * @return remainder
	 */
	public float addFuel(float fuel) {
		float max = getStats().getMaxFuel();
		this.fuel += fuel;
		if (this.fuel < 0) {
			float r = this.fuel;
			this.fuel = 0;
			return r;
		}
		else if (this.fuel > max) {
			float r = this.fuel - max;
			this.fuel = max;
			return r;
		}
		return 0;
	}
	
	public void setFuel(float fuel) {
		float max = getStats().getMaxFuel();
		if (fuel > max) fuel = max;
		else if (fuel < 0) fuel = 0;
		this.fuel = fuel;
	}
	
	@Override
	public float getCurrentAmmo() {
		return getFuel();
	}

	@Override
	public float getMaxAmmo() {
		return getStats().getMaxFuel();
	}

	@Override
	public void setCurrentAmmo(float ammo) {
		setFuel(ammo);
	}

	@Override
	public void setMaxAmmo(float max) {
	}

	@Override
	public boolean isCompatibleWithAmmoContinuity(String continuity) {
		return true;
	}

	@Override
	public boolean updateContinuityIfEmpty() {
		return false;
	}

	@Override
	public void setContinuity(String continuity) {
	}

	@Override
	public String getContinuity() {
		return "";
	}

}
