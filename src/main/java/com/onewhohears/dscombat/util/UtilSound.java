package com.onewhohears.dscombat.util;

import java.util.Collection;
import java.util.NoSuchElementException;

import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.common.network.toclient.ToClientDelayedSound;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PacketDistributor.TargetPoint;
import net.minecraftforge.registries.ForgeRegistries;

public class UtilSound {
	
	public static SoundEvent getSoundById(String id, SoundEvent alt) {
		try {
			return ForgeRegistries.SOUND_EVENTS.getDelegate(new ResourceLocation(id)).get().get();
		} catch(NoSuchElementException e) {
			return alt;
		}
	}
	
	public static void sendDelayedSound(SoundEvent sound, Vec3 pos, float radius, ResourceKey<Level> dim, float volume, float pitch) {
		PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(TargetPoint.p(pos.x, pos.y, pos.z, radius, dim)), 
				new ToClientDelayedSound(sound, pos, radius, volume, pitch));
	}
	
	public static SoundEvent getRandomSound() {
		Collection<SoundEvent> sounds = ForgeRegistries.SOUND_EVENTS.getValues();
		int size = sounds.size();
		int item = UtilParticles.random.nextInt(size);
		int i = 0;
		for(SoundEvent sound : sounds) {
			if (i == item) return sound;
			i++;
		}
		return SoundEvents.GENERIC_EXPLODE;
	}
	
}
