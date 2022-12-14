package com.onewhohears.dscombat;

import com.onewhohears.dscombat.client.screen.AircraftBlockScreen;
import com.onewhohears.dscombat.client.screen.AircraftScreen;
import com.onewhohears.dscombat.client.screen.WeaponsBlockScreen;
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
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DSCombatMod.MODID)
public class DSCombatMod
{
	public static final String MODID = "dscombat";
	
    //private static final Logger LOGGER = LogUtils.getLogger();

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
    }
    
    private void commonSetup(FMLCommonSetupEvent event) {
		PacketHandler.register();
	}
    
    private void clientSetup(FMLClientSetupEvent event) {
    	MenuScreens.register(ModContainers.PLANE_MENU.get(), AircraftScreen::new);
    	MenuScreens.register(ModContainers.WEAPONS_BLOCK_MENU.get(), WeaponsBlockScreen::new);
    	MenuScreens.register(ModContainers.AIRCRAFT_BLOCK_MENU.get(), AircraftBlockScreen::new);
    }
    
}
