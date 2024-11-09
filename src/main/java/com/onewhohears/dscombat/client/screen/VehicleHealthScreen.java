package com.onewhohears.dscombat.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.client.overlay.components.VehicleStatsOverlay;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.vehicle.RotableHitbox;
import com.onewhohears.onewholibs.util.UtilMCText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class VehicleHealthScreen extends VehicleSubScreen {

    public static final ResourceLocation BG_TEXTURE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/background/vehicle_health_screen.png");
    public static final ResourceLocation FUEL_GAUGE = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/fuel_guage.png");
    public static final ResourceLocation FUEL_GAUGE_ARROW = new ResourceLocation(DSCombatMod.MODID,
            "textures/ui/fuel_guage_arrow.png");
    public static final int FUEL_GAUGE_HEIGHT = 40, FUEL_GAUGE_WIDTH = 60;
    public static final int FUEL_ARROW_HEIGHT = 7, FUEL_ARROW_WIDTH = 24;

    private static final int imageWidth = 240, imageHeight = 180;
    private static final int textureSize = 256;

    private int maxHitboxNameWidth;
    private MutableComponent[] hitboxNames;

    protected VehicleHealthScreen() {
        super("screen.dscombat.vehicle_health_screen",
                BG_TEXTURE, imageWidth, imageHeight, textureSize, textureSize);
        left_padding = 6;
        right_padding = 6;
    }

    @Override
    protected void init() {
        super.init();
        EntityVehicle vehicle = getVehicle();
        hitboxNames = new MutableComponent[vehicle.getHitboxes().size()];
        maxHitboxNameWidth = 0;
        for (int i = 0; i < vehicle.getHitboxes().size(); ++i) {
            hitboxNames[i] = UtilMCText.translatable(vehicle.getHitboxes().get(i).getHitboxName())
                    .setStyle(Style.EMPTY.withColor(0x00AA00));
            int width = getMinecraft().font.width(hitboxNames[i]);
            if (width > maxHitboxNameWidth) maxHitboxNameWidth = width;
        }
        maxHitboxNameWidth += 10;
    }

    @Override
    public void renderBackground(@NotNull PoseStack poseStack) {
        super.renderBackground(poseStack);
        EntityVehicle vehicle = getVehicle();
        // VEHICLE BASE HEALTH / ARMOR
        getMinecraft().font.draw(poseStack, UtilMCText.translatable("ui.dscombat.root_health")
                        .append(":"+(int)vehicle.getHealth()+"/"+(int)vehicle.getMaxHealth()),
                guiX+left_padding, guiY+top_padding+40,
                getHealthColor(vehicle.getHealth(), vehicle.getMaxHealth()));
        getMinecraft().font.draw(poseStack, UtilMCText.translatable("ui.dscombat.root_armor")
                        .append(":"+(int)vehicle.getArmor()+"/"+(int)vehicle.getMaxTotalArmor()),
                guiX+left_padding+imageWidth/2f, guiY+top_padding+40,
                getHealthColor(vehicle.getArmor(), vehicle.getMaxTotalArmor()));
        // VEHICLE HITBOX HEALTH / ARMOR
        if (hitboxNames != null && hitboxNames.length > 0) {
            getMinecraft().font.draw(poseStack, UtilMCText.translatable("ui.dscombat.hitbox_health_armor"),
                    guiX + left_padding, guiY + top_padding + 54, 0x0000AA);
            float hitbox_scale = 2f / 3f;
            poseStack.pushPose();
            poseStack.scale(hitbox_scale, hitbox_scale, 1);
            int startX = (int) ((guiX + left_padding) / hitbox_scale);
            int startY = (int) ((guiY + top_padding + 66) / hitbox_scale);
            for (int i = 0; i < vehicle.getHitboxes().size(); ++i) {
                RotableHitbox hitbox = vehicle.getHitboxes().get(i);
                float health = hitbox.getHealth(), max_health = hitbox.getMaxHealth();
                Style healthStyle = Style.EMPTY.withColor(getHealthColor(health, max_health));
                float armor = hitbox.getArmor(), max_armor = hitbox.getMaxArmor();
                Style armorStyle = Style.EMPTY.withColor(getHealthColor(armor, max_armor));
                getMinecraft().font.draw(poseStack, hitboxNames[i], startX, startY + i * 10, 0xFFFFFF);
                if (i == 0 && vehicle.getStats().rootHitboxNoCollide) continue;
                getMinecraft().font.draw(poseStack, UtilMCText.literal("H: "+(int)health+"/"+(int)max_health)
                        .setStyle(healthStyle), startX+maxHitboxNameWidth, startY+i*10, 0xFFFFFF);
                getMinecraft().font.draw(poseStack, UtilMCText.literal("A: "+(int)armor+"/"+(int)max_armor)
                        .setStyle(armorStyle), startX+maxHitboxNameWidth+70, startY+i*10, 0xFFFFFF);
            }
            poseStack.popPose();
        }
        // FUEL
        int xOrigin = guiX+left_padding+2;
        int yOrigin = guiY+top_padding+130;

        RenderSystem.setShaderTexture(0, FUEL_GAUGE);
        blit(poseStack,
                xOrigin, yOrigin,
                0, 0,
                FUEL_GAUGE_WIDTH, FUEL_GAUGE_HEIGHT,
                FUEL_GAUGE_WIDTH, FUEL_GAUGE_HEIGHT);

        float max = vehicle.getMaxFuel(), fuelPercent = 0;
        if (max != 0) fuelPercent = vehicle.getCurrentFuel() / max;

        RenderSystem.setShaderTexture(0, FUEL_GAUGE_ARROW);
        poseStack.pushPose();
        poseStack.translate(xOrigin + (double) FUEL_GAUGE_WIDTH / 2, yOrigin + 24, 0);
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(160F * fuelPercent + 10F));
        blit(poseStack,
                -FUEL_ARROW_WIDTH + 5, -FUEL_ARROW_HEIGHT / 2,
                0, 0,
                FUEL_ARROW_WIDTH, FUEL_ARROW_HEIGHT,
                FUEL_ARROW_WIDTH, FUEL_ARROW_HEIGHT);
        poseStack.popPose();
    }

    public static int getHealthColor(float health, float max) {
        return VehicleStatsOverlay.getHealthColor(health, max);
    }
}
