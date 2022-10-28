package com.avereon.curve.math;

/**
 * NOTE: All methods in this class assume three-dimensional vectors. If a
 * smaller array is used then an {@link ArrayIndexOutOfBoundsException} will
 * likely be thrown. If a larger array is used the excess values are ignored.
 */
public class Vector extends Point {

	public static final double[] UNIT_X = of( 1, 0, 0 );

	public static final double[] UNIT_Y = of( 0, 1, 0 );

	public static final double[] UNIT_Z = of( 0, 0, 1 );

	public static double distance( double[] a, double[] b ) {
		return magnitude( b[ 0 ] - a[ 0 ], b[ 1 ] - a[ 1 ], b[ 2 ] - a[ 2 ] );
	}

	/**
	 * Calculate the magnitude of the vector. If the vector is invalid then Double.NaN is returned. If the vector is infinite then Double.POSITIVE_INFINITY is returned.
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
	 * Calculate the dot product of one vector with another. If either vector is invalid then Double.NaN is returned. If either vector is infinite then Double.POSITIVE_INFINITY is returned.
	 *
	 * @param a The first vector
	 * @param b The second vector
	 * @return The dot product of the two vectors
	 */
	public static double dot( double[] a, double[] b ) {
		return a[ 0 ] * b[ 0 ] + a[ 1 ] * b[ 1 ] + a[ 2 ] * b[ 2 ];
	}

	/**
	 * Calculate the cross product of one vector with another. If either vector is invalid then Double.NaN is returned. If either vector is infinite then Double.NaN is returned.
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

	/**
	 * Multiply a vector with a scalar.
	 *
	 * @param v The vector to scale
	 * @param scale The scalar value
	 * @return A new vector that is multiplied by the scalar
	 */
	public static double[] scale( double[] v, double scale ) {
		return of( v[ 0 ] * scale, v[ 1 ] * scale, v[ 2 ] * scale );
	}

	/**
	 * Multiply a vector with scalars for the X and Y coordinates.
	 *
	 * @param v The vector to scale
	 * @param scaleX The X scalar value
	 * @param scaleY The Y scalar value
	 * @return A new vector that is multiplied by the scalars
	 */
	public static double[] scale( double[] v, double scaleX, double scaleY ) {
		return of( v[ 0 ] * scaleX, v[ 1 ] * scaleY, v[ 2 ] );
	}

	/**
	 * Multiply a vector with scalars for each coordinate.
	 *
	 * @param v The vector to scale
	 * @param scaleX The x coordinate scalar
	 * @param scaleY The y coordinate scalar
	 * @param scaleZ The z coordinate scalar
	 * @return A new vector that is multiplied by the scale for each coordinate
	 */
	public static double[] scale( double[] v, double scaleX, double scaleY, double scaleZ ) {
		return of( v[ 0 ] * scaleX, v[ 1 ] * scaleY, v[ 2 ] * scaleZ );
	}

	/**
	 * Normalize a vector.
	 *
	 * @return A new vector that is the normalized vector
	 */
	public static double[] normalize( double[] v ) {
		double coefficient = 1 / magnitude( v );
		return of( coefficient * v[ 0 ], coefficient * v[ 1 ], coefficient * v[ 2 ] );
	}

	/**
	 * Linear interpolation. This is a more efficient version of
	 * <code>Vector.add( a, Vector.scale( Vector.subtract( b, a), t ) )</code>.
	 *
	 * @param a The source vector
	 * @param b The target vector
	 * @param t The interpolation ratio
	 * @return A new point that is the interpolation along the vector a to b
	 */
	public static double[] lerp( double[] a, double[] b, double t ) {
		if( isInfinite( a ) || isInfinite( b ) ) return INFINITY;
		return of( a[ 0 ] + t * (b[ 0 ] - a[ 0 ]), a[ 1 ] + t * (b[ 1 ] - a[ 1 ]), a[ 2 ] + t * (b[ 2 ] - a[ 2 ]) );
	}

	public static boolean lte( double[] a, double[] b ) {
		return (a[ 0 ] <= b[ 0 ] && a[ 1 ] <= b[ 1 ] && a[ 2 ] <= b[ 2 ]);
	}

	public static boolean gte( double[] a, double[] b ) {
		return (a[ 0 ] >= b[ 0 ] && a[ 1 ] >= b[ 1 ] && a[ 2 ] >= b[ 2 ]);
	}

	public static double[] rotate( double[] v, double a ) {
		return Geometry.polarToCartesian( Point.of( Vector.magnitude( v ), Geometry.getAngle( v ) + a ) );
	}

	private static double magnitude( double x, double y, double z ) {
		return Math.sqrt( x * x + y * y + z * z );
	}

}
