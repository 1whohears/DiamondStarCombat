package com.onewhohears.dscombat.init;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.common.container.AircraftMenuContainer;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModContainers {
	
	public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, DSCombatMod.MODID);
	
	public static void register(IEventBus eventBus) {
		CONTAINERS.register(eventBus);
    }
	
	public static final RegistryObject<MenuType<AircraftMenuContainer>> PLANE_MENU = 
			register("plane_menu", AircraftMenuContainer::new);
	
	private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> register(String id, MenuType.MenuSupplier<T> factory){
        return CONTAINERS.register(id, () -> new MenuType<>(factory));
    }
	
}
