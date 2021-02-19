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
	 * Get the midpoint between two points.
	 *
	 * @param a The first point
	 * @param b The second point
	 * @return The midpoint
	 */
	public static double[] midpoint( final double[] a, final double[] b ) {
		return Point.of( 0.5 * (a[ 0 ] + b[ 0 ]), 0.5 * (a[ 1 ] + b[ 1 ]), 0.5 * (a[ 2 ] + b[ 2 ]) );
	}

	public static double[] midpoint( final double[] origin, final double xRadius, final double yRadius, final double rotate, final double start, final double extent ) {
		// Find the bisecting angle
		double a = start + 0.5 * extent;

		// Find the unit point at that angle
		double[] p = polarToCartesian( Point.of( 1, a ) );

		// Scale the point to the x and y radius
		p = Vector.scale( p, xRadius, yRadius, 1 );

		// Apply the rotation
		p = Vector.rotate( p, rotate );

		return Vector.add( p, origin );
	}

	/**
	 * Get the distance from the origin to a point.
	 *
	 * @param point The point to which to measure the distance
	 * @return The distance between the origin and the point
	 */
	public static double distance( double[] point ) {
		return distance( Point.ZERO, point );
	}

	/**
	 * Get the distance between two points.
	 *
	 * @param a The first point
	 * @param b The second point
	 * @return The distance between the two points
	 */
	public static double distance( double[] a, double[] b ) {
		return Vector.distance( a, b );
	}

	/**
	 * Get the angle between the x-axis and the point with the vertex at the
	 * origin.
	 *
	 * @param point The point used to measure the angle
	 * @return The angle
	 */
	public static double getAngle( final double[] point ) {
		return Math.atan2( point[ 1 ], point[ 0 ] );
	}

	public static double getAngle( final double[] v1, final double[] v2 ) {
		double a = getAngle( v1 );
		double b = getAngle( v2 );
		return b - a;
		//return Math.acos( Vector.dot( Vector.normalize( v1 ), Vector.normalize( v2 ) ) );
	}

	/**
	 * Get the angle between two vectors. This is geometrically equivalent to the
	 * angle made by placing the second vector at the end of the first vector and
	 * measuring the angle made between the two. The angle is in the range 0 to PI
	 * and is always positive.
	 *
	 * @param v1 The first vector
	 * @param v2 The second vector
	 * @return The angle between the vectors
	 */
	public static double getAbsAngle( final double[] v1, final double[] v2 ) {
		return Math.acos( Vector.dot( Vector.normalize( v1 ), Vector.normalize( v2 ) ) );
	}

	public static double getAngle( final double[] o, final double[] n, final double[] a, final double[] b ) {
		// TODO Finish reimplementing

		//		double[] r = Vector.normalize( Vector.cross( n, Vector.minus( a, o ) ) );
		//
		//		Transform transform = new Orientation( o, n, r ).getTargetToLocalTransform();
		//
		//		double[] lb = transform.timesXY( b ).normalize();
		//		double[] lo = Vector.of();
		//		double[] la = transform.timesXY( a ).normalize();
		//
		//		if( Vector.magnitude( Vector.plus( la, lb ) ) == 0.0 ) return Math.PI;
		//		return Math.acos( Vector.dot( la, lb ) ) * getSpin( lb, lo, la );

		return Double.NaN;
	}

	/**
	 * Get the distance between a line defined by parameter a and parameter b and
	 * the point p.
	 *
	 * @param a The first point on the line
	 * @param b The second point on the line
	 * @param p The point to which to get the distance
	 * @return The distance between the line and point
	 */
	public static double pointLineDistance( double[] a, double[] b, double[] p ) {
		double[] t = Vector.minus( b, a );
		double[] u = Vector.cross( t, Vector.minus( b, p ) );
		return Vector.magnitude( u ) / Vector.magnitude( t );
	}

	/**
	 * Get the distance between a plane and a point.
	 *
	 * @param origin The origin of the plane
	 * @param normal The normal of the plane
	 * @param p The point to which to determine the distance
	 * @return The distance between the plane and the point
	 */
	public static double pointPlaneDistance( double[] origin, double[] normal, double[] p ) {
		return Math.abs( Vector.dot( normal, Vector.minus( p, origin ) ) ) / Vector.magnitude( normal );
	}

	/**
	 * Get the distance between a line defined by parameter a and parameter b and
	 * the point p. If the point is outside of the line segment then Double.NaN is
	 * returned.
	 *
	 * @param a The first point on the line
	 * @param b The second point on the line
	 * @param p The point to which to get the distance
	 * @return The distance between the line and point or NaN if the point is outside the line segment
	 */
	public static double pointLineBoundDistance( double[] a, double[] b, double[] p ) {
		double[] pb = Vector.minus( p, b );
		double[] pa = Vector.minus( p, a );
		double[] ba = Vector.minus( b, a );
		double[] ab = Vector.minus( a, b );
		double anglea = Geometry.getAbsAngle( ba, pa );
		double angleb = Geometry.getAbsAngle( ab, pb );
		if( anglea > Constants.QUARTER_CIRCLE || angleb > Constants.QUARTER_CIRCLE ) return Double.NaN;

		double[] u = Vector.cross( ba, Vector.minus( b, p ) );
		return Vector.magnitude( u ) / Vector.magnitude( ba );
	}

	/**
	 * Get the distance between two lines defined by the points a-b and c-d.
	 *
	 * @param a First point on first line
	 * @param b Second point on first line
	 * @param c First point on second line
	 * @param d Second point on second line
	 * @return The distance between the two lines
	 */
	public static double lineLineDistance( double[] a, double[] b, double[] c, double[] d ) {
		double[] p = Vector.minus( b, a );
		double[] q = Vector.minus( d, c );
		double[] r = Vector.minus( c, a );
		double[] n = Vector.cross( p, q );
		return Math.abs( Vector.dot( n, r ) / Vector.magnitude( n ) );
	}

	/**
	 * Get the angle in radians between two lines defined by the points a-b and
	 * c-d. The result will be in the range 0-Math.PI (inclusive).
	 *
	 * @param a First point on first line
	 * @param b Second point on first line
	 * @param c First point on second line
	 * @param d Second point on second line
	 * @return The angle between the two lines
	 */
	public static double lineLineAngle( double[] a, double[] b, double[] c, double[] d ) {
		return getAbsAngle( Vector.minus( b, a ), Vector.minus( d, c ) );
	}

	/**
	 * Get the vector from a point to the nearest point on a line.
	 *
	 * @param a The first point on the line
	 * @param b The second point on the line
	 * @param p The point from which to get the vector
	 * @return The vector from the point to the line
	 */
	public static double[] vectorToLine( double[] a, double[] b, double[] p ) {
		double[] t = Vector.minus( b, a );
		double[] u = Vector.cross( t, Vector.minus( b, p ) );
		if( Vector.magnitude( u ) == 0 ) return Vector.ZERO;
		return Vector.scale( Vector.normalize( Vector.cross( u, t ) ), Vector.magnitude( u ) / Vector.magnitude( t ) );
	}

	/**
	 * Get the vector between a point and the nearest point on the plane.
	 *
	 * @param origin The origin of the plane
	 * @param normal The normal vector of the plane
	 * @param point The point from which to get the vector
	 * @return The vector from the point to the plane
	 */
	public static double[] vectorToPlane( double[] origin, double[] normal, double[] point ) {
		double z = Vector.dot( normal, Vector.minus( point, origin ) ) / Vector.magnitude( normal );
		return Vector.scale( Vector.normalize( Vector.reverse( normal ) ), z );
	}

	/**
	 * Get the nearest point in a set of points to the specified point.
	 *
	 * @param points The set of points to check
	 * @param point The point from which to check
	 * @return The point nearest to the specified point
	 */
	public static double[] nearest( double[][] points, double[] point ) {
		double distance = Double.MAX_VALUE;
		double checkDistance;
		double[] nearest = null;
		for( double[] test : points ) {
			checkDistance = distance( test, point );
			if( checkDistance < distance ) {
				distance = checkDistance;
				nearest = test;
			}
		}
		return nearest;
	}

	/**
	 * Get the nearest point on a line from a point.
	 *
	 * @param a The first point on the line
	 * @param b The second point on the line
	 * @param p The point from which to get the nearest line point
	 * @return The nearest point on the line from the specified point
	 */
	public static double[] nearestLinePoint( double[] a, double[] b, double[] p ) {
		return Vector.add( p, vectorToLine( a, b, p ) );
	}

	/**
	 * Determine if all the points are collinear with a line defined by point a
	 * and point b. It is important to note that this method is only accurate if
	 * all the points are in between points a and b. All the points must lie
	 * within POINT_TOLERANCE.
	 *
	 * @param a The first point on the line
	 * @param b The second point on the line
	 * @param points The points to check
	 * @return True if all the points are collinear, false otherwise
	 */
	public static boolean areCollinear( double[] a, double[] b, double[]... points ) {
		for( double[] vector : points ) {
			if( pointLineDistance( a, b, vector ) >= Constants.RESOLUTION_LENGTH ) return false;
		}
		return true;
	}

	/**
	 * Determine if all the points are coplanar with the plane defined by the
	 * orientation.
	 *
	 * @param orientation The orientation used to define the plane
	 * @param points The points to check
	 * @return True if all points are coplanar, false otherwise
	 */
	public static boolean areCoplanar( Orientation orientation, double[]... points ) {
		return areCoplanar( orientation.getOrigin(), orientation.getNormal(), points );
	}

	/**
	 * Determine if all the points are coplanar with the plane defined by the
	 * origin and normal.
	 *
	 * @param origin The plane origin
	 * @param normal The plane normal vector
	 * @param points The points to check
	 * @return True if all the points are coplanar with the plane, false otherwise
	 */
	public static boolean areCoplanar( double[] origin, double[] normal, double[]... points ) {
		return areCoplanar( origin, normal, Constants.DISTANCE_TOLERANCE, points );
	}

	/**
	 * Determine if all the points are coplanar with the plane defined by the
	 * origin and normal. All the points must line within the tolerance from the
	 * plane. It is generally recommended that
	 * <code>areCoplanar( Vector, Vector, Vector...)</code> be used instead.
	 *
	 * @param origin The plane origin
	 * @param normal The plane normal vector
	 * @param tolerance The allowed distance-from-plane tolerance
	 * @param points The points to check
	 * @return True if all the points are within tolerance of the plane, false otherwise
	 */
	public static boolean areCoplanar( double[] origin, double[] normal, double tolerance, double[]... points ) {
		for( double[] vector : points ) {
			if( pointPlaneDistance( origin, normal, vector ) > tolerance ) return false;
		}
		return true;
	}

	/**
	 * Determine if two points can be considered the same by checking if the
	 * distance between the two points is smaller than RESOLUTION_LENGTH.
	 *
	 * @param point1 First point
	 * @param point2 Second point
	 * @return True if the two points are considered the same, false otherwise
	 */
	public static boolean areSamePoint( double[] point1, double[] point2 ) {
		return distance( point1, point2 ) < Constants.RESOLUTION_LENGTH;
	}

	/**
	 * Determine if two angles can be considered the same by checking if the
	 * difference between the two angles is smaller than RESOLUTION_ANGLE.
	 *
	 * @param angle1 First angle
	 * @param angle2 Second angle
	 * @return True if the two angles are considered the same, false otherwise
	 */
	public static boolean areSameAngle( double angle1, double angle2 ) {
		if( angle1 == angle2 ) return true;
		return Math.abs( angle1 - angle2 ) < Constants.RESOLUTION_ANGLE;
	}

	/**
	 * Determine if two vectors can be considered parallel by checking if the
	 * angle between the two vectors is smaller than ANGLE_TOLERANCE.
	 *
	 * @param vector1 The first vector
	 * @param vector2 The second vector
	 * @return True if the vectors are considered parallel, false otherwise
	 */
	public static boolean areParallel( double[] vector1, double[] vector2 ) {
		return distance( Vector.normalize( vector1 ), Vector.normalize( vector2 ) ) < Constants.RESOLUTION_NORMAL;
	}

	/**
	 * Determine if two vectors can be considered anti-parallel by checking if the
	 * angle between the two vectors, with one reversed, is smaller than
	 * ANGLE_TOLERANCE.
	 *
	 * @param vector1 The first vector
	 * @param vector2 The second vector
	 * @return True if the vectors are considered parallel, false otherwise
	 */
	public static boolean areAntiParallel( double[] vector1, double[] vector2 ) {
		return distance( Vector.normalize( vector1 ), Vector.normalize( Vector.reverse( vector2 ) ) ) < Constants.RESOLUTION_NORMAL;
	}

	/**
	 * Use the x and y coordinates to determine if the three points are in
	 * counter-clockwise, straight, or clockwise order.
	 *
	 * @param a First point
	 * @param b Second point
	 * @param c Third point
	 * @return Minus one if CCW, zero if straight, and one if CW.
	 */
	public static int getSpin( double[] a, double[] b, double[] c ) {
		double[] ab = Vector.minus( a, b );
		double[] cb = Vector.minus( c, b );
		double ccw = Vector.cross( cb, ab )[ 2 ];

		if( ccw == 0.0 || ccw == -0.0 ) return 0;
		return ccw < 0 ? -1 : 1;
	}

	/**
	 * Determine if two line segments, A and B, intersect given the respective end
	 * points.
	 *
	 * @param a1 Line A point 1
	 * @param a2 Line A point 2
	 * @param b1 Line B point 1
	 * @param b2 Line B point 2
	 * @return True if the line segments intersect, false otherwise
	 */
	public static boolean areIntersecting( double[] a1, double[] a2, double[] b1, double[] b2 ) {
		return (getSpin( b1, b2, a1 ) != getSpin( b1, b2, a2 )) & (getSpin( a1, a2, b1 ) != getSpin( a1, a2, b2 ));
	}

	public static double[] getNormal( double[] a, double[] b, double[] c ) {
		return Vector.cross( Vector.minus( a, b ), Vector.minus( b, c ) );
	}

	public static double determinant( double[] a, double[] b, double[] c ) {
		return Arithmetic.determinant( a[ 0 ], a[ 1 ], a[ 2 ], b[ 0 ], b[ 1 ], b[ 2 ], c[ 0 ], c[ 1 ], c[ 2 ] );
	}

	/**
	 * Convert a point in cartesian coordinates [x,y,z] to polar coordinates [r,a,z].
	 *
	 * @param point The point to convert
	 * @return The point in polar coordinates
	 */
	public static double[] cartesianToPolar( final double[] point ) {
		double r = distance( point );
		double a = Math.atan2( point[ 1 ], point[ 0 ] );
		return Point.of( r, a, point[ 2 ] );
	}

	/**
	 * Convert a point in polar coordinates [r,a,z] to cartesian coordinates [x,y,z].
	 *
	 * @param point The point to convert
	 * @return The point in cartesian coordinates
	 */
	public static double[] polarToCartesian( final double[] point ) {
		double x = point[ 0 ] * Math.cos( point[ 1 ] );
		double y = point[ 0 ] * Math.sin( point[ 1 ] );
		return new double[]{ x, y, point[ 2 ] };
	}

	/**
	 * Convert a point in cartesian coordinates [x,y,z] to polar coordinates [r,a,z]
	 * with the angle in degrees instead of radians.
	 *
	 * @param point The point to convert
	 * @return The point in polar coordinates
	 */
	public static double[] cartesianToPolarDegrees( final double[] point ) {
		double[] v = cartesianToPolar( point );
		return Point.of( v[ 0 ], Math.toDegrees( v[ 1 ] ), v[ 2 ] );
	}

	/**
	 * Convert a point in polar coordinates [r,a,z] to cartesian coordinates [x,y,z]
	 * with the angle in degrees instead of radians.
	 *
	 * @param point The point to convert
	 * @return The point in cartesian coordinates
	 */
	public static double[] polarDegreesToCartesian( final double[] point ) {
		return polarToCartesian( Point.of( point[ 0 ], Math.toRadians( point[ 1 ] ), point[ 2 ] ) );
	}

}
