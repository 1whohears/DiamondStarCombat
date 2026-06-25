package com.onewhohears.dscombat.common.network.toclient;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.data.weapon.stats.WeaponStats;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.init.DataSerializers;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class ToClientWeaponImpact extends BaseS2CMessage {
	
	public final WeaponStats.WeaponClientImpactType impactType;
	public final Vec3 pos;
	public final float explosionRadius;
	public final boolean causesFire;
	
	public ToClientWeaponImpact(EntityWeapon<?> weapon, Vec3 pos) {
		this.impactType = weapon.getClientImpactType();
		this.pos = pos;
		this.explosionRadius = weapon.getExplosionRadius();
		this.causesFire = weapon.isCausesFire();
	}
	
	public ToClientWeaponImpact(FriendlyByteBuf buffer) {
		impactType = WeaponStats.WeaponClientImpactType.getByOrdinal(buffer.readInt());
		pos = DataSerializers.VEC3.read(buffer);
		explosionRadius = buffer.readFloat();
		causesFire = buffer.readBoolean();
	}

    @Override
    public MessageType getType() {
        return PacketHandler.S2C_WEAPON_IMPACT;
    }

    @Override
	public void write(FriendlyByteBuf buffer) {
		if (impactType == null) {
			buffer.writeInt(-1);
		} else {
			buffer.writeInt(impactType.ordinal());
		}
		DataSerializers.VEC3.write(buffer, pos);
		buffer.writeFloat(explosionRadius);
		buffer.writeBoolean(causesFire);
	}

	@Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
			if (impactType != null) {
				com.onewhohears.dscombat.client.util.UtilClientPacket.weaponImpact(impactType, pos, explosionRadius, causesFire);
			}
		});
	}

}
