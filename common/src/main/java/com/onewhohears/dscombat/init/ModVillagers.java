package com.onewhohears.dscombat.init;

import java.util.Set;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableSet;
import com.onewhohears.dscombat.DSCombatMod;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ModVillagers {
	
	public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(
            DSCombatMod.MODID, Registry.POINT_OF_INTEREST_TYPE_REGISTRY);
	public static final DeferredRegister<VillagerProfession> VILLAGER_PROS = DeferredRegister.create(
            DSCombatMod.MODID, Registry.VILLAGER_PROFESSION_REGISTRY);
	
	public static final RegistrySupplier<PoiType> WEAPON_POI = POI_TYPES.register("weapon_workbench_poi",
			() -> new PoiType(getBlockStates(ModBlocks.WEAPONS_BLOCK.get()), 1, 1));
	
	public static final RegistrySupplier<PoiType> AIRCRAFT_POI = POI_TYPES.register("aircraft_workbench_poi", 
			() -> new PoiType(getBlockStates(ModBlocks.AIRCRAFT_BLOCK.get()), 1, 1));

    public static RegistrySupplier<PoiType> registerPoi(String name, Supplier<Block> block, int workers, int range) {
        RegistrySupplier<PoiType> type = POI_TYPES.register(name, () -> new PoiType(getBlockStates(block.get()), workers, range));
        platformRegisterPoiType(name, block, workers, range);
        return type;
    }

    @ExpectPlatform
    public static void platformRegisterPoiType(String name, Supplier<Block> block, int workers, int range) {
        throw new AssertionError();
    }

	public static final RegistrySupplier<VillagerProfession> WEAPONS_ENGINEER = VILLAGER_PROS.register("weapons_engineer", 
			() -> new VillagerProfession("weapons_engineer", 
					site -> site.is(WEAPON_POI.getId()),
					site -> site.is(WEAPON_POI.getId()),
					ImmutableSet.of(), ImmutableSet.of(),
					SoundEvents.VILLAGER_WORK_WEAPONSMITH));
	
	public static final RegistrySupplier<VillagerProfession> AIRCRAFT_ENGINEER = VILLAGER_PROS.register("aircraft_engineer", 
			() -> new VillagerProfession("aircraft_engineer", 
					(site) -> site.is(AIRCRAFT_POI.getId()),
					(site) -> site.is(AIRCRAFT_POI.getId()),
					ImmutableSet.of(), ImmutableSet.of(),
					SoundEvents.VILLAGER_WORK_WEAPONSMITH));
	
	public static void register() {
		POI_TYPES.register();
		VILLAGER_PROS.register();
	}
	
	private static Set<BlockState> getBlockStates(Block block) {
		return ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates());
	}
	
}
