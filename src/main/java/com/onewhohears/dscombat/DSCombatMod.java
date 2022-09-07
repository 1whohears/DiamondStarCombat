package com.onewhohears.dscombat;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
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

    public DSCombatMod()
    {
    	IEventBus eventBus =  FMLJavaModLoadingContext.get().getModEventBus();
        
    	DataSerializers.init(eventBus);
    	ModEntities.register(eventBus);
    	eventBus.addListener(this::setup);
        
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("HELLO FROM PREINIT");
    }
}
