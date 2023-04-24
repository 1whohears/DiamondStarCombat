package com.onewhohears.dscombat.init;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.block.entity.AircraftBlockEntity;
import com.onewhohears.dscombat.block.entity.WeaponsBlockEntity;
import com.onewhohears.dscombat.common.container.AircraftBlockMenuContainer;
import com.onewhohears.dscombat.common.container.AircraftMenuContainer;
import com.onewhohears.dscombat.common.container.WeaponsBlockMenuContainer;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModContainers {
	
	public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, DSCombatMod.MODID);
	
	public static void register(IEventBus eventBus) {
		CONTAINERS.register(eventBus);
    }
	
	public static final RegistryObject<MenuType<AircraftMenuContainer>> PLANE_MENU = 
			register("plane_menu", AircraftMenuContainer::new);
	public static final RegistryObject<MenuType<WeaponsBlockMenuContainer>> WEAPONS_BLOCK_MENU = 
			register("weapons_block_menu", (IContainerFactory<WeaponsBlockMenuContainer>) (windowId, playerInv, data) -> {
				WeaponsBlockEntity weaponsBlock = (WeaponsBlockEntity)playerInv.player.level.getBlockEntity(data.readBlockPos());
				return new WeaponsBlockMenuContainer(windowId, playerInv, weaponsBlock);
			});
	public static final RegistryObject<MenuType<AircraftBlockMenuContainer>> AIRCRAFT_BLOCK_MENU = 
			register("aircraft_block_menu", (IContainerFactory<AircraftBlockMenuContainer>) (windowId, playerInv, data) -> {
				AircraftBlockEntity aircraftBlock = (AircraftBlockEntity)playerInv.player.level.getBlockEntity(data.readBlockPos());
				return new AircraftBlockMenuContainer(windowId, playerInv, aircraftBlock);
			});
	
	private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> register(String id, MenuType.MenuSupplier<T> factory){
        return CONTAINERS.register(id, () -> new MenuType<>(factory));
    }
	
}
