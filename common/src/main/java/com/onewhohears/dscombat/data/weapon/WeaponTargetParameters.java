package com.onewhohears.dscombat.data.weapon;

import com.onewhohears.dscombat.data.radar.RadarTarget;
import com.onewhohears.dscombat.data.weapon.stats.TargetMode;
import com.onewhohears.dscombat.init.DataSerializers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WeaponTargetParameters {

    public final @Nullable RadarTarget ping;
    public final @Nullable Vec3 targetPos;
    public final @NotNull TargetMode targetMode;
    public final int selectedMarkerId;
    public final int opticalTargetEntityId;

    public WeaponTargetParameters(@Nullable RadarTarget ping, @Nullable Vec3 targetPos, @NotNull TargetMode targetMode,
                                  int markerId, int opticalTargetEntityId) {
        this.ping = ping;
        this.targetPos = targetPos;
        this.targetMode = targetMode;
        this.selectedMarkerId = markerId;
        this.opticalTargetEntityId = opticalTargetEntityId;
    }

    public WeaponTargetParameters(FriendlyByteBuf buffer) {
        if (buffer.readBoolean())
            ping = new RadarTarget(buffer);
        else ping = null;
        if (buffer.readBoolean())
            targetPos = DataSerializers.VEC3.read(buffer);
        else targetPos = null;
        targetMode = buffer.readEnum(TargetMode.class);
        selectedMarkerId = buffer.readInt();
        opticalTargetEntityId = buffer.readInt();
    }

    public void write(FriendlyByteBuf buffer) {
        if (ping != null) {
            buffer.writeBoolean(true);
            ping.write(buffer);
        } else buffer.writeBoolean(false);
        if (targetPos != null) {
            buffer.writeBoolean(true);
            DataSerializers.VEC3.write(buffer, targetPos);
        } else buffer.writeBoolean(false);
        buffer.writeEnum(targetMode);
        buffer.writeInt(selectedMarkerId);
        buffer.writeInt(opticalTargetEntityId);
    }

}
