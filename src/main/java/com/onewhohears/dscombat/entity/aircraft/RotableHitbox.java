package com.onewhohears.dscombat.entity.aircraft;

import java.util.List;
import java.util.function.Predicate;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
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
import net.minecraft.world.phys.shapes.VoxelShape;
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
		//this.size = hitbox.getMaxDimensions();
		this.size = EntityDimensions.scalable(1f, 1f);
		this.rel_pos = rel_pos;
		this.noPhysics = true;
		// this.blocksBuilding = true;
		refreshDimensions();
	}
	
	@Override
	public void tick() {
		positionSelf();
		//positionEntities();
		firstTick = false;
	}
	
	protected void positionSelf() {
		setOldPosAndRot();
		Quaternion q = getParent().getQBySide();
		Vec3 pos = getParent().position().add(UtilAngles.rotateVector(getRelPos(), q));
		setPos(pos);
		//hitbox.setCenter(UtilGeometry.convertVector(pos));
		hitbox.updateCollider(pos, q);
		//System.out.println("AABB="+getBoundingBox().toString()+" "+this);
		
	}
	
	public void handlePosibleCollision(List<VoxelShape> colliders, Entity entity, AABB aabb, Vec3 move) {
		if (!couldCollide(entity) || !hitbox.isColliding(aabb)) return;
		//System.out.println("HANDLE COLLISION "+entity+" "+move);
		//CollisionData data = new CollisionData();
		hitbox.addColliders(colliders, aabb);
		/*if (data.dir.getAxis().isVertical()) {
			entity.setOnGround(true);
			entity.verticalCollision = true;
			entity.verticalCollisionBelow = true;
			entity.causeFallDamage(entity.fallDistance, 1, DamageSource.FALL);
			entity.resetFallDistance();
			entity_move.add(yMoveBefore*data.normal.x(), 0, yMoveBefore*data.normal.z()); // prevents slide
		}
		if (data.dir.getAxis().isHorizontal()) {
			entity.horizontalCollision = true;
			// horizontal collide damage
		}*/
		// change movement based on collision
		//entity.setDeltaMovement(move.subtract(move.multiply(data.normal)));
	}
	
	/*protected void positionEntities() {
		Vec3 parent_move = getParent().getDeltaMovement();
		Vec3 parent_rot_rate = getParent().getAngularVel();
		Quaternion rot = getParent().getQBySide();
		List<Entity> list = level.getEntities(this, getBoundingBox(), getCouldCollideTest());
		//System.out.println("collision list size = "+list.size());
		//System.out.println("client side?"+level.isClientSide);
		for (Entity entity : list) {
			CollisionData data = new CollisionData();
			Vec3 entity_pos = entity.position();
			Vector3f entity_move = UtilGeometry.convertVector(entity.getDeltaMovement());
			// projectile collide
			if (entity instanceof Projectile pro) {
				Vec3 collide = hitbox.getCollidePos(entity_pos, entity_move, rot, data);
				if (collide == null) continue;
				boolean canHit = pro.canHitEntity(getParent());
				if (!canHit) continue;
				pro.setPos(collide);
				EntityHitResult hit = new EntityHitResult(this, collide);
				pro.onHitEntity(hit);
				continue;
			}
			// other entity collide
			float yMoveBefore = entity_move.y();
			AABB entity_bb = entity.getBoundingBox();
			Vec3 entity_feet = UtilGeometry.getBBFeet(entity_bb);
			Vec3 collide = hitbox.getCollideMovePos(entity_feet, 
					entity_move, rot, parent_move, parent_rot_rate, data);
			//System.out.println("collide "+(collide!=null)+" "+entity);
			if (collide == null) continue;
			//System.out.println("colliding "+entity);
			double dy = entity_pos.y-entity_bb.minY;
			entity.setPos(collide.add(0, dy, 0));
			entity.setYRot(entity.getYRot()-(float)parent_rot_rate.y);
			// set is colliding
			if (data.dir.getAxis().isVertical()) {
				entity.setOnGround(true);
				entity.verticalCollision = true;
				entity.verticalCollisionBelow = true;
				entity.causeFallDamage(entity.fallDistance, 1, DamageSource.FALL);
				entity.resetFallDistance();
				entity_move.add(yMoveBefore*data.normal.x(), 0, yMoveBefore*data.normal.z()); // prevents slide
			}
			if (data.dir.getAxis().isHorizontal()) {
				entity.horizontalCollision = true;
				// horizontal collide damage
			}
			// change movement based on collision
			entity.setDeltaMovement(UtilGeometry.convertVector(entity_move));
			if (entity instanceof EntityVehicle vehicle) {
				Quaternion q = vehicle.getQBySide();
				q.mul(Vector3f.YN.rotationDegrees((float)parent_rot_rate.y));
				vehicle.setQBySide(q);
			}
		}
	}*/
	
	public boolean couldCollide(Entity entity) {
		if (entity.noPhysics) return false;
		if (entity.isRemoved()) return false;
		if (!entity.canCollideWith(getParent())) return false;
		if (entity.isPassenger()) return false;
		if (entity.getRootVehicle().equals(getParent())) return false;
		return true;
	}
	
	public Predicate<? super Entity> getCouldCollideTest() {
		return (entity) -> couldCollide(entity);
	}
	
	@Override
    public boolean hurt(DamageSource source, float amount) {
		// FIXME 4.4 player can't punch RotbaleHitbox
		System.out.print(source+" hurt "+amount+" "+this);
    	return getParent().hurt(source, amount);
    }
	
	@Override
    protected AABB makeBoundingBox() {
    	/*EntityDimensions d = getDimensions(getPose());
    	if (d == null) return super.makeBoundingBox();
    	double pX = getX(), pY = getY(), pZ = getZ();
    	double f = d.width / 2.0F;
        double f1 = d.height / 2.0F;
        return new AABB(pX-f, pY-f1, pZ-f, 
        		pX+f, pY+f1, pZ+f);*/
		if (hitbox == null) return AABB.ofSize(position(), 1f, 1f, 1f);
		return hitbox.getDisguisedAABB(position());
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
		return true;
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
        return true;
    }
    
    @Override
    public boolean canCollideWith(Entity entity) {
    	return couldCollide(entity);
    }
	
    @Override
	public EntityDimensions getDimensions(Pose pPose) {
		return size;
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
