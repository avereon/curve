package com.avereon.curve.math;

import org.junit.jupiter.api.Test;

import java.nio.DoubleBuffer;

import static com.avereon.curve.match.Matchers.near;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TransformTest {

	@Test
	void testConstructorWithArrays() {
		new Transform( new double[ 4 ][ 4 ] );
	}

	@Test
	void testConstructorWithNumbers() {
		new Transform( 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 );
	}

	@Test
	void testGetMatrix() {
		Transform transform = new Transform( 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 );
		DoubleBuffer matrix = transform.getMatrix();
		matrix.rewind();
		assertThat( matrix.get(), is( 0.0 ) );
		assertThat( matrix.get(), is( 4.0 ) );
		assertThat( matrix.get(), is( 8.0 ) );
		assertThat( matrix.get(), is( 12.0 ) );
		assertThat( matrix.get(), is( 1.0 ) );
		assertThat( matrix.get(), is( 5.0 ) );
		assertThat( matrix.get(), is( 9.0 ) );
		assertThat( matrix.get(), is( 13.0 ) );
		assertThat( matrix.get(), is( 2.0 ) );
		assertThat( matrix.get(), is( 6.0 ) );
		assertThat( matrix.get(), is( 10.0 ) );
		assertThat( matrix.get(), is( 14.0 ) );
		assertThat( matrix.get(), is( 3.0 ) );
		assertThat( matrix.get(), is( 7.0 ) );
		assertThat( matrix.get(), is( 11.0 ) );
		assertThat( matrix.get(), is( 15.0 ) );
	}

	@Test
	void testTimes() {
		double[] vector = Transform.scale( 2, 2, 2 ).times( Vector.of( 1, 2, 3 ) );
		assertThat( vector, is( Vector.of( 2, 4, 6 ) ) );
	}

	@Test
	void testTimesDirection() {
		double[] vector = Transform.identity().timesDirection( Vector.of( 1, 2, 3 ) );
		assertThat( vector, is( Vector.of( 1, 2, 3 ) ) );
	}

	@Test
	void testTimesXY() {
		double[] vector = Transform.identity().timesXY( Vector.of( 1, 2, 3 ) );
		assertThat( vector, is( Vector.of( 1, 2, 0 ) ) );
	}

	@Test
	void testTimesZ() {
		double z = Transform.identity().timesZ( Vector.of( 1, 2, 3 ) );
		assertThat( z, is( 3.0 ) );
	}

	@Test
	void testCombine() {
		assertMatrixValues( Transform.identity().combine( Transform.identity() ), 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1 );
		assertMatrixValues( Transform.translation( 1, 2, 3 ).combine( Transform.translation( 4, 5, 6 ) ), 1, 0, 0, 5, 0, 1, 0, 7, 0, 0, 1, 9, 0, 0, 0, 1 );
		assertMatrixValues( Transform.translation( 1, 2, 3 ).combine( Transform.frustrum( -1, 1, -1, 1, -1, -3 ) ), -1, 0, -1, 0, 0, -1, -2, 0, 0, 0, -5, 3, 0, 0, -1, 0 );
	}

	@Test
	void testIdentity() {
		Transform transform = Transform.identity();
		assertMatrixValues( transform, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1 );
	}

	@Test
	void testScale() {
		Transform transform = Transform.scale( 1, 2, 3 );
		assertMatrixValues( transform, 1, 0, 0, 0, 0, 2, 0, 0, 0, 0, 3, 0, 0, 0, 0, 1 );
		assertThat( transform.times( Vector.of( 1, 1, 1 ) ), is( Vector.of( 1, 2, 3 ) ) );
	}

	@Test
	void testScaleWithOrigin() {
		Transform transform = Transform.scale( Vector.of( 1, 1, 1 ), 1, 2, 3 );
		assertMatrixValues( transform, 1, 0, 0, 0, 0, 2, 0, -1, 0, 0, 3, -2, 0, 0, 0, 1 );
		assertThat( transform.times( Vector.of( 0, 0, 0 ) ), is( Vector.of( 0, -1, -2 ) ) );
		assertThat( transform.times( Vector.of( 1, 1, 1 ) ), is( Vector.of( 1, 1, 1 ) ) );
		assertThat( transform.times( Vector.of( 2, 2, 2 ) ), is( Vector.of( 2, 3, 4 ) ) );
	}

	@Test
	void testTranslation() {
		Transform transform = Transform.translation( 1, 2, 3 );
		assertMatrixValues( transform, 1, 0, 0, 1, 0, 1, 0, 2, 0, 0, 1, 3, 0, 0, 0, 1 );
		assertThat( transform.times( Vector.of( 1, 1, 1 ) ), is( Vector.of( 2, 3, 4 ) ) );
	}

	@Test
	void testRotation() {
		Transform transform = Transform.rotation( Vector.of( 1, 1, 0 ), Math.PI );
		assertMatrixValues( transform, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 1 );
		assertThat( Geometry.distance( Vector.of( 0, 1, 0 ), transform.times( Vector.of( 1, 0, 0 ) ) ), closeTo( 0.0, 1e-15 ) );

		transform = Transform.rotation( Vector.UNIT_Y, 0 );
		assertThat( transform.times( Vector.UNIT_X ), is( Vector.UNIT_X ) );
	}

	@Test
	void testRotationWithZeroAxis() {
		Transform transform = Transform.rotation( Vector.of(), Math.PI );
		assertThat( transform.times( Vector.UNIT_X ), is( Vector.UNIT_X ) );
	}

	@Test
	void testRotationWithZeroAngle() {
		Transform transform = Transform.rotation( Vector.UNIT_Y, 0 );
		assertThat( transform.times( Vector.UNIT_X ), is( Vector.UNIT_X ) );
	}

	@Test
	void testRotationWithOrigin() {
		Transform transform = Transform.rotation( Vector.of( 1, 1, 0 ), Vector.UNIT_Z, Constants.QUARTER_CIRCLE );
		assertThat( transform.times( Vector.of( 2, 2, 0 ) ), near( Vector.of( 0, 2 ), 1e-15 ) );
	}

	@Test
	void testXrotation() {
		Transform transform = Transform.xrotation( Math.PI / 2 );
		assertMatrixValues( transform, 1, 0, 0, 0, 0, 0, -1, 0, 0, 1, 0, 0, 0, 0, 0, 1 );
		assertThat( Geometry.distance( Vector.of( 0, 0, 1 ), transform.times( Vector.of( 0, 1, 0 ) ) ), closeTo( 0.0, 1e-16 ) );
	}

	@Test
	void testYrotation() {
		Transform transform = Transform.yrotation( Math.PI / 2 );
		assertMatrixValues( transform, 0, 0, 1, 0, 0, 1, 0, 0, -1, 0, 0, 0, 0, 0, 0, 1 );
		assertThat( Geometry.distance( Vector.of( 0, 0, -1 ), transform.times( Vector.of( 1, 0, 0 ) ) ), closeTo( 0.0, 1e-16 ) );
	}

	@Test
	void testZrotation() {
		Transform transform = Transform.zrotation( Math.PI / 2 );
		assertMatrixValues( transform, 0, -1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1 );
		assertThat( Geometry.distance( Vector.of( -1, 0, 0 ), transform.times( Vector.of( 0, 1, 0 ) ) ), closeTo( 0.0, 1e-16 ) );
	}

	//	@Test
	//	 void testMirror() throws Exception {
	//		Transform transform = null;
	//
	//		transform = Transform.mirror( Vector.of( 1, 0 ), Vector.of( 1, 1 ), Vector.getUnitZ() );
	//		assertThat( Vector.of(), transform.times( Vector.of( 2, 0, 0 ) ), 1e-12 );
	//
	//		transform = Transform.mirror( Vector.of( 2, 0 ), Vector.of( 0, 2 ), Vector.getUnitZ() );
	//		assertThat( Vector.of(), transform.times( Vector.of( 2, 2, 0 ) ), 1e-12 );
	//	}

	@Test
	void testLocalTransform() {
		Transform transform = Transform.localTransform( Vector.of( 1, 0, 0 ), Vector.of( 0, 0, 1 ), Vector.of( 0, 1, 0 ) );
		assertMatrixValues( transform, 1, 0, 0, -1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1 );
		assertThat( transform.times( Vector.of( 1, 0, 0 ) ), is( Vector.of( 0, 0, 0 ) ) );
	}

	@Test
	void testWorldTransform() {
		Transform transform = Transform.targetTransform( Vector.of( 1, 0, 0 ), Vector.of( 0, 0, 1 ), Vector.of( 0, 1, 0 ) );
		assertMatrixValues( transform, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1 );
		assertThat( transform.times( Vector.of( 0, 0, 0 ) ), is( Vector.of( 1, 0, 0 ) ) );
	}

	@Test
	void testOrtho() {
		Transform transform = Transform.ortho( -1, 1, -1, 1, -1, 1 );
		assertMatrixValues( transform, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, -1, 0, 0, 0, 0, 1 );
	}

	@Test
	void testFrustrum() {
		Transform transform = Transform.frustrum( -1, 1, -1, 1, 0.2, 1 );
		assertMatrixValues( transform, 0.2, 0, 0, 0, 0, 0.2, 0, 0, 0, 0, -1.5, -0.5, 0, 0, -1, 0 );
	}

	@Test
	void testPerspective() {
		Transform transform = Transform.perspective( 1 );
		assertMatrixValues( transform, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0.25, 0.75, 0, 0, 0.25, 0.75 );
		assertThat( transform.times( Vector.of( 1, 1, 2 ) ), is( Vector.of( 0.8, 0.8, 1 ) ) );
	}

	@Test
	void testInverseIdentity() {
		Transform transform = Transform.identity();
		transform = transform.inverse();
		assertThat( transform.times( Vector.of( 1, 1, 1 ) ), is( Vector.of( 1, 1, 1 ) ) );
	}

	@Test
	void testInverseScale() {
		Transform transform = Transform.scale( 1, 2, 3 );
		transform = transform.inverse();
		assertThat( transform.times( Vector.of( 1, 1, 1 ) ), is( Vector.of( 1, 1 / 2.0, 1 / 3.0 ) ) );
	}

	@Test
	void testInverseTranslation() {
		Transform transform = Transform.translation( 1, 2, 3 );
		transform = transform.inverse();
		assertThat( transform.times( Vector.of() ), is( Vector.of( -1, -2, -3 ) ) );
	}

	@Test
	void testInverseRotation() {
		Transform transform = Transform.rotation( Vector.of( 0, 0, 1 ), Math.PI / 2 );
		transform = transform.inverse();
		assertThat( transform.times( Vector.of( 1, 0, 0 ) ), near( Vector.of( 0, -1, 0 ), 1E-16 ) );
	}

	@Test
	public void testInverseOrtho() {
		Transform transform = Transform.ortho( -1, 1, -1, 1, -1, -3 );
		transform = transform.inverse();
		assertThat( transform.times( Vector.of( 0, 0, -1 ) ), is( Vector.of( 0, 0, 1 ) ) );
		assertThat( transform.times( Vector.of( 0, 0, -0.5 ) ), is( Vector.of( 0, 0, 1.5 ) ) );
		assertThat( transform.times( Vector.of( 0, 0, 0 ) ), is( Vector.of( 0, 0, 2 ) ) );
		assertThat( transform.times( Vector.of( 0, 0, 0.5 ) ), is( Vector.of( 0, 0, 2.5 ) ) );
		assertThat( transform.times( Vector.of( 0, 0, 1 ) ), is( Vector.of( 0, 0, 3 ) ) );
	}

	@Test
	public void testInverseFrustrum() {
		Transform transform = Transform.frustrum( -1, 1, -1, 1, -1, -3 );
		transform = transform.inverse();
		assertThat( transform.times( Vector.of( 0, 0, -1 ) ), is( Vector.of( 0, 0, 1 ) ) );
		assertThat( transform.times( Vector.of( 0, 0, 0 ) ), is( Vector.of( 0, 0, 1.5 ) ) );
		assertThat( transform.times( Vector.of( 0, 0, 0.5 ) ), is( Vector.of( 0, 0, 2 ) ) );
		assertThat( transform.times( Vector.of( 0, 0, 0.8 ) ), is( Vector.of( 0, 0, 2.5 ) ) );
		assertThat( transform.times( Vector.of( 0, 0, 1 ) ), is( Vector.of( 0, 0, 3 ) ) );
	}

	@Test
	void testEquals() {
		Transform transform1 = Transform.identity();
		Transform transform2 = Transform.identity();
		Transform transform3 = Transform.translation( 3, 2, 1 );
		assertThat( transform2, is( transform1 ) );
		assertThat( transform1, is( transform2 ) );
		assertThat( transform3, is( not( transform1 ) ) );
	}

	@Test
	void testHashCode() {
		Transform transform = Transform.identity();
		assertThat( transform.hashCode(), is( 1082130432 ) );
	}

	@Test
	public void testToString() {
		StringBuilder builder = new StringBuilder();
		builder.append( "[\n" );
		builder.append( "  1.0, 0.0, 0.0, 0.0,\n" );
		builder.append( "  0.0, 1.0, 0.0, 0.0,\n" );
		builder.append( "  0.0, 0.0, 1.0, 0.0,\n" );
		builder.append( "  0.0, 0.0, 0.0, 1.0\n" );
		builder.append( "]\n" );
		assertThat( Transform.identity().toString(), is( builder.toString() ) );
	}

	private static void assertMatrixValues( Transform transform, double e00, double e01, double e02, double e03, double e10, double e11, double e12, double e13, double e20, double e21, double e22, double e23, double e30, double e31, double e32, double e33 ) {
		double[][] m = transform.getMatrixArray();
		assertThat( m[ 0 ][ 0 ], closeTo( e00, 1e-12 ) );
		assertThat( m[ 0 ][ 1 ], closeTo( e01, 1e-12 ) );
		assertThat( m[ 0 ][ 2 ], closeTo( e02, 1e-12 ) );
		assertThat( m[ 0 ][ 3 ], closeTo( e03, 1e-12 ) );
		assertThat( m[ 1 ][ 0 ], closeTo( e10, 1e-12 ) );
		assertThat( m[ 1 ][ 1 ], closeTo( e11, 1e-12 ) );
		assertThat( m[ 1 ][ 2 ], closeTo( e12, 1e-12 ) );
		assertThat( m[ 1 ][ 3 ], closeTo( e13, 1e-12 ) );
		assertThat( m[ 2 ][ 0 ], closeTo( e20, 1e-12 ) );
		assertThat( m[ 2 ][ 1 ], closeTo( e21, 1e-12 ) );
		assertThat( m[ 2 ][ 2 ], closeTo( e22, 1e-12 ) );
		assertThat( m[ 2 ][ 3 ], closeTo( e23, 1e-12 ) );
		assertThat( m[ 3 ][ 0 ], closeTo( e30, 1e-12 ) );
		assertThat( m[ 3 ][ 1 ], closeTo( e31, 1e-12 ) );
		assertThat( m[ 3 ][ 2 ], closeTo( e32, 1e-12 ) );
		assertThat( m[ 3 ][ 3 ], closeTo( e33, 1e-12 ) );
	}

}
