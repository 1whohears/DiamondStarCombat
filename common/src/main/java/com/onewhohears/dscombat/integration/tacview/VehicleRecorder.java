package com.onewhohears.dscombat.integration.tacview;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.math.QuaternionF;
import com.onewhohears.onewholibs.util.math.UtilAngles;
import com.onewhohears.tacview.client.core.ClientPlayback;
import com.onewhohears.tacview.common.core.EntityRecorder;
import com.onewhohears.tacview.common.core.KeyframeValue;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;

public abstract class VehicleRecorder<K extends VehicleKeyframe<E>, E extends EntityVehicle> extends EntityRecorder<K,E> {

    public static final BiFunction<ServerLevel,UUID, EntityVehicle> DEFAULT_VEHICLE_GETTER =
            (level, uuid) -> level.getEntity(uuid) instanceof EntityVehicle vehicle ? vehicle : null;

    public final KeyframeValue.StringV<E> preset = registerStringValue("preset", EntityVehicle::getStatsId, EntityVehicle::setStatsId);

    public VehicleRecorder(@NotNull E entity, int recordRate, @NotNull BiFunction<ServerLevel,UUID,E> entityFinder) {
        super(entity, recordRate, entityFinder);
    }

    public VehicleRecorder(@NotNull JsonObject data, @NotNull BiFunction<ServerLevel,UUID,E> entityFinder) {
        super(data, entityFinder);
    }

    @Override
    public void addOverlayInfo(@NotNull List<Component> overlayEntityInfo, @NotNull E entity, @NotNull K keyframe) {
        super.addOverlayInfo(overlayEntityInfo, entity, keyframe);
        KeyframeValue.addOverlayValue(overlayEntityInfo, preset.name, preset.get());
        KeyframeValue.addOverlayValue(overlayEntityInfo, "m/s", String.format("%.1f",keyframe.vel.get().length()*20));
    }

    @Override
    public void onPlaybackEntitySetup(@NotNull E entity, @NotNull ClientPlayback playback) {
        super.onPlaybackEntitySetup(entity, playback);
        entity.setPreset(preset.get());
        entity.updateClientStatsHolder();
        entity.textureManager.setupTextureLocations();
        entity.textureManager.setupDynamicTexture();
    }

    @Override
    public void onPlaybackTick(@NotNull E entity, @NotNull ClientPlayback playback) {
        entity.tickClientLandingGear();
        super.onPlaybackTick(entity, playback);
        entity.zRotO = entity.getZRot();
    }

    @Override
    public void onPlaybackRender(@NotNull E entity, @NotNull ClientPlayback playback,
                                 PoseStack stack, float yaw, @NotNull Vec3 renderPos,
                                 float partialTick, MultiBufferSource buffer, int packedLight) {
        super.onPlaybackRender(entity, playback, stack, yaw, renderPos, partialTick, buffer, packedLight);
        QuaternionF q = UtilAngles.toQuaternionF(entity.getYRot(), entity.getXRot(), entity.getZRot());
        entity.setQ(q);
        entity.setClientQ(q);
        entity.setPrevQ(q);
    }

    public static class Generic extends VehicleRecorder<VehicleKeyframe.Generic, EntityVehicle> {
        public Generic(@NotNull EntityVehicle entity, int recordRate) {
            super(entity, recordRate, DEFAULT_VEHICLE_GETTER);
        }
        public Generic(@NotNull JsonObject data) {
            super(data, DEFAULT_VEHICLE_GETTER);
        }
        @Override
        protected @Nullable VehicleKeyframe.Generic readKeyframe(@NotNull JsonObject data) {
            return new VehicleKeyframe.Generic(data);
        }
        @Override
        protected VehicleKeyframe.Generic newKeyframe(@NotNull EntityVehicle vehicle) {
            return new VehicleKeyframe.Generic(vehicle);
        }
        @Override
        protected VehicleKeyframe.Generic emptyKeyframe() {
            return new VehicleKeyframe.Generic();
        }
    }
}
