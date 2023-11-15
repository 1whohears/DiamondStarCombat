package com.onewhohears.dscombat.client.model.obj;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ObjModelColorHolder {
	
	public static float RED, GREEN, BLUE;
	
	public static void resetColor() {
		RED = GREEN = BLUE = 1f;
	}
	
	public static void setColor(float red, float green, float blue) {
		RED = red; GREEN = green; BLUE = blue;
	}
	
}
