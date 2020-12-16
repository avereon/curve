package com.avereon.curve.math;

import java.util.Arrays;

public class Point {

	public static final double[] ZERO = of( 0, 0, 0 );

	public static final double[] INFINITY = of( Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY );

	public static final double[] UNDEFINED = of( Double.NaN, Double.NaN, Double.NaN );

	public static double[] of() {
		return new double[]{ 0, 0, 0 };
	}

	public static double[] of( double x, double y ) {
		return new double[]{ x, y, 0 };
	}

	public static double[] of( double x, double y, double z ) {
		if( x == -0.0 ) x = 0.0;
		if( y == -0.0 ) y = 0.0;
		if( z == -0.0 ) z = 0.0;

		double[] v = new double[]{ x, y, z };
		if( isInfinite( v ) ) Arrays.fill( v, Double.POSITIVE_INFINITY );
		if( isUndefined( v ) ) Arrays.fill( v, Double.NaN );

		return v;
	}

	public static boolean isUndefined( double[] v ) {
		return v[ 0 ] != v[ 0 ] || v[ 1 ] != v[ 1 ] || v[ 2 ] != v[ 2 ];
	}

	public static boolean isInfinite( double[] v ) {
		return v[ 0 ] == Double.POSITIVE_INFINITY || v[ 0 ] == Double.NEGATIVE_INFINITY || v[ 1 ] == Double.POSITIVE_INFINITY || v[ 1 ] == Double.NEGATIVE_INFINITY || v[ 2 ] == Double.POSITIVE_INFINITY || v[ 2 ] == Double.NEGATIVE_INFINITY;
	}

	public static int hash( double[] v ) {
		long bits;
		if( isUndefined( v ) ) {
			bits = Double.doubleToLongBits( Double.NaN );
		} else if( isInfinite( v ) ) {
			bits = Double.doubleToLongBits( Double.POSITIVE_INFINITY );
		} else {
			bits = Double.doubleToLongBits( v[ 0 ] ) + Double.doubleToLongBits( v[ 1 ] ) + Double.doubleToLongBits( v[ 2 ] );
		}
		return (int)(bits ^ (bits >>> 32));
	}

	public static String toString( double[] v ) {
		return "[" + v[ 0 ] + ", " + v[ 1 ] + ", " + v[ 2 ] + "]";
	}

}
