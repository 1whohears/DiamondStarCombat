package com.onewhohears.dscombat.forge;

import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.DependencySafety;
import com.onewhohears.dscombat.client.event.ClientEventHandlers;
import com.onewhohears.dscombat.client.event.DSCEntityRenderers;
import com.onewhohears.dscombat.data.forge.*;
import com.onewhohears.dscombat.data.parts.PartPresetGenerator;
import com.onewhohears.dscombat.data.parts.client.PartClientPresetGenerator;
import com.onewhohears.dscombat.data.radar.RadarPresetGenerator;
import com.onewhohears.dscombat.data.sound.forge.DSCSoundDefinitionGenImpl;
import com.onewhohears.dscombat.data.vehicle.VehiclePresetGenerator;
import com.onewhohears.dscombat.data.vehicle.client.VehicleClientPresetGenerator;
import com.onewhohears.dscombat.data.weapon.WeaponPresetGenerator;
import com.onewhohears.dscombat.data.weapon.client.WeaponClientPresetGenerator;
import com.onewhohears.dscombat.init.DataSerializers;
import com.onewhohears.dscombat.init.forge.DataSerializersImpl;
import com.onewhohears.dscombat.init.forge.ModArgumentTypesForge;
import dev.architectury.platform.Platform;
import dev.architectury.platform.forge.EventBuses;
import dev.architectury.utils.Env;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DSCombatMod.MODID)
public class DSCombatModForge {

    public DSCombatModForge(FMLJavaModLoadingContext loadingContext) {
        IEventBus modEventBus = loadingContext.getModEventBus();
        EventBuses.registerModEventBus(DSCombatMod.MODID, modEventBus);

        loadingContext.registerConfig(ModConfig.Type.CLIENT, Config.clientSpec);
        loadingContext.registerConfig(ModConfig.Type.COMMON, Config.commonSpec);
        loadingContext.registerConfig(ModConfig.Type.SERVER, Config.serverSpec);

        modEventBus.addListener(this::onGatherData);

        DSCombatMod.init();
        if (Platform.getEnvironment() == Env.CLIENT && !DatagenModLoader.isRunningDataGen()) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> DSCombatMod::clientInit);
        }

        DataSerializersImpl.register(modEventBus);
        ModArgumentTypesForge.register(modEventBus);
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
            generator.addProvider(true, new DSCSoundDefinitionGenImpl(generator, event.getExistingFileHelper()));
            generator.addProvider(true, new VehicleClientPresetGenerator(generator));
            generator.addProvider(true, new PartClientPresetGenerator(generator));
            generator.addProvider(true, new WeaponClientPresetGenerator(generator));
        }
    }

    @Mod.EventBusSubscriber(modid = DSCombatMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            DSCEntityRenderers.register();
        }
        @SubscribeEvent
        public static void registerParticleFactory(RegisterParticleProvidersEvent event) {
            ClientEventHandlers.registerParticleProvider();
        }
    }

}
