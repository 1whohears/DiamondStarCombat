package com.onewhohears.dscombat.client.model.obj.customanims;

import com.google.gson.JsonObject;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;
import com.onewhohears.dscombat.util.UtilParse;
import com.onewhohears.dscombat.util.math.UtilAngles;

import net.minecraft.util.Mth;

public abstract class VehicleModelTransform {
	
	public static final Matrix4f INVISIBLE = Matrix4f.createScaleMatrix(0, 0, 0);
	public static final Matrix4f NOTHING = Matrix4f.createScaleMatrix(1, 1, 1);
	
	private final String model_part_key;
	public VehicleModelTransform(JsonObject data) {
		this.model_part_key = data.get("model_part_key").getAsString();
	}
	public String getKey() {
		return model_part_key;
	}
	public abstract Matrix4f getTransform(EntityVehicle entity, float partialTicks);
	
	public static abstract class Pivot extends VehicleModelTransform {
		private final Vector3f pivot;
		public Pivot(JsonObject data) {
			super(data);
			pivot = UtilParse.readVec3f(data, "pivot");
		}
		public Vector3f getPivot() {
			return pivot;
		}
	}
	
	public static enum RotationAxis {
		X, Y, Z
	}
	
	public abstract static class AxisRotation extends Pivot {
		private final RotationAxis rot_axis;
		public AxisRotation(JsonObject data) {
			super(data);
			rot_axis = UtilParse.getEnumSafe(data, "rot_axis", RotationAxis.class);
		}
		public RotationAxis getRotAxis() {
			return rot_axis;
		}
		public abstract float getRotDeg(EntityVehicle entity, float partialTicks);
		@Override
		public Matrix4f getTransform(EntityVehicle entity, float partialTicks) {
			float degrees = getRotDeg(entity, partialTicks);
			if (degrees == 0) return NOTHING;
			switch (rot_axis) {
			case X: return UtilAngles.pivotPixelsRotX(getPivot().x(), getPivot().y(), getPivot().z(), degrees);
			case Y: return UtilAngles.pivotPixelsRotY(getPivot().x(), getPivot().y(), getPivot().z(), degrees);
			case Z: return UtilAngles.pivotPixelsRotZ(getPivot().x(), getPivot().y(), getPivot().z(), degrees);
			}
			return null;
		}
	}
	
	public static class ContinuousRotation extends AxisRotation {
		private final float rot_rate;
		public ContinuousRotation(JsonObject data) {
			super(data);
			rot_rate = UtilParse.getFloatSafe(data, "rot_rate", 0);
		}
		public float getRotRate() {
			return rot_rate;
		}
		@Override
		public float getRotDeg(EntityVehicle entity, float partialTicks) {
			return UtilAngles.lerpAngle(partialTicks, entity.tickCount*getRotRate(), (entity.tickCount+1)*getRotRate());
		}
	}
	
	public static class MotorRotation extends ContinuousRotation {
		public MotorRotation(JsonObject data) {
			super(data);
		}
		@Override
		public float getRotDeg(EntityVehicle entity, float partialTicks) {
			return entity.getMotorRotation(partialTicks, getRotRate());
		}
	}
	
	public static class WheelRotation extends ContinuousRotation {
		public WheelRotation(JsonObject data) {
			super(data);
		}
		@Override
		public float getRotDeg(EntityVehicle entity, float partialTicks) {
			return entity.getWheelRotation(partialTicks, getRotRate());
		}
	}
	
	public static enum InputAxis {
		PITCH, YAW, ROLL
	}
	
	public static class InputBoundRotation extends AxisRotation {
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
		public float getInput(EntityVehicle entity) {
			switch (input_axis) {
			case PITCH: return entity.inputs.pitch;
			case ROLL: return entity.inputs.roll;
			case YAW: return entity.inputs.yaw;		
			}
			return 0;
		}
		@Override
		public float getRotDeg(EntityVehicle entity, float partialTicks) {
			return getInput(entity) * getBound();
		}
	}
	
	public static class SpinningRadar extends ContinuousRotation {
		private final String radar_id;
		public SpinningRadar(JsonObject data) {
			super(data);
			radar_id = UtilParse.getStringSafe(data, "radar_id", "");
		}
		public String getRadarId() {
			return radar_id;
		}
		@Override
		public Matrix4f getTransform(EntityVehicle entity, float partialTicks) {
			if (!entity.radarSystem.hasRadar(radar_id)) return INVISIBLE;
			return super.getTransform(entity, partialTicks);
		}
	}
	
	public static class LandingGear extends AxisRotation {
		private final float fold_angle;
		public LandingGear(JsonObject data) {
			super(data);
			fold_angle = UtilParse.getFloatSafe(data, "fold_angle", 0);
		}
		public float getFoldAngle() {
			return fold_angle;
		}
		@Override
		public float getRotDeg(EntityVehicle entity, float partialTicks) {
			float gear = entity.getLandingGearPos(partialTicks);
			return Mth.lerp(gear, 0, fold_angle);
		}
		@Override
		public Matrix4f getTransform(EntityVehicle entity, float partialTicks) {
			if (!entity.isLandingGear()) return INVISIBLE;
			return super.getTransform(entity, partialTicks);
		}
	}
	
}
