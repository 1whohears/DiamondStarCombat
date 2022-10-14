package com.onewhohears.dscombat.util;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;

public class UtilMCText {
	
	public static MutableComponent simpleText(String text) {
		return MutableComponent.create(new TranslatableContents(text));
	}
	
}
