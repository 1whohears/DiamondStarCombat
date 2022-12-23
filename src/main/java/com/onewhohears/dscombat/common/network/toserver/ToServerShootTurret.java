package com.onewhohears.dscombat.common.network.toserver;

import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToServerShootTurret extends IPacket {
	
	public ToServerShootTurret() {
		
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		// TODO Auto-generated method stub
		return false;
	}

}
