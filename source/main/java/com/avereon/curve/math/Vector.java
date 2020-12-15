package com.avereon.curve.math;

public class Vector extends Point {

	public static double distance( double[] a, double[] b ) {
		return magnitude( b[ 0 ] - a[ 0 ], b[ 1 ] - a[ 1 ], b[ 2 ] - a[ 2 ] );
	}

	public static boolean isUndefined( double[] v ) {
		return v[ 0 ] != v[ 0 ] || v[ 1 ] != v[ 1 ] || v[ 2 ] != v[ 2 ];
	}

	public static boolean isInfinite( double[] v ) {
		return v[ 0 ] == Double.POSITIVE_INFINITY || v[ 0 ] == Double.NEGATIVE_INFINITY || v[ 1 ] == Double.POSITIVE_INFINITY || v[ 1 ] == Double.NEGATIVE_INFINITY || v[ 2 ] == Double.POSITIVE_INFINITY || v[ 2 ] == Double.NEGATIVE_INFINITY;
	}

	/**
	 * Calculate the magnitude of the vector. If the vector is invalid then
	 * Double.NaN is returned. If the vector is infinite then
	 * Double.POSITIVE_INFINITY is returned.
	 *
	 * @return The vector magnitude
	 */
	public static double magnitude( double[] v ) {
		return magnitude( v[ 0 ], v[ 1 ], v[ 2 ] );
	}

	/**
	 * Return a new vector that is the inverse of this one.
	 *
	 * @return The inverse vector
	 */
	public static double[] inverse( double[] v ) {
		return of( -v[ 0 ], -v[ 1 ], -v[ 2 ] );
	}

	public static double[] reverse( double[] v ) {
		return inverse( v );
	}

	/**
	 * Calculate the dot product of one vector with another. If either vector is
	 * invalid then Double.NaN is returned. If either vector is infinite then
	 * Double.POSITIVE_INFINITY is returned.
	 *
	 * @param a The first vector
	 * @param b The second vector
	 * @return The dot product of the two vectors
	 */
	public static double dot( double[] a, double[] b ) {
		return a[ 0 ] * b[ 0 ] + a[ 1 ] * b[ 1 ] + a[ 2 ] * b[ 2 ];
	}

	/**
	 * Calculate the cross product of one vector with another. If either vector
	 * is invalid then Double.NaN is returned. If either vector is infinite then
	 * Double.NaN is returned.
	 *
	 * @param a The first vector
	 * @param b The second vector
	 * @return A new vector that is the cross product of the two vectors
	 */
	public static double[] cross( double[] a, double[] b ) {
		return of( a[ 1 ] * b[ 2 ] - a[ 2 ] * b[ 1 ], a[ 2 ] * b[ 0 ] - a[ 0 ] * b[ 2 ], a[ 0 ] * b[ 1 ] - a[ 1 ] * b[ 0 ] );
	}

	/**
	 * Calculate the sum of one vector with another.
	 *
	 * @param a The first vector
	 * @param b The second vector
	 * @return A new vector that is the sum of the two vectors
	 */
	public static double[] add( double[] a, double[] b ) {
		return of( a[ 0 ] + b[ 0 ], a[ 1 ] + b[ 1 ], a[ 2 ] + b[ 2 ] );
	}

	public static double[] plus( double[] a, double[] b ) {
		return add( a, b );
	}

	/**
	 * Calculate the difference of one vector with another.
	 *
	 * @param a The first vector
	 * @param b The second vector
	 * @return A new vector that is the difference of the two vectors
	 */
	public static double[] subtract( double[] a, double[] b ) {
		return of( a[ 0 ] - b[ 0 ], a[ 1 ] - b[ 1 ], a[ 2 ] - b[ 2 ] );
	}

	public static double[] minus( double[] a, double[] b ) {
		return subtract( a, b );
	}

	// NEXT Continue implementing vector functions

	private static double magnitude( double x, double y, double z ) {
		return Math.sqrt( x * x + y * y + z * z );
	}

}
