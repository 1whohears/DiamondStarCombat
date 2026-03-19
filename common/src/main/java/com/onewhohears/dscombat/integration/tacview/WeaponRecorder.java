package com.onewhohears.dscombat.integration.tacview;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.onewhohears.dscombat.data.weapon.NonTickingMissileManager;
import com.onewhohears.dscombat.entity.weapon.EntityMissile;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.tacview.client.core.ClientPlayback;
import com.onewhohears.tacview.common.core.EntityRecorder;
import com.onewhohears.tacview.common.core.KeyframeValue;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;

public abstract class WeaponRecorder<K extends WeaponKeyframe<E>, E extends EntityWeapon> extends EntityRecorder<K,E> {

    public static final BiFunction<ServerLevel,UUID,EntityWeapon> DEFAULT_WEAPON_GETTER =
            (level, uuid) -> level.getEntity(uuid) instanceof EntityWeapon weapon ? weapon : null;

    public final KeyframeValue.StringV<E> preset = registerStringValue("preset", EntityWeapon::getStatsId, EntityWeapon::setStatsId);
    public final KeyframeValue.UUIDV<E> owner = registerUUIDValue("owner", entity -> {
        Entity owner = entity.getOwner();
        if (owner != null) return owner.getUUID();
        else return KeyframeValue.UUIDV.DEFAULT_UUID;
    }, (entity, value) -> {});

    public WeaponRecorder(@NotNull E entity, int recordRate, @NotNull BiFunction<ServerLevel,UUID,E> entityFinder) {
        super(entity, recordRate, entityFinder);
    }

    public WeaponRecorder(@NotNull JsonObject data, @NotNull BiFunction<ServerLevel,UUID,E> entityFinder) {
        super(data, entityFinder);
    }

    @Override
    public void addOverlayInfo(@NotNull List<Component> overlayEntityInfo, @NotNull E entity, @NotNull K keyframe) {
        super.addOverlayInfo(overlayEntityInfo, entity, keyframe);
        KeyframeValue.addOverlayValue(overlayEntityInfo, preset.name, preset.getValueToString());
        KeyframeValue.addOverlayValue(overlayEntityInfo, owner.name, owner.getValueToString());
        KeyframeValue.addOverlayValue(overlayEntityInfo, "m/s", String.format("%.1f",keyframe.vel.get().length()*20));
    }

    @Override
    public void onPlaybackEntitySetup(@NotNull E entity, @NotNull ClientPlayback playback) {
        super.onPlaybackEntitySetup(entity, playback);
        entity.setPreset(preset.get());
        entity.updateClientStatsHolder();
    }

    @Override
    public void onPlaybackTick(@NotNull E entity, @NotNull ClientPlayback playback) {
        super.onPlaybackTick(entity, playback);
        if (!owner.isEqual(KeyframeValue.UUIDV.DEFAULT_UUID) && entity.getOwner() == null) {
            Entity o = playback.getEntity(owner.get());
            if (o != null) entity.setOwner(o);
        }
    }

    public static class Generic extends WeaponRecorder<WeaponKeyframe.Generic, EntityWeapon> {
        public Generic(@NotNull EntityWeapon entity, int recordRate) {
            super(entity, recordRate, DEFAULT_WEAPON_GETTER);
        }
        public Generic(@NotNull JsonObject data) {
            super(data, DEFAULT_WEAPON_GETTER);
        }
        @Override
        protected @Nullable WeaponKeyframe.Generic readKeyframe(@NotNull JsonObject data) {
            return new WeaponKeyframe.Generic(data);
        }
        @Override
        protected WeaponKeyframe.Generic newKeyframe(@NotNull EntityWeapon weapon) {
            return new WeaponKeyframe.Generic(weapon);
        }
        @Override
        protected WeaponKeyframe.Generic emptyKeyframe() {
            return new WeaponKeyframe.Generic();
        }
    }

    public static class Missile extends WeaponRecorder<WeaponKeyframe.Missile, EntityMissile<?>> {
        public static final BiFunction<ServerLevel,UUID,EntityMissile<?>> MISSILE_GETTER =
                (level, uuid) -> NonTickingMissileManager.getMissile(uuid);
        public Missile(@NotNull EntityMissile entity, int recordRate) {
            super(entity, recordRate, MISSILE_GETTER);
        }
        public Missile(@NotNull JsonObject data) {
            super(data, MISSILE_GETTER);
        }
        @Override
        protected @Nullable WeaponKeyframe.Missile readKeyframe(@NotNull JsonObject data) {
            return new WeaponKeyframe.Missile(data);
        }
        @Override
        protected WeaponKeyframe.Missile newKeyframe(@NotNull EntityMissile weapon) {
            return new WeaponKeyframe.Missile(weapon);
        }
        @Override
        protected WeaponKeyframe.Missile emptyKeyframe() {
            return new WeaponKeyframe.Missile();
        }

        @Override
        public void onPlaybackRender(@NotNull EntityMissile<?> entity, @NotNull ClientPlayback playback,
                                     PoseStack stack, float yaw, @NotNull Vec3 renderPos, float partialTick,
                                     MultiBufferSource buffer, int packedLight) {
            super.onPlaybackRender(entity, playback, stack, yaw, renderPos, partialTick, buffer, packedLight);

        }
    }
}
