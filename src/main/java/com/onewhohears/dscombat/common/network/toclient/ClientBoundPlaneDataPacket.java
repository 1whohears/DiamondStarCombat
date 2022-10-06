package com.onewhohears.dscombat.common.network.toclient;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.data.PartsManager;
import com.onewhohears.dscombat.data.WeaponSystem;
import com.onewhohears.dscombat.util.UtilPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class ClientBoundPlaneDataPacket extends IPacket {
	
	public final int id;
	public final PartsManager pm;
	public final WeaponSystem ws;
	
	public ClientBoundPlaneDataPacket(int id, PartsManager pm, WeaponSystem ws) {
		this.id = id;
		this.pm = pm;
		this.ws = ws;
		System.out.println("packet constructor "+pm);
	}
	
	public ClientBoundPlaneDataPacket(FriendlyByteBuf buffer) {
		id = buffer.readInt();
		pm = new PartsManager(buffer);
		ws = new WeaponSystem(buffer);
		System.out.println("decoding "+pm);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		pm.write(buffer);
		ws.write(buffer);
		System.out.println("encoding "+pm);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				UtilPacket.planeDataPacket(id, pm, ws);
				success.set(true);
			});
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
