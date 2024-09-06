package com.avereon.curve.math;

import java.util.function.BiFunction;
import java.util.function.Function;

public class RK4 {

	/**
	 * The Runge-Kutta 4th order method, for partial differential equations.
	 *
	 * @param t The current time
	 * @param y The current value
	 * @param dt The time step size
	 * @param dydt The derivative function
	 * @return The next value
	 */
	public static double next( double t, double y, double dt, BiFunction<Double, Double, Double> dydt ) {
		double k1 = dt * dydt.apply( t, y );
		double k2 = dt * dydt.apply( t + dt / 2, y + dt * k1 / 2 );
		double k3 = dt * dydt.apply( t + dt / 2, y + dt * k2 / 2 );
		double k4 = dt * dydt.apply( t + dt, y + dt * k3 );
		return y + (k1 + 4 * k2 + k4) / 6;
	}

	/**
	 * The Runge-Kutta 4th order method, optimized for non-partial differential
	 * equations.
	 *
	 * @param t The current time
	 * @param y The current value
	 * @param dt The time step size
	 * @param dydt The derivative function
	 * @return The next value
	 */
	public static double next( double t, double y, double dt, Function<Double, Double> dydt ) {
		// The slope at t
		double k1 = dt * dydt.apply( t );

		// The slope halfway through the step, used twice
		double k2 = dt * dydt.apply( t + dt / 2 );

		// The slope at the end of the step
		double k4 = dt * dydt.apply( t + dt );

		// The next y is computed as:
		return y + (k1 + 4 * k2 + k4) / 6;
	}

	@SafeVarargs
	public static double[] next( double t, double[] u, double dt, final Function<Double, Double>... didt  ) {
		int size = u.length;
		if( size != didt.length ) throw new IllegalArgumentException( "Vector u and derivative function array didt must be the same length" );

		// Create return array the same size as the input array
		double[] v = new double[ size ];

		for( int i = 0; i < size; i++ ) {
			// The slope at t
			double k1 = dt * didt[i].apply( t );

			// The slope halfway through the step, used twice
			double k2 = dt * didt[i].apply( t + dt / 2 );

			// The slope at the end of the step
			double k4 = dt * didt[i].apply( t + dt );

			// The next y is computed as:
			v[i] = u[i] + (k1 + 4 * k2 + k4) / 6;
		}

		return v;
	}

}
