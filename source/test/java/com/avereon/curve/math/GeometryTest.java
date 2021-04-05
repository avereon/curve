package com.avereon.curve.math;

import org.junit.jupiter.api.Test;

import static com.avereon.curve.match.Matchers.near;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GeometryTest {

	@Test
	void testSquare() {
		assertThat( Geometry.square( 0.5 ), is( 0.25 ) );
		assertThat( Geometry.square( 5 ), is( 25.0 ) );
		assertThat( Geometry.square( 2 ), is( 4.0 ) );
	}

	@Test
	void testMidpointWithLine() {
		assertThat( Geometry.midpoint( Point.of( -1, -1, -1 ), Point.of( 1, 1, 1 ) ), near( Point.of( 0, 0, 0 ) ) );
		assertThat( Geometry.midpoint( Point.of( 0, 0, 0 ), Point.of( 1, 1, 1 ) ), near( Point.of( 0.5, 0.5, 0.5 ) ) );
		assertThat( Geometry.midpoint( Point.of( 1, 1, 0 ), Point.of( 3, 5, 0 ) ), near( Point.of( 2, 3, 0 ) ) );
	}

	@Test
	void testMidpointWithArc() {
		assertThat( Geometry.midpoint( Point.of( 0, 0, 0 ), 1, 1, 0, 0, Constants.HALF_CIRCLE ), near( Point.of( 0, 1, 0 ) ) );
		assertThat( Geometry.midpoint( Point.of( 0, 0, 0 ), 1, 1, 0, 0, Constants.QUARTER_CIRCLE ), near( Point.of( Math.sqrt( 0.5 ), Math.sqrt( 0.5 ), 0 ) ) );
		assertThat( Geometry.midpoint( Point.of( 0, 0, 0 ), 5, 3, 0, 0, -Constants.HALF_CIRCLE ), near( Point.of( 0, -3, 0 ) ) );

		double d = 0.5 + 0.5 * Math.sqrt( 0.5 );
		assertThat( Geometry.midpoint( Point.of( -0.5, -0.5, 0 ), 1, 0.5, -0.5 * Constants.QUARTER_CIRCLE, 0, -Constants.HALF_CIRCLE ), near( Point.of( -d, -d, 0 ) ) );
	}

	@Test
	void testDistanceWithOne2DCoordinate() {
		assertThat( Geometry.distance( Point.of( 0, 0 ) ), is( 0.0 ) );
		assertThat( Geometry.distance( Point.of( 3, 4 ) ), is( 5.0 ) );
	}

	@Test
	void testDistanceWithOne3DCoordinate() {
		assertThat( Geometry.distance( Point.of( 0, 0, 0 ) ), is( 0.0 ) );
		assertThat( Geometry.distance( Point.of( 3, 4, 12 ) ), is( 13.0 ) );
	}

	@Test
	void testDistanceWithTwo2DCoordinates() {
		assertThat( Geometry.distance( Point.of( 0, 0 ), Point.of( 0, 0 ) ), is( 0.0 ) );
		assertThat( Geometry.distance( Point.of( 1, 1 ), Point.of( 4, 5 ) ), is( 5.0 ) );
	}

	@Test
	void testDistanceWithTwo3DCoordinates() {
		assertThat( Geometry.distance( Point.of( 0, 0, 0 ), Point.of( 0, 0, 0 ) ), is( 0.0 ) );
		assertThat( Geometry.distance( Point.of( 0, 0, 0 ), Point.of( 3, 4, 12 ) ), is( 13.0 ) );
	}

	@Test
	void testPointLineDistance() {
		assertThat( Geometry.pointLineDistance( Vector.of( 0, 0 ), Vector.of( 1, 0 ), Vector.of( -0.5, 1.0 ) ), is( 1.0 ) );
		assertThat( Geometry.pointLineDistance( Vector.of( 0, 0 ), Vector.of( 1, 0 ), Vector.of( 0.0, 1.0 ) ), is( 1.0 ) );
		assertThat( Geometry.pointLineDistance( Vector.of( 0, 0 ), Vector.of( 1, 0 ), Vector.of( 0.5, 1.0 ) ), is( 1.0 ) );
		assertThat( Geometry.pointLineDistance( Vector.of( 0, 0 ), Vector.of( 1, 0 ), Vector.of( 1.0, 1.0 ) ), is( 1.0 ) );
		assertThat( Geometry.pointLineDistance( Vector.of( 0, 0 ), Vector.of( 1, 0 ), Vector.of( 1.5, 1.0 ) ), is( 1.0 ) );
	}

	@Test
	void testPointPlaneDistance() {
		assertThat( Geometry.pointPlaneDistance( Vector.ZERO, Vector.UNIT_Z, Vector.of( 0, 0, 3 ) ), is( 3.0 ) );
		assertThat( Geometry.pointPlaneDistance( Vector.ZERO, Vector.UNIT_Z, Vector.of( 0, 0, 0 ) ), is( 0.0 ) );
		assertThat( Geometry.pointPlaneDistance( Vector.ZERO, Vector.UNIT_Z, Vector.of( 0, 0, -5 ) ), is( 5.0 ) );

		assertThat( Geometry.pointPlaneDistance( Vector.ZERO, Vector.UNIT_Y, Vector.of( 2, 7, -3 ) ), is( 7.0 ) );
		assertThat( Geometry.pointPlaneDistance( Vector.ZERO, Vector.UNIT_Y, Vector.of( 2, 0, 3 ) ), is( 0.0 ) );
		assertThat( Geometry.pointPlaneDistance( Vector.ZERO, Vector.UNIT_Y, Vector.of( -2, -6, 3 ) ), is( 6.0 ) );

		assertThat( Geometry.pointPlaneDistance( Vector.ZERO, Vector.UNIT_X, Vector.of( 2, 4, -3 ) ), is( 2.0 ) );
		assertThat( Geometry.pointPlaneDistance( Vector.ZERO, Vector.UNIT_X, Vector.of( 0, -3, 4 ) ), is( 0.0 ) );
		assertThat( Geometry.pointPlaneDistance( Vector.ZERO, Vector.UNIT_X, Vector.of( -4, -2, -3 ) ), is( 4.0 ) );
	}

	@Test
	void testPointLineBoundDistance() {
		assertThat( Geometry.pointLineBoundDistance( Vector.of( 1, -1, 0 ), Vector.of( 1, 1, 0 ), Vector.ZERO ), is( 1.0 ) );
		assertThat( Geometry.pointLineBoundDistance( Vector.of( 1, 4, 0 ), Vector.of( 1, 1, 0 ), Vector.ZERO ), is( Double.NaN ) );
		assertThat( Geometry.pointLineBoundDistance( Vector.of( 2, 1, 3 ), Vector.of( 2, 3, 3 ), Vector.of( 1, 2, 3 ) ), is( 1.0 ) );
		assertThat( Geometry.pointLineBoundDistance( Vector.of( 0, 1, 0 ), Vector.of( 1, 0, 0 ), Vector.ZERO ), near( Math.sqrt( 2 ) / 2, 1E-15 ) );
		assertThat( Geometry.pointLineBoundDistance( Vector.of( 0, 118 ), Vector.of( 526, 237 ), Vector.of( 51, 136 ) ), near( 6.302695656181068, 1E-15 ) );
	}

	@Test
	void testLineLineDistance() {
		assertThat( Geometry.lineLineDistance( Vector.of( 0, 0, 0 ), Vector.of( 1, 1, 0 ), Vector.of( 0, 1, -1 ), Vector.of( 1, 0, -1 ) ), is( 1.0 ) );
		assertThat( Geometry.lineLineDistance( Vector.of( 0, 0, 0 ), Vector.of( 1, 1, 0 ), Vector.of( 0, 1, 0 ), Vector.of( 1, 0, 0 ) ), is( 0.0 ) );
		assertThat( Geometry.lineLineDistance( Vector.of( 0, 0, 0 ), Vector.of( 1, 1, 0 ), Vector.of( 0, 1, 1 ), Vector.of( 1, 0, 1 ) ), is( 1.0 ) );

		assertThat( Geometry.lineLineDistance( Vector.of( 0, 0, -1 ), Vector.of( 0, 0, 1 ), Vector.of( 0, 2, 0 ), Vector.of( 2, 0, 0 ) ), near( Math.sqrt( 2 ), 1E-15 ) );
		assertThat( Geometry.lineLineDistance( Vector.of( 0, 0, -1 ), Vector.of( 0, 0, 1 ), Vector.of( 0, 2, 0 ), Vector.of( -2, 0, 0 ) ), near( Math.sqrt( 2 ), 1E-15 ) );
		assertThat( Geometry.lineLineDistance( Vector.of( 0, 0, -1 ), Vector.of( 0, 0, 1 ), Vector.of( 0, -2, 0 ), Vector.of( 2, 0, 0 ) ), near( Math.sqrt( 2 ), 1E-15 ) );
		assertThat( Geometry.lineLineDistance( Vector.of( 0, 0, -1 ), Vector.of( 0, 0, 1 ), Vector.of( 0, -2, 0 ), Vector.of( -2, 0, 0 ) ), near( Math.sqrt( 2 ), 1E-15 ) );
	}

	@Test
	void testLineLineAngle() {
		assertThat( Geometry.lineLineAngle( Vector.of( 0, 0 ), Vector.of( 1, 0 ), Vector.of( 0, 1 ), Vector.of( 1, 1 ) ), is( 0.0 ) );
		assertThat( Geometry.lineLineAngle( Vector.of( 0, 0 ), Vector.of( 1, 0 ), Vector.of( 1, 1 ), Vector.of( 0, 1 ) ), is( Constants.HALF_CIRCLE ) );

		assertThat( Geometry.lineLineAngle( Vector.of( 0, 0 ), Vector.of( 1, 1 ), Vector.of( 0, 1, -1 ), Vector.of( 1, 0, -1 ) ), is( Constants.QUARTER_CIRCLE ) );
		assertThat( Geometry.lineLineAngle( Vector.of( 0, 0 ), Vector.of( 1, 1 ), Vector.of( 1, 0, -1 ), Vector.of( 0, 1, -1 ) ), is( Constants.QUARTER_CIRCLE ) );
	}

	@Test
	void testEllipsePoint() {
		assertThat( Geometry.ellipsePoint( Vector.of( 0, 0 ), 2, 1, 0, 0 ), near( Vector.of( 2, 0 ) ) );

		assertThat( Geometry.ellipsePoint( Vector.of( 0, 0 ), 2, 1, 0, Constants.QUARTER_CIRCLE ), near( Vector.of( 0, 1 ) ) );
		assertThat( Geometry.ellipsePoint( Vector.of( 0, 0 ), 2, 1, 0, Constants.HALF_CIRCLE ), near( Vector.of( -2, 0 ) ) );
		assertThat( Geometry.ellipsePoint( Vector.of( 0, 0 ), 2, 1, 0, Constants.THREE_QUARTER_CIRCLE ), near( Vector.of( 0, -1 ) ) );
		assertThat( Geometry.ellipsePoint( Vector.of( 0, 0 ), 2, 1, 0, Constants.FULL_CIRCLE ), near( Vector.of( 2, 0 ) ) );

		assertThat( Geometry.ellipsePoint( Vector.of( 0, 0 ), 2, 1, 0, -Constants.QUARTER_CIRCLE ), near( Vector.of( 0, -1 ) ) );
		assertThat( Geometry.ellipsePoint( Vector.of( 0, 0 ), 2, 1, 0, -Constants.HALF_CIRCLE ), near( Vector.of( -2, 0 ) ) );
		assertThat( Geometry.ellipsePoint( Vector.of( 0, 0 ), 2, 1, 0, -Constants.THREE_QUARTER_CIRCLE ), near( Vector.of( 0, 1 ) ) );
		assertThat( Geometry.ellipsePoint( Vector.of( 0, 0 ), 2, 1, 0, -Constants.FULL_CIRCLE ), near( Vector.of( 2, 0 ) ) );
	}

	@Test
	void testEllipseAngle() {
		assertThat( Geometry.ellipseAngle( Vector.of( 0, 0 ), 2, 1, 0, Vector.of( 2, 0 ) ), near( 0.0 ) );
		assertThat( Geometry.ellipseAngle( Vector.of( 0, 0 ), 2, 1, 0, Vector.of( 0, 1 ) ), near( Constants.QUARTER_CIRCLE ) );
		assertThat( Geometry.ellipseAngle( Vector.of( 0, 0 ), 2, 1, 0, Vector.of( -2, 0 ) ), near( Constants.HALF_CIRCLE ) );
		assertThat( Geometry.ellipseAngle( Vector.of( 0, 0 ), 2, 1, 0, Vector.of( 0, -1 ) ), near( -Constants.QUARTER_CIRCLE ) );
	}

	@Test
	void testEllipsePointWithScale() {
		assertThat( Geometry.ellipsePoint( Vector.of( 2, 5 ), 2, 1, 0, 0 ), near( Vector.of( 4, 5 ) ) );
		assertThat( Geometry.ellipsePoint( Vector.of( -5, 2 ), 2, 1, 0, 0 ), near( Vector.of( -3, 2 ) ) );
	}

	@Test
	void testEllipsePointWithScaleAndRotate() {
		assertThat( Geometry.ellipsePoint( Vector.of( -5, 2 ), 2, 1, Constants.QUARTER_CIRCLE, 0 ), near( Vector.of( -5, 4 ) ) );
		assertThat( Geometry.ellipsePoint( Vector.of( -5, 2 ), 2, 1, Constants.HALF_CIRCLE, 0 ), near( Vector.of( -7, 2 ) ) );
	}

	@Test
	void testEllipsePointWithScaleRotateAndStart() {
		double n = Math.sqrt( 0.5 );
		assertThat( Geometry.ellipsePoint( Vector.of( -3, 3 ), 2, 1, Constants.QUARTER_CIRCLE, Math.toRadians( 45 ) ), near( Vector.of( -3 - n, 3 + 2 * n ) ) );
		assertThat( Geometry.ellipsePoint( Vector.of( -3, 3 ), 2, 1, Constants.QUARTER_CIRCLE, -Math.toRadians( 45 ) ), near( Vector.of( -3 + n, 3 + 2 * n ) ) );
	}

	@Test
	void testVectorToLine() {
		assertThat( Geometry.vectorToLine( Vector.of( 1, -1, 0 ), Vector.of( 1, 1, 0 ), Vector.ZERO ), is( Vector.of( 1, 0, 0 ) ) );
		assertThat( Geometry.vectorToLine( Vector.of( 1, 4, 0 ), Vector.of( 1, 1, 0 ), Vector.ZERO ), is( Vector.of( 1, 0, 0 ) ) );
		assertThat( Geometry.vectorToLine( Vector.of( 2, 1, 3 ), Vector.of( 2, 3, 3 ), Vector.of( 1, 2, 3 ) ), is( Vector.of( 1, 0, 0 ) ) );
		assertThat( Geometry.vectorToLine( Vector.of( -1, -1, 0 ), Vector.of( 1, 1, 0 ), Vector.ZERO ), near( Vector.ZERO, 1e-15 ) );

		assertThat( Geometry.vectorToLine( Vector.of( -1, 0, 0 ), Vector.of( -2, 0, 0 ), Vector.ZERO ), near( Vector.ZERO, 1e-15 ) );
		assertThat( Geometry.vectorToLine( Vector.of( 0, -1, 0 ), Vector.of( 0, -2, 0 ), Vector.ZERO ), near( Vector.ZERO, 1e-15 ) );
		assertThat( Geometry.vectorToLine( Vector.of( 0, 0, -1 ), Vector.of( 0, 0, -2 ), Vector.ZERO ), near( Vector.ZERO, 1e-15 ) );
	}

	@Test
	void getVectorToPlane() {
		assertThat( Geometry.vectorToPlane( Vector.ZERO, Vector.of( 0, 0, 10 ), Vector.of( 1, 1, 1 ) ), is( Vector.of( 0, 0, -1 ) ) );
		assertThat( Geometry.vectorToPlane( Vector.ZERO, Vector.of( 0, 0, 10 ), Vector.of( -1, -1, -1 ) ), is( Vector.of( 0, 0, 1 ) ) );

		assertThat( Geometry.vectorToPlane( Vector.of( 2, 1, 5 ), Vector.of( 0, 0, 10 ), Vector.of( 3, -1, 8 ) ), is( Vector.of( 0, 0, -3 ) ) );
		assertThat( Geometry.vectorToPlane( Vector.of( 2, 1, 5 ), Vector.of( 0, 0, 10 ), Vector.of( 3, -1, 0 ) ), is( Vector.of( 0, 0, 5 ) ) );
	}

	@Test
	void testCurveParametricValue() {
		assertThat( Geometry.curveParametricValue( Vector.of( 0, 1 ), Vector.of( 1, 2 ), Vector.of( 1, 0 ), Vector.of( 2, 1 ), Vector.of( 1, 1 ) ), near( 0.5 ) );
		assertThat( Geometry.curveParametricValue( Vector.of( 0, 1 ), Vector.of( 1, 0 ), Vector.of( 1, 2 ), Vector.of( 2, 1 ), Vector.of( 1, 1 ) ), near( 0.5 ) );
	}

	@Test
	void testCurveParametricValueB() {
		double[] a = Vector.of( 2, 8 );
		double[] b = Vector.of( 16, 4 );
		double[] c = Vector.of( -8, 4 );
		double[] d = Vector.of( 6, 0 );
		assertThat( Geometry.curveParametricValue( a, b, c, d, Point.of( 4, 4 ) ), near( 0.5 ) );
	}

	@Test
	void testCurveSubdivide() {
		double[] a = Point.of( 0, 0 );
		double[] b = Point.of( 0, 1 );
		double[] c = Point.of( 1, 1 );
		double[] d = Point.of( 1, 0 );

		double[][][] curves = Geometry.curveSubdivide( a, b, c, d, 0.5 );
		double[][] curveA = curves[ 0 ];
		double[][] curveB = curves[ 1 ];

		assertThat( curveA, is( new double[][]{ Point.of( 0, 0 ), Point.of( 0, 0.5 ), Point.of( 0.25, 0.75 ), Point.of( 0.5, 0.75 ) } ) );
		assertThat( curveB, is( new double[][]{ Point.of( 0.5, 0.75 ), Point.of( 0.75, 0.75 ), Point.of( 1, 0.5 ), Point.of( 1, 0 ) } ) );
	}

	@Test
	void testCurveCoefficients() {
		double[][] c = Geometry.curveCoefficients( Point.of( 0, 0 ), Point.of( 1, 1 ), Point.of( 2, -1 ), Point.of( 3, 0 ) );
		assertThat( c[ 0 ], is( Vector.of( 0, 6 ) ) );
		assertThat( c[ 1 ], is( Vector.of( 0, -9 ) ) );
		assertThat( c[ 2 ], is( Vector.of( 3, 3 ) ) );
		assertThat( c[ 3 ], is( Vector.of( 0, 0 ) ) );
	}

	@Test
	void testNearest() {
		double[][] points = new double[][]{ Vector.of( 0, 0, 0 ), Vector.of( 1, 0, 0 ), Vector.of( 1, 1, 0 ), Vector.of( 0, 1, 0 ) };

		assertThat( Geometry.nearest( points, Vector.of( 1, -1 ) ), is( Vector.of( 1, 0, 0 ) ) );
		assertThat( Geometry.nearest( points, Vector.of( 2, -1 ) ), is( Vector.of( 1, 0, 0 ) ) );
		assertThat( Geometry.nearest( points, Vector.of( 2, 0 ) ), is( Vector.of( 1, 0, 0 ) ) );

		assertThat( Geometry.nearest( points, Vector.of( 2, 1 ) ), is( Vector.of( 1, 1, 0 ) ) );
		assertThat( Geometry.nearest( points, Vector.of( 2, 2 ) ), is( Vector.of( 1, 1, 0 ) ) );
		assertThat( Geometry.nearest( points, Vector.of( 1, 2 ) ), is( Vector.of( 1, 1, 0 ) ) );

		assertThat( Geometry.nearest( points, Vector.of( 0, 2 ) ), is( Vector.of( 0, 1, 0 ) ) );
		assertThat( Geometry.nearest( points, Vector.of( -1, 2 ) ), is( Vector.of( 0, 1, 0 ) ) );
		assertThat( Geometry.nearest( points, Vector.of( -1, 1 ) ), is( Vector.of( 0, 1, 0 ) ) );

		assertThat( Geometry.nearest( points, Vector.of( -1, 0 ) ), is( Vector.of( 0, 0, 0 ) ) );
		assertThat( Geometry.nearest( points, Vector.of( -1, -1 ) ), is( Vector.of( 0, 0, 0 ) ) );
		assertThat( Geometry.nearest( points, Vector.of( 0, -1 ) ), is( Vector.of( 0, 0, 0 ) ) );
	}

	@Test
	void testNearestLinePoint() {
		assertThat( Geometry.nearestLinePoint( Vector.of( 0, -1, 0 ), Vector.of( 0, 1, 0 ), Vector.of( 1, 0, 0 ) ), is( Vector.ZERO ) );
		assertThat( Geometry.nearestLinePoint( Vector.of( -1, -1, 0 ), Vector.of( 1, 1, 0 ), Vector.of( 1, -1, 0 ) ), near( Vector.ZERO, 1e-15 ) );
		assertThat( Geometry.nearestLinePoint( Vector.of( -1, -1, 0 ), Vector.of( 1, 1, 0 ), Vector.ZERO ), is( Vector.ZERO ) );
	}

	@Test
	void testAreCollinear() {
		double inside = Constants.RESOLUTION_LENGTH - Math.ulp( Constants.RESOLUTION_LENGTH );
		assertFalse( Geometry.areCollinear( Vector.of( 0, 0 ), Vector.of( 2, 0 ), Vector.of( 1, -Constants.RESOLUTION_LENGTH ) ) );
		assertTrue( Geometry.areCollinear( Vector.of( 0, 0 ), Vector.of( 2, 0 ), Vector.of( 1, -inside ) ) );
		assertTrue( Geometry.areCollinear( Vector.of( 0, 0 ), Vector.of( 2, 0 ), Vector.of( 1, 0 ) ) );
		assertTrue( Geometry.areCollinear( Vector.of( 0, 0 ), Vector.of( 2, 0 ), Vector.of( 1, inside ) ) );
		assertFalse( Geometry.areCollinear( Vector.of( 0, 0 ), Vector.of( 2, 0 ), Vector.of( 1, Constants.RESOLUTION_LENGTH ) ) );
	}

	@Test
	void testAreCoplanar() {
		assertFalse( Geometry.areCoplanar( Vector.ZERO, Vector.UNIT_Z, Vector.of( 0, 0, 1 ) ) );
		assertTrue( Geometry.areCoplanar( Vector.ZERO, Vector.UNIT_Z, Vector.of( 0, 0, 0 ) ) );
		assertFalse( Geometry.areCoplanar( Vector.ZERO, Vector.UNIT_Z, Vector.of( 1, -1, 1 ) ) );
		assertTrue( Geometry.areCoplanar( Vector.ZERO, Vector.UNIT_Z, Vector.of( 2, 17, 0 ) ) );
	}

	@Test
	void testAreCoplanarWithTolerance() {
		assertFalse( Geometry.areCoplanar( Vector.ZERO, Vector.of( 1, 1, 1 ), 0.1, Vector.of( 0.1, 0.1, 0.1 ) ) );
		assertTrue( Geometry.areCoplanar( Vector.ZERO, Vector.of( 1, 1, 1 ), 0.2, Vector.of( 0.1, 0.1, 0.1 ) ) );
	}

	@Test
	void testAreCoplanarWithOrientation() {
		assertFalse( Geometry.areCoplanar( new Orientation( Vector.ZERO ), Vector.of( 0, 0, 1 ) ) );
		assertTrue( Geometry.areCoplanar( new Orientation( Vector.ZERO ), Vector.of( 0, 0, 0 ) ) );
		assertFalse( Geometry.areCoplanar( new Orientation( Vector.ZERO ), Vector.of( 1, -1, 1 ) ) );
		assertTrue( Geometry.areCoplanar( new Orientation( Vector.ZERO ), Vector.of( 2, 17, 0 ) ) );
	}

	@Test
	void testAreSameSize() {
		assertTrue( Geometry.areSameSize( 0, 0 ) );
		assertFalse( Geometry.areSameSize( 0, Constants.RESOLUTION_LENGTH ) );
		assertTrue( Geometry.areSameSize( 0, Constants.RESOLUTION_LENGTH - Math.ulp( Constants.RESOLUTION_LENGTH ) ) );
	}

	@Test
	void testAreSamePoint() {
		assertTrue( Geometry.areSamePoint( Vector.of(), Vector.of() ) );
		assertFalse( Geometry.areSamePoint( Vector.of(), Vector.of( Constants.RESOLUTION_LENGTH, 0, 0 ) ) );
		assertTrue( Geometry.areSamePoint( Vector.of(), Vector.of( Constants.RESOLUTION_LENGTH - Math.ulp( Constants.RESOLUTION_LENGTH ), 0, 0 ) ) );
	}

	@Test
	void testAreSameAngle() {
		assertTrue( Geometry.areSameAngle( 0, 0 ) );
		assertFalse( Geometry.areSameAngle( 0, Constants.RESOLUTION_ANGLE ) );
		assertTrue( Geometry.areSameAngle( 0, Constants.RESOLUTION_ANGLE - Math.ulp( Constants.RESOLUTION_ANGLE ) ) );
	}

	@Test
	void testAreParallel() {
		// Test edge cases.
		assertFalse( Geometry.areParallel( Vector.of(), Vector.of() ) );
		assertFalse( Geometry.areParallel( Vector.of( 1, 0 ), Vector.of() ) );
		assertFalse( Geometry.areParallel( Vector.of(), Vector.of( 1, 0 ) ) );

		// Test parallel and anti-parallel.
		assertTrue( Geometry.areParallel( Vector.of( 1, 0 ), Vector.of( 2, 0 ) ) );
		assertFalse( Geometry.areParallel( Vector.of( -1, 0 ), Vector.of( 2, 0 ) ) );

		// Test boundaries.
		double outside = 1.0001 * Constants.RESOLUTION_LENGTH;
		double inside = 0.9999 * Constants.RESOLUTION_LENGTH;
		assertFalse( Geometry.areParallel( Vector.of( 1, 0 ), Vector.of( 1, outside ) ) );
		assertTrue( Geometry.areParallel( Vector.of( 1, 0 ), Vector.of( 1, inside ) ) );
		assertTrue( Geometry.areParallel( Vector.of( 1, 0 ), Vector.of( 1, 0 ) ) );
		assertTrue( Geometry.areParallel( Vector.of( 1, 0 ), Vector.of( 1, -inside ) ) );
		assertFalse( Geometry.areParallel( Vector.of( 1, 0 ), Vector.of( 1, -outside ) ) );
	}

	@Test
	void testAreAntiParallel() {
		// Test edge cases.
		assertFalse( Geometry.areAntiParallel( Vector.of(), Vector.of() ) );
		assertFalse( Geometry.areAntiParallel( Vector.of( 1, 0 ), Vector.of() ) );
		assertFalse( Geometry.areAntiParallel( Vector.of(), Vector.of( 1, 0 ) ) );

		// Test parallel and anti-parallel.
		assertTrue( Geometry.areAntiParallel( Vector.of( -1, 0 ), Vector.of( 2, 0 ) ) );
		assertFalse( Geometry.areAntiParallel( Vector.of( 1, 0 ), Vector.of( 2, 0 ) ) );

		// Test boundaries.
		double outside = 1.0001 * Constants.RESOLUTION_LENGTH;
		double inside = 0.9999 * Constants.RESOLUTION_LENGTH;
		assertFalse( Geometry.areAntiParallel( Vector.of( -1, 0 ), Vector.of( 1, outside ) ) );
		assertTrue( Geometry.areAntiParallel( Vector.of( -1, 0 ), Vector.of( 1, inside ) ) );
		assertTrue( Geometry.areAntiParallel( Vector.of( -1, 0 ), Vector.of( 1, 0 ) ) );
		assertTrue( Geometry.areAntiParallel( Vector.of( -1, 0 ), Vector.of( 1, -inside ) ) );
		assertFalse( Geometry.areAntiParallel( Vector.of( -1, 0 ), Vector.of( 1, -outside ) ) );
	}

	@Test
	void testGetSpin() {
		assertThat( Geometry.getSpin( Vector.of( 1, 0 ), Vector.of( 0, 0 ), Vector.of( 1, 0 ) ), is( 0.0 ) );
		assertThat( Geometry.getSpin( Vector.of( -1, 0 ), Vector.of( 0, 0 ), Vector.of( 1, 0 ) ), is( 0.0 ) );

		assertThat( Geometry.getSpin( Vector.of( 0, 0 ), Vector.of( 1, 0 ), Vector.of( 1, -1 ) ), is( -1.0 ) );
		assertThat( Geometry.getSpin( Vector.of( 0, 0 ), Vector.of( 1, 0 ), Vector.of( 1, 1 ) ), is( 1.0 ) );

		assertThat( Geometry.getSpin( Vector.of( -1, -1 ), Vector.of( -1, 1 ), Vector.of( 1, -1 ) ), is( -1.0 ) );
		assertThat( Geometry.getSpin( Vector.of( 1, 1 ), Vector.of( -1, 1 ), Vector.of( 1, -1 ) ), is( 1.0 ) );

		assertThat( Geometry.getSpin( Vector.of( -1, -1 ), Vector.of( 1, 1 ), Vector.of( 1, -1 ) ), is( -1.0 ) );
		assertThat( Geometry.getSpin( Vector.of( -1, -1 ), Vector.of( 1, 1 ), Vector.of( -1, 1 ) ), is( 1.0 ) );
	}

	@Test
	void testAreIntersectingWithEndPoints() {
		assertTrue( Geometry.areIntersecting( Vector.of( -1, -1 ), Vector.of( 1, 1 ), Vector.of( -1, 1 ), Vector.of( 1, -1 ) ) );
		assertFalse( Geometry.areIntersecting( Vector.of( -1, -1 ), Vector.of( 1, -1 ), Vector.of( -1, 1 ), Vector.of( 1, 1 ) ) );
		assertFalse( Geometry.areIntersecting( Vector.of( -2, 0 ), Vector.of( -1, 0 ), Vector.of( 0, 1 ), Vector.of( 0, -1 ) ) );
		assertFalse( Geometry.areIntersecting( Vector.of( 342, 242 ), Vector.of( 328, 242 ), Vector.of( 446, 244 ), Vector.of( 441, 241 ) ) );
	}

	@Test
	void testGetNormalWithThreePoints() {
		assertThat( Geometry.getNormal( Vector.of( 0, 1 ), Vector.of( 0, 0 ), Vector.of( 1, 0 ) ), is( Vector.of( 0, 0, 1 ) ) );
		assertThat( Geometry.getNormal( Vector.of( 1, 0 ), Vector.of( 0, 0 ), Vector.of( 0, 1 ) ), is( Vector.of( 0, 0, -1 ) ) );
		assertThat( Geometry.getNormal( Vector.of( 1, 1 ), Vector.of( 0, 0 ), Vector.of( 1, -1 ) ), is( Vector.of( 0, 0, 2 ) ) );
	}

	@Test
	void testDeterminantWithThreeVectors() {
		assertThat( Geometry.determinant( Vector.of( 1, 2, 3 ), Vector.of( 4, 5, 6 ), Vector.of( 7, 8, 9 ) ), is( 0.0 ) );
	}

	@Test
	void testCartesianToPolar() {
		assertThat( Geometry.cartesianToPolar( Point.of( 1, 0, 0 ) ), near( Point.of( 1, 0, 0 ) ) );
		assertThat( Geometry.cartesianToPolar( Point.of( 0, 1, 0 ) ), near( Point.of( 1, Constants.PI_OVER_2, 0 ) ) );
		assertThat( Geometry.cartesianToPolar( Point.of( -1, 0, 0 ) ), near( Point.of( 1, Math.PI, 0 ) ) );
		assertThat( Geometry.cartesianToPolar( Point.of( 0, -1, 0 ) ), near( Point.of( 1, -Constants.PI_OVER_2, 0 ) ) );
	}

	@Test
	void testCartesianToPolarDegrees() {
		assertThat( Geometry.cartesianToPolarDegrees( Point.of( 1, 0, 0 ) ), near( Point.of( 1, 0, 0 ) ) );
		assertThat( Geometry.cartesianToPolarDegrees( Point.of( 0, 1, 0 ) ), near( Point.of( 1, 90, 0 ) ) );
		assertThat( Geometry.cartesianToPolarDegrees( Point.of( -1, 0, 0 ) ), near( Point.of( 1, 180, 0 ) ) );
		assertThat( Geometry.cartesianToPolarDegrees( Point.of( 0, -1, 0 ) ), near( Point.of( 1, -90, 0 ) ) );
	}

	@Test
	void testPolarToCartesian() {
		assertThat( Geometry.polarToCartesian( Point.of( 1, 0, 0 ) ), near( Point.of( 1, 0, 0 ) ) );
		assertThat( Geometry.polarToCartesian( Point.of( 1, 0.5 * Math.PI, 0 ) ), near( Point.of( 0, 1, 0 ) ) );
		assertThat( Geometry.polarToCartesian( Point.of( 1, Math.PI, 0 ) ), near( Point.of( -1, 0, 0 ) ) );
		assertThat( Geometry.polarToCartesian( Point.of( 1, 1.5 * Math.PI, 0 ) ), near( Point.of( 0, -1, 0 ) ) );
		assertThat( Geometry.polarToCartesian( Point.of( 1, 2.0 * Math.PI, 0 ) ), near( Point.of( 1, 0, 0 ) ) );

		assertThat( Geometry.polarToCartesian( Point.of( Constants.SQRT_TWO, 0.25 * Math.PI, 0 ) ), near( Point.of( 1, 1, 0 ) ) );
		assertThat( Geometry.polarToCartesian( Point.of( Constants.SQRT_TWO, 0.75 * Math.PI, 0 ) ), near( Point.of( -1, 1, 0 ) ) );
		assertThat( Geometry.polarToCartesian( Point.of( Constants.SQRT_TWO, 1.25 * Math.PI, 0 ) ), near( Point.of( -1, -1, 0 ) ) );
		assertThat( Geometry.polarToCartesian( Point.of( Constants.SQRT_TWO, 1.75 * Math.PI, 0 ) ), near( Point.of( 1, -1, 0 ) ) );

		assertThat( Geometry.polarToCartesian( Point.of( Constants.SQRT_TWO, -0.25 * Math.PI, 0 ) ), near( Point.of( 1, -1, 0 ) ) );
		assertThat( Geometry.polarToCartesian( Point.of( Constants.SQRT_TWO, -0.75 * Math.PI, 0 ) ), near( Point.of( -1, -1, 0 ) ) );
		assertThat( Geometry.polarToCartesian( Point.of( Constants.SQRT_TWO, -1.25 * Math.PI, 0 ) ), near( Point.of( -1, 1, 0 ) ) );
		assertThat( Geometry.polarToCartesian( Point.of( Constants.SQRT_TWO, -1.75 * Math.PI, 0 ) ), near( Point.of( 1, 1, 0 ) ) );
	}

	@Test
	void testPolarDegreesToCartesian() {
		assertThat( Geometry.polarDegreesToCartesian( Point.of( 1, 0, 0 ) ), near( Point.of( 1, 0, 0 ) ) );
		assertThat( Geometry.polarDegreesToCartesian( Point.of( 1, 90, 0 ) ), near( Point.of( 0, 1, 0 ) ) );
		assertThat( Geometry.polarDegreesToCartesian( Point.of( 1, 180, 0 ) ), near( Point.of( -1, 0, 0 ) ) );
		assertThat( Geometry.polarDegreesToCartesian( Point.of( 1, 270, 0 ) ), near( Point.of( 0, -1, 0 ) ) );
		assertThat( Geometry.polarDegreesToCartesian( Point.of( 1, 360, 0 ) ), near( Point.of( 1, 0, 0 ) ) );

		assertThat( Geometry.polarDegreesToCartesian( Point.of( Constants.SQRT_TWO, 45, 0 ) ), near( Point.of( 1, 1, 0 ) ) );
		assertThat( Geometry.polarDegreesToCartesian( Point.of( Constants.SQRT_TWO, 135, 0 ) ), near( Point.of( -1, 1, 0 ) ) );
		assertThat( Geometry.polarDegreesToCartesian( Point.of( Constants.SQRT_TWO, 225, 0 ) ), near( Point.of( -1, -1, 0 ) ) );
		assertThat( Geometry.polarDegreesToCartesian( Point.of( Constants.SQRT_TWO, 315, 0 ) ), near( Point.of( 1, -1, 0 ) ) );

		assertThat( Geometry.polarDegreesToCartesian( Point.of( Constants.SQRT_TWO, -45, 0 ) ), near( Point.of( 1, -1, 0 ) ) );
		assertThat( Geometry.polarDegreesToCartesian( Point.of( Constants.SQRT_TWO, -135, 0 ) ), near( Point.of( -1, -1, 0 ) ) );
		assertThat( Geometry.polarDegreesToCartesian( Point.of( Constants.SQRT_TWO, -225, 0 ) ), near( Point.of( -1, 1, 0 ) ) );
		assertThat( Geometry.polarDegreesToCartesian( Point.of( Constants.SQRT_TWO, -315, 0 ) ), near( Point.of( 1, 1, 0 ) ) );
	}

	@Test
	void testAngleWithTwoVectors() {
		assertThat( Geometry.getAngle( Vector.ZERO, Vector.ZERO ), is( 0.0 ) );

		assertThat( Geometry.getAngle( Vector.of( 1, 0, 0 ), Vector.of( 1, 0, 0 ) ), is( 0.0 ) );
		assertThat( Geometry.getAngle( Vector.of( 0, 1, 0 ), Vector.of( 0, 1, 0 ) ), is( 0.0 ) );

		assertThat( Geometry.getAngle( Vector.of( 1, 0, 0 ), Vector.of( 0, 1, 0 ) ), is( Constants.QUARTER_CIRCLE ) );
		assertThat( Geometry.getAngle( Vector.of( 1, 0, 0 ), Vector.of( 0, -1, 0 ) ), is( -Constants.QUARTER_CIRCLE ) );
		assertThat( Geometry.getAngle( Vector.of( -1, 0, 0 ), Vector.of( 0, 1, 0 ) ), is( -Constants.QUARTER_CIRCLE ) );
		assertThat( Geometry.getAngle( Vector.of( -1, 0, 0 ), Vector.of( 0, -1, 0 ) ), is( Constants.QUARTER_CIRCLE ) );

		assertThat( Geometry.getAngle( Vector.of( 0, 1, 0 ), Vector.of( 1, 0, 0 ) ), is( -Constants.QUARTER_CIRCLE ) );
		assertThat( Geometry.getAngle( Vector.of( 0, 1, 0 ), Vector.of( -1, 0, 0 ) ), is( Constants.QUARTER_CIRCLE ) );
		assertThat( Geometry.getAngle( Vector.of( 0, -1, 0 ), Vector.of( 1, 0, 0 ) ), is( Constants.QUARTER_CIRCLE ) );
		assertThat( Geometry.getAngle( Vector.of( 0, -1, 0 ), Vector.of( -1, 0, 0 ) ), is( -Constants.QUARTER_CIRCLE ) );

		assertThat( Geometry.getAngle( Vector.of( 0, 1, 0 ), Vector.of( 1, 1, 0 ) ), is( -0.5 * Constants.QUARTER_CIRCLE ) );
		assertThat( Geometry.getAngle( Vector.of( 0, 1, 0 ), Vector.of( -1, 1, 0 ) ), is( 0.5 * Constants.QUARTER_CIRCLE ) );
	}

	@Test
	void testNormalizeAngle() {
		assertThat( Geometry.normalizeAngle( 0 ), is( 0.0 ) );
		assertThat( Geometry.normalizeAngle( -Math.PI ), is( Math.PI ) );

		assertThat( Geometry.normalizeAngle( Math.PI + 1 ), is( -Math.PI + 1 ) );
		assertThat( Geometry.normalizeAngle( -Math.PI - 1 ), is( Math.PI - 1 ) );

		assertThat( Geometry.normalizeAngle( 13 * Math.PI + 1 ), near( -Math.PI + 1 ) );
		assertThat( Geometry.normalizeAngle( 13 * -Math.PI - 1 ), near( Math.PI - 1 ) );
	}

	@Test
	void testAbsAngleWithTwoVectors() {
		assertThat( Geometry.getAbsAngle( Vector.ZERO, Vector.ZERO ), is( Double.NaN ) );

		assertThat( Geometry.getAbsAngle( Vector.of( 1, 0, 0 ), Vector.of( 1, 0, 0 ) ), is( 0.0 ) );
		assertThat( Geometry.getAbsAngle( Vector.of( 0, 1, 0 ), Vector.of( 0, 1, 0 ) ), is( 0.0 ) );
		assertThat( Geometry.getAbsAngle( Vector.of( 0, 0, 1 ), Vector.of( 0, 0, 1 ) ), is( 0.0 ) );

		assertThat( Geometry.getAbsAngle( Vector.of( 0, 0, 1 ), Vector.of( 1, 0, 0 ) ), is( Math.PI / 2 ) );
		assertThat( Geometry.getAbsAngle( Vector.of( 0, 0, 1 ), Vector.of( 0, 1, 0 ) ), is( Math.PI / 2 ) );
		assertThat( Geometry.getAbsAngle( Vector.of( 0, 0, 1 ), Vector.of( -1, 0, 0 ) ), is( Math.PI / 2 ) );
		assertThat( Geometry.getAbsAngle( Vector.of( 0, 0, 1 ), Vector.of( 0, -1, 0 ) ), is( Math.PI / 2 ) );
		assertThat( Geometry.getAbsAngle( Vector.of( 0, 0, 1 ), Vector.of( 1, 1, 0 ) ), is( Math.PI / 2 ) );
		assertThat( Geometry.getAbsAngle( Vector.of( 0, 0, 1 ), Vector.of( -1, 1, 0 ) ), is( Math.PI / 2 ) );

		assertThat( Geometry.getAbsAngle( Vector.of( 0, 0, 1 ), Vector.of( 0, 0, -1 ) ), is( Math.PI ) );
	}

	@Test
	void testAngleInXYPlaneAndVector() {
		double[] normal = Vector.of( 0, 0, 1 );
		assertThat( Geometry.getAngle( Vector.ZERO, normal, Vector.of( 1, 0, 0 ), Vector.of( 1, 0, 0 ) ), near( 0.0 ) );

		assertThat( Geometry.getAngle( Vector.ZERO, normal, Vector.of( 1, 0, 0 ), Vector.of( 1, 1, 0 ) ), near( Math.PI * 0.25 ) );
		assertThat( Geometry.getAngle( Vector.ZERO, normal, Vector.of( 1, 0, 0 ), Vector.of( 0, 1, 0 ) ), near( Math.PI * 0.5 ) );
		assertThat( Geometry.getAngle( Vector.ZERO, normal, Vector.of( 1, 0, 0 ), Vector.of( -1, 1, 0 ) ), near( Math.PI * 0.75 ) );

		assertThat( Geometry.getAngle( Vector.ZERO, normal, Vector.of( 1, 0, 0 ), Vector.of( -1, 0, 0 ) ), near( Math.PI ) );

		assertThat( Geometry.getAngle( Vector.ZERO, normal, Vector.of( 1, 0, 0 ), Vector.of( -1, -1, 0 ) ), near( -Math.PI * 0.75 ) );
		assertThat( Geometry.getAngle( Vector.ZERO, normal, Vector.of( 1, 0, 0 ), Vector.of( 0, -1, 0 ) ), near( -Math.PI * 0.5 ) );
		assertThat( Geometry.getAngle( Vector.ZERO, normal, Vector.of( 1, 0, 0 ), Vector.of( 1, -1, 0 ) ), near( -Math.PI * 0.25 ) );

		assertThat( Geometry.getAngle( Vector.of( -2, 3, 0 ), normal, Vector.of( -0.75, 3, 0 ), Vector.of( -1.625, 3 - 0.5, 0 ) ), near( -0.9272952180016123 ) );
	}

	@Test
	void testPointAngle() {
		assertThat( Geometry.pointAngle( Vector.of( 4, -2 ), Vector.of( 3, -2 ), Vector.of( 4, -2 ) ), is( 0.0 ) );
		assertThat( Geometry.pointAngle( Vector.of( 4, -2 ), Vector.of( 3, -2 ), Vector.of( 4, -1 ) ), is( Math.PI * 0.25 ) );
		assertThat( Geometry.pointAngle( Vector.of( 4, -2 ), Vector.of( 3, -2 ), Vector.of( 3, -1 ) ), is( Math.PI * 0.5 ) );
		assertThat( Geometry.pointAngle( Vector.of( 4, -2 ), Vector.of( 3, -2 ), Vector.of( 2, -1 ) ), is( Math.PI * 0.75 ) );
		assertThat( Geometry.pointAngle( Vector.of( 2, -2 ), Vector.of( 3, -2 ), Vector.of( 4, -2 ) ), is( Math.PI ) );
		assertThat( Geometry.pointAngle( Vector.of( 4, -2 ), Vector.of( 3, -2 ), Vector.of( 2, -3 ) ), is( -Math.PI * 0.75 ) );
		assertThat( Geometry.pointAngle( Vector.of( 4, -2 ), Vector.of( 3, -2 ), Vector.of( 3, -3 ) ), is( -Math.PI * 0.5 ) );
		assertThat( Geometry.pointAngle( Vector.of( 4, -2 ), Vector.of( 3, -2 ), Vector.of( 4, -3 ) ), is( -Math.PI * 0.25 ) );
		assertThat( Geometry.pointAngle( Vector.of( 4, -2 ), Vector.of( 3, -2 ), Vector.of( 6, -2 ) ), is( 0.0 ) );

		assertThat( Geometry.pointAngle( Vector.of( 4, -1 ), Vector.of( 3, -2 ), Vector.of( 2, -1 ) ), is( Math.PI * 0.5 ) );
		assertThat( Geometry.pointAngle( Vector.of( 2, -1 ), Vector.of( 3, -2 ), Vector.of( 4, -1 ) ), is( -Math.PI * 0.5 ) );
		assertThat( Geometry.pointAngle( Vector.of( -4, -1 ), Vector.of( -3, 0 ), Vector.of( -2, -1 ) ), is( Math.PI * 0.5 ) );
		assertThat( Geometry.pointAngle( Vector.of( -2, -1 ), Vector.of( -3, 0 ), Vector.of( -4, -1 ) ), is( -Math.PI * 0.5 ) );
	}

}
