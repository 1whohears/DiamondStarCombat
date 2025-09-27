package com.onewhohears.dscombat.entity.parts;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.onewhohears.onewholibs.util.UtilEntity;
import org.jetbrains.annotations.Nullable;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toserver.ToServerGetHookChains;
import com.onewhohears.dscombat.data.parts.PartType;
import com.onewhohears.dscombat.data.parts.instance.ChainHookInstance;
import com.onewhohears.dscombat.data.parts.stats.ChainHookStats;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.ModTags;
import com.onewhohears.dscombat.util.UtilServerPacket;
import com.onewhohears.onewholibs.util.math.UtilAngles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityChainHook extends EntityPart<ChainHookStats, ChainHookInstance<ChainHookStats>> {
	
	public static final double CHAIN_LENGTH = 8;
	
	private final List<ChainConnection> chains = new ArrayList<>();
	
	public EntityChainHook(EntityType<?> entityType, Level level) {
		super(entityType, level, "chain_hook");
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		ListTag conns = nbt.getList("chains", 10);
		for (int i = 0; i < conns.size(); ++i) {
			CompoundTag tag = conns.getCompound(i);
			ChainConnection conn = fromNBT(tag, getWorld());
			if (conn == null) continue;
			chains.add(conn);
		}
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		ListTag conns = new ListTag();
		for (int i = 0; i < chains.size(); ++i) {
			CompoundTag tag = chains.get(i).toNBT();
			if (tag != null) conns.add(tag);
		}
		nbt.put("chains", conns);
	}
	
	@Override
	public void tick() {
		if (firstTick && isClientSide()) requestChainsFromServer();
		super.tick();
		for (int i = 0; i < chains.size(); ++i) {
			ChainConnection chain = chains.get(i);
			chain.tick();
			if (!isClientSide() && chain.isDisconnected())
				disconnectChain(i--);
		}
	}

    public boolean isClientSide() {
        return getWorld().isClientSide();
    }

    public Level getWorld() {
        return UtilEntity.getLevel(this);
    }
	
	private void requestChainsFromServer() {
		if (!isClientSide()) return;
        new ToServerGetHookChains(this).sendToServer();
	}
	
	public void sendAllVehicleChainsToClient(ServerPlayer reciever) {
		if (isClientSide()) return;
		for (ChainConnection chain : chains) {
			if (chain.isVehicleConnection()) {
				UtilServerPacket.sendChainAddVehicleTo(chain.getVehicle(), this, reciever);
			}
		}
	}
	
	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		ItemStack item = player.getItemInHand(hand);
		if (!item.isEmpty() && item.is(ModTags.Items.VEHICLE_CHAIN)) {
			handleChainInteract(player, item);
			return InteractionResult.sidedSuccess(isClientSide());
		} else if (hasChain()) {
			disconnectAllChains();
			return InteractionResult.sidedSuccess(isClientSide());
		}
		return InteractionResult.PASS;
	}
	
	protected void handleChainInteract(Player player, ItemStack item) {
		if (isClientSide()) return;
		if (isPlayerConnected(player)) return;
		List<EntityVehicle> vehicles = getWorld().getEntitiesOfClass(EntityVehicle.class,
			getBoundingBox().inflate(CHAIN_LENGTH), vehicle -> vehicle.isChainConnectedToPlayer(player));
		if (vehicles.size() == 0) {
			addPlayerConnection(player);
			return;
		}
		for (EntityVehicle vehicle : vehicles) addVehicleConnection(null, vehicle);
	}
	
	public void addPlayerConnection(Player player) {
		chains.add(new ChainConnection(this, player, null));
		if (!isClientSide()) {
			UtilServerPacket.sendChainAddPlayer(this, player);
			playChainConnectSound();
		}
	}
	
	public boolean addVehicleConnection(@Nullable Player player, EntityVehicle vehicle) {
		if (vehicle.equals(getParentVehicle())) return false;
		boolean foundConnection = false;
		if (player == null) {
			boolean alreadyConnected = false;
			for (ChainConnection chain : chains) {
				if (chain.isVehicleConnection() && chain.getVehicle().equals(vehicle)) {
					alreadyConnected = true;
					break;
				}
			}
			if (!alreadyConnected) chains.add(new ChainConnection(this, null, vehicle));
		} else {
			for (ChainConnection chain : chains) {
				if (chain.attachVehicle(player, vehicle)) {
					foundConnection = true;
					break;
				}
			}
		}
		if (!isClientSide()) {
			UtilServerPacket.sendChainAddVehicle(vehicle, this, player);
			playChainConnectSound();
		}
		return foundConnection;
	}
	
	public void playChainConnectSound() {
        getWorld().playSound(null, this, SoundEvents.CHAIN_PLACE, SoundSource.BLOCKS, 1, 1);
	}
	
	public void playChainDisconnectSound() {
        getWorld().playSound(null, this, SoundEvents.CHAIN_BREAK, SoundSource.BLOCKS, 1, 1);
	}
	
	public void disconnectVehicle(EntityVehicle vehicle) {
		for (int i = 0; i < chains.size(); ++i) 
			if (chains.get(i).isVehicleConnection() && chains.get(i).getVehicle().equals(vehicle)) 
				disconnectChain(i--);
	}
	
	public void disconnectPlayer(Player player) {
		for (int i = 0; i < chains.size(); ++i) 
			if (chains.get(i).isPlayerConnected(player)) 
				disconnectChain(i--);
	}
	
	public boolean hasChain() {
		return chains.size() > 0;
	}
	
	public void disconnectAllChains() {
		for (int i = 0; i < chains.size(); ++i) disconnectChain(i--);
	}
	
	public void disconnectChain(int index) {
		chains.get(index).onDisconnect();
		chains.remove(index);
	}
	
	public boolean isPlayerConnected(Player player) {
		for (ChainConnection chain : chains) 
			if (chain.isPlayerConnected(player))
				return true;
		return false;
	}
	
	public List<ChainConnection> getConnections() {
		return chains;
	}
	
	@Nullable
	private ChainConnection fromNBT(CompoundTag nbt, Level level) {
		UUID vehicleId = nbt.getUUID("vehicle");
		if (vehicleId == null) return null;
		return new ChainConnection(this, vehicleId);
	}
	
	public static class ChainConnection {
		private final EntityChainHook hook;
		private Player player;
		private EntityVehicle vehicle;
		private UUID vehicleUUID;
		private int vehicleId = -1;
		private ChainConnection(EntityChainHook hook, @Nullable Player player, @Nullable EntityVehicle vehicle) {
			this.hook = hook;
			this.player = player;
			this.vehicle = vehicle;
			if (vehicle != null) {
				vehicleUUID = vehicle.getUUID();
				vehicleId = vehicle.getId();
			}
		}
		private ChainConnection(EntityChainHook hook, UUID vehicleUUID) {
			this.hook = hook;
			this.vehicleUUID = vehicleUUID;
		}
		private ChainConnection(EntityChainHook hook, int vehicleId) {
			this.hook = hook;
			this.vehicleId = vehicleId;
		}
		@Nullable
		private CompoundTag toNBT() {
			if (!isVehicleConnection()) return null;
			CompoundTag tag = new CompoundTag();
			tag.putUUID("vehicle", vehicleUUID);
			return tag;
		}
		protected void tickPhysics() {
			if (getVehicle() == null) return;
			EntityVehicle parent = hook.getParentVehicle();
			if (parent == null) return;
			Vec3 vehicleHookDiff = hook.position().subtract(getVehicle().position());
			double distance = vehicleHookDiff.length();
			if (distance > (CHAIN_LENGTH - 0.5) || (!parent.isOnGround() && !getVehicle().isOnGround())) {
				parent.addForceMomentToClient(getVehicle().getWeightForce(), Vec3.ZERO);
			}
			if (distance <= CHAIN_LENGTH) return;
			double fraction = (distance - CHAIN_LENGTH) / distance;
			double yMove = vehicleHookDiff.y*fraction;
			if (parent.isOnGround() && getVehicle().isOnGround()) yMove = 0;
			Vec3 move = new Vec3(vehicleHookDiff.x*fraction, yMove, vehicleHookDiff.z*fraction);
			if (getVehicle().isOnGround()) {
				double speed = getVehicle().getXZSpeed() * getVehicle().getXZSpeedDir() + move.horizontalDistance();
				getVehicle().setDeltaMovement(move.normalize().scale(speed));
			} else {
				getVehicle().setDeltaMovement(getVehicle().getDeltaMovement().add(move));
			}
			getVehicle().move(MoverType.SELF, move);
			if (move.horizontalDistanceSqr() > 0.01) {
				float turn = Mth.approachDegrees(getVehicle().getYRot(), 
					UtilAngles.getYaw(move), getVehicle().getMaxDeltaYaw());
				getVehicle().setYRot(turn);
			}
		}
		protected double moveComponent(double diff, double fraction) {
			return diff * fraction;
		}
		public void tick() {
			if (isVehicleConnection()) {
				tickPhysics();
				return;
			}
		}
		private void onDisconnect() {
			if (getVehicle() != null) {
				getVehicle().disconnectChain();
				if (!hook.isClientSide()) {
					Containers.dropItemStack(hook.getWorld(), hook.getX(), hook.getY(), hook.getZ(),
							Items.CHAIN.getDefaultInstance());
					UtilServerPacket.sendChainDisconnectVehicle(getVehicle(), hook);
				}
			} else if (player != null) {
				if (!hook.isClientSide()) UtilServerPacket.sendChainDisconnectPlayer(hook, player);
			}
			if (!hook.isClientSide()) hook.playChainDisconnectSound();
			resetVehicle();
			player = null;
		}
		public boolean attachVehicle(Player player, EntityVehicle vehicle) {
			if (!canAttachVehicle(player, vehicle)) return false;
			this.player = null;
			this.vehicleId = vehicle.getId();
			this.vehicleUUID = vehicle.getUUID();
			this.vehicle = vehicle;
			this.vehicle.chainToHook(hook);
			return true;
		}
		public void resetVehicle() {
			vehicle = null;
			vehicleUUID = null;
			vehicleId = -1;
		}
		public boolean canAttachVehicle(Player player, EntityVehicle vehicle) {
			return isPlayerConnected(player) && vehicle.getChainHolderHook() == null;
		}
		public boolean isPlayerConnected(Player player) {
			return isPlayerConnection() && getPlayer().equals(player);
		}
		@Nullable
		public Player getPlayer() {
			return player;
		}
		@Nullable
		public EntityVehicle getVehicle() {
			if (vehicle == null) {
				if (vehicleUUID != null && !hook.isClientSide()) {
					Entity entity = ((ServerLevel)hook.getWorld()).getEntity(vehicleUUID);
					if (!(entity instanceof EntityVehicle v)) {
						resetVehicle();
						return null;
					}
					vehicle = v;
					vehicleId = vehicle.getId();
					vehicle.chainToHook(hook);
				} else if (vehicleId != -1 && hook.isClientSide()) {
					Entity entity = hook.getWorld().getEntity(vehicleId);
					if (!(entity instanceof EntityVehicle v)) {
						resetVehicle();
						return null;
					}
					vehicle = v;
					vehicle.chainToHook(hook);
				}
			}
			return vehicle;
		}
		@Nullable
		public Entity getEntity() {
			return (getVehicle() != null) ? getVehicle() : player;
		}
		public boolean isDisconnected() {
			return (getVehicle() == null && player == null) || isVehicleDead() || isVehicleDisconnected() || isPlayerTooFar();
		}
		public boolean isPlayerConnection() {
			return player != null;
		}
		public boolean isVehicleConnection() {
			return getVehicle() != null;
		}
		public boolean isVehicleDead() {
			return isVehicleConnection() && getVehicle().isRemoved();
		}
		public boolean isVehicleDisconnected() {
			return isVehicleConnection() && getVehicle().getChainHolderHook() == null;
		}
		public boolean isPlayerTooFar() {
			return isPlayerConnection() && hook.distanceTo(player) > CHAIN_LENGTH;
		}
		@Nullable
		public UUID getVehicleUUID() {
			return vehicleUUID;
		}
	}
	
	public enum ChainUpdateType {
		CHAIN_ADD_VEHICLE,
		CHAIN_ADD_PLAYER,
		CHAIN_DISCONNECT_PLAYER,
		CHAIN_DISCONNECT_VEHICLE,
		VEHICLE_ADD_PLAYER
	}

	@Override
	public boolean shouldRender() {
		return true;
	}

	@Override
	public PartType getPartType() {
		return PartType.CHAIN_HOOK;
	}

	@Override
	public boolean canGetHurt() {
		return true;
	}
	
	@Override
	public void kill() {
		disconnectAllChains();
		super.kill();
	}
	
	@Override
	protected void onNoParent() {
		disconnectAllChains();
		super.onNoParent();
	}

}
