package com.onewhohears.dscombat.common.network.toclient;

import com.onewhohears.dscombat.entity.vehicle.hitbox.RotableHitbox;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;

public class ClientBoundAddRotableHitboxPacket extends ClientboundAddEntityPacket {

    private final int parentId;
    private final String hitboxName;

    public ClientBoundAddRotableHitboxPacket(RotableHitbox hitbox) {
        super(hitbox, 0);
        parentId = hitbox.getParent().getId();
        hitboxName = hitbox.getHitboxName();
    }

    public ClientBoundAddRotableHitboxPacket(FriendlyByteBuf buffer) {
        super(buffer);
        parentId = buffer.readInt();
        hitboxName = buffer.readUtf();
    }

    public void write(FriendlyByteBuf buffer) {
        super.write(buffer);
        buffer.writeInt(parentId);
        buffer.writeUtf(hitboxName);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener) {
        super.handle(clientGamePacketListener);
        Minecraft mc = Minecraft.getInstance();
        Entity entity = mc.level.getEntity(getId());
        if (entity instanceof RotableHitbox hitbox) {
            hitbox.handleClientAddEntityPacket(parentId, hitboxName);
        }
    }

}
