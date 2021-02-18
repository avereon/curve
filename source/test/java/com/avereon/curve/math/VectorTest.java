package com.avereon.curve.math;

import org.junit.jupiter.api.Test;

import static com.avereon.curve.match.Matchers.near;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VectorTest {

	@Test
	void testZERO() {
		assertThat( Vector.ZERO, is( new double[]{ 0, 0, 0 } ) );
	}

	@Test
	void testUNIT_X() {
		assertThat( Vector.UNIT_X, is( new double[]{ 1, 0, 0 } ) );
	}

	@Test
	void testUNIT_Y() {
		assertThat( Vector.UNIT_Y, is( new double[]{ 0, 1, 0 } ) );
	}

	@Test
	void testUNIT_Z() {
		assertThat( Vector.UNIT_Z, is( new double[]{ 0, 0, 1 } ) );
	}

	@Test
	void testINFINITY() {
		assertThat( Vector.INFINITY, is( new double[]{ Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY } ) );
	}

	@Test
	void testUNDEFINED() {
		assertThat( Vector.UNDEFINED, is( new double[]{ Double.NaN, Double.NaN, Double.NaN } ) );
	}

	@Test
	void testOfWithTwoNumbers() {
		assertThat( Vector.of( 0, 0 ), is( new double[]{ 0, 0, 0 } ) );
		assertThat( Vector.of( 1.2, 3.4 ), is( new double[]{ 1.2, 3.4, 0 } ) );
	}

	@Test
	void testOfWithThreeNumbers() {
		assertThat( Vector.of( 0, 0, 0 ), is( new double[]{ 0, 0, 0 } ) );
		assertThat( Vector.of( 1.2, 3.4, 5.6 ), is( new double[]{ 1.2, 3.4, 5.6 } ) );
	}

	@Test
	void testDistance() {
		assertThat( Vector.distance( Vector.of( 0, 0 ), Vector.of( 0, 0 ) ), is( 0.0 ) );

		assertThat( Vector.distance( Vector.of( 0, 0 ), Vector.of( 1, 0 ) ), is( 1.0 ) );
		assertThat( Vector.distance( Vector.of( 0, 0 ), Vector.of( -1, 0 ) ), is( 1.0 ) );
		assertThat( Vector.distance( Vector.of( 0, 0 ), Vector.of( 0, 1 ) ), is( 1.0 ) );
		assertThat( Vector.distance( Vector.of( 0, 0 ), Vector.of( 0, -1 ) ), is( 1.0 ) );

		assertThat( Vector.distance( Vector.of( 1, 1 ), Vector.of( 5, 4 ) ), is( 5.0 ) );
		assertThat( Vector.distance( Vector.of( 1, 1 ), Vector.of( 4, 5 ) ), is( 5.0 ) );
		assertThat( Vector.distance( Vector.of( -1, 1 ), Vector.of( -5, 4 ) ), is( 5.0 ) );
		assertThat( Vector.distance( Vector.of( -1, 1 ), Vector.of( -4, 5 ) ), is( 5.0 ) );
		assertThat( Vector.distance( Vector.of( 1, -1 ), Vector.of( 5, -4 ) ), is( 5.0 ) );
		assertThat( Vector.distance( Vector.of( 1, -1 ), Vector.of( 4, -5 ) ), is( 5.0 ) );
		assertThat( Vector.distance( Vector.of( -1, -1 ), Vector.of( -5, -4 ) ), is( 5.0 ) );
		assertThat( Vector.distance( Vector.of( -1, -1 ), Vector.of( -4, -5 ) ), is( 5.0 ) );

		assertThat( Vector.distance( Vector.of( 0, 0 ), Vector.of( Double.NaN, 0 ) ), is( Double.NaN ) );
	}

	@Test
	void testIsUndefined() {
		assertTrue( Vector.isUndefined( Vector.of( Double.NaN, 0, 0 ) ) );
		assertTrue( Vector.isUndefined( Vector.of( 0, Double.NaN, 0 ) ) );
		assertTrue( Vector.isUndefined( Vector.of( 0, 0, Double.NaN ) ) );
	}

	@Test
	void testIsInfinite() {
		assertFalse( Vector.isInfinite( Vector.ZERO ) );

		assertFalse( Vector.isInfinite( Vector.of( Double.NaN, 0, 0 ) ) );
		assertFalse( Vector.isInfinite( Vector.of( 0, Double.NaN, 0 ) ) );
		assertFalse( Vector.isInfinite( Vector.of( 0, 0, Double.NaN ) ) );

		assertTrue( Vector.isInfinite( Vector.of( Double.POSITIVE_INFINITY, 0, 0 ) ) );
		assertTrue( Vector.isInfinite( Vector.of( 0, Double.POSITIVE_INFINITY, 0 ) ) );
		assertTrue( Vector.isInfinite( Vector.of( 0, 0, Double.POSITIVE_INFINITY ) ) );
		assertTrue( Vector.isInfinite( Vector.of( Double.NEGATIVE_INFINITY, 0, 0 ) ) );
		assertTrue( Vector.isInfinite( Vector.of( 0, Double.NEGATIVE_INFINITY, 0 ) ) );
		assertTrue( Vector.isInfinite( Vector.of( 0, 0, Double.NEGATIVE_INFINITY ) ) );
	}

	@Test
	void testMagnitude() {
		assertThat( Vector.magnitude( Vector.of( 0, 0, 0 ) ), is( 0.0 ) );
		assertThat( Vector.magnitude( Vector.of( 3, 4, 0 ) ), is( 5.0 ) );
		assertThat( Vector.magnitude( Vector.of( Double.NaN, 0, 0 ) ), is( Double.NaN ) );
		assertThat( Vector.magnitude( Vector.of( Double.POSITIVE_INFINITY, 0, 0 ) ), is( Double.POSITIVE_INFINITY ) );
		assertThat( Vector.magnitude( Vector.of( Double.NEGATIVE_INFINITY, 0, 0 ) ), is( Double.POSITIVE_INFINITY ) );
	}

	@Test
	void testReverse() {
		assertThat( Vector.reverse( Vector.of( 0, 0, 0 ) ), is( new double[]{ 0, 0, 0 } ) );
		assertThat( Vector.reverse( Vector.of( 1, 2, 3 ) ), is( new double[]{ -1, -2, -3 } ) );
		assertThat( Vector.reverse( Vector.of( -1, 2, -3 ) ), is( new double[]{ 1, -2, 3 } ) );
		assertThat( Vector.reverse( Vector.of( Double.NaN, 0, 0 ) ), is( Vector.UNDEFINED ) );
	}

	@Test
	void testDot() {
		assertThat( Vector.dot( Vector.of( 0, 0, 0 ), Vector.of( 0, 0, 0 ) ), is( 0.0 ) );
		assertThat( Vector.dot( Vector.of( 1, 1, 1 ), Vector.of( 1, 1, 1 ) ), is( 3.0 ) );
		assertThat( Vector.dot( Vector.of( Double.NaN, 0, 0 ), Vector.of( 0, 0, 0 ) ), is( Double.NaN ) );
		assertThat( Vector.dot( Vector.of( Double.POSITIVE_INFINITY, 0, 0 ), Vector.of( 1, 1, 1 ) ), is( Double.POSITIVE_INFINITY ) );
		assertThat( Vector.dot( Vector.of( Double.NEGATIVE_INFINITY, 0, 0 ), Vector.of( 1, 1, 1 ) ), is( Double.POSITIVE_INFINITY ) );
	}

	@Test
	void testCross() {
		assertThat( Vector.cross( Vector.of( 1, 0, 0 ), Vector.of( 0, 1, 0 ) ), is( new double[]{ 0, 0, 1 } ) );
		assertThat( Vector.cross( Vector.of( 0.3, 0, 0 ), Vector.of( 0, 0.3, 0 ) ), is( new double[]{ 0, 0, 0.09 } ) );
		assertThat( Vector.cross( Vector.of( Double.NaN, 0, 0 ), Vector.of( 0, 0.3, 0 ) ), is( Vector.UNDEFINED ) );
		assertThat( Vector.cross( Vector.of( Double.POSITIVE_INFINITY, 0, 0 ), Vector.of( 0, 0.3, 0 ) ), is( Vector.UNDEFINED ) );
		assertThat( Vector.cross( Vector.of( Double.NEGATIVE_INFINITY, 0, 0 ), Vector.of( 0, 0.3, 0 ) ), is( Vector.UNDEFINED ) );
	}

	@Test
	void testAdd() {
		assertThat( Vector.add( Vector.of( -1, 2, -3 ), Vector.of( 1, -2, 3 ) ), is( new double[]{ 0, 0, 0 } ) );
		assertThat( Vector.add( Vector.of( Double.NaN, 2, -3 ), Vector.of( 1, -2, 3 ) ), is( Vector.UNDEFINED ) );
		assertThat( Vector.add( Vector.of( Double.POSITIVE_INFINITY, 2, -3 ), Vector.of( 1, -2, 3 ) ), is( Vector.INFINITY ) );
		assertThat( Vector.add( Vector.of( Double.NEGATIVE_INFINITY, 2, -3 ), Vector.of( 1, -2, 3 ) ), is( Vector.INFINITY ) );
	}

	@Test
	void testSubtract() {
		assertThat( Vector.minus( Vector.of( -1, 2, -3 ), Vector.of( 1, -2, 3 ) ), is( new double[]{ -2, 4, -6 } ) );
		assertThat( Vector.subtract( Vector.of( Double.NaN, 2, -3 ), Vector.of( 1, -2, 3 ) ), is( Vector.UNDEFINED ) );
		assertThat( Vector.subtract( Vector.of( Double.POSITIVE_INFINITY, 2, -3 ), Vector.of( 1, -2, 3 ) ), is( Vector.INFINITY ) );
		assertThat( Vector.subtract( Vector.of( Double.NEGATIVE_INFINITY, 2, -3 ), Vector.of( 1, -2, 3 ) ), is( Vector.INFINITY ) );
	}

	@Test
	void testScale() {
		assertThat( Vector.scale( Vector.of( 1, 2, 3 ), 0 ), is( new double[]{ 0, 0, 0 } ) );
		assertThat( Vector.scale( Vector.of( 1, 2, 3 ), -2 ), is( new double[]{ -2, -4, -6 } ) );
		assertThat( Vector.scale( Vector.of( Double.NaN, 0, 0 ), 1 ), is( Vector.UNDEFINED ) );
		assertThat( Vector.scale( Vector.of( 0, 0, 0 ), Double.NaN ), is( Vector.UNDEFINED ) );
		assertThat( Vector.scale( Vector.of( Double.POSITIVE_INFINITY, 1, 1 ), 1 ), is( Vector.INFINITY ) );
		assertThat( Vector.scale( Vector.of( 1, 1, 1 ), Double.POSITIVE_INFINITY ), is( Vector.INFINITY ) );
		assertThat( Vector.scale( Vector.of( Double.NEGATIVE_INFINITY, 1, 1 ), 1 ), is( Vector.INFINITY ) );
		assertThat( Vector.scale( Vector.of( 1, 1, 1 ), Double.NEGATIVE_INFINITY ), is( Vector.INFINITY ) );
	}

	@Test
	void testScaleByCoordinate() {
		assertThat( Vector.scale( Vector.of( 1, 2, 3 ), 0, 0, 0 ), is( Vector.ZERO ) );
		assertThat( Vector.scale( Vector.of( 1, 2, 3 ), 3, 2, 1 ), is( new double[]{ 3, 4, 3 } ) );
		assertThat( Vector.scale( Vector.of( Double.NaN, 0, 0 ), 1, 1, 1 ), is( Vector.UNDEFINED ) );
		assertThat( Vector.scale( Vector.of( 0, 0, 0 ), 1, 1, Double.NaN ), is( Vector.UNDEFINED ) );
		assertThat( Vector.scale( Vector.of( Double.POSITIVE_INFINITY, 1, 1 ), 1, 1, 1 ), is( Vector.INFINITY ) );
		assertThat( Vector.scale( Vector.of( 1, 1, 1 ), 1, Double.POSITIVE_INFINITY, 1 ), is( Vector.INFINITY ) );
		assertThat( Vector.scale( Vector.of( Double.NEGATIVE_INFINITY, 1, 1 ), 1, 1, 1 ), is( Vector.INFINITY ) );
		assertThat( Vector.scale( Vector.of( 1, 1, 1 ), Double.NEGATIVE_INFINITY, 1, 1 ), is( Vector.INFINITY ) );
	}

	@Test
	void testNormalize() {
		double oneOverRoot2 = 1 / Math.sqrt( 2 );
		assertThat( Vector.normalize( Vector.of( 1, 1, 0 ) ), is( new double[]{ oneOverRoot2, oneOverRoot2, 0 } ) );
	}

	@Test
	void testLirp() {
		assertThat( Vector.lirp( Vector.of( 1, 1 ), Vector.of( 3, 2 ), 0.5 ), is( new double[]{ 2, 1.5, 0 } ) );
		assertThat( Vector.lirp( Vector.of( Double.NaN, 0, 0 ), Vector.of( 1, 1, 1 ), 0.5 ), is( Vector.UNDEFINED ) );
		assertThat( Vector.lirp( Vector.of( 0, 0, 0 ), Vector.of( 1, 1, Double.NaN ), 0.5 ), is( Vector.UNDEFINED ) );
		assertThat( Vector.lirp( Vector.of( 0, 0, 0 ), Vector.of( 1, 1, 1 ), Double.NaN ), is( Vector.UNDEFINED ) );
		assertThat( Vector.lirp( Vector.of( Double.POSITIVE_INFINITY, 0, 0 ), Vector.of( 1, 1, 1 ), 0.5 ), is( Vector.INFINITY ) );
		assertThat( Vector.lirp( Vector.of( 0, 0, 0 ), Vector.of( Double.POSITIVE_INFINITY, 1, 1 ), 0.5 ), is( Vector.INFINITY ) );
		assertThat( Vector.lirp( Vector.of( 0, 0, 0 ), Vector.of( 1, 1, 1 ), Double.POSITIVE_INFINITY ), is( Vector.INFINITY ) );
	}

	@Test
	void testLte() {
		assertTrue( Vector.lte( Vector.of( 0, 0, 0 ), Vector.of( 0, 0, 0 ) ) );
		assertTrue( Vector.lte( Vector.of( 0, 0, 0 ), Vector.of( 1, 0, 0 ) ) );
		assertTrue( Vector.lte( Vector.of( 0, 0, 0 ), Vector.of( 0, 1, 0 ) ) );
		assertTrue( Vector.lte( Vector.of( 0, 0, 0 ), Vector.of( 0, 0, 1 ) ) );
		assertFalse( Vector.lte( Vector.of( 1, 1, 1 ), Vector.of( 0, 0, 0 ) ) );
		assertFalse( Vector.lte( Vector.of( 1, 1, 1 ), Vector.of( 0, 2, 2 ) ) );
		assertFalse( Vector.lte( Vector.of( 1, 1, 1 ), Vector.of( 2, 0, 2 ) ) );
		assertFalse( Vector.lte( Vector.of( 1, 1, 1 ), Vector.of( 2, 2, 0 ) ) );
	}

	@Test
	void testGte() {
		assertTrue( Vector.gte( Vector.of( 0, 0, 0 ), Vector.of( 0, 0, 0 ) ) );
		assertTrue( Vector.gte( Vector.of( 1, 0, 0 ), Vector.of( 0, 0, 0 ) ) );
		assertTrue( Vector.gte( Vector.of( 0, 1, 0 ), Vector.of( 0, 0, 0 ) ) );
		assertTrue( Vector.gte( Vector.of( 0, 0, 1 ), Vector.of( 0, 0, 0 ) ) );
		assertFalse( Vector.gte( Vector.of( 0, 0, 0 ), Vector.of( 1, 1, 1 ) ) );
		assertFalse( Vector.gte( Vector.of( 0, 2, 2 ), Vector.of( 1, 1, 1 ) ) );
		assertFalse( Vector.gte( Vector.of( 2, 0, 2 ), Vector.of( 1, 1, 1 ) ) );
		assertFalse( Vector.gte( Vector.of( 2, 2, 0 ), Vector.of( 1, 1, 1 ) ) );
	}

	@Test
	void testRotate() {
		assertThat( Vector.rotate( Vector.of( 0, 0, 0 ), 0 ), is( Vector.of( 0, 0, 0 ) ) );
		assertThat( Vector.rotate( Vector.of( 1, 0, 0 ), 0 ), is( Vector.of( 1, 0, 0 ) ) );
		assertThat( Vector.rotate( Vector.of( 1, 0, 0 ), Constants.QUARTER_CIRCLE ), near( Vector.of( 0, 1, 0 ) ) );
		assertThat( Vector.rotate( Vector.of( 1, 0, 0 ), -Constants.QUARTER_CIRCLE ), near( Vector.of( 0, -1, 0 ) ) );
		assertThat( Vector.rotate( Vector.of( 1, 0, 0 ), Constants.HALF_CIRCLE ), near( Vector.of( -1, 0, 0 ) ) );
		assertThat( Vector.rotate( Vector.of( 1, 0, 0 ), -Constants.HALF_CIRCLE ), near( Vector.of( -1, 0, 0 ) ) );
	}

	@Test
	void testHash() {
		assertThat( Vector.hash( Vector.of( 0, 0, 0 ) ), is( Vector.hash( new double[]{ 0, 0, 0 } ) ) );
		assertThat( Vector.hash( Vector.of( 3, 2, 1 ) ), is( Vector.hash( new double[]{ 3, 2, 1 } ) ) );
		assertThat( Vector.hash( Vector.of( 0, 0, Double.POSITIVE_INFINITY ) ), is( Vector.hash( new double[]{ Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY } ) ) );
		assertThat( Vector.hash( Vector.of( 0, 0, Double.NEGATIVE_INFINITY ) ), is( Vector.hash( new double[]{ Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY } ) ) );
		assertThat( Vector.hash( Vector.of( 0, 0, Double.NaN ) ), is( Vector.hash( new double[]{ Double.NaN, Double.NaN, Double.NaN } ) ) );
	}

	@Test
	void testToString() {
		assertThat( Vector.toString( Vector.of( 1.2, 3.4, 5.6 ) ), is( "[1.2, 3.4, 5.6]" ) );
		assertThat( Vector.toString( Vector.of( 1.2, Double.POSITIVE_INFINITY, 5.6 ) ), is( "[Infinity, Infinity, Infinity]" ) );
		assertThat( Vector.toString( Vector.of( 1.2, Double.NaN, 5.6 ) ), is( "[NaN, NaN, NaN]" ) );
	}

}
