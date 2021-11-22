package com.avereon.curve.math;

import com.avereon.curve.assertion.VectorAssert;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GeometryTest {

	@Test
	void testSquare() {
		assertThat( Geometry.square( 0.5 ) ).isEqualTo( 0.25 );
		assertThat( Geometry.square( 5 ) ).isEqualTo( 25.0 );
		assertThat( Geometry.square( 2 ) ).isEqualTo( 4.0 );
	}

	@Test
	void testMidpointWithLine() {
		VectorAssert.assertThat( Geometry.midpoint( Point.of( -1, -1, -1 ), Point.of( 1, 1, 1 ) ) ).isCloseTo( Point.of( 0, 0, 0 ) );

		VectorAssert.assertThat( Geometry.midpoint( Point.of( -1, -1, -1 ), Point.of( 1, 1, 1 ) ) ).isCloseTo( Point.of( 0, 0, 0 ) );
		VectorAssert.assertThat( Geometry.midpoint( Point.of( 0, 0, 0 ), Point.of( 1, 1, 1 ) ) ).isCloseTo( Point.of( 0.5, 0.5, 0.5 ) );
		VectorAssert.assertThat( Geometry.midpoint( Point.of( 1, 1, 0 ), Point.of( 3, 5, 0 ) ) ).isCloseTo( Point.of( 2, 3, 0 ) );
	}

	@Test
	void testMidpointWithArc() {
		VectorAssert.assertThat( Geometry.midpoint( Point.of( 0, 0, 0 ), 1, 1, 0, 0, Constants.HALF_CIRCLE ) ).isCloseTo( Point.of( 0, 1, 0 ) );
		VectorAssert.assertThat( Geometry.midpoint( Point.of( 0, 0, 0 ), 1, 1, 0, 0, Constants.QUARTER_CIRCLE ) ).isCloseTo( Point.of( Math.sqrt( 0.5 ), Math.sqrt( 0.5 ), 0 ) );
		VectorAssert.assertThat( Geometry.midpoint( Point.of( 0, 0, 0 ), 5, 3, 0, 0, -Constants.HALF_CIRCLE ) ).isCloseTo( Point.of( 0, -3, 0 ) );

		double d = 0.5 + 0.5 * Math.sqrt( 0.5 );
		VectorAssert.assertThat( Geometry.midpoint( Point.of( -0.5, -0.5, 0 ), 1, 0.5, -0.5 * Constants.QUARTER_CIRCLE, 0, -Constants.HALF_CIRCLE ) ).isCloseTo( Point.of( -d, -d, 0 ) );
	}

	@Test
	void testDistanceWithOne2DCoordinate() {
		assertThat( Geometry.distance( Point.of( 0, 0 ) ) ).isEqualTo( 0.0 );
		assertThat( Geometry.distance( Point.of( 3, 4 ) ) ).isEqualTo( 5.0 );
	}

	@Test
	void testDistanceWithOne3DCoordinate() {
		assertThat( Geometry.distance( Point.of( 0, 0, 0 ) ) ).isEqualTo( 0.0 );
		assertThat( Geometry.distance( Point.of( 3, 4, 12 ) ) ).isEqualTo( 13.0 );
	}

	@Test
	void testDistanceWithTwo2DCoordinates() {
		assertThat( Geometry.distance( Point.of( 0, 0 ), Point.of( 0, 0 ) ) ).isEqualTo( 0.0 );
		assertThat( Geometry.distance( Point.of( 1, 1 ), Point.of( 4, 5 ) ) ).isEqualTo( 5.0 );
	}

	@Test
	void testDistanceWithTwo3DCoordinates() {
		assertThat( Geometry.distance( Point.of( 0, 0, 0 ), Point.of( 0, 0, 0 ) ) ).isEqualTo( 0.0 );
		assertThat( Geometry.distance( Point.of( 0, 0, 0 ), Point.of( 3, 4, 12 ) ) ).isEqualTo( 13.0 );
	}

	@Test
	void testPointLineDistance() {
		assertThat( Geometry.linePointDistance( Vector.of( 0, 0 ), Vector.of( 1, 0 ), Vector.of( -0.5, 1.0 ) ) ).isEqualTo( 1.0 );
		assertThat( Geometry.linePointDistance( Vector.of( 0, 0 ), Vector.of( 1, 0 ), Vector.of( 0.0, 1.0 ) ) ).isEqualTo( 1.0 );
		assertThat( Geometry.linePointDistance( Vector.of( 0, 0 ), Vector.of( 1, 0 ), Vector.of( 0.5, 1.0 ) ) ).isEqualTo( 1.0 );
		assertThat( Geometry.linePointDistance( Vector.of( 0, 0 ), Vector.of( 1, 0 ), Vector.of( 1.0, 1.0 ) ) ).isEqualTo( 1.0 );
		assertThat( Geometry.linePointDistance( Vector.of( 0, 0 ), Vector.of( 1, 0 ), Vector.of( 1.5, 1.0 ) ) ).isEqualTo( 1.0 );
	}

	@Test
	void testPointPlaneDistance() {
		assertThat( Geometry.pointPlaneDistance( Vector.ZERO, Vector.UNIT_Z, Vector.of( 0, 0, 3 ) ) ).isEqualTo( 3.0 );
		assertThat( Geometry.pointPlaneDistance( Vector.ZERO, Vector.UNIT_Z, Vector.of( 0, 0, 0 ) ) ).isEqualTo( 0.0 );
		assertThat( Geometry.pointPlaneDistance( Vector.ZERO, Vector.UNIT_Z, Vector.of( 0, 0, -5 ) ) ).isEqualTo( 5.0 );

		assertThat( Geometry.pointPlaneDistance( Vector.ZERO, Vector.UNIT_Y, Vector.of( 2, 7, -3 ) ) ).isEqualTo( 7.0 );
		assertThat( Geometry.pointPlaneDistance( Vector.ZERO, Vector.UNIT_Y, Vector.of( 2, 0, 3 ) ) ).isEqualTo( 0.0 );
		assertThat( Geometry.pointPlaneDistance( Vector.ZERO, Vector.UNIT_Y, Vector.of( -2, -6, 3 ) ) ).isEqualTo( 6.0 );

		assertThat( Geometry.pointPlaneDistance( Vector.ZERO, Vector.UNIT_X, Vector.of( 2, 4, -3 ) ) ).isEqualTo( 2.0 );
		assertThat( Geometry.pointPlaneDistance( Vector.ZERO, Vector.UNIT_X, Vector.of( 0, -3, 4 ) ) ).isEqualTo( 0.0 );
		assertThat( Geometry.pointPlaneDistance( Vector.ZERO, Vector.UNIT_X, Vector.of( -4, -2, -3 ) ) ).isEqualTo( 4.0 );
	}

	@Test
	void testPointLineBoundDistance() {
		assertThat( Geometry.pointLineBoundDistance( Vector.of( 1, -1, 0 ), Vector.of( 1, 1, 0 ), Vector.ZERO ) ).isEqualTo( 1.0 );
		assertThat( Geometry.pointLineBoundDistance( Vector.of( 1, 4, 0 ), Vector.of( 1, 1, 0 ), Vector.ZERO ) ).isNaN();
		assertThat( Geometry.pointLineBoundDistance( Vector.of( 2, 1, 3 ), Vector.of( 2, 3, 3 ), Vector.of( 1, 2, 3 ) ) ).isEqualTo( 1.0 );
		assertThat( Geometry.pointLineBoundDistance( Vector.of( 0, 1, 0 ), Vector.of( 1, 0, 0 ), Vector.ZERO ) ).isCloseTo( Math.sqrt( 2 ) / 2, Offset.offset( 1E-15 ) );
		assertThat( Geometry.pointLineBoundDistance( Vector.of( 0, 118 ), Vector.of( 526, 237 ), Vector.of( 51, 136 ) ) ).isCloseTo( 6.302695656181068, Offset.offset( 1E-15 ) );
	}

	@Test
	void testLineLineDistance() {
		assertThat( Geometry.lineLineDistance( Vector.of( 0, 0, 0 ), Vector.of( 1, 1, 0 ), Vector.of( 0, 1, -1 ), Vector.of( 1, 0, -1 ) ) ).isEqualTo( 1.0 );
		assertThat( Geometry.lineLineDistance( Vector.of( 0, 0, 0 ), Vector.of( 1, 1, 0 ), Vector.of( 0, 1, 0 ), Vector.of( 1, 0, 0 ) ) ).isEqualTo( 0.0 );
		assertThat( Geometry.lineLineDistance( Vector.of( 0, 0, 0 ), Vector.of( 1, 1, 0 ), Vector.of( 0, 1, 1 ), Vector.of( 1, 0, 1 ) ) ).isEqualTo( 1.0 );

		assertThat( Geometry.lineLineDistance( Vector.of( 0, 0, -1 ), Vector.of( 0, 0, 1 ), Vector.of( 0, 2, 0 ), Vector.of( 2, 0, 0 ) ) ).isCloseTo( Math.sqrt( 2 ), Offset.offset( 1E-15 ) );
		assertThat( Geometry.lineLineDistance( Vector.of( 0, 0, -1 ), Vector.of( 0, 0, 1 ), Vector.of( 0, 2, 0 ), Vector.of( -2, 0, 0 ) ) ).isCloseTo( Math.sqrt( 2 ), Offset.offset( 1E-15 ) );
		assertThat( Geometry.lineLineDistance( Vector.of( 0, 0, -1 ), Vector.of( 0, 0, 1 ), Vector.of( 0, -2, 0 ), Vector.of( 2, 0, 0 ) ) ).isCloseTo( Math.sqrt( 2 ), Offset.offset( 1E-15 ) );
		assertThat( Geometry.lineLineDistance( Vector.of( 0, 0, -1 ), Vector.of( 0, 0, 1 ), Vector.of( 0, -2, 0 ), Vector.of( -2, 0, 0 ) ) ).isCloseTo( Math.sqrt( 2 ), Offset.offset( 1E-15 ) );
	}

	@Test
	void testLineLineAngle() {
		assertThat( Geometry.lineLineAngle( Vector.of( 0, 0 ), Vector.of( 1, 0 ), Vector.of( 0, 1 ), Vector.of( 1, 1 ) ) ).isEqualTo( 0.0 );
		assertThat( Geometry.lineLineAngle( Vector.of( 0, 0 ), Vector.of( 1, 0 ), Vector.of( 1, 1 ), Vector.of( 0, 1 ) ) ).isEqualTo( Constants.HALF_CIRCLE );

		assertThat( Geometry.lineLineAngle( Vector.of( 0, 0 ), Vector.of( 1, 1 ), Vector.of( 0, 1, -1 ), Vector.of( 1, 0, -1 ) ) ).isEqualTo( Constants.QUARTER_CIRCLE );
		assertThat( Geometry.lineLineAngle( Vector.of( 0, 0 ), Vector.of( 1, 1 ), Vector.of( 1, 0, -1 ), Vector.of( 0, 1, -1 ) ) ).isEqualTo( Constants.QUARTER_CIRCLE );
	}

	@Test
	void testEllipsePoint() {
		VectorAssert.assertThat( Geometry.ellipsePoint( Vector.of( 0, 0 ), 2, 1, 0, 0 ) ).isCloseTo( Vector.of( 2, 0 ) );

		VectorAssert.assertThat( Geometry.ellipsePoint( Vector.of( 0, 0 ), 2, 1, 0, Constants.QUARTER_CIRCLE ) ).isCloseTo( Vector.of( 0, 1 ) );
		VectorAssert.assertThat( Geometry.ellipsePoint( Vector.of( 0, 0 ), 2, 1, 0, Constants.HALF_CIRCLE ) ).isCloseTo( Vector.of( -2, 0 ) );
		VectorAssert.assertThat( Geometry.ellipsePoint( Vector.of( 0, 0 ), 2, 1, 0, Constants.THREE_QUARTER_CIRCLE ) ).isCloseTo( Vector.of( 0, -1 ) );
		VectorAssert.assertThat( Geometry.ellipsePoint( Vector.of( 0, 0 ), 2, 1, 0, Constants.FULL_CIRCLE ) ).isCloseTo( Vector.of( 2, 0 ) );

		VectorAssert.assertThat( Geometry.ellipsePoint( Vector.of( 0, 0 ), 2, 1, 0, -Constants.QUARTER_CIRCLE ) ).isCloseTo( Vector.of( 0, -1 ) );
		VectorAssert.assertThat( Geometry.ellipsePoint( Vector.of( 0, 0 ), 2, 1, 0, -Constants.HALF_CIRCLE ) ).isCloseTo( Vector.of( -2, 0 ) );
		VectorAssert.assertThat( Geometry.ellipsePoint( Vector.of( 0, 0 ), 2, 1, 0, -Constants.THREE_QUARTER_CIRCLE ) ).isCloseTo( Vector.of( 0, 1 ) );
		VectorAssert.assertThat( Geometry.ellipsePoint( Vector.of( 0, 0 ), 2, 1, 0, -Constants.FULL_CIRCLE ) ).isCloseTo( Vector.of( 2, 0 ) );
	}

	@Test
	void testEllipseAngle() {
		assertThat( Geometry.ellipseAngle( Vector.of( 0, 0 ), 2, 1, 0, Vector.of( 2, 0 ) ) ).isCloseTo( 0.0, Offset.offset( 1E-15 ) );
		assertThat( Geometry.ellipseAngle( Vector.of( 0, 0 ), 2, 1, 0, Vector.of( 0, 1 ) ) ).isCloseTo( Constants.QUARTER_CIRCLE, Offset.offset( 1E-15 ) );
		assertThat( Geometry.ellipseAngle( Vector.of( 0, 0 ), 2, 1, 0, Vector.of( -2, 0 ) ) ).isCloseTo( Constants.HALF_CIRCLE, Offset.offset( 1E-15 ) );
		assertThat( Geometry.ellipseAngle( Vector.of( 0, 0 ), 2, 1, 0, Vector.of( 0, -1 ) ) ).isCloseTo( -Constants.QUARTER_CIRCLE, Offset.offset( 1E-15 ) );
	}

	@Test
	void testEllipsePointWithScale() {
		VectorAssert.assertThat( Geometry.ellipsePoint( Vector.of( 2, 5 ), 2, 1, 0, 0 ) ).isCloseTo( Vector.of( 4, 5 ) );
		VectorAssert.assertThat( Geometry.ellipsePoint( Vector.of( -5, 2 ), 2, 1, 0, 0 ) ).isCloseTo( Vector.of( -3, 2 ) );
	}

	@Test
	void testEllipsePointWithScaleAndRotate() {
		VectorAssert.assertThat( Geometry.ellipsePoint( Vector.of( -5, 2 ), 2, 1, Constants.QUARTER_CIRCLE, 0 ) ).isCloseTo( Vector.of( -5, 4 ) );
		VectorAssert.assertThat( Geometry.ellipsePoint( Vector.of( -5, 2 ), 2, 1, Constants.HALF_CIRCLE, 0 ) ).isCloseTo( Vector.of( -7, 2 ) );
	}

	@Test
	void testEllipsePointWithScaleRotateAndStart() {
		double n = Math.sqrt( 0.5 );
		VectorAssert.assertThat( Geometry.ellipsePoint( Vector.of( -3, 3 ), 2, 1, Constants.QUARTER_CIRCLE, Math.toRadians( 45 ) ) ).isCloseTo( Vector.of( -3 - n, 3 + 2 * n ) );
		VectorAssert.assertThat( Geometry.ellipsePoint( Vector.of( -3, 3 ), 2, 1, Constants.QUARTER_CIRCLE, -Math.toRadians( 45 ) ) ).isCloseTo( Vector.of( -3 + n, 3 + 2 * n ) );
	}

	@Test
	void testVectorToLine() {
		assertThat( Geometry.vectorToLine( Vector.of( 1, -1, 0 ), Vector.of( 1, 1, 0 ), Vector.ZERO ) ).isEqualTo( Vector.of( 1, 0, 0 ) );
		assertThat( Geometry.vectorToLine( Vector.of( 1, 4, 0 ), Vector.of( 1, 1, 0 ), Vector.ZERO ) ).isEqualTo( Vector.of( 1, 0, 0 ) );
		assertThat( Geometry.vectorToLine( Vector.of( 2, 1, 3 ), Vector.of( 2, 3, 3 ), Vector.of( 1, 2, 3 ) ) ).isEqualTo( Vector.of( 1, 0, 0 ) );
		VectorAssert.assertThat( Geometry.vectorToLine( Vector.of( -1, -1, 0 ), Vector.of( 1, 1, 0 ), Vector.ZERO ) ).isCloseTo( Vector.ZERO, 1e-15 );

		VectorAssert.assertThat( Geometry.vectorToLine( Vector.of( -1, 0, 0 ), Vector.of( -2, 0, 0 ), Vector.ZERO ) ).isCloseTo( Vector.ZERO, 1e-15 );
		VectorAssert.assertThat( Geometry.vectorToLine( Vector.of( 0, -1, 0 ), Vector.of( 0, -2, 0 ), Vector.ZERO ) ).isCloseTo( Vector.ZERO, 1e-15 );
		VectorAssert.assertThat( Geometry.vectorToLine( Vector.of( 0, 0, -1 ), Vector.of( 0, 0, -2 ), Vector.ZERO ) ).isCloseTo( Vector.ZERO, 1e-15 );
	}

	@Test
	void getVectorToPlane() {
		assertThat( Geometry.vectorToPlane( Vector.ZERO, Vector.of( 0, 0, 10 ), Vector.of( 1, 1, 1 ) ) ).isEqualTo( Vector.of( 0, 0, -1 ) );
		assertThat( Geometry.vectorToPlane( Vector.ZERO, Vector.of( 0, 0, 10 ), Vector.of( -1, -1, -1 ) ) ).isEqualTo( Vector.of( 0, 0, 1 ) );

		assertThat( Geometry.vectorToPlane( Vector.of( 2, 1, 5 ), Vector.of( 0, 0, 10 ), Vector.of( 3, -1, 8 ) ) ).isEqualTo( Vector.of( 0, 0, -3 ) );
		assertThat( Geometry.vectorToPlane( Vector.of( 2, 1, 5 ), Vector.of( 0, 0, 10 ), Vector.of( 3, -1, 0 ) ) ).isEqualTo( Vector.of( 0, 0, 5 ) );
	}

	@Test
	void testCurveParametricValue() {
		assertThat( Geometry.curveParametricValue( Vector.of( 0, 1 ), Vector.of( 1, 2 ), Vector.of( 1, 0 ), Vector.of( 2, 1 ), Vector.of( 1, 1 ) ) ).isCloseTo( 0.5, Offset.offset( 1e-15 ) );
		assertThat( Geometry.curveParametricValue( Vector.of( 0, 1 ), Vector.of( 1, 0 ), Vector.of( 1, 2 ), Vector.of( 2, 1 ), Vector.of( 1, 1 ) ) ).isCloseTo( 0.5, Offset.offset( 1e-15 ) );

		double[] q1 = Geometry.curvePoint( Vector.of( 0, 1 ), Vector.of( 1, 2 ), Vector.of( 1, 0 ), Vector.of( 2, 1 ), 0.25 );
		double[] q3 = Geometry.curvePoint( Vector.of( 0, 1 ), Vector.of( 1, 2 ), Vector.of( 1, 0 ), Vector.of( 2, 1 ), 0.75 );

		assertThat( Geometry.curveParametricValue( Vector.of( 0, 1 ), Vector.of( 1, 2 ), Vector.of( 1, 0 ), Vector.of( 2, 1 ), Vector.of( 0, 1 ) ) ).isCloseTo( 0.0, Offset.offset( 1e-15 ) );
		assertThat( Geometry.curveParametricValue( Vector.of( 0, 1 ), Vector.of( 1, 2 ), Vector.of( 1, 0 ), Vector.of( 2, 1 ), q1 ) ).isCloseTo( 0.25, Offset.offset( 1e-15 ) );
		assertThat( Geometry.curveParametricValue( Vector.of( 0, 1 ), Vector.of( 1, 2 ), Vector.of( 1, 0 ), Vector.of( 2, 1 ), q3 ) ).isCloseTo( 0.75, Offset.offset( 1e-15 ) );
		assertThat( Geometry.curveParametricValue( Vector.of( 0, 1 ), Vector.of( 1, 2 ), Vector.of( 1, 0 ), Vector.of( 2, 1 ), Vector.of( 2, 1 ) ) ).isCloseTo( 1.0, Offset.offset( 1e-15 ) );
	}

	@Test
	void testCurveParametricValueB() {
		double[] a = Vector.of( 2, 8 );
		double[] b = Vector.of( 16, 4 );
		double[] c = Vector.of( -8, 4 );
		double[] d = Vector.of( 6, 0 );
		assertThat( Geometry.curveParametricValue( a, b, c, d, Point.of( 4, 4 ) ) ).isCloseTo( 0.5, Offset.offset( 1e-15 ) );
	}

	@Test
	void testCurveParametricValueNear() {
		assertThat( Geometry.curveParametricValueNear( Vector.of( 0, 1 ), Vector.of( 1, 2 ), Vector.of( 1, 0 ), Vector.of( 2, 1 ), Vector.of( 0, 1 ) ) ).isCloseTo( 0.0, Offset.offset( 1e-15 ) );
		assertThat( Geometry.curveParametricValueNear( Vector.of( 0, 1 ), Vector.of( 1, 2 ), Vector.of( 1, 0 ), Vector.of( 2, 1 ), Vector.of( 0.5, 2 ) ) )
			.isCloseTo( 0.2019641810083393, Offset.offset( 1e-15 ) );
		assertThat( Geometry.curveParametricValueNear( Vector.of( 0, 1 ), Vector.of( 1, 2 ), Vector.of( 1, 0 ), Vector.of( 2, 1 ), Vector.of( 1, 1 ) ) ).isCloseTo( 0.5, Offset.offset( 1e-15 ) );
		assertThat( Geometry.curveParametricValueNear( Vector.of( 0, 1 ), Vector.of( 1, 2 ), Vector.of( 1, 0 ), Vector.of( 2, 1 ), Vector.of( 1.5, 0 ) ) )
			.isCloseTo( 0.7980358189916608, Offset.offset( 1e-15 ) );
		assertThat( Geometry.curveParametricValueNear( Vector.of( 0, 1 ), Vector.of( 1, 2 ), Vector.of( 1, 0 ), Vector.of( 2, 1 ), Vector.of( 2, 1 ) ) ).isCloseTo( 1.0, Offset.offset( 1e-15 ) );
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

		assertThat( curveA ).isEqualTo( new double[][]{ Point.of( 0, 0 ), Point.of( 0, 0.5 ), Point.of( 0.25, 0.75 ), Point.of( 0.5, 0.75 ) } );
		assertThat( curveB ).isEqualTo( new double[][]{ Point.of( 0.5, 0.75 ), Point.of( 0.75, 0.75 ), Point.of( 1, 0.5 ), Point.of( 1, 0 ) } );
	}

	@Test
	void testCurveCoefficients() {
		double[][] c = Geometry.curveCoefficients( Point.of( 0, 0 ), Point.of( 1, 1 ), Point.of( 2, -1 ), Point.of( 3, 0 ) );
		assertThat( c[ 0 ] ).isEqualTo( Vector.of( 0, 6 ) );
		assertThat( c[ 1 ] ).isEqualTo( Vector.of( 0, -9 ) );
		assertThat( c[ 2 ] ).isEqualTo( Vector.of( 3, 3 ) );
		assertThat( c[ 3 ] ).isEqualTo( Vector.of( 0, 0 ) );
	}

	@Test
	void testNearest() {
		double[][] points = new double[][]{ Vector.of( 0, 0, 0 ), Vector.of( 1, 0, 0 ), Vector.of( 1, 1, 0 ), Vector.of( 0, 1, 0 ) };

		assertThat( Geometry.nearest( points, Vector.of( 1, -1 ) ) ).isEqualTo( Vector.of( 1, 0, 0 ) );
		assertThat( Geometry.nearest( points, Vector.of( 2, -1 ) ) ).isEqualTo( Vector.of( 1, 0, 0 ) );
		assertThat( Geometry.nearest( points, Vector.of( 2, 0 ) ) ).isEqualTo( Vector.of( 1, 0, 0 ) );

		assertThat( Geometry.nearest( points, Vector.of( 2, 1 ) ) ).isEqualTo( Vector.of( 1, 1, 0 ) );
		assertThat( Geometry.nearest( points, Vector.of( 2, 2 ) ) ).isEqualTo( Vector.of( 1, 1, 0 ) );
		assertThat( Geometry.nearest( points, Vector.of( 1, 2 ) ) ).isEqualTo( Vector.of( 1, 1, 0 ) );

		assertThat( Geometry.nearest( points, Vector.of( 0, 2 ) ) ).isEqualTo( Vector.of( 0, 1, 0 ) );
		assertThat( Geometry.nearest( points, Vector.of( -1, 2 ) ) ).isEqualTo( Vector.of( 0, 1, 0 ) );
		assertThat( Geometry.nearest( points, Vector.of( -1, 1 ) ) ).isEqualTo( Vector.of( 0, 1, 0 ) );

		assertThat( Geometry.nearest( points, Vector.of( -1, 0 ) ) ).isEqualTo( Vector.of( 0, 0, 0 ) );
		assertThat( Geometry.nearest( points, Vector.of( -1, -1 ) ) ).isEqualTo( Vector.of( 0, 0, 0 ) );
		assertThat( Geometry.nearest( points, Vector.of( 0, -1 ) ) ).isEqualTo( Vector.of( 0, 0, 0 ) );
	}

	@Test
	void testNearestLinePoint() {
		assertThat( Geometry.nearestLinePoint( Vector.of( 0, -1, 0 ), Vector.of( 0, 1, 0 ), Vector.of( 1, 0, 0 ) ) ).isEqualTo( Vector.ZERO );
		VectorAssert.assertThat( Geometry.nearestLinePoint( Vector.of( -1, -1, 0 ), Vector.of( 1, 1, 0 ), Vector.of( 1, -1, 0 ) ) ).isCloseTo( Vector.ZERO, 1e-15 );
		assertThat( Geometry.nearestLinePoint( Vector.of( -1, -1, 0 ), Vector.of( 1, 1, 0 ), Vector.ZERO ) ).isEqualTo( Vector.ZERO );
	}

	@Test
	void testAreCollinear() {
		double inside = Constants.RESOLUTION_LENGTH - Math.ulp( Constants.RESOLUTION_LENGTH );
		assertThat( Geometry.areCollinear( Vector.of( 0, 0 ), Vector.of( 2, 0 ), Vector.of( 1, -Constants.RESOLUTION_LENGTH ) ) ).isFalse();
		assertThat( Geometry.areCollinear( Vector.of( 0, 0 ), Vector.of( 2, 0 ), Vector.of( 1, -inside ) ) ).isTrue();
		assertThat( Geometry.areCollinear( Vector.of( 0, 0 ), Vector.of( 2, 0 ), Vector.of( 1, 0 ) ) ).isTrue();
		assertThat( Geometry.areCollinear( Vector.of( 0, 0 ), Vector.of( 2, 0 ), Vector.of( 1, inside ) ) ).isTrue();
		assertThat( Geometry.areCollinear( Vector.of( 0, 0 ), Vector.of( 2, 0 ), Vector.of( 1, Constants.RESOLUTION_LENGTH ) ) ).isFalse();
	}

	@Test
	void testAreCoplanar() {
		assertThat( Geometry.areCoplanar( Vector.ZERO, Vector.UNIT_Z, Vector.of( 0, 0, 1 ) ) ).isFalse();
		assertThat( Geometry.areCoplanar( Vector.ZERO, Vector.UNIT_Z, Vector.of( 0, 0, 0 ) ) ).isTrue();
		assertThat( Geometry.areCoplanar( Vector.ZERO, Vector.UNIT_Z, Vector.of( 1, -1, 1 ) ) ).isFalse();
		assertThat( Geometry.areCoplanar( Vector.ZERO, Vector.UNIT_Z, Vector.of( 2, 17, 0 ) ) ).isTrue();
	}

	@Test
	void testAreCoplanarWithTolerance() {
		assertThat( Geometry.areCoplanar( Vector.ZERO, Vector.of( 1, 1, 1 ), 0.1, Vector.of( 0.1, 0.1, 0.1 ) ) ).isFalse();
		assertThat( Geometry.areCoplanar( Vector.ZERO, Vector.of( 1, 1, 1 ), 0.2, Vector.of( 0.1, 0.1, 0.1 ) ) ).isTrue();
	}

	@Test
	void testAreCoplanarWithOrientation() {
		assertThat( Geometry.areCoplanar( new Orientation( Vector.ZERO ), Vector.of( 0, 0, 1 ) ) ).isFalse();
		assertThat( Geometry.areCoplanar( new Orientation( Vector.ZERO ), Vector.of( 0, 0, 0 ) ) ).isTrue();
		assertThat( Geometry.areCoplanar( new Orientation( Vector.ZERO ), Vector.of( 1, -1, 1 ) ) ).isFalse();
		assertThat( Geometry.areCoplanar( new Orientation( Vector.ZERO ), Vector.of( 2, 17, 0 ) ) ).isTrue();
	}

	@Test
	void testAreSameSize() {
		assertThat( Geometry.areSameSize( 0, 0 ) ).isTrue();
		assertThat( Geometry.areSameSize( 0, Constants.RESOLUTION_LENGTH ) ).isFalse();
		assertThat( Geometry.areSameSize( 0, Constants.RESOLUTION_LENGTH - Math.ulp( Constants.RESOLUTION_LENGTH ) ) ).isTrue();
	}

	@Test
	void testAreSamePoint() {
		assertThat( Geometry.areSamePoint( Vector.of(), Vector.of() ) ).isTrue();
		assertThat( Geometry.areSamePoint( Vector.of(), Vector.of( Constants.RESOLUTION_LENGTH, 0, 0 ) ) ).isFalse();
		assertThat( Geometry.areSamePoint( Vector.of(), Vector.of( Constants.RESOLUTION_LENGTH - Math.ulp( Constants.RESOLUTION_LENGTH ), 0, 0 ) ) ).isTrue();
	}

	@Test
	void testAreSameAngle() {
		assertThat( Geometry.areSameAngle( 0, 0 ) ).isTrue();
		assertThat( Geometry.areSameAngle( 0, Constants.RESOLUTION_ANGLE ) ).isFalse();
		assertThat( Geometry.areSameAngle( 0, Constants.RESOLUTION_ANGLE - Math.ulp( Constants.RESOLUTION_ANGLE ) ) ).isTrue();
	}

	@Test
	void testAreParallel() {
		// Test edge cases.
		assertThat( Geometry.areParallel( Vector.of(), Vector.of() ) ).isFalse();
		assertThat( Geometry.areParallel( Vector.of( 1, 0 ), Vector.of() ) ).isFalse();
		assertThat( Geometry.areParallel( Vector.of(), Vector.of( 1, 0 ) ) ).isFalse();

		// Test parallel and anti-parallel.
		assertThat( Geometry.areParallel( Vector.of( 1, 0 ), Vector.of( 2, 0 ) ) ).isTrue();
		assertThat( Geometry.areParallel( Vector.of( -1, 0 ), Vector.of( 2, 0 ) ) ).isFalse();

		// Test boundaries.
		double outside = 1.0001 * Constants.RESOLUTION_LENGTH;
		double inside = 0.9999 * Constants.RESOLUTION_LENGTH;
		assertThat( Geometry.areParallel( Vector.of( 1, 0 ), Vector.of( 1, outside ) ) ).isFalse();
		assertThat( Geometry.areParallel( Vector.of( 1, 0 ), Vector.of( 1, inside ) ) ).isTrue();
		assertThat( Geometry.areParallel( Vector.of( 1, 0 ), Vector.of( 1, 0 ) ) ).isTrue();
		assertThat( Geometry.areParallel( Vector.of( 1, 0 ), Vector.of( 1, -inside ) ) ).isTrue();
		assertThat( Geometry.areParallel( Vector.of( 1, 0 ), Vector.of( 1, -outside ) ) ).isFalse();
	}

	@Test
	void testAreAntiParallel() {
		// Test edge cases.
		assertThat( Geometry.areAntiParallel( Vector.of(), Vector.of() ) ).isFalse();
		assertThat( Geometry.areAntiParallel( Vector.of( 1, 0 ), Vector.of() ) ).isFalse();
		assertThat( Geometry.areAntiParallel( Vector.of(), Vector.of( 1, 0 ) ) ).isFalse();

		// Test parallel and anti-parallel.
		assertThat( Geometry.areAntiParallel( Vector.of( -1, 0 ), Vector.of( 2, 0 ) ) ).isTrue();
		assertThat( Geometry.areAntiParallel( Vector.of( 1, 0 ), Vector.of( 2, 0 ) ) ).isFalse();

		// Test boundaries.
		double outside = 1.0001 * Constants.RESOLUTION_LENGTH;
		double inside = 0.9999 * Constants.RESOLUTION_LENGTH;
		assertThat( Geometry.areAntiParallel( Vector.of( -1, 0 ), Vector.of( 1, outside ) ) ).isFalse();
		assertThat( Geometry.areAntiParallel( Vector.of( -1, 0 ), Vector.of( 1, inside ) ) ).isTrue();
		assertThat( Geometry.areAntiParallel( Vector.of( -1, 0 ), Vector.of( 1, 0 ) ) ).isTrue();
		assertThat( Geometry.areAntiParallel( Vector.of( -1, 0 ), Vector.of( 1, -inside ) ) ).isTrue();
		assertThat( Geometry.areAntiParallel( Vector.of( -1, 0 ), Vector.of( 1, -outside ) ) ).isFalse();
	}

	@Test
	void testGetSpin() {
		assertThat( Geometry.getSpin( Vector.of( 1, 0 ), Vector.of( 0, 0 ), Vector.of( 1, 0 ) ) ).isEqualTo( 0.0 );
		assertThat( Geometry.getSpin( Vector.of( -1, 0 ), Vector.of( 0, 0 ), Vector.of( 1, 0 ) ) ).isEqualTo( 0.0 );

		assertThat( Geometry.getSpin( Vector.of( 0, 0 ), Vector.of( 1, 0 ), Vector.of( 1, -1 ) ) ).isEqualTo( -1.0 );
		assertThat( Geometry.getSpin( Vector.of( 0, 0 ), Vector.of( 1, 0 ), Vector.of( 1, 1 ) ) ).isEqualTo( 1.0 );

		assertThat( Geometry.getSpin( Vector.of( -1, -1 ), Vector.of( -1, 1 ), Vector.of( 1, -1 ) ) ).isEqualTo( -1.0 );
		assertThat( Geometry.getSpin( Vector.of( 1, 1 ), Vector.of( -1, 1 ), Vector.of( 1, -1 ) ) ).isEqualTo( 1.0 );

		assertThat( Geometry.getSpin( Vector.of( -1, -1 ), Vector.of( 1, 1 ), Vector.of( 1, -1 ) ) ).isEqualTo( -1.0 );
		assertThat( Geometry.getSpin( Vector.of( -1, -1 ), Vector.of( 1, 1 ), Vector.of( -1, 1 ) ) ).isEqualTo( 1.0 );
	}

	@Test
	void testAreIntersectingWithEndPoints() {
		assertThat( Geometry.areIntersecting( Vector.of( -1, -1 ), Vector.of( 1, 1 ), Vector.of( -1, 1 ), Vector.of( 1, -1 ) ) ).isTrue();
		assertThat( Geometry.areIntersecting( Vector.of( -1, -1 ), Vector.of( 1, -1 ), Vector.of( -1, 1 ), Vector.of( 1, 1 ) ) ).isFalse();
		assertThat( Geometry.areIntersecting( Vector.of( -2, 0 ), Vector.of( -1, 0 ), Vector.of( 0, 1 ), Vector.of( 0, -1 ) ) ).isFalse();
		assertThat( Geometry.areIntersecting( Vector.of( 342, 242 ), Vector.of( 328, 242 ), Vector.of( 446, 244 ), Vector.of( 441, 241 ) ) ).isFalse();
	}

	@Test
	void testGetNormalWithThreePoints() {
		assertThat( Geometry.getNormal( Vector.of( 0, 1 ), Vector.of( 0, 0 ), Vector.of( 1, 0 ) ) ).isEqualTo( Vector.of( 0, 0, 1 ) );
		assertThat( Geometry.getNormal( Vector.of( 1, 0 ), Vector.of( 0, 0 ), Vector.of( 0, 1 ) ) ).isEqualTo( Vector.of( 0, 0, -1 ) );
		assertThat( Geometry.getNormal( Vector.of( 1, 1 ), Vector.of( 0, 0 ), Vector.of( 1, -1 ) ) ).isEqualTo( Vector.of( 0, 0, 2 ) );
	}

	@Test
	void testDeterminantWithThreeVectors() {
		assertThat( Geometry.determinant( Vector.of( 1, 2, 3 ), Vector.of( 4, 5, 6 ), Vector.of( 7, 8, 9 ) ) ).isEqualTo( 0.0 );
	}

	@Test
	void testCartesianToPolar() {
		VectorAssert.assertThat( Geometry.cartesianToPolar( Point.of( 1, 0, 0 ) ) ).isCloseTo( Point.of( 1, 0, 0 ) );
		VectorAssert.assertThat( Geometry.cartesianToPolar( Point.of( 0, 1, 0 ) ) ).isCloseTo( Point.of( 1, Constants.PI_OVER_2, 0 ) );
		VectorAssert.assertThat( Geometry.cartesianToPolar( Point.of( -1, 0, 0 ) ) ).isCloseTo( Point.of( 1, Math.PI, 0 ) );
		VectorAssert.assertThat( Geometry.cartesianToPolar( Point.of( 0, -1, 0 ) ) ).isCloseTo( Point.of( 1, -Constants.PI_OVER_2, 0 ) );
	}

	@Test
	void testCartesianToPolarDegrees() {
		VectorAssert.assertThat( Geometry.cartesianToPolarDegrees( Point.of( 1, 0, 0 ) ) ).isCloseTo( Point.of( 1, 0, 0 ) );
		VectorAssert.assertThat( Geometry.cartesianToPolarDegrees( Point.of( 0, 1, 0 ) ) ).isCloseTo( Point.of( 1, 90, 0 ) );
		VectorAssert.assertThat( Geometry.cartesianToPolarDegrees( Point.of( -1, 0, 0 ) ) ).isCloseTo( Point.of( 1, 180, 0 ) );
		VectorAssert.assertThat( Geometry.cartesianToPolarDegrees( Point.of( 0, -1, 0 ) ) ).isCloseTo( Point.of( 1, -90, 0 ) );
	}

	@Test
	void testPolarToCartesian() {
		VectorAssert.assertThat( Geometry.polarToCartesian( Point.of( 1, 0, 0 ) ) ).isCloseTo( Point.of( 1, 0, 0 ) );
		VectorAssert.assertThat( Geometry.polarToCartesian( Point.of( 1, 0.5 * Math.PI, 0 ) ) ).isCloseTo( Point.of( 0, 1, 0 ) );
		VectorAssert.assertThat( Geometry.polarToCartesian( Point.of( 1, Math.PI, 0 ) ) ).isCloseTo( Point.of( -1, 0, 0 ) );
		VectorAssert.assertThat( Geometry.polarToCartesian( Point.of( 1, 1.5 * Math.PI, 0 ) ) ).isCloseTo( Point.of( 0, -1, 0 ) );
		VectorAssert.assertThat( Geometry.polarToCartesian( Point.of( 1, 2.0 * Math.PI, 0 ) ) ).isCloseTo( Point.of( 1, 0, 0 ) );

		VectorAssert.assertThat( Geometry.polarToCartesian( Point.of( Constants.SQRT_TWO, 0.25 * Math.PI, 0 ) ) ).isCloseTo( Point.of( 1, 1, 0 ) );
		VectorAssert.assertThat( Geometry.polarToCartesian( Point.of( Constants.SQRT_TWO, 0.75 * Math.PI, 0 ) ) ).isCloseTo( Point.of( -1, 1, 0 ) );
		VectorAssert.assertThat( Geometry.polarToCartesian( Point.of( Constants.SQRT_TWO, 1.25 * Math.PI, 0 ) ) ).isCloseTo( Point.of( -1, -1, 0 ) );
		VectorAssert.assertThat( Geometry.polarToCartesian( Point.of( Constants.SQRT_TWO, 1.75 * Math.PI, 0 ) ) ).isCloseTo( Point.of( 1, -1, 0 ) );

		VectorAssert.assertThat( Geometry.polarToCartesian( Point.of( Constants.SQRT_TWO, -0.25 * Math.PI, 0 ) ) ).isCloseTo( Point.of( 1, -1, 0 ) );
		VectorAssert.assertThat( Geometry.polarToCartesian( Point.of( Constants.SQRT_TWO, -0.75 * Math.PI, 0 ) ) ).isCloseTo( Point.of( -1, -1, 0 ) );
		VectorAssert.assertThat( Geometry.polarToCartesian( Point.of( Constants.SQRT_TWO, -1.25 * Math.PI, 0 ) ) ).isCloseTo( Point.of( -1, 1, 0 ) );
		VectorAssert.assertThat( Geometry.polarToCartesian( Point.of( Constants.SQRT_TWO, -1.75 * Math.PI, 0 ) ) ).isCloseTo( Point.of( 1, 1, 0 ) );
	}

	@Test
	void testPolarDegreesToCartesian() {
		VectorAssert.assertThat( Geometry.polarDegreesToCartesian( Point.of( 1, 0, 0 ) ) ).isCloseTo( Point.of( 1, 0, 0 ) );
		VectorAssert.assertThat( Geometry.polarDegreesToCartesian( Point.of( 1, 90, 0 ) ) ).isCloseTo( Point.of( 0, 1, 0 ) );
		VectorAssert.assertThat( Geometry.polarDegreesToCartesian( Point.of( 1, 180, 0 ) ) ).isCloseTo( Point.of( -1, 0, 0 ) );
		VectorAssert.assertThat( Geometry.polarDegreesToCartesian( Point.of( 1, 270, 0 ) ) ).isCloseTo( Point.of( 0, -1, 0 ) );
		VectorAssert.assertThat( Geometry.polarDegreesToCartesian( Point.of( 1, 360, 0 ) ) ).isCloseTo( Point.of( 1, 0, 0 ) );

		VectorAssert.assertThat( Geometry.polarDegreesToCartesian( Point.of( Constants.SQRT_TWO, 45, 0 ) ) ).isCloseTo( Point.of( 1, 1, 0 ) );
		VectorAssert.assertThat( Geometry.polarDegreesToCartesian( Point.of( Constants.SQRT_TWO, 135, 0 ) ) ).isCloseTo( Point.of( -1, 1, 0 ) );
		VectorAssert.assertThat( Geometry.polarDegreesToCartesian( Point.of( Constants.SQRT_TWO, 225, 0 ) ) ).isCloseTo( Point.of( -1, -1, 0 ) );
		VectorAssert.assertThat( Geometry.polarDegreesToCartesian( Point.of( Constants.SQRT_TWO, 315, 0 ) ) ).isCloseTo( Point.of( 1, -1, 0 ) );

		VectorAssert.assertThat( Geometry.polarDegreesToCartesian( Point.of( Constants.SQRT_TWO, -45, 0 ) ) ).isCloseTo( Point.of( 1, -1, 0 ) );
		VectorAssert.assertThat( Geometry.polarDegreesToCartesian( Point.of( Constants.SQRT_TWO, -135, 0 ) ) ).isCloseTo( Point.of( -1, -1, 0 ) );
		VectorAssert.assertThat( Geometry.polarDegreesToCartesian( Point.of( Constants.SQRT_TWO, -225, 0 ) ) ).isCloseTo( Point.of( -1, 1, 0 ) );
		VectorAssert.assertThat( Geometry.polarDegreesToCartesian( Point.of( Constants.SQRT_TWO, -315, 0 ) ) ).isCloseTo( Point.of( 1, 1, 0 ) );
	}

	@Test
	void testAngleWithTwoVectors() {
		assertThat( Geometry.getAngle( Vector.ZERO, Vector.ZERO ) ).isEqualTo( 0.0 );

		assertThat( Geometry.getAngle( Vector.of( 1, 0, 0 ), Vector.of( 1, 0, 0 ) ) ).isEqualTo( 0.0 );
		assertThat( Geometry.getAngle( Vector.of( 0, 1, 0 ), Vector.of( 0, 1, 0 ) ) ).isEqualTo( 0.0 );

		assertThat( Geometry.getAngle( Vector.of( 1, 0, 0 ), Vector.of( 0, 1, 0 ) ) ).isEqualTo( Constants.QUARTER_CIRCLE );
		assertThat( Geometry.getAngle( Vector.of( 1, 0, 0 ), Vector.of( 0, -1, 0 ) ) ).isEqualTo( -Constants.QUARTER_CIRCLE );
		assertThat( Geometry.getAngle( Vector.of( -1, 0, 0 ), Vector.of( 0, 1, 0 ) ) ).isEqualTo( -Constants.QUARTER_CIRCLE );
		assertThat( Geometry.getAngle( Vector.of( -1, 0, 0 ), Vector.of( 0, -1, 0 ) ) ).isEqualTo( Constants.QUARTER_CIRCLE );

		assertThat( Geometry.getAngle( Vector.of( 0, 1, 0 ), Vector.of( 1, 0, 0 ) ) ).isEqualTo( -Constants.QUARTER_CIRCLE );
		assertThat( Geometry.getAngle( Vector.of( 0, 1, 0 ), Vector.of( -1, 0, 0 ) ) ).isEqualTo( Constants.QUARTER_CIRCLE );
		assertThat( Geometry.getAngle( Vector.of( 0, -1, 0 ), Vector.of( 1, 0, 0 ) ) ).isEqualTo( Constants.QUARTER_CIRCLE );
		assertThat( Geometry.getAngle( Vector.of( 0, -1, 0 ), Vector.of( -1, 0, 0 ) ) ).isEqualTo( -Constants.QUARTER_CIRCLE );

		assertThat( Geometry.getAngle( Vector.of( 0, 1, 0 ), Vector.of( 1, 1, 0 ) ) ).isEqualTo( -0.5 * Constants.QUARTER_CIRCLE );
		assertThat( Geometry.getAngle( Vector.of( 0, 1, 0 ), Vector.of( -1, 1, 0 ) ) ).isEqualTo( 0.5 * Constants.QUARTER_CIRCLE );
	}

	@Test
	void testNormalizeAngle() {
		assertThat( Geometry.normalizeAngle( 0 ) ).isEqualTo( 0.0 );
		assertThat( Geometry.normalizeAngle( -Math.PI ) ).isEqualTo( Math.PI );

		assertThat( Geometry.normalizeAngle( Math.PI + 1 ) ).isEqualTo( -Math.PI + 1 );
		assertThat( Geometry.normalizeAngle( -Math.PI - 1 ) ).isEqualTo( Math.PI - 1 );

		assertThat( Geometry.normalizeAngle( 13 * Math.PI + 1 ) ).isCloseTo( -Math.PI + 1, Offset.offset( 1e-12 ) );
		assertThat( Geometry.normalizeAngle( 13 * -Math.PI - 1 ) ).isCloseTo( Math.PI - 1, Offset.offset( 1e-12 ) );
	}

	@Test
	void testAbsAngleWithTwoVectors() {
		assertThat( Geometry.getAbsAngle( Vector.ZERO, Vector.ZERO ) ).isNaN();

		assertThat( Geometry.getAbsAngle( Vector.of( 1, 0, 0 ), Vector.of( 1, 0, 0 ) ) ).isEqualTo( 0.0 );
		assertThat( Geometry.getAbsAngle( Vector.of( 0, 1, 0 ), Vector.of( 0, 1, 0 ) ) ).isEqualTo( 0.0 );
		assertThat( Geometry.getAbsAngle( Vector.of( 0, 0, 1 ), Vector.of( 0, 0, 1 ) ) ).isEqualTo( 0.0 );

		assertThat( Geometry.getAbsAngle( Vector.of( 0, 0, 1 ), Vector.of( 1, 0, 0 ) ) ).isEqualTo( Math.PI / 2 );
		assertThat( Geometry.getAbsAngle( Vector.of( 0, 0, 1 ), Vector.of( 0, 1, 0 ) ) ).isEqualTo( Math.PI / 2 );
		assertThat( Geometry.getAbsAngle( Vector.of( 0, 0, 1 ), Vector.of( -1, 0, 0 ) ) ).isEqualTo( Math.PI / 2 );
		assertThat( Geometry.getAbsAngle( Vector.of( 0, 0, 1 ), Vector.of( 0, -1, 0 ) ) ).isEqualTo( Math.PI / 2 );
		assertThat( Geometry.getAbsAngle( Vector.of( 0, 0, 1 ), Vector.of( 1, 1, 0 ) ) ).isEqualTo( Math.PI / 2 );
		assertThat( Geometry.getAbsAngle( Vector.of( 0, 0, 1 ), Vector.of( -1, 1, 0 ) ) ).isEqualTo( Math.PI / 2 );

		assertThat( Geometry.getAbsAngle( Vector.of( 0, 0, 1 ), Vector.of( 0, 0, -1 ) ) ).isEqualTo( Math.PI );
	}

	@Test
	void testAngleInXYPlaneAndVector() {
		double[] normal = Vector.of( 0, 0, 1 );
		assertThat( Geometry.getAngle( Vector.ZERO, normal, Vector.of( 1, 0, 0 ), Vector.of( 1, 0, 0 ) ) ).isCloseTo( 0.0, Offset.offset( 1e-15 ) );

		assertThat( Geometry.getAngle( Vector.ZERO, normal, Vector.of( 1, 0, 0 ), Vector.of( 1, 1, 0 ) ) ).isCloseTo( Math.PI * 0.25, Offset.offset( 1e-15 ) );
		assertThat( Geometry.getAngle( Vector.ZERO, normal, Vector.of( 1, 0, 0 ), Vector.of( 0, 1, 0 ) ) ).isCloseTo( Math.PI * 0.5, Offset.offset( 1e-15 ) );
		assertThat( Geometry.getAngle( Vector.ZERO, normal, Vector.of( 1, 0, 0 ), Vector.of( -1, 1, 0 ) ) ).isCloseTo( Math.PI * 0.75, Offset.offset( 1e-15 ) );

		assertThat( Geometry.getAngle( Vector.ZERO, normal, Vector.of( 1, 0, 0 ), Vector.of( -1, 0, 0 ) ) ).isCloseTo( Math.PI, Offset.offset( 1e-15 ) );

		assertThat( Geometry.getAngle( Vector.ZERO, normal, Vector.of( 1, 0, 0 ), Vector.of( -1, -1, 0 ) ) ).isCloseTo( -Math.PI * 0.75, Offset.offset( 1e-15 ) );
		assertThat( Geometry.getAngle( Vector.ZERO, normal, Vector.of( 1, 0, 0 ), Vector.of( 0, -1, 0 ) ) ).isCloseTo( -Math.PI * 0.5, Offset.offset( 1e-15 ) );
		assertThat( Geometry.getAngle( Vector.ZERO, normal, Vector.of( 1, 0, 0 ), Vector.of( 1, -1, 0 ) ) ).isCloseTo( -Math.PI * 0.25, Offset.offset( 1e-15 ) );

		assertThat( Geometry.getAngle( Vector.of( -2, 3, 0 ), normal, Vector.of( -0.75, 3, 0 ), Vector.of( -1.625, 3 - 0.5, 0 ) ) ).isCloseTo( -0.9272952180016123, Offset.offset( 1e-15 ) );
	}

	@Test
	void testPointAngle() {
		assertThat( Geometry.pointAngle( Vector.of( 4, -2 ), Vector.of( 3, -2 ), Vector.of( 4, -2 ) ) ).isEqualTo( 0.0 );
		assertThat( Geometry.pointAngle( Vector.of( 4, -2 ), Vector.of( 3, -2 ), Vector.of( 4, -1 ) ) ).isEqualTo( Math.PI * 0.25 );
		assertThat( Geometry.pointAngle( Vector.of( 4, -2 ), Vector.of( 3, -2 ), Vector.of( 3, -1 ) ) ).isEqualTo( Math.PI * 0.5 );
		assertThat( Geometry.pointAngle( Vector.of( 4, -2 ), Vector.of( 3, -2 ), Vector.of( 2, -1 ) ) ).isEqualTo( Math.PI * 0.75 );
		assertThat( Geometry.pointAngle( Vector.of( 2, -2 ), Vector.of( 3, -2 ), Vector.of( 4, -2 ) ) ).isEqualTo( Math.PI );
		assertThat( Geometry.pointAngle( Vector.of( 4, -2 ), Vector.of( 3, -2 ), Vector.of( 2, -3 ) ) ).isEqualTo( -Math.PI * 0.75 );
		assertThat( Geometry.pointAngle( Vector.of( 4, -2 ), Vector.of( 3, -2 ), Vector.of( 3, -3 ) ) ).isEqualTo( -Math.PI * 0.5 );
		assertThat( Geometry.pointAngle( Vector.of( 4, -2 ), Vector.of( 3, -2 ), Vector.of( 4, -3 ) ) ).isEqualTo( -Math.PI * 0.25 );
		assertThat( Geometry.pointAngle( Vector.of( 4, -2 ), Vector.of( 3, -2 ), Vector.of( 6, -2 ) ) ).isEqualTo( 0.0 );

		assertThat( Geometry.pointAngle( Vector.of( 4, -1 ), Vector.of( 3, -2 ), Vector.of( 2, -1 ) ) ).isEqualTo( Math.PI * 0.5 );
		assertThat( Geometry.pointAngle( Vector.of( 2, -1 ), Vector.of( 3, -2 ), Vector.of( 4, -1 ) ) ).isEqualTo( -Math.PI * 0.5 );
		assertThat( Geometry.pointAngle( Vector.of( -4, -1 ), Vector.of( -3, 0 ), Vector.of( -2, -1 ) ) ).isEqualTo( Math.PI * 0.5 );
		assertThat( Geometry.pointAngle( Vector.of( -2, -1 ), Vector.of( -3, 0 ), Vector.of( -4, -1 ) ) ).isEqualTo( -Math.PI * 0.5 );
	}

}
