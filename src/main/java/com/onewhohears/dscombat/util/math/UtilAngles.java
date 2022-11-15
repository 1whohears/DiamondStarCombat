package com.onewhohears.dscombat.util.math;

import com.mojang.math.Quaternion;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class UtilAngles {
	
	public static double getHorizontalDistanceSqr(Vec3 vec3) {
        return Math.sqrt((vec3.x * vec3.x) + (vec3.z * vec3.z));
    }

    public static double normalizedDotProduct(Vec3 v1, Vec3 v2) {
        return v1.dot(v2) / (v1.length() * v2.length());
    }

    public static float getPitch(Vec3 motion) {
        double y = -motion.y;
        return (float) Math.toDegrees(Math.atan2(y, Math.sqrt(motion.x * motion.x + motion.z * motion.z)));
    }

    public static float getYaw(Vec3 motion) {
        return (float) Math.toDegrees(Math.atan2(-motion.x, motion.z));
    }

    public static float lerpAngle(float perc, float start, float end) {
        return start + perc * Mth.wrapDegrees(end - start);
    }

    public static float lerpAngle180(float perc, float start, float end) {
        if (degreesDifferenceAbs(start, end) > 90)
            end += 180;
        return start + perc * Mth.wrapDegrees(end - start);
    }

    public static double lerpAngle180(double perc, double start, double end) {
        if (degreesDifferenceAbs(start, end) > 90)
            end += 180;
        return start + perc * Mth.wrapDegrees(end - start);
    }

    public static double lerpAngle(double perc, double start, double end) {
        return start + perc * Mth.wrapDegrees(end - start);
    }

    public static double degreesDifferenceAbs(double p_203301_0_, double p_203301_1_) {
        return Math.abs(wrapSubtractDegrees(p_203301_0_, p_203301_1_));
    }

    public static double wrapSubtractDegrees(double p_203302_0_, double p_203302_1_) {
        return Mth.wrapDegrees(p_203302_1_ - p_203302_0_);
    }

    public static Vec3 rotationToVector(double yaw, double pitch) {
        yaw = Math.toRadians(yaw);
        pitch = Math.toRadians(pitch);
        double xzLen = Math.cos(pitch);
        double x = -xzLen * Math.sin(yaw);
        double y = Math.sin(-pitch);
        double z = xzLen * Math.cos(-yaw);
        return new Vec3(x, y, z);
    }

    public static Vec3 rotationToVector(double yaw, double pitch, double size) {
        Vec3 vec = rotationToVector(yaw, pitch);
        return vec.scale(size / vec.length());
    }

    public static EulerAngles toRadians(Quaternion q) {
        EulerAngles angles = new EulerAngles();
        
        // roll (x-axis rotation)
        double sinr_cosp = 2 * (q.r() * q.k() + q.i() * q.j());
        double cosr_cosp = 1 - 2 * (q.k() * q.k() + q.i() * q.i());
        angles.roll = Math.atan2(sinr_cosp, cosr_cosp);

        // pitch (z-axis rotation)
        double sinp = 2 * (q.r() * q.i() - q.j() * q.k());
        if (Math.abs(sinp) >= 0.999) {
            angles.pitch = Math.signum(sinp) * Math.PI / 2; // use 90 degrees if out of range
        } else {
            angles.pitch = Math.asin(sinp);
        }

        // yaw (y-axis rotation)
        double siny_cosp = 2 * (q.r() * q.j() + q.k() * q.i());
        double cosy_cosp = 1 - 2 * (q.i() * q.i() + q.j() * q.j());
        angles.yaw = -Math.atan2(siny_cosp, cosy_cosp);

        return angles;
    }
    
    public static EulerAngles toDegrees(Quaternion q) {
    	EulerAngles angles = toRadians(q);
    	angles.roll = Math.toDegrees(angles.roll);
    	angles.pitch = Math.toDegrees(angles.pitch);
    	angles.yaw = Math.toDegrees(angles.yaw);
    	return angles;
    }

    public static float fastInvSqrt(float number) {
        float f = 0.5F * number;
        int i = Float.floatToIntBits(number);
        i = 1597463007 - (i >> 1);
        number = Float.intBitsToFloat(i);
        return number * (1.5F - f * number * number);
    }

    public static Quaternion normalizeQuaternion(Quaternion q) {
        float f = q.i() * q.i() + q.j() * q.j() + q.k() * q.k() + q.r() * q.r();
        float x = q.i();
        float y = q.j();
        float z = q.k();
        float w = q.r();
        if (f > 1.0E-6F) {
            float f1 = fastInvSqrt(f);
            x *= f1;
            y *= f1;
            z *= f1;
            w *= f1;
            return new Quaternion(x, y, z, w);
        } else {
            return new Quaternion(0, 0, 0, 0);
        }

    }

    public static Quaternion toQuaternion(double yaw, double pitch, double roll) { // yaw (Z), pitch (Y), roll (X)
        // Abbreviations for the various angular functions
        yaw = Math.toRadians(yaw);
        pitch = -Math.toRadians(pitch);
        roll = Math.toRadians(roll);

        double cy = Math.cos(yaw * 0.5);
        double sy = Math.sin(yaw * 0.5);
        double cp = Math.cos(pitch * 0.5);
        double sp = Math.sin(pitch * 0.5);
        double cr = Math.cos(roll * 0.5);
        double sr = Math.sin(roll * 0.5);

        float w = (float) (cr * cp * cy + sr * sp * sy);
        float z = (float) (sr * cp * cy - cr * sp * sy);
        float x = (float) (cr * sp * cy + sr * cp * sy);
        float y = (float) (cr * cp * sy - sr * sp * cy);

        return new Quaternion(x, y, z, w);
    }

    public static Quaternion lerpQ(float perc, Quaternion start, Quaternion end) {
        // Only unit quaternions are valid rotations.
        // Normalize to avoid undefined behavior.
        start = normalizeQuaternion(start);
        end = normalizeQuaternion(end);

        // Compute the cosine of the angle between the two vectors.
        double dot = start.i() * end.i() + start.j() * end.j() + start.k() * end.k() + start.r() * end.r();

        // If the dot product is negative, slerp won't take
        // the shorter path. Note that v1 and -v1 are equivalent when
        // the negation is applied to all four components. Fix by
        // reversing one quaternion.
        if (dot < 0.0f) {
            end = new Quaternion(-end.i(), -end.j(), -end.k(), -end.r());
            dot = -dot;
        }

        double DOT_THRESHOLD = 0.9995;
        if (dot > DOT_THRESHOLD) {
            // If the inputs are too close for comfort, linearly interpolate
            // and normalize the result.

            Quaternion quaternion = new Quaternion(
                start.i() * (1 - perc) + end.i() * perc,
                start.j() * (1 - perc) + end.j() * perc,
                start.k() * (1 - perc) + end.k() * perc,
                start.r() * (1 - perc) + end.r() * perc
            );
            return normalizeQuaternion(quaternion);
        }

        // Since dot is in range [0, DOT_THRESHOLD], acos is safe
        double theta_0 = Math.acos(dot);        // theta_0 = angle between input vectors
        double theta = theta_0 * perc;          // theta = angle between v0 and result
        double sin_theta = Math.sin(theta);     // compute this value only once
        double sin_theta_0 = Math.sin(theta_0); // compute this value only once

        float s0 = (float) (Math.cos(theta) - dot * sin_theta / sin_theta_0);  // == sin(theta_0 - theta) / sin(theta_0)
        float s1 = (float) (sin_theta / sin_theta_0);

        Quaternion quaternion = new Quaternion(
            start.i() * (s0) + end.i() * s1,
            start.j() * (s0) + end.j() * s1,
            start.k() * (s0) + end.k() * s1,
            start.r() * (s0) + end.r() * s1
        );
        return normalizeQuaternion(quaternion);
    }

    public static class EulerAngles {
        public double pitch, yaw, roll;

        public EulerAngles() {}

        public EulerAngles(EulerAngles a) {
            this.pitch = a.pitch;
            this.yaw = a.yaw;
            this.roll = a.roll;
        }

        public EulerAngles copy() {
            return new EulerAngles(this);
        }

        @Override
        public String toString() {
            return "EulerAngles{" +
                "pitch=" + pitch +
                ", yaw=" + yaw +
                ", roll=" + roll +
                '}';
        }
    }
    
    public static Vec3 getRollAxis(Quaternion q) {
    	EulerAngles a = toRadians(q);
    	return getRollAxis(a.pitch, a.yaw);
    }
    
    public static Vec3 getRollAxis(double pitchRad, double yawRad) {
		return new Vec3(-Math.sin(yawRad)*Math.cos(pitchRad), 
							Math.sin(-pitchRad), 
							Math.cos(yawRad)*Math.cos(pitchRad));
	}
    
    /**
     * TODO doesn't work
     */
    public static Vec3 getPitchAxis(Quaternion q) {
    	EulerAngles a = toRadians(q);
    	return getPitchAxis(a.pitch, a.yaw, a.roll);
    }
    
    /**
     * TODO doesn't work
     */
    public static Vec3 getPitchAxis(double pitchRad, double yawRad, double rollRad) {
		double CP = Math.cos(pitchRad);
		double SP = Math.sin(pitchRad);
		double CY = Math.cos(yawRad);
		double SY = Math.sin(yawRad);
		double CR = Math.cos(rollRad);
		double SR = Math.sin(rollRad);
		return new Vec3(-SY*SP*SR+CY*CR,
						-CP*SR,
						-CY*SP*SR-SY*CR);
	}
    
    public static Vec3 getYawAxis(Quaternion q) {
    	EulerAngles a = toRadians(q);
    	return getYawAxis(a.pitch, a.yaw, a.roll);
    }
    
    public static Vec3 getYawAxis(double pitchRad, double yawRad, double rollRad) {
    	double CP = Math.cos(-pitchRad);
		double SP = Math.sin(-pitchRad);
		double CY = Math.cos(yawRad);
		double SY = Math.sin(yawRad);
		double CR = Math.cos(rollRad);
		double SR = Math.sin(rollRad);
		return new Vec3(SY*SP*CR-CY*SR,
						CP*CR,
						-(CY*SP*CR+SY*SR));
	}
    
    public static Vec3 rotateVector(Vec3 n, Quaternion q) {
    	//System.out.println("ROTATE VECTOR");
    	//System.out.println(v);
    	//System.out.println(q);
    	Quaternion nq = new Quaternion((float)n.x, (float)n.y, (float)n.z, 0);
    	Quaternion cq = q.copy(); cq.conj();
    	Quaternion q1 = q.copy();
    	q1.mul(nq);
    	q1.mul(cq);
    	Vec3 a = new Vec3(q1.i(), q1.j(), q1.k());
    	//System.out.println(a);
    	return a;
    }
}
