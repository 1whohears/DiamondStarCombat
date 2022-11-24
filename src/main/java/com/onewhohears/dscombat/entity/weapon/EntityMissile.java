package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ClientBoundMissileMovePacket;
import com.onewhohears.dscombat.data.ChunkManager;
import com.onewhohears.dscombat.data.weapon.MissileData;
import com.onewhohears.dscombat.data.weapon.MissileData.TargetType;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.init.ModSounds;
import com.onewhohears.dscombat.util.UtilClientSafeSoundInstance;
import com.onewhohears.dscombat.util.UtilEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

public class EntityMissile extends EntityBullet {
	
	public Entity parent;
	public Entity target;
	public Vec3 targetPos;
	
	public EntityMissile(EntityType<?> type, Level level) {
		super(type, level);
	}
	
	public EntityMissile(Level level, Entity owner, MissileData data) {
		super(level, owner, data);
	}
	
	/*private EntityMissile(EntityType<? extends EntityMissile> type, Level level, Entity owner, MissileData data, ResourceLocation texture) {
		this(type, level, texture);
		this.setOwner(owner);
		this.setWeaponData(data);
	}*/
	
	@Override
	public void tick() {
		//System.out.println("ROCKET "+this.tickCount+" "+this.level);
		//System.out.println("target = "+target); // target entity is not set on client side
		MissileData data = (MissileData)getWeaponData();
		if (!this.level.isClientSide) {
			data.tickGuide(this);
			if (target != null && data.getTargetType() != TargetType.POS) {
				if (this.distanceTo(target) <= data.getFuseDist()) {
					//System.out.println("WITHIN FUSE DISTANCE");
					//System.out.println("IS REMOVED "+this.isRemoved());
					this.setPos(target.position());
					this.checkExplode();
					this.discard();
					if (target instanceof EntityMissile) {
						target.kill();
					}
					//System.out.println("close enough");
				}
			}
			if (targetPos != null) loadChunks();
			if (tickCount % 10 == 0) PacketHandler.INSTANCE.send(
					PacketDistributor.TRACKING_ENTITY.with(() -> this), 
					new ClientBoundMissileMovePacket(getId(), position(), 
							getDeltaMovement(), getXRot(), getYRot(), targetPos));
		}
		if (this.level.isClientSide /*&& this.tickCount % 2 == 0*/) {
			Vec3 move = getDeltaMovement();
			level.addParticle(ParticleTypes.SMOKE, 
					getX(), getY(), getZ(), 
					-move.x * 0.5D + random.nextGaussian() * 0.05D, 
					-move.y * 0.5D + random.nextGaussian() * 0.05D, 
					-move.z * 0.5D + random.nextGaussian() * 0.05D);
			data.clientGuidance(this);
		}
		engineSound();
		super.tick();
		//System.out.println("pos = "+position());
		//System.out.println("vel = "+getDeltaMovement());
		//System.out.println("pit = "+getXRot()+" yaw = "+getYRot());
	}
	
	private void engineSound() {
		if (!this.level.isClientSide) return;
		if (this.tickCount == 8) {
			UtilClientSafeSoundInstance.dopplerSound(Minecraft.getInstance(), this, ModSounds.MISSILE_ENGINE_1.get(), 
					0.8F, 1.0F, 10F);
		}
	}
	
	private void loadChunks() {
		ChunkPos cp = this.chunkPosition();
		ChunkManager.addChunk(this, cp.x, cp.z);
		Vec3 move = this.getDeltaMovement();
		Vec3 next = position().add(move);
		ChunkPos ncp = level.getChunk(new BlockPos(next)).getPos();
		if (ncp.x != cp.x || ncp.z != cp.z)
			ChunkManager.addChunk(this, ncp.x, ncp.z);
	}
	
	@Override
	protected void motion() {
		MissileData data = (MissileData)getWeaponData();
		Vec3 cm = getDeltaMovement();
		double B = 0.004d * UtilEntity.getAirPressure(getY());
		double bleed = B * (Math.abs(getXRot()-xRotO)+Math.abs(getYRot()-yRotO));
		double vel = cm.length() + data.getAcceleration() - bleed;
		double max = data.getSpeed();
		if (vel > max) vel = max;
		else if (vel < 0) vel = 0;
		Vec3 nm = getLookAngle().scale(vel);
		setDeltaMovement(nm);
	}
	
	@Override
	public WeaponData getDefaultData() {
		return WeaponPresets.getDefaultMissile();
	}
	
	public float getHeat() {
		return 2f;
	}
	
	@Override
    public boolean hurt(DamageSource source, float amount) {
		/*System.out.println("MISSILE GOT HURT!");
		System.out.println("ENTITY "+source.getEntity());
		System.out.println("DIRECT ENTITY "+source.getDirectEntity());*/
		if (this.isRemoved()) return false;
		if (this.equals(source.getDirectEntity())) return false;
		this.checkExplode();
		this.discard();
		return true;
	}
	
	/*@Override
	public boolean ignoreExplosion() {
		return false;
	}*/

}
