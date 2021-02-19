package com.avereon.curve.math;

public class Intersection3D  extends Intersection {

	protected Intersection3D( Type status, double[]... points ) {
		super( status, points );
	}

	public static Intersection3D intersectionLinePlane( double[] a, double[] b, double[] o, double[] n ) {
		double den = Vector.dot( Vector.subtract( b, a ), n );
		if( den == 0 ) return new Intersection3D( Type.NONE );

		double z = (Vector.dot( o, n ) - Vector.dot( a, n )) / den;
		return new Intersection3D( Type.INTERSECTION, Vector.add( a, Vector.scale( Vector.subtract( b, a ), z ) ) );
	}

}
