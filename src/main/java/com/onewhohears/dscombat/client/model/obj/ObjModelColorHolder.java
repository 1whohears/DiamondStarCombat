package com.onewhohears.dscombat.client.model.obj;

import java.awt.Color;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ObjModelColorHolder {
	
	public static float RED = 1f, GREEN = 1f, BLUE = 1f;
	
	public static void resetColor() {
		RED = GREEN = BLUE = 1f;
	}
	
	public static void setColor(float red, float green, float blue) {
		RED = red; GREEN = green; BLUE = blue;
	}
	
	public static void setColor(Color color) {
		setColor((float)color.getRed()/255f, (float)color.getGreen()/255f, (float)color.getBlue()/255f);
	}
	
}
