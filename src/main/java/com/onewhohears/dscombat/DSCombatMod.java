package com.onewhohears.dscombat;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.onewhohears.dscombat.client.screen.AircraftBlockScreen;
import com.onewhohears.dscombat.client.screen.AircraftScreen;
import com.onewhohears.dscombat.client.screen.WeaponsBlockScreen;
import com.onewhohears.dscombat.common.event.CommonForgeEvents;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.data.AircraftPresets;
import com.onewhohears.dscombat.data.radar.RadarPresets;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.init.ModBlockEntities;
import com.onewhohears.dscombat.init.ModBlocks;
import com.onewhohears.dscombat.init.ModContainers;
import com.onewhohears.dscombat.init.ModEntities;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.init.ModRecipeSerializers;
import com.onewhohears.dscombat.init.ModSounds;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DSCombatMod.MODID)
public class DSCombatMod
{
	public static final String MODID = "dscombat";
	
    private static final Logger LOGGER = LogUtils.getLogger();

    public DSCombatMod() {
    	IEventBus eventBus =  FMLJavaModLoadingContext.get().getModEventBus();
    	// ORDER MATTERS
    	WeaponPresets.setupPresets();
    	RadarPresets.setupPresets();
        AircraftPresets.setupPresets();
    	
        ModBlocks.register(eventBus);
        ModContainers.register(eventBus);
        ModEntities.register(eventBus);
        ModItems.register(eventBus);
        ModRecipeSerializers.register(eventBus);
        ModSounds.register(eventBus);
        ModBlockEntities.register(eventBus);
    	DataSerializers.register(eventBus);
    	
    	eventBus.addListener(this::commonSetup);
    	eventBus.addListener(this::clientSetup);
    	
    	MinecraftForge.EVENT_BUS.register(new CommonForgeEvents());
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    private void commonSetup(FMLCommonSetupEvent event) {
    	LOGGER.info("HELLO FROM PREINIT");
		PacketHandler.register();
	}
    
    private void clientSetup(FMLClientSetupEvent event) {
    	MenuScreens.register(ModContainers.PLANE_MENU.get(), AircraftScreen::new);
    	MenuScreens.register(ModContainers.WEAPONS_BLOCK_MENU.get(), WeaponsBlockScreen::new);
    	MenuScreens.register(ModContainers.AIRCRAFT_BLOCK_MENU.get(), AircraftBlockScreen::new);
    }
    
    @SubscribeEvent
	public static void serverStoping(ServerStoppingEvent event) {
    	//LOGGER.info("SERVER STOPPING "+event.getServer());
		//ChunkManager.unloadAll(event.getServer());
	}
	
	@SubscribeEvent
	public static void levelUnload(LevelEvent.Unload event) {
		//LOGGER.info("LEVEL UNLOADING "+event.getLevel());
		//ChunkManager.unloadAllInLevel(event.getLevel());
	}
    
}
