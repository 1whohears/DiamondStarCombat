package com.onewhohears.dscombat.common.network;

import com.onewhohears.dscombat.common.network.toserver.ToServerVehicleSyncAction;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public abstract class VehicleSyncAction {

    public static void register() {
        addVehicleSyncAction(new LandingGearAction(false));
        addVehicleSyncAction(new OpenStorageAction(0));
        addVehicleSyncAction(new OpenPartsAction());
    }

    public static void sendSyncAction(VehicleSyncAction action) {
        PacketHandler.INSTANCE.sendToServer(new ToServerVehicleSyncAction(action));
    }

    private static final IntObjectMap<VehicleSyncAction> sync_action_map = new IntObjectHashMap<>();

    @Nullable
    public static VehicleSyncAction getAction(int id) {
        return sync_action_map.get(id);
    }

    public static void addVehicleSyncAction(VehicleSyncAction action) {
        sync_action_map.put(action.id, action);
    }

    private final int id;

    protected VehicleSyncAction(int id) {
        this.id = id;
    }

    protected abstract BiPredicate<Player, EntityVehicle> getPermissionCheck();
    protected abstract BiConsumer<ServerPlayer, EntityVehicle> getServerAction();
    protected abstract Consumer<FriendlyByteBuf> getWriteData();
    protected abstract Consumer<FriendlyByteBuf> getReadData();

    public int getId() {
        return id;
    }

    public void writeData(FriendlyByteBuf buffer) {
        getWriteData().accept(buffer);
    }

    public void readData(FriendlyByteBuf buffer) {
        getReadData().accept(buffer);
    }

    public boolean hasPermission(Player player, EntityVehicle vehicle) {
        return getPermissionCheck().test(player, vehicle);
    }

    public void runServerAction(ServerPlayer player, EntityVehicle vehicle) {
        getServerAction().accept(player, vehicle);
    }

    public static class LandingGearAction extends VehicleSyncAction {
        private boolean active;
        public LandingGearAction(boolean active) {
            super(0);
            this.active = active;
        }
        public boolean isActive() {
            return active;
        }
        @Override
        protected BiPredicate<Player, EntityVehicle> getPermissionCheck() {
            return (player, vehicle) -> true;
        }
        @Override
        protected BiConsumer<ServerPlayer, EntityVehicle> getServerAction() {
            return (player, vehicle) -> {
                vehicle.setLandingGear(isActive());
            };
        }
        @Override
        protected Consumer<FriendlyByteBuf> getWriteData() {
            return (buffer) -> buffer.writeBoolean(isActive());
        }
        @Override
        protected Consumer<FriendlyByteBuf> getReadData() {
            return (buffer) -> active = buffer.readBoolean();
        }
    }

    public static class OpenStorageAction extends VehicleSyncAction {
        private int index;
        public OpenStorageAction(int index) {
            super(1);
            this.index = index;
        }
        @Override
        protected BiPredicate<Player, EntityVehicle> getPermissionCheck() {
            return (player, vehicle) -> true;
        }
        @Override
        protected BiConsumer<ServerPlayer, EntityVehicle> getServerAction() {
            return (player, vehicle) -> vehicle.openStorage(player, index);
        }
        @Override
        protected Consumer<FriendlyByteBuf> getWriteData() {
            return (buffer) -> buffer.writeInt(index);
        }
        @Override
        protected Consumer<FriendlyByteBuf> getReadData() {
            return (buffer) -> index = buffer.readInt();
        }
    }

    public static class OpenPartsAction extends VehicleSyncAction {
        public OpenPartsAction() {
            super(2);
        }
        @Override
        protected BiPredicate<Player, EntityVehicle> getPermissionCheck() {
            return (player, vehicle) -> true;
        }
        @Override
        protected BiConsumer<ServerPlayer, EntityVehicle> getServerAction() {
            return (player, vehicle) -> vehicle.openPartsMenu(player);
        }
        @Override
        protected Consumer<FriendlyByteBuf> getWriteData() {
            return (buffer) -> {};
        }
        @Override
        protected Consumer<FriendlyByteBuf> getReadData() {
            return (buffer) -> {};
        }
    }
}
