package com.onewhohears.dscombat.client.entityscreen;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.logging.LogUtils;
import com.onewhohears.dscombat.data.aircraft.EntityScreenData;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public class VehicleScreenMapReader {
	
	private static final Logger LOGGER = LogUtils.getLogger();
	
	public static List<VehicleScreenUV> readVehicleScreenUVs(ResourceLocation screenMap) {
		Minecraft m = Minecraft.getInstance();
		List<VehicleScreenUV> list = new ArrayList<VehicleScreenUV>();
		NativeImage image;
		try {
			InputStream stream = m.getResourceManager().getResource(screenMap).get().open();
			image = NativeImage.read(stream);
		} catch (IOException e) {
			LOGGER.debug(screenMap.toString()+" not loaded. This vehicle won't have any screens.");
			return list;
		}
		for (int x = 0; x < image.getWidth(); ++x) for (int y = 0; y < image.getHeight(); ++y) {
			
		}
		return list;
	}
	
	public static List<EntityScreenData> getVehicleScreenPos(String modelId, List<VehicleScreenUV> vehicleScreenUVs) {
		
	}
	
	public static class VehicleScreenUV {
		public final int screenType;
		public final float u0, u1, v0, v1;
		public VehicleScreenUV(int screenType, float u0, float u1, float v0, float v1) {
			this.screenType = screenType;
			this.u0 = u0;
			this.u1 = u1;
			this.v0 = v0;
			this.v1 = v1;
		}
		public static VehicleScreenUV getByColor(int color, float u0, float u1, float v0, float v1) {
			
			return new VehicleScreenUV(0, u0, u1, v0, v1);
		}
	}
	
}
