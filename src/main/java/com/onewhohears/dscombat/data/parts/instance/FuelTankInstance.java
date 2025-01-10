package com.onewhohears.dscombat.data.parts.instance;

import java.util.List;

import com.onewhohears.dscombat.crafting.FuelTankLoadRecipe;
import com.onewhohears.dscombat.crafting.PartItemLoadRecipe;
import com.onewhohears.dscombat.crafting.PartItemUnloadRecipe;
import com.onewhohears.dscombat.data.parts.stats.FuelTankStats;
import com.onewhohears.onewholibs.util.UtilMCText;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

public class FuelTankInstance<T extends FuelTankStats> extends PartInstance<T> implements ReloadablePartInstance {
	
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
		setDirty();
		return 0;
	}
	
	public void setFuel(float fuel) {
		float max = getStats().getMaxFuel();
		if (fuel > max) fuel = max;
		else if (fuel < 0) fuel = 0;
		this.fuel = fuel;
		setDirty();
	}
	
	@Override
	public float getCurrentAmmo() {
		return getFuel();
	}

	@Override
	public float getMaxAmmo() {
		return getMaxFuel();
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

	private static final PartItemLoadRecipe<?> LOAD_RECIPE = new FuelTankLoadRecipe(
			new ResourceLocation("dscombat:fuel_tank_load_recipe"));

	@Override
	public PartItemLoadRecipe<?> getLoadRecipe() {
		return LOAD_RECIPE;
	}

	@Override
	public @Nullable PartItemUnloadRecipe<?> getUnloadRecipe() {
		return null;
	}

	@Override
	public void addToolTips(List<Component> tips, TooltipFlag isAdvanced) {
		tips.add(UtilMCText.translatable("info.dscombat.fuel")
				.append(": "+(int)fuel+"/"+(int)getStats().getMaxFuel())
				.setStyle(Style.EMPTY.withColor(0xAAAAAA)));
		super.addToolTips(tips, isAdvanced);
	}
	
	@Override
	public float getCurrentFuel() {
		return getFuel();
	}
	
	@Override
	public float getMaxFuel() {
		return getStats().getMaxFuel();
	}

	@Override
	public void tick(String slotId) {
		super.tick(slotId);
		if (isDamaged()) addFuel(-0.06f);
	}

}
