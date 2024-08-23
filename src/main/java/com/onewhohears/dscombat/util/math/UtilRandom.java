package com.onewhohears.dscombat.util.math;

import java.util.Random;

public class UtilRandom {
	
	public static final Random RANDOM = new Random();
	
	public static final int weightedRandomInt(int limit, double weight) {
		limit += 1;
		return (int) (weight * RANDOM.nextDouble((double)limit * weight, limit) + (1-weight) * RANDOM.nextDouble((double)limit * weight));
	}
	
}
