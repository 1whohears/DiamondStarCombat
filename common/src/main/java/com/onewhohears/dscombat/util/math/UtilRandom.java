package com.onewhohears.dscombat.util.math;

import java.util.Random;

public class UtilRandom {
	
	public static final Random RANDOM = new Random();
	
	public static int weightedRandomInt(int limit, double weight) {
		limit += 1;
		double lxw = (double)limit * weight;
		if (lxw >= limit) lxw = (double)limit - 0.001;
		return (int) (weight * RANDOM.nextDouble(lxw, limit) + (1-weight) * RANDOM.nextDouble(lxw));
	}
	
}
