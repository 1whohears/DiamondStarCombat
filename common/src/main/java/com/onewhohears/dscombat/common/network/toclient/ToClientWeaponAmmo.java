package com.onewhohears.dscombat.common.network.toclient;

import com.onewhohears.dscombat.common.network.PacketHandler;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;

public class ToClientWeaponAmmo extends BaseS2CMessage {
	
	public final int id;
	public final String weaponId;
	public final String slotId;
	public final int ammo;
	
	public ToClientWeaponAmmo(int id, String weaponId, String slotId, int ammo) {
		this.id = id;
		this.weaponId = weaponId;
		this.slotId = slotId;
		this.ammo = ammo;
	}
	
	public ToClientWeaponAmmo(FriendlyByteBuf buffer) {
		id = buffer.readInt();
		weaponId = buffer.readUtf();
		slotId = buffer.readUtf();
		ammo = buffer.readInt();
	}

    @Override
    public MessageType getType() {
        return PacketHandler.S2C_WEAPON_AMMO;
    }

    @Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		buffer.writeUtf(weaponId);
		buffer.writeUtf(slotId);
		buffer.writeInt(ammo);
	}

	@Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            com.onewhohears.dscombat.client.util.UtilClientPacket.weaponAmmoPacket(id, weaponId, slotId, ammo);
		});
	}

}
