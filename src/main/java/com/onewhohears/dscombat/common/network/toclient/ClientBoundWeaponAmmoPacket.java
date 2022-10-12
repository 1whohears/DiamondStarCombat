package com.onewhohears.dscombat.common.network.toclient;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.util.UtilPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class ClientBoundWeaponAmmoPacket extends IPacket {
	
	public final int id;
	public final String weaponId;
	public final int ammo;
	
	public ClientBoundWeaponAmmoPacket(int id, String weaponId, int ammo) {
		this.id = id;
		this.weaponId = weaponId;
		this.ammo = ammo;
	}
	
	public ClientBoundWeaponAmmoPacket(FriendlyByteBuf buffer) {
		id = buffer.readInt();
		weaponId = buffer.readUtf();
		ammo = buffer.readInt();
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		buffer.writeUtf(weaponId);
		buffer.writeInt(ammo);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				UtilPacket.weaponAmmoPacket(id, weaponId, ammo);
				success.set(true);
			});
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}