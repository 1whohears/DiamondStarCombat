package com.onewhohears.dscombat.entity.aircraft;

import com.onewhohears.dscombat.util.math.RotableAABB;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;

public class EntityPlatform extends PartEntity<EntityAircraft> {
	
	private final RotableAABB hitbox;
	
	public EntityPlatform(EntityAircraft parent, Vec3 size) {
		super(parent);
		hitbox = new RotableAABB(size.x, size.y, size.z);
	}
	
	@Override
	public boolean isPickable() {
		return false;
	}

	@Override
	protected void defineSynchedData() {
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag nbt) {
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag nbt) {
	}

}
