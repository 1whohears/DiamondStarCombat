package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.onewholibs.util.math.QuaternionF;
import com.onewhohears.onewholibs.util.math.Vec3f;
import com.onewhohears.dscombat.client.entityscreen.EntityScreenTypes;
import com.onewhohears.dscombat.client.entityscreen.instance.EntityScreenInstance;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public interface EntityScreenRenderer<T extends Entity> {
	
	Int2ObjectMap<EntityScreenInstance> screenInstances = new Int2ObjectOpenHashMap<>();
	/**
	 * CLIENT ONLY
	 */
	static void clearCache() {
		for(EntityScreenInstance screen : screenInstances.values()) screen.close();
		screenInstances.clear();
	}
	/**
	 * CLIENT ONLY
	 */
	static EntityScreenInstance getOrCreateEntityScreenById(int screenId, int screenType) {
		if (screenInstances.containsKey(screenId)) return screenInstances.get(screenId);
		EntityScreenInstance screen = EntityScreenTypes.screenTypes.get(screenType).create(screenId);
		screenInstances.put(screenId, screen);
		return screen;
	}
	/**
	 * CLIENT ONLY
	 */
	static int getFreeScreenId(int min) {
		int id = min;
		IntSet keys = screenInstances.keySet();
		while (true) if (!keys.contains(++id)) return id;
	}
	
	boolean shouldRenderScreens(T entity);
	
	/**
	 * @param entity entity the texture is rendered onto
	 */ 
	default void renderScreen(T entity, int screenId, int screenType, PoseStack poseStack, MultiBufferSource buffer,
                              float partialTicks, int packedLight, Vec3 pos, float width, float height, float xRot, float yRot, float zRot) {
		if (screenId == -1) return;
		poseStack.pushPose();
		
		poseStack.translate(pos.x, pos.y, pos.z);
        QuaternionF q = Vec3f.XP.rotationDegrees(xRot);
        q.mul(Vec3f.YP.rotationDegrees(yRot));
        q.mul(Vec3f.ZP.rotationDegrees(zRot+180));
		poseStack.mulPose(q.convert());
		poseStack.scale(width, height, 1);
		
		getOrCreateEntityScreenById(screenId, screenType).draw(entity, poseStack, buffer, partialTicks, packedLight, 
				width, height);
		
		poseStack.popPose();
	}
	
}
