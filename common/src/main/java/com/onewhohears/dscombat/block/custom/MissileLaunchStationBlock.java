package com.onewhohears.dscombat.block.custom;

import com.onewhohears.dscombat.block.entity.MissileLaunchStationBlockEntity;
import com.onewhohears.dscombat.common.container.menu.MissileLaunchStationContainerMenu;
import com.onewhohears.onewholibs.util.UtilEntity;
import com.onewhohears.onewholibs.util.UtilMCText;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MissileLaunchStationBlock extends BaseEntityBlock {
	
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty ARMED = BooleanProperty.create("armed");
	private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 16, 16);
	
	public MissileLaunchStationBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState()
				.setValue(ARMED, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, ARMED);
	}
	
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	private static ExtendedMenuProvider getProvider(BlockPos pos) {
		return new ExtendedMenuProvider() {
			@Override
			public void saveExtraData(FriendlyByteBuf buf) {
				buf.writeBlockPos(pos);
			}
			@Override
			public @NotNull Component getDisplayName() {
				return UtilMCText.translatable("block.dscombat.missile_launch_station");
			}
			@Override
			public @NotNull AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
				MissileLaunchStationBlockEntity station = (MissileLaunchStationBlockEntity) UtilEntity.getLevel(player).getBlockEntity(pos);
				return new MissileLaunchStationContainerMenu(i, inventory, station, station.data);
			}
		};
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos,
								 Player player, InteractionHand hand, BlockHitResult hit) {
		if (!level.isClientSide()) {
			BlockEntity entity = level.getBlockEntity(pos);
			if(entity instanceof MissileLaunchStationBlockEntity) {
				MenuRegistry.openExtendedMenu((ServerPlayer)player, getProvider(pos));
			} else {
				throw new IllegalStateException("Missile Launch Station BlockEntity is missing!");
			}
		}
		return InteractionResult.sidedSuccess(level.isClientSide());
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new MissileLaunchStationBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return level.isClientSide() ? null : (lvl, pos, st, blockEntity) -> {
			if (blockEntity instanceof MissileLaunchStationBlockEntity station) {
				station.tick();
			}
		};
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if (blockEntity instanceof MissileLaunchStationBlockEntity station) {
				station.dropContents(level, pos);
			}
			super.onRemove(state, level, pos, newState, isMoving);
		}
	}
}
