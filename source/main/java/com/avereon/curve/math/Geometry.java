package com.avereon.curve.math;

public class Geometry {

	public static double[] cartesianToPolar( double[] point ) {
		double r = Vector.distance( Point.ZERO, point );
		double a =  Math.atan2( point[ 1 ], point[ 0 ]  );
		return Point.of( r, a, 0 );
	}

	public static double[] polarToCartesian( double[] point ) {
		double x = point[ 0 ] * Math.cos(  point[ 1 ]  );
		double y = point[ 0 ] * Math.sin(  point[ 1 ]  );
		return new double[]{ x, y, 0 };
	}

	public static double[] cartesianToPolarDegrees( double[] point ) {
		double[] v = cartesianToPolar( point );
		v[1] = Math.toDegrees( v[1] );
		return v;
	}

	public static double[] polarDegreesToCartesian( double[] point ) {
		return polarToCartesian( Point.of( point[0], Math.toRadians( point[1] ), point[2]) );
	}

}
