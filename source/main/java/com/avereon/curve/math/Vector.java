package com.avereon.curve.math;

public class Vector {

	public static double[] of( double x, double y ) {
		return new double[]{ x, y, 0 };
	}

	public static double[] of( double x, double y, double z ) {
		return new double[]{ x, y, z };
	}

	public static double distance( double[] a, double[] b ) {
		double dx = b[ 0 ] - a[ 0 ];
		double dy = b[ 1 ] - a[ 1 ];
		double dz = b[ 2 ] - a[ 2 ];
		return Math.sqrt( dx * dx + dy * dy + dz * dz );
	}

}
