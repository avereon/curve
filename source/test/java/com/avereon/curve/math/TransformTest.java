package com.avereon.curve.math;

import com.avereon.curve.assertion.VectorAssert;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import java.nio.DoubleBuffer;

import static org.assertj.core.api.Assertions.assertThat;

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
		assertThat( matrix.get() ).isEqualTo( 0.0 );
		assertThat( matrix.get() ).isEqualTo( 4.0 );
		assertThat( matrix.get() ).isEqualTo( 8.0 );
		assertThat( matrix.get() ).isEqualTo( 12.0 );
		assertThat( matrix.get() ).isEqualTo( 1.0 );
		assertThat( matrix.get() ).isEqualTo( 5.0 );
		assertThat( matrix.get() ).isEqualTo( 9.0 );
		assertThat( matrix.get() ).isEqualTo( 13.0 );
		assertThat( matrix.get() ).isEqualTo( 2.0 );
		assertThat( matrix.get() ).isEqualTo( 6.0 );
		assertThat( matrix.get() ).isEqualTo( 10.0 );
		assertThat( matrix.get() ).isEqualTo( 14.0 );
		assertThat( matrix.get() ).isEqualTo( 3.0 );
		assertThat( matrix.get() ).isEqualTo( 7.0 );
		assertThat( matrix.get() ).isEqualTo( 11.0 );
		assertThat( matrix.get() ).isEqualTo( 15.0 );
	}

	@Test
	void testApply() {
		double[] vector = Transform.scale( 2, 2, 2 ).apply( Vector.of( 1, 2, 3 ) );
		assertThat( vector ).isEqualTo( Vector.of( 2, 4, 6 ) );
	}

	@Test
	void testApplyDirection() {
		double[] vector = Transform.identity().applyDirection( Vector.of( 1, 2, 3 ) );
		assertThat( vector ).isEqualTo( Vector.of( 1, 2, 3 ) );
	}

	@Test
	void testApplyXY() {
		double[] vector = Transform.identity().applyXY( Vector.of( 1, 2, 3 ) );
		assertThat( vector ).isEqualTo( Vector.of( 1, 2, 0 ) );
	}

	@Test
	void testApplyZ() {
		double z = Transform.identity().applyZ( Vector.of( 1, 2, 3 ) );
		assertThat( z ).isEqualTo( 3.0 );
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
		assertThat( transform.apply( Vector.of( 1, 1, 1 ) ) ).isEqualTo( Vector.of( 1, 2, 3 ) );
	}

	@Test
	void testScaleWithOrigin() {
		Transform transform = Transform.scale( Vector.of( 1, 1, 1 ), 1, 2, 3 );
		assertMatrixValues( transform, 1, 0, 0, 0, 0, 2, 0, -1, 0, 0, 3, -2, 0, 0, 0, 1 );
		assertThat( transform.apply( Vector.of( 0, 0, 0 ) ) ).isEqualTo( Vector.of( 0, -1, -2 ) );
		assertThat( transform.apply( Vector.of( 1, 1, 1 ) ) ).isEqualTo( Vector.of( 1, 1, 1 ) );
		assertThat( transform.apply( Vector.of( 2, 2, 2 ) ) ).isEqualTo( Vector.of( 2, 3, 4 ) );
	}

	@Test
	void testTranslation() {
		Transform transform = Transform.translation( 1, 2, 3 );
		assertMatrixValues( transform, 1, 0, 0, 1, 0, 1, 0, 2, 0, 0, 1, 3, 0, 0, 0, 1 );
		assertThat( transform.apply( Vector.of( 1, 1, 1 ) ) ).isEqualTo( Vector.of( 2, 3, 4 ) );
	}

	@Test
	void testRotation() {
		Transform transform = Transform.rotation( Vector.of( 1, 1, 0 ), Math.PI );
		assertMatrixValues( transform, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 1 );
		assertThat( Geometry.distance( Vector.of( 0, 1, 0 ), transform.apply( Vector.of( 1, 0, 0 ) ) ) ).isCloseTo( 0.0, Offset.offset( 1e-15 ) );

		transform = Transform.rotation( Vector.UNIT_Y, 0 );
		assertThat( transform.apply( Vector.UNIT_X ) ).isEqualTo( Vector.UNIT_X );
	}

	@Test
	void testRotationWithZeroAxis() {
		Transform transform = Transform.rotation( Vector.of(), Math.PI );
		assertThat( transform.apply( Vector.UNIT_X ) ).isEqualTo( Vector.UNIT_X );
	}

	@Test
	void testRotationWithZeroAngle() {
		Transform transform = Transform.rotation( Vector.UNIT_Y, 0 );
		assertThat( transform.apply( Vector.UNIT_X ) ).isEqualTo( Vector.UNIT_X );
	}

	@Test
	void testRotationWithOrigin() {
		Transform transform = Transform.rotation( Vector.of( 1, 1, 0 ), Vector.UNIT_Z, Constants.QUARTER_CIRCLE );
		VectorAssert.assertThat( transform.apply( Vector.of( 2, 2, 0 ) ) ).isCloseTo( Vector.of( 0, 2 ), 1e-15 );
	}

	@Test
	void testXrotation() {
		Transform transform = Transform.xrotation( Math.PI / 2 );
		assertMatrixValues( transform, 1, 0, 0, 0, 0, 0, -1, 0, 0, 1, 0, 0, 0, 0, 0, 1 );
		assertThat( Geometry.distance( Vector.of( 0, 0, 1 ), transform.apply( Vector.of( 0, 1, 0 ) ) ) ).isCloseTo( 0.0, Offset.offset( 1e-16 ) );
	}

	@Test
	void testYrotation() {
		Transform transform = Transform.yrotation( Math.PI / 2 );
		assertMatrixValues( transform, 0, 0, 1, 0, 0, 1, 0, 0, -1, 0, 0, 0, 0, 0, 0, 1 );
		assertThat( Geometry.distance( Vector.of( 0, 0, -1 ), transform.apply( Vector.of( 1, 0, 0 ) ) ) ).isCloseTo( 0.0, Offset.offset( 1e-16 ) );
	}

	@Test
	void testZrotation() {
		Transform transform = Transform.zrotation( Math.PI / 2 );
		assertMatrixValues( transform, 0, -1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1 );
		assertThat( Geometry.distance( Vector.of( -1, 0, 0 ), transform.apply( Vector.of( 0, 1, 0 ) ) ) ).isCloseTo( 0.0, Offset.offset( 1e-16 ) );
	}

	@Test
	void testMirror() {
		Transform transform;

		transform = Transform.mirror( Vector.of( 1, 0 ), Vector.of( 1, 1 ), Vector.UNIT_Z );
		VectorAssert.assertThat( transform.apply( Vector.of( 2, 0, 0 ) ) ).isCloseTo( Vector.of() );

		transform = Transform.mirror( Vector.of( 2, 0 ), Vector.of( 0, 2 ), Vector.UNIT_Z );
		VectorAssert.assertThat( transform.apply( Vector.of( 2, 2, 0 ) ) ).isCloseTo( Vector.of() );
	}

	@Test
	void testIsMirror() {
		assertThat( Transform.mirror( Point.of( 0, 0, 0 ), Point.of( 0, 1, 0 ), Point.of( 0, 0, 1 ) ).isMirror() ).isTrue();
		assertThat( Transform.mirror( Point.of( 1, 1, -1 ), Point.of( 2, -2, 2 ), Point.of( -3, 3, 3 ) ).isMirror() ).isTrue();

		// Double mirrors should not be mirrors
		assertThat( Transform
			.mirror( Point.of( 0, 0, 0 ), Point.of( 0, 1, 0 ), Point.of( 0, 0, 1 ) )
			.combine( Transform.mirror( Point.of( 0, 0, 0 ), Point.of( 0, 1, 0 ), Point.of( 0, 0, 1 ) ) )
			.isMirror() ).isFalse();
		assertThat( Transform
			.mirror( Point.of( 1, 1, 1 ), Point.of( 2, 2, 2 ), Point.of( 3, 3, 3 ) )
			.combine( Transform.mirror( Point.of( 1, 1, 1 ), Point.of( 2, 2, 2 ), Point.of( 3, 3, 3 ) ) )
			.isMirror() ).isFalse();

		// Some types of scale should cause mirrors
		assertThat( Transform.scale( -1, 1, 1 ).isMirror() ).isTrue();
		assertThat( Transform.scale( 1, -1, 1 ).isMirror() ).isTrue();

		// Inverted scales should not be mirrors
		assertThat( Transform.scale( -1, 1, 1 ).combine( Transform.scale( -1, 1, 1 ) ).isMirror() ).isFalse();
		assertThat( Transform.scale( 1, -1, 1 ).combine( Transform.scale( 1, -1, 1 ) ).isMirror() ).isFalse();

		// Translations should not cause mirrors
		assertThat( Transform.translation( -1, 0, 0 ).isMirror() ).isFalse();
		assertThat( Transform.translation( 1, 0, 0 ).isMirror() ).isFalse();
		assertThat( Transform.translation( 0, -1, 0 ).isMirror() ).isFalse();
		assertThat( Transform.translation( 0, 1, 0 ).isMirror() ).isFalse();

		// Rotations should not cause mirrors
		assertThat( Transform.rotation( Point.of( 1, 1, 0 ), -270 ).isMirror() ).isFalse();
		assertThat( Transform.rotation( Point.of( 1, 1, 0 ), -180 ).isMirror() ).isFalse();
		assertThat( Transform.rotation( Point.of( 1, 1, 0 ), -90 ).isMirror() ).isFalse();
		assertThat( Transform.rotation( Point.of( 1, 1, 0 ), 0 ).isMirror() ).isFalse();
		assertThat( Transform.rotation( Point.of( 1, 1, 0 ), 90 ).isMirror() ).isFalse();
		assertThat( Transform.rotation( Point.of( 1, 1, 0 ), 180 ).isMirror() ).isFalse();
		assertThat( Transform.rotation( Point.of( 1, 1, 0 ), 270 ).isMirror() ).isFalse();
	}

	@Test
	void testLocalTransform() {
		Transform transform = Transform.localTransform( Vector.of( 1, 0, 0 ), Vector.of( 0, 0, 1 ), Vector.of( 0, 1, 0 ) );
		assertMatrixValues( transform, 1, 0, 0, -1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1 );
		assertThat( transform.apply( Vector.of( 1, 0, 0 ) ) ).isEqualTo( Vector.of( 0, 0, 0 ) );
	}

	@Test
	void testWorldTransform() {
		Transform transform = Transform.targetTransform( Vector.of( 1, 0, 0 ), Vector.of( 0, 0, 1 ), Vector.of( 0, 1, 0 ) );
		assertMatrixValues( transform, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1 );
		assertThat( transform.apply( Vector.of( 0, 0, 0 ) ) ).isEqualTo( Vector.of( 1, 0, 0 ) );
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
		assertThat( transform.apply( Vector.of( 1, 1, 2 ) ) ).isEqualTo( Vector.of( 0.8, 0.8, 1 ) );
	}

	@Test
	void testInverseIdentity() {
		Transform transform = Transform.identity();
		transform = transform.inverse();
		assertThat( transform.apply( Vector.of( 1, 1, 1 ) ) ).isEqualTo( Vector.of( 1, 1, 1 ) );
	}

	@Test
	void testInverseScale() {
		Transform transform = Transform.scale( 1, 2, 3 );
		transform = transform.inverse();
		assertThat( transform.apply( Vector.of( 1, 1, 1 ) ) ).isEqualTo( Vector.of( 1, 1 / 2.0, 1 / 3.0 ) );
	}

	@Test
	void testInverseTranslation() {
		Transform transform = Transform.translation( 1, 2, 3 );
		transform = transform.inverse();
		assertThat( transform.apply( Vector.of() ) ).isEqualTo( Vector.of( -1, -2, -3 ) );
	}

	@Test
	void testInverseRotation() {
		Transform transform = Transform.rotation( Vector.of( 0, 0, 1 ), Math.PI / 2 );
		transform = transform.inverse();
		VectorAssert.assertThat( transform.apply( Vector.of( 1, 0, 0 ) ) ).isCloseTo( Vector.of( 0, -1, 0 ) );
	}

	@Test
	public void testInverseOrtho() {
		Transform transform = Transform.ortho( -1, 1, -1, 1, -1, -3 );
		transform = transform.inverse();
		assertThat( transform.apply( Vector.of( 0, 0, -1 ) ) ).isEqualTo( Vector.of( 0, 0, 1 ) );
		assertThat( transform.apply( Vector.of( 0, 0, -0.5 ) ) ).isEqualTo( Vector.of( 0, 0, 1.5 ) );
		assertThat( transform.apply( Vector.of( 0, 0, 0 ) ) ).isEqualTo( Vector.of( 0, 0, 2 ) );
		assertThat( transform.apply( Vector.of( 0, 0, 0.5 ) ) ).isEqualTo( Vector.of( 0, 0, 2.5 ) );
		assertThat( transform.apply( Vector.of( 0, 0, 1 ) ) ).isEqualTo( Vector.of( 0, 0, 3 ) );
	}

	@Test
	public void testInverseFrustrum() {
		Transform transform = Transform.frustrum( -1, 1, -1, 1, -1, -3 );
		transform = transform.inverse();
		assertThat( transform.apply( Vector.of( 0, 0, -1 ) ) ).isEqualTo( Vector.of( 0, 0, 1 ) );
		assertThat( transform.apply( Vector.of( 0, 0, 0 ) ) ).isEqualTo( Vector.of( 0, 0, 1.5 ) );
		assertThat( transform.apply( Vector.of( 0, 0, 0.5 ) ) ).isEqualTo( Vector.of( 0, 0, 2 ) );
		assertThat( transform.apply( Vector.of( 0, 0, 0.8 ) ) ).isEqualTo( Vector.of( 0, 0, 2.5 ) );
		assertThat( transform.apply( Vector.of( 0, 0, 1 ) ) ).isEqualTo( Vector.of( 0, 0, 3 ) );
	}

	@Test
	void testEquals() {
		Transform transform1 = Transform.identity();
		Transform transform2 = Transform.identity();
		Transform transform3 = Transform.translation( 3, 2, 1 );
		assertThat( transform2 ).isEqualTo( transform1 );
		assertThat( transform1 ).isEqualTo( transform2 );
		assertThat( transform3 ).isNotEqualTo( transform1 );
	}

	@Test
	void testHashCode() {
		Transform transform = Transform.identity();
		assertThat( transform.hashCode() ).isEqualTo( 1082130432 );
	}

	@Test
	@SuppressWarnings( "StringBufferReplaceableByString" )
	public void testToString() {
		StringBuilder builder = new StringBuilder();
		builder.append( "[\n" );
		builder.append( "  1.0, 0.0, 0.0, 0.0,\n" );
		builder.append( "  0.0, 1.0, 0.0, 0.0,\n" );
		builder.append( "  0.0, 0.0, 1.0, 0.0,\n" );
		builder.append( "  0.0, 0.0, 0.0, 1.0\n" );
		builder.append( "]\n" );
		assertThat( Transform.identity().toString() ).isEqualTo( builder.toString() );
	}

	@SuppressWarnings( "SameParameterValue" )
	private static void assertMatrixValues(
		Transform transform,
		double e00,
		double e01,
		double e02,
		double e03,
		double e10,
		double e11,
		double e12,
		double e13,
		double e20,
		double e21,
		double e22,
		double e23,
		double e30,
		double e31,
		double e32,
		double e33
	) {
		double[][] m = transform.getMatrixArray();
		assertThat( m[ 0 ][ 0 ] ).isCloseTo( e00, Offset.offset( 1e-15 ) );
		assertThat( m[ 0 ][ 1 ] ).isCloseTo( e01, Offset.offset( 1e-15 ) );
		assertThat( m[ 0 ][ 2 ] ).isCloseTo( e02, Offset.offset( 1e-15 ) );
		assertThat( m[ 0 ][ 3 ] ).isCloseTo( e03, Offset.offset( 1e-15 ) );
		assertThat( m[ 1 ][ 0 ] ).isCloseTo( e10, Offset.offset( 1e-15 ) );
		assertThat( m[ 1 ][ 1 ] ).isCloseTo( e11, Offset.offset( 1e-15 ) );
		assertThat( m[ 1 ][ 2 ] ).isCloseTo( e12, Offset.offset( 1e-15 ) );
		assertThat( m[ 1 ][ 3 ] ).isCloseTo( e13, Offset.offset( 1e-15 ) );
		assertThat( m[ 2 ][ 0 ] ).isCloseTo( e20, Offset.offset( 1e-15 ) );
		assertThat( m[ 2 ][ 1 ] ).isCloseTo( e21, Offset.offset( 1e-15 ) );
		assertThat( m[ 2 ][ 2 ] ).isCloseTo( e22, Offset.offset( 1e-15 ) );
		assertThat( m[ 2 ][ 3 ] ).isCloseTo( e23, Offset.offset( 1e-15 ) );
		assertThat( m[ 3 ][ 0 ] ).isCloseTo( e30, Offset.offset( 1e-15 ) );
		assertThat( m[ 3 ][ 1 ] ).isCloseTo( e31, Offset.offset( 1e-15 ) );
		assertThat( m[ 3 ][ 2 ] ).isCloseTo( e32, Offset.offset( 1e-15 ) );
		assertThat( m[ 3 ][ 3 ] ).isCloseTo( e33, Offset.offset( 1e-15 ) );
	}

}
