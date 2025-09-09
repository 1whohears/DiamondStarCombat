package com.onewhohears.dscombat.init;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.block.entity.VehicleBlock;
import com.onewhohears.dscombat.block.entity.WeaponPartsBlockEntity;
import com.onewhohears.dscombat.block.entity.WeaponsBlockEntity;
import com.onewhohears.dscombat.common.container.menu.*;

import com.onewhohears.dscombat.data.parts.instance.StorageInstance;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;

public class ModContainers {
	
	public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(
            DSCombatMod.MODID, Registry.MENU_REGISTRY);
	
	public static final RegistrySupplier<MenuType<VehiclePartsMenu>> VEHICLE_PARTS_MENU =
			register("vehicle_parts_menu", MenuRegistry.ofExtended(
                    (windowId, playerInv, data) -> new VehiclePartsMenu(windowId, playerInv)));
	public static final RegistrySupplier<MenuType<WeaponsBlockContainerMenu>> WEAPONS_BLOCK_MENU =
            register("weapons_block_menu", MenuRegistry.ofExtended((windowId, playerInv, data) -> {
				WeaponsBlockEntity weaponsBlock = (WeaponsBlockEntity)playerInv.player.level.getBlockEntity(data.readBlockPos());
				return new WeaponsBlockContainerMenu(windowId, playerInv, weaponsBlock);
			}));
	public static final RegistrySupplier<MenuType<VehicleBlockContainerMenu>> AIRCRAFT_BLOCK_MENU =
            register("aircraft_block_menu", MenuRegistry.ofExtended((windowId, playerInv, data) -> {
				VehicleBlock aircraftBlock = (VehicleBlock)playerInv.player.level.getBlockEntity(data.readBlockPos());
				return new VehicleBlockContainerMenu(windowId, playerInv, aircraftBlock);
			}));
	public static final RegistrySupplier<MenuType<WeaponPartsBlockContainerMenu>> WEAPON_PARTS_BLOCK_MENU =
            register("weapon_parts_block_menu", MenuRegistry.ofExtended((windowId, playerInv, data) -> {
				WeaponPartsBlockEntity weaponsBlock = (WeaponPartsBlockEntity)playerInv.player.level.getBlockEntity(data.readBlockPos());
				return new WeaponPartsBlockContainerMenu(windowId, playerInv, weaponsBlock);
			}));
	public static final MenuType<ChestMenu> VEHICLE_STORAGE_MENU_FACTORY = MenuRegistry.ofExtended(
            (windowId, playerInv, data) -> {
		int storageId = data.readInt();
		if (playerInv.player.getRootVehicle() instanceof EntityVehicle vehicle) {
			StorageInstance<?> storageInstance = vehicle.partsManager.getStorageData(storageId);
			if (storageInstance != null)
				return storageInstance.createMenu(windowId, playerInv);
		}
		return StorageInstance.getEmptyStorageMenu(windowId, playerInv);
	});
	public static final RegistrySupplier<MenuType<ChestMenu>> VEHICLE_STORAGE_MENU_9x0 =
            register("vehicle_storage_menu_9x0", VEHICLE_STORAGE_MENU_FACTORY);
	public static final RegistrySupplier<MenuType<ChestMenu>> VEHICLE_STORAGE_MENU_9x1 =
			register("vehicle_storage_menu_9x1", VEHICLE_STORAGE_MENU_FACTORY);
	public static final RegistrySupplier<MenuType<ChestMenu>> VEHICLE_STORAGE_MENU_9x2 =
			register("vehicle_storage_menu_9x2", VEHICLE_STORAGE_MENU_FACTORY);
	public static final RegistrySupplier<MenuType<ChestMenu>> VEHICLE_STORAGE_MENU_9x3 =
			register("vehicle_storage_menu_9x3", VEHICLE_STORAGE_MENU_FACTORY);
	public static final RegistrySupplier<MenuType<ChestMenu>> VEHICLE_STORAGE_MENU_9x4 =
			register("vehicle_storage_menu_9x4", VEHICLE_STORAGE_MENU_FACTORY);
	public static final RegistrySupplier<MenuType<ChestMenu>> VEHICLE_STORAGE_MENU_9x5 =
			register("vehicle_storage_menu_9x5", VEHICLE_STORAGE_MENU_FACTORY);
	public static final RegistrySupplier<MenuType<ChestMenu>> VEHICLE_STORAGE_MENU_9x6 =
			register("vehicle_storage_menu_9x6", VEHICLE_STORAGE_MENU_FACTORY);

    private static <T extends AbstractContainerMenu> RegistrySupplier<MenuType<T>> register(
            String id, MenuType<T> type) {
        return CONTAINERS.register(id, () -> type);
    }

    public static void register() {
        CONTAINERS.register();
    }
	
}
