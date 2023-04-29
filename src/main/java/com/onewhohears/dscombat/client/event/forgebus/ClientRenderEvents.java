package com.onewhohears.dscombat.client.event.forgebus;

import com.mojang.math.Quaternion;
import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.entity.aircraft.EntityAircraft;
import com.onewhohears.dscombat.entity.parts.EntitySeat;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = DSCombatMod.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
public class ClientRenderEvents {
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void playerRenderPre2(RenderPlayerEvent.Pre event) {
		PlayerModel<AbstractClientPlayer> model = event.getRenderer().getModel();
		model.setupAnim((AbstractClientPlayer) event.getEntity(), 
				0, 0, 0, 
				0, 0);
	}
	
	/*private static HashMap<Integer,Boolean> fireEvent = new HashMap<>();
	
	private static boolean shouldFireEvent(int id) {
		return fireEvent.getOrDefault(fireEvent, true);
	}
	
	private static void setFireEvent(int id, boolean fire) {
		fireEvent.put(id, fire);
	}*/
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void playerRenderPre(RenderPlayerEvent.Pre event) {
		if (event.isCancelable()) return;
		Player player = event.getEntity();
		if (!player.isPassenger() || !(player.getRootVehicle() instanceof EntityAircraft plane)) {
			//setFireEvent(player.getId(), true);
			return;
		}
		Minecraft m = Minecraft.getInstance();
		changePlayerHitbox(player);
		if (player.equals(m.player) && m.options.getCameraType().isFirstPerson()) {
			event.setCanceled(true);
			//setFireEvent(player.getId(), true);
			return;
		}
		Quaternion q = UtilAngles.lerpQ(event.getPartialTick(), plane.getPrevQ(), plane.getClientQ());
		event.getPoseStack().mulPose(q);
		//float[] relangles = UtilAngles.globalToRelativeDegrees(player.getXRot(), player.getYRot(), q);
		//event.getPoseStack().mulPose(Vector3f.YN.rotationDegrees(relangles[1]));
		/*event.getRenderer().getModel().setupAnim((LocalPlayer)player, 
				0f, 0f, 0f, 
				0f, //Mth.wrapDegrees(relangles[1]+180), 
				-relangles[0]);*/
		/*player.setYBodyRot(relangles[1]);
		player.setYHeadRot(relangles[1]);
		player.setXRot(relangles[0]);*/
		// HOW 1 set player head model part x rot to relangles[0]
		//event.getRenderer().getModel().head.xRot = (float) Math.toRadians(relangles[0]);
		//event.getRenderer().getModel().head.xRot = 0;
		//float headY = Mth.wrapDegrees(plane.getYRot()+relangles[1]-player.getYRot());
		//float headY = Mth.wrapDegrees(player.getYRot()-plane.getYRot());
		System.out.println("player      y rot = "+player.getYRot());
		System.out.println("plane       y rot = "+plane.getYRot());
		//System.out.println("player rel  y rot = "+relangles[1]);
		//System.out.println("player head y rot = "+headY);
		//PlayerModel<AbstractClientPlayer> model = event.getRenderer().getModel();
		//model.getHead().yRot = Mth.wrapDegrees(relangles[1]+180) * Mth.DEG_TO_RAD;
		//model.getHead().xRot = -relangles[0] * Mth.DEG_TO_RAD;
		//model.getHead().yRot = player.getYRot() * Mth.DEG_TO_RAD;
		//model.getHead().xRot = player.getXRot() * Mth.DEG_TO_RAD;
		//model.getHead().yRot = headY * Mth.DEG_TO_RAD;
		//model.getHead().xRot = -relangles[0] * Mth.DEG_TO_RAD;
		/*if (getSetModelProperties() == null || getRenderLivingEntity() == null) return;
		try {
			getSetModelProperties().invoke(event.getRenderer(), (AbstractClientPlayer)player);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			setModelProperties = null;
			e.printStackTrace();
			return;
		}*/
		/*try {
			getRenderLivingEntity().invoke((LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>)event.getRenderer(), 
				(AbstractClientPlayer)player, 
				player.getYRot(),
				event.getPartialTick(), event.getPoseStack(), 
				event.getMultiBufferSource(), event.getPackedLight());
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			renderLivingEntity = null;
			e.printStackTrace();
			return;
		}*/
		//m.getEntityRenderDispatcher().renderers.get(EntityType.pla);
		event.setCanceled(true);
		/*event.getRenderer().render((AbstractClientPlayer)player, 
				player.getYRot(), event.getPartialTick(), 
				event.getPoseStack(), event.getMultiBufferSource(), 
				event.getPackedLight());*/
	}
	
	/*public static Method setModelProperties, renderLivingEntity;
	public static boolean tried1 = false, tried2 = false;;
	
	@Nullable
	public static Method getSetModelProperties() {
		if (setModelProperties != null) return setModelProperties;
		if (tried1) return null;
		tried1 = true;
		try {
			setModelProperties = PlayerRenderer.class.getDeclaredMethod("m_117818", AbstractClientPlayer.class);
			setModelProperties.setAccessible(true);
			return setModelProperties;
		} catch (NoSuchMethodException | SecurityException e) {
			System.out.println("WARNING: PlayerRenderer.setModelProperties is not m_117818");
		}
		try {
			setModelProperties = PlayerRenderer.class.getDeclaredMethod("setModelProperties", AbstractClientPlayer.class);
			setModelProperties.setAccessible(true);
			return setModelProperties;
		} catch (NoSuchMethodException | SecurityException e) {
			System.out.println("ERROR: PlayerRenderer.setModelProperties is not setModelProperties");
		}
		return null;
	}
	
	@Nullable
	public static Method getRenderLivingEntity() {
		if (renderLivingEntity != null) return renderLivingEntity;
		if (tried2) return null;
		tried2 = true;
		try {
			renderLivingEntity = LivingEntityRenderer.class.getDeclaredMethod("m_166121_", 
				LivingEntity.class, float.class, float.class, PoseStack.class, MultiBufferSource.class, int.class);
			renderLivingEntity.setAccessible(true);
			return renderLivingEntity;
		} catch (NoSuchMethodException | SecurityException e) {
			System.out.println("WARNING: PlayerRenderer.render is not m_166121_");
		}
		try {
			renderLivingEntity = LivingEntityRenderer.class.getDeclaredMethod("render", 
				LivingEntity.class, float.class, float.class, PoseStack.class, MultiBufferSource.class, int.class);
			renderLivingEntity.setAccessible(true);
			return renderLivingEntity;
		} catch (NoSuchMethodException | SecurityException e) {
			System.out.println("ERROR: PlayerRenderer.render is not render");
		}
		return null;
	}*/
	
	/*@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void playerRenderPost(RenderPlayerEvent.Post event) {
		Player player = event.getEntity();
		if (!player.isPassenger()) return;
		if (!(player.getRootVehicle() instanceof EntityAircraft plane)) return;
		Minecraft m = Minecraft.getInstance();
		if (player.equals(m.player) && m.options.getCameraType().isFirstPerson()) return;
		float y = m.getCameraEntity().getYRot();
		player.setYBodyRot(y);
		player.setYHeadRot(y);
		player.setXRot(m.getCameraEntity().getXRot());
	}*/
	
	private static void changePlayerHitbox(Player player) {
		double x = player.getX();
		double y = player.getY();
		double z = player.getZ();
		double w = player.getBbWidth()/2;
		player.setBoundingBox(new AABB(x+w, y+w, z+w, x-w, y-w, z-w));
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void onRenderPlayerHand(RenderHandEvent event) {
		Minecraft m = Minecraft.getInstance();
		final var player = m.player;
		if (!(player.getVehicle() instanceof EntitySeat seat)) return;
		/*System.out.println("player      y rot = "+player.getYRot());
		System.out.println("player view y rot = "+player.getViewYRot(event.getPartialTick()));
		System.out.println("camera      y rot = "+m.getCameraEntity().getYRot());
		System.out.println("camera view y rot = "+m.getCameraEntity().getViewYRot(event.getPartialTick()));*/
		/*if (event.getHand() == InteractionHand.MAIN_HAND) {
			if (!player.getMainHandItem().isEmpty()) return;
		} else if (event.getHand() == InteractionHand.OFF_HAND) {
			if (!player.getOffhandItem().isEmpty()) return;
		}*/
		// FIXME 9.1 revive vanilla hand swing effect
		// FIXME 9.2 mr crayfish guns first person model points in wrong direction
		// FIXME 9.3 journeymap stats no display when look at waypoint
		//event.getPoseStack().setIdentity();
	}
	
}
