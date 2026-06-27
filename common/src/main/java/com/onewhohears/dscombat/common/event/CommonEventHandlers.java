package com.onewhohears.dscombat.common.event;

import com.mojang.brigadier.CommandDispatcher;
import com.onewhohears.dscombat.DependencySafety;
import com.onewhohears.dscombat.command.*;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.VehicleSyncAction;
import com.onewhohears.dscombat.data.graph.StatGraphs;
import com.onewhohears.dscombat.data.parts.PartPresets;
import com.onewhohears.dscombat.data.radar.RadarPresets;
import com.onewhohears.dscombat.data.radar.TrackableEntitiesManager;
import com.onewhohears.dscombat.data.vehicle.VehiclePresets;
import com.onewhohears.dscombat.data.vehicle.physics.PhysicsComponentData;
import com.onewhohears.dscombat.data.villager.DSCVillagerTrades;
import com.onewhohears.dscombat.data.weapon.MissileChunkLoadingManager;
import com.onewhohears.dscombat.data.weapon.RadarTargetTypes;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.entity.CustomExplosion;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.vehicle.hitbox.RotableHitboxes;
import com.onewhohears.dscombat.util.ExplosionFireColumn;
import com.onewhohears.dscombat.util.UtilVehicleEntity;
import com.onewhohears.onewholibs.common.event.OWLEvents;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetReloadListener;
import com.onewhohears.onewholibs.util.UtilEntity;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.ExplosionEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.config.ModConfig;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommonEventHandlers {

    public static void init() {
        OWLEvents.GET_JSON_PRESET_LISTENERS.register(CommonEventHandlers::registerPresetListeners);
        LifecycleEvent.SERVER_STOPPING.register(CommonEventHandlers::serverStoppingEvent);
        LifecycleEvent.SETUP.register(CommonEventHandlers::onSetup);
        TickEvent.SERVER_PRE.register(CommonEventHandlers::onServerTickPre);
        TickEvent.PLAYER_POST.register(CommonEventHandlers::onPlayerTick);
        ExplosionEvent.DETONATE.register(CommonEventHandlers::onExplosionDetonate);
        CommandRegistrationEvent.EVENT.register(CommonEventHandlers::registerCommands);
    }

    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher,
                                        CommandBuildContext context, Commands.CommandSelection selection) {
        new MissileCommand(dispatcher);
        new VehicleCommand(dispatcher);
        new VehicleNbtCommand(dispatcher);
        new DSCParticleDebugCommand(dispatcher);
        new DebugSlotPosCommand(dispatcher);
        new DebugHitboxPosCommand(dispatcher);
        new DSCAdminCommands(dispatcher);
        new WindTunnelCommand(dispatcher);
        new DSCReloadCommand(dispatcher);
        new TrackDebugCommand(dispatcher);
        new EcmDebugCommand(dispatcher);
        new PartsDebugCommand(dispatcher);
        //ConfigCommand.register(dispatcher);
    }

    public static void onSetup() {
        PhysicsComponentData.register();
        VehicleSyncAction.register();
        PacketHandler.register();
        DSCGameRules.registerAll();
        DependencySafety.fmlCommonSetup();
        DSCVillagerTrades.register();
    }

    public static float onLivingHurt(LivingEntity livingEntity, DamageSource damageSource, float amount) {
        if (UtilVehicleEntity.isBypassArmor(damageSource))
            return amount;
        if (!livingEntity.isPassenger() || !(livingEntity.getRootVehicle() instanceof EntityVehicle plane))
            return amount;
        return plane.calcDamageToRider(damageSource, amount);
    }

    private static final Set<Integer> explodeRepeatCheck = new HashSet<>();

    public static void onExplosionDetonate(Level level, Explosion explosion, List<Entity> affectedEntities) {
        explodeRepeatCheck.clear();
        for (Entity e : affectedEntities) {
            if (!e.ignoreExplosion()) continue;
            if (explodeRepeatCheck.contains(e.getId())) continue;
            if (!(e instanceof CustomExplosion entity)) continue;
            entity.customExplosionHandler(explosion);
            explodeRepeatCheck.add(e.getId());
        }
    }

    public static void onPlayerTick(Player player) {
        if (player.isFallFlying() && UtilEntity.getLevel(player).getGameRules().getBoolean(DSCGameRules.DISABLE_ELYTRA_FLYING)) {
            player.stopFallFlying();
        }
    }

    public static void onServerTickPre(MinecraftServer server) {
        MissileChunkLoadingManager.serverTick(server);
        TrackableEntitiesManager.serverTick(server);
        EcmDebugCommand.serverTick(server, server.getTickCount());
    }

    public static void registerPresetListeners(List<JsonPresetReloadListener<?>> listeners) {
        listeners.add(StatGraphs.get());
        listeners.add(VehiclePresets.get());
        listeners.add(WeaponPresets.get());
        listeners.add(RadarPresets.get());
        listeners.add(PartPresets.get());
    }

    public static void serverStoppingEvent(MinecraftServer server) {
        RotableHitboxes.onServerStop();
        EcmDebugCommand.onServerStop();
        TrackableEntitiesManager.onServerStop();
        ExplosionFireColumn.clearAll();
        MissileChunkLoadingManager.clearAll();
    }

    public static void onReadConfig(ModConfig modConfig) {
        if (modConfig.getType() == ModConfig.Type.COMMON) {
            RadarTargetTypes.get().readConfig();
        }
    }

}
