package com.onewhohears.dscombat.common.network.toclient;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.init.DataSerializers;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class ToClientBallisticExplosion extends BaseS2CMessage {
    
    private final Vec3 pos;
    private final float radius;
    private final boolean fire;
    
    public ToClientBallisticExplosion(Vec3 pos, float radius, boolean fire) {
        this.pos = pos;
        this.radius = radius;
        this.fire = fire;
    }
    
    public ToClientBallisticExplosion(FriendlyByteBuf buffer) {
        this.pos = DataSerializers.VEC3.read(buffer);
        this.radius = buffer.readFloat();
        this.fire = buffer.readBoolean();
    }
    
    @Override
    public MessageType getType() {
        return PacketHandler.S2C_BALLISTIC_EXPLOSION;
    }
    
    @Override
    public void write(FriendlyByteBuf buffer) {
        DataSerializers.VEC3.write(buffer, pos);
        buffer.writeFloat(radius);
        buffer.writeBoolean(fire);
    }
    
    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> com.onewhohears.dscombat.client.util.UtilClientPacket.ballisticExplosion(pos, radius, fire));
    }
}
