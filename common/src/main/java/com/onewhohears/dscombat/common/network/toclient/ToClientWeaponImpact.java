package com.onewhohears.dscombat.common.network.toclient;

import com.onewhohears.dscombat.data.weapon.stats.WeaponStats;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.util.UtilClientPacket;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class ToClientWeaponImpact extends BaseS2CMessage {
	
	public final WeaponStats.WeaponClientImpactType impactType;
	public final Vec3 pos;
	
	public ToClientWeaponImpact(EntityWeapon<?> weapon, Vec3 pos) {
		this.impactType = weapon.getClientImpactType();
		this.pos = pos;
	}
	
	public ToClientWeaponImpact(FriendlyByteBuf buffer) {
		impactType = WeaponStats.WeaponClientImpactType.getByOrdinal(buffer.readInt());
		pos = DataSerializers.VEC3.read(buffer);
	}

    @Override
    public MessageType getType() {
        return null;
    }

    @Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(impactType.ordinal());
		DataSerializers.VEC3.write(buffer, pos);
	}

	@Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            UtilClientPacket.weaponImpact(impactType, pos);
		});
	}

}
