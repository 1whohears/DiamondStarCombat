package com.onewhohears.dscombat.forge;

import com.onewhohears.dscombat.Config;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.DependencySafety;
import com.onewhohears.dscombat.client.event.ClientEventHandlers;
import com.onewhohears.dscombat.client.event.DSCEntityRenderers;
import com.onewhohears.dscombat.data.forge.*;
import com.onewhohears.dscombat.data.mine.MinePresetGenerator;
import com.onewhohears.dscombat.data.parts.PartPresetGenerator;
import com.onewhohears.dscombat.data.parts.client.PartClientPresetGenerator;
import com.onewhohears.dscombat.data.radar.RadarPresetGenerator;
import com.onewhohears.dscombat.data.sound.forge.DSCSoundDefinitionGenImpl;
import com.onewhohears.dscombat.data.vehicle.VehiclePresetGenerator;
import com.onewhohears.dscombat.data.vehicle.client.VehicleClientPresetGenerator;
import com.onewhohears.dscombat.data.weapon.WeaponPresetGenerator;
import com.onewhohears.dscombat.data.weapon.client.WeaponClientPresetGenerator;
import com.onewhohears.dscombat.init.ModCMTabs;
import com.onewhohears.dscombat.init.ModDamageTypes;
import com.onewhohears.dscombat.init.forge.DataSerializersImpl;
import com.onewhohears.dscombat.init.forge.ModArgumentTypesForge;
import com.onewhohears.dscombat.item.FillableItemCategory;
import dev.architectury.platform.Platform;
import dev.architectury.platform.forge.EventBuses;
import dev.architectury.utils.Env;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Mod(DSCombatMod.MODID)
public class DSCombatModForge {

    // MAGMA FIX: Add no-args constructor for compatibility
    public DSCombatModForge() {
        this(FMLJavaModLoadingContext.get());
    }

    public DSCombatModForge(FMLJavaModLoadingContext loadingContext) {
        IEventBus modEventBus = loadingContext.getModEventBus();
        EventBuses.registerModEventBus(DSCombatMod.MODID, modEventBus);

        // MAGMA/MOHIST FIX: Try-catch for config registration compatibility
        try {
            loadingContext.registerConfig(ModConfig.Type.CLIENT, Config.clientSpec);
            loadingContext.registerConfig(ModConfig.Type.COMMON, Config.commonSpec);
            loadingContext.registerConfig(ModConfig.Type.SERVER, Config.serverSpec);
        } catch (NoSuchMethodError e) {
            // Magma/Mohist uses different method signature - try alternative
            try {
                // Try with file name parameter (Magma variant)
                java.lang.reflect.Method method = loadingContext.getClass().getMethod(
                    "registerConfig", ModConfig.Type.class, 
                    net.minecraftforge.fml.config.IConfigSpec.class, String.class);
                method.invoke(loadingContext, ModConfig.Type.CLIENT, Config.clientSpec, "dscombat-client.toml");
                method.invoke(loadingContext, ModConfig.Type.COMMON, Config.commonSpec, "dscombat-common.toml");
                method.invoke(loadingContext, ModConfig.Type.SERVER, Config.serverSpec, "dscombat-server.toml");
            } catch (Exception ex) {
                System.err.println("Failed to register configs: " + ex.getMessage());
            }
        }

        modEventBus.addListener(this::onGatherData);
        modEventBus.addListener(this::buildCreativeModeTabs);

        DSCombatMod.init();
        if (Platform.getEnvironment() == Env.CLIENT && !DatagenModLoader.isRunningDataGen()) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> DSCombatMod::clientInit);
            DSCEntityRenderers.register();
        }

        DataSerializersImpl.register(modEventBus);
        ModArgumentTypesForge.register(modEventBus);
    }

    private void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        PackOutput output = generator.getPackOutput();
        if (event.includeServer()) {
            CompletableFuture<HolderLookup.Provider> completableFuture = event.getLookupProvider();
            generator.addProvider(true, new VehiclePresetGenerator(output));
            generator.addProvider(true, new WeaponPresetGenerator(output));
            generator.addProvider(true, new RadarPresetGenerator(output));
            generator.addProvider(true, new PartPresetGenerator(output));
            generator.addProvider(true, new MinePresetGenerator(output));
            DependencySafety.serverDataGen(output, gen -> generator.addProvider(true, gen));
            generator.addProvider(true, new DSCRecipeGenerator(output));
            generator.addProvider(true, new EntityTypeTagGen(output, completableFuture, fileHelper));
            BlockTagGen blockGen = new BlockTagGen(output, completableFuture, fileHelper);
            generator.addProvider(true, blockGen);
            generator.addProvider(true, new ItemTagGen(output, completableFuture, blockGen.contentsGetter(), fileHelper));
            generator.addProvider(true, new FluidTagGen(output, completableFuture, fileHelper));
            generator.addProvider(true, new DatapackBuiltinEntriesProvider(
                    output, completableFuture,
                    new RegistrySetBuilder().add(Registries.DAMAGE_TYPE, ModDamageTypes::bootstrap),
                    Set.of(DSCombatMod.MODID)
            ));
            generator.addProvider(true, new DamageTypeTagGen(output, completableFuture, fileHelper));
        }
        if (event.includeClient()) {
            generator.addProvider(true, new DSCSoundDefinitionGenImpl(output, fileHelper));
            generator.addProvider(true, new VehicleClientPresetGenerator(output));
            generator.addProvider(true, new PartClientPresetGenerator(output));
            generator.addProvider(true, new WeaponClientPresetGenerator(output));
        }
    }

    private void buildCreativeModeTabs(BuildCreativeModeTabContentsEvent event) {
        List<Supplier<? extends Item>> items = ModCMTabs.CREATIVE_TAB_MAP.get(event.getTabKey());
        if (items == null) return;
        items.forEach(item -> {
            if (item.get() instanceof FillableItemCategory fill) {
                event.getEntries().remove(item.get().getDefaultInstance());
                List<ItemStack> stacks = new ArrayList<>();
                fill.fillItemCategory(stacks);
                stacks.forEach(event::accept);
            } else {
                event.accept(item);
            }
        });
    }

    @Mod.EventBusSubscriber(modid = DSCombatMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void registerParticleFactory(RegisterParticleProvidersEvent event) {
            ClientEventHandlers.registerParticleProvider();
        }
    }

}
