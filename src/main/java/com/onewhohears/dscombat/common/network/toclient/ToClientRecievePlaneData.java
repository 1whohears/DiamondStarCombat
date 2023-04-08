package com.onewhohears.dscombat.common.network.toclient;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.data.parts.PartSlot;
import com.onewhohears.dscombat.data.parts.PartsManager;
import com.onewhohears.dscombat.data.radar.RadarSystem;
import com.onewhohears.dscombat.data.weapon.WeaponSystem;
import com.onewhohears.dscombat.util.UtilPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

/*
 * TODO ToClientRecievePlaneData shouldn't be needed!
 * same with the add/remove radars and weapons
 * these should get updated when item changes in plane menu get sent to client
 * it's unnecessary extra data send from server to client
 */

public class ToClientRecievePlaneData extends IPacket {
	
	public final int id;
	public final List<PartSlot> slots;
	public final WeaponSystem ws;
	public final RadarSystem rs;
	
	public ToClientRecievePlaneData(int id, List<PartSlot> slots, WeaponSystem ws, RadarSystem rs) {
		this.id = id;
		this.slots = slots;
		this.ws = ws;
		this.rs = rs;
		//System.out.println("packet constructor "+pm);
	}
	
	public ToClientRecievePlaneData(FriendlyByteBuf buffer) {
		super(buffer);
		id = buffer.readInt();
		slots = PartsManager.readSlotsFromBuffer(buffer);
		ws = new WeaponSystem(buffer);
		rs = new RadarSystem(buffer);
		//System.out.println("decoding "+pm);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		PartsManager.writeSlotsToBuffer(buffer, slots);
		ws.write(buffer);
		rs.write(buffer);
		//System.out.println("encoding "+pm);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				UtilPacket.planeDataPacket(id, slots, ws, rs);
				success.set(true);
			});
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
