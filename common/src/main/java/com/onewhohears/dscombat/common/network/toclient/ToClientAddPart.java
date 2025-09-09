package com.onewhohears.dscombat.common.network.toclient;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.data.parts.instance.PartInstance;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.util.UtilClientPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToClientAddPart extends IPacket {
	
	public final int id;
	public final String slotId;
	public final PartInstance<?> data;
	
	public ToClientAddPart(int id, String slotId, PartInstance<?> data) {
		this.id = id;
		this.slotId = slotId;
		this.data = data;
	}
	
	public ToClientAddPart(FriendlyByteBuf buffer) {
		super(buffer);
		id = buffer.readInt();
		slotId = buffer.readUtf();
		data = DataSerializers.PART_DATA.read(buffer);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		buffer.writeUtf(slotId);
		DataSerializers.PART_DATA.write(buffer, data);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				UtilClientPacket.addPartPacket(id, slotId, data);
				success.set(true);
			});
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
