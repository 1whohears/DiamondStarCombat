package com.onewhohears.dscombat.client.entityscreen.instance;

import com.mojang.blaze3d.vertex.PoseStack;
import com.onewhohears.onewholibs.util.math.Mat4f;
import com.onewhohears.onewholibs.util.math.Vec3f;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import static com.onewhohears.dscombat.client.util.UtilRender.drawTextureCentered;

public class AltimeterScreenInstance extends SpinMeterScreenInstance {

	public static final ResourceLocation BACKGROUND = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/entity_screen/altimeter_bg.png");
    public static final ResourceLocation SPIN = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/entity_screen/point_needle.png");

	float altitude = 0;

	public AltimeterScreenInstance(int id) {
		super(id, BACKGROUND, SPIN);
	}

	@Override
	public void draw(Entity entity, PoseStack poseStack, MultiBufferSource buffer,
					 float partialTicks, int packedLight, float worldWidth, float worldHeight) {
		super.draw(entity, poseStack, buffer, partialTicks, packedLight, worldWidth, worldHeight);
		poseStack.pushPose();
		poseStack.mulPose(Vec3f.ZP.rotationDegrees(altitude*0.36f).convert());
		poseStack.scale(0.75f, 0.75f, 1);
		Mat4f matrix4f = Mat4f.from(poseStack.last().pose());
		drawTextureCentered(spinRenderType, matrix4f, buffer, packedLight, -0.002f);
		poseStack.popPose();
	}

	@Override
	protected float getAngleDegrees(Entity entity) {
		altitude = (float) ((EntityVehicle)entity).getAltitude();
		return altitude * 3.6f;
	}

}
