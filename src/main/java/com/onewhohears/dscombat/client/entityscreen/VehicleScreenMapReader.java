package com.onewhohears.dscombat.client.entityscreen;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.logging.LogUtils;
import com.onewhohears.dscombat.client.model.obj.ObjEntityModels;
import com.onewhohears.dscombat.data.aircraft.EntityScreenData;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.obj.ObjModel;
import net.minecraftforge.client.model.renderable.CompositeRenderable;

public class VehicleScreenMapReader {
	
	private static final Logger LOGGER = LogUtils.getLogger();
	/**
	 * CLIENT ONLY
	 */
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
		for (int y = 0; y < image.getHeight(); ++y) for (int x = 0; x < image.getWidth(); ++x) {
			int color = image.getPixelRGBA(x, y);
			int screenType = EntityScreenTypes.getScreenTypeIdByColor(color);
			if (color == -1) continue;
			if (x != 0 && image.getPixelRGBA(x-1, y) == color) continue;
			if (y != 0 && image.getPixelRGBA(x, y-1) == color) continue;
 			int u0 = x, u1 = x, v0 = y, v1 = y;
			for (int u = u0+1; u < image.getWidth(); ++u) {
				int c = image.getPixelRGBA(u, v0);
				if (c != color) break;
				u1 = u;
			}
			for (int v = v0+1; v < image.getHeight(); ++v) {
				int c = image.getPixelRGBA(u0, v);
				if (c != color) break;
				v1 = v;
			}
			VehicleScreenUV vsuv = new VehicleScreenUV(screenType, 
					(float)u0 / (float)image.getWidth(), (float)u1 / (float)image.getWidth(), 
					(float)v0 / (float)image.getHeight(), (float)v1 / (float)image.getHeight());
			list.add(vsuv);
			x = u1 + 1;
			LOGGER.debug("Found screen "+vsuv.toString()+" in "+screenMap.toString());
		}
		return list;
	}
	/**
	 * CLIENT ONLY!
	 * ONLY WORKS FOR OBJ MODELS!
	 */
	public static List<EntityScreenData> getVehicleScreenPos(String modelId, List<VehicleScreenUV> vehicleScreenUVs) {
		List<EntityScreenData> list = new ArrayList<>();
		ObjModel unbakedModel = ObjEntityModels.get().getUnbakedModel(modelId);
		CompositeRenderable model = ObjEntityModels.get().getBakedModel(modelId);
		for (VehicleScreenUV screenUV : vehicleScreenUVs) {
			float u = (screenUV.u0 + screenUV.u1) / 2f;
			float v = (screenUV.v0 + screenUV.v1) / 2f;
			// plan is to search all the meshes in the model to find the quad with this uv cord.
			// then use the quad vertex positions to find the in game position and direction of the screen.
			// going to need to add a lot of access transforms
			//int num = model.components.size();
		}
		return list;
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
		@Override
		public String toString() {
			return "[VehicleScreenUV|type:"+screenType+"|u0,u1,v0,v1:"+u0+","+u1+","+v0+","+v1+"]";
		}
	}
	
}
