package com.onewhohears.dscombat;

import com.onewhohears.dscombat.client.input.DSCKeys;
import com.onewhohears.dscombat.client.overlay.PilotOverlay;
import com.onewhohears.dscombat.client.screen.AircraftBlockScreen;
import com.onewhohears.dscombat.client.screen.AircraftScreen;
import com.onewhohears.dscombat.client.screen.WeaponsBlockScreen;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.data.aircraft.AircraftPresetGenerator;
import com.onewhohears.dscombat.data.radar.RadarPresetGenerator;
import com.onewhohears.dscombat.data.weapon.WeaponPresetGenerator;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.init.ModBlockEntities;
import com.onewhohears.dscombat.init.ModBlocks;
import com.onewhohears.dscombat.init.ModContainers;
import com.onewhohears.dscombat.init.ModEntities;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.init.ModRecipeSerializers;
import com.onewhohears.dscombat.init.ModSounds;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod(DSCombatMod.MODID)
public class DSCombatMod {
	
	public static final String MODID = "dscombat";

    public DSCombatMod() {
    	ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.clientSpec);
    	ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.serverSpec);
    	IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    	
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
    	eventBus.addListener(this::onGatherData);
    }
    
    private void commonSetup(FMLCommonSetupEvent event) {
		PacketHandler.register();
	}
    
    private void clientSetup(FMLClientSetupEvent event) {
    	DSCKeys.init();
		OverlayRegistry.registerOverlayBottom("aircraft_stats", PilotOverlay.HUD_Aircraft_Stats);
    	MenuScreens.register(ModContainers.PLANE_MENU.get(), AircraftScreen::new);
    	MenuScreens.register(ModContainers.WEAPONS_BLOCK_MENU.get(), WeaponsBlockScreen::new);
    	MenuScreens.register(ModContainers.AIRCRAFT_BLOCK_MENU.get(), AircraftBlockScreen::new);
    }
    
    private void onGatherData(GatherDataEvent event) {
    	DataGenerator generator = event.getGenerator();
    	event.includeServer();
    	generator.addProvider(new AircraftPresetGenerator(generator));
    	generator.addProvider(new WeaponPresetGenerator(generator));
    	generator.addProvider(new RadarPresetGenerator(generator));
    }
    
}
