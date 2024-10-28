package com.onewhohears.dscombat.common.network;

import com.onewhohears.dscombat.common.network.toserver.ToServerVehicleSyncAction;
import com.onewhohears.dscombat.data.radar.RadarStats;
import com.onewhohears.dscombat.data.weapon.WeaponSystem;
import com.onewhohears.dscombat.entity.parts.EntitySeat;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.DataSerializers;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public abstract class VehicleSyncAction {

    public static void register() {
        addVehicleSyncAction(new LandingGearAction(false));
        addVehicleSyncAction(new OpenStorageAction(0));
        addVehicleSyncAction(new OpenPartsAction());
        addVehicleSyncAction(new SetRadarModeAction(RadarStats.RadarMode.ALL));
        addVehicleSyncAction(new PingSelectAction(null));
        addVehicleSyncAction(new ShootAction(-1, null, null, WeaponSystem.TargetMode.LOOK));
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

    public static class SetRadarModeAction extends VehicleSyncAction {
        private RadarStats.RadarMode mode;
        public SetRadarModeAction(RadarStats.RadarMode mode) {
            super(3);
            this.mode = mode;
        }
        @Override
        protected BiPredicate<Player, EntityVehicle> getPermissionCheck() {
            return (player, vehicle) -> true;
        }
        @Override
        protected BiConsumer<ServerPlayer, EntityVehicle> getServerAction() {
            return (player, vehicle) -> vehicle.setRadarMode(mode);
        }
        @Override
        protected Consumer<FriendlyByteBuf> getWriteData() {
            return (buffer) -> buffer.writeEnum(mode);
        }
        @Override
        protected Consumer<FriendlyByteBuf> getReadData() {
            return (buffer) -> mode = buffer.readEnum(RadarStats.RadarMode.class);
        }
    }

    public static class PingSelectAction extends VehicleSyncAction {
        private RadarStats.RadarPing ping;
        public PingSelectAction(RadarStats.RadarPing ping) {
            super(4);
            this.ping = ping;
        }
        @Override
        protected BiPredicate<Player, EntityVehicle> getPermissionCheck() {
            return (player, vehicle) -> true;
        }
        @Override
        protected BiConsumer<ServerPlayer, EntityVehicle> getServerAction() {
            return (player, vehicle) -> vehicle.radarSystem.selectTarget(ping);
        }
        @Override
        protected Consumer<FriendlyByteBuf> getWriteData() {
            return (buffer) -> ping.write(buffer);
        }
        @Override
        protected Consumer<FriendlyByteBuf> getReadData() {
            return (buffer) -> ping = new RadarStats.RadarPing(buffer);
        }
    }

    public static class ShootAction extends VehicleSyncAction {
        private int selectedWeaponIndex;
        @Nullable private RadarStats.RadarPing ping;
        @Nullable private Vec3 targetPos;
        private WeaponSystem.TargetMode targetMode;
        public ShootAction(int selectedWeaponIndex, @Nullable RadarStats.RadarPing ping,
                           @Nullable Vec3 targetPos, WeaponSystem.TargetMode targetMode) {
            super(5);
            this.selectedWeaponIndex = selectedWeaponIndex;
            this.ping = ping;
            this.targetPos = targetPos;
            this.targetMode = targetMode;
        }
        @Override
        protected BiPredicate<Player, EntityVehicle> getPermissionCheck() {
            return (player, vehicle) -> true;
        }
        @Override
        protected BiConsumer<ServerPlayer, EntityVehicle> getServerAction() {
            return (player, vehicle) -> {
                if (!(player.getVehicle() instanceof EntitySeat seat)) return;
                if (ping != null) vehicle.radarSystem.selectTarget(ping);
                if (targetPos != null) vehicle.weaponSystem.setTargetPos(targetPos);
                vehicle.weaponSystem.setTargetMode(targetMode);
                if (seat.isTurret()) {
                    ((EntityTurret)seat).shoot(player);
                    return;
                }
                if (selectedWeaponIndex == -1) return;
                if (!seat.canPassengerShootParentWeapon()) return;
                vehicle.weaponSystem.setSelected(selectedWeaponIndex);
                vehicle.weaponSystem.shootSelected(player);
            };
        }
        @Override
        protected Consumer<FriendlyByteBuf> getWriteData() {
            return (buffer) -> {
                buffer.writeInt(selectedWeaponIndex);
                if (ping != null) {
                    buffer.writeBoolean(true);
                    ping.write(buffer);
                } else buffer.writeBoolean(false);
                if (targetPos != null) {
                    buffer.writeBoolean(true);
                    DataSerializers.VEC3.write(buffer, targetPos);
                } else buffer.writeBoolean(false);
                buffer.writeEnum(targetMode);
            };
        }
        @Override
        protected Consumer<FriendlyByteBuf> getReadData() {
            return (buffer) -> {
                selectedWeaponIndex = buffer.readInt();
                if (buffer.readBoolean())
                    ping = new RadarStats.RadarPing(buffer);
                else ping = null;
                if (buffer.readBoolean())
                    targetPos = DataSerializers.VEC3.read(buffer);
                else targetPos = null;
                targetMode = buffer.readEnum(WeaponSystem.TargetMode.class);
            };
        }
    }
}
