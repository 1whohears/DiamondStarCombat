package com.onewhohears.dscombat;

import com.onewhohears.dscombat.command.DSCGameRules;
import com.onewhohears.dscombat.common.network.PacketHandler;
import com.onewhohears.dscombat.data.parts.PartPresetGenerator;
import com.onewhohears.dscombat.data.radar.RadarPresetGenerator;
import com.onewhohears.dscombat.data.recipe.DSCRecipeGenerator;
import com.onewhohears.dscombat.data.sound.DSCSoundDefinitionGen;
import com.onewhohears.dscombat.data.tag.BlockTagGen;
import com.onewhohears.dscombat.data.tag.EntityTypeTagGen;
import com.onewhohears.dscombat.data.tag.FluidTagGen;
import com.onewhohears.dscombat.data.tag.ItemTagGen;
import com.onewhohears.dscombat.data.vehicle.VehiclePresetGenerator;
import com.onewhohears.dscombat.data.vehicle.client.VehicleClientPresetGenerator;
import com.onewhohears.dscombat.data.weapon.WeaponPresetGenerator;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.init.ModArgumentTypes;
import com.onewhohears.dscombat.init.ModBlockEntities;
import com.onewhohears.dscombat.init.ModBlocks;
import com.onewhohears.dscombat.init.ModContainers;
import com.onewhohears.dscombat.init.ModEntities;
import com.onewhohears.dscombat.init.ModFluids;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.init.ModParticles;
import com.onewhohears.dscombat.init.ModRecipes;
import com.onewhohears.dscombat.init.ModSounds;
import com.onewhohears.dscombat.init.ModTags;
import com.onewhohears.dscombat.init.ModVillagers;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * This is the main class of the Diamond Star Combat mod.
 * Some events are registered here but most are subscribed via Annotation. 
 * Here are some of the event classes and other possibly relevant Entry Points:
 * {@link com.onewhohears.dscombat.client.event.forgebus.ClientCameraEvents}
 * {@link com.onewhohears.dscombat.client.event.forgebus.ClientInputEvents}
 * {@link com.onewhohears.dscombat.client.event.forgebus.ClientRenderEvents}
 * {@link com.onewhohears.dscombat.client.event.ClientModEvents}
 * {@link com.onewhohears.dscombat.common.event.CommonForgeEvents}
 * {@link EntityVehicle}
 * 
 * @author 1whohears
 */
@Mod(DSCombatMod.MODID)
public class DSCombatMod {
	
	public static final String MODID = "dscombat";
	
	public static boolean minigamesLoaded = false;

    public DSCombatMod() {
    	ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.clientSpec);
    	ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.commonSpec);
    	
    	IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    	
        ModBlocks.register(eventBus);
        ModFluids.register(eventBus);
        ModContainers.register(eventBus);
        ModEntities.register(eventBus);
        ModItems.register(eventBus);
        ModRecipes.register(eventBus);
        ModSounds.register(eventBus);
        ModBlockEntities.register(eventBus);
    	DataSerializers.register(eventBus);
    	ModVillagers.register(eventBus);
    	ModParticles.register(eventBus);
    	ModArgumentTypes.register(eventBus);
    	ModTags.init();
    	
    	minigamesLoaded = ModList.get().isLoaded("minigames");
    	
    	eventBus.addListener(this::commonSetup);
    	eventBus.addListener(this::onGatherData);
    }
    
    private void commonSetup(FMLCommonSetupEvent event) {
		PacketHandler.register();
		DSCGameRules.registerAll();
		DependencySafety.fmlCommonSetup();
		event.enqueueWork(ModVillagers::registerPOIs);
	}
    
    private void onGatherData(GatherDataEvent event) {
    	DataGenerator generator = event.getGenerator();
    	if (event.includeServer()) {
    		generator.addProvider(true, new VehiclePresetGenerator(generator));
    		generator.addProvider(true, new WeaponPresetGenerator(generator));
    		generator.addProvider(true, new RadarPresetGenerator(generator));
    		generator.addProvider(true, new PartPresetGenerator(generator));
    		DependencySafety.serverDataGen(generator);
    		generator.addProvider(true, new DSCRecipeGenerator(generator));
    		generator.addProvider(true, new EntityTypeTagGen(generator, event.getExistingFileHelper()));
    		BlockTagGen blockGen = new BlockTagGen(generator, event.getExistingFileHelper());
    		generator.addProvider(true, blockGen);
    		generator.addProvider(true, new ItemTagGen(generator, blockGen, event.getExistingFileHelper()));
    		generator.addProvider(true, new FluidTagGen(generator, event.getExistingFileHelper()));
    	}
    	if (event.includeClient()) {
    		generator.addProvider(true, new DSCSoundDefinitionGen(generator, event.getExistingFileHelper()));
    		generator.addProvider(true, new VehicleClientPresetGenerator(generator));
    	}
    }
    
}
