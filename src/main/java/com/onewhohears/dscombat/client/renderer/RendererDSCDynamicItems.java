package com.onewhohears.dscombat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.data.vehicle.VehiclePresets;
import com.onewhohears.dscombat.data.vehicle.client.VehicleClientPresets;
import com.onewhohears.dscombat.data.vehicle.client.VehicleClientStats;
import com.onewhohears.dscombat.data.vehicle.stats.VehicleStats;
import com.onewhohears.dscombat.item.ItemVehicle;
import com.onewhohears.onewholibs.client.model.obj.ObjEntityModels;
import com.onewhohears.onewholibs.mixin.ObjModelAccess;
import com.onewhohears.onewholibs.util.math.UtilGeometry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.obj.ObjModel;
import net.minecraftforge.client.model.renderable.CompositeRenderable;

import java.util.HashMap;
import java.util.Map;

public class RendererDSCDynamicItems extends BlockEntityWithoutLevelRenderer {

    private static RendererDSCDynamicItems instance;

    public static RendererDSCDynamicItems get() {
        if (instance == null) {
            Minecraft m = Minecraft.getInstance();
            instance = new RendererDSCDynamicItems(m.getBlockEntityRenderDispatcher(), m.getEntityModels());
        }
        return instance;
    }

    private final Map<String, ItemObjModelData> models = new HashMap<>();

    protected RendererDSCDynamicItems(BlockEntityRenderDispatcher blockEntityRenderDispatcher,
                                      EntityModelSet entityModelSet) {
        super(blockEntityRenderDispatcher, entityModelSet);
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        models.clear();
    }

    @Override
    public void renderByItem(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack poseStack,
                             MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Item item = stack.getItem();
        if (item instanceof ItemVehicle itemVehicle) {
            String preset = itemVehicle.getPresetName(stack);
            ItemObjModelData model = models.get(preset);
            if (model == null) {
                VehicleStats vs = VehiclePresets.get().get(preset);
                if (vs == null) vs = VehiclePresets.get().get(itemVehicle.getDefaultPreset());
                String assetId = vs.getAssetId();
                VehicleClientStats vcs = VehicleClientPresets.get().get(assetId);
                String modelId = vcs.getModelId();
                // the model id needs to be set in client stats json thing
                CompositeRenderable composite = ObjEntityModels.get().getBakedModel(modelId);
                ObjEntityModels.ModelOverrides override = ObjEntityModels.get().getModelOverride(modelId);
                ObjModel obj = ObjEntityModels.get().getUnbakedModel(modelId);
                model = new ItemObjModelData(composite, override, obj);
                models.put(preset, model);
            }
            model.render(transformType, poseStack, buffer, packedLight, packedOverlay);
        }
    }

    public static class ItemObjModelData {
        public static final float SIZE_SCALE_FACTOR = 1.25f;
        public final CompositeRenderable compositeRenderable;
        public final ObjEntityModels.ModelOverrides modelOverrides;
        public final Vec3 center;
        public final float scale;
        public ItemObjModelData(CompositeRenderable compositeRenderable, ObjEntityModels.ModelOverrides modelOverrides, ObjModel obj) {
            this.compositeRenderable = compositeRenderable;
            this.modelOverrides = modelOverrides;
            Vec3[] sizeCenter = UtilGeometry.getSizeCenter(((ObjModelAccess)obj).getPositions());
            Vec3 size = sizeCenter[0];
            float maxSize = (float)Math.max(size.z(), size.x());
            center = sizeCenter[1];
            scale = SIZE_SCALE_FACTOR / maxSize;
        }
        public void render(ItemTransforms.TransformType transformType, PoseStack poseStack,
                           MultiBufferSource buffer, int packedLight, int packedOverlay) {
            poseStack.pushPose();
            modelOverrides.applyNoTranslate(poseStack);
            if (transformType == ItemTransforms.TransformType.GUI) {
                poseStack.translate(0.5, 0.5, 0.5);
                poseStack.mulPose(Vector3f.XP.rotationDegrees(30));
                poseStack.mulPose(Vector3f.YP.rotationDegrees(45));
            } else if (transformType == ItemTransforms.TransformType.FIXED) {
                poseStack.translate(0.5, 0.5, 0.35);
                poseStack.mulPose(Vector3f.XP.rotationDegrees(-90));
            } else if (transformType == ItemTransforms.TransformType.GROUND) {
                poseStack.translate(0.5, 0.5, 0.5);
            } else if (transformType == ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND) {
                poseStack.translate(0.5, 0.5, 0.3);
                poseStack.mulPose(Vector3f.XP.rotationDegrees(30));
                poseStack.mulPose(Vector3f.YP.rotationDegrees(225));
            } else if (transformType == ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND) {
                poseStack.translate(0.5, 0.5, 0.3);
                poseStack.mulPose(Vector3f.XP.rotationDegrees(30));
                poseStack.mulPose(Vector3f.YP.rotationDegrees(135));
            } else if (transformType == ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND) {
                poseStack.translate(0.5, 0.5, 0.5);
                poseStack.mulPose(Vector3f.XP.rotationDegrees(90));
                poseStack.mulPose(Vector3f.YP.rotationDegrees(225));
            } else if (transformType == ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND) {
                poseStack.translate(0.5, 0.5, 0.5);
                poseStack.mulPose(Vector3f.XP.rotationDegrees(90));
                poseStack.mulPose(Vector3f.YP.rotationDegrees(135));
            } else if (transformType == ItemTransforms.TransformType.HEAD) {
                poseStack.translate(0.5, 1.2, 0.5);
                poseStack.scale(2f, 2f, 2f);
                poseStack.mulPose(Vector3f.YP.rotationDegrees(180));
            } else if (transformType == ItemTransforms.TransformType.NONE) {
                poseStack.translate(0.5, 0.5, 0.5);
            }
            poseStack.scale(scale, scale, scale);
            poseStack.translate((float) -center.x(), (float) -center.y(), (float) -center.z());
            compositeRenderable.render(poseStack, buffer, RenderType::entitySolid, packedLight, packedOverlay,
                    0, CompositeRenderable.Transforms.EMPTY);
            poseStack.popPose();
        }
    }
}
