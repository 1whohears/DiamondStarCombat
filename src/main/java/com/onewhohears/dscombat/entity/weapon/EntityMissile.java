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
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

public class EntityMissile extends EntityBullet {
	
	private static final ResourceLocation TEXTURE_MISSILE1 = new ResourceLocation(DSCombatMod.MODID, "textures/entities/missile1.png");
	public static EntityModel<EntityTestPlane> MODEL_MISSILE1 = null;
	
	public Entity parent;
	public Entity target;
	public Vec3 targetPos = Vec3.ZERO;
	
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
			loadChunks();
			data.tickGuide(this);
			motionClamp();
			if (target != null && data.getTargetType() != TargetType.POS) {
				if (this.distanceTo(target) < data.getFuseDist()) {
					this.setPos(target.position());
					this.checkExplode();
					this.discard();
					//System.out.println("close enough");
				}
			}
			// TODO missile doesn't synch with client or looks glitchy
			PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), 
					new ClientBoundMissileMovePacket(this.getId(), this.position(), 
							this.getDeltaMovement(), this.getXRot(), this.getYRot(), this.targetPos));
		}
		super.tick();
		if (this.level.isClientSide /*&& this.tickCount % 2 == 0*/) {
			Vec3 move = getDeltaMovement();
			level.addParticle(ParticleTypes.SMOKE, 
					getX(), getY(), getZ(), 
					-move.x * 0.5D + random.nextGaussian() * 0.05D, 
					-move.y * 0.5D + random.nextGaussian() * 0.05D, 
					-move.z * 0.5D + random.nextGaussian() * 0.05D);
			data.clientGuidance(this);
		}
		//System.out.println("pos = "+position());
		//System.out.println("vel = "+getDeltaMovement());
		//System.out.println("pit = "+getXRot()+" yaw = "+getYRot());
	}
	
	private void loadChunks() {
		int cx = this.chunkPosition().x;
		int cz = this.chunkPosition().z;
		/*int r = 1;
		for (int x = cx-r; x < cx+r+1; ++x) {
			for (int z = cz-r; z < cz+r+1; ++z) {
				ChunkManager.addChunk(this, x, z);
			}
		}*/
		// TODO remove chunk loads that the missile isn't going towards
		ChunkManager.addChunk(this, cx, cz);
		Vec3 move = this.getDeltaMovement();
		if (move.x > 0) ChunkManager.addChunk(this, cx+1, cz);
		else if (move.x < 0) ChunkManager.addChunk(this, cx-1, cz);
		if (move.z > 0) ChunkManager.addChunk(this, cx, cz+1);
		else if (move.z < 0) ChunkManager.addChunk(this, cx, cz-1);
		if (Math.abs(move.x) > 0 && Math.abs(move.z) > 0) {
			ChunkManager.addChunk(this, cx+(int)Math.signum(move.x), cz+(int)Math.signum(move.z));
		}
	}
	
	public void motionClamp() {
		Vec3 motion = getDeltaMovement();
		double vel = motion.length();
		double max = 2d;
		if (vel > max) motion = motion.scale(max / vel);
		setDeltaMovement(motion);
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

}
