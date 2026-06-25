package com.onewhohears.dscombat.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public class VehicleNbtCommand {
    
    public VehicleNbtCommand(CommandDispatcher<CommandSourceStack> d) {
        d.register(Commands.literal("vehiclenbt").requires((stack) -> stack.hasPermission(2))
            .then(Commands.argument("vehicle", EntityArgument.entities())
                .executes((context) -> printVehicleNbt(context, EntityArgument.getEntities(context, "vehicle")))
            )
        );
    }
    
    private int printVehicleNbt(CommandContext<CommandSourceStack> context, Collection<? extends Entity> entities) {
        int count = 0;
        for (Entity entity : entities) {
            if (entity instanceof EntityVehicle vehicle) {
                CompoundTag nbt = new CompoundTag();
                vehicle.saveWithoutId(nbt);
                
                // Print to console/log
                System.out.println("=== Vehicle NBT for " + vehicle.getId() + " ===");
                System.out.println(nbt.getAsString());
                System.out.println("=== End Vehicle NBT ===");
                
                // Save to file
                String fileName = "vehicle_nbt_" + vehicle.getId() + ".txt";
                String filePath = context.getSource().getServer().getServerDirectory().getAbsolutePath() + "/" + fileName;
                try (FileWriter writer = new FileWriter(filePath)) {
                    writer.write(nbt.getAsString());
                    
                    // Send message to player
                    context.getSource().sendSuccess(() -> 
                        UtilMCText.literal("Vehicle NBT saved to: " + fileName + " and printed to console"), 
                        true
                    );
                } catch (IOException e) {
                    context.getSource().sendFailure(UtilMCText.literal("Failed to save NBT: " + e.getMessage()));
                }
                
                count++;
            }
        }
        
        if (count == 0) {
            context.getSource().sendFailure(UtilMCText.literal("No vehicles found"));
        }
        
        return count;
    }
}
