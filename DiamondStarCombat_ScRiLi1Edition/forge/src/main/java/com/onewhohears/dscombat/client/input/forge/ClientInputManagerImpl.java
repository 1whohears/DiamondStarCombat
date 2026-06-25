package com.onewhohears.dscombat.client.input.forge;

import net.minecraft.client.Minecraft;

public class ClientInputManagerImpl {

    public static double getMouseYVelocity(Minecraft mc) {
        return mc.mouseHandler.getYVelocity();
    }

    public static double getMouseXVelocity(Minecraft mc) {
        return mc.mouseHandler.getXVelocity();
    }

}
