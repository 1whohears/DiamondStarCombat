package com.onewhohears.dscombat.common.network.toclient;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.data.parts.PartData;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.util.UtilPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToClientAddPart extends IPacket {
	
	public final int id;
	public final String slotName;
	public final PartData data;
	
	public ToClientAddPart(int id, String slotName, PartData data) {
		this.id = id;
		this.slotName = slotName;
		this.data = data;
	}
	
	public ToClientAddPart(FriendlyByteBuf buffer) {
		super(buffer);
		id = buffer.readInt();
		slotName = buffer.readUtf();
		data = DataSerializers.PART_DATA.read(buffer);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		buffer.writeUtf(slotName);
		data.write(buffer);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				UtilPacket.addPartPacket(id, slotName, data);
				success.set(true);
			});
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
