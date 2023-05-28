package com.onewhohears.dscombat.client.event.forgebus;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
public class ClientRenderEvents {
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void playerRenderPre(RenderPlayerEvent.Pre event) {
		Player player = event.getPlayer();
		if (!player.isPassenger()) return;
		if (!(player.getRootVehicle() instanceof EntityAircraft plane)) return;
		Quaternion q = UtilAngles.lerpQ(event.getPartialTick(), plane.getPrevQ(), plane.getClientQ());
		Vec3 eye = new Vec3(0, player.getEyeHeight(), 0);
		Vec3 t = eye.subtract(UtilAngles.rotateVector(eye, q));
		event.getPoseStack().translate(t.x, t.y, t.z);
		event.getPoseStack().mulPose(q);
		float[] relangles = UtilAngles.globalToRelativeDegrees(player.getXRot(), player.getYRot(), q);
		player.setYHeadRot(relangles[1]);
		player.yHeadRotO = relangles[1];
		player.setYBodyRot(relangles[1]);
		player.yBodyRotO = relangles[1];
		// HOW 1 set player head model part x rot to relangles[0]
		// neither of these work
		//event.getRenderer().getModel().head.xRot = (float) Math.toRadians(relangles[0]);
		/*event.getRenderer().getModel().setupAnim((LocalPlayer)player, 0f, 
				0f, 0f, 0f, (float) Math.toRadians(relangles[0]));*/
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void playerRenderPost(RenderPlayerEvent.Post event) {
		Player player = event.getPlayer();
		if (!player.isPassenger()) return;
		if (!(player.getRootVehicle() instanceof EntityAircraft plane)) return;
		player.setYHeadRot(player.getYRot());
		player.yHeadRotO = player.getYRot();
		player.setYBodyRot(player.getYRot());
		player.yBodyRotO = player.getYRot();
	}
	
}
