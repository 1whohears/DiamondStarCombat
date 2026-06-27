package com.onewhohears.dscombat.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.onewhohears.dscombat.client.renderer.TrackTextureRenderer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

/**
 * Command to toggle track texture debug visualization
 */
public class TrackDebugCommand {
    
    public TrackDebugCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("trackdebug")
            .requires(stack -> stack.hasPermission(2))
            .then(Commands.argument("enabled", BoolArgumentType.bool())
                .executes(context -> {
                    boolean enabled = BoolArgumentType.getBool(context, "enabled");
                    TrackTextureRenderer.setDebugMode(enabled);
                    context.getSource().sendSuccess(
                        () -> Component.literal("Track debug mode " + (enabled ? "enabled" : "disabled")),
                        true
                    );
                    return 1;
                })
            )
            .executes(context -> {
                // Toggle without argument
                boolean newState = !TrackTextureRenderer.isDebugMode();
                TrackTextureRenderer.setDebugMode(newState);
                context.getSource().sendSuccess(
                    () -> Component.literal("Track debug mode " + (newState ? "enabled" : "disabled")),
                    true
                );
                return 1;
            })
        );
    }
}
