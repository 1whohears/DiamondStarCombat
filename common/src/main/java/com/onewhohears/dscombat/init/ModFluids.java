package com.onewhohears.dscombat.init;

import com.onewhohears.dscombat.DSCombatMod;
import dev.architectury.core.fluid.ArchitecturyFlowingFluid;
import dev.architectury.core.fluid.ArchitecturyFluidAttributes;
import dev.architectury.core.fluid.SimpleArchitecturyFluidAttributes;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public class ModFluids {

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(DSCombatMod.MODID, Registry.FLUID_REGISTRY);

    public static RegistrySupplier<Fluid> getOilFluidSource() {
        return OIL_FLUID_SOURCE;
    }
    public static RegistrySupplier<FlowingFluid> getOilFluidFlowing() {
        return OIL_FLUID_FLOWING;
    }

    public static final ArchitecturyFluidAttributes OIL_ATTRIBUTES = SimpleArchitecturyFluidAttributes.ofSupplier(
            ModFluids::getOilFluidSource, ModFluids::getOilFluidFlowing)
            .blockSupplier(() -> ModBlocks.OIL_LIQUID_BLOCK)
            .bucketItemSupplier(() -> ModItems.OIL_BUCKET)
            .density(1000).viscosity(1000).luminosity(1).dropOff(1)
            .color(0xFF000000).slopeFindDistance(4).temperature(10).tickDelay(15)
            .convertToSource(false).lighterThanAir(false)
            .flowingTexture(ResourceLocation.tryBuild(DSCombatMod.MODID, "block/oil_block"))
            .overlayTexture(ResourceLocation.tryBuild(DSCombatMod.MODID, "misc/in_oil_oil"))
            .sourceTexture(ResourceLocation.tryBuild(DSCombatMod.MODID, "block/oil_block"));

    public static final RegistrySupplier<Fluid> OIL_FLUID_SOURCE = FLUIDS.register("oil_fluid_source",
            () -> new OilFluid.Source(OIL_ATTRIBUTES));
    public static final RegistrySupplier<FlowingFluid> OIL_FLUID_FLOWING = FLUIDS.register("oil_fluid_flowing",
            () -> new ArchitecturyFlowingFluid.Flowing(OIL_ATTRIBUTES));

    public static void register() {
        FLUIDS.register();
    }

    /**
     * for reasons that I can't explain, arch didn't add a LEVEL property to ArchitecturyFlowingFluid.Source???
     * so the game crashes on startup constantly without this child class.
     * this took forever to figure out and I could not find anyone else having the same issue.
     * this is why I put off working on the fabric port for so long...
     */
    public static class OilFluid {
        public static class Source extends ArchitecturyFlowingFluid.Source {
            public Source(ArchitecturyFluidAttributes attributes) {
                super(attributes);
            }
            @Override
            protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
                super.createFluidStateDefinition(builder);
                builder.add(LEVEL);
            }
        }
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
