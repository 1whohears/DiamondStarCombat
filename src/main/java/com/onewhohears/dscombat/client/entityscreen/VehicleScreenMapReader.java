package com.onewhohears.dscombat.client.entityscreen;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.logging.LogUtils;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.client.model.obj.ObjEntityModels;
import com.onewhohears.dscombat.data.aircraft.EntityScreenData;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.mixin.CompositeRenderableAccess;
import com.onewhohears.dscombat.mixin.CompositeRenderableComponentAccess;
import com.onewhohears.dscombat.mixin.CompositeRenderableMeshAccess;
import com.onewhohears.dscombat.mixin.ObjModelAccess;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.obj.ObjModel;
import net.minecraftforge.client.model.renderable.CompositeRenderable;

public class VehicleScreenMapReader {
	
	private static final Logger LOGGER = LogUtils.getLogger();
	/**
	 * CLIENT ONLY
	 */
	public static void testScreenUVs(EntityVehicle entity, String modelId) {
		List<VehicleScreenUV> vehicleScreenUVs = readVehicleScreenUVs(entity.textureManager.getScreenMap());
		getVehicleScreenPos(modelId, vehicleScreenUVs);
	}
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
			if (NativeImage.getA(color) == 0) continue;
			int screenType = EntityScreenTypes.getScreenTypeIdByColor(color);
			if (screenType == -1) continue;
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
		if (!ObjEntityModels.get().hasModel(modelId)) {
			LOGGER.warn("Obj Model "+modelId+" does not exist! No screens created.");
			return list;
		}
		ObjModel unbakedModel = ObjEntityModels.get().getUnbakedModel(modelId);
		CompositeRenderable model = ObjEntityModels.get().getBakedModel(modelId);
		// plan is to search all the meshes in the model to find the quad with this uv cord.
		// then use the quad vertex positions to find the in game position and direction of the screen.
		// going to need to add a lot of access transforms
		for (VehicleScreenUV screenUV : vehicleScreenUVs) 
			for (CompositeRenderableComponentAccess component : ((CompositeRenderableAccess)model).getComponents()) 
				searchComponent(component, list, screenUV, unbakedModel);
		return list;
	}
	
	private static void searchComponent(CompositeRenderableComponentAccess component, List<EntityScreenData> list, VehicleScreenUV screenUV, ObjModel unbakedModel) {
		for (CompositeRenderableMeshAccess mesh : ((CompositeRenderableComponentAccess)component).getMeshes()) 
			searchMesh(mesh, list, screenUV, unbakedModel);
		for (CompositeRenderableComponentAccess childComponent : ((CompositeRenderableComponentAccess)component).getChildren()) 
			searchComponent(childComponent, list, screenUV, unbakedModel);
	}
	
	private static void searchMesh(CompositeRenderableMeshAccess mesh, List<EntityScreenData> list, VehicleScreenUV screenUV, ObjModel unbakedModel) {
		for (BakedQuad quad : ((CompositeRenderableMeshAccess)mesh).getQuads()) searchQuad(quad, list, screenUV, unbakedModel);
	}
	
	private static void searchQuad(BakedQuad quad, List<EntityScreenData> list, VehicleScreenUV screenUV, ObjModel unbakedModel) {
		if (!hasUV(quad, screenUV.um, screenUV.vm)) return;
		LOGGER.debug("Found screen pos in model! Adding screen type "+screenUV.screenType);
		System.out.println("Quad vertexes "+Arrays.toString(quad.getVertices()));
		System.out.println("Quad Sprite "+quad.getSprite().toString());
		//Vector3f[] pos = getQuadPositions(quad, unbakedModel);
		//System.out.println("Quad pos: "+Arrays.toString(pos));
	}
	
	private static Vector3f[] getQuadPositions(BakedQuad quad, ObjModel unbakedModel) {
		Vector3f[] pos = new Vector3f[quad.getVertices().length];
		List<Vector3f> positions = ((ObjModelAccess)unbakedModel).getPositions();
		for (int i = 0; i < quad.getVertices().length; ++i) 
			pos[i] = positions.get(quad.getVertices()[i]);
		return pos;
	}
	
	private static boolean hasUV(BakedQuad quad, float u, float v) {
		return quad.getSprite().getU0() <= u && quad.getSprite().getU1() >= u 
			&& quad.getSprite().getV0() <= v && quad.getSprite().getV1() >= v; 
	}
	
	public static class VehicleScreenUV {
		public final int screenType;
		public final float u0, u1, um, v0, v1, vm;
		public VehicleScreenUV(int screenType, float u0, float u1, float v0, float v1) {
			this.screenType = screenType;
			this.u0 = u0;
			this.u1 = u1;
			this.um = (u0 + u1) / 2f;
			this.v0 = v0;
			this.v1 = v1;
			this.vm = (v0 + v1) / 2f;
		}
		@Override
		public String toString() {
			return "[VehicleScreenUV|type:"+screenType+"|u0,u1,v0,v1:"+u0+","+u1+","+v0+","+v1+"]";
		}
	}
	
}
