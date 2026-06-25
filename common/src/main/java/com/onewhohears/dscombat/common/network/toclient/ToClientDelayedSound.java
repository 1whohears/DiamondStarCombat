package com.onewhohears.dscombat.common.network.toclient;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.init.DataSerializers;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;

public class ToClientDelayedSound extends BaseS2CMessage {
	
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
    public MessageType getType() {
        return PacketHandler.S2C_DELAYED_SOUND;
    }

    @Override
	public void write(FriendlyByteBuf buffer) {
		buffer.writeUtf(soundId);
		DataSerializers.VEC3.write(buffer, pos);
		buffer.writeFloat(range);
		buffer.writeFloat(volume);
		buffer.writeFloat(pitch);
	}

	@Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            com.onewhohears.dscombat.client.util.UtilClientPacket.delayedSound(soundId, pos, range, volume, pitch);
		});
	}

}
