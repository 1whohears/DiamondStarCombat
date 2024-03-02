package com.onewhohears.dscombat.client.entityscreen;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.logging.LogUtils;
import com.onewhohears.dscombat.client.model.obj.ObjEntityModels;
import com.onewhohears.dscombat.data.aircraft.EntityScreenData;
import com.onewhohears.dscombat.entity.aircraft.EntityVehicle;
import com.onewhohears.dscombat.mixin.CompositeRenderableAccess;
import com.onewhohears.dscombat.mixin.CompositeRenderableComponentAccess;
import com.onewhohears.dscombat.mixin.CompositeRenderableMeshAccess;
import com.onewhohears.dscombat.util.math.UtilGeometry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.renderable.CompositeRenderable;

public class VehicleScreenMapReader {
	
	private static final Logger LOGGER = LogUtils.getLogger();
	/**
	 * CLIENT ONLY!
	 * ONLY WORKS FOR OBJ MODELS!
	 */
	public static List<EntityScreenData> generateScreens(EntityVehicle entity, String modelId) {
		List<VehicleScreenUV> vehicleScreenUVs = readVehicleScreenUVs(entity.textureManager.getScreenMap());
		return getVehicleScreenPos(modelId, vehicleScreenUVs);
	}
	
	private static List<VehicleScreenUV> readVehicleScreenUVs(ResourceLocation screenMap) {
		Minecraft m = Minecraft.getInstance();
		List<VehicleScreenUV> list = new ArrayList<VehicleScreenUV>();
		NativeImage image;
		try {
			InputStream stream = m.getResourceManager().getResource(screenMap).get().open();
			image = NativeImage.read(stream);
		} catch (NoSuchElementException e) {
			LOGGER.debug(screenMap.toString()+" does not exist. This vehicle won't have any screens.");
			return list;
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
	
	private static List<EntityScreenData> getVehicleScreenPos(String modelId, List<VehicleScreenUV> vehicleScreenUVs) {
		List<EntityScreenData> list = new ArrayList<>();
		if (!ObjEntityModels.get().hasModel(modelId)) {
			LOGGER.warn("Obj Model "+modelId+" does not exist! No screens created.");
			return list;
		}
		CompositeRenderable model = ObjEntityModels.get().getBakedModel(modelId);
		// plan is to search all the meshes in the model to find the quad with this uv cord.
		// then use the quad vertex positions to find the in game position and direction of the screen.
		// going to need to add a lot of access transforms
		for (VehicleScreenUV screenUV : vehicleScreenUVs) 
			findScreenPos(screenUV, model, list);
			/*for (CompositeRenderableComponentAccess component : ((CompositeRenderableAccess)model).getComponents()) 
				searchComponent(component, list, screenUV);*/
		return list;
	}
	
	private static void findScreenPos(VehicleScreenUV screenUV, CompositeRenderable model, List<EntityScreenData> list) {
		List<BakedQuad> middleQuads = findQuadsWithUV(model, screenUV.um, screenUV.vm);
		List<BakedQuad> topLeftQuads = findQuadsWithUV(model, screenUV.u0, screenUV.v0);
		List<BakedQuad> topRightQuads = findQuadsWithUV(model, screenUV.u1, screenUV.v0);
		List<BakedQuad> bottomLeftQuads = findQuadsWithUV(model, screenUV.u0, screenUV.v1);
		List<BakedQuad> bottomRightQuads = findQuadsWithUV(model, screenUV.u1, screenUV.v1);
		addScreenFromQuads(middleQuads, list, screenUV, screenUV.um, screenUV.vm, 0.05f);
		addScreenFromQuads(topLeftQuads, list, screenUV, screenUV.u0, screenUV.v0, 0.02f);
		addScreenFromQuads(topRightQuads, list, screenUV, screenUV.u1, screenUV.v0, 0.02f);
		addScreenFromQuads(bottomLeftQuads, list, screenUV, screenUV.u0, screenUV.v1, 0.02f);
		addScreenFromQuads(bottomRightQuads, list, screenUV, screenUV.u1, screenUV.v1, 0.02f);
	}
	
	private static void addScreenFromQuads(List<BakedQuad> quads, List<EntityScreenData> list, VehicleScreenUV screenUV, float u, float v, float size) {
		for (BakedQuad quad : quads) {
			Vec2[] uvs = getUVs(quad);
			Vec3[] corners = getQuadPositions(quad);
			Vec3 pos = getWorldPos(u, v, uvs, corners);
			list.add(new EntityScreenData(screenUV.screenType, 
					pos.add(0, -2, 1.999), 
					size, size, 0, 0, 0));
		}
	}
	
	private static List<BakedQuad> findQuadsWithUV(CompositeRenderable model, float u, float v) {
		List<BakedQuad> quads = new ArrayList<>();
		for (CompositeRenderableComponentAccess component : ((CompositeRenderableAccess)model).getComponents()) 
			searchComponent(component, quads, u, v);
		return quads;
	}
	
	private static void searchComponent(CompositeRenderableComponentAccess component, List<BakedQuad> quads, float u, float v) {
		for (CompositeRenderableMeshAccess mesh : ((CompositeRenderableComponentAccess)component).getMeshes()) 
			searchMesh(mesh, quads, u, v);
		for (CompositeRenderableComponentAccess childComponent : ((CompositeRenderableComponentAccess)component).getChildren()) 
			searchComponent(childComponent, quads, u, v);
	}
	
	private static void searchMesh(CompositeRenderableMeshAccess mesh, List<BakedQuad> quads, float u, float v) {
		for (BakedQuad quad : ((CompositeRenderableMeshAccess)mesh).getQuads()) searchQuad(quad, quads, u, v);
	}
	
	private static void searchQuad(BakedQuad quad, List<BakedQuad> quads, float u, float v) {
		if (hasUV(getUVs(quad), u, v)) quads.add(quad);
	}
	
	/*private static void searchComponent(CompositeRenderableComponentAccess component, List<EntityScreenData> list, VehicleScreenUV screenUV) {
		for (CompositeRenderableMeshAccess mesh : ((CompositeRenderableComponentAccess)component).getMeshes()) 
			searchMesh(mesh, list, screenUV);
		for (CompositeRenderableComponentAccess childComponent : ((CompositeRenderableComponentAccess)component).getChildren()) 
			searchComponent(childComponent, list, screenUV);
	}
	
	private static void searchMesh(CompositeRenderableMeshAccess mesh, List<EntityScreenData> list, VehicleScreenUV screenUV) {
		for (BakedQuad quad : ((CompositeRenderableMeshAccess)mesh).getQuads()) searchQuad(quad, list, screenUV);
	}
	
	private static void searchQuad(BakedQuad quad, List<EntityScreenData> list, VehicleScreenUV screenUV) {
		Vec2[] uvs = getUVs(quad);
		if (!hasUV(uvs, screenUV.um, screenUV.vm)) return;
		LOGGER.debug("Found screen pos in model! Adding screen type "+screenUV.screenType);
		//System.out.println("Quad vertexes "+Arrays.toString(quad.getVertices()));
		System.out.println("screenUV.um = "+screenUV.um+" screenUV.vm = "+screenUV.vm);
		System.out.println("Quad uvs: "+UtilParse.vec2ToString(uvs));
		Vec3[] pos = getQuadPositions(quad);
		System.out.println("Quad pos: "+Arrays.toString(pos));
		Vec3 middlePos = getWorldPos(screenUV.um, screenUV.vm, uvs, pos);
		System.out.println("Screen pos = "+middlePos);
		//Vec3 topLeftPos = getWorldPos(screenUV.u0, screenUV.v0, uvs, pos);
		//Vec3 topRightPos = getWorldPos(screenUV.u1, screenUV.v0, uvs, pos);
		//Vec3 bottomLeftPos = getWorldPos(screenUV.u0, screenUV.v1, uvs, pos);
		//float width = (float)topRightPos.subtract(topLeftPos).length();
		//float height = (float)bottomLeftPos.subtract(topLeftPos).length(); 
		//System.out.println("width = "+width+" height = "+height);
		float width = 0.05f, height = 0.05f;
		list.add(new EntityScreenData(screenUV.screenType, 
				middlePos.add(0, -2, 1.999), 
				width, height, 0, 0, 0));
		for (int i = 0; i < pos.length; ++i) list.add(new EntityScreenData(screenUV.screenType, 
				pos[i].add(0, -2, 1.999), 
				0.02f, 0.02f, 0, 0, 0));
	}*/
	
	private static Vec3 getWorldPos(float u, float v, Vec2[] uvs, Vec3[] corners) {
		Vec2 uvg = new Vec2(u, v);
		// T2-T1, T3-T1, P-T1
		Vec2 uv1Duv0 = uvs[1].add(uvs[0].negated());
		Vec2 uv2Duv0 = uvs[2].add(uvs[0].negated());
		Vec2 uvgDuv0 = uvg.add(uvs[0].negated());
		// parametric coordinates [s,t]
	    // s = (P-T1)x(T2-T1) / (T3-T1)x(T2-T1)
	    // t = (P-T1)x(T3-T1) / (T2-T1)x(T3-T1)
		float s = UtilGeometry.crossProduct(uvgDuv0, uv1Duv0) / UtilGeometry.crossProduct(uv2Duv0, uv1Duv0);
		float t = UtilGeometry.crossProduct(uvgDuv0, uv2Duv0) / UtilGeometry.crossProduct(uv1Duv0, uv2Duv0);
		// XYZ = V1 + s(V2-V1) + t(V3-V1)
		return corners[0].add(corners[1].subtract(corners[0]).scale(s)).add(corners[2].subtract(corners[0]).scale(t));
	}
	
	private static Vec3[] getQuadPositions(BakedQuad quad) {
		Vec3[] pos = new Vec3[3];
		for (int i = 0; i < 3; ++i) 
			pos[i] = new Vec3((double)Float.intBitsToFloat(quad.getVertices()[i*8]), 
					(double)Float.intBitsToFloat(quad.getVertices()[i*8+1]), 
					(double)Float.intBitsToFloat(quad.getVertices()[i*8+2]));
		return pos;
	}
	
	private static Vec2[] getUVs(BakedQuad quad) {
		Vec2[] uvs = new Vec2[3];
		for (int i = 0; i < 3; ++i) 
			uvs[i] = new Vec2(Float.intBitsToFloat(quad.getVertices()[i*8+4]), 
					Float.intBitsToFloat(quad.getVertices()[i*8+5]));
		return uvs;
	}
	
	private static boolean hasUV(Vec2[] uvs, float u, float v) {
		Vec2 uv = new Vec2(u, v);
		return UtilGeometry.isIn2DTriangle(uv, uvs);
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
