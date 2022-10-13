package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.common.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ClientBoundMissileMovePacket;
import com.onewhohears.dscombat.data.ChunkManager;
import com.onewhohears.dscombat.data.MissileData;
import com.onewhohears.dscombat.data.MissileData.GuidanceType;
import com.onewhohears.dscombat.data.MissileData.TargetType;
import com.onewhohears.dscombat.data.WeaponData;
import com.onewhohears.dscombat.entity.aircraft.plane.EntityTestPlane;
import com.onewhohears.dscombat.init.ModEntities;

import net.minecraft.client.model.EntityModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

public class EntityMissile extends EntityBullet {
	
	private static final ResourceLocation TEXTURE_MISSILE1 = new ResourceLocation(DSCombatMod.MODID, "textures/entities/missile1.png");
	public static EntityModel<EntityTestPlane> MODEL_MISSILE1 = null;
	
	public Entity parent;
	public Entity target;
	public Vec3 targetPos;
	
	public EntityMissile(EntityType<? extends EntityMissile> type, Level level) {
		super(type, level);
	}
	
	public EntityMissile(Level level, Entity owner, MissileData data) {
		this(ModEntities.MISSILE.get(), level);
		this.setOwner(owner);
		this.setWeaponData(data);
	}
	
	@Override
	public void tick() {
		//System.out.println("ROCKET "+this.tickCount+" "+this.level);
		//System.out.println("target = "+target); // target entity is not set on client side
		MissileData data = (MissileData)getWeaponData();
		if (!this.level.isClientSide) {
			data.tickGuide(this);
			if (target != null && data.getTargetType() != TargetType.POS) {
				if (this.distanceTo(target) < data.getFuseDist()) {
					this.setPos(target.position());
					this.checkExplode();
					this.discard();
					//System.out.println("close enough");
				}
			}
			if (targetPos != null) loadChunks();
			PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), 
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
		motion(data);
		super.tick();
		//System.out.println("pos = "+position());
		//System.out.println("vel = "+getDeltaMovement());
		//System.out.println("pit = "+getXRot()+" yaw = "+getYRot());
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
	
	public void motion(MissileData data) {
		Vec3 cm = getDeltaMovement();
		double B = 0.001d;
		double bleed = B * (Math.abs(getXRot()-xRotO)+Math.abs(getYRot()-yRotO));
		double vel = cm.length() + data.getAcceleration() - bleed;
		double max = data.getSpeed();
		if (vel > max) vel = max;
		else if (vel < 0) vel = 0;
		Vec3 nm = getLookAngle().scale(vel);
		setDeltaMovement(nm);
	}
	
	@Override
	public ResourceLocation getTexture() {
		return TEXTURE_MISSILE1;
	}
	
	@Override
	public EntityModel<?> getModel() {
		return MODEL_MISSILE1;
	}
	
	@Override
	public WeaponData getDefaultData() {
		return new MissileData("default_missile", Vec3.ZERO,
				0, 0, 0, 0, 0, 0, 
				false, false, false, 0, 0,
				TargetType.POS, GuidanceType.IR,
				0, 0, 0, -1);
	}
	
	public float getHeat() {
		return 2f;
	}
	
	@Override
    public boolean hurt(DamageSource source, float amount) {
		this.checkExplode();
		return true;
	}

}
