package com.example.vehiclenbt;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class VehicleNBTPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("VehicleNBT Plugin enabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("vehiclenbt")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("This command can only be used by players!");
                return true;
            }

            Player player = (Player) sender;
            
            // Find nearest vehicle entity
            Entity nearestVehicle = null;
            double minDistance = Double.MAX_VALUE;
            
            for (Entity entity : player.getWorld().getEntities()) {
                if (entity.getType().toString().contains("VEHICLE") || 
                    entity.getType().toString().contains("dscombat")) {
                    double distance = entity.getLocation().distance(player.getLocation());
                    if (distance < minDistance && distance < 50) {
                        minDistance = distance;
                        nearestVehicle = entity;
                    }
                }
            }

            if (nearestVehicle == null) {
                player.sendMessage("§cNo vehicle found nearby!");
                return true;
            }

            try {
                // Get NMS entity and NBT
                net.minecraft.world.entity.Entity nmsEntity = ((CraftEntity) nearestVehicle).getHandle();
                net.minecraft.nbt.CompoundTag nbt = new net.minecraft.nbt.CompoundTag();
                nmsEntity.saveWithoutId(nbt);
                
                String nbtString = nbt.getAsString();
                
                // Print to console
                getLogger().info("=== Vehicle NBT ===");
                getLogger().info(nbtString);
                getLogger().info("=== End NBT ===");
                
                // Save to file
                String fileName = "vehicle_nbt_" + nearestVehicle.getEntityId() + ".txt";
                try (FileWriter writer = new FileWriter(getDataFolder().getParentFile().getParentFile() + "/" + fileName)) {
                    writer.write(nbtString);
                }
                
                player.sendMessage("§aVehicle NBT saved to: " + fileName);
                player.sendMessage("§aCheck console for full output!");
                
            } catch (Exception e) {
                player.sendMessage("§cError: " + e.getMessage());
                e.printStackTrace();
            }

            return true;
        }
        return false;
    }
}
