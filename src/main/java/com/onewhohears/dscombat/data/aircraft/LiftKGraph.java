package com.onewhohears.dscombat.data.aircraft;

public class LiftKGraph {
	
	private static final int[] ALEXIS_PLANE_DEGRESS = {-36,  -34,   -32,   -30,   -28,  -26,   -24,   -22,   -20,   -18,   -16,   -14,   -12,   -10,    -8,    -6,    -4,   -2,0,   2,    4,    6,    8,   10,   12,   14,   16,   18,   20,   22,   24,  26,   28,   30,   32,  34,36};
	private static final float[] ALEXIS_PLANE_LIFT  = {  0,-0.4f,-0.78f,-1.04f,-1.18f,-1.2f,-1.18f,-1.15f,-1.11f,-1.06f,-0.99f,-0.89f,-0.79f,-0.68f,-0.58f,-0.48f,-0.36f,-0.2f,0,0.2f,0.36f,0.48f,0.58f,0.68f,0.79f,0.89f,0.99f,1.06f,1.11f,1.15f,1.18f,1.2f,1.18f,1.04f,0.78f,0.4f,0};
	public static final LiftKGraph ALEXIS_PLANE_GRAPH = new LiftKGraph(ALEXIS_PLANE_DEGRESS, ALEXIS_PLANE_LIFT);
	
	private static final int[] JAVI_PLANE_DEGRESS = {-26,   -24,   -22,   -20,   -18,   -16,   -14,   -12,   -10,   -8,    -6,  -4,   -2,   0,   2,    4,    6,   8,   10,   12,  14,   16,  18,   20,   22,  24,26};
	private static final float[] JAVI_PLANE_LIFT  = {  0,-0.18f,-0.36f,-0.47f,-0.55f,-0.58f,-0.53f,-0.43f,-0.32f,-0.2f,-0.05f,0.1f,0.25f,0.4f,0.5f,0.68f,0.79f,0.9f,1.01f,1.12f,1.2f,1.28f,1.3f,1.28f,1.15f,0.7f,0};
	public static final LiftKGraph JAVI_PLANE_GRAPH = new LiftKGraph(JAVI_PLANE_DEGRESS, JAVI_PLANE_LIFT);
	
	private static final int[] WOODEN_PLANE_DEGRESS = {-20,   -18,   -17,   -16,   -15,   -14,   -13,   -12,   -10,    -8,   -6,    -4,    -2,0,    2,    4,    6,    8,   10,   12,   13,   14,   15,   16,   17,   18,20};
	private static final float[] WOODEN_PLANE_LIFT  = {  0,-0.78f,-1.04f,-1.15f,-1.20f,-1.18f,-1.15f,-1.11f,-0.99f,-0.84f,0.69f,-0.48f,-0.25f,0,0.25f,0.48f,0.69f,0.84f,0.99f,1.11f,1.15f,1.18f,1.20f,1.15f,1.04f,0.78f, 0};
	public static final LiftKGraph WOODEN_PLANE_GRAPH = new LiftKGraph(WOODEN_PLANE_DEGRESS, WOODEN_PLANE_LIFT);
	
	private static final int[] E3SENTRY_PLANE_DEGRESS = {-26,   -24,   -22,   -20,   -18,   -16,   -14,   -12,   -10,   -8,    -6,  -4,  -2,   0,    2,    4,    6,    8,   10,   12,   14,   16,  18,   20,   22,   24,26};
	private static final float[] E3SENTRY_PLANE_LIFT  = {  0,-0.18f,-0.36f,-0.47f,-0.55f,-0.58f,-0.53f,-0.43f,-0.32f,-0.2f,-0.05f,0.1f,0.2f,0.3f,0.42f,0.54f,0.64f,0.75f,0.86f,0.96f,1.02f,1.08f,1.1f,1.06f,0.96f,0.65f,0};
	public static final LiftKGraph E3SENTRY_PLANE_GRAPH = new LiftKGraph(E3SENTRY_PLANE_DEGRESS, E3SENTRY_PLANE_LIFT);
	
	private final int[] aoa;
	private final float[] lift;
	private final int criticalAOA, warnAOA;
	
	public LiftKGraph(int[] aoa, float[] lift) {
		if (aoa.length != lift.length) {
			this.aoa = new int[] {0};
			this.lift = new float[] {0};
			this.criticalAOA = 0;
			this.warnAOA = 0;
			return;
		}
		this.aoa = aoa;
		this.lift = lift;
		this.criticalAOA = findCriticalAOA();
		this.warnAOA = (int)((float)criticalAOA*0.5f);
	}
	
	public float getLift(float aoaDegrees) {
		int minI = getMinIndex(aoaDegrees);
		int maxI = getMaxIndex(aoaDegrees);
		if (minI == maxI) return lift[minI];
		float d = aoaDegrees - aoa[minI];
		return d*(lift[maxI]-lift[minI])/(aoa[maxI]-aoa[minI]) + lift[minI];
	}
	
	private int getMinIndex(float degrees) {
		int deg = (int)Math.floor(degrees);
		for (int i = 1; i < aoa.length; ++i) {
			if (aoa[i] > deg) return i-1;
		}
		return aoa.length-1;
	}
	
	private int getMaxIndex(float degrees) {
		int deg = (int)Math.ceil(degrees);
		for (int i = aoa.length-2; i >= 0; --i) {
			if (aoa[i] < deg) return i+1;
		}
		return 0;
	}
	
	private int findCriticalAOA() {
		for (int i = 1; i < aoa.length-1; ++i) {
			if (aoa[i] < 0) continue;
			if (lift[i-1] < lift[i] && lift[i+1] < lift[i])
				return aoa[i];
		}
		return 0;
	}
	
	public int getCriticalAOA() {
		return criticalAOA;
	}
	
	public int getWarnAOA() {
		return warnAOA;
	}
	
}
