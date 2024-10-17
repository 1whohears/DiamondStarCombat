package com.onewhohears.dscombat.client.model.obj.customanims;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.entity.vehicle.RotableHitbox;
import com.onewhohears.onewholibs.client.model.obj.customanims.EntityModelTransform;

import com.onewhohears.onewholibs.util.UtilParse;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

public class VehicleModelTransforms {
	
	public static class InputBoundTranslation<T extends EntityVehicle> extends EntityModelTransform.Translation<T> {
		private final InputAxis input_axis;
		public InputBoundTranslation(JsonObject data) {
			super(data);
			input_axis = UtilParse.getEnumSafe(data, "input_axis", InputAxis.class);
		}
		public InputAxis getInputAxis() {
			return input_axis;
		}
		@Override
		public float getTranslationProgress(T entity, float partialTicks) {
			return getInputAxis().getVehicleInput(entity);
		}
	}
	
	public static class HitboxDestroyPart<T extends EntityVehicle> extends EntityModelTransform<T> {
		private final String hitbox_name;
		public HitboxDestroyPart(JsonObject data) {
			super(data);
			hitbox_name = UtilParse.getStringSafe(data, "hitbox_name", "");
		}
		public String getHitboxName() {
			return hitbox_name;
		}
		@Override
		public Matrix4f getTransform(T entity, float partialTicks) {
			RotableHitbox hitbox = entity.getHitboxByName(getHitboxName());
			if (hitbox == null) return NOTHING;
			return hitbox.isDestroyed() ? INVISIBLE : NOTHING;
		}
	}
	
	public static class MotorRotation<T extends EntityVehicle> extends EntityModelTransform.ContinuousRotation<T> {
		public MotorRotation(JsonObject data) {
			super(data);
		}
		@Override
		public float getRotDeg(T entity, float partialTicks) {
			return entity.getMotorRotation(partialTicks, getRotRate());
		}
	}
	
	public static class WheelRotation<T extends EntityVehicle> extends EntityModelTransform.ContinuousRotation<T> {
		public WheelRotation(JsonObject data) {
			super(data);
		}
		@Override
		public float getRotDeg(T entity, float partialTicks) {
			return entity.getWheelRotation(partialTicks, getRotRate());
		}
	}
	
	private static interface InputFactory {
		float get(EntityVehicle entity);
	}
	
	public static enum InputAxis {
		PITCH((entity) -> entity.inputs.pitch), 
		YAW((entity) -> entity.inputs.yaw), 
		ROLL((entity) -> entity.inputs.roll), 
		THROTTLE(EntityVehicle::getCurrentThrottle);
		private final InputFactory input;
		private InputAxis(InputFactory input) {
			this.input = input;
		}
		public float getVehicleInput(EntityVehicle entity) {
			return input.get(entity);
		}
	}
	
	public static class InputBoundRotation<T extends EntityVehicle> extends EntityModelTransform.AxisRotation<T> {
		private final InputAxis input_axis;
		private final float bound;
		public InputBoundRotation(JsonObject data) {
			super(data);
			input_axis = UtilParse.getEnumSafe(data, "input_axis", InputAxis.class);
			bound = UtilParse.getFloatSafe(data, "bound", 0);
		}
		public InputAxis getInputAxis() {
			return input_axis;
		}
		public float getBound() {
			return bound;
		}
		public float getInput(T entity) {
			return getInputAxis().getVehicleInput(entity);
		}
		@Override
		public float getRotDeg(T entity, float partialTicks) {
			return getInput(entity) * getBound();
		}
	}
	
	public static class PlaneFlapRotation<T extends EntityVehicle> extends InputBoundRotation<T> {
		public PlaneFlapRotation(JsonObject data) {
			super(data);
		}
		@Override
		public float getInput(T entity) {
			if (entity.isFlapsDown()) return -1 * Math.signum(getBound());
			return getInputAxis().getVehicleInput(entity);
		}
	}
	
	public static class SpinningRadar<T extends EntityVehicle> extends EntityModelTransform.ContinuousRotation<T> {
		private final String radar_id;
		public SpinningRadar(JsonObject data) {
			super(data);
			radar_id = UtilParse.getStringSafe(data, "radar_id", "");
		}
		public String getRadarId() {
			return radar_id;
		}
		@Override
		public Matrix4f getTransform(T entity, float partialTicks) {
			if (!entity.radarSystem.hasRadar(radar_id)) return INVISIBLE;
			return super.getTransform(entity, partialTicks);
		}
	}
	
	public static class LandingGear<T extends EntityVehicle> extends EntityModelTransform.AxisRotation<T> {
		private final float fold_angle;
		public LandingGear(JsonObject data) {
			super(data);
			fold_angle = UtilParse.getFloatSafe(data, "fold_angle", 0);
		}
		public float getFoldAngle() {
			return fold_angle;
		}
		@Override
		public float getRotDeg(T entity, float partialTicks) {
			float gear = entity.getLandingGearPos(partialTicks);
			return Mth.lerp(gear, 0, fold_angle);
		}
		@Override
		public Matrix4f getTransform(T entity, float partialTicks) {
			if (entity.getLandingGearPos(partialTicks) == 1) return INVISIBLE;
			return super.getTransform(entity, partialTicks);
		}
	}
	
}
