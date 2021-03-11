package com.avereon.curve.math;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static com.avereon.curve.match.Matchers.near;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

public class Intersection2DTest {

	@Test
	void testEquals() {
		double[] a = Vector.of( 1, 2 );
		double[] b = Vector.of( 3, 4 );
		double[] c = Vector.of( 5, 6 );
		double[] d = Vector.of( 7, 8 );

		assertEquals( new Intersection2D( Intersection.Type.INTERSECTION, a, b, c, d ), new Intersection2D( Intersection.Type.INTERSECTION, a, b, c, d ) );
		assertEquals( new Intersection2D( Intersection.Type.INTERSECTION, a, b, c, d ), new Intersection2D( Intersection.Type.INTERSECTION, c, d, a, b ) );

		assertNotEquals( new Intersection2D( Intersection.Type.INTERSECTION, a, b, c ), new Intersection2D( Intersection.Type.INTERSECTION, a, d, b ) );
		assertNotEquals( new Intersection2D( Intersection.Type.INTERSECTION, a, b, c ), new Intersection2D( Intersection.Type.INTERSECTION, d ) );
	}

	@Test
	void testIntersectLineLine() {
		double[] a = Vector.of( 0, 0 );
		double[] b = Vector.of( -2, -2 );
		double[] c = Vector.of( 0, 2 );
		double[] d = Vector.of( 2, 0 );
		Intersection2D intersection = Intersection2D.intersectLineLine( a, b, c, d );

		assertThat( intersection, is( new Intersection2D( Intersection.Type.INTERSECTION, Vector.of( 1, 1 ) ) ) );
	}

	@Test
	void testIntersectSegmentSegment() {
		double[] a = Vector.of( 0, 0 );
		double[] b = Vector.of( 2, 2 );
		double[] c = Vector.of( 0, 2 );
		double[] d = Vector.of( 2, 0 );
		Intersection2D intersection = Intersection2D.intersectSegmentSegment( a, b, c, d );

		assertThat( intersection, is( new Intersection2D( Intersection.Type.INTERSECTION, Vector.of( 1, 1 ) ) ) );
	}

	@Test
	void testIntersectSegmentSegmentNone() {
		double[] a = Vector.of( 0, 0 );
		double[] b = Vector.of( 2, 2 );
		double[] c = Vector.of( 2, 4 );
		double[] d = Vector.of( 4, 2 );
		Intersection2D intersection = Intersection2D.intersectSegmentSegment( a, b, c, d );

		assertThat( intersection, is( new Intersection2D( Intersection.Type.NONE ) ) );
	}

	@Test
	void testIntersectSegmentSegmentParallel() {
		double[] a = Vector.of( 0, 1 );
		double[] b = Vector.of( 2, 3 );
		double[] c = Vector.of( 1, 0 );
		double[] d = Vector.of( 3, 2 );
		Intersection2D intersection = Intersection2D.intersectSegmentSegment( a, b, c, d );

		assertThat( intersection, is( new Intersection2D( Intersection.Type.PARALLEL ) ) );
	}

	@Test
	void testIntersectSegmentSegmentCoincident() {
		double[] a = Vector.of( 0, 0 );
		double[] b = Vector.of( 2, 2 );
		double[] c = Vector.of( 0, 0 );
		double[] d = Vector.of( 2, 2 );
		Intersection2D intersection = Intersection2D.intersectSegmentSegment( a, b, c, d );

		assertThat( intersection, is( new Intersection2D( Intersection.Type.COINCIDENT ) ) );
	}

	@Test
	void testIntersectLineCircle() {
		Intersection2D intersection = Intersection2D.intersectLineCircle( Point.of( -1, 0 ), Point.of( 1, 0 ), Point.of( 0, 0 ), 5.0 );
		assertThat( intersection.getType(), is( Intersection.Type.INTERSECTION ) );
		assertThat( intersection.getPoints().length, is( 2 ) );
		assertThat( Arrays.asList( intersection.getPoints() ), containsInAnyOrder( Point.of( -5, 0, 0 ), Point.of( 5, 0, 0 ) ) );

		intersection = Intersection2D.intersectLineCircle( Point.of( -1, 3 ), Point.of( 1, 3 ), Point.of( 0, 0 ), 5.0 );
		assertThat( intersection.getType(), is( Intersection.Type.INTERSECTION ) );
		assertThat( intersection.getPoints().length, is( 2 ) );
		assertThat( Arrays.asList( intersection.getPoints() ), containsInAnyOrder( Point.of( -4, 3, 0 ), Point.of( 4, 3, 0 ) ) );

		intersection = Intersection2D.intersectLineCircle( Point.of( -1, 4 ), Point.of( 1, 4 ), Point.of( 1, 1 ), 5.0 );
		assertThat( intersection.getType(), is( Intersection.Type.INTERSECTION ) );
		assertThat( intersection.getPoints().length, is( 2 ) );
		assertThat( Arrays.asList( intersection.getPoints() ), containsInAnyOrder( Point.of( -3, 4, 0 ), Point.of( 5, 4, 0 ) ) );
	}

	@Test
	void testIntersectLineRadius() {
		Intersection2D intersection = Intersection2D.intersectLineCircle( Point.of( -1, 0 ), Point.of( 0, 0 ), 5.0 );
		assertThat( intersection.getType(), is( Intersection.Type.INTERSECTION ) );
		assertThat( intersection.getPoints().length, is( 2 ) );
		assertThat( Arrays.asList( intersection.getPoints() ), containsInAnyOrder( Point.of( -5, 0, 0 ), Point.of( 5, 0, 0 ) ) );

		intersection = Intersection2D.intersectLineCircle( Point.of( -1, 3 ), Point.of( 1, 3 ), 5.0 );
		assertThat( intersection.getType(), is( Intersection.Type.INTERSECTION ) );
		assertThat( intersection.getPoints().length, is( 2 ) );
		assertThat( Arrays.asList( intersection.getPoints() ), containsInAnyOrder( Point.of( -4, 3, 0 ), Point.of( 4, 3, 0 ) ) );

		intersection = Intersection2D.intersectLineCircle( Point.of( -3, 1 ), Point.of( -3, -1 ), 5.0 );
		assertThat( intersection.getType(), is( Intersection.Type.INTERSECTION ) );
		assertThat( intersection.getPoints().length, is( 2 ) );
		assertThat( Arrays.asList( intersection.getPoints() ), containsInAnyOrder( Point.of( -3, -4, 0 ), Point.of( -3, 4, 0 ) ) );
	}

	@Test
	void testIntersectLineCircleTangent() {
		double[] p1 = Point.of( 1, 6 );
		double[] p2 = Point.of( 8, 6 );
		double[] o = Point.of( 4, 1 );
		double r = 5.0;
		Intersection2D intersection = Intersection2D.intersectLineCircle( p1, p2, o, r );

		assertThat( intersection.getType(), is( Intersection.Type.INTERSECTION ) );
		assertThat( intersection.getPoints().length, is( 1 ) );
		assertThat( intersection.getPoints()[ 0 ], is( Point.of( 4, 6 ) ) );
	}

	@Test
	void testIntersectLineCircleNoIntersection() {
		double[] p1 = Point.of( 1, 1 );
		double[] p2 = Point.of( 8, 1 );
		double[] o = Point.of( 1, 14 );
		double r = 5.0;

		Intersection2D intersection = Intersection2D.intersectLineCircle( p1, p2, o, r );

		assertThat( intersection.getType(), is( Intersection.Type.NONE ) );
		assertThat( intersection.getPoints().length, is( 0 ) );
	}

	@Test
	void testIntersectLineCurveWithZeroIntersections() {
		double[] l1 = Point.of( 0, 0.8 );
		double[] l2 = Point.of( 1, 0.8 );
		double[] a = Point.of( 0, 0 );
		double[] b = Point.of( 0, 1 );
		double[] c = Point.of( 1, 1 );
		double[] d = Point.of( 1, 0 );

		Intersection2D intersection = Intersection2D.intersectLineCurve( l1, l2, a, b, c, d );
		assertThat( intersection.getPoints().length, is( 0 ) );
		assertThat( intersection.getType(), is( Intersection.Type.NONE ) );
	}

	@Test
	void testIntersectLineCurveWithOneIntersection() {
		double[] l1 = Point.of( 0.25, 0 );
		double[] l2 = Point.of( 0.75, 1.5 );
		double[] a = Point.of( 0, 0 );
		double[] b = Point.of( 0, 1 );
		double[] c = Point.of( 1, 1 );
		double[] d = Point.of( 1, 0 );

		Intersection2D intersection = Intersection2D.intersectLineCurve( l1, l2, a, b, c, d );

		double[] e = Point.of( 0.5, 0.75 );
		assertThat( intersection.getPoints()[ 0 ], near( e ) );
		assertThat( intersection.getPoints().length, is( 1 ) );
		assertThat( intersection.getType(), is( Intersection.Type.INTERSECTION ) );
	}
		@Test
	void testIntersectLineCurveWithTwoIntersections() {
		double[] l1 = Point.of( 0, 0.5 );
		double[] l2 = Point.of( 1, 0.5 );
		double[] a = Point.of( 0, 0 );
		double[] b = Point.of( 0, 1 );
		double[] c = Point.of( 1, 1 );
		double[] d = Point.of( 1, 0 );

		Intersection2D intersection = Intersection2D.intersectLineCurve( l1, l2, a, b, c, d );

		double offset = 0.11509982054024945;
		double[] e = Point.of( offset, 0.5 );
		double[] f = Point.of( 1 - offset, 0.5 );
		assertThat( intersection.getPoints()[ 0 ], near( e ) );
		assertThat( intersection.getPoints()[ 1 ], near( f ) );
		assertThat( intersection.getPoints().length, is( 2 ) );
		assertThat( intersection.getType(), is( Intersection.Type.INTERSECTION ) );
	}

	@Test
	void testIntersectEllipseEllipse() {
		double[] c1 = Vector.of( 1, 2 );
		double rx1 = 3;
		double ry1 = 4;
		double[] c2 = Vector.of( 2, 2 );
		double rx2 = 4;
		double ry2 = 3;

		Intersection2D intersection = Intersection2D.intersectEllipseEllipse( c1, rx1, ry1, c2, rx2, ry2 );

		double[] a = Vector.of( 3.074285714285714, 4.88977931506818 );
		double[] b = Vector.of( -2.0, 1.9999999999999998 );
		double[] c = Vector.of( 3.074285714285714, -0.8897793150681803 );
		assertThat( intersection, is( new Intersection2D( Intersection.Type.INTERSECTION, a, b, c ) ) );
		assertThat( intersection.getPoints().length, is( 3 ) );
	}

	@Test
	void testIntersectEllipseEllipse1() {
		double[] c1 = Vector.of( 2, 4 );
		double rx1 = 3;
		double ry1 = 4;
		double[] c2 = Vector.of( 4, 4 );
		double rx2 = 4;
		double ry2 = 3;

		// The intersection points are close to (4,1) and (4,7).
		Intersection2D intersection = Intersection2D.intersectEllipseEllipse( c1, rx1, ry1, c2, rx2, ry2 );

		double[] a = Vector.of( 3.984333052423718, 1.0000230113300996 );
		double[] b = Vector.of( 3.984333052423718, 6.9999769886699 );
		assertThat( intersection, is( new Intersection2D( Intersection.Type.INTERSECTION, a, b ) ) );
		assertThat( intersection.getPoints().length, is( 2 ) );
	}

	@Test
	void testIntersectEllipseEllipse2() {
		int xOffset = -5;
		int yOffset = 7;

		double[] c1 = Vector.of( 3 + xOffset, yOffset );
		double rx1 = 5;
		double ry1 = 5;
		double[] c2 = Vector.of( -3 + xOffset, yOffset );
		double rx2 = 5;
		double ry2 = 5;

		Intersection2D intersection = Intersection2D.intersectEllipseEllipse( c1, rx1, ry1, c2, rx2, ry2 );

		double[] a = Vector.of( xOffset, 4 + yOffset );
		double[] b = Vector.of( xOffset, -4 + yOffset );
		assertThat( intersection, is( new Intersection2D( Intersection.Type.INTERSECTION, a, b ) ) );
		assertThat( intersection.getPoints().length, is( 2 ) );
	}

	@Test
	void testIntersectEllipseEllipse3() {
		int xOffset = 5;
		int yOffset = -7;

		double[] c1 = Vector.of( xOffset, 3 + yOffset );
		double rx1 = 5;
		double ry1 = 5;
		double[] c2 = Vector.of( xOffset, -3 + yOffset );
		double rx2 = 5;
		double ry2 = 5;

		Intersection2D intersection = Intersection2D.intersectEllipseEllipse( c1, rx1, ry1, c2, rx2, ry2 );

		double[] a = Vector.of( 4 + xOffset, yOffset );
		double[] b = Vector.of( -4 + xOffset, yOffset );
		assertThat( intersection, is( new Intersection2D( Intersection.Type.INTERSECTION, a, b ) ) );
		assertThat( intersection.getPoints().length, is( 2 ) );
	}

	@Test
	void testIntersectEllipseEllipseCoincident() {
		double[] c1 = Vector.of( 1, 2 );
		double rx1 = 3;
		double ry1 = 4;
		double[] c2 = Vector.of( 1, 2 );
		double rx2 = 3;
		double ry2 = 4;

		assertThat( Intersection2D.intersectEllipseEllipse( c1, rx1, ry1, c2, rx2, ry2 ), is( new Intersection2D( Intersection.Type.SAME ) ) );
	}

	@Test
	void testIntersectBezier3Bezier3() {
		double[] a1 = Vector.of( -1, -2 );
		double[] a2 = Vector.of( -1, 8 );
		double[] a3 = Vector.of( 1, -8 );
		double[] a4 = Vector.of( 1, 2 );

		double[] b1 = Vector.of( -2, -1 );
		double[] b2 = Vector.of( 8, -1 );
		double[] b3 = Vector.of( -8, 1 );
		double[] b4 = Vector.of( 2, 1 );

		Intersection2D intersection = Intersection2D.intersectBezier3Bezier3( a1, a2, a3, a4, b1, b2, b3, b4 );

		assertTrue( intersection.contains( Vector.of( -0.991950105629537, -0.9919501068768882 ) ) );
		assertTrue( intersection.contains( Vector.of( 0.3323413691452663, -0.939784256197992 ) ) );
		assertTrue( intersection.contains( Vector.of( 0.883883471495635, -0.8838834771956915 ) ) );
		assertTrue( intersection.contains( Vector.of( 0.9397842532556537, -0.332341352593303 ) ) );
		assertTrue( intersection.contains( Vector.of( 6.915286121511599E-9, -2.305095447852068E-9 ) ) );
		assertTrue( intersection.contains( Vector.of( -0.9397842544481865, 0.33234135307711543 ) ) );
		assertTrue( intersection.contains( Vector.of( -0.883883471744312, 0.8838834771601674 ) ) );
		assertTrue( intersection.contains( Vector.of( -0.33234136914526147, 0.9397842561979921 ) ) );
		assertTrue( intersection.contains( Vector.of( 0.9919501056295488, 0.9919501068768879 ) ) );

		assertThat( intersection.getType(), is( Intersection.Type.INTERSECTION ) );
		assertThat( intersection.getPoints().length, is( 9 ) );
	}

	@Test
	void testIntersectBezier3Bezier3Coincident() {
		double[] a1 = Vector.of( -1, -2 );
		double[] a2 = Vector.of( -1, 8 );
		double[] a3 = Vector.of( 1, -8 );
		double[] a4 = Vector.of( 1, 2 );

		double[] b1 = Vector.of( -1, -2 );
		double[] b2 = Vector.of( -1, 8 );
		double[] b3 = Vector.of( 1, -8 );
		double[] b4 = Vector.of( 1, 2 );

		assertThat( Intersection2D.intersectBezier3Bezier3( a1, a2, a3, a4, b1, b2, b3, b4 ), is( new Intersection2D( Intersection.Type.SAME ) ) );
		assertThat( Intersection2D.intersectBezier3Bezier3( a1, a2, a3, a4, b4, b3, b2, b1 ), is( new Intersection2D( Intersection.Type.SAME ) ) );
	}

}
