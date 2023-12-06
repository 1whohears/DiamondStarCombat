package com.onewhohears.dscombat.common.network.toclient;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.util.UtilPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToClientWeaponAmmo extends IPacket {
	
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
		//super(buffer);
		id = buffer.readInt();
		weaponId = buffer.readUtf();
		slotId = buffer.readUtf();
		ammo = buffer.readInt();
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		buffer.writeUtf(weaponId);
		buffer.writeUtf(slotId);
		buffer.writeInt(ammo);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				UtilPacket.weaponAmmoPacket(id, weaponId, slotId, ammo);
				success.set(true);
			});
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
