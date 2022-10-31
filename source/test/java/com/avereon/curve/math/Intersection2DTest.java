package com.avereon.curve.math;

import com.avereon.curve.assertion.VectorArrayAssert;
import com.avereon.curve.assertion.VectorAssert;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class Intersection2DTest {

	@Test
	void testEquals() {
		double[] a = Vector.of( 1, 2 );
		double[] b = Vector.of( 3, 4 );
		double[] c = Vector.of( 5, 6 );
		double[] d = Vector.of( 7, 8 );

		assertThat( new Intersection2D( Intersection.Type.INTERSECTION, a, b, c, d ) ).isEqualTo( new Intersection2D( Intersection.Type.INTERSECTION, a, b, c, d ) );
		assertThat( new Intersection2D( Intersection.Type.INTERSECTION, a, b, c, d ) ).isEqualTo( new Intersection2D( Intersection.Type.INTERSECTION, c, d, a, b ) );

		assertThat( new Intersection2D( Intersection.Type.INTERSECTION, a, b, c ) ).isNotEqualTo( new Intersection2D( Intersection.Type.INTERSECTION, a, d, b ) );
		assertThat( new Intersection2D( Intersection.Type.INTERSECTION, a, b, c ) ).isNotEqualTo( new Intersection2D( Intersection.Type.INTERSECTION, d ) );
	}

	@Test
	void testIntersectLineLine() {
		double[] a = Vector.of( 0, 0 );
		double[] b = Vector.of( -2, -2 );
		double[] c = Vector.of( 0, 2 );
		double[] d = Vector.of( 2, 0 );
		Intersection2D intersection = Intersection2D.intersectLineLine( a, b, c, d );

		assertThat( intersection ).isEqualTo( new Intersection2D( Intersection.Type.INTERSECTION, Vector.of( 1, 1 ) ) );
	}

	@Test
	void testIntersectLineLineWithSegments() {
		double[] a = Vector.of( 0, 0 );
		double[] b = Vector.of( 2, 2 );
		double[] c = Vector.of( 2, 4 );
		double[] d = Vector.of( 4, 2 );
		Intersection2D intersection = Intersection2D.intersectLineLine( a, b, c, d );

		assertThat( intersection ).isEqualTo( new Intersection2D( Intersection.Type.INTERSECTION, Vector.of( 3, 3 ) ) );
	}

	@Test
	void testIntersectSegmentSegment() {
		double[] a = Vector.of( 0, 0 );
		double[] b = Vector.of( 2, 2 );
		double[] c = Vector.of( 0, 2 );
		double[] d = Vector.of( 2, 0 );
		Intersection2D intersection = Intersection2D.intersectSegmentSegment( a, b, c, d );

		assertThat( intersection ).isEqualTo( new Intersection2D( Intersection.Type.INTERSECTION, Vector.of( 1, 1 ) ) );
	}

	@Test
	void testIntersectSegmentSegmentNone() {
		double[] a = Vector.of( 0, 0 );
		double[] b = Vector.of( 2, 2 );
		double[] c = Vector.of( 2, 4 );
		double[] d = Vector.of( 4, 2 );
		Intersection2D intersection = Intersection2D.intersectSegmentSegment( a, b, c, d );

		assertThat( intersection ).isEqualTo( new Intersection2D( Intersection.Type.NONE ) );
	}

	@Test
	void testIntersectSegmentSegmentParallel() {
		double[] a = Vector.of( 0, 1 );
		double[] b = Vector.of( 2, 3 );
		double[] c = Vector.of( 1, 0 );
		double[] d = Vector.of( 3, 2 );
		Intersection2D intersection = Intersection2D.intersectSegmentSegment( a, b, c, d );

		assertThat( intersection ).isEqualTo( new Intersection2D( Intersection.Type.PARALLEL ) );
	}

	@Test
	void testIntersectSegmentSegmentSame() {
		double[] a = Vector.of( 0, 0 );
		double[] b = Vector.of( 2, 2 );
		double[] c = Vector.of( 0, 0 );
		double[] d = Vector.of( 2, 2 );
		Intersection2D intersection = Intersection2D.intersectSegmentSegment( a, b, c, d );

		assertThat( intersection ).isEqualTo( new Intersection2D( Intersection.Type.SAME ) );
	}

	@Test
	void testIntersectSegmentSegmentCoincident() {
		double[] a = Vector.of( 0, 0 );
		double[] b = Vector.of( 2, 2 );
		double[] c = Vector.of( 1, 1 );
		double[] d = Vector.of( 3, 3 );
		Intersection2D intersection = Intersection2D.intersectSegmentSegment( a, b, c, d );

		assertThat( intersection ).isEqualTo( new Intersection2D( Intersection.Type.COINCIDENT ) );
	}

	@Test
	void testIntersectLineCircle() {
		Intersection2D intersection = Intersection2D.intersectLineCircle( Point.of( -1, 0 ), Point.of( 1, 0 ), Point.of( 0, 0 ), 5.0 );
		assertThat( intersection.getType() ).isEqualTo( Intersection.Type.INTERSECTION );
		assertThat( intersection.getPoints().length ).isEqualTo( 2 );
		assertThat( Arrays.asList( intersection.getPoints() ) ).contains( Point.of( -5, 0, 0 ), Point.of( 5, 0, 0 ) );

		intersection = Intersection2D.intersectLineCircle( Point.of( -1, 3 ), Point.of( 1, 3 ), Point.of( 0, 0 ), 5.0 );
		assertThat( intersection.getType() ).isEqualTo( Intersection.Type.INTERSECTION );
		assertThat( intersection.getPoints().length ).isEqualTo( 2 );
		assertThat( Arrays.asList( intersection.getPoints() ) ).contains( Point.of( -4, 3, 0 ), Point.of( 4, 3, 0 ) );

		intersection = Intersection2D.intersectLineCircle( Point.of( 4, -1 ), Point.of( 4, 1 ), Point.of( 1, 1 ), 5.0 );
		assertThat( intersection.getType() ).isEqualTo( Intersection.Type.INTERSECTION );
		assertThat( intersection.getPoints().length ).isEqualTo( 2 );
		assertThat( Arrays.asList( intersection.getPoints() ) ).contains( Point.of( 4, -3, 0 ), Point.of( 4, 5, 0 ) );
	}

	@Test
	void testIntersectLineRadius() {
		Intersection2D intersection = Intersection2D.intersectLineCircle( Point.of( -1, 0 ), Point.of( 0, 0 ), 5.0 );
		assertThat( intersection.getType() ).isEqualTo( Intersection.Type.INTERSECTION );
		assertThat( intersection.getPoints().length ).isEqualTo( 2 );
		assertThat( Arrays.asList( intersection.getPoints() ) ).contains( Point.of( -5, 0, 0 ), Point.of( 5, 0, 0 ) );

		intersection = Intersection2D.intersectLineCircle( Point.of( -1, 3 ), Point.of( 1, 3 ), 5.0 );
		assertThat( intersection.getType() ).isEqualTo( Intersection.Type.INTERSECTION );
		assertThat( intersection.getPoints().length ).isEqualTo( 2 );
		assertThat( Arrays.asList( intersection.getPoints() ) ).contains( Point.of( -4, 3, 0 ), Point.of( 4, 3, 0 ) );

		intersection = Intersection2D.intersectLineCircle( Point.of( -3, 1 ), Point.of( -3, -1 ), 5.0 );
		assertThat( intersection.getType() ).isEqualTo( Intersection.Type.INTERSECTION );
		assertThat( intersection.getPoints().length ).isEqualTo( 2 );
		assertThat( Arrays.asList( intersection.getPoints() ) ).contains( Point.of( -3, -4, 0 ), Point.of( -3, 4, 0 ) );
	}

	@Test
	void testIntersectLineCircleTangent() {
		double[] p1 = Point.of( 1, 6 );
		double[] p2 = Point.of( 8, 6 );
		double[] o = Point.of( 4, 1 );
		double r = 5.0;
		Intersection2D intersection = Intersection2D.intersectLineCircle( p1, p2, o, r );

		assertThat( intersection.getType() ).isEqualTo( Intersection.Type.INTERSECTION );
		assertThat( intersection.getPoints().length ).isEqualTo( 1 );
		VectorAssert.assertThat( intersection.getPoints()[ 0 ] ).isCloseTo( Point.of( 4, 6 ) );
	}

	@Test
	void testIntersectLineCircleNoIntersection() {
		double[] p1 = Point.of( 1, 1 );
		double[] p2 = Point.of( 8, 1 );
		double[] o = Point.of( 1, 14 );
		double r = 5.0;

		Intersection2D intersection = Intersection2D.intersectLineCircle( p1, p2, o, r );

		assertThat( intersection.getType() ).isEqualTo( Intersection.Type.NONE );
		assertThat( intersection.getPoints().length ).isEqualTo( 0 );
	}

	@Test
	void testIntersectLineEllipse() {
		Intersection2D intersection = Intersection2D.intersectLineEllipse( Point.of( -1, 0 ), Point.of( 1, 0 ), Point.of( 0, 0 ), 4.0, 5.0 );
		assertThat( intersection.getType() ).isEqualTo( Intersection.Type.INTERSECTION );
		assertThat( intersection.getPoints().length ).isEqualTo( 2 );
		assertThat( Arrays.asList( intersection.getPoints() ) ).contains( Point.of( -4, 0, 0 ), Point.of( 4, 0, 0 ) );

		intersection = Intersection2D.intersectLineEllipse( Point.of( -1, 3 ), Point.of( 1, 3 ), Point.of( 0, 0 ), 4.0, 5.0 );
		assertThat( intersection.getType() ).isEqualTo( Intersection.Type.INTERSECTION );
		assertThat( intersection.getPoints().length ).isEqualTo( 2 );
		VectorArrayAssert.assertThat( intersection.getPoints() ).areCloseTo( Point.of( -3.2, 3, 0 ), Point.of( 3.2, 3, 0 ) );

		intersection = Intersection2D.intersectLineEllipse( Point.of( 4, -1 ), Point.of( 4, 1 ), Point.of( 1, 1 ), 5.0, 4.0 );
		assertThat( intersection.getType() ).isEqualTo( Intersection.Type.INTERSECTION );
		assertThat( intersection.getPoints().length ).isEqualTo( 2 );
		VectorArrayAssert.assertThat( intersection.getPoints() ).areCloseTo( Point.of( 4, -2.2, 0 ), Point.of( 4, 4.2, 0 ) );
	}

	@Test
	void testIntersectLineRadiusRadius() {
		Intersection2D intersection = Intersection2D.intersectLineEllipse( Point.of( -1, 0 ), Point.of( 0, 0 ), 4.0, 5.0 );
		assertThat( intersection.getType() ).isEqualTo( Intersection.Type.INTERSECTION );
		assertThat( intersection.getPoints().length ).isEqualTo( 2 );
		VectorArrayAssert.assertThat( intersection.getPoints() ).areCloseTo( Point.of( -4, 0, 0 ), Point.of( 4, 0, 0 ) );

		intersection = Intersection2D.intersectLineEllipse( Point.of( -1, 3 ), Point.of( 1, 3 ), 4.0, 5.0 );
		assertThat( intersection.getType() ).isEqualTo( Intersection.Type.INTERSECTION );
		assertThat( intersection.getPoints().length ).isEqualTo( 2 );
		VectorArrayAssert.assertThat( intersection.getPoints() ).areCloseTo( Point.of( -3.2, 3, 0 ), Point.of( 3.2, 3, 0 ) );

		intersection = Intersection2D.intersectLineEllipse( Point.of( -3, 1 ), Point.of( -3, -1 ), 5.0, 4.0 );
		assertThat( intersection.getType() ).isEqualTo( Intersection.Type.INTERSECTION );
		assertThat( intersection.getPoints().length ).isEqualTo( 2 );
		VectorArrayAssert.assertThat( intersection.getPoints() ).areCloseTo( Point.of( -3, -3.2, 0 ), Point.of( -3, 3.2, 0 ) );
	}

	@Test
	void testIntersectLineEllipseTangent() {
		double[] p1 = Point.of( 1, 6 );
		double[] p2 = Point.of( 8, 6 );
		double[] o = Point.of( 4, 1 );
		double rx = 2.0;
		double ry = 5.0;
		Intersection2D intersection = Intersection2D.intersectLineEllipse( p1, p2, o, rx, ry );

		assertThat( intersection.getType() ).isEqualTo( Intersection.Type.INTERSECTION );
		assertThat( intersection.getPoints().length ).isEqualTo( 1 );
		VectorAssert.assertThat( intersection.getPoints()[ 0 ] ).isCloseTo( Point.of( 4, 6 ) );
	}

	@Test
	void testIntersectLineNoIntersection() {
		double[] p1 = Point.of( 1, 1 );
		double[] p2 = Point.of( 8, 1 );
		double[] o = Point.of( 1, 14 );
		double rx = 5.0;
		double ry = 2.0;

		Intersection2D intersection = Intersection2D.intersectLineEllipse( p1, p2, o, rx, ry );

		assertThat( intersection.getType() ).isEqualTo( Intersection.Type.NONE );
		assertThat( intersection.getPoints().length ).isEqualTo( 0 );
	}

	@Test
	void testIntersectLineCurveWithZeroIntersections() {
		double[] l1 = Point.of( 0, 0.8 );
		double[] l2 = Point.of( 1, 0.8 );
		double[] a = Point.of( 0, 0 );
		double[] b = Point.of( 0, 1 );
		double[] c = Point.of( 1, 1 );
		double[] d = Point.of( 1, 0 );

		Intersection2D intersection = Intersection2D.intersectLineBezier3( l1, l2, a, b, c, d );
		assertThat( intersection.getPoints().length ).isEqualTo( 0 );
		assertThat( intersection.getType() ).isEqualTo( Intersection.Type.NONE );
	}

	@Test
	void testIntersectLineCurveWithOneIntersection() {
		double[] l1 = Point.of( 0.25, 0 );
		double[] l2 = Point.of( 0.75, 1.5 );
		double[] a = Point.of( 0, 0 );
		double[] b = Point.of( 0, 1 );
		double[] c = Point.of( 1, 1 );
		double[] d = Point.of( 1, 0 );

		Intersection2D intersection = Intersection2D.intersectLineBezier3( l1, l2, a, b, c, d );

		double[] e = Point.of( 0.5, 0.75 );
		VectorAssert.assertThat( intersection.getPoints()[ 0 ] ).isCloseTo( e );
		assertThat( intersection.getPoints().length ).isEqualTo( 1 );
		assertThat( intersection.getType() ).isEqualTo( Intersection.Type.INTERSECTION );
	}

	@Test
	void testIntersectLineCurveWithTwoIntersections() {
		double[] a = Point.of( 0, 0 );
		double[] b = Point.of( 0, 1 );
		double[] c = Point.of( 1, 1 );
		double[] d = Point.of( 1, 0 );

		Intersection2D intersection = Intersection2D.intersectLineBezier3( Point.of( 0, 0.5 ), Point.of( 1, 0.5 ), a, b, c, d );
		double offset = 0.11509982054024945;
		VectorAssert.assertThat( intersection.getPoints()[ 0 ] ).isCloseTo( Point.of( offset, 0.5 ) );
		VectorAssert.assertThat( intersection.getPoints()[ 1 ] ).isCloseTo( Point.of( 1 - offset, 0.5 ) );
		assertThat( intersection.getPoints().length ).isEqualTo( 2 );
		assertThat( intersection.getType() ).isEqualTo( Intersection.Type.INTERSECTION );

		intersection = Intersection2D.intersectLineBezier3( Point.of( -0.25, 0 ), Point.of( 0.75, 1 ), a, b, c, d );
		VectorAssert.assertThat( intersection.getPoints()[ 0 ] ).isCloseTo( Point.of( 0.030397656825319842, 0.2803976568253193 ) );
		VectorAssert.assertThat( intersection.getPoints()[ 1 ] ).isCloseTo( Point.of( 0.5, 0.75 ) );
		assertThat( intersection.getPoints().length ).isEqualTo( 2 );
		assertThat( intersection.getType() ).isEqualTo( Intersection.Type.INTERSECTION );
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
		assertThat( intersection ).isEqualTo( new Intersection2D( Intersection.Type.INTERSECTION, a, b, c ) );
		assertThat( intersection.getPoints().length ).isEqualTo( 3 );
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
		assertThat( intersection ).isEqualTo( new Intersection2D( Intersection.Type.INTERSECTION, a, b ) );
		assertThat( intersection.getPoints().length ).isEqualTo( 2 );
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
		assertThat( intersection ).isEqualTo( new Intersection2D( Intersection.Type.INTERSECTION, a, b ) );
		assertThat( intersection.getPoints().length ).isEqualTo( 2 );
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
		assertThat( intersection ).isEqualTo( new Intersection2D( Intersection.Type.INTERSECTION, a, b ) );
		assertThat( intersection.getPoints().length ).isEqualTo( 2 );
	}

	@Test
	void testIntersectEllipseEllipseCoincident() {
		double[] c1 = Vector.of( 1, 2 );
		double rx1 = 3;
		double ry1 = 4;
		double[] c2 = Vector.of( 1, 2 );
		double rx2 = 3;
		double ry2 = 4;

		assertThat( Intersection2D.intersectEllipseEllipse( c1, rx1, ry1, c2, rx2, ry2 ) ).isEqualTo( new Intersection2D( Intersection.Type.SAME ) );
	}

	@Test
	void testIntersectEllipseEllipseWithRotations() {
		double ONE_THIRD = 1.0 / 3.0;
		double[] c1 = Vector.of( 1, 2 );
		double rx1 = 5;
		double ry1 = 2;
		double r1 = ONE_THIRD * Constants.QUARTER_CIRCLE;
		double[] c2 = Vector.of( 2, 1 );
		double rx2 = 3;
		double ry2 = 4;
		double r2 = 0.5 * Constants.QUARTER_CIRCLE;

		Intersection2D x = Intersection2D.intersectEllipseEllipse( c1, rx1, ry1, r1, c2, rx2, ry2, r2 );

		VectorAssert.assertThat( x.getPoints()[ 0 ] ).isCloseTo( Point.of( 4.943996959877696, 2.778855224957914, 0 ) );
		VectorAssert.assertThat( x.getPoints()[ 1 ] ).isCloseTo( Point.of( -2.1238408414073597, 2.1622077631954775, 0 ) );
		VectorAssert.assertThat( x.getPoints()[ 2 ] ).isCloseTo( Point.of( -0.4862630247260602, -0.8049108703010766, 0 ) );
		VectorAssert.assertThat( x.getPoints()[ 3 ] ).isCloseTo( Point.of( -3.3818161710771095, -0.3928664039763583, 0 ) );
		assertThat( x.getType() ).isEqualTo( Intersection.Type.INTERSECTION );
		assertThat( x.getPoints().length ).isEqualTo( 4 );
	}

	@Test
	void testIntersectEllipseEllipseWithRotationsAndSame() {
		double ONE_THIRD = 1.0 / 3.0;
		double[] c1 = Vector.of( 1, 2 );
		double rx1 = 5;
		double ry1 = 2;
		double r1 = ONE_THIRD * Constants.QUARTER_CIRCLE;
		double[] c2 = Vector.of( 1, 2 );
		double rx2 = 5;
		double ry2 = 2;
		double r2 = ONE_THIRD * Constants.QUARTER_CIRCLE;

		Intersection2D x = Intersection2D.intersectEllipseEllipse( c1, rx1, ry1, r1, c2, rx2, ry2, r2 );

		assertThat( x.getType() ).isEqualTo( Intersection.Type.SAME );
		assertThat( x.getPoints().length ).isEqualTo( 0 );
	}

	@Test
	void testIntersectEllipseEllipseWithRotationsAndNoIntersections() {
		double ONE_THIRD = 1.0 / 3.0;
		double[] c1 = Vector.of( 10, 2 );
		double rx1 = 5;
		double ry1 = 2;
		double r1 = ONE_THIRD * Constants.QUARTER_CIRCLE;
		double[] c2 = Vector.of( 2, -10 );
		double rx2 = 3;
		double ry2 = 4;
		double r2 = 0.5 * Constants.QUARTER_CIRCLE;

		Intersection2D x = Intersection2D.intersectEllipseEllipse( c1, rx1, ry1, r1, c2, rx2, ry2, r2 );

		assertThat( x.getType() ).isEqualTo( Intersection.Type.NONE );
		assertThat( x.getPoints().length ).isEqualTo( 0 );
	}

	@Test
	void testIntersectEllipseBezier3WithTwoIntersections() {
		double[] ec = Point.of( 0, 0 );
		double rx = 2;
		double ry = 4;
		double er = 0;

		double[] a = Vector.of( -4, 4 );
		double[] b = Vector.of( 4, 0 );
		double[] c = Vector.of( -4, 0 );
		double[] d = Vector.of( 4, -4 );

		Intersection2D x = Intersection2D.intersectEllipseBezier3( ec, rx, ry, er, a, b, c, d );
		VectorAssert.assertThat( x.getPoints()[ 0 ] ).isCloseTo( Point.of( -1.5351200609515785, 2.563908252470462, 0 ) );
		VectorAssert.assertThat( x.getPoints()[ 1 ] ).isCloseTo( Point.of( 1.5351200609515785, -2.563908252470462, 0 ) );
		assertThat( x.getType() ).isEqualTo( Intersection.Type.INTERSECTION );
		assertThat( x.getPoints().length ).isEqualTo( 2 );
	}

	@Test
	void testIntersectEllipseBezier3WithTwoIntersectionsA() {
		double[] ec = Point.of( 0, 0 );
		double rx = 4;
		double ry = 2;
		double er = Constants.QUARTER_CIRCLE;

		double[] a = Vector.of( -4, 4 );
		double[] b = Vector.of( 4, 0 );
		double[] c = Vector.of( -4, 0 );
		double[] d = Vector.of( 4, -4 );

		Intersection2D x = Intersection2D.intersectEllipseBezier3( ec, rx, ry, er, a, b, c, d );
		VectorAssert.assertThat( x.getPoints()[ 0 ] ).isCloseTo( Point.of( -1.5351200609515785, 2.563908252470462, 0 ) );
		VectorAssert.assertThat( x.getPoints()[ 1 ] ).isCloseTo( Point.of( 1.5351200609515785, -2.563908252470462, 0 ) );
		assertThat( x.getType() ).isEqualTo( Intersection.Type.INTERSECTION );
		assertThat( x.getPoints().length ).isEqualTo( 2 );
	}

	@Test
	void testIntersectEllipseBezier3WithTwoIntersectionsB() {
		double[] ec = Point.of( 4, 4 );
		double rx = 4;
		double ry = 2;
		double er = Constants.QUARTER_CIRCLE;

		double[] a = Vector.of( 0, 8 );
		double[] b = Vector.of( 8, 4 );
		double[] c = Vector.of( 0, 4 );
		double[] d = Vector.of( 8, 0 );

		Intersection2D x = Intersection2D.intersectEllipseBezier3( ec, rx, ry, er, a, b, c, d );
		VectorAssert.assertThat( x.getPoints()[ 0 ] ).isCloseTo( Point.of( 2.4648799390484215, 6.563908252470462, 0 ) );
		VectorAssert.assertThat( x.getPoints()[ 1 ] ).isCloseTo( Point.of( 5.5351200609515785, 1.436091747529538, 0 ) );
		assertThat( x.getType() ).isEqualTo( Intersection.Type.INTERSECTION );
		assertThat( x.getPoints().length ).isEqualTo( 2 );
	}

	@Test
	void testIntersectEllipseBezier3WithSixIntersections() {
		double[] ec = Point.of( 4, 4 );
		double rx = 4;
		double ry = 2;
		double er = Constants.QUARTER_CIRCLE;

		double[] a = Vector.of( 2, 8 );
		double[] b = Vector.of( 16, 4 );
		double[] c = Vector.of( -8, 4 );
		double[] d = Vector.of( 6, 0 );

		Intersection2D x = Intersection2D.intersectEllipseBezier3( ec, rx, ry, er, a, b, c, d );
		VectorAssert.assertThat( x.getPoints()[ 0 ] ).isCloseTo( Point.of( 3.1764536229073514, 7.645145497507086, 0.0 ) );
		VectorAssert.assertThat( x.getPoints()[ 1 ] ).isCloseTo( Point.of( 5.446609883713707, 6.762115021785103, 0.0 ) );
		VectorAssert.assertThat( x.getPoints()[ 2 ] ).isCloseTo( Point.of( 5.948948890909494, 4.897993784401789, 0.0 ) );
		VectorAssert.assertThat( x.getPoints()[ 3 ] ).isCloseTo( Point.of( 2.0510511090905066, 3.1020062155982124, 0.0 ) );
		VectorAssert.assertThat( x.getPoints()[ 4 ] ).isCloseTo( Point.of( 2.553390116286298, 1.2378849782148946, 0.0 ) );
		VectorAssert.assertThat( x.getPoints()[ 5 ] ).isCloseTo( Point.of( 4.823546377092655, 0.3548545024929117, 0.0 ) );
		assertThat( x.getType() ).isEqualTo( Intersection.Type.INTERSECTION );
		assertThat( x.getPoints().length ).isEqualTo( 6 );
	}

	@Test
	void testIntersectEllipseBezier3NoIntersections() {
		double[] ec = Point.of( -4, 4 );
		double rx = 4;
		double ry = 2;
		double er = 1.5 * Constants.QUARTER_CIRCLE;

		double[] a = Vector.of( 0, 8 );
		double[] b = Vector.of( 8, 4 );
		double[] c = Vector.of( 0, 4 );
		double[] d = Vector.of( 8, 0 );

		Intersection2D x = Intersection2D.intersectEllipseBezier3( ec, rx, ry, er, a, b, c, d );
		assertThat( x.getType() ).isEqualTo( Intersection.Type.NONE );
		assertThat( x.getPoints().length ).isEqualTo( 0 );
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

		assertThat( intersection.contains( Vector.of( -0.991950105629537, -0.9919501068768882 ) ) ).isTrue();
		assertThat( intersection.contains( Vector.of( 0.3323413691452663, -0.939784256197992 ) ) ).isTrue();
		assertThat( intersection.contains( Vector.of( 0.883883471495635, -0.8838834771956915 ) ) ).isTrue();
		assertThat( intersection.contains( Vector.of( 0.9397842532556537, -0.332341352593303 ) ) ).isTrue();
		assertThat( intersection.contains( Vector.of( 6.915286121511599E-9, -2.305095447852068E-9 ) ) ).isTrue();
		assertThat( intersection.contains( Vector.of( -0.9397842544481865, 0.33234135307711543 ) ) ).isTrue();
		assertThat( intersection.contains( Vector.of( -0.883883471744312, 0.8838834771601674 ) ) ).isTrue();
		assertThat( intersection.contains( Vector.of( -0.33234136914526147, 0.9397842561979921 ) ) ).isTrue();
		assertThat( intersection.contains( Vector.of( 0.9919501056295488, 0.9919501068768879 ) ) ).isTrue();

		assertThat( intersection.getType() ).isEqualTo( Intersection.Type.INTERSECTION );
		assertThat( intersection.getPoints().length ).isEqualTo( 9 );
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

		assertThat( Intersection2D.intersectBezier3Bezier3( a1, a2, a3, a4, b1, b2, b3, b4 ) ).isEqualTo( new Intersection2D( Intersection.Type.SAME ) );
		assertThat( Intersection2D.intersectBezier3Bezier3( a1, a2, a3, a4, b4, b3, b2, b1 ) ).isEqualTo( new Intersection2D( Intersection.Type.SAME ) );
	}

}
