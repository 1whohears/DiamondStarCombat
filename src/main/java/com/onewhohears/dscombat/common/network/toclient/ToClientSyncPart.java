package com.onewhohears.dscombat.common.network.toclient;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.data.parts.instance.PartInstance;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.util.UtilClientPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToClientSyncPart extends IPacket {
	
	private final int id;
	private final String slotId;
	private PartInstance<?> instance;
	private FriendlyByteBuf buffer;
	
	public ToClientSyncPart(EntityVehicle vehicle, PartInstance<?> instance) {
		this.id = vehicle.getId();
		this.slotId = instance.getSlotId();
		this.instance = instance;
	}
	
	public ToClientSyncPart(FriendlyByteBuf buffer) {
		super(buffer);
		id = buffer.readInt();
		slotId = buffer.readUtf();
		buffer.readUtf(); // read preset id cause it ain't needed
		this.buffer = buffer;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		buffer.writeUtf(slotId);
		instance.writeBuffer(buffer);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				UtilClientPacket.syncPartPacket(id, slotId, this.buffer);
				success.set(true);
			});
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
