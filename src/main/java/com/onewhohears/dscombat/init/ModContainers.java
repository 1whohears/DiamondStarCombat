package com.onewhohears.dscombat.init;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.block.entity.VehicleBlock;
import com.onewhohears.dscombat.block.entity.WeaponsBlockEntity;
import com.onewhohears.dscombat.common.container.menu.StorageBoxContainerMenu;
import com.onewhohears.dscombat.common.container.menu.VehicleBlockContainerMenu;
import com.onewhohears.dscombat.common.container.menu.VehiclePartsMenu;
import com.onewhohears.dscombat.common.container.menu.WeaponsBlockContainerMenu;

import com.onewhohears.dscombat.data.parts.instance.StorageInstance;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModContainers {
	
	public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, DSCombatMod.MODID);
	
	public static void register(IEventBus eventBus) {
		CONTAINERS.register(eventBus);
    }
	
	public static final RegistryObject<MenuType<VehiclePartsMenu>> VEHICLE_PARTS_MENU =
			register("vehicle_parts_menu", VehiclePartsMenu::new);
	public static final RegistryObject<MenuType<WeaponsBlockContainerMenu>> WEAPONS_BLOCK_MENU = 
			register("weapons_block_menu", (IContainerFactory<WeaponsBlockContainerMenu>) (windowId, playerInv, data) -> {
				WeaponsBlockEntity weaponsBlock = (WeaponsBlockEntity)playerInv.player.level.getBlockEntity(data.readBlockPos());
				return new WeaponsBlockContainerMenu(windowId, playerInv, weaponsBlock);
			});
	public static final RegistryObject<MenuType<VehicleBlockContainerMenu>> AIRCRAFT_BLOCK_MENU =
			register("aircraft_block_menu", (IContainerFactory<VehicleBlockContainerMenu>) (windowId, playerInv, data) -> {
				VehicleBlock aircraftBlock = (VehicleBlock)playerInv.player.level.getBlockEntity(data.readBlockPos());
				return new VehicleBlockContainerMenu(windowId, playerInv, aircraftBlock);
			});
	public static final IContainerFactory<ChestMenu> VEHICLE_STORAGE_MENU_FACTORY = (windowId, playerInv, data) -> {
		int storageId = data.readInt();
		if (playerInv.player.getRootVehicle() instanceof EntityVehicle vehicle) {
			StorageInstance<?> storageInstance = vehicle.partsManager.getStorageData(storageId);
			if (storageInstance != null)
				return storageInstance.createMenu(windowId, playerInv);
		}
		return StorageInstance.getEmptyStorageMenu(windowId, playerInv);
	};
	public static final RegistryObject<MenuType<ChestMenu>> VEHICLE_STORAGE_MENU_9x0 =
			register("vehicle_storage_menu_9x0", VEHICLE_STORAGE_MENU_FACTORY);
	public static final RegistryObject<MenuType<ChestMenu>> VEHICLE_STORAGE_MENU_9x1 =
			register("vehicle_storage_menu_9x1", VEHICLE_STORAGE_MENU_FACTORY);
	public static final RegistryObject<MenuType<ChestMenu>> VEHICLE_STORAGE_MENU_9x2 =
			register("vehicle_storage_menu_9x2", VEHICLE_STORAGE_MENU_FACTORY);
	public static final RegistryObject<MenuType<ChestMenu>> VEHICLE_STORAGE_MENU_9x3 =
			register("vehicle_storage_menu_9x3", VEHICLE_STORAGE_MENU_FACTORY);
	public static final RegistryObject<MenuType<ChestMenu>> VEHICLE_STORAGE_MENU_9x4 =
			register("vehicle_storage_menu_9x4", VEHICLE_STORAGE_MENU_FACTORY);
	public static final RegistryObject<MenuType<ChestMenu>> VEHICLE_STORAGE_MENU_9x5 =
			register("vehicle_storage_menu_9x5", VEHICLE_STORAGE_MENU_FACTORY);
	public static final RegistryObject<MenuType<ChestMenu>> VEHICLE_STORAGE_MENU_9x6 =
			register("vehicle_storage_menu_9x6", VEHICLE_STORAGE_MENU_FACTORY);
	
	private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> register(String id, MenuType.MenuSupplier<T> factory){
        return CONTAINERS.register(id, () -> new MenuType<>(factory));
    }
	
}
