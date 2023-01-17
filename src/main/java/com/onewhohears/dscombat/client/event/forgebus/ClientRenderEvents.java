package com.onewhohears.dscombat.client.event.forgebus;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
public class ClientRenderEvents {
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void playerRender(RenderPlayerEvent.Pre event) {
		Minecraft m = Minecraft.getInstance();
		final var playerC = m.player;
		Player player = event.getEntity();
		if (player.getRootVehicle() instanceof EntityAircraft plane) {
			if (player.equals(playerC) && m.options.getCameraType().isFirstPerson()) {
				event.setCanceled(true);
				return;
			}
			Quaternion q = UtilAngles.lerpQ(event.getPartialTick(), plane.getPrevQ(), plane.getClientQ());
			event.getPoseStack().mulPose(q);
			float[] relangles = UtilAngles.globalToRelativeDegrees(player.getXRot(), player.getYRot(), q);
			player.setYBodyRot(relangles[1]);
			player.setYHeadRot(relangles[1]);
			// HOW set player head model part x rot to relangles[0]
			//event.getRenderer().getModel().head.xRot = (float) Math.toRadians(relangles[0]);
			//event.getRenderer().getModel().head.xRot = 0;
			/*event.getRenderer().getModel().setupAnim((LocalPlayer)player, 0f, 
					0f, 0f, 0f, (float) Math.toRadians(relangles[0]));*/
		}
	}
	
}
