package com.onewhohears.dscombat.client.texture;

import java.util.List;

import com.onewhohears.dscombat.data.aircraft.EntityScreenData;

import net.minecraft.resources.ResourceLocation;

public class VehicleScreenMapReader {
	
	public static List<VehicleScreenUV> readVehicleScreenUVs(ResourceLocation screenMap) {
		
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
