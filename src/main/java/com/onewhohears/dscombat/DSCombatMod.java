package com.onewhohears.dscombat;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.onewhohears.dscombat.common.PacketHandler;
import com.onewhohears.dscombat.common.event.CommonForgeEvents;
import com.onewhohears.dscombat.data.AircraftPresets;
import com.onewhohears.dscombat.data.ChunkManager;
import com.onewhohears.dscombat.data.RadarPresets;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.init.ModContainers;
import com.onewhohears.dscombat.init.ModEntities;
import com.onewhohears.dscombat.init.ModSounds;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DSCombatMod.MODID)
public class DSCombatMod
{
	public static final String MODID = "dscombat";
	
    private static final Logger LOGGER = LogUtils.getLogger();

    public DSCombatMod() {
    	IEventBus eventBus =  FMLJavaModLoadingContext.get().getModEventBus();
        
    	DataSerializers.register(eventBus);
    	ModEntities.register(eventBus);
    	ModSounds.register(eventBus);
    	ModContainers.register(eventBus);
    	eventBus.addListener(this::commonSetup);
    	
    	MinecraftForge.EVENT_BUS.register(new CommonForgeEvents());
        MinecraftForge.EVENT_BUS.register(this);
        
        AircraftPresets.setupPresets();
        WeaponPresets.setupPresets();
        RadarPresets.setupPresets();
    }
    
    private void commonSetup(final FMLCommonSetupEvent event) {
    	LOGGER.info("HELLO FROM PREINIT");
		PacketHandler.register();
	}
    
    @SubscribeEvent
	public static void serverStoping(ServerStoppingEvent event) {
    	LOGGER.info("SERVER STOPPING "+event.getServer());
		ChunkManager.unloadAll(event.getServer());
	}
	
	@SubscribeEvent
	public static void levelUnload(LevelEvent.Unload event) {
		LOGGER.info("LEVEL UNLOADING "+event.getLevel());
		ChunkManager.unloadAllInLevel(event.getLevel());
	}
    
}
