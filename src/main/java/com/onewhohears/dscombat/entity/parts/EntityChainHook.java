package com.onewhohears.dscombat.entity.parts;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.parts.PartData.PartType;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.init.ModTags;
import com.onewhohears.dscombat.util.UtilPacket;

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
	public void tick() {
		super.tick();
		for (int i = 0; i < chains.size(); ++i) {
			chains.get(i).tick(this);
			if (!level.isClientSide && chains.get(i).isDisconnected(this)) disconnectChain(i--);
		}
	}
	
	@Override
	public InteractionResult interact(Player player, InteractionHand hand) {
		ItemStack item = player.getItemInHand(hand);
		if (item.isEmpty()) return InteractionResult.PASS;
		if (item.is(ModTags.Items.VEHICLE_CHAIN)) {
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
		chains.add(new ChainConnection(player, null));
		if (!level.isClientSide) UtilPacket.sendChainAddPlayer(this, player);
	}
	
	public void addVehicleConnection(@Nullable Player player, EntityVehicle vehicle) {
		if (player == null) chains.add(new ChainConnection(null, vehicle));
		else for (ChainConnection chain : chains) 
				if (chain.attachVehicle(this, player, vehicle)) 
					break;
		if (!level.isClientSide) UtilPacket.sendChainAddVehicle(vehicle, this, player);
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
		chains.get(index).onDisconnect(this);
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
	
	public static class ChainConnection {
		private Player player;
		private EntityVehicle vehicle;
		private ChainConnection(@Nullable Player player, @Nullable EntityVehicle vehicle) {
			this.player = player;
			this.vehicle = vehicle;
		}
		protected void tickPhysics(EntityChainHook hook) {
			if (vehicle == null) return;
			EntityVehicle parent = hook.getParentVehicle();
			if (parent == null) return;
			Vec3 vehicleHookDiff = hook.position().subtract(vehicle.position());
			double distance = vehicleHookDiff.length();
			if (distance <= CHAIN_LENGTH) return;
			parent.addForceMomentToClient(vehicle.getWeightForce(), Vec3.ZERO);
			//vehicle.addForceMomentToClient(parent.getForces(), Vec3.ZERO);
			double fraction = (distance - CHAIN_LENGTH) / distance;
			Vec3 move = new Vec3(vehicleHookDiff.x*fraction, vehicleHookDiff.y*fraction, vehicleHookDiff.z*fraction);
			vehicle.setDeltaMovement(move);
			vehicle.move(MoverType.SELF, move);
		}
		protected double moveComponent(double diff, double fraction) {
			return diff * fraction;
		}
		public void tick(EntityChainHook hook) {
			if (isVehicleConnection()) {
				tickPhysics(hook);
				return;
			}
		}
		private void onDisconnect(EntityChainHook hook) {
			if (vehicle != null) {
				vehicle.disconnectChain();
				if (!hook.level.isClientSide) UtilPacket.sendChainDisconnectVehicle(vehicle, hook);
			} else if (player != null) {
				if (!hook.level.isClientSide) UtilPacket.sendChainDisconnectPlayer(hook, player);
			}
			vehicle = null;
			player = null;
		}
		public boolean attachVehicle(EntityChainHook hook, Player player, EntityVehicle vehicle) {
			if (!canAttachVehicle(player, vehicle)) return false;
			this.player = null;
			this.vehicle = vehicle;
			this.vehicle.chainToHook(hook);
			return true;
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
			return vehicle;
		}
		@Nullable
		public Entity getEntity() {
			return (vehicle != null) ? vehicle : player;
		}
		public boolean isDisconnected(EntityChainHook hook) {
			return (vehicle == null && player == null) || isVehicleDead() || isVehicleDisconnected() || isPlayerTooFar(hook);
		}
		public boolean isPlayerConnection() {
			return player != null;
		}
		public boolean isVehicleConnection() {
			return vehicle != null;
		}
		public boolean isVehicleDead() {
			return isVehicleConnection() && getVehicle().isRemoved();
		}
		public boolean isVehicleDisconnected() {
			return isVehicleConnection() && getVehicle().getChainHolderHook() == null;
		}
		public boolean isPlayerTooFar(EntityChainHook hook) {
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
