package com.onewhohears.dscombat.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.onewhohears.dscombat.client.renderer.PartsDebugRenderer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

/**
 * Command to toggle parts debug visualization
 * Shows part positions, icons, names, and coordinates in 3D space on vehicles
 */
public class PartsDebugCommand {
    
    public PartsDebugCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("partsdebug")
            .requires(stack -> stack.hasPermission(2))
            .then(Commands.argument("enabled", BoolArgumentType.bool())
                .executes(context -> {
                    boolean enabled = BoolArgumentType.getBool(context, "enabled");
                    PartsDebugRenderer.setDebugEnabled(enabled);
                    context.getSource().sendSuccess(
                        () -> Component.literal("Parts debug mode " + (enabled ? "enabled" : "disabled")),
                        true
                    );
                    return 1;
                })
            )
            .executes(context -> {
                // Toggle without argument
                boolean newState = !PartsDebugRenderer.isDebugEnabled();
                PartsDebugRenderer.setDebugEnabled(newState);
                context.getSource().sendSuccess(
                    () -> Component.literal("Parts debug mode " + (newState ? "enabled" : "disabled")),
                    true
                );
                return 1;
            })
        );
    }
}
