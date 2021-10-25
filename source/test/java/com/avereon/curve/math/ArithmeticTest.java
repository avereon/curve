package com.avereon.curve.math;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ArithmeticTest {

	@Test
	void testDefaultDigits() {
		assertThat( Arithmetic.DEFAULT_DIGITS, is( 12 ) );
	}

	@Test
	public void testDefaultPrecision() {
		assertThat( Arithmetic.DEFAULT_PRECISION, is( 0.000000000001 ) );
	}

	//private void assertThat( Object b, Object a ) {}

	@Test
	public void testSign() {
		assertThat( Arithmetic.sign( -Math.PI ), is( -1.0 ) );
		assertThat( Arithmetic.sign( 0 ), is( 1.0 ) );
		assertThat( Arithmetic.sign( Math.PI ), is( 1.0 ) );
	}

	@Test
	public void testTrim() {
		assertThat( Arithmetic.trim( 0.0000000000001 ), is( 0.000000000000 ) );
		assertThat( Arithmetic.trim( 0.0000000000004 ), is( 0.000000000000 ) );
		assertThat( Arithmetic.trim( 0.0000000000005 ), is( 0.000000000001 ) );
		assertThat( Arithmetic.trim( 0.0000000000010 ), is( 0.000000000001 ) );
	}

	@Test
	public void testTrimWithDigits() {
		assertThat( Arithmetic.trim( 2.24412, 2 ), is( 2.24 ) );
		assertThat( Arithmetic.trim( 2.24658, 2 ), is( 2.25 ) );
		assertThat( Arithmetic.trim( 2.25423, 2 ), is( 2.25 ) );
		assertThat( Arithmetic.trim( 2.25658, 2 ), is( 2.26 ) );

		assertThat( Arithmetic.trim( 0.0001, 3 ), is( 0.000 ) );
		assertThat( Arithmetic.trim( 0.0004, 3 ), is( 0.000 ) );
		assertThat( Arithmetic.trim( 0.0005, 3 ), is( 0.001 ) );
		assertThat( Arithmetic.trim( 0.0010, 3 ), is( 0.001 ) );
	}

	@Test
	public void testNearest() {
		assertThat( Arithmetic.nearest( 0.125, 0.5 ), is( 0.0 ) );
		assertThat( Arithmetic.nearest( 0.375, 0.5 ), is( 0.5 ) );
	}

	@Test
	public void testNearestAbove() {
		assertThat( Arithmetic.nearestAbove( 0.0, 0.5 ), is( 0.0 ) );
		assertThat( Arithmetic.nearestAbove( 0.125, 0.5 ), is( 0.5 ) );
		assertThat( Arithmetic.nearestAbove( 0.375, 0.5 ), is( 0.5 ) );
		assertThat( Arithmetic.nearestAbove( 0.5, 0.5 ), is( 0.5 ) );
	}

	@Test
	public void testNearestBelow() {
		assertThat( Arithmetic.nearestBelow( 0.0, 0.5 ), is( 0.0 ) );
		assertThat( Arithmetic.nearestBelow( 0.125, 0.5 ), is( 0.0 ) );
		assertThat( Arithmetic.nearestBelow( 0.375, 0.5 ), is( 0.0 ) );
		assertThat( Arithmetic.nearestBelow( 0.5, 0.5 ), is( 0.5 ) );
	}

	@Test
	public void testDeterminant2() {
		assertThat( Arithmetic.determinant( 1, 2, 3, 4 ), is( -2.0 ) );
	}

	@Test
	public void testDeterminant3() {
		assertThat( Arithmetic.determinant( 1, 2, 3, 4, 5, 6, 7, 8, 9 ), is( 0.0 ) );
	}

	@Test
	public void testBc() {
		assertThat( Arithmetic.bc( 0, 0 ), is( 1 ) );
		assertThat( Arithmetic.bc( 1, 0 ), is( 1 ) );
		assertThat( Arithmetic.bc( 1, 1 ), is( 1 ) );
		assertThat( Arithmetic.bc( 2, 1 ), is( 2 ) );
		assertThat( Arithmetic.bc( 6, 3 ), is( 20 ) );
	}

	@Test
	public void testBchi() {
		assertThat( Arithmetic.bchi( 0 ), is( 1.0 ) );
		assertThat( Arithmetic.bchi( 1 ), is( 0.5 ) );
		assertThat( Arithmetic.bchi( 2 ), is( -0.125 ) );
		assertThat( Arithmetic.bchi( 3 ), is( 0.0625 ) );
		assertThat( Arithmetic.bchi( 4 ), is( -0.0390625 ) );
		assertThat( Arithmetic.bchi( 5 ), is( 0.02734375 ) );
		assertThat( Arithmetic.bchi( 6 ), is( -0.0205078125 ) );
		assertThat( Arithmetic.bchi( 7 ), is( 0.01611328125 ) );
	}

	@Test
	public void testFactorial() {
		assertThat( Arithmetic.factorial( 0 ), is( 1 ) );
		assertThat( Arithmetic.factorial( 1 ), is( 1 ) );
		assertThat( Arithmetic.factorial( 2 ), is( 2 ) );
		assertThat( Arithmetic.factorial( 3 ), is( 6 ) );
		assertThat( Arithmetic.factorial( 4 ), is( 24 ) );
		assertThat( Arithmetic.factorial( 5 ), is( 120 ) );
	}

}
