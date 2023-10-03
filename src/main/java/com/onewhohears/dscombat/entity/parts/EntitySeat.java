package com.onewhohears.dscombat.entity.parts;

import javax.annotation.Nullable;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.data.parts.PartData.PartType;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class EntitySeat extends EntityVehiclePart {
	
	public final Vec3 passengerOffset;
	
	private Entity passenger;
	
	public EntitySeat(EntityAircraft parent, String modelId, EntityDimensions size, String slotId, Vec3 pos, float z_rot, Vec3 offset) {
		super(parent, modelId, size, slotId, pos, z_rot);
		this.passengerOffset = offset;
	}
	
	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		if (player.isSecondaryUseActive()) {
			return InteractionResult.PASS;
		} else if (!level.isClientSide) {
			if (getParent().ridePreferredSeat(player, getSlotId())) return InteractionResult.CONSUME;
			return InteractionResult.PASS;
		}
		return InteractionResult.SUCCESS;
	}
	
	@Override
	public boolean isPickable() {
		return true;
	}
	
	@Override
	public void tick() {
		super.tick();
		positionPassenger();
	}
	
	protected void positionPassenger() {
		Entity passenger = getPassenger();
		if (passenger == null) return;
		Quaternion q;
		if (level.isClientSide) q = getParent().getClientQ();
		else q = getParent().getQ();
		double offset = getPassengersRidingOffset() + passenger.getMyRidingOffset() + passenger.getEyeHeight();
		Vec3 rel = UtilAngles.rotateVector(new Vec3(0, offset, 0), q)
				.subtract(0, passenger.getEyeHeight(), 0);
		passenger.setPos(position().add(rel));
	}
	
	public boolean setPassenger(Entity passenger) {
		if (isOccupied()) return false;
		this.passenger = passenger;
		return true;
	}
	
	public void removePassenger() {
		this.passenger =  null;
	}
	
	@Nullable
	public Player getPlayer() {
		if (passenger instanceof Player p) return p;
		return null;
	}
	
	@Nullable
	public Entity getPassenger() {
		return passenger;
	}
	
	public boolean isOccupied() {
		return passenger != null;
	}
	
	public boolean isVacant() {
		return passenger == null;
	}
	
	@Nullable
	@Override
    public Entity getControllingPassenger() {
		Player p = getPlayer();
		if (p == null) return super.getControllingPassenger();
		return p;
	}
    
    @Override
    public double getPassengersRidingOffset() {
        return passengerOffset.y;
    }

	@Override
	public boolean shouldRender() {
		return false;
	}
	
	@Override
    public boolean hurt(DamageSource source, float amount) {
		if (source.isExplosion() || source.isMagic()) return true;
		getParent().hurt(source, amount);
		return true;
	}

	@Override
	public PartType getPartType() {
		return PartType.SEAT;
	}
	
	@Override
	public boolean fireImmune() {
		return true;
	}

	@Override
	public boolean canGetHurt() {
		return false;
	}

}
