package com.avereon.curve.math;

public class Point {

	public static final double[] ZERO = of( 0, 0, 0 );

	public static double[] of( double x, double y ) {
		return new double[]{ x, y, 0 };
	}

	public static double[] of( double x, double y, double z ) {
		return new double[]{ x, y, z };
	}

}
