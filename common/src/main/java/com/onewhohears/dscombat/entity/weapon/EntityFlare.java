package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.entity.IREmitter;
import com.onewhohears.dscombat.init.ModEntities;
import com.onewhohears.dscombat.init.ModParticles;
import com.onewhohears.onewholibs.util.UtilEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class EntityFlare extends Entity implements IREmitter {
	
	private float heat;
	private int age;
	private float pow;
	private float decay;
	private boolean spawnedInitialBurst = false;
	
	public EntityFlare(EntityType<? extends EntityFlare> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.heat = 20;
		this.age = 120;
		this.pow = 3;
		calcDecay();
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
	public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
		return new ClientboundAddEntityPacket(this);
	}
	
	@Override
	public void tick() {
		super.tick();
		Vec3 move = getDeltaMovement().scale(0.999).add(0, -0.01, 0);
		if (move.y < -0.2) move = new Vec3(move.x, -0.2, move.z);
		setDeltaMovement(move);
		if (!isClientSide() && tickCount > age) {
			discard();
		}
		if (isClientSide()) {
			// Spawn initial burst on first tick
			if (!spawnedInitialBurst && tickCount == 1) {
				spawnLaunchBurst();
				spawnedInitialBurst = true;
			}
			// More frequent and colorful particles
			spawnColorfulParticles();
		}
		move(MoverType.SELF, getDeltaMovement());
	}

    public boolean isClientSide() {
        return getWorld().isClientSide();
    }

    public Level getWorld() {
        return UtilEntity.getLevel(this);
    }
	
	private void particle() {
		Vec3 move = getDeltaMovement();
        getWorld().addParticle(ModParticles.FLARE.get(),
				getX(), getY(), getZ(), 
				move.x + random.nextGaussian() * 0.0001D, 
				move.y + random.nextGaussian() * 0.0001D, 
				move.z + random.nextGaussian() * 0.0001D);
	}
	
	private void spawnColorfulParticles() {
		Vec3 move = getDeltaMovement();
		double spread = 0.05;
		
		// Main bright flare particles (1-2 per tick)
		if (tickCount % 2 == 0) {
			getWorld().addParticle(ModParticles.FLARE.get(),
					getX() + (random.nextDouble() - 0.5) * 0.1, 
					getY() + (random.nextDouble() - 0.5) * 0.1, 
					getZ() + (random.nextDouble() - 0.5) * 0.1,
					move.x + (random.nextDouble() - 0.5) * spread, 
					move.y + (random.nextDouble() - 0.5) * spread * 0.5, 
					move.z + (random.nextDouble() - 0.5) * spread);
		}
		
		// Continuous trail effect - leave particles behind
		if (tickCount % 3 == 0) {
			// Bright trail particle
			getWorld().addParticle(ModParticles.FLARE.get(),
					getX(), getY(), getZ(),
					0, 0, 0);
		}
		
		// Smoke trail particles (less frequent)
		if (tickCount % 4 == 0) {
			getWorld().addParticle(net.minecraft.core.particles.ParticleTypes.SMOKE,
					getX() + (random.nextDouble() - 0.5) * 0.2, 
					getY() + (random.nextDouble() - 0.5) * 0.2, 
					getZ() + (random.nextDouble() - 0.5) * 0.2,
					(random.nextDouble() - 0.5) * 0.02, 
					0.01 + random.nextDouble() * 0.02, 
					(random.nextDouble() - 0.5) * 0.02);
		}
		
		// Spark particles (rare)
		if (random.nextFloat() < 0.15f) {
			getWorld().addParticle(net.minecraft.core.particles.ParticleTypes.LAVA,
					getX(), getY(), getZ(),
					(random.nextDouble() - 0.5) * 0.1, 
					(random.nextDouble() - 0.5) * 0.1, 
					(random.nextDouble() - 0.5) * 0.1);
		}
	}
	
	private void spawnLaunchBurst() {
		// Initial explosion burst when flare is launched
		for (int i = 0; i < 15; i++) {
			double angle = random.nextDouble() * Math.PI * 2;
			double speed = 0.1 + random.nextDouble() * 0.15;
			double vx = Math.cos(angle) * speed;
			double vz = Math.sin(angle) * speed;
			double vy = random.nextDouble() * 0.1;
			
			// Bright flare particles
			getWorld().addParticle(ModParticles.FLARE.get(),
					getX(), getY(), getZ(),
					vx, vy, vz);
		}
		
		// Add some smoke to the burst
		for (int i = 0; i < 8; i++) {
			getWorld().addParticle(net.minecraft.core.particles.ParticleTypes.LARGE_SMOKE,
					getX() + (random.nextDouble() - 0.5) * 0.3, 
					getY() + (random.nextDouble() - 0.5) * 0.3, 
					getZ() + (random.nextDouble() - 0.5) * 0.3,
					(random.nextDouble() - 0.5) * 0.05, 
					random.nextDouble() * 0.05, 
					(random.nextDouble() - 0.5) * 0.05);
		}
		
		// Add some flame particles
		for (int i = 0; i < 5; i++) {
			getWorld().addParticle(net.minecraft.core.particles.ParticleTypes.FLAME,
					getX(), getY(), getZ(),
					(random.nextDouble() - 0.5) * 0.1, 
					random.nextDouble() * 0.1, 
					(random.nextDouble() - 0.5) * 0.1);
		}
	}

	@Override
	public float getIRHeat() {
		return heat - (float) Math.pow(tickCount, pow) * decay;
	}

}
