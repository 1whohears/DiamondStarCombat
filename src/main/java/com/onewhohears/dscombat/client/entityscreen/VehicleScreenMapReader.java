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
import com.onewhohears.dscombat.util.math.UtilAngles;
import com.onewhohears.dscombat.util.math.UtilGeometry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.renderable.CompositeRenderable;

public class VehicleScreenMapReader {
	
	private static final Logger LOGGER = LogUtils.getLogger();
	/**
	 * CLIENT ONLY! ONLY WORKS FOR OBJ MODELS!
	 */
	public static List<EntityScreenData> generateScreens(EntityVehicle entity, String modelId, Vec3 modelOffset) {
		List<VehicleScreenUV> vehicleScreenUVs = readVehicleScreenUVs(entity.textureManager.getScreenMap());
		return getVehicleScreenPos(modelId, vehicleScreenUVs, modelOffset);
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
			++u1; ++v1;
			VehicleScreenUV vsuv = new VehicleScreenUV(screenType, 
					(float)u0 / (float)image.getWidth(), (float)u1 / (float)image.getWidth(), 
					(float)v0 / (float)image.getHeight(), (float)v1 / (float)image.getHeight());
			list.add(vsuv);
			x = u1;
			LOGGER.debug("Found screen "+vsuv.toString()+" in "+screenMap.toString());
		}
		return list;
	}
	
	private static List<EntityScreenData> getVehicleScreenPos(String modelId, List<VehicleScreenUV> vehicleScreenUVs, Vec3 offset) {
		List<EntityScreenData> list = new ArrayList<>();
		if (!ObjEntityModels.get().hasModel(modelId)) {
			LOGGER.warn("Obj Model "+modelId+" does not exist! No screens created.");
			return list;
		}
		CompositeRenderable model = ObjEntityModels.get().getBakedModel(modelId);
		for (VehicleScreenUV screenUV : vehicleScreenUVs) findScreenPos(screenUV, model, list, offset);
		return list;
	}
	
	private static void findScreenPos(VehicleScreenUV screenUV, CompositeRenderable model, List<EntityScreenData> list, Vec3 offset) {
		List<BakedQuad> middleQuads = findQuadsWithUV(model, screenUV.um, screenUV.vm);
		for (BakedQuad quad : middleQuads) addScreenFromQuad(quad, list, screenUV, model, offset);
	}
	
	private static void addScreenFromQuad(BakedQuad quad, List<EntityScreenData> list, VehicleScreenUV screenUV, CompositeRenderable model, Vec3 offset) {
		Vec2[] uvs = getUVs(quad);
		Vec3[] corners = getQuadPositions(quad);
		Vec3 pos = getWorldPos(screenUV.um, screenUV.vm, uvs, corners);
		float width = 0.05f, height = 0.05f;
		float[] wh = getWidthHeight(uvs, corners, pos, screenUV);
		if (wh == null) wh = getWidthHeightOtherQuad(pos, screenUV, model);
		if (wh != null) {
			width = wh[0]; 
			height = wh[1];
		}
		Vec3 normal = corners[2].subtract(corners[0]).cross(corners[1].subtract(corners[0])).normalize();
		pos = pos.add(offset).subtract(normal.scale(0.001));
		LOGGER.debug("Found screen pos in model! Adding screen type "+screenUV.screenType+" at "+pos);
		list.add(new EntityScreenData(screenUV.screenType, pos, width, height, 
				UtilAngles.getPitch(normal), UtilAngles.getYaw(normal), 0));
	}
	
	private static float[] getWidthHeightOtherQuad(Vec3 pos, VehicleScreenUV screenUV, CompositeRenderable model) {
		List<BakedQuad> cornerQuads = findQuadsWithUV(model, screenUV.u0, screenUV.v0);
		for (BakedQuad quad : cornerQuads) {
			Vec2[] uvs = getUVs(quad);
			Vec3[] corners = getQuadPositions(quad);
			Vec3 cornerPos = getWorldPos(screenUV.u0, screenUV.v0, uvs, corners);
			if (cornerPos.distanceTo(pos) > 1) continue;
			Vec2 uvm = new Vec2(screenUV.um, screenUV.vm);
			Vec2 uvc = new Vec2(screenUV.u0, screenUV.v0);
			return getWidthHeight(pos, cornerPos, uvm, uvc);
		}
		return null;
	}
	
	private static float[] getWidthHeight(Vec2[] uvs, Vec3[] corners, Vec3 pos, VehicleScreenUV screenUV) {
		float[] wh = null;
		Vec2 uvm = new Vec2(screenUV.um, screenUV.vm);
		wh = getWidthHeight(uvs, corners, pos, uvm, new Vec2(screenUV.u0, screenUV.v0));
		if (wh == null) wh = getWidthHeight(uvs, corners, pos, uvm, new Vec2(screenUV.u1, screenUV.v0));
		if (wh == null) wh = getWidthHeight(uvs, corners, pos, uvm, new Vec2(screenUV.u0, screenUV.v1));
		if (wh == null) wh = getWidthHeight(uvs, corners, pos, uvm, new Vec2(screenUV.u1, screenUV.v1));
		return wh;
	}
	
	private static float[] getWidthHeight(Vec2[] uvs, Vec3[] corners, Vec3 pos, Vec2 uvm, Vec2 uvc) {
		if (!hasUV(uvs, uvc.x, uvc.y)) return null;
		Vec3 cornerPos = getWorldPos(uvc.x, uvc.y, uvs, corners);
		return getWidthHeight(pos, cornerPos, uvm, uvc);
	}
	
	private static float[] getWidthHeight(Vec3 middlePos, Vec3 cornerPos, Vec2 uvm, Vec2 uvc) {
		Vec3 posDiff = middlePos.subtract(cornerPos);
		Vec2 uvDiff = uvm.add(uvc.negated());
		float posLength = (float) posDiff.length();
		float uvLength = uvDiff.length();
		float scale = posLength / uvLength;
		float width = Mth.abs(uvDiff.x) * 2 * scale;
		float height = Mth.abs(uvDiff.y) * 2 * scale;
		return new float[] {width, height};
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
	
	private static Vec3 getWorldPos(float u, float v, Vec2[] uvs, Vec3[] corners) {
		Vec2 uvg = new Vec2(u, v);
		if (isUVsTri(uvs)) return getWorldPosFromTri(uvg, uvs, corners);
		Vec2[] uvt1 = new Vec2[] {uvs[0], uvs[1], uvs[2]};
		if (UtilGeometry.isIn2DTriangle(uvg, uvt1)) return getWorldPosFromTri(uvg, uvt1, 
				new Vec3[] {corners[0], corners[1], corners[2]});
		Vec2[] uvt2 = new Vec2[] {uvs[0], uvs[2], uvs[3]};
		return getWorldPosFromTri(uvg, uvt2, 
				new Vec3[] {corners[0], corners[2], corners[3]});
	}
	
	private static Vec3 getWorldPosFromTri(Vec2 uvg, Vec2[] uvs, Vec3[] corners) {
		// http://mr-pc.org/t/cisc3620/readings/03b-color.html
		Vec2 Q = uvg, A = uvs[0], B = uvs[1], C = uvs[2];
		Vec2 P = UtilGeometry.intersect(C, Q, B, A);
		Vec2 v = B.add(A.negated());
		float t = 0, s = 0;
		if (v.x != 0) t = P.add(A.negated()).x / v.x;
		else if (v.y != 0) t = P.add(A.negated()).y / v.y;
		Vec2 u = P.add(C.negated());
		if (u.x != 0) s = Q.add(C.negated()).x / u.x;
		else if (u.y != 0) s = Q.add(C.negated()).y / u.y;
		return corners[0].scale((1-t)*s).add(corners[1].scale(t*s)).add(corners[2].scale(1-s));
	}
	
	private static Vec3[] getQuadPositions(BakedQuad quad) {
		Vec3[] pos = new Vec3[4];
		for (int i = 0; i < 4; ++i) 
			pos[i] = new Vec3((double)Float.intBitsToFloat(quad.getVertices()[i*8]), 
					(double)Float.intBitsToFloat(quad.getVertices()[i*8+1]), 
					(double)Float.intBitsToFloat(quad.getVertices()[i*8+2]));
		return pos;
	}
	
	private static Vec2[] getUVs(BakedQuad quad) {
		Vec2[] uvs = new Vec2[4];
		for (int i = 0; i < 4; ++i) 
			uvs[i] = new Vec2(Float.intBitsToFloat(quad.getVertices()[i*8+4]), 
					Float.intBitsToFloat(quad.getVertices()[i*8+5]));
		return uvs;
	}
	
	private static boolean hasUV(Vec2[] uvs, float u, float v) {
		Vec2 uv = new Vec2(u, v);
		if (isUVsTri(uvs)) UtilGeometry.isIn2DTriangle(uv, uvs);
		return UtilGeometry.isIn2DQuad(uv, uvs);
	}
	
	private static boolean isUVsTri(Vec2[] uvs) {
		for (int i = 0; i < uvs.length; ++i) {
			for (int j = 0; j < uvs.length; ++j) {
				if (j == i) continue;
				if (uvs[i].x == uvs[j].x && uvs[i].y == uvs[j].y) 
					return true;
			}
		}
		return false;
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
