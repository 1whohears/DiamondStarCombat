package com.onewhohears.dscombat.entity.vehicle;

import com.onewhohears.dscombat.data.vehicle.RotableHitboxData;
import com.onewhohears.dscombat.init.ModEntities;
import com.onewhohears.dscombat.util.math.RotableAABB;
import com.onewhohears.onewholibs.util.math.UtilAngles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import org.joml.Quaternionf;

public class RotableHitbox extends Entity implements IEntityAdditionalSpawnData, CustomExplosion {
	
	public static final EntityDataAccessor<Float> HEALTH = SynchedEntityData.defineId(RotableHitbox.class, EntityDataSerializers.FLOAT);
	public static final EntityDataAccessor<Float> ARMOR = SynchedEntityData.defineId(RotableHitbox.class, EntityDataSerializers.FLOAT);
	
	private EntityVehicle parent;
	private RotableHitboxData data;
	private RotableAABB hitbox;
	
	private Vec3 test_pos, test_size;
	
	public RotableHitbox(EntityVehicle parent, RotableHitboxData data) {
		this(ModEntities.ROTABLE_HITBOX.get(), parent.level());
		this.parent = parent;
		this.data = data;
		this.noPhysics = true;
		initStats();
		setHealth(data.getMaxHealth());
		setArmor(data.getMaxArmor());
	}
	
	public void readNbt(CompoundTag nbt) {
		CompoundTag tag = nbt.getCompound(getHitboxName());
		if (tag == null || tag.isEmpty()) return;
		if (tag.contains("health")) setHealth(tag.getFloat("health"));
		if (tag.contains("armor")) setArmor(tag.getFloat("armor"));
	}
	
	public void writeNbt(CompoundTag nbt) {
		CompoundTag tag = new CompoundTag();
		tag.putFloat("health", getHealth());
		tag.putFloat("armor", getArmor());
		nbt.put(getHitboxName(), tag);
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
		parent = (EntityVehicle) level().getEntity(parentId);
		String name = buffer.readUtf();
		data = parent.getStats().getHitboxDataByName(name);
		initStats();
		parent.addRotableHitboxForClient(this);
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
		if (!level().isClientSide && tickCount > 20 && data.isRemoveOnDestroy() && isDestroyed()) {
			kill();
			return;
		}
		positionSelf();
		firstTick = false;
	}
	
	protected void positionSelf() {
		setOldPosAndRot();
		Quaternionf q = getParent().getQBySide();
		Vec3 pos = getParent().position().add(UtilAngles.rotateVector(getRelPos(), q));
		hitbox.setCenterAndRot(pos, q);
		setPos(pos);
	}
	
	public Vec3 collide(Entity entity, AABB aabb, Vec3 move) {
		if (hitbox == null || getParent() == null) return move;
		//System.out.println("==========");
		//System.out.println("HANDLE COLLISION "+entity+" "+move+" "+this);
		/*if (getParent().didEntityAlreadyCollide(entity)) {
			//System.out.println("Already Collided");
			return move;
		}*/
		if (getParent().isEntityHitboxCooldown(entity)) {
			//System.out.println("Hitbox Cooldown "+entity.level.isClientSide);
			return move;
		}
		if (!couldCollide(entity)) {
			//System.out.println("Can't Collide");
			return move;
		}
		if (!hitbox.isInside(aabb, RotableAABB.COLLIDE_CHECK_SKIN)) {
			//System.out.println("Not Inside");
			return move;
		}
		// FIXME 4.6 prevent entities from falling off when the chunks load
		//System.out.println("==========");
		//System.out.println("PRE COLLISION "+getHitboxName()+" "+getId()+" "+entity.level.isClientSide+" "+getParent().tickCount+" "+entity.position()+" "+move+" "+entity.isOnGround());
		//getParent().addEntityCollidedHitbox(entity);
		if (isInside(entity)) {
			//System.out.println("INSIDE");
			Vec3 push = hitbox.getPushOutPos(entity.position(), entity.getBoundingBox(), RotableAABB.INSIDE_PUSH_OUT_SKIN);
			entity.moveTo(push);
			entity.setOnGround(true);
			entity.resetFallDistance();
			move = move.multiply(1, 0, 1);
		}
		//System.out.println("OUTSIDE");
		moveEntityFromParent(entity);
		//Vec3 entityMoveByParent = moveEntityFromParent(entity);
		/*if (!UtilGeometry.isZero(entityMoveByParent)) {
			//move = move.add(entityMoveByParent);
			System.out.println("parent move = "+getParent().getDeltaMovement());
			System.out.println("entityMoveByParent = "+entityMoveByParent);
		}*/
		move = hitbox.collide(entity.position(), entity.getBoundingBox(), move);
		//if (entity.level.isClientSide) ClientSideHitboxStuckFixer.onCollide(entity, isInside);
		getParent().addEntityCollideInfo(entity, this, entity.position());
		if (getParent().isStuckInHitbox(entity)) {
			getParent().addEntityToHitboxCooldown(entity);
			entity.moveTo(new Vec3(entity.getX(), getParent().getMaxHitboxY(), entity.getZ()));
		}
		//System.out.println("POST COLLISION "+entity.position()+" "+move);
		return move;
	}
	
	public Vec3 moveEntityFromParent(Entity entity) {
		Vec3 parent_pos = getParent().position();
		Vec3 parent_move = getParent().getDeltaMovement();
		Vec3 parent_rot_rate = getParent().getAngularVel();
		Quaternionf q = getParent().getQBySide();
		Quaternionf qi = new Quaternionf(q);
		qi.conjugate();
		Vec3 rel_pos = UtilAngles.rotateVector(entity.position().subtract(parent_pos), qi);
		Vec3 rel_tan_vel = parent_rot_rate.scale(Math.toRadians(1d))
				.multiply(-1d,-1d,1d).cross(rel_pos);
		Vec3 tan_vel = UtilAngles.rotateVector(rel_tan_vel, q);
		Vec3 entityMoveByParent = parent_move.add(tan_vel);
		// FIXME 4.2 server client desyncs from tan_vel cause the entity to slowly fall off
		entity.setPos(entity.position().add(entityMoveByParent));
		//entity.moveTo(entity.position().add(entityMoveByParent));
		entity.setYRot(entity.getYRot()+(float)parent_rot_rate.y);
		return entityMoveByParent;
	}
	
	public boolean isInside(Entity entity) {
		return hitbox.isInside(entity.getBoundingBox(), RotableAABB.IS_INSIDE_CHECK_SKIN);
	}
	
	public boolean couldCollide(Entity entity) {
		if (getParent() == null) return false;
		if (isRemoved()) return false;
		if (isDestroyed()) return false;
		if (entity.noPhysics) return false;
		if (entity.isRemoved()) return false;
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
		if (test_pos != null) return test_pos;
		return getHitboxData().getRelPos();
	}
	
	public Vec3 getHitboxSize() {
		if (test_size != null) return test_size;
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
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
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
		entityData.define(HEALTH, 10f);
        entityData.define(ARMOR, 10f);
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

	@Override
	public void push(Entity entity) {
		// this override fixes the client getting pushed off the boat hitbox when playing on a server
	}
	
	public float getHealth() {
		return entityData.get(HEALTH);
	}
	
	public float getMaxHealth() {
		if (data == null) return 0;
		return data.getMaxHealth();
	}
	
	public float getArmor() {
		return entityData.get(ARMOR);
	}
	
	public float getMaxArmor() {
		if (data == null) return 0;
		return data.getMaxArmor();
	}
	
	public void setHealth(float health) {
		if (health < 0) health = 0;
		else if (health > data.getMaxHealth()) health = getMaxHealth();
		entityData.set(HEALTH, health);
	}
	
	public void setArmor(float armor) {
		if (armor < 0) armor = 0;
		else if (armor > data.getMaxArmor()) armor = getMaxArmor();
		entityData.set(ARMOR, armor);
	}
	
	public void addHealth(float health) {
		setHealth(getHealth()+health);
	}
	
	public void addArmor(float armor) {
		setArmor(getArmor()+armor);
	}
	
	public void fullyRepair() {
		repair(100000); // repair function revives hitbox if needed
		repair(100000); // repair twice to fix armor and health
	}
	
	public void repair(float repair) {
		if (!level().isClientSide && isRemoved()) {
			revive();
			level().addFreshEntity(this);
		}
		if (getHealth() < getMaxHealth()) addHealth(repair);
		else if (getArmor() < getMaxArmor()) addArmor(repair);
	}
	
	public boolean isDamaged() {
		return getHealth() < getMaxHealth() || getArmor() < getMaxArmor();
	}
	
	public boolean isDestroyed() {
		return getHealth() <= 0 && getMaxHealth() > 0;
	}
	
	@Override
	public String toString() {
		if (getParent() == null) return super.toString() + "{"+getHitboxName()+",NO_PARENT}";
		return super.toString() + "{"+getHitboxName()+",HI:"+getHitboxIndex()+",PID:"+getParent().getId()+"}";
	}

	public void setTestPos(Vec3 test_pos) {
		this.test_pos = test_pos;
	}

	public void setTestSize(Vec3 test_size) {
		this.test_size = test_size;
		getHitbox().setExtents(test_size.scale(0.5));
	}
	
	/**
	 * ignoring vanilla explosion effects.
	 * see {@link RotableHitbox#customExplosionHandler(Explosion)}
	 * for custom explosion handling.
	 */
	@Override
	public boolean ignoreExplosion() {
		return true;
	}
	
	@Override
	public void customExplosionHandler(Explosion exp) {
		if (getParent() != null) getParent().customExplosionHandler(exp, this);
	}
	
	public int getHitboxIndex() {
		if (data == null) return -1;
		return data.getIndex();
	}
	
	public boolean canHandleInteract() {
		return getHitboxIndex() == 0 && getParent() != null && !getParent().rootHitboxEntityInteract();
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		if (canHandleInteract()) return getParent().interact(player, hand);
		return InteractionResult.PASS;
	}
	
	@Override
	public Entity getRootVehicle() {
		return getParent() != null ? getParent() : this;
	}

	@Override
	public boolean isAlliedTo(Entity entity) {
		return getRootVehicle().isAlliedTo(entity);
	}

	@Override
	public boolean isAlliedTo(Team team) {
		return getRootVehicle().isAlliedTo(team);
	}
	
	public double getMaxY() {
		return hitbox.getMaxY();
	}
	
}
