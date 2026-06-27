package com.onewhohears.dscombat;

import com.onewhohears.dscombat.client.event.ClientEventHandlers;
import com.onewhohears.dscombat.client.input.DSCKeys;
import com.onewhohears.dscombat.client.model.obj.HardCodedModelAnims;
import com.onewhohears.dscombat.common.event.CommonEventHandlers;
import com.onewhohears.dscombat.data.parts.client.PartAssets;
import com.onewhohears.dscombat.data.sound.VehiclePassengerSoundPacks;
import com.onewhohears.dscombat.data.vehicle.client.VehicleClientPresets;
import com.onewhohears.dscombat.data.weapon.client.WeaponAssets;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.init.*;

import dev.architectury.platform.Platform;
import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.server.packs.PackType;

/**
 * This is the main class of the Diamond Star Combat mod.
 * Some events are registered here but most are subscribed via Annotation. 
 * Here are some of the event classes and other possibly relevant Entry Points:
 * {@link com.onewhohears.dscombat.client.event.ClientEventHandlers}
 * {@link com.onewhohears.dscombat.client.event.ClientCameraEventHandlers}
 * {@link com.onewhohears.dscombat.client.event.ClientInputEventHandlers}
 * {@link com.onewhohears.dscombat.client.event.ClientRenderEventHandlers}
 * {@link com.onewhohears.dscombat.common.event.CommonEventHandlers}
 * {@link EntityVehicle}
 * 
 * @author 1whohears
 */
public class DSCombatMod {
	
	public static final String MODID = "dscombat";
	
	public static boolean minigamesLoaded = false;
	public static boolean distantPlayersLoaded = false;
	public static boolean tacViewLoaded = false;

    public static void init() {
        minigamesLoaded = Platform.isModLoaded("minigames");
        distantPlayersLoaded = Platform.isModLoaded("distant_players");
        tacViewLoaded = Platform.isModLoaded("tacview");

        CommonEventHandlers.init();
        ModCMTabs.register();
        ModFluids.register();
        ModBlocks.register();
        ModContainers.register();
        ModEntities.register();
        ModItems.register();
        ModRecipes.register();
        ModSounds.register();
        ModBlockEntities.register();
        ModVillagers.register();
        ModParticles.register();
        ModTags.init();
        DataSerializers.init();
    }

    public static void clientInit() {
        ClientEventHandlers.init();
        HardCodedModelAnims.reload();
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, VehicleClientPresets.get());
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, PartAssets.get());
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, WeaponAssets.get());
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, VehiclePassengerSoundPacks.get());
        DSCKeys.init();
    }
    
}
