package com.onewhohears.dscombat.util.math;

public class UtilEstimate {

    public static double nextGuessSecantMethod(double x0, double x1, double f_x0, double f_x1) throws IllegalArgumentException {
        if (f_x1 == f_x0) {
            throw new IllegalArgumentException("Division by zero: f(x1) and f(x0) cannot be equal");
        }

        return x1 - f_x1 * (x1 - x0) / (f_x1 - f_x0);
    }

}
