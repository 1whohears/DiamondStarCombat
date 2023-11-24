package com.onewhohears.dscombat.common.network.toclient;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.data.aircraft.EntityScreenData;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.util.UtilPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToClientVehicleScreenDebug extends IPacket {
	
	public final int type;
	public final Vec3 rel_pos;
	public final float width, height;
	public final float rel_x_rot, rel_y_rot, rel_z_rot;
	
	public ToClientVehicleScreenDebug(EntityScreenData screenData) {
		type = screenData.type;
		rel_pos = screenData.rel_pos;
		width = screenData.width;
		height = screenData.height;
		rel_x_rot = screenData.xRot;
		rel_y_rot = screenData.yRot;
		rel_z_rot = screenData.zRot;
	}
	
	public ToClientVehicleScreenDebug(FriendlyByteBuf buffer) {
		type = buffer.readInt();
		rel_pos = DataSerializers.VEC3.read(buffer);
		width = buffer.readFloat();
		height = buffer.readFloat();
		rel_x_rot = buffer.readFloat();
		rel_y_rot = buffer.readFloat();
		rel_z_rot = buffer.readFloat();
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(type);
		DataSerializers.VEC3.write(buffer, rel_pos);
		buffer.writeFloat(width);
		buffer.writeFloat(height);
		buffer.writeFloat(rel_x_rot);
		buffer.writeFloat(rel_y_rot);
		buffer.writeFloat(rel_z_rot);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				UtilPacket.vehicleScreenDebug(type, rel_pos, width, height, rel_x_rot, rel_y_rot, rel_z_rot);
				success.set(true);
			});
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
