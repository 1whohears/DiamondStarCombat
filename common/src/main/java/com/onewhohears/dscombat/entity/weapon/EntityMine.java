package com.onewhohears.dscombat.entity.weapon;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ToClientMineExplode;
import com.onewhohears.dscombat.data.mine.MinePresets;
import com.onewhohears.dscombat.data.mine.MineStats;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.ModEntities;
import com.onewhohears.onewholibs.util.UtilEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class EntityMine extends Entity {
	
	public static final EntityDataAccessor<Integer> MINE_TYPE = SynchedEntityData.defineId(EntityMine.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Boolean> ARMED = SynchedEntityData.defineId(EntityMine.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<String> MINE_PRESET_ID = SynchedEntityData.defineId(EntityMine.class, EntityDataSerializers.STRING);
	
	private float damage;
	private float radius;
	private int armTime;
	private int ticksExisted;
	private MineType mineType;
	private boolean chainReaction;
	private String presetId;
	private static final Random random = new Random();
	
	public enum MineType {
		ANTI_PERSONNEL(0, 20.0f, 1.2f, 40),
		ANTI_TANK(1, 50.0f, 3.5f, 60);
		
		private final int id;
		private final float damage;
		private final float radius;
		private final int armTime;
		
		MineType(int id, float damage, float radius, int armTime) {
			this.id = id;
			this.damage = damage;
			this.radius = radius;
			this.armTime = armTime;
		}
		
		public int getId() { return id; }
		public float getDamage() { return damage; }
		public float getRadius() { return radius; }
		public int getArmTime() { return armTime; }
		
		public static MineType fromId(int id) {
			for (MineType type : values()) {
				if (type.id == id) return type;
			}
			return ANTI_PERSONNEL;
		}
	}
	
	public EntityMine(EntityType<? extends EntityMine> entityType, Level level) {
		super(entityType, level);
		this.presetId = "anti_personnel_mine";
		loadFromPreset();
	}
	
	public EntityMine(Level level, Vec3 pos, String presetId) {
		this(ModEntities.MINE.get(), level);
		this.presetId = presetId;
		loadFromPreset();
		// Adjust position to be on the block surface, not floating above it
		setPos(pos.x, pos.y - 0.1, pos.z);
		entityData.set(MINE_TYPE, mineType.getId());
		entityData.set(MINE_PRESET_ID, presetId);
	}
	
	public EntityMine(Level level, Vec3 pos, MineType type) {
		this(level, pos, type == MineType.ANTI_TANK ? "anti_tank_mine" : "anti_personnel_mine");
	}
	
	private void loadFromPreset() {
		MineStats stats = MinePresets.getStats(presetId);
		if (stats != null) {
			this.mineType = stats.getMineType();
			this.damage = stats.getDamage();
			this.radius = stats.getRadius();
			this.armTime = stats.getArmTime();
			this.chainReaction = stats.hasChainReaction();
		} else {
			// Fallback to default values
			this.mineType = MineType.ANTI_PERSONNEL;
			this.damage = mineType.getDamage();
			this.radius = mineType.getRadius();
			this.armTime = mineType.getArmTime();
			this.chainReaction = true;
		}
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(MINE_TYPE, 0);
		entityData.define(ARMED, false);
		entityData.define(MINE_PRESET_ID, "anti_personnel_mine");
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		this.presetId = tag.getString("presetId");
		if (presetId.isEmpty()) presetId = "anti_personnel_mine";
		
		loadFromPreset();
		
		this.ticksExisted = tag.getInt("ticksExisted");
		entityData.set(MINE_TYPE, mineType.getId());
		entityData.set(ARMED, ticksExisted >= armTime);
		entityData.set(MINE_PRESET_ID, presetId);
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		tag.putString("presetId", presetId);
		tag.putInt("ticksExisted", ticksExisted);
	}

	@Override
	public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
		return new ClientboundAddEntityPacket(this);
	}
	
	@Override
	public void tick() {
		super.tick();
		
		if (!isClientSide()) {
			ticksExisted++;
			
			// Arm the mine after armTime ticks
			if (ticksExisted == armTime) {
				entityData.set(ARMED, true);
			}
			
			// Check for entities only when armed
			if (isArmed()) {
				checkForTrigger();
			}
		}
	}
	
	private void checkForTrigger() {
		AABB detectionBox = getBoundingBox().inflate(1.0);
		List<Entity> entities = level().getEntities(this, detectionBox);
		
		for (Entity entity : entities) {
			boolean shouldTrigger = false;
			
			if (mineType == MineType.ANTI_PERSONNEL) {
				// Trigger only on players
				shouldTrigger = entity instanceof Player;
			} else if (mineType == MineType.ANTI_TANK) {
				// Trigger only on vehicles
				shouldTrigger = entity instanceof EntityVehicle;
			}
			
			if (shouldTrigger) {
				explode();
				break;
			}
		}
	}
	
	private void explode() {
		if (!isClientSide()) {
			// Send visual effects packet to clients
			PacketHandler.sendToTrackers(new ToClientMineExplode(this), this);
			
			// ExplosionInteraction.MOB allows damage to entities but prevents block destruction
			level().explode(this, getX(), getY(), getZ(), 
					radius, false, Level.ExplosionInteraction.MOB);
			
			// Trigger nearby mines (chain reaction)
			triggerNearbyMines();
			
			discard();
		}
	}
	
	private void triggerNearbyMines() {
		if (!chainReaction) return;
		
		// Search for mines in explosion radius + 2 blocks
		AABB searchBox = getBoundingBox().inflate(radius + 2.0);
		List<EntityMine> nearbyMines = level().getEntitiesOfClass(EntityMine.class, searchBox);
		
		for (EntityMine mine : nearbyMines) {
			if (mine != this && !mine.isRemoved()) {
				// Trigger mine after a short delay for realistic chain reaction
				mine.triggerExplosion(5 + random.nextInt(10));
			}
		}
	}
	
	public void triggerExplosion(int delay) {
		if (delay <= 0) {
			explode();
		} else {
			// Schedule explosion
			new java.util.Timer().schedule(new java.util.TimerTask() {
				@Override
				public void run() {
					if (!isRemoved()) {
						explode();
					}
				}
			}, delay * 50L); // delay in ticks * 50ms
		}
	}
	
	public boolean isArmed() {
		return entityData.get(ARMED);
	}
	
	public MineType getMineType() {
		return MineType.fromId(entityData.get(MINE_TYPE));
	}
	
	public boolean isClientSide() {
		return getWorld().isClientSide();
	}
	
	public Level getWorld() {
		return UtilEntity.getLevel(this);
	}
	
	@Override
	public boolean isPickable() {
		return false;
	}
	
	@Override
	public boolean isPushable() {
		return false;
	}
}
