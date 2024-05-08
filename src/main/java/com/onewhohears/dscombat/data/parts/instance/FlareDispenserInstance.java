package com.onewhohears.dscombat.data.parts.instance;

import com.onewhohears.dscombat.data.parts.LoadableRecipePartInstance;
import com.onewhohears.dscombat.data.parts.stats.FlareDispenserStats;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.weapon.EntityFlare;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;

public class FlareDispenserInstance<T extends FlareDispenserStats> extends PartInstance<T> implements LoadableRecipePartInstance {
	
	private int flares = 0;
	
	public FlareDispenserInstance(T stats) {
		super(stats);
	}
	
	@Override
	public void readNBT(CompoundTag tag) {
		super.readNBT(tag);
		flares = tag.getInt("flares");
	}
	
	@Override
	public CompoundTag writeNBT() {
		CompoundTag tag = super.writeNBT();
		tag.putInt("flares", flares);
		return tag;
	}
	
	@Override
	public void readBuffer(FriendlyByteBuf buffer) {
		super.readBuffer(buffer);
		flares = buffer.readInt();
	}
	
	@Override
	public void writeBuffer(FriendlyByteBuf buffer) {
		super.writeBuffer(buffer);
		buffer.writeInt(flares);
	}
	
	@Override
	public boolean isSetup(String slotId, EntityVehicle craft) {
		return false;
	}
	
	/**
	 * @param flares
	 * @return remainder
	 */
	public int addFlares(int flares) {
		int max = getStats().getMaxFlares();
		this.flares += flares;
		if (this.flares < 0) {
			int r = this.flares;
			this.flares = 0;
			return r;
		}
		else if (this.flares > max) {
			int r = this.flares - max;
			this.flares = max;
			return r;
		}
		return 0;
	}
	
	public void setFlares(int flares) {
		int max = getStats().getMaxFlares();
		if (flares > max) flares = max;
		else if (flares < 0) flares = 0;
		this.flares = flares;
	}
	
	@Override
	public int getFlares() {
		return flares;
	}
	
	public boolean flare(boolean consume) {
		if (getParent() == null) return false;
		if (getFlares() <= 0) return false;
		Level level = getParent().level;
		EntityFlare flare = new EntityFlare(level, getStats().getInitHeat(), getStats().getMaxAge(), 3);
		flare.setPos(getParent().position().add(getRelPos()));
		flare.setDeltaMovement(getParent().getDeltaMovement());
		level.addFreshEntity(flare);
		if (consume) addFlares(-1);
		return true;
	}

	@Override
	public float getCurrentAmmo() {
		return getFlares();
	}

	@Override
	public float getMaxAmmo() {
		return getStats().getMaxFlares();
	}

	@Override
	public void setCurrentAmmo(float ammo) {
		setFlares((int)ammo);
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
