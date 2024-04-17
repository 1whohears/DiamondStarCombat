package com.onewhohears.dscombat.entity.parts;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.parts.PartData.PartType;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.init.ModTags;
import com.onewhohears.dscombat.util.UtilServerPacket;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityChainHook extends EntityPart {
	
	public static final double CHAIN_LENGTH = 8;
	
	private final List<ChainConnection> chains = new ArrayList<>();
	
	public EntityChainHook(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		ListTag conns = nbt.getList("chains", 10);
		for (int i = 0; i < conns.size(); ++i) {
			CompoundTag tag = conns.getCompound(i);
			ChainConnection conn = fromNBT(tag, level);
			if (conn == null) continue;
			chains.add(conn);
		}
	}
	
	@Override
	protected void addAdditionalSaveData(CompoundTag nbt) {
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
		super.tick();
		for (int i = 0; i < chains.size(); ++i) {
			chains.get(i).tick();
			if (!level.isClientSide && chains.get(i).isDisconnected()) disconnectChain(i--);
		}
	}
	
	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		ItemStack item = player.getItemInHand(hand);
		if (!item.isEmpty() && item.is(ModTags.Items.VEHICLE_CHAIN)) {
			handleChainInteract(player, item);
			return InteractionResult.sidedSuccess(level.isClientSide);
		} else if (hasChain()) {
			disconnectAllChains();
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		return InteractionResult.PASS;
	}
	
	protected void handleChainInteract(Player player, ItemStack item) {
		if (level.isClientSide) return;
		if (isPlayerConnected(player)) return;
		List<EntityVehicle> vehicles = level.getEntitiesOfClass(EntityVehicle.class, 
			getBoundingBox().inflate(CHAIN_LENGTH), vehicle -> vehicle.isChainConnectedToPlayer(player));
		if (vehicles.size() == 0) {
			addPlayerConnection(player);
			return;
		}
		for (EntityVehicle vehicle : vehicles) addVehicleConnection(null, vehicle);
	}
	
	public void addPlayerConnection(Player player) {
		chains.add(new ChainConnection(this, player, null));
		if (!level.isClientSide) {
			UtilServerPacket.sendChainAddPlayer(this, player);
			playChainConnectSound();
		}
	}
	
	public boolean addVehicleConnection(@Nullable Player player, EntityVehicle vehicle) {
		if (vehicle.equals(getParentVehicle())) return false;
		boolean foundConnection = false;
		if (player == null) {
			chains.add(new ChainConnection(this, null, vehicle));
		} else {
			for (ChainConnection chain : chains) {
				if (chain.attachVehicle(player, vehicle)) {
					foundConnection = true;
					break;
				}
			}
		}
		if (!level.isClientSide) {
			UtilServerPacket.sendChainAddVehicle(vehicle, this, player);
			playChainConnectSound();
		}
		return foundConnection;
	}
	
	public void playChainConnectSound() {
		level.playSound(null, this, SoundEvents.CHAIN_PLACE, SoundSource.BLOCKS, 1, 1);
	}
	
	public void playChainDisconnectSound() {
		level.playSound(null, this, SoundEvents.CHAIN_BREAK, SoundSource.BLOCKS, 1, 1);
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
		private boolean sentToClient = false;
		private ChainConnection(EntityChainHook hook, @Nullable Player player, @Nullable EntityVehicle vehicle) {
			this.hook = hook;
			this.player = player;
			this.vehicle = vehicle;
			if (vehicle != null) vehicleUUID = vehicle.getUUID();
			sentToClient = true;
		}
		private ChainConnection(EntityChainHook hook, UUID vehicleUUID) {
			this.hook = hook;
			this.vehicleUUID = vehicleUUID;
			sentToClient = false;
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
			if (distance <= CHAIN_LENGTH) return;
			parent.addForceMomentToClient(getVehicle().getWeightForce(), Vec3.ZERO);
			double fraction = (distance - CHAIN_LENGTH) / distance;
			Vec3 move = new Vec3(vehicleHookDiff.x*fraction, vehicleHookDiff.y*fraction, vehicleHookDiff.z*fraction);
			getVehicle().setDeltaMovement(move);
			getVehicle().move(MoverType.SELF, move);
			float turn = Mth.approachDegrees(getVehicle().getYRot(), 
					UtilAngles.getYaw(move), getVehicle().getMaxDeltaYaw());
			getVehicle().setYRot(turn);
		}
		protected double moveComponent(double diff, double fraction) {
			return diff * fraction;
		}
		public void tick() {
			if (!isSentToClient() && !hook.level.isClientSide) sendToClient();
			if (isVehicleConnection()) {
				tickPhysics();
				return;
			}
		}
		private void sendToClient() {
			sentToClient = true;
			if (!isVehicleConnection()) return;
			vehicle.chainToHook(hook);
			UtilServerPacket.sendChainAddVehicle(getVehicle(), hook, null);
		}
		public boolean isSentToClient() {
			return sentToClient;
		}
		private void onDisconnect() {
			if (getVehicle() != null) {
				getVehicle().disconnectChain();
				if (!hook.level.isClientSide) UtilServerPacket.sendChainDisconnectVehicle(getVehicle(), hook);
			} else if (player != null) {
				if (!hook.level.isClientSide) UtilServerPacket.sendChainDisconnectPlayer(hook, player);
			}
			if (!hook.level.isClientSide) hook.playChainDisconnectSound();
			resetVehicle();
			player = null;
		}
		public boolean attachVehicle(Player player, EntityVehicle vehicle) {
			if (!canAttachVehicle(player, vehicle)) return false;
			this.player = null;
			this.vehicleUUID = vehicle.getUUID();
			this.vehicle = vehicle;
			this.vehicle.chainToHook(hook);
			return true;
		}
		public void resetVehicle() {
			vehicle = null;
			vehicleUUID = null;
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
			if (vehicle == null && vehicleUUID != null && !hook.level.isClientSide) {
				Entity entity = ((ServerLevel)hook.level).getEntity(vehicleUUID);
				if (!(entity instanceof EntityVehicle v)) {
					resetVehicle();
					return null;
				}
				vehicle = v;
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
	}
	
	public static enum ChainUpdateType {
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
