package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ClientBoundMissileMovePacket;
import com.onewhohears.dscombat.data.ChunkManager;
import com.onewhohears.dscombat.data.weapon.MissileData;
import com.onewhohears.dscombat.data.weapon.MissileData.GuidanceType;
import com.onewhohears.dscombat.data.weapon.MissileData.TargetType;
import com.onewhohears.dscombat.data.weapon.WeaponData;
import com.onewhohears.dscombat.init.ModEntities;

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
	
	public static final ResourceLocation TEXTURE_MISSILE1 = new ResourceLocation(DSCombatMod.MODID, "textures/entities/missile1.png");
	public static final ResourceLocation TEXTURE_MISSILE2 = new ResourceLocation(DSCombatMod.MODID, "textures/entities/missile2.png");
	public static final ResourceLocation TEXTURE_MISSILE3 = new ResourceLocation(DSCombatMod.MODID, "textures/entities/missile3.png");
	
	public static EntityMissile missile(Level level, Entity owner, MissileData data) {
		switch (data.getId()) {
		case "gbu": return new EntityMissile(ModEntities.MISSILE1.get(), level, owner, data, TEXTURE_MISSILE1);
		case "fox3_1": return new EntityMissile(ModEntities.MISSILE2.get(), level, owner, data, TEXTURE_MISSILE2);
		case "fox2_1": return new EntityMissile(ModEntities.MISSILE3.get(), level, owner, data, TEXTURE_MISSILE3);
		}
		return new EntityMissile(ModEntities.MISSILE1.get(), level, owner, data, TEXTURE_MISSILE1);
	}
	
	private final ResourceLocation TEXTURE;
	
	public Entity parent;
	public Entity target;
	public Vec3 targetPos;
	
	public EntityMissile(EntityType<? extends EntityMissile> type, Level level, ResourceLocation texture) {
		super(type, level);
		TEXTURE = texture;
		//System.out.println("NEW MISSILE "+level+" "+texture);
	}
	
	private EntityMissile(EntityType<? extends EntityMissile> type, Level level, Entity owner, MissileData data, ResourceLocation texture) {
		this(type, level, texture);
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
					if (target.getType().equals(ModEntities.MISSILE1.get())) {
						target.kill();
					}
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
	
	@Override
	protected void motion() {
		MissileData data = (MissileData)getWeaponData();
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
		return TEXTURE;
	}
	
	@Override
	public WeaponData getDefaultData() {
		return new MissileData("default_missile", Vec3.ZERO,
				0, 0, 0, false, 0, 0, 0, 
				false, false, false, 0, 0,
				TargetType.POS, GuidanceType.IR,
				0, 0, 0, -1);
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
		if (source.getDirectEntity().equals(this)) return false;
		this.checkExplode();
		this.discard();
		return true;
	}
	
	/*@Override
	public boolean ignoreExplosion() {
		return false;
	}*/

}
