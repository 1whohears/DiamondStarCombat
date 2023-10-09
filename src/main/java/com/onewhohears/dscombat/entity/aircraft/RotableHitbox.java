package com.onewhohears.dscombat.entity.aircraft;

import java.util.List;
import java.util.function.Predicate;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.util.math.RotableAABB;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;

public class RotableHitbox extends PartEntity<EntityAircraft> {
	
	private final String name;
	private final RotableAABB hitbox;
	private final EntityDimensions size;
	private final Vec3 rel_pos;
	private final float precision;
	
	// FIXME 5 the hitbox shouldn't be an entity cause it would probably cause performance issues. will have to create a custom collision system...pain
	
	public RotableHitbox(EntityAircraft parent, String name, Vec3 size, Vec3 rel_pos, float precision) {
		super(parent);
		this.name = name;
		this.hitbox = new RotableAABB(size.x, size.y, size.z);
		this.size = hitbox.getMaxDimensions();
		this.rel_pos = rel_pos;
		this.precision = precision;
		this.noPhysics = true;
		refreshDimensions();
	}
	
	@Override
	public void tick() {
		positionSelf();
		positionEntities();
		firstTick = false;
	}
	
	protected void positionSelf() {
		setOldPosAndRot();
		Quaternion q;
		if (level.isClientSide) q = getParent().getClientQ();
		else q = getParent().getQ();
		Vec3 pos = getParent().position().add(UtilAngles.rotateVector(getRelPos(), q));
		setPos(pos);
		hitbox.setCenter(pos);
		hitbox.setRot(q);
	}
	
	protected void positionEntities() {
		Vec3 parent_move = getParent().getDeltaMovement();
		Vec3 parent_rot = getParent().getAngularVel();
		List<Entity> list = level.getEntities(this, getBoundingBox(), canMoveEntity());
		for (Entity entity : list) {
			//fix position
			Vec3 collide = hitbox.getCollidePos(entity.getBoundingBox().expandTowards(entity.getDeltaMovement()), 0.1);
			if (collide == null) continue;
			entity.resetFallDistance();
			entity.setOnGround(true);
			entity.setPos(collide.add(parent_move));
			// change speed
			Vec3 entity_move = entity.getDeltaMovement();
			if (entity_move.y < 0) entity_move = entity_move.multiply(1, 0, 1);
			//entity_move = entity_move.add(parent_move);
			/*entity_move.add(hitbox.getTangetVel(
				entity.position().subtract(hitbox.getCenter()), 
				parent_rot));*/
			entity.setDeltaMovement(entity_move);
			//System.out.println("collide "+entity+" "+entity_move);
		}
	}
	
	public Predicate<? super Entity> canMoveEntity() {
		return (entity) -> {
			if (entity.noPhysics) return false;
			if (entity.equals(getParent())) return false;
			if (entity.getRootVehicle().equals(getParent())) return false;
			return true;
		};
	}
	
	@Override
    public boolean hurt(DamageSource source, float amount) {
    	return getParent().hurt(source, amount);
    }
	
	@Override
    protected AABB makeBoundingBox() {
    	EntityDimensions d = getDimensions(getPose());
    	if (d == null) return super.makeBoundingBox();
    	double pX = getX(), pY = getY(), pZ = getZ();
    	double f = d.width / 2.0F;
        double f1 = d.height / 2.0F;
        return new AABB(pX-f, pY-f1, pZ-f, 
        		pX+f, pY+f1, pZ+f);
    }
	
	public String getHitboxName() {
		return name;
	}
	
	public RotableAABB getHitbox() {
		return hitbox;
	}
	
	public Vec3 getRelPos() {
		return rel_pos;
	}
	
	public float getPrecision() {
		return precision;
	}
	
	@Override
	public boolean isPickable() {
		return false;
	}
	
	@Override
    public boolean isPushable() {
    	return false;
    }
    
    @Override
    public boolean isPushedByFluid() {
    	return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }
    
    @Override
    public boolean canCollideWith(Entity entity) {
    	return false;
    }
	
    @Override
	public EntityDimensions getDimensions(Pose pPose) {
		return size;
	}
    
	@Override
	public boolean is(Entity entity) {
		return this == entity || getParent() == entity;
	}
	
	@Override
	public Packet<?> getAddEntityPacket() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean shouldBeSaved() {
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
