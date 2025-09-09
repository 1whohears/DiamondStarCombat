package com.onewhohears.dscombat.forge;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.DependencySafety;
import com.onewhohears.dscombat.data.parts.PartPresetGenerator;
import com.onewhohears.dscombat.data.parts.client.PartClientPresetGenerator;
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
import com.onewhohears.dscombat.data.weapon.client.WeaponClientPresetGenerator;
import dev.architectury.platform.Platform;
import dev.architectury.platform.forge.EventBuses;
import dev.architectury.utils.Env;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DSCombatMod.MODID)
public class DSCombatModForge {

    public DSCombatModForge() {
        @SuppressWarnings("removal")
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(DSCombatMod.MODID, modEventBus);

        modEventBus.addListener(this::onGatherData);

        DSCombatMod.init();
        if (Platform.getEnvironment() == Env.CLIENT && !DatagenModLoader.isRunningDataGen()) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> DSCombatMod::clientInit);
        }
    }

    public DSCombatModForge(FMLJavaModLoadingContext loadingContext) {
        IEventBus modEventBus = loadingContext.getModEventBus();
        EventBuses.registerModEventBus(DSCombatMod.MODID, modEventBus);

        modEventBus.addListener(this::onGatherData);

        DSCombatMod.init();
        if (Platform.getEnvironment() == Env.CLIENT && !DatagenModLoader.isRunningDataGen()) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> DSCombatMod::clientInit);
        }
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
            generator.addProvider(true, new PartClientPresetGenerator(generator));
            generator.addProvider(true, new WeaponClientPresetGenerator(generator));
        }
    }

}
