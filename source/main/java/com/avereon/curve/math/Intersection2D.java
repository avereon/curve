package com.avereon.curve.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * A reference on 2D intersections: http://www.kevlindev.com/geometry/2D/intersections/index.htm
 */
public class Intersection2D extends Intersection {

	public Intersection2D( Type status, double[]... points ) {
		super( status, points );
	}

	private static boolean near( double a ) {
		return Math.abs( a ) <= Constants.RESOLUTION_LENGTH;
	}

	private static boolean near( double a, double b ) {
		return Math.abs( b - a ) <= Constants.RESOLUTION_LENGTH;
	}

	private static boolean near( double[] a, double[] b ) {
		return Geometry.areSamePoint( a, b );
	}

	private static boolean nearAngle( double a, double b ) {
		return Geometry.areSameAngle( a, b );
	}

	/**
	 * Find the intersection of two infinitely long lines. An intersection object
	 * is returned with the following values:
	 * <ul>
	 * <li>Coincident, no points: the lines overlap
	 * <li>Parallel, no points: the lines are parallel, but do not overlap
	 * <li>Intersection, 1 intersection point: the lines intersect
	 * </ul>
	 *
	 * @param a1 Line A point 1
	 * @param a2 Line A point 2
	 * @param b1 Line B point 1
	 * @param b2 Line B point 2
	 * @return The intersection
	 */
	public static Intersection2D intersectLineLine( double[] a1, double[] a2, double[] b1, double[] b2 ) {
		double distanceA2B = (b2[ 1 ] - b1[ 1 ]) * (a2[ 0 ] - a1[ 0 ]) - (b2[ 0 ] - b1[ 0 ]) * (a2[ 1 ] - a1[ 1 ]);
		double distanceB2A = (b2[ 0 ] - b1[ 0 ]) * (a1[ 1 ] - b1[ 1 ]) - (b2[ 1 ] - b1[ 1 ]) * (a1[ 0 ] - b1[ 0 ]);
		double scale = distanceB2A / distanceA2B;

		if( near( distanceB2A ) ) return new Intersection2D( Type.SAME );
		return new Intersection2D( Type.INTERSECTION, Vector.of( a1[ 0 ] + scale * (a2[ 0 ] - a1[ 0 ]), a1[ 1 ] + scale * (a2[ 1 ] - a1[ 1 ]) ) );
	}

	/**
	 * Find the intersection of two line segments. An intersection object is
	 * returned with the following values:
	 * <ul>
	 * <li>None, no points: the segments do not intersect
	 * <li>Coincident, no points: the segments overlap
	 * <li>Parallel, no points: the segments are parallel, but do not overlap
	 * <li>Intersection, 1 intersection point: the segments intersect
	 * </ul>
	 *
	 * @param a1 Line A point 1
	 * @param a2 Line A point 2
	 * @param b1 Line B point 1
	 * @param b2 Line B point 2
	 * @return The intersection
	 */
	public static Intersection2D intersectSegmentSegment( double[] a1, double[] a2, double[] b1, double[] b2 ) {
		// Same segments
		if( Arrays.equals( a1, b1 ) && Arrays.equals( a2, b2 ) ) return new Intersection2D( Intersection2D.Type.SAME );
		if( Arrays.equals( a1, b2 ) && Arrays.equals( a2, b1 ) ) return new Intersection2D( Intersection2D.Type.SAME );

		// Matching end points
		if( Arrays.equals( a1, b1 ) ) return new Intersection2D( Intersection2D.Type.INTERSECTION, a1 );
		if( Arrays.equals( a1, b2 ) ) return new Intersection2D( Intersection2D.Type.INTERSECTION, a1 );
		if( Arrays.equals( a2, b1 ) ) return new Intersection2D( Intersection2D.Type.INTERSECTION, a2 );
		if( Arrays.equals( a2, b2 ) ) return new Intersection2D( Intersection2D.Type.INTERSECTION, a2 );

		double distanceA2B = (b2[ 0 ] - b1[ 0 ]) * (a1[ 1 ] - b1[ 1 ]) - (b2[ 1 ] - b1[ 1 ]) * (a1[ 0 ] - b1[ 0 ]);
		double distanceB2A = (a2[ 0 ] - a1[ 0 ]) * (a1[ 1 ] - b1[ 1 ]) - (a2[ 1 ] - a1[ 1 ]) * (a1[ 0 ] - b1[ 0 ]);
		double angleA2B = (b2[ 1 ] - b1[ 1 ]) * (a2[ 0 ] - a1[ 0 ]) - (b2[ 0 ] - b1[ 0 ]) * (a2[ 1 ] - a1[ 1 ]);

		Intersection2D result;
		if( angleA2B != 0 ) {
			double ua = distanceA2B / angleA2B;
			double ub = distanceB2A / angleA2B;

			if( 0 <= ua && ua <= 1 && 0 <= ub && ub <= 1 ) {
				result = new Intersection2D( Type.INTERSECTION, Vector.of( a1[ 0 ] + ua * (a2[ 0 ] - a1[ 0 ]), a1[ 1 ] + ua * (a2[ 1 ] - a1[ 1 ]) ) );
			} else {
				result = new Intersection2D( Type.NONE );
			}
		} else {
			if( near( distanceA2B ) || near( distanceB2A ) ) {
				result = new Intersection2D( Type.COINCIDENT );
			} else {
				result = new Intersection2D( Type.PARALLEL );
			}
		}

		return result;
	}

	/**
	 * Find the intersection of a line and a circle. An intersection object is
	 * returned with the following values:
	 * <ul>
	 * <li>None, no points: the line and circle do not intersect
	 * <li>Intersection, 1 intersection point: the line is tangent to the circle
	 * <li>Intersection, 2 intersection points: line and circle intersect
	 * </ul>
	 *
	 * @param a1 The first line point
	 * @param a2 The other line point
	 * @param o The center of the circle
	 * @param radius The radius of the circle
	 * @return The intersection
	 */
	public static Intersection2D intersectLineCircle( double[] a1, double[] a2, double[] o, double radius ) {
		// Transform the line points relative to the circle origin
		double[] p1 = Vector.subtract( a1, o );
		double[] p2 = Vector.subtract( a2, o );

		Intersection2D x = intersectLineCircle( p1, p2, radius );

		// Transform the intersection points relative to the circle origin
		double[][] points = Arrays.stream( x.getPoints() ).map( p -> Vector.add( p, o ) ).toArray( double[][]::new );

		return new Intersection2D( x.getType(), points );
	}

	/**
	 * This implementation assumes that the circle is at the origin.
	 *
	 * @param p1 First line point
	 * @param p2 Other line point
	 * @param radius The circle radius
	 * @return The intersection
	 */
	public static Intersection2D intersectLineCircle( double[] p1, double[] p2, double radius ) {
		// Determine the separation from the origin of the circle
		double[] offset = Geometry.vectorToLine( p1, p2, Point.of( 0, 0 ) );

		// If the offset is greater than the radius the line does not intersect
		if( Vector.magnitude( offset ) > radius ) return new Intersection2D( Type.NONE );

		// If within tolerance the line is tangent to the circle
		double[] tangent = Vector.scale( Vector.normalize( offset ), radius );
		if( Geometry.areSamePoint( offset, tangent ) ) return new Intersection2D( Type.INTERSECTION, offset );

		// At this point the line crosses the circle at two points
		double dx = p2[ 0 ] - p1[ 0 ];
		double dy = p2[ 1 ] - p1[ 1 ];
		double dr = Math.sqrt( dx * dx + dy * dy );

		double dr2 = dr * dr;
		double determinant = p1[ 0 ] * p2[ 1 ] - p2[ 0 ] * p1[ 1 ];
		double dis2 = radius * radius * dr2 - determinant * determinant;
		if( dis2 <= 0 ) return new Intersection2D( Type.NONE );

		double discriminant = Math.sqrt( dis2 );

		double x1 = (determinant * dy + Arithmetic.sign( dy ) * dx * discriminant) / dr2;
		double y1 = (-determinant * dx + Math.abs( dy ) * discriminant) / dr2;
		double x2 = (determinant * dy - Arithmetic.sign( dy ) * dx * discriminant) / dr2;
		double y2 = (-determinant * dx - Math.abs( dy ) * discriminant) / dr2;

		return new Intersection2D( Type.INTERSECTION, Point.of( x1, y1 ), Point.of( x2, y2 ) );
	}

	/**
	 * Find the intersection of a line and an ellipse. An intersection object is
	 * returned with the following values:
	 * <ul>
	 * <li>None, no points: the line and ellipse do not intersect
	 * <li>Intersection, 1 intersection point: the line is tangent to the ellipse
	 * <li>Intersection, 2 intersection points: line and ellipse intersect
	 * </ul>
	 *
	 * @param a1 The first line point
	 * @param a2 The other line point
	 * @param o The center of the ellipse
	 * @param rx
	 * @param ry
	 * @return
	 */
	public static Intersection2D intersectLineEllipse( double[] a1, double[] a2, double[] o, double rx, double ry ) {
		// Transform the line points relative to the ellipse origin
		double[] p1 = Vector.subtract( a1, o );
		double[] p2 = Vector.subtract( a2, o );

		Intersection2D x = intersectLineEllipse( p1, p2, rx, ry );

		// Transform the intersection points relative to the ellipse origin
		double[][] points = Arrays.stream( x.getPoints() ).map( p -> Vector.add( p, o ) ).toArray( double[][]::new );

		return new Intersection2D( x.getType(), points );
	}

	/**
	 * This implementation assumes that the ellipse is at the origin.
	 *
	 * @param p1 First line point
	 * @param p2 Other line point
	 * @param rx The circle x radius
	 * @param ry The circle y radius
	 * @return The intersection
	 */
	public static Intersection2D intersectLineEllipse( double[] p1, double[] p2, double rx, double ry ) {
		Transform targetToLocal = Transform.scale( 1, rx / ry, 0 );
		Transform localToTarget = Transform.scale( 1, ry / rx, 0 );

		// Transform the line points according to the eccentricity of the ellipse
		double[] p3 = targetToLocal.apply( p1 );
		double[] p4 = targetToLocal.apply( p2 );

		Intersection2D intersection = intersectLineCircle( p3, p4, rx );
		double[][] points = intersection.getPoints();
		for( int index = 0; index < points.length; index++ ) {
			points[ index ] = localToTarget.apply( points[ index ] );
		}
		return new Intersection2D( intersection.getType(), points );
	}

	public static Intersection2D intersectLineBezier3( double[] l1, double[] l2, double[] a, double[] b, double[] c, double[] d ) {
		double[] roots = Geometry.curveLineRoots( a, b, c, d, l1, l2 );

		List<double[]> intersections = new ArrayList<>( 3 );
		for( double t : roots ) {
			if( t < 0.0 || t > 1.0 ) continue;
			intersections.add( Geometry.curvePoint( a, b, c, d, t ) );
		}
		double[][] result = intersections.toArray( new double[ 0 ][ 0 ] );

		return new Intersection2D( (result.length == 0 ? Type.NONE : Type.INTERSECTION), result );
	}

	/**
	 * Find the intersection of two ellipses. An intersection object is returned
	 * with the following values:
	 * <ul>
	 * <li>No Intersection, no points: the ellipses do not intersect
	 * <li>Coincident, no points: the ellipses overlap
	 * <li>Intersection, 1-4 points: the ellipses intersect
	 * </ul>
	 * This code is based on MgcIntr2DElpElp.cpp written by David Eberly. His code
	 * along with many other excellent examples are available at his site:
	 * http://www.magic-software.com
	 *
	 * @param oc1 Ellipse 1 center
	 * @param rx1 Ellipse 1 X radius
	 * @param ry1 Ellipse 1 Y radius
	 * @param oc2 Ellipse 2 center
	 * @param rx2 Ellipse 2 X radius
	 * @param ry2 Ellipse 2 Y radius
	 * @return The intersection
	 */
	public static Intersection2D intersectEllipseEllipse( double[] oc1, double rx1, double ry1, double[] oc2, double rx2, double ry2 ) {
		if( Arrays.equals( oc1, oc2 ) && rx1 == rx2 && ry1 == ry2 ) return new Intersection2D( Type.SAME );

		double[] c1 = Vector.of( 0, 0 );
		double[] c2 = Vector.of( oc2[ 0 ] - oc1[ 0 ], oc2[ 1 ] - oc1[ 1 ] );

		// Each array has six values
		double[] a = Geometry.ellipseCoefficients( c1, rx1, ry1 );
		double[] b = Geometry.ellipseCoefficients( c2, rx2, ry2 );

		Polynomial yPoly = bezout( a, b );
		double[] yRoots = yPoly.getRoots();

		double norm0 = 1e-3 * (a[ 0 ] * a[ 0 ] + 2 * a[ 1 ] * a[ 1 ] + a[ 2 ] * a[ 2 ]);
		double norm1 = 1e-3 * (b[ 0 ] * b[ 0 ] + 2 * b[ 1 ] * b[ 1 ] + b[ 2 ] * b[ 2 ]);

		List<double[]> intersections = new ArrayList<>();

		for( double yRoot : yRoots ) {
			Polynomial xPoly = new Polynomial( a[ 0 ], a[ 3 ] + yRoot * a[ 1 ], a[ 5 ] + yRoot * (a[ 4 ] + yRoot * a[ 2 ]) );
			double[] xRoots = xPoly.getRoots();
			for( double xRoot : xRoots ) {
				double test0 = (a[ 0 ] * xRoot + a[ 1 ] * yRoot + a[ 3 ]) * xRoot + (a[ 2 ] * yRoot + a[ 4 ]) * yRoot + a[ 5 ];
				if( Math.abs( test0 ) < norm0 ) {
					double test1 = (b[ 0 ] * xRoot + b[ 1 ] * yRoot + b[ 3 ]) * xRoot + (b[ 2 ] * yRoot + b[ 4 ]) * yRoot + b[ 5 ];
					if( Math.abs( test1 ) < norm1 ) intersections.add( Vector.of( xRoot + oc1[ 0 ], yRoot + oc1[ 1 ] ) );
				}
			}
		}

		return intersections.size() == 0 ? new Intersection2D( Type.NONE ) : new Intersection2D( Type.INTERSECTION, intersections.toArray( new double[ intersections.size() ][] ) );
	}

	public static Intersection2D intersectEllipseEllipse( double[] oc1, double rx1, double ry1, double r1, double[] oc2, double rx2, double ry2, double r2 ) {
		if( near( oc1, oc2 ) && near( rx1, rx2 ) && near( ry1, ry2 ) && nearAngle( r1, r2 ) ) return new Intersection2D( Type.SAME );

		// Move everything so that the center of ellipse 1 is at the origin
		oc2 = Vector.subtract( oc2, oc1 );

		// Rotate everything so that the axes of ellipse 1 are parallel with the X and Y axes
		oc2 = Vector.rotate( oc2, -r1 );
		r2 = r2 - r1;

		// Scale everything so that ellipse 1 is a circle
		double e = rx1 / ry1;
		oc2 = Vector.scale( oc2, 1, e );
		ry2 = e * ry2;
		r2 = Geometry.cartesianToPolar( Vector.scale( Geometry.polarToCartesian( Vector.of( 1, r2 ) ), 1, e ) )[ 1 ];

		// Rotate everything so that the axes of ellipse 2 are parallel with the X and Y axes
		oc2 = Vector.rotate( oc2, -r2 );

		// Call intersectEllipseEllipse() without rotations
		Intersection2D xn = intersectEllipseEllipse( Point.of( 0, 0, 0 ), rx1, rx1, oc2, rx2, ry2 );

		// For any intersection points, undo rotate, scale, rotate and move
		double[][] intersections = xn.getPoints();
		for( int index = 0; index < intersections.length; index++ ) {
			intersections[ index ] = Vector.add( Vector.rotate( Vector.scale( Vector.rotate( intersections[ index ], r2 ), 1, 1 / e ), r1 ), oc1 );
		}

		return intersections.length == 0 ? new Intersection2D( Type.NONE ) : new Intersection2D( Type.INTERSECTION, intersections );
	}

	public static Intersection2D intersectEllipseBezier3( double[] ec, double rx, double ry, double er, double[] a, double[] b, double[] c, double[] d ) {
		// Move everything so that the center of the ellipse is at the origin
		a = Vector.subtract( a, ec );
		b = Vector.subtract( b, ec );
		c = Vector.subtract( c, ec );
		d = Vector.subtract( d, ec );

		// Rotate everything so that the axes of the ellipse are parallel with the X and Y axes
		a = Vector.rotate( a, -er );
		b = Vector.rotate( b, -er );
		c = Vector.rotate( c, -er );
		d = Vector.rotate( d, -er );

		// Find the intersections
		Intersection2D xn = intersectEllipseBezier3( Point.of( 0, 0, 0 ), rx, ry, a, b, c, d );

		// For any intersection points, undo rotate and move
		double[][] intersections = xn.getPoints();
		for( int index = 0; index < intersections.length; index++ ) {
			intersections[ index ] = Vector.add( Vector.rotate( intersections[ index ], er ), ec );
		}

		return intersections.length == 0 ? new Intersection2D( Type.NONE ) : new Intersection2D( Type.INTERSECTION, intersections );
	}

	/**
	 * This implementation assumes that the ellipse axes are aligned with the X
	 * and Y axes.
	 *
	 * @param ec The ellipse center
	 * @param rx The ellipse x radius
	 * @param ry The ellipse y radius
	 * @param a The curve point a
	 * @param b The curve point b
	 * @param c The curve point c
	 * @param d The curve point d
	 * @return The intersection
	 */
	public static Intersection2D intersectEllipseBezier3( double[] ec, double rx, double ry, double[] a, double[] b, double[] c, double[] d ) {
		double[][] coefficients = Geometry.curveCoefficients( a, b, c, d );
		double[] c3 = coefficients[ 0 ];
		double[] c2 = coefficients[ 1 ];
		double[] c1 = coefficients[ 2 ];
		double[] c0 = coefficients[ 3 ];

		double rxrx = rx * rx;
		double ryry = ry * ry;
		Polynomial poly = new Polynomial(
			c3[ 0 ] * c3[ 0 ] * ryry + c3[ 1 ] * c3[ 1 ] * rxrx,
			2 * (c3[ 0 ] * c2[ 0 ] * ryry + c3[ 1 ] * c2[ 1 ] * rxrx),
			2 * (c3[ 0 ] * c1[ 0 ] * ryry + c3[ 1 ] * c1[ 1 ] * rxrx) + c2[ 0 ] * c2[ 0 ] * ryry + c2[ 1 ] * c2[ 1 ] * rxrx,
			2 * c3[ 0 ] * ryry * (c0[ 0 ] - ec[ 0 ]) + 2 * c3[ 1 ] * rxrx * (c0[ 1 ] - ec[ 1 ]) + 2 * (c2[ 0 ] * c1[ 0 ] * ryry + c2[ 1 ] * c1[ 1 ] * rxrx),
			2 * c2[ 0 ] * ryry * (c0[ 0 ] - ec[ 0 ]) + 2 * c2[ 1 ] * rxrx * (c0[ 1 ] - ec[ 1 ]) + c1[ 0 ] * c1[ 0 ] * ryry + c1[ 1 ] * c1[ 1 ] * rxrx,
			2 * c1[ 0 ] * ryry * (c0[ 0 ] - ec[ 0 ]) + 2 * c1[ 1 ] * rxrx * (c0[ 1 ] - ec[ 1 ]),
			c0[ 0 ] * c0[ 0 ] * ryry - 2 * c0[ 1 ] * ec[ 1 ] * rxrx - 2 * c0[ 0 ] * ec[ 0 ] * ryry + c0[ 1 ] * c0[ 1 ] * rxrx + ec[ 0 ] * ec[ 0 ] * ryry + ec[ 1 ] * ec[ 1 ] * rxrx - rxrx * ryry
		);
		var roots = poly.getRootsInInterval( 0, 1 );

		double[][] intersections = new double[ roots.length ][];
		IntStream.range( 0, roots.length ).forEach( i -> intersections[ i ] = Geometry.curvePoint( a, b, c, d, roots[ i ] ) );

		return intersections.length == 0 ? new Intersection2D( Type.NONE ) : new Intersection2D( Type.INTERSECTION, intersections );
	}

	public static Intersection2D intersectBezier3Bezier3( double[] a1, double[] a2, double[] a3, double[] a4, double[] b1, double[] b2, double[] b3, double[] b4 ) {
		boolean sameForward = Arrays.equals( a1, b1 ) && Arrays.equals( a2, b2 ) && Arrays.equals( a3, b3 ) && Arrays.equals( a4, b4 );
		boolean sameBackward = Arrays.equals( a1, b4 ) && Arrays.equals( a2, b3 ) && Arrays.equals( a3, b2 ) && Arrays.equals( a4, b1 );
		if( sameForward || sameBackward ) return new Intersection2D( Type.SAME );

		double[][] coefficientsA = Geometry.curveCoefficients( a1, a2, a3, a4 );
		double[] c13 = coefficientsA[ 0 ];
		double[] c12 = coefficientsA[ 1 ];
		double[] c11 = coefficientsA[ 2 ];
		double[] c10 = coefficientsA[ 3 ];

		double[][] coefficientsB = Geometry.curveCoefficients( b1, b2, b3, b4 );
		double[] c23 = coefficientsB[ 0 ];
		double[] c22 = coefficientsB[ 1 ];
		double[] c21 = coefficientsB[ 2 ];
		double[] c20 = coefficientsB[ 3 ];

		double c10x2 = c10[ 0 ] * c10[ 0 ];
		double c10x3 = c10[ 0 ] * c10[ 0 ] * c10[ 0 ];
		double c10y2 = c10[ 1 ] * c10[ 1 ];
		double c10y3 = c10[ 1 ] * c10[ 1 ] * c10[ 1 ];
		double c11x2 = c11[ 0 ] * c11[ 0 ];
		double c11x3 = c11[ 0 ] * c11[ 0 ] * c11[ 0 ];
		double c11y2 = c11[ 1 ] * c11[ 1 ];
		double c11y3 = c11[ 1 ] * c11[ 1 ] * c11[ 1 ];
		double c12x2 = c12[ 0 ] * c12[ 0 ];
		double c12x3 = c12[ 0 ] * c12[ 0 ] * c12[ 0 ];
		double c12y2 = c12[ 1 ] * c12[ 1 ];
		double c12y3 = c12[ 1 ] * c12[ 1 ] * c12[ 1 ];
		double c13x2 = c13[ 0 ] * c13[ 0 ];
		double c13x3 = c13[ 0 ] * c13[ 0 ] * c13[ 0 ];
		double c13y2 = c13[ 1 ] * c13[ 1 ];
		double c13y3 = c13[ 1 ] * c13[ 1 ] * c13[ 1 ];
		double c20x2 = c20[ 0 ] * c20[ 0 ];
		double c20x3 = c20[ 0 ] * c20[ 0 ] * c20[ 0 ];
		double c20y2 = c20[ 1 ] * c20[ 1 ];
		double c20y3 = c20[ 1 ] * c20[ 1 ] * c20[ 1 ];
		double c21x2 = c21[ 0 ] * c21[ 0 ];
		double c21x3 = c21[ 0 ] * c21[ 0 ] * c21[ 0 ];
		double c21y2 = c21[ 1 ] * c21[ 1 ];
		double c22x2 = c22[ 0 ] * c22[ 0 ];
		double c22x3 = c22[ 0 ] * c22[ 0 ] * c22[ 0 ];
		double c22y2 = c22[ 1 ] * c22[ 1 ];
		double c23x2 = c23[ 0 ] * c23[ 0 ];
		double c23x3 = c23[ 0 ] * c23[ 0 ] * c23[ 0 ];
		double c23y2 = c23[ 1 ] * c23[ 1 ];
		double c23y3 = c23[ 1 ] * c23[ 1 ] * c23[ 1 ];
		Polynomial poly = new Polynomial(
			-c13x3 * c23y3 + c13y3 * c23x3 - 3 * c13[ 0 ] * c13y2 * c23x2 * c23[ 1 ] + 3 * c13x2 * c13[ 1 ] * c23[ 0 ] * c23y2,
			-6 * c13[ 0 ] * c22[ 0 ] * c13y2 * c23[ 0 ] * c23[ 1 ] + 6 * c13x2 * c13[ 1 ] * c22[ 1 ] * c23[ 0 ] * c23[ 1 ] + 3 * c22[ 0 ] * c13y3 * c23x2 - 3 * c13x3 * c22[ 1 ] * c23y2 - 3 * c13[ 0 ] * c13y2 * c22[ 1 ] * c23x2 + 3 * c13x2 * c22[ 0 ] * c13[ 1 ] * c23y2,
			-6 * c21[ 0 ] * c13[ 0 ] * c13y2 * c23[ 0 ] * c23[ 1 ] - 6 * c13[ 0 ] * c22[ 0 ] * c13y2 * c22[ 1 ] * c23[ 0 ] + 6 * c13x2 * c22[ 0 ] * c13[ 1 ] * c22[ 1 ] * c23[ 1 ] + 3 * c21[ 0 ] * c13y3 * c23x2 + 3 * c22x2 * c13y3 * c23[ 0 ] + 3 * c21[ 0 ] * c13x2 * c13[ 1 ] * c23y2 - 3 * c13[ 0 ] * c21[ 1 ] * c13y2 * c23x2 - 3 * c13[ 0 ] * c22x2 * c13y2 * c23[ 1 ] + c13x2 * c13[ 1 ] * c23[ 0 ] * (6 * c21[ 1 ] * c23[ 1 ] + 3 * c22y2) + c13x3 * (-c21[ 1 ] * c23y2 - 2 * c22y2 * c23[ 1 ] - c23[ 1 ] * (2 * c21[ 1 ] * c23[ 1 ] + c22y2)),
			c11[ 0 ] * c12[ 1 ] * c13[ 0 ] * c13[ 1 ] * c23[ 0 ] * c23[ 1 ] - c11[ 1 ] * c12[ 0 ] * c13[ 0 ] * c13[ 1 ] * c23[ 0 ] * c23[ 1 ] + 6 * c21[ 0 ] * c22[ 0 ] * c13y3 * c23[ 0 ] + 3 * c11[ 0 ] * c12[ 0 ] * c13[ 0 ] * c13[ 1 ] * c23y2 + 6 * c10[ 0 ] * c13[ 0 ] * c13y2 * c23[ 0 ] * c23[ 1 ] - 3 * c11[ 0 ] * c12[ 0 ] * c13y2 * c23[ 0 ] * c23[ 1 ] - 3 * c11[ 1 ] * c12[ 1 ] * c13[ 0 ] * c13[ 1 ] * c23x2 - 6 * c10[ 1 ] * c13x2 * c13[ 1 ] * c23[ 0 ] * c23[ 1 ] - 6 * c20[ 0 ] * c13[ 0 ] * c13y2 * c23[ 0 ] * c23[ 1 ] + 3 * c11[ 1 ] * c12[ 1 ] * c13x2 * c23[ 0 ] * c23[ 1 ] - 2 * c12[ 0 ] * c12y2 * c13[ 0 ] * c23[ 0 ] * c23[ 1 ] - 6 * c21[ 0 ] * c13[ 0 ] * c22[ 0 ] * c13y2 * c23[ 1 ] - 6 * c21[ 0 ] * c13[ 0 ] * c13y2 * c22[ 1 ] * c23[ 0 ] - 6 * c13[ 0 ] * c21[ 1 ] * c22[ 0 ] * c13y2 * c23[ 0 ] + 6 * c21[ 0 ] * c13x2 * c13[ 1 ] * c22[ 1 ] * c23[ 1 ] + 2 * c12x2 * c12[ 1 ] * c13[ 1 ] * c23[ 0 ] * c23[ 1 ] + c22x3 * c13y3 - 3 * c10[ 0 ] * c13y3 * c23x2 + 3 * c10[ 1 ] * c13x3 * c23y2 + 3 * c20[ 0 ] * c13y3 * c23x2 + c12y3 * c13[ 0 ] * c23x2 - c12x3 * c13[ 1 ] * c23y2 - 3 * c10[ 0 ] * c13x2 * c13[ 1 ] * c23y2 + 3 * c10[ 1 ] * c13[ 0 ] * c13y2 * c23x2 - 2 * c11[ 0 ] * c12[ 1 ] * c13x2 * c23y2 + c11[ 0 ] * c12[ 1 ] * c13y2 * c23x2 - c11[ 1 ] * c12[ 0 ] * c13x2 * c23y2 + 2 * c11[ 1 ] * c12[ 0 ] * c13y2 * c23x2 + 3 * c20[ 0 ] * c13x2 * c13[ 1 ] * c23y2 - c12[ 0 ] * c12y2 * c13[ 1 ] * c23x2 - 3 * c20[ 1 ] * c13[ 0 ] * c13y2 * c23x2 + c12x2 * c12[ 1 ] * c13[ 0 ] * c23y2 - 3 * c13[ 0 ] * c22x2 * c13y2 * c22[ 1 ] + c13x2 * c13[ 1 ] * c23[ 0 ] * (6 * c20[ 1 ] * c23[ 1 ] + 6 * c21[ 1 ] * c22[ 1 ]) + c13x2 * c22[ 0 ] * c13[ 1 ] * (6 * c21[ 1 ] * c23[ 1 ] + 3 * c22y2) + c13x3 * (-2 * c21[ 1 ] * c22[ 1 ] * c23[ 1 ] - c20[ 1 ] * c23y2 - c22[ 1 ] * (2 * c21[ 1 ] * c23[ 1 ] + c22y2) - c23[ 1 ] * (2 * c20[ 1 ] * c23[ 1 ] + 2 * c21[ 1 ] * c22[ 1 ])),
			6 * c11[ 0 ] * c12[ 0 ] * c13[ 0 ] * c13[ 1 ] * c22[ 1 ] * c23[ 1 ] + c11[ 0 ] * c12[ 1 ] * c13[ 0 ] * c22[ 0 ] * c13[ 1 ] * c23[ 1 ] + c11[ 0 ] * c12[ 1 ] * c13[ 0 ] * c13[ 1 ] * c22[ 1 ] * c23[ 0 ] - c11[ 1 ] * c12[ 0 ] * c13[ 0 ] * c22[ 0 ] * c13[ 1 ] * c23[ 1 ] - c11[ 1 ] * c12[ 0 ] * c13[ 0 ] * c13[ 1 ] * c22[ 1 ] * c23[ 0 ] - 6 * c11[ 1 ] * c12[ 1 ] * c13[ 0 ] * c22[ 0 ] * c13[ 1 ] * c23[ 0 ] - 6 * c10[ 0 ] * c22[ 0 ] * c13y3 * c23[ 0 ] + 6 * c20[ 0 ] * c22[ 0 ] * c13y3 * c23[ 0 ] + 6 * c10[ 1 ] * c13x3 * c22[ 1 ] * c23[ 1 ] + 2 * c12y3 * c13[ 0 ] * c22[ 0 ] * c23[ 0 ] - 2 * c12x3 * c13[ 1 ] * c22[ 1 ] * c23[ 1 ] + 6 * c10[ 0 ] * c13[ 0 ] * c22[ 0 ] * c13y2 * c23[ 1 ] + 6 * c10[ 0 ] * c13[ 0 ] * c13y2 * c22[ 1 ] * c23[ 0 ] + 6 * c10[ 1 ] * c13[ 0 ] * c22[ 0 ] * c13y2 * c23[ 0 ] - 3 * c11[ 0 ] * c12[ 0 ] * c22[ 0 ] * c13y2 * c23[ 1 ] - 3 * c11[ 0 ] * c12[ 0 ] * c13y2 * c22[ 1 ] * c23[ 0 ] + 2 * c11[ 0 ] * c12[ 1 ] * c22[ 0 ] * c13y2 * c23[ 0 ] + 4 * c11[ 1 ] * c12[ 0 ] * c22[ 0 ] * c13y2 * c23[ 0 ] - 6 * c10[ 0 ] * c13x2 * c13[ 1 ] * c22[ 1 ] * c23[ 1 ] - 6 * c10[ 1 ] * c13x2 * c22[ 0 ] * c13[ 1 ] * c23[ 1 ] - 6 * c10[ 1 ] * c13x2 * c13[ 1 ] * c22[ 1 ] * c23[ 0 ] - 4 * c11[ 0 ] * c12[ 1 ] * c13x2 * c22[ 1 ] * c23[ 1 ] - 6 * c20[ 0 ] * c13[ 0 ] * c22[ 0 ] * c13y2 * c23[ 1 ] - 6 * c20[ 0 ] * c13[ 0 ] * c13y2 * c22[ 1 ] * c23[ 0 ] - 2 * c11[ 1 ] * c12[ 0 ] * c13x2 * c22[ 1 ] * c23[ 1 ] + 3 * c11[ 1 ] * c12[ 1 ] * c13x2 * c22[ 0 ] * c23[ 1 ] + 3 * c11[ 1 ] * c12[ 1 ] * c13x2 * c22[ 1 ] * c23[ 0 ] - 2 * c12[ 0 ] * c12y2 * c13[ 0 ] * c22[ 0 ] * c23[ 1 ] - 2 * c12[ 0 ] * c12y2 * c13[ 0 ] * c22[ 1 ] * c23[ 0 ] - 2 * c12[ 0 ] * c12y2 * c22[ 0 ] * c13[ 1 ] * c23[ 0 ] - 6 * c20[ 1 ] * c13[ 0 ] * c22[ 0 ] * c13y2 * c23[ 0 ] - 6 * c21[ 0 ] * c13[ 0 ] * c21[ 1 ] * c13y2 * c23[ 0 ] - 6 * c21[ 0 ] * c13[ 0 ] * c22[ 0 ] * c13y2 * c22[ 1 ] + 6 * c20[ 0 ] * c13x2 * c13[ 1 ] * c22[ 1 ] * c23[ 1 ] + 2 * c12x2 * c12[ 1 ] * c13[ 0 ] * c22[ 1 ] * c23[ 1 ] + 2 * c12x2 * c12[ 1 ] * c22[ 0 ] * c13[ 1 ] * c23[ 1 ] + 2 * c12x2 * c12[ 1 ] * c13[ 1 ] * c22[ 1 ] * c23[ 0 ] + 3 * c21[ 0 ] * c22x2 * c13y3 + 3 * c21x2 * c13y3 * c23[ 0 ] - 3 * c13[ 0 ] * c21[ 1 ] * c22x2 * c13y2 - 3 * c21x2 * c13[ 0 ] * c13y2 * c23[ 1 ] + c13x2 * c22[ 0 ] * c13[ 1 ] * (6 * c20[ 1 ] * c23[ 1 ] + 6 * c21[ 1 ] * c22[ 1 ]) + c13x2 * c13[ 1 ] * c23[ 0 ] * (6 * c20[ 1 ] * c22[ 1 ] + 3 * c21y2) + c21[ 0 ] * c13x2 * c13[ 1 ] * (6 * c21[ 1 ] * c23[ 1 ] + 3 * c22y2) + c13x3 * (-2 * c20[ 1 ] * c22[ 1 ] * c23[ 1 ] - c23[ 1 ] * (2 * c20[ 1 ] * c22[ 1 ] + c21y2) - c21[ 1 ] * (2 * c21[ 1 ] * c23[ 1 ] + c22y2) - c22[ 1 ] * (2 * c20[ 1 ] * c23[ 1 ] + 2 * c21[ 1 ] * c22[ 1 ])),
			c11[ 0 ] * c21[ 0 ] * c12[ 1 ] * c13[ 0 ] * c13[ 1 ] * c23[ 1 ] + c11[ 0 ] * c12[ 1 ] * c13[ 0 ] * c21[ 1 ] * c13[ 1 ] * c23[ 0 ] + c11[ 0 ] * c12[ 1 ] * c13[ 0 ] * c22[ 0 ] * c13[ 1 ] * c22[ 1 ] - c11[ 1 ] * c12[ 0 ] * c21[ 0 ] * c13[ 0 ] * c13[ 1 ] * c23[ 1 ] - c11[ 1 ] * c12[ 0 ] * c13[ 0 ] * c21[ 1 ] * c13[ 1 ] * c23[ 0 ] - c11[ 1 ] * c12[ 0 ] * c13[ 0 ] * c22[ 0 ] * c13[ 1 ] * c22[ 1 ] - 6 * c11[ 1 ] * c21[ 0 ] * c12[ 1 ] * c13[ 0 ] * c13[ 1 ] * c23[ 0 ] - 6 * c10[ 0 ] * c21[ 0 ] * c13y3 * c23[ 0 ] + 6 * c20[ 0 ] * c21[ 0 ] * c13y3 * c23[ 0 ] + 2 * c21[ 0 ] * c12y3 * c13[ 0 ] * c23[ 0 ] + 6 * c10[ 0 ] * c21[ 0 ] * c13[ 0 ] * c13y2 * c23[ 1 ] + 6 * c10[ 0 ] * c13[ 0 ] * c21[ 1 ] * c13y2 * c23[ 0 ] + 6 * c10[ 0 ] * c13[ 0 ] * c22[ 0 ] * c13y2 * c22[ 1 ] + 6 * c10[ 1 ] * c21[ 0 ] * c13[ 0 ] * c13y2 * c23[ 0 ] - 3 * c11[ 0 ] * c12[ 0 ] * c21[ 0 ] * c13y2 * c23[ 1 ] - 3 * c11[ 0 ] * c12[ 0 ] * c21[ 1 ] * c13y2 * c23[ 0 ] - 3 * c11[ 0 ] * c12[ 0 ] * c22[ 0 ] * c13y2 * c22[ 1 ] + 2 * c11[ 0 ] * c21[ 0 ] * c12[ 1 ] * c13y2 * c23[ 0 ] + 4 * c11[ 1 ] * c12[ 0 ] * c21[ 0 ] * c13y2 * c23[ 0 ] - 6 * c10[ 1 ] * c21[ 0 ] * c13x2 * c13[ 1 ] * c23[ 1 ] - 6 * c10[ 1 ] * c13x2 * c21[ 1 ] * c13[ 1 ] * c23[ 0 ] - 6 * c10[ 1 ] * c13x2 * c22[ 0 ] * c13[ 1 ] * c22[ 1 ] - 6 * c20[ 0 ] * c21[ 0 ] * c13[ 0 ] * c13y2 * c23[ 1 ] - 6 * c20[ 0 ] * c13[ 0 ] * c21[ 1 ] * c13y2 * c23[ 0 ] - 6 * c20[ 0 ] * c13[ 0 ] * c22[ 0 ] * c13y2 * c22[ 1 ] + 3 * c11[ 1 ] * c21[ 0 ] * c12[ 1 ] * c13x2 * c23[ 1 ] - 3 * c11[ 1 ] * c12[ 1 ] * c13[ 0 ] * c22x2 * c13[ 1 ] + 3 * c11[ 1 ] * c12[ 1 ] * c13x2 * c21[ 1 ] * c23[ 0 ] + 3 * c11[ 1 ] * c12[ 1 ] * c13x2 * c22[ 0 ] * c22[ 1 ] - 2 * c12[ 0 ] * c21[ 0 ] * c12y2 * c13[ 0 ] * c23[ 1 ] - 2 * c12[ 0 ] * c21[ 0 ] * c12y2 * c13[ 1 ] * c23[ 0 ] - 2 * c12[ 0 ] * c12y2 * c13[ 0 ] * c21[ 1 ] * c23[ 0 ] - 2 * c12[ 0 ] * c12y2 * c13[ 0 ] * c22[ 0 ] * c22[ 1 ] - 6 * c20[ 1 ] * c21[ 0 ] * c13[ 0 ] * c13y2 * c23[ 0 ] - 6 * c21[ 0 ] * c13[ 0 ] * c21[ 1 ] * c22[ 0 ] * c13y2 + 6 * c20[ 1 ] * c13x2 * c21[ 1 ] * c13[ 1 ] * c23[ 0 ] + 2 * c12x2 * c21[ 0 ] * c12[ 1 ] * c13[ 1 ] * c23[ 1 ] + 2 * c12x2 * c12[ 1 ] * c21[ 1 ] * c13[ 1 ] * c23[ 0 ] + 2 * c12x2 * c12[ 1 ] * c22[ 0 ] * c13[ 1 ] * c22[ 1 ] - 3 * c10[ 0 ] * c22x2 * c13y3 + 3 * c20[ 0 ] * c22x2 * c13y3 + 3 * c21x2 * c22[ 0 ] * c13y3 + c12y3 * c13[ 0 ] * c22x2 + 3 * c10[ 1 ] * c13[ 0 ] * c22x2 * c13y2 + c11[ 0 ] * c12[ 1 ] * c22x2 * c13y2 + 2 * c11[ 1 ] * c12[ 0 ] * c22x2 * c13y2 - c12[ 0 ] * c12y2 * c22x2 * c13[ 1 ] - 3 * c20[ 1 ] * c13[ 0 ] * c22x2 * c13y2 - 3 * c21x2 * c13[ 0 ] * c13y2 * c22[ 1 ] + c12x2 * c12[ 1 ] * c13[ 0 ] * (2 * c21[ 1 ] * c23[ 1 ] + c22y2) + c11[ 0 ] * c12[ 0 ] * c13[ 0 ] * c13[ 1 ] * (6 * c21[ 1 ] * c23[ 1 ] + 3 * c22y2) + c21[ 0 ] * c13x2 * c13[ 1 ] * (6 * c20[ 1 ] * c23[ 1 ] + 6 * c21[ 1 ] * c22[ 1 ]) + c12x3 * c13[ 1 ] * (-2 * c21[ 1 ] * c23[ 1 ] - c22y2) + c10[ 1 ] * c13x3 * (6 * c21[ 1 ] * c23[ 1 ] + 3 * c22y2) + c11[ 1 ] * c12[ 0 ] * c13x2 * (-2 * c21[ 1 ] * c23[ 1 ] - c22y2) + c11[ 0 ] * c12[ 1 ] * c13x2 * (-4 * c21[ 1 ] * c23[ 1 ] - 2 * c22y2) + c10[ 0 ] * c13x2 * c13[ 1 ] * (-6 * c21[ 1 ] * c23[ 1 ] - 3 * c22y2) + c13x2 * c22[ 0 ] * c13[ 1 ] * (6 * c20[ 1 ] * c22[ 1 ] + 3 * c21y2) + c20[ 0 ] * c13x2 * c13[ 1 ] * (6 * c21[ 1 ] * c23[ 1 ] + 3 * c22y2) + c13x3 * (-2 * c20[ 1 ] * c21[ 1 ] * c23[ 1 ] - c22[ 1 ] * (2 * c20[ 1 ] * c22[ 1 ] + c21y2) - c20[ 1 ] * (2 * c21[ 1 ] * c23[ 1 ] + c22y2) - c21[ 1 ] * (2 * c20[ 1 ] * c23[ 1 ] + 2 * c21[ 1 ] * c22[ 1 ])),
			-c10[ 0 ] * c11[ 0 ] * c12[ 1 ] * c13[ 0 ] * c13[ 1 ] * c23[ 1 ] + c10[ 0 ] * c11[ 1 ] * c12[ 0 ] * c13[ 0 ] * c13[ 1 ] * c23[ 1 ] + 6 * c10[ 0 ] * c11[ 1 ] * c12[ 1 ] * c13[ 0 ] * c13[ 1 ] * c23[ 0 ] - 6 * c10[ 1 ] * c11[ 0 ] * c12[ 0 ] * c13[ 0 ] * c13[ 1 ] * c23[ 1 ] - c10[ 1 ] * c11[ 0 ] * c12[ 1 ] * c13[ 0 ] * c13[ 1 ] * c23[ 0 ] + c10[ 1 ] * c11[ 1 ] * c12[ 0 ] * c13[ 0 ] * c13[ 1 ] * c23[ 0 ] + c11[ 0 ] * c11[ 1 ] * c12[ 0 ] * c12[ 1 ] * c13[ 0 ] * c23[ 1 ] - c11[ 0 ] * c11[ 1 ] * c12[ 0 ] * c12[ 1 ] * c13[ 1 ] * c23[ 0 ] + c11[ 0 ] * c20[ 0 ] * c12[ 1 ] * c13[ 0 ] * c13[ 1 ] * c23[ 1 ] + c11[ 0 ] * c20[ 1 ] * c12[ 1 ] * c13[ 0 ] * c13[ 1 ] * c23[ 0 ] + c11[ 0 ] * c21[ 0 ] * c12[ 1 ] * c13[ 0 ] * c13[ 1 ] * c22[ 1 ] + c11[ 0 ] * c12[ 1 ] * c13[ 0 ] * c21[ 1 ] * c22[ 0 ] * c13[ 1 ] - c20[ 0 ] * c11[ 1 ] * c12[ 0 ] * c13[ 0 ] * c13[ 1 ] * c23[ 1 ] - 6 * c20[ 0 ] * c11[ 1 ] * c12[ 1 ] * c13[ 0 ] * c13[ 1 ] * c23[ 0 ] - c11[ 1 ] * c12[ 0 ] * c20[ 1 ] * c13[ 0 ] * c13[ 1 ] * c23[ 0 ] - c11[ 1 ] * c12[ 0 ] * c21[ 0 ] * c13[ 0 ] * c13[ 1 ] * c22[ 1 ] - c11[ 1 ] * c12[ 0 ] * c13[ 0 ] * c21[ 1 ] * c22[ 0 ] * c13[ 1 ] - 6 * c11[ 1 ] * c21[ 0 ] * c12[ 1 ] * c13[ 0 ] * c22[ 0 ] * c13[ 1 ] - 6 * c10[ 0 ] * c20[ 0 ] * c13y3 * c23[ 0 ] - 6 * c10[ 0 ] * c21[ 0 ] * c22[ 0 ] * c13y3 - 2 * c10[ 0 ] * c12y3 * c13[ 0 ] * c23[ 0 ] + 6 * c20[ 0 ] * c21[ 0 ] * c22[ 0 ] * c13y3 + 2 * c20[ 0 ] * c12y3 * c13[ 0 ] * c23[ 0 ] + 2 * c21[ 0 ] * c12y3 * c13[ 0 ] * c22[ 0 ] + 2 * c10[ 1 ] * c12x3 * c13[ 1 ] * c23[ 1 ] - 6 * c10[ 0 ] * c10[ 1 ] * c13[ 0 ] * c13y2 * c23[ 0 ] + 3 * c10[ 0 ] * c11[ 0 ] * c12[ 0 ] * c13y2 * c23[ 1 ] - 2 * c10[ 0 ] * c11[ 0 ] * c12[ 1 ] * c13y2 * c23[ 0 ] - 4 * c10[ 0 ] * c11[ 1 ] * c12[ 0 ] * c13y2 * c23[ 0 ] + 3 * c10[ 1 ] * c11[ 0 ] * c12[ 0 ] * c13y2 * c23[ 0 ] + 6 * c10[ 0 ] * c10[ 1 ] * c13x2 * c13[ 1 ] * c23[ 1 ] + 6 * c10[ 0 ] * c20[ 0 ] * c13[ 0 ] * c13y2 * c23[ 1 ] - 3 * c10[ 0 ] * c11[ 1 ] * c12[ 1 ] * c13x2 * c23[ 1 ] + 2 * c10[ 0 ] * c12[ 0 ] * c12y2 * c13[ 0 ] * c23[ 1 ] + 2 * c10[ 0 ] * c12[ 0 ] * c12y2 * c13[ 1 ] * c23[ 0 ] + 6 * c10[ 0 ] * c20[ 1 ] * c13[ 0 ] * c13y2 * c23[ 0 ] + 6 * c10[ 0 ] * c21[ 0 ] * c13[ 0 ] * c13y2 * c22[ 1 ] + 6 * c10[ 0 ] * c13[ 0 ] * c21[ 1 ] * c22[ 0 ] * c13y2 + 4 * c10[ 1 ] * c11[ 0 ] * c12[ 1 ] * c13x2 * c23[ 1 ] + 6 * c10[ 1 ] * c20[ 0 ] * c13[ 0 ] * c13y2 * c23[ 0 ] + 2 * c10[ 1 ] * c11[ 1 ] * c12[ 0 ] * c13x2 * c23[ 1 ] - 3 * c10[ 1 ] * c11[ 1 ] * c12[ 1 ] * c13x2 * c23[ 0 ] + 2 * c10[ 1 ] * c12[ 0 ] * c12y2 * c13[ 0 ] * c23[ 0 ] + 6 * c10[ 1 ] * c21[ 0 ] * c13[ 0 ] * c22[ 0 ] * c13y2 - 3 * c11[ 0 ] * c20[ 0 ] * c12[ 0 ] * c13y2 * c23[ 1 ] + 2 * c11[ 0 ] * c20[ 0 ] * c12[ 1 ] * c13y2 * c23[ 0 ] + c11[ 0 ] * c11[ 1 ] * c12y2 * c13[ 0 ] * c23[ 0 ] - 3 * c11[ 0 ] * c12[ 0 ] * c20[ 1 ] * c13y2 * c23[ 0 ] - 3 * c11[ 0 ] * c12[ 0 ] * c21[ 0 ] * c13y2 * c22[ 1 ] - 3 * c11[ 0 ] * c12[ 0 ] * c21[ 1 ] * c22[ 0 ] * c13y2 + 2 * c11[ 0 ] * c21[ 0 ] * c12[ 1 ] * c22[ 0 ] * c13y2 + 4 * c20[ 0 ] * c11[ 1 ] * c12[ 0 ] * c13y2 * c23[ 0 ] + 4 * c11[ 1 ] * c12[ 0 ] * c21[ 0 ] * c22[ 0 ] * c13y2 - 2 * c10[ 0 ] * c12x2 * c12[ 1 ] * c13[ 1 ] * c23[ 1 ] - 6 * c10[ 1 ] * c20[ 0 ] * c13x2 * c13[ 1 ] * c23[ 1 ] - 6 * c10[ 1 ] * c20[ 1 ] * c13x2 * c13[ 1 ] * c23[ 0 ] - 6 * c10[ 1 ] * c21[ 0 ] * c13x2 * c13[ 1 ] * c22[ 1 ] - 2 * c10[ 1 ] * c12x2 * c12[ 1 ] * c13[ 0 ] * c23[ 1 ] - 2 * c10[ 1 ] * c12x2 * c12[ 1 ] * c13[ 1 ] * c23[ 0 ] - 6 * c10[ 1 ] * c13x2 * c21[ 1 ] * c22[ 0 ] * c13[ 1 ] - c11[ 0 ] * c11[ 1 ] * c12x2 * c13[ 1 ] * c23[ 1 ] - 2 * c11[ 0 ] * c11y2 * c13[ 0 ] * c13[ 1 ] * c23[ 0 ] + 3 * c20[ 0 ] * c11[ 1 ] * c12[ 1 ] * c13x2 * c23[ 1 ] - 2 * c20[ 0 ] * c12[ 0 ] * c12y2 * c13[ 0 ] * c23[ 1 ] - 2 * c20[ 0 ] * c12[ 0 ] * c12y2 * c13[ 1 ] * c23[ 0 ] - 6 * c20[ 0 ] * c20[ 1 ] * c13[ 0 ] * c13y2 * c23[ 0 ] - 6 * c20[ 0 ] * c21[ 0 ] * c13[ 0 ] * c13y2 * c22[ 1 ] - 6 * c20[ 0 ] * c13[ 0 ] * c21[ 1 ] * c22[ 0 ] * c13y2 + 3 * c11[ 1 ] * c20[ 1 ] * c12[ 1 ] * c13x2 * c23[ 0 ] + 3 * c11[ 1 ] * c21[ 0 ] * c12[ 1 ] * c13x2 * c22[ 1 ] + 3 * c11[ 1 ] * c12[ 1 ] * c13x2 * c21[ 1 ] * c22[ 0 ] - 2 * c12[ 0 ] * c20[ 1 ] * c12y2 * c13[ 0 ] * c23[ 0 ] - 2 * c12[ 0 ] * c21[ 0 ] * c12y2 * c13[ 0 ] * c22[ 1 ] - 2 * c12[ 0 ] * c21[ 0 ] * c12y2 * c22[ 0 ] * c13[ 1 ] - 2 * c12[ 0 ] * c12y2 * c13[ 0 ] * c21[ 1 ] * c22[ 0 ] - 6 * c20[ 1 ] * c21[ 0 ] * c13[ 0 ] * c22[ 0 ] * c13y2 - c11y2 * c12[ 0 ] * c12[ 1 ] * c13[ 0 ] * c23[ 0 ] + 2 * c20[ 0 ] * c12x2 * c12[ 1 ] * c13[ 1 ] * c23[ 1 ] + 6 * c20[ 1 ] * c13x2 * c21[ 1 ] * c22[ 0 ] * c13[ 1 ] + 2 * c11x2 * c11[ 1 ] * c13[ 0 ] * c13[ 1 ] * c23[ 1 ] + c11x2 * c12[ 0 ] * c12[ 1 ] * c13[ 1 ] * c23[ 1 ] + 2 * c12x2 * c20[ 1 ] * c12[ 1 ] * c13[ 1 ] * c23[ 0 ] + 2 * c12x2 * c21[ 0 ] * c12[ 1 ] * c13[ 1 ] * c22[ 1 ] + 2 * c12x2 * c12[ 1 ] * c21[ 1 ] * c22[ 0 ] * c13[ 1 ] + c21x3 * c13y3 + 3 * c10x2 * c13y3 * c23[ 0 ] - 3 * c10y2 * c13x3 * c23[ 1 ] + 3 * c20x2 * c13y3 * c23[ 0 ] + c11y3 * c13x2 * c23[ 0 ] - c11x3 * c13y2 * c23[ 1 ] - c11[ 0 ] * c11y2 * c13x2 * c23[ 1 ] + c11x2 * c11[ 1 ] * c13y2 * c23[ 0 ] - 3 * c10x2 * c13[ 0 ] * c13y2 * c23[ 1 ] + 3 * c10y2 * c13x2 * c13[ 1 ] * c23[ 0 ] - c11x2 * c12y2 * c13[ 0 ] * c23[ 1 ] + c11y2 * c12x2 * c13[ 1 ] * c23[ 0 ] - 3 * c21x2 * c13[ 0 ] * c21[ 1 ] * c13y2 - 3 * c20x2 * c13[ 0 ] * c13y2 * c23[ 1 ] + 3 * c20y2 * c13x2 * c13[ 1 ] * c23[ 0 ] + c11[ 0 ] * c12[ 0 ] * c13[ 0 ] * c13[ 1 ] * (6 * c20[ 1 ] * c23[ 1 ] + 6 * c21[ 1 ] * c22[ 1 ]) + c12x3 * c13[ 1 ] * (-2 * c20[ 1 ] * c23[ 1 ] - 2 * c21[ 1 ] * c22[ 1 ]) + c10[ 1 ] * c13x3 * (6 * c20[ 1 ] * c23[ 1 ] + 6 * c21[ 1 ] * c22[ 1 ]) + c11[ 1 ] * c12[ 0 ] * c13x2 * (-2 * c20[ 1 ] * c23[ 1 ] - 2 * c21[ 1 ] * c22[ 1 ]) + c12x2 * c12[ 1 ] * c13[ 0 ] * (2 * c20[ 1 ] * c23[ 1 ] + 2 * c21[ 1 ] * c22[ 1 ]) + c11[ 0 ] * c12[ 1 ] * c13x2 * (-4 * c20[ 1 ] * c23[ 1 ] - 4 * c21[ 1 ] * c22[ 1 ]) + c10[ 0 ] * c13x2 * c13[ 1 ] * (-6 * c20[ 1 ] * c23[ 1 ] - 6 * c21[ 1 ] * c22[ 1 ]) + c20[ 0 ] * c13x2 * c13[ 1 ] * (6 * c20[ 1 ] * c23[ 1 ] + 6 * c21[ 1 ] * c22[ 1 ]) + c21[ 0 ] * c13x2 * c13[ 1 ] * (6 * c20[ 1 ] * c22[ 1 ] + 3 * c21y2) + c13x3 * (-2 * c20[ 1 ] * c21[ 1 ] * c22[ 1 ] - c20y2 * c23[ 1 ] - c21[ 1 ] * (2 * c20[ 1 ] * c22[ 1 ] + c21y2) - c20[ 1 ] * (2 * c20[ 1 ] * c23[ 1 ] + 2 * c21[ 1 ] * c22[ 1 ])),
			-c10[ 0 ] * c11[ 0 ] * c12[ 1 ] * c13[ 0 ] * c13[ 1 ] * c22[ 1 ] + c10[ 0 ] * c11[ 1 ] * c12[ 0 ] * c13[ 0 ] * c13[ 1 ] * c22[ 1 ] + 6 * c10[ 0 ] * c11[ 1 ] * c12[ 1 ] * c13[ 0 ] * c22[ 0 ] * c13[ 1 ] - 6 * c10[ 1 ] * c11[ 0 ] * c12[ 0 ] * c13[ 0 ] * c13[ 1 ] * c22[ 1 ] - c10[ 1 ] * c11[ 0 ] * c12[ 1 ] * c13[ 0 ] * c22[ 0 ] * c13[ 1 ] + c10[ 1 ] * c11[ 1 ] * c12[ 0 ] * c13[ 0 ] * c22[ 0 ] * c13[ 1 ] + c11[ 0 ] * c11[ 1 ] * c12[ 0 ] * c12[ 1 ] * c13[ 0 ] * c22[ 1 ] - c11[ 0 ] * c11[ 1 ] * c12[ 0 ] * c12[ 1 ] * c22[ 0 ] * c13[ 1 ] + c11[ 0 ] * c20[ 0 ] * c12[ 1 ] * c13[ 0 ] * c13[ 1 ] * c22[ 1 ] + c11[ 0 ] * c20[ 1 ] * c12[ 1 ] * c13[ 0 ] * c22[ 0 ] * c13[ 1 ] + c11[ 0 ] * c21[ 0 ] * c12[ 1 ] * c13[ 0 ] * c21[ 1 ] * c13[ 1 ] - c20[ 0 ] * c11[ 1 ] * c12[ 0 ] * c13[ 0 ] * c13[ 1 ] * c22[ 1 ] - 6 * c20[ 0 ] * c11[ 1 ] * c12[ 1 ] * c13[ 0 ] * c22[ 0 ] * c13[ 1 ] - c11[ 1 ] * c12[ 0 ] * c20[ 1 ] * c13[ 0 ] * c22[ 0 ] * c13[ 1 ] - c11[ 1 ] * c12[ 0 ] * c21[ 0 ] * c13[ 0 ] * c21[ 1 ] * c13[ 1 ] - 6 * c10[ 0 ] * c20[ 0 ] * c22[ 0 ] * c13y3 - 2 * c10[ 0 ] * c12y3 * c13[ 0 ] * c22[ 0 ] + 2 * c20[ 0 ] * c12y3 * c13[ 0 ] * c22[ 0 ] + 2 * c10[ 1 ] * c12x3 * c13[ 1 ] * c22[ 1 ] - 6 * c10[ 0 ] * c10[ 1 ] * c13[ 0 ] * c22[ 0 ] * c13y2 + 3 * c10[ 0 ] * c11[ 0 ] * c12[ 0 ] * c13y2 * c22[ 1 ] - 2 * c10[ 0 ] * c11[ 0 ] * c12[ 1 ] * c22[ 0 ] * c13y2 - 4 * c10[ 0 ] * c11[ 1 ] * c12[ 0 ] * c22[ 0 ] * c13y2 + 3 * c10[ 1 ] * c11[ 0 ] * c12[ 0 ] * c22[ 0 ] * c13y2 + 6 * c10[ 0 ] * c10[ 1 ] * c13x2 * c13[ 1 ] * c22[ 1 ] + 6 * c10[ 0 ] * c20[ 0 ] * c13[ 0 ] * c13y2 * c22[ 1 ] - 3 * c10[ 0 ] * c11[ 1 ] * c12[ 1 ] * c13x2 * c22[ 1 ] + 2 * c10[ 0 ] * c12[ 0 ] * c12y2 * c13[ 0 ] * c22[ 1 ] + 2 * c10[ 0 ] * c12[ 0 ] * c12y2 * c22[ 0 ] * c13[ 1 ] + 6 * c10[ 0 ] * c20[ 1 ] * c13[ 0 ] * c22[ 0 ] * c13y2 + 6 * c10[ 0 ] * c21[ 0 ] * c13[ 0 ] * c21[ 1 ] * c13y2 + 4 * c10[ 1 ] * c11[ 0 ] * c12[ 1 ] * c13x2 * c22[ 1 ] + 6 * c10[ 1 ] * c20[ 0 ] * c13[ 0 ] * c22[ 0 ] * c13y2 + 2 * c10[ 1 ] * c11[ 1 ] * c12[ 0 ] * c13x2 * c22[ 1 ] - 3 * c10[ 1 ] * c11[ 1 ] * c12[ 1 ] * c13x2 * c22[ 0 ] + 2 * c10[ 1 ] * c12[ 0 ] * c12y2 * c13[ 0 ] * c22[ 0 ] - 3 * c11[ 0 ] * c20[ 0 ] * c12[ 0 ] * c13y2 * c22[ 1 ] + 2 * c11[ 0 ] * c20[ 0 ] * c12[ 1 ] * c22[ 0 ] * c13y2 + c11[ 0 ] * c11[ 1 ] * c12y2 * c13[ 0 ] * c22[ 0 ] - 3 * c11[ 0 ] * c12[ 0 ] * c20[ 1 ] * c22[ 0 ] * c13y2 - 3 * c11[ 0 ] * c12[ 0 ] * c21[ 0 ] * c21[ 1 ] * c13y2 + 4 * c20[ 0 ] * c11[ 1 ] * c12[ 0 ] * c22[ 0 ] * c13y2 - 2 * c10[ 0 ] * c12x2 * c12[ 1 ] * c13[ 1 ] * c22[ 1 ] - 6 * c10[ 1 ] * c20[ 0 ] * c13x2 * c13[ 1 ] * c22[ 1 ] - 6 * c10[ 1 ] * c20[ 1 ] * c13x2 * c22[ 0 ] * c13[ 1 ] - 6 * c10[ 1 ] * c21[ 0 ] * c13x2 * c21[ 1 ] * c13[ 1 ] - 2 * c10[ 1 ] * c12x2 * c12[ 1 ] * c13[ 0 ] * c22[ 1 ] - 2 * c10[ 1 ] * c12x2 * c12[ 1 ] * c22[ 0 ] * c13[ 1 ] - c11[ 0 ] * c11[ 1 ] * c12x2 * c13[ 1 ] * c22[ 1 ] - 2 * c11[ 0 ] * c11y2 * c13[ 0 ] * c22[ 0 ] * c13[ 1 ] + 3 * c20[ 0 ] * c11[ 1 ] * c12[ 1 ] * c13x2 * c22[ 1 ] - 2 * c20[ 0 ] * c12[ 0 ] * c12y2 * c13[ 0 ] * c22[ 1 ] - 2 * c20[ 0 ] * c12[ 0 ] * c12y2 * c22[ 0 ] * c13[ 1 ] - 6 * c20[ 0 ] * c20[ 1 ] * c13[ 0 ] * c22[ 0 ] * c13y2 - 6 * c20[ 0 ] * c21[ 0 ] * c13[ 0 ] * c21[ 1 ] * c13y2 + 3 * c11[ 1 ] * c20[ 1 ] * c12[ 1 ] * c13x2 * c22[ 0 ] + 3 * c11[ 1 ] * c21[ 0 ] * c12[ 1 ] * c13x2 * c21[ 1 ] - 2 * c12[ 0 ] * c20[ 1 ] * c12y2 * c13[ 0 ] * c22[ 0 ] - 2 * c12[ 0 ] * c21[ 0 ] * c12y2 * c13[ 0 ] * c21[ 1 ] - c11y2 * c12[ 0 ] * c12[ 1 ] * c13[ 0 ] * c22[ 0 ] + 2 * c20[ 0 ] * c12x2 * c12[ 1 ] * c13[ 1 ] * c22[ 1 ] - 3 * c11[ 1 ] * c21x2 * c12[ 1 ] * c13[ 0 ] * c13[ 1 ] + 6 * c20[ 1 ] * c21[ 0 ] * c13x2 * c21[ 1 ] * c13[ 1 ] + 2 * c11x2 * c11[ 1 ] * c13[ 0 ] * c13[ 1 ] * c22[ 1 ] + c11x2 * c12[ 0 ] * c12[ 1 ] * c13[ 1 ] * c22[ 1 ] + 2 * c12x2 * c20[ 1 ] * c12[ 1 ] * c22[ 0 ] * c13[ 1 ] + 2 * c12x2 * c21[ 0 ] * c12[ 1 ] * c21[ 1 ] * c13[ 1 ] - 3 * c10[ 0 ] * c21x2 * c13y3 + 3 * c20[ 0 ] * c21x2 * c13y3 + 3 * c10x2 * c22[ 0 ] * c13y3 - 3 * c10y2 * c13x3 * c22[ 1 ] + 3 * c20x2 * c22[ 0 ] * c13y3 + c21x2 * c12y3 * c13[ 0 ] + c11y3 * c13x2 * c22[ 0 ] - c11x3 * c13y2 * c22[ 1 ] + 3 * c10[ 1 ] * c21x2 * c13[ 0 ] * c13y2 - c11[ 0 ] * c11y2 * c13x2 * c22[ 1 ] + c11[ 0 ] * c21x2 * c12[ 1 ] * c13y2 + 2 * c11[ 1 ] * c12[ 0 ] * c21x2 * c13y2 + c11x2 * c11[ 1 ] * c22[ 0 ] * c13y2 - c12[ 0 ] * c21x2 * c12y2 * c13[ 1 ] - 3 * c20[ 1 ] * c21x2 * c13[ 0 ] * c13y2 - 3 * c10x2 * c13[ 0 ] * c13y2 * c22[ 1 ] + 3 * c10y2 * c13x2 * c22[ 0 ] * c13[ 1 ] - c11x2 * c12y2 * c13[ 0 ] * c22[ 1 ] + c11y2 * c12x2 * c22[ 0 ] * c13[ 1 ] - 3 * c20x2 * c13[ 0 ] * c13y2 * c22[ 1 ] + 3 * c20y2 * c13x2 * c22[ 0 ] * c13[ 1 ] + c12x2 * c12[ 1 ] * c13[ 0 ] * (2 * c20[ 1 ] * c22[ 1 ] + c21y2) + c11[ 0 ] * c12[ 0 ] * c13[ 0 ] * c13[ 1 ] * (6 * c20[ 1 ] * c22[ 1 ] + 3 * c21y2) + c12x3 * c13[ 1 ] * (-2 * c20[ 1 ] * c22[ 1 ] - c21y2) + c10[ 1 ] * c13x3 * (6 * c20[ 1 ] * c22[ 1 ] + 3 * c21y2) + c11[ 1 ] * c12[ 0 ] * c13x2 * (-2 * c20[ 1 ] * c22[ 1 ] - c21y2) + c11[ 0 ] * c12[ 1 ] * c13x2 * (-4 * c20[ 1 ] * c22[ 1 ] - 2 * c21y2) + c10[ 0 ] * c13x2 * c13[ 1 ] * (-6 * c20[ 1 ] * c22[ 1 ] - 3 * c21y2) + c20[ 0 ] * c13x2 * c13[ 1 ] * (6 * c20[ 1 ] * c22[ 1 ] + 3 * c21y2) + c13x3 * (-2 * c20[ 1 ] * c21y2 - c20y2 * c22[ 1 ] - c20[ 1 ] * (2 * c20[ 1 ] * c22[ 1 ] + c21y2)),
			-c10[ 0 ] * c11[ 0 ] * c12[ 1 ] * c13[ 0 ] * c21[ 1 ] * c13[ 1 ] + c10[ 0 ] * c11[ 1 ] * c12[ 0 ] * c13[ 0 ] * c21[ 1 ] * c13[ 1 ] + 6 * c10[ 0 ] * c11[ 1 ] * c21[ 0 ] * c12[ 1 ] * c13[ 0 ] * c13[ 1 ] - 6 * c10[ 1 ] * c11[ 0 ] * c12[ 0 ] * c13[ 0 ] * c21[ 1 ] * c13[ 1 ] - c10[ 1 ] * c11[ 0 ] * c21[ 0 ] * c12[ 1 ] * c13[ 0 ] * c13[ 1 ] + c10[ 1 ] * c11[ 1 ] * c12[ 0 ] * c21[ 0 ] * c13[ 0 ] * c13[ 1 ] - c11[ 0 ] * c11[ 1 ] * c12[ 0 ] * c21[ 0 ] * c12[ 1 ] * c13[ 1 ] + c11[ 0 ] * c11[ 1 ] * c12[ 0 ] * c12[ 1 ] * c13[ 0 ] * c21[ 1 ] + c11[ 0 ] * c20[ 0 ] * c12[ 1 ] * c13[ 0 ] * c21[ 1 ] * c13[ 1 ] + 6 * c11[ 0 ] * c12[ 0 ] * c20[ 1 ] * c13[ 0 ] * c21[ 1 ] * c13[ 1 ] + c11[ 0 ] * c20[ 1 ] * c21[ 0 ] * c12[ 1 ] * c13[ 0 ] * c13[ 1 ] - c20[ 0 ] * c11[ 1 ] * c12[ 0 ] * c13[ 0 ] * c21[ 1 ] * c13[ 1 ] - 6 * c20[ 0 ] * c11[ 1 ] * c21[ 0 ] * c12[ 1 ] * c13[ 0 ] * c13[ 1 ] - c11[ 1 ] * c12[ 0 ] * c20[ 1 ] * c21[ 0 ] * c13[ 0 ] * c13[ 1 ] - 6 * c10[ 0 ] * c20[ 0 ] * c21[ 0 ] * c13y3 - 2 * c10[ 0 ] * c21[ 0 ] * c12y3 * c13[ 0 ] + 6 * c10[ 1 ] * c20[ 1 ] * c13x3 * c21[ 1 ] + 2 * c20[ 0 ] * c21[ 0 ] * c12y3 * c13[ 0 ] + 2 * c10[ 1 ] * c12x3 * c21[ 1 ] * c13[ 1 ] - 2 * c12x3 * c20[ 1 ] * c21[ 1 ] * c13[ 1 ] - 6 * c10[ 0 ] * c10[ 1 ] * c21[ 0 ] * c13[ 0 ] * c13y2 + 3 * c10[ 0 ] * c11[ 0 ] * c12[ 0 ] * c21[ 1 ] * c13y2 - 2 * c10[ 0 ] * c11[ 0 ] * c21[ 0 ] * c12[ 1 ] * c13y2 - 4 * c10[ 0 ] * c11[ 1 ] * c12[ 0 ] * c21[ 0 ] * c13y2 + 3 * c10[ 1 ] * c11[ 0 ] * c12[ 0 ] * c21[ 0 ] * c13y2 + 6 * c10[ 0 ] * c10[ 1 ] * c13x2 * c21[ 1 ] * c13[ 1 ] + 6 * c10[ 0 ] * c20[ 0 ] * c13[ 0 ] * c21[ 1 ] * c13y2 - 3 * c10[ 0 ] * c11[ 1 ] * c12[ 1 ] * c13x2 * c21[ 1 ] + 2 * c10[ 0 ] * c12[ 0 ] * c21[ 0 ] * c12y2 * c13[ 1 ] + 2 * c10[ 0 ] * c12[ 0 ] * c12y2 * c13[ 0 ] * c21[ 1 ] + 6 * c10[ 0 ] * c20[ 1 ] * c21[ 0 ] * c13[ 0 ] * c13y2 + 4 * c10[ 1 ] * c11[ 0 ] * c12[ 1 ] * c13x2 * c21[ 1 ] + 6 * c10[ 1 ] * c20[ 0 ] * c21[ 0 ] * c13[ 0 ] * c13y2 + 2 * c10[ 1 ] * c11[ 1 ] * c12[ 0 ] * c13x2 * c21[ 1 ] - 3 * c10[ 1 ] * c11[ 1 ] * c21[ 0 ] * c12[ 1 ] * c13x2 + 2 * c10[ 1 ] * c12[ 0 ] * c21[ 0 ] * c12y2 * c13[ 0 ] - 3 * c11[ 0 ] * c20[ 0 ] * c12[ 0 ] * c21[ 1 ] * c13y2 + 2 * c11[ 0 ] * c20[ 0 ] * c21[ 0 ] * c12[ 1 ] * c13y2 + c11[ 0 ] * c11[ 1 ] * c21[ 0 ] * c12y2 * c13[ 0 ] - 3 * c11[ 0 ] * c12[ 0 ] * c20[ 1 ] * c21[ 0 ] * c13y2 + 4 * c20[ 0 ] * c11[ 1 ] * c12[ 0 ] * c21[ 0 ] * c13y2 - 6 * c10[ 0 ] * c20[ 1 ] * c13x2 * c21[ 1 ] * c13[ 1 ] - 2 * c10[ 0 ] * c12x2 * c12[ 1 ] * c21[ 1 ] * c13[ 1 ] - 6 * c10[ 1 ] * c20[ 0 ] * c13x2 * c21[ 1 ] * c13[ 1 ] - 6 * c10[ 1 ] * c20[ 1 ] * c21[ 0 ] * c13x2 * c13[ 1 ] - 2 * c10[ 1 ] * c12x2 * c21[ 0 ] * c12[ 1 ] * c13[ 1 ] - 2 * c10[ 1 ] * c12x2 * c12[ 1 ] * c13[ 0 ] * c21[ 1 ] - c11[ 0 ] * c11[ 1 ] * c12x2 * c21[ 1 ] * c13[ 1 ] - 4 * c11[ 0 ] * c20[ 1 ] * c12[ 1 ] * c13x2 * c21[ 1 ] - 2 * c11[ 0 ] * c11y2 * c21[ 0 ] * c13[ 0 ] * c13[ 1 ] + 3 * c20[ 0 ] * c11[ 1 ] * c12[ 1 ] * c13x2 * c21[ 1 ] - 2 * c20[ 0 ] * c12[ 0 ] * c21[ 0 ] * c12y2 * c13[ 1 ] - 2 * c20[ 0 ] * c12[ 0 ] * c12y2 * c13[ 0 ] * c21[ 1 ] - 6 * c20[ 0 ] * c20[ 1 ] * c21[ 0 ] * c13[ 0 ] * c13y2 - 2 * c11[ 1 ] * c12[ 0 ] * c20[ 1 ] * c13x2 * c21[ 1 ] + 3 * c11[ 1 ] * c20[ 1 ] * c21[ 0 ] * c12[ 1 ] * c13x2 - 2 * c12[ 0 ] * c20[ 1 ] * c21[ 0 ] * c12y2 * c13[ 0 ] - c11y2 * c12[ 0 ] * c21[ 0 ] * c12[ 1 ] * c13[ 0 ] + 6 * c20[ 0 ] * c20[ 1 ] * c13x2 * c21[ 1 ] * c13[ 1 ] + 2 * c20[ 0 ] * c12x2 * c12[ 1 ] * c21[ 1 ] * c13[ 1 ] + 2 * c11x2 * c11[ 1 ] * c13[ 0 ] * c21[ 1 ] * c13[ 1 ] + c11x2 * c12[ 0 ] * c12[ 1 ] * c21[ 1 ] * c13[ 1 ] + 2 * c12x2 * c20[ 1 ] * c21[ 0 ] * c12[ 1 ] * c13[ 1 ] + 2 * c12x2 * c20[ 1 ] * c12[ 1 ] * c13[ 0 ] * c21[ 1 ] + 3 * c10x2 * c21[ 0 ] * c13y3 - 3 * c10y2 * c13x3 * c21[ 1 ] + 3 * c20x2 * c21[ 0 ] * c13y3 + c11y3 * c21[ 0 ] * c13x2 - c11x3 * c21[ 1 ] * c13y2 - 3 * c20y2 * c13x3 * c21[ 1 ] - c11[ 0 ] * c11y2 * c13x2 * c21[ 1 ] + c11x2 * c11[ 1 ] * c21[ 0 ] * c13y2 - 3 * c10x2 * c13[ 0 ] * c21[ 1 ] * c13y2 + 3 * c10y2 * c21[ 0 ] * c13x2 * c13[ 1 ] - c11x2 * c12y2 * c13[ 0 ] * c21[ 1 ] + c11y2 * c12x2 * c21[ 0 ] * c13[ 1 ] - 3 * c20x2 * c13[ 0 ] * c21[ 1 ] * c13y2 + 3 * c20y2 * c21[ 0 ] * c13x2 * c13[ 1 ],
			c10[ 0 ] * c10[ 1 ] * c11[ 0 ] * c12[ 1 ] * c13[ 0 ] * c13[ 1 ] - c10[ 0 ] * c10[ 1 ] * c11[ 1 ] * c12[ 0 ] * c13[ 0 ] * c13[ 1 ] + c10[ 0 ] * c11[ 0 ] * c11[ 1 ] * c12[ 0 ] * c12[ 1 ] * c13[ 1 ] - c10[ 1 ] * c11[ 0 ] * c11[ 1 ] * c12[ 0 ] * c12[ 1 ] * c13[ 0 ] - c10[ 0 ] * c11[ 0 ] * c20[ 1 ] * c12[ 1 ] * c13[ 0 ] * c13[ 1 ] + 6 * c10[ 0 ] * c20[ 0 ] * c11[ 1 ] * c12[ 1 ] * c13[ 0 ] * c13[ 1 ] + c10[ 0 ] * c11[ 1 ] * c12[ 0 ] * c20[ 1 ] * c13[ 0 ] * c13[ 1 ] - c10[ 1 ] * c11[ 0 ] * c20[ 0 ] * c12[ 1 ] * c13[ 0 ] * c13[ 1 ] - 6 * c10[ 1 ] * c11[ 0 ] * c12[ 0 ] * c20[ 1 ] * c13[ 0 ] * c13[ 1 ] + c10[ 1 ] * c20[ 0 ] * c11[ 1 ] * c12[ 0 ] * c13[ 0 ] * c13[ 1 ] - c11[ 0 ] * c20[ 0 ] * c11[ 1 ] * c12[ 0 ] * c12[ 1 ] * c13[ 1 ] + c11[ 0 ] * c11[ 1 ] * c12[ 0 ] * c20[ 1 ] * c12[ 1 ] * c13[ 0 ] + c11[ 0 ] * c20[ 0 ] * c20[ 1 ] * c12[ 1 ] * c13[ 0 ] * c13[ 1 ] - c20[ 0 ] * c11[ 1 ] * c12[ 0 ] * c20[ 1 ] * c13[ 0 ] * c13[ 1 ] - 2 * c10[ 0 ] * c20[ 0 ] * c12y3 * c13[ 0 ] + 2 * c10[ 1 ] * c12x3 * c20[ 1 ] * c13[ 1 ] - 3 * c10[ 0 ] * c10[ 1 ] * c11[ 0 ] * c12[ 0 ] * c13y2 - 6 * c10[ 0 ] * c10[ 1 ] * c20[ 0 ] * c13[ 0 ] * c13y2 + 3 * c10[ 0 ] * c10[ 1 ] * c11[ 1 ] * c12[ 1 ] * c13x2 - 2 * c10[ 0 ] * c10[ 1 ] * c12[ 0 ] * c12y2 * c13[ 0 ] - 2 * c10[ 0 ] * c11[ 0 ] * c20[ 0 ] * c12[ 1 ] * c13y2 - c10[ 0 ] * c11[ 0 ] * c11[ 1 ] * c12y2 * c13[ 0 ] + 3 * c10[ 0 ] * c11[ 0 ] * c12[ 0 ] * c20[ 1 ] * c13y2 - 4 * c10[ 0 ] * c20[ 0 ] * c11[ 1 ] * c12[ 0 ] * c13y2 + 3 * c10[ 1 ] * c11[ 0 ] * c20[ 0 ] * c12[ 0 ] * c13y2 + 6 * c10[ 0 ] * c10[ 1 ] * c20[ 1 ] * c13x2 * c13[ 1 ] + 2 * c10[ 0 ] * c10[ 1 ] * c12x2 * c12[ 1 ] * c13[ 1 ] + 2 * c10[ 0 ] * c11[ 0 ] * c11y2 * c13[ 0 ] * c13[ 1 ] + 2 * c10[ 0 ] * c20[ 0 ] * c12[ 0 ] * c12y2 * c13[ 1 ] + 6 * c10[ 0 ] * c20[ 0 ] * c20[ 1 ] * c13[ 0 ] * c13y2 - 3 * c10[ 0 ] * c11[ 1 ] * c20[ 1 ] * c12[ 1 ] * c13x2 + 2 * c10[ 0 ] * c12[ 0 ] * c20[ 1 ] * c12y2 * c13[ 0 ] + c10[ 0 ] * c11y2 * c12[ 0 ] * c12[ 1 ] * c13[ 0 ] + c10[ 1 ] * c11[ 0 ] * c11[ 1 ] * c12x2 * c13[ 1 ] + 4 * c10[ 1 ] * c11[ 0 ] * c20[ 1 ] * c12[ 1 ] * c13x2 - 3 * c10[ 1 ] * c20[ 0 ] * c11[ 1 ] * c12[ 1 ] * c13x2 + 2 * c10[ 1 ] * c20[ 0 ] * c12[ 0 ] * c12y2 * c13[ 0 ] + 2 * c10[ 1 ] * c11[ 1 ] * c12[ 0 ] * c20[ 1 ] * c13x2 + c11[ 0 ] * c20[ 0 ] * c11[ 1 ] * c12y2 * c13[ 0 ] - 3 * c11[ 0 ] * c20[ 0 ] * c12[ 0 ] * c20[ 1 ] * c13y2 - 2 * c10[ 0 ] * c12x2 * c20[ 1 ] * c12[ 1 ] * c13[ 1 ] - 6 * c10[ 1 ] * c20[ 0 ] * c20[ 1 ] * c13x2 * c13[ 1 ] - 2 * c10[ 1 ] * c20[ 0 ] * c12x2 * c12[ 1 ] * c13[ 1 ] - 2 * c10[ 1 ] * c11x2 * c11[ 1 ] * c13[ 0 ] * c13[ 1 ] - c10[ 1 ] * c11x2 * c12[ 0 ] * c12[ 1 ] * c13[ 1 ] - 2 * c10[ 1 ] * c12x2 * c20[ 1 ] * c12[ 1 ] * c13[ 0 ] - 2 * c11[ 0 ] * c20[ 0 ] * c11y2 * c13[ 0 ] * c13[ 1 ] - c11[ 0 ] * c11[ 1 ] * c12x2 * c20[ 1 ] * c13[ 1 ] + 3 * c20[ 0 ] * c11[ 1 ] * c20[ 1 ] * c12[ 1 ] * c13x2 - 2 * c20[ 0 ] * c12[ 0 ] * c20[ 1 ] * c12y2 * c13[ 0 ] - c20[ 0 ] * c11y2 * c12[ 0 ] * c12[ 1 ] * c13[ 0 ] + 3 * c10y2 * c11[ 0 ] * c12[ 0 ] * c13[ 0 ] * c13[ 1 ] + 3 * c11[ 0 ] * c12[ 0 ] * c20y2 * c13[ 0 ] * c13[ 1 ] + 2 * c20[ 0 ] * c12x2 * c20[ 1 ] * c12[ 1 ] * c13[ 1 ] - 3 * c10x2 * c11[ 1 ] * c12[ 1 ] * c13[ 0 ] * c13[ 1 ] + 2 * c11x2 * c11[ 1 ] * c20[ 1 ] * c13[ 0 ] * c13[ 1 ] + c11x2 * c12[ 0 ] * c20[ 1 ] * c12[ 1 ] * c13[ 1 ] - 3 * c20x2 * c11[ 1 ] * c12[ 1 ] * c13[ 0 ] * c13[ 1 ] - c10x3 * c13y3 + c10y3 * c13x3 + c20x3 * c13y3 - c20y3 * c13x3 - 3 * c10[ 0 ] * c20x2 * c13y3 - c10[ 0 ] * c11y3 * c13x2 + 3 * c10x2 * c20[ 0 ] * c13y3 + c10[ 1 ] * c11x3 * c13y2 + 3 * c10[ 1 ] * c20y2 * c13x3 + c20[ 0 ] * c11y3 * c13x2 + c10x2 * c12y3 * c13[ 0 ] - 3 * c10y2 * c20[ 1 ] * c13x3 - c10y2 * c12x3 * c13[ 1 ] + c20x2 * c12y3 * c13[ 0 ] - c11x3 * c20[ 1 ] * c13y2 - c12x3 * c20y2 * c13[ 1 ] - c10[ 0 ] * c11x2 * c11[ 1 ] * c13y2 + c10[ 1 ] * c11[ 0 ] * c11y2 * c13x2 - 3 * c10[ 0 ] * c10y2 * c13x2 * c13[ 1 ] - c10[ 0 ] * c11y2 * c12x2 * c13[ 1 ] + c10[ 1 ] * c11x2 * c12y2 * c13[ 0 ] - c11[ 0 ] * c11y2 * c20[ 1 ] * c13x2 + 3 * c10x2 * c10[ 1 ] * c13[ 0 ] * c13y2 + c10x2 * c11[ 0 ] * c12[ 1 ] * c13y2 + 2 * c10x2 * c11[ 1 ] * c12[ 0 ] * c13y2 - 2 * c10y2 * c11[ 0 ] * c12[ 1 ] * c13x2 - c10y2 * c11[ 1 ] * c12[ 0 ] * c13x2 + c11x2 * c20[ 0 ] * c11[ 1 ] * c13y2 - 3 * c10[ 0 ] * c20y2 * c13x2 * c13[ 1 ] + 3 * c10[ 1 ] * c20x2 * c13[ 0 ] * c13y2 + c11[ 0 ] * c20x2 * c12[ 1 ] * c13y2 - 2 * c11[ 0 ] * c20y2 * c12[ 1 ] * c13x2 + c20[ 0 ] * c11y2 * c12x2 * c13[ 1 ] - c11[ 1 ] * c12[ 0 ] * c20y2 * c13x2 - c10x2 * c12[ 0 ] * c12y2 * c13[ 1 ] - 3 * c10x2 * c20[ 1 ] * c13[ 0 ] * c13y2 + 3 * c10y2 * c20[ 0 ] * c13x2 * c13[ 1 ] + c10y2 * c12x2 * c12[ 1 ] * c13[ 0 ] - c11x2 * c20[ 1 ] * c12y2 * c13[ 0 ] + 2 * c20x2 * c11[ 1 ] * c12[ 0 ] * c13y2 + 3 * c20[ 0 ] * c20y2 * c13x2 * c13[ 1 ] - c20x2 * c12[ 0 ] * c12y2 * c13[ 1 ] - 3 * c20x2 * c20[ 1 ] * c13[ 0 ] * c13y2 + c12x2 * c20y2 * c12[ 1 ] * c13[ 0 ]
		);
		double[] roots = poly.getRootsInInterval( 0, 1 );

		List<double[]> intersections = new ArrayList<>();

		for( double s : roots ) {
			double[] xRoots = new Polynomial( c13[ 0 ], c12[ 0 ], c11[ 0 ], c10[ 0 ] - c20[ 0 ] - s * c21[ 0 ] - s * s * c22[ 0 ] - s * s * s * c23[ 0 ] ).getRoots();
			double[] yRoots = new Polynomial( c13[ 1 ], c12[ 1 ], c11[ 1 ], c10[ 1 ] - c20[ 1 ] - s * c21[ 1 ] - s * s * c22[ 1 ] - s * s * s * c23[ 1 ] ).getRoots();

			if( xRoots.length > 0 && yRoots.length > 0 ) {
				double TOLERANCE = 1e-4;

				checkRoots:
				for( double xRoot : xRoots ) {
					if( 0 <= xRoot && xRoot <= 1 ) {
						for( double yRoot : yRoots ) {
							if( Math.abs( xRoot - yRoot ) < TOLERANCE ) {
								intersections.add( Vector.add( Vector.scale( c23, s * s * s ), Vector.add( Vector.scale( c22, s * s ), Vector.add( Vector.scale( c21, s ), c20 ) ) ) );
								break checkRoots;
							}
						}
					}
				}
			}
		}

		return intersections.size() == 0 ? new Intersection2D( Type.NONE ) : new Intersection2D( Type.INTERSECTION, intersections.toArray( new double[ intersections.size() ][] ) );
	}

	private static Polynomial bezout( double[] e1, double[] e2 ) {
		double AB = e1[ 0 ] * e2[ 1 ] - e2[ 0 ] * e1[ 1 ];
		double AC = e1[ 0 ] * e2[ 2 ] - e2[ 0 ] * e1[ 2 ];
		double AD = e1[ 0 ] * e2[ 3 ] - e2[ 0 ] * e1[ 3 ];
		double AE = e1[ 0 ] * e2[ 4 ] - e2[ 0 ] * e1[ 4 ];
		double AF = e1[ 0 ] * e2[ 5 ] - e2[ 0 ] * e1[ 5 ];

		double BC = e1[ 1 ] * e2[ 2 ] - e2[ 1 ] * e1[ 2 ];
		double BE = e1[ 1 ] * e2[ 4 ] - e2[ 1 ] * e1[ 4 ];
		double BF = e1[ 1 ] * e2[ 5 ] - e2[ 1 ] * e1[ 5 ];

		double CD = e1[ 2 ] * e2[ 3 ] - e2[ 2 ] * e1[ 3 ];

		double DE = e1[ 3 ] * e2[ 4 ] - e2[ 3 ] * e1[ 4 ];
		double DF = e1[ 3 ] * e2[ 5 ] - e2[ 3 ] * e1[ 5 ];

		double BFpDE = BF + DE;
		double BEmCD = BE - CD;

		return new Polynomial( AB * BC - AC * AC, AB * BEmCD + AD * BC - 2 * AC * AE, AB * BFpDE + AD * BEmCD - AE * AE - 2 * AC * AF, AB * DF + AD * BFpDE - 2 * AE * AF, AD * DF - AF * AF );
	}

}
