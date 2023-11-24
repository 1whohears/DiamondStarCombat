package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.client.renderer.texture.EntityScreenInstance;
import com.onewhohears.dscombat.client.renderer.texture.EntityScreenTypes;

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
	 * @param texture texture to be rendered
	 * @param topLeft in world top left corner position of the texture
	 * @param topRight in world top right corner position of the texture
	 * @param bottomLeft in world bottom left corner position of the texture
	 */ 
	default void renderScreen(T entity, int screenId, int screenType, PoseStack poseStack, MultiBufferSource buffer,
                              float partialTicks, int packedLight, Vec3 pos, float width, float height, float xRot, float yRot, float zRot) {
		if (screenId == -1) return;
		poseStack.pushPose();
		
		poseStack.translate(pos.x, pos.y, pos.z);
		poseStack.mulPose(Vector3f.XP.rotationDegrees(xRot));
		poseStack.mulPose(Vector3f.YP.rotationDegrees(yRot));
		poseStack.mulPose(Vector3f.ZP.rotationDegrees(zRot+180));
		poseStack.translate(-0.5*width, -0.5*height, 0);
		poseStack.scale(width, height, 1);
		
		getOrCreateEntityScreenById(screenId, screenType).draw(entity, poseStack, buffer, partialTicks, packedLight, 
				width, height);
		
		poseStack.popPose();
	}
	
}
