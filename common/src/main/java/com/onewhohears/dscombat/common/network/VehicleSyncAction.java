package com.onewhohears.dscombat.common.network;

import com.onewhohears.dscombat.common.network.toserver.ToServerVehicleSyncAction;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.parts.instance.ReloadablePartInstance;
import com.onewhohears.dscombat.data.radar.RadarStats;
import com.onewhohears.dscombat.entity.parts.EntityRidablePart;
import com.onewhohears.dscombat.entity.parts.EntityTurret;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.init.ModSounds;
import com.onewhohears.dscombat.item.ItemParachute;
import com.onewhohears.onewholibs.util.UtilMCText;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.collection.IntObjectMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public abstract class VehicleSyncAction {

    public static final BiPredicate<Player, EntityVehicle> PILOT_CHECK = (player, vehicle) -> {
        if (!vehicle.isPilotOrCopilot(player)) {
            player.displayClientMessage(
                    UtilMCText.translatable("error.dscombat.not_a_pilot"),
                    true);
            return false;
        }
        return true;
    };

    public static final BiPredicate<Player, EntityVehicle> OWNER_CHECK = (player, vehicle) -> {
        if (!vehicle.isOwner(player)) {
            player.displayClientMessage(
                    UtilMCText.translatable("error.dscombat.not_owner"),
                    true);
            return false;
        }
        return true;
    };

    public static final BiPredicate<Player, EntityVehicle> PERMISSION_CHECK = (player, vehicle) -> {
        if (!vehicle.hasPermission(player)) {
            player.displayClientMessage(
                    UtilMCText.translatable("error.dscombat.no_perm_vehicle"),
                    true);
            return false;
        }
        return true;
    };

    public static void register() {
        addVehicleSyncAction(new LandingGearAction(false));
        addVehicleSyncAction(new OpenStorageAction(0));
        addVehicleSyncAction(new OpenPartsAction());
        addVehicleSyncAction(new SetRadarModeAction(RadarStats.RadarMode.ALL));
        addVehicleSyncAction(new PingSelectAction(null));
        addVehicleSyncAction(new ShootAction(-1, null, null));
        addVehicleSyncAction(new ToItemAction());
        addVehicleSyncAction(new DismountAction());
        addVehicleSyncAction(new SwitchSeatAction());
        addVehicleSyncAction(new LoadPartAction("", false));
        addVehicleSyncAction(new JetesinAction(""));
        addVehicleSyncAction(new SetPermModeAction(EntityVehicle.PermMode.PUBLIC));
        addVehicleSyncAction(new SetCustomNameAction(Component.empty()));
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
            return PILOT_CHECK;
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
            return (player, vehicle) -> {
                if (!vehicle.canOpenPartsMenu()) {
                    player.displayClientMessage(UtilMCText.translatable(
                            vehicle.getOpenMenuError()), true);
                    return false;
                }
                if (!vehicle.partsManager.hasStorageBoxes()) {
                    player.displayClientMessage(UtilMCText.translatable(
                            "error.dscombat.no_storage_boxes"), true);
                    return false;
                }
                return true;
            };
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
            return (player, vehicle) -> {
                if (!vehicle.canOpenPartsMenu()) {
                    player.displayClientMessage(UtilMCText.translatable(
                            vehicle.getOpenMenuError()), true);
                    return false;
                }
                return true;
            };
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
        public ShootAction(int selectedWeaponIndex, @Nullable RadarStats.RadarPing ping, @Nullable Vec3 targetPos) {
            super(5);
            this.selectedWeaponIndex = selectedWeaponIndex;
            this.ping = ping;
            this.targetPos = targetPos;
        }
        @Override
        protected BiPredicate<Player, EntityVehicle> getPermissionCheck() {
            return (player, vehicle) -> true;
        }
        @Override
        protected BiConsumer<ServerPlayer, EntityVehicle> getServerAction() {
            return (player, vehicle) -> {
                if (!(player.getVehicle() instanceof EntityRidablePart seat)) return;
                if (ping != null) vehicle.radarSystem.selectTarget(ping);
                if (targetPos != null) vehicle.weaponSystem.setTargetPos(targetPos);
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
            };
        }
    }

    public static class ToItemAction extends VehicleSyncAction {
        public ToItemAction() {
            super(6);
        }
        @Override
        protected BiPredicate<Player, EntityVehicle> getPermissionCheck() {
            return (player, vehicle) -> {
                Component reason = vehicle.getCantBecomeItemReason(player);
                if (reason != null) {
                    player.displayClientMessage(reason, true);
                    return false;
                }
                return true;
            };
        }
        @Override
        protected BiConsumer<ServerPlayer, EntityVehicle> getServerAction() {
            return (player, vehicle) -> {
                ItemStack item = vehicle.getItem();
                if (player.getInventory().getFreeSlot() != -1 && player.addItem(item)) {
                    vehicle.discard();
                    return;
                }
                vehicle.becomeItem(player.position());
            };
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

    public static class DismountAction extends VehicleSyncAction {
        private boolean eject = false;
        public DismountAction() {
            super(7);
        }
        public DismountAction(boolean eject) {
            this();
            this.eject = eject;
        }
        @Override
        protected BiPredicate<Player, EntityVehicle> getPermissionCheck() {
            return (player, vehicle) -> true;
        }
        @Override
        protected BiConsumer<ServerPlayer, EntityVehicle> getServerAction() {
            return (player, vehicle) -> {
                if (eject && player.getVehicle() instanceof EntityRidablePart seat && seat.canEject()) {
                    seat.useEject();
                    player.stopRiding();
                    double EJECT_PUSH = 4, EJECT_MOVE = 1;
                    Vec3 dir;
                    if (vehicle != null) {
                        dir = UtilAngles.getYawAxis(vehicle.getQ());
                        player.setDeltaMovement(vehicle.getDeltaMovement().add(dir.scale(EJECT_MOVE)));
                    } else dir = new Vec3(0, 1, 0);
                    player.setPos(player.position().add(dir.scale(EJECT_PUSH)));
                    ItemParachute.createParachute(player.getLevel(), player, null);
                    player.getLevel().playSound(null, player.blockPosition(),
                            ModSounds.EJECT, SoundSource.PLAYERS, 1, 1);
                } else player.stopRiding();
            };
        }
        @Override
        protected Consumer<FriendlyByteBuf> getWriteData() {
            return (buffer) -> buffer.writeBoolean(eject);
        }
        @Override
        protected Consumer<FriendlyByteBuf> getReadData() {
            return (buffer) -> eject = buffer.readBoolean();
        }
    }

    public static class SwitchSeatAction extends VehicleSyncAction {
        public SwitchSeatAction() {
            super(8);
        }
        @Override
        protected BiPredicate<Player, EntityVehicle> getPermissionCheck() {
            return (player, vehicle) -> true;
        }
        @Override
        protected BiConsumer<ServerPlayer, EntityVehicle> getServerAction() {
            return (player, vehicle) -> {
                if (!vehicle.switchSeat(player)) player.displayClientMessage(
                        UtilMCText.translatable("error.dscombat.no_open_seats"),
                        true);
            };
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

    public static class LoadPartAction extends VehicleSyncAction {
        private String slotId = "";
        private boolean unload = false, all = false;
        public LoadPartAction(String slotId, boolean unload) {
            super(9);
            this.slotId = slotId;
            this.unload = unload;
        }
        public LoadPartAction(boolean unload) {
            super(9);
            this.unload = unload;
            this.all = true;
        }
        @Override
        protected BiPredicate<Player, EntityVehicle> getPermissionCheck() {
            return (player, vehicle) -> vehicle.canReload(player);
        }
        @Override
        protected BiConsumer<ServerPlayer, EntityVehicle> getServerAction() {
            return (player, vehicle) -> {
                if (all) for (PartSlot slot : vehicle.partsManager.getReloadableParts())
                    handleSlot(player, (ReloadablePartInstance) slot.getPartData());
                else handleSlot(player, vehicle.partsManager.getReloadablePart(slotId));
            };
        }
        private void handleSlot(ServerPlayer player, @Nullable ReloadablePartInstance part) {
            if (part == null) return;
            if (unload && (!all || part.canUnload())) 
                part.unloadPartToInventory(player);
            else part.loadPartFromInventory(player);
        }
        @Override
        protected Consumer<FriendlyByteBuf> getWriteData() {
            return (buffer) -> {
                buffer.writeUtf(slotId);
                buffer.writeBoolean(unload);
                buffer.writeBoolean(all);
            };
        }
        @Override
        protected Consumer<FriendlyByteBuf> getReadData() {
            return (buffer) -> {
                slotId = buffer.readUtf();
                unload = buffer.readBoolean();
                all = buffer.readBoolean();
            };
        }
    }

    public static class JetesinAction extends VehicleSyncAction {
        private String slotId = "";
        public JetesinAction(String slotId) {
            super(10);
            this.slotId = slotId;
        }
        @Override
        protected BiPredicate<Player, EntityVehicle> getPermissionCheck() {
            return PILOT_CHECK;
        }
        @Override
        protected BiConsumer<ServerPlayer, EntityVehicle> getServerAction() {
            return (player, vehicle) -> {
                vehicle.jetesinPart(slotId);
            };
        }
        @Override
        protected Consumer<FriendlyByteBuf> getWriteData() {
            return (buffer) -> {
                buffer.writeUtf(slotId);
            };
        }
        @Override
        protected Consumer<FriendlyByteBuf> getReadData() {
            return (buffer) -> {
                slotId = buffer.readUtf();
            };
        }
    }

    public static class SetPermModeAction extends VehicleSyncAction {
        private EntityVehicle.PermMode mode;
        public SetPermModeAction(EntityVehicle.PermMode mode) {
            super(11);
            this.mode = mode;
        }
        @Override
        protected BiPredicate<Player, EntityVehicle> getPermissionCheck() {
            return OWNER_CHECK;
        }
        @Override
        protected BiConsumer<ServerPlayer, EntityVehicle> getServerAction() {
            return (player, vehicle) -> vehicle.setPermMode(mode);
        }
        @Override
        protected Consumer<FriendlyByteBuf> getWriteData() {
            return (buffer) -> buffer.writeEnum(mode);
        }
        @Override
        protected Consumer<FriendlyByteBuf> getReadData() {
            return (buffer) -> mode = buffer.readEnum(EntityVehicle.PermMode.class);
        }
    }

    public static class SetCustomNameAction extends VehicleSyncAction {
        private Component name;
        public SetCustomNameAction(Component name) {
            super(12);
            this.name = name;
        }
        @Override
        protected BiPredicate<Player, EntityVehicle> getPermissionCheck() {
            return PERMISSION_CHECK;
        }
        @Override
        protected BiConsumer<ServerPlayer, EntityVehicle> getServerAction() {
            return (player, vehicle) -> {
                if (name.getString().isEmpty()) vehicle.setCustomName(null);
                else vehicle.setCustomName(name);
            };
        }
        @Override
        protected Consumer<FriendlyByteBuf> getWriteData() {
            return (buffer) -> buffer.writeComponent(name);
        }
        @Override
        protected Consumer<FriendlyByteBuf> getReadData() {
            return (buffer) -> name = buffer.readComponent();
        }
    }
}
