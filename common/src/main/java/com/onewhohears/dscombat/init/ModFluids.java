package com.onewhohears.dscombat.init;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.fluid.CustomFluid;
import dev.architectury.core.fluid.ArchitecturyFluidAttributes;
import dev.architectury.core.fluid.SimpleArchitecturyFluidAttributes;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;

public class ModFluids {

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(DSCombatMod.MODID, Registry.FLUID_REGISTRY);

    public static RegistrySupplier<FlowingFluid> getOilFluidSource() {
        return OIL_FLUID_SOURCE;
    }
    public static RegistrySupplier<FlowingFluid> getOilFluidFlowing() {
        return OIL_FLUID_FLOWING;
    }

    public static final ArchitecturyFluidAttributes OIL_ATTRIBUTES = SimpleArchitecturyFluidAttributes.ofSupplier(
            ModFluids::getOilFluidFlowing, ModFluids::getOilFluidSource)
            .blockSupplier(() -> ModBlocks.OIL_LIQUID_BLOCK)
            .bucketItemSupplier(() -> ModItems.OIL_BUCKET)
            .density(1000).viscosity(5000).luminosity(1).dropOff(2)
            .color(0xFF000000).slopeFindDistance(2).temperature(10).tickDelay(30)
            .explosionResistance(100f)
            .convertToSource(false).lighterThanAir(false)
            .flowingTexture(ResourceLocation.tryBuild(DSCombatMod.MODID, "block/oil_block"))
            .overlayTexture(ResourceLocation.tryBuild(DSCombatMod.MODID, "misc/in_oil_oil"))
            .sourceTexture(ResourceLocation.tryBuild(DSCombatMod.MODID, "block/oil_block"));

    public static final RegistrySupplier<FlowingFluid> OIL_FLUID_SOURCE = FLUIDS.register("oil_fluid_source",
            () -> new CustomFluid.Source(OIL_ATTRIBUTES));
    public static final RegistrySupplier<FlowingFluid> OIL_FLUID_FLOWING = FLUIDS.register("oil_fluid_flowing",
            () -> new CustomFluid.Flowing(OIL_ATTRIBUTES));

    public static void register() {
        FLUIDS.register();
    }

	/* FIXME how to register the rest of these properties?
	public static final RegistryObject<FluidType> OIL_FLUID_TYPE = registerType("oil",
			FluidType.Properties.create().lightLevel(1).density(1000).viscosity(1000).fallDistanceModifier(0.1f)
				.canConvertToSource(false).canDrown(true).canExtinguish(false).supportsBoating(true)
				.canHydrate(false).canPushEntity(true).canSwim(false).motionScale(0.001)
				.sound(SoundAction.get("drink"), SoundEvents.WITCH_DRINK), 
			new ResourceLocation("block/oil_block"), 
			new ResourceLocation("misc/in_oil_oil"), 
			new ResourceLocation("block/oil_block"), 
			0xFF000000, 10, 10, 10, 0.2f, 1.2f);
	
	public static final ArchitecturyFluidAttributes OIL_FLUID_PROPERTIES = new SimpleArchitecturyFluidAttributes(
			OIL_FLUID_TYPE, OIL_FLUID_SOURCE, OIL_FLUID_FLOWING)
			.slopeFindDistance(2).levelDecreasePerBlock(2).tickRate(15)
			.block(ModBlocks.OIL_LIQUID_BLOCK).bucket(ModItems.OIL_BUCKET);
	
	public static RegistryObject<FluidType> registerType(String name, FluidType.Properties properties,
			ResourceLocation stillTexture, ResourceLocation overlayTexture, ResourceLocation flowingTexture, 
			int tintColor, float fogR, float fogG, float fogB, float fogStart, float fogEnd) {
		return FLUID_TYPES.register(name, () -> new CustomFluidType(properties, 
				stillTexture, overlayTexture, flowingTexture, tintColor, 
				new Vec3f(fogR/255f, fogG/255f, fogB/255f), fogStart, fogEnd));
	}*/
	
}
