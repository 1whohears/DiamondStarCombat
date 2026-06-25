package com.onewhohears.dscombat.common.network.toclient;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.init.DataSerializers;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;

/**
 * Packet for sending distant gunfire/cannon sounds to clients.
 * Creates a "cannonade" effect that can be heard from far away.
 */
public class ToClientDistantGunfire extends BaseS2CMessage {
	
	public final String soundId;
	public final Vec3 pos;
	public final float volume, pitch;
	public final int durationTicks;
	
	public ToClientDistantGunfire(SoundEvent sound, Vec3 pos, float volume, float pitch, int durationTicks) {
		this.soundId = sound.getLocation().toString();
		this.pos = pos;
		this.volume = volume;
		this.pitch = pitch;
		this.durationTicks = durationTicks;
	}
	
	public ToClientDistantGunfire(FriendlyByteBuf buffer) {
		soundId = buffer.readUtf();
		pos = DataSerializers.VEC3.read(buffer);
		volume = buffer.readFloat();
		pitch = buffer.readFloat();
		durationTicks = buffer.readInt();
	}

	@Override
	public MessageType getType() {
		return PacketHandler.S2C_DISTANT_GUNFIRE;
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeUtf(soundId);
		DataSerializers.VEC3.write(buffer, pos);
		buffer.writeFloat(volume);
		buffer.writeFloat(pitch);
		buffer.writeInt(durationTicks);
	}

	@Override
	public void handle(NetworkManager.PacketContext context) {
		context.queue(() -> {
			com.onewhohears.dscombat.client.util.UtilClientPacket.distantGunfire(soundId, pos, volume, pitch, durationTicks);
		});
	}

}
