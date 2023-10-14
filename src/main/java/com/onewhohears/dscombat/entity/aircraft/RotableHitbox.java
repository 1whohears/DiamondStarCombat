package com.onewhohears.dscombat.entity.aircraft;

import java.util.List;
import java.util.function.Predicate;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.util.math.RotableAABB;
import com.onewhohears.dscombat.util.math.UtilAngles;
import com.onewhohears.dscombat.util.math.UtilGeometry;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;

public class RotableHitbox extends PartEntity<EntityVehicle> {
	
	private final String name;
	private final RotableAABB hitbox;
	private final EntityDimensions size;
	private final Vec3 rel_pos;
	
	public RotableHitbox(EntityVehicle parent, String name, Vector3f size, Vec3 rel_pos) {
		super(parent);
		this.name = name;
		this.hitbox = new RotableAABB(size.x(), size.y(), size.z());
		this.size = hitbox.getMaxDimensions();
		this.rel_pos = rel_pos;
		this.noPhysics = true;
		this.blocksBuilding = true;
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
		Quaternion q = getParent().getQBySide();
		Vec3 pos = getParent().position().add(UtilAngles.rotateVector(getRelPos(), q));
		setPos(pos);
		hitbox.setCenter(UtilGeometry.convertVector(pos));
	}
	
	protected void positionEntities() {
		Vec3 parent_move = getParent().getDeltaMovement();
		Vec3 parent_rot_rate = getParent().getAngularVel();
		Quaternion rot = getParent().getQBySide();
		List<Entity> list = level.getEntities(this, getBoundingBox(), canMoveEntity());
		//System.out.println("collision list size = "+list.size());
		System.out.println("client side?"+level.isClientSide);
		for (Entity entity : list) {
			/**
			 * FIXME 4 this custom collision code works somewhat but lots of issues still
			 * are there performance issues? my laptop frame rate drops when an aircraft carrier exists
			 * the translations from vehicle rotations probably only work well in the y axis
			 * can't place anything on the platform
			 * when the vehicle moves or rotates, the passengers experience chopy movement
			 * when the chunks load, entities that were on the platform may start falling before platform loads
			 */
			AABB entity_bb = entity.getBoundingBox();
			Vec3 entity_pos = entity.position();
			Vec3 entity_feet = UtilGeometry.getBBFeet(entity_bb);
			Vec3 entity_move = entity.getDeltaMovement();
			Vec3 collide = hitbox.getCollideMovePos(entity_feet, 
					entity_move, rot, parent_move, parent_rot_rate);
			//System.out.println("collide "+(collide!=null)+" "+entity);
			if (collide == null) continue;
			System.out.println("colliding "+entity);
			double dy = entity_pos.y-entity_bb.minY;
			entity.setPos(collide.add(0, dy, 0));
			entity.setYRot(entity.getYRot()-(float)parent_rot_rate.y);
			// set is colliding
			entity.causeFallDamage(entity.fallDistance, 1, DamageSource.FALL);
			entity.resetFallDistance();
			entity.setOnGround(true);
			entity.verticalCollision = true;
			entity.verticalCollisionBelow = true;
			entity.causeFallDamage(entity.fallDistance, 1, DamageSource.FALL);
			entity.resetFallDistance();
			// prevent moving down
			if (entity_move.y < 0) entity_move = entity_move.multiply(1, 0, 1);
			entity.setDeltaMovement(entity_move);
			if (entity instanceof EntityVehicle plane) {
				Vec3 forces = plane.getForces();
				if (forces.y < 0) forces = forces.multiply(1, 0, 1);
				plane.setForces(forces);
			}
		}
	}
	
	public Predicate<? super Entity> canMoveEntity() {
		return (entity) -> {
			if (entity.noPhysics) return false;
			if (!entity.canCollideWith(getParent())) return false;
			if (entity.isPassenger()) return false;
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
