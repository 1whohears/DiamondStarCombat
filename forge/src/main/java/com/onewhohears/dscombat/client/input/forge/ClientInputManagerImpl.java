package com.onewhohears.dscombat.client.input.forge;

import com.onewhohears.dscombat.client.input.DSCClientInputs;
import net.minecraft.client.Minecraft;

public class ClientInputManagerImpl {

    public static double getMouseYVelocity(Minecraft mc) {
        return DSCClientInputs.getAndResetMouseDeltaY();
    }

    public static double getMouseXVelocity(Minecraft mc) {
        return DSCClientInputs.getAndResetMouseDeltaX();
    }

}
