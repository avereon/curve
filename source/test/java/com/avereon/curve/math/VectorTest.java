package com.avereon.curve.math;

import com.avereon.curve.assertion.VectorAssert;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class VectorTest {

	@Test
	void testZERO() {
		VectorAssert.assertThat( Vector.ZERO ).isCloseTo( new double[]{ 0, 0, 0 } );
	}

	@Test
	void testUNIT_X() {
		VectorAssert.assertThat( Vector.UNIT_X ).isCloseTo( new double[]{ 1, 0, 0 } );
	}

	@Test
	void testUNIT_Y() {
		VectorAssert.assertThat( Vector.UNIT_Y ).isCloseTo( new double[]{ 0, 1, 0 } );
	}

	@Test
	void testUNIT_Z() {
		VectorAssert.assertThat( Vector.UNIT_Z ).isCloseTo( new double[]{ 0, 0, 1 } );
	}

	@Test
	void testINFINITY() {
		VectorAssert.assertThat( Vector.INFINITY ).isEqualTo( new double[]{ Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY } );
	}

	@Test
	void testUNDEFINED() {
		VectorAssert.assertThat( Vector.UNDEFINED ).isEqualTo( new double[]{ Double.NaN, Double.NaN, Double.NaN } );
	}

	@Test
	void testOfWithTwoNumbers() {
		VectorAssert.assertThat( Vector.of( 0, 0 ) ).isCloseTo( new double[]{ 0, 0, 0 } );
		VectorAssert.assertThat( Vector.of( 1.2, 3.4 ) ).isCloseTo( new double[]{ 1.2, 3.4, 0 } );
	}

	@Test
	void testOfWithThreeNumbers() {
		VectorAssert.assertThat( Vector.of( 0, 0, 0 ) ).isCloseTo( new double[]{ 0, 0, 0 } );
		VectorAssert.assertThat( Vector.of( 1.2, 3.4, 5.6 ) ).isCloseTo( new double[]{ 1.2, 3.4, 5.6 } );
	}

	@Test
	void testDistance() {
		assertThat( Vector.distance( Vector.of( 0, 0 ), Vector.of( 0, 0 ) ) ).isCloseTo( 0.0, Offset.offset( 1e-16 ) );

		assertThat( Vector.distance( Vector.of( 0, 0 ), Vector.of( 1, 0 ) ) ).isCloseTo( 1.0, Offset.offset( 1e-16 ) );
		assertThat( Vector.distance( Vector.of( 0, 0 ), Vector.of( -1, 0 ) ) ).isCloseTo( 1.0, Offset.offset( 1e-16 ) );
		assertThat( Vector.distance( Vector.of( 0, 0 ), Vector.of( 0, 1 ) ) ).isCloseTo( 1.0, Offset.offset( 1e-16 ) );
		assertThat( Vector.distance( Vector.of( 0, 0 ), Vector.of( 0, -1 ) ) ).isCloseTo( 1.0, Offset.offset( 1e-16 ) );

		assertThat( Vector.distance( Vector.of( 1, 1 ), Vector.of( 5, 4 ) ) ).isCloseTo( 5.0, Offset.offset( 1e-16 ) );
		assertThat( Vector.distance( Vector.of( 1, 1 ), Vector.of( 4, 5 ) ) ).isCloseTo( 5.0, Offset.offset( 1e-16 ) );
		assertThat( Vector.distance( Vector.of( -1, 1 ), Vector.of( -5, 4 ) ) ).isCloseTo( 5.0, Offset.offset( 1e-16 ) );
		assertThat( Vector.distance( Vector.of( -1, 1 ), Vector.of( -4, 5 ) ) ).isCloseTo( 5.0, Offset.offset( 1e-16 ) );
		assertThat( Vector.distance( Vector.of( 1, -1 ), Vector.of( 5, -4 ) ) ).isCloseTo( 5.0, Offset.offset( 1e-16 ) );
		assertThat( Vector.distance( Vector.of( 1, -1 ), Vector.of( 4, -5 ) ) ).isCloseTo( 5.0, Offset.offset( 1e-16 ) );
		assertThat( Vector.distance( Vector.of( -1, -1 ), Vector.of( -5, -4 ) ) ).isCloseTo( 5.0, Offset.offset( 1e-16 ) );
		assertThat( Vector.distance( Vector.of( -1, -1 ), Vector.of( -4, -5 ) ) ).isCloseTo( 5.0, Offset.offset( 1e-16 ) );

		assertThat( Vector.distance( Vector.of( 0, 0 ), Vector.of( Double.NaN, 0 ) ) ).isNaN();
	}

	@Test
	void testIsUndefined() {
		assertThat( Vector.isUndefined( Vector.of( Double.NaN, 0, 0 ) ) ).isTrue();
		assertThat( Vector.isUndefined( Vector.of( 0, Double.NaN, 0 ) ) ).isTrue();
		assertThat( Vector.isUndefined( Vector.of( 0, 0, Double.NaN ) ) ).isTrue();
	}

	@Test
	void testIsInfinite() {
		assertThat( Vector.isInfinite( Vector.ZERO ) ).isFalse();

		assertThat( Vector.isInfinite( Vector.of( Double.NaN, 0, 0 ) ) ).isFalse();
		assertThat( Vector.isInfinite( Vector.of( 0, Double.NaN, 0 ) ) ).isFalse();
		assertThat( Vector.isInfinite( Vector.of( 0, 0, Double.NaN ) ) ).isFalse();

		assertThat( Vector.isInfinite( Vector.of( Double.POSITIVE_INFINITY, 0, 0 ) ) ).isTrue();
		assertThat( Vector.isInfinite( Vector.of( 0, Double.POSITIVE_INFINITY, 0 ) ) ).isTrue();
		assertThat( Vector.isInfinite( Vector.of( 0, 0, Double.POSITIVE_INFINITY ) ) ).isTrue();
		assertThat( Vector.isInfinite( Vector.of( Double.NEGATIVE_INFINITY, 0, 0 ) ) ).isTrue();
		assertThat( Vector.isInfinite( Vector.of( 0, Double.NEGATIVE_INFINITY, 0 ) ) ).isTrue();
		assertThat( Vector.isInfinite( Vector.of( 0, 0, Double.NEGATIVE_INFINITY ) ) ).isTrue();
	}

	@Test
	void testMagnitude() {
		assertThat( Vector.magnitude( Vector.of( 0, 0, 0 ) ) ).isCloseTo( 0.0, Offset.offset( 1e-16 ) );
		assertThat( Vector.magnitude( Vector.of( 3, 4, 0 ) ) ).isCloseTo( 5.0, Offset.offset( 1e-16 ) );
		assertThat( Vector.magnitude( Vector.of( Double.NaN, 0, 0 ) ) ).isNaN();
		assertThat( Vector.magnitude( Vector.of( Double.POSITIVE_INFINITY, 0, 0 ) ) ).isInfinite();
		assertThat( Vector.magnitude( Vector.of( Double.NEGATIVE_INFINITY, 0, 0 ) ) ).isInfinite();
	}

	@Test
	void testReverse() {
		VectorAssert.assertThat( Vector.reverse( Vector.of( 0, 0, 0 ) ) ).isCloseTo( new double[]{ 0, 0, 0 } );
		VectorAssert.assertThat( Vector.reverse( Vector.of( 1, 2, 3 ) ) ).isCloseTo( new double[]{ -1, -2, -3 } );
		VectorAssert.assertThat( Vector.reverse( Vector.of( -1, 2, -3 ) ) ).isCloseTo( new double[]{ 1, -2, 3 } );
		VectorAssert.assertThat( Vector.reverse( Vector.of( Double.NaN, 0, 0 ) ) ).isEqualTo( Vector.UNDEFINED );
	}

	@Test
	void testDot() {
		assertThat( Vector.dot( Vector.of( 0, 0, 0 ), Vector.of( 0, 0, 0 ) ) ).isCloseTo( 0.0, Offset.offset( 1e-16 ) );
		assertThat( Vector.dot( Vector.of( 1, 1, 1 ), Vector.of( 1, 1, 1 ) ) ).isCloseTo( 3.0, Offset.offset( 1e-16 ) );
		assertThat( Vector.dot( Vector.of( Double.NaN, 0, 0 ), Vector.of( 0, 0, 0 ) ) ).isNaN();
		assertThat( Vector.dot( Vector.of( Double.POSITIVE_INFINITY, 0, 0 ), Vector.of( 1, 1, 1 ) ) ).isInfinite();
		assertThat( Vector.dot( Vector.of( Double.NEGATIVE_INFINITY, 0, 0 ), Vector.of( 1, 1, 1 ) ) ).isInfinite();
	}

	@Test
	void testCross() {
		VectorAssert.assertThat( Vector.cross( Vector.of( 1, 0, 0 ), Vector.of( 0, 1, 0 ) ) ).isCloseTo( new double[]{ 0, 0, 1 } );
		VectorAssert.assertThat( Vector.cross( Vector.of( 0.3, 0, 0 ), Vector.of( 0, 0.3, 0 ) ) ).isCloseTo( new double[]{ 0, 0, 0.09 } );
		VectorAssert.assertThat( Vector.cross( Vector.of( Double.NaN, 0, 0 ), Vector.of( 0, 0.3, 0 ) ) ).isEqualTo( Vector.UNDEFINED );
		VectorAssert.assertThat( Vector.cross( Vector.of( Double.POSITIVE_INFINITY, 0, 0 ), Vector.of( 0, 0.3, 0 ) ) ).isEqualTo( Vector.UNDEFINED );
		VectorAssert.assertThat( Vector.cross( Vector.of( Double.NEGATIVE_INFINITY, 0, 0 ), Vector.of( 0, 0.3, 0 ) ) ).isEqualTo( Vector.UNDEFINED );
	}

	@Test
	void testAdd() {
		VectorAssert.assertThat( Vector.add( Vector.of( -1, 2, -3 ), Vector.of( 1, -2, 3 ) ) ).isCloseTo( new double[]{ 0, 0, 0 } );
		VectorAssert.assertThat( Vector.add( Vector.of( Double.NaN, 2, -3 ), Vector.of( 1, -2, 3 ) ) ).isEqualTo( Vector.UNDEFINED );
		VectorAssert.assertThat( Vector.add( Vector.of( Double.POSITIVE_INFINITY, 2, -3 ), Vector.of( 1, -2, 3 ) ) ).isEqualTo( Vector.INFINITY );
		VectorAssert.assertThat( Vector.add( Vector.of( Double.NEGATIVE_INFINITY, 2, -3 ), Vector.of( 1, -2, 3 ) ) ).isEqualTo( Vector.INFINITY );
	}

	@Test
	void testSubtract() {
		VectorAssert.assertThat( Vector.minus( Vector.of( -1, 2, -3 ), Vector.of( 1, -2, 3 ) ) ).isCloseTo( new double[]{ -2, 4, -6 } );
		VectorAssert.assertThat( Vector.subtract( Vector.of( Double.NaN, 2, -3 ), Vector.of( 1, -2, 3 ) ) ).isEqualTo( Vector.UNDEFINED );
		VectorAssert.assertThat( Vector.subtract( Vector.of( Double.POSITIVE_INFINITY, 2, -3 ), Vector.of( 1, -2, 3 ) ) ).isEqualTo( Vector.INFINITY );
		VectorAssert.assertThat( Vector.subtract( Vector.of( Double.NEGATIVE_INFINITY, 2, -3 ), Vector.of( 1, -2, 3 ) ) ).isEqualTo( Vector.INFINITY );
	}

	@Test
	void testScale() {
		VectorAssert.assertThat( Vector.scale( Vector.of( 1, 2, 3 ), 0 ) ).isCloseTo( new double[]{ 0, 0, 0 } );
		VectorAssert.assertThat( Vector.scale( Vector.of( 1, 2, 3 ), -2 ) ).isCloseTo( new double[]{ -2, -4, -6 } );
		VectorAssert.assertThat( Vector.scale( Vector.of( Double.NaN, 0, 0 ), 1 ) ).isEqualTo( Vector.UNDEFINED );
		VectorAssert.assertThat( Vector.scale( Vector.of( 0, 0, 0 ), Double.NaN ) ).isEqualTo( Vector.UNDEFINED );
		VectorAssert.assertThat( Vector.scale( Vector.of( Double.POSITIVE_INFINITY, 1, 1 ), 1 ) ).isEqualTo( Vector.INFINITY );
		VectorAssert.assertThat( Vector.scale( Vector.of( 1, 1, 1 ), Double.POSITIVE_INFINITY ) ).isEqualTo( Vector.INFINITY );
		VectorAssert.assertThat( Vector.scale( Vector.of( Double.NEGATIVE_INFINITY, 1, 1 ), 1 ) ).isEqualTo( Vector.INFINITY );
		VectorAssert.assertThat( Vector.scale( Vector.of( 1, 1, 1 ), Double.NEGATIVE_INFINITY ) ).isEqualTo( Vector.INFINITY );
	}

	@Test
	void testScaleByCoordinate() {
		VectorAssert.assertThat( Vector.scale( Vector.of( 1, 2, 3 ), 0, 0, 0 ) ).isCloseTo( Vector.ZERO );
		VectorAssert.assertThat( Vector.scale( Vector.of( 1, 2, 3 ), 3, 2, 1 ) ).isCloseTo( new double[]{ 3, 4, 3 } );
		VectorAssert.assertThat( Vector.scale( Vector.of( Double.NaN, 0, 0 ), 1, 1, 1 ) ).isEqualTo( Vector.UNDEFINED );
		VectorAssert.assertThat( Vector.scale( Vector.of( 0, 0, 0 ), 1, 1, Double.NaN ) ).isEqualTo( Vector.UNDEFINED );
		VectorAssert.assertThat( Vector.scale( Vector.of( Double.POSITIVE_INFINITY, 1, 1 ), 1, 1, 1 ) ).isEqualTo( Vector.INFINITY );
		VectorAssert.assertThat( Vector.scale( Vector.of( 1, 1, 1 ), 1, Double.POSITIVE_INFINITY, 1 ) ).isEqualTo( Vector.INFINITY );
		VectorAssert.assertThat( Vector.scale( Vector.of( Double.NEGATIVE_INFINITY, 1, 1 ), 1, 1, 1 ) ).isEqualTo( Vector.INFINITY );
		VectorAssert.assertThat( Vector.scale( Vector.of( 1, 1, 1 ), Double.NEGATIVE_INFINITY, 1, 1 ) ).isEqualTo( Vector.INFINITY );
	}

	@Test
	void testNormalize() {
		double oneOverRoot2 = 1 / Math.sqrt( 2 );
		VectorAssert.assertThat( Vector.normalize( Vector.of( 1, 1, 0 ) ) ).isCloseTo( new double[]{ oneOverRoot2, oneOverRoot2, 0 } );
	}

	@Test
	void testLirp() {
		VectorAssert.assertThat( Vector.lerp( Vector.of( 1, 1 ), Vector.of( 3, 2 ), 0.5 ) ).isCloseTo( new double[]{ 2, 1.5, 0 } );
		VectorAssert.assertThat( Vector.lerp( Vector.of( Double.NaN, 0, 0 ), Vector.of( 1, 1, 1 ), 0.5 ) ).isEqualTo( Vector.UNDEFINED );
		VectorAssert.assertThat( Vector.lerp( Vector.of( 0, 0, 0 ), Vector.of( 1, 1, Double.NaN ), 0.5 ) ).isEqualTo( Vector.UNDEFINED );
		VectorAssert.assertThat( Vector.lerp( Vector.of( 0, 0, 0 ), Vector.of( 1, 1, 1 ), Double.NaN ) ).isEqualTo( Vector.UNDEFINED );
		VectorAssert.assertThat( Vector.lerp( Vector.of( Double.POSITIVE_INFINITY, 0, 0 ), Vector.of( 1, 1, 1 ), 0.5 ) ).isEqualTo( Vector.INFINITY );
		VectorAssert.assertThat( Vector.lerp( Vector.of( 0, 0, 0 ), Vector.of( Double.POSITIVE_INFINITY, 1, 1 ), 0.5 ) ).isEqualTo( Vector.INFINITY );
		VectorAssert.assertThat( Vector.lerp( Vector.of( 0, 0, 0 ), Vector.of( 1, 1, 1 ), Double.POSITIVE_INFINITY ) ).isEqualTo( Vector.INFINITY );
	}

	@Test
	void testLte() {
		assertThat( Vector.lte( Vector.of( 0, 0, 0 ), Vector.of( 0, 0, 0 ) ) ).isTrue();
		assertThat( Vector.lte( Vector.of( 0, 0, 0 ), Vector.of( 1, 0, 0 ) ) ).isTrue();
		assertThat( Vector.lte( Vector.of( 0, 0, 0 ), Vector.of( 0, 1, 0 ) ) ).isTrue();
		assertThat( Vector.lte( Vector.of( 0, 0, 0 ), Vector.of( 0, 0, 1 ) ) ).isTrue();
		assertThat( Vector.lte( Vector.of( 1, 1, 1 ), Vector.of( 0, 0, 0 ) ) ).isFalse();
		assertThat( Vector.lte( Vector.of( 1, 1, 1 ), Vector.of( 0, 2, 2 ) ) ).isFalse();
		assertThat( Vector.lte( Vector.of( 1, 1, 1 ), Vector.of( 2, 0, 2 ) ) ).isFalse();
		assertThat( Vector.lte( Vector.of( 1, 1, 1 ), Vector.of( 2, 2, 0 ) ) ).isFalse();
	}

	@Test
	void testGte() {
		assertThat( Vector.gte( Vector.of( 0, 0, 0 ), Vector.of( 0, 0, 0 ) ) ).isTrue();
		assertThat( Vector.gte( Vector.of( 1, 0, 0 ), Vector.of( 0, 0, 0 ) ) ).isTrue();
		assertThat( Vector.gte( Vector.of( 0, 1, 0 ), Vector.of( 0, 0, 0 ) ) ).isTrue();
		assertThat( Vector.gte( Vector.of( 0, 0, 1 ), Vector.of( 0, 0, 0 ) ) ).isTrue();
		assertThat( Vector.gte( Vector.of( 0, 0, 0 ), Vector.of( 1, 1, 1 ) ) ).isFalse();
		assertThat( Vector.gte( Vector.of( 0, 2, 2 ), Vector.of( 1, 1, 1 ) ) ).isFalse();
		assertThat( Vector.gte( Vector.of( 2, 0, 2 ), Vector.of( 1, 1, 1 ) ) ).isFalse();
		assertThat( Vector.gte( Vector.of( 2, 2, 0 ), Vector.of( 1, 1, 1 ) ) ).isFalse();
	}

	@Test
	void testRotate() {
		VectorAssert.assertThat( Vector.rotate( Vector.of( 0, 0, 0 ), 0 ) ).isCloseTo( Vector.of( 0, 0, 0 ) );
		VectorAssert.assertThat( Vector.rotate( Vector.of( 1, 0, 0 ), 0 ) ).isCloseTo( Vector.of( 1, 0, 0 ) );
		VectorAssert.assertThat( Vector.rotate( Vector.of( 1, 0, 0 ), Constants.QUARTER_CIRCLE ) ).isCloseTo( Vector.of( 0, 1, 0 ) );
		VectorAssert.assertThat( Vector.rotate( Vector.of( 1, 0, 0 ), -Constants.QUARTER_CIRCLE ) ).isCloseTo( Vector.of( 0, -1, 0 ) );
		VectorAssert.assertThat( Vector.rotate( Vector.of( 1, 0, 0 ), Constants.HALF_CIRCLE ) ).isCloseTo( Vector.of( -1, 0, 0 ) );
		VectorAssert.assertThat( Vector.rotate( Vector.of( 1, 0, 0 ), -Constants.HALF_CIRCLE ) ).isCloseTo( Vector.of( -1, 0, 0 ) );
	}

	@Test
	void testRotateWithAxis() {
		VectorAssert.assertThat( Vector.rotate( Vector.of( 0, 0, 0 ), Vector.of( 0, 0, 0 ), 0 ) ).isCloseTo( Vector.of( 0, 0, 0 ) );
		VectorAssert.assertThat( Vector.rotate( Vector.of( 0, 0, 0 ), Vector.of( 1, 0, 0 ), 0 ) ).isCloseTo( Vector.of( 1, 0, 0 ) );
		VectorAssert.assertThat( Vector.rotate( Vector.of( 1, 1, 0 ), Vector.of( 2, 1, 0 ), Constants.QUARTER_CIRCLE ) ).isCloseTo( Vector.of( 1, 2, 0 ) );
		VectorAssert.assertThat( Vector.rotate( Vector.of( 1, 1, 0 ), Vector.of( 2, 1, 0 ), -Constants.QUARTER_CIRCLE ) ).isCloseTo( Vector.of( 1, 0, 0 ) );
		VectorAssert.assertThat( Vector.rotate( Vector.of( 1, 1, 0 ), Vector.of( 2, 1, 0 ), Constants.HALF_CIRCLE ) ).isCloseTo( Vector.of( 0, 1, 0 ) );
		VectorAssert.assertThat( Vector.rotate( Vector.of( 1, 1, 0 ), Vector.of( 2, 1, 0 ), -Constants.HALF_CIRCLE ) ).isCloseTo( Vector.of( 0, 1, 0 ) );
	}

	@Test
	void testHash() {
		assertThat( Vector.hash( Vector.of( 0, 0, 0 ) ) ).isEqualTo( Vector.hash( new double[]{ 0, 0, 0 } ) );
		assertThat( Vector.hash( Vector.of( 3, 2, 1 ) ) ).isEqualTo( Vector.hash( new double[]{ 3, 2, 1 } ) );
		assertThat( Vector.hash( Vector.of( 0, 0, Double.POSITIVE_INFINITY ) ) ).isEqualTo( Vector.hash( new double[]{ Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY } ) );
		assertThat( Vector.hash( Vector.of( 0, 0, Double.NEGATIVE_INFINITY ) ) ).isEqualTo( Vector.hash( new double[]{ Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY } ) );
		assertThat( Vector.hash( Vector.of( 0, 0, Double.NaN ) ) ).isEqualTo( Vector.hash( new double[]{ Double.NaN, Double.NaN, Double.NaN } ) );
	}

	@Test
	void testToString() {
		assertThat( Vector.toString( Vector.of( 1.2, 3.4, 5.6 ) ) ).isEqualTo( "Vector[1.2, 3.4, 5.6]" );
		assertThat( Vector.toString( Vector.of( 1.2, Double.POSITIVE_INFINITY, 5.6 ) ) ).isEqualTo( "Vector[Infinity, Infinity, Infinity]" );
		assertThat( Vector.toString( Vector.of( 1.2, Double.NaN, 5.6 ) ) ).isEqualTo( "Vector[NaN, NaN, NaN]" );
	}

}
