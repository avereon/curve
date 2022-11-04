package com.avereon.curve.math;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ArithmeticTest {

	@Test
	void testDefaultDigits() {
		assertThat( Arithmetic.DEFAULT_DIGITS ).isEqualTo( 12 );
	}

	@Test
	public void testDefaultPrecision() {
		assertThat( Arithmetic.DEFAULT_PRECISION ).isEqualTo( 0.000000000001 );
	}

	//private void assertThat( Object b, Object a ) {}

	@Test
	public void testSign() {
		assertThat( Arithmetic.sign( -Math.PI ) ).isEqualTo( -1.0 );
		assertThat( Arithmetic.sign( 0 ) ).isEqualTo( 1.0 );
		assertThat( Arithmetic.sign( Math.PI ) ).isEqualTo( 1.0 );
	}

	@Test
	public void testTrim() {
		assertThat( Arithmetic.trim( 0.0000000000001 ) ).isEqualTo( 0.000000000000 );
		assertThat( Arithmetic.trim( 0.0000000000004 ) ).isEqualTo( 0.000000000000 );
		assertThat( Arithmetic.trim( 0.0000000000005 ) ).isEqualTo( 0.000000000001 );
		assertThat( Arithmetic.trim( 0.0000000000010 ) ).isEqualTo( 0.000000000001 );
	}

	@Test
	public void testTrimWithDigits() {
		assertThat( Arithmetic.trim( 2.24412, 2 ) ).isEqualTo( 2.24 );
		assertThat( Arithmetic.trim( 2.24658, 2 ) ).isEqualTo( 2.25 );
		assertThat( Arithmetic.trim( 2.25423, 2 ) ).isEqualTo( 2.25 );
		assertThat( Arithmetic.trim( 2.25658, 2 ) ).isEqualTo( 2.26 );

		assertThat( Arithmetic.trim( 0.0001, 3 ) ).isEqualTo( 0.000 );
		assertThat( Arithmetic.trim( 0.0004, 3 ) ).isEqualTo( 0.000 );
		assertThat( Arithmetic.trim( 0.0005, 3 ) ).isEqualTo( 0.001 );
		assertThat( Arithmetic.trim( 0.0010, 3 ) ).isEqualTo( 0.001 );
	}

	@Test
	public void testRound() {
		assertThat( Arithmetic.round( -1.0 ) ).isEqualTo( -1.0 );
		assertThat( Arithmetic.round( -0.6 ) ).isEqualTo( -1.0 );
		assertThat( Arithmetic.round( -0.5 ) ).isEqualTo( 0.0 );
		assertThat( Arithmetic.round( -0.0 ) ).isEqualTo( 0.0 );
		assertThat( Arithmetic.round( 0.0 ) ).isEqualTo( 0.0 );
		assertThat( Arithmetic.round( 0.4 ) ).isEqualTo( 0.0 );
		assertThat( Arithmetic.round( 0.5 ) ).isEqualTo( 1.0 );
		assertThat( Arithmetic.round( 1.0 ) ).isEqualTo( 1.0 );
	}

	@Test
	public void testNearest() {
		assertThat( Arithmetic.nearest( 0.125, 0.5 ) ).isEqualTo( 0.0 );
		assertThat( Arithmetic.nearest( 0.375, 0.5 ) ).isEqualTo( 0.5 );
	}

	@Test
	public void testNearestAbove() {
		assertThat( Arithmetic.nearestAbove( 0.0, 0.5 ) ).isEqualTo( 0.0 );
		assertThat( Arithmetic.nearestAbove( 0.125, 0.5 ) ).isEqualTo( 0.5 );
		assertThat( Arithmetic.nearestAbove( 0.375, 0.5 ) ).isEqualTo( 0.5 );
		assertThat( Arithmetic.nearestAbove( 0.5, 0.5 ) ).isEqualTo( 0.5 );
	}

	@Test
	public void testNearestBelow() {
		assertThat( Arithmetic.nearestBelow( 0.0, 0.5 ) ).isEqualTo( 0.0 );
		assertThat( Arithmetic.nearestBelow( 0.125, 0.5 ) ).isEqualTo( 0.0 );
		assertThat( Arithmetic.nearestBelow( 0.375, 0.5 ) ).isEqualTo( 0.0 );
		assertThat( Arithmetic.nearestBelow( 0.5, 0.5 ) ).isEqualTo( 0.5 );
	}

	@Test
	public void testDeterminant2() {
		assertThat( Arithmetic.determinant( 1, 2, 3, 4 ) ).isEqualTo( -2.0 );
	}

	@Test
	public void testDeterminant3() {
		assertThat( Arithmetic.determinant( 1, 2, 3, 4, 5, 6, 7, 8, 9 ) ).isEqualTo( 0.0 );
	}

	@Test
	public void testBc() {
		assertThat( Arithmetic.bc( 0, 0 ) ).isEqualTo( 1 );
		assertThat( Arithmetic.bc( 1, 0 ) ).isEqualTo( 1 );
		assertThat( Arithmetic.bc( 1, 1 ) ).isEqualTo( 1 );
		assertThat( Arithmetic.bc( 2, 1 ) ).isEqualTo( 2 );
		assertThat( Arithmetic.bc( 6, 3 ) ).isEqualTo( 20 );
	}

	@Test
	public void testBchi() {
		assertThat( Arithmetic.bchi( 0 ) ).isEqualTo( 1.0 );
		assertThat( Arithmetic.bchi( 1 ) ).isEqualTo( 0.5 );
		assertThat( Arithmetic.bchi( 2 ) ).isEqualTo( -0.125 );
		assertThat( Arithmetic.bchi( 3 ) ).isEqualTo( 0.0625 );
		assertThat( Arithmetic.bchi( 4 ) ).isEqualTo( -0.0390625 );
		assertThat( Arithmetic.bchi( 5 ) ).isEqualTo( 0.02734375 );
		assertThat( Arithmetic.bchi( 6 ) ).isEqualTo( -0.0205078125 );
		assertThat( Arithmetic.bchi( 7 ) ).isEqualTo( 0.01611328125 );
	}

	@Test
	public void testFactorial() {
		assertThat( Arithmetic.factorial( 0 ) ).isEqualTo( 1 );
		assertThat( Arithmetic.factorial( 1 ) ).isEqualTo( 1 );
		assertThat( Arithmetic.factorial( 2 ) ).isEqualTo( 2 );
		assertThat( Arithmetic.factorial( 3 ) ).isEqualTo( 6 );
		assertThat( Arithmetic.factorial( 4 ) ).isEqualTo( 24 );
		assertThat( Arithmetic.factorial( 5 ) ).isEqualTo( 120 );
	}

}
