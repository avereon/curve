package com.avereon.curve.math;

public class Arithmetic {

	public static final int EXTRA_HIGH_PRECISION_DIGITS = 18;
	public static final int HIGH_PRECISION_DIGITS = 15;
	public static final int DEFAULT_PRECISION_DIGITS = 12;
	public static final int LOW_PRECISION_DIGITS = 9;
	public static final int EXTRA_LOW_PRECISION_DIGITS = 6;

	public static final double EXTRA_HIGH_PRECISION = 1.0 / Math.pow( 10, EXTRA_HIGH_PRECISION_DIGITS );
	public static final double HIGH_PRECISION = 1.0 / Math.pow( 10, HIGH_PRECISION_DIGITS );
	public static final double DEFAULT_PRECISION = 1.0 / Math.pow( 10, DEFAULT_PRECISION_DIGITS );
	public static final double LOW_PRECISION = 1.0 / Math.pow( 10, LOW_PRECISION_DIGITS );
	public static final double EXTRA_LOW_PRECISION = 1.0 / Math.pow( 10, EXTRA_LOW_PRECISION_DIGITS );

	private static final double[] factors = new double[ DEFAULT_PRECISION_DIGITS << 1 ];

	static {
		for( int index = 0; index < factors.length; index++ ) {
			factors[ index ] = Math.pow( 10, index );
		}
	}

	public static double sign( double number ) {
		return number < 0 ? -1.0 : 1.0;
	}

	public static double trim( double value ) {
		return trim( value, DEFAULT_PRECISION_DIGITS );
	}

	public static double trim( double value, int digits ) {
		return Math.floor( value * factors[ digits ] + 0.5 ) / factors[ digits ];
	}

	public static double round( double value ) {
		return Math.floor( value + 0.5 );
	}

	public static double nearest( double value, double precision ) {
		return round( value / precision ) * precision;
	}

	public static double nearestAbove( double value, double precision ) {
		double newValue = nearest( value, precision );
		return newValue >= value ? newValue : newValue + precision;
	}

	public static double nearestBelow( double value, double precision ) {
		double newValue = nearest( value, precision );
		return newValue <= value ? newValue : newValue - precision;
	}

	/**
	 * Evaluate a 2x2 matrix determinant.
	 *
	 * @param a1 Element a1
	 * @param a2 Element a2
	 * @param b1 Element b1
	 * @param b2 Element b2
	 * @return The determinant for a 2x2 matrix
	 */
	public static double determinant( double a1, double a2, double b1, double b2 ) {
		return a1 * b2 - a2 * b1;
	}

	/**
	 * Evaluate a 3x3 matrix determinant.
	 *
	 * @param a1 Element a1
	 * @param a2 Element a2
	 * @param a3 Element a3
	 * @param b1 Element b1
	 * @param b2 Element b2
	 * @param b3 Element b3
	 * @param c1 Element c1
	 * @param c2 Element c2
	 * @param c3 Element c3
	 * @return The determinant for a 2x2 matrix
	 */
	public static double determinant( double a1, double a2, double a3, double b1, double b2, double b3, double c1, double c2, double c3 ) {
		double a = a1 * b2 * c3;
		double b = a1 * b3 * c2;
		double c = a2 * b1 * c3;
		double d = a2 * b3 * c1;
		double e = a3 * b1 * c2;
		double f = a3 * b2 * c1;
		return a - b - c + d + e - f;
	}

	/**
	 * Compute the binomial coefficient 'n choose k'.
	 *
	 * @param n The number of elements to choose from
	 * @param k The number of elements to choose
	 * @return The binomial coefficient 'n choose k'
	 */
	public static int bc( int n, int k ) {
		if( k > n ) return 0;
		if( k == 0 || k == n ) return 1;
		return bc( n - 1, k - 1 ) + bc( n - 1, k );
	}

	public static double bchi( int k ) {
		int twok = k + k;

		int b = bc( twok, k );
		double n = Math.pow( -1, k + 1 );
		double d = Math.pow( 2, twok ) * (twok - 1);
		double c = n / d;

		return b * c;
	}

	/**
	 * Calculate the nth factorial.
	 *
	 * @param n The factorial index
	 * @return The nth factorial
	 */
	public static int factorial( int n ) {
		if( n == 0 ) return 1;
		return n * factorial( n - 1 );
	}

}
