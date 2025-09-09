package com.onewhohears.dscombat.integration.distant_players;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.onewholibs.util.math.QuaternionF;
import com.onewhohears.distant_players.common.core.extra_render_info.ExtraRenderTargetInfo;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.DataSerializers;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class DSCVehicleRenderInfo implements ExtraRenderTargetInfo {

    private String preset = "wooden_plane";
    private QuaternionF q = QuaternionF.ONE;
    private boolean landingGear = false;
    private float throttle = 0;
    private int baseTextureIndex = 0;

    @Override
    public void tickFakeEntity(@NotNull Entity entity) {
        if (!(entity instanceof EntityVehicle vehicle)) return;
        vehicle.clientTick();
    }

    @Override
    public void updateFakeEntity(@NotNull Entity entity) {
        if (!(entity instanceof EntityVehicle vehicle)) return;
        vehicle.setPreset(preset);
        vehicle.updateClientStatsHolder();
        vehicle.setPrevQ(q);
        vehicle.setClientQ(q);
        vehicle.setLandingGear(landingGear);
        vehicle.setCurrentThrottle(throttle);
        vehicle.textureManager.setBaseTexture(baseTextureIndex);
    }

    @Override
    public void setupEntityOnCreate(@NotNull Entity entity) {
        if (!(entity instanceof EntityVehicle vehicle)) return;
        vehicle.setPreset(preset);
        vehicle.updateClientStatsHolder();
        vehicle.textureManager.setupTextureLocations();
        vehicle.textureManager.setupDynamicTexture();
    }

    @Override
    public void getInfoServerSide(@NotNull Entity entity) {
        if (!(entity instanceof EntityVehicle vehicle)) return;
        preset = vehicle.getStatsId();
        q = vehicle.getQBySide();
        landingGear = vehicle.isLandingGear();
        throttle = vehicle.getCurrentThrottle();
        baseTextureIndex = vehicle.textureManager.getBaseTextureIndex();
    }

    @Override
    public void getInfoClientSide(FriendlyByteBuf buffer) {
        preset = buffer.readUtf();
        q = DataSerializers.QUATERNION.read(buffer);
        landingGear = buffer.readBoolean();
        throttle = buffer.readFloat();
        baseTextureIndex = buffer.readInt();
    }

    @Override
    public void encodeInfoServerSide(FriendlyByteBuf buffer) {
        buffer.writeUtf(preset);
        DataSerializers.QUATERNION.write(buffer, q);
        buffer.writeBoolean(landingGear);
        buffer.writeFloat(throttle);
        buffer.writeInt(baseTextureIndex);
    }

    @Override
    public Vec3 onRender(@NotNull Entity entity, PoseStack poseStack, Camera camera, float yaw, Vec3 renderDisplacement,
                         float partialTick, MultiBufferSource buffer, int packedLight) {
        return renderDisplacement;
    }
}
