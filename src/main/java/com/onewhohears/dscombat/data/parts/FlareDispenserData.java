package com.onewhohears.dscombat.data.parts;

import com.onewhohears.dscombat.data.parts.PartSlot.SlotType;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.weapon.EntityFlare;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class FlareDispenserData extends PartData implements LoadableRecipePartData {
	
	private final int max;
	private final float heat;
	private final int age;
	private int flares;
	
	public FlareDispenserData(float weight, int flares, int max, float heat, int age, ResourceLocation itemid, SlotType[] compatibleSlots) {
		super(weight, itemid, compatibleSlots);
		this.flares = flares;
		this.heat = heat;
		this.age = age;
		this.max = max;
	}
	
	public void read(CompoundTag tag) {
		super.read(tag);
		flares = tag.getInt("flares");
	}
	
	public CompoundTag write() {
		CompoundTag tag = super.write();
		tag.putInt("flares", flares);
		return tag;
	}
	
	public void read(FriendlyByteBuf buffer) {
		super.read(buffer);
		flares = buffer.readInt();
	}
	
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
		buffer.writeInt(flares);
	}
	
	@Override
	public PartType getType() {
		return PartType.FLARE_DISPENSER;
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
		this.flares += flares;
		if (this.flares < 0) {
			int r = this.flares;
			this.flares = 0;
			return r;
		}
		else if (this.flares > max) {
			int r = this.flares - this.max;
			this.flares = max;
			return r;
		}
		return 0;
	}
	
	public void setFlares(int flares) {
		if (flares > max) flares = max;
		else if (flares < 0) flares = 0;
		this.flares = flares;
	}
	
	@Override
	public int getFlares() {
		return flares;
	}
	
	public int getMaxFlares() {
		return max;
	}
	
	@Override
	public float getWeight() {
		return super.getWeight();
	}
	
	public boolean flare(boolean consume) {
		if (getParent() == null) return false;
		if (getFlares() <= 0) return false;
		Level level = getParent().level;
		EntityFlare flare = new EntityFlare(level, heat, age, 3);
		flare.setPos(getParent().position().add(relPos));
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
		return getMaxFlares();
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
