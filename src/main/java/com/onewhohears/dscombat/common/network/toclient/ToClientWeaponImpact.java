package com.onewhohears.dscombat.common.network.toclient;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.data.weapon.WeaponData;
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
	
	public final WeaponData.WeaponClientImpactType impactType;
	public final Vec3 pos;
	
	public ToClientWeaponImpact(EntityWeapon weapon, HitResult result) {
		this.impactType = weapon.getClientImpactType();
		this.pos = result.getLocation();
	}
	
	public ToClientWeaponImpact(FriendlyByteBuf buffer) {
		impactType = WeaponData.WeaponClientImpactType.getByOrdinal(buffer.readInt());
		pos = DataSerializers.VEC3.read(buffer);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(impactType.ordinal());
		DataSerializers.VEC3.write(buffer, pos);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				UtilPacket.weaponImpact(impactType, pos);
				success.set(true);
			});
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
