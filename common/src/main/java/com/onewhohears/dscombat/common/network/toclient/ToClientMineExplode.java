package com.onewhohears.dscombat.common.network.toclient;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.entity.weapon.EntityMine;
import com.onewhohears.dscombat.init.DataSerializers;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class ToClientMineExplode extends BaseS2CMessage {
	
	public final Vec3 pos;
	public final boolean isAntiTank;
	
	public ToClientMineExplode(EntityMine mine) {
		this.pos = mine.position();
		this.isAntiTank = mine.getMineType() == EntityMine.MineType.ANTI_TANK;
	}
	
	public ToClientMineExplode(FriendlyByteBuf buffer) {
		pos = DataSerializers.VEC3.read(buffer);
		isAntiTank = buffer.readBoolean();
	}

	@Override
	public MessageType getType() {
		return PacketHandler.S2C_MINE_EXPLODE;
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		DataSerializers.VEC3.write(buffer, pos);
		buffer.writeBoolean(isAntiTank);
	}

	@Override
	public void handle(NetworkManager.PacketContext context) {
		context.queue(() -> {
			com.onewhohears.dscombat.client.util.UtilClientPacket.mineExplode(pos, isAntiTank);
		});
	}

}
