package com.onewhohears.dscombat.data.aircraft;

public class LiftKGraph {
	
	private static final int[] ALEXIS_PLANE_DEGRESS = {-20,   -18,   -17,   -16,   -15,   -14,   -13,   -12,   -10,    -8,   -6,    -4,    -2,0,    2,    4,    6,    8,   10,   12,   13,   14,   15,   16,   17,   18,20};
	private static final float[] ALEXIS_PLANE_LIFT  = {  0,-0.78f,-1.04f,-1.15f,-1.20f,-1.18f,-1.15f,-1.11f,-0.99f,-0.84f,0.69f,-0.48f,-0.25f,0,0.25f,0.48f,0.69f,0.84f,0.99f,1.11f,1.15f,1.18f,1.20f,1.15f,1.04f,0.78f, 0};
	public static final LiftKGraph ALEXIS_PLANE_GRAPH = new LiftKGraph(ALEXIS_PLANE_DEGRESS, ALEXIS_PLANE_LIFT);
	
	private static final int[] JAVI_PLANE_DEGRESS = ALEXIS_PLANE_DEGRESS;
	private static final float[] JAVI_PLANE_LIFT = ALEXIS_PLANE_LIFT;
	public static final LiftKGraph JAVI_PLANE_GRAPH = new LiftKGraph(JAVI_PLANE_DEGRESS, JAVI_PLANE_LIFT);
	
	private final int[] aoa;
	private final float[] lift;
	
	public LiftKGraph(int[] aoa, float[] lift) {
		if (aoa.length != lift.length) {
			this.aoa = new int[] {0};
			this.lift = new float[] {0};
			return;
		}
		this.aoa = aoa;
		this.lift = lift;
	}
	
	public float getLift(float aoaDegrees) {
		int minI = getMinIndex(aoaDegrees);
		int maxI = getMaxIndex(aoaDegrees);
		if (minI == maxI) return lift[minI];
		return (lift[maxI]-lift[minI])/(aoa[maxI]-aoa[minI]) + lift[minI];
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
	
}
