package com.avereon.curve.math;

import java.util.Arrays;

public class Point {

	public static final double[] ZERO = of( 0, 0, 0 );

	public static double[] of( double x, double y ) {
		return new double[]{ x, y, 0 };
	}

	public static double[] of( double x, double y, double z ) {
		if( x == -0.0 ) x = 0.0;
		if( y == -0.0 ) y = 0.0;
		if( z == -0.0 ) z = 0.0;

		double[] v = new double[]{ x, y, z };
		if( Vector.isInfinite( v ) ) Arrays.fill( v, Double.POSITIVE_INFINITY );
		if( Vector.isUndefined( v ) ) Arrays.fill( v, Double.NaN );

		return v;
	}

}
