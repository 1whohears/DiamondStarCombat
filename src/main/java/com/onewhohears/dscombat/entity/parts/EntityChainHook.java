package com.onewhohears.dscombat.entity.parts;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.onewhohears.dscombat.data.parts.PartData.PartType;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.init.ModTags;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

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
			if (chains.get(i).isDisconnected()) disconnectChain(i--);
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
		// if already has this player connected to it do nothing
		// else if another vehicle was connected to this player within 8 blocks
		// else attach this player to the chains
	}
	
	public void addPlayerConnection(Player player) {
		chains.add(new ChainConnection(player, null));
		// send packet
	}
	
	public boolean addVehicleConnection(Player player, EntityVehicle vehicle) {
		for (ChainConnection chain : chains) 
			if (chain.attachVehicle(this, player, vehicle)) 
				return true;
		return false;
	}
	
	public boolean hasChain() {
		return chains.size() > 0;
	}
	
	public void disconnectAllChains() {
		for (int i = 0; i < chains.size(); ++i) disconnectChain(i--);
	}
	
	public void disconnectChain(int index) {
		if (level.isClientSide) return;
		chains.get(index).onDisconnect();
		chains.remove(index);
		// send disconnect packet
	}
	
	public static class ChainConnection {
		private Player player;
		private EntityVehicle vehicle;
		private ChainConnection(@Nullable Player player, @Nullable EntityVehicle vehicle) {
			this.player = player;
			this.vehicle = vehicle;
		}
		private void tickPhysics(EntityChainHook hook) {
			if (vehicle == null) return;
			EntityVehicle parent = hook.getParentVehicle();
			if (parent == null) return;
			
		}
		public void tick(EntityChainHook hook) {
			if (isPlayerConnection()) {
				if (hook.distanceTo(player) > CHAIN_LENGTH) onDisconnect();
				return;
			} else if (isVehicleConnection()) {
				tickPhysics(hook);
				return;
			}
		}
		private void onDisconnect() {
			if (vehicle != null) {
				vehicle.disconnectChain();
				vehicle = null;
			}
			if (player != null) {
				
				player = null;
			}
		}
		public boolean attachVehicle(EntityChainHook hook, Player player, EntityVehicle vehicle) {
			if (!canAttachVehicle(player, vehicle)) return false;
			this.player = null;
			this.vehicle = vehicle;
			this.vehicle.chainToHook(hook);
			// send packet
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
		public boolean isDisconnected() {
			return vehicle == null && player == null;
		}
		public boolean isPlayerConnection() {
			return player != null;
		}
		public boolean isVehicleConnection() {
			return vehicle != null;
		}
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

}
