package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.init.ModEntities;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public abstract class EntityAbstractWeapon extends Projectile {
	
	public double speed = 1;
	
	public EntityAbstractWeapon(EntityType<? extends EntityAbstractWeapon> type, Level level) {
		super(type, level);
	}
	
	public EntityAbstractWeapon(Level level, Entity owner) {
		super(ModEntities.BULLET.get(), level);
		this.setOwner(owner);
	}

	@Override
	protected void defineSynchedData() {
		
	}
	
	@Override
	public void tick() {
		super.tick();
		if (this.tickCount > 100) { kill(); return; }
		if (Double.isNaN(getDeltaMovement().length())) {
            setDeltaMovement(Vec3.ZERO);
        }
		System.out.println(this);
		move(MoverType.SELF, getDeltaMovement());
		setPacketCoordinates(this.getX(), this.getY(), this.getZ());
	}
	
	public void shoot(Vec3 pos, Vec3 direction) {
		this.setPos(pos);
		this.setDeltaMovement(direction.scale(speed));
	}
	
	public void shoot(Vec3 pos, Vec3 direction, Vec3 initMove) {
		this.setPos(pos);
		this.setDeltaMovement(initMove.add(direction.scale(speed)));
	}
	
	@Override
	public void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		System.out.println("BULLET HIT "+result.getBlockPos());
		this.kill();
	}
	
	@Override
	public void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		System.out.println("BULLET HIT "+result.getEntity());
		this.kill();
	}

}
