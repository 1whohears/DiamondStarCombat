package com.onewhohears.dscombat.common.network.toserver;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.onewholibs.util.UtilEntity;
import com.onewhohears.onewholibs.util.math.QuaternionF;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.DataSerializers;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * this packet synchronizes the vehicle's speed and rotation from the pilot client's perspective with the server.
 * this is done to the client side doesn't have to worry about server lag-backs causing sudden 
 * drastic changes in the vehicle's position, speed and/or rotation.
 * @author 1whohears
 */
public class ToServerVehicleMoveRot extends BaseC2SMessage {
	
	public final int id;
	public final Vec3 motion;
	public final QuaternionF q;
	public final Vec3 av;
	
	public ToServerVehicleMoveRot(EntityVehicle e) {
		this.id = e.getId();
		this.motion = e.getDeltaMovement();
		this.q = e.getClientQ();
		this.av = e.clientAV;
	}
	
	public ToServerVehicleMoveRot(FriendlyByteBuf buffer) {
		id = buffer.readInt();
		motion = DataSerializers.VEC3.read(buffer);
		q = DataSerializers.QUATERNION.read(buffer);
		av = DataSerializers.VEC3.read(buffer);
	}

    @Override
    public MessageType getType() {
        return PacketHandler.C2S_VEHICLE_MOVE_ROT;
    }

    @Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(id);
		DataSerializers.VEC3.write(buffer, motion);
		DataSerializers.QUATERNION.write(buffer, q);
		DataSerializers.VEC3.write(buffer, av);
	}

	@Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            Player player = context.getPlayer();
            Level level = UtilEntity.getLevel(player);
			if (level.getEntity(id) instanceof EntityVehicle plane) {
				plane.setDeltaMovement(motion);
				plane.setPrevQ(plane.getQ());
				plane.setQ(q);
				plane.setAngularVel(av);
			}
		});
	}

}
