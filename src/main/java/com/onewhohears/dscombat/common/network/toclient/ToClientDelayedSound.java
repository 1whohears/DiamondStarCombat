package com.onewhohears.dscombat.common.network.toclient;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.onewhohears.dscombat.common.network.IPacket;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.util.UtilPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class ToClientDelayedSound extends IPacket {
	
	public final String soundId;
	public final Vec3 pos;
	public final float range, volume, pitch;
	
	public ToClientDelayedSound(SoundEvent sound, Vec3 pos, float range, float volume, float pitch) {
		this.soundId = sound.getLocation().toString();
		this.pos = pos;
		this.range = range;
		this.volume = volume;
		this.pitch = pitch;
	}
	
	public ToClientDelayedSound(FriendlyByteBuf buffer) {
		soundId = buffer.readUtf();
		pos = DataSerializers.VEC3.read(buffer);
		range = buffer.readFloat();
		volume = buffer.readFloat();
		pitch = buffer.readFloat();
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeUtf(soundId);
		DataSerializers.VEC3.write(buffer, pos);
		buffer.writeFloat(range);
		buffer.writeFloat(volume);
		buffer.writeFloat(pitch);
	}

	@Override
	public boolean handle(Supplier<Context> ctx) {
		final var success = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				UtilPacket.delayedSound(soundId, pos, range, volume, pitch);
				success.set(true);
			});
		});
		ctx.get().setPacketHandled(true);
		return success.get();
	}

}
