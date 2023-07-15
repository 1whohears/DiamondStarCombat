package com.onewhohears.dscombat.init;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.onewhohears.dscombat.DSCombatMod;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModVillagers {
	
	public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, DSCombatMod.MODID);
	public static final DeferredRegister<VillagerProfession> VILLAGER_PROS = DeferredRegister.create(ForgeRegistries.PROFESSIONS, DSCombatMod.MODID);
	
	public static final RegistryObject<PoiType> WEAPON_POI = POI_TYPES.register("weapon_workbench_poi", 
			() -> new PoiType("weapon_workbench_poi", 
					getBlockStates(ModBlocks.WEAPONS_BLOCK.get()), 
					1, 1));
	
	public static final RegistryObject<PoiType> AIRCRAFT_POI = POI_TYPES.register("aircraft_workbench_poi", 
			() -> new PoiType("aircraft_workbench_poi", 
					getBlockStates(ModBlocks.AIRCRAFT_BLOCK.get()), 
					1, 1));
	
	public static final RegistryObject<VillagerProfession> WEAPONS_ENGINEER = VILLAGER_PROS.register("weapons_engineer", 
			() -> new VillagerProfession("weapons_engineer", 
					WEAPON_POI.get(), 
					ImmutableSet.of(), 
					ImmutableSet.of(),
					SoundEvents.VILLAGER_WORK_WEAPONSMITH));
	
	public static final RegistryObject<VillagerProfession> AIRCRAFT_ENGINEER = VILLAGER_PROS.register("aircraft_engineer", 
			() -> new VillagerProfession("aircraft_engineer", 
					AIRCRAFT_POI.get(), 
					ImmutableSet.of(), 
					ImmutableSet.of(),
					SoundEvents.VILLAGER_WORK_WEAPONSMITH));
	
    public static void registerPOIs() {
        try {
        	Method registerBlockStates = ObfuscationReflectionHelper.findMethod(PoiType.class,
                    "registerBlockStates", PoiType.class);
        	registerBlockStates.invoke(null, WEAPON_POI.get());
        	registerBlockStates.invoke(null, WEAPON_POI.get());
        } catch (InvocationTargetException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
    }
	
	public static void register(IEventBus eventBus) {
		POI_TYPES.register(eventBus);
		VILLAGER_PROS.register(eventBus);
	}
	
	private static Set<BlockState> getBlockStates(Block block) {
		return ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates());
	}
	
}
