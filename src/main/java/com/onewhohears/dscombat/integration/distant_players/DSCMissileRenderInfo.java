package com.onewhohears.dscombat.integration.distant_players;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.distant_players.common.core.extra_render_info.ExtraRenderTargetInfo;
import com.onewhohears.dscombat.entity.weapon.EntityMissile;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class DSCMissileRenderInfo implements ExtraRenderTargetInfo {

    private String preset = "agm114k";

    @Override
    public void tickFakeEntity(@NotNull Entity entity) {
        if (!(entity instanceof EntityMissile<?> missile)) return;
        missile.clientTickParticles();
    }

    @Override
    public void updateFakeEntity(@NotNull Entity entity) {
        if (!(entity instanceof EntityMissile<?> missile)) return;
        missile.setPreset(preset);
    }

    @Override
    public void setupEntityOnCreate(@NotNull Entity entity) {
        if (!(entity instanceof EntityMissile<?> missile)) return;
        missile.setPreset(preset);
    }

    @Override
    public void getInfoServerSide(@NotNull Entity entity) {
        if (!(entity instanceof EntityMissile<?> missile)) return;
        preset = missile.getStatsId();
    }

    @Override
    public void getInfoClientSide(FriendlyByteBuf buffer) {
        preset = buffer.readUtf();
    }

    @Override
    public void encodeInfoServerSide(FriendlyByteBuf buffer) {
        buffer.writeUtf(preset);
    }

    @Override
    public Vec3 onRender(@NotNull Entity entity, PoseStack poseStack, Camera camera, float yaw, Vec3 renderDisplacement,
                         float partialTick, MultiBufferSource buffer, int packedLight) {
        return renderDisplacement;
    }
}
