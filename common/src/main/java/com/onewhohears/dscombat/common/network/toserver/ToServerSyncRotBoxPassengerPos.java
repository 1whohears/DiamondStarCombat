package com.onewhohears.dscombat.common.network.toserver;

import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.onewholibs.util.UtilEntity;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ToServerSyncRotBoxPassengerPos extends BaseC2SMessage {
	
	private final int[] ids;
	private final Vec3[] pos;
	
	/**
	 * ids and pos arrays must be the same length!
	 * @param ids
	 * @param pos
	 */
	public ToServerSyncRotBoxPassengerPos(int[] ids, Vec3[] pos) {
		this.ids = ids;
		this.pos = pos;
	}
	
	public ToServerSyncRotBoxPassengerPos(FriendlyByteBuf buffer) {
		ids = buffer.readVarIntArray();
		pos = new Vec3[ids.length];
		for (int i = 0; i < pos.length; ++i)
			pos[i] = DataSerializers.VEC3.read(buffer);
	}

    @Override
    public MessageType getType() {
        return null;
    }

    @Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeVarIntArray(ids);
		for (int i = 0; i < pos.length; ++i) 
			DataSerializers.VEC3.write(buffer, pos[i]);
	}

	@Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            Player player = context.getPlayer();
			Level level = player.level;
			for (int i = 0; i < ids.length; ++i) {
				if (pos[i].y == -1000) continue;
				Entity entity = level.getEntity(ids[i]);
				if (entity == null) continue;
				if (UtilEntity.isPlayer(entity)) continue;
				entity.setPos(pos[i]);
			}
		});
	}

}
