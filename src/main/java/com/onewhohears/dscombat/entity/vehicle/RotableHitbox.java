package com.onewhohears.dscombat.entity.vehicle;

import java.util.List;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.data.vehicle.RotableHitboxData;
import com.onewhohears.dscombat.init.ModEntities;
import com.onewhohears.dscombat.util.math.RotableAABB;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

public class RotableHitbox extends Entity implements IEntityAdditionalSpawnData {
	
	private EntityVehicle parent;
	private RotableHitboxData data;
	private RotableAABB hitbox;
	
	public RotableHitbox(EntityVehicle parent, RotableHitboxData data) {
		this(ModEntities.ROTABLE_HITBOX.get(), parent.level);
		this.parent = parent;
		this.data = data;
		this.noPhysics = true;
		initStats();
	}
	
	public RotableHitbox(EntityType<?> type, Level level) {
		super(type, level);
	}
	
	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		buffer.writeInt(parent.getId());
		buffer.writeUtf(getHitboxName());
	}

	@Override
	public void readSpawnData(FriendlyByteBuf buffer) {
		int parentId = buffer.readInt();
		parent = (EntityVehicle) level.getEntity(parentId);
		parent.addRotableHitboxForClient(this);
		String name = buffer.readUtf();
		data = parent.getStats().getHitboxDataByName(name);
		initStats();
	}
	
	protected void initStats() {
		if (data == null) return;
		hitbox = new RotableAABB(getHitboxSize().x(), getHitboxSize().y(), getHitboxSize().z());
		refreshDimensions();
	}
	
	@Override
	public void tick() {
		if (parent == null || parent.isRemoved()) {
			discard();
			return;
		}
		if (data == null || hitbox == null) {
			discard();
			return;
		}
		positionSelf();
		firstTick = false;
	}
	
	protected void positionSelf() {
		setOldPosAndRot();
		Quaternion q = getParent().getQBySide();
		Vec3 pos = getParent().position().add(UtilAngles.rotateVector(getRelPos(), q));
		hitbox.setCenterAndRot(pos, q);
		setPos(pos);
	}
	
	public boolean handlePosibleCollision(List<VoxelShape> colliders, Entity entity, AABB aabb, Vec3 move) {
		if (hitbox == null) return false;
		//System.out.println("==========");
		//System.out.println("HANDLE COLLISION "+entity+" "+move+" "+this);
		if (getParent().didEntityAlreadyCollide(entity)) {
			//System.out.println("Already Collided");
			return false;
		}
		if (!couldCollide(entity)) {
			//System.out.println("Can't Collide");
			return false;
		}
		if (!hitbox.isInside(aabb, RotableAABB.SUB_COL_SKIN)) {
			//System.out.println("Not Inside");
			return false;
		}
		// FIXME 4.1 walking on hitbox is sometimes doesn't allow sneaking
		// FIXME 4.2 sometimes can't open vehicle inventories when the vehicle is on a boat hitbox
		// FIXME 4.6 prevent entities from falling off when the chunks load
		getParent().addHitboxCollider(entity);
		//System.out.println("PRE COLLISION "+entity.level.isClientSide+" "+entity.tickCount+" "+entity.position()+" "+move+" "+position());
		boolean stuck = false;
		if (isInside(entity)) {
			//System.out.println("INSIDE");
			Vec3 push = hitbox.getPushOutPos(entity.position(), entity.getBoundingBox(), 0.1);
			entity.moveTo(push);
			//entity.setDeltaMovement(Vec3.ZERO);
			stuck = true;
		}
		//System.out.println("OUTSIDE");
		Vec3 entityMoveByParent = moveEntityFromParent(entity);
		//System.out.println("entityMoveByParent = "+entityMoveByParent);
		if (hitbox.updateColliders(colliders, entity.position(), entity.getBoundingBox(), entityMoveByParent)) {
			stuck = true;
		}
		//System.out.println("POST COLLISION "+entity.level.isClientSide+" "+entity.tickCount+" "+entity.position());
		return stuck;
	}
	
	public Vec3 moveEntityFromParent(Entity entity) {
		Vec3 parent_pos = getParent().position();
		Vec3 parent_move = getParent().getDeltaMovement();
		Vec3 parent_rot_rate = getParent().getAngularVel();
		Quaternion q = getParent().getQBySide();
		Quaternion qi = q.copy();
		qi.conj();
		Vec3 rel_pos = UtilAngles.rotateVector(entity.position().subtract(parent_pos), qi);
		Vec3 rel_tan_vel = parent_rot_rate.scale(Math.toRadians(1d))
				.multiply(-1d,-1d,1d).cross(rel_pos);
		Vec3 tan_vel = UtilAngles.rotateVector(rel_tan_vel, q);
		Vec3 entityMoveByParent = parent_move.add(tan_vel);
		entity.setPos(entity.position().add(entityMoveByParent));
		entity.setYRot(entity.getYRot()+(float)parent_rot_rate.y);
		return entityMoveByParent;
	}
	
	public boolean isInside(Entity entity) {
		return hitbox.isInside(entity.getBoundingBox(), -0.1);
	}
	
	public boolean couldCollide(Entity entity) {
		if (entity.noPhysics) return false;
		if (entity.isRemoved()) return false;
		if (!entity.canCollideWith(getParent())) return false;
		if (entity.isPassenger()) return false;
		if (entity.getRootVehicle().equals(getParent())) return false;
		return true;
	}
	
	@Override
    public boolean hurt(DamageSource source, float amount) {
    	return getParent().hurtHitbox(source, amount, this);
    }
	
	@Override
    protected AABB makeBoundingBox() {
		if (hitbox == null) return AABB.ofSize(position(), 1f, 1f, 1f);
		return hitbox.getDisguisedAABB(position());
    }
	
	public RotableHitboxData getHitboxData() {
		return data;
	}
	
	public String getHitboxName() {
		return getHitboxData().getName();
	}
	
	public RotableAABB getHitbox() {
		return hitbox;
	}
	
	public Vec3 getRelPos() {
		return getHitboxData().getRelPos();
	}
	
	public Vec3 getHitboxSize() {
		return getHitboxData().getSize();
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
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	@Override
	public boolean shouldBeSaved() {
		return false;
	}
	
	@Override
	public void onRemovedFromWorld() {
		super.onRemovedFromWorld();
		RotableHitboxes.removeHitbox(this);
	}
	
	@Override
	public void onAddedToWorld() {
		super.onAddedToWorld();
		RotableHitboxes.addHitbox(this);
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
	
	public EntityVehicle getParent() {
		return parent;
	}

	@Override
	public boolean shouldRender(double pX, double pY, double pZ) {
		return false;
	}

	@Override
	public boolean shouldRenderAtSqrDistance(double pDistance) {
		return false;
	}
	
}
