package com.avereon.curve.math;

public class Geometry {

	/**
	 * Get the square of the value. This is mainly a convenience method for
	 * readability.
	 *
	 * @param value The value to square
	 * @return The square of the value
	 */
	public static double square( double value ) {
		return value * value;
	}

	/**
	 * Get the angle between the x-axis and the point with the vertex at the
	 * origin.
	 *
	 * @param point The point used to measure the angle
	 * @return The angle
	 */
	public static double getAngle( final double[] point ) {
		return Math.atan2( point[1], point[0] );
	}

	public static double getAngle( final double[] o, final double[] n, final double[] a, final double[] b ) {
		// TODO Continue reimplementing
		//double[] r = Vector.normalize( Vector.cross( n, Vector.minus( a, o ) ) );

//		Vector r = n.cross( a.minus( o ) ).normalize();
//		Transform transform = new Orientation( o, n, r ).getTargetToLocalTransform();
//
//		Vector lb = transform.timesXY( b ).normalize();
//		Vector lo = new Vector();
//		Vector la = transform.timesXY( a ).normalize();
//
//		if( la.plus( lb ).magnitude() == 0.0 ) return Math.PI;
//		return Math.acos( la.dot( lb ) ) * getSpin( lb, lo, la );
		return Double.NaN;
	}

	public static double[] midpoint( final double[] a, final double[] b ) {
		return Point.of( 0.5 * (a[ 0 ] + b[ 0 ]), 0.5 * (a[ 1 ] + b[ 1 ]), 0.5 * (a[ 2 ] + b[ 2 ]) );
	}

	public static double[] cartesianToPolar( final double[] point ) {
		double r = Vector.distance( Point.ZERO, point );
		double a = Math.atan2( point[ 1 ], point[ 0 ] );
		return Point.of( r, a, 0 );
	}

	public static double[] polarToCartesian( final double[] point ) {
		double x = point[ 0 ] * Math.cos( point[ 1 ] );
		double y = point[ 0 ] * Math.sin( point[ 1 ] );
		return new double[]{ x, y, 0 };
	}

	public static double[] cartesianToPolarDegrees( final double[] point ) {
		double[] v = cartesianToPolar( point );
		v[ 1 ] = Math.toDegrees( v[ 1 ] );
		return v;
	}

	public static double[] polarDegreesToCartesian( final double[] point ) {
		return polarToCartesian( Point.of( point[ 0 ], Math.toRadians( point[ 1 ] ), point[ 2 ] ) );
	}

}
