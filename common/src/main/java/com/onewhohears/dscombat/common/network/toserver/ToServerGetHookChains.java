package com.onewhohears.dscombat.common.network.toserver;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.entity.parts.EntityChainHook;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class ToServerGetHookChains extends BaseC2SMessage {
			
	public final int hookId;
	
	public ToServerGetHookChains(EntityChainHook hook) {
		hookId = hook.getId();
	}
	
	public ToServerGetHookChains(FriendlyByteBuf buffer) {
		hookId = buffer.readInt();
	}

    @Override
    public MessageType getType() {
        return PacketHandler.C2S_GET_HOOK_CHAINS;
    }

    @Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(hookId);
	}

	@Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            Player player = context.getPlayer();
			if (player.level.getEntity(hookId) instanceof EntityChainHook hook) {
				hook.sendAllVehicleChainsToClient((ServerPlayer) player);
			}
		});
	}

}
