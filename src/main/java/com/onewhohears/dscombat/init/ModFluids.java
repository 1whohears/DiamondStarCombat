package com.onewhohears.dscombat.init;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.fluid.CustomFluidType;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.SoundAction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.joml.Vector3f;

public class ModFluids {
	
	public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, DSCombatMod.MODID);
	public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, DSCombatMod.MODID);
	
	public static final RegistryObject<FluidType> OIL_FLUID_TYPE = registerType("oil", 
			FluidType.Properties.create().lightLevel(1).density(1000).viscosity(1000).fallDistanceModifier(0.1f)
				.canConvertToSource(false).canDrown(true).canExtinguish(false).supportsBoating(true)
				.canHydrate(false).canPushEntity(true).canSwim(false).motionScale(0.001)
				.sound(SoundAction.get("drink"), SoundEvents.WITCH_DRINK), 
			new ResourceLocation("block/oil_block"), 
			new ResourceLocation("misc/in_oil_oil"), 
			new ResourceLocation("block/oil_block"), 
			0xFF000000, 10, 10, 10, 0.2f, 1.2f);
	
	public static final RegistryObject<FlowingFluid> OIL_FLUID_SOURCE = FLUIDS.register("oil_fluid_source", 
			() -> new ForgeFlowingFluid.Source(ModFluids.OIL_FLUID_PROPERTIES));
	public static final RegistryObject<FlowingFluid> OIL_FLUID_FLOWING = FLUIDS.register("oil_fluid_flowing", 
			() -> new ForgeFlowingFluid.Flowing(ModFluids.OIL_FLUID_PROPERTIES));
	
	public static final ForgeFlowingFluid.Properties OIL_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
			OIL_FLUID_TYPE, OIL_FLUID_SOURCE, OIL_FLUID_FLOWING)
			.slopeFindDistance(2).levelDecreasePerBlock(2).tickRate(15)
			.block(ModBlocks.OIL_LIQUID_BLOCK).bucket(ModItems.OIL_BUCKET);
	
	public static void register(IEventBus eventBus) {
		FLUIDS.register(eventBus);
		FLUID_TYPES.register(eventBus);
    }
	
	public static RegistryObject<FluidType> registerType(String name, FluidType.Properties properties,
			ResourceLocation stillTexture, ResourceLocation overlayTexture, ResourceLocation flowingTexture, 
			int tintColor, float fogR, float fogG, float fogB, float fogStart, float fogEnd) {
		return FLUID_TYPES.register(name, () -> new CustomFluidType(properties, 
				stillTexture, overlayTexture, flowingTexture, tintColor, 
				new Vector3f(fogR/255f, fogG/255f, fogB/255f), fogStart, fogEnd));
	}
	
}
