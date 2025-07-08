package com.avereon.curve.math;

import org.tinyspline.BSpline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.avereon.curve.math.Constants.FULL_CIRCLE;

/**
 * A Bézier curve reference: <a href="https://pomax.github.io/bezierinfo">https://pomax.github.io/bezierinfo</a>
 */
public class Geometry {

	/**
	 * Get the square of the value. This is mainly a convenience method for readability.
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

	/**
	 * Get the midpoint of an ellipse arc.
	 *
	 * @param origin The center of the ellipse arc
	 * @param xRadius The x radius of the ellipse arc
	 * @param yRadius The y radius of the ellipse arc
	 * @param rotate The rotation of the ellipse arc
	 * @param start The start angle of the ellipse arc
	 * @param extent The extent angle of the ellipse arc
	 * @return The midpoint of the ellipse arc
	 */
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
	 * Get the total distance between a lit of points.
	 *
	 * @param points The list of points
	 * @return The total distance between all points
	 */
	public static double length( double[]... points ) {
		if( points.length == 0 ) return Double.NaN;
		if( points.length == 1 ) return 0.0;
		double length = 0;
		double[] last = points[ 0 ];
		for( int index = 1; index < points.length; index++ ) {
			double[] next = points[ index ];
			length += distance( last, next );
			last = next;
		}
		return length;
	}

	/**
	 * Determine the bounds of a set of points.
	 *
	 * @param points The set of points
	 * @return The bounds of the points
	 */
	public static double[][] bounds( double[]... points ) {
		if( points.length == 0 ) return new double[][]{ Point.ZERO, Point.ZERO };
		if( points.length == 1 ) return new double[][]{ points[ 0 ], points[ 0 ] };
		double[] min = new double[] { points[0][0], points[0][1], points[0][2] };
		double[] max = new double[] { points[0][0], points[0][1], points[0][2] };
		for( int index = 1; index < points.length; index++ ) {
			double[] point = points[ index ];
			min[ 0 ] = Math.min( min[ 0 ], point[ 0 ] );
			min[ 1 ] = Math.min( min[ 1 ], point[ 1 ] );
			max[ 0 ] = Math.max( max[ 0 ], point[ 0 ] );
			max[ 1 ] = Math.max( max[ 1 ], point[ 1 ] );
		}
		return new double[][]{ min, max };
	}

	/**
	 * Get the angle between the x-axis and the point with the vertex at the origin.
	 *
	 * @param point The point used to measure the angle
	 * @return The angle
	 */
	public static double getAngle( final double[] point ) {
		return Math.atan2( point[ 1 ], point[ 0 ] );
	}

	/**
	 * Get the angle between two vectors. This is the angle needed to go from the
	 * angle of the first vector to the angle of the second vector such that v1 +
	 * getAngle( v1, v2 ) = v2
	 *
	 * @param v1 The start vector
	 * @param v2 The final vector
	 * @return The angle between the two vectors in radians
	 */
	public static double getAngle( final double[] v1, final double[] v2 ) {
		//return clampAngle( Math.atan2( v2[0],v2[1] ) - Math.atan2( v1[0],v1[1] ) );
		return getAngle( v1[ 0 ], v1[ 1 ], v2[ 0 ], v2[ 1 ] );
	}

	public static double getAngle( final double ux, final double uy, final double vx, final double vy ) {
		var dot = ux * vx + uy * vy;
		var modulus = Math.sqrt( (ux * ux + uy * uy) * (vx * vx + vy * vy) );
		var radians = Math.acos( dot / modulus );
		if( ux * vy - uy * vx < 0.0 ) radians = -radians;
		return radians;
	}

	/**
	 * Convert any angle into a normalized angle, an angle between -PI (not
	 * inclusive) and PI (inclusive).
	 *
	 * @param a The angle to normalize
	 * @return The normalized angle
	 */
	public static double clampAngle( double a ) {
		a %= FULL_CIRCLE;
		if( a <= -Constants.HALF_CIRCLE ) a += FULL_CIRCLE;
		if( a > Constants.HALF_CIRCLE ) a -= FULL_CIRCLE;
		return a;
	}

	/**
	 * Get the angle between a line and a plane. The angle is in the range 0 to PI and is always positive.
	 *
	 * @param o The plane origin
	 * @param n The plane normal
	 * @param a The first point on the line
	 * @param b The other point on the line
	 * @return The angle between the plane and the line
	 */
	public static double getAngle( final double[] o, final double[] n, final double[] a, final double[] b ) {
		double[] r = Vector.normalize( Vector.cross( n, Vector.minus( a, o ) ) );

		Transform transform = new Orientation( o, n, r ).getWorldToLocalTransform();

		double[] lb = Vector.normalize( transform.applyXY( b ) );
		double[] lo = Vector.of();
		double[] la = Vector.normalize( transform.applyXY( a ) );

		if( Vector.magnitude( Vector.plus( la, lb ) ) == 0.0 ) return Math.PI;
		return Math.acos( Vector.dot( la, lb ) ) * getSpin( lb, lo, la );
	}

	/**
	 * Computes the phase theta by computing an arc tangent of y/x in the range of
	 * 0 to 2pi.
	 *
	 * @param x
	 * @param y
	 * @return the theta component of the point (r, theta) in polar coordinates
	 *         that corresponds to the point (x, y) in Cartesian coordinates.
	 */
	public static double theta( double x, double y ) {
		double theta = Math.atan2( y, x );
		if( theta < 0 ) theta += Constants.TWO_PI;
		return theta;
	}

	/**
	 * Computes the phase theta by computing an arc tangent of y/x in the range of
	 * 0 to 2pi using point a as the origin.
	 *
	 * @param a The origin.
	 * @param b The point to calculate the angle for.
	 * @return the theta component of the point (r, theta) in polar coordinates
	 *         that corresponds to the point (b.x-a.x, b.y-a.y) in Cartesian
	 *         coordinates.
	 */
	public static double theta( double[] a, double[] b ) {
		return theta( b[0] - a[0], b[1] - a[1] );
	}

	/**
	 * Get the angle defined by points a, b and c with b at the vertex.
	 *
	 * @param a Point a
	 * @param b Point b
	 * @param c Point c
	 * @return The angle between the points
	 */
	public static double pointAngle( final double[] a, final double[] b, final double[] c ) {
		return clampAngle( getAngle( Vector.subtract( c, b ) ) - getAngle( Vector.subtract( a, b ) ) );
	}

	/**
	 * Get the angle between two vectors. This is geometrically equivalent to the angle made by placing the second vector at the end of the first vector and
	 * measuring the angle made between the two. The angle is in the range 0 to PI and is
	 * always positive.
	 *
	 * @param v1 The first vector
	 * @param v2 The second vector
	 * @return The angle between the vectors
	 */
	public static double getAbsAngle( final double[] v1, final double[] v2 ) {
		return Math.acos( Vector.dot( Vector.normalize( v1 ), Vector.normalize( v2 ) ) );
	}

	/**
	 * Get the distance between a point p and the line defined by parameter a and parameter b.
	 *
	 * @param p The point to which to get the distance
	 * @param a The first point on the line
	 * @param b The second point on the line
	 * @return The distance between the point and line
	 */
	public static double pointLineDistance( double[] p, double[] a, double[] b ) {
		double[] t = Vector.minus( b, a );
		double[] u = Vector.cross( t, Vector.minus( b, p ) );
		return Vector.magnitude( u ) / Vector.magnitude( t );
	}

	public static double pointLineOffset( double[] p, double[] a, double[] b ) {
		double[] s = Vector.minus( b, p );
		double[] t = Vector.minus( b, a );
		return -Vector.crossProduct( t, s ) / Vector.magnitude( t );
	}

	/**
	 * Get the distance between a line defined by parameter a and parameter b and the point p.
	 *
	 * @param a The first point on the line
	 * @param b The second point on the line
	 * @param p The point to which to get the distance
	 * @return The distance between the line and point
	 */
	public static double linePointDistance( double[] a, double[] b, double[] p ) {
		return pointLineDistance( p, a, b );
	}

	/**
	 * Get the distance between a plane and a point.
	 *
	 * @param p The point to which to determine the distance
	 * @param origin The origin of the plane
	 * @param normal The normal of the plane
	 * @return The distance between the plane and the point
	 */
	public static double pointPlaneDistance( double[] p, double[] origin, double[] normal ) {
		return Math.abs( pointPlaneOffset( p, origin, normal ) );
	}

	/**
	 * Get the distance between a plane and a point.
	 *
	 * @param origin The origin of the plane
	 * @param normal The normal of the plane
	 * @param p The point to which to determine the distance
	 * @return The distance between the plane and the point
	 */
	public static double pointPlaneOffset( double[] p, double[] origin, double[] normal ) {
		return Vector.dot( normal, Vector.minus( p, origin ) ) / Vector.magnitude( normal );
	}

	/**
	 * Get the distance between a line defined by parameter a and parameter b and the point p.
	 * If the point is beyond the line segment then Double.NaN is returned.
	 *
	 * @param p The point to which to get the distance
	 * @param a The first point on the line
	 * @param b The second point on the line
	 * @return The distance between the line and point or NaN if the point is outside the line segment
	 */
	public static double pointLineBoundDistance( double[] p, double[] a, double[] b ) {
		double[] pb = Vector.minus( p, b );
		double[] pa = Vector.minus( p, a );
		double[] ba = Vector.minus( b, a );
		double[] ab = Vector.minus( a, b );
		double anglea = Geometry.getAbsAngle( ba, pa );
		double angleb = Geometry.getAbsAngle( ab, pb );
		if( anglea > Constants.QUARTER_CIRCLE || angleb > Constants.QUARTER_CIRCLE ) return Double.NaN;

		return pointLineDistance( p, a, b );
	}

	public static double pointLineBoundOffset( double[] p, double[] a, double[] b ) {
		double[] line = Vector.subtract( b, a );
		double[] c = Vector.add( a, -line[ 1 ], line[ 0 ] );
		double[] d = Vector.add( b, -line[ 1 ], line[ 0 ] );

		//		Point2D line = b.subtract( a );
		//		Point2D c = a.add( -line.getY(), line.getX() );
		//		Point2D d = b.add( -line.getY(), line.getX() );
		double dl = pointLineOffset( p, a, c );
		double dr = pointLineOffset( p, b, d );
		return (dl < 0 && dr > 0) ? pointLineOffset( p, a, b ) : Double.NaN;
	}

	/**
	 * Get the distance between a point and an arc, including elliptic arcs. If
	 * the point angle is between start and extent then the distance is the
	 * distance to the ellipse, otherwise, the distance is the distance to the
	 * closest endpoint.
	 *
	 * @param p The point to which to get the distance
	 * @param c The center of the arc
	 * @param r The radii of the arc
	 * @param rotate The rotate angle of the arc
	 * @param start The start angle of the arc
	 * @param extent The extent angle of the arc
	 * @return The distance between the point and the arc
	 */
	public static double pointArcDistance( double[] p, double[] c, double[] r, double rotate, double start, double extent ) {
		// If the point is between start and extent then the distance is the distance to the ellipse
		double angle = ellipseAngle( c, r, rotate, p );
		if( angle >= (start - rotate) && angle <= (start + extent - rotate) ) {
			return distance( p, ellipsePoint( c, r, rotate, angle ) );
		}

		// Otherwise, the distance is the distance to the closest endpoint
		double[][] endpoints = arcEndPoints( c, r, rotate, start, extent );
		return distance( p, nearest( p, endpoints ) );
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
	 * Get the angle in radians between two lines defined by the points a-b and c-d.
	 * The result will be in the range 0-Math.PI (inclusive).
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
	 * Get the point on an ellipse for the specified angle. The point is initially
	 * calculated with the ellipse at 0,0 and no rotation. The point is then
	 * translated according to the ellipse origin and rotated according to the
	 * ellipse rotation.
	 *
	 * @param origin The center of the ellipse
	 * @param radii The radii of the ellipse
	 * @param rotate The rotation angle of the ellipse
	 * @param angle The angle for which to compute the point
	 * @return The point at the angle on the ellipse
	 */
	public static double[] ellipsePoint( double[] origin, double[] radii, double rotate, double angle ) {
		double[] p = polarToCartesian( Vector.of( 1, angle ) );
		p = Vector.scale( p, radii[ 0 ], radii[ 1 ] );
		p = Vector.rotate( p, rotate );
		return Point.of( origin[ 0 ] + p[ 0 ], origin[ 1 ] + p[ 1 ], origin[ 2 ] + p[ 2 ] );
	}

	public static double ellipseAngle( double[] origin, double[] radii, double rotate, double[] point ) {
		return ellipseAngle( origin, radii[ 0 ], radii[ 1 ], rotate, point );
	}

	/**
	 * Get the angle for a point on an ellipse. Technically the point does not
	 * even need to be on the ellipse, the angle will still be computed.
	 *
	 * @param origin The center of the ellipse
	 * @param xRadius The x radius of the ellipse
	 * @param yRadius The y radius of the ellipse
	 * @param rotate The rotation angle of the ellipse
	 * @param point The point for which to compute the angle
	 * @return The angle of the point on the ellipse
	 */
	public static double ellipseAngle( double[] origin, double xRadius, double yRadius, double rotate, double[] point ) {
		double[] p = Point.of( point[ 0 ] - origin[ 0 ], point[ 1 ] - origin[ 1 ] );
		p = Vector.rotate( p, -rotate );
		p = Vector.scale( p, 1 / xRadius, 1 / yRadius );
		return clampAngle( Math.atan2( p[ 1 ], p[ 0 ] ) );
	}

	/**
	 * Compute the bounds of an ellipse.
	 *
	 * @param origin The ellipse origin
	 * @param radii The ellipse radii
	 * @param rotate The ellipse rotation
	 * @return The bounds of the ellipse as two points
	 */
	public static double[][] ellipseBounds( double[] origin, double[] radii, double rotate ) {
		double[] e = ellipseE( ellipseUV( radii, rotate ) );
		return new double[][]{ Vector.subtract( origin, e ), Vector.add( origin, e ) };
	}

	private static double[][] ellipseUV( double[] radii, double angle ) {
		double sin = Math.sin( angle );
		double cos = Math.cos( angle );
		double[] u = Point.of( cos * radii[ 0 ], sin * radii[ 0 ] );
		double[] v = Point.of( sin * radii[ 1 ], cos * radii[ 1 ] );
		return new double[][]{ u, v };
	}

	private static double[] ellipseE( double[][] uv ) {
		double[] usqr = Vector.multiply( uv[ 0 ], uv[ 0 ] );
		double[] vsqr = Vector.multiply( uv[ 1 ], uv[ 1 ] );
		double[] esqr = Vector.add( usqr, vsqr );
		return Point.of( Math.sqrt( esqr[ 0 ] ), Math.sqrt( esqr[ 1 ] ) );
	}

	private static double[] ellipseF( double[] radii, double angle ) {
		double n = (radii[ 0 ] * radii[ 0 ] + radii[ 1 ] * radii[ 1 ]);
		double d = (radii[ 1 ] * radii[ 1 ] - radii[ 0 ] * radii[ 0 ]);
		double m = (d * d) / n;
		System.out.println( "n=" + n + " d=" + d + " m=" + m );

		// NOTE Eh, this sort of works, but doesn't always

		// Now split m according to the rotation
		double e = angle / Constants.QUARTER_CIRCLE;
		double ei = 1 - e;
		System.out.println( "e=" + e + " ei=" + ei );

		double a = m * e;
		double b = m * ei;

		return Point.of( Math.sqrt( a ), Math.sqrt( b ) );
	}

	/**
	 * Compute the bounds of an elliptic arc.
	 *
	 * @param origin The arc origin
	 * @param radii The arc radii
	 * @param rotate The arc rotation
	 * @param start The arc start angle
	 * @param extent The arc extent angle
	 * @return The bounds of the arc as two points
	 */
	public static double[][] arcBounds( double[] origin, double[] radii, double rotate, double start, double extent ) {
		double[][] points = arcEndPoints( origin, radii, rotate, start, extent );
		double[][] b = ellipseBounds( origin, radii, rotate );

		// NEXT Implement Geometry.arcBounds()

		return b;
	}

	/**
	 * Compute the polynomial coefficients for an ellipse.
	 *
	 * @param c The center of the ellipse
	 * @param rx The x radius of the ellipse
	 * @param ry The y radius of the ellipse
	 * @return The polynomial coefficients as an array
	 */
	public static double[] ellipseCoefficients( double[] c, double rx, double ry ) {
		return new double[]{ ry * ry, 0, rx * rx, -2 * ry * ry * c[ 0 ], -2 * rx * rx * c[ 1 ], ry * ry * c[ 0 ] * c[ 0 ] + rx * rx * c[ 1 ] * c[ 1 ] - rx * rx * ry * ry };
	}

	/**
	 * Determine if an ellipse is circular by comparing the two radii. If the
	 * radii are the same size according to {@link #areSameSize(double, double)}
	 * then the ellipse is circular.
	 *
	 * @param radii The ellipse radii
	 * @return True if the ellipse is circular, false otherwise
	 */
	public static boolean isCircular( double[] radii ) {
		return areSameSize( radii[ 0 ], radii[ 1 ] );
	}

	public static double arcLength( double[] c, double[] r, double rotate, double start, double extent ) {
		return arcLength( c, r, rotate, start, extent, Constants.RESOLUTION_LENGTH );
	}

	public static double arcLength( double[] c, double[] r, double rotate, double start, double extent, double tolerance ) {
		// If the arc is circular then use the circle formula
		if( isCircular( r ) ) return r[ 0 ] * Math.abs( extent );

		// Calc the elliptic arc length parametrically
		double next;
		double error = Double.MAX_VALUE;
		int segments = Constants.MINIMUM_SEGMENTS;
		int iteration = 0;
		int iterationLimit = 20;
		double[] startPoint = ellipsePoint( c, r, rotate, start );
		double[] endPoint = ellipsePoint( c, r, rotate, start + extent );

		// Start with just the distance between control points
		double last = length( startPoint, endPoint );

		// Refine the distance until the error is less than the tolerance
		while( error > tolerance && iteration < iterationLimit ) {
			double[][] points = arcAsPoints( c, r, rotate, start, extent, segments );
			next = length( points );
			error = Math.abs( next - last );

			segments *= 2;
			last = next;
			iteration++;
		}

		return last;
	}

	/**
	 * Convert an arc to a list of points.
	 *
	 * @param c The center of the arc
	 * @param r The radii of the arc
	 * @param rotate The rotate angle of the arc
	 * @param start The start angle of the arc
	 * @param extent The extent angle of the arc
	 * @param count The number of points to generate, must be at least 2
	 * @return A list of points on the arc
	 */
	public static double[][] arcAsPoints( double[] c, double[] r, double rotate, double start, double extent, int count ) {
		// Note, the first point is p1 and the last point is p4
		// Only need to calculate the points in between
		int segments = count - 1;

		double[][] points = new double[ count ][];
		double offset = extent / segments;

		// Make the start point accurate
		points[ 0 ] = ellipsePoint( c, r, rotate, start );

		// Iterate through the internal points
		for( int index = 1; index < segments; index++ ) {
			points[ index ] = ellipsePoint( c, r, rotate, start + (index * offset) );
		}

		// Make the end point accurate
		points[ count - 1 ] = ellipsePoint( c, r, rotate, start + extent );

		return points;
	}

	/**
	 * Get the start and end points of an arc.
	 *
	 * @return The start and end points of an arc as an array of points
	 */
	public static double[][] arcEndPoints( double[] c, double[] r, double rotate, double start, double extent ) {
		return arcAsPoints( c, r, rotate, start, extent, 2 );
	}

	public static double[][] arcEndPoints( double cx, double cy, double rx, double ry, double rotate, double start, double extent ) {
		return arcAsPoints( new double[]{ cx, cy }, new double[]{ rx, ry }, rotate, start, extent, 2 );
	}

	/**
	 * Get the start, mid and end points of an arc.
	 *
	 * @return The start, mid and end points of an arc as an array of points
	 */
	public static double[][] arcReferencePoints( double[] c, double[] r, double rotate, double start, double extent ) {
		return arcAsPoints( c, r, rotate, start, extent, 3 );
	}

	/**
	 * Convert an arc definition from endpoint format to center format.
	 * <p>
	 * NOTE: Angles values are expected in radians and returned in radians
	 * <p>
	 * Derived from <a href="https://www.w3.org/TR/SVG11/implnote.html#ArcConversionEndpointToCenter">https://www.w3.org/TR/SVG11/implnote.html#ArcConversionEndpointToCenter</a>
	 *
	 * @param p1 The start point of the arc
	 * @param data The remaining data for the arc in the format [x, y, rx, ry, rotate, largeArc, sweep]
	 * @return The center format of the arc in the format [cx, cy, rx, ry, startAngle, deltaAngle]
	 */
	public static double[] arcEndpointToCenter( double[] p1, double[] data ) {
		// Define the incoming values
		double x1 = p1[ 0 ];
		double y1 = p1[ 1 ];
		double x2 = data[ 0 ];
		double y2 = data[ 1 ];
		double rx = data[ 2 ];
		double ry = data[ 3 ];
		double rotate = data[ 4 ];
		double arcFlag = data[ 5 ];
		double sweepFlag = data[ 6 ];

		// F6.6 Step 1: Ensure radii are non-zero
		if( rx == 0.0 || ry == 0.0 ) throw new IllegalArgumentException( "Radius x and radius y cannot be zero" );

		// F6.6 Step 2: Ensure radii are positive
		if( rx < 0 ) rx = -rx;
		if( ry < 0 ) ry = -ry;

		// F6.5 Step 1: Compute (x1_, y1_) from (x1, y1)
		double c_phi = Math.cos( rotate );
		double s_phi = Math.sin( rotate );

		var hd_x = (x1 - x2) / 2.0; // half diff of x
		var hd_y = (y1 - y2) / 2.0; // half diff of y
		var hs_x = (x1 + x2) / 2.0; // half sum of x
		var hs_y = (y1 + y2) / 2.0; // half sum of y

		var x1_ = c_phi * hd_x + s_phi * hd_y;
		var y1_ = c_phi * hd_y - s_phi * hd_x;

		// F6.6 Step 3: Ensure radii are large enough
		var lambda = (x1_ * x1_) / (rx * rx) + (y1_ * y1_) / (ry * ry);
		if( lambda > 1 ) {
			rx = rx * Math.sqrt( lambda );
			ry = ry * Math.sqrt( lambda );
		}

		var rxry = rx * ry;
		var rxy1_ = rx * y1_;
		var ryx1_ = ry * x1_;
		var squareSum = rxy1_ * rxy1_ + ryx1_ * ryx1_;
		if( squareSum == 0 ) throw new IllegalArgumentException( "Start point can not be same as end point" );

		var coeff = Math.sqrt( Math.abs( (rxry * rxry - squareSum) / squareSum ) );
		if( arcFlag == sweepFlag ) coeff = -coeff;

		// F6.5 Step 2: Compute (cx_, cy_) from (x1_, y1_)
		var cx_ = coeff * rxy1_ / ry;
		var cy_ = -coeff * ryx1_ / rx;

		// F6.5 Step 3: Compute (cx, cy) from (cx_, cy_)
		double cx = c_phi * cx_ - s_phi * cy_ + hs_x;
		double cy = s_phi * cx_ + c_phi * cy_ + hs_y;

		var xcr1 = (x1_ - cx_) / rx;
		var xcr2 = (x1_ + cx_) / rx;
		var ycr1 = (y1_ - cy_) / ry;
		var ycr2 = (y1_ + cy_) / ry;

		// F6.5 Step 4: Compute startAngle
		double startAngle = getAngle( 1.0, 0.0, xcr1, ycr1 );

		// F6.5 Step 4: Compute deltaAngle
		double deltaAngle = getAngle( xcr1, ycr1, -xcr2, -ycr2 );
		if( deltaAngle > FULL_CIRCLE ) {deltaAngle -= FULL_CIRCLE;}
		if( deltaAngle < 0.0 ) {deltaAngle += FULL_CIRCLE;}
		if( sweepFlag == 0 ) {deltaAngle -= FULL_CIRCLE;}

		return new double[]{ cx, cy, rx, ry, startAngle, deltaAngle };
	}

	/**
	 * Get the root (the parametric values) for given curve and line.
	 *
	 * @param a The curve point a
	 * @param b The curve point b
	 * @param c The curve point c
	 * @param d The curve point d
	 * @param l1 The first line point
	 * @param l2 The other line point
	 * @return The parametric values corresponding to the curve line intersections
	 */
	// MVS This implementation is verified 11-Mar-2021
	public static double[] curveLineRoots( double[] a, double[] b, double[] c, double[] d, double[] l1, double[] l2 ) {
		//la=y2-y1
		double la = l2[ 1 ] - l1[ 1 ];
		//lb=x1-x2
		double lb = l1[ 0 ] - l2[ 0 ];
		//lc=x1*(y1-y2)+y1*(x2-x1)
		double lc = l1[ 0 ] * (l1[ 1 ] - l2[ 1 ]) + l1[ 1 ] * (l2[ 0 ] - l1[ 0 ]);

		double[][] coefficients = Geometry.cubicCoefficients( a, b, c, d );
		double c3 = la * coefficients[ 0 ][ 0 ] + lb * coefficients[ 0 ][ 1 ];
		double c2 = la * coefficients[ 1 ][ 0 ] + lb * coefficients[ 1 ][ 1 ];
		double c1 = la * coefficients[ 2 ][ 0 ] + lb * coefficients[ 2 ][ 1 ];
		double c0 = la * coefficients[ 3 ][ 0 ] + lb * coefficients[ 3 ][ 1 ] + lc;

		return new Polynomial( c3, c2, c1, c0 ).getRoots();
	}

	/**
	 * Compute the point on a cubic Bézier curve for parametric value. This method
	 * uses the linear interpolation method to compute the curve point. It is
	 * generally more efficient than the polynomial method for a single point.
	 *
	 * @param a The curve point a
	 * @param b The curve point b
	 * @param c The curve point c
	 * @param d The curve point d
	 * @param t The parametric value between 0 and 1
	 * @return The point on the curve at the parametric value
	 */
	public static double[] curvePoint( double[] a, double[] b, double[] c, double[] d, double t ) {
		double[] e = Vector.lerp( a, b, t );
		double[] f = Vector.lerp( b, c, t );
		double[] g = Vector.lerp( c, d, t );
		double[] h = Vector.lerp( e, f, t );
		double[] i = Vector.lerp( f, g, t );
		return Vector.lerp( h, i, t );
	}

	/**
	 * Compute the point on a cubic Bézier curve for parametric value. This method
	 * uses the polynomial method to compute the curve point. It is generally
	 * less efficient than the linear interpolation method for a single point.
	 *
	 * @param a The curve point a
	 * @param b The curve point b
	 * @param c The curve point c
	 * @param d The curve point d
	 * @param t The parametric value between 0 and 1
	 * @return The point on the curve at the parametric value
	 */
	public static double[] curvePointByPolynomial( double[] a, double[] b, double[] c, double[] d, double t ) {
		// This gives the same result as using Geometry.curvePoint() but it requires
		// calculating the coefficients first and therefore takes more compute time
		// for a single point.
		double[][] coefficients = Geometry.cubicCoefficients( a, b, c, d );
		double x = coefficients[ 0 ][ 0 ] * t * t * t + coefficients[ 1 ][ 0 ] * t * t + coefficients[ 2 ][ 0 ] * t + coefficients[ 3 ][ 0 ];
		double y = coefficients[ 0 ][ 1 ] * t * t * t + coefficients[ 1 ][ 1 ] * t * t + coefficients[ 2 ][ 1 ] * t + coefficients[ 3 ][ 1 ];
		return new double[]{ x, y, 0 };
	}

	/**
	 * Compute the parametric value of a point on a curve. It is assumed that
	 * all points are coplanar and no bounds checks are done for performance.
	 *
	 * @param a The curve point a
	 * @param b The curve point b
	 * @param c The curve point c
	 * @param d The curve point d
	 * @param r The reference point
	 * @return The parametric value for the reference point
	 */
	public static double curveParametricValue( double[] a, double[] b, double[] c, double[] d, double[] r ) {
		// Get the curve coefficients
		double[][] coefficients = cubicCoefficients( a, b, c, d );

		// Subtract the reference point x coordinate from the last coefficient
		coefficients[ 3 ][ 0 ] -= r[ 0 ];

		// Calculate the polynomial roots
		double[] roots = new Polynomial( coefficients[ 0 ][ 0 ], coefficients[ 1 ][ 0 ], coefficients[ 2 ][ 0 ], coefficients[ 3 ][ 0 ] ).getRoots();

		// Test each root for which one matches the reference point
		for( double root : roots ) {
			if( root >= 0 && root <= 1 && Geometry.areSamePoint( Geometry.curvePoint( a, b, c, d, root ), r ) ) return root;
		}
		return Double.NaN;
	}

	/**
	 * Compute the parametric value of a point on a curve. It is assumed that
	 * all points are coplanar and no bounds checks are done for performance.
	 *
	 * @param a The curve point a
	 * @param b The curve point b
	 * @param c The curve point c
	 * @param d The curve point d
	 * @param r The reference point
	 * @return The parametric value for the reference point
	 */
	public static double curveParametricValueNear( double[] a, double[] b, double[] c, double[] d, double[] r ) {
		// Get the curve coefficients
		double[][] coefficients = cubicCoefficients( a, b, c, d );

		// Subtract the reference point x coordinate from the last coefficient
		coefficients[ 3 ][ 0 ] -= r[ 0 ];

		// Calculate the polynomial roots
		double[] roots = new Polynomial( coefficients[ 0 ][ 0 ], coefficients[ 1 ][ 0 ], coefficients[ 2 ][ 0 ], coefficients[ 3 ][ 0 ] ).getRoots();

		// Test each root for which one matches the reference point
		Map<double[], Double> rootMap = new HashMap<>();
		for( double root : roots ) {
			if( root >= 0 && root <= 1 ) {
				rootMap.put( Geometry.curvePoint( a, b, c, d, root ), root );
			}
		}

		double[] nearest = nearest( r, new ArrayList<>( rootMap.keySet() ).toArray( new double[ 0 ][ 0 ] ) );
		return nearest == null ? Double.NaN : rootMap.get( nearest );
	}

	/**
	 * Subdivide a cubic Bézier curve. The parameters t is expected to be in the
	 * range 0.0 to 1.0 but is not range checked for performance reasons.
	 *
	 * @param p1 Control point a
	 * @param p2 Control point b
	 * @param p3 Control point c
	 * @param p4 Control point d
	 * @param t The parametric location to divide the curve
	 * @return Two cubic Bézier curves (an array of two arrays of four points each)
	 */
	public static double[][][] curveSubdivide( double[] p1, double[] p2, double[] p3, double[] p4, double t ) {
		double[] p5 = Vector.lerp( p1, p2, t );
		double[] p6 = Vector.lerp( p2, p3, t );
		double[] p7 = Vector.lerp( p3, p4, t );
		double[] p8 = Vector.lerp( p5, p6, t );
		double[] p9 = Vector.lerp( p6, p7, t );
		double[] p10 = Vector.lerp( p8, p9, t );
		return new double[][][]{ new double[][]{ p1, p5, p8, p10 }, new double[][]{ p10, p9, p7, p4 } };
	}

	/**
	 * Get a cubic Bézier curve as a set of interpolated points.
	 *
	 * @param p1 Control point a
	 * @param p2 Control point b
	 * @param p3 Control point c
	 * @param p4 Control point d
	 * @param count The number of points along the curve
	 * @return The interpolated points
	 */
	public static double[][] curveAsPoints( double[] p1, double[] p2, double[] p3, double[] p4, int count ) {
		// Note, the first point is p1 and the last point is p4
		// Only need to calculate the points in between
		int segments = count - 1;

		double[][] points = new double[ count ][];
		points[ 0 ] = p1;
		points[ count - 1 ] = p4;
		double offset = 1.0 / segments;

		for( int index = 1; index < segments; index++ ) {
			points[ index ] = curvePoint( p1, p2, p3, p4, index * offset );
		}

		return points;
	}

	/**
	 * Get a quadratic Bézier curve as a set of interpolated points.
	 *
	 * @param p1 Control point a
	 * @param p2 Control point b
	 * @param p3 Control point c
	 * @return The interpolated points
	 */
	public static double quadArcLength( double[] p1, double[] p2, double[] p3 ) {
		return cubicArcLength( p1, p2, p2, p3, Constants.RESOLUTION_LENGTH );
	}

	/**
	 * Compute the arc length of a quadratic Bézier curve.
	 *
	 * @param p1 Control point a
	 * @param p2 Control point b
	 * @param p3 Control point c
	 * @return The estimated arc length within the specific tolerance
	 */
	public static double quadArcLength( double[] p1, double[] p2, double[] p3, double tolerance ) {
		return cubicArcLength( p1, p2, p2, p3, tolerance );
	}

	public static double[][] quadBounds( double[] a, double[] b, double[] c ) {
		// Some help: https://iquilezles.org/articles/
		return null;
	}

	public static double cubicArcLength( double[] p1, double[] p2, double[] p3, double[] p4 ) {
		return cubicArcLength( p1, p2, p3, p4, Constants.RESOLUTION_LENGTH );
	}

	/**
	 * Compute the arc length of a cubic Bézier curve.
	 *
	 * @param p1 Control point a
	 * @param p2 Control point b
	 * @param p3 Control point c
	 * @param p4 Control point d
	 * @return The estimated arc length within the specific tolerance
	 */
	public static double cubicArcLength( double[] p1, double[] p2, double[] p3, double[] p4, double tolerance ) {
		// Start with just the distance between control points
		double last = length( p1, p2, p3, p4 );

		double next;
		double error = Double.MAX_VALUE;
		int segments = Constants.MINIMUM_SEGMENTS;
		int iteration = 0;
		int iterationLimit = 20;

		while( error > tolerance && iteration < iterationLimit ) {
			double[][] points = curveAsPoints( p1, p2, p3, p4, segments );
			next = length( points );

			error = Math.abs( next - last );
			segments *= 2;
			last = next;
			iteration++;
		}

		return last;
	}

	/**
	 * Compute the x/y coefficients of a cubic Bézier curve.
	 * <pre>
	 * c[0] = coefficients for t^3
	 * c[1] = coefficients for t^2
	 * c[2] = coefficients for t^1
	 * c[3] = coefficients for t^0
	 * </pre>
	 *
	 * @param a The curve point a
	 * @param b The curve point b
	 * @param c The curve point c
	 * @param d The curve point d
	 * @return An array of coefficient pairs corresponding to the x and y axes
	 */
	// MVS This implementation is verified 11-Mar-2021
	public static double[][] cubicCoefficients( double[] a, double[] b, double[] c, double[] d ) {
		double[] e, f, g, h; // temporary variables
		double[] c3, c2, c1, c0; // coefficients

		e = Vector.scale( a, -1 );
		f = Vector.scale( b, 3 );
		g = Vector.scale( c, -3 );
		h = Vector.add( e, Vector.add( f, Vector.add( g, d ) ) );
		c3 = Vector.of( h[ 0 ], h[ 1 ] );

		e = Vector.scale( a, 3 );
		f = Vector.scale( b, -6 );
		g = Vector.scale( c, 3 );
		h = Vector.add( e, Vector.add( f, g ) );
		c2 = Vector.of( h[ 0 ], h[ 1 ] );

		e = Vector.scale( a, -3 );
		f = Vector.scale( b, 3 );
		g = Vector.add( e, f );
		c1 = Vector.of( g[ 0 ], g[ 1 ] );

		c0 = Vector.of( a[ 0 ], a[ 1 ] );

		return new double[][]{ c3, c2, c1, c0 };
	}

	/**
	 * Convert a list of points into a natural cubic spline curve as a list of
	 * cubic Bézier curve segments.
	 *
	 * @param points The list of 2D points to interpolate
	 * @return The list of cubic curve segments
	 */
	public static double[][][] interpolateCubicNatural( double[][] points ) {
		List<Double> valueList = new ArrayList<>( points.length * 3 );
		for( double[] point : points ) {
			valueList.add( point[ 0 ] );
			valueList.add( point[ 1 ] );
			valueList.add( point[ 2 ] );
		}

		// Use tinyspline to interpolate the curves
		BSpline spline = BSpline.interpolateCubicNatural( valueList, 3 );

		List<Double> controlPoints = spline.getControlPoints();
		int size = (int)(spline.getOrder() * spline.getDimension());
		int surfaceCount = controlPoints.size() / size;

		double[][][] curves = new double[ surfaceCount ][ 4 ][ 3 ];
		for( int index = 0; index < surfaceCount; index++ ) {
			int offset = index * size;
			curves[ index ][ 0 ][ 0 ] = controlPoints.get( offset );
			curves[ index ][ 0 ][ 1 ] = controlPoints.get( offset + 1 );
			curves[ index ][ 0 ][ 2 ] = controlPoints.get( offset + 2 );
			curves[ index ][ 1 ][ 0 ] = controlPoints.get( offset + 3 );
			curves[ index ][ 1 ][ 1 ] = controlPoints.get( offset + 4 );
			curves[ index ][ 1 ][ 2 ] = controlPoints.get( offset + 5 );
			curves[ index ][ 2 ][ 0 ] = controlPoints.get( offset + 6 );
			curves[ index ][ 2 ][ 1 ] = controlPoints.get( offset + 7 );
			curves[ index ][ 2 ][ 2 ] = controlPoints.get( offset + 8 );
			curves[ index ][ 3 ][ 0 ] = controlPoints.get( offset + 9 );
			curves[ index ][ 3 ][ 1 ] = controlPoints.get( offset + 10 );
			curves[ index ][ 3 ][ 2 ] = controlPoints.get( offset + 11 );
		}

		return curves;
	}

	public static double[][] cubicBounds( double[] a, double[] b, double[] c, double[] d ) {
		// Some help: https://iquilezles.org/articles/
		return null;
	}

	/**
	 * Get the nearest point in a set of points to the specified point.
	 *
	 * @param point The point from which to check
	 * @param points The set of points to check
	 * @return The point nearest to the specified point
	 */
	public static double[] nearest( double[] point, double[]... points ) {
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

	public static double[] nearestBoundLinePoint( double[] a, double[] b, double[] p ) {
		double[] pb = Vector.minus( p, b );
		double[] pa = Vector.minus( p, a );
		double[] ba = Vector.minus( b, a );
		double[] ab = Vector.minus( a, b );
		double anglea = Geometry.getAbsAngle( ba, pa );
		double angleb = Geometry.getAbsAngle( ab, pb );
		if( anglea > Constants.QUARTER_CIRCLE || angleb > Constants.QUARTER_CIRCLE ) return null;

		return nearestLinePoint( a, b, p );
	}

	public static boolean near( double distance ) {
		return near( distance, Constants.RESOLUTION_LENGTH );
	}

	public static boolean near( double distance, double tolerance ) {
		return distance <= tolerance;
	}

	/**
	 * Determine if all the points are collinear with a line defined by point a and point b. It is important to note that this method is only accurate if all the
	 * points are in between points a and b. All the points must lie within
	 * POINT_TOLERANCE.
	 *
	 * @param a The first point on the line
	 * @param b The second point on the line
	 * @param points The points to check
	 * @return True if all the points are collinear, false otherwise
	 */
	public static boolean areCollinear( double[] a, double[] b, double[]... points ) {
		for( double[] vector : points ) {
			if( linePointDistance( a, b, vector ) >= Constants.RESOLUTION_LENGTH ) return false;
		}
		return true;
	}

	/**
	 * Determine if all the points are coplanar with the plane defined by the orientation.
	 *
	 * @param orientation The orientation used to define the plane
	 * @param points The points to check
	 * @return True if all points are coplanar, false otherwise
	 */
	public static boolean areCoplanar( Orientation orientation, double[]... points ) {
		return areCoplanar( orientation.getOrigin(), orientation.getNormal(), points );
	}

	/**
	 * Determine if all the points are coplanar with the plane defined by the origin and normal.
	 *
	 * @param origin The plane origin
	 * @param normal The plane normal vector
	 * @param points The points to check
	 * @return True if all the points are coplanar with the plane, false otherwise
	 */
	public static boolean areCoplanar( double[] origin, double[] normal, double[]... points ) {
		return areCoplanar( origin, normal, Constants.RESOLUTION_LENGTH, points );
	}

	/**
	 * Determine if all the points are coplanar with the plane defined by the origin and normal. All the points must line within the tolerance from the plane. It
	 * is generally recommended that
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
			if( pointPlaneDistance( vector, origin, normal ) > tolerance ) return false;
		}
		return true;
	}

	/**
	 * Determine if two sizes (lengths, radii, etc.) can be considered the same
	 * by checking if teh difference between them is smaller than
	 * RESOLUTION_LENGTH.
	 *
	 * @param a First size
	 * @param b Second size
	 * @return True if the two sizes are considered the same, false otherwise
	 */
	public static boolean areSameSize( double a, double b ) {
		return Math.abs( a - b ) < Constants.RESOLUTION_LENGTH;
	}

	/**
	 * Determine if two points can be considered the same by checking if the distance between the two points is smaller than RESOLUTION_LENGTH.
	 *
	 * @param point1 First point
	 * @param point2 Second point
	 * @return True if the two points are considered the same, false otherwise
	 */
	public static boolean areSamePoint( double[] point1, double[] point2 ) {
		return distance( point1, point2 ) < Constants.RESOLUTION_LENGTH;
	}

	/**
	 * Determine if two angles can be considered the same by checking if the difference between the two angles is smaller than RESOLUTION_ANGLE.
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
	 * Determine if two vectors can be considered parallel by checking if the angle between the two vectors is smaller than ANGLE_TOLERANCE.
	 *
	 * @param vector1 The first vector
	 * @param vector2 The second vector
	 * @return True if the vectors are considered parallel, false otherwise
	 */
	public static boolean areParallel( double[] vector1, double[] vector2 ) {
		return distance( Vector.normalize( vector1 ), Vector.normalize( vector2 ) ) < Constants.RESOLUTION_LENGTH;
	}

	/**
	 * Determine if two vectors can be considered antiparallel by checking if the angle between the two vectors, with one reversed, is smaller than
	 * ANGLE_TOLERANCE.
	 *
	 * @param vector1 The first vector
	 * @param vector2 The second vector
	 * @return True if the vectors are considered parallel, false otherwise
	 */
	public static boolean areAntiParallel( double[] vector1, double[] vector2 ) {
		return distance( Vector.normalize( vector1 ), Vector.normalize( Vector.reverse( vector2 ) ) ) < Constants.RESOLUTION_LENGTH;
	}

	/**
	 * Use the x and y coordinates to determine if the three points are in counter-clockwise, straight, or clockwise order.
	 *
	 * @param a First point
	 * @param b Second point
	 * @param c Third point
	 * @return One if CCW, zero if straight, and minus one if CW.
	 */
	public static double getSpin( double[] a, double[] b, double[] c ) {
		double[] ab = Vector.minus( a, b );
		double[] cb = Vector.minus( c, b );
		return Math.signum( Vector.cross( cb, ab )[ 2 ] );
	}

	/**
	 * Determine if two line segments, A and B, intersect given the respective end points.
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

	/**
	 * Compute a normal vector for three points.
	 *
	 * @param a Point a
	 * @param b Point b
	 * @param c Point c
	 * @return The normal vector
	 */
	public static double[] getNormal( double[] a, double[] b, double[] c ) {
		return Vector.cross( Vector.minus( a, b ), Vector.minus( b, c ) );
	}

	/**
	 * Compute the determinant for three points.
	 *
	 * @param a Point a
	 * @param b Point b
	 * @param c Point c
	 * @return The determinant
	 */
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
	 * Convert a point in cartesian coordinates [x,y,z] to polar coordinates [r,a,z] with the angle in degrees instead of radians.
	 *
	 * @param point The point to convert
	 * @return The point in polar coordinates
	 */
	public static double[] cartesianToPolarDegrees( final double[] point ) {
		double[] v = cartesianToPolar( point );
		return Point.of( v[ 0 ], Math.toDegrees( v[ 1 ] ), v[ 2 ] );
	}

	/**
	 * Convert a point in polar coordinates [r,a,z] to cartesian coordinates [x,y,z] with the angle in degrees instead of radians.
	 *
	 * @param point The point to convert
	 * @return The point in cartesian coordinates
	 */
	public static double[] polarDegreesToCartesian( final double[] point ) {
		return polarToCartesian( Point.of( point[ 0 ], Math.toRadians( point[ 1 ] ), point[ 2 ] ) );
	}

}
