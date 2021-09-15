package com.avereon.curve.math;

public class Intersection3D extends Intersection {

	protected Intersection3D( Type status, double[]... points ) {
		super( status, points );
	}

	public static Intersection3D intersectLinePlane( double[] a, double[] b, double[] o, double[] n ) {
		double den = Vector.dot( Vector.subtract( b, a ), n );
		if( den == 0 ) return new Intersection3D( Type.NONE );

		double z = (Vector.dot( o, n ) - Vector.dot( a, n )) / den;
		return new Intersection3D( Type.INTERSECTION, Vector.add( a, Vector.scale( Vector.subtract( b, a ), z ) ) );
	}

	public static Intersection3D intersectPlanePlane( double[] ao, double[] an, double[] bo, double[] bn ) {
		// Determine the vector parallel to the intersection line
		double[] v = Vector.cross( an, bn );

		// TODO Determine a specific point on the line

		// TODO Implement plane-plane intersection
		return new Intersection3D( Type.NONE );
	}

}
