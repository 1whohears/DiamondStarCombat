package com.onewhohears.dscombat;

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

import dev.architectury.platform.Platform;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

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
public class DSCombatMod {
	
	public static final String MODID = "dscombat";
	
	public static boolean minigamesLoaded = false;
	public static boolean distantPlayersLoaded = false;

    public static void init() {
        minigamesLoaded = Platform.isModLoaded("minigames");
        distantPlayersLoaded = Platform.isModLoaded("distant_players");

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
    }

    public static void clientInit() {

    }

    public DSCombatMod() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.clientSpec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.commonSpec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.serverSpec);
    }
    
}
