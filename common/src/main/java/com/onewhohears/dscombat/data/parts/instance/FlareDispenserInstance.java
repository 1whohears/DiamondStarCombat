package com.onewhohears.dscombat.data.parts.instance;

import com.onewhohears.dscombat.crafting.FlareDispenserLoadRecipe;
import com.onewhohears.dscombat.crafting.PartItemLoadRecipe;
import com.onewhohears.dscombat.crafting.PartItemUnloadRecipe;
import com.onewhohears.dscombat.data.parts.stats.FlareDispenserStats;
import com.onewhohears.dscombat.entity.weapon.EntityFlare;

import com.onewhohears.onewholibs.util.UtilMCText;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class FlareDispenserInstance<T extends FlareDispenserStats> extends PartInstance<T> implements ReloadablePartInstance {
	
	private int flares = 0;
	
	public FlareDispenserInstance(T stats) {
		super(stats);
	}
	
	@Override
	public void setFilled(String param) {
		super.setFilled(param);
		flares = getStats().getMaxFlares();
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
		setDirty();
		return 0;
	}
	
	public void setFlares(int flares) {
		int max = getStats().getMaxFlares();
		if (flares > max) flares = max;
		else if (flares < 0) flares = 0;
		this.flares = flares;
		setDirty();
	}
	
	@Override
	public int getFlares() {
		return flares;
	}
	
	public boolean flare(boolean consume) {
		if (isDamaged()) return false;
		if (getParent() == null) return false;
		if (getFlares() <= 0) return false;
		Level level = getParent().getWorld();

		// Build proper right vector using yaw only (pitch doesn't affect left/right axis)
		float yawRad = (float) Math.toRadians(getParent().getYRot());
		// In Minecraft: yaw=0 faces south (+Z), yaw=90 faces west (-X)
		// Right vector (perpendicular, 90 degrees clockwise from forward)
		double rightX = Math.cos(yawRad);
		double rightY = 0;
		double rightZ = -Math.sin(yawRad);

		// Down vector along the vehicle's local Y axis (accounts for pitch and roll)
		float pitchRad = (float) Math.toRadians(getParent().getXRot());
		float yaw = yawRad;
		// Local down = rotated (0,-1,0) by pitch around right axis, then yaw
		double downX = Math.sin(yaw) * Math.sin(pitchRad);
		double downY = -Math.cos(pitchRad);
		double downZ = Math.cos(yaw) * Math.sin(pitchRad);

		double sideOffset = 1.5;
		double sideVelocity = 0.3;
		double downOffset = 0.3; // eject slightly downward relative to vehicle

		java.util.Random rand = new java.util.Random();

		// Right flare
		EntityFlare flareRight = new EntityFlare(level, getStats().getInitHeat(), getStats().getMaxAge(), 3);
		flareRight.setPos(getParent().position()
				.add(getRelPos())
				.add(rightX * sideOffset + downX * downOffset,
					 rightY * sideOffset + downY * downOffset + rand.nextDouble() * 0.2,
					 rightZ * sideOffset + downZ * downOffset));
		double randomVelR = sideVelocity + (rand.nextDouble() - 0.5) * 0.1;
		flareRight.setDeltaMovement(getParent().getDeltaMovement().add(
				rightX * randomVelR + downX * 0.1 + (rand.nextDouble() - 0.5) * 0.05,
				rightY * randomVelR + downY * 0.1 + (rand.nextDouble() - 0.5) * 0.05,
				rightZ * randomVelR + downZ * 0.1 + (rand.nextDouble() - 0.5) * 0.05));
		level.addFreshEntity(flareRight);

		// Left flare
		EntityFlare flareLeft = new EntityFlare(level, getStats().getInitHeat(), getStats().getMaxAge(), 3);
		flareLeft.setPos(getParent().position()
				.add(getRelPos())
				.add(-rightX * sideOffset + downX * downOffset,
					 -rightY * sideOffset + downY * downOffset + rand.nextDouble() * 0.2,
					 -rightZ * sideOffset + downZ * downOffset));
		double randomVelL = sideVelocity + (rand.nextDouble() - 0.5) * 0.1;
		flareLeft.setDeltaMovement(getParent().getDeltaMovement().add(
				-rightX * randomVelL + downX * 0.1 + (rand.nextDouble() - 0.5) * 0.05,
				-rightY * randomVelL + downY * 0.1 + (rand.nextDouble() - 0.5) * 0.05,
				-rightZ * randomVelL + downZ * 0.1 + (rand.nextDouble() - 0.5) * 0.05));
		level.addFreshEntity(flareLeft);

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

	private static final PartItemLoadRecipe<?> LOAD_RECIPE = new FlareDispenserLoadRecipe(
			new ResourceLocation("dscombat:flare_load_recipe"));

	@Override
	public PartItemLoadRecipe<?> getLoadRecipe() {
		return LOAD_RECIPE;
	}

	@Override
	public PartItemUnloadRecipe<?> getUnloadRecipe() {
		return null;
	}

	@Override
	public void addToolTips(List<Component> tips, TooltipFlag isAdvanced) {
		super.addToolTips(tips, isAdvanced);
		tips.add(UtilMCText.translatable("info.dscombat.ammo")
				.append(" "+(int)getCurrentAmmo()+"/"+(int)getMaxAmmo())
				.setStyle(Style.EMPTY.withColor(0xAAAAAA)));
	}
}
