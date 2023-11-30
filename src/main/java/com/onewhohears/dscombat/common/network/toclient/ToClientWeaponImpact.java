package com.onewhohears.dscombat.common.network.toclient;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.util.UtilPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToClientWeaponImpact extends IPacket {
	
	public final int weaponId;
	//public final int ownerId;
	//public final int targetId;
	public final Vec3 pos;
	
	public ToClientWeaponImpact(EntityWeapon weapon, HitResult result) {
		this.weaponId = weapon.getId();
		/*if (weapon.getOwner() == null) ownerId = -1;
		else ownerId = weapon.getOwner().getId();
		if (result.getType() != Type.ENTITY) targetId = -1;
		else targetId = ((EntityHitResult)result).getEntity().getId();*/
		this.pos = result.getLocation();
	}
	
	public ToClientWeaponImpact(FriendlyByteBuf buffer) {
		weaponId = buffer.readInt();
		//ownerId = buffer.readInt();
		//targetId = buffer.readInt();
		pos = DataSerializers.VEC3.read(buffer);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(weaponId);
		//buffer.writeInt(ownerId);
		//buffer.writeInt(targetId);
		DataSerializers.VEC3.write(buffer, pos);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				UtilPacket.weaponImpact(weaponId, pos);
				success.set(true);
			});
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
