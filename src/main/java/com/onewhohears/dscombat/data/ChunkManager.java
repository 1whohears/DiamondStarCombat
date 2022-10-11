package com.onewhohears.dscombat.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.entity.weapon.EntityMissile;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.world.ForgeChunkManager;

public class ChunkManager {
	
	public static final int maxTicks = 200;
	
	public static List<LoadedChunk> chunks = new ArrayList<LoadedChunk>();
	
	public static void serverTick(MinecraftServer server) {
		System.out.println(debug());
		for (int i = 0; i < chunks.size(); ++i) {
			chunks.get(i).tick();
			if (chunks.get(i).isUnloaded()) chunks.remove(i--);
		}
	}
	
	public static void addChunk(EntityMissile missile, int chunkX, int chunkZ) {
		ServerLevel level = (ServerLevel) missile.level;
		UUID uuid = missile.getUUID();
		for (int i = 0; i < chunks.size(); ++i) {
			LoadedChunk c = chunks.get(i);
			if (c.chunkX == chunkX && c.chunkZ == chunkZ 
					&& c.level.dimensionType() == level.dimensionType()) {
				c.update(uuid);
				System.out.println("UPDATED LOADED CHUNK "+c);
				return;
			}
		}
		/*long pos = level.getChunk(chunkX, chunkZ).getPos().toLong();
		if (level.getChunkSource().isPositionTicking(pos)) { // this probably doesn't work
			System.out.println("CHUNK ALREADY TICKING ["+chunkX+":"+chunkZ+"]");
			return;
		}*/
		chunks.add(new LoadedChunk(level, chunkX, chunkZ, uuid));
		System.out.println("ADDED LOADED CHUNK "+chunks.get(chunks.size()-1));
	}
	
	public static class LoadedChunk {
		
		private ServerLevel level;
		private int chunkX;
		private int chunkZ;
		private UUID uuid;
		private int ticks = 0;
		private boolean unload = false;
		
		public LoadedChunk(ServerLevel level, int chunkX, int chunkZ, UUID uuid) {
			this.level = level;
			this.chunkX = chunkX;
			this.chunkZ = chunkZ;
			this.uuid = uuid;
			ForgeChunkManager.forceChunk(level, DSCombatMod.MODID, 
					this.uuid, this.chunkX, this.chunkZ, true, true);
		}
		
		public void tick() {
			if (ticks > maxTicks) {
				ForgeChunkManager.forceChunk(level, DSCombatMod.MODID, 
						this.uuid, this.chunkX, this.chunkZ, false, false);
				unload = true;
				System.out.println("UNLOADED "+this);
			}
			++ticks;
		}
		
		public void update(UUID uuid) {
			/*if (ticks > 1 && this.uuid != uuid) {
				ForgeChunkManager.forceChunk(level, DSCombatMod.MODID, 
						this.uuid, this.chunkX, this.chunkZ, false, false);
				this.uuid = uuid;
				ForgeChunkManager.forceChunk(level, DSCombatMod.MODID, 
						this.uuid, this.chunkX, this.chunkZ, true, true);
			}*/
			ticks = 0;
		}
		
		public boolean isUnloaded() {
			return unload;
		}
		
		@Override
		public String toString() {
			return "["+level+":"+chunkX+":"+chunkZ/*+":"+uuid*/+":"+ticks+"]";
		}
		
	}
	
	public static String debug() {
		String r = "ChunkManager chunks: ";
		for (int i = 0; i < chunks.size(); ++i) r += chunks.get(i);
		return r;
	}
	
}
