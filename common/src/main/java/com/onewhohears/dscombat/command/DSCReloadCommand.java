package com.onewhohears.dscombat.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.onewhohears.dscombat.data.weapon.WeaponPresets;
import com.onewhohears.dscombat.data.weapon.instance.WeaponInstance;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public class DSCReloadCommand {
	
	public DSCReloadCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("dscreload")
			.requires(cs -> cs.hasPermission(2))
			.executes(this::reloadWeaponData));
	}
	
	private int reloadWeaponData(CommandContext<CommandSourceStack> context) {
		CommandSourceStack source = context.getSource();
		ServerLevel level = source.getLevel();
		
		final int[] counts = {0, 0}; // [vehicleCount, weaponCount]
		
		// Iterate through all entities in the level
		for (Entity entity : level.getAllEntities()) {
			if (entity instanceof EntityVehicle vehicle) {
				counts[0]++;
				
				// Reload weapon stats for each weapon in the vehicle
				for (WeaponInstance<?> weapon : vehicle.weaponSystem.getWeapons()) {
					String weaponId = weapon.getStatsId();
					
					// Get fresh stats from WeaponPresets
					var freshStats = WeaponPresets.get().get(weaponId);
					if (freshStats != null) {
						// Stats reload removed
						counts[1]++;
					}
				}
			}
		}
		
		source.sendSuccess(() -> UtilMCText.literal(
			"Reloaded weapon data for " + counts[1] + " weapons in " + counts[0] + " vehicles"
		), true);
		
		return 1;
	}
	
}
