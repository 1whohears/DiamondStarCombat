package com.onewhohears.dscombat.client.input.fabric;

import net.minecraft.client.Minecraft;

public class ClientInputManagerImpl {

    public static double getMouseYVelocity(Minecraft mc) {
        return mc.mouseHandler.accumulatedDY;
    }

    public static double getMouseXVelocity(Minecraft mc) {
        return mc.mouseHandler.accumulatedDX;
    }

}
