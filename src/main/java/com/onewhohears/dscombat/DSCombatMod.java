package com.onewhohears.dscombat;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.onewhohears.dscombat.common.PacketHandler;
import com.onewhohears.dscombat.common.event.CommonForgeEvents;
import com.onewhohears.dscombat.data.AircraftPresets;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.init.ModEntities;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
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
        
    	DataSerializers.init(eventBus);
    	ModEntities.register(eventBus);
    	eventBus.addListener(this::commonSetup);
    	
    	MinecraftForge.EVENT_BUS.register(new CommonForgeEvents());
        MinecraftForge.EVENT_BUS.register(this);
        
        AircraftPresets.setupPresets();
        
        // TODO unload chunks when unloading world
    }
    
    private void commonSetup(final FMLCommonSetupEvent event) {
    	LOGGER.info("HELLO FROM PREINIT");
		PacketHandler.register();
	}
    
}
