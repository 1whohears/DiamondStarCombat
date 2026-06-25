package com.onewhohears.dscombat.block.entity;

import com.onewhohears.dscombat.block.custom.MissileLaunchStationBlock;
import com.onewhohears.dscombat.common.container.menu.MissileLaunchStationContainerMenu;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.data.weapon.stats.WeaponStats;
import com.onewhohears.dscombat.entity.weapon.EntityBallisticMissile;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.init.ModBlockEntities;
import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class MissileLaunchStationBlockEntity extends BlockEntity implements MenuProvider {
	
	// Inventory: 1 missile slot only
	private NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);
	
	// Target coordinates
	private double targetX = 0;
	private double targetY = 64;
	private double targetZ = 0;
	
	// Launch state
	private boolean armed = false;
	private int launchCooldown = 0;
	private int selectedMissileSlot = 0;

	// Pre-flight estimates (recalculated when target or missile changes)
	private int estimatedDistanceBlocks = 0;
	private int estimatedFlightTicks = 0;
	
	// Container data for syncing with client
	public final ContainerData data = new ContainerData() {
		@Override
		public int get(int index) {
			return switch (index) {
				case 0 -> (int) targetX;
				case 1 -> (int) targetY;
				case 2 -> (int) targetZ;
				case 3 -> armed ? 1 : 0;
				case 4 -> launchCooldown;
				case 5 -> selectedMissileSlot;
				case 6 -> estimatedDistanceBlocks;
				case 7 -> estimatedFlightTicks;
				default -> 0;
			};
		}

		@Override
		public void set(int index, int value) {
			switch (index) {
				case 0 -> targetX = value;
				case 1 -> targetY = value;
				case 2 -> targetZ = value;
				case 3 -> armed = value != 0;
				case 4 -> launchCooldown = value;
				case 5 -> selectedMissileSlot = value;
				case 6 -> estimatedDistanceBlocks = value;
				case 7 -> estimatedFlightTicks = value;
			}
		}

		@Override
		public int getCount() {
			return 8;
		}
	};
	
	public MissileLaunchStationBlockEntity(BlockPos pos, BlockState blockState) {
		super(ModBlockEntities.MISSILE_LAUNCH_STATION_ENTITY.get(), pos, blockState);
	}

	@Override
	public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
		return new MissileLaunchStationContainerMenu(containerId, playerInventory, this, this.data);
	}
	
	public NonNullList<ItemStack> getInventory() {
		return inventory;
	}

	@Override
	public Component getDisplayName() {
		return UtilMCText.translatable("container.dscombat.missile_launch_station");
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		ContainerHelper.loadAllItems(tag, inventory);
		targetX = tag.getDouble("targetX");
		targetY = tag.getDouble("targetY");
		targetZ = tag.getDouble("targetZ");
		armed = tag.getBoolean("armed");
		launchCooldown = tag.getInt("launchCooldown");
		selectedMissileSlot = tag.getInt("selectedMissileSlot");
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		ContainerHelper.saveAllItems(tag, inventory);
		tag.putDouble("targetX", targetX);
		tag.putDouble("targetY", targetY);
		tag.putDouble("targetZ", targetZ);
		tag.putBoolean("armed", armed);
		tag.putInt("launchCooldown", launchCooldown);
		tag.putInt("selectedMissileSlot", selectedMissileSlot);
		super.saveAdditional(tag);
	}
	
	public boolean stillValid(Player player) {
		return this.level.getBlockEntity(this.worldPosition) == this 
				&& player.distanceToSqr(this.worldPosition.getX() + 0.5, 
						this.worldPosition.getY() + 0.5, 
						this.worldPosition.getZ() + 0.5) <= 64.0;
	}
	
	public void tick() {
		if (level == null || level.isClientSide()) return;
		
		// Update cooldown
		if (launchCooldown > 0) {
			launchCooldown--;
			setChanged();
		}
		
		// Update armed state in block
		BlockState state = getBlockState();
		if (state.getValue(MissileLaunchStationBlock.ARMED) != armed) {
			level.setBlock(worldPosition, state.setValue(MissileLaunchStationBlock.ARMED, armed), 3);
		}
	}

	/** Recalculates estimated flight distance and time based on current missile and target. */
	private void recalculateEstimates() {
		ItemStack missileStack = getSelectedMissile();
		if (missileStack.isEmpty()) {
			estimatedDistanceBlocks = 0;
			estimatedFlightTicks = 0;
			return;
		}
		String weaponId = missileStack.getOrCreateTag().getString("weapon");
		WeaponStats stats = WeaponPresets.get().get(weaponId);
		if (stats == null) {
			estimatedDistanceBlocks = 0;
			estimatedFlightTicks = 0;
			return;
		}
		if (!(stats instanceof com.onewhohears.dscombat.data.weapon.stats.BulletStats bulletStats)) {
			estimatedDistanceBlocks = 0;
			estimatedFlightTicks = 0;
			return;
		}
		Vec3 launchPos = getLaunchPos();
		Vec3 target = getTargetPos();
		int[] result = EntityBallisticMissile.estimateFlight(launchPos, target, bulletStats.getSpeed());
		estimatedDistanceBlocks = result[0];
		estimatedFlightTicks = result[1];
		setChanged();
	}

	public Vec3 getTargetPos() {
		return new Vec3(targetX, targetY, targetZ);
	}
	
	public void setTargetPos(double x, double y, double z) {
		this.targetX = x;
		this.targetY = y;
		this.targetZ = z;
		recalculateEstimates();
		setChanged();
	}
	
	public boolean isArmed() {
		return armed;
	}
	
	public void setArmed(boolean armed) {
		this.armed = armed;
		setChanged();
	}
	
	public void toggleArmed() {
		setArmed(!armed);
	}
	
	public int getSelectedMissileSlot() {
		return selectedMissileSlot;
	}
	
	public void setSelectedMissileSlot(int slot) {
		if (slot >= 0 && slot < 1) {
			this.selectedMissileSlot = slot;
			setChanged();
		}
	}
	
	public ItemStack getSelectedMissile() {
		return inventory.get(selectedMissileSlot);
	}
	
	/**
	 * Get the weapon ID of the loaded missile for rendering
	 * @return weapon ID or null if no missile loaded
	 */
	public String getLoadedMissileId() {
		ItemStack missile = getSelectedMissile();
		if (missile.isEmpty()) return null;
		String weaponId = missile.getOrCreateTag().getString("weapon");
		return weaponId.isEmpty() ? null : weaponId;
	}
	
	public boolean canLaunch() {
		return armed && launchCooldown == 0 && !getSelectedMissile().isEmpty();
	}
	
	public Direction getFacing() {
		return getBlockState().getValue(MissileLaunchStationBlock.FACING);
	}
	
	public Vec3 getLaunchPos() {
		// Launch position is 1 block above the station
		return new Vec3(worldPosition.getX() + 0.5, worldPosition.getY() + 1.5, worldPosition.getZ() + 0.5);
	}
	
	/**
	 * Attempt to launch the selected missile
	 * @param player The player initiating the launch
	 * @return true if launch was successful
	 */
	public boolean launchMissile(Player player) {
		if (!canLaunch()) return false;
		
		ItemStack missileStack = getSelectedMissile();
		if (missileStack.isEmpty()) return false;
		
		// Get weapon ID from the missile item
		String weaponId = missileStack.getOrCreateTag().getString("weapon");
		if (weaponId.isEmpty()) return false;
		
		// Get weapon stats
		WeaponStats stats = WeaponPresets.get().get(weaponId);
		if (stats == null) return false;
		
		// Create BALLISTIC missile entity (converts any missile to ballistic trajectory)
		EntityType<?> entityType = com.onewhohears.dscombat.init.ModEntities.POS_MISSILE.get();
		Entity entity = entityType.create(level);
		
		// Replace with ballistic missile
		if (entity != null) {
			entity.discard();
			entity = new com.onewhohears.dscombat.entity.weapon.EntityBallisticMissile<>(
				(EntityType<? extends com.onewhohears.dscombat.entity.weapon.EntityBallisticMissile<?>>) entityType,
				level,
				weaponId
			);
		}
		
		if (!(entity instanceof EntityWeapon<?> weapon)) return false;
		
		// Set weapon preset
		weapon.setPreset(weaponId);
		
		// Set position (1 block above station)
		Vec3 launchPos = getLaunchPos();
		weapon.setPos(launchPos);
		
		// Set owner
		weapon.setOwner(player);
		
		// Set target position for ballistic missile
		Vec3 targetPos = getTargetPos();
		if (entity instanceof com.onewhohears.dscombat.entity.weapon.EntityBallisticMissile<?> ballistic) {
			ballistic.targetPos = targetPos;
			ballistic.setTargetPos(targetPos);
		}
		
		// Initial velocity — small upward push, missile will home to target after CLIMB_TICKS
		weapon.setDeltaMovement(new Vec3(0, 0.5, 0));
		weapon.setYRot(player != null ? player.getYRot() : 0);
		weapon.setXRot(-80f);
		
		// Spawn entity
		level.addFreshEntity(weapon);
		
		// Play launch sound
		level.playSound(null, worldPosition, 
			net.minecraft.sounds.SoundEvents.GENERIC_EXPLODE, 
			net.minecraft.sounds.SoundSource.BLOCKS, 
			1.5f, 0.8f);
		
		// Consume one missile
		missileStack.shrink(1);
		
		// Set cooldown (100 ticks = 5 seconds)
		launchCooldown = 100;
		
		setChanged();
		
		// Send message to player
		if (player != null) {
			player.displayClientMessage(
				UtilMCText.literal("Ballistic missile launched! Target: " + 
					String.format("%.0f, %.0f, %.0f", targetX, targetY, targetZ)), 
				true
			);
		}
		
		return true;
	}
	
	public void dropContents(Level level, BlockPos pos) {
		Containers.dropContents(level, pos, inventory);
	}
}
