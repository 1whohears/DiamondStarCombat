package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.init.ModEntities;
import com.onewhohears.dscombat.init.ModParticles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class EntityFlare extends Entity {
	
	private float heat;
	private int age;
	private float pow;
	private float decay;
	
	public EntityFlare(EntityType<? extends EntityFlare> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}
	
	public EntityFlare(Level level, float initHeat, int maxAge, float order) {
		this(ModEntities.FLARE.get(), level);
		this.heat = initHeat;
		this.age = maxAge;
		this.pow = order;
		calcDecay();
	}
	
	private void calcDecay() {
		if (heat == 0 || age == 0 || pow == 0) return;
		this.decay = heat / (float) Math.pow(age, pow);
	}

	@Override
	protected void defineSynchedData() {
		
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		if (heat != 0 && age != 0 && pow != 0) return;
		heat = tag.getFloat("heat");
		age = tag.getInt("age");
		pow = tag.getFloat("pow");
		calcDecay();
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	public float getHeat() {
		return heat - (float) Math.pow(tickCount, pow) * decay;
	}
	
	@Override
	public void tick() {
		super.tick();
		Vec3 move = getDeltaMovement().scale(0.999).add(0, -0.01, 0);
		if (move.y < -0.2) move = new Vec3(move.x, -0.2, move.z);
		setDeltaMovement(move);
		if (!level.isClientSide && tickCount > age) {
			discard();
		}
		if (level.isClientSide && tickCount % 2 == 0) {
			particle();
		}
		move(MoverType.SELF, getDeltaMovement());
	}
	
	private void particle() {
		Vec3 move = getDeltaMovement();
		level.addParticle(ModParticles.FLARE.get(), 
				getX(), getY(), getZ(), 
				move.x + random.nextGaussian() * 0.0001D, 
				move.y + random.nextGaussian() * 0.0001D, 
				move.z + random.nextGaussian() * 0.0001D);
	}

}
