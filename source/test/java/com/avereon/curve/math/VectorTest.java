package com.avereon.curve.math;

import org.junit.jupiter.api.Test;

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
		assertThat( Vector.reverse( Vector.of( Double.NaN, 0, 0 ) ), is( new double[]{ Double.NaN, Double.NaN, Double.NaN } ) );
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
		assertThat( Vector.cross( Vector.of( Double.NaN, 0, 0 ), Vector.of( 0, 0.3, 0 ) ), is( new double[]{ Double.NaN, Double.NaN, Double.NaN } ) );
		assertThat( Vector.cross( Vector.of( Double.POSITIVE_INFINITY, 0, 0 ), Vector.of( 0, 0.3, 0 ) ), is( new double[]{ Double.NaN, Double.NaN, Double.NaN } ) );
		assertThat( Vector.cross( Vector.of( Double.NEGATIVE_INFINITY, 0, 0 ), Vector.of( 0, 0.3, 0 ) ), is( new double[]{ Double.NaN, Double.NaN, Double.NaN } ) );
	}

	@Test
	void testAdd() {
		assertThat( Vector.add( Vector.of( -1, 2, -3 ), Vector.of( 1, -2, 3 ) ), is( new double[]{ 0, 0, 0 } ) );
		assertThat( Vector.add( Vector.of( Double.NaN, 2, -3 ), Vector.of( 1, -2, 3 ) ), is( new double[]{ Double.NaN, Double.NaN, Double.NaN } ) );
		assertThat( Vector.add( Vector.of( Double.POSITIVE_INFINITY, 2, -3 ), Vector.of( 1, -2, 3 ) ), is( new double[]{ Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY } ) );
		assertThat( Vector.add( Vector.of( Double.NEGATIVE_INFINITY, 2, -3 ), Vector.of( 1, -2, 3 ) ), is( new double[]{ Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY } ) );
	}

	@Test
	void testSubtract() {
		assertThat( Vector.minus( Vector.of( -1, 2, -3 ), Vector.of( 1, -2, 3 ) ), is( new double[]{ -2, 4, -6 } ) );
		assertThat( Vector.subtract( Vector.of( Double.NaN, 2, -3 ), Vector.of( 1, -2, 3 ) ), is( new double[]{ Double.NaN, Double.NaN, Double.NaN } ) );
		assertThat( Vector.subtract( Vector.of( Double.POSITIVE_INFINITY, 2, -3 ), Vector.of( 1, -2, 3 ) ), is( new double[]{ Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY } ) );
		assertThat( Vector.subtract( Vector.of( Double.NEGATIVE_INFINITY, 2, -3 ), Vector.of( 1, -2, 3 ) ), is( new double[]{ Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY } ) );
	}

}
